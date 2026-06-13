package com.it.yts_project.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 纸張面字典表实体类
 * 存储纸张面分类字典（有粉纸/无粉纸/胶底纸/布料/塑胶/其它）
 */
@Data
public class ReferencePaperSurface {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 大类：有粉纸/无粉纸/胶底纸/布料/塑膠/其它
     */
    private String category;

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
