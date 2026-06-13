/**
 * 匹配度计算工具函数
 */

import type { UserMatchPreferencesGrouped } from '@/api/types/matchPreference';

/**
 * 匹配字段列表
 */
export const MATCH_FIELDS = ['color', 'size', 'tightness', 'temperature', 'performance'] as const;

/**
 * 匹配字段类型
 */
export type MatchField = typeof MATCH_FIELDS[number];

/**
 * 产品数据接口（用于匹配计算）
 */
export interface ProductData {
  color?: string;
  size?: string;
  tightness?: string;
  temperature?: string;
  performance?: string;
  [key: string]: any; // 允许其他字段
}

/**
 * 匹配结果接口
 */
export interface MatchResult {
  matchScore: number; // 匹配度百分比
  matchedFields: MatchField[]; // 匹配的字段列表
  unmatchedFields: MatchField[]; // 未匹配的字段列表
  totalFields: number; // 总字段数
  matchedCount: number; // 匹配的字段数
}

/**
 * 计算产品与用户偏好的匹配度
 * @param userPreferences 用户匹配偏好
 * @param productData 产品数据
 * @returns 匹配结果
 */
export function calculateMatchScore(
  userPreferences: UserMatchPreferencesGrouped,
  productData: ProductData
): MatchResult {
  const matchedFields: MatchField[] = [];
  const unmatchedFields: MatchField[] = [];
  
  // 遍历所有匹配字段
  for (const field of MATCH_FIELDS) {
    const userValues = userPreferences[field] || [];
    const productValue = productData[field];
    
    // 如果用户没有设置该字段的偏好，跳过该字段
    if (userValues.length === 0) {
      continue;
    }
    
    // 检查产品值是否在用户偏好中
    if (productValue && userValues.includes(productValue)) {
      matchedFields.push(field);
    } else {
      unmatchedFields.push(field);
    }
  }
  
  // 计算有效字段数（用户设置了偏好的字段）
  const totalFields = matchedFields.length + unmatchedFields.length;
  const matchedCount = matchedFields.length;
  
  // 计算匹配度百分比
  const matchScore = totalFields > 0 ? Math.round((matchedCount / totalFields) * 100) : 0;
  
  return {
    matchScore,
    matchedFields,
    unmatchedFields,
    totalFields,
    matchedCount
  };
}

/**
 * 批量计算多个产品的匹配度
 * @param userPreferences 用户匹配偏好
 * @param products 产品列表
 * @returns 带有匹配度的产品列表
 */
export function calculateBatchMatchScores<T extends ProductData>(
  userPreferences: UserMatchPreferencesGrouped,
  products: T[]
): (T & { matchResult: MatchResult })[] {
  return products.map(product => ({
    ...product,
    matchResult: calculateMatchScore(userPreferences, product)
  }));
}

/**
 * 根据匹配度对产品进行排序
 * @param products 带有匹配结果的产品列表
 * @param order 排序方式，'desc' 为降序（默认），'asc' 为升序
 * @returns 排序后的产品列表
 */
export function sortByMatchScore<T extends { matchResult: MatchResult }>(
  products: T[],
  order: 'desc' | 'asc' = 'desc'
): T[] {
  return [...products].sort((a, b) => {
    if (order === 'desc') {
      return b.matchResult.matchScore - a.matchResult.matchScore;
    } else {
      return a.matchResult.matchScore - b.matchResult.matchScore;
    }
  });
}

/**
 * 过滤匹配度达到指定阈值的产品
 * @param products 带有匹配结果的产品列表
 * @param threshold 匹配度阈值（0-100）
 * @returns 过滤后的产品列表
 */
export function filterByMatchThreshold<T extends { matchResult: MatchResult }>(
  products: T[],
  threshold: number
): T[] {
  return products.filter(product => product.matchResult.matchScore >= threshold);
}

/**
 * 获取匹配度等级描述
 * @param matchScore 匹配度分数
 * @returns 匹配度等级描述
 */
export function getMatchScoreLevel(matchScore: number): string {
  if (matchScore >= 90) return '完美匹配';
  if (matchScore >= 80) return '高度匹配';
  if (matchScore >= 60) return '良好匹配';
  if (matchScore >= 40) return '一般匹配';
  if (matchScore >= 20) return '低度匹配';
  return '不匹配';
}

/**
 * 获取匹配度等级颜色类名
 * @param matchScore 匹配度分数
 * @returns CSS颜色类名
 */
export function getMatchScoreColorClass(matchScore: number): string {
  if (matchScore >= 90) return 'text-green-600';
  if (matchScore >= 80) return 'text-blue-600';
  if (matchScore >= 60) return 'text-yellow-600';
  if (matchScore >= 40) return 'text-orange-600';
  if (matchScore >= 20) return 'text-red-600';
  return 'text-gray-600';
}
