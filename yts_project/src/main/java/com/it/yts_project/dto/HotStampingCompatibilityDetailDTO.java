package com.it.yts_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 烫金工艺兼容性详细信息DTO
 * 包含关联表的信息，方便用户管理规则
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotStampingCompatibilityDetailDTO {
    
    // 兼容性规则基本信息
    private Long id;
    private String paperPerformance;
    private String productType;
    private String patternCharacteristic;
    private String hotStampingType;
    private String compatibility;
    private String patternType;
    private java.math.BigDecimal lineThicknessMin;
    private java.math.BigDecimal lineThicknessMax;
    private java.math.BigDecimal solidAreaMin;
    private java.math.BigDecimal solidAreaMax;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // ID字段
    private Integer productTypeId;
    private Integer patternCharacteristicId;
    private Integer hotStampingTypeId;
    private Integer preProcessingStepId;
    private Integer postProcessingStepId;
    
    // 关联表信息（用于显示和管理）
    private String productTypeName;           // 产品类型名称
    private String patternCharacteristicName; // 图案特征名称
    private String hotStampingTypeName;       // 烫金类型名称
    private String preProcessingStepName;     // 前工序名称
    private String postProcessingStepName;    // 后工序名称
    
    // 关联表详细信息
    private String productTypeDescription;    // 产品类型描述
    private String patternTypeDescription;    // 图案类型描述
    private String hotStampingTypeDescription; // 烫金类型描述
    private String preProcessingStepDescription; // 前工序描述
    private String postProcessingStepDescription; // 后工序描述
    
    // 状态信息
    private Boolean isActive;                 // 是否启用
    private Integer sortOrder;                // 排序
}
