package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeGlitteringStarProduct;
import com.it.yts_project.model.SiliconeGlitteringStarCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeGlitteringStarMapper {
    List<SiliconeGlitteringStarProduct> findAllProducts();
    SiliconeGlitteringStarProduct findProductById(@Param("id") Integer id);
    List<SiliconeGlitteringStarProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeGlitteringStarProduct product);
    int updateProduct(SiliconeGlitteringStarProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeGlitteringStarCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeGlitteringStarCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeGlitteringStarCompatibility compatibility);
    int updateCompatibility(SiliconeGlitteringStarCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeGlitteringStarCompatibility> compatibilities);
    SiliconeGlitteringStarCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeGlitteringStarProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
