package com.it.yts_project.mapper;

import com.it.yts_project.model.PreProcessingSteps;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前工序步骤Mapper接口
 */
@Mapper
public interface PreProcessingStepsMapper {
    
    /**
     * 根据步骤ID获取所有激活的前工序步骤
     * @param stepId 步骤ID（对应pre_processing_steps_options表的id）
     * @return 前工序步骤列表
     */
    List<PreProcessingSteps> getActiveStepsByStepId(@Param("stepId") Integer stepId);
    
    /**
     * 根据步骤ID和系列名称获取前工序步骤
     * @param stepId 步骤ID
     * @param seriesName 系列名称
     * @return 前工序步骤列表
     */
    List<PreProcessingSteps> getStepsByStepIdAndSeriesName(@Param("stepId") Integer stepId, 
                                                          @Param("seriesName") String seriesName);
    
    /**
     * 根据步骤ID和产品ID获取前工序步骤
     * @param stepId 步骤ID
     * @param productId 产品ID
     * @return 前工序步骤列表
     */
    List<PreProcessingSteps> getStepsByStepIdAndProductId(@Param("stepId") Integer stepId, 
                                                         @Param("productId") Integer productId);
    
    /**
     * 根据步骤ID、系列名称和纸张类型获取前工序步骤
     * @param stepId 步骤ID
     * @param seriesName 系列名称
     * @param paperType 纸张类型
     * @return 前工序步骤列表
     */
    List<PreProcessingSteps> getStepsByStepIdSeriesNameAndPaperType(@Param("stepId") Integer stepId, 
                                                                   @Param("seriesName") String seriesName, 
                                                                   @Param("paperType") String paperType);
    
    /**
     * 根据步骤ID、产品ID和纸张类型获取前工序步骤
     * @param stepId 步骤ID
     * @param productId 产品ID
     * @param paperType 纸张类型
     * @return 前工序步骤列表
     */
    List<PreProcessingSteps> getStepsByStepIdProductIdAndPaperType(@Param("stepId") Integer stepId, 
                                                                  @Param("productId") Integer productId, 
                                                                  @Param("paperType") String paperType);
    
    /**
     * 获取所有激活的前工序步骤
     * @return 前工序步骤列表
     */
    List<PreProcessingSteps> getAllActiveSteps();
    
    /**
     * 根据ID获取前工序步骤
     * @param id 主键ID
     * @return 前工序步骤
     */
    PreProcessingSteps getById(@Param("id") Integer id);
    
    /**
     * 根据步骤ID获取所有前工序步骤（包括未激活的）
     * @param stepId 步骤ID
     * @return 前工序步骤列表
     */
    List<PreProcessingSteps> getAllStepsByStepId(@Param("stepId") Integer stepId);
    
    /**
     * 插入前工序步骤
     * @param step 前工序步骤
     * @return 影响行数
     */
    int insert(PreProcessingSteps step);
    
    /**
     * 更新前工序步骤
     * @param step 前工序步骤
     * @return 影响行数
     */
    int update(PreProcessingSteps step);
    
    /**
     * 根据ID删除前工序步骤
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据步骤ID删除所有前工序步骤
     * @param stepId 步骤ID（对应pre_processing_steps_options表的id）
     * @return 影响行数
     */
    int deleteByStepId(@Param("stepId") Integer stepId);
    
    /**
     * 获取所有前工序步骤（包括未激活的）
     * @return 前工序步骤列表
     */
    List<PreProcessingSteps> getAllSteps();
    
    /**
     * 检查是否存在相同的记录（用于唯一性验证）
     * @param stepId 步骤ID
     * @param seriesName 产品系列名称
     * @param productId 产品ID（可为null）
     * @param paperType 烫金纸性能类型
     * @param excludeId 排除的ID（用于更新时排除当前记录）
     * @return 如果存在返回true，否则返回false
     */
    boolean existsByUniqueFields(@Param("stepId") Integer stepId,
                                 @Param("seriesName") String seriesName,
                                 @Param("productId") Integer productId,
                                 @Param("paperType") String paperType,
                                 @Param("excludeId") Integer excludeId);
    
    /**
     * 检查产品系列是否存在通用映射（product_id 为 null）
     * @param stepId 步骤ID
     * @param seriesName 产品系列名称
     * @param paperType 烫金纸性能类型
     * @return 如果存在返回true，否则返回false
     */
    boolean existsGeneralMapping(@Param("stepId") Integer stepId,
                                @Param("seriesName") String seriesName,
                                @Param("paperType") String paperType);
    
    /**
     * 根据唯一性字段查找前工序步骤
     * @param stepId 步骤ID
     * @param seriesName 产品系列名称
     * @param productId 产品ID（可为null）
     * @param paperType 烫金纸性能类型
     * @return 如果找到返回PreProcessingSteps对象，否则返回null
     */
    PreProcessingSteps findByUniqueFields(@Param("stepId") Integer stepId,
                                          @Param("seriesName") String seriesName,
                                          @Param("productId") Integer productId,
                                          @Param("paperType") String paperType);

    /**
     * 更新注意事项ID列表
     */
    void updateNoticeIds(@Param("id") Integer id, @Param("noticeIds") List<Integer> noticeIds);
}
