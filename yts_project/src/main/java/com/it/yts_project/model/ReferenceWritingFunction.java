package com.it.yts_project.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 表面写字功能字典表实体类
 * 存储表面写字功能分类字典（油性笔/水性笔/其它笔）
 */
@Data
public class ReferenceWritingFunction {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 笔类型：油性笔/水性笔/其它笔
     */
    private String category;

    /**
     * 具体笔种
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
