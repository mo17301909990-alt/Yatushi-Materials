package com.it.yts_project.mapper;

import com.it.yts_project.model.LeoNonFlatProduct;
import com.it.yts_project.model.LeoNonFlatCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LeoNonFlatMapper {
    List<LeoNonFlatProduct> findAllProducts();
    LeoNonFlatProduct findProductById(@Param("id") Integer id);
    List<LeoNonFlatProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(LeoNonFlatProduct product);
    int updateProduct(LeoNonFlatProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<LeoNonFlatCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    LeoNonFlatCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(LeoNonFlatCompatibility compatibility);
    int updateCompatibility(LeoNonFlatCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<LeoNonFlatCompatibility> compatibilities);
    LeoNonFlatCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<LeoNonFlatProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
