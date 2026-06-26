package com.it.yts_project.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.yts_project.dto.*;
import com.it.yts_project.model.NoticeDictionary;
import com.it.yts_project.service.DashScopeChatService;
import com.it.yts_project.service.GoldFoilProductService;
import com.it.yts_project.service.ai.AiChatOrchestratorService;
import com.it.yts_project.service.MatchingNoticeService;
import com.it.yts_project.agent.Intent;
import com.it.yts_project.agent.IntentDetector;
import com.it.yts_project.agent.KeywordIntentDetector;
import com.it.yts_project.service.CompatibilityQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = { "/api/ai", "/ai" })
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class AiController {

    private static final Logger log = LoggerFactory.getLogger(AiController.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Pattern MARKDOWN_JSON_BLOCK = Pattern.compile("(?s)```(?:json)?\\s*([\\s\\S]*?)```");

    private static final String PARSE_SYSTEM_PROMPT =
        "你是烫金纸匹配系统的解析助手。用户用自然语言描述匹配条件，你只输出一个 JSON 对象，不要任何其他文字或说明。\n"
        + "JSON 字段（均为可选，无法识别时省略或填 null）：\n"
        + "companyNumber: 公司编号字符串\n"
        + "paizi: 牌子/系列字符串\n"
        + "productTypeId: 产品类型ID（数字）\n"
        + "preProcessingStepsOptionId: 前工序选项ID（数字）\n"
        + "clothPaperTypeId: 布料纸类型ID（数字）\n"
        + "paperPerformance: 烫金纸性能，只能取其一：普通金紙、普通耐磨、高耐磨、鐳射燙金紙\n"
        + "hotStampingTypeOptionId: 烫金类型选项ID（数字）\n"
        + "patternId: 图案类型ID（数字）\n"
        + "postProcessingStepId: 后加工步骤ID（数字）\n"
        + "laminationMaterialId: 过胶材质ID（数字）\n"
        + "priceLevel: 价格优先度（数字，越低越便宜）\n"
        + "colorNum: 颜色编码字符串\n"
        + "注意：如果用户消息开头包含【当前已累积的筛选参数】段落，那是对已选条件的说明，请在输出 JSON 时保留这些已有参数，只根据新消息新增或修改。\n"
        + "只输出上述字段的 JSON，不要 markdown 标记。";

    @Autowired
    private DashScopeChatService dashScopeChatService;
    @Autowired
    private AiChatOrchestratorService aiChatOrchestratorService;
    @Autowired
    private GoldFoilProductService goldFoilProductService;
    @Autowired
    private MatchingNoticeService matchingNoticeService;
    @Autowired
    private KeywordIntentDetector keywordIntentDetector;
    @Autowired
    private CompatibilityQueryService compatibilityQueryService;

    /**
     * 简单对话（烫金匹配问答）
     */
    @PostMapping("/chat")
    public ResponseEntity<AiChatResponse> chat(@RequestBody AiChatRequest request) {
        if (request == null || request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().body(new AiChatResponse("请输入问题。"));
        }
        try {
            // 混合：以系统工具数据为主 + 知识库 RAG 为辅，再调用大模型组织语言
            String reply = aiChatOrchestratorService.chat(request.getMessage().trim());
            return ResponseEntity.ok(new AiChatResponse(reply));
        } catch (Exception e) {
            log.error("AI 对话失败", e);
            return ResponseEntity.status(500).body(new AiChatResponse("调用失败: " + e.getMessage()));
        }
    }

    /**
     * Copilot 智能对话（带建议和扩展参数）
     */
    @PostMapping("/copilot/chat")
    public ResponseEntity<CopilotResponse> copilotChat(@RequestBody AiChatRequest request) {
        if (request == null || request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().body(
                CopilotResponse.builder()
                    .reply("请输入问题。")
                    .suggestions(List.of("继续查询", "查看帮助", "咨询客服"))
                    .params(new HashMap<>())
                    .build()
            );
        }
        try {
            String reply = aiChatOrchestratorService.chat(request.getMessage().trim(), request.getSystemPrompt());
            // 从回复中提取 ===PARAMS=== JSON 块，填充 params 并从 reply 中剥离
            Map<String, Object> params = new HashMap<>();
            String cleanedReply = extractParamsBlock(reply, params);
            // 从回复中提取 ===CARD_START=== 卡片块，填充 cardType / cardData 并从 reply 中剥离
            Map<String, Object> cardBlock = new HashMap<>();
            cleanedReply = extractCardBlock(cleanedReply, cardBlock);
            String cardType = (String) cardBlock.getOrDefault("type", "text");
            @SuppressWarnings("unchecked")
            Map<String, Object> cardData = (Map<String, Object>) cardBlock.getOrDefault("data", new HashMap<String, Object>());
            List<String> suggestions = generateSuggestions(cleanedReply);
            return ResponseEntity.ok(
                CopilotResponse.builder()
                    .reply(cleanedReply)
                    .suggestions(suggestions)
                    .params(params)
                    .type(cardType)
                    .data(cardData)
                    .build()
            );
        } catch (Exception e) {
            log.error("Copilot 对话失败", e);
            return ResponseEntity.status(500).body(
                CopilotResponse.builder()
                    .reply("调用失败: " + e.getMessage())
                    .suggestions(List.of("换个条件", "查看帮助", "咨询客服"))
                    .params(new HashMap<>())
                    .build()
            );
        }
    }

    /**
     * 从回复文本中提取 ===PARAMS===...===PARAMS=== 块，解析为 params map，并从原文中剥离
     */
    private String extractParamsBlock(String reply, Map<String, Object> paramsOut) {
        if (reply == null || reply.isBlank()) return reply;
        var matcher = Pattern.compile("===PARAMS===\\s*(\\{[\\s\\S]*?\\})\\s*===PARAMS===").matcher(reply);
        if (matcher.find()) {
            try {
                JsonNode node = OBJECT_MAPPER.readTree(matcher.group(1));
                node.fieldNames().forEachRemaining(k -> paramsOut.put(k, node.get(k)));
            } catch (Exception e) {
                log.warn("解析 ===PARAMS=== JSON 失败: {}", matcher.group(1), e);
            }
            return reply.substring(0, matcher.start()).trim();
        }
        return reply;
    }

    /**
     * 从回复文本中提取 ===CARD_START===...===CARD_END=== 块，解析为 {type, data} 并填入 cardBlockOut，从原文中剥离
     */
    private String extractCardBlock(String reply, Map<String, Object> cardBlockOut) {
        if (reply == null || reply.isBlank()) return reply;
        var matcher = Pattern.compile("(?s)===CARD_START===\\s*(\\{[\\s\\S]*?\\})\\s*===CARD_END===").matcher(reply);
        if (matcher.find()) {
            try {
                JsonNode node = OBJECT_MAPPER.readTree(matcher.group(1));
                if (node.has("type") && !node.get("type").isNull()) {
                    cardBlockOut.put("type", node.get("type").asText());
                }
                if (node.has("data") && !node.get("data").isNull()) {
                    cardBlockOut.put("data", OBJECT_MAPPER.convertValue(node.get("data"), Map.class));
                }
            } catch (Exception e) {
                log.warn("解析 ===CARD_START=== JSON 失败: {}", matcher.group(1), e);
            }
            // 拼接 card block 前和后的文本
            String before = reply.substring(0, matcher.start()).trim();
            String after = reply.substring(matcher.end()).trim();
            if (before.isEmpty()) return after;
            if (after.isEmpty()) return before;
            return before + "\n" + after;
        }
        return reply;
    }

    private static List<String> generateSuggestions(String reply) {
        if (reply == null || reply.isBlank()) {
            return List.of("继续查询", "查看全部产品", "工艺建议");
        }
        if (reply.contains("匹配") || reply.contains("推荐")) {
            return List.of("查看详情", "对比方案", "重新搜索");
        }
        if (reply.contains("兼容")) {
            return List.of("查兼容产品", "看匹配方案", "工艺建议");
        }
        if (reply.contains("不适用") || reply.contains("无法")) {
            return List.of("换个条件", "查看帮助", "咨询客服");
        }
        return List.of("继续查询", "查看全部产品", "工艺建议");
    }

    /**
     * 自然语言解析为查询参数并执行匹配，返回与 POST /api/gold-foil/match 一致的结构
     */
    @PostMapping("/parse-and-match")
    public ResponseEntity<AiParseMatchResponse> parseAndMatch(@RequestBody AiParseMatchRequest request) {
        if (request == null || request.getMessage() == null || request.getMessage().isBlank()) {
            PagedResult<GoldFoilProductDTO> empty = new PagedResult<>(List.of(), 0L, 15, 1);
            return ResponseEntity.badRequest().body(new AiParseMatchResponse(empty, Collections.emptyList(), Collections.emptyMap()));
        }
        try {
            String raw = dashScopeChatService.chat(PARSE_SYSTEM_PROMPT, request.getMessage().trim());
            String jsonStr = stripMarkdownJson(raw);
            if (jsonStr.isBlank()) {
                log.warn("模型未返回有效 JSON: {}", raw);
                return ResponseEntity.ok(emptyResponse());
            }
            JsonNode node = OBJECT_MAPPER.readTree(jsonStr);
            GoldFoilQueryParams params = new GoldFoilQueryParams();
            params.setPageSize(15);
            params.setOffset(0);

            Map<String, Object> extractedParams = new HashMap<>();

            if (node.has("companyNumber") && !node.get("companyNumber").isNull()) {
                params.setCompanyNumber(node.get("companyNumber").asText(null));
                extractedParams.put("companyNumber", node.get("companyNumber").asText());
            }
            if (node.has("paizi") && !node.get("paizi").isNull()) {
                params.setPaizi(node.get("paizi").asText(null));
                extractedParams.put("paizi", node.get("paizi").asText());
            }
            if (node.has("productTypeId") && !node.get("productTypeId").isNull()) {
                params.setProductTypeId(node.get("productTypeId").asInt());
                extractedParams.put("productTypeId", node.get("productTypeId").asInt());
            }
            if (node.has("preProcessingStepsOptionId") && !node.get("preProcessingStepsOptionId").isNull()) {
                params.setPreProcessingStepsOptionId(node.get("preProcessingStepsOptionId").asInt());
                extractedParams.put("preProcessingStepsOptionId", node.get("preProcessingStepsOptionId").asInt());
            }
            if (node.has("clothPaperTypeId") && !node.get("clothPaperTypeId").isNull()) {
                params.setClothPaperTypeId(node.get("clothPaperTypeId").asInt());
                extractedParams.put("clothPaperTypeId", node.get("clothPaperTypeId").asInt());
                // 同时补上兼容状态默认值 V
                params.setClothPaperCompatibilityStatus("V");
            }
            if (node.has("paperPerformance") && !node.get("paperPerformance").isNull()) {
                params.setPaperPerformance(node.get("paperPerformance").asText(null));
                extractedParams.put("paperPerformance", node.get("paperPerformance").asText());
            }
            if (node.has("hotStampingTypeOptionId") && !node.get("hotStampingTypeOptionId").isNull()) {
                params.setHotStampingTypeOptionId(node.get("hotStampingTypeOptionId").asInt());
                extractedParams.put("hotStampingTypeOptionId", node.get("hotStampingTypeOptionId").asInt());
            }
            if (node.has("patternId") && !node.get("patternId").isNull()) {
                params.setPatternId(node.get("patternId").asInt());
                extractedParams.put("patternId", node.get("patternId").asInt());
            }
            if (node.has("postProcessingStepId") && !node.get("postProcessingStepId").isNull()) {
                params.setPostProcessingStepId(node.get("postProcessingStepId").asInt());
                extractedParams.put("postProcessingStepId", node.get("postProcessingStepId").asInt());
            }
            if (node.has("laminationMaterialId") && !node.get("laminationMaterialId").isNull()) {
                params.setLaminationMaterialId(node.get("laminationMaterialId").asInt());
                extractedParams.put("laminationMaterialId", node.get("laminationMaterialId").asInt());
            }
            if (node.has("priceLevel") && !node.get("priceLevel").isNull()) {
                params.setPriceLevel(node.get("priceLevel").asInt());
                extractedParams.put("priceLevel", node.get("priceLevel").asInt());
            }
            if (node.has("colorNum") && !node.get("colorNum").isNull()) {
                params.setColorNum(node.get("colorNum").asText(null));
                extractedParams.put("colorNum", node.get("colorNum").asText());
            }

            log.debug("AI 解析参数: {}", params);

            List<GoldFoilProductDTO> products = goldFoilProductService.getProducts(params);
            Long total = goldFoilProductService.countProducts(params);
            PagedResult<GoldFoilProductDTO> pagedResult = new PagedResult<>(products, total, 15, 1);
            List<NoticeDictionary> notices = Collections.emptyList();
            try {
                notices = matchingNoticeService.collectMatchingNotices(params);
            } catch (Exception e) {
                log.warn("收集注意事项失败: {}", e.getMessage());
            }
            return ResponseEntity.ok(new AiParseMatchResponse(pagedResult, notices, extractedParams));
        } catch (Exception e) {
            log.error("解析或匹配失败", e);
            return ResponseEntity.status(500).body(emptyResponse());
        }
    }

    /**
     * 关键词兼容性查询（Phase 1 MVP）
     * 接收自然语言关键词，返回匹配的物料兼容性信息
     */
    @PostMapping("/compatibility/query")
    public ResponseEntity<CopilotResponse> queryCompatibility(@RequestBody Map<String, String> request) {
        String keywords = request.getOrDefault("message", "");
        if (keywords.isBlank()) {
            return ResponseEntity.badRequest().body(
                CopilotResponse.builder().reply("请输入查询关键词").build()
            );
        }

        // 1. 检测意图
        IntentDetector.DetectionResult result = keywordIntentDetector.detect(keywords);
        Intent intent = result.intent();

        // 2. 如果不是兼容性/匹配意图，走通用回复
        if (intent != Intent.COMPATIBILITY && intent != Intent.MATCH) {
            return ResponseEntity.ok(
                CopilotResponse.builder()
                    .reply("请输入物料名称或兼容性关键词进行查询，例如 \"UV哑油\"、\"烫金纸\"")
                    .suggestions(List.of("查兼容产品", "看匹配方案", "工艺建议"))
                    .type("text")
                    .build()
            );
        }

        // 3. 调用 CompatibilityQueryService 搜索
        CompatibilityQueryResult queryResult = compatibilityQueryService.searchCompatibility(keywords);
        List<CompatibilityProductDTO> products = queryResult.getProducts();

        // 4. 如果无匹配，返回提示
        if (products.isEmpty()) {
            return ResponseEntity.ok(
                CopilotResponse.builder()
                    .reply("未找到与 \"" + keywords + "\" 相关的物料，请尝试其他关键词")
                    .suggestions(List.of("重新搜索", "查看全部物料", "咨询客服"))
                    .type("text")
                    .build()
            );
        }

        // 5. 取第一个物料名称查询生产统计
        String firstMaterialName = products.get(0).getMaterialName();
        CompatibilityQueryResult.ProductionStats stats = compatibilityQueryService.getProductionStats(firstMaterialName);

        // 6. 构造返回 data
        Map<String, Object> data = new HashMap<>();
        data.put("products", products);
        if (stats != null) {
            Map<String, Object> statsMap = new HashMap<>();
            statsMap.put("v", stats.getVerifiedCount());
            statsMap.put("x", stats.getNotCompatibleCount());
            data.put("productionStats", statsMap);
        }

        return ResponseEntity.ok(
            CopilotResponse.builder()
                .reply("找到 " + products.size() + " 个相关物料")
                .type("compatibility")
                .data(data)
                .suggestions(List.of("查看详情", "重新搜索", "工艺建议"))
                .build()
        );
    }

    private static String stripMarkdownJson(String raw) {
        if (raw == null) return "";
        String s = raw.trim();
        var m = MARKDOWN_JSON_BLOCK.matcher(s);
        if (m.find()) {
            return m.group(1).trim();
        }
        return s;
    }

    private AiParseMatchResponse emptyResponse() {
        PagedResult<GoldFoilProductDTO> empty = new PagedResult<>(List.of(), 0L, 15, 1);
        return new AiParseMatchResponse(empty, Collections.emptyList(), Collections.emptyMap());
    }
}
