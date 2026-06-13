package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 过胶材料选项模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaminationMaterialOption {
    
    /**
     * 材料ID
     */
    @ExcelProperty(value = "ID", index = 0)
    private Integer id;
    
    /**
     * 材料名称
     */
    @ExcelProperty(value = "材料名稱", index = 1)
    private String materialName;
    
    /**
     * 材料编码
     */
    @ExcelProperty(value = "材料編碼", index = 2)
    private String materialCode;
    
    /**
     * 显示顺序
     */
    @ExcelProperty(value = "顯示順序", index = 3)
    private Integer displayOrder;
    
    /**
     * 是否激活
     */
    @ExcelProperty(value = "是否激活", index = 4)
    private String isActiveText;
    
    /**
     * 描述
     */
    @ExcelProperty(value = "描述", index = 5)
    private String description;
    
    /**
     * 父级ID
     */
    @ExcelProperty(value = "父級ID", index = 6)
    private Integer pid;
    
    /**
     * 创建时间
     */
    @ExcelProperty(value = "創建時間", index = 7)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新時間", index = 8)
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
