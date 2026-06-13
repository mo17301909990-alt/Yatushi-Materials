package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoldFoilType {
    private Integer id;
    private Integer project_id;
    private String flat_hot_stamping;
    private String embossed_gold_stamping;
    private String refractive_holographic_patterned_textured_hot_stamping;
    private String post_hot_stamping_embossing_debossing;

}
