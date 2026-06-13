package com.it.yts_project.controller;

import com.it.yts_project.dto.CompatibilityQueryParams;
import com.it.yts_project.dto.HotStampingCompatibilityDetailDTO;
import com.it.yts_project.model.HotStampingCompatibility;
import com.it.yts_project.service.SmartCompatibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 耐磨金纸映射管理控制器
 */
@RestController
@RequestMapping("/api/smart-compatibility")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SmartCompatibilityController {

    @Autowired
    private SmartCompatibilityService smartCompatibilityService;

    /**
     * 获取兼容性规则列表
     */
    @GetMapping("/rules")
    public ResponseEntity<Map<String, Object>> getRules(
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String patternType,
            @RequestParam(required = false) String hotStampingType,
            @RequestParam(required = false) String compatibility,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            CompatibilityQueryParams params = new CompatibilityQueryParams();
            params.setProductType(productType);
            params.setPatternType(patternType);
            params.setHotStampingType(hotStampingType);
            params.setCompatibility(compatibility != null && !compatibility.isEmpty() ? compatibility.charAt(0) : null);
            params.setSearch(search);
            params.setPage(page);
            params.setSize(size);
            
            Map<String, Object> result = smartCompatibilityService.getRules(params);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "获取兼容性规则失败: " + e.getMessage()));
        }
    }

    /**
     * 获取单个兼容性规则
     */
    @GetMapping("/rules/{id}")
    public ResponseEntity<HotStampingCompatibility> getRule(@PathVariable Long id) {
        try {
            HotStampingCompatibility rule = smartCompatibilityService.getRule(id);
            if (rule != null) {
                return ResponseEntity.ok(rule);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 创建兼容性规则
     */
    @PostMapping("/rules")
    public ResponseEntity<?> createRule(@RequestBody HotStampingCompatibility rule) {
        try {
            HotStampingCompatibility createdRule = smartCompatibilityService.createRule(rule);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRule);
        } catch (RuntimeException e) {
            // 业务异常（如重复数据）返回400错误和错误信息
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "创建规则失败: " + e.getMessage()));
        }
    }

    /**
     * 更新兼容性规则
     */
    @PutMapping("/rules/{id}")
    public ResponseEntity<?> updateRule(
            @PathVariable Long id, 
            @RequestBody HotStampingCompatibility rule) {
        try {
            rule.setId(id);
            HotStampingCompatibility updatedRule = smartCompatibilityService.updateRule(rule);
            if (updatedRule != null) {
                return ResponseEntity.ok(updatedRule);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            // 业务异常（如重复数据）返回400错误和错误信息
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "更新规则失败: " + e.getMessage()));
        }
    }

    /**
     * 删除兼容性规则
     */
    @DeleteMapping("/rules/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        try {
            boolean deleted = smartCompatibilityService.deleteRule(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 批量操作
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchOperation(@RequestBody Map<String, Object> operation) {
        try {
            Map<String, Object> result = smartCompatibilityService.batchOperation(operation);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "批量操作失败: " + e.getMessage()));
        }
    }

    /**
     * 获取兼容性矩阵
     */
    @GetMapping("/matrix")
    public ResponseEntity<Map<String, Object>> getMatrix() {
        try {
            Map<String, Object> matrix = smartCompatibilityService.getMatrix();
            return ResponseEntity.ok(matrix);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "获取兼容性矩阵失败: " + e.getMessage()));
        }
    }

    /**
     * 获取选项数据
     */
    @GetMapping("/options")
    public ResponseEntity<Map<String, List<String>>> getOptions() {
        try {
            Map<String, List<String>> options = smartCompatibilityService.getOptions();
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", List.of("获取选项数据失败: " + e.getMessage())));
        }
    }

    /**
     * 验证兼容性规则
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateRule(@RequestBody HotStampingCompatibility rule) {
        try {
            Map<String, Object> validation = smartCompatibilityService.validateRule(rule);
            return ResponseEntity.ok(validation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "验证规则失败: " + e.getMessage()));
        }
    }

    /**
     * 导入兼容性规则
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importRules(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = smartCompatibilityService.importRules(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "导入规则失败: " + e.getMessage()));
        }
    }

    /**
     * 导出兼容性规则
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportRules(
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String patternType,
            @RequestParam(required = false) String hotStampingType,
            @RequestParam(required = false) String compatibility,
            @RequestParam(required = false) String search) {
        
        try {
            CompatibilityQueryParams params = new CompatibilityQueryParams();
            params.setProductType(productType);
            params.setPatternType(patternType);
            params.setHotStampingType(hotStampingType);
            params.setCompatibility(compatibility != null && !compatibility.isEmpty() ? compatibility.charAt(0) : null);
            params.setSearch(search);
            
            byte[] excelData = smartCompatibilityService.exportRules(params);
            
            return ResponseEntity.ok()
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .header("Content-Disposition", "attachment; filename=compatibility_rules.xlsx")
                    .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 复制兼容性规则
     */
    @PostMapping("/rules/{id}/copy")
    public ResponseEntity<HotStampingCompatibility> copyRule(
            @PathVariable Long id, 
            @RequestBody(required = false) HotStampingCompatibility modifications) {
        try {
            HotStampingCompatibility copiedRule = smartCompatibilityService.copyRule(id, modifications);
            return ResponseEntity.ok(copiedRule);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取规则统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> statistics = smartCompatibilityService.getStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "获取统计信息失败: " + e.getMessage()));
        }
    }

    /**
     * 搜索兼容性规则
     */
    @GetMapping("/search")
    public ResponseEntity<List<HotStampingCompatibility>> searchRules(@RequestParam String q) {
        try {
            List<HotStampingCompatibility> rules = smartCompatibilityService.searchRules(q);
            return ResponseEntity.ok(rules);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取规则预览
     */
    @PostMapping("/preview")
    public ResponseEntity<Map<String, Object>> getRulePreview(@RequestBody HotStampingCompatibility rule) {
        try {
            Map<String, Object> preview = smartCompatibilityService.getRulePreview(rule);
            return ResponseEntity.ok(preview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "获取规则预览失败: " + e.getMessage()));
        }
    }

    /**
     * 核心业务接口：根据多个ID字段筛选烫金纸性能类型
     * 这是耐磨金纸映射管理的核心功能
     */
    @GetMapping("/filter-paper-performance")
    public ResponseEntity<List<String>> filterPaperPerformance(
            @RequestParam(required = false) Integer productTypeId,
            @RequestParam(required = false) Integer patternCharacteristicId,
            @RequestParam(required = false) Integer hotStampingTypeId,
            @RequestParam(required = false) Integer preProcessingStepId,
            @RequestParam(required = false) Integer postProcessingStepId) {
        try {
            List<String> paperPerformanceTypes = smartCompatibilityService.getPaperPerformanceByMultipleIds(
                    productTypeId,
                    patternCharacteristicId,
                    hotStampingTypeId,
                    preProcessingStepId,
                    postProcessingStepId
            );
            return ResponseEntity.ok(paperPerformanceTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取完整的兼容性规则（包含所有字段）
     */
    @GetMapping("/compatibility-rules")
    public ResponseEntity<List<HotStampingCompatibility>> getCompatibilityRules(
            @RequestParam(required = false) Integer productTypeId,
            @RequestParam(required = false) Integer patternCharacteristicId,
            @RequestParam(required = false) Integer hotStampingTypeId,
            @RequestParam(required = false) Integer preProcessingStepId,
            @RequestParam(required = false) Integer postProcessingStepId) {
        try {
            List<HotStampingCompatibility> rules = smartCompatibilityService.getCompatibilityRulesByMultipleIds(
                    productTypeId,
                    patternCharacteristicId,
                    hotStampingTypeId,
                    preProcessingStepId,
                    postProcessingStepId
            );
            return ResponseEntity.ok(rules);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 获取完整的兼容性规则列表（包含关联表信息）
     * 用于耐磨金纸映射管理界面，方便用户管理规则
     */
    @GetMapping("/rules-with-details")
    public ResponseEntity<List<HotStampingCompatibilityDetailDTO>> getCompatibilityRulesWithDetails() {
        try {
            List<HotStampingCompatibilityDetailDTO> rules = smartCompatibilityService.getCompatibilityRulesWithDetails();
            return ResponseEntity.ok(rules);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
