package com.it.yts_project.dto;

import lombok.Data;

import java.util.List;

/**
 * 分页查询结果
 * @param <T> 数据类型
 */
@Data
public class PagedResult<T> {
    private List<T> items;      // 当前页数据
    private Long total;         // 总记录数
    private Integer pageSize;  // 每页大小
    private Integer currentPage; // 当前页码
    private Integer totalPages; // 总页数

    public PagedResult() {
    }

    public PagedResult(List<T> items, Long total, Integer pageSize, Integer currentPage) {
        this.items = items;
        this.total = total;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }
}

