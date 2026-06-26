package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeSandblastUvProduct;
import com.it.yts_project.model.SiliconeSandblastUvCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeSandblastUvMapper {
    List<SiliconeSandblastUvProduct> findAllProducts();
    SiliconeSandblastUvProduct findProductById(@Param("id") Integer id);
    List<SiliconeSandblastUvProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeSandblastUvProduct product);
    int updateProduct(SiliconeSandblastUvProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeSandblastUvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeSandblastUvCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeSandblastUvCompatibility compatibility);
    int updateCompatibility(SiliconeSandblastUvCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeSandblastUvCompatibility> compatibilities);
    SiliconeSandblastUvCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeSandblastUvProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
