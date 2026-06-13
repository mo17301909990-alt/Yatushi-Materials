package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 规则头实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RgResourceRuleHeader {
    private Integer id;
    private Integer resourceGroupId;
    private String ruleType;  // STRUCT_RULES / TEXT_RULES
    private String ruleName;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
}

