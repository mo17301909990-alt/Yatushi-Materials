import request from '../request'

export interface ProductTypeOption {
  id: number
  productName: string
  displayOrder: number
}

export interface HotStampingPatternBaseOption {
  id: number
  optionName: string
  patternType: string
  sortOrder: number
}

export interface HotStampingTypeOption {
  id: number
  stampingType: string
  positionType: string | null
  sortOrder: number
}

export interface ClothPaperTypeOption {
  id: number
  typeName: string
  category: string
  sortOrder: number
}

export interface PreProcessingStepOption {
  id: number
  preProcessingStepsName: string
  displayOrder: number
}

export interface PatternAreaOption {
  id: number
  optionName: string
  areaCategory: string
  description?: string
}

export interface LaminationMaterialOption {
  id: number
  materialName: string
  displayOrder: number
}

export interface PostProcessingStepOption {
  id: number
  stepName: string
  displayOrder: number
}

/** 获取所有启用的产品类型选项 */
export function getProductTypeOptions(): Promise<ProductTypeOption[]> {
  return request.get('/product-type-options/active')
}

/** 获取所有启用的烫金图案基础 */
export function getHotStampingPatternBase(): Promise<HotStampingPatternBaseOption[]> {
  return request.get('/hot-stamping-pattern-base/active')
}

/** 获取所有启用的烫金类型选项 */
export function getHotStampingTypeOptions(): Promise<HotStampingTypeOption[]> {
  return request.get('/hot-stamping-type-options/active')
}

/** 获取所有启用的布料纸类型 */
export function getClothPaperTypes(): Promise<ClothPaperTypeOption[]> {
  return request.get('/cloth-paper-types/active')
}

/** 获取所有启用的前工序选项 */
export function getPreProcessingStepsOptions(): Promise<PreProcessingStepOption[]> {
  return request.get('/pre-processing-steps/options')
}

/** 获取所有启用的烫金图案区域选项 */
export function getPatternAreaOptions(): Promise<PatternAreaOption[]> {
  return request.get('/hot-stamping-pattern-area-options/active')
}

/** 获取所有启用的过胶材料选项 */
export function getLaminationMaterialOptions(): Promise<LaminationMaterialOption[]> {
  return request.get('/laminating/options/materials')
}

/** 获取所有启用的后加工工序选项 */
export function getPostProcessingStepOptions(): Promise<PostProcessingStepOption[]> {
  return request.get('/laminating/options/steps')
}
