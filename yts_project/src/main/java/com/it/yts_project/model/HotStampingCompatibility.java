package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 烫金工艺兼容性表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotStampingCompatibility {
    
    @ExcelProperty(value = "ID", index = 0)
    private Long id;
    
    /**
     * 烫金纸性能类型
     * 普通金紙, 普通耐磨, 高耐磨
     */
    @ExcelProperty(value = "燙金紙性能類型", index = 1)
    private String paperPerformance;
    
    /**
     * 主要应用产品类型
     * 精平裝/咭書, 賀咭/紙袋, 包裝
     */
    @ExcelProperty(value = "產品類型", index = 2)
    private String productType;
    
    /**
     * 烫金图案特征
     * 净线条/字母≤0.5mm, 净实地10mm<X≤20mm 等
     */
    @ExcelProperty(value = "圖案特徵", index = 3)
    private String patternCharacteristic;
    
    /**
     * 烫金类型
     * 平面烫金-於中間位, 平面烫金-到邊位, 立體烫金, 磨砂烫金, 折光烫金
     */
    @ExcelProperty(value = "燙金類型", index = 4)
    private String hotStampingType;
    
    /**
     * 兼容性标识
     * V: 兼容, X: 不兼容
     */
    @ExcelProperty(value = "兼容性", index = 5)
    private String compatibility;
    
    /**
     * 前工序ID
     */
    @ExcelProperty(value = "前工序ID", index = 6)
    private Integer preProcessingStepId;
    
    /**
     * 产品类型ID
     */
    @ExcelProperty(value = "產品類型ID", index = 7)
    private Integer productTypeId;
    
    /**
     * 图案特征ID
     */
    @ExcelProperty(value = "圖案特徵ID", index = 8)
    private Integer patternCharacteristicId;
    
    /**
     * 烫金类型ID
     */
    @ExcelProperty(value = "燙金類型ID", index = 9)
    private Integer hotStampingTypeId;
    
    /**
     * 后工序ID
     */
    @ExcelProperty(value = "後工序ID", index = 10)
    private Integer postProcessingStepId;
    
    /**
     * 创建时间
     */
    @ExcelProperty(value = "創建時間", index = 11)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新時間", index = 12)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
