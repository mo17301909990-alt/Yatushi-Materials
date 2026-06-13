package com.it.yts_project.controller;

import com.it.yts_project.model.PreProcessingStepsOptions;
import com.it.yts_project.model.PreProcessingSteps;
import com.it.yts_project.model.Product;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.service.PreProcessingStepsOptionsService;
import com.it.yts_project.service.PreProcessingStepsService;
import com.it.yts_project.util.ExcelExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 前工序选项控制器
 */
@RestController
@RequestMapping("/api/pre-processing-steps")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class PreProcessingStepsOptionsController {
    
    @Autowired
    private PreProcessingStepsOptionsService preProcessingStepsOptionsService;
    
    @Autowired
    private PreProcessingStepsService preProcessingStepsService;
    
    @Autowired
    private ProductMapper productMapper;
    
    /**
     * 获取所有激活的前工序选项
     * @return 前工序选项列表
     */
    @GetMapping("/options")
    public ResponseEntity<List<PreProcessingStepsOptions>> getAllActiveOptions() {
        List<PreProcessingStepsOptions> options = preProcessingStepsOptionsService.getAllActiveOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 根据ID获取前工序选项
     * @param id 选项ID
     * @return 前工序选项
     */
    @GetMapping("/options/{id}")
    public ResponseEntity<PreProcessingStepsOptions> getById(@PathVariable Integer id) {
        PreProcessingStepsOptions option = preProcessingStepsOptionsService.getById(id);
        if (option != null) {
            return ResponseEntity.ok(option);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取所有前工序选项（包括未激活的）
     * @return 前工序选项列表
     */
    @GetMapping("/options/all")
    public ResponseEntity<List<PreProcessingStepsOptions>> getAllOptions() {
        List<PreProcessingStepsOptions> options = preProcessingStepsOptionsService.getAllOptions();
        return ResponseEntity.ok(options);
    }
    
    /**
     * 创建前工序选项
     * @param option 前工序选项
     * @return 创建的前工序选项
     */
    @PostMapping("/options")
    public ResponseEntity<?> create(@RequestBody PreProcessingStepsOptions option) {
        try {
            PreProcessingStepsOptions createdOption = preProcessingStepsOptionsService.create(option);
            return ResponseEntity.ok(createdOption);
        } catch (IllegalArgumentException e) {
            // 返回友好的错误信息
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // 其他异常也返回友好提示
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新前工序选项
     * @param id 选项ID
     * @param option 前工序选项
     * @return 更新后的前工序选项
     */
    @PutMapping("/options/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody PreProcessingStepsOptions option) {
        try {
            option.setId(id);
            PreProcessingStepsOptions updatedOption = preProcessingStepsOptionsService.update(option);
            return ResponseEntity.ok(updatedOption);
        } catch (IllegalArgumentException e) {
            // 返回友好的错误信息
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // 其他异常也返回友好提示
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 获取删除前工序选项前的确认信息
     * @param id 选项ID
     * @return 确认信息（包含关联映射数量）
     */
    @GetMapping("/options/{id}/delete-info")
    public ResponseEntity<Map<String, Object>> getDeleteInfo(@PathVariable Integer id) {
        try {
            PreProcessingStepsOptions option = preProcessingStepsOptionsService.getById(id);
            if (option == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "前工序选项不存在");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 检查关联的映射数量
            List<PreProcessingSteps> steps = preProcessingStepsService.getAllStepsByStepId(id);
            int mappingCount = steps != null ? steps.size() : 0;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("optionName", option.getPreProcessingStepsName());
            response.put("mappingCount", mappingCount);
            response.put("message", mappingCount > 0 
                ? String.format("删除此前工序选项将同时删除 %d 条关联映射，请确认是否继续？", mappingCount)
                : "请确认是否要删除此前工序选项？");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取删除信息失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除前工序选项
     * @param id 选项ID
     * @return 删除结果
     */
    @DeleteMapping("/options/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) {
        try {
            // 先检查是否存在关联的映射
            List<PreProcessingSteps> steps = preProcessingStepsService.getAllStepsByStepId(id);
            int mappingCount = steps != null ? steps.size() : 0;
            
            // 先删除关联的映射
            if (mappingCount > 0) {
                preProcessingStepsService.deleteByStepId(id);
            }
            
            // 再删除前工序选项
            boolean success = preProcessingStepsOptionsService.deleteById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            if (success) {
                String message = mappingCount > 0 
                    ? String.format("删除成功，已同时删除 %d 条关联映射", mappingCount)
                    : "删除成功";
                response.put("message", message);
            } else {
                response.put("message", "删除失败");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 批量更新状态
     * @param request 批量更新请求
     * @return 更新结果
     */
    @PutMapping("/options/batch-status")
    public ResponseEntity<Map<String, Object>> batchUpdateStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Integer> ids = (List<Integer>) request.get("ids");
            Boolean isActive = (Boolean) request.get("isActive");
            
            boolean success = preProcessingStepsOptionsService.batchUpdateStatus(ids, isActive);
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "批量更新成功" : "批量更新失败");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新显示顺序
     * @param id 选项ID
     * @param request 更新请求
     * @return 更新结果
     */
    @PutMapping("/options/{id}/order")
    public ResponseEntity<Map<String, Object>> updateDisplayOrder(@PathVariable Integer id, @RequestBody Map<String, Object> request) {
        try {
            Integer displayOrder = (Integer) request.get("displayOrder");
            boolean success = preProcessingStepsOptionsService.updateDisplayOrder(id, displayOrder);
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "更新顺序成功" : "更新顺序失败");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新顺序失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // ==================== PreProcessingSteps API ====================
    
    /**
     * 根据步骤ID获取所有前工序步骤
     * @param stepId 步骤ID
     * @return 前工序步骤列表
     */
    @GetMapping("/options/{stepId}/steps")
    public ResponseEntity<List<PreProcessingSteps>> getStepsByStepId(@PathVariable Integer stepId) {
        List<PreProcessingSteps> steps = preProcessingStepsService.getAllStepsByStepId(stepId);
        return ResponseEntity.ok(steps);
    }
    
    /**
     * 创建前工序步骤
     * @param step 前工序步骤
     * @return 创建的前工序步骤
     */
    @PostMapping("/steps")
    public ResponseEntity<?> createStep(@RequestBody PreProcessingSteps step) {
        try {
            PreProcessingSteps createdStep = preProcessingStepsService.create(step);
            return ResponseEntity.ok(createdStep);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 更新前工序步骤
     * @param id 步骤ID
     * @param step 前工序步骤
     * @return 更新后的前工序步骤
     */
    @PutMapping("/steps/{id}")
    public ResponseEntity<?> updateStep(@PathVariable Integer id, @RequestBody PreProcessingSteps step) {
        try {
            step.setId(id);
            PreProcessingSteps updatedStep = preProcessingStepsService.update(step);
            return ResponseEntity.ok(updatedStep);
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 删除前工序步骤
     * @param id 步骤ID
     * @return 删除结果
     */
    @DeleteMapping("/steps/{id}")
    public ResponseEntity<Map<String, Object>> deleteStep(@PathVariable Integer id) {
        try {
            boolean success = preProcessingStepsService.deleteById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "删除成功" : "删除失败");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出前工序选项到Excel
     */
    @GetMapping("/options/export")
    public ResponseEntity<byte[]> exportOptionsToExcel() {
        try {
            List<PreProcessingStepsOptions> allData = preProcessingStepsOptionsService.getAllOptions();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("前工序选项");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, PreProcessingStepsOptions.class, fileName, "前工序选项");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导出前工序步骤到Excel（根据stepId）
     */
    @GetMapping("/steps/export")
    public ResponseEntity<byte[]> exportStepsToExcel(@RequestParam(required = false) Integer stepId) {
        try {
            List<PreProcessingSteps> allData;
            
            if (stepId != null) {
                // 根据stepId获取数据
                allData = preProcessingStepsService.getAllStepsByStepId(stepId);
                
                // 根据stepId获取对应的前工序名称（preProcessingStepsName）
                PreProcessingStepsOptions option = preProcessingStepsOptionsService.getById(stepId);
                if (option != null && option.getPreProcessingStepsName() != null) {
                    // 为每条数据填充前工序名称
                    for (PreProcessingSteps step : allData) {
                        step.setPreProcessingStepsName(option.getPreProcessingStepsName());
                    }
                }
            } else {
                // 如果没有stepId，返回空数据
                allData = new ArrayList<>();
            }
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("前工序映射配置");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, PreProcessingSteps.class, fileName, "前工序映射配置");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导出所有前工序适用界面映射到Excel
     */
    @GetMapping("/steps/export-all")
    public ResponseEntity<byte[]> exportAllStepsToExcel() {
        try {
            // 获取所有映射数据
            List<PreProcessingSteps> allData = preProcessingStepsService.getAllSteps();
            
            // 为每条数据填充前工序名称
            if (allData != null && !allData.isEmpty()) {
                // 获取所有前工序选项，建立ID到名称的映射
                List<PreProcessingStepsOptions> allOptions = preProcessingStepsOptionsService.getAllOptions();
                Map<Integer, String> stepIdToNameMap = new HashMap<>();
                for (PreProcessingStepsOptions option : allOptions) {
                    if (option.getId() != null && option.getPreProcessingStepsName() != null) {
                        stepIdToNameMap.put(option.getId(), option.getPreProcessingStepsName());
                    }
                }
                
                // 为每条映射数据填充前工序名称
                for (PreProcessingSteps step : allData) {
                    if (step.getStepId() != null && stepIdToNameMap.containsKey(step.getStepId())) {
                        step.setPreProcessingStepsName(stepIdToNameMap.get(step.getStepId()));
                    }
                }
            }
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("所有前工序适用界面映射");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, PreProcessingSteps.class, fileName, "所有前工序适用界面映射");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导入前工序步骤
     */
    @PostMapping("/steps/import")
    public ResponseEntity<Map<String, Object>> importSteps(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("stepId") Integer stepId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                response.put("success", false);
                response.put("message", "文件不能为空");
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
            List<PreProcessingSteps> successList = new ArrayList<>();
            AtomicInteger totalCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);
            
            // 使用EasyExcel读取数据
            com.alibaba.excel.EasyExcel.read(file.getInputStream(), PreProcessingSteps.class,
                new com.alibaba.excel.read.listener.ReadListener<PreProcessingSteps>() {
                    @Override
                    public void invoke(PreProcessingSteps data, com.alibaba.excel.context.AnalysisContext context) {
                        totalCount.incrementAndGet();
                        try {
                            // 根据前工序名称（preProcessingStepsName）查找对应的stepId
                            if (data.getPreProcessingStepsName() == null || data.getPreProcessingStepsName().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 工艺类型不能为空", context.readRowHolder().getRowIndex() + 1));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 根据前工序名称查找对应的前工序选项
                            PreProcessingStepsOptions option = preProcessingStepsOptionsService.getByPreProcessingStepsName(data.getPreProcessingStepsName().trim());
                            if (option == null) {
                                errorMessages.add(String.format("第%d行: 未找到工艺类型 \"%s\" 对应的前工序选项", 
                                        context.readRowHolder().getRowIndex() + 1, 
                                        data.getPreProcessingStepsName()));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 设置stepId
                            data.setStepId(option.getId());
                            
                            if (data.getSeriesName() == null || data.getSeriesName().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 产品系列不能为空", context.readRowHolder().getRowIndex() + 1));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证产品系列是否存在
                            List<Product> seriesProducts = productMapper.findByName(data.getSeriesName().trim());
                            if (seriesProducts == null || seriesProducts.isEmpty()) {
                                errorMessages.add(String.format("第%d行: 产品系列 \"%s\" 不存在", 
                                        context.readRowHolder().getRowIndex() + 1, 
                                        data.getSeriesName()));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 根据产品型号、系列名称和烫金纸性能类型转换为产品ID
                            // 如果 productModelNumber 不为空，需要查找对应的 productId
                            List<Product> products = null;
                            if (data.getProductModelNumber() != null && !data.getProductModelNumber().trim().isEmpty()) {
                                products = productMapper.findByModelNumberAndNameAndPaperType(
                                        data.getProductModelNumber().trim(), 
                                        data.getSeriesName().trim(),
                                        data.getPaperType() != null ? data.getPaperType().trim() : null
                                );
                                if (products == null || products.isEmpty()) {
                                    String errorDetail = String.format("产品型号: %s, 产品系列: %s", 
                                            data.getProductModelNumber(), 
                                            data.getSeriesName());
                                    if (data.getPaperType() != null && !data.getPaperType().trim().isEmpty()) {
                                        errorDetail += String.format(", 烫金纸性能类型: %s", data.getPaperType());
                                    }
                                    errorMessages.add(String.format("第%d行: 未找到匹配的产品（%s）", 
                                            context.readRowHolder().getRowIndex() + 1, 
                                            errorDetail));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 设置默认值
                            // step_name 字段已废弃，但数据库要求不能为null，设置默认值
                            if (data.getStepName() == null || data.getStepName().trim().isEmpty()) {
                                // 使用前工序名称作为默认值，或者使用固定值
                                data.setStepName(data.getPreProcessingStepsName() != null ? 
                                        data.getPreProcessingStepsName() : "默认步骤名称");
                            }
                            if (data.getStepOrder() == null) {
                                data.setStepOrder(1);
                            }
                            if (data.getIsActive() == null) {
                                data.setIsActive(true);
                            }
                            if (data.getStatus() == null || data.getStatus().trim().isEmpty()) {
                                data.setStatus("V");
                            }
                            
                            // 如果找到多个产品，为每个产品创建映射
                            if (products != null && products.size() > 1) {
                                // 为每个产品创建映射
                                for (Product product : products) {
                                    PreProcessingSteps stepData = createStepDataCopy(data, option.getId(), product.getId());
                                    processStepData(stepData, option.getId(), context, errorMessages, successList, successCount, failCount);
                                }
                            } else {
                                // 单个产品或通用映射（productId为null）
                                Integer productId = (products != null && !products.isEmpty()) ? products.get(0).getId() : null;
                                data.setProductId(productId);
                                processStepData(data, option.getId(), context, errorMessages, successList, successCount, failCount);
                            }
                            
                        } catch (IllegalArgumentException e) {
                            // 业务验证错误，显示友好提示
                            errorMessages.add(String.format("第%d行: %s", context.readRowHolder().getRowIndex() + 1, e.getMessage()));
                            failCount.incrementAndGet();
                        } catch (Exception e) {
                            // 其他异常，显示详细错误信息
                            String errorMsg = e.getMessage();
                            if (errorMsg == null || errorMsg.trim().isEmpty()) {
                                errorMsg = "未知错误: " + e.getClass().getSimpleName();
                            }
                            errorMessages.add(String.format("第%d行: %s", context.readRowHolder().getRowIndex() + 1, errorMsg));
                            failCount.incrementAndGet();
                        }
                    }
                    
                    @Override
                    public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext context) {
                        // 处理完成
                    }
                }).sheet().doRead();
            
            // 构建响应
            int finalSuccessCount = successCount.get();
            int finalFailCount = failCount.get();
            response.put("success", finalFailCount == 0);
            response.put("message", String.format("导入完成！成功：%d 条，失败：%d 条", finalSuccessCount, finalFailCount));
            response.put("totalCount", totalCount.get());
            response.put("successCount", finalSuccessCount);
            response.put("failCount", finalFailCount);
            if (!errorMessages.isEmpty()) {
                response.put("details", errorMessages);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "导入失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 下载导入模板
     */
    @GetMapping("/steps/import-template")
    public ResponseEntity<byte[]> downloadImportTemplate() {
        try {
            // 创建示例数据
            List<PreProcessingSteps> templateData = new ArrayList<>();
            PreProcessingSteps example = new PreProcessingSteps();
            example.setPreProcessingStepsName("示例前工序名称"); // 前工序名称（必填，用于查找对应的stepId）
            example.setSeriesName("A19");
            example.setProductModelNumber("A19-001"); // 示例产品型号（可选，为空则表示通用映射）
            example.setPaperType("普通金纸");
            example.setStepOrder(1);
            example.setIsActive(true);
            example.setStatus("V");
            example.setDescription("示例描述");
            templateData.add(example);
            
            // 生成文件名
            String fileName = "映射配置导入模板.xlsx";
            
            // 导出Excel模板
            return ExcelExportUtil.exportToResponse(templateData, PreProcessingSteps.class, fileName, "映射配置导入模板");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导入所有前工序适用界面映射（不限制stepId）
     */
    @PostMapping("/steps/import-all")
    public ResponseEntity<Map<String, Object>> importAllSteps(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                response.put("success", false);
                response.put("message", "文件不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 验证文件类型
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                response.put("success", false);
                response.put("message", "文件格式不正确，请上传Excel文件");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 读取Excel并导入数据（复用现有的导入逻辑，但不限制stepId）
            List<String> errorMessages = new ArrayList<>();
            List<PreProcessingSteps> successList = new ArrayList<>();
            AtomicInteger totalCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);
            
            // 使用EasyExcel读取数据
            com.alibaba.excel.EasyExcel.read(file.getInputStream(), PreProcessingSteps.class,
                new com.alibaba.excel.read.listener.ReadListener<PreProcessingSteps>() {
                    @Override
                    public void invoke(PreProcessingSteps data, com.alibaba.excel.context.AnalysisContext context) {
                        totalCount.incrementAndGet();
                        try {
                            // 根据前工序名称（preProcessingStepsName）查找对应的stepId
                            if (data.getPreProcessingStepsName() == null || data.getPreProcessingStepsName().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 工艺类型不能为空", context.readRowHolder().getRowIndex() + 1));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 根据前工序名称查找对应的前工序选项
                            PreProcessingStepsOptions option = preProcessingStepsOptionsService.getByPreProcessingStepsName(data.getPreProcessingStepsName().trim());
                            if (option == null) {
                                errorMessages.add(String.format("第%d行: 未找到工艺类型 \"%s\" 对应的前工序选项", 
                                        context.readRowHolder().getRowIndex() + 1, 
                                        data.getPreProcessingStepsName()));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 设置stepId
                            data.setStepId(option.getId());
                            
                            if (data.getSeriesName() == null || data.getSeriesName().trim().isEmpty()) {
                                errorMessages.add(String.format("第%d行: 产品系列不能为空", context.readRowHolder().getRowIndex() + 1));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 验证产品系列是否存在
                            List<Product> seriesProducts = productMapper.findByName(data.getSeriesName().trim());
                            if (seriesProducts == null || seriesProducts.isEmpty()) {
                                errorMessages.add(String.format("第%d行: 产品系列 \"%s\" 不存在", 
                                        context.readRowHolder().getRowIndex() + 1, 
                                        data.getSeriesName()));
                                failCount.incrementAndGet();
                                return;
                            }
                            
                            // 根据产品型号、系列名称和烫金纸性能类型转换为产品ID
                            // 如果 productModelNumber 不为空，需要查找对应的 productId
                            List<Product> products = null;
                            if (data.getProductModelNumber() != null && !data.getProductModelNumber().trim().isEmpty()) {
                                products = productMapper.findByModelNumberAndNameAndPaperType(
                                        data.getProductModelNumber().trim(), 
                                        data.getSeriesName().trim(),
                                        data.getPaperType() != null ? data.getPaperType().trim() : null
                                );
                                if (products == null || products.isEmpty()) {
                                    String errorDetail = String.format("产品型号: %s, 产品系列: %s", 
                                            data.getProductModelNumber(), 
                                            data.getSeriesName());
                                    if (data.getPaperType() != null && !data.getPaperType().trim().isEmpty()) {
                                        errorDetail += String.format(", 烫金纸性能类型: %s", data.getPaperType());
                                    }
                                    errorMessages.add(String.format("第%d行: 未找到匹配的产品（%s）", 
                                            context.readRowHolder().getRowIndex() + 1, 
                                            errorDetail));
                                    failCount.incrementAndGet();
                                    return;
                                }
                            }
                            
                            // 设置默认值
                            if (data.getStepName() == null || data.getStepName().trim().isEmpty()) {
                                data.setStepName(data.getPreProcessingStepsName() != null ? 
                                        data.getPreProcessingStepsName() : "默认步骤名称");
                            }
                            if (data.getStepOrder() == null) {
                                data.setStepOrder(1);
                            }
                            if (data.getIsActive() == null) {
                                data.setIsActive(true);
                            }
                            if (data.getStatus() == null || data.getStatus().trim().isEmpty()) {
                                data.setStatus("V");
                            }
                            
                            // 如果找到多个产品，为每个产品创建映射
                            if (products != null && products.size() > 1) {
                                // 为每个产品创建映射
                                for (Product product : products) {
                                    PreProcessingSteps stepData = createStepDataCopy(data, option.getId(), product.getId());
                                    processStepData(stepData, option.getId(), context, errorMessages, successList, successCount, failCount);
                                }
                            } else {
                                // 单个产品或通用映射（productId为null）
                                Integer productId = (products != null && !products.isEmpty()) ? products.get(0).getId() : null;
                                data.setProductId(productId);
                                processStepData(data, option.getId(), context, errorMessages, successList, successCount, failCount);
                            }
                            
                        } catch (IllegalArgumentException e) {
                            // 业务验证错误，显示友好提示
                            errorMessages.add(String.format("第%d行: %s", context.readRowHolder().getRowIndex() + 1, e.getMessage()));
                            failCount.incrementAndGet();
                        } catch (Exception e) {
                            // 其他异常，显示详细错误信息
                            String errorMsg = e.getMessage();
                            if (errorMsg == null || errorMsg.trim().isEmpty()) {
                                errorMsg = "未知错误: " + e.getClass().getSimpleName();
                            }
                            errorMessages.add(String.format("第%d行: %s", context.readRowHolder().getRowIndex() + 1, errorMsg));
                            failCount.incrementAndGet();
                        }
                    }
                    
                    @Override
                    public void doAfterAllAnalysed(com.alibaba.excel.context.AnalysisContext context) {
                        // 处理完成
                    }
                }).sheet().doRead();
            
            // 构建响应
            int finalSuccessCount = successCount.get();
            int finalFailCount = failCount.get();
            response.put("success", finalFailCount == 0);
            response.put("message", String.format("导入完成！成功：%d 条，失败：%d 条", finalSuccessCount, finalFailCount));
            response.put("totalCount", totalCount.get());
            response.put("successCount", finalSuccessCount);
            response.put("failCount", finalFailCount);
            if (!errorMessages.isEmpty()) {
                response.put("details", errorMessages);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "导入失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 创建步骤数据的副本，用于为每个产品创建映射
     * @param original 原始步骤数据
     * @param stepId 步骤ID
     * @param productId 产品ID
     * @return 新的步骤数据副本
     */
    private PreProcessingSteps createStepDataCopy(PreProcessingSteps original, Integer stepId, Integer productId) {
        PreProcessingSteps copy = new PreProcessingSteps();
        copy.setStepId(stepId);
        copy.setSeriesName(original.getSeriesName());
        copy.setProductId(productId);
        copy.setPaperType(original.getPaperType());
        copy.setStepName(original.getStepName());
        copy.setStepOrder(original.getStepOrder());
        copy.setIsActive(original.getIsActive());
        copy.setStatus(original.getStatus());
        copy.setDescription(original.getDescription());
        copy.setPreProcessingStepsName(original.getPreProcessingStepsName());
        copy.setProductModelNumber(original.getProductModelNumber());
        // 注意：不复制id，让系统自动创建新记录
        return copy;
    }
    
    /**
     * 处理单个步骤数据的创建或更新
     * @param data 步骤数据
     * @param stepId 步骤ID
     * @param context Excel读取上下文
     * @param errorMessages 错误消息列表
     * @param successList 成功列表
     * @param successCount 成功计数
     * @param failCount 失败计数
     */
    private void processStepData(PreProcessingSteps data, Integer stepId, 
                                 com.alibaba.excel.context.AnalysisContext context,
                                 List<String> errorMessages, List<PreProcessingSteps> successList,
                                 AtomicInteger successCount, AtomicInteger failCount) {
        // 判断是更新还是创建
        // 如果提供了id，尝试更新已存在的记录
        if (data.getId() != null) {
            try {
                // 更新已存在的记录
                data.setStepId(stepId); // 确保stepId正确
                preProcessingStepsService.update(data);
                successList.add(data);
                successCount.incrementAndGet();
            } catch (Exception e) {
                // 如果更新失败（例如记录不存在），尝试创建
                data.setId(null); // 清除id，尝试创建
                try {
                    preProcessingStepsService.create(data);
                    successList.add(data);
                    successCount.incrementAndGet();
                } catch (Exception createEx) {
                    // 创建也失败，记录错误
                    if (createEx instanceof IllegalArgumentException) {
                        errorMessages.add(String.format("第%d行: %s", 
                                context.readRowHolder().getRowIndex() + 1, 
                                createEx.getMessage()));
                    } else {
                        errorMessages.add(String.format("第%d行: 更新和创建都失败: %s", 
                                context.readRowHolder().getRowIndex() + 1, 
                                createEx.getMessage()));
                    }
                    failCount.incrementAndGet();
                }
            }
        } else {
            // 没有提供id，检查是否已存在相同的记录（根据唯一性字段）
            PreProcessingSteps existing = preProcessingStepsService.findByUniqueFields(
                    data.getStepId(),
                    data.getSeriesName(),
                    data.getProductId(),
                    data.getPaperType()
            );
            if (existing != null) {
                // 记录已存在，更新它
                data.setId(existing.getId());
                try {
                    preProcessingStepsService.update(data);
                    successList.add(data);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    if (e instanceof IllegalArgumentException) {
                        errorMessages.add(String.format("第%d行: %s", 
                                context.readRowHolder().getRowIndex() + 1, 
                                e.getMessage()));
                    } else {
                        errorMessages.add(String.format("第%d行: 更新失败: %s", 
                                context.readRowHolder().getRowIndex() + 1, 
                                e.getMessage()));
                    }
                    failCount.incrementAndGet();
                }
            } else {
                // 记录不存在，创建新记录
                try {
                    preProcessingStepsService.create(data);
                    successList.add(data);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    if (e instanceof IllegalArgumentException) {
                        errorMessages.add(String.format("第%d行: %s", 
                                context.readRowHolder().getRowIndex() + 1, 
                                e.getMessage()));
                    } else {
                        errorMessages.add(String.format("第%d行: 创建失败: %s", 
                                context.readRowHolder().getRowIndex() + 1, 
                                e.getMessage()));
                    }
                    failCount.incrementAndGet();
                }
            }
        }
    }
}
