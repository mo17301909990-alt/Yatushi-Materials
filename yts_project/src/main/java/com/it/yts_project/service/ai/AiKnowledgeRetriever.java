package com.it.yts_project.service.ai;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 混合 RAG：向量相似度（优先）+ 关键词评分（降级）双路检索。
 * 当 KnowledgeVectorizer 有向量索引时启用 0.6×cosine + 0.4×keyword 混合打分；
 * 否则纯关键词 token 重叠打分。
 */
@Component
public class AiKnowledgeRetriever {

    private static final Logger log = LoggerFactory.getLogger(AiKnowledgeRetriever.class);
    private static final Pattern NON_WORD = Pattern.compile("[\\s\\p{Punct}]+");

    /** 同义词映射：用户可能用不同的词描述同一概念 */
    private static final java.util.Map<String, java.util.List<String>> SYNONYMS = java.util.Map.ofEntries(
        java.util.Map.entry("哑胶", java.util.List.of("哑膜", "哑面OPP", "哑胶面")),
        java.util.Map.entry("光胶", java.util.List.of("光膜", "光面OPP", "光胶面")),
        java.util.Map.entry("过胶", java.util.List.of("过膜", "覆膜")),
        java.util.Map.entry("耐磨", java.util.List.of("耐磨纸", "耐磨金纸")),
        java.util.Map.entry("普通金纸", java.util.List.of("普通金紙", "普通金箔紙")),
        java.util.Map.entry("界面", java.util.List.of("前工序", "适用界面")),
        java.util.Map.entry("烫金", java.util.List.of("烫印", "燙金")),
        java.util.Map.entry("布面纸", java.util.List.of("布料纸", "布面")),
        java.util.Map.entry("丝印", java.util.List.of("丝网印刷", "丝印打底")),
        java.util.Map.entry("高耐磨", java.util.List.of("TB815", "高耐磨纸")),
        java.util.Map.entry("镭射", java.util.List.of("激光", "鐳射")),
        java.util.Map.entry("胶带测试", java.util.List.of("3M810", "3M#810", "胶带测试")),
        java.util.Map.entry("电晕", java.util.List.of("达因", "电晕笔")),
        java.util.Map.entry("UV", java.util.List.of("紫外光", "紫外线")),
        java.util.Map.entry("过哑胶", java.util.List.of("过哑膜", "哑胶面")),
        java.util.Map.entry("过光胶", java.util.List.of("过光膜", "光胶面"))
    );

    private final List<String> chunks = new ArrayList<>();
    private final List<String> sourceRefs = new ArrayList<>();

    @Autowired(required = false)
    private KnowledgeVectorizer knowledgeVectorizer;
    @Autowired(required = false)
    private EmbeddingService embeddingService;

    private volatile boolean vectorEnabled = false;

    @PostConstruct
    public void loadKnowledge() {
        chunks.clear();
        sourceRefs.clear();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath:ai-knowledge/**/*.md");
            for (Resource res : resources) {
                if (!res.exists() || !res.isReadable()) {
                    continue;
                }
                String text = new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                if (text.isBlank()) {
                    continue;
                }
                for (String para : text.split("\n{2,}")) {
                    String p = para.trim();
                    if (p.length() < 20) {
                        continue;
                    }
                    String ref = extractSourceRef(p);
                    chunks.add(p);
                    sourceRefs.add(ref);
                }
                log.info("已加载知识库文件: {}", res.getFilename());
            }
        } catch (IOException e) {
            log.warn("加载 ai-knowledge 失败（可忽略，将仅依赖工具数据）: {}", e.getMessage());
        }
        if (chunks.isEmpty()) {
            log.info("未找到 ai-knowledge/**/*.md，RAG 片段为空");
        }

