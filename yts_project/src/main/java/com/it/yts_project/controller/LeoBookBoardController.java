package com.it.yts_project.controller;

import com.it.yts_project.dto.LeoBookBoardProductDTO;
import com.it.yts_project.dto.LeoBookBoardMatchParams;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.LeoBookBoardProduct;
import com.it.yts_project.model.LeoBookBoardCompatibility;
import com.it.yts_project.service.LeoBookBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/leo-book-board", "/api/leo_book_board"})
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class LeoBookBoardController {

    @Autowired
    private LeoBookBoardService leoBookBoardService;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<LeoBookBoardProduct>> getAllProducts() {
        return ResponseEntity.ok(leoBookBoardService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<LeoBookBoardProduct> getProductById(@PathVariable Integer id) {
        LeoBookBoardProduct product = leoBookBoardService.getProductById(id);
        if (product != null) return ResponseEntity.ok(product);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<LeoBookBoardProduct>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(leoBookBoardService.searchProducts(keyword));
    }

    @PostMapping("/products")
    public ResponseEntity<LeoBookBoardProduct> createProduct(@RequestBody LeoBookBoardProduct product) {
        return ResponseEntity.ok(leoBookBoardService.saveProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<LeoBookBoardProduct> updateProduct(@PathVariable Integer id, @RequestBody LeoBookBoardProduct product) {
        product.setId(id);
        return ResponseEntity.ok(leoBookBoardService.saveProduct(product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        leoBookBoardService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<LeoBookBoardCompatibility>> getCompatibilitiesByProductId(@RequestParam Integer productId) {
        return ResponseEntity.ok(leoBookBoardService.getCompatibilitiesByProductId(productId));
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<LeoBookBoardCompatibility> getCompatibilityById(@PathVariable Integer id) {
        LeoBookBoardCompatibility compatibility = leoBookBoardService.getCompatibilityById(id);
        if (compatibility != null) return ResponseEntity.ok(compatibility);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody LeoBookBoardCompatibility compatibility) {
        try {
            return ResponseEntity.ok(leoBookBoardService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody LeoBookBoardCompatibility compatibility) {
        try {
            compatibility.setId(id);
            return ResponseEntity.ok(leoBookBoardService.saveCompatibility(compatibility));
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<?> batchSaveCompatibilities(@RequestBody List<LeoBookBoardCompatibility> compatibilities) {
        try {
            leoBookBoardService.batchSaveCompatibilities(compatibilities);
            Map<String, Object> r = new HashMap<>(); r.put("success", true); r.put("message", "批量保存成功");
            return ResponseEntity.ok(r);
        } catch (IllegalArgumentException e) {
            Map<String, Object> r = new HashMap<>(); r.put("success", false); r.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(r);
        }
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        leoBookBoardService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        Map<String, Object> r = new HashMap<>();
        if (ids == null || ids.isEmpty()) {
            r.put("success", false); r.put("message", "请选择要删除的记录");
            return ResponseEntity.badRequest().body(r);
        }
        for (Integer id : ids) { leoBookBoardService.deleteCompatibility(id); }
        r.put("success", true); r.put("message", String.format("成功删除 %d 条记录", ids.size()));
        return ResponseEntity.ok(r);
    }

    // ========== 匹配查询 ==========

    @PostMapping("/match")
    public ResponseEntity<PagedResult<LeoBookBoardProduct>> match(@RequestBody LeoBookBoardMatchParams params) {
        if (params.getPage() == null || params.getPage() < 1) params.setPage(1);
        if (params.getSize() == null || params.getSize() < 1) params.setSize(15);
        return ResponseEntity.ok(leoBookBoardService.searchProducts(
            params.getKeyword(), params.getStepName(), params.getPage(), params.getSize()));
    }

    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        return ResponseEntity.ok(leoBookBoardService.getDistinctSteps());
    }

    @GetMapping("/products/{id}/detail")
    public ResponseEntity<LeoBookBoardProductDTO> getProductDetail(@PathVariable Integer id) {
        LeoBookBoardProductDTO dto = leoBookBoardService.getProductDetail(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.notFound().build();
    }
}
