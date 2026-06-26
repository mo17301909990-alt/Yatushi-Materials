package com.it.yts_project.controller;

import com.it.yts_project.dto.BatchNoticeUpdateRequest;
import com.it.yts_project.service.BatchNoticeUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 批量更新注意事项控制器
 */
@RestController
@RequestMapping("/batch-notice")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "http://120.26.101.0",
    "http://120.26.101.0:80",
    "http://120.26.101.0:8080",
    "http://120.26.101.0:3000"
}, allowCredentials = "true")
public class BatchNoticeUpdateController {

    @Autowired
    private BatchNoticeUpdateService batchNoticeUpdateService;

    /**
     * 批量更新指定规则类型的注意事项
     * @param ruleType 规则类型
     * @param request 批量更新请求
     * @return 更新结果
     */
    @PostMapping("/update/{ruleType}")
    public ResponseEntity<Map<String, Object>> batchUpdateNotices(
            @PathVariable String ruleType,
            @RequestBody BatchNoticeUpdateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (request.getIds() == null || request.getIds().isEmpty()) {
                response.put("success", false);
                response.put("message", "请选择要更新的规则");
                return ResponseEntity.badRequest().body(response);
            }

            int updatedCount = batchNoticeUpdateService.batchUpdateNotices(ruleType, request);

            response.put("success", true);
            response.put("message", "成功更新 " + updatedCount + " 条规则的注意事项");
            response.put("updatedCount", updatedCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
