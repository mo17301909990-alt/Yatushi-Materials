package com.it.yts_project.service;

import com.it.yts_project.model.SiliconeWrinkleUvCompatibility;
import com.it.yts_project.model.SiliconeWrinkleUvProduct;

import java.util.List;

/**
 * 硅胶_皱纹UV Service接口
 */
public interface SiliconeWrinkleUvService {

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    List<SiliconeWrinkleUvProduct> getAllProducts();

    /**
     * 获取启用的产品
     */
    List<SiliconeWrinkleUvProduct> getActiveProducts();

    /**
     * 根据ID获取产品
     */
    SiliconeWrinkleUvProduct getProductById(Integer id);

    /**
     * 搜索产品
     */
    List<SiliconeWrinkleUvProduct> searchProducts(String keyword);

    /**
     * 保存产品（新增或更新）
     */
    SiliconeWrinkleUvProduct saveProduct(SiliconeWrinkleUvProduct product);

    /**
     * 删除产品
     */
    void deleteProduct(Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID获取兼容性列表
     */
    List<SiliconeWrinkleUvCompatibility> getCompatibilitiesByProductId(Integer productId);

    /**
     * 获取所有兼容性
     */
    List<SiliconeWrinkleUvCompatibility> getAllCompatibilities();

    /**
     * 根据ID获取兼容性
     */
    SiliconeWrinkleUvCompatibility getCompatibilityById(Integer id);

    /**
     * 保存兼容性（新增或更新）
     */
    SiliconeWrinkleUvCompatibility saveCompatibility(SiliconeWrinkleUvCompatibility compatibility);

    /**
     * 批量保存兼容性
     */
    void batchSaveCompatibilities(List<SiliconeWrinkleUvCompatibility> compatibilities);

    /**
     * 删除兼容性
     */
    void deleteCompatibility(Integer id);

    /**
     * 批量删除兼容性
     */
    void batchDeleteCompatibilities(List<Integer> ids);
}
