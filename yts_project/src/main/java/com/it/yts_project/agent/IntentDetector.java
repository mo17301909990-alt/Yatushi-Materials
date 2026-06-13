package com.it.yts_project.agent;

/**
 * 意图检测器接口。
 * <p>实现类通过 NLP 或关键词匹配识别用户自然语言查询中的意图，并提取结构化参数。</p>
 */
public interface IntentDetector {

    /**
     * 检测用户查询的意图并提取参数。
     *
     * @param query 用户自然语言查询
     * @return 检测结果，包含意图和匹配上下文参数
     */
    DetectionResult detect(String query);

    /**
     * 意图检测结果。
     *
     * @param intent  识别的意图
     * @param context 提取的结构化参数
     */
    record DetectionResult(Intent intent, MatchContext context) {}
}
