package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 资源组实体类
 * 对应表 rg_resource_group
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RgResourceGroup {
    private Integer id;
    private String resourceGroupCode;  // 资源组编码
    private String name;               // 资源组名称
    private String workCenterCode;     // 工作中心编码
    private String workCenterName;     // 工作中心名称
    private String family;             // 家族（如 贺咭/拼图）
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
