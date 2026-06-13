package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * UV油_哑光水油产品模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaterVarnishMatteProduct {

    private Integer id;

    /** 物料型號/編號 */
    private String materialCode;

    /** 採購部申請編號 */
    private String supplierCode;

    /** 物料編碼 */
    private String stockCode;

    /** 物料名稱 */
    private String materialName;

    /** 物料用途 */
    private String usage;

    /** 材質 */
    private String materialType;

    /** 顏色 */
    private String color;

    /** 測試員(中文名) */
    private String responsiblePerson;

    /** 用紙尺寸-最小(mm) */
    private String minSheetSize;

    /** 用紙尺寸-最大(mm) */
    private String maxSheetSize;

    /** 點-最小(mm) */
    private String minPoint;

    /** 點-最大(mm) */
    private String maxPoint;

    /** 線-最小(mm) */
    private String minLine;

    /** 線-最大(mm) */
    private String maxLine;

    /** 間距-最小(mm) */
    private String minSpacing;

    /** 間距-最大(mm) */
    private String maxSpacing;

    /** 單個圖案加工面積-最小(mm²) */
    private String minArea;

    /** 單個圖案加工面積-最大(mm²) */
    private String maxArea;

    /** 結構應用(適用產品) */
    private String structureApplication;

    /** 適用界面資訊 */
    private String applicableInterface;

    /** 注意事項、限制的備注與說明 */
    private String notes;

    /** 光澤 */
    private String gloss;

    /** 表面寫字功能說明 */
    private String writingFunction;

    /** 是否激活 */
    private Boolean isActive;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
