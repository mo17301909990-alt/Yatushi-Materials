package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 产品信息模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    
    /**
     * 产品ID
     */
    @ExcelProperty("產品ID")
    private Integer id;
    
    /**
     * 燙金紙系列
     */
    @ExcelProperty("燙金紙系列")
    private String name;
    
    /**
     * 型号
     */
    @ExcelProperty("型號")
    private String modelNumber;
    
    /**
     * 物料编号
     */
    @ExcelProperty("物料編號")
    private String materialNumber;
    
    /**
     * 烫金纸张类型
     */
    @ExcelProperty("燙金紙張類型")
    private String hotStampingPaperType;
    
    /**
     * 描述
     */
    @ExcelProperty("描述")
    private String descirption;
    
    /**
     * 封度
     */
    @ExcelProperty("封度")
    private String fengdu;
    
    /**
     * 牌子
     */
    @ExcelProperty("牌子")
    private String paizi;
    
    /**
     * 概述
     */
    @ExcelProperty("概述")
    private String gaishu;
    
    /**
     * 单位
     */
    @ExcelProperty("單位")
    private String danwei;
    
    /**
     * 是否常用
     */
    @ExcelProperty("是否常用")
    private String isChangyong;
    
    /**
     * 是否街货
     */
    @ExcelProperty("是否街貨")
    private Boolean isJiehuo;
    
    /**
     * 物料状态标记：可用、废弃
     */
    @ExcelProperty("物料狀態")
    private String status;
    
    /**
     * 创建时间
     */
    @ExcelProperty("創建時間")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @ExcelProperty("更新時間")
    private LocalDateTime updatedAt;
    
    /**
     * 规格信息列表
     */
    private List<Specification> specifications;
    
    /**
     * 价格信息列表
     */
    private List<Pricing> pricingList;
}
