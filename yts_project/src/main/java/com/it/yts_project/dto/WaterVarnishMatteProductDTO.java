package com.it.yts_project.dto;

import com.it.yts_project.model.WaterVarnishMatteCompatibility;
import com.it.yts_project.model.WaterVarnishMatteProduct;
import lombok.Data;

import java.util.List;

/**
 * 水油(哑光) 产品详情DTO（含兼容性列表）
 */
@Data
public class WaterVarnishMatteProductDTO {
    /** 产品基本信息 */
    private WaterVarnishMatteProduct product;

    /** 该产品的所有兼容性配置 */
    private List<WaterVarnishMatteCompatibility> compatibilities;
}
