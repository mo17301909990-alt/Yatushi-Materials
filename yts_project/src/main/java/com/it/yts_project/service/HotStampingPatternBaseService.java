package com.it.yts_project.service;

import com.it.yts_project.model.HotStampingPatternBase;
import java.util.List;

public interface HotStampingPatternBaseService {
    
    /**
     * 获取所有激活的烫金图案基础信息
     */
    List<HotStampingPatternBase> getActivePatterns();
    
    /**
     * 获取所有烫金图案基础信息
     */
    List<HotStampingPatternBase> getAllPatterns();
    
    /**
     * 根据ID获取烫金图案基础信息
     */
    HotStampingPatternBase getPatternById(Long id);
}
