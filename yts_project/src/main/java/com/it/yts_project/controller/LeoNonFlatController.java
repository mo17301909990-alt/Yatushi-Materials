package com.it.yts_project.controller;

import com.it.yts_project.dto.LeoNonFlatProductDTO;
import com.it.yts_project.dto.LeoNonFlatMatchParams;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.LeoNonFlatProduct;
import com.it.yts_project.model.LeoNonFlatCompatibility;
import com.it.yts_project.service.LeoNonFlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/leo-non-flat", "/api/leo_non_flat"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class LeoNonFlatController {

    @Autowired
    private LeoNonFlatService leoNonFlatService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<LeoNonFlatProduct>> getAllProducts() {
        return ResponseEntity.ok(leoNonFlatService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<LeoNonFlatProduct> getProductById(@PathVariable Integer id) {
        LeoNonFlatProduct product = leoNonFlatService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<LeoNonFlatProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(leoNonFlatService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<LeoNonFlatProduct> createProduct(@RequestBody LeoNonFlatProduct product) {
        return ResponseEntity.ok(leoNonFlatService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<LeoNonFlatProduct> updateProduct(@PathVariable Integer id, @RequestBody LeoNonFlatProduct product) {
        product.setId(id);
        return ResponseEntity.ok(leoNonFlatService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        leoNonFlatService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<LeoNonFlatCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(leoNonFlatService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<LeoNonFlatCompatibility> getCompatibilityById(@PathVariable Integer id) {
        LeoNonFlatCompatibility compatibility = leoNonFlatService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody LeoNonFlatCompatibility compatibility) {
        try {
            return ResponseEntity.ok(leoNonFlatService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody LeoNonFlatCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(leoNonFlatService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<LeoNonFlatCompatibility> compatibilities) {
        try {
            leoNonFlatService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        leoNonFlatService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { leoNonFlatService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<LeoNonFlatProduct>> match(@RequestBody LeoNonFlatMatchParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(leoNonFlatService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(leoNonFlatService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<LeoNonFlatProductDTO> getProductDetail(@PathVariable Integer id) {
        LeoNonFlatProductDTO dto = leoNonFlatService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
