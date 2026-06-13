package com.it.yts_project.service;

import com.it.yts_project.model.ReferenceWritingFunction;

import java.util.List;

/**
 * 表面写字功能字典表 Service 接口
 */
public interface ReferenceWritingFunctionService {

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
    ReferenceWritingFunction findById(Integer id);

    /**
     * 根据分类查询
     * @param category 笔类型
     * @return 写字功能字典列表
     */
    List<ReferenceWritingFunction> findByCategory(String category);

    /**
     * 创建
     * @param writingFunction 写字功能字典
     * @return 创建的写字功能字典
     */
    ReferenceWritingFunction create(ReferenceWritingFunction writingFunction);

    /**
     * 更新
     * @param writingFunction 写字功能字典
     * @return 更新的写字功能字典
     */
    ReferenceWritingFunction update(ReferenceWritingFunction writingFunction);

    /**
     * 删除
     * @param id ID
     * @return 是否成功
     */
    boolean delete(Integer id);
}
