package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 烫金图案基础信息表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotStampingPatternBase {
    
    @ExcelProperty(value = "ID", index = 0)
    private Long id;
    
    /**
     * 选项名称
     */
    @ExcelProperty(value = "選項名稱", index = 1)
    private String optionName;
    
    /**
     * 图案类型
     */
    @ExcelProperty(value = "圖案類型", index = 2)
    private String patternType;
    
    /**
     * 线条粗细最小值
     */
    @ExcelProperty(value = "線條粗細最小值", index = 3)
    private BigDecimal lineThicknessMin;
    
    /**
     * 线条粗细最大值
     */
    @ExcelProperty(value = "線條粗細最大值", index = 4)
    private BigDecimal lineThicknessMax;
    
    /**
     * 实地面积最小值
     */
    @ExcelProperty(value = "實地面積最小值", index = 5)
    private BigDecimal solidAreaMin;
    
    /**
     * 实地面积最大值
     */
    @ExcelProperty(value = "實地面積最大值", index = 6)
    private BigDecimal solidAreaMax;
    
    /**
     * 是否激活
     */
    @ExcelProperty(value = "是否激活", index = 7)
    private String isActiveText;
    
    /**
     * 排序顺序
     */
    @ExcelProperty(value = "排序順序", index = 8)
    private Integer sortOrder;
    
    /**
     * 创建时间
     */
    @ExcelProperty(value = "創建時間", index = 9)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新時間", index = 10)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    /**
     * 是否激活（原始字段，用于业务逻辑）
     */
    private Boolean isActive;

    /**
     * 关联的注意事项ID数组
     */
    private List<Integer> noticeIds;

    /**
     * 获取激活状态文本
     */
    public String getIsActiveText() {
        return isActive != null && isActive ? "是" : "否";
    }
}