package com.it.yts_project.mapper;

import com.it.yts_project.model.HotStampingPatternBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HotStampingPatternBaseMapper {

    /**
     * 获取所有激活的烫金图案基础信息
     */
    @Select("SELECT * FROM hot_stamping_pattern_base WHERE is_active = true ORDER BY sort_order, id")
    List<HotStampingPatternBase> getActivePatterns();

    /**
     * 获取所有烫金图案基础信息
     */
    @Select("SELECT * FROM hot_stamping_pattern_base ORDER BY sort_order, id")
    List<HotStampingPatternBase> getAllPatterns();

    /**
     * 根据ID获取烫金图案基础信息
     */
    @Select("SELECT * FROM hot_stamping_pattern_base WHERE id = #{id}")
    HotStampingPatternBase getPatternById(Long id);

    /**
     * 根据ID列表获取烫金图案基础信息
     */
    List<HotStampingPatternBase> getByIds(List<Integer> ids);
}