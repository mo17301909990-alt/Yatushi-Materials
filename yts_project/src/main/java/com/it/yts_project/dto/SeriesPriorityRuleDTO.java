package com.it.yts_project.dto;

import lombok.Data;
import java.util.List;

/**
 * 系列优先级规则 DTO（含档位列表，供后台配置）
 */
@Data
public class SeriesPriorityRuleDTO {
    private Integer id;
    private String ruleCode;
    private String ruleName;
    private Integer sortOrder;
    private Boolean isActive;
    private List<SeriesPriorityRuleItemDTO> items;
}
