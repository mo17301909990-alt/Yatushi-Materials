import axios from 'axios'

// 硅胶粗纹UV产品接口
export interface SiliconeCoarseUvProduct {
  id?: number
  materialCode?: string
  supplierCode?: string
  stockCode?: string
  materialName: string
  usageText?: string
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

// 硅胶粗纹UV兼容性接口
export interface SiliconeCoarseUvCompatibility {
  id?: number
  productId: number
  postProcessingStep: string
  compatibilityStatus: string
  displayOrder?: number
  createdAt?: string
  productName?: string
}

const API_BASE_URL = '/api'

export const siliconeCoarseUvApi = {
  // ========== 产品管理 ==========

  getAllProducts: async (): Promise<SiliconeCoarseUvProduct[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_coarse_uv/products`)
    return response.data
  },

  getProductById: async (id: number): Promise<SiliconeCoarseUvProduct> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_coarse_uv/products/${id}`)
    return response.data
  },

  searchProducts: async (keyword: string): Promise<SiliconeCoarseUvProduct[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_coarse_uv/products/search`, {
      params: { keyword }
    })
    return response.data
  },

  createProduct: async (product: SiliconeCoarseUvProduct): Promise<SiliconeCoarseUvProduct> => {
    const response = await axios.post(`${API_BASE_URL}/silicone_coarse_uv/products`, product)
    return response.data
  },

  updateProduct: async (id: number, product: SiliconeCoarseUvProduct): Promise<SiliconeCoarseUvProduct> => {
    const response = await axios.put(`${API_BASE_URL}/silicone_coarse_uv/products/${id}`, product)
    return response.data
  },

  deleteProduct: async (id: number): Promise<{ success: boolean; message: string }> => {
    const response = await axios.delete(`${API_BASE_URL}/silicone_coarse_uv/products/${id}`)
    return response.data
  },

  // ========== 兼容性管理 ==========

  getAllCompatibilities: async (): Promise<SiliconeCoarseUvCompatibility[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_coarse_uv/compatibilities`)
    return response.data
  },

  getCompatibilitiesByProductId: async (productId: number): Promise<SiliconeCoarseUvCompatibility[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_coarse_uv/compatibilities/product/${productId}`)
    return response.data
  },

  getCompatibilityById: async (id: number): Promise<SiliconeCoarseUvCompatibility> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_coarse_uv/compatibilities/${id}`)
    return response.data
  },

  createCompatibility: async (compatibility: SiliconeCoarseUvCompatibility): Promise<SiliconeCoarseUvCompatibility> => {
    const response = await axios.post(`${API_BASE_URL}/silicone_coarse_uv/compatibilities`, compatibility)
    return response.data
  },

  updateCompatibility: async (id: number, compatibility: SiliconeCoarseUvCompatibility): Promise<SiliconeCoarseUvCompatibility> => {
    const response = await axios.put(`${API_BASE_URL}/silicone_coarse_uv/compatibilities/${id}`, compatibility)
    return response.data
  },

  deleteCompatibility: async (id: number): Promise<{ success: boolean; message: string }> => {
    const response = await axios.delete(`${API_BASE_URL}/silicone_coarse_uv/compatibilities/${id}`)
    return response.data
  },

  batchSaveCompatibilities: async (compatibilities: SiliconeCoarseUvCompatibility[]): Promise<{ success: boolean; message: string }> => {
    const response = await axios.post(`${API_BASE_URL}/silicone_coarse_uv/compatibilities/batch`, compatibilities)
    return response.data
  },

  // ========== 选项数据 ==========

  getAllPostProcessingSteps: async (): Promise<string[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_coarse_uv/options/post-processing-steps`)
    return response.data
  },

  getAllProductNames: async (): Promise<string[]> => {
    const response = await axios.get(`${API_BASE_URL}/silicone_coarse_uv/options/product-names`)
    return response.data
  }
}
