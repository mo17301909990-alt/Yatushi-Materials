package com.it.yts_project.controller;

// 源文件: 哑光水油标准书-20231117 (6).xlsx

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.WaterVarnishMatteProductDTO;
import com.it.yts_project.dto.WaterVarnishMatteQueryParams;
import com.it.yts_project.model.WaterVarnishMatteProduct;
import com.it.yts_project.model.WaterVarnishMatteCompatibility;
import com.it.yts_project.service.WaterVarnishMatteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 水油(哑光) Controller
 */
@RestController
@RequestMapping({"/api/water-varnish-matte", "/api/water_varnish_matte"})
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
     * 新增产品
     */
    @PostMapping("/products")
    public ResponseEntity<WaterVarnishMatteProduct> createProduct(@RequestBody WaterVarnishMatteProduct product) {
        WaterVarnishMatteProduct saved = waterVarnishMatteService.saveProduct(product);
        return ResponseEntity.ok(saved);
    }

    /**
     * 更新产品
     */
    @PutMapping("/products/{id}")
    public ResponseEntity<WaterVarnishMatteProduct> updateProduct(@PathVariable Integer id, @RequestBody WaterVarnishMatteProduct product) {
        product.setId(id);
        WaterVarnishMatteProduct updated = waterVarnishMatteService.saveProduct(product);
        return ResponseEntity.ok(updated);
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
            @RequestParam Integer productId) {
        List<WaterVarnishMatteCompatibility> list = waterVarnishMatteService.getCompatibilitiesByProductId(productId);
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
     * 新增兼容性
     */
    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody WaterVarnishMatteCompatibility compatibility) {
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
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<WaterVarnishMatteCompatibility> compatibilities) {
        try {
            waterVarnishMatteService.batchSaveCompatibilities(compatibilities);
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
        waterVarnishMatteService.deleteCompatibility(id);
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
            waterVarnishMatteService.deleteCompatibility(id);
        }
        response.put("success", true);
        response.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(response);
    }

    // ========== 匹配查询 ==========

    /**
     * 匹配查询：关键词搜索 + 工序筛选 + 分页
     */
    @PostMapping("/match")
    public ResponseEntity<PagedResult<WaterVarnishMatteProduct>> match(@RequestBody WaterVarnishMatteQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) {
            params.setPage(1);
        }
        if (params.getSize() == null || params.getSize() < 1) {
            params.setSize(15);
        }
        PagedResult<WaterVarnishMatteProduct> result = waterVarnishMatteService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize());
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有后加工工序步骤名称（去重）
     */
    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        List<String> steps = waterVarnishMatteService.getDistinctSteps();
        return ResponseEntity.ok(steps);
    }

    /**
     * 获取产品详情（含兼容性列表）
     */
    @GetMapping("/products/{id}/detail")
    public ResponseEntity<WaterVarnishMatteProductDTO> getProductDetail(@PathVariable Integer id) {
        WaterVarnishMatteProductDTO dto = waterVarnishMatteService.getProductDetail(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
