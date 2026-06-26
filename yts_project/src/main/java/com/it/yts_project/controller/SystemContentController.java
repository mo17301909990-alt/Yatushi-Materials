package com.it.yts_project.controller;

import com.it.yts_project.dto.SystemContentUpdateRequest;
import com.it.yts_project.model.SystemContent;
import com.it.yts_project.service.SystemContentService;
import com.it.yts_project.util.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 系統可編輯內容（操作指南等）。GET/PUT 的具體校驗見 {@link com.it.yts_project.config.ApiAuthorizationFilter}：
 * GET 需管理員或 system:guide:view 或 material:hot-stamping-material:view；PUT 需管理員或 system:guide:edit。
 */
@RestController
@RequestMapping("/api/system-content")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://120.26.101.0",
        "http://120.26.101.0:80",
        "http://120.26.101.0:8080",
        "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class SystemContentController {

    @Autowired
    private SystemContentService systemContentService;

    @GetMapping("/{contentKey}")
    public ResponseEntity<?> getByKey(@PathVariable String contentKey) {
        SystemContent c = systemContentService.getByKey(contentKey);
        if (c == null) {
            Map<String, String> err = new HashMap<>();
            err.put("message", "Content not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(c);
    }

    @PutMapping("/{contentKey}")
    public ResponseEntity<?> updateByKey(@PathVariable String contentKey, @RequestBody SystemContentUpdateRequest req) {
        Integer uid = CurrentUserHolder.getOperatorId();
        if (uid == null) {
            Map<String, String> err = new HashMap<>();
            err.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(err);
        }
        try {
            SystemContent updated = systemContentService.updateByKey(contentKey, req, uid);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            Map<String, String> err = new HashMap<>();
            err.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }
}
