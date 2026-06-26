package com.it.yts_project.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 硅胶产品匹配结果 DTO
 */
@Data
public class SiliconeProductDTO {
    private Integer id;
    private String materialCode;
    private String supplierCode;
    private String stockCode;
    private String materialName;
    private String usage;
    private String category;
    private String color;
    private String thickness;
    private String shape;
    private String tester;
    private String responsiblePerson;
    private String minSheetSize;
    private String maxSheetSize;
    private String minSpacing;
    private String maxSpacing;
    private String designInfo;
    private String applicableInterface;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 硅胶类型标识（如 white_uv, glow_ink） */
    private String siliconeType;

    /** 硅胶类型中文标签 */
    private String siliconeTypeLabel;

    /** 兼容性状态（V/X/null），仅单步骤匹配查询时返回 */
    private String compatibilityStatus;

    /** 多步骤匹配时，每步骤的兼容性状态映射（step -> V/X/null） */
    private Map<String, String> compatibilityStatusMap;

    /** 该产品的所有兼容性配置，仅详情查询时返回 */
    private List<?> compatibilities;
}
