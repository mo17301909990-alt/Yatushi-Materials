package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 耐磨金纸映射表实体类
 * 对应数据库表: wear_resistant_gold_paper_mapping
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WearResistantGoldPaperMapping {
    
    /**
     * 主键ID
     */
    @ExcelIgnore
    private Integer id;
    
    /**
     * 燙金紙系列
     */
    @ExcelProperty("燙金紙系列")
    private String productName;
    
    /**
     * 产品型号（可为空，表示系列级映射）
     */
    @ExcelProperty("產品型號")
    private String productModelNumber;
    
    /**
     * 烫金类型（固定为"燙金後擊凸"）
     */
    @ExcelProperty("燙金類型")
    private String hotStampingType;
    
    /**
     * 耐磨金纸类型
     */
    @ExcelProperty("耐磨金紙類型")
    private String goldPaperType;
    
    /**
     * 兼容性状态
     * V: 适用, X: 不适用, NA: 不确定, ▷: 需要打底处理
     */
    @ExcelProperty("兼容性狀態(V:适用, X:不适用, NA:不确定, ▷:需要打底处理)")
    private String compatibilityStatus;
    
    /**
     * 备注
     */
    @ExcelProperty("備註")
    private String remarks;
    
    /**
     * 创建时间
     */
    @ExcelIgnore
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @ExcelIgnore
    private LocalDateTime updatedAt;
}


