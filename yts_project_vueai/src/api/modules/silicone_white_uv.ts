import axios from 'axios'

// 产品接口
export interface SiliconeWhiteUvProduct {
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
  designInfo?: string
  applicableInterface?: string
  notes?: string
  isActive?: boolean
  createdAt?: string
  updatedAt?: string
}

// 兼容性接口
export interface SiliconeWhiteUvCompatibility {
  id?: number
  productId?: number
  postProcessingStep?: string
  compatibilityStatus?: string
  displayOrder?: number
  createdAt?: string
  productName?: string
}

const API_BASE_URL = '/api'

// 硅胶_白UV API
export const siliconeWhiteUvApi = {
  // ========== 产品管理 ==========

  // 获取所有产品
  getAllProducts: async (): Promise<SiliconeWhiteUvProduct[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_white_uv/products`)
    return response.data
  },

  // 获取激活的产品
  getActiveProducts: async (): Promise<SiliconeWhiteUvProduct[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_white_uv/products/active`)
    return response.data
  },

  // 根据ID获取产品
  getProductById: async (id: number): Promise<SiliconeWhiteUvProduct> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_white_uv/products/${id}`)
    return response.data
  },

  // 搜索产品
  searchProducts: async (keyword: string): Promise<SiliconeWhiteUvProduct[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_white_uv/products/search`, { params: { keyword } })
    return response.data
  },

  // 新增产品
  createProduct: async (product: SiliconeWhiteUvProduct): Promise<SiliconeWhiteUvProduct> => {
    const response = await axios.post(`${API_BASE_URL}/silicone_white_uv/products`, product)
    return response.data
  },

  // 更新产品
  updateProduct: async (id: number, product: SiliconeWhiteUvProduct): Promise<SiliconeWhiteUvProduct> => {
    const response = await axios.put(`${API_BASE_URL}/silicone_white_uv/products/${id}`, product)
    return response.data
  },

  // 删除产品
  deleteProduct: async (id: number): Promise<{ success: boolean; message: string }> => {
    const response = await axios.delete(`${API_BASE_URL}/silicone_white_uv/products/${id}`)
    return response.data
  },

  // ========== 兼容性管理 ==========

  // 根据产品ID获取兼容性列表
  getCompatibilitiesByProductId: async (productId?: number): Promise<SiliconeWhiteUvCompatibility[]> => {
    const params: any = {}
    if (productId !== undefined) params.productId = productId
    const response = await axios.get(`${API_BASE_URL}/silicone_white_uv/compatibilities`, { params })
    return response.data
  },

  // 根据ID获取兼容性
  getCompatibilityById: async (id: number): Promise<SiliconeWhiteUvCompatibility> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_white_uv/compatibilities/${id}`)
    return response.data
  },

  // 新增兼容性
  createCompatibility: async (compatibility: SiliconeWhiteUvCompatibility): Promise<SiliconeWhiteUvCompatibility> => {
    const response = await axios.post(`${API_BASE_URL}/silicone_white_uv/compatibilities`, compatibility)
    return response.data
  },

  // 更新兼容性
  updateCompatibility: async (id: number, compatibility: SiliconeWhiteUvCompatibility): Promise<SiliconeWhiteUvCompatibility> => {
    const response = await axios.put(`${API_BASE_URL}/silicone_white_uv/compatibilities/${id}`, compatibility)
    return response.data
  },

  // 批量保存兼容性
  batchSaveCompatibilities: async (compatibilities: SiliconeWhiteUvCompatibility[]): Promise<{ success: boolean; message: string }> => {
    const response = await axios.post(`${API_BASE_URL}/silicone_white_uv/compatibilities/batch`, compatibilities)
    return response.data
  },

  // 删除兼容性
  deleteCompatibility: async (id: number): Promise<{ success: boolean; message: string }> => {
    const response = await axios.delete(`${API_BASE_URL}/silicone_white_uv/compatibilities/${id}`)
    return response.data
  },

  // 根据产品ID删除兼容性
  deleteCompatibilitiesByProductId: async (productId: number): Promise<{ success: boolean; message: string }> => {
    const response = await axios.delete(`${API_BASE_URL}/silicone_white_uv/compatibilities/by-product/${productId}`)
    return response.data
  }
}
