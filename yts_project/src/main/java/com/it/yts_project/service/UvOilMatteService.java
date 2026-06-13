package com.it.yts_project.service;

import com.it.yts_project.model.UvOilMatteProduct;
import com.it.yts_project.model.UvOilMatteCompatibility;

import java.util.List;

/**
 * UV油_哑光UV油 Service接口
 */
public interface UvOilMatteService {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<UvOilMatteProduct> getAllProducts();

    /**
     * 根据ID查询产品
     */
    UvOilMatteProduct getProductById(Integer id);

    /**
     * 搜索产品
     */
    List<UvOilMatteProduct> searchProducts(String keyword);

    /**
     * 保存产品（新增或更新）
     */
    UvOilMatteProduct saveProduct(UvOilMatteProduct product);

    /**
     * 删除产品
     */
    void deleteProduct(Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<UvOilMatteCompatibility> getCompatibilitiesByProductId(Integer productId);

    /**
     * 根据ID查询兼容性
     */
    UvOilMatteCompatibility getCompatibilityById(Integer id);

    /**
     * 保存兼容性（新增或更新）
     */
    UvOilMatteCompatibility saveCompatibility(UvOilMatteCompatibility compatibility);

    /**
     * 删除兼容性
     */
    void deleteCompatibility(Integer id);

    /**
     * 批量保存兼容性（用于矩阵导入）
     */
    void batchSaveCompatibilities(List<UvOilMatteCompatibility> compatibilities);
}
