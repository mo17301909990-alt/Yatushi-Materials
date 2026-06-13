package com.it.yts_project.service;

import com.it.yts_project.dto.ClothPaperCompatibilityDTO;

import java.util.List;

/**
 * 布面纸兼容性服务接口
 */
public interface ClothPaperCompatibilityService {
    
    /**
     * 获取所有兼容性记录
     * @return 兼容性记录列表
     */
    List<ClothPaperCompatibilityDTO> getAllCompatibility();
    
    /**
     * 根据燙金紙系列获取兼容性记录
     * @param projectId 燙金紙系列
     * @return 兼容性记录列表
     */
    List<ClothPaperCompatibilityDTO> getByProjectId(String projectId);
    
    /**
     * 根据布面纸类型ID获取兼容性记录
     * @param clothPaperTypeId 布面纸类型ID
     * @return 兼容性记录列表
     */
    List<ClothPaperCompatibilityDTO> getByClothPaperTypeId(Integer clothPaperTypeId);
    
    /**
     * 根据燙金紙系列和布面纸类型ID获取兼容性记录
     * @param projectId 燙金紙系列
     * @param clothPaperTypeId 布面纸类型ID
     * @return 兼容性记录
     */
    ClothPaperCompatibilityDTO getByProjectAndType(String projectId, Integer clothPaperTypeId);
    
    /**
     * 根据燙金紙系列、布面纸类型ID和燙金紙性能類型获取兼容性记录
     * @param projectId 燙金紙系列
     * @param clothPaperTypeId 布面纸类型ID
     * @param paperType 燙金紙性能類型
     * @return 兼容性记录
     */
    ClothPaperCompatibilityDTO getByProjectAndTypeAndPaperType(String projectId, Integer clothPaperTypeId, String paperType);
    
    /**
     * 创建兼容性记录
     * @param compatibility 兼容性记录
     * @return 创建的兼容性记录
     */
    ClothPaperCompatibilityDTO create(ClothPaperCompatibilityDTO compatibility);
    
    /**
     * 更新兼容性记录
     * @param compatibility 兼容性记录
     * @return 更新后的兼容性记录
     */
    ClothPaperCompatibilityDTO update(ClothPaperCompatibilityDTO compatibility);
    
    /**
     * 删除兼容性记录
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean deleteById(Integer id);
    
    /**
     * 根据燙金紙系列和布面纸类型ID删除兼容性记录
     * @param projectId 燙金紙系列
     * @param clothPaperTypeId 布面纸类型ID
     * @return 是否删除成功
     */
    boolean deleteByProjectAndType(String projectId, Integer clothPaperTypeId);
    
    /**
     * 批量创建兼容性记录
     * @param compatibilities 兼容性记录列表
     * @return 是否创建成功
     */
    boolean batchCreate(List<ClothPaperCompatibilityDTO> compatibilities);
    
    /**
     * 批量更新产品兼容性状态
     * @param projectId 燙金紙系列
     * @param compatibilities 兼容性记录列表
     * @return 是否更新成功
     */
    boolean batchUpdateByProject(String projectId, List<ClothPaperCompatibilityDTO> compatibilities);
    
    /**
     * 获取所有燙金紙系列
     * @return 燙金紙系列列表
     */
    List<String> getAllProductNames();
    
    /**
     * 根据燙金紙系列获取燙金紙系列
     * @param productName 燙金紙系列
     * @return 燙金紙系列
     */
    String getProjectIdByName(String productName);
}
