package com.it.yts_project.mapper;

import com.it.yts_project.model.LaminationMaterialCompatibility;
import com.it.yts_project.model.LaminationMaterialProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 过胶材料产品Mapper接口
 */
@Mapper
public interface LaminationMaterialProductMapper {

    // ========== 产品管理 ==========

    List<LaminationMaterialProduct> findAllProducts();

    LaminationMaterialProduct findProductById(@Param("id") Integer id);

    List<LaminationMaterialProduct> searchProducts(@Param("keyword") String keyword);

    int insertProduct(LaminationMaterialProduct product);

    int updateProduct(LaminationMaterialProduct product);

    int deleteProductById(@Param("id") Integer id);

    // ========== 兼容性管理 ==========

    List<LaminationMaterialCompatibility> findCompatibilitiesByProductId(@Param("productId") Integer productId);

    List<LaminationMaterialCompatibility> findAllCompatibilities();

    LaminationMaterialCompatibility findCompatibilityById(@Param("id") Integer id);

    LaminationMaterialCompatibility findCompatibilityByUniqueKey(
            @Param("productId") Integer productId,
            @Param("postProcessingStep") String postProcessingStep);

    int insertCompatibility(LaminationMaterialCompatibility compatibility);

    int updateCompatibility(LaminationMaterialCompatibility compatibility);

    int deleteCompatibilityById(@Param("id") Integer id);

    int deleteCompatibilitiesByProductId(@Param("productId") Integer productId);

    int batchInsertCompatibilities(@Param("list") List<LaminationMaterialCompatibility> list);

    List<String> getAllPostProcessingSteps();

    // ========== 匹配查询 ==========

    /**
     * 搜索产品（可多材料类型 + 多工序筛选 + 分页）
     */
    List<LaminationMaterialProduct> searchProductsWithStep(
            @Param("keyword") String keyword,
            @Param("materialTypes") List<String> materialTypes,
            @Param("steps") List<String> steps,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 统计产品数量（可多材料类型 + 多工序筛选）
     */
    Long countProductsWithStep(
            @Param("keyword") String keyword,
            @Param("materialTypes") List<String> materialTypes,
            @Param("steps") List<String> steps);

    /**
     * 获取所有后加工工序步骤名称（去重）
     */
    List<String> getDistinctPostProcessingSteps();

    /**
     * 获取所有材料类型（去重）
     */
    List<String> getDistinctMaterialTypes();
}
