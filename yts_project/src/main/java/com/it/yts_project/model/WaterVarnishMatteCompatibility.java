package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 水油(哑光)兼容性模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaterVarnishMatteCompatibility {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 关联产品ID
     */
    private Integer productId;

    /**
     * 后加工工序步骤名称
     */
    private String postProcessingStep;

    /**
     * 兼容性状态(V=兼容/X=不兼容/▷=有条件兼容)
     */
    private String compatibilityStatus;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 产品名称（非数据库字段，用于显示）
     */
    private String productName;
}
