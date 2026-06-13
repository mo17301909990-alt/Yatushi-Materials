package com.it.yts_project.service;

import com.it.yts_project.model.ReferencePaperSurface;

import java.util.List;

/**
 * 纸張面字典表 Service 接口
 */
public interface ReferencePaperSurfaceService {

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
    ReferencePaperSurface findById(Integer id);

    /**
     * 根据分类查询
     * @param category 大类
     * @return 纸张面字典列表
     */
    List<ReferencePaperSurface> findByCategory(String category);

    /**
     * 创建
     * @param paperSurface 纸张面字典
     * @return 创建的纸张面字典
     */
    ReferencePaperSurface create(ReferencePaperSurface paperSurface);

    /**
     * 更新
     * @param paperSurface 纸张面字典
     * @return 更新的纸张面字典
     */
    ReferencePaperSurface update(ReferencePaperSurface paperSurface);

    /**
     * 删除
     * @param id ID
     * @return 是否成功
     */
    boolean delete(Integer id);
}
