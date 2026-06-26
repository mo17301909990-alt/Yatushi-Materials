package com.it.yts_project.mapper;

import com.it.yts_project.model.ProductTypeSortConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品类型特殊映射配置Mapper接口
 */
@Mapper
public interface ProductTypeSortConfigMapper {

    /**
     * 获取所有配置
     */
    List<ProductTypeSortConfig> findAll();

    /**
     * 根据ID获取配置
     */
    ProductTypeSortConfig findById(@Param("id") Integer id);

    /**
     * 根据产品类型ID获取配置
     */
    ProductTypeSortConfig findByProductTypeId(@Param("productTypeId") Integer productTypeId);

    /**
     * 获取所有激活的配置
     */
    List<ProductTypeSortConfig> findAllActive();

    /**
     * 插入配置
     */
    int insert(ProductTypeSortConfig config);

    /**
     * 更新配置
     */
    int update(ProductTypeSortConfig config);

    /**
     * 删除配置
     */
    int deleteById(@Param("id") Integer id);
}
