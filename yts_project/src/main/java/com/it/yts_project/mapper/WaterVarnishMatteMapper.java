package com.it.yts_project.mapper;

import com.it.yts_project.model.WaterVarnishMatteProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 水油(哑光) Mapper接口
 */
@Mapper
public interface WaterVarnishMatteMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<WaterVarnishMatteProduct> findAllProducts();

    /**
     * 根据ID查询产品
     */
    WaterVarnishMatteProduct findProductById(@Param("id") Integer id);

    /**
     * 搜索产品
     */
    List<WaterVarnishMatteProduct> searchProducts(@Param("keyword") String keyword);

    /**
     * 插入产品
     */
    int insertProduct(WaterVarnishMatteProduct product);

    /**
     * 更新产品
     */
    int updateProduct(WaterVarnishMatteProduct product);

    /**
     * 删除产品
     */
    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<com.it.yts_project.model.WaterVarnishMatteCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 根据ID查询兼容性
     */
    com.it.yts_project.model.WaterVarnishMatteCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 插入兼容性
     */
    int insertCompatibility(com.it.yts_project.model.WaterVarnishMatteCompatibility compatibility);

    /**
     * 更新兼容性
     */
    int updateCompatibility(com.it.yts_project.model.WaterVarnishMatteCompatibility compatibility);

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
    int batchInsertCompatibilities(List<com.it.yts_project.model.WaterVarnishMatteCompatibility> compatibilities);

    /**
     * 查询兼容性是否存在（按唯一键）
     */
    com.it.yts_project.model.WaterVarnishMatteCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    // ========== 匹配查询 ==========

    /**
     * 搜索产品（可选工序筛选 + 分页）
     */
    List<WaterVarnishMatteProduct> searchProductsWithStep(
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
}
