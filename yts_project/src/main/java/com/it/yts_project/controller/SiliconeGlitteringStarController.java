package com.it.yts_project.controller;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeGlitteringStarProductDTO;
import com.it.yts_project.dto.SiliconeGlitteringStarQueryParams;
import com.it.yts_project.model.SiliconeGlitteringStarProduct;
import com.it.yts_project.model.SiliconeGlitteringStarCompatibility;
import com.it.yts_project.service.SiliconeGlitteringStarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/silicone-glittering-star", "/api/silicone_glittering_star"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeGlitteringStarController {

    @Autowired
    private SiliconeGlitteringStarService siliconeGlitteringStarService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<SiliconeGlitteringStarProduct>> getAllProducts() {
        return ResponseEntity.ok(siliconeGlitteringStarService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeGlitteringStarProduct> getProductById(@PathVariable Integer id) {
        SiliconeGlitteringStarProduct product = siliconeGlitteringStarService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<SiliconeGlitteringStarProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(siliconeGlitteringStarService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<SiliconeGlitteringStarProduct> createProduct(@RequestBody SiliconeGlitteringStarProduct product) {
        return ResponseEntity.ok(siliconeGlitteringStarService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeGlitteringStarProduct> updateProduct(@PathVariable Integer id, @RequestBody SiliconeGlitteringStarProduct product) {
        product.setId(id);
        return ResponseEntity.ok(siliconeGlitteringStarService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        siliconeGlitteringStarService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeGlitteringStarCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(siliconeGlitteringStarService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeGlitteringStarCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeGlitteringStarCompatibility compatibility = siliconeGlitteringStarService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeGlitteringStarCompatibility compatibility) {
        try {
            return ResponseEntity.ok(siliconeGlitteringStarService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody SiliconeGlitteringStarCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(siliconeGlitteringStarService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<SiliconeGlitteringStarCompatibility> compatibilities) {
        try {
            siliconeGlitteringStarService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        siliconeGlitteringStarService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { siliconeGlitteringStarService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<SiliconeGlitteringStarProduct>> match(@RequestBody SiliconeGlitteringStarQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(siliconeGlitteringStarService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(siliconeGlitteringStarService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<SiliconeGlitteringStarProductDTO> getProductDetail(@PathVariable Integer id) {
        SiliconeGlitteringStarProductDTO dto = siliconeGlitteringStarService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
