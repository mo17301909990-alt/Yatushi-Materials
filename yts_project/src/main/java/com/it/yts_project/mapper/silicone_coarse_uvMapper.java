package com.it.yts_project.mapper;

import com.it.yts_project.model.silicone_coarse_uvCompatibility;
import com.it.yts_project.model.silicone_coarse_uvProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 硅胶粗纹UV Mapper接口
 */
@Mapper
public interface silicone_coarse_uvMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<silicone_coarse_uvProduct> findAllProducts();

    /**
     * 查询激活的产品
     */
    List<silicone_coarse_uvProduct> findActiveProducts();

    /**
     * 根据ID查询产品
     */
    silicone_coarse_uvProduct findProductById(@Param("id") Integer id);

    /**
     * 搜索产品
     */
    List<silicone_coarse_uvProduct> searchProducts(@Param("keyword") String keyword);

    /**
     * 插入产品
     */
    int insertProduct(silicone_coarse_uvProduct product);

    /**
     * 更新产品
     */
    int updateProduct(silicone_coarse_uvProduct product);

    /**
     * 删除产品
     */
    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<silicone_coarse_uvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 查询所有兼容性（含产品名称）
     */
    List<silicone_coarse_uvCompatibility> findAllCompatibilities();

    /**
     * 根据ID查询兼容性
     */
    silicone_coarse_uvCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 根据唯一键查询兼容性
     */
    silicone_coarse_uvCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    /**
     * 插入兼容性
     */
    int insertCompatibility(silicone_coarse_uvCompatibility compatibility);

    /**
     * 更新兼容性
     */
    int updateCompatibility(silicone_coarse_uvCompatibility compatibility);

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
    int batchInsertCompatibilities(@Param("list") List<silicone_coarse_uvCompatibility> list);

    /**
     * 获取所有后加工工序名称（去重）
     */
    List<String> getAllPostProcessingSteps();

    /**
     * 获取所有产品名称（去重）
     */
    List<String> getAllProductNames();
}
