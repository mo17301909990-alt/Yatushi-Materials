package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 哑光UV油产品模型
 * 源文件: 哑光UV油标准书-20250730 (10)(1).xlsx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YaguangUvOilProduct {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * NTD测试单号
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
     * 厚度
     */
    private String thickness;

    /**
     * 尺寸
     */
    private String size;

    /**
     * 颜色
     */
    private String color;

    /**
     * 形状
     */
    private String shape;

    /**
     * 测试员
     */
    private String responsiblePerson;

    /**
     * 用纸尺寸最小
     */
    private String minSheetSize;

    /**
     * 用纸尺寸最大
     */
    private String maxSheetSize;

    /**
     * 点最小
     */
    private String minDot;

    /**
     * 点最大
     */
    private String maxDot;

    /**
     * 线最小
     */
    private String minLine;

    /**
     * 线最大
     */
    private String maxLine;

    /**
     * 间距最小
     */
    private String minSpacing;

    /**
     * 间距最大
     */
    private String maxSpacing;

    /**
     * 单图案面积最小
     */
    private String minPatternArea;

    /**
     * 单图案面积最大
     */
    private String maxPatternArea;

    /**
     * 适用产品描述
     */
    private String applicableProduct;

    /**
     * 结构-单面 V/X
     */
    private String structureSingleSide;

    /**
     * 结构-双面 V/X
     */
    private String structureDoubleSide;

    /**
     * 结构-封面 V/X
     */
    private String structureCover;

    /**
     * 结构-书脊 V/X
     */
    private String structureSpine;

    /**
     * 结构-踩坑位 V/X
     */
    private String structureDeboss;

    /**
     * 结构-内文 V/X
     */
    private String structureInnerPage;

    /**
     * 基材厚度
     */
    private String substrateThickness;

    /**
     * 纸张面-适用
     */
    private String paperSurfaceApplicable;

    /**
     * 纸张面-不适用
     */
    private String paperSurfaceInapplicable;

    /**
     * 油墨面-适用
     */
    private String inkSurfaceApplicable;

    /**
     * 油墨面-不适用
     */
    private String inkSurfaceInapplicable;

    /**
     * 涂层面-适用
     */
    private String coatingSurfaceApplicable;

    /**
     * 涂层面-不适用
     */
    private String coatingSurfaceInapplicable;

    /**
     * 原配笔 V/X
     */
    private String writingStandardPen;

    /**
     * 客户指定笔 V/X
     */
    private String writingCustomerPen;

    /**
     * 备注说明
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
