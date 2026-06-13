package com.it.yts_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

/**
 * 批量保存用户匹配偏好请求对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@NonNull
public class BatchSavePreferencesRequest {
    
    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 匹配偏好设置
     * Key: 字段名称 (color, size, tightness, temperature, performance)
     * Value: 该字段的偏好值列表
     */
    private Map<String, List<String>> preferences;
}
