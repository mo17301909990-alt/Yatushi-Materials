package com.it.yts_project.controller;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeSandblastUvProductDTO;
import com.it.yts_project.dto.SiliconeSandblastUvQueryParams;
import com.it.yts_project.model.SiliconeSandblastUvProduct;
import com.it.yts_project.model.SiliconeSandblastUvCompatibility;
import com.it.yts_project.service.SiliconeSandblastUvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/silicone-sandblast-uv", "/api/silicone_sandblast_uv"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeSandblastUvController {

    @Autowired
    private SiliconeSandblastUvService siliconeSandblastUvService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<SiliconeSandblastUvProduct>> getAllProducts() {
        return ResponseEntity.ok(siliconeSandblastUvService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeSandblastUvProduct> getProductById(@PathVariable Integer id) {
        SiliconeSandblastUvProduct product = siliconeSandblastUvService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<SiliconeSandblastUvProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(siliconeSandblastUvService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<SiliconeSandblastUvProduct> createProduct(@RequestBody SiliconeSandblastUvProduct product) {
        return ResponseEntity.ok(siliconeSandblastUvService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeSandblastUvProduct> updateProduct(@PathVariable Integer id, @RequestBody SiliconeSandblastUvProduct product) {
        product.setId(id);
        return ResponseEntity.ok(siliconeSandblastUvService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        siliconeSandblastUvService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeSandblastUvCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(siliconeSandblastUvService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeSandblastUvCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeSandblastUvCompatibility compatibility = siliconeSandblastUvService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeSandblastUvCompatibility compatibility) {
        try {
            return ResponseEntity.ok(siliconeSandblastUvService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody SiliconeSandblastUvCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(siliconeSandblastUvService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<SiliconeSandblastUvCompatibility> compatibilities) {
        try {
            siliconeSandblastUvService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        siliconeSandblastUvService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { siliconeSandblastUvService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<SiliconeSandblastUvProduct>> match(@RequestBody SiliconeSandblastUvQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(siliconeSandblastUvService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(siliconeSandblastUvService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<SiliconeSandblastUvProductDTO> getProductDetail(@PathVariable Integer id) {
        SiliconeSandblastUvProductDTO dto = siliconeSandblastUvService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
