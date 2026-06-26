package com.it.yts_project.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 智能兼容性规则导出 DTO，用于「导出全部」Excel（扁平表）。
 * 列顺序：序、產品類型、前工序界面、圖案特徵、燙金類型、燙金紙性能（映射配置）、兼容性。（不含工艺流程/後工序）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmartCompatibilityExportDTO {

    @ExcelProperty(value = "序", index = 0)
    private Integer serialNumber;

    @ExcelProperty(value = "產品類型", index = 1)
    private String productTypeName;

    @ExcelProperty(value = "前工序界面", index = 2)
    private String preProcessingStepName;

    @ExcelProperty(value = "圖案特徵（燙金圖案X）", index = 3)
    private String patternCharacteristicName;

    @ExcelProperty(value = "燙金類型", index = 4)
    private String hotStampingTypeName;

    @ExcelProperty(value = "燙金紙性能（映射配置）", index = 5)
    private String paperPerformance;

    @ExcelProperty(value = "兼容性", index = 6)
    private String compatibilityDisplay;
}
