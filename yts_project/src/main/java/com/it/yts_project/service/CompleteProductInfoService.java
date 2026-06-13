package com.it.yts_project.service;

import com.it.yts_project.model.CompleteProductInfo;

import java.util.List;

/**
 * 完整产品信息服务接口
 */
public interface CompleteProductInfoService {
    
    /**
     * 获取所有完整产品信息
     */
    List<CompleteProductInfo> findAllCompleteProductInfo();
    
    /**
     * 根据产品ID获取完整产品信息
     */
    CompleteProductInfo findCompleteProductInfoById(Integer id);
    
    /**
     * 根据物料编号获取完整产品信息
     */
    List<CompleteProductInfo> findCompleteProductInfoByMaterialNumber(String materialNumber);
    
    /**
     * 根据型号获取完整产品信息
     */
    List<CompleteProductInfo> findCompleteProductInfoByModelNumber(String modelNumber);
    
    /**
     * 根据Leo Touch编号获取完整产品信息
     */
    List<CompleteProductInfo> findCompleteProductInfoByCompanyNumber(String companyNumber);
    
    /**
     * 根据SPNO获取完整产品信息
     */
    List<CompleteProductInfo> findCompleteProductInfoByGpNo(String gpNo);
    
    /**
     * 根据烫金纸张类型获取完整产品信息
     */
    List<CompleteProductInfo> findCompleteProductInfoByHotStampingPaperType(String hotStampingPaperType);
    
    /**
     * 保存完整产品信息
     */
    CompleteProductInfo saveCompleteProductInfo(CompleteProductInfo completeProductInfo);
    
    /**
     * 更新完整产品信息
     */
    CompleteProductInfo updateCompleteProductInfo(Integer id, CompleteProductInfo completeProductInfo);
    
    /**
     * 批量更新产品信息
     * @param ids 产品ID列表
     * @param updateFields 要更新的字段（只包含需要更新的字段）
     * @return 更新成功的数量
     */
    /**
     * 批量更新产品（部分成功模式）
     * @param ids 产品ID列表
     * @param updateFields 要更新的字段
     * @return 包含成功数量、失败数量和错误信息的Map
     */
    java.util.Map<String, Object> batchUpdateProducts(List<Integer> ids, java.util.Map<String, Object> updateFields);
    
    /**
     * 批量删除产品
     * @param ids 产品ID列表
     * @return 删除成功的数量
     */
    /**
     * 批量删除产品（部分成功模式）
     * @param ids 产品ID列表
     * @return 包含成功数量、失败数量和错误信息的Map
     */
    java.util.Map<String, Object> batchDeleteProducts(List<Integer> ids);
}
