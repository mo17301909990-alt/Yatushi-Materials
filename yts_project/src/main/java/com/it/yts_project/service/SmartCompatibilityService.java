package com.it.yts_project.service;

import com.it.yts_project.dto.CompatibilityQueryParams;
import com.it.yts_project.dto.HotStampingCompatibilityDetailDTO;
import com.it.yts_project.model.HotStampingCompatibility;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 耐磨金纸映射管理服务接口
 */
public interface SmartCompatibilityService {

    /**
     * 获取兼容性规则列表
     */
    Map<String, Object> getRules(CompatibilityQueryParams params);

    /**
     * 获取单个兼容性规则
     */
    HotStampingCompatibility getRule(Long id);

    /**
     * 创建兼容性规则
     */
    HotStampingCompatibility createRule(HotStampingCompatibility rule);

    /**
     * 更新兼容性规则
     */
    HotStampingCompatibility updateRule(HotStampingCompatibility rule);

    /**
     * 删除兼容性规则
     */
    boolean deleteRule(Long id);

    /**
     * 批量操作
     */
    Map<String, Object> batchOperation(Map<String, Object> operation);

    /**
     * 获取兼容性矩阵
     */
    Map<String, Object> getMatrix();

    /**
     * 获取选项数据
     */
    Map<String, List<String>> getOptions();

    /**
     * 验证兼容性规则
     */
    Map<String, Object> validateRule(HotStampingCompatibility rule);

    /**
     * 导入兼容性规则
     */
    Map<String, Object> importRules(MultipartFile file);

    /**
     * 导出兼容性规则
     */
    byte[] exportRules(CompatibilityQueryParams params);

    /**
     * 复制兼容性规则
     */
    HotStampingCompatibility copyRule(Long id, HotStampingCompatibility modifications);

    /**
     * 获取规则统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 搜索兼容性规则
     */
    List<HotStampingCompatibility> searchRules(String query);

    /**
     * 获取规则预览
     */
    Map<String, Object> getRulePreview(HotStampingCompatibility rule);

    /**
     * 根据多个ID字段筛选烫金纸性能类型
     */
    List<String> getPaperPerformanceByMultipleIds(
            Integer productTypeId,
            Integer patternCharacteristicId,
            Integer hotStampingTypeId,
            Integer preProcessingStepId,
            Integer postProcessingStepId
    );

    /**
     * 根据条件获取完整的兼容性规则
     */
    List<HotStampingCompatibility> getCompatibilityRulesByMultipleIds(
            Integer productTypeId,
            Integer patternCharacteristicId,
            Integer hotStampingTypeId,
            Integer preProcessingStepId,
            Integer postProcessingStepId
    );

    /**
     * 获取完整的兼容性规则列表（包含关联表信息）
     */
    List<HotStampingCompatibilityDetailDTO> getCompatibilityRulesWithDetails();
}
