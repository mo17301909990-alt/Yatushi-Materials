package com.it.yts_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

/**
 * 用户匹配偏好实体类
 * 用于存储用户的匹配度配置偏好设置
 */
@NonNull
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMatchPreference {
    
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * 用户ID，关联users表
     */
    private Integer userId;
    
    /**
     * 字段名称
     * 可选值：color(颜色)、size(尺寸)、tightness(松紧度)、temperature(温度)、performance(特性与场景)
     */
    private String fieldName;
    
    /**
     * 字段值，用户选择的偏好值
     */
    private String fieldValue;
    
    /**
     * 创建时间
     */
    private Date createdAt;
    
    /**
     * 更新时间
     */
    private Date updatedAt;
}
