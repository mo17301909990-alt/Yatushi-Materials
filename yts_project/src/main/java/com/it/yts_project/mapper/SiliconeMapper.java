package com.it.yts_project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 硅胶组合匹配 Mapper 接口
 */
@Mapper
public interface SiliconeMapper {

    // ========== 多步骤匹配 ==========

    /**
     * 多步骤匹配：查找兼容 ALL 指定步骤的产品 (id, silicone_type)
     */
    List<Map<String, Object>> searchMultiStepProductIds(
            @Param("types") List<String> types,
            @Param("keyword") String keyword,
            @Param("steps") List<String> steps,
            @Param("stepCount") Integer stepCount,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 多步骤匹配：统计兼容 ALL 指定步骤的产品数
     */
    Long countMultiStepProducts(
            @Param("types") List<String> types,
            @Param("keyword") String keyword,
            @Param("steps") List<String> steps,
            @Param("stepCount") Integer stepCount);

    // ========== 单步骤匹配 ==========

    /**
     * 搜索产品（可选工序筛选 + 分页），返回 Map（含 silicone_type, compatibility_status）
     */
    List<Map<String, Object>> searchProductsWithStep(
            @Param("types") List<String> types,
            @Param("keyword") String keyword,
            @Param("stepName") String stepName,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 统计产品数量（可选工序筛选）
     */
    Long countProductsWithStep(
            @Param("types") List<String> types,
            @Param("keyword") String keyword,
            @Param("stepName") String stepName);

    // ========== 产品详情 ==========

    /**
     * 根据 ID + 类型查询产品
     */
    Map<String, Object> findProductByIdAndType(
            @Param("id") Integer id,
            @Param("type") String type);

    /**
     * 根据产品 ID + 类型查询兼容性列表
     */
    List<Map<String, Object>> findCompatibilitiesByProductIdAndType(
            @Param("productId") Integer productId,
            @Param("type") String type);

    // ========== 兼容性状态 ==========

    /**
     * 查询指定产品在指定步骤下的兼容性状态
     */
    List<Map<String, Object>> findCompatibilityStatusForProducts(
            @Param("ids") List<Integer> ids,
            @Param("steps") List<String> steps,
            @Param("type") String type);

    // ========== 步骤列表 ==========

    /**
     * 获取所有后加工工序步骤名称（去重）
     */
    List<String> getDistinctSteps(@Param("types") List<String> types);

    /**
     * 获取去重的 (step_category, post_processing_step) 对
     */
    List<Map<String, Object>> getDistinctStepsWithCategory(@Param("types") List<String> types);
}
