package com.it.yts_project.mapper;

import com.it.yts_project.model.ReferenceWritingFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 表面写字功能字典表 Mapper 接口
 */
@Mapper
public interface ReferenceWritingFunctionMapper {

    /**
     * 查询所有写字功能字典
     * @return 写字功能字典列表
     */
    List<ReferenceWritingFunction> findAll();

    /**
     * 根据ID查询
     * @param id ID
     * @return 写字功能字典
     */
    ReferenceWritingFunction findById(@Param("id") Integer id);

    /**
     * 根据分类查询
     * @param category 笔类型
     * @return 写字功能字典列表
     */
    List<ReferenceWritingFunction> findByCategory(@Param("category") String category);

    /**
     * 插入
     * @param writingFunction 写字功能字典
     * @return 影响行数
     */
    int insert(ReferenceWritingFunction writingFunction);

    /**
     * 更新
     * @param writingFunction 写字功能字典
     * @return 影响行数
     */
    int update(ReferenceWritingFunction writingFunction);

    /**
     * 根据ID删除
     * @param id ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
}
