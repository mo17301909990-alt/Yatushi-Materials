package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 烫金类型选项表实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotStampingTypeOptions {
    
    @ExcelProperty(value = "ID", index = 0)
    private Long id;
    
    /**
     * 烫金类型
     */
    @ExcelProperty(value = "燙金類型", index = 1)
    private String stampingType;
    
    /**
     * 位置类型
     */
    @ExcelProperty(value = "位置類型", index = 2)
    private String positionType;
    
    /**
     * 描述
     */
    @ExcelProperty(value = "描述", index = 3)
    private String description;
    
    /**
     * 是否激活
     */
    @ExcelProperty(value = "是否激活", index = 4)
    private String isActiveText;
    
    /**
     * 排序顺序
     */
    @ExcelProperty(value = "排序順序", index = 5)
    private Integer sortOrder;
    
    /**
     * 创建时间
     */
    @ExcelProperty(value = "創建時間", index = 6)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新時間", index = 7)
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