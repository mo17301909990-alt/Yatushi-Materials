package com.it.yts_project.mapper;

import com.it.yts_project.model.LeoFlatProduct;
import com.it.yts_project.model.LeoFlatCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LeoFlatMapper {
    List<LeoFlatProduct> findAllProducts();
    LeoFlatProduct findProductById(@Param("id") Integer id);
    List<LeoFlatProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(LeoFlatProduct product);
    int updateProduct(LeoFlatProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<LeoFlatCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    LeoFlatCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(LeoFlatCompatibility compatibility);
    int updateCompatibility(LeoFlatCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<LeoFlatCompatibility> compatibilities);
    LeoFlatCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<LeoFlatProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
