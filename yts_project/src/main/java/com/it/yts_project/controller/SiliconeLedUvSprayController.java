package com.it.yts_project.controller;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeLedUvSprayProductDTO;
import com.it.yts_project.dto.SiliconeLedUvSprayQueryParams;
import com.it.yts_project.model.SiliconeLedUvSprayProduct;
import com.it.yts_project.model.SiliconeLedUvSprayCompatibility;
import com.it.yts_project.service.SiliconeLedUvSprayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/silicone-led-uv-spray", "/api/silicone_led_uv_spray"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeLedUvSprayController {

    @Autowired
    private SiliconeLedUvSprayService siliconeLedUvSprayService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<SiliconeLedUvSprayProduct>> getAllProducts() {
        return ResponseEntity.ok(siliconeLedUvSprayService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeLedUvSprayProduct> getProductById(@PathVariable Integer id) {
        SiliconeLedUvSprayProduct product = siliconeLedUvSprayService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<SiliconeLedUvSprayProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(siliconeLedUvSprayService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<SiliconeLedUvSprayProduct> createProduct(@RequestBody SiliconeLedUvSprayProduct product) {
        return ResponseEntity.ok(siliconeLedUvSprayService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeLedUvSprayProduct> updateProduct(@PathVariable Integer id, @RequestBody SiliconeLedUvSprayProduct product) {
        product.setId(id);
        return ResponseEntity.ok(siliconeLedUvSprayService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        siliconeLedUvSprayService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeLedUvSprayCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(siliconeLedUvSprayService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeLedUvSprayCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeLedUvSprayCompatibility compatibility = siliconeLedUvSprayService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeLedUvSprayCompatibility compatibility) {
        try {
            return ResponseEntity.ok(siliconeLedUvSprayService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody SiliconeLedUvSprayCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(siliconeLedUvSprayService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<SiliconeLedUvSprayCompatibility> compatibilities) {
        try {
            siliconeLedUvSprayService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        siliconeLedUvSprayService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { siliconeLedUvSprayService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<SiliconeLedUvSprayProduct>> match(@RequestBody SiliconeLedUvSprayQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(siliconeLedUvSprayService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(siliconeLedUvSprayService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<SiliconeLedUvSprayProductDTO> getProductDetail(@PathVariable Integer id) {
        SiliconeLedUvSprayProductDTO dto = siliconeLedUvSprayService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
