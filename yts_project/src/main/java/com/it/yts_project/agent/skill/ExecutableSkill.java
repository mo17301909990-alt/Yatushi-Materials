package com.it.yts_project.agent.skill;

import java.util.Map;

/**
 * Agent 可执行 Skill 接口。
 * <p>
 * 所有 Agent Skill 必须实现此接口，通过统一的 execute 入口接收参数字典，
 * 返回结构化的 SkillResult，包含执行状态、数据、匹配度和说明信息。
 * </p>
 */
public interface ExecutableSkill {

    /**
     * 执行 Skill 逻辑。
     *
     * @param params 参数字典，由 Agent 编排层传入，各 Skill 自行解析所需字段
     * @return 结构化执行结果，包含 success / data / message / matchDegree / totalCount
     */
    SkillResult<?> execute(Map<String, Object> params);
}
