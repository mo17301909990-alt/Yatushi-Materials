package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialCategoryDTO {
    private String id;
    private String name;
    private String icon;
    private int count;
    private List<MaterialStandardDTO> standards;
}
