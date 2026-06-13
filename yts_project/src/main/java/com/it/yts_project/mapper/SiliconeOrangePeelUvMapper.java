package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeOrangePeelUvProduct;
import com.it.yts_project.model.SiliconeOrangePeelUvCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 硅胶桔纹UV(Orange Peel UV) Mapper接口
 */
@Mapper
public interface SiliconeOrangePeelUvMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<SiliconeOrangePeelUvProduct> findAllProducts();

    /**
     * 查询激活的产品
     */
    List<SiliconeOrangePeelUvProduct> findActiveProducts();

    /**
     * 根据ID查询产品
     */
    SiliconeOrangePeelUvProduct findProductById(@Param("id") Integer id);

    /**
     * 搜索产品
     */
    List<SiliconeOrangePeelUvProduct> searchProducts(@Param("keyword") String keyword);

    /**
     * 插入产品
     */
    int insertProduct(SiliconeOrangePeelUvProduct product);

    /**
     * 更新产品
     */
    int updateProduct(SiliconeOrangePeelUvProduct product);

    /**
     * 删除产品
     */
    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<SiliconeOrangePeelUvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 查询所有兼容性列表（含产品名称）
     */
    List<SiliconeOrangePeelUvCompatibility> findAllCompatibilities();

    /**
     * 根据ID查询兼容性
     */
    SiliconeOrangePeelUvCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 根据产品和工序查询兼容性
     */
    SiliconeOrangePeelUvCompatibility findCompatibilityByProductAndStep(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    /**
     * 插入兼容性
     */
    int insertCompatibility(SiliconeOrangePeelUvCompatibility compatibility);

    /**
     * 更新兼容性
     */
    int updateCompatibility(SiliconeOrangePeelUvCompatibility compatibility);

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
    int batchInsertCompatibilities(@Param("list") List<SiliconeOrangePeelUvCompatibility> list);

    /**
     * 获取所有后加工工序名称
     */
    List<String> getAllPostProcessingSteps();
}
