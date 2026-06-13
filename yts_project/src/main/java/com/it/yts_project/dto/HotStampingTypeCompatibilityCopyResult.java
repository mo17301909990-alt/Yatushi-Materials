package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量复制常用界面映射的结果DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotStampingTypeCompatibilityCopyResult {
    /**
     * 成功复制的数量
     */
    private int successCount;
    
    /**
     * 失败的数量（通常是因为目标已存在相同的映射）
     */
    private int failCount;
    
    /**
     * 操作结果消息
     */
    private String message;
}

