package com.it.yts_project.dto;

import lombok.Data;

/**
 * 系列优先级规则档位明细 DTO（后台可配置：映射到 SK、L817 等 + 优先级）
 */
@Data
public class SeriesPriorityRuleItemDTO {
    private Integer id;
    private Integer ruleId;
    private Integer priorityOrder;
    private String seriesNames;   // 逗号分隔，如 SK 或 L817,L817/GB
    private Boolean isPriceFallback;
    private String remark;
}
