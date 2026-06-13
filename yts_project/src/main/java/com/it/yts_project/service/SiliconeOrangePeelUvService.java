package com.it.yts_project.service;

import com.it.yts_project.model.SiliconeOrangePeelUvProduct;
import com.it.yts_project.model.SiliconeOrangePeelUvCompatibility;

import java.util.List;

/**
 * 硅胶桔纹UV(Orange Peel UV) Service接口
 */
public interface SiliconeOrangePeelUvService {

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    List<SiliconeOrangePeelUvProduct> getAllProducts();

    /**
     * 获取激活的产品
     */
    List<SiliconeOrangePeelUvProduct> getActiveProducts();

    /**
     * 根据ID获取产品
     */
    SiliconeOrangePeelUvProduct getProductById(Integer id);

    /**
     * 搜索产品
     */
    List<SiliconeOrangePeelUvProduct> searchProducts(String keyword);

    /**
     * 保存产品（新增或更新）
     */
    SiliconeOrangePeelUvProduct saveProduct(SiliconeOrangePeelUvProduct product);

    /**
     * 删除产品
     */
    void deleteProduct(Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID获取兼容性列表
     */
    List<SiliconeOrangePeelUvCompatibility> getCompatibilitiesByProductId(Integer productId);

    /**
     * 获取所有兼容性列表
     */
    List<SiliconeOrangePeelUvCompatibility> getAllCompatibilities();

    /**
     * 根据ID获取兼容性
     */
    SiliconeOrangePeelUvCompatibility getCompatibilityById(Integer id);

    /**
     * 保存兼容性（新增或更新）
     */
    SiliconeOrangePeelUvCompatibility saveCompatibility(SiliconeOrangePeelUvCompatibility compatibility);

    /**
     * 批量保存兼容性
     */
    void batchSaveCompatibilities(List<SiliconeOrangePeelUvCompatibility> compatibilities);

    /**
     * 删除兼容性
     */
    void deleteCompatibility(Integer id);

    /**
     * 根据产品ID删除所有兼容性
     */
    void deleteCompatibilitiesByProductId(Integer productId);

    /**
     * 获取所有后加工工序名称
     */
    List<String> getAllPostProcessingSteps();
}
