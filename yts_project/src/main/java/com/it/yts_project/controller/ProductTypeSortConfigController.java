package com.it.yts_project.controller;

import com.it.yts_project.model.ProductTypeSortConfig;
import com.it.yts_project.service.ProductTypeSortConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品类型特殊映射配置控制器
 */
@RestController
@RequestMapping("/api/product-type-sort-config")
public class ProductTypeSortConfigController {

    @Autowired
    private ProductTypeSortConfigService configService;

    /**
     * 获取所有配置
     */
    @GetMapping
    public ResponseEntity<List<ProductTypeSortConfig>> getAll() {
        return ResponseEntity.ok(configService.getAll());
    }

    /**
     * 获取所有激活的配置
     */
    @GetMapping("/active")
    public ResponseEntity<List<ProductTypeSortConfig>> getAllActive() {
        return ResponseEntity.ok(configService.getAllActive());
    }

    /**
     * 根据ID获取配置
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeSortConfig> getById(@PathVariable Integer id) {
        ProductTypeSortConfig config = configService.getById(id);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }

    /**
     * 根据产品类型ID获取配置
     */
    @GetMapping("/by-product-type/{productTypeId}")
    public ResponseEntity<ProductTypeSortConfig> getByProductTypeId(@PathVariable Integer productTypeId) {
        ProductTypeSortConfig config = configService.getByProductTypeId(productTypeId);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }

    /**
     * 创建或更新配置
     */
    @PostMapping
    public ResponseEntity<ProductTypeSortConfig> save(@RequestBody ProductTypeSortConfig config) {
        return ResponseEntity.ok(configService.save(config));
    }

    /**
     * 更新配置
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeSortConfig> update(@PathVariable Integer id, @RequestBody ProductTypeSortConfig config) {
        config.setId(id);
        return ResponseEntity.ok(configService.save(config));
    }

    /**
     * 删除配置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        configService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
