package com.it.yts_project.mapper;

import com.it.yts_project.model.HotStampingTypeCompatibility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 烫金类型兼容性Mapper接口
 */
@Mapper
public interface HotStampingTypeCompatibilityMapper {
    
    /**
     * 根据烫金类型ID获取兼容性配置
     */
    List<HotStampingTypeCompatibility> findByHotStampingTypeId(@Param("hotStampingTypeId") Integer hotStampingTypeId);
    
    /**
     * 根据ID获取兼容性配置
     */
    HotStampingTypeCompatibility findById(@Param("id") Integer id);
    
    /**
     * 插入兼容性配置
     */
    int insert(HotStampingTypeCompatibility compatibility);
    
    /**
     * 更新兼容性配置
     */
    int update(HotStampingTypeCompatibility compatibility);
    
    /**
     * 删除兼容性配置
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据烫金类型ID删除所有兼容性配置
     */
    int deleteByHotStampingTypeId(@Param("hotStampingTypeId") Integer hotStampingTypeId);
    
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
    List<String> getPaperTypesByProductName(@Param("productName") String productName);
    
    /**
     * 检查燙金紙系列和烫金类型ID的组合是否已存在
     */
    int countByProductNameAndHotStampingTypeId(@Param("productName") String productName, 
                                              @Param("hotStampingTypeId") Integer hotStampingTypeId);

    /**
     * 统计常用界面映射中某烫金类型的规则数量
     */
    int countByHotStampingTypeId(@Param("hotStampingTypeId") Integer hotStampingTypeId);
    
    /**
     * 获取所有兼容性配置（包含烫金类型信息）
     */
    List<HotStampingTypeCompatibility> findAll();
}
