package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.HotStampingCompatibilityMapper;
import com.it.yts_project.mapper.HotStampingPatternBaseMapper;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.service.MaterialRuleStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class MaterialRuleStatisticsServiceImpl implements MaterialRuleStatisticsService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private HotStampingCompatibilityMapper compatibilityMapper;

    @Autowired
    private HotStampingPatternBaseMapper hotStampingPatternBaseMapper;

    @Override
    public Map<String, Long> getOverviewCounts() {
        Map<String, Long> m = new LinkedHashMap<>(4);
        m.put("hotStampingMaterialCount", productMapper.countAll());
        m.put("productTypeCount", compatibilityMapper.countDistinctProductTypes());
        m.put("hotStampingPatternCount", hotStampingPatternBaseMapper.countAllPatterns());
        m.put("applicableInterfaceCount", compatibilityMapper.countDistinctPaperPerformanceTypes());
        return m;
    }
}
