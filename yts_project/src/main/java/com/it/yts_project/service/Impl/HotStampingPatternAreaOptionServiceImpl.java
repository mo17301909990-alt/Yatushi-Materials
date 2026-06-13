package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.HotStampingPatternAreaOptionDTO;
import com.it.yts_project.mapper.HotStampingPatternAreaOptionMapper;
import com.it.yts_project.model.HotStampingPatternAreaCompatibility;
import com.it.yts_project.service.HotStampingPatternAreaOptionService;
import com.it.yts_project.service.HotStampingPatternAreaCompatibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 烫金图案区域选项服务实现类
 */
@Service
public class HotStampingPatternAreaOptionServiceImpl implements HotStampingPatternAreaOptionService {
    
    @Autowired
    private HotStampingPatternAreaOptionMapper hotStampingPatternAreaOptionMapper;
    
    @Autowired
    private HotStampingPatternAreaCompatibilityService hotStampingPatternAreaCompatibilityService;
    
    @Override
    public List<HotStampingPatternAreaOptionDTO> getAllActivePatternAreaOptions() {
        return hotStampingPatternAreaOptionMapper.getAllActivePatternAreaOptions();
    }

    @Override
    public List<HotStampingPatternAreaOptionDTO> getAllPatternAreaOptions() {
        return hotStampingPatternAreaOptionMapper.getAllPatternAreaOptions();
    }

    @Override
    public HotStampingPatternAreaOptionDTO getPatternAreaOptionById(Integer id) {
        return hotStampingPatternAreaOptionMapper.getPatternAreaOptionById(id);
    }

    @Override
    public HotStampingPatternAreaOptionDTO createPatternAreaOption(HotStampingPatternAreaOptionDTO option) {
        hotStampingPatternAreaOptionMapper.insertPatternAreaOption(option);
        return option;
    }

    @Override
    public HotStampingPatternAreaOptionDTO updatePatternAreaOption(Integer id, HotStampingPatternAreaOptionDTO option) {
        option.setId(id);
        int updated = hotStampingPatternAreaOptionMapper.updatePatternAreaOption(option);
        if (updated > 0) {
            return hotStampingPatternAreaOptionMapper.getPatternAreaOptionById(id);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deletePatternAreaOptionWithMappings(Integer id) {
        // 先删除关联的映射关系
        List<HotStampingPatternAreaCompatibility> compatibilities = 
            hotStampingPatternAreaCompatibilityService.getCompatibilityByPatternAreaId(id);
        int mappingCount = compatibilities != null ? compatibilities.size() : 0;
        
        if (mappingCount > 0) {
            hotStampingPatternAreaCompatibilityService.deleteCompatibilityByPatternAreaId(id);
        }
        
        // 再删除图案区域选项
        hotStampingPatternAreaOptionMapper.deletePatternAreaOption(id);
        
        return mappingCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePatternAreaOption(Integer id) {
        int deleted = hotStampingPatternAreaOptionMapper.deletePatternAreaOption(id);
        return deleted > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeletePatternAreaOptionsWithMappings(List<Integer> ids) {
        int totalMappingCount = 0;
        
        // 先删除所有关联的映射关系
        for (Integer id : ids) {
            List<HotStampingPatternAreaCompatibility> compatibilities = 
                hotStampingPatternAreaCompatibilityService.getCompatibilityByPatternAreaId(id);
            int mappingCount = compatibilities != null ? compatibilities.size() : 0;
            totalMappingCount += mappingCount;
            
            if (mappingCount > 0) {
                hotStampingPatternAreaCompatibilityService.deleteCompatibilityByPatternAreaId(id);
            }
        }
        
        // 再批量删除图案区域选项
        hotStampingPatternAreaOptionMapper.batchDeletePatternAreaOptions(ids);
        
        return totalMappingCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeletePatternAreaOptions(List<Integer> ids) {
        hotStampingPatternAreaOptionMapper.batchDeletePatternAreaOptions(ids);
    }
    
    @Override
    public HotStampingPatternAreaOptionDTO getPatternAreaOptionByName(String optionName) {
        return hotStampingPatternAreaOptionMapper.getPatternAreaOptionByName(optionName);
    }
    
    @Override
    public HotStampingPatternAreaOptionDTO getPatternAreaOptionByPatternType(String patternType) {
        return hotStampingPatternAreaOptionMapper.getPatternAreaOptionByPatternType(patternType);
    }
}
