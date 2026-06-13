package com.it.yts_project.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 后加工步骤选项模型
 */
@Data
public class PostProcessingOptions {
    private Integer id;
    private String stepName;
    private String stepCode;
    private Integer displayOrder;
    private Boolean isActive;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
