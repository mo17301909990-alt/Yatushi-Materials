package com.it.yts_project.agent.skill;

import com.it.yts_project.model.AdminChangeRecord;
import com.it.yts_project.service.AdminChangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 管理员修改 Skill — 处理价格、规格等后台数据变更。
 * <p>由 {@link com.it.yts_project.agent.SimpleSkillRouter} 自动注册到 ADMIN_MODIFY 意图。
 * 通过 {@link AdminChangeService} 执行 preview → execute → rollback 流程。</p>
 *
 * <h3>参数约定（params）：</h3>
 * <ul>
 *   <li>{@code entityType} — 实体类型（pricing / specification）</li>
 *   <li>{@code entityId} — 实体主键</li>
 *   <li>{@code changes} — 变更字段 Map（{ "price": 12.5, "remark": "新备注" }）</li>
 *   <li>{@code risk} — 风险评估（low / medium / high）</li>
 *   <li>{@code reason} — 变更原因</li>
 *   <li>{@code operatorId} — 操作人 ID</li>
 *   <li>{@code operatorName} — 操作人姓名</li>
 *   <li>{@code mode} — "preview"（预览）或 "execute"（执行）</li>
 * </ul>
 */
@Component
public class AdminModifySkill implements ExecutableSkill {

    private static final Logger log = LoggerFactory.getLogger(AdminModifySkill.class);

    private final AdminChangeService adminChangeService;

    public AdminModifySkill(AdminChangeService adminChangeService) {
        this.adminChangeService = adminChangeService;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SkillResult<?> execute(Map<String, Object> params) {
        try {
            String entityType = (String) params.get("entityType");
            Object entityIdObj = params.get("entityId");
            Map<String, Object> changes = (Map<String, Object>) params.get("changes");
            Integer operatorId = params.get("operatorId") instanceof Number n ? n.intValue() : null;
            String operatorName = (String) params.get("operatorName");

            if (entityType == null || entityIdObj == null || changes == null || changes.isEmpty()) {
                return SkillResult.failure("参数不足：需要 entityType、entityId 和 changes");
            }

            Long entityId = entityIdObj instanceof Number n ? n.longValue() : Long.parseLong(entityIdObj.toString());
            String mode = (String) params.getOrDefault("mode", "execute");

            if ("preview".equals(mode)) {
                AdminChangeService.PreviewResult pr = adminChangeService.preview(entityType, entityId, changes, operatorId);
                Map<String, Object> data = Map.of(
                    "entityType", pr.entityType(),
                    "entityId", pr.entityId(),
                    "currentValues", pr.currentValues(),
                    "proposedChanges", pr.proposedChanges(),
                    "risk", pr.risk()
                );
                return SkillResult.success(List.of(data), 1, 100, "预览完成，请确认变更。风险等级: " + pr.risk());
            }

            String risk = (String) params.getOrDefault("risk", "low");
            String reason = (String) params.getOrDefault("reason", "");
            boolean confirmed = Boolean.TRUE.equals(params.get("confirmed"))
                    || "true".equals(String.valueOf(params.get("confirmed")));

            AdminChangeRecord record = adminChangeService.execute(
                    entityType, entityId, changes, risk, reason,
                    operatorId, operatorName, confirmed);
            Map<String, Object> resultData = Map.of(
                "recordId", record.getId(),
                "entityType", record.getEntityType(),
                "entityId", record.getEntityId(),
                "status", record.getStatus()
            );
            return SkillResult.success(List.of(resultData), 1, 100, "变更已成功执行 (记录 #" + record.getId() + ")");

        } catch (Exception e) {
            log.error("[AdminModifySkill] 执行失败", e);
            return SkillResult.failure("操作失败: " + e.getMessage());
        }
    }
}
