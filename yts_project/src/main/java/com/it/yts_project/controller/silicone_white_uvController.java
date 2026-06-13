package com.it.yts_project.controller;

// 源文件: 硅胶白UV(Matt UV-Silk Screen)标准书-20210818 (1).xlsx

import com.it.yts_project.model.silicone_white_uvProduct;
import com.it.yts_project.model.silicone_white_uvCompatibility;
import com.it.yts_project.service.silicone_white_uvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 硅胶_白UV Controller
 */
@RestController
@RequestMapping("/api/silicone_white_uv")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class silicone_white_uvController {

    @Autowired
    private silicone_white_uvService service;

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    @GetMapping("/products")
    public ResponseEntity<List<silicone_white_uvProduct>> getAllProducts() {
        List<silicone_white_uvProduct> list = service.getAllProducts();
        return ResponseEntity.ok(list);
    }

    /**
     * 获取激活的产品
     */
    @GetMapping("/products/active")
    public ResponseEntity<List<silicone_white_uvProduct>> getActiveProducts() {
        List<silicone_white_uvProduct> list = service.getActiveProducts();
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<silicone_white_uvProduct> getProductById(@PathVariable Integer id) {
        silicone_white_uvProduct product = service.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 搜索产品
     */
    @GetMapping("/products/search")
    public ResponseEntity<List<silicone_white_uvProduct>> searchProducts(@RequestParam String keyword) {
        List<silicone_white_uvProduct> list = service.searchProducts(keyword);
        return ResponseEntity.ok(list);
    }

    /**
     * 新增产品
     */
    @PostMapping("/products")
    public ResponseEntity<silicone_white_uvProduct> createProduct(@RequestBody silicone_white_uvProduct product) {
        silicone_white_uvProduct saved = service.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<silicone_white_uvProduct> updateProduct(@PathVariable Integer id, @RequestBody silicone_white_uvProduct product) {
        product.setId(id);
        silicone_white_uvProduct updated = service.saveProduct(product);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            service.deleteProduct(id);
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID获取兼容性列表
     */
    @GetMapping("/compatibilities")
    public ResponseEntity<List<silicone_white_uvCompatibility>> getCompatibilitiesByProductId(
            @RequestParam(required = false) Integer productId) {
        if (productId != null) {
            return ResponseEntity.ok(service.getCompatibilitiesByProductId(productId));
        }
        return ResponseEntity.ok(service.getAllCompatibilities());
    }

    /**
     * 根据ID获取兼容性
     */
    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<silicone_white_uvCompatibility> getCompatibilityById(@PathVariable Integer id) {
        silicone_white_uvCompatibility compatibility = service.getCompatibilityById(id);
        if (compatibility != null) {
            return ResponseEntity.ok(compatibility);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 新增兼容性
     */
    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody silicone_white_uvCompatibility compatibility) {
        try {
            silicone_white_uvCompatibility saved = service.saveCompatibility(compatibility);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "保存失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 更新兼容性
     */
    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody silicone_white_uvCompatibility compatibility) {
        try {
            compatibility.setId(id);
            silicone_white_uvCompatibility updated = service.saveCompatibility(compatibility);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 批量保存兼容性
     */
    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<silicone_white_uvCompatibility> compatibilities) {
        try {
            service.batchSaveCompatibilities(compatibilities);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "批量保存成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量保存失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除兼容性
     */
    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Map<String, Object>> deleteCompatibility(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            service.deleteCompatibility(id);
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据产品ID删除兼容性
     */
    @DeleteMapping("/compatibilities/by-product/{productId}")
    public ResponseEntity<Map<String, Object>> deleteCompatibilitiesByProductId(@PathVariable Integer productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            service.deleteCompatibilitiesByProductId(productId);
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
