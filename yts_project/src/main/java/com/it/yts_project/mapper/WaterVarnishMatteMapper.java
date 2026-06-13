package com.it.yts_project.mapper;

import com.it.yts_project.model.WaterVarnishMatteCompatibility;
import com.it.yts_project.model.WaterVarnishMatteProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UV油_哑光水油 Mapper接口
 */
@Mapper
public interface WaterVarnishMatteMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<WaterVarnishMatteProduct> findAllProducts();

    /**
     * 查询激活的产品
     */
    List<WaterVarnishMatteProduct> findActiveProducts();

    /**
     * 根据ID查询产品
     */
    WaterVarnishMatteProduct findProductById(@Param("id") Integer id);

    /**
     * 根据物料编码查询产品
     */
    WaterVarnishMatteProduct findProductByMaterialCode(@Param("materialCode") String materialCode);

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
    List<WaterVarnishMatteCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 查询所有兼容性记录
     */
    List<WaterVarnishMatteCompatibility> findAllCompatibilities();

    /**
     * 根据ID查询兼容性
     */
    WaterVarnishMatteCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 根据产品和工序查找兼容性
     */
    WaterVarnishMatteCompatibility findCompatibilityByProductAndStep(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    /**
     * 插入兼容性
     */
    int insertCompatibility(WaterVarnishMatteCompatibility compatibility);

    /**
     * 批量插入兼容性
     */
    int batchInsertCompatibility(@Param("list") List<WaterVarnishMatteCompatibility> list);

    /**
     * 更新兼容性
     */
    int updateCompatibility(WaterVarnishMatteCompatibility compatibility);

    /**
     * 删除兼容性
     */
    int deleteCompatibilityById(@Param("id") Integer id);

    /**
     * 根据产品ID删除兼容性
     */
    int deleteCompatibilityByProductId(@Param("productId") Integer productId);

    /**
     * 获取所有后加工工序步骤名称（去重）
     */
    List<String> getAllPostProcessingSteps();
}
