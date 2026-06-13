package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 前工序选项实体类
 */
@Data
public class PreProcessingStepsOptions {
    
    /**
     * 主键ID
     */
    @ExcelProperty(value = "ID", index = 0)
    private Integer id;
    
    /**
     * 前工序名称
     */
    @ExcelProperty(value = "前工序名稱", index = 1)
    private String preProcessingStepsName;
    
    /**
     * 显示顺序
     */
    @ExcelProperty(value = "顯示順序", index = 2)
    private Integer displayOrder;
    
    /**
     * 是否激活
     */
    @ExcelProperty(value = "是否激活", index = 3)
    private String isActiveText;
    
    /**
     * 描述
     */
    @ExcelProperty(value = "描述", index = 4)
    private String description;
    
    /**
     * 创建时间
     */
    @ExcelProperty(value = "創建時間", index = 5)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新時間", index = 6)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    /**
     * 步骤
     */
    @ExcelProperty(value = "步驟", index = 7)
    private String steps;
    
    /**
     * 工艺
     */
    @ExcelProperty(value = "工藝", index = 8)
    private String process;
    
    /**
     * 是否激活（原始字段，用于业务逻辑）
     */
    private Boolean isActive;
    
    /**
     * 获取激活状态文本
     */
    public String getIsActiveText() {
        return isActive != null && isActive ? "是" : "否";
    }
}
