package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeGlowInkProduct;
import com.it.yts_project.model.SiliconeGlowInkCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeGlowInkMapper {
    List<SiliconeGlowInkProduct> findAllProducts();
    SiliconeGlowInkProduct findProductById(@Param("id") Integer id);
    List<SiliconeGlowInkProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeGlowInkProduct product);
    int updateProduct(SiliconeGlowInkProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeGlowInkCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeGlowInkCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeGlowInkCompatibility compatibility);
    int updateCompatibility(SiliconeGlowInkCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeGlowInkCompatibility> compatibilities);
    SiliconeGlowInkCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeGlowInkProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
