package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeWhiteUvProduct;
import com.it.yts_project.model.SiliconeWhiteUvCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeWhiteUvMapper {
    List<SiliconeWhiteUvProduct> findAllProducts();
    SiliconeWhiteUvProduct findProductById(@Param("id") Integer id);
    List<SiliconeWhiteUvProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeWhiteUvProduct product);
    int updateProduct(SiliconeWhiteUvProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeWhiteUvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeWhiteUvCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeWhiteUvCompatibility compatibility);
    int updateCompatibility(SiliconeWhiteUvCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeWhiteUvCompatibility> compatibilities);
    SiliconeWhiteUvCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeWhiteUvProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
