package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.CompatibilityQueryParams;
import com.it.yts_project.dto.HotStampingCompatibilityDetailDTO;
import com.it.yts_project.mapper.HotStampingCompatibilityMapper;
import com.it.yts_project.model.HotStampingCompatibility;
import com.it.yts_project.model.ProductTypeOptions;
import com.it.yts_project.model.HotStampingPatternBase;
import com.it.yts_project.model.HotStampingTypeOptions;
import com.it.yts_project.service.SmartCompatibilityService;
import com.it.yts_project.service.ProductTypeOptionsService;
import com.it.yts_project.service.HotStampingPatternBaseService;
import com.it.yts_project.service.HotStampingTypeOptionsService;
import com.it.yts_project.service.PreProcessingStepsOptionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 耐磨金纸映射管理服务实现类
 */
@Service
public class SmartCompatibilityServiceImpl implements SmartCompatibilityService {

    private static final Logger log = LoggerFactory.getLogger(SmartCompatibilityServiceImpl.class);

    @Autowired
    private HotStampingCompatibilityMapper compatibilityMapper;
    
    @Autowired
    private ProductTypeOptionsService productTypeOptionsService;
    
    @Autowired
    private HotStampingPatternBaseService hotStampingPatternBaseService;
    
    @Autowired
    private HotStampingTypeOptionsService hotStampingTypeOptionsService;
    
    @Autowired
    private PreProcessingStepsOptionsService preProcessingStepsOptionsService;
    
    /**
     * 根据ID字段自动填充文本字段
     */
    private void fillTextFieldsFromIds(HotStampingCompatibility rule) {
        // 填充产品类型
        if (rule.getProductTypeId() != null && (rule.getProductType() == null || rule.getProductType().isEmpty())) {
            ProductTypeOptions productType = productTypeOptionsService.getById(rule.getProductTypeId());
            if (productType != null) {
                rule.setProductType(productType.getProductName());
            }
        }
        
        // 填充图案特征
        if (rule.getPatternCharacteristicId() != null && (rule.getPatternCharacteristic() == null || rule.getPatternCharacteristic().isEmpty())) {
            HotStampingPatternBase pattern = hotStampingPatternBaseService.getPatternById(rule.getPatternCharacteristicId().longValue());
            if (pattern != null) {
                rule.setPatternCharacteristic(pattern.getOptionName());
            }
        }
        
        // 填充烫金类型
        if (rule.getHotStampingTypeId() != null && (rule.getHotStampingType() == null || rule.getHotStampingType().isEmpty())) {
            HotStampingTypeOptions hotStampingType = hotStampingTypeOptionsService.getTypeById(rule.getHotStampingTypeId().longValue());
            if (hotStampingType != null) {
                // 构建完整的烫金类型名称
                String typeName = hotStampingType.getStampingType();
                if (hotStampingType.getPositionType() != null && !hotStampingType.getPositionType().isEmpty()) {
                    typeName = typeName + "-" + hotStampingType.getPositionType();
                }
                rule.setHotStampingType(typeName);
            }
        }
        
        // 填充前工序（可选，因为可能为null）
        // 注意：前工序名称不需要存储到hot_stamping_compatibility表，这里只是验证ID是否存在
        if (rule.getPreProcessingStepId() != null) {
            try {
                preProcessingStepsOptionsService.getById(rule.getPreProcessingStepId());
                // 如果ID存在，验证通过；如果不存在会抛出异常
            } catch (Exception e) {
                // 忽略错误，前工序可能不存在或已删除
            }
        }
    }

