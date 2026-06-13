package com.it.yts_project.dto;

import lombok.Data;

/**
 * 布料纸类型DTO
 */
@Data
public class ClothPaperTypeDTO {
    private Integer id;
    private String typeName;
    private String category;
    private Integer sortOrder;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}


