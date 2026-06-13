package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 硅胶_白UV物料产品模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class silicone_white_uvProduct {

    /**
     * 产品ID
     */
    private Integer id;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 采购部申请编号
     */
    private String supplierCode;

    /**
     * 物料型号/编号
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
     * 材质
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
     * 最小用纸尺寸(mm)
     */
    private String minSheetSize;

    /**
     * 最大用纸尺寸(mm)
     */
    private String maxSheetSize;

    /**
     * 最小间距(mm)
     */
    private String minSpacing;

    /**
     * 设计限制(点/线/间距/面积/结构)
     */
    private String designInfo;

    /**
     * 适用界面
     */
    private String applicableInterface;

    /**
     * 注意事项、限制的备注与说明
     */
    private String notes;

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
}
