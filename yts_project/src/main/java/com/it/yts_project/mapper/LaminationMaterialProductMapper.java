package com.it.yts_project.mapper;

import com.it.yts_project.model.LaminationMaterialCompatibility;
import com.it.yts_project.model.LaminationMaterialProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 过胶材料产品Mapper接口
 */
@Mapper
public interface LaminationMaterialProductMapper {

    // ========== 产品管理 ==========

    List<LaminationMaterialProduct> findAllProducts();

    LaminationMaterialProduct findProductById(@Param("id") Integer id);

    List<LaminationMaterialProduct> searchProducts(@Param("keyword") String keyword);

    int insertProduct(LaminationMaterialProduct product);

    int updateProduct(LaminationMaterialProduct product);

    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    List<LaminationMaterialCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    List<LaminationMaterialCompatibility> findAllCompatibilities();

    LaminationMaterialCompatibility findCompatibilityById(@Param("id") Integer id);

    LaminationMaterialCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    int insertCompatibility(LaminationMaterialCompatibility compatibility);

    int updateCompatibility(LaminationMaterialCompatibility compatibility);

    int deleteCompatibilityById(@Param("id") Integer id);

    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);

    int batchInsertCompatibilities(@Param("list") List<LaminationMaterialCompatibility> list);

    List<String> getAllPostProcessingSteps();
}
