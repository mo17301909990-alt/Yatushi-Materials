package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 资源组-任务映射实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RgResourceGroupTaskMap {
    private Integer id;
    private Integer resourceGroupId;
    private String taskCode;
    private String supportType;  // YES / CONDITIONAL
    private String remark;
    private LocalDateTime createdAt;
}

