package com.it.yts_project.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 硅胶发光油墨兼容性模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiliconeGlowInkCompatibility {

    /**
     * 主键ID
     */
    @ExcelProperty(value = "ID", index = 0)
    private Integer id;

    /**
     * 关联产品ID
     */
    @ExcelProperty(value = "产品ID", index = 1)
    private Integer productId;

    /**
     * 后加工工序名称
     */
    @ExcelProperty(value = "后加工工序", index = 2)
    private String postProcessingStep;

    /**
     * 兼容性状态(V=兼容, X=不兼容, ▷=有条件兼容)
     */
    @ExcelProperty(value = "兼容性状态", index = 3)
    private String compatibilityStatus;

    /**
     * 显示排序
     */
    @ExcelProperty(value = "显示排序", index = 4)
    private Integer displayOrder;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 5)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间", index = 6)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * 关联产品名称（非数据库字段，用于前端显示）
     */
    private String productName;
}
