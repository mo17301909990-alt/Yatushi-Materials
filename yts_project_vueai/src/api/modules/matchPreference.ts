import request from '../request';
import type { 
  BatchSavePreferencesRequest, 
  UserMatchPreferencesGrouped,
  ApiResponse 
} from '../types/matchPreference';

/**
 * 匹配偏好相关API
 */
export const matchPreferenceApi = {
  
  /**
   * 获取用户的匹配偏好（按字段分组）
   * @param userId 用户ID
   * @returns 按字段分组的匹配偏好
   */
  getUserPreferences(userId: number): Promise<UserMatchPreferencesGrouped> {
    console.log('Calling getUserPreferences API with userId:', userId);
    return request.get(`/users/${userId}/match-preferences`);
  },

  /**
   * 批量保存用户匹配偏好
   * @param userId 用户ID
   * @param preferences 匹配偏好数据
   * @returns 保存结果
   */
  batchSavePreferences(userId: number, preferences: Record<string, string[]>): Promise<ApiResponse> {
    console.log('Calling batchSavePreferences API with:', { userId, preferences });
    
    const request_data: BatchSavePreferencesRequest = {
      userId,
      preferences
    };
    
    return request.post(`/users/${userId}/match-preferences`, request_data);
  },

  /**
   * 删除单个匹配偏好
   * @param id 匹配偏好ID
   * @returns 删除结果
   */
  deletePreference(id: number): Promise<ApiResponse> {
    console.log('Calling deletePreference API with id:', id);
    return request.delete(`/users/match-preferences/${id}`);
  }
};
