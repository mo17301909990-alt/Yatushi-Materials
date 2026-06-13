package com.it.yts_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 烫金类型分组DTO（支持多级下拉框）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotStampingTypeGroupDTO {
    
    /**
     * 烫金类型名称（如：平面燙金）
     */
    private String stampingType;
    
    /**
     * 是否为默认选项（没有子选项）
     */
    private Boolean isDefault;
    
    /**
     * 子选项列表（位置类型选项）
     */
    private List<HotStampingTypeOptionDTO> options;
}

