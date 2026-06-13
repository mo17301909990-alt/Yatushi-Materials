package com.it.yts_project.service;

import com.it.yts_project.model.YaguangUvOilProduct;
import com.it.yts_project.model.YaguangUvOilCompatibility;

import java.util.List;

/**
 * 哑光UV油 Service接口
 */
public interface YaguangUvOilService {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<YaguangUvOilProduct> getAllProducts();

    /**
     * 根据ID查询产品
     */
    YaguangUvOilProduct getProductById(Integer id);

    /**
     * 搜索产品
     */
    List<YaguangUvOilProduct> searchProducts(String keyword);

    /**
     * 保存产品（新增或更新）
     */
    YaguangUvOilProduct saveProduct(YaguangUvOilProduct product);

    /**
     * 删除产品
     */
    void deleteProduct(Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<YaguangUvOilCompatibility> getCompatibilitiesByProductId(Integer productId);

    /**
     * 根据ID查询兼容性
     */
    YaguangUvOilCompatibility getCompatibilityById(Integer id);

    /**
     * 保存兼容性（新增或更新）
     */
    YaguangUvOilCompatibility saveCompatibility(YaguangUvOilCompatibility compatibility);

    /**
     * 删除兼容性
     */
    void deleteCompatibility(Integer id);

    /**
     * 批量保存兼容性（用于矩阵导入）
     */
    void batchSaveCompatibilities(List<YaguangUvOilCompatibility> compatibilities);
}
