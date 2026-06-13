package com.it.yts_project.controller;

import com.it.yts_project.dto.ClothPaperTypeDTO;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.model.PostProcessingPrint;
import com.it.yts_project.model.Products;
import com.it.yts_project.model.Product;
import com.it.yts_project.service.ClothPaperTypeService;
import com.it.yts_project.service.PostProcessingPrintService;
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
 * 印刷后加工控制器
 */
@RestController
@RequestMapping("/api/post-processing-print")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class PostProcessingPrintController {
    
    @Autowired
    private PostProcessingPrintService printService;
    
    @Autowired
    private ClothPaperTypeService clothPaperTypeService;
    
    @Autowired
    private ProductsMapper productsMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    /**
     * 获取所有印刷配置
     */
    @GetMapping
    public ResponseEntity<List<PostProcessingPrint>> getAllPrintConfigs() {
        List<PostProcessingPrint> configs = printService.getAllPrintConfigs();
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 分页获取印刷配置
     */
    @GetMapping("/pagination")
    public ResponseEntity<Map<String, Object>> getPrintConfigsWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<PostProcessingPrint> configs = printService.getPrintConfigsWithPagination(page, size);
        int totalCount = printService.getTotalCount();
        
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
     * 根据ID获取印刷配置
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostProcessingPrint> getPrintConfigById(@PathVariable Integer id) {
        PostProcessingPrint config = printService.getPrintConfigById(id);
        if (config != null) {
            return ResponseEntity.ok(config);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 创建印刷配置
     */
    @PostMapping
    public ResponseEntity<PostProcessingPrint> createPrintConfig(@RequestBody PostProcessingPrint postProcessingPrint) {
        PostProcessingPrint created = printService.createPrintConfig(postProcessingPrint);
        return ResponseEntity.ok(created);
    }
    
    /**
     * 更新印刷配置
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostProcessingPrint> updatePrintConfig(
            @PathVariable Integer id,
            @RequestBody PostProcessingPrint postProcessingPrint) {
        postProcessingPrint.setId(id);
        PostProcessingPrint updated = printService.updatePrintConfig(postProcessingPrint);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * 删除印刷配置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrintConfig(@PathVariable Integer id) {
        printService.deletePrintConfig(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 批量删除印刷配置
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeletePrintConfigs(@RequestBody List<Integer> ids) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (ids == null || ids.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择要删除的记录");
                return ResponseEntity.badRequest().body(response);
            }
            
            printService.batchDeletePrintConfigs(ids);
            
            response.put("success", true);
            response.put("message", String.format("成功删除 %d 条记录", ids.size()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 根据燙金紙系列搜索
     */
    @GetMapping("/search/product-name")
    public ResponseEntity<List<PostProcessingPrint>> searchByProductName(@RequestParam String productName) {
        List<PostProcessingPrint> configs = printService.searchByProductName(productName);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 根据产品型号搜索
     */
    @GetMapping("/search/product-model")
    public ResponseEntity<List<PostProcessingPrint>> searchByProductModelNumber(@RequestParam String productModelNumber) {
        List<PostProcessingPrint> configs = printService.searchByProductModelNumber(productModelNumber);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 根据颜色搜索
     */
    @GetMapping("/search/color")
    public ResponseEntity<List<PostProcessingPrint>> searchByColor(@RequestParam String color) {
        List<PostProcessingPrint> configs = printService.searchByColor(color);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 根据布面纸类型ID搜索
     */
    @GetMapping("/search/cloth-paper-type")
    public ResponseEntity<List<PostProcessingPrint>> searchByClothPaperTypeId(@RequestParam Integer clothPaperTypeId) {
        List<PostProcessingPrint> configs = printService.searchByClothPaperTypeId(clothPaperTypeId);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 获取所有可用的燙金紙系列
     */
    @GetMapping("/options/product-names")
    public ResponseEntity<List<String>> getAllProductNames() {
        List<String> productNames = printService.getAllProductNames();
        return ResponseEntity.ok(productNames);
    }
    
    /**
     * 获取所有可用的产品型号
     */
    @GetMapping("/options/product-model-numbers")
    public ResponseEntity<List<String>> getAllProductModelNumbers() {
        List<String> modelNumbers = printService.getAllProductModelNumbers();
        return ResponseEntity.ok(modelNumbers);
    }
    
    /**
     * 获取所有可用的颜色
     */
    @GetMapping("/options/colors")
    public ResponseEntity<List<String>> getAllColors() {
        List<String> colors = printService.getAllColors();
        return ResponseEntity.ok(colors);
    }
    
    /**
     * 根据产品名称和型号获取颜色选项
     */
    @GetMapping("/options/colors-by-product-and-model")
    public ResponseEntity<List<String>> getColorsByProductAndModel(
            @RequestParam String productName,
            @RequestParam String productModelNumber) {
        List<String> colors = printService.getColorsByProductAndModel(productName, productModelNumber);
        return ResponseEntity.ok(colors);
    }
    
    /**
     * 根据产品名称获取颜色选项
     */
    @GetMapping("/options/colors-by-product-name")
    public ResponseEntity<List<String>> getColorsByProductName(@RequestParam String productName) {
        List<String> colors = printService.getColorsByProductName(productName);
        return ResponseEntity.ok(colors);
    }
    
    /**
     * 根据产品型号获取颜色选项
     */
    @GetMapping("/options/colors-by-product-model")
    public ResponseEntity<List<String>> getColorsByProductModelNumber(@RequestParam String productModelNumber) {
        List<String> colors = printService.getColorsByProductModelNumber(productModelNumber);
        return ResponseEntity.ok(colors);
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出印刷后加工配置到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportPrintConfigsToExcel() {
        try {
            List<PostProcessingPrint> allData = printService.getAllPrintConfigs();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("印刷配置");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, PostProcessingPrint.class, fileName, "印刷配置");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导入印刷后加工配置
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importPrintConfigs(
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
            com.alibaba.excel.EasyExcel.read(file.getInputStream(), PostProcessingPrint.class,
                new com.alibaba.excel.read.listener.ReadListener<PostProcessingPrint>() {
                    @Override
                    public void invoke(PostProcessingPrint data, com.alibaba.excel.context.AnalysisContext context) {
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
                            List<Products> products = productsMapper.getProductsBySeriesName(productName);
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
                            
                            // 验证颜色（如果提供）
                            String color = data.getColor() != null && !data.getColor().trim().isEmpty()
                                ? data.getColor().trim() : null;
                            
                            if (color != null) {
                                // 验证颜色是否存在且属于指定的产品
                                boolean isValidColor = printService.validateColor(color, productName, productModelNumber);
                                if (!isValidColor) {
                                    errorMessages.add(String.format("第%d行: 顏色「%s」不屬於指定的產品系列「%s」%s",
                                        rowIndex,
                                        color,
                                        productName,
                                        productModelNumber != null ? "或產品型號「" + productModelNumber + "」" : ""));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 验证必填字段：布面紙類型
                            if (data.getClothPaperTypeName() == null || data.getClothPaperTypeName().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 布面紙類型不能為空", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            String clothPaperTypeName = data.getClothPaperTypeName().trim();
                            
                            // 解析 category.type_name 格式
                            String category = null;
                            String typeName = null;
                            
                            if (clothPaperTypeName.contains(".")) {
                                String[] parts = clothPaperTypeName.split("\\.", 2);
                                if (parts.length == 2) {
                                    category = parts[0].trim();
                                    typeName = parts[1].trim();
                                } else {
                                    errorMessages.add(String.format("第%d行: 布面紙類型格式錯誤，應為「分類.類型名稱」（例如: 尼龍絲.JHT-8001），當前值: %s", rowIndex, clothPaperTypeName));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            } else {
                                errorMessages.add(String.format("第%d行: 布面紙類型格式錯誤，應為「分類.類型名稱」（例如: 尼龍絲.JHT-8001），當前值: %s", rowIndex, clothPaperTypeName));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证分类和类型名称不能为空
                            if (category == null || category.isEmpty()) {
                                errorMessages.add(String.format("第%d行: 布面紙類型格式錯誤，分類不能為空，當前值: %s", rowIndex, clothPaperTypeName));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            if (typeName == null || typeName.isEmpty()) {
                                errorMessages.add(String.format("第%d行: 布面紙類型格式錯誤，類型名稱不能為空，當前值: %s", rowIndex, clothPaperTypeName));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 根据分类和类型名称查找ID
                            ClothPaperTypeDTO clothPaperType = clothPaperTypeService.getByTypeNameAndCategory(typeName, category);
                            
                            if (clothPaperType == null) {
                                errorMessages.add(String.format("第%d行: 布面紙類型「%s」（分類: %s, 類型名稱: %s）不存在", 
                                        rowIndex, 
                                        clothPaperTypeName,
                                        category,
                                        typeName));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            Integer resolvedClothPaperTypeId = clothPaperType.getId();
                            
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
                                // 验证paperType是否在系统中存在
                                List<String> seriesByPaperType = productsMapper.getSeriesNamesByPaperType(paperType);
                                if (seriesByPaperType == null || seriesByPaperType.isEmpty()) {
                                    errorMessages.add(String.format("第%d行: 燙金紙性能類型「%s」在系統中不存在", rowIndex, paperType));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                // 验证该系列是否支持该paperType
                                boolean supportsPaperType = productsMapper.checkSeriesSupportsPaperType(productName, paperType);
                                if (!supportsPaperType) {
                                    errorMessages.add(String.format("第%d行: 燙金紙系列「%s」不支持燙金紙性能類型「%s」", rowIndex, productName, paperType));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 检查是否已存在（根据唯一键）
                            PostProcessingPrint existing = printService.findByUniqueKey(
                                productName,
                                productModelNumber,
                                color,
                                resolvedClothPaperTypeId,
                                paperType.isEmpty() ? null : paperType
                            );
                            
                            // 创建或更新记录
                            PostProcessingPrint toSave = new PostProcessingPrint();
                            if (existing != null) {
                                // 更新现有记录
                                toSave.setId(existing.getId());
                                toSave.setProductName(productName);
                                toSave.setProductModelNumber(productModelNumber);
                                toSave.setColor(color);
                                toSave.setClothPaperTypeId(resolvedClothPaperTypeId);
                                toSave.setCompatibilityStatus(status);
                                toSave.setPaperType(paperType.isEmpty() ? null : paperType);
                                printService.updatePrintConfig(toSave);
                            } else {
                                // 创建新记录
                                toSave.setProductName(productName);
                                toSave.setProductModelNumber(productModelNumber);
                                toSave.setColor(color);
                                toSave.setClothPaperTypeId(resolvedClothPaperTypeId);
                                toSave.setCompatibilityStatus(status);
                                toSave.setPaperType(paperType.isEmpty() ? null : paperType);
                                printService.createPrintConfig(toSave);
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
            List<PostProcessingPrint> exampleData = new ArrayList<>();
            PostProcessingPrint example = new PostProcessingPrint();
            example.setProductName("示例系列");
            example.setProductModelNumber("示例型号");
            example.setColor("示例颜色");
            example.setClothPaperTypeName("示例分类.示例类型");
            example.setCompatibilityStatus("V");
            example.setPaperType("普通金紙");
            exampleData.add(example);
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("印刷配置導入模板");
            
            // 导出Excel模板
            return ExcelExportUtil.exportToResponse(exampleData, PostProcessingPrint.class, fileName, "印刷配置導入模板");
            
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
            
            // 验证兼容性狀態的值是否有效
            String status = compatibilityStatus.trim();
            if (!status.equals("V") && !status.equals("X") && !status.equals("NA") && !status.equals("▷")) {
                response.put("success", false);
                response.put("message", "兼容性狀態值無效，必須為 V（適用）、X（不適用）、NA（不確定）或 ▷（需要打底處理）之一");
                return ResponseEntity.badRequest().body(response);
            }
            
            printService.batchUpdateStatus(ids, status);
            
            response.put("success", true);
            response.put("message", String.format("成功更新 %d 條記錄的兼容性狀態", ids.size()));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量更新失敗: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * 检查唯一性约束（用于批量添加时检查是否已存在）
     */
    @GetMapping("/check-unique")
    public ResponseEntity<PostProcessingPrint> checkUnique(
        @RequestParam String productName,
        @RequestParam(required = false) String productModelNumber,
        @RequestParam(required = false) String color,
        @RequestParam Integer clothPaperTypeId,
        @RequestParam(required = false) String paperType
    ) {
        PostProcessingPrint existing = printService.findByUniqueKey(
            productName,
            productModelNumber,
            color,
            clothPaperTypeId,
            paperType
        );
        if (existing != null) {
            return ResponseEntity.ok(existing);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 验证颜色是否存在且属于指定的产品
     */
    @GetMapping("/validate-color")
    public ResponseEntity<Map<String, Object>> validateColor(
        @RequestParam String color,
        @RequestParam(required = false) String productName,
        @RequestParam(required = false) String productModelNumber
    ) {
        Map<String, Object> response = new HashMap<>();
        
        boolean isValid = printService.validateColor(color, productName, productModelNumber);
        
        response.put("valid", isValid);
        if (!isValid) {
            response.put("message", String.format("顏色「%s」不屬於指定的產品%s",
                color,
                productName != null ? "系列「" + productName + "」" : ""
                + (productModelNumber != null ? "或產品型號「" + productModelNumber + "」" : "")));
        }
        
        return ResponseEntity.ok(response);
    }
}