package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.HotStampingPatternAreaCompatibilityMapper;
import com.it.yts_project.model.HotStampingPatternAreaCompatibility;
import com.it.yts_project.service.HotStampingPatternAreaCompatibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 烫金图案区域兼容性服务实现类
 */
@Service
public class HotStampingPatternAreaCompatibilityServiceImpl implements HotStampingPatternAreaCompatibilityService {
    
    @Autowired
    private HotStampingPatternAreaCompatibilityMapper mapper;
    
    @Override
    public List<HotStampingPatternAreaCompatibility> getAllCompatibility() {
        return mapper.findAll();
    }
    
    @Override
    public List<HotStampingPatternAreaCompatibility> getCompatibilityByPatternAreaId(Integer patternAreaId) {
        return mapper.findByPatternAreaId(patternAreaId);
    }
    
    @Override
    public HotStampingPatternAreaCompatibility getCompatibilityById(Integer id) {
        return mapper.findById(id);
    }
    
    @Override
    public HotStampingPatternAreaCompatibility createCompatibility(HotStampingPatternAreaCompatibility compatibility) {
        // 检查是否已存在相同的组合（包含 paper_type）
        HotStampingPatternAreaCompatibility existing = mapper.findByProductNameAndPatternAreaIdAndPaperType(
            compatibility.getProductName(), 
            compatibility.getHotStampingPatternxAreaId(), 
            compatibility.getPaperType()
        );
        
        if (existing != null) {
            throw new org.springframework.dao.DuplicateKeyException(
                "该燙金紙系列、图案区域和烫金纸性能类型的组合已存在"
            );
        }
        
        LocalDateTime now = LocalDateTime.now();
        compatibility.setCreatedAt(now);
        compatibility.setUpdatedAt(now);
        
        mapper.insert(compatibility);
        return compatibility;
    }
    
    @Override
    public HotStampingPatternAreaCompatibility updateCompatibility(HotStampingPatternAreaCompatibility compatibility) {
        // 检查是否已存在相同的组合（排除当前记录）
        HotStampingPatternAreaCompatibility existing = mapper.findByProductNameAndPatternAreaIdAndPaperType(
            compatibility.getProductName(), 
            compatibility.getHotStampingPatternxAreaId(), 
            compatibility.getPaperType()
        );
        
        // 如果存在且不是当前记录，则抛出异常
        if (existing != null && !existing.getId().equals(compatibility.getId())) {
            throw new org.springframework.dao.DuplicateKeyException(
                "该燙金紙系列、图案区域和烫金纸性能类型的组合已存在"
            );
        }
        
        compatibility.setUpdatedAt(LocalDateTime.now());
        mapper.update(compatibility);
        return mapper.findByProductNameAndPatternAreaIdAndPaperType(
            compatibility.getProductName(), 
            compatibility.getHotStampingPatternxAreaId(), 
            compatibility.getPaperType()
        );
    }
    
    @Override
    public void deleteCompatibility(Integer id) {
        mapper.deleteById(id);
    }
    
    @Override
    public void deleteCompatibilityByPatternAreaId(Integer patternAreaId) {
        mapper.deleteByPatternAreaId(patternAreaId);
    }
    
    @Override
    public List<String> getAllProductNames() {
        return mapper.getAllProductNames();
    }
    
    @Override
    public boolean existsByProductNameAndPatternAreaId(String productName, Integer patternAreaId) {
        return mapper.countByProductNameAndPatternAreaId(productName, patternAreaId) > 0;
    }
    
    @Override
    public HotStampingPatternAreaCompatibility getCompatibilityByProductNameAndPatternAreaId(String productName, Integer patternAreaId) {
        return mapper.findByProductNameAndPatternAreaId(productName, patternAreaId);
    }
    
    @Override
    public HotStampingPatternAreaCompatibility getCompatibilityByProductNameAndPatternAreaIdAndPaperType(
            String productName, Integer patternAreaId, String paperType) {
        return mapper.findByProductNameAndPatternAreaIdAndPaperType(productName, patternAreaId, paperType);
    }
}