package com.it.yts_project.dto;

import lombok.Data;

/*
这个是右侧烫金纸供应型号的数据传输对象
 */
@Data
public class GoldFoilProductDTO {
    // Products表字段
    private Long id; // 产品ID，用于更新usage_count
    private String name;
    private String modelNumber;
    private String materialNumber;
    private String descirption;
    private String fengdu;
    private String gaishu;
    private String danwei;
    private String isJiehuo;
    private String isChangyong;
    private String hotStampingPaperType;
    private Long usageCount; // 物料被筛选出来的次数，用于判断是否常用
    // Specifications表字段
    private String color;
    private String size;
    private String tightness;
    private String temperatureRange;
    private String performance;

    // GoldFoilType表字段
    private String flatHotStamping;
    private String embossedGoldStamping;
    private String refractiveHolographicPatternedTexturedHotStamping;
    private String postHotStampingEmbossingDebossing;

    // Pricing表字段
    private Double price;

    // LeoGpNumbers表字段
    private String companyNumber;
    private String gpNo;

    // ProcessingIroning表字段
    private String uvPrinting; // 11.燙後加工.印刷UV
    private String ledUvGlitter; // 12.燙後加工.絲印LED UV灑閃粉
    private String stampingPrinting; // 13.燙後加工.烫金+印刷
    
    // 匹配度相关字段（计算得出，不存储在数据库）
    private Integer matchScore; // 匹配度分数（0-100），保留用于兼容
    private Double finalMatchScore; // 最终匹配度分数（0-100），基于价格分档和使用次数计算
}
