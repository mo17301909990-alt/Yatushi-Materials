package com.it.yts_project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * 通义千问 DashScope 兼容接口调用（OpenAI 兼容模式）
 * 使用 RestTemplate 调用 HTTP API，无需额外 SDK 依赖。
 */
@Service
public class DashScopeChatService {

    private static final Logger log = LoggerFactory.getLogger(DashScopeChatService.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${dashscope.api-key:}")
    private String apiKey;

    @Value("${dashscope.base-url:https://dashscope.aliyuncs.com/compatible-mode/v1}")
    private String baseUrl;

    @Value("${dashscope.model:qwen-plus}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 发送对话请求，返回助手回复文本
     *
     * @param systemPrompt 系统提示（可为 null）
     * @param userMessage  用户消息
     * @return 助手回复内容；若未配置 apiKey 或调用失败则抛异常或返回错误信息
     */
    public String chat(String systemPrompt, String userMessage) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("未配置 dashscope.api-key，请在 application-local.properties 中配置");
        }
        String url = baseUrl.endsWith("/") ? baseUrl + "chat/completions" : baseUrl + "/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey.trim());

        List<Map<String, String>> messages = new java.util.ArrayList<>();
        if (systemPrompt != null && !systemPrompt.isBlank()) {
            messages.add(Map.of("role", "system", "content", systemPrompt));
        }
        messages.add(Map.of("role", "user", "content", userMessage));

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", messages
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.warn("DashScope 返回非 2xx 或空体: {}", response.getStatusCode());
            throw new RuntimeException("通义千问调用失败: " + response.getStatusCode());
        }

        String responseBody = response.getBody();
        try {
            JsonNode root = OBJECT_MAPPER.readTree(responseBody);
            // 检查业务错误（部分接口 200 但 body 里带 error）
            JsonNode err = root.path("error");
            if (!err.isMissingNode()) {
                String msg = err.path("message").asText(err.toString());
                log.warn("DashScope 返回错误: {}", msg);
                throw new RuntimeException("通义千问: " + msg);
            }
            // 兼容 OpenAI 格式：choices[0].message.content（可能为字符串或数组）
            JsonNode choices = root.path("choices");
            if (!choices.isEmpty()) {
                JsonNode msg = choices.get(0).path("message");
                JsonNode contentNode = msg.path("content");
                String content = null;
                if (contentNode.isTextual()) {
                    content = contentNode.asText(null);
                } else if (contentNode.isArray() && contentNode.size() > 0) {
                    for (int i = 0; i < contentNode.size(); i++) {
                        JsonNode part = contentNode.get(i);
                        if (part.has("text")) {
                            content = part.get("text").asText(null);
                            break;
                        }
                    }
                }
                if (content != null && !content.isBlank()) {
                    return content.trim();
                }
            }
            // 兼容部分接口的 output.text
            JsonNode output = root.path("output");
            if (!output.isMissingNode()) {
                String text = output.path("text").asText(null);
                if (text != null && !text.isBlank()) {
                    return text.trim();
                }
            }
            log.warn("DashScope 返回无内容，原始响应: {}", responseBody != null && responseBody.length() > 500 ? responseBody.substring(0, 500) + "..." : responseBody);
            return "模型未返回内容，请稍后重试或检查 API 配额。";
        } catch (Exception e) {
            log.error("解析 DashScope 响应失败，原始: {}", responseBody, e);
            throw new RuntimeException("解析通义千问响应失败: " + e.getMessage(), e);
        }
    }
}
