package com.it.yts_project.dto;

import com.it.yts_project.model.UvOilMatteCompatibility;
import com.it.yts_project.model.UvOilMatteProduct;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * UV油_哑光UV油 产品详情DTO（含兼容性列表）
 * 同时作为匹配查询结果行，包含产品字段 + 兼容性状态
 */
@Data
public class UvOilMatteProductDTO {

    /** 主键ID */
    private Integer id;

    /** 物料编码 */
    private String materialCode;

    /** 供应商编码 */
    private String supplierCode;

    /** 存仓物料号/型号 */
    private String stockCode;

    /** 物料名称 */
    private String materialName;

    /** 物料用途 */
    private String usage;

    /** 材质分类 */
    private String category;

    /** 颜色 */
    private String color;

    /** 厚度 */
    private String thickness;

    /** 形状 */
    private String shape;

    /** 测试员 */
    private String responsiblePerson;

    /** 用纸尺寸-最小 */
    private String minSheetSize;

    /** 用纸尺寸-最大 */
    private String maxSheetSize;

    /** 间距-最小 */
    private String minSpacing;

    /** 设计限制信息 */
    private String designInfo;

    /** 适用界面 */
    private String applicableInterface;

    /** 备注 */
    private String notes;

    /** 是否激活 */
    private Boolean isActive;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /** 兼容性状态（V/X/null），仅单步骤匹配查询时返回 */
    private String compatibilityStatus;

    /** 多步骤匹配时，每步骤的兼容性状态映射（step -> V/X/null） */
    private Map<String, String> compatibilityStatusMap;

    /** 该产品的所有兼容性配置，仅详情查询时返回 */
    private List<UvOilMatteCompatibility> compatibilities;

    /** 从产品模型创建DTO */
    public static UvOilMatteProductDTO fromProduct(UvOilMatteProduct p) {
        UvOilMatteProductDTO dto = new UvOilMatteProductDTO();
        dto.setId(p.getId());
        dto.setMaterialCode(p.getMaterialCode());
        dto.setSupplierCode(p.getSupplierCode());
        dto.setStockCode(p.getStockCode());
        dto.setMaterialName(p.getMaterialName());
        dto.setUsage(p.getUsage());
        dto.setCategory(p.getCategory());
        dto.setColor(p.getColor());
        dto.setThickness(p.getThickness());
        dto.setShape(p.getShape());
        dto.setResponsiblePerson(p.getResponsiblePerson());
        dto.setMinSheetSize(p.getMinSheetSize());
        dto.setMaxSheetSize(p.getMaxSheetSize());
        dto.setMinSpacing(p.getMinSpacing());
        dto.setDesignInfo(p.getDesignInfo());
        dto.setApplicableInterface(p.getApplicableInterface());
        dto.setNotes(p.getNotes());
        dto.setIsActive(p.getIsActive());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());
        return dto;
    }
}
