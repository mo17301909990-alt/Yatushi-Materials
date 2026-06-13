package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用选项项（ID + 名称）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionItem {
    private Integer id;
    private String name;
}