        // 检查向量索引是否可用
        if (knowledgeVectorizer != null) {
            KnowledgeVectorizer.VectorStore store = knowledgeVectorizer.getStore();
            if (store != null && store.entries() != null && !store.entries().isEmpty()) {
                vectorEnabled = true;
                log.info("向量索引可用，启用混合检索 ({} 条向量)", store.entries().size());
            } else {
                log.info("向量索引不可用，降级为关键词检索");
            }
        }
    }

    /**
     * 检索与 query 最相关的若干段落文本（可能为空字符串）。
     * 向量优先 → 混合排序 → 关键字降级。
     */
    public String retrieveTop(String query, int topK, int maxTotalChars) {
        if (query == null || query.isBlank() || chunks.isEmpty()) {
            return "";
        }
        String q = query.trim();

        List<ScoredEntry> results;
        if (vectorEnabled) {
            results = hybridSearch(q, topK);
        } else {
            results = keywordSearch(q, topK);
        }

        if (results.isEmpty()) {
            return "";
        }

        StringBuilder out = new StringBuilder();
        int n = 0;
        for (ScoredEntry se : results) {
            if (n >= topK) break;
            if (out.length() + se.text.length() + 50 > maxTotalChars) break;
            if (out.length() > 0) {
                out.append("\n\n---\n\n");
            }
            out.append(se.text);
            if (se.sourceRef != null && !se.sourceRef.isEmpty()) {
                out.append("\n[来源: ").append(se.sourceRef).append("]");
            }
            n++;
        }
        return out.toString().trim();
    }

    /**
     * 混合搜索：向量余弦 + 关键词双重打分
     */
    private List<ScoredEntry> hybridSearch(String query, int topK) {
        KnowledgeVectorizer.VectorStore store = knowledgeVectorizer.getStore();
        if (store == null || store.entries() == null || store.entries().isEmpty()) {
            return keywordSearch(query, topK);
        }

        List<KnowledgeVectorizer.VectorEntry> entries = store.entries();

        // 对 query 做 embedding
        float[] queryVec = null;
        if (embeddingService != null) {
            queryVec = embeddingService.embed(query);
        }

        if (queryVec == null || queryVec.length == 0) {
            log.debug("Embedding 失败，降级为纯关键词检索 (query={})", query);
            return keywordSearch(query, topK);
        }

        // 计算每条向量的余弦相似度 + 关键词分
        List<HybridScore> scored = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            KnowledgeVectorizer.VectorEntry ve = entries.get(i);
            double cosineSim = cosineSimilarity(queryVec, ve.embedding());
            int kwScore = keywordScore(query, ve.chunk());
            scored.add(new HybridScore(ve.chunk(), ve.sourceRef(), cosineSim, kwScore));
        }

        // 归一化 + 加权
        double minCos = scored.stream().mapToDouble(s -> s.cosine).min().orElse(0);
        double maxCos = scored.stream().mapToDouble(s -> s.cosine).max().orElse(1);
        double rangeCos = maxCos - minCos;
        int minKw = scored.stream().mapToInt(s -> s.keyword).min().orElse(0);
        int maxKw = scored.stream().mapToInt(s -> s.keyword).max().orElse(1);
        double rangeKw = maxKw - minKw;

        scored.sort((a, b) -> {
            double aNormCos = rangeCos > 0 ? (a.cosine - minCos) / rangeCos : 0.5;
            double bNormCos = rangeCos > 0 ? (b.cosine - minCos) / rangeCos : 0.5;
            double aNormKw = rangeKw > 0 ? (double)(a.keyword - minKw) / rangeKw : 0.5;
            double bNormKw = rangeKw > 0 ? (double)(b.keyword - minKw) / rangeKw : 0.5;
            double aScore = 0.6 * aNormCos + 0.4 * aNormKw;
            double bScore = 0.6 * bNormCos + 0.4 * bNormKw;
            return Double.compare(bScore, aScore);
        });

        List<ScoredEntry> results = new ArrayList<>();
        for (HybridScore hs : scored) {
            results.add(new ScoredEntry(hs.text, hs.sourceRef));
        }
        return results;
    }

    /**
     * 纯关键词检索（降级路径）
     */
    private List<ScoredEntry> keywordSearch(String query, int topK) {
        List<Scored> scored = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i++) {
            int s = keywordScore(query, chunks.get(i));
            if (s > 0) {
                scored.add(new Scored(chunks.get(i), sourceRefs.get(i), s));
            }
        }
        scored.sort((a, b) -> Integer.compare(b.score, a.score));

        List<ScoredEntry> results = new ArrayList<>();
        int n = 0;
        for (Scored s : scored) {
            if (n >= topK * 2) break;
            results.add(new ScoredEntry(s.text, s.sourceRef));
            n++;
        }
        return results;
    }

    private static double cosineSimilarity(float[] a, float[] b) {
        if (a == null || b == null || a.length != b.length || a.length == 0) return 0;
        double dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += (double) a[i] * b[i];
            normA += (double) a[i] * a[i];
            normB += (double) b[i] * b[i];
        }
        double denom = Math.sqrt(normA) * Math.sqrt(normB);
        return denom > 0 ? dot / denom : 0;
    }

    private static int keywordScore(String query, String chunk) {
        String cq = chunk.toLowerCase(Locale.ROOT);
        int score = 0;
        for (String token : tokenize(query)) {
            if (token.length() < 2) continue;
            String t = token.toLowerCase(Locale.ROOT);
            if (cq.contains(t)) {
                score += Math.min(20, t.length() * 2);
            }
            java.util.List<String> syns = SYNONYMS.get(token);
            if (syns != null) {
                for (String syn : syns) {
                    if (cq.contains(syn.toLowerCase(Locale.ROOT))) {
                        score += 2;
                        break;
                    }
                }
            }
        }
        String compactQ = query.replaceAll("\\s+", "");
        for (int i = 0; i < compactQ.length() - 1; i++) {
            String bi = compactQ.substring(i, i + 2);
            if (chunk.contains(bi)) {
                score += 3;
            }
        }
        String trimmedQuery = query.trim();
        if (trimmedQuery.length() > 4 && chunk.contains(trimmedQuery)) {
            score += 10;
        }
        return score;
    }

    private static String extractSourceRef(String text) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("<!--\\s*来源:\\s*([^-]+(?:-[^-]+)*)\\s*-->").matcher(text);
        return m.find() ? m.group(1).trim() : "";
    }

    private static List<String> tokenize(String query) {
        List<String> list = new ArrayList<>();
        for (String part : NON_WORD.split(query)) {
            if (!part.isEmpty()) {
                list.add(part);
            }
        }
        return list;
    }

    private record Scored(String text, String sourceRef, int score) {}
    private record ScoredEntry(String text, String sourceRef) {}
    private record HybridScore(String text, String sourceRef, double cosine, int keyword) {}
}
