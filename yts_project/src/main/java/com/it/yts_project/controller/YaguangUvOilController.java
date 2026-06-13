package com.it.yts_project.controller;

// 源文件: 哑光UV油标准书-20250730 (10)(1).xlsx

import com.it.yts_project.model.YaguangUvOilProduct;
import com.it.yts_project.model.YaguangUvOilCompatibility;
import com.it.yts_project.service.YaguangUvOilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 哑光UV油 Controller
 */
@RestController
@RequestMapping("/api/yaguang_uv_oil")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class YaguangUvOilController {

    @Autowired
    private YaguangUvOilService yaguangUvOilService;

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    @GetMapping("/products")
    public ResponseEntity<List<YaguangUvOilProduct>> getAllProducts() {
        List<YaguangUvOilProduct> products = yaguangUvOilService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<YaguangUvOilProduct> getProductById(@PathVariable Integer id) {
        YaguangUvOilProduct product = yaguangUvOilService.getProductById(id);
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
    public ResponseEntity<List<YaguangUvOilProduct>> searchProducts(@RequestParam String keyword) {
        List<YaguangUvOilProduct> products = yaguangUvOilService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     * 新增产品
     */
    @PostMapping("/products")
    public ResponseEntity<YaguangUvOilProduct> createProduct(@RequestBody YaguangUvOilProduct product) {
        YaguangUvOilProduct saved = yaguangUvOilService.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<YaguangUvOilProduct> updateProduct(@PathVariable Integer id, @RequestBody YaguangUvOilProduct product) {
        product.setId(id);
        YaguangUvOilProduct updated = yaguangUvOilService.saveProduct(product);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        yaguangUvOilService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID获取兼容性列表
     */
    @GetMapping("/compatibilities")
    public ResponseEntity<List<YaguangUvOilCompatibility>> getCompatibilitiesByProductId(
            @RequestParam Integer productId) {
        List<YaguangUvOilCompatibility> list = yaguangUvOilService.getCompatibilitiesByProductId(productId);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ID获取兼容性
     */
    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<YaguangUvOilCompatibility> getCompatibilityById(@PathVariable Integer id) {
        YaguangUvOilCompatibility compatibility = yaguangUvOilService.getCompatibilityById(id);
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
    public ResponseEntity<?> createCompatibility(@RequestBody YaguangUvOilCompatibility compatibility) {
        try {
            YaguangUvOilCompatibility saved = yaguangUvOilService.saveCompatibility(compatibility);
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
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody YaguangUvOilCompatibility compatibility) {
        try {
            compatibility.setId(id);
            YaguangUvOilCompatibility updated = yaguangUvOilService.saveCompatibility(compatibility);
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
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<YaguangUvOilCompatibility> compatibilities) {
        try {
            yaguangUvOilService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "批量保存成功");
            return ResponseEntity.ok(response);
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
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        yaguangUvOilService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量删除兼容性
     */
    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> response = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            response.put("success", false);
            response.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(response);
        }
        for (Integer id : ids) {
            yaguangUvOilService.deleteCompatibility(id);
        }
        response.put("success", true);
        response.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(response);
    }
}
