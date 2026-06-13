package com.it.yts_project.service;

import com.it.yts_project.model.WearResistantGoldPaperMapping;

import java.util.List;

/**
 * 耐磨金纸映射服务接口
 */
public interface WearResistantGoldPaperMappingService {
    
    /**
     * 获取所有映射
     */
    List<WearResistantGoldPaperMapping> getAllMappings();
    
    /**
     * 分页获取映射
     */
    List<WearResistantGoldPaperMapping> getMappingsWithPagination(int page, int size);
    
    /**
     * 获取总记录数
     */
    int getTotalCount();
    
    /**
     * 根据ID获取映射
     */
    WearResistantGoldPaperMapping getMappingById(Integer id);
    
    /**
     * 创建映射
     */
    WearResistantGoldPaperMapping createMapping(WearResistantGoldPaperMapping mapping);
    
    /**
     * 更新映射
     */
    WearResistantGoldPaperMapping updateMapping(WearResistantGoldPaperMapping mapping);
    
    /**
     * 删除映射
     */
    void deleteMapping(Integer id);
    
    /**
     * 批量删除映射
     */
    void batchDeleteMappings(List<Integer> ids);
    
    /**
     * 根据燙金紙系列搜索
     */
    List<WearResistantGoldPaperMapping> searchByProductName(String productName);
    
    /**
     * 根据产品型号搜索
     */
    List<WearResistantGoldPaperMapping> searchByProductModelNumber(String productModelNumber);
    
    /**
     * 根据耐磨金纸类型搜索
     */
    List<WearResistantGoldPaperMapping> searchByGoldPaperType(String goldPaperType);
    
    /**
     * 搜索（支持多条件）
     */
    List<WearResistantGoldPaperMapping> search(String productName, String productModelNumber, String goldPaperType);
    
    /**
     * 根据唯一键查找记录
     */
    WearResistantGoldPaperMapping findByUniqueKey(String productName, String productModelNumber, String goldPaperType);
    
    /**
     * 批量更新兼容性状态
     */
    void batchUpdateStatus(List<Integer> ids, String compatibilityStatus);
    
    /**
     * 获取所有可用的耐磨金纸类型
     */
    List<String> getAllGoldPaperTypes();
    
    /**
     * 验证产品系列是否存在
     */
    boolean validateProductName(String productName);
    
    /**
     * 验证产品型号是否存在
     */
    boolean validateProductModelNumber(String productModelNumber);
    
    /**
     * 验证产品型号是否属于指定系列
     */
    boolean validateProductModelBelongsToSeries(String productName, String productModelNumber);
}


