package com.it.yts_project.controller;

import com.it.yts_project.dto.ClothPaperCompatibilityDTO;
import com.it.yts_project.dto.ClothPaperTypeDTO;
import com.it.yts_project.mapper.ProductsMapper;
import com.it.yts_project.service.ClothPaperCompatibilityService;
import com.it.yts_project.service.ClothPaperTypeService;
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
 * 布面纸兼容性控制器
 */
@RestController
@RequestMapping("/api/cloth-paper-compatibility")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class ClothPaperCompatibilityController {
    
    @Autowired
    private ClothPaperCompatibilityService clothPaperCompatibilityService;
    
    @Autowired
    private ClothPaperTypeService clothPaperTypeService;
    
    @Autowired
    private ProductsMapper productsMapper;
    
    /**
     * 获取所有兼容性记录
     * @return 兼容性记录列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<ClothPaperCompatibilityDTO>> getAllCompatibility() {
        List<ClothPaperCompatibilityDTO> compatibilities = clothPaperCompatibilityService.getAllCompatibility();
        return ResponseEntity.ok(compatibilities);
    }
    
    /**
     * 根据燙金紙系列获取兼容性记录
     * @param projectId 燙金紙系列
     * @return 兼容性记录列表
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ClothPaperCompatibilityDTO>> getByProjectId(@PathVariable String projectId) {
        List<ClothPaperCompatibilityDTO> compatibilities = clothPaperCompatibilityService.getByProjectId(projectId);
        return ResponseEntity.ok(compatibilities);
    }
    
    /**
     * 根据布面纸类型ID获取兼容性记录
     * @param clothPaperTypeId 布面纸类型ID
     * @return 兼容性记录列表
     */
    @GetMapping("/type/{clothPaperTypeId}")
    public ResponseEntity<List<ClothPaperCompatibilityDTO>> getByClothPaperTypeId(@PathVariable Integer clothPaperTypeId) {
        List<ClothPaperCompatibilityDTO> compatibilities = clothPaperCompatibilityService.getByClothPaperTypeId(clothPaperTypeId);
        return ResponseEntity.ok(compatibilities);
    }
    
    /**
     * 根据燙金紙系列和布面纸类型ID获取兼容性记录
     * @param projectId 燙金紙系列
     * @param clothPaperTypeId 布面纸类型ID
     * @return 兼容性记录
     */
    @GetMapping("/project/{projectId}/type/{clothPaperTypeId}")
    public ResponseEntity<ClothPaperCompatibilityDTO> getByProjectAndType(
            @PathVariable String projectId, 
            @PathVariable Integer clothPaperTypeId) {
        ClothPaperCompatibilityDTO compatibility = clothPaperCompatibilityService.getByProjectAndType(projectId, clothPaperTypeId);
        if (compatibility != null) {
            return ResponseEntity.ok(compatibility);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 创建兼容性记录
     * @param compatibility 兼容性记录
     * @return 创建的兼容性记录
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ClothPaperCompatibilityDTO compatibility) {
        try {
            ClothPaperCompatibilityDTO createdCompatibility = clothPaperCompatibilityService.create(compatibility);
            return ResponseEntity.ok(createdCompatibility);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("组合已存在")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", errorMessage));
            }
            return ResponseEntity.badRequest()
                .body(Map.of("error", "该燙金紙系列、布面纸类型和烫金纸性能类型的组合已存在，请选择其他组合"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "创建失败：" + e.getMessage()));
        }
    }
    
    /**
     * 更新兼容性记录
     * @param id 主键ID
     * @param compatibility 兼容性记录
     * @return 更新后的兼容性记录
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ClothPaperCompatibilityDTO compatibility) {
        try {
            compatibility.setId(id);
            ClothPaperCompatibilityDTO updatedCompatibility = clothPaperCompatibilityService.update(compatibility);
            return ResponseEntity.ok(updatedCompatibility);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null && errorMessage.contains("组合已存在")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", errorMessage));
            }
            return ResponseEntity.badRequest()
                .body(Map.of("error", "该燙金紙系列、布面纸类型和烫金纸性能类型的组合已存在，请选择其他组合"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "更新失败：" + e.getMessage()));
        }
    }
    
    /**
     * 删除兼容性记录
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Integer id) {
        try {
            boolean success = clothPaperCompatibilityService.deleteById(id);
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
    
    /**
     * 根据产品ID和布面纸类型ID删除兼容性记录
     * @param projectId 产品ID
     * @param clothPaperTypeId 布面纸类型ID
     * @return 删除结果
     */
    @DeleteMapping("/project/{projectId}/type/{clothPaperTypeId}")
    public ResponseEntity<Map<String, Object>> deleteByProjectAndType(
            @PathVariable Integer projectId, 
            @PathVariable Integer clothPaperTypeId) {
        try {
            boolean success = clothPaperCompatibilityService.deleteByProjectAndType(projectId.toString(), clothPaperTypeId);
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
    
    /**
     * 批量创建兼容性记录
     * @param compatibilities 兼容性记录列表
     * @return 创建结果
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchCreate(@RequestBody List<ClothPaperCompatibilityDTO> compatibilities) {
        try {
            boolean success = clothPaperCompatibilityService.batchCreate(compatibilities);
            Map<String, Object> response = new HashMap<>();
            response.put("success", success);
            response.put("message", success ? "批量创建成功" : "批量创建失败");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "批量创建失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 批量更新产品兼容性状态
     * @param projectId 燙金紙系列
     * @param compatibilities 兼容性记录列表
     * @return 更新结果
     */
    @PutMapping("/project/{projectId}/batch")
    public ResponseEntity<Map<String, Object>> batchUpdateByProject(
            @PathVariable String projectId, 
            @RequestBody List<ClothPaperCompatibilityDTO> compatibilities) {
        try {
            boolean success = clothPaperCompatibilityService.batchUpdateByProject(projectId, compatibilities);
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
     * 获取所有燙金紙系列
     * @return 燙金紙系列列表
     */
    @GetMapping("/products")
    public ResponseEntity<List<String>> getAllProductNames() {
        List<String> productNames = clothPaperCompatibilityService.getAllProductNames();
        return ResponseEntity.ok(productNames);
    }
    
    /**
     * 根据燙金紙系列获取燙金紙系列
     * @param productName 燙金紙系列
     * @return 燙金紙系列
     */
    @GetMapping("/product/{productName}/id")
    public ResponseEntity<Map<String, Object>> getProjectIdByName(@PathVariable String productName) {
        try {
            String projectId = clothPaperCompatibilityService.getProjectIdByName(productName);
            Map<String, Object> response = new HashMap<>();
            response.put("projectId", projectId);
            response.put("success", projectId != null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "获取燙金紙系列失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 导出兼容性配置到Excel
     * @param clothPaperTypeId 布面纸类型ID（可选）
     * @return Excel文件
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCompatibilityToExcel(@RequestParam(required = false) Integer clothPaperTypeId) {
        try {
            List<ClothPaperCompatibilityDTO> allData;
            
            if (clothPaperTypeId != null) {
                // 根据布面纸类型ID获取数据
                allData = clothPaperCompatibilityService.getByClothPaperTypeId(clothPaperTypeId);
            } else {
                // 获取所有数据
                allData = clothPaperCompatibilityService.getAllCompatibility();
            }
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("適用界面映射配置");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, ClothPaperCompatibilityDTO.class, fileName, "適用界面映射配置");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导入兼容性配置
     * @param file Excel文件
     * @param clothPaperTypeId 布面纸类型ID（可选）
     * @return 导入结果
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importCompatibility(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Integer clothPaperTypeId) {
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
            AtomicInteger totalCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failCount = new AtomicInteger(0);
            
            // 使用EasyExcel读取数据
            com.alibaba.excel.EasyExcel.read(file.getInputStream(), ClothPaperCompatibilityDTO.class,
                new com.alibaba.excel.read.listener.ReadListener<ClothPaperCompatibilityDTO>() {
                    @Override
                    public void invoke(ClothPaperCompatibilityDTO data, com.alibaba.excel.context.AnalysisContext context) {
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
                            
                            // 根据 clothPaperTypeName 和 clothPaperCategory 查找 clothPaperTypeId
                            Integer resolvedClothPaperTypeId = clothPaperTypeId;
                            
                            // 如果传入了 clothPaperTypeId，验证 Excel 中的类型名称和分类是否匹配
                            if (resolvedClothPaperTypeId != null) {
                                ClothPaperTypeDTO clothPaperTypeById = clothPaperTypeService.getById(resolvedClothPaperTypeId);
                                if (clothPaperTypeById == null) {
                                    errorMessages.add(String.format("第%d行: 適用界面類型ID「%d」不存在", rowIndex, resolvedClothPaperTypeId));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                // 如果 Excel 中提供了类型名称和分类，验证是否与传入的 ID 匹配
                                if (data.getClothPaperTypeName() != null && !data.getClothPaperTypeName().trim().isEmpty() &&
                                    data.getClothPaperCategory() != null && !data.getClothPaperCategory().trim().isEmpty()) {
                                    String excelTypeName = data.getClothPaperTypeName().trim();
                                    String excelCategory = data.getClothPaperCategory().trim();
                                    
                                    if (!excelTypeName.equals(clothPaperTypeById.getTypeName()) || 
                                        !excelCategory.equals(clothPaperTypeById.getCategory())) {
                                        errorMessages.add(String.format("第%d行: Excel中的適用界面類型（類型名稱: %s, 分類: %s）與指定的適用界面類型ID「%d」（類型名稱: %s, 分類: %s）不匹配", 
                                                rowIndex, 
                                                excelTypeName, 
                                                excelCategory,
                                                resolvedClothPaperTypeId,
                                                clothPaperTypeById.getTypeName(),
                                                clothPaperTypeById.getCategory()));
                                        failCount.incrementAndGet();
                                        return;
                                    }
                                }
                            } else {
                                // 如果没有传入 clothPaperTypeId，从 Excel 中读取并验证
                                // 验证適用界面類型名稱
                                if (data.getClothPaperTypeName() == null || data.getClothPaperTypeName().trim().isEmpty()) {
                                    errorMessages.add(String.format("第%d行: 適用界面類型名稱不能為空", rowIndex));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                // 验证適用界面分類
                                if (data.getClothPaperCategory() == null || data.getClothPaperCategory().trim().isEmpty()) {
                                    errorMessages.add(String.format("第%d行: 適用界面分類不能為空", rowIndex));
                                    failCount.incrementAndGet();
                                    return;
                                }
                                
                                // 查找對應的適用界面類型
                                ClothPaperTypeDTO clothPaperType = clothPaperTypeService.getByTypeNameAndCategory(
                                    data.getClothPaperTypeName().trim(), 
                                    data.getClothPaperCategory().trim()
                                );
                                if (clothPaperType != null) {
                                    resolvedClothPaperTypeId = clothPaperType.getId();
                                } else {
                                    errorMessages.add(String.format("第%d行: 找不到對應的適用界面類型（類型名稱: %s, 分類: %s）", 
                                            rowIndex, 
                                            data.getClothPaperTypeName().trim(), 
                                            data.getClothPaperCategory().trim()));
                                    failCount.incrementAndGet();
                                    return;
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
                            
                            data.setClothPaperTypeId(resolvedClothPaperTypeId);
                            
                            // 检查是否已存在（根据燙金紙系列、適用界面類型ID和燙金紙性能類型）
                            ClothPaperCompatibilityDTO existing = clothPaperCompatibilityService.getByProjectAndTypeAndPaperType(
                                productName, resolvedClothPaperTypeId, paperType);
                            
                            if (existing != null) {
                                // 已存在，更新
                                existing.setCompatibilityStatus(data.getCompatibilityStatus());
                                existing.setPaperType(data.getPaperType());
                                clothPaperCompatibilityService.update(existing);
                                successCount.incrementAndGet();
                            } else {
                                // 不存在，创建
                                clothPaperCompatibilityService.create(data);
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
            List<ClothPaperCompatibilityDTO> templateData = new ArrayList<>();
            ClothPaperCompatibilityDTO example = new ClothPaperCompatibilityDTO();
            example.setProductName("示例系列");
            example.setCompatibilityStatus("V");
            example.setPaperType("普通金紙");
            example.setClothPaperTypeName("示例類型");
            example.setClothPaperCategory("示例分類");
            templateData.add(example);
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("適用界面映射配置導入模板");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(templateData, ClothPaperCompatibilityDTO.class, fileName, "映射配置導入模板");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
