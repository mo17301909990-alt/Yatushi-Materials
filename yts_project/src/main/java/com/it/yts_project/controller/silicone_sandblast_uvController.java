package com.it.yts_project.controller;

// 源文件: 硅胶磨砂UV(Sandblast Frost)标准书-20210827(1).xlsx

import com.it.yts_project.model.silicone_sandblast_uvProduct;
import com.it.yts_project.model.silicone_sandblast_uvCompatibility;
import com.it.yts_project.service.silicone_sandblast_uvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 硅胶磨砂UV管理Controller
 */
@RestController
@RequestMapping("/api/silicone_sandblast_uv")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class silicone_sandblast_uvController {

    @Autowired
    private silicone_sandblast_uvService service;

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    @GetMapping("/products")
    public ResponseEntity<List<silicone_sandblast_uvProduct>> getAllProducts() {
        List<silicone_sandblast_uvProduct> list = service.getActiveProducts();
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<silicone_sandblast_uvProduct> getProductById(@PathVariable Integer id) {
        silicone_sandblast_uvProduct product = service.getProductById(id);
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
    public ResponseEntity<List<silicone_sandblast_uvProduct>> searchProducts(@RequestParam String keyword) {
        List<silicone_sandblast_uvProduct> list = service.searchProducts(keyword);
        return ResponseEntity.ok(list);
    }

    /**
     * 保存产品
     */
    @PostMapping("/products")
    public ResponseEntity<silicone_sandblast_uvProduct> saveProduct(@RequestBody silicone_sandblast_uvProduct product) {
        silicone_sandblast_uvProduct saved = service.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<silicone_sandblast_uvProduct> updateProduct(@PathVariable Integer id, @RequestBody silicone_sandblast_uvProduct product) {
        product.setId(id);
        silicone_sandblast_uvProduct updated = service.saveProduct(product);
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
     * 获取所有兼容性
     */
    @GetMapping("/compatibilities")
    public ResponseEntity<List<silicone_sandblast_uvCompatibility>> getAllCompatibilities() {
        List<silicone_sandblast_uvCompatibility> list = service.getAllCompatibilities();
        return ResponseEntity.ok(list);
    }

    /**
     * 根据产品ID获取兼容性列表
     */
    @GetMapping("/compatibilities/product/{productId}")
    public ResponseEntity<List<silicone_sandblast_uvCompatibility>> getCompatibilitiesByProductId(@PathVariable Integer productId) {
        List<silicone_sandblast_uvCompatibility> list = service.getCompatibilitiesByProductId(productId);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ID获取兼容性
     */
    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<silicone_sandblast_uvCompatibility> getCompatibilityById(@PathVariable Integer id) {
        silicone_sandblast_uvCompatibility compatibility = service.getCompatibilityById(id);
        if (compatibility != null) {
            return ResponseEntity.ok(compatibility);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 保存兼容性
     */
    @PostMapping("/compatibilities")
    public ResponseEntity<?> saveCompatibility(@RequestBody silicone_sandblast_uvCompatibility compatibility) {
        try {
            silicone_sandblast_uvCompatibility saved = service.saveCompatibility(compatibility);
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
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody silicone_sandblast_uvCompatibility compatibility) {
        try {
            compatibility.setId(id);
            silicone_sandblast_uvCompatibility updated = service.saveCompatibility(compatibility);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
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
     * 批量保存兼容性
     */
    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<silicone_sandblast_uvCompatibility> compatibilities) {
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
}
