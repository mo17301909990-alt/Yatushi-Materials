package com.it.yts_project.service;

import com.it.yts_project.model.ReferenceInkSurface;

import java.util.List;

/**
 * 印刷油墨面字典表 Service 接口
 */
public interface ReferenceInkSurfaceService {

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
    ReferenceInkSurface findById(Integer id);

    /**
     * 根据分类查询
     * @param category 分组
     * @return 油墨面字典列表
     */
    List<ReferenceInkSurface> findByCategory(String category);

    /**
     * 创建
     * @param inkSurface 油墨面字典
     * @return 创建的油墨面字典
     */
    ReferenceInkSurface create(ReferenceInkSurface inkSurface);

    /**
     * 更新
     * @param inkSurface 油墨面字典
     * @return 更新的油墨面字典
     */
    ReferenceInkSurface update(ReferenceInkSurface inkSurface);

    /**
     * 删除
     * @param id ID
     * @return 是否成功
     */
    boolean delete(Integer id);
}
