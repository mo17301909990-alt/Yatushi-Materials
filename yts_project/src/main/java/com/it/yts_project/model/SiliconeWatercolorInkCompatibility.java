package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 硅胶水彩油墨后加工兼容性模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiliconeWatercolorInkCompatibility {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 产品ID，关联silicone_watercolor_ink_product
     */
    private Integer productId;

    /**
     * 产品名称（冗余字段，用于展示）
     */
    private String productName;

    /**
     * 后加工工序名称（如：單面印刷、普通燙金、局部UV等）
     */
    private String postProcessingStep;

    /**
     * 兼容性状态：V=兼容, X=不兼容, ▷=有條件兼容
     */
    private String compatibilityStatus;

    /**
     * 显示排序
     */
    private Integer displayOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
