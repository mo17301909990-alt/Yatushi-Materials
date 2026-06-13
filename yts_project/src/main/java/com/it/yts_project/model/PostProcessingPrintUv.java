package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 印刷UV后加工表实体类
 * 对应数据库表: post_processing_print_uv
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostProcessingPrintUv {
    
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
    
    /**
     * 公司编号/客户GP号
     * 对应 leo_gp_numbers 表中的 company_number 或 gp_no
     */
    @ExcelProperty("公司編號/客戶GP號")
    private String companyNumber;
    
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
