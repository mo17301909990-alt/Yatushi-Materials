package com.it.yts_project.mapper;

import com.it.yts_project.model.silicone_white_uvProduct;
import com.it.yts_project.model.silicone_white_uvCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 硅胶_白UV Mapper接口
 */
@Mapper
public interface silicone_white_uvMapper {

    // ========== 产品管理 ==========

    /**
     * 查询所有产品
     */
    List<silicone_white_uvProduct> findAllProducts();

    /**
     * 查询激活的产品
     */
    List<silicone_white_uvProduct> findActiveProducts();

    /**
     * 根据ID查询产品
     */
    silicone_white_uvProduct findProductById(@Param("id") Integer id);

    /**
     * 搜索产品
     */
    List<silicone_white_uvProduct> searchProducts(@Param("keyword") String keyword);

    /**
     * 插入产品
     */
    int insertProduct(silicone_white_uvProduct product);

    /**
     * 更新产品
     */
    int updateProduct(silicone_white_uvProduct product);

    /**
     * 删除产品
     */
    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID查询兼容性列表
     */
    List<silicone_white_uvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    /**
     * 查询所有兼容性（含产品名称）
     */
    List<silicone_white_uvCompatibility> findAllCompatibilities();

    /**
     * 根据ID查询兼容性
     */
    silicone_white_uvCompatibility findCompatibilityById(@Param("id") Integer id);

    /**
     * 插入兼容性
     */
    int insertCompatibility(silicone_white_uvCompatibility compatibility);

    /**
     * 更新兼容性
     */
    int updateCompatibility(silicone_white_uvCompatibility compatibility);

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
    int batchInsertCompatibilities(@Param("list") List<silicone_white_uvCompatibility> list);
}
