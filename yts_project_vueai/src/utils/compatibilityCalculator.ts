/**
 * 烫金工艺兼容性计算工具
 */

import type { 
  CompatibilityQueryParams, 
  HotStampingCompatibility, 
  CompatibilityMatchResult,
  PatternType,
  HotStampingType
} from '@/types/compatibility';

/**
 * 图案特征解析器
 */
export class PatternCharacteristicParser {
  
  /**
   * 解析图案描述，提取图案类型和尺寸信息
   */
  static parsePatternDescription(description: string): {
    patternType: PatternType;
    lineThickness?: number;
    solidAreaSize?: number;
    isMixedPattern: boolean;
  } {
    const isMixedPattern = description.includes('混合圖案');
    
    if (isMixedPattern) {
      return {
        patternType: 'MIXED',
        isMixedPattern: true,
        lineThickness: this.extractLineThickness(description),
        solidAreaSize: this.extractSolidAreaSize(description)
      };
    }
    
    if (description.includes('線條') || description.includes('字母')) {
      return {
        patternType: 'LINE',
        isMixedPattern: false,
        lineThickness: this.extractLineThickness(description)
      };
    }
    
    if (description.includes('實地')) {
      return {
        patternType: 'SOLID',
        isMixedPattern: false,
        solidAreaSize: this.extractSolidAreaSize(description)
      };
    }
    
    return {
      patternType: 'ALL',
      isMixedPattern: false
    };
  }
  
  /**
   * 提取线条粗细信息
   */
  private static extractLineThickness(description: string): number | undefined {
    const patterns = [
      { regex: /≤0\.5mm/, value: 0.5 },
      { regex: /0\.5mm<X≤5mm/, value: 3 },
      { regex: /5mm<X≤10mm/, value: 7.5 },
      { regex: />10mm/, value: 15 }
    ];
    
    for (const pattern of patterns) {
      if (pattern.regex.test(description)) {
        return pattern.value;
      }
    }
    
    return undefined;
  }
  
  /**
   * 提取实地尺寸信息
   */
  private static extractSolidAreaSize(description: string): number | undefined {
    const patterns = [
      { regex: /≤10mm/, value: 5 },
      { regex: /10mm<X≤20mm/, value: 15 },
      { regex: />20mm/, value: 25 }
    ];
    
    for (const pattern of patterns) {
      if (pattern.regex.test(description)) {
        return pattern.value;
      }
    }
    
    return undefined;
  }
}

/**
 * 兼容性匹配计算器
 */
export class CompatibilityCalculator {
  
  /**
   * 计算兼容性匹配结果
   */
  static calculateMatchResult(
    compatibilities: HotStampingCompatibility[],
    targetTypes?: HotStampingType[]
  ): CompatibilityMatchResult {
    const compatibleTypes = compatibilities
      .filter(c => c.compatibility === 'V')
      .map(c => c.hotStampingType)
      .filter((type, index, array) => array.indexOf(type) === index);
    
    const incompatibleTypes = compatibilities
      .filter(c => c.compatibility === 'X')
      .map(c => c.hotStampingType)
      .filter((type, index, array) => array.indexOf(type) === index);
    
    // 如果指定了目标类型，只返回目标类型的兼容性
    if (targetTypes && targetTypes.length > 0) {
      const filteredCompatible = compatibleTypes.filter(type => targetTypes.includes(type));
      const filteredIncompatible = incompatibleTypes.filter(type => targetTypes.includes(type));
      
      return {
        compatibleTypes: filteredCompatible,
        incompatibleTypes: filteredIncompatible,
        matchScore: this.calculateMatchScore(filteredCompatible, targetTypes),
        recommendations: this.generateRecommendations(filteredCompatible, filteredIncompatible)
      };
    }
    
    return {
      compatibleTypes,
      incompatibleTypes,
      matchScore: this.calculateMatchScore(compatibleTypes, []),
      recommendations: this.generateRecommendations(compatibleTypes, incompatibleTypes)
    };
  }
  
  /**
   * 计算匹配分数
   */
  private static calculateMatchScore(compatibleTypes: HotStampingType[], targetTypes: HotStampingType[]): number {
    if (targetTypes.length === 0) {
      return compatibleTypes.length > 0 ? 100 : 0;
    }
    
    const matchCount = compatibleTypes.filter(type => targetTypes.includes(type)).length;
    return Math.round((matchCount / targetTypes.length) * 100);
  }
  
  /**
   * 生成推荐建议
   */
  private static generateRecommendations(
    compatibleTypes: HotStampingType[], 
    incompatibleTypes: HotStampingType[]
  ): string[] {
    const recommendations: string[] = [];
    
    if (compatibleTypes.length === 0) {
      recommendations.push('当前参数组合没有兼容的烫金类型，建议调整参数');
      return recommendations;
    }
    
    if (compatibleTypes.includes('平面烫金-於中間位')) {
      recommendations.push('推荐使用平面烫金-於中間位，兼容性最佳');
    }
    
    if (compatibleTypes.includes('立體烫金')) {
      recommendations.push('支持立體烫金，可提供更丰富的视觉效果');
    }
    
    if (compatibleTypes.includes('磨砂烫金') || compatibleTypes.includes('折光烫金')) {
      recommendations.push('支持特殊效果烫金，可提供独特的视觉质感');
    }
    
    if (incompatibleTypes.length > 0) {
      recommendations.push(`以下烫金类型不兼容：${incompatibleTypes.join('、')}`);
    }
    
    return recommendations;
  }
}

/**
 * 兼容性验证器
 */
export class CompatibilityValidator {
  
  /**
   * 验证参数完整性
   */
  static validateParams(params: CompatibilityQueryParams): {
    isValid: boolean;
    errors: string[];
  } {
    const errors: string[] = [];
    
    if (!params.paperPerformance) {
      errors.push('请选择烫金纸性能类型');
    }
    
    if (!params.productType) {
      errors.push('请选择产品类型');
    }
    
    if (!params.patternDescription && !params.lineThickness && !params.solidAreaSize) {
      errors.push('请提供图案特征信息');
    }
    
    return {
      isValid: errors.length === 0,
      errors
    };
  }
  
  /**
   * 验证图案参数合理性
   */
  static validatePatternParams(params: CompatibilityQueryParams): {
    isValid: boolean;
    warnings: string[];
  } {
    const warnings: string[] = [];
    
    if (params.lineThickness && params.lineThickness < 0) {
      warnings.push('线条粗细不能为负数');
    }
    
    if (params.solidAreaSize && params.solidAreaSize < 0) {
      warnings.push('实地尺寸不能为负数');
    }
    
    if (params.isMixedPattern && !params.lineThickness && !params.solidAreaSize) {
      warnings.push('混合图案需要提供线条粗细或实地尺寸信息');
    }
    
    return {
      isValid: warnings.length === 0,
      warnings
    };
  }
}
