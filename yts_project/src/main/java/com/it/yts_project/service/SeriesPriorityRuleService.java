package com.it.yts_project.service;

import com.it.yts_project.dto.SeriesPriorityRuleDTO;
import com.it.yts_project.dto.SeriesPriorityRuleItemDTO;
import java.util.List;

/**
 * 系列优先级规则：供 Match 排序使用 + 后台手动配置（可配置任意系列名与优先级）
 */
public interface SeriesPriorityRuleService {

    /** 按规则ID获取档位列表（用于排序，已按 priority_order 升序） */
    List<SeriesPriorityRuleItemDTO> getRuleItemsByRuleId(Integer ruleId);

    /** 后台：列表所有规则 */
    List<SeriesPriorityRuleDTO> listRules();

    /** 后台：获取规则详情（含档位列表） */
    SeriesPriorityRuleDTO getRuleWithItems(Integer ruleId);

    /** 后台：新建规则（可选默认档位：1 档、2 档、其余按价格） */
    SeriesPriorityRuleDTO createRule(String ruleName, Integer sortOrder, Boolean isActive);

    /** 后台：更新规则名称/排序/状态 */
    void updateRule(Integer id, String ruleName, Integer sortOrder, Boolean isActive);

    /** 后台：删除规则（会解除所有引用该规则的适用界面绑定） */
    void deleteRule(Integer id);

    /** 后台：保存规则档位（全量覆盖该规则的 items，系列名可任意配置） */
    void saveRuleItems(Integer ruleId, List<SeriesPriorityRuleItemDTO> items);
}
