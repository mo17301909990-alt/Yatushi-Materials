package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * UV油_哑光UV油产品模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UvOilMatteProduct {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 物料编码(种类+材料+主类别+副类别+流水码)
     */
    private String materialCode;

    /**
     * 采购部申请编号/供应商编码
     */
    private String supplierCode;

    /**
     * 物料型号/编号(存仓物料号)
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
     * 材质分类
     */
    private String category;

    /**
     * 颜色
     */
    private String color;

    /**
     * 测试员(中文名)
     */
    private String responsiblePerson;

    /**
     * 用纸尺寸-最小(mm)
     */
    private String minSheetSize;

    /**
     * 用纸尺寸-最大(mm)
     */
    private String maxSheetSize;

    /**
     * 间距-最小(mm)
     */
    private String minSpacing;

    /**
     * 设计限制信息(点/线/间距/面积/结构)
     */
    private String designInfo;

    /**
     * 适用界面(纸张面/印刷油墨面/后加工涂层面)
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
