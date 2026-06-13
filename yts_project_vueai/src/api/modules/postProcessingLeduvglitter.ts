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

export interface PostProcessingLeduvglitter {
  id?: number
  productName: string
  productModelNumber: string
  clothPaperTypeId: number
  clothPaperTypeName?: string
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
  createdAt?: string
  updatedAt?: string
}

export interface PaginationResponse {
  data: PostProcessingLeduvglitter[]
  totalCount: number
  currentPage: number
  pageSize: number
  totalPages: number
}

export interface CreatePostProcessingLeduvglitter {
  productName: string
  productModelNumber: string
  clothPaperTypeId: number
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
}

export interface UpdatePostProcessingLeduvglitter {
  productName?: string
  productModelNumber?: string
  clothPaperTypeId?: number
  compatibilityStatus?: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
}

export const postProcessingLeduvglitterApi = {
  // 获取所有LED UV灑閃粉配置
  getAllLeduvglitterConfigs: async (): Promise<PostProcessingLeduvglitter[]> => {
    const response = await axios.get<PostProcessingLeduvglitter[]>(`${API_BASE_URL}/api/post-processing-leduvglitter`)
    return response.data || []
  },

  // 分页获取LED UV灑閃粉配置
  getLeduvglitterConfigsWithPagination: async (page: number = 1, size: number = 10): Promise<PaginationResponse> => {
    const response = await axios.get<PaginationResponse>(`${API_BASE_URL}/api/post-processing-leduvglitter/pagination?page=${page}&size=${size}`)
    return response.data
  },

  // 根据ID获取LED UV灑閃粉配置
  getLeduvglitterConfigById: async (id: number): Promise<PostProcessingLeduvglitter> => {
    const response = await axios.get<PostProcessingLeduvglitter>(`${API_BASE_URL}/api/post-processing-leduvglitter/${id}`)
    return response.data
  },

  // 创建LED UV灑閃粉配置
  createLeduvglitterConfig: async (data: CreatePostProcessingLeduvglitter): Promise<PostProcessingLeduvglitter> => {
    const response = await axios.post<PostProcessingLeduvglitter>(`${API_BASE_URL}/api/post-processing-leduvglitter`, data)
    return response.data
  },

  // 更新LED UV灑閃粉配置
  updateLeduvglitterConfig: async (id: number, data: UpdatePostProcessingLeduvglitter): Promise<PostProcessingLeduvglitter> => {
    const response = await axios.put<PostProcessingLeduvglitter>(`${API_BASE_URL}/api/post-processing-leduvglitter/${id}`, data)
    return response.data
  },

  // 删除LED UV灑閃粉配置
  deleteLeduvglitterConfig: async (id: number): Promise<OperationResult> => {
    const response = await axios.delete<OperationResult>(`${API_BASE_URL}/api/post-processing-leduvglitter/${id}`)
    return response.data
  },

  // 根据燙金紙系列搜索
  searchByProductName: async (productName: string): Promise<PostProcessingLeduvglitter[]> => {
    const response = await axios.get<PostProcessingLeduvglitter[]>(`${API_BASE_URL}/api/post-processing-leduvglitter/search/product-name?productName=${encodeURIComponent(productName)}`)
    return response.data || []
  },

  // 根据产品型号搜索
  searchByProductModelNumber: async (productModelNumber: string): Promise<PostProcessingLeduvglitter[]> => {
    const response = await axios.get<PostProcessingLeduvglitter[]>(`${API_BASE_URL}/api/post-processing-leduvglitter/search/product-model?productModelNumber=${encodeURIComponent(productModelNumber)}`)
    return response.data || []
  },

  // 根据布面纸类型ID搜索
  searchByClothPaperTypeId: async (clothPaperTypeId: number): Promise<PostProcessingLeduvglitter[]> => {
    const response = await axios.get<PostProcessingLeduvglitter[]>(`${API_BASE_URL}/api/post-processing-leduvglitter/search/cloth-paper-type?clothPaperTypeId=${clothPaperTypeId}`)
    return response.data || []
  },

  // 根据纸张类型搜索
  searchByPaperType: async (paperType: string): Promise<PostProcessingLeduvglitter[]> => {
    const response = await axios.get<PostProcessingLeduvglitter[]>(`${API_BASE_URL}/api/post-processing-leduvglitter/search/paper-type?paperType=${encodeURIComponent(paperType)}`)
    return response.data || []
  },

  // 获取所有可用的燙金紙系列
  getAllProductNames: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-leduvglitter/options/product-names`)
    return response.data || []
  },

  // 获取所有可用的产品型号
  getAllProductModelNumbers: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-leduvglitter/options/product-model-numbers`)
    return response.data || []
  },

  // 获取所有可用的布面纸类型ID
  getAllClothPaperTypeIds: async (): Promise<number[]> => {
    const response = await axios.get<number[]>(`${API_BASE_URL}/api/post-processing-leduvglitter/options/cloth-paper-type-ids`)
    return response.data || []
  },

  // 获取所有可用的纸张类型
  getAllPaperTypes: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-leduvglitter/options/paper-types`)
    return response.data || []
  },

  // 检查絲印LED UV灑閃粉后加工兼容性
  checkCompatibility: async (
    productName: string,
    productModelNumber: string,
    clothPaperTypeId: number,
    paperType?: string
  ): Promise<string> => {
    const params = new URLSearchParams({
      productName,
      productModelNumber,
      clothPaperTypeId: clothPaperTypeId.toString()
    })
    if (paperType) {
      params.append('paperType', paperType)
    }
    const response = await axios.get<string>(`${API_BASE_URL}/api/post-processing-leduvglitter/check-compatibility?${params}`)
    return response.data
  },

  // 获取布面纸类型选项（用于下拉选择）
  getClothPaperTypeOptions: async (): Promise<Array<{id: number, label: string}>> => {
    try {
      const response = await axios.get<Array<{id: number, typeName: string, category: string}>>(`${API_BASE_URL}/api/cloth-paper-types/active`)
      return response.data.map(item => ({
        id: item.id,
        label: `${item.category}.${item.typeName} (ID: ${item.id})`
      }))
    } catch (error) {
      console.error('获取布面纸类型选项失败:', error)
      return []
    }
  },

  // 根据产品名称获取对应的产品型号选项
  getProductModelNumbersByProductName: async (productName: string): Promise<string[]> => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/product-management/search/name`, {
        params: { name: productName }
      })
      const products = response.data || []
      const modelNumbers = [...new Set(products.map((product: any) => product.modelNumber).filter((model: string) => model && model.trim() !== ''))]
      return modelNumbers
    } catch (error) {
      console.error('获取产品型号失败:', error)
      return []
    }
  },

  // 根据产品名称获取对应的纸张类型（参考常用界面映射的实现）
  getPaperTypesByProductName: async (productName: string): Promise<string[]> => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/product-management/search/name`, {
        params: { name: productName }
      })
      const products = response.data || []
      const paperTypes = [...new Set(products
        .map((product: any) => product.hotStampingPaperType)
        .filter((type: string) => type && type.trim() !== '')
      )]
      return paperTypes
    } catch (error) {
      console.error('获取纸张类型失败:', error)
      return []
    }
  }
}

// 共享的产品管理API方法 - 供其他后加工配置页面使用
export const sharedProductApi = {
  // 获取所有产品名称
  getAllProductNames: async (): Promise<string[]> => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/product-management/names`)
      return response.data || []
    } catch (error) {
      console.error('获取产品名称失败:', error)
      return []
    }
  },

  // 根据产品名称获取对应的产品型号选项
  getProductModelNumbersByProductName: async (productName: string): Promise<string[]> => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/product-management/search/name`, {
        params: { name: productName }
      })
      const products = response.data || []
      return [...new Set(products.map((product: any) => product.modelNumber).filter((model: string) => model && model.trim() !== ''))]
    } catch (error) {
      console.error('获取产品型号失败:', error)
      return []
    }
  },

  // 根据产品名称获取对应的纸张类型
  getPaperTypesByProductName: async (productName: string): Promise<string[]> => {
    try {
      const response = await axios.get(`${API_BASE_URL}/api/product-management/search/name`, {
        params: { name: productName }
      })
      const products = response.data || []
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

  // 获取所有可用的纸张类型（性能类型）
  getAllPaperTypes: async (): Promise<string[]> => {
    try {
      const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-leduvglitter/options/paper-types`)
      return response.data || []
    } catch (error) {
      console.error('获取所有纸张类型失败:', error)
      return []
    }
  }
}

