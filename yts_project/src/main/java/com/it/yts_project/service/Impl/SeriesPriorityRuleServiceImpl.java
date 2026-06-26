package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.SeriesPriorityRuleDTO;
import com.it.yts_project.dto.SeriesPriorityRuleItemDTO;
import com.it.yts_project.mapper.ClothPaperTypeMapper;
import com.it.yts_project.mapper.SeriesPriorityRuleItemMapper;
import com.it.yts_project.mapper.SeriesPriorityRuleMapper;
import com.it.yts_project.service.SeriesPriorityRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class SeriesPriorityRuleServiceImpl implements SeriesPriorityRuleService {

    @Autowired
    private SeriesPriorityRuleMapper seriesPriorityRuleMapper;
    @Autowired
    private SeriesPriorityRuleItemMapper seriesPriorityRuleItemMapper;
    @Autowired
    private ClothPaperTypeMapper clothPaperTypeMapper;

    @Override
    public List<SeriesPriorityRuleItemDTO> getRuleItemsByRuleId(Integer ruleId) {
        if (ruleId == null) return Collections.emptyList();
        return seriesPriorityRuleItemMapper.selectByRuleId(ruleId);
    }

    @Override
    public List<SeriesPriorityRuleDTO> listRules() {
        return seriesPriorityRuleMapper.selectAll();
    }

    @Override
    public SeriesPriorityRuleDTO getRuleWithItems(Integer ruleId) {
        SeriesPriorityRuleDTO rule = seriesPriorityRuleMapper.selectById(ruleId);
        if (rule == null) return null;
        rule.setItems(seriesPriorityRuleItemMapper.selectByRuleId(ruleId));
        return rule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SeriesPriorityRuleDTO createRule(String ruleName, Integer sortOrder, Boolean isActive) {
        if (ruleName == null || ruleName.trim().isEmpty()) {
            throw new IllegalArgumentException("Rule name is required");
        }
        String ruleCode = "r_" + System.currentTimeMillis();
        SeriesPriorityRuleDTO dto = new SeriesPriorityRuleDTO();
        dto.setRuleCode(ruleCode);
        dto.setRuleName(ruleName.trim());
        dto.setSortOrder(sortOrder != null ? sortOrder : 0);
        dto.setIsActive(isActive != null ? isActive : true);
        seriesPriorityRuleMapper.insert(dto);
        Integer ruleId = dto.getId();
        if (ruleId == null) return dto;
        // 默认三档：1 档、2 档、其余按价格（系列名用户可改）
        seriesPriorityRuleItemMapper.insert(item(ruleId, 1, "SK", false));
        seriesPriorityRuleItemMapper.insert(item(ruleId, 2, "L817,L817/GB", false));
        seriesPriorityRuleItemMapper.insert(item(ruleId, 3, "", true));
        return getRuleWithItems(ruleId);
    }

    private static SeriesPriorityRuleItemDTO item(Integer ruleId, int order, String seriesNames, boolean isPriceFallback) {
        SeriesPriorityRuleItemDTO dto = new SeriesPriorityRuleItemDTO();
        dto.setRuleId(ruleId);
        dto.setPriorityOrder(order);
        dto.setSeriesNames(seriesNames);
        dto.setIsPriceFallback(isPriceFallback);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRule(Integer id, String ruleName, Integer sortOrder, Boolean isActive) {
        if (id == null) return;
        seriesPriorityRuleMapper.updateById(id, ruleName, sortOrder, isActive);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRule(Integer id) {
        if (id == null) return;
        clothPaperTypeMapper.unsetSeriesPriorityRuleId(id);
        seriesPriorityRuleItemMapper.deleteByRuleId(id);
        seriesPriorityRuleMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRuleItems(Integer ruleId, List<SeriesPriorityRuleItemDTO> items) {
        if (ruleId == null) return;
        if (items == null || items.isEmpty()) {
            seriesPriorityRuleItemMapper.deleteByRuleId(ruleId);
            return;
        }
        long priceFallbackCount = items.stream().filter(i -> Boolean.TRUE.equals(i.getIsPriceFallback())).count();
        if (priceFallbackCount != 1) {
            throw new IllegalArgumentException("Exactly one item must have isPriceFallback=true (其余按价格). Current count: " + priceFallbackCount);
        }
        seriesPriorityRuleItemMapper.deleteByRuleId(ruleId);
        for (SeriesPriorityRuleItemDTO item : items) {
            item.setRuleId(ruleId);
            item.setId(null);
            // 当 isPriceFallback=true 时，确保 series_names 为空字符串而不是 null（数据库 NOT NULL 约束）
            if (Boolean.TRUE.equals(item.getIsPriceFallback())) {
                item.setSeriesNames("");
            } else if (item.getSeriesNames() == null) {
                // 非价格回退项，如果 series_names 为 null，也设置为空字符串
                item.setSeriesNames("");
            }
            seriesPriorityRuleItemMapper.insert(item);
        }
    }
}
