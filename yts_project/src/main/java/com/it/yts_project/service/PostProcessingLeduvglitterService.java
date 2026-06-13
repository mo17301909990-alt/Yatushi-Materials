package com.it.yts_project.service;

import com.it.yts_project.model.PostProcessingLeduvglitter;
import java.util.List;

/**
 * 絲印LED UV灑閃粉后加工服务接口
 */
public interface PostProcessingLeduvglitterService {
    
    /**
     * 获取所有LED UV灑閃粉配置
     */
    List<PostProcessingLeduvglitter> getAllLeduvglitterConfigs();
    
    /**
     * 分页获取LED UV灑閃粉配置
     */
    List<PostProcessingLeduvglitter> getLeduvglitterConfigsWithPagination(int page, int size);
    
    /**
     * 获取总记录数
     */
    int getTotalCount();
    
    /**
     * 根据ID获取LED UV灑閃粉配置
     */
    PostProcessingLeduvglitter getLeduvglitterConfigById(Integer id);
    
    /**
     * 创建LED UV灑閃粉配置
     */
    PostProcessingLeduvglitter createLeduvglitterConfig(PostProcessingLeduvglitter postProcessingLeduvglitter);
    
    /**
     * 更新LED UV灑閃粉配置
     */
    PostProcessingLeduvglitter updateLeduvglitterConfig(PostProcessingLeduvglitter postProcessingLeduvglitter);
    
    /**
     * 删除LED UV灑閃粉配置
     */
    void deleteLeduvglitterConfig(Integer id);
    
    /**
     * 根据燙金紙系列搜索
     */
    List<PostProcessingLeduvglitter> searchByProductName(String productName);
    
    /**
     * 根据产品型号搜索
     */
    List<PostProcessingLeduvglitter> searchByProductModelNumber(String productModelNumber);
    
    /**
     * 根据布面纸类型ID搜索
     */
    List<PostProcessingLeduvglitter> searchByClothPaperTypeId(Integer clothPaperTypeId);
    
    /**
     * 根据纸张类型搜索
     */
    List<PostProcessingLeduvglitter> searchByPaperType(String paperType);
    
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
     * 
     * @param productName 产品系列
     * @param productModelNumber 产品型号
     * @param clothPaperTypeId 适用界面ID
     * @param paperType 纸张类型
     * @return 兼容性状态 V/X/NA/▷，如果未找到返回null
     */
    String checkLeduvglitterCompatibility(
        String productName,
        String productModelNumber,
        Integer clothPaperTypeId,
        String paperType
    );
    
    /**
     * 根据唯一键查找记录（用于导入时的更新判断）
     */
    PostProcessingLeduvglitter findByUniqueKey(
        String productName,
        String productModelNumber,
        Integer clothPaperTypeId,
        String paperType
    );
    
    /**
     * 批量更新兼容性状态
     */
    void batchUpdateStatus(List<Integer> ids, String compatibilityStatus);
}