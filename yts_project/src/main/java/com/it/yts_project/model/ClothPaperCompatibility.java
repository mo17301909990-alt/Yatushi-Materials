package com.it.yts_project.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 布面纸兼容性实体类
 * 对应数据库表：cloth_paper_compatibility
 */
@Data
public class ClothPaperCompatibility {
    private Integer id;
    private String productName;  // 燙金紙系列
    private Integer clothPaperTypeId;
    private String compatibilityStatus;
    private String paperType;  // 纸张类型
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Integer> noticeIds;  // 关联的注意事项ID数组

    // 关联查询字段
    private String clothPaperTypeName;  // 布面纸类型名称
    private String clothPaperCategory;  // 布面纸分类
}
