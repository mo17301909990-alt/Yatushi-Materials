package com.it.yts_project.mapper;

import com.it.yts_project.model.YaguangUvOilProduct;
import com.it.yts_project.model.YaguangUvOilCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 哑光UV油 Mapper接口
 */
@Mapper
public interface YaguangUvOilMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<YaguangUvOilProduct> findAllProducts();

    /**
     * 根据ID查询产品
     */
    YaguangUvOilProduct findProductById(@Param("id") Integer id);

    /**
     * 搜索产品
     */
    List<YaguangUvOilProduct> searchProducts(@Param("keyword") String keyword);

    /**
     * 插入产品
     */
    int insertProduct(YaguangUvOilProduct product);

    /**
     * 更新产品
     */
    int updateProduct(YaguangUvOilProduct product);

    /**
     * 删除产品
     */
    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<YaguangUvOilCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 根据ID查询兼容性
     */
    YaguangUvOilCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 插入兼容性
     */
    int insertCompatibility(YaguangUvOilCompatibility compatibility);

    /**
     * 更新兼容性
     */
    int updateCompatibility(YaguangUvOilCompatibility compatibility);

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
    int batchInsertCompatibilities(List<YaguangUvOilCompatibility> compatibilities);

    /**
     * 查询兼容性是否存在（按唯一键）
     */
    YaguangUvOilCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("stepName") String stepName);
}
