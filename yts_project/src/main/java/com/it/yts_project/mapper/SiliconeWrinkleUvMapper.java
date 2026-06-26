package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeWrinkleUvProduct;
import com.it.yts_project.model.SiliconeWrinkleUvCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeWrinkleUvMapper {
    List<SiliconeWrinkleUvProduct> findAllProducts();
    SiliconeWrinkleUvProduct findProductById(@Param("id") Integer id);
    List<SiliconeWrinkleUvProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeWrinkleUvProduct product);
    int updateProduct(SiliconeWrinkleUvProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeWrinkleUvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeWrinkleUvCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeWrinkleUvCompatibility compatibility);
    int updateCompatibility(SiliconeWrinkleUvCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeWrinkleUvCompatibility> compatibilities);
    SiliconeWrinkleUvCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeWrinkleUvProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
