package com.it.yts_project.service;

import com.it.yts_project.model.WaterVarnishMatteCompatibility;
import com.it.yts_project.model.WaterVarnishMatteProduct;

import java.util.List;

/**
 * UV油_哑光水油 Service接口
 */
public interface WaterVarnishMatteService {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<WaterVarnishMatteProduct> getAllProducts();

    /**
     * 查询激活的产品
     */
    List<WaterVarnishMatteProduct> getActiveProducts();

    /**
     * 根据ID查询产品
     */
    WaterVarnishMatteProduct getProductById(Integer id);

    /**
     * 根据物料编码查询产品
     */
    WaterVarnishMatteProduct getProductByMaterialCode(String materialCode);

    /**
     * 搜索产品
     */
    List<WaterVarnishMatteProduct> searchProducts(String keyword);

    /**
     * 保存产品（新增或更新）
     */
    WaterVarnishMatteProduct saveProduct(WaterVarnishMatteProduct product);

    /**
     * 删除产品
     */
    void deleteProduct(Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<WaterVarnishMatteCompatibility> getCompatibilitiesByProductId(Integer productId);

    /**
     * 查询所有兼容性记录
     */
    List<WaterVarnishMatteCompatibility> getAllCompatibilities();

    /**
     * 根据ID查询兼容性
     */
    WaterVarnishMatteCompatibility getCompatibilityById(Integer id);

    /**
     * 保存兼容性（新增或更新）
     */
    WaterVarnishMatteCompatibility saveCompatibility(WaterVarnishMatteCompatibility compatibility);

    /**
     * 批量保存兼容性
     */
    void batchSaveCompatibility(List<WaterVarnishMatteCompatibility> compatibilities);

    /**
     * 删除兼容性
     */
    void deleteCompatibility(Integer id);

    /**
     * 根据产品ID删除兼容性
     */
    void deleteCompatibilityByProductId(Integer productId);

    /**
     * 获取所有后加工工序步骤名称
     */
    List<String> getAllPostProcessingSteps();
}
