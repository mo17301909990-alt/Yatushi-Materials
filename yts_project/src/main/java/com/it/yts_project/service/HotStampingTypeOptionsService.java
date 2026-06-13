package com.it.yts_project.service;

import com.it.yts_project.model.HotStampingTypeOptions;
import java.util.List;

public interface HotStampingTypeOptionsService {
    
    /**
     * 获取所有激活的烫金类型选项
     */
    List<HotStampingTypeOptions> getActiveTypes();
    
    /**
     * 获取所有烫金类型选项
     */
    List<HotStampingTypeOptions> getAllTypes();
    
    /**
     * 根据ID获取烫金类型选项
     */
    HotStampingTypeOptions getTypeById(Long id);
}
