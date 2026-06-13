package com.it.yts_project.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * DashScope text-embedding-v2 向量嵌入服务。
 * 使用与 DashScopeChatService 相同的 API key，调用 OpenAI 兼容的 embeddings 端点。
 */
@Service
public class EmbeddingService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingService.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final int DIMENSION = 1536;
    private static final int BATCH_MAX = 25;
    private static final int CACHE_MAX = 500;

    @Value("${dashscope.api-key:}")
    private String apiKey;

    @Value("${dashscope.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final LRUCache<String, float[]> cache = new LRUCache<>(CACHE_MAX);
    private final ReentrantLock cacheLock = new ReentrantLock();

    /**
     * 对单段文本生成向量嵌入。
     *
     * @return 1536 维 float 数组；失败或未配置时返回空数组
     */
    public float[] embed(String text) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("未配置 dashscope.api-key，embedding 不可用");
            return new float[0];
        }
        if (text == null || text.isBlank()) {
            return new float[0];
        }

        // 缓存命中
        cacheLock.lock();
        try {
            float[] cached = cache.get(text);
            if (cached != null) {
                return cached;
            }
        } finally {
            cacheLock.unlock();
        }

        List<float[]> results = doEmbed(List.of(text));
        if (results.isEmpty()) {
            return new float[0];
        }

        float[] embedding = results.get(0);
        cacheLock.lock();
        try {
            cache.put(text, embedding);
        } finally {
            cacheLock.unlock();
        }
        return embedding;
    }

    /**
     * 批量生成向量嵌入。
     *
     * @return 与输入对应顺序的向量列表；失败时返回空列表
     */
    public List<float[]> embedBatch(List<String> texts) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("未配置 dashscope.api-key，embedding 不可用");
            return List.of();
        }
        if (texts == null || texts.isEmpty()) {
            return List.of();
        }

        List<float[]> all = new ArrayList<>();
        for (int i = 0; i < texts.size(); i += BATCH_MAX) {
            int end = Math.min(i + BATCH_MAX, texts.size());
            List<String> batch = texts.subList(i, end);
            List<float[]> results = doEmbed(batch);
            all.addAll(results);
        }
        return all;
    }

    @SuppressWarnings("unchecked")
    private List<float[]> doEmbed(List<String> texts) {
        String url = baseUrl.endsWith("/") ? baseUrl + "embeddings" : baseUrl + "/embeddings";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey.trim());

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", "text-embedding-v2");
        // OpenAI 兼容格式：单个字符串或字符串数组
        if (texts.size() == 1) {
            body.put("input", texts.get(0));
        } else {
            body.put("input", texts);
        }

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                log.warn("Embedding API 返回非 2xx: {}", response.getStatusCode());
                return List.of();
            }
            JsonNode root = OBJECT_MAPPER.readTree(response.getBody());
            JsonNode data = root.path("data");
            if (data.isMissingNode() || !data.isArray()) {
                log.warn("Embedding API 响应中无 data 数组: {}", response.getBody());
                return List.of();
            }
            // data 数组按 index 排序
            List<float[]> results = new ArrayList<>();
            for (JsonNode item : data) {
                JsonNode vec = item.path("embedding");
                if (vec.isArray() && vec.size() == DIMENSION) {
                    float[] arr = new float[DIMENSION];
                    for (int i = 0; i < DIMENSION; i++) {
                        arr[i] = (float) vec.get(i).asDouble(0);
                    }
                    results.add(arr);
                }
            }
            return results;
        } catch (Exception e) {
            log.warn("Embedding API 调用失败: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * 简单的 LRU 缓存实现（基于 LinkedHashMap）
     */
    private static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int maxSize;

        LRUCache(int maxSize) {
            super(16, 0.75f, true);
            this.maxSize = maxSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > maxSize;
        }
    }
}
