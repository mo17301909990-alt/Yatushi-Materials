package com.it.yts_project.service.Impl;

import com.it.yts_project.mapper.PreProcessingStepsMapper;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.model.PreProcessingSteps;
import com.it.yts_project.service.PreProcessingStepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 前工序步骤服务实现类
 */
@Service
public class PreProcessingStepsServiceImpl implements PreProcessingStepsService {
    
    @Autowired
    private PreProcessingStepsMapper preProcessingStepsMapper;
    
    @Autowired
    private ProductsMapper productsMapper;
    
    @Override
    public List<PreProcessingSteps> getAllStepsByStepId(Integer stepId) {
        return preProcessingStepsMapper.getAllStepsByStepId(stepId);
    }
    
    @Override
    public PreProcessingSteps getById(Integer id) {
        return preProcessingStepsMapper.getById(id);
    }
    
    @Override
    public PreProcessingSteps create(PreProcessingSteps step) {
        // 验证必填字段
        if (step.getStepId() == null) {
            throw new IllegalArgumentException("步骤ID不能为空");
        }
        if (step.getSeriesName() == null || step.getSeriesName().trim().isEmpty()) {
            throw new IllegalArgumentException("产品系列名称不能为空");
        }
        
        // 验证唯一性：检查是否存在相同的记录（step_id + series_name + product_id + paper_type）
        boolean exists = preProcessingStepsMapper.existsByUniqueFields(
                step.getStepId(),
                step.getSeriesName(),
                step.getProductId(),
                step.getPaperType(),
                null  // 创建时不需要排除任何记录
        );
        if (exists) {
            String duplicateInfo = String.format("步骤ID: %d, 产品系列: %s", 
                    step.getStepId(), step.getSeriesName());
            if (step.getProductId() != null) {
                duplicateInfo += String.format(", 产品ID: %d", step.getProductId());
            }
            if (step.getPaperType() != null && !step.getPaperType().trim().isEmpty()) {
                duplicateInfo += String.format(", 烫金纸性能类型: %s", step.getPaperType());
            }
            throw new IllegalArgumentException("该映射配置已存在：" + duplicateInfo);
        }
        
        // 验证：如果添加的是某个产品的映射（product_id 不为 null），检查该产品系列是否已经有通用映射
        if (step.getProductId() != null) {
            boolean hasGeneralMapping = preProcessingStepsMapper.existsGeneralMapping(
                    step.getStepId(),
                    step.getSeriesName(),
                    step.getPaperType()
            );
            if (hasGeneralMapping) {
                throw new IllegalArgumentException(
                        String.format("产品系列 \"%s\" 已存在通用映射（适用于该系列所有产品），无法为该系列下的特定产品创建映射。请先删除通用映射或选择其他产品系列。", 
                                step.getSeriesName())
                );
            }
        }
        
        // 验证：如果指定了产品系列和烫金纸性能类型，检查产品系列是否支持该烫金纸性能类型
        if (step.getSeriesName() != null && !step.getSeriesName().trim().isEmpty()
                && step.getPaperType() != null && !step.getPaperType().trim().isEmpty()) {
            boolean supports = productsMapper.checkSeriesSupportsPaperType(
                    step.getSeriesName(), 
                    step.getPaperType()
            );
            if (!supports) {
                throw new IllegalArgumentException(
                        "产品系列 \"" + step.getSeriesName() + "\" 不支持烫金纸性能类型 \"" + step.getPaperType() + "\""
                );
            }
        }
        
        // 设置创建时间
        step.setCreatedAt(LocalDateTime.now());
        step.setUpdatedAt(LocalDateTime.now());
        
        // 设置默认值
        if (step.getIsActive() == null) {
            step.setIsActive(true);
        }
        if (step.getStepOrder() == null) {
            step.setStepOrder(1);
        }
        
        preProcessingStepsMapper.insert(step);
        return step;
    }
    
