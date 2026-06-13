package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 絲印LED UV灑閃粉后加工表实体类
 * 对应数据库表: post_processing_leduvglitter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostProcessingLeduvglitter {
    
    @ExcelIgnore
    private Integer id;
    
    /**
     * 燙金紙系列
     */
    @ExcelProperty("燙金紙系列")
    private String productName;
    
    /**
     * 产品型号
     */
    @ExcelProperty("產品型號")
    private String productModelNumber;
    
    @ExcelIgnore
    private Integer clothPaperTypeId;
    
    /**
     * 布面纸类型名称 (category + "." + type_name)
     */
    @ExcelProperty("布面紙類型")
    private String clothPaperTypeName;
    
    /**
     * 布面纸类型分类（用于导入时查找ID）
     */
    @ExcelIgnore
    private String clothPaperTypeCategory;
    
    /**
     * 兼容性状态
     * V: 适用, X: 不适用, NA: 不确定, ▷: 需要打底处理
     */
    @ExcelProperty("兼容性狀態(V:适用, X:不适用, NA:不确定, ▷:需要打底处理)")
    private String compatibilityStatus;
    
    /**
     * 纸张类型
     */
    @ExcelProperty("燙金紙性能類型")
    private String paperType;
    
    @ExcelIgnore
    private LocalDateTime createdAt;
    
    @ExcelIgnore
    private LocalDateTime updatedAt;
}