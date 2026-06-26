package com.it.yts_project.controller;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeWrinkleUvProductDTO;
import com.it.yts_project.dto.SiliconeWrinkleUvQueryParams;
import com.it.yts_project.model.SiliconeWrinkleUvProduct;
import com.it.yts_project.model.SiliconeWrinkleUvCompatibility;
import com.it.yts_project.service.SiliconeWrinkleUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/silicone-wrinkle-uv", "/api/silicone_wrinkle_uv"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeWrinkleUvController {

    @Autowired
    private SiliconeWrinkleUvService siliconeWrinkleUvService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<SiliconeWrinkleUvProduct>> getAllProducts() {
        return ResponseEntity.ok(siliconeWrinkleUvService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeWrinkleUvProduct> getProductById(@PathVariable Integer id) {
        SiliconeWrinkleUvProduct product = siliconeWrinkleUvService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<SiliconeWrinkleUvProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(siliconeWrinkleUvService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<SiliconeWrinkleUvProduct> createProduct(@RequestBody SiliconeWrinkleUvProduct product) {
        return ResponseEntity.ok(siliconeWrinkleUvService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeWrinkleUvProduct> updateProduct(@PathVariable Integer id, @RequestBody SiliconeWrinkleUvProduct product) {
        product.setId(id);
        return ResponseEntity.ok(siliconeWrinkleUvService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        siliconeWrinkleUvService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeWrinkleUvCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(siliconeWrinkleUvService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeWrinkleUvCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeWrinkleUvCompatibility compatibility = siliconeWrinkleUvService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeWrinkleUvCompatibility compatibility) {
        try {
            return ResponseEntity.ok(siliconeWrinkleUvService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody SiliconeWrinkleUvCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(siliconeWrinkleUvService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<SiliconeWrinkleUvCompatibility> compatibilities) {
        try {
            siliconeWrinkleUvService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        siliconeWrinkleUvService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { siliconeWrinkleUvService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<SiliconeWrinkleUvProduct>> match(@RequestBody SiliconeWrinkleUvQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(siliconeWrinkleUvService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(siliconeWrinkleUvService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<SiliconeWrinkleUvProductDTO> getProductDetail(@PathVariable Integer id) {
        SiliconeWrinkleUvProductDTO dto = siliconeWrinkleUvService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
