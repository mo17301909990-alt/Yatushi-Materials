package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 硅胶_白UV兼容性模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class silicone_white_uvCompatibility {

    /**
     * 兼容性ID
     */
    private Integer id;

    /**
     * 关联产品ID
     */
    private Integer productId;

    /**
     * 后加工工序名称
     */
    private String postProcessingStep;

    /**
     * 兼容性状态(V=兼容/X=不兼容)
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
     * 产品名称（关联查询用，非数据库字段）
     */
    private String productName;
}
