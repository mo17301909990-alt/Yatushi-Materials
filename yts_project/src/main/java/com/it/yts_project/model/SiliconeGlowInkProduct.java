package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 硅胶发光油墨产品模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiliconeGlowInkProduct {

    /**
     * 主键ID
     */
    @ExcelProperty(value = "ID", index = 0)
    private Integer id;

    /**
     * 物料编码(种类+材料+主类别+副类别+流水码)
     */
    @ExcelProperty(value = "物料编码", index = 1)
    private String materialCode;

    /**
     * 供应商/采购部申请编号
     */
    @ExcelProperty(value = "供应商编号", index = 2)
    private String supplierCode;

    /**
     * 物料型号/编号(存仓物料/跟单物料)
     */
    @ExcelProperty(value = "物料型号/编号", index = 3)
    private String stockCode;

    /**
     * 物料名称
     */
    @ExcelProperty(value = "物料名称", index = 4)
    private String materialName;

    /**
     * 物料用途
     */
    @ExcelProperty(value = "物料用途", index = 5)
    private String usage;

    /**
     * 材质
     */
    @ExcelProperty(value = "材质", index = 6)
    private String category;

    /**
     * 颜色
     */
    @ExcelProperty(value = "颜色", index = 7)
    private String color;

    /**
     * 测试员(中文名)
     */
    @ExcelProperty(value = "测试员", index = 8)
    private String responsiblePerson;

    /**
     * 用纸尺寸(最小)
     */
    @ExcelProperty(value = "用纸尺寸(最小)", index = 9)
    private String minSheetSize;

    /**
     * 用纸尺寸(最大)
     */
    @ExcelProperty(value = "用纸尺寸(最大)", index = 10)
    private String maxSheetSize;

    /**
     * 间距(最小)
     */
    @ExcelProperty(value = "间距(最小)", index = 11)
    private String minSpacing;

    /**
     * 间距(最大)
     */
    @ExcelProperty(value = "间距(最大)", index = 12)
    private String maxSpacing;

    /**
     * 设计限制信息
     */
    @ExcelProperty(value = "设计限制", index = 13)
    private String designInfo;

    /**
     * 适用界面
     */
    @ExcelProperty(value = "适用界面", index = 14)
    private String applicableInterface;

    /**
     * 注意事项、限制的备注与说明
     */
    @ExcelProperty(value = "注意事项", index = 15)
    private String notes;

    /**
     * 是否激活
     */
    @ExcelProperty(value = "是否激活", index = 16)
    private Boolean isActive;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 17)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间", index = 18)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
