package com.it.yts_project.mapper;

import com.it.yts_project.dto.ClothPaperCompatibilityDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 布面纸兼容性Mapper接口
 */
@Mapper
public interface ClothPaperCompatibilityMapper {

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
    List<ClothPaperCompatibilityDTO> getByProjectId(@Param("projectId") String projectId);

    /**
     * 根据布面纸类型ID获取兼容性记录
     * @param clothPaperTypeId 布面纸类型ID
     * @return 兼容性记录列表
     */
    List<ClothPaperCompatibilityDTO> getByClothPaperTypeId(@Param("clothPaperTypeId") Integer clothPaperTypeId);

    /**
     * 根据燙金紙系列和布面纸类型ID获取兼容性记录
     * @param projectId 燙金紙系列
     * @param clothPaperTypeId 布面纸类型ID
     * @return 兼容性记录
     */
    ClothPaperCompatibilityDTO getByProjectAndType(@Param("projectId") String projectId, @Param("clothPaperTypeId") Integer clothPaperTypeId);
    
    /**
     * 根据燙金紙系列、布面纸类型ID和燙金紙性能類型获取兼容性记录
     * @param projectId 燙金紙系列
     * @param clothPaperTypeId 布面纸类型ID
     * @param paperType 燙金紙性能類型
     * @return 兼容性记录
     */
    ClothPaperCompatibilityDTO getByProjectAndTypeAndPaperType(
        @Param("projectId") String projectId, 
        @Param("clothPaperTypeId") Integer clothPaperTypeId,
        @Param("paperType") String paperType);

    /**
     * 插入兼容性记录
     * @param compatibility 兼容性记录
     * @return 影响行数
     */
    int insert(ClothPaperCompatibilityDTO compatibility);

    /**
     * 更新兼容性记录
     * @param compatibility 兼容性记录
     * @return 影响行数
     */
    int update(ClothPaperCompatibilityDTO compatibility);

    /**
     * 根据ID删除兼容性记录
     * @param id 主键ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 根据燙金紙系列和布面纸类型ID删除兼容性记录
     * @param projectId 燙金紙系列
     * @param clothPaperTypeId 布面纸类型ID
     * @return 影响行数
     */
    int deleteByProjectAndType(@Param("projectId") String projectId, @Param("clothPaperTypeId") Integer clothPaperTypeId);

    /**
     * 批量插入兼容性记录
     * @param compatibilities 兼容性记录列表
     * @return 影响行数
     */
    int batchInsert(@Param("compatibilities") List<ClothPaperCompatibilityDTO> compatibilities);

    /**
     * 批量更新兼容性状态
     * @param projectId 燙金紙系列
     * @param compatibilities 兼容性记录列表
     * @return 影响行数
     */
    int batchUpdateByProject(@Param("projectId") String projectId, @Param("compatibilities") List<ClothPaperCompatibilityDTO> compatibilities);

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
    String getProjectIdByName(@Param("productName") String productName);
}
