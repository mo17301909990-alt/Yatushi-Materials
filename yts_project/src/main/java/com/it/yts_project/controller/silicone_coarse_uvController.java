package com.it.yts_project.controller;

// 源文件: 硅胶粗纹UV(Coarse UV)标准书-20230907 (2).xlsx

import com.it.yts_project.model.silicone_coarse_uvCompatibility;
import com.it.yts_project.model.silicone_coarse_uvProduct;
import com.it.yts_project.service.silicone_coarse_uvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 硅胶粗纹UV Controller
 */
@RestController
@RequestMapping("/api/silicone_coarse_uv")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class silicone_coarse_uvController {

    @Autowired
    private silicone_coarse_uvService service;

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    @GetMapping("/products")
    public ResponseEntity<List<silicone_coarse_uvProduct>> getAllProducts() {
        List<silicone_coarse_uvProduct> products = service.getActiveProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<silicone_coarse_uvProduct> getProductById(@PathVariable Integer id) {
        silicone_coarse_uvProduct product = service.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 搜索产品
     */
    @GetMapping("/products/search")
    public ResponseEntity<List<silicone_coarse_uvProduct>> searchProducts(@RequestParam String keyword) {
        List<silicone_coarse_uvProduct> products = service.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     * 新增产品
     */
    @PostMapping("/products")
    public ResponseEntity<silicone_coarse_uvProduct> createProduct(@RequestBody silicone_coarse_uvProduct product) {
        silicone_coarse_uvProduct saved = service.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<silicone_coarse_uvProduct> updateProduct(@PathVariable Integer id,
                                                                   @RequestBody silicone_coarse_uvProduct product) {
        product.setId(id);
        silicone_coarse_uvProduct updated = service.saveProduct(product);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Integer id) {
        try {
            service.deleteProduct(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ========== 兼容性管理 ==========

    /**
     * 获取所有兼容性
     */
    @GetMapping("/compatibilities")
    public ResponseEntity<List<silicone_coarse_uvCompatibility>> getAllCompatibilities() {
        List<silicone_coarse_uvCompatibility> list = service.getAllCompatibilities();
        return ResponseEntity.ok(list);
    }

    /**
     * 根据产品ID获取兼容性
     */
    @GetMapping("/compatibilities/product/{productId}")
    public ResponseEntity<List<silicone_coarse_uvCompatibility>> getCompatibilitiesByProductId(
            @PathVariable Integer productId) {
        List<silicone_coarse_uvCompatibility> list = service.getCompatibilitiesByProductId(productId);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ID获取兼容性
     */
    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<silicone_coarse_uvCompatibility> getCompatibilityById(@PathVariable Integer id) {
        silicone_coarse_uvCompatibility compatibility = service.getCompatibilityById(id);
        if (compatibility != null) {
            return ResponseEntity.ok(compatibility);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 新增兼容性
     */
    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody silicone_coarse_uvCompatibility compatibility) {
        try {
            silicone_coarse_uvCompatibility saved = service.saveCompatibility(compatibility);
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
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id,
                                                 @RequestBody silicone_coarse_uvCompatibility compatibility) {
        try {
            compatibility.setId(id);
            silicone_coarse_uvCompatibility updated = service.saveCompatibility(compatibility);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 删除兼容性
     */
    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Map<String, Object>> deleteCompatibility(@PathVariable Integer id) {
        try {
            service.deleteCompatibility(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 批量保存兼容性
     */
    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<silicone_coarse_uvCompatibility> compatibilities) {
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

    // ========== 选项数据 ==========

    /**
     * 获取所有后加工工序名称
     */
    @GetMapping("/options/post-processing-steps")
    public ResponseEntity<List<String>> getAllPostProcessingSteps() {
        List<String> steps = service.getAllPostProcessingSteps();
        return ResponseEntity.ok(steps);
    }

    /**
     * 获取所有产品名称
     */
    @GetMapping("/options/product-names")
    public ResponseEntity<List<String>> getAllProductNames() {
        List<String> names = service.getAllProductNames();
        return ResponseEntity.ok(names);
    }
}
