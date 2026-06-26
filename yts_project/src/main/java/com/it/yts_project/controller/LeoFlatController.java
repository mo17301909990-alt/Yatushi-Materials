package com.it.yts_project.controller;

import com.it.yts_project.dto.LeoFlatProductDTO;
import com.it.yts_project.dto.LeoFlatMatchParams;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.LeoFlatProduct;
import com.it.yts_project.model.LeoFlatCompatibility;
import com.it.yts_project.service.LeoFlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/leo-flat", "/api/leo_flat"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class LeoFlatController {

    @Autowired
    private LeoFlatService leoFlatService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<LeoFlatProduct>> getAllProducts() {
        return ResponseEntity.ok(leoFlatService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<LeoFlatProduct> getProductById(@PathVariable Integer id) {
        LeoFlatProduct product = leoFlatService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<LeoFlatProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(leoFlatService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<LeoFlatProduct> createProduct(@RequestBody LeoFlatProduct product) {
        return ResponseEntity.ok(leoFlatService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<LeoFlatProduct> updateProduct(@PathVariable Integer id, @RequestBody LeoFlatProduct product) {
        product.setId(id);
        return ResponseEntity.ok(leoFlatService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        leoFlatService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<LeoFlatCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(leoFlatService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<LeoFlatCompatibility> getCompatibilityById(@PathVariable Integer id) {
        LeoFlatCompatibility compatibility = leoFlatService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody LeoFlatCompatibility compatibility) {
        try {
            return ResponseEntity.ok(leoFlatService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody LeoFlatCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(leoFlatService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<LeoFlatCompatibility> compatibilities) {
        try {
            leoFlatService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        leoFlatService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { leoFlatService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<LeoFlatProduct>> match(@RequestBody LeoFlatMatchParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(leoFlatService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(leoFlatService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<LeoFlatProductDTO> getProductDetail(@PathVariable Integer id) {
        LeoFlatProductDTO dto = leoFlatService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
