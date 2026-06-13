package com.it.yts_project.mapper;

import com.it.yts_project.model.RgResourceGroupCapacity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 资源组产能Mapper
 */
@Mapper
public interface RgResourceGroupCapacityMapper {

    @Select("SELECT id, resource_group_id, capacity_mode, shift_name, " +
            "work_hours_per_shift, shifts_per_day, " +
            "capacity_sheet_per_hour, capacity_sqm_per_hour, " +
            "capacity_sheet_per_day, capacity_sqm_per_day, " +
            "utilization_rate, effective_from, effective_to, remark, " +
            "created_at, updated_at " +
            "FROM rg_resource_group_capacity " +
            "WHERE resource_group_id = #{resourceGroupId} " +
            "ORDER BY capacity_mode, shift_name")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "resourceGroupId", column = "resource_group_id"),
            @Result(property = "capacityMode", column = "capacity_mode"),
            @Result(property = "shiftName", column = "shift_name"),
            @Result(property = "workHoursPerShift", column = "work_hours_per_shift"),
            @Result(property = "shiftsPerDay", column = "shifts_per_day"),
            @Result(property = "capacitySheetPerHour", column = "capacity_sheet_per_hour"),
            @Result(property = "capacitySqmPerHour", column = "capacity_sqm_per_hour"),
            @Result(property = "capacitySheetPerDay", column = "capacity_sheet_per_day"),
            @Result(property = "capacitySqmPerDay", column = "capacity_sqm_per_day"),
            @Result(property = "utilizationRate", column = "utilization_rate"),
            @Result(property = "effectiveFrom", column = "effective_from"),
            @Result(property = "effectiveTo", column = "effective_to"),
            @Result(property = "remark", column = "remark"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    List<RgResourceGroupCapacity> findByResourceGroupId(@Param("resourceGroupId") Integer resourceGroupId);
}

