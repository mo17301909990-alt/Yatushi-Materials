package com.it.yts_project.dto;

import lombok.Data;

/**
 * 水油(哑光) 匹配查询参数
 */
@Data
public class WaterVarnishMatteQueryParams {
    /** 搜索关键词（物料名称/编码/型号） */
    private String keyword;

    /** 后加工工序步骤名称（用于筛选兼容该步骤的产品） */
    private String stepName;

    /** 当前页码（从1开始） */
    private Integer page = 1;

    /** 每页条数 */
    private Integer size = 15;
}
