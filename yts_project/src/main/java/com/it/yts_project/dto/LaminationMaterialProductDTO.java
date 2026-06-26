package com.it.yts_project.dto;

import com.it.yts_project.model.LaminationMaterialCompatibility;
import com.it.yts_project.model.LaminationMaterialProduct;
import lombok.Data;

import java.util.List;

/**
 * 过胶材料 产品详情DTO（含兼容性列表）
 */
@Data
public class LaminationMaterialProductDTO {
    /** 产品基本信息 */
    private LaminationMaterialProduct product;

    /** 该产品的所有兼容性配置 */
    private List<LaminationMaterialCompatibility> compatibilities;
}
