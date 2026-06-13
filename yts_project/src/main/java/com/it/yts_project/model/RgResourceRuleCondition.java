package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 规则条件实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RgResourceRuleCondition {
    private Integer id;
    private Integer ruleHeaderId;
    private String conditionGroup;
    private String paramCode;      // sheet_count/thickness/gsm/width/height
    private String paramName;      // 印张石数/厚度/克重/宽度/高度
    private String operator;       // >=/<=/BETWEEN
    private BigDecimal valueMin;   // 最小值
    private BigDecimal valueMax;   // 最大值
    private String valueText;      // 文本规则原文
    private String ruleType;       // STRUCT_RULE / TEXT_RULE
    private Boolean isBlocking;    // 是否阻塞规则（暂不上机原因）
    private Boolean isRequired;    // 是否为硬性规则：TRUE=必须满足，FALSE=可选规则
    private Integer sortOrder;
    private LocalDateTime createdAt;
}

