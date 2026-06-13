package com.it.yts_project.controller;

// 源文件: 硅胶发光油墨(Glow-In-The-Dark)标准书-20250530 (2).xlsx

import com.it.yts_project.model.SiliconeGlowInkCompatibility;
import com.it.yts_project.model.SiliconeGlowInkProduct;
import com.it.yts_project.service.SiliconeGlowInkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 硅胶发光油墨Controller
 */
@RestController
@RequestMapping("/api/silicone_glow_ink")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeGlowInkController {

    @Autowired
    private SiliconeGlowInkService siliconeGlowInkService;

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    @GetMapping("/products")
    public ResponseEntity<List<SiliconeGlowInkProduct>> getAllProducts() {
        List<SiliconeGlowInkProduct> products = siliconeGlowInkService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeGlowInkProduct> getProductById(@PathVariable Integer id) {
        SiliconeGlowInkProduct product = siliconeGlowInkService.getProductById(id);
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
    public ResponseEntity<List<SiliconeGlowInkProduct>> searchProducts(@RequestParam String keyword) {
        List<SiliconeGlowInkProduct> products = siliconeGlowInkService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     * 新增产品
     */
    @PostMapping("/products")
    public ResponseEntity<SiliconeGlowInkProduct> createProduct(@RequestBody SiliconeGlowInkProduct product) {
        SiliconeGlowInkProduct savedProduct = siliconeGlowInkService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeGlowInkProduct> updateProduct(@PathVariable Integer id, @RequestBody SiliconeGlowInkProduct product) {
        product.setId(id);
        SiliconeGlowInkProduct updatedProduct = siliconeGlowInkService.saveProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        siliconeGlowInkService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    /**
     * 获取所有兼容性列表
     */
    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeGlowInkCompatibility>> getAllCompatibilities() {
        List<SiliconeGlowInkCompatibility> list = siliconeGlowInkService.getAllCompatibilities();
        return ResponseEntity.ok(list);
    }

    /**
     * 根据产品ID获取兼容性列表
     */
    @GetMapping("/compatibilities/by-product/{productId}")
    public ResponseEntity<List<SiliconeGlowInkCompatibility>> getCompatibilitiesByProductId(@PathVariable Integer productId) {
        List<SiliconeGlowInkCompatibility> list = siliconeGlowInkService.getCompatibilitiesByProductId(productId);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ID获取兼容性
     */
    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeGlowInkCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeGlowInkCompatibility compatibility = siliconeGlowInkService.getCompatibilityById(id);
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
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeGlowInkCompatibility compatibility) {
        try {
            SiliconeGlowInkCompatibility savedCompatibility = siliconeGlowInkService.saveCompatibility(compatibility);
            return ResponseEntity.ok(savedCompatibility);
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
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody SiliconeGlowInkCompatibility compatibility) {
        try {
            compatibility.setId(id);
            SiliconeGlowInkCompatibility updatedCompatibility = siliconeGlowInkService.saveCompatibility(compatibility);
            return ResponseEntity.ok(updatedCompatibility);
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
    public ResponseEntity<Void> batchSaveCompatibilities(@RequestBody List<SiliconeGlowInkCompatibility> compatibilities) {
        siliconeGlowInkService.batchSaveCompatibilities(compatibilities);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除兼容性
     */
    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        siliconeGlowInkService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取所有后加工工序名称
     */
    @GetMapping("/options/post-processing-steps")
    public ResponseEntity<List<String>> getAllPostProcessingSteps() {
        List<String> steps = siliconeGlowInkService.getAllPostProcessingSteps();
        return ResponseEntity.ok(steps);
    }
}
