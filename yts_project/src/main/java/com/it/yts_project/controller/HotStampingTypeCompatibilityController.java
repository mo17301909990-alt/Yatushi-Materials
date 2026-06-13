package com.it.yts_project.controller;

import com.it.yts_project.dto.HotStampingTypeCompatibilityCopyResult;
import com.it.yts_project.model.HotStampingTypeCompatibility;
import com.it.yts_project.service.HotStampingTypeCompatibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 烫金类型兼容性控制器
 */
@RestController
@RequestMapping("/api/hot-stamping-type-compatibility")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class HotStampingTypeCompatibilityController {
    
    @Autowired
    private HotStampingTypeCompatibilityService compatibilityService;
    
    /**
     * 根据烫金类型ID获取兼容性配置列表
     */
    @GetMapping("/hot-stamping-type/{hotStampingTypeId}")
    public ResponseEntity<List<HotStampingTypeCompatibility>> getCompatibilityByHotStampingTypeId(
            @PathVariable Integer hotStampingTypeId) {
        List<HotStampingTypeCompatibility> compatibilities = 
            compatibilityService.getCompatibilityByHotStampingTypeId(hotStampingTypeId);
        return ResponseEntity.ok(compatibilities);
    }
    
    /**
     * 根据ID获取兼容性配置
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotStampingTypeCompatibility> getCompatibilityById(@PathVariable Integer id) {
        HotStampingTypeCompatibility compatibility = compatibilityService.getCompatibilityById(id);
        if (compatibility != null) {
            return ResponseEntity.ok(compatibility);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 创建兼容性配置
     */
    @PostMapping
    public ResponseEntity<?> createCompatibility(
            @RequestBody HotStampingTypeCompatibility compatibility) {
        try {
            HotStampingTypeCompatibility created = compatibilityService.createCompatibility(compatibility);
            return ResponseEntity.ok(created);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "该燙金紙系列和烫金类型的组合已存在，请选择其他组合"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "创建失败：" + e.getMessage()));
        }
    }
    
    /**
     * 更新兼容性配置
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompatibility(
            @PathVariable Integer id,
            @RequestBody HotStampingTypeCompatibility compatibility) {
        try {
            compatibility.setId(id);
            HotStampingTypeCompatibility updated = compatibilityService.updateCompatibility(compatibility);
            return ResponseEntity.ok(updated);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "该燙金紙系列和烫金类型的组合已存在，请选择其他组合"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "更新失败：" + e.getMessage()));
        }
    }
    
    /**
     * 删除兼容性配置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        compatibilityService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 根据烫金类型ID删除所有兼容性配置
     */
    @DeleteMapping("/hot-stamping-type/{hotStampingTypeId}")
    public ResponseEntity<Void> deleteCompatibilityByHotStampingTypeId(@PathVariable Integer hotStampingTypeId) {
        compatibilityService.deleteCompatibilityByHotStampingTypeId(hotStampingTypeId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取所有可用的燙金紙系列
     */
    @GetMapping("/product-names")
    public ResponseEntity<List<String>> getAllProductNames() {
        List<String> productNames = compatibilityService.getAllProductNames();
        return ResponseEntity.ok(productNames);
    }
    
    /**
     * 获取所有可用的纸张类型
     */
    @GetMapping("/paper-types")
    public ResponseEntity<List<String>> getAllPaperTypes() {
        List<String> paperTypes = compatibilityService.getAllPaperTypes();
        return ResponseEntity.ok(paperTypes);
    }

    /**
     * 根据产品名称获取对应的纸张类型
     */
    @GetMapping("/paper-types-by-product")
    public ResponseEntity<List<String>> getPaperTypesByProductName(@RequestParam String productName) {
        List<String> paperTypes = compatibilityService.getPaperTypesByProductName(productName);
        return ResponseEntity.ok(paperTypes);
    }
    
    /**
     * 批量复制兼容性配置到其他烫金类型
     */
    @PostMapping("/batch-copy")
    public ResponseEntity<HotStampingTypeCompatibilityCopyResult> batchCopyCompatibility(
            @RequestBody Map<String, Object> requestBody) {
        try {
            @SuppressWarnings("unchecked")
            List<Integer> sourceIds = (List<Integer>) requestBody.get("sourceIds");
            @SuppressWarnings("unchecked")
            List<Integer> targetHotStampingTypeIds = (List<Integer>) requestBody.get("targetHotStampingTypeIds");
            
            HotStampingTypeCompatibilityCopyResult result = compatibilityService.batchCopyCompatibility(
                    sourceIds, targetHotStampingTypeIds);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new HotStampingTypeCompatibilityCopyResult(0, 0, "批量复制失败：" + e.getMessage()));
        }
    }
    
    /**
     * 批量删除兼容性配置
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> response = new java.util.HashMap<>();
        try {
            int deletedCount = compatibilityService.batchDeleteCompatibility(ids);
            response.put("success", true);
            response.put("deletedCount", deletedCount);
            response.put("message", "成功删除 " + deletedCount + " 条记录");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量删除失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 获取所有兼容性配置（包含烫金类型信息）
     */
    @GetMapping("/all")
    public ResponseEntity<List<HotStampingTypeCompatibility>> getAllCompatibility() {
        List<HotStampingTypeCompatibility> compatibilities = compatibilityService.getAllCompatibility();
        return ResponseEntity.ok(compatibilities);
    }
}
