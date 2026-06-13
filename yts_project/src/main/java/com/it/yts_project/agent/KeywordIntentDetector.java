package com.it.yts_project.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 关键词意图检测器。
 * <p>通过关键词匹配识别用户意图并提取结构化参数。</p>
 *
 * <h3>意图识别关键词：</h3>
 * <ul>
 *   <li>MATCH — 找、查产品、推荐、匹配</li>
 *   <li>QUERY — 查信息、查询、供应商、公司</li>
 *   <li>COMPARE — 对比、比较、vs</li>
 *   <li>COMPATIBILITY — 兼容、能用、是否适用、工艺</li>
 *   <li>ADMIN_MODIFY — 改、修改、更新、调价（仅超级管理员入口调用）</li>
 *   <li>FEEDBACK — 反馈、评价、意见、不好、满意</li>
 *   <li>GREETING — 你好、嗨、早上好</li>
 *   <li>HELP — 帮助、怎么用、功能</li>
 * </ul>
 */
@Component
public class KeywordIntentDetector implements IntentDetector {

    private static final Logger log = LoggerFactory.getLogger(KeywordIntentDetector.class);

    // ========== 关键词模式 ==========

    private static final Pattern PATTERN_GREETING = Pattern.compile(
            "^(你好|嗨|早上好|下午好|晚上好|hello|hi|hey)\\s*");
    private static final Pattern PATTERN_HELP = Pattern.compile(
            "帮助|怎么用|功能|说明|指南|教程|用法|指令|命令|菜单|功能列表");
    private static final Pattern PATTERN_MATCH = Pattern.compile(
            "找|查产品|推荐|匹配|搜索|查找|寻找|有.*吗|哪个|什么.*合适|什么.*好|耐磨|型号|有什么|哪些|找.*纸|查.*纸");
    private static final Pattern PATTERN_QUERY = Pattern.compile(
            "(查|查询|看|看看|显示|列出).*(信息|资料|数据|供应商|公司|客户)");
    private static final Pattern PATTERN_COMPARE = Pattern.compile(
            "对比|比较|vs|区别|不同|哪个好|优劣");
    private static final Pattern PATTERN_COMPATIBILITY = Pattern.compile(
            "兼容|能用|是否适用|工艺|能不能用|适合|可不可以|行不行|可行");
    private static final Pattern PATTERN_FEEDBACK = Pattern.compile(
            "反馈|评价|意见|不好|满意|建议|投诉|点赞|差评|好评");

    // ========== 参数提取模式 ==========

    private static final Pattern PARAM_PAIZI = Pattern.compile(
            "牌子[是为:：]?\\s*(\\S+)");
    private static final Pattern PARAM_COLOR_NUM = Pattern.compile(
            "(颜色|色号|颜色编号)[是为:：]?\\s*(\\S+)");
    private static final Pattern PARAM_PRODUCT_TYPE = Pattern.compile(
            "(产品类型|类型|产品种类)[是为:：]?\\s*(\\S+)");
    private static final Pattern PARAM_COMPANY = Pattern.compile(
            "(公司|客户)(编号|号)[是为:：]?\\s*(\\S+)");
    private static final Pattern PARAM_GP_NO = Pattern.compile(
            "(GP|gp|Gp)[号#]?[是为:：]?\\s*(\\S+)");
    private static final Pattern PARAM_PRICE = Pattern.compile(
            "(价格|价位|价格等级)[是为:：]?\\s*(\\d+)");

    private static final Map<String, String> PAPER_TYPE_MAP = Map.of(
            "高耐磨", "高耐磨",
            "耐磨", "高耐磨",
            "普通耐磨", "普通耐磨",
            "通用型", "通用型",
            "紧实型", "紧实型",
            "环保型", "环保型",
            "镭射", "镭射",
            "激光", "镭射",
            "色箔", "色箔");

    @Override
    public DetectionResult detect(String query) {
        if (query == null || query.isBlank()) {
            log.debug("[KeywordIntentDetector] 空查询，返回 HELP");
            return new DetectionResult(Intent.HELP, new MatchContext());
        }

        String trimmed = query.trim();

        // 1. 意图识别（优先级从高到低）
        Intent intent = resolveIntent(trimmed);
        log.debug("[KeywordIntentDetector] 查询=\"{}\" -> {}", trimmed, intent);

        // 2. 参数提取
        Map<String, Object> params = extractParams(trimmed);
        MatchContext context = MatchContext.from(params);

        log.debug("[KeywordIntentDetector] 提取参数: {}", params);
        return new DetectionResult(intent, context);
    }

    // ========== 私有方法 ==========

    private Intent resolveIntent(String query) {
        if (PATTERN_GREETING.matcher(query).find()) return Intent.GREETING;
        if (PATTERN_HELP.matcher(query).find()) return Intent.HELP;
        if (PATTERN_FEEDBACK.matcher(query).find()) return Intent.FEEDBACK;
        if (PATTERN_COMPATIBILITY.matcher(query).find()) return Intent.COMPATIBILITY;
        if (PATTERN_COMPARE.matcher(query).find()) return Intent.COMPARE;
        if (PATTERN_QUERY.matcher(query).find()) return Intent.QUERY;
        if (PATTERN_MATCH.matcher(query).find()) return Intent.MATCH;
        return Intent.MATCH;
    }

    private Map<String, Object> extractParams(String query) {
        Map<String, Object> params = new HashMap<>();

        applyIfMatch(query, PARAM_PAIZI, m -> params.put("paizi", m.group(1)));
        applyIfMatch(query, PARAM_COLOR_NUM, m -> params.put("colorNum", m.group(2)));
        applyIfMatch(query, PARAM_PRODUCT_TYPE, m -> params.put("productType", m.group(2)));
        applyIfMatch(query, PARAM_COMPANY, m -> params.put("companyNumber", m.group(3)));
        applyIfMatch(query, PARAM_GP_NO, m -> params.put("gpNo", m.group(2)));
        applyIfMatch(query, PARAM_PRICE, m -> {
            try {
                params.put("priceLevel", Integer.parseInt(m.group(2)));
            } catch (NumberFormatException ignored) {
            }
        });

        if (query.contains("纸类型") || query.contains("纸有什么类型")
                || query.contains("纸有哪些类型") || query.contains("有什么纸")
                || query.matches(".*什么纸.*") || query.equals("纸")) {
            params.put("paperTypeQuery", true);
        }

        for (Map.Entry<String, String> e : PAPER_TYPE_MAP.entrySet()) {
            if (query.contains(e.getKey())) {
                params.put("hotStampingPaperType", e.getValue());
                break;
            }
        }

        return params;
    }

    @FunctionalInterface
    private interface MatchConsumer {
        void accept(java.util.regex.Matcher m);
    }

    private static void applyIfMatch(String query, Pattern pattern, MatchConsumer consumer) {
        java.util.regex.Matcher m = pattern.matcher(query);
        if (m.find()) {
            consumer.accept(m);
        }
    }
}
