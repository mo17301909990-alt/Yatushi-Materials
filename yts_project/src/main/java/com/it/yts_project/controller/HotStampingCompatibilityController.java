package com.it.yts_project.controller;

import com.it.yts_project.dto.CompatibilityQueryParams;
import com.it.yts_project.dto.HotStampingTypeGroupDTO;
import com.it.yts_project.model.HotStampingCompatibility;
import com.it.yts_project.model.HotStampingPatternBase;
import com.it.yts_project.model.HotStampingTypeOptions;
import com.it.yts_project.model.LaminationMaterialOptions;
import com.it.yts_project.model.PostProcessingOptions;
import com.it.yts_project.service.HotStampingCompatibilityService;
import com.it.yts_project.util.ExcelExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 烫金工艺兼容性控制器
 */
@RestController
@RequestMapping("/api/compatibility")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class HotStampingCompatibilityController {
    
    @Autowired
    private HotStampingCompatibilityService compatibilityService;
    
    /**
     * 获取兼容的烫金类型
     */
    @PostMapping("/hot-stamping-types")
    public ResponseEntity<List<HotStampingCompatibility>> getCompatibleHotStampingTypes(
            @RequestBody CompatibilityQueryParams params) {
        List<HotStampingCompatibility> result = compatibilityService.getCompatibleHotStampingTypes(params);
        return ResponseEntity.ok(result);
    }
    
    /**
     * 智能推荐烫金类型
     */
    @PostMapping("/recommend")
    public ResponseEntity<List<String>> getRecommendedHotStampingTypes(
            @RequestBody CompatibilityQueryParams params) {
        List<String> result = compatibilityService.getRecommendedHotStampingTypes(params);
        return ResponseEntity.ok(result);
    }
    

    
    /**
     * 获取所有烫金纸性能类型
     */
    @GetMapping("/paper-performance-types")
    public ResponseEntity<List<String>> getAllPaperPerformanceTypes() {
        List<String> result = compatibilityService.getAllPaperPerformanceTypes();
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取所有产品类型
     */
    @GetMapping("/product-types")
    public ResponseEntity<List<String>> getAllProductTypes() {
        List<String> result = compatibilityService.getAllProductTypes();
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取所有烫金类型
     */
    @GetMapping("/hot-stamping-types")
    public ResponseEntity<List<String>> getAllHotStampingTypes() {
        List<String> result = compatibilityService.getAllHotStampingTypes();
        return ResponseEntity.ok(result);
    }
    
    // ========== 过胶兼容性相关API ==========
    
    /**
     * 获取所有后加工步骤选项
     */
    @GetMapping("/post-processing-steps")
    public ResponseEntity<List<PostProcessingOptions>> getAllPostProcessingSteps() {
        List<PostProcessingOptions> result = compatibilityService.getAllPostProcessingSteps();
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取所有过胶材质选项
     */
    @GetMapping("/lamination-materials")
    public ResponseEntity<List<LaminationMaterialOptions>> getAllLaminationMaterials() {
        List<LaminationMaterialOptions> result = compatibilityService.getAllLaminationMaterials();
        return ResponseEntity.ok(result);
    }

    /**
     * 根据产品类型ID获取图案选项（用于5. 6.燙金圖案(X)(耐磨燙金紙選用)）
     */
    @GetMapping("/pattern-options/{productTypeId}")
    public ResponseEntity<List<HotStampingPatternBase>> getPatternOptionsByProductTypeId(
            @PathVariable Integer productTypeId) {
        List<HotStampingPatternBase> patterns = compatibilityService.getPatternOptionsByProductTypeId(productTypeId);
        return ResponseEntity.ok(patterns);
    }
    
    /**
     * 获取所有烫金类型分组（支持多级下拉框）
     */
    @GetMapping("/hot-stamping-type-groups")
    public ResponseEntity<List<HotStampingTypeGroupDTO>> getAllHotStampingTypeGroups() {
        List<HotStampingTypeGroupDTO> result = compatibilityService.getAllHotStampingTypeGroups();
        return ResponseEntity.ok(result);
    }
    
    /**
     * 根据烫金类型获取位置选项
     */
    @GetMapping("/position-options/{stampingType}")
    public ResponseEntity<List<HotStampingTypeOptions>> getPositionOptionsByStampingType(
            @PathVariable String stampingType) {
        List<HotStampingTypeOptions> result = compatibilityService.getPositionOptionsByStampingType(stampingType);
        return ResponseEntity.ok(result);
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出烫金图案数据到Excel
     */
    @GetMapping("/pattern-options/export")
    public ResponseEntity<byte[]> exportPatternOptionsToExcel() {
        try {
            // 获取所有图案数据（这里需要根据实际业务逻辑调整）
            List<HotStampingPatternBase> allData = compatibilityService.getAllPatternOptions();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("烫金图案选项");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, HotStampingPatternBase.class, fileName, "烫金图案选项");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    /**
     * 导出烫金兼容性数据到Excel
     */
    @PostMapping("/compatibility/export")
    public ResponseEntity<byte[]> exportCompatibilityToExcel(@RequestBody CompatibilityQueryParams params) {
        try {
            // 获取兼容性数据
            List<HotStampingCompatibility> allData = compatibilityService.getCompatibleHotStampingTypes(params);
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("烫金兼容性数据");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, HotStampingCompatibility.class, fileName, "烫金兼容性数据");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
