package com.it.yts_project.mapper;

import com.it.yts_project.model.silicone_sandblast_uvProduct;
import com.it.yts_project.model.silicone_sandblast_uvCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 硅胶磨砂UV产品Mapper接口
 */
@Mapper
public interface silicone_sandblast_uvMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<silicone_sandblast_uvProduct> findAllProducts();

    /**
     * 查询激活的产品
     */
    List<silicone_sandblast_uvProduct> findActiveProducts();

    /**
     * 根据ID查询产品
     */
    silicone_sandblast_uvProduct findProductById(@Param("id") Integer id);

    /**
     * 搜索产品
     */
    List<silicone_sandblast_uvProduct> searchProducts(@Param("keyword") String keyword);

    /**
     * 插入产品
     */
    int insertProduct(silicone_sandblast_uvProduct product);

    /**
     * 更新产品
     */
    int updateProduct(silicone_sandblast_uvProduct product);

    /**
     * 删除产品
     */
    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<silicone_sandblast_uvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 查询所有兼容性列表（关联产品名称）
     */
    List<silicone_sandblast_uvCompatibility> findAllCompatibilities();

    /**
     * 根据ID查询兼容性
     */
    silicone_sandblast_uvCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 插入兼容性
     */
    int insertCompatibility(silicone_sandblast_uvCompatibility compatibility);

    /**
     * 更新兼容性
     */
    int updateCompatibility(silicone_sandblast_uvCompatibility compatibility);

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
    int batchInsertCompatibilities(@Param("list") List<silicone_sandblast_uvCompatibility> list);
}
