package com.it.yts_project.controller;

// 源文件: 硅胶桔纹UV(Orange Peel UV)标准书-20230718.xlsx

import com.it.yts_project.model.SiliconeOrangePeelUvProduct;
import com.it.yts_project.model.SiliconeOrangePeelUvCompatibility;
import com.it.yts_project.service.SiliconeOrangePeelUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 硅胶桔纹UV(Orange Peel UV) Controller
 */
@RestController
@RequestMapping("/api/silicone_orange_peel_uv")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeOrangePeelUvController {

    @Autowired
    private SiliconeOrangePeelUvService service;

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    @GetMapping("/products")
    public ResponseEntity<List<SiliconeOrangePeelUvProduct>> getAllProducts() {
        List<SiliconeOrangePeelUvProduct> products = service.getActiveProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 获取所有产品（含未激活）
     */
    @GetMapping("/products/all")
    public ResponseEntity<List<SiliconeOrangePeelUvProduct>> getAllProductsIncludeInactive() {
        List<SiliconeOrangePeelUvProduct> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeOrangePeelUvProduct> getProductById(@PathVariable Integer id) {
        SiliconeOrangePeelUvProduct product = service.getProductById(id);
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
    public ResponseEntity<List<SiliconeOrangePeelUvProduct>> searchProducts(@RequestParam String keyword) {
        List<SiliconeOrangePeelUvProduct> products = service.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     * 新增产品
     */
    @PostMapping("/products")
    public ResponseEntity<SiliconeOrangePeelUvProduct> createProduct(@RequestBody SiliconeOrangePeelUvProduct product) {
        SiliconeOrangePeelUvProduct saved = service.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeOrangePeelUvProduct> updateProduct(@PathVariable Integer id, @RequestBody SiliconeOrangePeelUvProduct product) {
        product.setId(id);
        SiliconeOrangePeelUvProduct updated = service.saveProduct(product);
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
     * 获取所有兼容性列表
     */
    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeOrangePeelUvCompatibility>> getAllCompatibilities() {
        List<SiliconeOrangePeelUvCompatibility> list = service.getAllCompatibilities();
        return ResponseEntity.ok(list);
    }

    /**
     * 根据产品ID获取兼容性列表
     */
    @GetMapping("/compatibilities/product/{productId}")
    public ResponseEntity<List<SiliconeOrangePeelUvCompatibility>> getCompatibilitiesByProductId(@PathVariable Integer productId) {
        List<SiliconeOrangePeelUvCompatibility> list = service.getCompatibilitiesByProductId(productId);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ID获取兼容性
     */
    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeOrangePeelUvCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeOrangePeelUvCompatibility compatibility = service.getCompatibilityById(id);
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
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeOrangePeelUvCompatibility compatibility) {
        try {
            SiliconeOrangePeelUvCompatibility saved = service.saveCompatibility(compatibility);
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
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody SiliconeOrangePeelUvCompatibility compatibility) {
        try {
            compatibility.setId(id);
            SiliconeOrangePeelUvCompatibility updated = service.saveCompatibility(compatibility);
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
    public ResponseEntity<Void> batchSaveCompatibilities(@RequestBody List<SiliconeOrangePeelUvCompatibility> compatibilities) {
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
     * 根据产品ID删除所有兼容性
     */
    @DeleteMapping("/compatibilities/product/{productId}")
    public ResponseEntity<Void> deleteCompatibilitiesByProductId(@PathVariable Integer productId) {
        service.deleteCompatibilitiesByProductId(productId);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取所有后加工工序名称
     */
    @GetMapping("/post-processing-steps")
    public ResponseEntity<List<String>> getAllPostProcessingSteps() {
        List<String> steps = service.getAllPostProcessingSteps();
        return ResponseEntity.ok(steps);
    }
}
