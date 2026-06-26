package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 兼容性查询整体结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompatibilityQueryResult {

    /** 匹配的物料产品列表 */
    private List<CompatibilityProductDTO> products;

    /** 生产验证统计数据 */
    private ProductionStats productionStats;

    /**
     * 生产验证统计 — 从 material_process_compatibility 和 hot_stamping_compatibility 汇总
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductionStats {
        /** 物料名称 */
        private String materialName;

        /** 已验证兼容次数（V） */
        private int verifiedCount;

        /** 已验证不兼容次数（X） */
        private int notCompatibleCount;

        /** 总验证次数 */
        private int totalCount;
    }
}
