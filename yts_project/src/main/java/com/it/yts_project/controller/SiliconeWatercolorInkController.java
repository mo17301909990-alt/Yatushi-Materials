package com.it.yts_project.controller;

// 源文件: 硅胶水彩油墨(Watercolor Ink)标准书-20230620.xlsx

import com.it.yts_project.model.SiliconeWatercolorInkProduct;
import com.it.yts_project.model.SiliconeWatercolorInkCompatibility;
import com.it.yts_project.service.SiliconeWatercolorInkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 硅胶水彩油墨Controller
 */
@RestController
@RequestMapping("/api/silicone_watercolor_ink")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeWatercolorInkController {

    @Autowired
    private SiliconeWatercolorInkService service;

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    @GetMapping("/products")
    public ResponseEntity<List<SiliconeWatercolorInkProduct>> getAllProducts() {
        List<SiliconeWatercolorInkProduct> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 搜索产品
     */
    @GetMapping("/products/search")
    public ResponseEntity<List<SiliconeWatercolorInkProduct>> searchProducts(
            @RequestParam(required = false) String keyword) {
        List<SiliconeWatercolorInkProduct> products = service.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeWatercolorInkProduct> getProductById(@PathVariable Integer id) {
        SiliconeWatercolorInkProduct product = service.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 新增产品
     */
    @PostMapping("/products")
    public ResponseEntity<SiliconeWatercolorInkProduct> createProduct(@RequestBody SiliconeWatercolorInkProduct product) {
        SiliconeWatercolorInkProduct saved = service.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeWatercolorInkProduct> updateProduct(
            @PathVariable Integer id,
            @RequestBody SiliconeWatercolorInkProduct product) {
        product.setId(id);
        SiliconeWatercolorInkProduct updated = service.saveProduct(product);
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
    public ResponseEntity<List<SiliconeWatercolorInkCompatibility>> getAllCompatibilities() {
        List<SiliconeWatercolorInkCompatibility> list = service.getAllCompatibilities();
        return ResponseEntity.ok(list);
    }

    /**
     * 根据产品ID获取兼容性列表
     */
    @GetMapping("/compatibilities/by-product/{productId}")
    public ResponseEntity<List<SiliconeWatercolorInkCompatibility>> getCompatibilitiesByProductId(
            @PathVariable Integer productId) {
        List<SiliconeWatercolorInkCompatibility> list = service.getCompatibilitiesByProductId(productId);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ID获取兼容性
     */
    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeWatercolorInkCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeWatercolorInkCompatibility compatibility = service.getCompatibilityById(id);
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
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeWatercolorInkCompatibility compatibility) {
        try {
            SiliconeWatercolorInkCompatibility saved = service.saveCompatibility(compatibility);
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
            @RequestBody SiliconeWatercolorInkCompatibility compatibility) {
        try {
            compatibility.setId(id);
            SiliconeWatercolorInkCompatibility updated = service.saveCompatibility(compatibility);
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
    public ResponseEntity<Void> batchSaveCompatibility(
            @RequestBody List<SiliconeWatercolorInkCompatibility> compatibilities) {
        service.batchSaveCompatibility(compatibilities);
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
}
