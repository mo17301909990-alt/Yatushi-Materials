package com.it.yts_project.service;

import com.it.yts_project.model.LaminationCompatibility;
import com.it.yts_project.model.LaminationMaterialOption;
import com.it.yts_project.model.PostProcessingOption;

import java.util.List;

/**
 * 过胶管理Service接口
 */
public interface LaminatingService {
    
    // ========== 过胶材料选项管理 ==========
    
    /**
     * 查询所有过胶材料选项
     */
    List<LaminationMaterialOption> getAllMaterialOptions();
    
    /**
     * 查询激活的过胶材料选项
     */
    List<LaminationMaterialOption> getActiveMaterialOptions();
    
    /**
     * 根据ID查询过胶材料选项
     */
    LaminationMaterialOption getMaterialOptionById(Integer id);
    
    /**
     * 保存过胶材料选项
     */
    LaminationMaterialOption saveMaterialOption(LaminationMaterialOption option);
    
    /**
     * 删除过胶材料选项
     */
    void deleteMaterialOption(Integer id);
    
    // ========== 后处理工序选项管理 ==========
    
    /**
     * 查询所有后处理工序选项
     */
    List<PostProcessingOption> getAllProcessingOptions();
    
    /**
     * 查询激活的后处理工序选项
     */
    List<PostProcessingOption> getActiveProcessingOptions();
    
    /**
     * 根据工序名称查询
     */
    PostProcessingOption getProcessingOptionByStepName(String stepName);
    
    /**
     * 保存后处理工序选项
     */
    PostProcessingOption saveProcessingOption(PostProcessingOption option);
    
    /**
     * 删除后处理工序选项
     */
    void deleteProcessingOption(Integer id);
    
    // ========== 过胶兼容性管理 ==========
    
    /**
     * 查询过胶兼容性列表（带分页）
     */
    List<LaminationCompatibility> getCompatibilityList(String foilSeries, String productType, 
                                                       String modelNumber, Integer postProcessingStepId,
                                                       Integer laminationMaterialId, String compatibility,
                                                       Integer page, Integer size);
    
    /**
     * 统计过胶兼容性记录数
     */
    int countCompatibility(String foilSeries, String productType, String modelNumber,
                          Integer postProcessingStepId, Integer laminationMaterialId, String compatibility);
    
    /**
     * 根据ID查询过胶兼容性
     */
    LaminationCompatibility getCompatibilityById(Integer id);
    
    /**
     * 保存过胶兼容性（新增或更新）
     */
    LaminationCompatibility saveCompatibility(LaminationCompatibility compatibility);
    
    /**
     * 批量保存过胶兼容性
     */
    void batchSaveCompatibility(List<LaminationCompatibility> compatibilities);

    /**
     * 批量保存过胶兼容性（矩阵导入用快捷版本，仅确保 interfaceTypeId 为 0）
     */
    void batchSaveCompatibilityFast(List<LaminationCompatibility> compatibilities);
    
    /**
     * 删除过胶兼容性
     */
    void deleteCompatibility(Integer id);
    
    /**
     * 批量删除过胶兼容性
     */
    void batchDeleteCompatibility(List<Integer> ids);
    
    // ========== 选项数据获取 ==========
    
    /**
     * 获取所有产品系列名称
     */
    List<String> getAllFoilSeries();
    
    /**
     * 获取所有产品类型
     */
    List<String> getAllProductTypes();
    
    /**
     * 获取所有型号
     */
    List<String> getAllModelNumbers();
    
    /**
     * 获取"过胶"工序ID
     */
    Integer getLaminatingStepId();
    
    /**
     * 根据新的唯一键查找记录
     * 唯一键：foilSeries + modelNumber + productType + laminationMaterialId + postProcessingStepId
     */
    LaminationCompatibility findByUniqueKeyNew(
        String foilSeries,
        String modelNumber,
        String productType,
        Integer laminationMaterialId,
        Integer postProcessingStepId
    );
    
    /**
     * 批量更新兼容性状态
     */
    void batchUpdateStatus(List<Integer> ids, String compatibility);
}
