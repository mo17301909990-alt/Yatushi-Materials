package com.it.yts_project.mapper;

import com.it.yts_project.model.ReferencePaperSurface;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 纸張面字典表 Mapper 接口
 */
@Mapper
public interface ReferencePaperSurfaceMapper {

    /**
     * 查询所有纸张面字典
     * @return 纸张面字典列表
     */
    List<ReferencePaperSurface> findAll();

    /**
     * 根据ID查询
     * @param id ID
     * @return 纸张面字典
     */
    ReferencePaperSurface findById(@Param("id") Integer id);

    /**
     * 根据分类查询
     * @param category 大类
     * @return 纸张面字典列表
     */
    List<ReferencePaperSurface> findByCategory(@Param("category") String category);

    /**
     * 插入
     * @param paperSurface 纸张面字典
     * @return 影响行数
     */
    int insert(ReferencePaperSurface paperSurface);

    /**
     * 更新
     * @param paperSurface 纸张面字典
     * @return 影响行数
     */
    int update(ReferencePaperSurface paperSurface);

    /**
     * 根据ID删除
     * @param id ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
}
