package com.it.yts_project.mapper;

import com.it.yts_project.model.RgWorkCenter;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 工作中心Mapper
 */
@Mapper
public interface RgWorkCenterMapper {

    @Select("SELECT id, work_center_code, work_center_name, department_name, " +
            "description, remark, is_active, created_at, updated_at " +
            "FROM rg_work_center WHERE is_active = true ORDER BY work_center_code")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "workCenterCode", column = "work_center_code"),
            @Result(property = "workCenterName", column = "work_center_name"),
            @Result(property = "departmentName", column = "department_name"),
            @Result(property = "description", column = "description"),
            @Result(property = "remark", column = "remark"),
            @Result(property = "isActive", column = "is_active"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    List<RgWorkCenter> findAll();

    @Select("SELECT id, work_center_code, work_center_name, department_name, " +
            "description, remark, is_active, created_at, updated_at " +
            "FROM rg_work_center WHERE work_center_code = #{workCenterCode}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "workCenterCode", column = "work_center_code"),
            @Result(property = "workCenterName", column = "work_center_name"),
            @Result(property = "departmentName", column = "department_name"),
            @Result(property = "description", column = "description"),
            @Result(property = "remark", column = "remark"),
            @Result(property = "isActive", column = "is_active"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    RgWorkCenter findByCode(@Param("workCenterCode") String workCenterCode);
}

