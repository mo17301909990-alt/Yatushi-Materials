package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OilAndUvTypes {
    private Integer id;
    private Integer project_id;
    private String oil_6812_glossy_3301_3440_dumb;
    private String gv_led_uv_gloss_matte_oil;
    private String oil_based_gloss_matte_on_powder_paper;
    private String oil_based_gloss_matte_non_powder_paper;

}
