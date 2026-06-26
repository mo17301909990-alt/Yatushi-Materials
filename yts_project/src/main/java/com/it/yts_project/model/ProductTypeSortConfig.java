package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 产品类型特殊映射配置实体
 * 用于动态配置哪些产品类型需要应用特殊映射规则
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTypeSortConfig {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 产品类型ID（关联product_type_options表）
     */
    private Integer productTypeId;

    /**
     * 是否启用耐磨金纸特殊映射
     * true=如果公司有耐磨金纸，直接将查询结果限制为配置的耐磨金纸类型；false=按现时规则
     */
    private Boolean enableWearResistantPriority;

    /**
     * 耐磨金纸类型列表，逗号分隔，如：普通耐磨,高耐磨
     * 当公司有耐磨金纸时，查询结果将限制为这些类型
     */
    private String wearResistantPaperTypes;

    /**
     * 配置描述
     */
    private String description;

    /**
     * 是否激活
     */
    private Boolean isActive;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 获取耐磨金纸类型列表（解析逗号分隔的字符串）
     * @return 纸型名称列表
     */
    public List<String> getWearResistantPaperTypeList() {
        if (wearResistantPaperTypes == null || wearResistantPaperTypes.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(wearResistantPaperTypes.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toList());
    }
}
