package com.it.yts_project.service;

import com.it.yts_project.dto.CompatibilityQueryParams;
import com.it.yts_project.model.HotStampingCompatibility;
import com.it.yts_project.model.LaminationCompatibility;
import com.it.yts_project.model.LaminationMaterialOptions;
import com.it.yts_project.model.PostProcessingOptions;

import java.util.List;

/**
 * 烫金工艺兼容性服务接口
 */
public interface HotStampingCompatibilityService {
    
    /**
     * 根据查询参数获取兼容的烫金类型
     */
    List<HotStampingCompatibility> getCompatibleHotStampingTypes(CompatibilityQueryParams params);
    
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
     * 智能匹配兼容的烫金类型
     * 根据用户输入的参数自动匹配最合适的烫金类型
     */
    List<String> getRecommendedHotStampingTypes(CompatibilityQueryParams params);
    
    // ========== 过胶兼容性相关方法 ==========
    
    /**
     * 根据参数查询过胶兼容性
     */
    List<LaminationCompatibility> getLaminationCompatibility(CompatibilityQueryParams params);
    
    /**
     * 根据烫金纸系列和后加工步骤查询兼容的过胶材质
     */
    List<LaminationMaterialOptions> getCompatibleLaminationMaterials(
        String foilSeries, Integer postProcessingStepId, Integer interfaceTypeId);
    
    /**
     * 根据烫金纸系列和过胶材质查询兼容的后加工步骤
     */
    List<PostProcessingOptions> getCompatiblePostProcessingSteps(
        String foilSeries, Integer laminationMaterialId, Integer interfaceTypeId);
    
    /**
     * 获取所有过胶材质选项
     */
    List<LaminationMaterialOptions> getAllLaminationMaterials();
    
    /**
     * 获取所有后加工步骤选项
     */
    List<PostProcessingOptions> getAllPostProcessingSteps();
    
    /**
     * 检查过胶兼容性
     */
    Character checkLaminationCompatibility(
        String foilSeries, Integer postProcessingStepId, 
        Integer laminationMaterialId, Integer interfaceTypeId);

     // ========== 图案选项相关方法 ==========

     /**
      * 根据产品类型ID获取图案选项（从兼容性表取去重的 pattern_characteristic_id，再到基础表取详情）
      */
     java.util.List<com.it.yts_project.model.HotStampingPatternBase> getPatternOptionsByProductTypeId(Integer productTypeId);
     
     /**
      * 获取所有图案选项
      */
     List<com.it.yts_project.model.HotStampingPatternBase> getAllPatternOptions();
     
     /**
      * 获取所有烫金类型分组（支持多级下拉框）
      */
     List<com.it.yts_project.dto.HotStampingTypeGroupDTO> getAllHotStampingTypeGroups();
     
     /**
      * 根据烫金类型获取位置选项
      */
     List<com.it.yts_project.model.HotStampingTypeOptions> getPositionOptionsByStampingType(String stampingType);

    /**
     * 根据多个ID字段筛选烫金纸性能类型
     * 根据用户选择的条件，从兼容性规则表中提取可用的纸类型列表
     * 
     * 逻辑：
     * - 如果只传入产品类型ID：查询 product_type_id = ? AND compatibility = 'V'，提取所有 paper_performance
     * - 如果传入产品类型ID + 图案ID：查询 product_type_id = ? AND pattern_characteristic_id = ? AND compatibility = 'V'，提取所有 paper_performance
     * - 如果传入产品类型ID + 图案ID + 烫金类型ID：查询 product_type_id = ? AND pattern_characteristic_id = ? AND hot_stamping_type_id = ? AND compatibility = 'V'，提取所有 paper_performance
     * 
     * @param productTypeId 产品类型ID
     * @param patternCharacteristicId 图案特征ID
     * @param hotStampingTypeId 烫金类型ID（对应hot_stamping_type_options表的id）
     * @param preProcessingStepId 前工序ID
     * @param postProcessingStepId 后工序ID
     * @return 符合条件的纸类型列表（去重），如果没有找到则返回空列表
     */
    List<String> getPaperPerformanceByMultipleIds(
            Integer productTypeId,
            Integer patternCharacteristicId,
            Integer hotStampingTypeId,
            Integer preProcessingStepId,
            Integer postProcessingStepId
    );

    /**
     * 是否存在耐磨映射（hot_stamping_compatibility 表）
     */
    boolean hasDurableMapping(Integer hotStampingTypeId);

    /**
     * 是否存在常用界面映射（hot_stamping_type_compatibility 表）
     */
    boolean hasCommonMapping(Integer hotStampingTypeId);

}
