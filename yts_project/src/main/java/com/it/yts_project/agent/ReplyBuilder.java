package com.it.yts_project.agent;

import com.it.yts_project.agent.skill.SkillResult;

import java.util.List;

/**
 * 回复构建器接口。
 * <p>根据意图、上下文和 Skill 执行结果生成自然语言回复和建议。</p>
 */
public interface ReplyBuilder {

    /**
     * 构建回复文本。
     */
    String build(Intent intent, MatchContext context, SkillResult<?> result);

    /**
     * 构建后续建议列表。
     */
    List<String> buildSuggestions(Intent intent, MatchContext context, SkillResult<?> result);
}
