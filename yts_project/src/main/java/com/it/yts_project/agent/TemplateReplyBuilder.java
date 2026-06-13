package com.it.yts_project.agent;

import com.it.yts_project.agent.skill.SkillResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板回复构建器。
 * <p>使用中文模板生成回复文本和后续建议。</p>
 */
@Component
public class TemplateReplyBuilder implements ReplyBuilder {

    private static final Logger log = LoggerFactory.getLogger(TemplateReplyBuilder.class);

    @Override
    public String build(Intent intent, MatchContext context, SkillResult<?> result) {
        return switch (intent) {
            case MATCH -> buildMatchReply(context, result);
            case QUERY -> buildQueryReply(context, result);
            case COMPARE -> buildCompareReply(context, result);
            case COMPATIBILITY -> buildCompatibilityReply(context, result);
            case ADMIN_MODIFY -> buildAdminModifyReply(result);
            case FEEDBACK -> buildFeedbackReply(result);
            case GREETING -> buildGreetingReply();
            case HELP -> buildHelpReply();
        };
    }

    @Override
    public List<String> buildSuggestions(Intent intent, MatchContext context, SkillResult<?> result) {
        List<String> suggestions = new ArrayList<>();

        if (result != null && (!result.isSuccess() || result.getData() == null || result.getData().isEmpty())) {
            suggestions.add("换个条件试试");
            suggestions.add("查看全部产品");
            suggestions.add("使用帮助");
            return suggestions;
        }

        return switch (intent) {
            case MATCH -> List.of("对比一下这些产品", "检查工艺兼容性", "查一下供应商信息");
            case QUERY -> List.of("看看这些产品", "对比一下供应商", "还有什么其他选择");
            case COMPARE -> List.of("看看匹配度最高的", "检查兼容性", "查一下供应商");
            case COMPATIBILITY -> List.of("找兼容的产品", "看看匹配的产品", "对比可选方案");
            case ADMIN_MODIFY -> List.of("继续修改", "查看变更记录", "返回查询");
            case FEEDBACK -> List.of("继续匹配产品", "重新搜索", "查看帮助");
            case GREETING -> List.of("我想找产品", "检查兼容性", "查看帮助");
            case HELP -> List.of("我想找烫金产品", "检查工艺兼容性", "查供应商信息");
        };
    }

    // ========== 各意图模板 ==========

    private String buildMatchReply(MatchContext context, SkillResult<?> result) {
        if (result == null || !result.isSuccess()) {
            return "抱歉，搜索时出了点问题：" + (result != null ? result.getMessage() : "未知错误");
        }
        if (result.getData() == null || result.getData().isEmpty()) {
            if (isEmptyContext(context)) {
                return "请告诉我您想找什么样的产品？例如「找红色烫金纸」或「匹配GP-1001」。";
            }
            return "暂时没找到匹配的产品，您可以换个条件试试";
        }
        int count = result.getData().size();
        return String.format("已为您找到 %d 个匹配产品，按匹配度排序如下：%s",
                count, result.getMessage() != null ? "\n" + result.getMessage() : "");
    }

    private String buildQueryReply(MatchContext context, SkillResult<?> result) {
        if (result == null || !result.isSuccess()) {
            return "查询时出了点问题：" + (result != null ? result.getMessage() : "未知错误");
        }
        if (result.getData() == null || result.getData().isEmpty()) {
            return "没有查到相关信息，请确认查询条件是否正确。";
        }
        return String.format("查询到以下信息：%s",
                result.getMessage() != null ? "\n" + result.getMessage() : "");
    }

    private String buildCompareReply(MatchContext context, SkillResult<?> result) {
        if (result == null || !result.isSuccess()) {
            return "对比功能暂时不可用：" + (result != null ? result.getMessage() : "未知错误");
        }
        if (result.getData() == null || result.getData().size() < 2) {
            return "至少需要两个产品才能对比。请先搜索找到更多产品。";
        }
        return String.format("找到 %d 个产品可供对比。您可以查看详细参数来选择最合适的。",
                result.getData().size());
    }

    private String buildCompatibilityReply(MatchContext context, SkillResult<?> result) {
        if (result == null || !result.isSuccess()) {
            return "兼容性检查出了点问题：" + (result != null ? result.getMessage() : "未知错误");
        }
        if (result.getData() == null || result.getData().isEmpty()) {
            return "未找到兼容性记录，建议补充工艺数据或联系技术部门。";
        }
        int matchDegree = result.getMatchDegree() != null ? result.getMatchDegree() : 0;
        return String.format("兼容性检查完成，兼容度 %d%%%s",
                matchDegree, result.getMessage() != null ? "。\n" + result.getMessage() : "");
    }

    private String buildAdminModifyReply(SkillResult<?> result) {
        if (result == null || !result.isSuccess()) {
            return "操作执行失败：" + (result != null ? result.getMessage() : "未知错误");
        }
        return "操作已执行。" + (result.getMessage() != null ? "\n" + result.getMessage() : "");
    }

    private String buildFeedbackReply(SkillResult<?> result) {
        return "收到您的反馈，感谢您帮助我们做得更好！";
    }

    private String buildGreetingReply() {
        return "您好！我是匹配 Agent，可以帮您搜索烫金纸产品。试试说「找金色烫金纸」或「耐磨的型号」";
    }

    private String buildHelpReply() {
        return "您可以这样使用我：\n"
                + "1. 找产品：例如「找红色烫金纸」「匹配 GP-1001」\n"
                + "2. 查兼容性：例如「检查工艺兼容性」「这个能用吗」\n"
                + "3. 查供应商：例如「查一下供应商信息」「这个产品的供应商」\n"
                + "4. 对比：例如「对比这两个产品」「A 和 B 哪个好」\n"
                + "5. 反馈：例如「反馈一下」「这个结果满意」";
    }

    // ========== 辅助方法 ==========

    private static boolean isEmptyContext(MatchContext context) {
        return context == null || context.toMap().isEmpty();
    }
}
