package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Specifications {
    private Integer id;
    private Integer project_id;
    private String color;
    private String size;
    private String tightness;
    private String temperature_range;
    private String performance;

}
