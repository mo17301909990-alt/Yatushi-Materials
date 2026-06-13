package com.it.yts_project.service;

import com.it.yts_project.dto.BatchSavePreferencesRequest;
import com.it.yts_project.dto.UserMatchPreferenceDTO;

import java.util.List;
import java.util.Map;

/**
 * 用户匹配偏好服务接口
 */
public interface UserMatchPreferenceService {
    
    /**
     * 获取用户的所有匹配偏好
     * @param userId 用户ID
     * @return 用户匹配偏好列表
     */
    List<UserMatchPreferenceDTO> getUserPreferences(Integer userId);
    
    /**
     * 获取用户的匹配偏好（按字段分组）
     * @param userId 用户ID
     * @return 按字段分组的匹配偏好Map，Key为字段名，Value为该字段的偏好值列表
     */
    Map<String, List<String>> getUserPreferencesGrouped(Integer userId);
    
    /**
     * 批量保存用户匹配偏好
     * 此操作会先删除用户的所有现有偏好，然后插入新的偏好
     * @param request 批量保存请求对象
     * @return 保存成功的记录数
     */
    int batchSavePreferences(BatchSavePreferencesRequest request);
    
    /**
     * 删除单个匹配偏好
     * @param id 匹配偏好ID
     * @return 删除成功的记录数
     */
    int deletePreference(Integer id);
    
    /**
     * 删除用户的所有匹配偏好
     * @param userId 用户ID
     * @return 删除成功的记录数
     */
    int deleteAllUserPreferences(Integer userId);
    
    /**
     * 验证字段名是否有效
     * @param fieldName 字段名
     * @return 是否有效
     */
    boolean isValidFieldName(String fieldName);
}
