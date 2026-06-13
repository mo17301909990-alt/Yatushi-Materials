package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pattern {
    private Integer id;
    private Integer project_id;
    private String gradient_halftone;
    private String thin_lines_letters;
    private String medium_small_thin_lines_letters;
    private String medium_large_area_fine_lines_letters;
    private String extra_large_area;

}
