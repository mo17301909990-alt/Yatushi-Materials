package com.it.yts_project.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 注意事项字典表实体类
 * 用于存储所有规则匹配时需要显示的注意事项
 */
@Data
public class NoticeDictionary {
    
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * 注意事项编码，唯一标识，如 "PRINT_001"
     */
    private String noticeCode;
    
    /**
     * 标题（可选）
     */
    private String title;
    
    /**
     * 问题描述（可选）
     */
    private String problemDescription;
    
    /**
     * 解决方案（可选）
     */
    private String solution;
    
    /**
     * 分类：印刷、过胶、烫金、前工序等
     */
    private String category;
    
    /**
     * 优先级，数值越大越重要
     */
    private Integer priority;
    
    /**
     * 是否启用
     */
    private Boolean isActive;
    
    /**
     * 富文本内容（支持HTML表格、格式化文本等）
     */
    private String richContent;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
