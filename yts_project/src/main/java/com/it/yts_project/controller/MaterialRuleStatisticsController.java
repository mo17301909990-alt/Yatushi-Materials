package com.it.yts_project.controller;

import com.it.yts_project.service.MaterialRuleStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 物料規則 Hub（/admin/material-rule-management）統計：單次請求返回計數，避免前端拉全表。
 */
@RestController
@RequestMapping("/api/material-rule")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://120.26.101.0",
        "http://120.26.101.0:80",
        "http://120.26.101.0:8080",
        "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class MaterialRuleStatisticsController {

    @Autowired
    private MaterialRuleStatisticsService materialRuleStatisticsService;

    @GetMapping("/statistics/overview")
    public ResponseEntity<Map<String, Long>> overview() {
        return ResponseEntity.ok(materialRuleStatisticsService.getOverviewCounts());
    }
}
