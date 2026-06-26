package com.it.yts_project.util;

import com.it.yts_project.model.ProductTypeSortConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 产品类型排序配置「从 XXX 修改为 XXX」的变更详情，便于操作日志记录具体改动。
 */
public final class ProductTypeSortConfigChangeDetailBuilder {

    public static String build(ProductTypeSortConfig before, ProductTypeSortConfig after) {
        if (after == null) return null;
        List<String> parts = new ArrayList<>();
        addDiff(parts, "配置描述", before != null ? before.getDescription() : null, after.getDescription());
        addDiffBool(parts, "启用耐磨金纸优先", before != null ? before.getEnableWearResistantPriority() : null, after.getEnableWearResistantPriority());
        addDiff(parts, "耐磨金纸类型", before != null ? before.getWearResistantPaperTypes() : null, after.getWearResistantPaperTypes());
        addDiffBool(parts, "是否激活", before != null ? before.getIsActive() : null, after.getIsActive());
        if (parts.isEmpty()) return null;
        return String.join("; ", parts);
    }

    private static void addDiff(List<String> parts, String label, String oldVal, String newVal) {
        String o = oldVal == null ? "" : oldVal.trim();
        String n = newVal == null ? "" : newVal.trim();
        if (Objects.equals(o, n)) return;
        parts.add(label + ": " + (o.isEmpty() ? "(空)" : o) + " → " + (n.isEmpty() ? "(空)" : n));
    }

    private static void addDiffBool(List<String> parts, String label, Boolean oldVal, Boolean newVal) {
        if (Objects.equals(oldVal, newVal)) return;
        String o = oldVal != null && Boolean.TRUE.equals(oldVal) ? "是" : "否";
        String n = newVal != null && Boolean.TRUE.equals(newVal) ? "是" : "否";
        parts.add(label + ": " + o + " → " + n);
    }
}
