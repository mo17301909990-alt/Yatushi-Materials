package com.it.yts_project.mapper;

import com.it.yts_project.model.RgResourceGroupDetail;
import org.apache.ibatis.annotations.*;

/**
 * 资源组技术详情Mapper
 */
@Mapper
public interface RgResourceGroupDetailMapper {

    @Select("SELECT id, resource_group_id, " +
            "max_width_mm, max_height_mm, min_width_mm, min_height_mm, " +
            "min_thickness_mm, max_thickness_mm, min_gsm, max_gsm, " +
            "min_speed_sheet_per_hour, max_speed_sheet_per_hour, design_speed_sheet_per_hour, " +
            "setup_time_min, changeover_time_min, " +
            "machine_model, manufacturer, year_of_make, capability_remark, " +
            "extra_params::text, created_at, updated_at " +
            "FROM rg_resource_group_detail WHERE resource_group_id = #{resourceGroupId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "resourceGroupId", column = "resource_group_id"),
            @Result(property = "maxWidthMm", column = "max_width_mm"),
            @Result(property = "maxHeightMm", column = "max_height_mm"),
            @Result(property = "minWidthMm", column = "min_width_mm"),
            @Result(property = "minHeightMm", column = "min_height_mm"),
            @Result(property = "minThicknessMm", column = "min_thickness_mm"),
            @Result(property = "maxThicknessMm", column = "max_thickness_mm"),
            @Result(property = "minGsm", column = "min_gsm"),
            @Result(property = "maxGsm", column = "max_gsm"),
            @Result(property = "minSpeedSheetPerHour", column = "min_speed_sheet_per_hour"),
            @Result(property = "maxSpeedSheetPerHour", column = "max_speed_sheet_per_hour"),
            @Result(property = "designSpeedSheetPerHour", column = "design_speed_sheet_per_hour"),
            @Result(property = "setupTimeMin", column = "setup_time_min"),
            @Result(property = "changeoverTimeMin", column = "changeover_time_min"),
            @Result(property = "machineModel", column = "machine_model"),
            @Result(property = "manufacturer", column = "manufacturer"),
            @Result(property = "yearOfMake", column = "year_of_make"),
            @Result(property = "capabilityRemark", column = "capability_remark"),
            @Result(property = "extraParams", column = "extra_params"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    RgResourceGroupDetail findByResourceGroupId(@Param("resourceGroupId") Integer resourceGroupId);
}

