package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 硅胶磨砂UV兼容性模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class silicone_sandblast_uvCompatibility {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 产品ID，关联silicone_sandblast_uv_product
     */
    private Integer productId;

    /**
     * 後加工工序名称
     */
    private String postProcessingStep;

    /**
     * 兼容性状态(V=兼容, X=不兼容, ▷=有条件)
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
}
