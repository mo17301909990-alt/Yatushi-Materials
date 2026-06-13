package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

/**
 * 过胶材料兼容性模型
 * 对应表: lamination_material_compatibility
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaminationMaterialCompatibility {

    private Integer id;
    private Integer productId;
    private String postProcessingStep;
    private String compatibilityStatus;
    private String remark;
    private Integer displayOrder;
    private LocalDateTime createdAt;
}
