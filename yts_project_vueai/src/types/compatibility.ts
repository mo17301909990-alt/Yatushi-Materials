/**
 * 烫金工艺兼容性相关类型定义
 */

/**
 * 烫金纸性能类型
 */
export type PaperPerformanceType = '普通金紙' | '普通耐磨' | '高耐磨';

/**
 * 产品类型
 */
export type ProductType = '精平裝/咭書' | '賀咭/紙袋' | '包裝';

/**
 * 图案类型
 */
export type PatternType = 'LINE' | 'SOLID' | 'MIXED' | 'ALL';

/**
 * 烫金类型
 */
export type HotStampingType = 
  | '平面烫金-於中間位' 
  | '平面烫金-到邊位' 
  | '立體烫金(含烫金+击凸，且重叠)' 
  | '磨砂烫金' 
  | '折光烫金';

/**
 * 兼容性查询参数
 */
export interface CompatibilityQueryParams {
  paperPerformance?: PaperPerformanceType;
  productType?: ProductType;
  patternDescription?: string;
  lineThickness?: number;
  solidAreaSize?: number;
  isMixedPattern?: boolean;
  patternType?: PatternType;
  targetHotStampingTypes?: HotStampingType[];
  
  // 过胶兼容性相关参数
  foilSeries?: string;
  postProcessingStepId?: number;
  laminationMaterialId?: number;
  interfaceTypeId?: number;
  compatibility?: 'V' | 'X';
}

/**
 * 兼容性规则
 */
export interface HotStampingCompatibility {
  id: number;
  paperPerformance: PaperPerformanceType;
  productType: ProductType;
  patternCharacteristic: string;
  hotStampingType: HotStampingType;
  compatibility: 'V' | 'X';
  patternType: PatternType;
  lineThickness?: string;
  solidAreaSize?: string;
  referenceCode?: string;
}

/**
 * 兼容性匹配结果
 */
export interface CompatibilityMatchResult {
  compatibleTypes: HotStampingType[];
  incompatibleTypes: HotStampingType[];
  matchScore: number;
  recommendations: string[];
}

/**
 * 图案特征选项
 */
export interface PatternOption {
  label: string;
  value: string;
  patternType: PatternType;
  lineThickness?: number;
  solidAreaSize?: number;
  isMixedPattern?: boolean;
}

/**
 * 兼容性筛选选项
 */
export interface CompatibilityFilterOptions {
  paperPerformanceTypes: PaperPerformanceType[];
  productTypes: ProductType[];
  hotStampingTypes: HotStampingType[];
  patternOptions: PatternOption[];
}

// ========== 过胶兼容性相关类型定义 ==========

/**
 * 过胶材质选项
 */
export interface LaminationMaterialOptions {
  id: number;
  materialName: string;
  materialCode: string;
  displayOrder: number;
  isActive: boolean;
  description?: string;
  pid?: number; // 父级ID，用于层级结构
  createdAt: string;
  updatedAt: string;
}

/**
 * 后加工步骤选项
 */
export interface PostProcessingOptions {
  id: number;
  stepName: string;
  stepCode?: string;
  displayOrder: number;
  isActive: boolean;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

/**
 * 过胶兼容性
 */
export interface LaminationCompatibility {
  id: number;
  foilSeries: string; // 烫金纸系列
  interfaceTypeId: number; // 接口类型ID
  postProcessingStepId: number; // 后加工步骤ID
  laminationMaterialId: number; // 过胶材质ID
  compatibility: 'V' | 'X'; // 兼容性状态
  createdAt: string;
  updatedAt: string;
  
  // 关联对象
  postProcessingStep?: PostProcessingOptions;
  laminationMaterial?: LaminationMaterialOptions;
}

/**
 * 过胶兼容性查询参数
 */
export interface LaminationCompatibilityQueryParams {
  foilSeries?: string;
  postProcessingStepId?: number;
  laminationMaterialId?: number;
  interfaceTypeId?: number;
  compatibility?: 'V' | 'X';
}

/**
 * 过胶兼容性筛选选项
 */
export interface LaminationFilterOptions {
  laminationMaterials: LaminationMaterialOptions[];
  postProcessingSteps: PostProcessingOptions[];
  foilSeries: string[];
}
