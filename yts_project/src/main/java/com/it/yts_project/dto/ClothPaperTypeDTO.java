package com.it.yts_project.dto;

import lombok.Data;
import java.util.List;

/**
 * 布料纸类型DTO
 */
@Data
public class ClothPaperTypeDTO {
    private Integer id;
    private String typeName;
    private String category;
    private Integer sortOrder;
    private Boolean isActive;
    /** 选用该界面时启用的系列优先级规则ID（方案A） */
    private Integer seriesPriorityRuleId;
    /** 设为 true 时，该类型为「特殊界面布面纸」，常用界面燙印性 組合應用表导出时不参与（不显示为特殊界面列） */
    private Boolean excludeFromCommonInterfaceMatrix;
    /** 设为 true 时，该布面纸类型禁止与「燙後加工.印刷UV」同时使用（匹配时若同选则结果为空） */
    private Boolean disallowUvPrintWithType;
    private String createdAt;
    private String updatedAt;
    private List<Integer> noticeIds;  // 关联的注意事项ID数组
}


