package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 印刷后加工表实体类
 * 对应数据库表: post_processing_print
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostProcessingPrint {
    
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
     * 产品型号
     */
    @ExcelProperty("產品型號")
    private String productModelNumber;
    
    /**
     * 颜色
     */
    @ExcelProperty("顏色")
    private String color;
    
    /**
     * 布面纸类型ID
     * 注意：此字段不用于导入导出，不会出现在Excel模板中
     * 导入时通过clothPaperTypeName（格式：category.type_name）自动查找对应的clothPaperTypeId
     */
    @ExcelIgnore
    private Integer clothPaperTypeId;
    
    /**
     * 布面纸类型名称（用于导出，格式：category.type_name）
     * 注意：此字段不存储在数据库中，仅用于Excel导出显示
     */
    @ExcelProperty("布面紙類型")
    private String clothPaperTypeName;
    
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

    /**
     * 关联的注意事项ID列表
     * 对应数据库字段: notice_ids (INTEGER[])
     */
    @ExcelIgnore
    private List<Integer> noticeIds;
}