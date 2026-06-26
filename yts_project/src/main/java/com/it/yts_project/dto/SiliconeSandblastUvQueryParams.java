package com.it.yts_project.dto;

import lombok.Data;

@Data
public class SiliconeSandblastUvQueryParams {
    private String keyword;
    private String stepName;
    private Integer page = 1;
    private Integer size = 15;
}
