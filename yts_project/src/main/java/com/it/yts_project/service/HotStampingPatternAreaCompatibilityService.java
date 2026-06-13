package com.it.yts_project.service;

import com.it.yts_project.model.HotStampingPatternAreaCompatibility;

import java.util.List;

/**
 * 烫金图案区域兼容性服务接口
 */
public interface HotStampingPatternAreaCompatibilityService {
    
    /**
     * 获取所有兼容性配置列表
     */
    List<HotStampingPatternAreaCompatibility> getAllCompatibility();
    
    /**
     * 根据图案区域ID获取兼容性配置列表
     */
    List<HotStampingPatternAreaCompatibility> getCompatibilityByPatternAreaId(Integer patternAreaId);
    
    /**
     * 根据ID获取兼容性配置
     */
    HotStampingPatternAreaCompatibility getCompatibilityById(Integer id);
    
    /**
     * 创建兼容性配置
     */
    HotStampingPatternAreaCompatibility createCompatibility(HotStampingPatternAreaCompatibility compatibility);
    
    /**
     * 更新兼容性配置
     */
    HotStampingPatternAreaCompatibility updateCompatibility(HotStampingPatternAreaCompatibility compatibility);
    
    /**
     * 删除兼容性配置
     */
    void deleteCompatibility(Integer id);
    
    /**
     * 根据图案区域ID删除所有兼容性配置
     */
    void deleteCompatibilityByPatternAreaId(Integer patternAreaId);
    
    /**
     * 获取所有可用的燙金紙系列
     */
    List<String> getAllProductNames();
    
    /**
     * 检查燙金紙系列和图案区域ID的组合是否已存在
     */
    boolean existsByProductNameAndPatternAreaId(String productName, Integer patternAreaId);
    
    /**
     * 根据燙金紙系列和图案区域ID获取兼容性配置
     */
    HotStampingPatternAreaCompatibility getCompatibilityByProductNameAndPatternAreaId(String productName, Integer patternAreaId);
    
    /**
     * 根据燙金紙系列、图案区域ID和燙金紙性能類型获取兼容性配置
     */
    HotStampingPatternAreaCompatibility getCompatibilityByProductNameAndPatternAreaIdAndPaperType(
        String productName, Integer patternAreaId, String paperType);
}
