package com.it.yts_project.mapper;

import com.it.yts_project.model.Pricing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 价格信息Mapper接口
 */
@Mapper
public interface PricingMapper {
    
    /**
     * 根据产品ID查询价格
     */
    List<Pricing> findByProjectId(@Param("projectId") Integer projectId);
    
    /**
     * 根据ID查询价格
     */
    Pricing findById(@Param("id") Integer id);
    
    /**
     * 插入价格
     */
    int insert(Pricing pricing);
    
    /**
     * 更新价格
     */
    int update(Pricing pricing);
    
    /**
     * 删除价格
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据产品ID删除所有价格
     */
    int deleteByProjectId(@Param("projectId") Integer projectId);
}
