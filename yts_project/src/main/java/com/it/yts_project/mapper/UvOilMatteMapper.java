package com.it.yts_project.mapper;

import com.it.yts_project.model.UvOilMatteProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}
