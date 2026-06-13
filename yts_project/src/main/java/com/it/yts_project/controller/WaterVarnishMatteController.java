package com.it.yts_project.controller;

// 源文件: 哑光水油标准书-20231117 (6).xlsx

import com.it.yts_project.model.WaterVarnishMatteCompatibility;
import com.it.yts_project.model.WaterVarnishMatteProduct;
import com.it.yts_project.service.WaterVarnishMatteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UV油_哑光水油 Controller
 */
@RestController
@RequestMapping("/api/water_varnish_matte")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class WaterVarnishMatteController {

    @Autowired
    private WaterVarnishMatteService waterVarnishMatteService;

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    @GetMapping("/products")
    public ResponseEntity<List<WaterVarnishMatteProduct>> getAllProducts() {
        List<WaterVarnishMatteProduct> products = waterVarnishMatteService.getActiveProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 获取所有产品（含未激活）
     */
    @GetMapping("/products/all")
    public ResponseEntity<List<WaterVarnishMatteProduct>> getAllProductsIncludingInactive() {
        List<WaterVarnishMatteProduct> products = waterVarnishMatteService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<WaterVarnishMatteProduct> getProductById(@PathVariable Integer id) {
        WaterVarnishMatteProduct product = waterVarnishMatteService.getProductById(id);
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
    public ResponseEntity<List<WaterVarnishMatteProduct>> searchProducts(@RequestParam String keyword) {
        List<WaterVarnishMatteProduct> products = waterVarnishMatteService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     * 保存产品
     */
    @PostMapping("/products")
    public ResponseEntity<WaterVarnishMatteProduct> saveProduct(@RequestBody WaterVarnishMatteProduct product) {
        WaterVarnishMatteProduct savedProduct = waterVarnishMatteService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<WaterVarnishMatteProduct> updateProduct(@PathVariable Integer id, @RequestBody WaterVarnishMatteProduct product) {
        product.setId(id);
        WaterVarnishMatteProduct updatedProduct = waterVarnishMatteService.saveProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        waterVarnishMatteService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID获取兼容性列表
     */
    @GetMapping("/compatibilities")
    public ResponseEntity<List<WaterVarnishMatteCompatibility>> getCompatibilitiesByProductId(
            @RequestParam(required = false) Integer productId) {
        List<WaterVarnishMatteCompatibility> list;
        if (productId != null) {
            list = waterVarnishMatteService.getCompatibilitiesByProductId(productId);
        } else {
            list = waterVarnishMatteService.getAllCompatibilities();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ID获取兼容性
     */
    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<WaterVarnishMatteCompatibility> getCompatibilityById(@PathVariable Integer id) {
        WaterVarnishMatteCompatibility compatibility = waterVarnishMatteService.getCompatibilityById(id);
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
    public ResponseEntity<?> saveCompatibility(@RequestBody WaterVarnishMatteCompatibility compatibility) {
        try {
            WaterVarnishMatteCompatibility saved = waterVarnishMatteService.saveCompatibility(compatibility);
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
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody WaterVarnishMatteCompatibility compatibility) {
        try {
            compatibility.setId(id);
            WaterVarnishMatteCompatibility updated = waterVarnishMatteService.saveCompatibility(compatibility);
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
    public ResponseEntity<Void> batchSaveCompatibility(@RequestBody List<WaterVarnishMatteCompatibility> compatibilities) {
        waterVarnishMatteService.batchSaveCompatibility(compatibilities);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除兼容性
     */
    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        waterVarnishMatteService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据产品ID删除兼容性
     */
    @DeleteMapping("/compatibilities/product/{productId}")
    public ResponseEntity<Void> deleteCompatibilityByProductId(@PathVariable Integer productId) {
        waterVarnishMatteService.deleteCompatibilityByProductId(productId);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取所有后加工工序步骤名称
     */
    @GetMapping("/post-processing-steps")
    public ResponseEntity<List<String>> getAllPostProcessingSteps() {
        List<String> steps = waterVarnishMatteService.getAllPostProcessingSteps();
        return ResponseEntity.ok(steps);
    }
}
