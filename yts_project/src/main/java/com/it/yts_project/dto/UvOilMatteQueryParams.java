package com.it.yts_project.dto;

import lombok.Data;

import java.util.List;

/**
 * UV油_哑光UV油 匹配查询参数
 */
@Data
public class UvOilMatteQueryParams {
    /** 搜索关键词（物料名称/编码/型号） */
    private String keyword;

    /** 后加工工序步骤名称（兼容旧版单步骤查询） */
    private String stepName;

    /** 后加工工序步骤列表（多步骤 INTERSECT 查询） */
    private List<String> steps;

    /** 当前页码（从1开始） */
    private Integer page = 1;

    /** 每页条数 */
    private Integer size = 15;
}
