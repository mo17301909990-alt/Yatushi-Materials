import axios from 'axios'

// 硅胶磨砂UV产品接口
export interface SiliconeSandblastUvProduct {
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

// 硅胶磨砂UV兼容性接口
export interface SiliconeSandblastUvCompatibility {
  id?: number
  productId?: number
  postProcessingStep?: string
  compatibilityStatus?: string
  displayOrder?: number
  createdAt?: string
}

const API_BASE_URL = '/api'

// 硅胶磨砂UV管理API
export const siliconeSandblastUvApi = {
  // ========== 产品管理 ==========

  // 获取所有产品
  getAllProducts: async (): Promise<SiliconeSandblastUvProduct[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_sandblast_uv/products`)
    return response.data
  },

  // 根据ID获取产品
  getProductById: async (id: number): Promise<SiliconeSandblastUvProduct> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_sandblast_uv/products/${id}`)
    return response.data
  },

  // 搜索产品
  searchProducts: async (keyword: string): Promise<SiliconeSandblastUvProduct[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_sandblast_uv/products/search`, { params: { keyword } })
    return response.data
  },

  // 保存产品
  saveProduct: async (product: SiliconeSandblastUvProduct): Promise<SiliconeSandblastUvProduct> => {
    const response = await axios.post(`${API_BASE_URL}/silicone_sandblast_uv/products`, product)
    return response.data
  },

  // 更新产品
  updateProduct: async (id: number, product: SiliconeSandblastUvProduct): Promise<SiliconeSandblastUvProduct> => {
    const response = await axios.put(`${API_BASE_URL}/silicone_sandblast_uv/products/${id}`, product)
    return response.data
  },

  // 删除产品
  deleteProduct: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/silicone_sandblast_uv/products/${id}`)
  },

  // ========== 兼容性管理 ==========

  // 获取所有兼容性
  getAllCompatibilities: async (): Promise<SiliconeSandblastUvCompatibility[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_sandblast_uv/compatibilities`)
    return response.data
  },

  // 根据产品ID获取兼容性列表
  getCompatibilitiesByProductId: async (productId: number): Promise<SiliconeSandblastUvCompatibility[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_sandblast_uv/compatibilities/product/${productId}`)
    return response.data
  },

  // 根据ID获取兼容性
  getCompatibilityById: async (id: number): Promise<SiliconeSandblastUvCompatibility> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_sandblast_uv/compatibilities/${id}`)
    return response.data
  },

  // 保存兼容性
  saveCompatibility: async (compatibility: SiliconeSandblastUvCompatibility): Promise<SiliconeSandblastUvCompatibility> => {
    const response = await axios.post(`${API_BASE_URL}/silicone_sandblast_uv/compatibilities`, compatibility)
    return response.data
  },

  // 更新兼容性
  updateCompatibility: async (id: number, compatibility: SiliconeSandblastUvCompatibility): Promise<SiliconeSandblastUvCompatibility> => {
    const response = await axios.put(`${API_BASE_URL}/silicone_sandblast_uv/compatibilities/${id}`, compatibility)
    return response.data
  },

  // 删除兼容性
  deleteCompatibility: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/silicone_sandblast_uv/compatibilities/${id}`)
  },

  // 批量保存兼容性
  batchSaveCompatibilities: async (compatibilities: SiliconeSandblastUvCompatibility[]): Promise<{ success: boolean; message: string }> => {
    const response = await axios.post(`${API_BASE_URL}/silicone_sandblast_uv/compatibilities/batch`, compatibilities)
    return response.data
  }
}
