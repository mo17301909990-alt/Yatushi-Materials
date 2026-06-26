package com.it.yts_project.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 布面纸兼容性DTO
 */
@Data
public class ClothPaperCompatibilityDTO {
    @ExcelIgnore
    private Integer id;
    
    @ExcelProperty("燙金紙系列")
    private String productName;  // 燙金紙系列
    
    @ExcelIgnore
    private Integer clothPaperTypeId;
    
    @ExcelProperty("兼容性狀態")
    private String compatibilityStatus;
    
    @ExcelProperty("燙金紙性能類型(V:适用, X:不适用, NA:不确定, ▷:需要打底处理)")
    private String paperType;  // 纸张类型
    
    @ExcelIgnore
    private LocalDateTime createdAt;
    
    @ExcelIgnore
    private LocalDateTime updatedAt;

    private List<Integer> noticeIds;  // 关联的注意事项ID数组

    // 关联查询字段
    @ExcelProperty("適用界面類型名稱")
    private String clothPaperTypeName;  // 布面纸类型名称
    
    @ExcelProperty("適用界面分類")
    private String clothPaperCategory;  // 布面纸分类
    
    // 兼容性状态描述
    public String getCompatibilityStatusDescription() {
        switch (compatibilityStatus) {
            case "V": return "适用";
            case "X": return "不适用";
            case "NA": return "不确定";
            case "▷": return "需要打底处理";
            default: return "未知";
        }
    }
}
