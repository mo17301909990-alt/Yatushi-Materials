package com.it.yts_project.controller;

import com.it.yts_project.model.Product;
import com.it.yts_project.model.Specification;
import com.it.yts_project.model.Pricing;
import com.it.yts_project.model.CompleteProductInfo;
import com.it.yts_project.service.ProductService;
import com.it.yts_project.service.CompleteProductInfoService;
import com.it.yts_project.util.ExcelExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品信息Controller
 */
@RestController
@RequestMapping("/api/product-management")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CompleteProductInfoService completeProductInfoService;
    
    /**
     * 查询所有产品
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }
    
    /**
     * 根据ID查询产品
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = productService.findById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据燙金紙系列查询
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<Product>> getProductsByName(@RequestParam String name) {
        List<Product> products = productService.findByName(name);
        return ResponseEntity.ok(products);
    }
    
    /**
     * 获取所有产品名称
     */
    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllProductNames() {
        List<String> productNames = productService.getAllProductNames();
        return ResponseEntity.ok(productNames);
    }
    
    /**
     * 根据型号查询
     */
    @GetMapping("/search/model")
    public ResponseEntity<List<Product>> getProductsByModelNumber(@RequestParam String modelNumber) {
        List<Product> products = productService.findByModelNumber(modelNumber);
        return ResponseEntity.ok(products);
    }
    
    /**
     * 根据物料编号查询
     */
    @GetMapping("/search/material")
    public ResponseEntity<List<Product>> getProductsByMaterialNumber(@RequestParam String materialNumber) {
        List<Product> products = productService.findByMaterialNumber(materialNumber);
        return ResponseEntity.ok(products);
    }
    
    /**
     * 根据烫金纸张类型查询
     */
    @GetMapping("/search/paper-type")
    public ResponseEntity<List<Product>> getProductsByPaperType(@RequestParam String hotStampingPaperType) {
        List<Product> products = productService.findByHotStampingPaperType(hotStampingPaperType);
        return ResponseEntity.ok(products);
    }
    
    /**
     * 搜索产品
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(products);
    }
    
    /**
     * 保存产品
     */
    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.ok(savedProduct);
    }
    
    /**
     * 更新产品
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        product.setId(id);
        Product updatedProduct = productService.save(product);
        return ResponseEntity.ok(updatedProduct);
    }
    
    /**
     * 删除产品
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取所有烫金纸张类型选项
     */
    @GetMapping("/options/paper-types")
    public ResponseEntity<List<String>> getHotStampingPaperTypeOptions() {
        List<String> options = productService.getHotStampingPaperTypeOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 获取所有单位选项
     */
    @GetMapping("/options/danwei")
    public ResponseEntity<List<String>> getDanweiOptions() {
        List<String> options = productService.getDanweiOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 获取所有牌子选项
     */
    @GetMapping("/options/paizi")
    public ResponseEntity<List<String>> getPaiziOptions() {
        List<String> options = productService.getPaiziOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 获取所有封度选项
     */
    @GetMapping("/options/fengdu")
    public ResponseEntity<List<String>> getFengduOptions() {
        List<String> options = productService.getFengduOptions();
        return ResponseEntity.ok(options);
    }
    
    // ========== 规格管理 ==========
    
    /**
     * 根据产品ID查询规格
     */
    @GetMapping("/{productId}/specifications")
    public ResponseEntity<List<Specification>> getSpecificationsByProductId(@PathVariable Integer productId) {
        List<Specification> specifications = productService.getSpecificationsByProductId(productId);
        return ResponseEntity.ok(specifications);
    }
    
    /**
     * 保存产品规格
     */
    @PostMapping("/specifications")
    public ResponseEntity<Specification> saveSpecification(@RequestBody Specification specification) {
        Specification savedSpecification = productService.saveSpecification(specification);
        return ResponseEntity.ok(savedSpecification);
    }
    
    /**
     * 更新产品规格
     */
    @PutMapping("/specifications/{id}")
    public ResponseEntity<Specification> updateSpecification(@PathVariable Integer id, @RequestBody Specification specification) {
        specification.setId(id);
        Specification updatedSpecification = productService.saveSpecification(specification);
        return ResponseEntity.ok(updatedSpecification);
    }
    
    /**
     * 删除产品规格
     */
    @DeleteMapping("/specifications/{id}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable Integer id) {
        productService.deleteSpecificationById(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取所有颜色选项
     */
    @GetMapping("/options/colors")
    public ResponseEntity<List<String>> getColorOptions() {
        List<String> options = productService.getColorOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 获取所有尺寸选项
     */
    @GetMapping("/options/sizes")
    public ResponseEntity<List<String>> getSizeOptions() {
        List<String> options = productService.getSizeOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 获取所有紧密度选项
     */
    @GetMapping("/options/tightness")
    public ResponseEntity<List<String>> getTightnessOptions() {
        List<String> options = productService.getTightnessOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 获取所有温度范围选项
     */
    @GetMapping("/options/temperature-ranges")
    public ResponseEntity<List<String>> getTemperatureRangeOptions() {
        List<String> options = productService.getTemperatureRangeOptions();
        return ResponseEntity.ok(options);
    }
    
    // ========== 价格管理 ==========
    
    /**
     * 根据产品ID查询价格
     */
    @GetMapping("/{productId}/pricing")
    public ResponseEntity<List<Pricing>> getPricingByProductId(@PathVariable Integer productId) {
        List<Pricing> pricingList = productService.getPricingByProductId(productId);
        return ResponseEntity.ok(pricingList);
    }
    
    /**
     * 保存产品价格
     */
    @PostMapping("/pricing")
    public ResponseEntity<Pricing> savePricing(@RequestBody Pricing pricing) {
        Pricing savedPricing = productService.savePricing(pricing);
        return ResponseEntity.ok(savedPricing);
    }
    
    /**
     * 更新产品价格
     */
    @PutMapping("/pricing/{id}")
    public ResponseEntity<Pricing> updatePricing(@PathVariable Integer id, @RequestBody Pricing pricing) {
        pricing.setId(id);
        Pricing updatedPricing = productService.savePricing(pricing);
        return ResponseEntity.ok(updatedPricing);
    }
    
    /**
     * 删除产品价格
     */
    @DeleteMapping("/pricing/{id}")
    public ResponseEntity<Void> deletePricing(@PathVariable Integer id) {
        productService.deletePricingById(id);
        return ResponseEntity.ok().build();
    }
    
    // ========== 完整产品信息管理 ==========
    
    /**
     * 获取所有完整产品信息
     */
    @GetMapping("/complete")
    public ResponseEntity<List<CompleteProductInfo>> getAllCompleteProductInfo() {
        List<CompleteProductInfo> completeProductInfoList = completeProductInfoService.findAllCompleteProductInfo();
        return ResponseEntity.ok(completeProductInfoList);
    }
    
    /**
     * 根据产品ID获取完整产品信息
     */
    @GetMapping("/complete/{id}")
    public ResponseEntity<CompleteProductInfo> getCompleteProductInfoById(@PathVariable Integer id) {
        CompleteProductInfo completeProductInfo = completeProductInfoService.findCompleteProductInfoById(id);
        if (completeProductInfo != null) {
            return ResponseEntity.ok(completeProductInfo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 根据物料编号获取完整产品信息
     */
    @GetMapping("/complete/search/material")
    public ResponseEntity<List<CompleteProductInfo>> getCompleteProductInfoByMaterialNumber(@RequestParam String materialNumber) {
        List<CompleteProductInfo> completeProductInfoList = completeProductInfoService.findCompleteProductInfoByMaterialNumber(materialNumber);
        return ResponseEntity.ok(completeProductInfoList);
    }
    
    /**
     * 根据型号获取完整产品信息
     */
    @GetMapping("/complete/search/model")
    public ResponseEntity<List<CompleteProductInfo>> getCompleteProductInfoByModelNumber(@RequestParam String modelNumber) {
        List<CompleteProductInfo> completeProductInfoList = completeProductInfoService.findCompleteProductInfoByModelNumber(modelNumber);
        return ResponseEntity.ok(completeProductInfoList);
    }
    
    /**
     * 根据Leo Touch编号获取完整产品信息
     */
    @GetMapping("/complete/search/company-number")
    public ResponseEntity<List<CompleteProductInfo>> getCompleteProductInfoByCompanyNumber(@RequestParam String companyNumber) {
        List<CompleteProductInfo> completeProductInfoList = completeProductInfoService.findCompleteProductInfoByCompanyNumber(companyNumber);
        return ResponseEntity.ok(completeProductInfoList);
    }
    
    /**
     * 根据SPNO获取完整产品信息
     */
    @GetMapping("/complete/search/gp-no")
    public ResponseEntity<List<CompleteProductInfo>> getCompleteProductInfoByGpNo(@RequestParam String gpNo) {
        List<CompleteProductInfo> completeProductInfoList = completeProductInfoService.findCompleteProductInfoByGpNo(gpNo);
        return ResponseEntity.ok(completeProductInfoList);
    }
    
    /**
     * 根据烫金纸张类型获取完整产品信息
     */
    @GetMapping("/complete/search/paper-type")
    public ResponseEntity<List<CompleteProductInfo>> getCompleteProductInfoByHotStampingPaperType(@RequestParam String hotStampingPaperType) {
        List<CompleteProductInfo> completeProductInfoList = completeProductInfoService.findCompleteProductInfoByHotStampingPaperType(hotStampingPaperType);
        return ResponseEntity.ok(completeProductInfoList);
    }
    
    /**
     * 保存完整产品信息
     */
    @PostMapping("/complete")
    public ResponseEntity<CompleteProductInfo> saveCompleteProductInfo(@RequestBody CompleteProductInfo completeProductInfo) {
        CompleteProductInfo savedProductInfo = completeProductInfoService.saveCompleteProductInfo(completeProductInfo);
        return ResponseEntity.ok(savedProductInfo);
    }
    
    /**
     * 更新完整产品信息
     */
    @PutMapping("/complete/{id}")
    public ResponseEntity<CompleteProductInfo> updateCompleteProductInfo(@PathVariable Integer id, @RequestBody CompleteProductInfo completeProductInfo) {
        CompleteProductInfo updatedProductInfo = completeProductInfoService.updateCompleteProductInfo(id, completeProductInfo);
        return ResponseEntity.ok(updatedProductInfo);
    }
    
    /**
     * 批量更新产品信息
     */
    @PutMapping("/batch/update")
    public ResponseEntity<java.util.Map<String, Object>> batchUpdateProducts(@RequestBody java.util.Map<String, Object> request) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            @SuppressWarnings("unchecked")
            java.util.List<Integer> ids = (java.util.List<Integer>) request.get("ids");
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> updateFields = (java.util.Map<String, Object>) request.get("updateFields");
            
            if (ids == null || ids.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择要更新的产品");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (updateFields == null || updateFields.isEmpty()) {
                response.put("success", false);
                response.put("message", "请至少选择一个要修改的字段");
                return ResponseEntity.badRequest().body(response);
            }
            
            java.util.Map<String, Object> batchResult = completeProductInfoService.batchUpdateProducts(ids, updateFields);
            
            int successCount = (Integer) batchResult.get("successCount");
            int failCount = (Integer) batchResult.get("failCount");
            @SuppressWarnings("unchecked")
            java.util.List<String> errorMessages = (java.util.List<String>) batchResult.get("errorMessages");
            
            response.put("success", failCount == 0);
            if (failCount == 0) {
                response.put("message", String.format("成功更新 %d 条记录", successCount));
            } else {
                response.put("message", String.format("成功更新 %d 条，失败 %d 条", successCount, failCount));
                response.put("errorMessages", errorMessages);
            }
            response.put("successCount", successCount);
            response.put("failCount", failCount);
            response.put("totalCount", ids.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量更新失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 批量删除产品
     */
    @PostMapping("/batch/delete")
    public ResponseEntity<java.util.Map<String, Object>> batchDeleteProducts(@RequestBody java.util.Map<String, Object> request) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        try {
            @SuppressWarnings("unchecked")
            java.util.List<Integer> ids = (java.util.List<Integer>) request.get("ids");
            
            if (ids == null || ids.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择要删除的产品");
                return ResponseEntity.badRequest().body(response);
            }
            
            java.util.Map<String, Object> batchResult = completeProductInfoService.batchDeleteProducts(ids);
            
            int successCount = (Integer) batchResult.get("successCount");
            int failCount = (Integer) batchResult.get("failCount");
            @SuppressWarnings("unchecked")
            java.util.List<String> errorMessages = (java.util.List<String>) batchResult.get("errorMessages");
            
            response.put("success", failCount == 0);
            if (failCount == 0) {
                response.put("message", String.format("成功删除 %d 条记录", successCount));
            } else {
                response.put("message", String.format("成功删除 %d 条，失败 %d 条", successCount, failCount));
                response.put("errorMessages", errorMessages);
            }
            response.put("successCount", successCount);
            response.put("failCount", failCount);
            response.put("totalCount", ids.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量删除失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出产品管理信息到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportProductManagementToExcel() {
        try {
            List<Product> allData = productService.findAll();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("产品管理信息");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, Product.class, fileName, "产品管理信息");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导出完整产品信息到Excel
     */
    @GetMapping("/complete/export")
    public ResponseEntity<byte[]> exportCompleteProductInfoToExcel() {
        try {
            List<CompleteProductInfo> allData = completeProductInfoService.findAllCompleteProductInfo();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("完整产品信息");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, CompleteProductInfo.class, fileName, "完整产品信息");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
