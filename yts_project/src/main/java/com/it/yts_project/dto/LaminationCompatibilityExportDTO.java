package com.it.yts_project.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 过胶兼容性Excel导出DTO
 * 用于Excel导出时的数据格式化和字段映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaminationCompatibilityExportDTO {
    
    @ExcelProperty(value = "ID", index = 0)
    private Integer id;
    
    @ExcelProperty(value = "箔纸系列", index = 1)
    private String foilSeries;
    
    @ExcelProperty(value = "产品型号", index = 2)
    private String modelNumber;
    
    @ExcelProperty(value = "产品类型", index = 3)
    private String productType;
    
    @ExcelProperty(value = "过胶材料", index = 4)
    private String laminationMaterialName;
    
    @ExcelProperty(value = "后处理工序", index = 5)
    private String postProcessingStepName;
    
    @ExcelProperty(value = "兼容性", index = 6)
    private String compatibility;
    
    @ExcelProperty(value = "是否接货", index = 7)
    private String isJiehuo;
    
    @ExcelProperty(value = "创建时间", index = 8)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @ExcelProperty(value = "更新时间", index = 9)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
