package com.it.yts_project.controller;

import com.it.yts_project.model.ProductTypeOptions;
import com.it.yts_project.service.ProductTypeOptionsService;
import com.it.yts_project.util.ExcelExportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品类型选项控制器
 */
@RestController
@RequestMapping("/product-type-options")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class ProductTypeOptionsController {
    
    private static final Logger log = LoggerFactory.getLogger(ProductTypeOptionsController.class);
    
    @Autowired
    private ProductTypeOptionsService productTypeOptionsService;
    
    /**
     * 获取所有激活的产品类型选项
     * @return 产品类型选项列表
     */
    @GetMapping("/active")
    public ResponseEntity<List<ProductTypeOptions>> getAllActiveOptions() {
        List<ProductTypeOptions> options = productTypeOptionsService.getAllActiveOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 根据ID获取产品类型选项
     * @param id 主键ID
     * @return 产品类型选项
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeOptions> getById(@PathVariable Integer id) {
        ProductTypeOptions option = productTypeOptionsService.getById(id);
        if (option != null) {
            return ResponseEntity.ok(option);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取所有产品类型选项（包括非激活的）
     * @return 产品类型选项列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<ProductTypeOptions>> getAllOptions() {
        List<ProductTypeOptions> options = productTypeOptionsService.getAllOptions();
        log.debug("获取所有产品类型选项，共 {} 个选项", options.size());
        return ResponseEntity.ok(options);
    }
    
    /**
     * 创建产品类型选项
     * @param productType 产品类型选项
     * @return 创建的产品类型选项
     */
    @PostMapping
    public ResponseEntity<ProductTypeOptions> createProductType(@RequestBody ProductTypeOptions productType) {
        log.debug("创建产品类型请求 - 数据: {}", productType);
        ProductTypeOptions createdProductType = productTypeOptionsService.save(productType);
        log.debug("创建成功: {}", createdProductType);
        return ResponseEntity.ok(createdProductType);
    }
    
    /**
     * 更新产品类型选项
     * @param id 主键ID
     * @param productType 产品类型选项
     * @return 更新的产品类型选项
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeOptions> updateProductType(@PathVariable Integer id, @RequestBody ProductTypeOptions productType) {
        log.debug("更新产品类型请求 - ID: {}, 数据: {}", id, productType);
        productType.setId(id);
        ProductTypeOptions updatedProductType = productTypeOptionsService.save(productType);
        log.debug("更新成功: {}", updatedProductType);
        return ResponseEntity.ok(updatedProductType);
    }
    
    /**
     * 删除产品类型选项
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Integer id) {
        log.debug("删除产品类型请求 - ID: {}", id);
        productTypeOptionsService.deleteById(id);
        log.debug("删除成功 - ID: {}", id);
        return ResponseEntity.ok().build();
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出产品类型选项数据到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportProductTypeOptionsToExcel() {
        List<ProductTypeOptions> allData = productTypeOptionsService.getAllOptions();
        
        // 生成文件名
        String fileName = ExcelExportUtil.generateFileName("产品类型选项");
        
        // 导出Excel
        return ExcelExportUtil.exportToResponse(allData, ProductTypeOptions.class, fileName, "产品类型选项");
    }
}
