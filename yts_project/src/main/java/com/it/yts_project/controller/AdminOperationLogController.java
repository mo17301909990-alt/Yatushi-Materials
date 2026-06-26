package com.it.yts_project.controller;

import com.it.yts_project.service.AdminOperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 后台操作日志：仅提供分页查询，供管理员查看「谁在何时改了什么」
 */
@Slf4j
@RestController
@RequestMapping(value = { "/api/admin-operation-log", "/admin-operation-log" })
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class AdminOperationLogController {

    @Autowired
    private AdminOperationLogService adminOperationLogService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(required = false) Integer operatorId,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize) {
        try {
            Map<String, Object> result = adminOperationLogService.list(operatorId, module, operationType, from, to, pageNo, pageSize);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("List operation log failed", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
