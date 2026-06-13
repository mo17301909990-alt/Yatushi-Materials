package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products {
    private Integer id;
    private String name;
    private String model_number;
    private String material_number;
    private String hot_stamping_paper_type;

}
