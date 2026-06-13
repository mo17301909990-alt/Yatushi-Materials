package com.it.yts_project.service;

import com.it.yts_project.model.SiliconeGlowInkCompatibility;
import com.it.yts_project.model.SiliconeGlowInkProduct;

import java.util.List;

/**
 * 硅胶发光油墨Service接口
 */
public interface SiliconeGlowInkService {

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    List<SiliconeGlowInkProduct> getAllProducts();

    /**
     * 根据ID获取产品
     */
    SiliconeGlowInkProduct getProductById(Integer id);

    /**
     * 搜索产品
     */
    List<SiliconeGlowInkProduct> searchProducts(String keyword);

    /**
     * 保存产品（新增/更新）
     */
    SiliconeGlowInkProduct saveProduct(SiliconeGlowInkProduct product);

    /**
     * 删除产品
     */
    void deleteProduct(Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID获取兼容性列表
     */
    List<SiliconeGlowInkCompatibility> getCompatibilitiesByProductId(Integer productId);

    /**
     * 获取所有兼容性列表
     */
    List<SiliconeGlowInkCompatibility> getAllCompatibilities();

    /**
     * 根据ID获取兼容性
     */
    SiliconeGlowInkCompatibility getCompatibilityById(Integer id);

    /**
     * 保存兼容性（新增/更新）
     */
    SiliconeGlowInkCompatibility saveCompatibility(SiliconeGlowInkCompatibility compatibility);

    /**
     * 删除兼容性
     */
    void deleteCompatibility(Integer id);

    /**
     * 批量保存兼容性
     */
    void batchSaveCompatibilities(List<SiliconeGlowInkCompatibility> compatibilities);

    /**
     * 获取所有后加工工序名称
     */
    List<String> getAllPostProcessingSteps();
}
