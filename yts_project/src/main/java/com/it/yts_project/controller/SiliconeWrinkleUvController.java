package com.it.yts_project.controller;

// 源文件: 硅胶皱纹UV(Wrinkle Frost)标准书-20210811.xlsx

import com.it.yts_project.model.SiliconeWrinkleUvCompatibility;
import com.it.yts_project.model.SiliconeWrinkleUvProduct;
import com.it.yts_project.service.SiliconeWrinkleUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 硅胶_皱纹UV Controller
 */
@RestController
@RequestMapping("/api/silicone_wrinkle_uv")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeWrinkleUvController {

    @Autowired
    private SiliconeWrinkleUvService service;

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    @GetMapping("/products")
    public ResponseEntity<List<SiliconeWrinkleUvProduct>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    /**
     * 获取启用的产品
     */
    @GetMapping("/products/active")
    public ResponseEntity<List<SiliconeWrinkleUvProduct>> getActiveProducts() {
        return ResponseEntity.ok(service.getActiveProducts());
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeWrinkleUvProduct> getProductById(@PathVariable Integer id) {
        SiliconeWrinkleUvProduct product = service.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 搜索产品
     */
    @GetMapping("/products/search")
    public ResponseEntity<List<SiliconeWrinkleUvProduct>> searchProducts(
            @RequestParam(required = false) String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.ok(service.getAllProducts());
        }
        return ResponseEntity.ok(service.searchProducts(keyword));
    }

    /**
     * 保存产品
     */
    @PostMapping("/products")
    public ResponseEntity<SiliconeWrinkleUvProduct> saveProduct(@RequestBody SiliconeWrinkleUvProduct product) {
        SiliconeWrinkleUvProduct saved = service.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeWrinkleUvProduct> updateProduct(
            @PathVariable Integer id,
            @RequestBody SiliconeWrinkleUvProduct product) {
        product.setId(id);
        SiliconeWrinkleUvProduct updated = service.saveProduct(product);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        service.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    /**
     * 获取兼容性列表
     */
    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeWrinkleUvCompatibility>> getAllCompatibilities() {
        return ResponseEntity.ok(service.getAllCompatibilities());
    }

    /**
     * 根据产品ID获取兼容性列表
     */
    @GetMapping("/compatibilities/product/{productId}")
    public ResponseEntity<List<SiliconeWrinkleUvCompatibility>> getCompatibilitiesByProductId(
            @PathVariable Integer productId) {
        return ResponseEntity.ok(service.getCompatibilitiesByProductId(productId));
    }

    /**
     * 根据ID获取兼容性
     */
    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeWrinkleUvCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeWrinkleUvCompatibility compatibility = service.getCompatibilityById(id);
        if (compatibility != null) {
            return ResponseEntity.ok(compatibility);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 保存兼容性
     */
    @PostMapping("/compatibilities")
    public ResponseEntity<?> saveCompatibility(@RequestBody SiliconeWrinkleUvCompatibility compatibility) {
        try {
            SiliconeWrinkleUvCompatibility saved = service.saveCompatibility(compatibility);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 更新兼容性
     */
    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(
            @PathVariable Integer id,
            @RequestBody SiliconeWrinkleUvCompatibility compatibility) {
        try {
            compatibility.setId(id);
            SiliconeWrinkleUvCompatibility updated = service.saveCompatibility(compatibility);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 批量保存兼容性
     */
    @PostMapping("/compatibilities/batch")
    public ResponseEntity<Void> batchSaveCompatibilities(
            @RequestBody List<SiliconeWrinkleUvCompatibility> compatibilities) {
        service.batchSaveCompatibilities(compatibilities);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除兼容性
     */
    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        service.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量删除兼容性
     */
    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<?> batchDeleteCompatibilities(@RequestBody List<Integer> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "请选择要删除的记录");
                return ResponseEntity.badRequest().body(response);
            }
            service.batchDeleteCompatibilities(ids);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", String.format("成功删除 %d 条记录", ids.size()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
