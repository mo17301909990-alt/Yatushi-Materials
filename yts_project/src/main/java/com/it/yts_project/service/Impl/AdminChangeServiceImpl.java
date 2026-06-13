package com.it.yts_project.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.yts_project.mapper.AdminChangeMapper;
import com.it.yts_project.mapper.PricingMapper;
import com.it.yts_project.mapper.ProductMapper;
import com.it.yts_project.mapper.SpecificationMapper;
import com.it.yts_project.model.AdminChangeRecord;
import com.it.yts_project.model.AdminChangeSnapshot;
import com.it.yts_project.model.Pricing;
import com.it.yts_project.model.Product;
import com.it.yts_project.model.Specification;
import com.it.yts_project.service.AdminChangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class AdminChangeServiceImpl implements AdminChangeService {

    private static final Logger log = LoggerFactory.getLogger(AdminChangeServiceImpl.class);

    private final AdminChangeMapper changeMapper;
    private final PricingMapper pricingMapper;
    private final SpecificationMapper specificationMapper;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;

    public AdminChangeServiceImpl(AdminChangeMapper changeMapper,
                                  PricingMapper pricingMapper,
                                  SpecificationMapper specificationMapper,
                                  ProductMapper productMapper,
                                  ObjectMapper objectMapper) {
        this.changeMapper = changeMapper;
        this.pricingMapper = pricingMapper;
        this.specificationMapper = specificationMapper;
        this.productMapper = productMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public PreviewResult preview(String entityType, Long entityId, Map<String, Object> changes, Integer operatorId) {
        Map<String, Object> current = fetchCurrent(entityType, entityId);

        String risk = assessRiskWithValues(entityType, current, changes);
        return new PreviewResult(entityType, entityId, current, changes, risk);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdminChangeRecord execute(String entityType, Long entityId, Map<String, Object> changes,
                                      String risk, String reason, Integer operatorId, String operatorName,
                                      boolean confirmed) {
        // 1. 读取当前数据
        Map<String, Object> current = fetchCurrent(entityType, entityId);
        if (current == null || current.isEmpty()) {
            throw new IllegalArgumentException("实体不存在: " + entityType + "/" + entityId);
        }

        // 2. 服务端重新评估风险—不信任客户端传入的 risk 等级
        String actualRisk = assessRiskWithValues(entityType, current, changes);
        if ("high".equals(actualRisk) && !confirmed) {
            throw new IllegalArgumentException("高风险变更（变动幅度超过 10%）必须确认后才能执行。请先调用 preview 确认后再试。");
        }
        String finalRisk = actualRisk;

        // 2. 计算变更集 + 建快照
        String beforeJson = toJson(current);
        String checksum = sha256(beforeJson);
        Map<String, Object> changeSet = buildChangeSet(current, changes);

        AdminChangeSnapshot snapshot = AdminChangeSnapshot.builder()
                .entityType(entityType)
                .entityId(entityId)
                .beforeData(beforeJson)
                .checksum(checksum)
                .createdBy(operatorId)
                .build();
        changeMapper.insertSnapshot(snapshot);

        // 3. 写入变更
        applyChanges(entityType, entityId, changes);

        // 4. 更新快照的 after_data
        Map<String, Object> after = fetchCurrent(entityType, entityId);
        changeMapper.updateSnapshotAfterData(snapshot.getId(), toJson(after));

        // 5. 记录变更记录
        AdminChangeRecord record = AdminChangeRecord.builder()
                .entityType(entityType)
                .entityId(entityId)
                .changeSet(toJson(changeSet))
                .snapshotIds(String.valueOf(snapshot.getId()))
                .operatorId(operatorId)
                .operatorName(operatorName)
                .risk(finalRisk)
                .reason(reason != null ? reason : "")
                .status("committed")
                .build();
        changeMapper.insertRecord(record);

        // 6. 更新状态为 committed
        changeMapper.updateRecordStatus(record.getId(), "committed", null);

        log.info("[AdminChange] 变更已执行: type={} id={} recordId={} operator={}",
                entityType, entityId, record.getId(), operatorName);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdminChangeRecord rollback(Long recordId, Integer operatorId, String operatorName) {
        AdminChangeRecord record = changeMapper.findRecordById(recordId);
        if (record == null) {
            throw new IllegalArgumentException("变更记录不存在: " + recordId);
        }
        if (!"committed".equals(record.getStatus())) {
            throw new IllegalStateException("仅 committed 状态的记录可回滚，当前状态: " + record.getStatus());
        }

        // 读取快照
        List<AdminChangeSnapshot> snapshots = changeMapper.findSnapshotsByRecordId(recordId);
        if (snapshots.isEmpty()) {
            throw new IllegalStateException("关联快照不存在，无法回滚: " + recordId);
        }

        // 逐条回滚（逆序）
        for (int i = snapshots.size() - 1; i >= 0; i--) {
            AdminChangeSnapshot snap = snapshots.get(i);
            Map<String, Object> beforeData = parseJson(snap.getBeforeData());

            // SHA-256 校验
            String currentChecksum = sha256(toJson(fetchCurrent(snap.getEntityType(), snap.getEntityId())));
            if (!snap.getChecksum().equals(currentChecksum)) {
                throw new IllegalStateException("数据已被修改，回滚中止: entity="
                        + snap.getEntityType() + "/" + snap.getEntityId());
            }

            // 写入回滚值
            applyMapToEntity(snap.getEntityType(), snap.getEntityId(), beforeData);
        }

        // 创建回滚记录
        AdminChangeRecord rollbackRecord = AdminChangeRecord.builder()
                .entityType(record.getEntityType())
                .entityId(record.getEntityId())
                .changeSet("{\"rollback\":true,\"fromRecordId\":" + recordId + "}")
                .snapshotIds("")
                .operatorId(operatorId)
                .operatorName(operatorName)
                .risk("medium")
                .reason("回滚操作: " + record.getReason())
                .status("committed")
                .build();
        changeMapper.insertRecord(rollbackRecord);

        // 标记原记录回滚
        changeMapper.updateRecordStatus(recordId, "rolled_back", null);
        changeMapper.markRollback(recordId, rollbackRecord.getId());

        log.info("[AdminChange] 回滚完成: recordId={} rollbackRecordId={} operator={}",
                recordId, rollbackRecord.getId(), operatorName);
        return rollbackRecord;
    }

    @Override
    public Map<String, Object> queryChanges(Integer operatorId, String entityType, String status,
                                             int pageNo, int pageSize) {
        int offset = (pageNo - 1) * pageSize;
        List<AdminChangeRecord> list = changeMapper.findRecords(operatorId, entityType, status, offset, pageSize);
        int total = changeMapper.countRecords(operatorId, entityType, status);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public AdminChangeRecord getRecord(Long recordId) {
        return changeMapper.findRecordById(recordId);
    }

    @Override
    public List<AdminChangeSnapshot> getSnapshots(Long recordId) {
        return changeMapper.findSnapshotsByRecordId(recordId);
    }

    @Override
    public List<Map<String, Object>> searchEntity(String keyword) {
        if (keyword == null || keyword.isBlank()) return List.of();
        List<Product> products = productMapper.searchProducts(keyword.trim());
        if (products.isEmpty()) return List.of();
        return products.stream().limit(10).map(p -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("entityType", "product");
            m.put("entityId", p.getId());
            m.put("name", p.getName());
            m.put("modelNumber", p.getModelNumber());
            return m;
        }).toList();
    }

    // ========== 内部方法 ==========

    private Map<String, Object> fetchCurrent(String entityType, Long entityId) {
        return switch (entityType) {
            case "pricing" -> {
                Pricing p = pricingMapper.findById(entityId.intValue());
                yield p == null ? Collections.emptyMap() : objectMapper.convertValue(p, Map.class);
            }
            case "specification" -> {
                Specification s = specificationMapper.findById(entityId.intValue());
                yield s == null ? Collections.emptyMap() : objectMapper.convertValue(s, Map.class);
            }
            // 其他实体类型按需扩展
            default -> throw new IllegalArgumentException("不支持的实体类型: " + entityType);
        };
    }

    private void applyChanges(String entityType, Long entityId, Map<String, Object> changes) {
        switch (entityType) {
            case "pricing" -> {
                Pricing p = pricingMapper.findById(entityId.intValue());
                if (p == null) throw new IllegalArgumentException("价格记录不存在: " + entityId);
                changes.forEach((key, val) -> {
                    switch (key) {
                        case "price" -> p.setPrice(val instanceof Number ? java.math.BigDecimal.valueOf(((Number) val).doubleValue()) : null);
                        default -> log.warn("未知 pricing 字段: {}", key);
                    }
                });
                pricingMapper.update(p);
            }
            case "specification" -> {
                Specification s = specificationMapper.findById(entityId.intValue());
                if (s == null) throw new IllegalArgumentException("规格记录不存在: " + entityId);
                changes.forEach((key, val) -> {
                    switch (key) {
                        case "color" -> s.setColor(val instanceof String ? (String) val : null);
                        case "size" -> s.setSize(val instanceof String ? (String) val : null);
                        case "tightness" -> s.setTightness(val instanceof String ? (String) val : null);
                        case "temperatureRange" -> s.setTemperatureRange(val instanceof String ? (String) val : null);
                        case "performance" -> s.setPerformance(val instanceof String ? (String) val : null);
                        default -> log.warn("未知 specification 字段: {}", key);
                    }
                });
                specificationMapper.update(s);
            }
            default -> throw new IllegalArgumentException("不支持的实体类型: " + entityType);
        }
    }

    private void applyMapToEntity(String entityType, Long entityId, Map<String, Object> values) {
        // 回滚时直接覆盖所有字段
        applyChanges(entityType, entityId, values);
    }

    private Map<String, Object> buildChangeSet(Map<String, Object> current, Map<String, Object> changes) {
        Map<String, Object> cs = new LinkedHashMap<>();
        changes.forEach((key, newVal) -> {
            Object oldVal = current.get(key);
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("old", oldVal);
            entry.put("new", newVal);
            cs.put(key, entry);
        });
        return cs;
    }

    /**
     * 风险分级（无当前值，仅按实体类型和变更特征粗判）。
     * <p>preview/execute 内部会使用 {@link #assessRiskWithValues} 基于实际数值重新计算。</p>
     */
    String assessRisk(String entityType, Map<String, Object> changes) {
        if ("pricing".equals(entityType) && changes.containsKey("price")) return "medium";
        if (changes.containsKey("delete") || changes.containsKey("DELETE")) return "high";
        if (changes.size() > 10) return "high";
        return "low";
    }

    /**
     * 基于实际当前值与新值计算精确风险等级。
     * <p>preview 和 execute 内部调用此方法替代粗判。</p>
     */
    private String assessRiskWithValues(String entityType, Map<String, Object> current, Map<String, Object> changes) {
        if (changes.containsKey("price") && current.containsKey("price")) {
            Object oldObj = current.get("price");
            Object newObj = changes.get("price");
            if (oldObj instanceof Number oldNum && newObj instanceof Number newNum) {
                double oldVal = oldNum.doubleValue();
                if (oldVal == 0) return "medium";
                double pct = Math.abs((newNum.doubleValue() - oldVal) / oldVal);
                if (pct > 0.10) return "high";
                if (pct > 0) return "medium";
            }
            return "medium";
        }
        if (changes.containsKey("delete") || changes.containsKey("DELETE")) {
            return "high";
        }
        if (changes.size() > 10) return "high";
        return "low";
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 序列化失败", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> parseJson(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 解析失败", e);
        }
    }

    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
