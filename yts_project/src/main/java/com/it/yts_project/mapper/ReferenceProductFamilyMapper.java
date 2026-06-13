package com.it.yts_project.mapper;

import com.it.yts_project.model.ReferenceProductFamily;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品家族字典表 Mapper 接口
 */
@Mapper
public interface ReferenceProductFamilyMapper {

    /**
     * 查询所有产品家族
     */
    List<ReferenceProductFamily> findAll();

    /**
     * 根据ID查询
     */
    ReferenceProductFamily findById(@Param("id") Integer id);

    /**
     * 根据大类查询
     */
    List<ReferenceProductFamily> findByCategory(@Param("category") String category);

    /**
     * 查询所有不重复的大类列表
     */
    List<String> findDistinctCategories();

    /**
     * 查询所有不重复的子类列表（按大类过滤可选）
     */
    List<String> findDistinctSubCategories(@Param("category") String category);

    /**
     * 插入
     */
    int insert(ReferenceProductFamily entity);

    /**
     * 更新
     */
    int update(ReferenceProductFamily entity);

    /**
     * 根据ID删除
     */
    int deleteById(@Param("id") Integer id);
}
