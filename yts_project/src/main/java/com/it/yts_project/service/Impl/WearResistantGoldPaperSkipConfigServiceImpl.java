package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.WearResistantGoldPaperSkipConfigMapper;
import com.it.yts_project.service.WearResistantGoldPaperSkipConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 耐磨金纸映射「跳过 Match 耐磨规则」配置服务实现
 */
@Service
public class WearResistantGoldPaperSkipConfigServiceImpl implements WearResistantGoldPaperSkipConfigService {

    @Autowired
    private WearResistantGoldPaperSkipConfigMapper mapper;

    @Override
    public List<String> getAllSkipPaperTypes() {
        List<String> types = mapper.getAllSkipPaperTypes();
        if (types == null || types.isEmpty()) {
            return Collections.emptyList();
        }
        // 去重 & 去掉空字符串
        return types.stream()
                .filter(t -> t != null && !t.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveSkipPaperTypes(List<String> paperTypes) {
        // 先清空再插入，保证配置是全量覆盖
        mapper.deleteAll();
        if (paperTypes == null || paperTypes.isEmpty()) {
            return;
        }
        // 过滤空值并去重
        List<String> filtered = paperTypes.stream()
                .filter(t -> t != null && !t.trim().isEmpty())
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
        if (!filtered.isEmpty()) {
            mapper.batchInsert(filtered);
        }
    }
}



