package com.it.yts_project.service;

import com.it.yts_project.model.PostProcessingPrintUv;
import java.util.List;

/**
 * 印刷UV后加工服务接口
 */
public interface PostProcessingPrintUvService {
    
    /**
     * 获取所有UV印刷配置
     */
    List<PostProcessingPrintUv> getAllUvPrintConfigs();
    
    /**
     * 分页获取UV印刷配置
     */
    List<PostProcessingPrintUv> getUvPrintConfigsWithPagination(int page, int size);
    
    /**
     * 获取总记录数
     */
    int getTotalCount();
    
    /**
     * 根据ID获取UV印刷配置
     */
    PostProcessingPrintUv getUvPrintConfigById(Integer id);
    
    /**
     * 创建UV印刷配置
     */
    PostProcessingPrintUv createUvPrintConfig(PostProcessingPrintUv postProcessingPrintUv);
    
    /**
     * 更新UV印刷配置
     */
    PostProcessingPrintUv updateUvPrintConfig(PostProcessingPrintUv postProcessingPrintUv);
    
    /**
     * 删除UV印刷配置
     */
    void deleteUvPrintConfig(Integer id);
    
    /**
     * 根据燙金紙系列搜索
     */
    List<PostProcessingPrintUv> searchByProductName(String productName);
    
    /**
     * 根据产品型号搜索
     */
    List<PostProcessingPrintUv> searchByProductModelNumber(String productModelNumber);
    
    /**
     * 根据公司编号搜索
     */
    List<PostProcessingPrintUv> searchByCompanyNumber(String companyNumber);
    
    /**
     * 根据纸张类型搜索
     */
    List<PostProcessingPrintUv> searchByPaperType(String paperType);
    
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
     * 
     * @param productName 产品系列
     * @param productModelNumber 产品型号
     * @param companyNumber 公司编号
     * @param paperType 纸张类型
     * @return 兼容性状态 V/X/NA/▷，如果未找到返回null
     */
    String checkPrintUvCompatibility(
        String productName,
        String productModelNumber,
        String companyNumber,
        String paperType
    );
    
    /**
     * 根据唯一键查找记录
     */
    PostProcessingPrintUv findByUniqueKey(
        String productName,
        String productModelNumber,
        String companyNumber,
        String paperType
    );
    
    /**
     * 批量更新兼容性状态
     */
    void batchUpdateStatus(List<Integer> ids, String compatibilityStatus);
    
    /**
     * 根据产品系列名称获取所有公司编号
     */
    List<String> getCompanyNumbersByProductName(String productName);
    
    /**
     * 根据产品型号和名称获取公司编号
     */
    List<String> getCompanyNumbersByProductModelNumberAndName(String productModelNumber, String productName);
    
    /**
     * 验证公司编号是否属于指定的产品系列或型号
     */
    boolean validateCompanyNumber(String companyNumber, String productName, String productModelNumber);
}
