/**
 * 匹配偏好相关的类型定义
 */

/**
 * 用户匹配偏好DTO
 */
export interface UserMatchPreferenceDTO {
  id: number;
  userId: number;
  fieldName: string;
  fieldValue: string;
  createdAt: string;
  updatedAt: string;
}

/**
 * 批量保存匹配偏好请求
 */
export interface BatchSavePreferencesRequest {
  userId: number;
  preferences: Record<string, string[]>;
}

/**
 * 匹配偏好响应（按字段分组）
 */
export interface UserMatchPreferencesGrouped {
  color: string[];
  size: string[];
  tightness: string[];
  temperature: string[];
  performance: string[];
}

/**
 * 匹配字段配置
 */
export interface MatchFieldConfig {
  label: string;
  options: string[];
}

/**
 * 所有匹配字段的配置
 */
export interface MatchFieldsConfig {
  color: MatchFieldConfig;
  size: MatchFieldConfig;
  tightness: MatchFieldConfig;
  temperature: MatchFieldConfig;
  performance: MatchFieldConfig;
}

/**
 * API响应基础类型
 */
export interface ApiResponse<T = any> {
  data?: T;
  message?: string;
  savedCount?: number;
  deletedCount?: number;
}
