package com.it.yts_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 兼容性查询参数DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompatibilityQueryParams {
    
    /**
     * 烫金纸性能类型
     */
    private String paperPerformance;
    
    /**
     * 产品类型
     */
    private String productType;
    
    /**
     * 产品类型ID（表 hot_stamping_compatibility.product_type_id）
     */
    private Integer productTypeId;
    
    /**
     * 图案特征描述
     */
    private String patternCharacteristic;
    
    /**
     * 图案特征ID（表 hot_stamping_compatibility.pattern_characteristic_id）
     */
    private Integer patternCharacteristicId;
    
    /**
     * 线条粗细 (mm)
     */
    private Double lineThickness;
    
    /**
     * 实地尺寸 (mm)
     */
    private Double solidAreaSize;
    
    /**
     * 是否为混合图案
     */
    private Boolean isMixedPattern;
    
    /**
     * 图案类型
     * LINE: 线条, SOLID: 实地, MIXED: 混合图案
     */
    private String patternType;
    
    /**
     * 目标烫金类型列表
     */
    private String hotStampingType;
    
    /**
     * 目标烫金类型数组（用于IN查询）
     */
    private String[] targetHotStampingTypes;
    
    /**
     * 烫金类型ID（表 hot_stamping_compatibility.hot_stamping_type_id）
     */
    private Integer hotStampingTypeId;

    /**
     * 前工序（适用界面）ID（表 hot_stamping_compatibility.pre_processing_step_id）
     */
    private Integer preProcessingStepId;
    
    // ========== 过胶兼容性相关参数 ==========
    
    /**
     * 烫金纸系列
     */
    private String foilSeries;
    
    /**
     * 后加工步骤ID
     */
    private Integer postProcessingStepId;
    
    /**
     * 过胶材质ID
     */
    private Integer laminationMaterialId;
    
    /**
     * 接口类型ID
     */
    private Integer interfaceTypeId;
    
    /**
     * 兼容性状态筛选 (V/X)
     */
    private Character compatibility;
    
    /**
     * 搜索关键词
     */
    private String search;
    
    /**
     * 页码
     */
    private Integer page;
    
    /**
     * 每页大小
     */
    private Integer size;
}
