package com.it.yts_project.controller;

import com.it.yts_project.mapper.LeoGpNumberMapper;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.model.LeoGpNumber;
import com.it.yts_project.model.PostProcessingPrintUv;
import com.it.yts_project.model.Product;
import com.it.yts_project.service.PostProcessingPrintUvService;
import com.it.yts_project.util.ExcelExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 印刷UV后加工控制器
 */
@RestController
@RequestMapping("/api/post-processing-print-uv")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class PostProcessingPrintUvController {
    
    @Autowired
    private PostProcessingPrintUvService uvService;
    
    @Autowired
    private ProductsMapper productsMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private LeoGpNumberMapper leoGpNumberMapper;
    
    /**
     * 获取所有UV印刷配置
     */
    @GetMapping
    public ResponseEntity<List<PostProcessingPrintUv>> getAllUvPrintConfigs() {
        List<PostProcessingPrintUv> configs = uvService.getAllUvPrintConfigs();
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 分页获取UV印刷配置
     */
    @GetMapping("/pagination")
    public ResponseEntity<Map<String, Object>> getUvPrintConfigsWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<PostProcessingPrintUv> configs = uvService.getUvPrintConfigsWithPagination(page, size);
        int totalCount = uvService.getTotalCount();
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", configs);
        response.put("totalElements", totalCount);
        response.put("number", page - 1); // Spring Data JPA uses 0-based page numbers
        response.put("size", size);
        response.put("totalPages", (int) Math.ceil((double) totalCount / size));
        response.put("first", page == 1);
        response.put("last", page >= (int) Math.ceil((double) totalCount / size));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 根据ID获取UV印刷配置
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostProcessingPrintUv> getUvPrintConfigById(@PathVariable Integer id) {
        PostProcessingPrintUv config = uvService.getUvPrintConfigById(id);
        if (config != null) {
            return ResponseEntity.ok(config);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 创建UV印刷配置
     */
    @PostMapping
    public ResponseEntity<PostProcessingPrintUv> createUvPrintConfig(@RequestBody PostProcessingPrintUv postProcessingPrintUv) {
        PostProcessingPrintUv created = uvService.createUvPrintConfig(postProcessingPrintUv);
        return ResponseEntity.ok(created);
    }
    
    /**
     * 更新UV印刷配置
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostProcessingPrintUv> updateUvPrintConfig(
            @PathVariable Integer id,
            @RequestBody PostProcessingPrintUv postProcessingPrintUv) {
        postProcessingPrintUv.setId(id);
        PostProcessingPrintUv updated = uvService.updateUvPrintConfig(postProcessingPrintUv);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * 删除UV印刷配置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUvPrintConfig(@PathVariable Integer id) {
        uvService.deleteUvPrintConfig(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 根据燙金紙系列搜索
     */
    @GetMapping("/search/product-name")
    public ResponseEntity<List<PostProcessingPrintUv>> searchByProductName(@RequestParam String productName) {
        List<PostProcessingPrintUv> configs = uvService.searchByProductName(productName);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 根据产品型号搜索
     */
    @GetMapping("/search/product-model")
    public ResponseEntity<List<PostProcessingPrintUv>> searchByProductModelNumber(@RequestParam String productModelNumber) {
        List<PostProcessingPrintUv> configs = uvService.searchByProductModelNumber(productModelNumber);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 根据公司编号搜索
     */
    @GetMapping("/search/company-number")
    public ResponseEntity<List<PostProcessingPrintUv>> searchByCompanyNumber(@RequestParam String companyNumber) {
        List<PostProcessingPrintUv> configs = uvService.searchByCompanyNumber(companyNumber);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 根据纸张类型搜索
     */
    @GetMapping("/search/paper-type")
    public ResponseEntity<List<PostProcessingPrintUv>> searchByPaperType(@RequestParam String paperType) {
        List<PostProcessingPrintUv> configs = uvService.searchByPaperType(paperType);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 获取所有可用的燙金紙系列
     */
    @GetMapping("/options/product-names")
    public ResponseEntity<List<String>> getAllProductNames() {
        List<String> productNames = uvService.getAllProductNames();
        return ResponseEntity.ok(productNames);
    }
    
    /**
     * 获取所有可用的产品型号
     */
    @GetMapping("/options/product-model-numbers")
    public ResponseEntity<List<String>> getAllProductModelNumbers() {
        List<String> modelNumbers = uvService.getAllProductModelNumbers();
        return ResponseEntity.ok(modelNumbers);
    }
    
    /**
     * 获取所有可用的公司编号
     */
    @GetMapping("/options/company-numbers")
    public ResponseEntity<List<String>> getAllCompanyNumbers() {
        List<String> companyNumbers = uvService.getAllCompanyNumbers();
        return ResponseEntity.ok(companyNumbers);
    }
    
    /**
     * 根据产品系列名称获取公司编号列表
     */
    @GetMapping("/options/company-numbers-by-product-name")
    public ResponseEntity<List<String>> getCompanyNumbersByProductName(@RequestParam String productName) {
        List<String> companyNumbers = uvService.getCompanyNumbersByProductName(productName);
        return ResponseEntity.ok(companyNumbers);
    }
    
    /**
     * 根据产品型号和名称获取公司编号列表
     */
    @GetMapping("/options/company-numbers-by-product-model")
    public ResponseEntity<List<String>> getCompanyNumbersByProductModel(
            @RequestParam String productModelNumber,
            @RequestParam String productName) {
        List<String> companyNumbers = uvService.getCompanyNumbersByProductModelNumberAndName(
            productModelNumber, productName);
        return ResponseEntity.ok(companyNumbers);
    }
    
    /**
     * 验证公司编号是否属于指定的产品系列或型号
     */
    @GetMapping("/validate-company-number")
    public ResponseEntity<Map<String, Object>> validateCompanyNumber(
            @RequestParam String companyNumber,
            @RequestParam String productName,
            @RequestParam(required = false) String productModelNumber) {
        Map<String, Object> response = new HashMap<>();
        boolean isValid = uvService.validateCompanyNumber(companyNumber, productName, productModelNumber);
        response.put("valid", isValid);
        if (!isValid) {
            response.put("message", "公司編號/客戶GP號「" + companyNumber + "」不屬於指定的產品系列或型號");
        }
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取所有可用的纸张类型
     */
    @GetMapping("/options/paper-types")
    public ResponseEntity<List<String>> getAllPaperTypes() {
        List<String> paperTypes = uvService.getAllPaperTypes();
        return ResponseEntity.ok(paperTypes);
    }
    
    /**
     * 检查印刷UV后加工兼容性
     */
    @GetMapping("/check-compatibility")
    public ResponseEntity<String> checkCompatibility(
        @RequestParam String productName,
        @RequestParam String productModelNumber,
        @RequestParam String companyNumber,
        @RequestParam(required = false) String paperType
    ) {
        String result = uvService.checkPrintUvCompatibility(
            productName,
            productModelNumber,
            companyNumber,
            paperType
        );
        return ResponseEntity.ok(result);
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出印刷UV后加工配置到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportUvPrintConfigsToExcel() {
        try {
            List<PostProcessingPrintUv> allData = uvService.getAllUvPrintConfigs();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("UV印刷配置");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, PostProcessingPrintUv.class, fileName, "UV印刷配置");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 批量更新兼容性状态
     */
    @PutMapping("/batch/status")
    public ResponseEntity<Map<String, Object>> batchUpdateStatus(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            @SuppressWarnings("unchecked")
            List<Integer> ids = (List<Integer>) request.get("ids");
            String compatibilityStatus = (String) request.get("compatibilityStatus");
            
            if (ids == null || ids.isEmpty()) {
                response.put("success", false);
                response.put("message", "請選擇要更新的記錄");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (compatibilityStatus == null || compatibilityStatus.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "兼容性狀態不能為空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证兼容性狀態值
            String status = compatibilityStatus.trim();
            if (!status.equals("V") && !status.equals("X") && !status.equals("NA") && !status.equals("▷")) {
                response.put("success", false);
                response.put("message", "兼容性狀態值無效，必須為 V（適用）、X（不適用）、NA（不確定）或 ▷（需要打底處理）之一");
                return ResponseEntity.badRequest().body(response);
            }
            
            uvService.batchUpdateStatus(ids, status);
            
            response.put("success", true);
            response.put("message", String.format("成功更新 %d 條記錄", ids.size()));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量更新失敗: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 检查唯一性
     * 如果 companyNumber 未提供，则根据产品信息自动获取
     */
    @GetMapping("/check-unique")
    public ResponseEntity<PostProcessingPrintUv> checkUnique(
        @RequestParam String productName,
        @RequestParam(required = false) String productModelNumber,
        @RequestParam(required = false) String companyNumber,
        @RequestParam(required = false) String paperType) {
        
        // 如果 companyNumber 未提供，尝试根据产品信息自动获取
        String resolvedCompanyNumber = companyNumber;
        if (resolvedCompanyNumber == null || resolvedCompanyNumber.trim().isEmpty()) {
            if (productModelNumber != null && !productModelNumber.trim().isEmpty()) {
                // 根据产品型号和名称查找对应的公司编号或客户GP号
                List<Product> products = productMapper.findByModelNumberAndName(
                    productModelNumber.trim(), productName.trim());
                if (products != null && !products.isEmpty()) {
                    List<LeoGpNumber> leoGpNumbers =
                        leoGpNumberMapper.findByProjectId(products.get(0).getId());
                    if (leoGpNumbers != null && !leoGpNumbers.isEmpty()) {
                        LeoGpNumber leo = leoGpNumbers.get(0);
                        if (leo.getCompany_number() != null && !leo.getCompany_number().trim().isEmpty()) {
                            resolvedCompanyNumber = leo.getCompany_number();
                        } else if (leo.getGp_no() != null && !leo.getGp_no().trim().isEmpty()) {
                            resolvedCompanyNumber = leo.getGp_no();
                        }
                    }
                }
            }
        }
        
        // 如果仍然无法获取 companyNumber，使用空字符串进行匹配
        if (resolvedCompanyNumber == null || resolvedCompanyNumber.trim().isEmpty()) {
            resolvedCompanyNumber = "";
        }
        
        PostProcessingPrintUv existing = uvService.findByUniqueKey(
            productName,
            productModelNumber,
            resolvedCompanyNumber,
            paperType
        );
        if (existing != null) {
            return ResponseEntity.ok(existing);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 导入UV印刷配置
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importUvPrintConfigs(
            @RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                response.put("success", false);
                response.put("message", "請選擇要導入的文件");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                response.put("success", false);
                response.put("message", "文件格式不正確，請上傳Excel文件");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 读取Excel并导入数据
            List<String> errorMessages = new ArrayList<>();
            AtomicInteger totalCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);
            
            // 使用EasyExcel读取数据
            com.alibaba.excel.EasyExcel.read(file.getInputStream(), PostProcessingPrintUv.class,
                new com.alibaba.excel.read.listener.ReadListener<PostProcessingPrintUv>() {
                    @Override
                    public void invoke(PostProcessingPrintUv data, com.alibaba.excel.context.AnalysisContext context) {
                        totalCount.incrementAndGet();
                        try {
                            int rowIndex = context.readRowHolder().getRowIndex() + 1;
                            
                            // 验证必填字段：燙金紙系列
                            if (data.getProductName() == null || data.getProductName().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 燙金紙系列不能為空", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            String productName = data.getProductName().trim();
                            
                            // 验证燙金紙系列是否存在
                            List<com.it.yts_project.model.Products> products = productsMapper.getProductsBySeriesName(productName);
                            if (products == null || products.isEmpty()) {
                                errorMessages.add(String.format("第%d行: 燙金紙系列「%s」不存在", rowIndex, productName));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证产品型号（如果提供）
                            String productModelNumber = data.getProductModelNumber() != null && !data.getProductModelNumber().trim().isEmpty() 
                                ? data.getProductModelNumber().trim() : null;
                            
                            if (productModelNumber != null) {
                                // 首先验证产品型号是否在系统中存在
                                List<Product> productsByModel = productMapper.findByModelNumber(productModelNumber);
                                if (productsByModel == null || productsByModel.isEmpty()) {
                                    errorMessages.add(String.format("第%d行: 產品型號「%s」在系統中不存在", rowIndex, productModelNumber));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                // 然后验证产品型号是否在该系列中存在
                                boolean modelExistsInSeries = products.stream()
                                    .anyMatch(p -> productModelNumber.equals(p.getModel_number()));
                                if (!modelExistsInSeries) {
                                    errorMessages.add(String.format("第%d行: 產品型號「%s」在燙金紙系列「%s」中不存在", rowIndex, productModelNumber, productName));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 验证公司编号（如果提供）
                            String companyNumberFromExcel = data.getCompanyNumber() != null && !data.getCompanyNumber().trim().isEmpty() 
                                ? data.getCompanyNumber().trim() : null;
                            
                            if (companyNumberFromExcel != null) {
                                // 验证公司编号是否属于指定的产品系列或型号
                                boolean isValidCompanyNumber = uvService.validateCompanyNumber(
                                    companyNumberFromExcel, 
                                    productName, 
                                    productModelNumber
                                );
                                
                                if (!isValidCompanyNumber) {
                                    errorMessages.add(String.format("第%d行: 公司編號「%s」不屬於指定的產品系列「%s」%s", 
                                        rowIndex, 
                                        companyNumberFromExcel, 
                                        productName,
                                        productModelNumber != null ? "或產品型號「" + productModelNumber + "」" : ""));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 验证必填字段：兼容性狀態
                            if (data.getCompatibilityStatus() == null || data.getCompatibilityStatus().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 兼容性狀態不能為空", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证兼容性狀態的值是否有效
                            String status = data.getCompatibilityStatus().trim();
                            if (!status.equals("V") && !status.equals("X") && !status.equals("NA") && !status.equals("▷")) {
                                errorMessages.add(String.format("第%d行: 兼容性狀態值無效，必須為 V（適用）、X（不適用）、NA（不確定）或 ▷（需要打底處理）之一", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证paperType（如果提供）
                            String paperType = data.getPaperType() != null ? data.getPaperType().trim() : "";
                            if (!paperType.isEmpty()) {
                                // 首先验证该paperType是否在系统中存在
                                List<String> seriesNamesByPaperType = productsMapper.getSeriesNamesByPaperType(paperType);
                                if (seriesNamesByPaperType == null || seriesNamesByPaperType.isEmpty()) {
                                    errorMessages.add(String.format("第%d行: 燙金紙性能類型「%s」在系統中不存在", rowIndex, paperType));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                // 然后验证该系列是否支持该paperType
                                boolean supports = productsMapper.checkSeriesSupportsPaperType(productName, paperType);
                                if (!supports) {
                                    errorMessages.add(String.format("第%d行: 燙金紙系列「%s」不支持燙金紙性能類型「%s」", rowIndex, productName, paperType));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 确定要使用的公司编号
                            // 优先级：Excel中提供的 > 自动获取的
                            String companyNumberToUse = companyNumberFromExcel;
                            
                            // 如果Excel中没有提供公司编号/客户GP号，尝试自动获取
                            if (companyNumberToUse == null || companyNumberToUse.trim().isEmpty()) {
                                if (productModelNumber != null && !productModelNumber.trim().isEmpty()) {
                                    // 根据产品型号和名称查找对应的公司编号或客户GP号
                                    List<Product> foundProducts = productMapper.findByModelNumberAndName(
                                        productModelNumber.trim(), productName.trim());
                                    if (foundProducts != null && !foundProducts.isEmpty()) {
                                        List<LeoGpNumber> leoGpNumbers =
                                            leoGpNumberMapper.findByProjectId(foundProducts.get(0).getId());
                                        if (leoGpNumbers != null && !leoGpNumbers.isEmpty()) {
                                            LeoGpNumber leo = leoGpNumbers.get(0);
                                            if (leo.getCompany_number() != null && !leo.getCompany_number().trim().isEmpty()) {
                                                companyNumberToUse = leo.getCompany_number();
                                            } else if (leo.getGp_no() != null && !leo.getGp_no().trim().isEmpty()) {
                                                companyNumberToUse = leo.getGp_no();
                                            }
                                        }
                                    }
                                }
                            }
                            
                            // 检查是否已存在（根据唯一键）
                            PostProcessingPrintUv existing = null;
                            
                            // 如果确定了公司编号，使用它查找
                            if (companyNumberToUse != null && !companyNumberToUse.trim().isEmpty()) {
                                existing = uvService.findByUniqueKey(
                                    productName,
                                    productModelNumber,
                                    companyNumberToUse,
                                    paperType.isEmpty() ? null : paperType
                                );
                            } else {
                                // 如果没有公司编号，尝试用 null 查找（匹配所有可能的 companyNumber）
                                existing = uvService.findByUniqueKey(
                                    productName,
                                    productModelNumber,
                                    null,
                                    paperType.isEmpty() ? null : paperType
                                );
                            }
                            
                            // 创建或更新记录
                            PostProcessingPrintUv toSave = new PostProcessingPrintUv();
                            if (existing != null) {
                                // 更新现有记录
                                toSave.setId(existing.getId());
                                toSave.setProductName(productName);
                                toSave.setProductModelNumber(productModelNumber);
                                // 如果Excel中提供了公司编号，使用它；否则保持原有的
                                if (companyNumberFromExcel != null && !companyNumberFromExcel.trim().isEmpty()) {
                                    toSave.setCompanyNumber(companyNumberFromExcel);
                                } else {
                                    toSave.setCompanyNumber(existing.getCompanyNumber()); // 保持原有的 companyNumber
                                }
                                toSave.setCompatibilityStatus(status);
                                toSave.setPaperType(paperType.isEmpty() ? null : paperType);
                                uvService.updateUvPrintConfig(toSave);
                            } else {
                                // 创建新记录
                                toSave.setProductName(productName);
                                toSave.setProductModelNumber(productModelNumber);
                                // 如果Excel中提供了公司编号，使用它；否则让后端自动获取
                                if (companyNumberFromExcel != null && !companyNumberFromExcel.trim().isEmpty()) {
                                    toSave.setCompanyNumber(companyNumberFromExcel);
                                }
                                // 如果不设置 companyNumber，后端会自动获取
                                toSave.setCompatibilityStatus(status);
                                toSave.setPaperType(paperType.isEmpty() ? null : paperType);
                                uvService.createUvPrintConfig(toSave);
                            }
                            
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            int rowIndex = context.readRowHolder().getRowIndex() + 1;
                            errorMessages.add(String.format("第%d行: 處理失敗 - %s", rowIndex, e.getMessage()));
                            failCount.incrementAndGet();
                        }
                    }
                    
                    @Override
                    public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext context) {
                        // 所有数据读取完成
                    }
                }).sheet().doRead();
            
            // 构建响应
            response.put("success", failCount.get() == 0);
            response.put("totalCount", totalCount.get());
            response.put("successCount", successCount.get());
            response.put("failCount", failCount.get());
            response.put("message", String.format("導入完成：總數 %d，成功 %d，失敗 %d", 
                totalCount.get(), successCount.get(), failCount.get()));
            
            if (!errorMessages.isEmpty()) {
                response.put("errorMessages", errorMessages);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "導入失敗: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 下载导入模板
     */
    @GetMapping("/import-template")
    public ResponseEntity<byte[]> downloadImportTemplate() {
        try {
            // 创建示例数据
            List<PostProcessingPrintUv> exampleData = new ArrayList<>();
            PostProcessingPrintUv example = new PostProcessingPrintUv();
            example.setProductName("示例系列");
            example.setProductModelNumber("示例型号");
            example.setCompanyNumber("示例公司编号或客戶GP號"); // 可选：如果提供，系统会验证；如果不提供，系统会自动获取
            example.setCompatibilityStatus("V");
            example.setPaperType("普通金紙");
            exampleData.add(example);
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("UV印刷配置導入模板");
            
            // 导出Excel模板
            return ExcelExportUtil.exportToResponse(exampleData, PostProcessingPrintUv.class, fileName, "UV印刷配置導入模板");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
