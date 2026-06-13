package com.it.yts_project.controller;

import com.it.yts_project.dto.ImportResult;
import com.it.yts_project.dto.ImportConfig;
import com.it.yts_project.model.CompleteProductInfo;
import com.it.yts_project.model.Product;
import com.it.yts_project.service.ProductImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 产品导入控制器
 * 提供Excel文件导入功能，支持数据冲突处理
 */
@Slf4j
@RestController
@RequestMapping("/api/product-import")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class ProductImportController {
    
    @Autowired
    private ProductImportService productImportService;
    
    /**
     * 导入完整产品信息（智能模式）
     * 
     * @param file Excel文件
     * @param importMode 导入模式（可选，默认SMART）
     * @return 导入结果
     */
    @PostMapping("/complete")
    public ResponseEntity<ImportResult<CompleteProductInfo>> importCompleteProductInfo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "importMode", defaultValue = "SMART") String importMode) {
        
        log.info("开始导入完整产品信息，文件名: {}, 导入模式: {}", 
            file.getOriginalFilename(), importMode);
        
        try {
            // 构建导入配置
            ImportConfig config = ImportConfig.builder()
                    .conflictStrategy(ImportConfig.ConflictStrategy.SKIP) // 智能模式下使用SKIP作为基础策略
                    .validateData(true)
                    .backupBeforeImport(false) // 智能模式不需要备份
                    .sendNotification(true)
                    .maxImportCount(1000)
                    .build();
            ImportResult<CompleteProductInfo> result = productImportService.importCompleteProductInfo(file, config);
            
            log.info("完整产品信息导入完成，成功: {}, 失败: {}", 
                result.getSuccessCount(), result.getErrorCount());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("导入完整产品信息失败", e);
            return ResponseEntity.badRequest().body(
                ImportResult.failure("导入失败: " + e.getMessage())
            );
        }
    }
    
    /**
     * 导入完整产品信息（自定义配置）
     * 
     * @param file Excel文件
     * @param conflictStrategy 冲突处理策略
     * @param validateData 是否验证数据
     * @return 导入结果
     */
    @PostMapping("/complete/custom")
    public ResponseEntity<ImportResult<CompleteProductInfo>> importCompleteProductInfoCustom(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "conflictStrategy", defaultValue = "SKIP") String conflictStrategy,
            @RequestParam(value = "validateData", defaultValue = "true") boolean validateData) {
        
        log.info("开始导入完整产品信息（自定义配置），文件名: {}, 冲突策略: {}", 
            file.getOriginalFilename(), conflictStrategy);
        
        try {
            // 构建自定义配置
            ImportConfig.ConflictStrategy strategy = ImportConfig.ConflictStrategy.valueOf(conflictStrategy);
            ImportConfig config = ImportConfig.builder()
                    .conflictStrategy(strategy)
                    .validateData(validateData)
                    .backupBeforeImport(strategy == ImportConfig.ConflictStrategy.OVERWRITE)
                    .sendNotification(true)
                    .maxImportCount(1000)
                    .build();
            
            ImportResult<CompleteProductInfo> result = productImportService.importCompleteProductInfo(file, config);
            
            log.info("完整产品信息导入完成（自定义），成功: {}, 失败: {}", 
                result.getSuccessCount(), result.getErrorCount());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("导入完整产品信息失败（自定义）", e);
            return ResponseEntity.badRequest().body(
                ImportResult.failure("导入失败: " + e.getMessage())
            );
        }
    }
    
    /**
     * 导入基础产品信息（用户选择模式）
     * 
     * @param file Excel文件
     * @param conflictStrategy 冲突处理策略（可选，默认SKIP）
     * @return 导入结果
     */
    @PostMapping("/basic")
    public ResponseEntity<ImportResult<Product>> importBasicProductInfo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "conflictStrategy", defaultValue = "SKIP") String conflictStrategy) {
        
        log.info("开始导入基础产品信息，文件名: {}, 冲突策略: {}", 
            file.getOriginalFilename(), conflictStrategy);
        
        try {
            // 构建用户选择的配置
            ImportConfig.ConflictStrategy strategy = ImportConfig.ConflictStrategy.valueOf(conflictStrategy);
            ImportConfig config = ImportConfig.builder()
                    .conflictStrategy(strategy)
                    .validateData(true)
                    .backupBeforeImport(strategy == ImportConfig.ConflictStrategy.OVERWRITE)
                    .sendNotification(true)
                    .maxImportCount(1000)
                    .build();
            ImportResult<Product> result = productImportService.importBasicProductInfo(file, config);
            
            log.info("基础产品信息导入完成，成功: {}, 失败: {}", 
                result.getSuccessCount(), result.getErrorCount());
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("导入基础产品信息失败", e);
            return ResponseEntity.badRequest().body(
                ImportResult.failure("导入失败: " + e.getMessage())
            );
        }
    }
    
    /**
     * 获取导入配置选项
     * 
     * @return 配置选项
     */
    @GetMapping("/config-options")
    public ResponseEntity<?> getImportConfigOptions() {
        return ResponseEntity.ok(new Object() {
            public final String[] importModes = {"SMART", "SKIP", "OVERWRITE", "CREATE_NEW"};
            public final String[] importModeDescriptions = {
                "SMART - 智能模式（推荐，自动判断新增/更新）",
                "SKIP - 跳过冲突数据（安全）",
                "OVERWRITE - 覆盖现有数据（谨慎使用）",
                "CREATE_NEW - 创建新记录（忽略冲突）"
            };
            public final int maxImportCount = 1000;
            public final String[] supportedFormats = {".xlsx", ".xls"};
            public final String defaultMode = "SMART";
        });
    }
    
    /**
     * 验证导入文件
     * 
     * @param file Excel文件
     * @return 验证结果
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateImportFile(@RequestParam("file") MultipartFile file) {
        try {
            // 基础验证
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("文件不能为空");
            }
            
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return ResponseEntity.badRequest().body("文件格式不正确，请上传Excel文件");
            }
            
            // 文件大小验证
            long maxSize = 10 * 1024 * 1024; // 10MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest().body("文件过大，请上传小于10MB的文件");
            }
            
            return ResponseEntity.ok(new Object() {
                public final boolean valid = true;
                public final String message = "文件验证通过";
                public final String fileName = file.getOriginalFilename();
                public final long fileSize = file.getSize();
            });
            
        } catch (Exception e) {
            log.error("文件验证失败", e);
            return ResponseEntity.badRequest().body("文件验证失败: " + e.getMessage());
        }
    }
}
