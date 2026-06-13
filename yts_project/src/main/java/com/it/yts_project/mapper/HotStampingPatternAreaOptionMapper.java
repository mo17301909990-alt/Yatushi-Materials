package com.it.yts_project.mapper;

import com.it.yts_project.dto.HotStampingPatternAreaOptionDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 烫金图案区域选项Mapper接口
 */
@Mapper
public interface HotStampingPatternAreaOptionMapper {
    
    /**
     * 获取所有激活的烫金图案区域选项
     * @return 烫金图案区域选项列表
     */
    List<HotStampingPatternAreaOptionDTO> getAllActivePatternAreaOptions();

    /**
     * 获取所有烫金图案区域选项
     * @return 烫金图案区域选项列表
     */
    List<HotStampingPatternAreaOptionDTO> getAllPatternAreaOptions();

    /**
     * 根据ID获取烫金图案区域选项
     * @param id 选项ID
     * @return 烫金图案区域选项
     */
    HotStampingPatternAreaOptionDTO getPatternAreaOptionById(@Param("id") Integer id);

    /**
     * 插入烫金图案区域选项
     * @param option 烫金图案区域选项数据
     * @return 插入结果
     */
    int insertPatternAreaOption(HotStampingPatternAreaOptionDTO option);

    /**
     * 更新烫金图案区域选项
     * @param option 烫金图案区域选项数据
     * @return 更新结果
     */
    int updatePatternAreaOption(HotStampingPatternAreaOptionDTO option);

    /**
     * 删除烫金图案区域选项
     * @param id 选项ID
     * @return 删除结果
     */
    int deletePatternAreaOption(@Param("id") Integer id);

    /**
     * 批量删除烫金图案区域选项
     * @param ids 选项ID列表
     * @return 删除结果
     */
    int batchDeletePatternAreaOptions(@Param("ids") List<Integer> ids);
    
    /**
     * 根据选项名称获取烫金图案区域选项
     * @param optionName 选项名称
     * @return 烫金图案区域选项
     */
    HotStampingPatternAreaOptionDTO getPatternAreaOptionByName(@Param("optionName") String optionName);
    
    /**
     * 根据图案类型获取烫金图案区域选项
     * @param patternType 图案类型
     * @return 烫金图案区域选项
     */
    HotStampingPatternAreaOptionDTO getPatternAreaOptionByPatternType(@Param("patternType") String patternType);
}
