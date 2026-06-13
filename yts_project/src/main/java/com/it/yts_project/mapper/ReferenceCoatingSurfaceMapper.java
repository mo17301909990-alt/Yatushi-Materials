package com.it.yts_project.mapper;

import com.it.yts_project.model.ReferenceCoatingSurface;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 後加工塗層面字典表 Mapper 接口
 */
@Mapper
public interface ReferenceCoatingSurfaceMapper {

    /**
     * 查询所有涂层面字典
     * @return 涂层面字典列表
     */
    List<ReferenceCoatingSurface> findAll();

    /**
     * 根据ID查询
     * @param id ID
     * @return 涂层面字典
     */
    ReferenceCoatingSurface findById(@Param("id") Integer id);

    /**
     * 根据分类查询
     * @param category 工艺大类
     * @return 涂层面字典列表
     */
    List<ReferenceCoatingSurface> findByCategory(@Param("category") String category);

    /**
     * 关键字搜索（匹配分类和详细名称）
     * @param keyword 搜索关键字
     * @return 涂层面字典列表
     */
    List<ReferenceCoatingSurface> search(@Param("keyword") String keyword);

    /**
     * 插入
     * @param coatingSurface 涂层面字典
     * @return 影响行数
     */
    int insert(ReferenceCoatingSurface coatingSurface);

    /**
     * 更新
     * @param coatingSurface 涂层面字典
     * @return 影响行数
     */
    int update(ReferenceCoatingSurface coatingSurface);

    /**
     * 根据ID删除
     * @param id ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
}
