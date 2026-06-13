package com.it.yts_project.mapper;

import com.it.yts_project.dto.ClothPaperTypeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 布料纸类型Mapper接口
 */
@Mapper
public interface ClothPaperTypeMapper {
    
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
    ClothPaperTypeDTO getById(@Param("id") Integer id);
    
    /**
     * 根据分类获取布料纸类型
     * @param category 分类
     * @return 布料纸类型列表
     */
    List<ClothPaperTypeDTO> getByCategory(@Param("category") String category);
    
    /**
     * 插入布料纸类型
     * @param clothPaperType 布料纸类型
     * @return 影响行数
     */
    int insert(ClothPaperTypeDTO clothPaperType);
    
    /**
     * 更新布料纸类型
     * @param clothPaperType 布料纸类型
     * @return 影响行数
     */
    int update(ClothPaperTypeDTO clothPaperType);
    
    /**
     * 根据ID删除布料纸类型
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);
    
    /**
     * 批量更新状态
     * @param ids ID列表
     * @param isActive 激活状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Integer> ids, @Param("isActive") Boolean isActive);
    
    /**
     * 更新排序顺序
     * @param id 主键ID
     * @param sortOrder 排序顺序
     * @return 影响行数
     */
    int updateSortOrder(@Param("id") Integer id, @Param("sortOrder") Integer sortOrder);
    
    /**
     * 根据类型名称和分类获取布料纸类型
     * @param typeName 类型名称
     * @param category 分类
     * @return 布料纸类型
     */
    ClothPaperTypeDTO getByTypeNameAndCategory(@Param("typeName") String typeName, @Param("category") String category);
    
    /**
     * 根据类型名称获取布料纸类型（返回第一个匹配的）
     * @param typeName 类型名称
     * @return 布料纸类型
     */
    ClothPaperTypeDTO getByTypeName(@Param("typeName") String typeName);
}


