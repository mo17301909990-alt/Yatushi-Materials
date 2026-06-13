package com.it.yts_project.controller;

import com.it.yts_project.dto.LaminationCompatibilityExportDTO;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.model.LaminationCompatibility;
import com.it.yts_project.model.LaminationMaterialOption;
import com.it.yts_project.model.PostProcessingOption;
import com.it.yts_project.model.Products;
import com.it.yts_project.model.Product;
import com.it.yts_project.service.LaminatingService;
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
import java.util.stream.Collectors;

/**
 * 过胶管理Controller
 */
@RestController
@RequestMapping("/api/laminating")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class LaminatingController {
    
    @Autowired
    private LaminatingService laminatingService;
    
    @Autowired
    private ProductsMapper productsMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    // ========== 过胶材料选项管理 ==========
    
    /**
     * 获取所有过胶材料选项
     */
    @GetMapping("/options/materials")
    public ResponseEntity<List<LaminationMaterialOption>> getAllMaterialOptions() {
        List<LaminationMaterialOption> options = laminatingService.getActiveMaterialOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 根据ID获取过胶材料选项
     */
    @GetMapping("/options/materials/{id}")
    public ResponseEntity<LaminationMaterialOption> getMaterialOptionById(@PathVariable Integer id) {
        LaminationMaterialOption option = laminatingService.getMaterialOptionById(id);
        if (option != null) {
            return ResponseEntity.ok(option);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 保存过胶材料选项
     */
    @PostMapping("/options/materials")
    public ResponseEntity<LaminationMaterialOption> saveMaterialOption(@RequestBody LaminationMaterialOption option) {
        LaminationMaterialOption savedOption = laminatingService.saveMaterialOption(option);
        return ResponseEntity.ok(savedOption);
    }
    
    /**
     * 更新过胶材料选项
     */
    @PutMapping("/options/materials/{id}")
    public ResponseEntity<LaminationMaterialOption> updateMaterialOption(@PathVariable Integer id, @RequestBody LaminationMaterialOption option) {
        option.setId(id);
        LaminationMaterialOption updatedOption = laminatingService.saveMaterialOption(option);
        return ResponseEntity.ok(updatedOption);
    }
    
    /**
     * 删除过胶材料选项
     */
    @DeleteMapping("/options/materials/{id}")
    public ResponseEntity<Void> deleteMaterialOption(@PathVariable Integer id) {
        laminatingService.deleteMaterialOption(id);
        return ResponseEntity.ok().build();
    }
    
    // ========== 后处理工序选项管理 ==========
    
    /**
     * 获取所有后处理工序选项
     */
    @GetMapping("/options/steps")
    public ResponseEntity<List<PostProcessingOption>> getAllProcessingOptions() {
        List<PostProcessingOption> options = laminatingService.getActiveProcessingOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 根据工序名称获取后处理工序选项
     */
    @GetMapping("/options/steps/by-name/{stepName}")
    public ResponseEntity<PostProcessingOption> getProcessingOptionByStepName(@PathVariable String stepName) {
        PostProcessingOption option = laminatingService.getProcessingOptionByStepName(stepName);
        if (option != null) {
            return ResponseEntity.ok(option);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 保存后处理工序选项
     */
    @PostMapping("/options/steps")
    public ResponseEntity<PostProcessingOption> saveProcessingOption(@RequestBody PostProcessingOption option) {
        PostProcessingOption savedOption = laminatingService.saveProcessingOption(option);
        return ResponseEntity.ok(savedOption);
    }
    
    /**
     * 更新后处理工序选项
     */
    @PutMapping("/options/steps/{id}")
    public ResponseEntity<PostProcessingOption> updateProcessingOption(@PathVariable Integer id, @RequestBody PostProcessingOption option) {
        option.setId(id);
        PostProcessingOption updatedOption = laminatingService.saveProcessingOption(option);
        return ResponseEntity.ok(updatedOption);
    }
    
    /**
     * 删除后处理工序选项
     */
    @DeleteMapping("/options/steps/{id}")
    public ResponseEntity<Void> deleteProcessingOption(@PathVariable Integer id) {
        laminatingService.deleteProcessingOption(id);
        return ResponseEntity.ok().build();
    }
    
    // ========== 过胶兼容性管理 ==========
    
    /**
     * 获取过胶兼容性列表（带分页）
     */
    @GetMapping("/compatibility")
    public ResponseEntity<Map<String, Object>> getCompatibilityList(
            @RequestParam(required = false) String foilSeries,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String modelNumber,
            @RequestParam(required = false) Integer postProcessingStepId,
            @RequestParam(required = false) Integer laminationMaterialId,
            @RequestParam(required = false) String compatibility,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        
        List<LaminationCompatibility> list = laminatingService.getCompatibilityList(
            foilSeries, productType, modelNumber, postProcessingStepId, 
            laminationMaterialId, compatibility, page, size);
        
        int total = laminatingService.countCompatibility(
            foilSeries, productType, modelNumber, postProcessingStepId, 
            laminationMaterialId, compatibility);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        result.put("totalPages", (total + size - 1) / size);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 根据ID获取过胶兼容性
     */
    @GetMapping("/compatibility/{id}")
    public ResponseEntity<LaminationCompatibility> getCompatibilityById(@PathVariable Integer id) {
        LaminationCompatibility compatibility = laminatingService.getCompatibilityById(id);
        if (compatibility != null) {
            return ResponseEntity.ok(compatibility);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 保存过胶兼容性
     */
    @PostMapping("/compatibility")
    public ResponseEntity<?> saveCompatibility(@RequestBody LaminationCompatibility compatibility) {
        try {
        LaminationCompatibility savedCompatibility = laminatingService.saveCompatibility(compatibility);
        return ResponseEntity.ok(savedCompatibility);
        } catch (IllegalArgumentException e) {
            // 处理唯一性验证错误
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // 处理其他异常
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "保存失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新过胶兼容性
     */
    @PutMapping("/compatibility/{id}")
    public ResponseEntity<?> updateCompatibility(@PathVariable Integer id, @RequestBody LaminationCompatibility compatibility) {
        try {
        compatibility.setId(id);
        LaminationCompatibility updatedCompatibility = laminatingService.saveCompatibility(compatibility);
        return ResponseEntity.ok(updatedCompatibility);
        } catch (IllegalArgumentException e) {
            // 处理唯一性验证错误
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // 处理其他异常
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 批量保存过胶兼容性
     */
    @PostMapping("/compatibility/batch")
    public ResponseEntity<Void> batchSaveCompatibility(@RequestBody List<LaminationCompatibility> compatibilities) {
        laminatingService.batchSaveCompatibility(compatibilities);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 删除过胶兼容性
     */
    @DeleteMapping("/compatibility/{id}")
    public ResponseEntity<Void> deleteCompatibility(@PathVariable Integer id) {
        laminatingService.deleteCompatibility(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 批量删除过胶兼容性
     */
    @DeleteMapping("/compatibility/batch")
    public ResponseEntity<?> batchDeleteCompatibility(@RequestBody List<Integer> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "请选择要删除的记录");
                return ResponseEntity.badRequest().body(response);
            }
            
            laminatingService.batchDeleteCompatibility(ids);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", String.format("成功删除 %d 条记录", ids.size()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // ========== 选项数据获取 ==========
    
    /**
     * 获取所有产品系列名称
     */
    @GetMapping("/options/foil-series")
    public ResponseEntity<List<String>> getAllFoilSeries() {
        List<String> foilSeries = laminatingService.getAllFoilSeries();
        return ResponseEntity.ok(foilSeries);
    }
    
    /**
     * 获取所有产品类型
     */
    @GetMapping("/options/product-types")
    public ResponseEntity<List<String>> getAllProductTypes() {
        List<String> productTypes = laminatingService.getAllProductTypes();
        return ResponseEntity.ok(productTypes);
    }
    
    /**
     * 获取所有型号
     */
    @GetMapping("/options/model-numbers")
    public ResponseEntity<List<String>> getAllModelNumbers() {
        List<String> modelNumbers = laminatingService.getAllModelNumbers();
        return ResponseEntity.ok(modelNumbers);
    }
    
    /**
     * 获取"过胶"工序ID
     */
    @GetMapping("/options/laminating-step-id")
    public ResponseEntity<Map<String, Object>> getLaminatingStepId() {
        Integer stepId = laminatingService.getLaminatingStepId();
        Map<String, Object> result = new HashMap<>();
        result.put("stepId", stepId);
        return ResponseEntity.ok(result);
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出过胶兼容性数据到Excel
     */
    @GetMapping("/compatibility/export")
    public ResponseEntity<byte[]> exportCompatibilityToExcel(
            @RequestParam(required = false) String foilSeries,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String modelNumber,
            @RequestParam(required = false) Integer postProcessingStepId,
            @RequestParam(required = false) Integer laminationMaterialId,
            @RequestParam(required = false) String compatibility) {
        
        try {
            // 获取所有符合条件的数据（不分页）
            List<LaminationCompatibility> allData = laminatingService.getCompatibilityList(
                foilSeries, productType, modelNumber, postProcessingStepId, 
                laminationMaterialId, compatibility, 1, Integer.MAX_VALUE);
            
            // 处理isJiehuo字段，设置文本值用于导出
            for (LaminationCompatibility item : allData) {
                item.setIsJiehuoText(item.getIsJiehuoText());
            }
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("過膠兼容性配置");
            
            // 导出Excel（直接使用模型类，因为已添加Excel注解）
            return ExcelExportUtil.exportToResponse(allData, LaminationCompatibility.class, fileName, "過膠兼容性配置");
            
        } catch (Exception e) {
            // 返回错误响应
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导出过胶材料选项数据到Excel
     */
    @GetMapping("/options/materials/export")
    public ResponseEntity<byte[]> exportMaterialOptionsToExcel() {
        try {
            List<LaminationMaterialOption> allData = laminatingService.getAllMaterialOptions();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("过胶材料选项");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, LaminationMaterialOption.class, fileName, "过胶材料选项");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导出后处理工序选项数据到Excel
     */
    @GetMapping("/options/steps/export")
    public ResponseEntity<byte[]> exportProcessingOptionsToExcel() {
        try {
            List<PostProcessingOption> allData = laminatingService.getAllProcessingOptions();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("后处理工序选项");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, PostProcessingOption.class, fileName, "后处理工序选项");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导入过胶兼容性配置
     */
    @PostMapping("/compatibility/import")
    public ResponseEntity<Map<String, Object>> importCompatibility(
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
            com.alibaba.excel.EasyExcel.read(file.getInputStream(), LaminationCompatibility.class,
                new com.alibaba.excel.read.listener.ReadListener<LaminationCompatibility>() {
                    @Override
                    public void invoke(LaminationCompatibility data, com.alibaba.excel.context.AnalysisContext context) {
                        totalCount.incrementAndGet();
                        try {
                            int rowIndex = context.readRowHolder().getRowIndex() + 1;
                            
                            // 验证必填字段：產品系列
                            if (data.getFoilSeries() == null || data.getFoilSeries().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 產品系列不能為空", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            String foilSeries = data.getFoilSeries().trim();
                            
                            // 验证產品系列是否存在
                            List<Products> products = productsMapper.getProductsBySeriesName(foilSeries);
                            if (products == null || products.isEmpty()) {
                                errorMessages.add(String.format("第%d行: 產品系列「%s」不存在", rowIndex, foilSeries));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证产品型号（如果提供）
                            String modelNumber = data.getModelNumber() != null && !data.getModelNumber().trim().isEmpty()
                                ? data.getModelNumber().trim() : null;
                            
                            if (modelNumber != null) {
                                // 首先验证产品型号是否在系统中存在
                                List<Product> productsByModel = productMapper.findByModelNumber(modelNumber);
                                if (productsByModel == null || productsByModel.isEmpty()) {
                                    errorMessages.add(String.format("第%d行: 產品型號「%s」在系統中不存在", rowIndex, modelNumber));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                // 然后验证产品型号是否在该系列中存在
                                boolean modelExistsInSeries = products.stream()
                                    .anyMatch(p -> modelNumber.equals(p.getModel_number()));
                                if (!modelExistsInSeries) {
                                    errorMessages.add(String.format("第%d行: 產品型號「%s」在產品系列「%s」中不存在", rowIndex, modelNumber, foilSeries));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 验证必填字段：產品類型
                            if (data.getProductType() == null || data.getProductType().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 產品類型不能為空", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            String productType = data.getProductType().trim();
                            
                            // 验证產品類型是否存在（验证products表中是否存在该hot_stamping_paper_type）
                            List<String> seriesByProductType = productsMapper.getSeriesNamesByPaperType(productType);
                            if (seriesByProductType == null || seriesByProductType.isEmpty()) {
                                errorMessages.add(String.format("第%d行: 產品類型「%s」在系統中不存在", rowIndex, productType));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证该系列是否支持该productType
                            boolean supportsProductType = productsMapper.checkSeriesSupportsPaperType(foilSeries, productType);
                            if (!supportsProductType) {
                                errorMessages.add(String.format("第%d行: 產品系列「%s」不支持產品類型「%s」", rowIndex, foilSeries, productType));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证必填字段：過膠材料
                            if (data.getLaminationMaterialName() == null || data.getLaminationMaterialName().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 過膠材料不能為空", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            String laminationMaterialName = data.getLaminationMaterialName().trim();
                            
                            // 根据材料名称查找ID
                            List<LaminationMaterialOption> materialOptions = laminatingService.getActiveMaterialOptions();
                            LaminationMaterialOption materialOption = materialOptions.stream()
                                .filter(m -> laminationMaterialName.equals(m.getMaterialName()))
                                .findFirst()
                                .orElse(null);
                            
                            if (materialOption == null) {
                                errorMessages.add(String.format("第%d行: 過膠材料「%s」不存在", rowIndex, laminationMaterialName));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            Integer resolvedLaminationMaterialId = materialOption.getId();
                            
                            // 验证过胶后工藝（如果提供）
                            Integer resolvedPostProcessingStepId = null;
                            if (data.getPostProcessingStepName() != null && !data.getPostProcessingStepName().trim().isEmpty()) {
                                String postProcessingStepName = data.getPostProcessingStepName().trim();
                                
                                // 根据工序名称查找ID
                                PostProcessingOption stepOption = laminatingService.getProcessingOptionByStepName(postProcessingStepName);
                                if (stepOption == null) {
                                    errorMessages.add(String.format("第%d行: 過膠後工藝「%s」不存在", rowIndex, postProcessingStepName));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                resolvedPostProcessingStepId = stepOption.getId();
                            }
                            
                            // 验证必填字段：兼容性狀態
                            if (data.getCompatibility() == null || data.getCompatibility().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 兼容性狀態不能為空", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证兼容性狀態的值是否有效
                            String compatibility = data.getCompatibility().trim();
                            if (!compatibility.equals("V") && !compatibility.equals("X") && !compatibility.equals("▷")) {
                                errorMessages.add(String.format("第%d行: 兼容性狀態值無效，必須為 V（兼容）、X（不兼容）或 ▷（有條件兼容）之一", rowIndex));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 处理街货标识（通过setIsJiehuoText方法自动转换）
                            if (data.getIsJiehuoText() != null) {
                                data.setIsJiehuoText(data.getIsJiehuoText());
                            } else {
                                data.setIsJiehuo(false);
                            }
                            
                            // 检查是否已存在（根据唯一键）
                            LaminationCompatibility existing = laminatingService.findByUniqueKeyNew(
                                foilSeries,
                                modelNumber,
                                productType,
                                resolvedLaminationMaterialId,
                                resolvedPostProcessingStepId
                            );
                            
                            // 创建或更新记录
                            LaminationCompatibility toSave = new LaminationCompatibility();
                            if (existing != null) {
                                // 更新现有记录
                                toSave.setId(existing.getId());
                                toSave.setFoilSeries(foilSeries);
                                toSave.setModelNumber(modelNumber);
                                toSave.setProductType(productType);
                                toSave.setLaminationMaterialId(resolvedLaminationMaterialId);
                                toSave.setPostProcessingStepId(resolvedPostProcessingStepId);
                                toSave.setCompatibility(compatibility);
                                toSave.setIsJiehuo(data.getIsJiehuo()); // 使用data中已转换的Boolean值
                                toSave.setInterfaceTypeId(0); // 废弃字段，固定为0
                                laminatingService.saveCompatibility(toSave);
                            } else {
                                // 创建新记录
                                toSave.setFoilSeries(foilSeries);
                                toSave.setModelNumber(modelNumber);
                                toSave.setProductType(productType);
                                toSave.setLaminationMaterialId(resolvedLaminationMaterialId);
                                toSave.setPostProcessingStepId(resolvedPostProcessingStepId);
                                toSave.setCompatibility(compatibility);
                                toSave.setIsJiehuo(data.getIsJiehuo()); // 使用data中已转换的Boolean值
                                toSave.setInterfaceTypeId(0); // 废弃字段，固定为0
                                laminatingService.saveCompatibility(toSave);
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
    @GetMapping("/compatibility/import-template")
    public ResponseEntity<byte[]> downloadImportTemplate() {
        try {
            // 创建示例数据
            List<LaminationCompatibility> exampleData = new ArrayList<>();
            LaminationCompatibility example = new LaminationCompatibility();
            example.setFoilSeries("示例系列");
            example.setModelNumber("示例型号");
            example.setProductType("普通金紙");
            example.setLaminationMaterialName("普通預塗菲林-光膠");
            example.setPostProcessingStepName("無任何加工");
            example.setCompatibility("V");
            example.setIsJiehuoText("否");
            exampleData.add(example);
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("過膠兼容性配置導入模板");
            
            // 导出Excel模板
            return ExcelExportUtil.exportToResponse(exampleData, LaminationCompatibility.class, fileName, "過膠兼容性配置導入模板");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 批量更新兼容性状态
     */
    @PutMapping("/compatibility/batch/status")
    public ResponseEntity<Map<String, Object>> batchUpdateStatus(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            @SuppressWarnings("unchecked")
            List<Integer> ids = (List<Integer>) request.get("ids");
            String compatibility = (String) request.get("compatibility");
            
            if (ids == null || ids.isEmpty()) {
                response.put("success", false);
                response.put("message", "請選擇要更新的記錄");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (compatibility == null || compatibility.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "兼容性狀態不能為空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证兼容性狀態的值是否有效
            String status = compatibility.trim();
            if (!status.equals("V") && !status.equals("X") && !status.equals("▷")) {
                response.put("success", false);
                response.put("message", "兼容性狀態值無效，必須為 V（兼容）、X（不兼容）或 ▷（有條件兼容）之一");
                return ResponseEntity.badRequest().body(response);
            }
            
            laminatingService.batchUpdateStatus(ids, status);
            
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
    @GetMapping("/compatibility/check-unique")
    public ResponseEntity<LaminationCompatibility> checkUnique(
        @RequestParam String foilSeries,
        @RequestParam(required = false) String modelNumber,
        @RequestParam String productType,
        @RequestParam Integer laminationMaterialId,
        @RequestParam(required = false) Integer postProcessingStepId
    ) {
        LaminationCompatibility existing = laminatingService.findByUniqueKeyNew(
            foilSeries,
            modelNumber,
            productType,
            laminationMaterialId,
            postProcessingStepId
        );
        if (existing != null) {
            return ResponseEntity.ok(existing);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 将LaminationCompatibility转换为导出DTO
     */
    private LaminationCompatibilityExportDTO convertToExportDTO(LaminationCompatibility compatibility) {
        return LaminationCompatibilityExportDTO.builder()
                .id(compatibility.getId())
                .foilSeries(compatibility.getFoilSeries())
                .modelNumber(compatibility.getModelNumber())
                .productType(compatibility.getProductType())
                .laminationMaterialName(compatibility.getLaminationMaterialName())
                .postProcessingStepName(compatibility.getPostProcessingStepName())
                .compatibility(compatibility.getCompatibility())
                .isJiehuo(compatibility.getIsJiehuo() != null && compatibility.getIsJiehuo() ? "是" : "否")
                .createdAt(compatibility.getCreatedAt())
                .updatedAt(compatibility.getUpdatedAt())
                .build();
    }
}
