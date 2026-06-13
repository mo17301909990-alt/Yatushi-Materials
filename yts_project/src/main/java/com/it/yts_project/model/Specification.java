package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 规格信息模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Specification {
    
    /**
     * 规格ID
     */
    private Integer id;
    
    /**
     * 产品ID
     */
    private Integer projectId;
    
    /**
     * 颜色
     */
    private String color;
    
    /**
     * 尺寸
     */
    private String size;
    
    /**
     * 紧密度
     */
    private String tightness;
    
    /**
     * 温度范围
     */
    private String temperatureRange;
    
    /**
     * 性能
     */
    private String performance;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
