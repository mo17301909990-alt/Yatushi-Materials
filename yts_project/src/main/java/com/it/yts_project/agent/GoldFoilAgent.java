package com.it.yts_project.agent;

import com.it.yts_project.agent.skill.ExecutableSkill;
import com.it.yts_project.agent.skill.SkillResult;
import com.it.yts_project.dto.AgentResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 烫金小 Agent 核心编排器。
 * <p>完整执行 detect → route → execute → build 流程：</p>
 * <ol>
 *   <li><b>detect</b> — 通过 {@link IntentDetector} 识别用户意图并提取参数</li>
 *   <li><b>route</b> — 通过 {@link SimpleSkillRouter} 获取匹配的 {@link ExecutableSkill}</li>
 *   <li><b>execute</b> — 执行 Skill 获取 {@link SkillResult}</li>
 *   <li><b>build</b> — 通过 {@link ReplyBuilder} 生成自然语言回复</li>
 * </ol>
 *
 * <p>本 Agent 为只读查询 Agent，不处理管理员修改意图。
 * ADMIN_MODIFY 意图由 AdminModifySkill 单独处理（仅超级管理员入口）。</p>
 */
@Component
public class GoldFoilAgent {

    private static final Logger log = LoggerFactory.getLogger(GoldFoilAgent.class);

    private final IntentDetector intentDetector;
    private final SimpleSkillRouter skillRouter;
    private final ReplyBuilder replyBuilder;

    @Autowired
    public GoldFoilAgent(IntentDetector intentDetector,
                         SimpleSkillRouter skillRouter,
                         ReplyBuilder replyBuilder) {
        this.intentDetector = intentDetector;
        this.skillRouter = skillRouter;
        this.replyBuilder = replyBuilder;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 处理用户查询，返回 Agent 回复。
     */
    public AgentReply process(String query) {
        log.info("[GoldFoilAgent] 处理查询: \"{}\"", query);

        // 1. detect — 意图检测 + 参数提取
        IntentDetector.DetectionResult detection = intentDetector.detect(query);
        Intent intent = detection.intent();
        MatchContext context = detection.context();
        Map<String, Object> params = context.toMap();

        log.debug("[GoldFoilAgent] 意图={}, 参数={}", intent, params);

        // 只读 Agent 拒绝 ADMIN_MODIFY
        if (intent == Intent.ADMIN_MODIFY) {
            log.warn("[GoldFoilAgent] 只读 Agent 收到 ADMIN_MODIFY 意图，拒绝处理");
            return new AgentReply(
                "此功能需要超级管理员权限，请通过管理后台操作。",
                List.of("搜索产品", "检查兼容性", "查看帮助"),
                intent, false, params, List.of()
            );
        }

        // 2. route — 路由到 Skill
        ExecutableSkill skill = skillRouter.route(intent);

        // 3. execute — 执行 Skill
        SkillResult<?> result;
        if (skill == null) {
            log.warn("[GoldFoilAgent] 意图 {} 没有对应的 Skill", intent);
            result = SkillResult.failure("暂时无法处理 " + intent + " 类型的请求");
        } else {
            result = skill.execute(params);
        }

        // 4. build — 构建回复
        String reply = replyBuilder.build(intent, context, result);
        List<String> suggestions = replyBuilder.buildSuggestions(intent, context, result);

        List<Map<String, Object>> results = convertDataToMaps(result.getData());

        log.info("[GoldFoilAgent] 回复完成: {}\n  -> {}", intent, reply);
        return new AgentReply(reply, suggestions, intent, result.isSuccess(), params, results);
    }

    /**
     * 兼容旧调用的入口方法（返回 DTO 对象）。
     */
    public AgentResponse agentRun(String query) {
        AgentReply reply = process(query);
        AgentResponse resp = new AgentResponse();
        resp.setIntent(reply.intent().name());
        resp.setParams(reply.params());
        resp.setResults(reply.results());
        resp.setSuggestions(reply.suggestions());
        resp.setReply(reply.reply());
        return resp;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> convertDataToMaps(List<?> data) {
        if (data == null || data.isEmpty()) return List.of();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object item : data) {
            if (item == null) continue;
            if (item instanceof Map) {
                result.add((Map<String, Object>) item);
            } else {
                try {
                    Map<String, Object> map = objectMapper.convertValue(item, new TypeReference<Map<String, Object>>() {});
                    result.add(map);
                } catch (Exception e) {
                    log.warn("[GoldFoilAgent] 转换数据对象为 Map 失败: {}", e.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * Agent 回复结果。
     *
     * @param reply       回复文本
     * @param suggestions 后续操作建议列表
     * @param intent      识别到的意图
     * @param success     Skill 是否执行成功
     * @param params      提取的结构化参数
     * @param results     匹配结果列表（Map 形式）
     */
    public record AgentReply(
            String reply,
            List<String> suggestions,
            Intent intent,
            boolean success,
            Map<String, Object> params,
            List<Map<String, Object>> results
    ) {}
}
