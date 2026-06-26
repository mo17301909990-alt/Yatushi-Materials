package com.it.yts_project.controller;

import com.it.yts_project.dto.LaminationMaterialProductDTO;
import com.it.yts_project.dto.LaminationMaterialQueryParams;
import com.it.yts_project.dto.PagedResult;
import com.it.yts_project.model.LaminationMaterialCompatibility;
import com.it.yts_project.model.LaminationMaterialProduct;
import com.it.yts_project.service.LaminationMaterialProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 过胶材料产品Controller
 */
@RestController
@RequestMapping("/api/lamination-material")
@RequiredArgsConstructor
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class LaminationMaterialProductController {

    private final LaminationMaterialProductService service;

    // ========== 产品管理 ==========

    @GetMapping("/products")
    public ResponseEntity<List<LaminationMaterialProduct>> getAllProducts() {
        List<LaminationMaterialProduct> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<LaminationMaterialProduct> getProductById(@PathVariable Integer id) {
        LaminationMaterialProduct product = service.getProductById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<LaminationMaterialProduct>> searchProducts(@RequestParam String keyword) {
        List<LaminationMaterialProduct> products = service.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<LaminationMaterialProduct> createProduct(@RequestBody LaminationMaterialProduct product) {
        LaminationMaterialProduct savedProduct = service.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<LaminationMaterialProduct> updateProduct(@PathVariable Integer id, @RequestBody LaminationMaterialProduct product) {
        product.setId(id);
        LaminationMaterialProduct updatedProduct = service.saveProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        service.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // ========== 兼容性管理 ==========

    @GetMapping("/compatibilities")
    public ResponseEntity<List<LaminationMaterialCompatibility>> getAllCompatibilities() {
        List<LaminationMaterialCompatibility> list = service.getAllCompatibilities();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/compatibilities/by-product/{productId}")
    public ResponseEntity<List<LaminationMaterialCompatibility>> getCompatibilitiesByProductId(@PathVariable Integer productId) {
        List<LaminationMaterialCompatibility> list = service.getCompatibilitiesByProductId(productId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/compatibilities/{id}")
    public ResponseEntity<LaminationMaterialCompatibility> getCompatibilityById(@PathVariable Integer id) {
        LaminationMaterialCompatibility compatibility = service.getCompatibilityById(id);
        if (compatibility != null) {
            return ResponseEntity.ok(compatibility);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/compatibilities")
    public ResponseEntity<?> createCompatibility(@RequestBody LaminationMaterialCompatibility compatibility) {
        try {
            LaminationMaterialCompatibility saved = service.saveCompatibility(compatibility);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/compatibilities/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody LaminationMaterialCompatibility compatibility) {
        try {
            compatibility.setId(id);
            LaminationMaterialCompatibility updated = service.saveCompatibility(compatibility);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/compatibilities/batch")
    public ResponseEntity<Void> batchSaveCompatibilities(@RequestBody List<LaminationMaterialCompatibility> compatibilities) {
        service.batchSaveCompatibilities(compatibilities);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/compatibilities/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        service.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/options/post-processing-steps")
    public ResponseEntity<List<String>> getAllPostProcessingSteps() {
        List<String> steps = service.getAllPostProcessingSteps();
        return ResponseEntity.ok(steps);
    }

    // ========== 匹配查询 ==========

    /**
     * 匹配查询：关键词搜索 + 多材料类型筛选 + 多工序筛选 + 分页
     */
    @PostMapping("/match")
    public ResponseEntity<PagedResult<LaminationMaterialProduct>> match(@RequestBody LaminationMaterialQueryParams params) {
        if (params.getPage() == null || params.getPage() < 1) {
            params.setPage(1);
        }
        if (params.getSize() == null || params.getSize() < 1) {
            params.setSize(15);
        }
        PagedResult<LaminationMaterialProduct> result = service.searchProducts(
            params.getKeyword(), params.getMaterialTypes(), params.getSteps(), params.getPage(), params.getSize());
        return ResponseEntity.ok(result);
    }

    /**
     * 获取所有后加工工序步骤名称（去重）
     */
    @GetMapping("/steps")
    public ResponseEntity<List<String>> getSteps() {
        List<String> steps = service.getDistinctSteps();
        return ResponseEntity.ok(steps);
    }

    /**
     * 获取所有材料类型（去重）
     */
    @GetMapping("/material-types")
    public ResponseEntity<List<String>> getMaterialTypes() {
        List<String> types = service.getDistinctMaterialTypes();
        return ResponseEntity.ok(types);
    }

    /**
     * 获取产品详情（含兼容性列表）
     */
    @GetMapping("/products/{id}/detail")
    public ResponseEntity<LaminationMaterialProductDTO> getProductDetail(@PathVariable Integer id) {
        LaminationMaterialProductDTO dto = service.getProductDetail(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
