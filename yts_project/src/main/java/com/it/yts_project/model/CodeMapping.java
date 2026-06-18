package com.it.yts_project.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 物料编码映射实体类
 * 对应数据库表: code_mapping
 * 连接 P0 知识库与生产系统的物料编码
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeMapping {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 来源模块表名，如 silicone_glow_ink_product
     */
    private String p0TableName;

    /**
     * 来源行 ID
     */
    private Integer p0RowId;

    /**
     * P0 物料编码
     */
    private String p0MaterialCode;

    /**
     * P0 物料名称
     */
    private String p0MaterialName;

    /**
     * 目标类型: material_catalog / paper_performance
     */
    private String targetType;

    /**
     * material_catalog.id 或 NULL
     */
    private Integer targetId;

    /**
     * 目标编码
     */
    private String targetCode;

    /**
     * 目标名称
     */
    private String targetName;

    /**
     * 匹配方式: auto / manual / verified
     */
    private String matchType;

    /**
     * 置信度 0~1
     */
    private java.math.BigDecimal confidence;

    /**
     * 备注
     */
    private String notes;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
