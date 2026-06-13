package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingIroning {
    private Integer id;
    private Integer project_id;
    private String lamination;
    private String uv_printing;
    private String silk_screen_led_uv_sparkle_powder;
    private String printing;
}
