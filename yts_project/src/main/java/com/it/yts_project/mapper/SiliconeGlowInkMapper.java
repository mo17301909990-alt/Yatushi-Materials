package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeGlowInkCompatibility;
import com.it.yts_project.model.SiliconeGlowInkProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 硅胶发光油墨Mapper接口
 */
@Mapper
public interface SiliconeGlowInkMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有硅胶发光油墨产品
     */
    List<SiliconeGlowInkProduct> findAllProducts();

    /**
     * 根据ID查询产品
     */
    SiliconeGlowInkProduct findProductById(@Param("id") Integer id);

    /**
     * 搜索产品
     */
    List<SiliconeGlowInkProduct> searchProducts(@Param("keyword") String keyword);

    /**
     * 插入产品
     */
    int insertProduct(SiliconeGlowInkProduct product);

    /**
     * 更新产品
     */
    int updateProduct(SiliconeGlowInkProduct product);

    /**
     * 删除产品
     */
    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<SiliconeGlowInkCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 查询所有兼容性列表
     */
    List<SiliconeGlowInkCompatibility> findAllCompatibilities();

    /**
     * 根据ID查询兼容性
     */
    SiliconeGlowInkCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 根据唯一键查询兼容性
     */
    SiliconeGlowInkCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    /**
     * 插入兼容性
     */
    int insertCompatibility(SiliconeGlowInkCompatibility compatibility);

    /**
     * 更新兼容性
     */
    int updateCompatibility(SiliconeGlowInkCompatibility compatibility);

    /**
     * 删除兼容性
     */
    int deleteCompatibilityById(@Param("id") Integer id);

    /**
     * 根据产品ID删除兼容性
     */
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 批量插入兼容性
     */
    int batchInsertCompatibilities(@Param("list") List<SiliconeGlowInkCompatibility> list);

    /**
     * 获取所有后加工工序名称（去重）
     */
    List<String> getAllPostProcessingSteps();
}
