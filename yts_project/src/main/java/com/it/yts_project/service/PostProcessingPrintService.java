package com.it.yts_project.service;

import com.it.yts_project.model.PostProcessingPrint;

import java.util.List;

/**
 * 印刷后加工服务接口
 */
public interface PostProcessingPrintService {
    
    /**
     * 获取所有印刷配置
     */
    List<PostProcessingPrint> getAllPrintConfigs();
    
    /**
     * 分页获取印刷配置
     */
    List<PostProcessingPrint> getPrintConfigsWithPagination(int page, int size);
    
    /**
     * 获取总记录数
     */
    int getTotalCount();
    
    /**
     * 根据ID获取印刷配置
     */
    PostProcessingPrint getPrintConfigById(Integer id);
    
    /**
     * 创建印刷配置
     */
    PostProcessingPrint createPrintConfig(PostProcessingPrint postProcessingPrint);
    
    /**
     * 更新印刷配置
     */
    PostProcessingPrint updatePrintConfig(PostProcessingPrint postProcessingPrint);
    
    /**
     * 删除印刷配置
     */
    void deletePrintConfig(Integer id);
    
    /**
     * 批量删除印刷配置
     */
    void batchDeletePrintConfigs(List<Integer> ids);
    
    /**
     * 根据燙金紙系列搜索
     */
    List<PostProcessingPrint> searchByProductName(String productName);
    
    /**
     * 根据产品型号搜索
     */
    List<PostProcessingPrint> searchByProductModelNumber(String productModelNumber);
    
    /**
     * 根据颜色搜索
     */
    List<PostProcessingPrint> searchByColor(String color);
    
    /**
     * 根据布面纸类型ID搜索
     */
    List<PostProcessingPrint> searchByClothPaperTypeId(Integer clothPaperTypeId);
    
    /**
     * 获取所有可用的燙金紙系列
     */
    List<String> getAllProductNames();
    
    /**
     * 获取所有可用的产品型号
     */
    List<String> getAllProductModelNumbers();
    
    /**
     * 获取所有可用的颜色
     */
    List<String> getAllColors();
    
    /**
     * 根据产品名称和型号获取颜色选项
     */
    List<String> getColorsByProductAndModel(String productName, String productModelNumber);
    
    /**
     * 根据产品名称获取颜色选项
     */
    List<String> getColorsByProductName(String productName);
    
    /**
     * 根据产品型号获取颜色选项
     */
    List<String> getColorsByProductModelNumber(String productModelNumber);
    
    /**
     * 根据唯一键查找记录
     */
    PostProcessingPrint findByUniqueKey(
        String productName,
        String productModelNumber,
        String color,
        Integer clothPaperTypeId,
        String paperType
    );
    
    /**
     * 批量更新兼容性状态
     */
    void batchUpdateStatus(List<Integer> ids, String compatibilityStatus);
    
    /**
     * 验证颜色是否存在且属于指定的产品
     */
    boolean validateColor(String color, String productName, String productModelNumber);
}