package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiliconeCoarseUvProduct {
    private Integer id;
    private String materialCode;
    private String supplierCode;
    private String stockCode;
    private String materialName;
    private String usage;
    private String category;
    private String color;
    private String responsiblePerson;
    private String minSheetSize;
    private String maxSheetSize;
    private String minSpacing;
    private String designInfo;
    private String applicableInterface;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
