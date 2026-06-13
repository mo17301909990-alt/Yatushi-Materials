package com.it.yts_project.controller;

import com.it.yts_project.dto.ClothPaperTypeDTO;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.model.PostProcessingLeduvglitter;
import com.it.yts_project.service.ClothPaperTypeService;
import com.it.yts_project.service.PostProcessingLeduvglitterService;
import com.it.yts_project.util.ExcelExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 絲印LED UV灑閃粉后加工控制器
 */
@RestController
@RequestMapping("/api/post-processing-leduvglitter")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class PostProcessingLeduvglitterController {
    
    @Autowired
    private PostProcessingLeduvglitterService leduvglitterService;
    
    @Autowired
    private ClothPaperTypeService clothPaperTypeService;
    
    @Autowired
    private ProductsMapper productsMapper;
    
    /**
     * 获取所有LED UV灑閃粉配置
     */
    @GetMapping
    public ResponseEntity<List<PostProcessingLeduvglitter>> getAllLeduvglitterConfigs() {
        List<PostProcessingLeduvglitter> configs = leduvglitterService.getAllLeduvglitterConfigs();
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 分页获取LED UV灑閃粉配置
     */
    @GetMapping("/pagination")
    public ResponseEntity<Map<String, Object>> getLeduvglitterConfigsWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<PostProcessingLeduvglitter> configs = leduvglitterService.getLeduvglitterConfigsWithPagination(page, size);
        int totalCount = leduvglitterService.getTotalCount();
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", configs);
        response.put("totalCount", totalCount);
        response.put("currentPage", page);
        response.put("pageSize", size);
        response.put("totalPages", (int) Math.ceil((double) totalCount / size));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 根据ID获取LED UV灑閃粉配置
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostProcessingLeduvglitter> getLeduvglitterConfigById(@PathVariable Integer id) {
        PostProcessingLeduvglitter config = leduvglitterService.getLeduvglitterConfigById(id);
        if (config != null) {
            return ResponseEntity.ok(config);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 创建LED UV灑閃粉配置
     */
    @PostMapping
    public ResponseEntity<PostProcessingLeduvglitter> createLeduvglitterConfig(@RequestBody PostProcessingLeduvglitter postProcessingLeduvglitter) {
        PostProcessingLeduvglitter created = leduvglitterService.createLeduvglitterConfig(postProcessingLeduvglitter);
        return ResponseEntity.ok(created);
    }
    
    /**
     * 更新LED UV灑閃粉配置
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostProcessingLeduvglitter> updateLeduvglitterConfig(
            @PathVariable Integer id,
            @RequestBody PostProcessingLeduvglitter postProcessingLeduvglitter) {
        postProcessingLeduvglitter.setId(id);
        PostProcessingLeduvglitter updated = leduvglitterService.updateLeduvglitterConfig(postProcessingLeduvglitter);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * 删除LED UV灑閃粉配置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeduvglitterConfig(@PathVariable Integer id) {
        leduvglitterService.deleteLeduvglitterConfig(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 根据燙金紙系列搜索
     */
    @GetMapping("/search/product-name")
    public ResponseEntity<List<PostProcessingLeduvglitter>> searchByProductName(@RequestParam String productName) {
        List<PostProcessingLeduvglitter> configs = leduvglitterService.searchByProductName(productName);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 根据产品型号搜索
     */
    @GetMapping("/search/product-model")
    public ResponseEntity<List<PostProcessingLeduvglitter>> searchByProductModelNumber(@RequestParam String productModelNumber) {
        List<PostProcessingLeduvglitter> configs = leduvglitterService.searchByProductModelNumber(productModelNumber);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 根据布面纸类型ID搜索
     */
    @GetMapping("/search/cloth-paper-type")
    public ResponseEntity<List<PostProcessingLeduvglitter>> searchByClothPaperTypeId(@RequestParam Integer clothPaperTypeId) {
        List<PostProcessingLeduvglitter> configs = leduvglitterService.searchByClothPaperTypeId(clothPaperTypeId);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 根据纸张类型搜索
     */
    @GetMapping("/search/paper-type")
    public ResponseEntity<List<PostProcessingLeduvglitter>> searchByPaperType(@RequestParam String paperType) {
        List<PostProcessingLeduvglitter> configs = leduvglitterService.searchByPaperType(paperType);
        return ResponseEntity.ok(configs);
    }
    
    /**
     * 获取所有可用的燙金紙系列
     */
    @GetMapping("/options/product-names")
    public ResponseEntity<List<String>> getAllProductNames() {
        List<String> productNames = leduvglitterService.getAllProductNames();
        return ResponseEntity.ok(productNames);
    }
    
    /**
     * 获取所有可用的产品型号
     */
    @GetMapping("/options/product-model-numbers")
    public ResponseEntity<List<String>> getAllProductModelNumbers() {
        List<String> modelNumbers = leduvglitterService.getAllProductModelNumbers();
        return ResponseEntity.ok(modelNumbers);
    }
    
    /**
     * 获取所有可用的布面纸类型ID
     */
    @GetMapping("/options/cloth-paper-type-ids")
    public ResponseEntity<List<Integer>> getAllClothPaperTypeIds() {
        List<Integer> clothPaperTypeIds = leduvglitterService.getAllClothPaperTypeIds();
        return ResponseEntity.ok(clothPaperTypeIds);
    }
    
    /**
     * 获取所有可用的纸张类型
     */
    @GetMapping("/options/paper-types")
    public ResponseEntity<List<String>> getAllPaperTypes() {
        List<String> paperTypes = leduvglitterService.getAllPaperTypes();
        return ResponseEntity.ok(paperTypes);
    }
    
    /**
     * 检查絲印LED UV灑閃粉后加工兼容性
     */
    @GetMapping("/check-compatibility")
    public ResponseEntity<String> checkCompatibility(
        @RequestParam String productName,
        @RequestParam String productModelNumber,
        @RequestParam Integer clothPaperTypeId,
        @RequestParam(required = false) String paperType
    ) {
        String result = leduvglitterService.checkLeduvglitterCompatibility(
            productName,
            productModelNumber,
            clothPaperTypeId,
            paperType
        );
        return ResponseEntity.ok(result);
    }
    
    /**
     * 检查唯一性约束（用于批量添加时检查是否已存在）
     */
    @GetMapping("/check-unique")
    public ResponseEntity<PostProcessingLeduvglitter> checkUnique(
        @RequestParam String productName,
        @RequestParam(required = false) String productModelNumber,
        @RequestParam Integer clothPaperTypeId,
        @RequestParam(required = false) String paperType
    ) {
        PostProcessingLeduvglitter existing = leduvglitterService.findByUniqueKey(
            productName,
            productModelNumber,
            clothPaperTypeId,
            paperType
        );
        if (existing != null) {
            return ResponseEntity.ok(existing);
        }
        return ResponseEntity.notFound().build();
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出LED UV灑閃粉后加工配置到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportLeduvglitterConfigsToExcel() {
        try {
            List<PostProcessingLeduvglitter> allData = leduvglitterService.getAllLeduvglitterConfigs();
            
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("絲印LED UV灑閃粉配置");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, PostProcessingLeduvglitter.class, fileName, "絲印LED UV灑閃粉配置");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导入LED UV灑閃粉后加工配置
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importLeduvglitterConfigs(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "clothPaperTypeId", required = false) Integer clothPaperTypeId) {
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
            com.alibaba.excel.EasyExcel.read(file.getInputStream(), PostProcessingLeduvglitter.class,
                new com.alibaba.excel.read.listener.ReadListener<PostProcessingLeduvglitter>() {
                    @Override
                    public void invoke(PostProcessingLeduvglitter data, com.alibaba.excel.context.AnalysisContext context) {
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
                            
                            // 根据布面紙類型名稱查找 clothPaperTypeId
                            // 布面紙類型格式为: category.type_name (例如: 尼龍絲.JHT-8001)
                            Integer resolvedClothPaperTypeId = null;
                            
                            // 验证布面紙類型名稱
                            if (data.getClothPaperTypeName() == null || data.getClothPaperTypeName().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 布面紙類型名稱不能為空", rowIndex));
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
                            
                            // 根据分类和类型名称查找ID，验证类型是否存在
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
                            
                            resolvedClothPaperTypeId = clothPaperType.getId();
                            
                            // 如果传入了 clothPaperTypeId，验证是否与Excel中的类型名称匹配
                            if (clothPaperTypeId != null) {
                                if (!resolvedClothPaperTypeId.equals(clothPaperTypeId)) {
                                    ClothPaperTypeDTO clothPaperTypeById = clothPaperTypeService.getById(clothPaperTypeId);
                                    String expectedTypeName = clothPaperTypeById != null ? 
                                        (clothPaperTypeById.getCategory() + "." + clothPaperTypeById.getTypeName()) : "未知";
                                    errorMessages.add(String.format("第%d行: Excel中的布面紙類型「%s」與指定的布面紙類型ID「%d」（類型: %s）不匹配", 
                                            rowIndex, 
                                            clothPaperTypeName,
                                            clothPaperTypeId,
                                            expectedTypeName));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 验证paperType（如果提供）
                            String paperType = data.getPaperType() != null ? data.getPaperType().trim() : "";
                            if (!paperType.isEmpty()) {
                                // 验证该系列是否支持该paperType
                                boolean supports = productsMapper.checkSeriesSupportsPaperType(productName, paperType);
                                if (!supports) {
                                    errorMessages.add(String.format("第%d行: 燙金紙系列「%s」不支持燙金紙性能類型「%s」", rowIndex, productName, paperType));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 处理productModelNumber（可能为空）
                            String productModelNumber = data.getProductModelNumber() != null && !data.getProductModelNumber().trim().isEmpty() 
                                ? data.getProductModelNumber().trim() : null;
                            
                            // 检查是否已存在（根据唯一键）
                            PostProcessingLeduvglitter existing = leduvglitterService.findByUniqueKey(
                                productName,
                                productModelNumber,
                                resolvedClothPaperTypeId,
                                paperType.isEmpty() ? null : paperType
                            );
                            
                            // 创建或更新记录
                            PostProcessingLeduvglitter toSave = new PostProcessingLeduvglitter();
                            if (existing != null) {
                                // 更新现有记录
                                toSave.setId(existing.getId());
                                toSave.setProductName(productName);
                                toSave.setProductModelNumber(productModelNumber);
                                toSave.setClothPaperTypeId(resolvedClothPaperTypeId);
                                toSave.setCompatibilityStatus(status);
                                toSave.setPaperType(paperType.isEmpty() ? null : paperType);
                                leduvglitterService.updateLeduvglitterConfig(toSave);
                            } else {
                                // 创建新记录
                                toSave.setProductName(productName);
                                toSave.setProductModelNumber(productModelNumber);
                                toSave.setClothPaperTypeId(resolvedClothPaperTypeId);
                                toSave.setCompatibilityStatus(status);
                                toSave.setPaperType(paperType.isEmpty() ? null : paperType);
                                leduvglitterService.createLeduvglitterConfig(toSave);
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
                        // 所有数据读取完成后的处理
                    }
                }).sheet().doRead();
            
            // 构建响应
            response.put("success", failCount.get() == 0);
            response.put("totalCount", totalCount.get());
            response.put("successCount", successCount.get());
            response.put("failCount", failCount.get());
            if (!errorMessages.isEmpty()) {
                response.put("errors", errorMessages);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "導入失敗: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 下载导入模板
     */
    @GetMapping("/import-template")
    public ResponseEntity<byte[]> downloadImportTemplate() {
        try {
            // 创建示例数据
            List<PostProcessingLeduvglitter> exampleData = new ArrayList<>();
            PostProcessingLeduvglitter example = new PostProcessingLeduvglitter();
            example.setProductName("示例系列");
            example.setProductModelNumber("示例型号");
            example.setClothPaperTypeName("示例分类.示例类型");
            example.setCompatibilityStatus("V");
            example.setPaperType("普通金紙");
            exampleData.add(example);
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("絲印LED UV灑閃粉配置導入模板");
            
            // 导出Excel模板
            return ExcelExportUtil.exportToResponse(exampleData, PostProcessingLeduvglitter.class, fileName, "絲印LED UV灑閃粉配置導入模板");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 批量更新兼容性状态
     */
    @PutMapping("/batch/status")
    public ResponseEntity<Map<String, Object>> batchUpdateStatus(
            @RequestBody Map<String, Object> request) {
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
            
            // 验证状态值
            String status = compatibilityStatus.trim();
            if (!status.equals("V") && !status.equals("X") && !status.equals("NA") && !status.equals("▷")) {
                response.put("success", false);
                response.put("message", "兼容性狀態值無效，必須為 V、X、NA 或 ▷ 之一");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 批量更新
            leduvglitterService.batchUpdateStatus(ids, status);
            
            response.put("success", true);
            response.put("message", String.format("成功更新 %d 條記錄", ids.size()));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量更新失敗: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}