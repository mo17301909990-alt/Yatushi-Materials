package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeWrinkleUvCompatibility;
import com.it.yts_project.model.SiliconeWrinkleUvProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 硅胶_皱纹UV Mapper接口
 */
@Mapper
public interface SiliconeWrinkleUvMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<SiliconeWrinkleUvProduct> findAllProducts();

    /**
     * 查询启用的产品
     */
    List<SiliconeWrinkleUvProduct> findActiveProducts();

    /**
     * 根据ID查询产品
     */
    SiliconeWrinkleUvProduct findProductById(@Param("id") Integer id);

    /**
     * 搜索产品
     */
    List<SiliconeWrinkleUvProduct> searchProducts(@Param("keyword") String keyword);

    /**
     * 插入产品
     */
    int insertProduct(SiliconeWrinkleUvProduct product);

    /**
     * 更新产品
     */
    int updateProduct(SiliconeWrinkleUvProduct product);

    /**
     * 删除产品
     */
    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<SiliconeWrinkleUvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 查询所有兼容性
     */
    List<SiliconeWrinkleUvCompatibility> findAllCompatibilities();

    /**
     * 根据ID查询兼容性
     */
    SiliconeWrinkleUvCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 根据产品和工序查询兼容性（唯一键检查）
     */
    SiliconeWrinkleUvCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    /**
     * 插入兼容性
     */
    int insertCompatibility(SiliconeWrinkleUvCompatibility compatibility);

    /**
     * 更新兼容性
     */
    int updateCompatibility(SiliconeWrinkleUvCompatibility compatibility);

    /**
     * 删除兼容性
     */
    int deleteCompatibilityById(@Param("id") Integer id);

    /**
     * 批量删除兼容性
     */
    int batchDeleteCompatibilities(@Param("ids") List<Integer> ids);

    /**
     * 批量插入兼容性
     */
    int batchInsertCompatibilities(@Param("list") List<SiliconeWrinkleUvCompatibility> list);
}
