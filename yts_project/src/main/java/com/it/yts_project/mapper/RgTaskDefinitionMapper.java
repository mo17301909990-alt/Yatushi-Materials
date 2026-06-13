package com.it.yts_project.mapper;

import com.it.yts_project.model.RgTaskDefinition;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 任务定义Mapper
 */
@Mapper
public interface RgTaskDefinitionMapper {
    
    @Select("SELECT * FROM rg_task_definition WHERE is_active = true ORDER BY sort_order")
    @Results({
        @Result(property = "taskCode", column = "task_code"),
        @Result(property = "taskName", column = "task_name"),
        @Result(property = "sortOrder", column = "sort_order"),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    List<RgTaskDefinition> findAll();
    
    @Select("SELECT * FROM rg_task_definition WHERE task_code = #{taskCode}")
    @Results({
        @Result(property = "taskCode", column = "task_code"),
        @Result(property = "taskName", column = "task_name"),
        @Result(property = "sortOrder", column = "sort_order"),
        @Result(property = "isActive", column = "is_active"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at")
    })
    RgTaskDefinition findByTaskCode(String taskCode);
}

