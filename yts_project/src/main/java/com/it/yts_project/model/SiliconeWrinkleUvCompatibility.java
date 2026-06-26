package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 硅胶_皱纹UV兼容性模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiliconeWrinkleUvCompatibility {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 后加工工序名称
     */
    private String postProcessingStep;

    /**
     * 兼容性状态(V/X/▷)
     */
    private String compatibilityStatus;

    /**
     * 显示顺序
     */
    @Builder.Default
    private Integer displayOrder = 0;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 关联产品名称（非数据库字段，用于前端显示）
     */
    private String productName;
}
