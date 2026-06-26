package com.it.yts_project.service;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.StepCategoryGroup;
import com.it.yts_project.dto.UvOilMatteProductDTO;
import com.it.yts_project.model.UvOilMatteProduct;
import com.it.yts_project.model.UvOilMatteCompatibility;

import java.util.List;
import java.util.Map;

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

    // ========== 匹配查询 ==========

    /**
     * 搜索产品（关键词 + 单工序筛选 + 分页）
     *
     * @param keyword  搜索关键词
     * @param stepName 后加工工序名称（可选）
     * @param page     当前页码（从1开始）
     * @param size     每页条数
     * @return 分页结果（含兼容性状态）
     */
    PagedResult<UvOilMatteProductDTO> searchProducts(String keyword, String stepName, int page, int size);

    /**
     * 搜索产品（关键词 + 多步骤 INTERSECT 筛选 + 分页）
     *
     * @param keyword  搜索关键词
     * @param steps    后加工工序步骤列表（必须全部兼容）
     * @param page     当前页码（从1开始）
     * @param size     每页条数
     * @return 分页结果（含每步骤兼容性状态映射）
     */
    PagedResult<UvOilMatteProductDTO> searchProductsMultiStep(String keyword, List<String> steps, int page, int size);

    /**
     * 获取所有后加工工序步骤名称（去重），按大类分组
     *
     * @return 按大类分组的步骤列表，每项包含 category（大类）和 steps（该大类下的具体步骤）
     */
    List<StepCategoryGroup> getDistinctSteps();

    /**
     * 获取后加工工序步骤（两级结构），直接从数据库按 step_category 分组
     *
     * @return 按大类分组的步骤列表，每项包含 category 和 steps 字段
     */
    List<Map<String, Object>> getSteps();

    /**
     * 获取产品详情（含兼容性列表）
     *
     * @param id 产品ID
     * @return 产品详情DTO，不存在返回null
     */
    UvOilMatteProductDTO getProductDetail(Integer id);
}
