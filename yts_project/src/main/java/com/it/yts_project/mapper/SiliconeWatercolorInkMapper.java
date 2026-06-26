package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeWatercolorInkProduct;
import com.it.yts_project.model.SiliconeWatercolorInkCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeWatercolorInkMapper {
    List<SiliconeWatercolorInkProduct> findAllProducts();
    SiliconeWatercolorInkProduct findProductById(@Param("id") Integer id);
    List<SiliconeWatercolorInkProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeWatercolorInkProduct product);
    int updateProduct(SiliconeWatercolorInkProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeWatercolorInkCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeWatercolorInkCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeWatercolorInkCompatibility compatibility);
    int updateCompatibility(SiliconeWatercolorInkCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeWatercolorInkCompatibility> compatibilities);
    SiliconeWatercolorInkCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeWatercolorInkProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
