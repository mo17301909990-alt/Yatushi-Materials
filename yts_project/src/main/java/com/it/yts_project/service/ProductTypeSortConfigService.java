package com.it.yts_project.service;

import com.it.yts_project.model.ProductTypeSortConfig;

import java.util.List;

/**
 * 产品类型特殊映射配置服务接口
 */
public interface ProductTypeSortConfigService {

    /**
     * 获取所有配置
     */
    List<ProductTypeSortConfig> getAll();

    /**
     * 根据ID获取配置
     */
    ProductTypeSortConfig getById(Integer id);

    /**
     * 根据产品类型ID获取配置
     */
    ProductTypeSortConfig getByProductTypeId(Integer productTypeId);

    /**
     * 获取所有激活的配置
     */
    List<ProductTypeSortConfig> getAllActive();

    /**
     * 保存配置（新增或更新）
     */
    ProductTypeSortConfig save(ProductTypeSortConfig config);

    /**
     * 删除配置
     */
    void deleteById(Integer id);
}
