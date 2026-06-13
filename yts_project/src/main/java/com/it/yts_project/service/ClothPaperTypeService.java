package com.it.yts_project.service;

import com.it.yts_project.dto.ClothPaperTypeDTO;

import java.util.List;

/**
 * 布料纸类型服务接口
 */
public interface ClothPaperTypeService {
    
    /**
     * 获取所有激活的布料纸类型
     * @return 布料纸类型列表
     */
    List<ClothPaperTypeDTO> getAllActiveClothPaperTypes();
    
    /**
     * 获取所有布料纸类型（包括未激活的）
     * @return 布料纸类型列表
     */
    List<ClothPaperTypeDTO> getAllClothPaperTypes();
    
    /**
     * 根据ID获取布料纸类型
     * @param id 主键ID
     * @return 布料纸类型
     */
    ClothPaperTypeDTO getById(Integer id);
    
    /**
     * 根据分类获取布料纸类型
     * @param category 分类
     * @return 布料纸类型列表
     */
    List<ClothPaperTypeDTO> getByCategory(String category);
    
    /**
     * 创建布料纸类型
     * @param clothPaperType 布料纸类型
     * @return 创建的布料纸类型
     */
    ClothPaperTypeDTO create(ClothPaperTypeDTO clothPaperType);
    
    /**
     * 更新布料纸类型
     * @param clothPaperType 布料纸类型
     * @return 更新后的布料纸类型
     */
    ClothPaperTypeDTO update(ClothPaperTypeDTO clothPaperType);
    
    /**
     * 删除布料纸类型
     * @param id 主键ID
     * @return 是否删除成功
     */
    boolean deleteById(Integer id);
    
    /**
     * 批量更新状态
     * @param ids ID列表
     * @param isActive 激活状态
     * @return 是否更新成功
     */
    boolean batchUpdateStatus(List<Integer> ids, Boolean isActive);
    
    /**
     * 更新排序顺序
     * @param id 主键ID
     * @param sortOrder 排序顺序
     * @return 是否更新成功
     */
    boolean updateSortOrder(Integer id, Integer sortOrder);
    
    /**
     * 根据类型名称和分类获取布料纸类型
     * @param typeName 类型名称
     * @param category 分类
     * @return 布料纸类型
     */
    ClothPaperTypeDTO getByTypeNameAndCategory(String typeName, String category);
    
    /**
     * 根据类型名称获取布料纸类型（返回第一个匹配的）
     * @param typeName 类型名称
     * @return 布料纸类型
     */
    ClothPaperTypeDTO getByTypeName(String typeName);
}


