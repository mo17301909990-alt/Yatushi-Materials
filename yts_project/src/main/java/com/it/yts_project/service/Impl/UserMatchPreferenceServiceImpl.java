package com.it.yts_project.service.Impl;

import com.it.yts_project.dto.BatchSavePreferencesRequest;
import com.it.yts_project.dto.UserMatchPreferenceDTO;
import com.it.yts_project.mapper.UserMatchPreferenceMapper;
import com.it.yts_project.model.UserMatchPreference;
import com.it.yts_project.service.UserMatchPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户匹配偏好服务实现类
 */
@Service
public class UserMatchPreferenceServiceImpl implements UserMatchPreferenceService {
    
    @Autowired
    private UserMatchPreferenceMapper userMatchPreferenceMapper;
    
    // 有效的字段名列表
    private static final Set<String> VALID_FIELD_NAMES = Set.of(
        "color", "size", "tightness", "temperature", "performance"
    );
    
    @Override
    public List<UserMatchPreferenceDTO> getUserPreferences(Integer userId) {
        List<UserMatchPreference> preferences = userMatchPreferenceMapper.findByUserId(userId);
        return preferences.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, List<String>> getUserPreferencesGrouped(Integer userId) {
        List<UserMatchPreference> preferences = userMatchPreferenceMapper.findByUserId(userId);
        
        Map<String, List<String>> groupedPreferences = new HashMap<>();
        
        // 初始化所有字段为空列表
        for (String fieldName : VALID_FIELD_NAMES) {
            groupedPreferences.put(fieldName, new ArrayList<>());
        }
        
        // 按字段分组
        for (UserMatchPreference preference : preferences) {
            String fieldName = preference.getFieldName();
            if (groupedPreferences.containsKey(fieldName)) {
                groupedPreferences.get(fieldName).add(preference.getFieldValue());
            }
        }
        
        return groupedPreferences;
    }
    
    @Override
    @Transactional
    public int batchSavePreferences(BatchSavePreferencesRequest request) {
        Integer userId = request.getUserId();
        Map<String, List<String>> preferences = request.getPreferences();
        
        // 验证用户ID
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        
        // 先删除用户的所有现有偏好
        userMatchPreferenceMapper.deleteByUserId(userId);
        
        // 准备批量插入的数据
        List<UserMatchPreference> preferencesToInsert = new ArrayList<>();
        
        for (Map.Entry<String, List<String>> entry : preferences.entrySet()) {
            String fieldName = entry.getKey();
            List<String> fieldValues = entry.getValue();
            
            // 验证字段名
            if (!isValidFieldName(fieldName)) {
                throw new IllegalArgumentException("无效的字段名: " + fieldName);
            }
            
            // 为每个字段值创建记录
            for (String fieldValue : fieldValues) {
                if (fieldValue != null && !fieldValue.trim().isEmpty()) {
                    UserMatchPreference preference = new UserMatchPreference();
                    preference.setUserId(userId);
                    preference.setFieldName(fieldName);
                    preference.setFieldValue(fieldValue.trim());
                    preferencesToInsert.add(preference);
                }
            }
        }
        
        // 批量插入新的偏好
        if (!preferencesToInsert.isEmpty()) {
            return userMatchPreferenceMapper.batchInsert(preferencesToInsert);
        }
        
        return 0;
    }
    
    @Override
    public int deletePreference(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("偏好ID不能为空或无效");
        }
        return userMatchPreferenceMapper.deleteById(id);
    }
    
    @Override
    public int deleteAllUserPreferences(Integer userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("用户ID不能为空或无效");
        }
        return userMatchPreferenceMapper.deleteByUserId(userId);
    }
    
    @Override
    public boolean isValidFieldName(String fieldName) {
        return fieldName != null && VALID_FIELD_NAMES.contains(fieldName);
    }
    
    /**
     * 将实体对象转换为DTO对象
     */
    private UserMatchPreferenceDTO convertToDTO(UserMatchPreference preference) {
        UserMatchPreferenceDTO dto = new UserMatchPreferenceDTO();
        dto.setId(preference.getId().intValue());
        dto.setUserId(preference.getUserId());
        dto.setFieldName(preference.getFieldName());
        dto.setFieldValue(preference.getFieldValue());
        dto.setCreatedAt(preference.getCreatedAt());
        dto.setUpdatedAt(preference.getUpdatedAt());
        return dto;
    }
}
