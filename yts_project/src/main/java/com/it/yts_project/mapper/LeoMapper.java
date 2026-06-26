package com.it.yts_project.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * LEO纸品 统一匹配查询 Mapper
 * 跨 leo_book_board / leo_flat / leo_non_flat 三张表做 UNION ALL + INTERSECT
 */
@Mapper
public interface LeoMapper {

    /**
     * 分页查询匹配的产品ID和来源类型
     * 对每个选中的类型：INTERSECT所有步骤 → 关键词过滤 → UNION ALL
     */
    List<Map<String, Object>> searchLeoProductIds(
            @Param("types") List<String> types,
            @Param("steps") List<String> steps,
            @Param("stepCount") Integer stepCount,
            @Param("keyword") String keyword,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);

    /**
     * 统计匹配产品总数
     */
    Long countLeoProducts(
            @Param("types") List<String> types,
            @Param("steps") List<String> steps,
            @Param("stepCount") Integer stepCount,
            @Param("keyword") String keyword);

    // ========== 按类型查询兼容性状态 ==========

    List<Map<String, Object>> findBookBoardCompatibilityStatus(
            @Param("ids") List<Integer> ids,
            @Param("steps") List<String> steps);

    List<Map<String, Object>> findFlatCompatibilityStatus(
            @Param("ids") List<Integer> ids,
            @Param("steps") List<String> steps);

    List<Map<String, Object>> findNonFlatCompatibilityStatus(
            @Param("ids") List<Integer> ids,
            @Param("steps") List<String> steps);

    /**
     * 获取所有来源类型的去重步骤（含分类）
     */
    List<Map<String, Object>> getDistinctStepsWithCategory();
}
