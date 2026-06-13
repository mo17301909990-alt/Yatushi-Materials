package com.it.yts_project.controller;

import com.it.yts_project.dto.ClothPaperTypeDTO;
import com.it.yts_project.service.ClothPaperTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 布料纸类型控制器
 */
@RestController
@RequestMapping("/api/cloth-paper-types")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class ClothPaperTypeController {
    
    @Autowired
    private ClothPaperTypeService clothPaperTypeService;
    
    /**
     * 获取所有激活的布料纸类型
     * @return 布料纸类型列表
     */
    @GetMapping("/active")
    public List<ClothPaperTypeDTO> getActiveClothPaperTypes() {
        return clothPaperTypeService.getAllActiveClothPaperTypes();
    }
    
    /**
     * 获取所有布料纸类型（包括未激活的）
     * @return 布料纸类型列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<ClothPaperTypeDTO>> getAllClothPaperTypes() {
        List<ClothPaperTypeDTO> types = clothPaperTypeService.getAllClothPaperTypes();
        return ResponseEntity.ok(types);
    }
    
    /**
     * 根据ID获取布料纸类型
     * @param id 主键ID
     * @return 布料纸类型
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClothPaperTypeDTO> getById(@PathVariable Integer id) {
        ClothPaperTypeDTO type = clothPaperTypeService.getById(id);
        if (type != null) {
            return ResponseEntity.ok(type);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据分类获取布料纸类型
     * @param category 分类
     * @return 布料纸类型列表
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ClothPaperTypeDTO>> getByCategory(@PathVariable String category) {
        List<ClothPaperTypeDTO> types = clothPaperTypeService.getByCategory(category);
        return ResponseEntity.ok(types);
    }
    
    /**
     * 创建布料纸类型
     * @param clothPaperType 布料纸类型
     * @return 创建的布料纸类型
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ClothPaperTypeDTO clothPaperType) {
        try {
            // 验证必填字段
            if (clothPaperType.getTypeName() == null || clothPaperType.getTypeName().trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "类型名称不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            ClothPaperTypeDTO createdType = clothPaperTypeService.create(clothPaperType);
            return ResponseEntity.ok(createdType);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // 处理数据库约束违反（如唯一约束）
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            if (e.getMessage() != null && (e.getMessage().contains("unique") || e.getMessage().contains("UNIQUE"))) {
                response.put("error", "类型名称已存在，请使用其他名称");
            } else {
                response.put("error", "数据验证失败：" + (e.getMessage() != null ? e.getMessage() : "未知错误"));
            }
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "创建失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新布料纸类型
     * @param id 主键ID
     * @param clothPaperType 布料纸类型
     * @return 更新后的布料纸类型
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ClothPaperTypeDTO clothPaperType) {
        try {
            // 验证必填字段
            if (clothPaperType.getTypeName() == null || clothPaperType.getTypeName().trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("error", "类型名称不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            clothPaperType.setId(id);
            ClothPaperTypeDTO updatedType = clothPaperTypeService.update(clothPaperType);
            return ResponseEntity.ok(updatedType);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // 处理数据库约束违反（如唯一约束）
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            if (e.getMessage() != null && (e.getMessage().contains("unique") || e.getMessage().contains("UNIQUE"))) {
                response.put("error", "类型名称已存在，请使用其他名称");
            } else {
                response.put("error", "数据验证失败：" + e.getMessage());
            }
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "更新失败：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除布料纸类型
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) {
        try {
            boolean success = clothPaperTypeService.deleteById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "删除成功" : "删除失败");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 批量更新状态
     * @param request 批量更新请求
     * @return 更新结果
     */
    @PutMapping("/batch-status")
    public ResponseEntity<Map<String, Object>> batchUpdateStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Integer> ids = (List<Integer>) request.get("ids");
            Boolean isActive = (Boolean) request.get("isActive");
            
            boolean success = clothPaperTypeService.batchUpdateStatus(ids, isActive);
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "批量更新成功" : "批量更新失败");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新排序顺序
     * @param id 主键ID
     * @param request 更新请求
     * @return 更新结果
     */
    @PutMapping("/{id}/order")
    public ResponseEntity<Map<String, Object>> updateSortOrder(@PathVariable Integer id, @RequestBody Map<String, Object> request) {
        try {
            Integer sortOrder = (Integer) request.get("sortOrder");
            boolean success = clothPaperTypeService.updateSortOrder(id, sortOrder);
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "更新顺序成功" : "更新顺序失败");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新顺序失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}


