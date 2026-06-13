package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

/**
 * 用户匹配偏好数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@NonNull
public class UserMatchPreferenceDTO {
    
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 字段名称
     */
    private String fieldName;
    
    /**
     * 字段值
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
