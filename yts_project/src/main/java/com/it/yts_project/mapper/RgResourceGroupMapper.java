package com.it.yts_project.mapper;

import com.it.yts_project.model.RgResourceGroup;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 资源组Mapper
 */
@Mapper
public interface RgResourceGroupMapper {
    
    @Select("SELECT id, resource_group_code, name, work_center_code, work_center_name, " +
            "family, is_active, created_at, updated_at " +
            "FROM rg_resource_group WHERE is_active = true ORDER BY work_center_code, resource_group_code")
    @Results(id = "resourceGroupResultMap", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "resourceGroupCode", column = "resource_group_code"),
        @Result(property = "name", column = "name"),
        @Result(property = "workCenterCode", column = "work_center_code"),
        @Result(property = "workCenterName", column = "work_center_name"),
        @Result(property = "family", column = "family"),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<RgResourceGroup> findAll();
    
    @Select("SELECT id, resource_group_code, name, work_center_code, work_center_name, " +
            "family, is_active, created_at, updated_at " +
            "FROM rg_resource_group WHERE id = #{id}")
    @ResultMap("resourceGroupResultMap")
    RgResourceGroup findById(Integer id);
    
    @Select("""
        SELECT rg.id, rg.resource_group_code, rg.name, rg.work_center_code, rg.work_center_name,
               rg.family, rg.is_active, rg.created_at, rg.updated_at
        FROM rg_resource_group rg
        JOIN rg_resource_group_task_map tm ON rg.id = tm.resource_group_id
        WHERE tm.task_code = #{taskCode}
          AND rg.is_active = true
        ORDER BY rg.work_center_code, rg.resource_group_code
    """)
    @ResultMap("resourceGroupResultMap")
    List<RgResourceGroup> findByTaskCode(String taskCode);
    
    @Select("SELECT id, resource_group_code, name, work_center_code, work_center_name, " +
            "family, is_active, created_at, updated_at " +
            "FROM rg_resource_group " +
            "WHERE work_center_code = #{workCenterCode} AND is_active = true " +
            "ORDER BY resource_group_code")
    @ResultMap("resourceGroupResultMap")
    List<RgResourceGroup> findByWorkCenterCode(@Param("workCenterCode") String workCenterCode);
}
