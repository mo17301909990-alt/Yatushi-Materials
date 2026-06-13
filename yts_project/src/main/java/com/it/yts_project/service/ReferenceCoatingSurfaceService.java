package com.it.yts_project.service;

import com.it.yts_project.model.ReferenceCoatingSurface;

import java.util.List;

/**
 * 後加工塗層面字典表 Service 接口
 */
public interface ReferenceCoatingSurfaceService {

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
    ReferenceCoatingSurface findById(Integer id);

    /**
     * 根据分类查询
     * @param category 工艺大类
     * @return 涂层面字典列表
     */
    List<ReferenceCoatingSurface> findByCategory(String category);

    /**
     * 关键字搜索
     * @param keyword 搜索关键字
     * @return 涂层面字典列表
     */
    List<ReferenceCoatingSurface> search(String keyword);

    /**
     * 创建
     * @param coatingSurface 涂层面字典
     * @return 创建的涂层面字典
     */
    ReferenceCoatingSurface create(ReferenceCoatingSurface coatingSurface);

    /**
     * 更新
     * @param coatingSurface 涂层面字典
     * @return 更新的涂层面字典
     */
    ReferenceCoatingSurface update(ReferenceCoatingSurface coatingSurface);

    /**
     * 删除
     * @param id ID
     * @return 是否成功
     */
    boolean delete(Integer id);
}
