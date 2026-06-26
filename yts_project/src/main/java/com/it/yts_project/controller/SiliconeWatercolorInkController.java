package com.it.yts_project.controller;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeWatercolorInkProductDTO;
import com.it.yts_project.dto.SiliconeWatercolorInkQueryParams;
import com.it.yts_project.model.SiliconeWatercolorInkProduct;
import com.it.yts_project.model.SiliconeWatercolorInkCompatibility;
import com.it.yts_project.service.SiliconeWatercolorInkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/silicone-watercolor-ink", "/api/silicone_watercolor_ink"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeWatercolorInkController {

    @Autowired
    private SiliconeWatercolorInkService siliconeWatercolorInkService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<SiliconeWatercolorInkProduct>> getAllProducts() {
        return ResponseEntity.ok(siliconeWatercolorInkService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeWatercolorInkProduct> getProductById(@PathVariable Integer id) {
        SiliconeWatercolorInkProduct product = siliconeWatercolorInkService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<SiliconeWatercolorInkProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(siliconeWatercolorInkService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<SiliconeWatercolorInkProduct> createProduct(@RequestBody SiliconeWatercolorInkProduct product) {
        return ResponseEntity.ok(siliconeWatercolorInkService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeWatercolorInkProduct> updateProduct(@PathVariable Integer id, @RequestBody SiliconeWatercolorInkProduct product) {
        product.setId(id);
        return ResponseEntity.ok(siliconeWatercolorInkService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        siliconeWatercolorInkService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeWatercolorInkCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(siliconeWatercolorInkService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeWatercolorInkCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeWatercolorInkCompatibility compatibility = siliconeWatercolorInkService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeWatercolorInkCompatibility compatibility) {
        try {
            return ResponseEntity.ok(siliconeWatercolorInkService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody SiliconeWatercolorInkCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(siliconeWatercolorInkService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<SiliconeWatercolorInkCompatibility> compatibilities) {
        try {
            siliconeWatercolorInkService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        siliconeWatercolorInkService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { siliconeWatercolorInkService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<SiliconeWatercolorInkProduct>> match(@RequestBody SiliconeWatercolorInkQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(siliconeWatercolorInkService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(siliconeWatercolorInkService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<SiliconeWatercolorInkProductDTO> getProductDetail(@PathVariable Integer id) {
        SiliconeWatercolorInkProductDTO dto = siliconeWatercolorInkService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
