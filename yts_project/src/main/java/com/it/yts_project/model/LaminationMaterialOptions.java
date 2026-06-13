package com.it.yts_project.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 过胶材质选项模型
 */
@Data
public class LaminationMaterialOptions {
    private Integer id;
    private String materialName;
    private String materialCode;
    private Integer displayOrder;
    private Boolean isActive;
    private String description;
    private Integer pid; // 父级ID，用于层级结构
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
