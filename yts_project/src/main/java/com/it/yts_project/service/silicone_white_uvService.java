package com.it.yts_project.service;

import com.it.yts_project.model.silicone_white_uvProduct;
import com.it.yts_project.model.silicone_white_uvCompatibility;

import java.util.List;

/**
 * 硅胶_白UV Service接口
 */
public interface silicone_white_uvService {

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    List<silicone_white_uvProduct> getAllProducts();

    /**
     * 获取激活的产品
     */
    List<silicone_white_uvProduct> getActiveProducts();

    /**
     * 根据ID获取产品
     */
    silicone_white_uvProduct getProductById(Integer id);

    /**
     * 搜索产品
     */
    List<silicone_white_uvProduct> searchProducts(String keyword);

    /**
     * 保存产品（新增/更新）
     */
    silicone_white_uvProduct saveProduct(silicone_white_uvProduct product);

    /**
     * 删除产品
     */
    void deleteProduct(Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID获取兼容性列表
     */
    List<silicone_white_uvCompatibility> getCompatibilitiesByProductId(Integer productId);

    /**
     * 获取所有兼容性
     */
    List<silicone_white_uvCompatibility> getAllCompatibilities();

    /**
     * 根据ID获取兼容性
     */
    silicone_white_uvCompatibility getCompatibilityById(Integer id);

    /**
     * 保存兼容性（新增/更新）
     */
    silicone_white_uvCompatibility saveCompatibility(silicone_white_uvCompatibility compatibility);

    /**
     * 批量保存兼容性
     */
    void batchSaveCompatibilities(List<silicone_white_uvCompatibility> compatibilities);

    /**
     * 删除兼容性
     */
    void deleteCompatibility(Integer id);

    /**
     * 根据产品ID删除兼容性
     */
    void deleteCompatibilitiesByProductId(Integer productId);
}
