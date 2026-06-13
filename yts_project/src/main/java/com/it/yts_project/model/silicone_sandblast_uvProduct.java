package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 硅胶磨砂UV产品模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class silicone_sandblast_uvProduct {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 物料编码(種類+材料+主類別+副類別+流水碼)
     */
    private String materialCode;

    /**
     * 採購部申請編號
     */
    private String supplierCode;

    /**
     * 物料型號/編號
     */
    private String stockCode;

    /**
     * 物料名稱
     */
    private String materialName;

    /**
     * 物料用途
     */
    private String usage;

    /**
     * 材質
     */
    private String category;

    /**
     * 顏色
     */
    private String color;

    /**
     * 測試員(中文名)
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
     * 設計資訊(點/線/單個圖案加工面積/結構應用等)
     */
    private String designInfo;

    /**
     * 適用界面(紙張面/印刷油墨面/後加工塗層面)
     */
    private String applicableInterface;

    /**
     * 注意事項、限制的備注與說明
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
