package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

/**
 * 过胶材料产品模型
 * 对应表: lamination_material_products
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaminationMaterialProduct {

    private Integer id;
    private String materialCode;
    private String stockCode;
    private String materialName;
    private String usageText;
    private String materialType;
    private String thicknessFilm;
    private String thicknessGlue;
    private String sizeInfo;
    private String color;
    private String shape;
    private String responsiblePerson;
    private String category;
    private Boolean isActive;
    private String filmThickness;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
