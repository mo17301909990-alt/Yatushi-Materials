package com.it.yts_project.controller;

import com.it.yts_project.model.AdminChangeRecord;
import com.it.yts_project.model.AdminChangeSnapshot;
import com.it.yts_project.service.AdminChangeService;
import com.it.yts_project.util.CurrentUserHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员变更管理控制器 — 预览/执行/回滚/查询变更。
 * <p>所有接口限制管理员角色（由 ApiAuthorizationFilter 守卫）。</p>
 */
@RestController
@RequestMapping("/api/agent/admin")
public class AdminChangeController {

    private static final Logger log = LoggerFactory.getLogger(AdminChangeController.class);

    private final AdminChangeService adminChangeService;

    public AdminChangeController(AdminChangeService adminChangeService) {
        this.adminChangeService = adminChangeService;
    }

    /** 预览变更：查询当前值并计算风险等级 */
    @PostMapping("/preview")
    public ResponseEntity<?> preview(@RequestBody Map<String, Object> body) {
        try {
            String entityType = (String) body.get("entityType");
            Number entityIdNum = (Number) body.get("entityId");
            @SuppressWarnings("unchecked")
            Map<String, Object> changes = (Map<String, Object>) body.get("changes");

            if (entityType == null || entityIdNum == null || changes == null || changes.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "参数不足：需要 entityType、entityId、changes"));
            }

            Integer operatorId = CurrentUserHolder.getOperatorId();
            AdminChangeService.PreviewResult pr = adminChangeService.preview(entityType, entityIdNum.longValue(), changes, operatorId);
            return ResponseEntity.ok(Map.of(
                "entityType", pr.entityType(),
                "entityId", pr.entityId(),
                "currentValues", pr.currentValues(),
                "proposedChanges", pr.proposedChanges(),
                "risk", pr.risk()
            ));
        } catch (Exception e) {
            log.error("预览失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** 执行变更：建快照 → 写入 → 记录 */
    @PostMapping("/execute")
    public ResponseEntity<?> execute(@RequestBody Map<String, Object> body) {
        try {
            String entityType = (String) body.get("entityType");
            Number entityIdNum = (Number) body.get("entityId");
            @SuppressWarnings("unchecked")
            Map<String, Object> changes = (Map<String, Object>) body.get("changes");
            String risk = (String) body.getOrDefault("risk", "low");
            String reason = (String) body.getOrDefault("reason", "");
            boolean confirmed = Boolean.TRUE.equals(body.get("confirmed"))
                    || "true".equals(String.valueOf(body.get("confirmed")));

            if (entityType == null || entityIdNum == null || changes == null || changes.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "参数不足：需要 entityType、entityId、changes"));
            }

            Integer operatorId = CurrentUserHolder.getOperatorId();
            String operatorName = CurrentUserHolder.getOperatorName();

            AdminChangeRecord record = adminChangeService.execute(
                    entityType, entityIdNum.longValue(), changes, risk, reason,
                    operatorId, operatorName, confirmed);
            return ResponseEntity.ok(Map.of(
                "recordId", record.getId(),
                "entityType", record.getEntityType(),
                "entityId", record.getEntityId(),
                "status", record.getStatus(),
                "message", "变更已执行"
            ));
        } catch (Exception e) {
            log.error("执行变更失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** 回滚指定变更 */
    @PostMapping("/rollback/{recordId}")
    public ResponseEntity<?> rollback(@PathVariable Long recordId) {
        try {
            Integer operatorId = CurrentUserHolder.getOperatorId();
            String operatorName = CurrentUserHolder.getOperatorName();

            AdminChangeRecord record = adminChangeService.rollback(recordId, operatorId, operatorName);
            return ResponseEntity.ok(Map.of(
                "recordId", record.getId(),
                "status", record.getStatus(),
                "message", "回滚成功"
            ));
        } catch (Exception e) {
            log.error("回滚失败 recordId={}", recordId, e);
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** 查询变更历史 */
    @GetMapping("/changes")
    public ResponseEntity<Map<String, Object>> queryChanges(
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize) {
        Integer operatorId = CurrentUserHolder.getOperatorId();
        return ResponseEntity.ok(adminChangeService.queryChanges(operatorId, entityType, status, pageNo, pageSize));
    }

    /** 查询单条变更详情 + 关联快照 */
    @GetMapping("/changes/{recordId}")
    public ResponseEntity<?> getChangeDetail(@PathVariable Long recordId) {
        try {
            AdminChangeRecord record = adminChangeService.getRecord(recordId);
            if (record == null) {
                return ResponseEntity.notFound().build();
            }
            List<AdminChangeSnapshot> snapshots = adminChangeService.getSnapshots(recordId);
            return ResponseEntity.ok(Map.of(
                "record", record,
                "snapshots", snapshots
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /** 搜索实体（按名称模糊匹配） */
    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(adminChangeService.searchEntity(keyword));
    }
}
