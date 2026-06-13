package com.it.yts_project.service;

import com.it.yts_project.model.ProductTypeOptions;

import java.util.List;

/**
 * 产品类型选项服务接口
 */
public interface ProductTypeOptionsService {
    
    /**
     * 获取所有激活的产品类型选项
     * @return 产品类型选项列表
     */
    List<ProductTypeOptions> getAllActiveOptions();
    
    /**
     * 根据ID获取产品类型选项
     * @param id 主键ID
     * @return 产品类型选项
     */
    ProductTypeOptions getById(Integer id);
    
    /**
     * 获取所有产品类型选项（包括非激活的）
     * @return 产品类型选项列表
     */
    List<ProductTypeOptions> getAllOptions();
    
    /**
     * 保存产品类型选项（新增或更新）
     * @param productType 产品类型选项
     * @return 保存后的产品类型选项
     */
    ProductTypeOptions save(ProductTypeOptions productType);
    
    /**
     * 根据ID删除产品类型选项
     * @param id 主键ID
     */
    void deleteById(Integer id);
}
