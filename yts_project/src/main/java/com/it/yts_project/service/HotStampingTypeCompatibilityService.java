package com.it.yts_project.service;

import com.it.yts_project.dto.HotStampingTypeCompatibilityCopyResult;
import com.it.yts_project.model.HotStampingTypeCompatibility;

import java.util.List;

/**
 * 烫金类型兼容性服务接口
 */
public interface HotStampingTypeCompatibilityService {
    
    /**
     * 根据烫金类型ID获取兼容性配置列表
     */
    List<HotStampingTypeCompatibility> getCompatibilityByHotStampingTypeId(Integer hotStampingTypeId);
    
    /**
     * 根据ID获取兼容性配置
     */
    HotStampingTypeCompatibility getCompatibilityById(Integer id);
    
    /**
     * 创建兼容性配置
     */
    HotStampingTypeCompatibility createCompatibility(HotStampingTypeCompatibility compatibility);
    
    /**
     * 更新兼容性配置
     */
    HotStampingTypeCompatibility updateCompatibility(HotStampingTypeCompatibility compatibility);
    
    /**
     * 删除兼容性配置
     */
    void deleteCompatibility(Integer id);
    
    /**
     * 根据烫金类型ID删除所有兼容性配置
     */
    void deleteCompatibilityByHotStampingTypeId(Integer hotStampingTypeId);
    
    /**
     * 获取所有可用的燙金紙系列
     */
    List<String> getAllProductNames();
    
    /**
     * 获取所有可用的纸张类型
     */
    List<String> getAllPaperTypes();

    /**
     * 根据产品名称获取对应的纸张类型
     */
    List<String> getPaperTypesByProductName(String productName);
    
    /**
     * 检查燙金紙系列和烫金类型ID的组合是否已存在
     */
    boolean existsByProductNameAndHotStampingTypeId(String productName, Integer hotStampingTypeId);
    
    /**
     * 批量复制兼容性配置到其他烫金类型
     * @param sourceIds 源映射ID列表
     * @param targetHotStampingTypeIds 目标烫金类型ID列表
     * @return 复制结果
     */
    HotStampingTypeCompatibilityCopyResult batchCopyCompatibility(List<Integer> sourceIds, List<Integer> targetHotStampingTypeIds);
    
    /**
     * 批量删除兼容性配置
     * @param ids 要删除的映射ID列表
     * @return 删除的记录数
     */
    int batchDeleteCompatibility(List<Integer> ids);
    
    /**
     * 获取所有兼容性配置（包含烫金类型信息）
     */
    List<HotStampingTypeCompatibility> getAllCompatibility();
}
