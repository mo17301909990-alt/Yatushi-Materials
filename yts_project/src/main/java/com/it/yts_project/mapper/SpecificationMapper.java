package com.it.yts_project.mapper;

import com.it.yts_project.model.Specification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 规格信息Mapper接口
 */
@Mapper
public interface SpecificationMapper {
    
    /**
     * 根据产品ID查询规格
     */
    List<Specification> findByProjectId(@Param("projectId") Integer projectId);
    
    /**
     * 根据ID查询规格
     */
    Specification findById(@Param("id") Integer id);
    
    /**
     * 插入规格
     */
    int insert(Specification specification);
    
    /**
     * 更新规格
     */
    int update(Specification specification);
    
    /**
     * 删除规格
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据产品ID删除所有规格
     */
    int deleteByProjectId(@Param("projectId") Integer projectId);
    
    /**
     * 获取所有颜色选项
     */
    List<String> getColorOptions();
    
    /**
     * 获取所有尺寸选项
     */
    List<String> getSizeOptions();
    
    /**
     * 获取所有紧密度选项
     */
    List<String> getTightnessOptions();
    
    /**
     * 获取所有温度范围选项
     */
    List<String> getTemperatureRangeOptions();
}
