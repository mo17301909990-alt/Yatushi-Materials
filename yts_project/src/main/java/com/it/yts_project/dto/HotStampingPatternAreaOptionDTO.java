package com.it.yts_project.dto;

import lombok.Data;
import java.util.List;

/**
 * 烫金图案区域选项DTO
 */
@Data
public class HotStampingPatternAreaOptionDTO {
    private Integer id;
    private String optionName;
    private String areaCategory;
    private String areaRange;
    private String patternType;
    private Integer minSizeMm;
    private Integer maxSizeMm;
    private String description;
    private Boolean isActive;
    private Integer sortOrder;
    private String createdAt;
    private String updatedAt;
    private List<Integer> noticeIds;  // 关联的注意事项ID数组
}
