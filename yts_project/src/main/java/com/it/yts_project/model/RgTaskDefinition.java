package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 任务定义实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RgTaskDefinition {
    private Integer id;
    private String taskCode;
    private String taskName;
    private String description;
    private Integer sortOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

