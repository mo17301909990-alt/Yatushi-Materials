import axios from 'axios'

const API_BASE_URL = '/api'

// 定义接口类型
export interface ApiResponse<T> {
  success: boolean
  data: T
  message?: string
}

export interface OperationResult {
  success: boolean
  message?: string
}

export interface SiliconeGlowInkProduct {
  id?: number
  materialCode?: string
  supplierCode?: string
  stockCode?: string
  materialName?: string
  usage?: string
  category?: string
  color?: string
  responsiblePerson?: string
  minSheetSize?: string
  maxSheetSize?: string
  minSpacing?: string
  maxSpacing?: string
  designInfo?: string
  applicableInterface?: string
  notes?: string
  isActive?: boolean
  createdAt?: string
  updatedAt?: string
}

export interface SiliconeGlowInkCompatibility {
  id?: number
  productId: number
  postProcessingStep: string
  compatibilityStatus: string
  displayOrder?: number
  createdAt?: string
}

export const siliconeGlowInkApi = {
  // ========== 产品管理 ==========

  /** 获取所有产品 */
  getAllProducts: async (): Promise<SiliconeGlowInkProduct[]> => {
    const response = await axios.get<SiliconeGlowInkProduct[]>(`${API_BASE_URL}/silicone_glow_ink/products`)
    return response.data || []
  },

  /** 根据ID获取产品 */
  getProductById: async (id: number): Promise<SiliconeGlowInkProduct> => {
    const response = await axios.get<SiliconeGlowInkProduct>(`${API_BASE_URL}/silicone_glow_ink/products/${id}`)
    return response.data
  },

  /** 搜索产品 */
  searchProducts: async (keyword: string): Promise<SiliconeGlowInkProduct[]> => {
    const response = await axios.get<SiliconeGlowInkProduct[]>(`${API_BASE_URL}/silicone_glow_ink/products/search`, {
      params: { keyword }
    })
    return response.data || []
  },

  /** 新增产品 */
  createProduct: async (data: SiliconeGlowInkProduct): Promise<SiliconeGlowInkProduct> => {
    const response = await axios.post<SiliconeGlowInkProduct>(`${API_BASE_URL}/silicone_glow_ink/products`, data)
    return response.data
  },

  /** 更新产品 */
  updateProduct: async (id: number, data: SiliconeGlowInkProduct): Promise<SiliconeGlowInkProduct> => {
    const response = await axios.put<SiliconeGlowInkProduct>(`${API_BASE_URL}/silicone_glow_ink/products/${id}`, data)
    return response.data
  },

  /** 删除产品 */
  deleteProduct: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/silicone_glow_ink/products/${id}`)
  },

  // ========== 兼容性管理 ==========

  /** 获取所有兼容性 */
  getAllCompatibilities: async (): Promise<SiliconeGlowInkCompatibility[]> => {
    const response = await axios.get<SiliconeGlowInkCompatibility[]>(`${API_BASE_URL}/silicone_glow_ink/compatibilities`)
    return response.data || []
  },

  /** 根据产品ID获取兼容性 */
  getCompatibilitiesByProductId: async (productId: number): Promise<SiliconeGlowInkCompatibility[]> => {
    const response = await axios.get<SiliconeGlowInkCompatibility[]>(`${API_BASE_URL}/silicone_glow_ink/compatibilities/by-product/${productId}`)
    return response.data || []
  },

  /** 根据ID获取兼容性 */
  getCompatibilityById: async (id: number): Promise<SiliconeGlowInkCompatibility> => {
    const response = await axios.get<SiliconeGlowInkCompatibility>(`${API_BASE_URL}/silicone_glow_ink/compatibilities/${id}`)
    return response.data
  },

  /** 新增兼容性 */
  createCompatibility: async (data: SiliconeGlowInkCompatibility): Promise<SiliconeGlowInkCompatibility> => {
    const response = await axios.post<SiliconeGlowInkCompatibility>(`${API_BASE_URL}/silicone_glow_ink/compatibilities`, data)
    return response.data
  },

  /** 更新兼容性 */
  updateCompatibility: async (id: number, data: SiliconeGlowInkCompatibility): Promise<SiliconeGlowInkCompatibility> => {
    const response = await axios.put<SiliconeGlowInkCompatibility>(`${API_BASE_URL}/silicone_glow_ink/compatibilities/${id}`, data)
    return response.data
  },

  /** 批量保存兼容性 */
  batchSaveCompatibilities: async (data: SiliconeGlowInkCompatibility[]): Promise<void> => {
    await axios.post(`${API_BASE_URL}/silicone_glow_ink/compatibilities/batch`, data)
  },

  /** 删除兼容性 */
  deleteCompatibility: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/silicone_glow_ink/compatibilities/${id}`)
  },

  /** 获取所有后加工工序名称 */
  getAllPostProcessingSteps: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/silicone_glow_ink/options/post-processing-steps`)
    return response.data || []
  }
}
