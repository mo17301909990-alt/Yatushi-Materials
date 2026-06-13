package com.it.yts_project.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.model.WearResistantGoldPaperMapping;
import com.it.yts_project.model.Products;
import com.it.yts_project.model.Product;
import com.it.yts_project.service.WearResistantGoldPaperMappingService;
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
 * 耐磨金纸映射控制器
 */
@RestController
@RequestMapping("/api/wear-resistant-gold-paper-mapping")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class WearResistantGoldPaperMappingController {
    
    @Autowired
    private WearResistantGoldPaperMappingService mappingService;
    
    @Autowired
    private ProductsMapper productsMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    /**
     * 获取所有映射
     */
    @GetMapping
    public ResponseEntity<List<WearResistantGoldPaperMapping>> getAllMappings() {
        List<WearResistantGoldPaperMapping> mappings = mappingService.getAllMappings();
        return ResponseEntity.ok(mappings);
    }
    
    /**
     * 分页获取映射
     */
    @GetMapping("/pagination")
    public ResponseEntity<Map<String, Object>> getMappingsWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<WearResistantGoldPaperMapping> mappings = mappingService.getMappingsWithPagination(page, size);
        int totalCount = mappingService.getTotalCount();
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", mappings);
        response.put("totalElements", totalCount);
        response.put("number", page - 1);
        response.put("size", size);
        response.put("totalPages", (int) Math.ceil((double) totalCount / size));
        response.put("first", page == 1);
        response.put("last", page >= (int) Math.ceil((double) totalCount / size));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 根据ID获取映射
     */
    @GetMapping("/{id}")
    public ResponseEntity<WearResistantGoldPaperMapping> getMappingById(@PathVariable Integer id) {
        WearResistantGoldPaperMapping mapping = mappingService.getMappingById(id);
        if (mapping != null) {
            return ResponseEntity.ok(mapping);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 创建映射
     */
    @PostMapping
    public ResponseEntity<?> createMapping(@RequestBody WearResistantGoldPaperMapping mapping) {
        try {
            // 数据校验
            String error = validateMapping(mapping, false);
            if (error != null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", error);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // 检查唯一性
            WearResistantGoldPaperMapping existing = mappingService.findByUniqueKey(
                mapping.getProductName(),
                mapping.getProductModelNumber(),
                mapping.getGoldPaperType()
            );
            if (existing != null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "该映射已存在，请勿重复添加");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            WearResistantGoldPaperMapping created = mappingService.createMapping(mapping);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", created);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 更新映射
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMapping(
            @PathVariable Integer id,
            @RequestBody WearResistantGoldPaperMapping mapping) {
        try {
            mapping.setId(id);
            
            // 数据校验
            String error = validateMapping(mapping, true);
            if (error != null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", error);
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // 检查唯一性（排除当前记录）
            WearResistantGoldPaperMapping existing = mappingService.findByUniqueKey(
                mapping.getProductName(),
                mapping.getProductModelNumber(),
                mapping.getGoldPaperType()
            );
            if (existing != null && !existing.getId().equals(id)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "该映射已存在，请修改唯一键字段");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            WearResistantGoldPaperMapping updated = mappingService.updateMapping(mapping);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * 删除映射
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMapping(@PathVariable Integer id) {
        mappingService.deleteMapping(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 批量删除映射
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchDeleteMappings(@RequestBody List<Integer> ids) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (ids == null || ids.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择要删除的记录");
                return ResponseEntity.badRequest().body(response);
            }
            
            mappingService.batchDeleteMappings(ids);
            
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
     * 搜索（支持多条件）
     */
    @GetMapping("/search")
    public ResponseEntity<List<WearResistantGoldPaperMapping>> search(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productModelNumber,
            @RequestParam(required = false) String goldPaperType) {
        List<WearResistantGoldPaperMapping> mappings = mappingService.search(productName, productModelNumber, goldPaperType);
        return ResponseEntity.ok(mappings);
    }
    
    /**
     * 检查唯一性
     */
    @GetMapping("/check-unique")
    public ResponseEntity<Map<String, Object>> checkUnique(
            @RequestParam String productName,
            @RequestParam(required = false) String productModelNumber,
            @RequestParam String goldPaperType) {
        WearResistantGoldPaperMapping existing = mappingService.findByUniqueKey(
            productName, productModelNumber, goldPaperType
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("exists", existing != null);
        if (existing != null) {
            response.put("data", existing);
        }
        return ResponseEntity.ok(response);
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
                response.put("message", "请选择要更新的记录");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (compatibilityStatus == null || compatibilityStatus.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "兼容性状态不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证兼容性状态值
            String status = compatibilityStatus.trim();
            if (!status.equals("V") && !status.equals("X") && !status.equals("NA") && !status.equals("▷")) {
                response.put("success", false);
                response.put("message", "兼容性状态值无效，必须为 V、X、NA 或 ▷ 之一");
                return ResponseEntity.badRequest().body(response);
            }
            
            mappingService.batchUpdateStatus(ids, status);
            
            response.put("success", true);
            response.put("message", String.format("成功更新 %d 条记录", ids.size()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "批量更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取所有可用的耐磨金纸类型
     */
    @GetMapping("/options/gold-paper-types")
    public ResponseEntity<List<String>> getAllGoldPaperTypes() {
        List<String> types = mappingService.getAllGoldPaperTypes();
        return ResponseEntity.ok(types);
    }
    
    /**
     * 导出映射到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportMappingsToExcel() {
        try {
            List<WearResistantGoldPaperMapping> allData = mappingService.getAllMappings();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("耐磨金纸映射");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, WearResistantGoldPaperMapping.class, fileName, "耐磨金纸映射");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 下载导入模板
     */
    @GetMapping("/import-template")
    public ResponseEntity<byte[]> downloadImportTemplate() {
        try {
            // 创建示例数据
            List<WearResistantGoldPaperMapping> templateData = new ArrayList<>();
            WearResistantGoldPaperMapping example = new WearResistantGoldPaperMapping();
            example.setProductName("示例产品系列");
            example.setProductModelNumber("示例型号（可选）");
            example.setHotStampingType("燙金後擊凸");
            example.setGoldPaperType("示例耐磨金纸类型");
            example.setCompatibilityStatus("V");
            example.setRemarks("示例备注");
            templateData.add(example);
            
            // 生成文件名
            String fileName = "耐磨金纸映射导入模板";
            
            // 导出Excel模板
            return ExcelExportUtil.exportToResponse(templateData, WearResistantGoldPaperMapping.class, fileName, "模板");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导入映射
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importMappings(
            @RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择要导入的文件");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                response.put("success", false);
                response.put("message", "文件格式不正确，请上传Excel文件");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 读取Excel并导入数据
            List<String> errorMessages = new ArrayList<>();
            AtomicInteger totalCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);
            
            // 使用EasyExcel读取数据
            EasyExcel.read(file.getInputStream(), WearResistantGoldPaperMapping.class,
                new ReadListener<WearResistantGoldPaperMapping>() {
                    @Override
                    public void invoke(WearResistantGoldPaperMapping data, AnalysisContext context) {
                        totalCount.incrementAndGet();
                        try {
                            int rowIndex = context.readRowHolder().getRowIndex() + 1;
                            
                            // 验证必填字段：燙金紙系列
                            if (data.getProductName() == null || data.getProductName().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 燙金紙系列不能为空", rowIndex));
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
                                // 验证产品型号是否存在
                                List<Product> productsByModel = productMapper.findByModelNumber(productModelNumber);
                                if (productsByModel == null || productsByModel.isEmpty()) {
                                    errorMessages.add(String.format("第%d行: 产品型号「%s」不存在", rowIndex, productModelNumber));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                // 验证产品型号是否属于该系列
                                boolean modelExistsInSeries = products.stream()
                                    .anyMatch(p -> productModelNumber.equals(p.getModel_number()));
                                if (!modelExistsInSeries) {
                                    errorMessages.add(String.format("第%d行: 产品型号「%s」不属于燙金紙系列「%s」", rowIndex, productModelNumber, productName));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 验证必填字段：耐磨金纸类型
                            if (data.getGoldPaperType() == null || data.getGoldPaperType().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 耐磨金纸类型不能为空", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            String goldPaperType = data.getGoldPaperType().trim();
                            
                            // 验证必填字段：兼容性狀態
                            if (data.getCompatibilityStatus() == null || data.getCompatibilityStatus().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 兼容性狀態不能为空", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证兼容性狀態的值是否有效
                            String status = data.getCompatibilityStatus().trim();
                            if (!status.equals("V") && !status.equals("X") && !status.equals("NA") && !status.equals("▷")) {
                                errorMessages.add(String.format("第%d行: 兼容性狀態值无效，必须为 V（适用）、X（不适用）、NA（不确定）或 ▷（需要打底处理）之一", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 强制设置烫金类型为"燙金後擊凸"
                            data.setHotStampingType("燙金後擊凸");
                            
                            // 检查是否已存在（根据唯一键）
                            WearResistantGoldPaperMapping existing = mappingService.findByUniqueKey(
                                productName,
                                productModelNumber,
                                goldPaperType
                            );
                            
                            // 创建或更新记录
                            if (existing != null) {
                                // 更新现有记录
                                existing.setCompatibilityStatus(status);
                                existing.setRemarks(data.getRemarks());
                                mappingService.updateMapping(existing);
                            } else {
                                // 创建新记录
                                data.setProductName(productName);
                                data.setProductModelNumber(productModelNumber);
                                data.setGoldPaperType(goldPaperType);
                                data.setCompatibilityStatus(status);
                                mappingService.createMapping(data);
                            }
                            
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            int rowIndex = context.readRowHolder().getRowIndex() + 1;
                            errorMessages.add(String.format("第%d行: 处理失败 - %s", rowIndex, e.getMessage()));
                            failCount.incrementAndGet();
                        }
                    }
                    
                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        // 所有数据读取完成
                    }
                }).sheet().doRead();
            
            // 构建响应
            response.put("success", failCount.get() == 0);
            response.put("totalCount", totalCount.get());
            response.put("successCount", successCount.get());
            response.put("failCount", failCount.get());
            response.put("message", String.format("导入完成：成功 %d 条，失败 %d 条", successCount.get(), failCount.get()));
            
            if (!errorMessages.isEmpty()) {
                response.put("errors", errorMessages);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "导入失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 数据校验方法
     */
    private String validateMapping(WearResistantGoldPaperMapping mapping, boolean isUpdate) {
        // 验证必填字段
        if (mapping.getProductName() == null || mapping.getProductName().trim().isEmpty()) {
            return "燙金紙系列不能为空";
        }
        
        if (mapping.getGoldPaperType() == null || mapping.getGoldPaperType().trim().isEmpty()) {
            return "耐磨金纸类型不能为空";
        }
        
        if (mapping.getCompatibilityStatus() == null || mapping.getCompatibilityStatus().trim().isEmpty()) {
            return "兼容性状态不能为空";
        }
        
        // 验证兼容性状态值
        String status = mapping.getCompatibilityStatus().trim();
        if (!status.equals("V") && !status.equals("X") && !status.equals("NA") && !status.equals("▷")) {
            return "兼容性状态值无效，必须为 V（适用）、X（不适用）、NA（不确定）或 ▷（需要打底处理）之一";
        }
        
        // 验证产品系列存在性
        if (!mappingService.validateProductName(mapping.getProductName().trim())) {
            return "燙金紙系列「" + mapping.getProductName() + "」不存在";
        }
        
        // 验证产品型号（如果提供）
        if (mapping.getProductModelNumber() != null && !mapping.getProductModelNumber().trim().isEmpty()) {
            if (!mappingService.validateProductModelNumber(mapping.getProductModelNumber().trim())) {
                return "产品型号「" + mapping.getProductModelNumber() + "」不存在";
            }
            
            // 验证产品型号与系列的关联性
            if (!mappingService.validateProductModelBelongsToSeries(
                mapping.getProductName().trim(),
                mapping.getProductModelNumber().trim())) {
                return "产品型号「" + mapping.getProductModelNumber() + "」不属于燙金紙系列「" + mapping.getProductName() + "」";
            }
        }
        
        return null; // 校验通过
    }
}

