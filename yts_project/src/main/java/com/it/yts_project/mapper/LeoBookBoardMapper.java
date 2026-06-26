package com.it.yts_project.mapper;

import com.it.yts_project.model.LeoBookBoardProduct;
import com.it.yts_project.model.LeoBookBoardCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface LeoBookBoardMapper {
    List<LeoBookBoardProduct> findAllProducts();
    LeoBookBoardProduct findProductById(@Param("id") Integer id);
    List<LeoBookBoardProduct> searchProducts(@Param("keyword") String keyword);
    int insertProduct(LeoBookBoardProduct product);
    int updateProduct(LeoBookBoardProduct product);
    int deleteProductById(@Param("id") Integer id);

    List<LeoBookBoardCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);
    LeoBookBoardCompatibility findCompatibilityById(@Param("id") Integer id);
    int insertCompatibility(LeoBookBoardCompatibility compatibility);
    int updateCompatibility(LeoBookBoardCompatibility compatibility);
    int deleteCompatibilityById(@Param("id") Integer id);
    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);
    int batchInsertCompatibilities(List<LeoBookBoardCompatibility> compatibilities);
    LeoBookBoardCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    List<LeoBookBoardProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);
    List<String> getDistinctPostProcessingSteps();
}
