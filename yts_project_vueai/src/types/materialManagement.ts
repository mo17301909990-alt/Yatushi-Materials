// 物料管理相关类型定义

// 烫金物料信息
export interface HotStampingMaterial {
  id?: number
  name: string
  modelNumber: string
  materialNumber?: string
  color?: string
  size?: string
  tightness?: string
  temperatureRange?: string
  performance?: string
  flatHotStamping?: string
  embossedGoldStamping?: string
  refractiveHolographicPatternedTexturedHotStamping?: string
  postHotStampingEmbossingDebossing?: string
  price?: number
  companyNumber?: string
  gpNo?: string
  uvPrinting?: string
  ledUvGlitter?: string
  stampingPrinting?: string
  status?: '可用' | '废弃'  // 物料状态标记
  createdAt?: string
  updatedAt?: string
}

// 产品类型
export interface ProductType {
  id?: number
  name: string
  description?: string
  isActive: boolean
  sortOrder: number
  createdAt?: string
  updatedAt?: string
}

// 烫金类型选项
export interface HotStampingTypeOption {
  id?: number
  stampingType: string
  positionType?: string
  description?: string
  isActive: boolean
  sortOrder: number
  createdAt?: string
  updatedAt?: string
}

// 烫金图案基础信息
export interface HotStampingPattern {
  id?: number
  patternName: string
  patternType?: string
  description?: string
  isActive: boolean
  sortOrder: number
  createdAt?: string
  updatedAt?: string
}

// 烫金图案区域选项
export interface HotStampingPatternAreaOption {
  id?: number
  areaName: string
  areaType?: string
  description?: string
  isActive: boolean
  sortOrder: number
  createdAt?: string
  updatedAt?: string
}

// 适用界面(前工序)
export interface PreProcessingInterface {
  id?: number
  interfaceName: string
  interfaceType: string
  description?: string
  isActive: boolean
  sortOrder: number
  createdAt?: string
  updatedAt?: string
}

// 适用界面
export interface ApplicableInterface {
  id?: number
  interfaceName: string
  interfaceType: string
  description?: string
  isActive: boolean
  sortOrder: number
  createdAt?: string
  updatedAt?: string
}

// 烫后加工 - 印刷UV
export interface PostProcessingPrintUv {
  id?: number
  productName?: string
  productModelNumber?: string
  companyNumber?: string
  compatibilityStatus: 'V' | 'X' | 'N'
  paperType?: string
  createdAt?: string
  updatedAt?: string
}

// 烫后加工 - 印刷
export interface PostProcessingPrint {
  id?: number
  productName?: string
  productModelNumber?: string
  color?: string
  clothPaperTypeId?: number
  compatibilityStatus: 'V' | 'X' | 'NA'
  paperType?: string
  createdAt?: string
  updatedAt?: string
}

// 烫后加工 - 絲印LED UV灑閃粉
export interface PostProcessingLeduvglitter {
  id?: number
  productName?: string
  productModelNumber?: string
  clothPaperTypeId?: number
  compatibilityStatus: 'V' | 'X' | 'N'
  paperType?: string
  createdAt?: string
  updatedAt?: string
}

// 通用查询参数
export interface MaterialQueryParams {
  page?: number
  size?: number
  name?: string
  isActive?: boolean
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

// 烫金物料查询参数
export interface HotStampingMaterialQueryParams extends MaterialQueryParams {
  modelNumber?: string
  color?: string
  companyNumber?: string
}

// 产品类型查询参数
export interface ProductTypeQueryParams extends MaterialQueryParams {
  // 继承基础查询参数
}

// 烫金类型查询参数
export interface HotStampingTypeQueryParams extends MaterialQueryParams {
  stampingType?: string
  positionType?: string
}

// 烫金图案查询参数
export interface HotStampingPatternQueryParams extends MaterialQueryParams {
  patternType?: string
}

// 烫金图案区域查询参数
export interface HotStampingPatternAreaQueryParams extends MaterialQueryParams {
  areaType?: string
}

// 前工序界面查询参数
export interface PreProcessingInterfaceQueryParams extends MaterialQueryParams {
  interfaceType?: string
}

// 适用界面查询参数
export interface ApplicableInterfaceQueryParams extends MaterialQueryParams {
  interfaceType?: string
}

// 烫后加工查询参数
export interface PostProcessingQueryParams extends MaterialQueryParams {
  productName?: string
  productModelNumber?: string
  companyNumber?: string
  compatibilityStatus?: string
  paperType?: string
}

// API响应类型
export interface ApiResponse<T> {
  data: T
  message?: string
  success: boolean
}

// 分页响应类型
export interface PaginatedResponse<T> {
  items: T[]
  total: number
  page: number
  size: number
  totalPages: number
}

// 表单验证错误
export interface ValidationError {
  field: string
  message: string
}

// 操作结果
export interface OperationResult {
  success: boolean
  message: string
  data?: any
  errors?: ValidationError[]
}

// 注意事项字典
export interface NoticeDictionary {
  id: number
  noticeCode: string
  title?: string
  problemDescription?: string
  solution?: string
  category?: string
  priority: number
  isActive: boolean
  richContent?: string  // 富文本内容（支持HTML表格等）
  createdAt?: string
  updatedAt?: string
}

// 纸張面字典
export interface PaperSurface {
  id?: number
  category: string
  detailName: string
  displayOrder?: number
  createdAt?: string
  updatedAt?: string
}

// 印刷油墨面字典
export interface InkSurface {
  id?: number
  category: string
  detailName: string
  displayOrder?: number
  createdAt?: string
  updatedAt?: string
}

// 後加工塗層面字典
export interface CoatingSurface {
  id?: number
  category: string
  subCategory?: string
  detailName: string
  displayOrder?: number
  isActive?: boolean
  createdAt?: string
  updatedAt?: string
}

// 匹配结果（包含注意事项）
export interface MatchResultWithNotices<T> {
  products: PaginatedResponse<T>
  notices: NoticeDictionary[]
}

