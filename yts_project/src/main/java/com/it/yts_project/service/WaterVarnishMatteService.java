package com.it.yts_project.service;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.WaterVarnishMatteProductDTO;
import com.it.yts_project.model.WaterVarnishMatteProduct;
import com.it.yts_project.model.WaterVarnishMatteCompatibility;

import java.util.List;

/**
 * 水油(哑光) Service接口
 */
public interface WaterVarnishMatteService {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<WaterVarnishMatteProduct> getAllProducts();

    /**
     * 根据ID查询产品
     */
    WaterVarnishMatteProduct getProductById(Integer id);

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
     * 根据ID查询兼容性
     */
    WaterVarnishMatteCompatibility getCompatibilityById(Integer id);

    /**
     * 保存兼容性（新增或更新）
     */
    WaterVarnishMatteCompatibility saveCompatibility(WaterVarnishMatteCompatibility compatibility);

    /**
     * 删除兼容性
     */
    void deleteCompatibility(Integer id);

    /**
     * 批量保存兼容性（用于矩阵导入）
     */
    void batchSaveCompatibilities(List<WaterVarnishMatteCompatibility> compatibilities);

    // ========== 匹配查询 ==========

    /**
     * 搜索产品（关键词 + 工序筛选 + 分页）
     *
     * @param keyword  搜索关键词
     * @param stepName 后加工工序名称（可选）
     * @param page     当前页码（从1开始）
     * @param size     每页条数
     * @return 分页结果
     */
    PagedResult<WaterVarnishMatteProduct> searchProducts(String keyword, String stepName, int page, int size);

    /**
     * 获取所有后加工工序步骤名称（去重）
     */
    List<String> getDistinctSteps();

    /**
     * 获取产品详情（含兼容性列表）
     *
     * @param id 产品ID
     * @return 产品详情DTO，不存在返回null
     */
    WaterVarnishMatteProductDTO getProductDetail(Integer id);
}
