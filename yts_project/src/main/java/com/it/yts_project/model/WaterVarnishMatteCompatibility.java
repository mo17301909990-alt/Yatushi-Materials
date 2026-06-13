package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * UV油_哑光水油后加工兼容性模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaterVarnishMatteCompatibility {

    private Integer id;

    /** 产品ID,关联water_varnish_matte_product.id */
    private Integer productId;

    /** 后加工工序步骤名称 */
    private String postProcessingStep;

    /** 兼容性状态: V=兼容, X=不兼容 */
    private String compatibilityStatus;

    /** 显示顺序 */
    private Integer displayOrder;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
