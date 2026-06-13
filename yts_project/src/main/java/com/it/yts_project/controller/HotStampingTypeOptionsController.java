package com.it.yts_project.controller;

import com.it.yts_project.model.HotStampingTypeOptions;
import com.it.yts_project.service.HotStampingTypeOptionsService;
import com.it.yts_project.util.ExcelExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hot-stamping-type-options")
@CrossOrigin(origins = {"http://localhost:5173", "http://120.26.101.0", "http://120.26.101.0:80", "http://120.26.101.0:8080", "http://120.26.101.0:3000"}, allowCredentials = "true")
public class HotStampingTypeOptionsController {

    @Autowired
    private HotStampingTypeOptionsService hotStampingTypeOptionsService;

    /**
     * 获取所有激活的烫金类型选项
     */
    @GetMapping("/active")
    public ResponseEntity<List<HotStampingTypeOptions>> getActiveTypes() {
        try {
            List<HotStampingTypeOptions> types = hotStampingTypeOptionsService.getActiveTypes();
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 获取所有烫金类型选项
     */
    @GetMapping
    public ResponseEntity<List<HotStampingTypeOptions>> getAllTypes() {
        try {
            List<HotStampingTypeOptions> types = hotStampingTypeOptionsService.getAllTypes();
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID获取烫金类型选项
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotStampingTypeOptions> getTypeById(@PathVariable Long id) {
        try {
            HotStampingTypeOptions type = hotStampingTypeOptionsService.getTypeById(id);
            if (type != null) {
                return ResponseEntity.ok(type);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出烫金类型选项到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportTypesToExcel() {
        try {
            List<HotStampingTypeOptions> allData = hotStampingTypeOptionsService.getAllTypes();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("烫金类型选项");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, HotStampingTypeOptions.class, fileName, "烫金类型选项");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}