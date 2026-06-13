package com.it.yts_project.controller;

import com.it.yts_project.model.HotStampingPatternBase;
import com.it.yts_project.service.HotStampingPatternBaseService;
import com.it.yts_project.util.ExcelExportUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hot-stamping-pattern-base")
@CrossOrigin(origins = {"http://localhost:5173", "http://120.26.101.0", "http://120.26.101.0:80", "http://120.26.101.0:8080", "http://120.26.101.0:3000"}, allowCredentials = "true")
public class HotStampingPatternBaseController {

    @Autowired
    private HotStampingPatternBaseService hotStampingPatternBaseService;

    /**
     * 获取所有激活的烫金图案基础信息
     */
    @GetMapping("/active")
    public ResponseEntity<List<HotStampingPatternBase>> getActivePatterns() {
        try {
            List<HotStampingPatternBase> patterns = hotStampingPatternBaseService.getActivePatterns();
            return ResponseEntity.ok(patterns);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 获取所有烫金图案基础信息
     */
    @GetMapping
    public ResponseEntity<List<HotStampingPatternBase>> getAllPatterns() {
        try {
            List<HotStampingPatternBase> patterns = hotStampingPatternBaseService.getAllPatterns();
            return ResponseEntity.ok(patterns);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * 根据ID获取烫金图案基础信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<HotStampingPatternBase> getPatternById(@PathVariable Long id) {
        try {
            HotStampingPatternBase pattern = hotStampingPatternBaseService.getPatternById(id);
            if (pattern != null) {
                return ResponseEntity.ok(pattern);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    // ========== Excel导出功能 ==========
    
    /**
     * 导出烫金图案基础信息到Excel
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportPatternsToExcel() {
        try {
            List<HotStampingPatternBase> allData = hotStampingPatternBaseService.getAllPatterns();
            
            // 生成文件名
            String fileName = ExcelExportUtil.generateFileName("烫金图案基础信息");
            
            // 导出Excel
            return ExcelExportUtil.exportToResponse(allData, HotStampingPatternBase.class, fileName, "烫金图案基础信息");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

