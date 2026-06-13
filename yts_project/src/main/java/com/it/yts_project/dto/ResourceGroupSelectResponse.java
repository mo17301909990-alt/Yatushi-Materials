package com.it.yts_project.dto;

import lombok.Data;
import java.util.List;

/**
 * 资源组筛选响应DTO
 */
@Data
public class ResourceGroupSelectResponse {
    /**
     * 任务编码
     */
    private String taskCode;
    
    /**
     * 任务名称
     */
    private String taskName;
    
    /**
     * 候选资源组列表
     */
    private List<ResourceGroupCandidate> candidates;
    
    /**
     * 通过数量
     */
    private Integer passCount;
    
    /**
     * 不通过数量
     */
    private Integer failCount;
    
    /**
     * 无法判断数量
     */
    private Integer unknownCount;
}