    @Override
    public PreProcessingSteps update(PreProcessingSteps step) {
        // 验证必填字段
        if (step.getId() == null) {
            throw new IllegalArgumentException("记录ID不能为空");
        }
        if (step.getStepId() == null) {
            throw new IllegalArgumentException("步骤ID不能为空");
        }
        if (step.getSeriesName() == null || step.getSeriesName().trim().isEmpty()) {
            throw new IllegalArgumentException("产品系列名称不能为空");
        }
        
        // 验证唯一性：检查是否存在相同的记录（排除当前记录）
        boolean exists = preProcessingStepsMapper.existsByUniqueFields(
                step.getStepId(),
                step.getSeriesName(),
                step.getProductId(),
                step.getPaperType(),
                step.getId()  // 更新时排除当前记录
        );
        if (exists) {
            String duplicateInfo = String.format("步骤ID: %d, 产品系列: %s", 
                    step.getStepId(), step.getSeriesName());
            if (step.getProductId() != null) {
                duplicateInfo += String.format(", 产品ID: %d", step.getProductId());
            }
            if (step.getPaperType() != null && !step.getPaperType().trim().isEmpty()) {
                duplicateInfo += String.format(", 烫金纸性能类型: %s", step.getPaperType());
            }
            throw new IllegalArgumentException("该映射配置已存在：" + duplicateInfo);
        }
        
        // 验证：如果更新后是某个产品的映射（product_id 不为 null），检查该产品系列是否已经有通用映射
        // 需要先获取原始记录，检查 product_id 是否从 null 变为非 null
        PreProcessingSteps originalStep = preProcessingStepsMapper.getById(step.getId());
        if (originalStep != null && originalStep.getProductId() == null && step.getProductId() != null) {
            // 从通用映射改为特定产品映射，需要检查是否已有其他通用映射
            boolean hasGeneralMapping = preProcessingStepsMapper.existsGeneralMapping(
                    step.getStepId(),
                    step.getSeriesName(),
                    step.getPaperType()
            );
            if (hasGeneralMapping) {
                throw new IllegalArgumentException(
                        String.format("产品系列 \"%s\" 已存在通用映射（适用于该系列所有产品），无法将该映射修改为特定产品映射。", 
                                step.getSeriesName())
                );
            }
        } else if (step.getProductId() != null) {
            // 如果原本就是特定产品映射，检查是否有通用映射
            boolean hasGeneralMapping = preProcessingStepsMapper.existsGeneralMapping(
                    step.getStepId(),
                    step.getSeriesName(),
                    step.getPaperType()
            );
            if (hasGeneralMapping) {
                throw new IllegalArgumentException(
                        String.format("产品系列 \"%s\" 已存在通用映射（适用于该系列所有产品），无法为该系列下的特定产品创建映射。请先删除通用映射或选择其他产品系列。", 
                                step.getSeriesName())
                );
            }
        }
        
        // 验证：如果指定了产品系列和烫金纸性能类型，检查产品系列是否支持该烫金纸性能类型
        if (step.getSeriesName() != null && !step.getSeriesName().trim().isEmpty()
                && step.getPaperType() != null && !step.getPaperType().trim().isEmpty()) {
            boolean supports = productsMapper.checkSeriesSupportsPaperType(
                    step.getSeriesName(), 
                    step.getPaperType()
            );
            if (!supports) {
                throw new IllegalArgumentException(
                        "产品系列 \"" + step.getSeriesName() + "\" 不支持烫金纸性能类型 \"" + step.getPaperType() + "\""
                );
            }
        }
        
        // 设置更新时间
        step.setUpdatedAt(LocalDateTime.now());
        
        preProcessingStepsMapper.update(step);
        return step;
    }
    
    @Override
    public boolean deleteById(Integer id) {
        return preProcessingStepsMapper.deleteById(id) > 0;
    }
    
    @Override
    public int deleteByStepId(Integer stepId) {
        return preProcessingStepsMapper.deleteByStepId(stepId);
    }
    
    @Override
    public List<PreProcessingSteps> getAllSteps() {
        return preProcessingStepsMapper.getAllSteps();
    }
    
    @Override
    public PreProcessingSteps findByUniqueFields(Integer stepId, String seriesName, Integer productId, String paperType) {
        return preProcessingStepsMapper.findByUniqueFields(stepId, seriesName, productId, paperType);
    }
}

