package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 产品类型选项实体类
 * 对应数据库表：product_type_options
 */
@Data
public class ProductTypeOptions {
    
    /**
     * 主键ID
     */
    @ExcelProperty(value = "ID", index = 0)
    private Integer id;
    
    /**
     * 燙金紙系列
     */
    @ExcelProperty(value = "產品名稱", index = 1)
    private String productName;
    
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
