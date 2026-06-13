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

export interface HotStampingTypeCompatibility {
  id?: number
  productName: string
  hotStampingTypeId: number
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
  isActive?: boolean
  createdAt?: string
  updatedAt?: string
  hotStampingTypeName?: string
}

export interface CreateHotStampingTypeCompatibility {
  productName: string
  hotStampingTypeId: number
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
  isActive?: boolean
}

export interface UpdateHotStampingTypeCompatibility {
  productName?: string
  hotStampingTypeId?: number
  compatibilityStatus?: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
  isActive?: boolean
}

export interface HotStampingTypeCompatibilityCopyResult {
  successCount: number
  failCount: number
  message: string
}

export const hotStampingTypeCompatibilityApi = {
  // 根据烫金类型ID获取兼容性配置列表
  getCompatibilityByHotStampingTypeId: async (hotStampingTypeId: number): Promise<HotStampingTypeCompatibility[]> => {
    const response = await axios.get<HotStampingTypeCompatibility[]>(`${API_BASE_URL}/api/hot-stamping-type-compatibility/hot-stamping-type/${hotStampingTypeId}`)
    return response.data || []
  },

  // 获取所有可用的燙金紙系列
  getAllProductNames: async (): Promise<string[]> => {
    try {
      const response = await axios.get<string[]>(`${API_BASE_URL}/api/product-management/names`)
      return response.data || []
    } catch (error) {
      console.error('获取产品名称失败:', error)
      return []
    }
  },

  // 获取所有可用的纸张类型
  getAllPaperTypes: async (): Promise<string[]> => {
    try {
      const response = await axios.get<string[]>(`${API_BASE_URL}/api/product-management/options/paper-types`)
      return response.data || []
    } catch (error) {
      console.error('获取纸张类型失败:', error)
      return []
    }
  },

  // 根据产品名称获取对应的纸张类型
  getPaperTypesByProductName: async (productName: string): Promise<string[]> => {
    try {
      // 使用ProductController的接口获取产品列表，然后提取纸张类型
      const response = await axios.get(`${API_BASE_URL}/api/product-management/search/name`, {
        params: { name: productName }
      })
      const products = response.data || []
      // 提取所有不重复的纸张类型
      const paperTypes = [...new Set(products
        .map((product: any) => product.hotStampingPaperType)
        .filter((type: string) => type && type.trim() !== '')
      )]
      return paperTypes
    } catch (error) {
      console.error('获取纸张类型失败:', error)
      return []
    }
  },

  // 创建兼容性配置
  createCompatibility: async (data: CreateHotStampingTypeCompatibility): Promise<HotStampingTypeCompatibility> => {
    const response = await axios.post<HotStampingTypeCompatibility>(`${API_BASE_URL}/api/hot-stamping-type-compatibility`, data)
    return response.data
  },

  // 更新兼容性配置
  updateCompatibility: async (id: number, data: UpdateHotStampingTypeCompatibility): Promise<HotStampingTypeCompatibility> => {
    const response = await axios.put<HotStampingTypeCompatibility>(`${API_BASE_URL}/api/hot-stamping-type-compatibility/${id}`, data)
    return response.data
  },

  // 删除兼容性配置
  deleteCompatibility: async (id: number): Promise<OperationResult> => {
    const response = await axios.delete<OperationResult>(`${API_BASE_URL}/api/hot-stamping-type-compatibility/${id}`)
    return response.data
  },

  // 批量复制兼容性配置到其他烫金类型
  batchCopyCompatibility: async (sourceIds: number[], targetHotStampingTypeIds: number[]): Promise<HotStampingTypeCompatibilityCopyResult> => {
    const response = await axios.post<HotStampingTypeCompatibilityCopyResult>(
      `${API_BASE_URL}/api/hot-stamping-type-compatibility/batch-copy`,
      { sourceIds, targetHotStampingTypeIds }
    )
    return response.data
  },

  // 批量删除兼容性配置
  batchDeleteCompatibility: async (ids: number[]): Promise<{ success: boolean; deletedCount: number; message: string }> => {
    const response = await axios.delete<{ success: boolean; deletedCount: number; message: string }>(
      `${API_BASE_URL}/api/hot-stamping-type-compatibility/batch`,
      { data: ids }
    )
    return response.data
  },
  
  // 获取所有兼容性配置（包含烫金类型信息）
  getAllCompatibility: async (): Promise<HotStampingTypeCompatibility[]> => {
    const response = await axios.get<HotStampingTypeCompatibility[]>(`${API_BASE_URL}/api/hot-stamping-type-compatibility/all`)
    return response.data || []
  }
}
