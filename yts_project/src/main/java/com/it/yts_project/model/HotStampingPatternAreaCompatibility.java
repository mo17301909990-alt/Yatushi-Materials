package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 烫金图案区域兼容性表实体类
 * 对应数据库表: hot_stamping_patternx_area_compatibility
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotStampingPatternAreaCompatibility {
    
    @ExcelIgnore
    private Integer id;
    
    /**
     * 燙金紙系列
     */
    @ExcelProperty("燙金紙系列")
    private String productName;
    
    @ExcelIgnore
    private Integer hotStampingPatternxAreaId;
    
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
    
    // 关联查询字段（用于导出显示）
    @ExcelProperty("圖案类型")
    private String patternType;
    
    // 关联查询字段（用于显示图案区域名称）
    @ExcelIgnore
    private String optionName;
}
