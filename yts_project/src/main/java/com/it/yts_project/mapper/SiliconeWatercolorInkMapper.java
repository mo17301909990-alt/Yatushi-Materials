package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeWatercolorInkProduct;
import com.it.yts_project.model.SiliconeWatercolorInkCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 硅胶水彩油墨Mapper接口
 */
@Mapper
public interface SiliconeWatercolorInkMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<SiliconeWatercolorInkProduct> findAllProducts();

    /**
     * 根据ID查询产品
     */
    SiliconeWatercolorInkProduct findProductById(@Param("id") Integer id);

    /**
     * 搜索产品
     */
    List<SiliconeWatercolorInkProduct> searchProducts(@Param("keyword") String keyword);

    /**
     * 插入产品
     */
    int insertProduct(SiliconeWatercolorInkProduct product);

    /**
     * 更新产品
     */
    int updateProduct(SiliconeWatercolorInkProduct product);

    /**
     * 删除产品
     */
    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<SiliconeWatercolorInkCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 查询所有兼容性列表（关联产品名称）
     */
    List<SiliconeWatercolorInkCompatibility> findAllCompatibilities();

    /**
     * 根据ID查询兼容性
     */
    SiliconeWatercolorInkCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 根据产品和工序查询兼容性
     */
    SiliconeWatercolorInkCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    /**
     * 插入兼容性
     */
    int insertCompatibility(SiliconeWatercolorInkCompatibility compatibility);

    /**
     * 更新兼容性
     */
    int updateCompatibility(SiliconeWatercolorInkCompatibility compatibility);

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
    int batchInsertCompatibility(List<SiliconeWatercolorInkCompatibility> compatibilities);
}
