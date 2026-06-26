package com.it.yts_project.controller;

import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.dto.SiliconeWaterBaseTransparentProductDTO;
import com.it.yts_project.dto.SiliconeWaterBaseTransparentQueryParams;
import com.it.yts_project.model.SiliconeWaterBaseTransparentProduct;
import com.it.yts_project.model.SiliconeWaterBaseTransparentCompatibility;
import com.it.yts_project.service.SiliconeWaterBaseTransparentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/silicone-water-base-transparent", "/api/silicone_water_base_transparent"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SiliconeWaterBaseTransparentController {

    @Autowired
    private SiliconeWaterBaseTransparentService siliconeWaterBaseTransparentService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<SiliconeWaterBaseTransparentProduct>> getAllProducts() {
        return ResponseEntity.ok(siliconeWaterBaseTransparentService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<SiliconeWaterBaseTransparentProduct> getProductById(@PathVariable Integer id) {
        SiliconeWaterBaseTransparentProduct product = siliconeWaterBaseTransparentService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<SiliconeWaterBaseTransparentProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(siliconeWaterBaseTransparentService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<SiliconeWaterBaseTransparentProduct> createProduct(@RequestBody SiliconeWaterBaseTransparentProduct product) {
        return ResponseEntity.ok(siliconeWaterBaseTransparentService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<SiliconeWaterBaseTransparentProduct> updateProduct(@PathVariable Integer id, @RequestBody SiliconeWaterBaseTransparentProduct product) {
        product.setId(id);
        return ResponseEntity.ok(siliconeWaterBaseTransparentService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        siliconeWaterBaseTransparentService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<SiliconeWaterBaseTransparentCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(siliconeWaterBaseTransparentService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<SiliconeWaterBaseTransparentCompatibility> getCompatibilityById(@PathVariable Integer id) {
        SiliconeWaterBaseTransparentCompatibility compatibility = siliconeWaterBaseTransparentService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody SiliconeWaterBaseTransparentCompatibility compatibility) {
        try {
            return ResponseEntity.ok(siliconeWaterBaseTransparentService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody SiliconeWaterBaseTransparentCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(siliconeWaterBaseTransparentService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<SiliconeWaterBaseTransparentCompatibility> compatibilities) {
        try {
            siliconeWaterBaseTransparentService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        siliconeWaterBaseTransparentService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { siliconeWaterBaseTransparentService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<SiliconeWaterBaseTransparentProduct>> match(@RequestBody SiliconeWaterBaseTransparentQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(siliconeWaterBaseTransparentService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(siliconeWaterBaseTransparentService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<SiliconeWaterBaseTransparentProductDTO> getProductDetail(@PathVariable Integer id) {
        SiliconeWaterBaseTransparentProductDTO dto = siliconeWaterBaseTransparentService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
