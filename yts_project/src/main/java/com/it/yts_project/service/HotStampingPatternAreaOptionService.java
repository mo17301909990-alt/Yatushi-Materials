package com.it.yts_project.service;

import com.it.yts_project.dto.HotStampingPatternAreaOptionDTO;

import java.util.List;

/**
 * 烫金图案区域选项服务接口
 */
public interface HotStampingPatternAreaOptionService {
    
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
    HotStampingPatternAreaOptionDTO getPatternAreaOptionById(Integer id);

    /**
     * 创建烫金图案区域选项
     * @param option 烫金图案区域选项数据
     * @return 创建的烫金图案区域选项
     */
    HotStampingPatternAreaOptionDTO createPatternAreaOption(HotStampingPatternAreaOptionDTO option);

    /**
     * 更新烫金图案区域选项
     * @param id 选项ID
     * @param option 烫金图案区域选项数据
     * @return 更新后的烫金图案区域选项
     */
    HotStampingPatternAreaOptionDTO updatePatternAreaOption(Integer id, HotStampingPatternAreaOptionDTO option);

    /**
     * 删除烫金图案区域选项（包含级联删除映射关系）
     * @param id 选项ID
     * @return 删除的映射数量
     */
    int deletePatternAreaOptionWithMappings(Integer id);

    /**
     * 删除烫金图案区域选项
     * @param id 选项ID
     * @return 删除结果
     */
    boolean deletePatternAreaOption(Integer id);

    /**
     * 批量删除烫金图案区域选项（包含级联删除映射关系）
     * @param ids 选项ID列表
     * @return 删除的映射总数
     */
    int batchDeletePatternAreaOptionsWithMappings(List<Integer> ids);

    /**
     * 批量删除烫金图案区域选项
     * @param ids 选项ID列表
     */
    void batchDeletePatternAreaOptions(List<Integer> ids);
    
    /**
     * 根据选项名称获取烫金图案区域选项
     * @param optionName 选项名称
     * @return 烫金图案区域选项
     */
    HotStampingPatternAreaOptionDTO getPatternAreaOptionByName(String optionName);
    
    /**
     * 根据图案类型获取烫金图案区域选项
     * @param patternType 图案类型
     * @return 烫金图案区域选项
     */
    HotStampingPatternAreaOptionDTO getPatternAreaOptionByPatternType(String patternType);
}
