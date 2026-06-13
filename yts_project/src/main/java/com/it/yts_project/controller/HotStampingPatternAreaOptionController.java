package com.it.yts_project.controller;

import com.it.yts_project.dto.HotStampingPatternAreaOptionDTO;
import com.it.yts_project.service.HotStampingPatternAreaOptionService;
import com.it.yts_project.service.HotStampingPatternAreaCompatibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 烫金图案区域选项控制器
 */
@RestController
@RequestMapping("/api/hot-stamping-pattern-area-options")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class HotStampingPatternAreaOptionController {
    
    @Autowired
    private HotStampingPatternAreaOptionService hotStampingPatternAreaOptionService;
    
    @Autowired
    private HotStampingPatternAreaCompatibilityService hotStampingPatternAreaCompatibilityService;
    
    /**
     * 获取所有激活的烫金图案区域选项
     * @return 烫金图案区域选项列表
     */
    @GetMapping("/active")
    public ResponseEntity<List<HotStampingPatternAreaOptionDTO>> getActivePatternAreaOptions() {
        try {
            List<HotStampingPatternAreaOptionDTO> options = hotStampingPatternAreaOptionService.getAllActivePatternAreaOptions();
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 获取所有烫金图案区域选项
     * @return 烫金图案区域选项列表
     */
    @GetMapping
    public ResponseEntity<List<HotStampingPatternAreaOptionDTO>> getAllPatternAreaOptions() {
        try {
            List<HotStampingPatternAreaOptionDTO> options = hotStampingPatternAreaOptionService.getAllPatternAreaOptions();
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID获取烫金图案区域选项
     * @param id 选项ID
     * @return 烫金图案区域选项
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotStampingPatternAreaOptionDTO> getPatternAreaOptionById(@PathVariable Integer id) {
        try {
            HotStampingPatternAreaOptionDTO option = hotStampingPatternAreaOptionService.getPatternAreaOptionById(id);
            if (option != null) {
                return ResponseEntity.ok(option);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 创建烫金图案区域选项
     * @param option 烫金图案区域选项数据
     * @return 创建的烫金图案区域选项
     */
    @PostMapping
    public ResponseEntity<HotStampingPatternAreaOptionDTO> createPatternAreaOption(@RequestBody HotStampingPatternAreaOptionDTO option) {
        try {
            HotStampingPatternAreaOptionDTO createdOption = hotStampingPatternAreaOptionService.createPatternAreaOption(option);
            return ResponseEntity.ok(createdOption);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 更新烫金图案区域选项
     * @param id 选项ID
     * @param option 烫金图案区域选项数据
     * @return 更新后的烫金图案区域选项
     */
    @PutMapping("/{id}")
    public ResponseEntity<HotStampingPatternAreaOptionDTO> updatePatternAreaOption(@PathVariable Integer id, @RequestBody HotStampingPatternAreaOptionDTO option) {
        try {
            HotStampingPatternAreaOptionDTO updatedOption = hotStampingPatternAreaOptionService.updatePatternAreaOption(id, option);
            if (updatedOption != null) {
                return ResponseEntity.ok(updatedOption);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 获取删除信息（用于确认提示）
     * @param id 选项ID
     * @return 删除信息（包含关联映射数量）
     */
    @GetMapping("/{id}/delete-info")
    public ResponseEntity<Map<String, Object>> getDeleteInfo(@PathVariable Integer id) {
        Map<String, Object> info = new HashMap<>();
        try {
            // 获取关联的映射数量
            List<com.it.yts_project.model.HotStampingPatternAreaCompatibility> compatibilities = 
                hotStampingPatternAreaCompatibilityService.getCompatibilityByPatternAreaId(id);
            int mappingCount = compatibilities != null ? compatibilities.size() : 0;
            
            info.put("mappingCount", mappingCount);
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            info.put("mappingCount", 0);
            return ResponseEntity.ok(info);
        }
    }

    /**
     * 删除烫金图案区域选项
     * @param id 选项ID
     * @return 删除结果（包含删除的映射数量）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePatternAreaOption(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 使用带事务的删除方法，自动级联删除映射关系
            int mappingCount = hotStampingPatternAreaOptionService.deletePatternAreaOptionWithMappings(id);
            
            response.put("success", true);
            response.put("message", String.format("删除成功，同时删除了 %d 条关联的映射配置", mappingCount));
            response.put("mappingCount", mappingCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 批量删除烫金图案区域选项
     * @param ids 选项ID列表
     * @return 删除结果（包含删除的映射数量）
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeletePatternAreaOptions(@RequestBody List<Integer> ids) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 使用带事务的批量删除方法，自动级联删除映射关系
            int totalMappingCount = hotStampingPatternAreaOptionService.batchDeletePatternAreaOptionsWithMappings(ids);
            
            response.put("success", true);
            response.put("message", String.format("批量删除成功，同时删除了 %d 条关联的映射配置", totalMappingCount));
            response.put("mappingCount", totalMappingCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量删除失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
