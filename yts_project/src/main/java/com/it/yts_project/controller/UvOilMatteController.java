package com.it.yts_project.controller;

// 源文件: 哑光UV油标准书-20250730 (10)(1).xlsx

import com.it.yts_project.model.UvOilMatteProduct;
import com.it.yts_project.model.UvOilMatteCompatibility;
import com.it.yts_project.service.UvOilMatteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UV油_哑光UV油 Controller
 */
@RestController
@RequestMapping("/api/uv_oil_matte")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class UvOilMatteController {

    @Autowired
    private UvOilMatteService uvOilMatteService;

    // ========== 产品管理 ==========

    /**
     * 获取所有产品
     */
    @GetMapping("/products")
    public ResponseEntity<List<UvOilMatteProduct>> getAllProducts() {
        List<UvOilMatteProduct> products = uvOilMatteService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * 根据ID获取产品
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<UvOilMatteProduct> getProductById(@PathVariable Integer id) {
        UvOilMatteProduct product = uvOilMatteService.getProductById(id);
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
    public ResponseEntity<List<UvOilMatteProduct>> searchProducts(@RequestParam String keyword) {
        List<UvOilMatteProduct> products = uvOilMatteService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    /**
     * 新增产品
     */
    @PostMapping("/products")
    public ResponseEntity<UvOilMatteProduct> createProduct(@RequestBody UvOilMatteProduct product) {
        UvOilMatteProduct saved = uvOilMatteService.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<UvOilMatteProduct> updateProduct(@PathVariable Integer id, @RequestBody UvOilMatteProduct product) {
        product.setId(id);
        UvOilMatteProduct updated = uvOilMatteService.saveProduct(product);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除产品
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        uvOilMatteService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    /**
     * 根据产品ID获取兼容性列表
     */
    @GetMapping("/compatibilities")
    public ResponseEntity<List<UvOilMatteCompatibility>> getCompatibilitiesByProductId(
            @RequestParam Integer productId) {
        List<UvOilMatteCompatibility> list = uvOilMatteService.getCompatibilitiesByProductId(productId);
        return ResponseEntity.ok(list);
    }

    /**
     * 根据ID获取兼容性
     */
    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<UvOilMatteCompatibility> getCompatibilityById(@PathVariable Integer id) {
        UvOilMatteCompatibility compatibility = uvOilMatteService.getCompatibilityById(id);
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
    public ResponseEntity<?> createCompatibility(@RequestBody UvOilMatteCompatibility compatibility) {
        try {
            UvOilMatteCompatibility saved = uvOilMatteService.saveCompatibility(compatibility);
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
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody UvOilMatteCompatibility compatibility) {
        try {
            compatibility.setId(id);
            UvOilMatteCompatibility updated = uvOilMatteService.saveCompatibility(compatibility);
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
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<UvOilMatteCompatibility> compatibilities) {
        try {
            uvOilMatteService.batchSaveCompatibilities(compatibilities);
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
        uvOilMatteService.deleteCompatibility(id);
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
            uvOilMatteService.deleteCompatibility(id);
        }
        response.put("success", true);
        response.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(response);
    }
}
