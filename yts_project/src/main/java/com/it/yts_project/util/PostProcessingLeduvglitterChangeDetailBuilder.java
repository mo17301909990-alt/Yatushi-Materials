package com.it.yts_project.util;

import com.it.yts_project.model.PostProcessingLeduvglitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** 烫后加工(LED UV灑閃粉) 变更详情：从 XXX 修改为 XXX */
public final class PostProcessingLeduvglitterChangeDetailBuilder {

    public static String build(PostProcessingLeduvglitter before, PostProcessingLeduvglitter after) {
        if (after == null) return null;
        List<String> parts = new ArrayList<>();
        addDiff(parts, "燙金紙系列", before != null ? before.getProductName() : null, after.getProductName());
        addDiff(parts, "產品型號", before != null ? before.getProductModelNumber() : null, after.getProductModelNumber());
        addDiff(parts, "布面紙類型", before != null ? before.getClothPaperTypeName() : null, after.getClothPaperTypeName());
        addDiff(parts, "燙金紙性能類型", before != null ? before.getPaperType() : null, after.getPaperType());
        addDiff(parts, "兼容性狀態", before != null ? before.getCompatibilityStatus() : null, after.getCompatibilityStatus());
        if (parts.isEmpty()) return null;
        return String.join("; ", parts);
    }

    private static void addDiff(List<String> parts, String label, String oldVal, String newVal) {
        String o = oldVal == null ? "" : (oldVal.trim());
        String n = newVal == null ? "" : (newVal.trim());
        if (Objects.equals(o, n)) return;
        parts.add(label + ": " + (o.isEmpty() ? "(空)" : o) + " → " + (n.isEmpty() ? "(空)" : n));
    }
}
