package com.it.yts_project.mapper;

import com.it.yts_project.model.PostProcessingPrintUv;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 印刷UV后加工Mapper接口
 */
@Mapper
public interface PostProcessingPrintUvMapper {
    
    /**
     * 获取所有UV印刷配置
     */
    List<PostProcessingPrintUv> findAll();
    
    /**
     * 分页获取UV印刷配置
     */
    List<PostProcessingPrintUv> findWithPagination(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 获取总记录数
     */
    int getTotalCount();
    
    /**
     * 根据ID获取UV印刷配置
     */
    PostProcessingPrintUv findById(@Param("id") Integer id);
    
    /**
     * 插入UV印刷配置
     */
    int insert(PostProcessingPrintUv postProcessingPrintUv);
    
    /**
     * 更新UV印刷配置
     */
    int update(PostProcessingPrintUv postProcessingPrintUv);
    
    /**
     * 删除UV印刷配置
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 根据燙金紙系列搜索
     */
    List<PostProcessingPrintUv> findByProductName(@Param("productName") String productName);
    
    /**
     * 根据产品型号搜索
     */
    List<PostProcessingPrintUv> findByProductModelNumber(@Param("productModelNumber") String productModelNumber);
    
    /**
     * 根据公司编号搜索
     */
    List<PostProcessingPrintUv> findByCompanyNumber(@Param("companyNumber") String companyNumber);
    
    /**
     * 根据纸张类型搜索
     */
    List<PostProcessingPrintUv> findByPaperType(@Param("paperType") String paperType);
    
    /**
     * 获取所有可用的燙金紙系列
     */
    List<String> getAllProductNames();
    
    /**
     * 获取所有可用的产品型号
     */
    List<String> getAllProductModelNumbers();
    
    /**
     * 获取所有可用的公司编号
     */
    List<String> getAllCompanyNumbers();
    
    /**
     * 获取所有可用的纸张类型
     */
    List<String> getAllPaperTypes();
    
    /**
     * 检查印刷UV后加工兼容性
     * 渐进式匹配逻辑：
     * 1. 优先匹配 product_model_number + company_number + paper_type
     * 2. 其次匹配 product_name + company_number + paper_type
     * 
     * @param productName 产品系列
     * @param productModelNumber 产品型号
     * @param companyNumber 公司编号
     * @param paperType 纸张类型
     * @return 兼容性状态 V/X/NA/▷，如果未找到返回null
     */
    String checkPrintUvCompatibility(
        @Param("productName") String productName,
        @Param("productModelNumber") String productModelNumber,
        @Param("companyNumber") String companyNumber,
        @Param("paperType") String paperType
    );
    
    /**
     * 根据唯一键查找记录
     * 唯一键：productName + productModelNumber + companyNumber + paperType
     */
    PostProcessingPrintUv findByUniqueKey(
        @Param("productName") String productName,
        @Param("productModelNumber") String productModelNumber,
        @Param("companyNumber") String companyNumber,
        @Param("paperType") String paperType
    );
    
    /**
     * 批量更新兼容性状态
     */
    int batchUpdateStatus(@Param("ids") List<Integer> ids, @Param("compatibilityStatus") String compatibilityStatus);

    /**
     * 更新注意事项ID列表
     */
    void updateNoticeIds(@Param("id") Integer id, @Param("noticeIds") List<Integer> noticeIds);
}
