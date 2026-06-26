package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeLedUvSprayProduct;
import com.it.yts_project.model.SiliconeLedUvSprayCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeLedUvSprayMapper {
    List<SiliconeLedUvSprayProduct> findAllProducts();
    SiliconeLedUvSprayProduct findProductById(@Param("id") Integer id);
    List<SiliconeLedUvSprayProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeLedUvSprayProduct product);
    int updateProduct(SiliconeLedUvSprayProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeLedUvSprayCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeLedUvSprayCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeLedUvSprayCompatibility compatibility);
    int updateCompatibility(SiliconeLedUvSprayCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeLedUvSprayCompatibility> compatibilities);
    SiliconeLedUvSprayCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeLedUvSprayProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
