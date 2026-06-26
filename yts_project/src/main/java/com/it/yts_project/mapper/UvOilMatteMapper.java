package com.it.yts_project.mapper;

import com.it.yts_project.dto.UvOilMatteProductDTO;
import com.it.yts_project.model.UvOilMatteProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * UV油_哑光UV油 Mapper接口
 */
@Mapper
public interface UvOilMatteMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<UvOilMatteProduct> findAllProducts();

    /**
     * 根据ID查询产品
     */
    UvOilMatteProduct findProductById(@Param("id") Integer id);

    /**
     * 搜索产品
     */
    List<UvOilMatteProduct> searchProducts(@Param("keyword") String keyword);

    /**
     * 插入产品
     */
    int insertProduct(UvOilMatteProduct product);

    /**
     * 更新产品
     */
    int updateProduct(UvOilMatteProduct product);

    /**
     * 删除产品
     */
    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<com.it.yts_project.model.UvOilMatteCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 根据ID查询兼容性
     */
    com.it.yts_project.model.UvOilMatteCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 插入兼容性
     */
    int insertCompatibility(com.it.yts_project.model.UvOilMatteCompatibility compatibility);

    /**
     * 更新兼容性
     */
    int updateCompatibility(com.it.yts_project.model.UvOilMatteCompatibility compatibility);

    /**
     * 删除兼容性
     */
    int deleteCompatibilityById(@Param("id") Integer id);

    /**
     * 根据产品ID删除所有兼容性
     */
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 批量插入兼容性
     */
    int batchInsertCompatibilities(List<com.it.yts_project.model.UvOilMatteCompatibility> compatibilities);

    /**
     * 查询兼容性是否存在（按唯一键）
     */
    com.it.yts_project.model.UvOilMatteCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    // ========== 匹配查询 ==========

    /**
     * 搜索产品（可选工序筛选 + 分页），返回含兼容性状态的DTO
     */
    List<UvOilMatteProductDTO> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 统计产品数量（可选工序筛选）
     */
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);

    /**
     * 获取所有后加工工序步骤名称（去重）
     */
    List<String> getDistinctPostProcessingSteps();

    /**
     * 获取去重的 (step_category, post_processing_step) 对，用于前端两级选择
     */
    List<Map<String, Object>> getDistinctStepsWithCategory();

    // ========== 多步骤匹配查询 ==========

    /**
     * 多步骤匹配：查找兼容 ALL 指定步骤的产品 ID（分页）
     */
    List<Integer> searchMultiStepProductIds(
            @Param("keyword") String keyword,
            @Param("steps") List<String> steps,
            @Param("stepCount") Integer stepCount,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 多步骤匹配：统计兼容 ALL 指定步骤的产品数
     */
    Long countMultiStepProducts(
            @Param("keyword") String keyword,
            @Param("steps") List<String> steps,
            @Param("stepCount") Integer stepCount);

    /**
     * 根据 ID 列表批量查询产品
     */
    List<UvOilMatteProduct> findProductsByIds(@Param("ids") List<Integer> ids);

    /**
     * 查询指定产品在指定步骤下的兼容性状态
     */
    List<Map<String, Object>> findCompatibilityStatusForProducts(
            @Param("ids") List<Integer> ids,
            @Param("steps") List<String> steps);
}
