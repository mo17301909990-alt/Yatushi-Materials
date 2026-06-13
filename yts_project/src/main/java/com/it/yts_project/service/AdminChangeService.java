package com.it.yts_project.service;

import com.it.yts_project.model.AdminChangeRecord;
import com.it.yts_project.model.AdminChangeSnapshot;

import java.util.List;
import java.util.Map;

/**
 * 管理员变更管理服务 — 预览/执行/回滚/查询变更历史。
 * <p>所有写操作前先建快照，执行时事务写入，支持按快照精确回滚。</p>
 */
public interface AdminChangeService {

    /** 预览变更：查询当前值并计算 diff，不写入数据库 */
    PreviewResult preview(String entityType, Long entityId, Map<String, Object> changes, Integer operatorId);

    /** 执行变更：建快照 → 事务写入 → 记录变更 */
    AdminChangeRecord execute(String entityType, Long entityId, Map<String, Object> changes,
                              String risk, String reason, Integer operatorId, String operatorName,
                              boolean confirmed);

    /** 回滚指定变更记录 */
    AdminChangeRecord rollback(Long recordId, Integer operatorId, String operatorName);

    /** 查询变更历史 */
    Map<String, Object> queryChanges(Integer operatorId, String entityType, String status,
                                     int pageNo, int pageSize);

    /** 查询单条记录详情 */
    AdminChangeRecord getRecord(Long recordId);

    /** 查询记录关联的快照 */
    List<AdminChangeSnapshot> getSnapshots(Long recordId);

    /** 预览结果 */
    record PreviewResult(String entityType, Long entityId, Map<String, Object> currentValues,
                         Map<String, Object> proposedChanges, String risk) {}

    /** 搜索实体（按名称模糊匹配，用于自然语言输入兜底） */
    List<Map<String, Object>> searchEntity(String keyword);
}
