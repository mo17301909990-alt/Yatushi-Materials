package com.it.yts_project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 烫金类型选项DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotStampingTypeOptionDTO {
    
    /**
     * 选项ID
     */
    private Long id;
    
    /**
     * 显示名称
     */
    private String displayName;
    
    /**
     * 位置类型（null表示默认）
     */
    private String positionType;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 排序顺序
     */
    private Integer sortOrder;
}
