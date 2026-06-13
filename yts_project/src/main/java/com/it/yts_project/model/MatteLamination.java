package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatteLamination {
    private Integer id;
    private Integer project_id;
    private String standard_furonghui_22d;
    private String pre_coated_hy1206_65;
    private String high_tack_pre_coated_hy40;
    private String pre_coated_economical_high_wear_resistant_yt008a;
    private String pre_coated_high_wear_resistant_tn008;
    private String pre_coated_standard_wear_resistant_kvmb_f;
    private String soft_touch_matte_laminate_6015a;

}
