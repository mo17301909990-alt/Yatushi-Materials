package com.it.yts_project.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资源组产能参数表实体
 */
public class RgResourceGroupCapacity {
    private Integer id;
    private Integer resourceGroupId;

    private String capacityMode;         // 产能模式：STANDARD/OT/WEEKDAY/WEEKEND
    private String shiftName;            // 班次名称：白班/夜班
    private BigDecimal workHoursPerShift;   // 每班工作小时数
    private BigDecimal shiftsPerDay;        // 每天班次数

    // 产能
    private BigDecimal capacitySheetPerHour;  // 标准产能：张/小时
    private BigDecimal capacitySqmPerHour;    // 标准产能：平方米/小时
    private BigDecimal capacitySheetPerDay;   // 折算日产能：张/天
    private BigDecimal capacitySqmPerDay;     // 折算日产能：平方米/天

    private BigDecimal utilizationRate;       // 稼动率/利用率（%）

    private LocalDate effectiveFrom;          // 生效起始日期
    private LocalDate effectiveTo;            // 生效结束日期

    private String remark;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getResourceGroupId() { return resourceGroupId; }
    public void setResourceGroupId(Integer resourceGroupId) { this.resourceGroupId = resourceGroupId; }

    public String getCapacityMode() { return capacityMode; }
    public void setCapacityMode(String capacityMode) { this.capacityMode = capacityMode; }

    public String getShiftName() { return shiftName; }
    public void setShiftName(String shiftName) { this.shiftName = shiftName; }

    public BigDecimal getWorkHoursPerShift() { return workHoursPerShift; }
    public void setWorkHoursPerShift(BigDecimal workHoursPerShift) { this.workHoursPerShift = workHoursPerShift; }

    public BigDecimal getShiftsPerDay() { return shiftsPerDay; }
    public void setShiftsPerDay(BigDecimal shiftsPerDay) { this.shiftsPerDay = shiftsPerDay; }

    public BigDecimal getCapacitySheetPerHour() { return capacitySheetPerHour; }
    public void setCapacitySheetPerHour(BigDecimal capacitySheetPerHour) { this.capacitySheetPerHour = capacitySheetPerHour; }

    public BigDecimal getCapacitySqmPerHour() { return capacitySqmPerHour; }
    public void setCapacitySqmPerHour(BigDecimal capacitySqmPerHour) { this.capacitySqmPerHour = capacitySqmPerHour; }

    public BigDecimal getCapacitySheetPerDay() { return capacitySheetPerDay; }
    public void setCapacitySheetPerDay(BigDecimal capacitySheetPerDay) { this.capacitySheetPerDay = capacitySheetPerDay; }

    public BigDecimal getCapacitySqmPerDay() { return capacitySqmPerDay; }
    public void setCapacitySqmPerDay(BigDecimal capacitySqmPerDay) { this.capacitySqmPerDay = capacitySqmPerDay; }

    public BigDecimal getUtilizationRate() { return utilizationRate; }
    public void setUtilizationRate(BigDecimal utilizationRate) { this.utilizationRate = utilizationRate; }

    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

