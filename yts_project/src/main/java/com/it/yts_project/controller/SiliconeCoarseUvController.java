package com.it.yts_project.controller;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeCoarseUvProductDTO;
import com.it.yts_project.dto.SiliconeCoarseUvQueryParams;
import com.it.yts_project.model.SiliconeCoarseUvProduct;
import com.it.yts_project.model.SiliconeCoarseUvCompatibility;
import com.it.yts_project.service.SiliconeCoarseUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/silicone-coarse-uv", "/api/silicone_coarse_uv"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeCoarseUvController {

    @Autowired
    private SiliconeCoarseUvService siliconeCoarseUvService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<SiliconeCoarseUvProduct>> getAllProducts() {
        return ResponseEntity.ok(siliconeCoarseUvService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeCoarseUvProduct> getProductById(@PathVariable Integer id) {
        SiliconeCoarseUvProduct product = siliconeCoarseUvService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<SiliconeCoarseUvProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(siliconeCoarseUvService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<SiliconeCoarseUvProduct> createProduct(@RequestBody SiliconeCoarseUvProduct product) {
        return ResponseEntity.ok(siliconeCoarseUvService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeCoarseUvProduct> updateProduct(@PathVariable Integer id, @RequestBody SiliconeCoarseUvProduct product) {
        product.setId(id);
        return ResponseEntity.ok(siliconeCoarseUvService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        siliconeCoarseUvService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeCoarseUvCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(siliconeCoarseUvService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeCoarseUvCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeCoarseUvCompatibility compatibility = siliconeCoarseUvService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeCoarseUvCompatibility compatibility) {
        try {
            return ResponseEntity.ok(siliconeCoarseUvService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody SiliconeCoarseUvCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(siliconeCoarseUvService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<SiliconeCoarseUvCompatibility> compatibilities) {
        try {
            siliconeCoarseUvService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        siliconeCoarseUvService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { siliconeCoarseUvService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<SiliconeCoarseUvProduct>> match(@RequestBody SiliconeCoarseUvQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(siliconeCoarseUvService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(siliconeCoarseUvService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<SiliconeCoarseUvProductDTO> getProductDetail(@PathVariable Integer id) {
        SiliconeCoarseUvProductDTO dto = siliconeCoarseUvService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
