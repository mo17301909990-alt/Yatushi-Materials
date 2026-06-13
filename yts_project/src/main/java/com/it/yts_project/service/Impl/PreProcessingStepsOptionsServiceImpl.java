package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.PreProcessingStepsOptionsMapper;
import com.it.yts_project.model.PreProcessingStepsOptions;
import com.it.yts_project.service.PreProcessingStepsOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 前工序选项服务实现类
 */
@Service
public class PreProcessingStepsOptionsServiceImpl implements PreProcessingStepsOptionsService {
    
    @Autowired
    private PreProcessingStepsOptionsMapper preProcessingStepsOptionsMapper;
    
    @Override
    public List<PreProcessingStepsOptions> getAllActiveOptions() {
        return preProcessingStepsOptionsMapper.getAllActiveOptions();
    }
    
    @Override
    public PreProcessingStepsOptions getById(Integer id) {
        return preProcessingStepsOptionsMapper.getById(id);
    }
    
    @Override
    public List<PreProcessingStepsOptions> getAllOptions() {
        return preProcessingStepsOptionsMapper.getAllOptions();
    }
    
    @Override
    public PreProcessingStepsOptions create(PreProcessingStepsOptions option) {
        // 检查工艺类型是否重复
        if (option.getProcess() != null && !option.getProcess().trim().isEmpty()) {
            List<PreProcessingStepsOptions> allOptions = preProcessingStepsOptionsMapper.getAllOptions();
            boolean exists = allOptions.stream()
                    .anyMatch(opt -> opt.getProcess() != null 
                            && opt.getProcess().equals(option.getProcess())
                            && (option.getId() == null || !opt.getId().equals(option.getId())));
            if (exists) {
                throw new IllegalArgumentException("工艺类型 \"" + option.getProcess() + "\" 已存在，请使用其他工艺类型");
            }
        }
        
        // 设置创建时间
        option.setCreatedAt(LocalDateTime.now());
        option.setUpdatedAt(LocalDateTime.now());
        
        // 如果没有设置显示顺序，设置为最大值+1
        if (option.getDisplayOrder() == null) {
            List<PreProcessingStepsOptions> allOptions = preProcessingStepsOptionsMapper.getAllOptions();
            int maxOrder = allOptions.stream()
                    .mapToInt(opt -> opt.getDisplayOrder() != null ? opt.getDisplayOrder() : 0)
                    .max()
                    .orElse(0);
            option.setDisplayOrder(maxOrder + 1);
        }
        
        // 默认激活状态
        if (option.getIsActive() == null) {
            option.setIsActive(true);
        }
        
        preProcessingStepsOptionsMapper.insert(option);
        return option;
    }
    
    @Override
    public PreProcessingStepsOptions update(PreProcessingStepsOptions option) {
        // 检查工艺类型是否重复（排除自己）
        if (option.getProcess() != null && !option.getProcess().trim().isEmpty()) {
            List<PreProcessingStepsOptions> allOptions = preProcessingStepsOptionsMapper.getAllOptions();
            boolean exists = allOptions.stream()
                    .anyMatch(opt -> opt.getProcess() != null 
                            && opt.getProcess().equals(option.getProcess())
                            && option.getId() != null 
                            && !opt.getId().equals(option.getId()));
            if (exists) {
                throw new IllegalArgumentException("工艺类型 \"" + option.getProcess() + "\" 已存在，请使用其他工艺类型");
            }
        }
        
        // 设置更新时间
        option.setUpdatedAt(LocalDateTime.now());
        
        preProcessingStepsOptionsMapper.update(option);
        return preProcessingStepsOptionsMapper.getById(option.getId());
    }
    
    @Override
    public boolean deleteById(Integer id) {
        int result = preProcessingStepsOptionsMapper.deleteById(id);
        return result > 0;
    }
    
    @Override
    public boolean batchUpdateStatus(List<Integer> ids, Boolean isActive) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        int result = preProcessingStepsOptionsMapper.batchUpdateStatus(ids, isActive);
        return result > 0;
    }
    
    @Override
    public boolean updateDisplayOrder(Integer id, Integer displayOrder) {
        int result = preProcessingStepsOptionsMapper.updateDisplayOrder(id, displayOrder);
        return result > 0;
    }
    
    @Override
    public PreProcessingStepsOptions getByProcess(String process) {
        return preProcessingStepsOptionsMapper.getByProcess(process);
    }
    
    @Override
    public PreProcessingStepsOptions getByPreProcessingStepsName(String preProcessingStepsName) {
        return preProcessingStepsOptionsMapper.getByPreProcessingStepsName(preProcessingStepsName);
    }
}
