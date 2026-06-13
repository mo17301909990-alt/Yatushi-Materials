package com.it.yts_project.mapper;

import com.it.yts_project.dto.CompatibilityQueryParams;
import com.it.yts_project.dto.HotStampingCompatibilityDetailDTO;
import com.it.yts_project.model.HotStampingCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 烫金工艺兼容性Mapper接口
 */
@Mapper
public interface HotStampingCompatibilityMapper {
    
    /**
     * 根据查询参数获取兼容的烫金类型
     */
    List<HotStampingCompatibility> findCompatibleHotStampingTypes(CompatibilityQueryParams params);
    
    /**
     * 获取所有烫金纸性能类型
     */
    List<String> getAllPaperPerformanceTypes();
    
    /**
     * 获取所有产品类型
     */
    List<String> getAllProductTypes();
    
    /**
     * 获取所有烫金类型
     */
    List<String> getAllHotStampingTypes();
    
    /**
     * 根据图案特征匹配兼容性规则
     */
    List<HotStampingCompatibility> matchPatternCompatibility(
            @Param("productType") String productType,
            @Param("patternCharacteristic") String patternCharacteristic,
            @Param("hotStampingType") String hotStampingType
    );

    /**
     * 根据产品类型ID获取去重的图案特征ID列表
     */
    List<Integer> getPatternCharacteristicIdsByProductTypeId(@Param("productTypeId") Integer productTypeId);
    
    /**
     * 获取所有烫金类型分组（支持多级下拉框）
     */
    List<com.it.yts_project.dto.HotStampingTypeGroupDTO> getAllHotStampingTypeGroups();
    
    /**
     * 根据烫金类型获取位置选项
     */
    List<com.it.yts_project.model.HotStampingTypeOptions> getPositionOptionsByStampingType(@Param("stampingType") String stampingType);
    
    /**
     * 根据多个ID字段筛选烫金纸性能类型
     * 这是核心业务逻辑：通过产品类型、图案特征、烫金类型、工序等条件筛选适用的烫金纸性能
     */
    List<String> getPaperPerformanceByMultipleIds(
            @Param("productTypeId") Integer productTypeId,
            @Param("patternCharacteristicId") Integer patternCharacteristicId,
            @Param("hotStampingTypeId") Integer hotStampingTypeId,
            @Param("preProcessingStepId") Integer preProcessingStepId,
            @Param("postProcessingStepId") Integer postProcessingStepId
    );
    
    /**
     * 根据条件获取完整的兼容性规则（包含所有字段）
     */
    List<HotStampingCompatibility> getCompatibilityRulesByMultipleIds(
            @Param("productTypeId") Integer productTypeId,
            @Param("patternCharacteristicId") Integer patternCharacteristicId,
            @Param("hotStampingTypeId") Integer hotStampingTypeId,
            @Param("preProcessingStepId") Integer preProcessingStepId,
            @Param("postProcessingStepId") Integer postProcessingStepId
    );

    /**
     * 获取完整的兼容性规则列表（包含关联表信息）
     */
    List<HotStampingCompatibilityDetailDTO> getCompatibilityRulesWithDetails();
    
    /**
     * 根据ID删除兼容性规则
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据ID获取单个兼容性规则
     */
    HotStampingCompatibility getById(@Param("id") Long id);
    
    /**
     * 插入新的兼容性规则
     */
    int insert(HotStampingCompatibility rule);
    
    /**
     * 更新兼容性规则
     */
    int update(HotStampingCompatibility rule);
    
    /**
     * 检查是否存在重复的兼容性规则
     * 重复定义：相同的 productTypeId + patternCharacteristicId + hotStampingTypeId + 
     *          preProcessingStepId + postProcessingStepId + paperPerformance + compatibility
     */
    int countDuplicateRule(
            @Param("productTypeId") Integer productTypeId,
            @Param("patternCharacteristicId") Integer patternCharacteristicId,
            @Param("hotStampingTypeId") Integer hotStampingTypeId,
            @Param("preProcessingStepId") Integer preProcessingStepId,
            @Param("postProcessingStepId") Integer postProcessingStepId,
            @Param("paperPerformance") String paperPerformance,
            @Param("compatibility") String compatibility,
            @Param("excludeId") Long excludeId  // 排除的ID（用于更新时检查）
    );

    /**
     * 统计某个烫金类型在耐磨映射表中的规则数量
     */
    int countByHotStampingTypeId(@Param("hotStampingTypeId") Integer hotStampingTypeId);
}
