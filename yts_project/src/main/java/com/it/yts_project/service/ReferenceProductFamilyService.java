package com.it.yts_project.service;

import com.it.yts_project.model.ReferenceProductFamily;

import java.util.List;

/**
 * 产品家族字典表 Service 接口
 */
public interface ReferenceProductFamilyService {

    /**
     * 查询所有产品家族
     */
    List<ReferenceProductFamily> findAll();

    /**
     * 根据ID查询
     */
    ReferenceProductFamily findById(Integer id);

    /**
     * 根据大类查询
     */
    List<ReferenceProductFamily> findByCategory(String category);

    /**
     * 查询所有不重复的大类列表
     */
    List<String> findDistinctCategories();

    /**
     * 查询所有不重复的子类列表
     */
    List<String> findDistinctSubCategories(String category);

    /**
     * 创建
     */
    ReferenceProductFamily create(ReferenceProductFamily entity);

    /**
     * 更新
     */
    ReferenceProductFamily update(ReferenceProductFamily entity);

    /**
     * 删除
     */
    boolean delete(Integer id);
}
