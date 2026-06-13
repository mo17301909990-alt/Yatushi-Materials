package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmButterPaper {

    private Integer id;
    private Integer project_id;
    private String vet_vvc_avet;
    private String scratch_averse_avet_no_foil;
    private String butter_paper_gtf;
    private String butter_paper_ztf;

}
