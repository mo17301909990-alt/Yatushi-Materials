package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeWaterBaseTransparentProduct;
import com.it.yts_project.model.SiliconeWaterBaseTransparentCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeWaterBaseTransparentMapper {
    List<SiliconeWaterBaseTransparentProduct> findAllProducts();
    SiliconeWaterBaseTransparentProduct findProductById(@Param("id") Integer id);
    List<SiliconeWaterBaseTransparentProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeWaterBaseTransparentProduct product);
    int updateProduct(SiliconeWaterBaseTransparentProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeWaterBaseTransparentCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeWaterBaseTransparentCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeWaterBaseTransparentCompatibility compatibility);
    int updateCompatibility(SiliconeWaterBaseTransparentCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeWaterBaseTransparentCompatibility> compatibilities);
    SiliconeWaterBaseTransparentCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeWaterBaseTransparentProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
