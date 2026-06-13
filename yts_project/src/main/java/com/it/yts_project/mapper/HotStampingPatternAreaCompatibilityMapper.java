package com.it.yts_project.mapper;

import com.it.yts_project.model.HotStampingPatternAreaCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 烫金图案区域兼容性Mapper接口
 */
@Mapper
public interface HotStampingPatternAreaCompatibilityMapper {
    
    /**
     * 获取所有兼容性配置
     */
    List<HotStampingPatternAreaCompatibility> findAll();
    
    /**
     * 根据图案区域ID获取兼容性配置
     */
    List<HotStampingPatternAreaCompatibility> findByPatternAreaId(@Param("patternAreaId") Integer patternAreaId);
    
    /**
     * 根据ID获取兼容性配置
     */
    HotStampingPatternAreaCompatibility findById(@Param("id") Integer id);
    
    /**
     * 插入兼容性配置
     */
    int insert(HotStampingPatternAreaCompatibility compatibility);
    
    /**
     * 更新兼容性配置
     */
    int update(HotStampingPatternAreaCompatibility compatibility);
    
    /**
     * 删除兼容性配置
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据图案区域ID删除所有兼容性配置
     */
    int deleteByPatternAreaId(@Param("patternAreaId") Integer patternAreaId);
    
    /**
     * 获取所有可用的燙金紙系列
     */
    List<String> getAllProductNames();
    
    /**
     * 检查燙金紙系列和图案区域ID的组合是否已存在
     */
    int countByProductNameAndPatternAreaId(@Param("productName") String productName, 
                                          @Param("patternAreaId") Integer patternAreaId);
    
    /**
     * 根据燙金紙系列和图案区域ID获取兼容性配置
     */
    HotStampingPatternAreaCompatibility findByProductNameAndPatternAreaId(@Param("productName") String productName, 
                                                                          @Param("patternAreaId") Integer patternAreaId);
    
    /**
     * 根据燙金紙系列、图案区域ID和燙金紙性能類型获取兼容性配置
     */
    HotStampingPatternAreaCompatibility findByProductNameAndPatternAreaIdAndPaperType(
        @Param("productName") String productName, 
        @Param("patternAreaId") Integer patternAreaId,
        @Param("paperType") String paperType);
}
