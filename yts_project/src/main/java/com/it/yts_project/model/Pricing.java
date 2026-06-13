package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 价格信息模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pricing {
    
    /**
     * 价格ID
     */
    private Integer id;
    
    /**
     * 产品ID
     */
    private Integer projectId;
    
    /**
     * 价格
     */
    private BigDecimal price;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}