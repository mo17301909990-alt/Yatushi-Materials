package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeoGpNumber {
    private Integer id;
    private Integer project_id;
    private String company_number;
    private String gp_no;

}
