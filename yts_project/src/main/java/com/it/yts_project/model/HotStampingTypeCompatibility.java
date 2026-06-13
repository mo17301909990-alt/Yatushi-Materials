package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 烫金类型兼容性表实体类
 * 对应数据库表: hot_stamping_type_compatibility
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotStampingTypeCompatibility {
    
    private Integer id;
    
    /**
     * 燙金紙系列
     */
    private String productName;
    
    /**
     * 烫金类型ID
     */
    private Integer hotStampingTypeId;
    
    /**
     * 兼容性状态
     * V: 适用, X: 不适用, NA: 不确定, ▷: 需要打底处理
     */
    private String compatibilityStatus;
    
    /**
     * 纸张类型
     */
    private String paperType;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 烫金类型名称（用于显示，不存储在数据库中）
     */
    private String hotStampingTypeName;
}
