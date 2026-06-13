package com.it.yts_project.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 产品家族字典表实体类
 * 用于存储产品分类体系：大类 > 子类 > 详细名称
 */
@Data
public class ReferenceProductFamily {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 大类：书版/咭书/活页书/盒/拼图等
     */
    private String category;

    /**
     * 子类
     */
    private String subCategory;

    /**
     * 详细名称
     */
    private String detailName;

    /**
     * 显示排序
     */
    private Integer displayOrder;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
