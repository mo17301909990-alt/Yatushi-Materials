package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 硅胶_皱纹UV产品模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiliconeWrinkleUvProduct {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 库存编码
     */
    private String stockCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料用途
     */
    private String usage;

    /**
     * 材质类别
     */
    private String category;

    /**
     * 颜色
     */
    private String color;

    /**
     * 测试员
     */
    private String responsiblePerson;

    /**
     * 最小用纸尺寸
     */
    private String minSheetSize;

    /**
     * 最大用纸尺寸
     */
    private String maxSheetSize;

    /**
     * 最小间距
     */
    private String minSpacing;

    /**
     * 设计信息(点/线/面积/结构应用)
     */
    private String designInfo;

    /**
     * 适用界面
     */
    private String applicableInterface;

    /**
     * 注意事项
     */
    private String notes;

    /**
     * 是否启用
     */
    @Builder.Default
    private Boolean isActive = true;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
