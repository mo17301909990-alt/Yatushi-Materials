import axios from 'axios'

const API_BASE_URL = '/api'

// 分页响应类型
export interface PaginationResponse {
  content: PostProcessingPrint[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

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

export interface PostProcessingPrint {
  id?: number
  productName: string
  productModelNumber: string
  color: string
  clothPaperTypeId: number
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
  createdAt?: string
  updatedAt?: string
}

export interface CreatePostProcessingPrint {
  productName: string
  productModelNumber: string
  color: string
  clothPaperTypeId: number
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
}

export interface UpdatePostProcessingPrint {
  productName?: stri
  productModelNumber?: string
  color?: string
  clothPaperTypeId?: number
  compatibilityStatus?: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
}

export const postProcessingPrintApi = {
  // 获取所有印刷配置
  getAllPrintConfigs: async (): Promise<PostProcessingPrint[]> => {
    const response = await axios.get<PostProcessingPrint[]>(`${API_BASE_URL}/api/post-processing-print`)
    return response.data || []
  },

  // 分页获取印刷配置
  getPrintConfigsWithPagination: async (page: number = 1, size: number = 10): Promise<PaginationResponse> => {
    const response = await axios.get<PaginationResponse>(`${API_BASE_URL}/api/post-processing-print/pagination?page=${page}&size=${size}`)
    return response.data
  },

  // 根据ID获取印刷配置
  getPrintConfigById: async (id: number): Promise<PostProcessingPrint> => {
    const response = await axios.get<PostProcessingPrint>(`${API_BASE_URL}/api/post-processing-print/${id}`)
    return response.data
  },

  // 创建印刷配置
  createPrintConfig: async (data: CreatePostProcessingPrint): Promise<PostProcessingPrint> => {
    const response = await axios.post<PostProcessingPrint>(`${API_BASE_URL}/api/post-processing-print`, data)
    return response.data
  },

  // 更新印刷配置
  updatePrintConfig: async (id: number, data: UpdatePostProcessingPrint): Promise<PostProcessingPrint> => {
    const response = await axios.put<PostProcessingPrint>(`${API_BASE_URL}/api/post-processing-print/${id}`, data)
    return response.data
  },

  // 删除印刷配置
  deletePrintConfig: async (id: number): Promise<OperationResult> => {
    const response = await axios.delete<OperationResult>(`${API_BASE_URL}/api/post-processing-print/${id}`)
    return response.data
  },

  // 根据燙金紙系列搜索
  searchByProductName: async (productName: string): Promise<PostProcessingPrint[]> => {
    const response = await axios.get<PostProcessingPrint[]>(`${API_BASE_URL}/api/post-processing-print/search/product-name?productName=${encodeURIComponent(productName)}`)
    return response.data || []
  },

  // 根据产品型号搜索
  searchByProductModelNumber: async (productModelNumber: string): Promise<PostProcessingPrint[]> => {
    const response = await axios.get<PostProcessingPrint[]>(`${API_BASE_URL}/api/post-processing-print/search/product-model?productModelNumber=${encodeURIComponent(productModelNumber)}`)
    return response.data || []
  },

  // 根据颜色搜索
  searchByColor: async (color: string): Promise<PostProcessingPrint[]> => {
    const response = await axios.get<PostProcessingPrint[]>(`${API_BASE_URL}/api/post-processing-print/search/color?color=${encodeURIComponent(color)}`)
    return response.data || []
  },

  // 根据布面纸类型ID搜索
  searchByClothPaperTypeId: async (clothPaperTypeId: number): Promise<PostProcessingPrint[]> => {
    const response = await axios.get<PostProcessingPrint[]>(`${API_BASE_URL}/api/post-processing-print/search/cloth-paper-type?clothPaperTypeId=${clothPaperTypeId}`)
    return response.data || []
  },

  // 根据布面纸类型名称搜索
  searchByClothPaperType: async (clothPaperType: string): Promise<PostProcessingPrint[]> => {
    const response = await axios.get<PostProcessingPrint[]>(`${API_BASE_URL}/api/post-processing-print/search/cloth-paper-type-name?clothPaperType=${encodeURIComponent(clothPaperType)}`)
    return response.data || []
  },

  // 获取所有可用的燙金紙系列
  getAllProductNames: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print/options/product-names`)
    return response.data || []
  },

  // 获取所有可用的产品型号
  getAllProductModelNumbers: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print/options/product-model-numbers`)
    return response.data || []
  },

  // 获取所有可用的颜色
  getAllColors: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print/options/colors`)
    return response.data || []
  },

  // 根据产品名称和型号获取颜色选项
  getColorsByProductAndModel: async (productName: string, productModelNumber: string): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print/options/colors-by-product-and-model`, {
      params: { productName, productModelNumber }
    })
    return response.data || []
  },

  // 根据产品名称获取颜色选项
  getColorsByProductName: async (productName: string): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print/options/colors-by-product-name`, {
      params: { productName }
    })
    return response.data || []
  },

  // 根据产品型号获取颜色选项
  getColorsByProductModelNumber: async (productModelNumber: string): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print/options/colors-by-product-model`, {
      params: { productModelNumber }
    })
    return response.data || []
  },

  // 批量更新兼容性状态
  batchUpdateStatus: async (ids: number[], compatibilityStatus: 'V' | 'X' | 'NA' | '▷'): Promise<OperationResult> => {
    const response = await axios.put<OperationResult>(`${API_BASE_URL}/api/post-processing-print/batch/status`, {
      ids,
      compatibilityStatus
    })
    return response.data
  },

  // 批量删除印刷配置
  batchDeletePrintConfigs: async (ids: number[]): Promise<OperationResult> => {
    const response = await axios.delete<OperationResult>(`${API_BASE_URL}/api/post-processing-print/batch`, { data: ids })
    return response.data
  },

  // 检查唯一性
  checkUnique: async (
    productName: string,
    productModelNumber: string | undefined,
    color: string | undefined,
    clothPaperTypeId: number,
    paperType: string | undefined
  ): Promise<PostProcessingPrint | null> => {
    try {
      const params: any = {
        productName,
        clothPaperTypeId
      }
      if (productModelNumber) params.productModelNumber = productModelNumber
      if (color) params.color = color
      if (paperType) params.paperType = paperType
      
      const response = await axios.get<PostProcessingPrint>(`${API_BASE_URL}/api/post-processing-print/check-unique`, { params })
      return response.data
    } catch (error: any) {
      if (error.response?.status === 404) {
        return null
      }
      throw error
    }
  },

  // 导出印刷配置
  exportPrintConfigs: async (): Promise<Blob> => {
    const response = await axios.get(`${API_BASE_URL}/api/post-processing-print/export`, {
      responseType: 'blob'
    })
    return response.data
  },

  // 导入印刷配置
  importPrintConfigs: async (file: File): Promise<{
    success: boolean
    totalCount: number
    successCount: number
    failCount: number
    message: string
    errorMessages?: string[]
  }> => {
    const formData = new FormData()
    formData.append('file', file)
    const response = await axios.post(`${API_BASE_URL}/api/post-processing-print/import`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    return response.data
  },

  // 下载导入模板
  downloadImportTemplate: async (): Promise<Blob> => {
    const response = await axios.get(`${API_BASE_URL}/api/post-processing-print/import-template`, {
      responseType: 'blob'
    })
    return response.data
  }
}
