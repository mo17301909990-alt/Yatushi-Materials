package com.it.yts_project.controller;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeOrangePeelUvProductDTO;
import com.it.yts_project.dto.SiliconeOrangePeelUvQueryParams;
import com.it.yts_project.model.SiliconeOrangePeelUvProduct;
import com.it.yts_project.model.SiliconeOrangePeelUvCompatibility;
import com.it.yts_project.service.SiliconeOrangePeelUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/silicone-orange-peel-uv", "/api/silicone_orange_peel_uv"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeOrangePeelUvController {

    @Autowired
    private SiliconeOrangePeelUvService siliconeOrangePeelUvService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<SiliconeOrangePeelUvProduct>> getAllProducts() {
        return ResponseEntity.ok(siliconeOrangePeelUvService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeOrangePeelUvProduct> getProductById(@PathVariable Integer id) {
        SiliconeOrangePeelUvProduct product = siliconeOrangePeelUvService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<SiliconeOrangePeelUvProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(siliconeOrangePeelUvService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<SiliconeOrangePeelUvProduct> createProduct(@RequestBody SiliconeOrangePeelUvProduct product) {
        return ResponseEntity.ok(siliconeOrangePeelUvService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeOrangePeelUvProduct> updateProduct(@PathVariable Integer id, @RequestBody SiliconeOrangePeelUvProduct product) {
        product.setId(id);
        return ResponseEntity.ok(siliconeOrangePeelUvService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        siliconeOrangePeelUvService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeOrangePeelUvCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(siliconeOrangePeelUvService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeOrangePeelUvCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeOrangePeelUvCompatibility compatibility = siliconeOrangePeelUvService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeOrangePeelUvCompatibility compatibility) {
        try {
            return ResponseEntity.ok(siliconeOrangePeelUvService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody SiliconeOrangePeelUvCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(siliconeOrangePeelUvService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<SiliconeOrangePeelUvCompatibility> compatibilities) {
        try {
            siliconeOrangePeelUvService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        siliconeOrangePeelUvService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { siliconeOrangePeelUvService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<SiliconeOrangePeelUvProduct>> match(@RequestBody SiliconeOrangePeelUvQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(siliconeOrangePeelUvService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(siliconeOrangePeelUvService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<SiliconeOrangePeelUvProductDTO> getProductDetail(@PathVariable Integer id) {
        SiliconeOrangePeelUvProductDTO dto = siliconeOrangePeelUvService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
