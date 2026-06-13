package com.it.yts_project.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 印刷油墨面字典表实体类
 * 存储印刷油墨面分类字典（普通油墨/UV系油墨/特种油墨）
 */
@Data
public class ReferenceInkSurface {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 分组：普通油墨/UV系油墨/特种油墨
     */
    private String category;

    /**
     * 油墨名称
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
