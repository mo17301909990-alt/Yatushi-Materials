package com.it.yts_project.mapper;

import com.it.yts_project.model.SiliconeMicaPearlProduct;
import com.it.yts_project.model.SiliconeMicaPearlCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SiliconeMicaPearlMapper {
    List<SiliconeMicaPearlProduct> findAllProducts();
    SiliconeMicaPearlProduct findProductById(@Param("id") Integer id);
    List<SiliconeMicaPearlProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(SiliconeMicaPearlProduct product);
    int updateProduct(SiliconeMicaPearlProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<SiliconeMicaPearlCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    SiliconeMicaPearlCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(SiliconeMicaPearlCompatibility compatibility);
    int updateCompatibility(SiliconeMicaPearlCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<SiliconeMicaPearlCompatibility> compatibilities);
    SiliconeMicaPearlCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<SiliconeMicaPearlProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
