package com.it.yts_project.service;

import com.it.yts_project.model.silicone_coarse_uvCompatibility;
import com.it.yts_project.model.silicone_coarse_uvProduct;

import java.util.List;

/**
 * 硅胶粗纹UV Service接口
 */
public interface silicone_coarse_uvService {

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    List<silicone_coarse_uvProduct> getAllProducts();

    /**
     * 获取激活的产品
     */
    List<silicone_coarse_uvProduct> getActiveProducts();

    /**
     * 根据ID获取产品
     */
    silicone_coarse_uvProduct getProductById(Integer id);

    /**
     * 搜索产品
     */
    List<silicone_coarse_uvProduct> searchProducts(String keyword);

    /**
     * 保存产品（新增或更新）
     */
    silicone_coarse_uvProduct saveProduct(silicone_coarse_uvProduct product);

    /**
     * 删除产品
     */
    void deleteProduct(Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID获取兼容性列表
     */
    List<silicone_coarse_uvCompatibility> getCompatibilitiesByProductId(Integer productId);

    /**
     * 获取所有兼容性
     */
    List<silicone_coarse_uvCompatibility> getAllCompatibilities();

    /**
     * 根据ID获取兼容性
     */
    silicone_coarse_uvCompatibility getCompatibilityById(Integer id);

    /**
     * 保存兼容性（新增或更新）
     */
    silicone_coarse_uvCompatibility saveCompatibility(silicone_coarse_uvCompatibility compatibility);

    /**
     * 删除兼容性
     */
    void deleteCompatibility(Integer id);

    /**
     * 批量保存兼容性
     */
    void batchSaveCompatibilities(List<silicone_coarse_uvCompatibility> compatibilities);

    /**
     * 获取所有后加工工序名称
     */
    List<String> getAllPostProcessingSteps();

    /**
     * 获取所有产品名称
     */
    List<String> getAllProductNames();
}
