package com.it.yts_project.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 後加工塗層面字典表实体类
 * 存储后加工涂层面分类字典（印刷过油/印刷工艺/过胶/烫金/丝印/植毛）
 */
@Data
public class ReferenceCoatingSurface {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 工艺大类：印刷过油/印刷工艺/过胶/烫金/丝印/植毛
     */
    private String category;

    /**
     * 子类别（可选，如精裝書/平裝書等）
     */
    private String subCategory;

    /**
     * 详细工艺名称
     */
    private String detailName;

    /**
     * 显示排序
     */
    private Integer displayOrder;

    /**
     * 是否启用
     */
    private Boolean isActive;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
