import axios from 'axios'

// 产品接口
export interface SiliconeWrinkleUvProduct {
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
export interface SiliconeWrinkleUvCompatibility {
  id?: number
  productId: number
  postProcessingStep?: string
  compatibilityStatus?: string
  displayOrder?: number
  createdAt?: string
}

const API_BASE_URL = '/api/silicone_wrinkle_uv'

export const siliconeWrinkleUvApi = {
  // ========== 产品管理 ==========

  // 获取所有产品
  getAllProducts: async (): Promise<SiliconeWrinkleUvProduct[]> => {
    const response = await axios.get(`${API_BASE_URL}/products`)
    return response.data
  },

  // 获取启用的产品
  getActiveProducts: async (): Promise<SiliconeWrinkleUvProduct[]> => {
    const response = await axios.get(`${API_BASE_URL}/products/active`)
    return response.data
  },

  // 根据ID获取产品
  getProductById: async (id: number): Promise<SiliconeWrinkleUvProduct> => {
    const response = await axios.get(`${API_BASE_URL}/products/${id}`)
    return response.data
  },

  // 搜索产品
  searchProducts: async (keyword: string): Promise<SiliconeWrinkleUvProduct[]> => {
    const response = await axios.get(`${API_BASE_URL}/products/search`, { params: { keyword } })
    return response.data
  },

  // 保存产品
  saveProduct: async (product: SiliconeWrinkleUvProduct): Promise<SiliconeWrinkleUvProduct> => {
    const response = await axios.post(`${API_BASE_URL}/products`, product)
    return response.data
  },

  // 更新产品
  updateProduct: async (id: number, product: SiliconeWrinkleUvProduct): Promise<SiliconeWrinkleUvProduct> => {
    const response = await axios.put(`${API_BASE_URL}/products/${id}`, product)
    return response.data
  },

  // 删除产品
  deleteProduct: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/products/${id}`)
  },

  // ========== 兼容性管理 ==========

  // 获取所有兼容性
  getAllCompatibilities: async (): Promise<SiliconeWrinkleUvCompatibility[]> => {
    const response = await axios.get(`${API_BASE_URL}/compatibilities`)
    return response.data
  },

  // 根据产品ID获取兼容性列表
  getCompatibilitiesByProductId: async (productId: number): Promise<SiliconeWrinkleUvCompatibility[]> => {
    const response = await axios.get(`${API_BASE_URL}/compatibilities/product/${productId}`)
    return response.data
  },

  // 根据ID获取兼容性
  getCompatibilityById: async (id: number): Promise<SiliconeWrinkleUvCompatibility> => {
    const response = await axios.get(`${API_BASE_URL}/compatibilities/${id}`)
    return response.data
  },

  // 保存兼容性
  saveCompatibility: async (compatibility: SiliconeWrinkleUvCompatibility): Promise<SiliconeWrinkleUvCompatibility> => {
    const response = await axios.post(`${API_BASE_URL}/compatibilities`, compatibility)
    return response.data
  },

  // 更新兼容性
  updateCompatibility: async (id: number, compatibility: SiliconeWrinkleUvCompatibility): Promise<SiliconeWrinkleUvCompatibility> => {
    const response = await axios.put(`${API_BASE_URL}/compatibilities/${id}`, compatibility)
    return response.data
  },

  // 批量保存兼容性
  batchSaveCompatibilities: async (compatibilities: SiliconeWrinkleUvCompatibility[]): Promise<void> => {
    await axios.post(`${API_BASE_URL}/compatibilities/batch`, compatibilities)
  },

  // 删除兼容性
  deleteCompatibility: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/compatibilities/${id}`)
  },

  // 批量删除兼容性
  batchDeleteCompatibilities: async (ids: number[]): Promise<{ success: boolean; message: string }> => {
    const response = await axios.delete(`${API_BASE_URL}/compatibilities/batch`, { data: ids })
    return response.data
  }
}
