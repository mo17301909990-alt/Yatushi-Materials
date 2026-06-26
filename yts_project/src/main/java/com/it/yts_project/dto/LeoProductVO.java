package com.it.yts_project.dto;

import com.it.yts_project.model.LeoBookBoardProduct;
import com.it.yts_project.model.LeoFlatProduct;
import com.it.yts_project.model.LeoNonFlatProduct;
import com.it.yts_project.model.LeoBookBoardCompatibility;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * LEO纸品 统一产品视图对象
 * 用于组合匹配查询结果，包含来源类型标识
 */
@Data
public class LeoProductVO {

    /** 主键ID（原始表ID） */
    private Integer id;

    /** 来源类型：book_board / flat / non_flat */
    private String sourceType;

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

    /** 负责人/测试员 */
    private String responsiblePerson;

    /** 厚度 */
    private String thickness;

    /** 形状/克重 */
    private String shape;

    /** 检测员 */
    private String tester;

    /** 用纸尺寸-最小 */
    private String minSheetSize;

    /** 用纸尺寸-最大 */
    private String maxSheetSize;

    /** 间距-最小 */
    private String minSpacing;

    /** 间距-最大 */
    private String maxSpacing;

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

    /** 多步骤匹配时，每步骤的兼容性状态映射（step -> V/X/null） */
    private Map<String, String> compatibilityStatusMap;

    /** 该产品的所有兼容性配置，仅详情查询时返回 */
    private List<LeoBookBoardCompatibility> compatibilities;

    /** 从书板产品创建 */
    public static LeoProductVO fromBookBoardProduct(LeoBookBoardProduct p) {
        return fromBase(p.getId(), p.getMaterialCode(), p.getSupplierCode(), p.getStockCode(),
            p.getMaterialName(), p.getUsage(), p.getCategory(), p.getColor(),
            p.getResponsiblePerson(), p.getThickness(), p.getShape(), p.getTester(),
            p.getMinSheetSize(), p.getMaxSheetSize(), p.getMinSpacing(), p.getMaxSpacing(),
            p.getDesignInfo(), p.getApplicableInterface(), p.getNotes(), p.getIsActive(),
            p.getCreatedAt(), p.getUpdatedAt());
    }

    /** 从平面产品创建 */
    public static LeoProductVO fromFlatProduct(LeoFlatProduct p) {
        return fromBase(p.getId(), p.getMaterialCode(), p.getSupplierCode(), p.getStockCode(),
            p.getMaterialName(), p.getUsage(), p.getCategory(), p.getColor(),
            p.getResponsiblePerson(), p.getThickness(), p.getShape(), p.getTester(),
            p.getMinSheetSize(), p.getMaxSheetSize(), p.getMinSpacing(), p.getMaxSpacing(),
            p.getDesignInfo(), p.getApplicableInterface(), p.getNotes(), p.getIsActive(),
            p.getCreatedAt(), p.getUpdatedAt());
    }

    /** 从非平面产品创建 */
    public static LeoProductVO fromNonFlatProduct(LeoNonFlatProduct p) {
        return fromBase(p.getId(), p.getMaterialCode(), p.getSupplierCode(), p.getStockCode(),
            p.getMaterialName(), p.getUsage(), p.getCategory(), p.getColor(),
            p.getResponsiblePerson(), p.getThickness(), p.getShape(), p.getTester(),
            p.getMinSheetSize(), p.getMaxSheetSize(), p.getMinSpacing(), p.getMaxSpacing(),
            p.getDesignInfo(), p.getApplicableInterface(), p.getNotes(), p.getIsActive(),
            p.getCreatedAt(), p.getUpdatedAt());
    }

    private static LeoProductVO fromBase(
            Integer id, String materialCode, String supplierCode, String stockCode,
            String materialName, String usage, String category, String color,
            String responsiblePerson, String thickness, String shape, String tester,
            String minSheetSize, String maxSheetSize, String minSpacing, String maxSpacing,
            String designInfo, String applicableInterface, String notes, Boolean isActive,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        LeoProductVO vo = new LeoProductVO();
        vo.setId(id);
        vo.setMaterialCode(materialCode);
        vo.setSupplierCode(supplierCode);
        vo.setStockCode(stockCode);
        vo.setMaterialName(materialName);
        vo.setUsage(usage);
        vo.setCategory(category);
        vo.setColor(color);
        vo.setResponsiblePerson(responsiblePerson);
        vo.setThickness(thickness);
        vo.setShape(shape);
        vo.setTester(tester);
        vo.setMinSheetSize(minSheetSize);
        vo.setMaxSheetSize(maxSheetSize);
        vo.setMinSpacing(minSpacing);
        vo.setMaxSpacing(maxSpacing);
        vo.setDesignInfo(designInfo);
        vo.setApplicableInterface(applicableInterface);
        vo.setNotes(notes);
        vo.setIsActive(isActive);
        vo.setCreatedAt(createdAt);
        vo.setUpdatedAt(updatedAt);
        return vo;
    }
}
