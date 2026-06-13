package com.it.yts_project.service;

import com.it.yts_project.model.PreProcessingSteps;

import java.util.List;

/**
 * 前工序步骤服务接口
 */
public interface PreProcessingStepsService {
    
    /**
     * 根据步骤ID获取所有前工序步骤
     * @param stepId 步骤ID（对应pre_processing_steps_options表的id）
     * @return 前工序步骤列表
     */
    List<PreProcessingSteps> getAllStepsByStepId(Integer stepId);
    
    /**
     * 根据ID获取前工序步骤
     * @param id 步骤ID
     * @return 前工序步骤
     */
    PreProcessingSteps getById(Integer id);
    
    /**
     * 创建前工序步骤
     * @param step 前工序步骤
     * @return 创建的前工序步骤
     */
    PreProcessingSteps create(PreProcessingSteps step);
    
    /**
     * 更新前工序步骤
     * @param step 前工序步骤
     * @return 更新后的前工序步骤
     */
    PreProcessingSteps update(PreProcessingSteps step);
    
    /**
     * 删除前工序步骤
     * @param id 步骤ID
     * @return 是否删除成功
     */
    boolean deleteById(Integer id);
    
    /**
     * 根据步骤ID删除所有前工序步骤
     * @param stepId 步骤ID（对应pre_processing_steps_options表的id）
     * @return 删除的记录数
     */
    int deleteByStepId(Integer stepId);
    
    /**
     * 获取所有前工序步骤（包括未激活的）
     * @return 前工序步骤列表
     */
    List<PreProcessingSteps> getAllSteps();
    
    /**
     * 根据唯一性字段查找前工序步骤
     * @param stepId 步骤ID
     * @param seriesName 产品系列名称
     * @param productId 产品ID（可为null）
     * @param paperType 烫金纸性能类型
     * @return 如果找到返回PreProcessingSteps对象，否则返回null
     */
    PreProcessingSteps findByUniqueFields(Integer stepId, String seriesName, Integer productId, String paperType);
}

