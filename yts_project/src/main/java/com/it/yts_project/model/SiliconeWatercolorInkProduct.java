package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 硅胶水彩油墨产品信息模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiliconeWatercolorInkProduct {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 物料编码（種類+材料+主類別+副類別+流水碼组合）
     */
    private String materialCode;

    /**
     * 供應商物料型號/編號
     */
    private String supplierCode;

    /**
     * 庫存編碼/採購申請編號
     */
    private String stockCode;

    /**
     * 物料名稱（如：水彩墨）
     */
    private String materialName;

    /**
     * 物料用途
     */
    private String usage;

    /**
     * 材質分類
     */
    private String category;

    /**
     * 顏色
     */
    private String color;

    /**
     * 測試員（負責人）
     */
    private String responsiblePerson;

    /**
     * 用紙尺寸-最小(mm)
     */
    private String minSheetSize;

    /**
     * 用紙尺寸-最大(mm)
     */
    private String maxSheetSize;

    /**
     * 間距-最小(mm)
     */
    private String minSpacing;

    /**
     * 間距-最大(mm)
     */
    private String maxSpacing;

    /**
     * 設計限制信息（點/線/結構應用等）
     */
    private String designInfo;

    /**
     * 適用界面信息（紙張面/印刷油墨面/後加工涂層面）
     */
    private String applicableInterface;

    /**
     * 注意事項、限制的備注與說明
     */
    private String notes;

    /**
     * 是否启用
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
