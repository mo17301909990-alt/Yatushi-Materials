package com.it.yts_project.mapper;

import com.it.yts_project.model.PostProcessingLeduvglitter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 絲印LED UV灑閃粉后加工Mapper接口
 */
@Mapper
public interface PostProcessingLeduvglitterMapper {
    
    /**
     * 获取所有LED UV灑閃粉配置
     */
    List<PostProcessingLeduvglitter> findAll();
    
    /**
     * 分页查询LED UV灑閃粉配置
     */
    List<PostProcessingLeduvglitter> findWithPagination(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 获取总记录数
     */
    int getTotalCount();
    
    /**
     * 根据ID获取LED UV灑閃粉配置
     */
    PostProcessingLeduvglitter findById(@Param("id") Integer id);
    
    /**
     * 插入LED UV灑閃粉配置
     */
    int insert(PostProcessingLeduvglitter postProcessingLeduvglitter);
    
    /**
     * 更新LED UV灑閃粉配置
     */
    int update(PostProcessingLeduvglitter postProcessingLeduvglitter);
    
    /**
     * 删除LED UV灑閃粉配置
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据燙金紙系列搜索
     */
    List<PostProcessingLeduvglitter> findByProductName(@Param("productName") String productName);
    
    /**
     * 根据产品型号搜索
     */
    List<PostProcessingLeduvglitter> findByProductModelNumber(@Param("productModelNumber") String productModelNumber);
    
    /**
     * 根据布面纸类型ID搜索
     */
    List<PostProcessingLeduvglitter> findByClothPaperTypeId(@Param("clothPaperTypeId") Integer clothPaperTypeId);
    
    /**
     * 根据纸张类型搜索
     */
    List<PostProcessingLeduvglitter> findByPaperType(@Param("paperType") String paperType);
    
    /**
     * 获取所有可用的燙金紙系列
     */
    List<String> getAllProductNames();
    
    /**
     * 获取所有可用的产品型号
     */
    List<String> getAllProductModelNumbers();
    
    /**
     * 获取所有可用的布面纸类型ID
     */
    List<Integer> getAllClothPaperTypeIds();
    
    /**
     * 获取所有可用的纸张类型
     */
    List<String> getAllPaperTypes();
    
    /**
     * 检查絲印LED UV灑閃粉后加工兼容性
     * 渐进式匹配逻辑：
     * 1. 优先匹配 product_model_number + cloth_paper_type_id + paper_type
     * 2. 其次匹配 product_name + cloth_paper_type_id + paper_type
     * 
     * @param productName 产品系列
     * @param productModelNumber 产品型号
     * @param clothPaperTypeId 适用界面ID
     * @param paperType 纸张类型
     * @return 兼容性状态 V/X/NA/▷，如果未找到返回null
     */
    String checkLeduvglitterCompatibility(
        @Param("productName") String productName,
        @Param("productModelNumber") String productModelNumber,
        @Param("clothPaperTypeId") Integer clothPaperTypeId,
        @Param("paperType") String paperType
    );
    
    /**
     * 根据唯一键查找记录（用于导入时的更新判断）
     * 唯一键：productName + productModelNumber + clothPaperTypeId + paperType
     */
    PostProcessingLeduvglitter findByUniqueKey(
        @Param("productName") String productName,
        @Param("productModelNumber") String productModelNumber,
        @Param("clothPaperTypeId") Integer clothPaperTypeId,
        @Param("paperType") String paperType
    );
    
    /**
     * 批量更新兼容性状态
     */
    int batchUpdateStatus(
        @Param("ids") List<Integer> ids,
        @Param("compatibilityStatus") String compatibilityStatus
    );
}