package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 渐进式可选项返回模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressiveOptionsDTO {
    private List<String> paperPerformances;
    private List<OptionItem> productTypes;
    private List<OptionItem> patternCharacteristics;
    private List<OptionItem> hotStampingTypes;
}