    @Override
    public Map<String, Object> getRules(CompatibilityQueryParams params) {
        try {
            // 获取所有兼容性规则
            List<HotStampingCompatibility> allRules = compatibilityMapper.findCompatibleHotStampingTypes(params);
            
            // 应用搜索过滤
            if (params.getSearch() != null && !params.getSearch().trim().isEmpty()) {
                String searchTerm = params.getSearch().toLowerCase();
                allRules = allRules.stream()
                        .filter(rule -> 
                            (rule.getProductType() != null && rule.getProductType().toLowerCase().contains(searchTerm)) ||
                            (rule.getPatternCharacteristic() != null && rule.getPatternCharacteristic().toLowerCase().contains(searchTerm)) ||
                            (rule.getHotStampingType() != null && rule.getHotStampingType().toLowerCase().contains(searchTerm))
                        )
                        .collect(Collectors.toList());
            }
            
            // 分页处理
            int total = allRules.size();
            int page = params.getPage() != null ? params.getPage() : 1;
            int size = params.getSize() != null ? params.getSize() : 10;
            int start = (page - 1) * size;
            int end = Math.min(start + size, total);
            
            List<HotStampingCompatibility> pagedRules = allRules.subList(start, end);
            
            Map<String, Object> result = new HashMap<>();
            result.put("items", pagedRules);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);
            result.put("totalPages", (int) Math.ceil((double) total / size));
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("获取兼容性规则失败", e);
        }
    }

    @Override
    public HotStampingCompatibility getRule(Long id) {
        try {
            return compatibilityMapper.getById(id);
        } catch (Exception e) {
            throw new RuntimeException("获取兼容性规则失败", e);
        }
    }

    @Override
    public HotStampingCompatibility createRule(HotStampingCompatibility rule) {
        try {
            // 根据ID字段自动填充文本字段（如果文本字段为空）
            fillTextFieldsFromIds(rule);
            
            // 检查必填字段
            if (rule.getProductTypeId() == null || rule.getPatternCharacteristicId() == null 
                || rule.getHotStampingTypeId() == null || rule.getPaperPerformance() == null 
                || rule.getCompatibility() == null) {
                throw new RuntimeException("必填字段不能为空");
            }
            
            // 检查是否存在重复的规则
            int duplicateCount = compatibilityMapper.countDuplicateRule(
                rule.getProductTypeId(),
                rule.getPatternCharacteristicId(),
                rule.getHotStampingTypeId(),
                rule.getPreProcessingStepId(),
                rule.getPostProcessingStepId(),
                rule.getPaperPerformance(),
                rule.getCompatibility(),
                null  // 新增时不需要排除ID
            );
            
            if (duplicateCount > 0) {
                throw new RuntimeException("已存在相同的兼容性规则，无法重复添加");
            }
            
            // 设置创建时间
            rule.setCreatedAt(java.time.LocalDateTime.now());
            rule.setUpdatedAt(java.time.LocalDateTime.now());
            
            // 调用mapper的插入方法
            int result = compatibilityMapper.insert(rule);
            if (result > 0) {
                return rule;
            } else {
                throw new RuntimeException("创建规则失败");
            }
        } catch (RuntimeException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("创建兼容性规则失败: " + e.getMessage(), e);
        }
    }

    @Override
    public HotStampingCompatibility updateRule(HotStampingCompatibility rule) {
        try {
            // 检查ID是否存在
            if (rule.getId() == null) {
                throw new RuntimeException("规则ID不能为空");
            }
            
            // 根据ID字段自动填充文本字段（如果文本字段为空）
            fillTextFieldsFromIds(rule);
            
            // 检查必填字段
            if (rule.getProductTypeId() == null || rule.getPatternCharacteristicId() == null 
                || rule.getHotStampingTypeId() == null || rule.getPaperPerformance() == null 
                || rule.getCompatibility() == null) {
                throw new RuntimeException("必填字段不能为空");
            }
            
            // 检查是否存在重复的规则（排除当前规则）
            int duplicateCount = compatibilityMapper.countDuplicateRule(
                rule.getProductTypeId(),
                rule.getPatternCharacteristicId(),
                rule.getHotStampingTypeId(),
                rule.getPreProcessingStepId(),
                rule.getPostProcessingStepId(),
                rule.getPaperPerformance(),
                rule.getCompatibility(),
                rule.getId()  // 排除当前规则ID
            );
            
            if (duplicateCount > 0) {
                throw new RuntimeException("已存在相同的兼容性规则，无法更新");
            }
            
            // 设置更新时间
            rule.setUpdatedAt(java.time.LocalDateTime.now());
            
            // 调用mapper的更新方法
            int result = compatibilityMapper.update(rule);
            if (result > 0) {
                return rule;
            } else {
                throw new RuntimeException("更新规则失败，规则可能不存在");
            }
        } catch (RuntimeException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("更新兼容性规则失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteRule(Long id) {
        try {
            // 先检查规则是否存在
            HotStampingCompatibility rule = getRule(id);
            if (rule == null) {
                return false;
            }
            
            // 调用mapper的删除方法
            int deletedRows = compatibilityMapper.deleteById(id);
            return deletedRows > 0;
        } catch (Exception e) {
            throw new RuntimeException("删除兼容性规则失败", e);
        }
    }

    @Override
    public Map<String, Object> batchOperation(Map<String, Object> operation) {
        try {
            log.debug("批量操作请求: {}", operation);
            
            List<?> rawIds = (List<?>) operation.get("ids");
            List<Long> ids = rawIds.stream()
                    .map(id -> {
                        if (id instanceof Integer) {
                            return ((Integer) id).longValue();
                        } else if (id instanceof Long) {
                            return (Long) id;
                        } else {
                            return Long.valueOf(id.toString());
                        }
                    })
                    .collect(Collectors.toList());
            String operationType = (String) operation.get("operation");
            
            log.debug("操作类型: {}, 规则ID列表: {}", operationType, ids);
            
            Map<String, Object> result = new HashMap<>();
            
            switch (operationType) {
                case "edit":
                    // 批量编辑逻辑
                    if (ids != null && !ids.isEmpty()) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> updateData = (Map<String, Object>) operation.get("updateData");
                        int updatedCount = 0;
                        for (Long id : ids) {
                            try {
                                HotStampingCompatibility existingRule = getRule(id);
                                if (existingRule != null) {
                                    // 优先更新ID字段
                                    if (updateData.containsKey("productTypeId")) {
                                        Object productTypeId = updateData.get("productTypeId");
                                        if (productTypeId != null) {
                                            existingRule.setProductTypeId(productTypeId instanceof Integer ? (Integer) productTypeId : Integer.valueOf(productTypeId.toString()));
                                        }
                                    }
                                    if (updateData.containsKey("patternCharacteristicId")) {
                                        Object patternCharacteristicId = updateData.get("patternCharacteristicId");
                                        if (patternCharacteristicId != null) {
                                            existingRule.setPatternCharacteristicId(patternCharacteristicId instanceof Integer ? (Integer) patternCharacteristicId : Integer.valueOf(patternCharacteristicId.toString()));
                                        }
                                    }
                                    if (updateData.containsKey("hotStampingTypeId")) {
                                        Object hotStampingTypeId = updateData.get("hotStampingTypeId");
                                        if (hotStampingTypeId != null) {
                                            existingRule.setHotStampingTypeId(hotStampingTypeId instanceof Integer ? (Integer) hotStampingTypeId : Integer.valueOf(hotStampingTypeId.toString()));
                                        }
                                    }
                                    if (updateData.containsKey("preProcessingStepId")) {
                                        Object preProcessingStepId = updateData.get("preProcessingStepId");
                                        if (preProcessingStepId != null) {
                                            existingRule.setPreProcessingStepId(preProcessingStepId instanceof Integer ? (Integer) preProcessingStepId : Integer.valueOf(preProcessingStepId.toString()));
                                        }
                                    }
                                    if (updateData.containsKey("postProcessingStepId")) {
                                        Object postProcessingStepId = updateData.get("postProcessingStepId");
                                        if (postProcessingStepId != null) {
                                            existingRule.setPostProcessingStepId(postProcessingStepId instanceof Integer ? (Integer) postProcessingStepId : Integer.valueOf(postProcessingStepId.toString()));
                                        }
                                    }
                                    // 更新文本字段（兼容性）
                                    if (updateData.containsKey("productType")) {
                                        existingRule.setProductType((String) updateData.get("productType"));
                                    }
                                    if (updateData.containsKey("patternCharacteristic")) {
                                        existingRule.setPatternCharacteristic((String) updateData.get("patternCharacteristic"));
                                    }
                                    if (updateData.containsKey("hotStampingType")) {
                                        existingRule.setHotStampingType((String) updateData.get("hotStampingType"));
                                    }
                                    if (updateData.containsKey("compatibility")) {
                                        existingRule.setCompatibility((String) updateData.get("compatibility"));
                                    }
                                    if (updateData.containsKey("paperPerformance")) {
                                        existingRule.setPaperPerformance((String) updateData.get("paperPerformance"));
                                    }
                                    
                                    HotStampingCompatibility updatedRule = updateRule(existingRule);
                                    if (updatedRule != null) {
                                        updatedCount++;
                                    }
                                }
                            } catch (Exception e) {
                                log.error("更新规则失败，ID: {}", id, e);
                            }
                        }
                        result.put("processedCount", updatedCount);
                        result.put("message", "成功更新 " + updatedCount + " 个规则");
                    } else {
                        result.put("processedCount", 0);
                        result.put("message", "没有提供要更新的规则ID");
                    }
                    break;
                case "copy":
                    // 批量复制逻辑
                    if (ids != null && !ids.isEmpty()) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> modifications = (Map<String, Object>) operation.get("modifications");
                        int copiedCount = 0;
                        for (Long id : ids) {
                            try {
                                // 将modifications转换为HotStampingCompatibility对象
                                HotStampingCompatibility mods = null;
                                if (modifications != null) {
                                    mods = new HotStampingCompatibility();
                                    // 设置ID字段
                                    if (modifications.containsKey("productTypeId")) {
                                        Object productTypeId = modifications.get("productTypeId");
                                        if (productTypeId != null) {
                                            mods.setProductTypeId(productTypeId instanceof Integer ? (Integer) productTypeId : Integer.valueOf(productTypeId.toString()));
                                        }
                                    }
                                    if (modifications.containsKey("patternCharacteristicId")) {
                                        Object patternCharacteristicId = modifications.get("patternCharacteristicId");
                                        if (patternCharacteristicId != null) {
                                            mods.setPatternCharacteristicId(patternCharacteristicId instanceof Integer ? (Integer) patternCharacteristicId : Integer.valueOf(patternCharacteristicId.toString()));
                                        }
                                    }
                                    if (modifications.containsKey("hotStampingTypeId")) {
                                        Object hotStampingTypeId = modifications.get("hotStampingTypeId");
                                        if (hotStampingTypeId != null) {
                                            mods.setHotStampingTypeId(hotStampingTypeId instanceof Integer ? (Integer) hotStampingTypeId : Integer.valueOf(hotStampingTypeId.toString()));
                                        }
                                    }
                                    if (modifications.containsKey("preProcessingStepId")) {
                                        Object preProcessingStepId = modifications.get("preProcessingStepId");
                                        if (preProcessingStepId == null) {
                                            // null 表示用户想清空，使用 -1 作为标记
                                            mods.setPreProcessingStepId(-1);
                                        } else {
                                            mods.setPreProcessingStepId(preProcessingStepId instanceof Integer ? (Integer) preProcessingStepId : Integer.valueOf(preProcessingStepId.toString()));
                                        }
                                    }
                                    if (modifications.containsKey("postProcessingStepId")) {
                                        Object postProcessingStepId = modifications.get("postProcessingStepId");
                                        if (postProcessingStepId == null) {
                                            // null 表示用户想清空，使用 -1 作为标记
                                            mods.setPostProcessingStepId(-1);
                                        } else {
                                            mods.setPostProcessingStepId(postProcessingStepId instanceof Integer ? (Integer) postProcessingStepId : Integer.valueOf(postProcessingStepId.toString()));
                                        }
                                    }
                                    // 设置文本字段
                                    if (modifications.containsKey("productType")) {
                                        mods.setProductType((String) modifications.get("productType"));
                                    }
                                    if (modifications.containsKey("patternCharacteristic")) {
                                        mods.setPatternCharacteristic((String) modifications.get("patternCharacteristic"));
                                    }
                                    if (modifications.containsKey("hotStampingType")) {
                                        mods.setHotStampingType((String) modifications.get("hotStampingType"));
                                    }
                                    if (modifications.containsKey("compatibility")) {
                                        mods.setCompatibility((String) modifications.get("compatibility"));
                                    }
                                    if (modifications.containsKey("paperPerformance")) {
                                        mods.setPaperPerformance((String) modifications.get("paperPerformance"));
                                    }
                                }
                                
                                HotStampingCompatibility copiedRule = copyRule(id, mods);
                                if (copiedRule != null) {
                                    copiedCount++;
                                }
                            } catch (Exception e) {
                                log.error("复制规则失败，ID: {}", id, e);
                            }
                        }
                        result.put("processedCount", copiedCount);
                        result.put("message", "成功复制 " + copiedCount + " 个规则");
                    } else {
                        result.put("processedCount", 0);
                        result.put("message", "没有提供要复制的规则ID");
                    }
                    break;
                case "delete":
                    // 批量删除逻辑
                    if (ids != null && !ids.isEmpty()) {
                        int deletedCount = 0;
                        for (Long id : ids) {
                            try {
                                boolean deleted = deleteRule(id);
                                if (deleted) {
                                    deletedCount++;
                                }
                            } catch (Exception e) {
                                log.error("删除规则失败，ID: {}", id, e);
                            }
                        }
                        result.put("processedCount", deletedCount);
                        result.put("message", "成功删除 " + deletedCount + " 个规则");
                    } else {
                        result.put("processedCount", 0);
                        result.put("message", "没有提供要删除的规则ID");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("不支持的操作类型: " + operationType);
            }
            
            return result;
        } catch (Exception e) {
            log.error("批量操作失败", e);
            throw new RuntimeException("批量操作失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getMatrix() {
        try {
            Map<String, Object> matrix = new HashMap<>();
            
            // 获取所有产品类型
            List<String> productTypes = compatibilityMapper.getAllProductTypes();
            matrix.put("productTypes", productTypes);
            
            // 获取所有烫金类型
            List<String> hotStampingTypes = compatibilityMapper.getAllHotStampingTypes();
            matrix.put("hotStampingTypes", hotStampingTypes);
            
            // 获取所有烫金纸性能类型
            List<String> paperPerformanceTypes = compatibilityMapper.getAllPaperPerformanceTypes();
            matrix.put("paperPerformanceTypes", paperPerformanceTypes);
            
            // 获取所有规则
            CompatibilityQueryParams params = new CompatibilityQueryParams();
            List<HotStampingCompatibility> rules = compatibilityMapper.findCompatibleHotStampingTypes(params);
            matrix.put("rules", rules);
            
            return matrix;
        } catch (Exception e) {
            throw new RuntimeException("获取兼容性矩阵失败", e);
        }
    }

    @Override
    public Map<String, List<String>> getOptions() {
        try {
            Map<String, List<String>> options = new HashMap<>();
            
            // 获取产品类型选项
            List<String> productTypes = compatibilityMapper.getAllProductTypes();
            options.put("productTypes", productTypes);
            
            // 获取烫金类型选项
            List<String> hotStampingTypes = compatibilityMapper.getAllHotStampingTypes();
            options.put("hotStampingTypes", hotStampingTypes);
            
            // 图案类型选项（从现有规则中提取）
            CompatibilityQueryParams params = new CompatibilityQueryParams();
            List<HotStampingCompatibility> rules = compatibilityMapper.findCompatibleHotStampingTypes(params);
            List<String> patternTypes = rules.stream()
                    .map(HotStampingCompatibility::getPatternCharacteristic)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());
            options.put("patternTypes", patternTypes);
            
            return options;
        } catch (Exception e) {
            throw new RuntimeException("获取选项数据失败", e);
        }
    }

    @Override
    public Map<String, Object> validateRule(HotStampingCompatibility rule) {
        try {
            Map<String, Object> validation = new HashMap<>();
            List<HotStampingCompatibility> conflicts = new ArrayList<>();
            List<HotStampingCompatibility> suggestions = new ArrayList<>();
            
            // 检查是否存在冲突的规则
            CompatibilityQueryParams params = new CompatibilityQueryParams();
            params.setProductType(rule.getProductType());
            params.setHotStampingType(rule.getHotStampingType());
            
            List<HotStampingCompatibility> existingRules = compatibilityMapper.findCompatibleHotStampingTypes(params);
            conflicts = existingRules.stream()
                    .filter(existing -> existing.getPatternCharacteristic().equals(rule.getPatternCharacteristic()))
                    .collect(Collectors.toList());
            
            // 生成建议
            if (conflicts.isEmpty()) {
                suggestions = existingRules.stream()
                        .filter(existing -> !existing.getPatternCharacteristic().equals(rule.getPatternCharacteristic()))
                        .limit(3)
                        .collect(Collectors.toList());
            }
            
            validation.put("isValid", conflicts.isEmpty());
            validation.put("conflicts", conflicts);
            validation.put("suggestions", suggestions);
            
            return validation;
        } catch (Exception e) {
            throw new RuntimeException("验证规则失败", e);
        }
    }

    @Override
    public Map<String, Object> importRules(MultipartFile file) {
        try {
            Map<String, Object> result = new HashMap<>();
            
            // 这里需要实现Excel文件解析逻辑
            // 暂时返回模拟结果
            result.put("success", 0);
            result.put("failed", 0);
            result.put("errors", Arrays.asList("导入功能待实现"));
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("导入规则失败", e);
        }
    }

    @Override
    public byte[] exportRules(CompatibilityQueryParams params) {
        try {
            // 这里需要实现Excel导出逻辑
            // 暂时返回空字节数组
            return new byte[0];
        } catch (Exception e) {
            throw new RuntimeException("导出规则失败", e);
        }
    }

    @Override
    public HotStampingCompatibility copyRule(Long id, HotStampingCompatibility modifications) {
        try {
            // 获取原规则
            HotStampingCompatibility originalRule = getRule(id);
            if (originalRule == null) {
                throw new RuntimeException("原规则不存在");
            }
            
            // 创建副本 - 优先使用ID字段
            HotStampingCompatibility copiedRule = new HotStampingCompatibility();
            // 复制ID字段（主要字段）
            copiedRule.setProductTypeId(originalRule.getProductTypeId());
            copiedRule.setPatternCharacteristicId(originalRule.getPatternCharacteristicId());
            copiedRule.setHotStampingTypeId(originalRule.getHotStampingTypeId());
            copiedRule.setPreProcessingStepId(originalRule.getPreProcessingStepId());
            copiedRule.setPostProcessingStepId(originalRule.getPostProcessingStepId());
            // 复制文本字段（兼容性字段）
            copiedRule.setProductType(originalRule.getProductType());
            copiedRule.setPatternCharacteristic(originalRule.getPatternCharacteristic());
            copiedRule.setHotStampingType(originalRule.getHotStampingType());
            copiedRule.setCompatibility(originalRule.getCompatibility());
            copiedRule.setPaperPerformance(originalRule.getPaperPerformance());
            
            // 应用修改
            if (modifications != null) {
                // 优先更新ID字段
                if (modifications.getProductTypeId() != null) {
                    copiedRule.setProductTypeId(modifications.getProductTypeId());
                }
                if (modifications.getPatternCharacteristicId() != null) {
                    copiedRule.setPatternCharacteristicId(modifications.getPatternCharacteristicId());
                }
                if (modifications.getHotStampingTypeId() != null) {
                    copiedRule.setHotStampingTypeId(modifications.getHotStampingTypeId());
                }
                // 对于可选的ID字段，如果modifications中设置了，就应用
                // 注意：在batchOperation中，如果用户选择了"清空"（null），我们已经设置了null
                // 但Java无法区分"未设置"和"设置为null"
                // 解决方案：在batchOperation中，如果用户选择了"清空"，我们使用-1作为标记
                // 然后在copyRule中，将-1转换为null
                // 但为了简化，我们暂时只支持"设置为非null值"，不支持"清空"
                // 如果需要"清空"功能，可以在前端使用特殊值（如-1）表示，然后在这里转换
                if (modifications.getPreProcessingStepId() != null) {
                    // 如果值为-1，表示用户想清空
                    if (modifications.getPreProcessingStepId() == -1) {
                        copiedRule.setPreProcessingStepId(null);
                    } else {
                        copiedRule.setPreProcessingStepId(modifications.getPreProcessingStepId());
                    }
                }
                if (modifications.getPostProcessingStepId() != null) {
                    // 如果值为-1，表示用户想清空
                    if (modifications.getPostProcessingStepId() == -1) {
                        copiedRule.setPostProcessingStepId(null);
                    } else {
                        copiedRule.setPostProcessingStepId(modifications.getPostProcessingStepId());
                    }
                }
                // 更新文本字段
                if (modifications.getProductType() != null) {
                    copiedRule.setProductType(modifications.getProductType());
                }
                if (modifications.getPatternCharacteristic() != null) {
                    copiedRule.setPatternCharacteristic(modifications.getPatternCharacteristic());
                }
                if (modifications.getHotStampingType() != null) {
                    copiedRule.setHotStampingType(modifications.getHotStampingType());
                }
                if (modifications.getCompatibility() != null) {
                    copiedRule.setCompatibility(modifications.getCompatibility());
                }
                if (modifications.getPaperPerformance() != null && !modifications.getPaperPerformance().isEmpty()) {
                    copiedRule.setPaperPerformance(modifications.getPaperPerformance());
                }
            }
            
            // 创建新规则
            return createRule(copiedRule);
        } catch (Exception e) {
            throw new RuntimeException("复制规则失败", e);
        }
    }

    @Override
    public Map<String, Object> getStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            CompatibilityQueryParams params = new CompatibilityQueryParams();
            List<HotStampingCompatibility> allRules = compatibilityMapper.findCompatibleHotStampingTypes(params);
            
            int totalRules = allRules.size();
            long compatibleRules = allRules.stream().filter(rule -> "V".equals(rule.getCompatibility())).count();
            long incompatibleRules = allRules.stream().filter(rule -> "X".equals(rule.getCompatibility())).count();
            
            statistics.put("totalRules", totalRules);
            statistics.put("compatibleRules", compatibleRules);
            statistics.put("incompatibleRules", incompatibleRules);
            
            // 按产品类型统计
            Map<String, Long> productTypeStats = allRules.stream()
                    .collect(Collectors.groupingBy(
                            HotStampingCompatibility::getProductType,
                            Collectors.counting()
                    ));
            statistics.put("productTypeStats", productTypeStats);
            
            // 按图案类型统计
            Map<String, Long> patternTypeStats = allRules.stream()
                    .collect(Collectors.groupingBy(
                            HotStampingCompatibility::getPatternCharacteristic,
                            Collectors.counting()
                    ));
            statistics.put("patternTypeStats", patternTypeStats);
            
            // 按烫金类型统计
            Map<String, Long> hotStampingTypeStats = allRules.stream()
                    .collect(Collectors.groupingBy(
                            HotStampingCompatibility::getHotStampingType,
                            Collectors.counting()
                    ));
            statistics.put("hotStampingTypeStats", hotStampingTypeStats);
            
            return statistics;
        } catch (Exception e) {
            throw new RuntimeException("获取统计信息失败", e);
        }
    }

    @Override
    public List<HotStampingCompatibility> searchRules(String query) {
        try {
            CompatibilityQueryParams params = new CompatibilityQueryParams();
            params.setSearch(query);
            return compatibilityMapper.findCompatibleHotStampingTypes(params);
        } catch (Exception e) {
            throw new RuntimeException("搜索规则失败", e);
        }
    }

    @Override
    public Map<String, Object> getRulePreview(HotStampingCompatibility rule) {
        try {
            Map<String, Object> preview = new HashMap<>();
            
            // 创建预览对象
            HotStampingCompatibility previewRule = new HotStampingCompatibility();
            previewRule.setProductType(rule.getProductType());
            previewRule.setPatternCharacteristic(rule.getPatternCharacteristic());
            previewRule.setHotStampingType(rule.getHotStampingType());
            previewRule.setCompatibility(rule.getCompatibility());
            // 注意：patternType, lineThickness, solidAreaSize, referenceCode 字段已从模型中移除
            // 这些信息现在存储在关联的 hot_stamping_pattern_base 表中
            
            // 查找受影响的规则
            CompatibilityQueryParams params = new CompatibilityQueryParams();
            params.setProductType(rule.getProductType());
            params.setHotStampingType(rule.getHotStampingType());
            List<HotStampingCompatibility> affectedRules = compatibilityMapper.findCompatibleHotStampingTypes(params);
            
            preview.put("preview", previewRule);
            preview.put("affectedRules", affectedRules);
            preview.put("impact", "此规则将影响 " + affectedRules.size() + " 个相关规则");
            
            return preview;
        } catch (Exception e) {
            throw new RuntimeException("获取规则预览失败", e);
        }
    }

    /**
     * 核心业务方法：根据多个ID字段筛选烫金纸性能类型
     * 这是耐磨金纸映射管理的核心功能
     */
    @Override
    public List<String> getPaperPerformanceByMultipleIds(
            Integer productTypeId,
            Integer patternCharacteristicId,
            Integer hotStampingTypeId,
            Integer preProcessingStepId,
            Integer postProcessingStepId) {
        try {
            return compatibilityMapper.getPaperPerformanceByMultipleIds(
                    productTypeId,
                    patternCharacteristicId,
                    hotStampingTypeId,
                    preProcessingStepId,
                    postProcessingStepId
            );
        } catch (Exception e) {
            throw new RuntimeException("筛选烫金纸性能类型失败", e);
        }
    }

    /**
     * 获取完整的兼容性规则（包含所有字段）
     */
    @Override
    public List<HotStampingCompatibility> getCompatibilityRulesByMultipleIds(
            Integer productTypeId,
            Integer patternCharacteristicId,
            Integer hotStampingTypeId,
            Integer preProcessingStepId,
            Integer postProcessingStepId) {
        try {
            return compatibilityMapper.getCompatibilityRulesByMultipleIds(
                    productTypeId,
                    patternCharacteristicId,
                    hotStampingTypeId,
                    preProcessingStepId,
                    postProcessingStepId
            );
        } catch (Exception e) {
            throw new RuntimeException("获取兼容性规则失败", e);
        }
    }

    /**
     * 获取完整的兼容性规则列表（包含关联表信息）
     */
    @Override
    public List<HotStampingCompatibilityDetailDTO> getCompatibilityRulesWithDetails() {
        try {
            return compatibilityMapper.getCompatibilityRulesWithDetails();
        } catch (Exception e) {
            throw new RuntimeException("获取兼容性规则详细信息失败", e);
        }
    }
}
