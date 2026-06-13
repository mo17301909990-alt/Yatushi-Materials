package com.it.yts_project.controller;

import com.it.yts_project.dto.HotStampingPatternAreaOptionDTO;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.model.HotStampingPatternAreaCompatibility;
import com.it.yts_project.service.HotStampingPatternAreaCompatibilityService;
import com.it.yts_project.service.HotStampingPatternAreaOptionService;
import com.it.yts_project.util.ExcelExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 烫金图案区域兼容性控制器
 */
@RestController
@RequestMapping("/api/hot-stamping-pattern-area-compatibility")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class HotStampingPatternAreaCompatibilityController {
    
    @Autowired
    private HotStampingPatternAreaCompatibilityService compatibilityService;
    
    @Autowired
    private HotStampingPatternAreaOptionService patternAreaOptionService;
    
    @Autowired
    private ProductsMapper productsMapper;
    
    /**
     * 获取所有兼容性配置列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<HotStampingPatternAreaCompatibility>> getAllCompatibility() {
        try {
            List<HotStampingPatternAreaCompatibility> compatibilities = 
                compatibilityService.getAllCompatibility();
            return ResponseEntity.ok(compatibilities);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 根据图案区域ID获取兼容性配置列表
     */
    @GetMapping("/pattern-area/{patternAreaId}")
    public ResponseEntity<List<HotStampingPatternAreaCompatibility>> getCompatibilityByPatternAreaId(
            @PathVariable Integer patternAreaId) {
        try {
            List<HotStampingPatternAreaCompatibility> compatibilities = 
                compatibilityService.getCompatibilityByPatternAreaId(patternAreaId);
            return ResponseEntity.ok(compatibilities);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 根据ID获取兼容性配置
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotStampingPatternAreaCompatibility> getCompatibilityById(@PathVariable Integer id) {
        try {
            HotStampingPatternAreaCompatibility compatibility = compatibilityService.getCompatibilityById(id);
            if (compatibility != null) {
                return ResponseEntity.ok(compatibility);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 创建兼容性配置
     */
    @PostMapping
    public ResponseEntity<?> createCompatibility(
            @RequestBody HotStampingPatternAreaCompatibility compatibility) {
        try {
            HotStampingPatternAreaCompatibility created = compatibilityService.createCompatibility(compatibility);
            return ResponseEntity.ok(created);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("组合已存在")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", errorMessage));
            }
            return ResponseEntity.badRequest()
                .body(Map.of("error", "该燙金紙系列、图案区域和烫金纸性能类型的组合已存在，请选择其他组合"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "创建失败：" + e.getMessage()));
        }
    }
    
    /**
     * 更新兼容性配置
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompatibility(
            @PathVariable Integer id,
            @RequestBody HotStampingPatternAreaCompatibility compatibility) {
        try {
            compatibility.setId(id);
            HotStampingPatternAreaCompatibility updated = compatibilityService.updateCompatibility(compatibility);
            return ResponseEntity.ok(updated);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("组合已存在")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", errorMessage));
            }
            return ResponseEntity.badRequest()
                .body(Map.of("error", "该燙金紙系列、图案区域和烫金纸性能类型的组合已存在，请选择其他组合"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("error", "更新失败：" + e.getMessage()));
        }
    }
    
    /**
     * 删除兼容性配置
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        try {
            compatibilityService.deleteCompatibility(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 根据图案区域ID删除所有兼容性配置
     */
    @DeleteMapping("/pattern-area/{patternAreaId}")
    public ResponseEntity<Void> deleteCompatibilityByPatternAreaId(@PathVariable Integer patternAreaId) {
        try {
            compatibilityService.deleteCompatibilityByPatternAreaId(patternAreaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 获取所有可用的燙金紙系列
     */
    @GetMapping("/product-names")
    public ResponseEntity<List<String>> getAllProductNames() {
        try {
            List<String> productNames = compatibilityService.getAllProductNames();
            return ResponseEntity.ok(productNames);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导出兼容性配置到Excel
     * @param patternAreaId 图案区域ID（可选）
     * @return Excel文件
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCompatibilityToExcel(@RequestParam(required = false) Integer patternAreaId) {
        try {
            List<HotStampingPatternAreaCompatibility> allData;
            
            if (patternAreaId != null) {
                // 根据图案区域ID获取数据
                allData = compatibilityService.getCompatibilityByPatternAreaId(patternAreaId);
            } else {
                // 获取所有数据
                allData = compatibilityService.getAllCompatibility();
            }
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("燙金圖案區域映射配置");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, HotStampingPatternAreaCompatibility.class, fileName, "燙金圖案區域映射配置");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导入兼容性配置
     * @param file Excel文件
     * @param patternAreaId 图案区域ID（可选）
     * @return 导入结果
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importCompatibility(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Integer patternAreaId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                response.put("success", false);
                response.put("message", "文件不能為空");
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
            com.alibaba.excel.EasyExcel.read(file.getInputStream(), HotStampingPatternAreaCompatibility.class,
                new com.alibaba.excel.read.listener.ReadListener<HotStampingPatternAreaCompatibility>() {
                    @Override
                    public void invoke(HotStampingPatternAreaCompatibility data, com.alibaba.excel.context.AnalysisContext context) {
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
                            
                            // 根据 patternType 查找 patternAreaId
                            Integer resolvedPatternAreaId = patternAreaId;
                            if (resolvedPatternAreaId == null) {
                                // 验证圖案类型
                                if (data.getPatternType() == null || data.getPatternType().trim().isEmpty()) {
                                    errorMessages.add(String.format("第%d行: 圖案类型不能為空", rowIndex));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                // 查找對應的燙金圖案區域
                                HotStampingPatternAreaOptionDTO patternArea = patternAreaOptionService.getPatternAreaOptionByPatternType(
                                    data.getPatternType().trim());
                                if (patternArea != null) {
                                    resolvedPatternAreaId = patternArea.getId();
                                } else {
                                    errorMessages.add(String.format("第%d行: 找不到對應的燙金圖案區域（圖案类型: %s）", 
                                            rowIndex, data.getPatternType().trim()));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            } else {
                                // 如果传入了 patternAreaId，验证 Excel 中的图案类型是否匹配
                                HotStampingPatternAreaOptionDTO patternAreaById = patternAreaOptionService.getPatternAreaOptionById(resolvedPatternAreaId);
                                if (patternAreaById == null) {
                                    errorMessages.add(String.format("第%d行: 燙金圖案區域ID「%d」不存在", rowIndex, resolvedPatternAreaId));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                // 如果 Excel 中提供了图案类型，验证是否与传入的 ID 匹配
                                if (data.getPatternType() != null && !data.getPatternType().trim().isEmpty()) {
                                    String excelPatternType = data.getPatternType().trim();
                                    if (!excelPatternType.equals(patternAreaById.getPatternType())) {
                                        errorMessages.add(String.format("第%d行: Excel中的圖案类型「%s」與指定的燙金圖案區域ID「%d」（圖案类型: %s）不匹配", 
                                                rowIndex, 
                                                excelPatternType,
                                                resolvedPatternAreaId,
                                                patternAreaById.getPatternType()));
                                        failCount.incrementAndGet();
                                        return;
                                    }
                                }
                            }
                            
                            // 設置值
                            data.setProductName(productName);
                            data.setCompatibilityStatus(status);
                            
                            // 如果 paperType 不为空，进行 trim 并验证
                            String paperType = null;
                            if (data.getPaperType() != null && !data.getPaperType().trim().isEmpty()) {
                                paperType = data.getPaperType().trim();
                                data.setPaperType(paperType);
                                
                                // 验证该系列是否支持该 paperType
                                boolean supportsPaperType = productsMapper.checkSeriesSupportsPaperType(
                                    productName, paperType);
                                
                                if (!supportsPaperType) {
                                    errorMessages.add(String.format("第%d行: 燙金紙系列「%s」不支持燙金紙性能類型「%s」", 
                                            rowIndex, productName, paperType));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            data.setHotStampingPatternxAreaId(resolvedPatternAreaId);
                            
                            // 检查是否已存在（根据燙金紙系列、燙金圖案區域ID和燙金紙性能類型）
                            HotStampingPatternAreaCompatibility existing = compatibilityService
                                .getCompatibilityByProductNameAndPatternAreaIdAndPaperType(
                                    productName, resolvedPatternAreaId, paperType);
                            
                            if (existing != null) {
                                // 已存在，更新
                                existing.setCompatibilityStatus(data.getCompatibilityStatus());
                                existing.setPaperType(data.getPaperType());
                                compatibilityService.updateCompatibility(existing);
                                successCount.incrementAndGet();
                            } else {
                                // 不存在，创建
                                compatibilityService.createCompatibility(data);
                                successCount.incrementAndGet();
                            }
                        } catch (Exception e) {
                            int rowIndex = context.readRowHolder().getRowIndex() + 1;
                            errorMessages.add(String.format("第%d行: %s", rowIndex, e.getMessage()));
                            failCount.incrementAndGet();
                        }
                    }
                    
                    @Override
                    public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext context) {
                        // 所有数据读取完成
                    }
                }).sheet().doRead();
            
            response.put("success", failCount.get() == 0);
            response.put("message", String.format("導入完成！總計：%d 條，成功：%d 條，失敗：%d 條", 
                    totalCount.get(), successCount.get(), failCount.get()));
            if (!errorMessages.isEmpty()) {
                response.put("details", errorMessages);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "導入失敗：" + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 下载导入模板
     * @return Excel模板文件
     */
    @GetMapping("/import-template")
    public ResponseEntity<byte[]> downloadImportTemplate() {
        try {
            // 创建示例数据
            List<HotStampingPatternAreaCompatibility> templateData = new ArrayList<>();
            HotStampingPatternAreaCompatibility example = new HotStampingPatternAreaCompatibility();
            example.setProductName("示例系列");
            example.setCompatibilityStatus("V");
            example.setPaperType("普通金紙");
            example.setPatternType("示例圖案类型");
            templateData.add(example);
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("燙金圖案區域映射配置導入模板");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(templateData, HotStampingPatternAreaCompatibility.class, fileName, "映射配置導入模板");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
