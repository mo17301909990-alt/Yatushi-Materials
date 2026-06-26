package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeCoarseUvProduct;
import com.it.yts_project.model.SiliconeCoarseUvCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeCoarseUvMapper {
    List<SiliconeCoarseUvProduct> findAllProducts();
    SiliconeCoarseUvProduct findProductById(@Param("id") Integer id);
    List<SiliconeCoarseUvProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeCoarseUvProduct product);
    int updateProduct(SiliconeCoarseUvProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeCoarseUvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeCoarseUvCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeCoarseUvCompatibility compatibility);
    int updateCompatibility(SiliconeCoarseUvCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeCoarseUvCompatibility> compatibilities);
    SiliconeCoarseUvCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeCoarseUvProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
