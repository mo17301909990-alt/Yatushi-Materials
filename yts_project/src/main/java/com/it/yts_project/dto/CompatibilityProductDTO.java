package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 兼容性查询 — 物料产品及其兼容性信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompatibilityProductDTO {

    /** 产品ID */
    private Integer productId;

    /** 物料编码 */
    private String materialCode;

    /** 物料名称 */
    private String materialName;

    /** 所属模块名称（中文，如"硅胶发光油墨"） */
    private String moduleName;

    /** 材质分类 */
    private String category;

    /** 颜色 */
    private String color;

    /** 物料用途 */
    private String usage;

    /** 注意事项 */
    private String notes;

    /** 后加工工序兼容性列表 */
    private List<CompatibilityItem> compatibilities;

    /**
     * 单条兼容性记录
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompatibilityItem {
        /** 后加工工序名称 */
        private String postProcessingStep;

        /** 兼容性状态（V=兼容, X=不兼容, ▷=有条件兼容） */
        private String compatibilityStatus;

        /** 备注/条件说明 */
        private String remark;
    }
}
