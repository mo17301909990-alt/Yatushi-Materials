package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeOrangePeelUvProduct;
import com.it.yts_project.model.SiliconeOrangePeelUvCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeOrangePeelUvMapper {
    List<SiliconeOrangePeelUvProduct> findAllProducts();
    SiliconeOrangePeelUvProduct findProductById(@Param("id") Integer id);
    List<SiliconeOrangePeelUvProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeOrangePeelUvProduct product);
    int updateProduct(SiliconeOrangePeelUvProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeOrangePeelUvCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeOrangePeelUvCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeOrangePeelUvCompatibility compatibility);
    int updateCompatibility(SiliconeOrangePeelUvCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeOrangePeelUvCompatibility> compatibilities);
    SiliconeOrangePeelUvCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeOrangePeelUvProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
