package com.it.yts_project.dto;

import lombok.Data;

import java.util.List;

/**
 * 过胶材料 匹配查询参数（支持多材料类型、多工序筛选）
 */
@Data
public class LaminationMaterialQueryParams {
    /** 搜索关键词（物料名称/编码/型号） */
    private String keyword;

    /** 材料类型筛选（BOPP/PET/BOPA等，多选） */
    private List<String> materialTypes;

    /** 后加工工序步骤名称筛选（多选） */
    private List<String> steps;

    /** 当前页码（从1开始） */
    private Integer page = 1;

    /** 每页条数 */
    private Integer size = 15;
}
