package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 硅胶粗纹UV兼容性模型
 * 对应 silicone_coarse_uv_compatibility 表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class silicone_coarse_uvCompatibility {

    private Integer id;

    /**
     * 产品ID，关联 silicone_coarse_uv_product
     */
    private Integer productId;

    /**
     * 后加工工序名称
     */
    private String postProcessingStep;

    /**
     * 兼容性状态（V=兼容, X=不兼容, 空=未标注）
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

    // ========== 关联字段（非数据库列） ==========

    /**
     * 产品名称（关联查询填充）
     */
    private String productName;
}
