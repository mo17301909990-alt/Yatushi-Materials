package com.it.yts_project.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 资源组技术参数详情表实体
 */
public class RgResourceGroupDetail {
    private Integer id;
    private Integer resourceGroupId;

    // 尺寸能力
    private BigDecimal maxWidthMm;       // 最大宽度（mm）
    private BigDecimal maxHeightMm;      // 最大高度（mm）
    private BigDecimal minWidthMm;       // 最小宽度（mm）
    private BigDecimal minHeightMm;      // 最小高度（mm）

    // 厚度 / 克重能力
    private BigDecimal minThicknessMm;   // 最小厚度（mm）
    private BigDecimal maxThicknessMm;   // 最大厚度（mm）
    private BigDecimal minGsm;           // 最小克重（g/m2）
    private BigDecimal maxGsm;           // 最大克重（g/m2）

    // 速度与设备参数
    private BigDecimal minSpeedSheetPerHour;    // 最低生产速度（张/小时）
    private BigDecimal maxSpeedSheetPerHour;    // 最高生产速度（张/小时）
    private BigDecimal designSpeedSheetPerHour; // 设计极限速度（张/小时）

    private BigDecimal setupTimeMin;       // 标准换单/上机准备时间（分钟）
    private BigDecimal changeoverTimeMin;  // 机种切换时间（分钟）

    // 其他技术说明
    private String machineModel;          // 机型型号
    private String manufacturer;          // 设备厂家
    private String yearOfMake;            // 设备年份
    private String capabilityRemark;      // 能力说明

    // 预留扩展 JSONB
    private String extraParams;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getResourceGroupId() { return resourceGroupId; }
    public void setResourceGroupId(Integer resourceGroupId) { this.resourceGroupId = resourceGroupId; }

    public BigDecimal getMaxWidthMm() { return maxWidthMm; }
    public void setMaxWidthMm(BigDecimal maxWidthMm) { this.maxWidthMm = maxWidthMm; }

    public BigDecimal getMaxHeightMm() { return maxHeightMm; }
    public void setMaxHeightMm(BigDecimal maxHeightMm) { this.maxHeightMm = maxHeightMm; }

    public BigDecimal getMinWidthMm() { return minWidthMm; }
    public void setMinWidthMm(BigDecimal minWidthMm) { this.minWidthMm = minWidthMm; }

    public BigDecimal getMinHeightMm() { return minHeightMm; }
    public void setMinHeightMm(BigDecimal minHeightMm) { this.minHeightMm = minHeightMm; }

    public BigDecimal getMinThicknessMm() { return minThicknessMm; }
    public void setMinThicknessMm(BigDecimal minThicknessMm) { this.minThicknessMm = minThicknessMm; }

    public BigDecimal getMaxThicknessMm() { return maxThicknessMm; }
    public void setMaxThicknessMm(BigDecimal maxThicknessMm) { this.maxThicknessMm = maxThicknessMm; }

    public BigDecimal getMinGsm() { return minGsm; }
    public void setMinGsm(BigDecimal minGsm) { this.minGsm = minGsm; }

    public BigDecimal getMaxGsm() { return maxGsm; }
    public void setMaxGsm(BigDecimal maxGsm) { this.maxGsm = maxGsm; }

    public BigDecimal getMinSpeedSheetPerHour() { return minSpeedSheetPerHour; }
    public void setMinSpeedSheetPerHour(BigDecimal minSpeedSheetPerHour) { this.minSpeedSheetPerHour = minSpeedSheetPerHour; }

    public BigDecimal getMaxSpeedSheetPerHour() { return maxSpeedSheetPerHour; }
    public void setMaxSpeedSheetPerHour(BigDecimal maxSpeedSheetPerHour) { this.maxSpeedSheetPerHour = maxSpeedSheetPerHour; }

    public BigDecimal getDesignSpeedSheetPerHour() { return designSpeedSheetPerHour; }
    public void setDesignSpeedSheetPerHour(BigDecimal designSpeedSheetPerHour) { this.designSpeedSheetPerHour = designSpeedSheetPerHour; }

    public BigDecimal getSetupTimeMin() { return setupTimeMin; }
    public void setSetupTimeMin(BigDecimal setupTimeMin) { this.setupTimeMin = setupTimeMin; }

    public BigDecimal getChangeoverTimeMin() { return changeoverTimeMin; }
    public void setChangeoverTimeMin(BigDecimal changeoverTimeMin) { this.changeoverTimeMin = changeoverTimeMin; }

    public String getMachineModel() { return machineModel; }
    public void setMachineModel(String machineModel) { this.machineModel = machineModel; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getYearOfMake() { return yearOfMake; }
    public void setYearOfMake(String yearOfMake) { this.yearOfMake = yearOfMake; }

    public String getCapabilityRemark() { return capabilityRemark; }
    public void setCapabilityRemark(String capabilityRemark) { this.capabilityRemark = capabilityRemark; }

    public String getExtraParams() { return extraParams; }
    public void setExtraParams(String extraParams) { this.extraParams = extraParams; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

