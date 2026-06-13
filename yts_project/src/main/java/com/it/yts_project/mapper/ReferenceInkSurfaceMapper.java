package com.it.yts_project.mapper;

import com.it.yts_project.model.ReferenceInkSurface;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 印刷油墨面字典表 Mapper 接口
 */
@Mapper
public interface ReferenceInkSurfaceMapper {

    /**
     * 查询所有油墨面字典
     * @return 油墨面字典列表
     */
    List<ReferenceInkSurface> findAll();

    /**
     * 根据ID查询
     * @param id ID
     * @return 油墨面字典
     */
    ReferenceInkSurface findById(@Param("id") Integer id);

    /**
     * 根据分类查询
     * @param category 分组
     * @return 油墨面字典列表
     */
    List<ReferenceInkSurface> findByCategory(@Param("category") String category);

    /**
     * 插入
     * @param inkSurface 油墨面字典
     * @return 影响行数
     */
    int insert(ReferenceInkSurface inkSurface);

    /**
     * 更新
     * @param inkSurface 油墨面字典
     * @return 影响行数
     */
    int update(ReferenceInkSurface inkSurface);

    /**
     * 根据ID删除
     * @param id ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
}
