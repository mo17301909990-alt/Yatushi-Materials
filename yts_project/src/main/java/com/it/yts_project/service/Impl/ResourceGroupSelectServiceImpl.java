package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.*;
import com.it.yts_project.mapper.*;
import com.it.yts_project.model.*;
import com.it.yts_project.service.ResourceGroupSelectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源组选择服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceGroupSelectServiceImpl implements ResourceGroupSelectService {
    
    private final RgResourceGroupMapper resourceGroupMapper;
    private final RgResourceRuleMapper resourceRuleMapper;
    private final RgTaskDefinitionMapper taskDefinitionMapper;
    private final RgWorkCenterMapper workCenterMapper;
    private final RgResourceGroupDetailMapper resourceGroupDetailMapper;
    private final RgResourceGroupCapacityMapper resourceGroupCapacityMapper;
    
    @Override
    public List<RgTaskDefinition> getAllTasks() {
        return taskDefinitionMapper.findAll();
    }
    
    @Override
    public Map<String, Boolean> getTaskRequiredParams(String taskCode) {
        // 获取该任务下的所有资源组
        List<RgResourceGroup> resourceGroups = resourceGroupMapper.findByTaskCode(taskCode);
        
        Map<String, Boolean> result = new HashMap<>();
        result.put("requiresSize", false);
        result.put("requiresSheetCount", false);
        result.put("requiresGsm", false);
        result.put("requiresThickness", false);
        
        // 检查是否有资源组需要这些参数
        for (RgResourceGroup rg : resourceGroups) {
            List<RgResourceRuleCondition> structRules = resourceRuleMapper.findStructRulesByResourceGroupId(rg.getId());
            
            for (RgResourceRuleCondition rule : structRules) {
                if (Boolean.TRUE.equals(rule.getIsRequired())) {
                    String paramCode = rule.getParamCode();
                    if ("width".equals(paramCode) || "height".equals(paramCode)) {
                        result.put("requiresSize", true);
                    } else if ("sheet_count".equals(paramCode)) {
                        result.put("requiresSheetCount", true);
                    } else if ("gsm".equals(paramCode)) {
                        result.put("requiresGsm", true);
                    } else if ("thickness".equals(paramCode)) {
                        result.put("requiresThickness", true);
                    }
                }
            }
        }
        
        return result;
    }
    
    @Override
    public ResourceGroupSelectResponse selectResourceGroups(ResourceGroupSelectRequest request) {
        log.info("筛选资源组, 任务: {}", request.getTaskCode());
        
        // 获取任务定义
        RgTaskDefinition task = taskDefinitionMapper.findByTaskCode(request.getTaskCode());
        if (task == null) {
            throw new RuntimeException("任务不存在: " + request.getTaskCode());
        }
        
        // 获取该任务下的所有资源组
        List<RgResourceGroup> resourceGroups = resourceGroupMapper.findByTaskCode(request.getTaskCode());
        
        // 评估每个资源组
        List<ResourceGroupCandidate> candidates = new ArrayList<>();
        
        for (RgResourceGroup rg : resourceGroups) {
            // 检查该资源组是否有硬性规则参数未填写
            if (hasMissingRequiredParams(rg, request)) {
                // 跳过该资源组，不评估
                log.debug("跳过资源组 {} (有硬性规则参数未填写)", rg.getResourceGroupCode());
                continue;
            }
            
            // 正常评估资源组
            ResourceGroupCandidate candidate = evaluateResourceGroup(rg, request);
            if (candidate != null) {
                candidates.add(candidate);
            }
        }
        
        // 统计结果
        int passCount = (int) candidates.stream().filter(c -> "PASS".equals(c.getMatchStatus())).count();
        int failCount = (int) candidates.stream().filter(c -> "FAIL".equals(c.getMatchStatus())).count();
        int unknownCount = (int) candidates.stream().filter(c -> "UNKNOWN".equals(c.getMatchStatus())).count();
        
        // 构建响应
        ResourceGroupSelectResponse response = new ResourceGroupSelectResponse();
        response.setTaskCode(task.getTaskCode());
        response.setTaskName(task.getTaskName());
        response.setCandidates(candidates);
        response.setPassCount(passCount);
        response.setFailCount(failCount);
        response.setUnknownCount(unknownCount);
        
        return response;
    }
    
    /**
     * 检查资源组是否有硬性规则参数未填写
     * 如果硬性规则参数未填写，返回true（应该跳过该资源组）
     * 
     * 规则：
     * - 如果资源组有硬性规则（is_required = TRUE）且该规则有数值限制（min 或 max 不为 null）
     * - 但用户未填写对应的参数值
     * - 则应该跳过该资源组（不显示）
     */
    private boolean hasMissingRequiredParams(RgResourceGroup rg, ResourceGroupSelectRequest request) {
        List<RgResourceRuleCondition> structRules = resourceRuleMapper.findStructRulesByResourceGroupId(rg.getId());
        
        for (RgResourceRuleCondition rule : structRules) {
            // 只检查硬性规则（is_required = TRUE）
            if (!Boolean.TRUE.equals(rule.getIsRequired())) {
                continue;
            }
            
            String paramCode = rule.getParamCode();
            BigDecimal min = rule.getValueMin();
            BigDecimal max = rule.getValueMax();
            
            // 如果规则有数值限制（min 或 max 不为 null），说明有要求
            if (min != null || max != null) {
                // 检查对应的参数是否填写
                Number inputValue = getNumericInputValue(paramCode, request);
                if (inputValue == null) {
                    // 硬性规则参数未填写，应该跳过该资源组
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * 评估资源组
     */
    private ResourceGroupCandidate evaluateResourceGroup(RgResourceGroup rg, ResourceGroupSelectRequest request) {
        ResourceGroupCandidate candidate = new ResourceGroupCandidate();
        candidate.setId(rg.getId());
        candidate.setResourceGroupCode(rg.getResourceGroupCode());
        candidate.setName(rg.getName());
        candidate.setWorkCenterCode(rg.getWorkCenterCode());
        candidate.setWorkCenterName(rg.getWorkCenterName());
        candidate.setFamily(rg.getFamily());
        
        // 获取规则
        List<RgResourceRuleCondition> structRules = resourceRuleMapper.findStructRulesByResourceGroupId(rg.getId());
        List<RgResourceRuleCondition> textRules = resourceRuleMapper.findTextRulesByResourceGroupId(rg.getId());
        
        // 分离阻塞规则、硬性规则、可选规则
        List<RgResourceRuleCondition> blockingRules = structRules.stream()
            .filter(r -> Boolean.TRUE.equals(r.getIsBlocking()))
            .collect(Collectors.toList());
        
        List<RgResourceRuleCondition> requiredRules = structRules.stream()
            .filter(r -> Boolean.TRUE.equals(r.getIsRequired()) && !Boolean.TRUE.equals(r.getIsBlocking()))
            .collect(Collectors.toList());
        
        List<RgResourceRuleCondition> optionalRules = structRules.stream()
            .filter(r -> !Boolean.TRUE.equals(r.getIsRequired()) && !Boolean.TRUE.equals(r.getIsBlocking()))
            .collect(Collectors.toList());
        
        List<ResourceGroupCandidate.MatchDetail> matchDetails = new ArrayList<>();
        
        // 1. 先检查阻塞规则
        for (RgResourceRuleCondition rule : blockingRules) {
            ResourceGroupCandidate.MatchDetail detail = evaluateCondition(rule, request);
            if (detail != null) {
                matchDetails.add(detail);
                if ("FAIL".equals(detail.getStatus())) {
                    candidate.setMatchStatus("FAIL");
                    candidate.setMatchDetails(matchDetails);
                    return candidate;
                }
            }
        }
        
        // 2. 检查硬性规则
        for (RgResourceRuleCondition rule : requiredRules) {
            ResourceGroupCandidate.MatchDetail detail = evaluateCondition(rule, request);
            if (detail != null) {
                matchDetails.add(detail);
                if ("FAIL".equals(detail.getStatus())) {
                    candidate.setMatchStatus("FAIL");
                    candidate.setMatchDetails(matchDetails);
                    return candidate;
                }
            }
        }
        
        // 3. 检查可选规则
        boolean hasOptionalPass = false;
        Map<String, List<ResourceGroupCandidate.MatchDetail>> optionalGroups = new HashMap<>();
        
        for (RgResourceRuleCondition rule : optionalRules) {
            String group = rule.getConditionGroup() != null ? rule.getConditionGroup() : "OPTIONAL_1";
            optionalGroups.computeIfAbsent(group, k -> new ArrayList<>());
            
            ResourceGroupCandidate.MatchDetail detail = evaluateCondition(rule, request);
            if (detail != null) {
                optionalGroups.get(group).add(detail);
                if ("PASS".equals(detail.getStatus())) {
                    hasOptionalPass = true;
                }
            }
        }
        
        // 如果可选规则组中有任何一个组全部通过，则整体通过
        if (hasOptionalPass) {
            for (List<ResourceGroupCandidate.MatchDetail> groupDetails : optionalGroups.values()) {
                boolean allPass = groupDetails.stream().allMatch(d -> "PASS".equals(d.getStatus()));
                if (allPass && !groupDetails.isEmpty()) {
                    matchDetails.addAll(groupDetails);
                    candidate.setMatchStatus("PASS");
                    candidate.setMatchDetails(matchDetails);
                    return candidate;
                }
            }
        }
        
        // 如果硬性规则都通过，但没有可选规则通过，标记为UNKNOWN
        if (matchDetails.stream().noneMatch(d -> "FAIL".equals(d.getStatus()))) {
            candidate.setMatchStatus("UNKNOWN");
        } else {
            candidate.setMatchStatus("FAIL");
        }
        
        candidate.setMatchDetails(matchDetails);
        return candidate;
    }
    
    /**
     * 评估单个条件
     */
    private ResourceGroupCandidate.MatchDetail evaluateCondition(
            RgResourceRuleCondition rule, ResourceGroupSelectRequest request) {
        
        ResourceGroupCandidate.MatchDetail detail = new ResourceGroupCandidate.MatchDetail();
        detail.setParamCode(rule.getParamCode());
        detail.setParamName(rule.getParamName());
        
        String paramCode = rule.getParamCode();
        BigDecimal min = rule.getValueMin();
        BigDecimal max = rule.getValueMax();
        
        // 获取用户输入值
        Number inputValue = getNumericInputValue(paramCode, request);
        
        if (inputValue == null) {
            // 如果硬性规则参数未填写
            if (Boolean.TRUE.equals(rule.getIsRequired())) {
                detail.setStatus("FAIL");
                detail.setInputValue("未填写");
                detail.setRuleValue(buildRuleValueString(min, max));
                return detail;
            } else {
                // 可选规则参数未填写，返回null（不添加到详情中）
                return null;
            }
        }
        
        // 检查范围
        boolean pass = checkRange(inputValue, min, max);
        
        detail.setInputValue(String.valueOf(inputValue));
        detail.setRuleValue(buildRuleValueString(min, max));
        detail.setStatus(pass ? "PASS" : "FAIL");
        
        return detail;
    }
    
    /**
     * 获取数值输入值
     */
    private Number getNumericInputValue(String paramCode, ResourceGroupSelectRequest request) {
        switch (paramCode) {
            case "width":
                return request.getWidth();
            case "height":
                return request.getHeight();
            case "sheet_count":
                return request.getSheetCount();
            case "gsm":
                return request.getGsm();
            case "thickness":
                return request.getThickness() != null ? request.getThickness() : null;
            default:
                return null;
        }
    }
    
    /**
     * 检查值是否在范围内
     */
    private boolean checkRange(Number value, BigDecimal min, BigDecimal max) {
        BigDecimal valueDecimal = value instanceof BigDecimal 
            ? (BigDecimal) value 
            : BigDecimal.valueOf(value.doubleValue());
        
        if (min != null && valueDecimal.compareTo(min) < 0) {
            return false;
        }
        if (max != null && valueDecimal.compareTo(max) > 0) {
            return false;
        }
        return true;
    }
    
    /**
     * 构建规则值字符串
     */
    private String buildRuleValueString(BigDecimal min, BigDecimal max) {
        if (min != null && max != null) {
            return min + " ~ " + max;
        } else if (min != null) {
            return ">= " + min;
        } else if (max != null) {
            return "<= " + max;
        } else {
            return "无限制";
        }
    }
    
    @Override
    public Map<String, Object> getWorkCenterDetail(String workCenterCode) {
        // TODO: 实现获取工作中心详情
        return new HashMap<>();
    }
    
    @Override
    public Map<String, List<String>> getParamOptions() {
        // TODO: 实现获取公共参数下拉选项
        Map<String, List<String>> result = new HashMap<>();
        result.put("departments", resourceRuleMapper.findDistinctValuesByParamCode("department"));
        result.put("productTypes", resourceRuleMapper.findDistinctValuesByParamCode("product_type"));
        result.put("surfaceTypes", resourceRuleMapper.findDistinctValuesByParamCode("surface_type"));
        result.put("grainDirections", resourceRuleMapper.findDistinctValuesByParamCode("grain_direction"));
        return result;
    }
}

