import axios from 'axios'

const API_BASE_URL = '/api'

// 定义接口类型
export interface HotStampingPatternAreaCompatibility {
  id?: number
  productName: string
  hotStampingPatternxAreaId: number
  compatibilityStatus: string
  paperType?: string
  optionName?: string
  patternType?: string
  createdAt?: string
  updatedAt?: string
}

export interface ApiResponse<T> {
  success: boolean
  data: T
  message?: string
}

// API 调用函数
export const hotStampingPatternAreaCompatibilityApi = {
  // 获取所有兼容性配置列表
  getAllCompatibility: async (): Promise<HotStampingPatternAreaCompatibility[]> => {
    const response = await axios.get(`${API_BASE_URL}/api/hot-stamping-pattern-area-compatibility/all`)
    return response.data
  },
  
  // 根据图案区域ID获取兼容性配置列表
  getCompatibilityByPatternAreaId: async (patternAreaId: number): Promise<HotStampingPatternAreaCompatibility[]> => {
    const response = await axios.get(`${API_BASE_URL}/api/hot-stamping-pattern-area-compatibility/pattern-area/${patternAreaId}`)
    return response.data
  },

  // 根据ID获取兼容性配置
  getCompatibilityById: async (id: number): Promise<HotStampingPatternAreaCompatibility> => {
    const response = await axios.get(`${API_BASE_URL}/api/hot-stamping-pattern-area-compatibility/${id}`)
    return response.data
  },

  // 创建兼容性配置
  createCompatibility: async (compatibility: Omit<HotStampingPatternAreaCompatibility, 'id'>): Promise<HotStampingPatternAreaCompatibility> => {
    const response = await axios.post(`${API_BASE_URL}/api/hot-stamping-pattern-area-compatibility`, compatibility)
    return response.data
  },

  // 更新兼容性配置
  updateCompatibility: async (id: number, compatibility: Partial<HotStampingPatternAreaCompatibility>): Promise<HotStampingPatternAreaCompatibility> => {
    const response = await axios.put(`${API_BASE_URL}/api/hot-stamping-pattern-area-compatibility/${id}`, compatibility)
    return response.data
  },

  // 删除兼容性配置
  deleteCompatibility: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/api/hot-stamping-pattern-area-compatibility/${id}`)
  },

  // 根据图案区域ID删除所有兼容性配置
  deleteCompatibilityByPatternAreaId: async (patternAreaId: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/api/hot-stamping-pattern-area-compatibility/pattern-area/${patternAreaId}`)
  },

  // 获取所有可用的燙金紙系列
  getAllProductNames: async (): Promise<string[]> => {
    const response = await axios.get(`${API_BASE_URL}/api/hot-stamping-pattern-area-compatibility/product-names`)
    return response.data
  }
}