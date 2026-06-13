package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 耐磨金纸映射「跳过 Match 耐磨规则」配置实体
 * 按烫金纸类型（product.hot_stamping_paper_type）控制是否在 Match 中跳过耐磨映射
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WearResistantGoldPaperSkipConfig {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 烫金纸类型（对应 product.hot_stamping_paper_type）
     */
    private String hotStampingPaperType;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}



