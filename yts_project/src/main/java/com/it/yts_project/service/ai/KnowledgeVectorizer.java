package com.it.yts_project.service.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 知识库向量化：加载 ai-knowledge/*.md → 切块 → embedding → 缓存。
 * 启动时自动检测 MD5，变更时自动重建索引。
 */
@Component
public class KnowledgeVectorizer {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeVectorizer.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Pattern SOURCE_REF = Pattern.compile("<!--\\s*来源:\\s*([^-]+(?:-[^-]+)*)\\s*-->");

    private final EmbeddingService embeddingService;
    private volatile VectorStore store = new VectorStore(List.of(), "");

    @Autowired
    public KnowledgeVectorizer(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @PostConstruct
    public void init() {
        try {
            rebuildIfChanged();
        } catch (Exception e) {
            log.warn("KnowledgeVectorizer 初始化失败，将降级为无向量检索: {}", e.getMessage());
        }
    }

    public VectorStore getStore() {
        return store;
    }

    /**
     * 强制重建索引
     */
    public synchronized void reindex() {
        try {
            doIndex();
        } catch (Exception e) {
            log.error("重建向量索引失败: {}", e.getMessage());
        }
    }

    private synchronized void rebuildIfChanged() {
        try {
            String currentMd5 = computeMd5OfKnowledgeFiles();
            String cachedMd5 = loadCachedMd5();
            if (currentMd5.equals(cachedMd5) && cachedMd5.isEmpty()) {
                // 无缓存，直接建索引
                doIndex();
            } else if (currentMd5.equals(cachedMd5)) {
                // 缓存匹配，反序列化
                File cacheFile = getCacheFile();
                if (cacheFile.exists()) {
                    try {
                        store = OBJECT_MAPPER.readValue(cacheFile, new TypeReference<VectorStore>() {});
                        log.info("知识库向量索引从缓存加载，MD5: {}", currentMd5);
                        return;
                    } catch (IOException e) {
                        log.warn("缓存反序列化失败，重建索引: {}", e.getMessage());
                    }
                }
                doIndex();
            } else {
                log.info("知识库文件变更，重建向量索引 ({} → {})", cachedMd5, currentMd5);
                doIndex();
            }
        } catch (Exception e) {
            log.warn("向量索引重建失败，降级为无向量检索: {}", e.getMessage());
        }
    }

    private void doIndex() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:ai-knowledge/**/*.md");

        List<RawParagraph> rawParagraphs = new ArrayList<>();
        StringBuilder allContent = new StringBuilder();

        for (Resource res : resources) {
            if (!res.exists() || !res.isReadable()) continue;
            String filename = res.getFilename();
            if (filename == null) continue;
            String text = new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            if (text.isBlank()) continue;

            allContent.append(text);

            String[] paragraphs = text.split("\n{2,}");
            int paraIdx = 0;
            for (String para : paragraphs) {
                String trimmed = para.trim();
                if (trimmed.length() < 20) continue;
                String sourceRef = extractSourceRef(trimmed);
                rawParagraphs.add(new RawParagraph(trimmed, filename, paraIdx, sourceRef));
                paraIdx++;
            }
        }

        if (rawParagraphs.isEmpty()) {
            log.warn("未找到知识库段落，向量索引为空");
            store = new VectorStore(List.of(), "");
            return;
        }

        List<String> texts = rawParagraphs.stream().map(p -> p.text).collect(Collectors.toList());
        List<float[]> embeddings = embeddingService.embedBatch(texts);

        if (embeddings.isEmpty()) {
            log.info("Embedding 服务不可用，向量索引为空（将降级为关键字检索）");
            store = new VectorStore(List.of(), "");
            return;
        }

        if (embeddings.size() != rawParagraphs.size()) {
            log.warn("Embedding 返回数量 ({}) 不匹配段落数 ({}), 跳过向量索引", embeddings.size(), rawParagraphs.size());
            store = new VectorStore(List.of(), "");
            return;
        }

        List<VectorEntry> entries = new ArrayList<>();
        for (int i = 0; i < rawParagraphs.size(); i++) {
            RawParagraph rp = rawParagraphs.get(i);
            entries.add(new VectorEntry(rp.text, rp.sourceFile, rp.paragraphIndex, rp.sourceRef, embeddings.get(i)));
        }

        String md5 = computeMd5(allContent.toString());
        store = new VectorStore(entries, md5);

        // 保存缓存
        try {
            File cacheFile = getCacheFile();
            File parent = cacheFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            OBJECT_MAPPER.writeValue(cacheFile, store);
            log.info("知识库向量索引已保存 ({} 条, MD5: {})", entries.size(), md5);
        } catch (IOException e) {
            log.warn("保存向量缓存失败: {}", e.getMessage());
        }
    }

    private static String extractSourceRef(String text) {
        Matcher m = SOURCE_REF.matcher(text);
        return m.find() ? m.group(1).trim() : "";
    }

    private static String computeMd5OfKnowledgeFiles() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath:ai-knowledge/**/*.md");
            StringBuilder all = new StringBuilder();
            for (Resource res : resources) {
                if (!res.exists() || !res.isReadable()) continue;
                String text = new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                all.append(text);
            }
            return computeMd5(all.toString());
        } catch (IOException e) {
            return "";
        }
    }

    private static String computeMd5(String content) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private String loadCachedMd5() {
        File cacheFile = getCacheFile();
        if (!cacheFile.exists()) return "";
        try {
            JsonNode node = OBJECT_MAPPER.readTree(cacheFile);
            JsonNode md5 = node.get("md5Hash");
            return md5 != null ? md5.asText("") : "";
        } catch (IOException e) {
            return "";
        }
    }

    private File getCacheFile() {
        // 保存到用户目录，避免写入 classpath 问题
        String path = System.getProperty("user.home") + "/.yts_rag_cache/ai-knowledge-vectors.json";
        return new File(path);
    }

    // --- 内部数据结构 ---

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VectorEntry(
            String chunk,
            String sourceFile,
            int paragraphIndex,
            String sourceRef,
            float[] embedding
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record VectorStore(
            List<VectorEntry> entries,
            String md5Hash
    ) {}

    private record RawParagraph(
            String text,
            String sourceFile,
            int paragraphIndex,
            String sourceRef
    ) {}
}
