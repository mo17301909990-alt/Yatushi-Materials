package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.CompatibilityQueryParams;
import com.it.yts_project.mapper.HotStampingCompatibilityMapper;
import com.it.yts_project.mapper.HotStampingPatternBaseMapper;
import com.it.yts_project.mapper.HotStampingTypeCompatibilityMapper;
import com.it.yts_project.mapper.LaminationCompatibilityMapper;
import com.it.yts_project.model.HotStampingCompatibility;
import com.it.yts_project.model.HotStampingPatternBase;
import com.it.yts_project.model.LaminationCompatibility;
import com.it.yts_project.model.LaminationMaterialOptions;
import com.it.yts_project.model.PostProcessingOptions;
import com.it.yts_project.service.HotStampingCompatibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 烫金工艺兼容性服务实现类
 */
@Service
public class HotStampingCompatibilityServiceImpl implements HotStampingCompatibilityService {
    
    @Autowired
    private HotStampingCompatibilityMapper compatibilityMapper;
    
    @Autowired
    private LaminationCompatibilityMapper laminationCompatibilityMapper;
    
    @Autowired
    private HotStampingPatternBaseMapper hotStampingPatternBaseMapper;
    
    @Autowired
    private HotStampingTypeCompatibilityMapper hotStampingTypeCompatibilityMapper;
    
    @Override
    public List<HotStampingCompatibility> getCompatibleHotStampingTypes(CompatibilityQueryParams params) {
        return compatibilityMapper.findCompatibleHotStampingTypes(params);
    }
    
    @Override
    public List<String> getAllPaperPerformanceTypes() {
        return compatibilityMapper.getAllPaperPerformanceTypes();
    }
    
    @Override
    public List<String> getAllProductTypes() {
        return compatibilityMapper.getAllProductTypes();
    }
    
    @Override
    public List<String> getAllHotStampingTypes() {
        return compatibilityMapper.getAllHotStampingTypes();
    }
    
    @Override
    public List<String> getRecommendedHotStampingTypes(CompatibilityQueryParams params) {
        // 智能匹配兼容的烫金类型
        List<HotStampingCompatibility> compatibilities = compatibilityMapper.matchPatternCompatibility(
            params.getProductType(),
            params.getPatternCharacteristic(),
            params.getHotStampingType()
        );
        
        return compatibilities.stream()
            .map(HotStampingCompatibility::getPaperPerformance)
            .distinct()
            .collect(Collectors.toList());
    }
    
    // ========== 过胶兼容性相关方法实现 ==========
    
    @Override
    public List<LaminationCompatibility> getLaminationCompatibility(CompatibilityQueryParams params) {
        return laminationCompatibilityMapper.findLaminationCompatibility(params);
    }
    
    @Override
    public List<LaminationMaterialOptions> getCompatibleLaminationMaterials(
            String foilSeries, Integer postProcessingStepId, Integer interfaceTypeId) {
        return laminationCompatibilityMapper.findCompatibleMaterials(
            foilSeries, postProcessingStepId, interfaceTypeId);
    }
    
    @Override
    public List<PostProcessingOptions> getCompatiblePostProcessingSteps(
            String foilSeries, Integer laminationMaterialId, Integer interfaceTypeId) {
        return laminationCompatibilityMapper.findCompatiblePostProcessingSteps(
            foilSeries, laminationMaterialId, interfaceTypeId);
    }
    
    @Override
    public List<LaminationMaterialOptions> getAllLaminationMaterials() {
        return laminationCompatibilityMapper.getAllLaminationMaterials();
    }
    
    @Override
    public List<PostProcessingOptions> getAllPostProcessingSteps() {
        return laminationCompatibilityMapper.getAllPostProcessingSteps();
    }
    
    @Override
    public Character checkLaminationCompatibility(
            String foilSeries, Integer postProcessingStepId, 
            Integer laminationMaterialId, Integer interfaceTypeId) {
        return laminationCompatibilityMapper.checkLaminationCompatibility(
            foilSeries, postProcessingStepId, laminationMaterialId, interfaceTypeId);
    }

    @Override
    public java.util.List<HotStampingPatternBase> getPatternOptionsByProductTypeId(Integer productTypeId) {
        java.util.List<Integer> patternIds = compatibilityMapper.getPatternCharacteristicIdsByProductTypeId(productTypeId);
        if (patternIds == null || patternIds.isEmpty()) {
            return java.util.List.of();
        }
        return hotStampingPatternBaseMapper.getByIds(patternIds);
    }
    
    @Override
    public List<HotStampingPatternBase> getAllPatternOptions() {
        return hotStampingPatternBaseMapper.getAllPatterns();
    }
    
    @Override
    public List<com.it.yts_project.dto.HotStampingTypeGroupDTO> getAllHotStampingTypeGroups() {
        List<com.it.yts_project.dto.HotStampingTypeGroupDTO> groups = compatibilityMapper.getAllHotStampingTypeGroups();
        
        // 为每个分组获取选项数据
        for (com.it.yts_project.dto.HotStampingTypeGroupDTO group : groups) {
            List<com.it.yts_project.model.HotStampingTypeOptions> options = 
                compatibilityMapper.getPositionOptionsByStampingType(group.getStampingType());
            
            // 构建选项列表
            List<com.it.yts_project.dto.HotStampingTypeOptionDTO> optionList = new java.util.ArrayList<>();
            for (com.it.yts_project.model.HotStampingTypeOptions option : options) {
                com.it.yts_project.dto.HotStampingTypeOptionDTO optionDTO = new com.it.yts_project.dto.HotStampingTypeOptionDTO();
                optionDTO.setId(option.getId());
                optionDTO.setDisplayName(option.getPositionType() == null ? 
                    "默认" : 
                    option.getPositionType());
                optionDTO.setPositionType(option.getPositionType());
                optionDTO.setDescription(option.getDescription());
                optionDTO.setSortOrder(option.getSortOrder());
                optionList.add(optionDTO);
            }
            
            group.setOptions(optionList);
        }
        
        return groups;
    }
    
    @Override
    public List<com.it.yts_project.model.HotStampingTypeOptions> getPositionOptionsByStampingType(String stampingType) {
        return compatibilityMapper.getPositionOptionsByStampingType(stampingType);
    }

    @Override
    public List<String> getPaperPerformanceByMultipleIds(
            Integer productTypeId,
            Integer patternCharacteristicId,
            Integer hotStampingTypeId,
            Integer preProcessingStepId,
            Integer postProcessingStepId) {
        return compatibilityMapper.getPaperPerformanceByMultipleIds(
                productTypeId,
                patternCharacteristicId,
                hotStampingTypeId,
                preProcessingStepId,
                postProcessingStepId
        );
    }

    @Override
    public boolean hasDurableMapping(Integer hotStampingTypeId) {
        if (hotStampingTypeId == null) {
            return false;
        }
        return compatibilityMapper.countByHotStampingTypeId(hotStampingTypeId) > 0;
    }

    @Override
    public boolean hasCommonMapping(Integer hotStampingTypeId) {
        if (hotStampingTypeId == null) {
            return false;
        }
        return hotStampingTypeCompatibilityMapper.countByHotStampingTypeId(hotStampingTypeId) > 0;
    }

}
