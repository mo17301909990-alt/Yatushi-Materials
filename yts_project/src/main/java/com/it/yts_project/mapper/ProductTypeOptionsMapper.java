package com.it.yts_project.mapper;

import com.it.yts_project.model.ProductTypeOptions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品类型选项Mapper接口
 */
@Mapper
public interface ProductTypeOptionsMapper {
    
    /**
     * 获取所有激活的产品类型选项
     * @return 产品类型选项列表
     */
    List<ProductTypeOptions> getAllActiveOptions();
    
    /**
     * 根据ID获取产品类型选项
     * @param id 主键ID
     * @return 产品类型选项
     */
    ProductTypeOptions getById(@Param("id") Integer id);
    
    /**
     * 获取所有产品类型选项（包括非激活的）
     * @return 产品类型选项列表
     */
    List<ProductTypeOptions> getAllOptions();
    
    /**
     * 插入产品类型选项
     * @param productType 产品类型选项
     * @return 影响行数
     */
    int insert(ProductTypeOptions productType);
    
    /**
     * 更新产品类型选项
     * @param productType 产品类型选项
     * @return 影响行数
     */
    int update(ProductTypeOptions productType);
    
    /**
     * 根据ID删除产品类型选项
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 更新注意事项ID列表
     */
    void updateNoticeIds(@Param("id") Integer id, @Param("noticeIds") List<Integer> noticeIds);
}
