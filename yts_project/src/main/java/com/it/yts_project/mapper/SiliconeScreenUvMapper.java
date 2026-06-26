package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeScreenUvProduct;
import com.it.yts_project.model.SiliconeScreenUvCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeScreenUvMapper {
    List<SiliconeScreenUvProduct> findAllProducts();
    SiliconeScreenUvProduct findProductById(@Param("id") Integer id);
    List<SiliconeScreenUvProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeScreenUvProduct product);
    int updateProduct(SiliconeScreenUvProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeScreenUvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeScreenUvCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeScreenUvCompatibility compatibility);
    int updateCompatibility(SiliconeScreenUvCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeScreenUvCompatibility> compatibilities);
    SiliconeScreenUvCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeScreenUvProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
