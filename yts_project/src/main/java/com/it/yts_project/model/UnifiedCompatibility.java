package com.it.yts_project.model;

import java.time.LocalDateTime;

/**
 * 统一兼容性记录 — 所有 P0 物料模块收敛到此模型。
 */
public class UnifiedCompatibility {
    private Long id;
    private String materialCode;
    private String materialName;
    private String moduleType;
    private String processOperation;
    private String compatibilityStatus;
    private String conditionDesc;
    private String sourceTable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaterialCode() { return materialCode; }
    public void setMaterialCode(String materialCode) { this.materialCode = materialCode; }

    public String getMaterialName() { return materialName; }
    public void setMaterialName(String materialName) { this.materialName = materialName; }

    public String getModuleType() { return moduleType; }
    public void setModuleType(String moduleType) { this.moduleType = moduleType; }

    public String getProcessOperation() { return processOperation; }
    public void setProcessOperation(String processOperation) { this.processOperation = processOperation; }

    public String getCompatibilityStatus() { return compatibilityStatus; }
    public void setCompatibilityStatus(String compatibilityStatus) { this.compatibilityStatus = compatibilityStatus; }

    public String getConditionDesc() { return conditionDesc; }
    public void setConditionDesc(String conditionDesc) { this.conditionDesc = conditionDesc; }

    public String getSourceTable() { return sourceTable; }
    public void setSourceTable(String sourceTable) { this.sourceTable = sourceTable; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
