package com.it.yts_project.controller;

import com.it.yts_project.service.WearResistantGoldPaperSkipConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 耐磨金纸映射「跳过 Match 耐磨规则」配置控制器
 * 仅维护一份烫金纸类型列表，用于在 Match 中跳过耐磨映射规则
 */
@RestController
@RequestMapping("/api/wear-resistant-gold-paper-skip-config")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://120.26.101.0",
        "http://120.26.101.0:80",
        "http://120.26.101.0:8080",
        "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class WearResistantGoldPaperSkipConfigController {

    @Autowired
    private WearResistantGoldPaperSkipConfigService skipConfigService;

    /**
     * 获取所有需要跳过耐磨映射的烫金纸类型
     */
    @GetMapping
    public ResponseEntity<List<String>> getAllSkipPaperTypes() {
        List<String> types = skipConfigService.getAllSkipPaperTypes();
        return ResponseEntity.ok(types);
    }

    /**
     * 保存需要跳过耐磨映射的烫金纸类型（全量覆盖）
     */
    @PutMapping
    public ResponseEntity<Void> saveSkipPaperTypes(@RequestBody List<String> paperTypes) {
        skipConfigService.saveSkipPaperTypes(paperTypes);
        return ResponseEntity.ok().build();
    }
}



