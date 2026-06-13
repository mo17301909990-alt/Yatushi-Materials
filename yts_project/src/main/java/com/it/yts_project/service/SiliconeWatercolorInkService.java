package com.it.yts_project.service;

import com.it.yts_project.model.SiliconeWatercolorInkProduct;
import com.it.yts_project.model.SiliconeWatercolorInkCompatibility;

import java.util.List;

/**
 * 硅胶水彩油墨Service接口
 */
public interface SiliconeWatercolorInkService {

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    List<SiliconeWatercolorInkProduct> getAllProducts();

    /**
     * 根据ID获取产品
     */
    SiliconeWatercolorInkProduct getProductById(Integer id);

    /**
     * 搜索产品
     */
    List<SiliconeWatercolorInkProduct> searchProducts(String keyword);

    /**
     * 保存产品（新增/更新）
     */
    SiliconeWatercolorInkProduct saveProduct(SiliconeWatercolorInkProduct product);

    /**
     * 删除产品
     */
    void deleteProduct(Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 获取所有兼容性列表
     */
    List<SiliconeWatercolorInkCompatibility> getAllCompatibilities();

    /**
     * 根据产品ID获取兼容性列表
     */
    List<SiliconeWatercolorInkCompatibility> getCompatibilitiesByProductId(Integer productId);

    /**
     * 根据ID获取兼容性
     */
    SiliconeWatercolorInkCompatibility getCompatibilityById(Integer id);

    /**
     * 保存兼容性（新增/更新）
     */
    SiliconeWatercolorInkCompatibility saveCompatibility(SiliconeWatercolorInkCompatibility compatibility);

    /**
     * 批量保存兼容性
     */
    void batchSaveCompatibility(List<SiliconeWatercolorInkCompatibility> compatibilities);

    /**
     * 删除兼容性
     */
    void deleteCompatibility(Integer id);
}
