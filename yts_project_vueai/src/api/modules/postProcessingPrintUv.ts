import axios from 'axios'

const API_BASE_URL = '/api'

// 分页响应类型
export interface PaginationResponse {
  content: PostProcessingPrintUv[]
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

export interface PostProcessingPrintUv {
  id?: number
  productName: string
  productModelNumber: string
  companyNumber: string
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
  createdAt?: string
  updatedAt?: string
}

export interface CreatePostProcessingPrintUv {
  productName: string
  productModelNumber: string
  companyNumber: string
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
}

export interface UpdatePostProcessingPrintUv {
  productName?: string
  productModelNumber?: string
  companyNumber?: string | null
  compatibilityStatus?: 'V' | 'X' | 'NA' | '▷'
  paperType?: string
}

export const postProcessingPrintUvApi = {
  // 获取所有UV印刷配置
  getAllUvPrintConfigs: async (): Promise<PostProcessingPrintUv[]> => {
    const response = await axios.get<PostProcessingPrintUv[]>(`${API_BASE_URL}/api/post-processing-print-uv`)
    return response.data || []
  },

  // 分页获取UV印刷配置
  getUvPrintConfigsWithPagination: async (page: number = 1, size: number = 10): Promise<PaginationResponse> => {
    const response = await axios.get<PaginationResponse>(`${API_BASE_URL}/api/post-processing-print-uv/pagination?page=${page}&size=${size}`)
    return response.data
  },

  // 根据ID获取UV印刷配置
  getUvPrintConfigById: async (id: number): Promise<PostProcessingPrintUv> => {
    const response = await axios.get<PostProcessingPrintUv>(`${API_BASE_URL}/api/post-processing-print-uv/${id}`)
    return response.data
  },

  // 创建UV印刷配置
  createUvPrintConfig: async (data: CreatePostProcessingPrintUv): Promise<PostProcessingPrintUv> => {
    const response = await axios.post<PostProcessingPrintUv>(`${API_BASE_URL}/api/post-processing-print-uv`, data)
    return response.data
  },

  // 更新UV印刷配置
  updateUvPrintConfig: async (id: number, data: UpdatePostProcessingPrintUv): Promise<PostProcessingPrintUv> => {
    const response = await axios.put<PostProcessingPrintUv>(`${API_BASE_URL}/api/post-processing-print-uv/${id}`, data)
    return response.data
  },

  // 删除UV印刷配置
  deleteUvPrintConfig: async (id: number): Promise<OperationResult> => {
    const response = await axios.delete<OperationResult>(`${API_BASE_URL}/api/post-processing-print-uv/${id}`)
    return response.data
  },

  // 根据燙金紙系列搜索
  searchByProductName: async (productName: string): Promise<PostProcessingPrintUv[]> => {
    const response = await axios.get<PostProcessingPrintUv[]>(`${API_BASE_URL}/api/post-processing-print-uv/search/product-name?productName=${encodeURIComponent(productName)}`)
    return response.data || []
  },

  // 根据产品型号搜索
  searchByProductModelNumber: async (productModelNumber: string): Promise<PostProcessingPrintUv[]> => {
    const response = await axios.get<PostProcessingPrintUv[]>(`${API_BASE_URL}/api/post-processing-print-uv/search/product-model?productModelNumber=${encodeURIComponent(productModelNumber)}`)
    return response.data || []
  },

  // 根据公司编号搜索
  searchByCompanyNumber: async (companyNumber: string): Promise<PostProcessingPrintUv[]> => {
    const response = await axios.get<PostProcessingPrintUv[]>(`${API_BASE_URL}/api/post-processing-print-uv/search/company-number?companyNumber=${encodeURIComponent(companyNumber)}`)
    return response.data || []
  },

  // 根据纸张类型搜索
  searchByPaperType: async (paperType: string): Promise<PostProcessingPrintUv[]> => {
    const response = await axios.get<PostProcessingPrintUv[]>(`${API_BASE_URL}/api/post-processing-print-uv/search/paper-type?paperType=${encodeURIComponent(paperType)}`)
    return response.data || []
  },

  // 获取所有可用的燙金紙系列
  getAllProductNames: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print-uv/options/product-names`)
    return response.data || []
  },

  // 获取所有可用的产品型号
  getAllProductModelNumbers: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print-uv/options/product-model-numbers`)
    return response.data || []
  },

  // 获取所有可用的公司编号
  getAllCompanyNumbers: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print-uv/options/company-numbers`)
    return response.data || []
  },
  
  // 根据产品系列名称获取公司编号列表
  getCompanyNumbersByProductName: async (productName: string): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print-uv/options/company-numbers-by-product-name?productName=${encodeURIComponent(productName)}`)
    return response.data || []
  },
  
  // 根据产品型号和名称获取公司编号列表
  getCompanyNumbersByProductModel: async (productModelNumber: string, productName: string): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print-uv/options/company-numbers-by-product-model?productModelNumber=${encodeURIComponent(productModelNumber)}&productName=${encodeURIComponent(productName)}`)
    return response.data || []
  },
  
  // 验证公司编号是否属于指定的产品系列或型号
  validateCompanyNumber: async (companyNumber: string, productName: string, productModelNumber?: string): Promise<{ valid: boolean; message?: string }> => {
    const params = new URLSearchParams({
      companyNumber,
      productName
    })
    if (productModelNumber) {
      params.append('productModelNumber', productModelNumber)
    }
    const response = await axios.get<{ valid: boolean; message?: string }>(`${API_BASE_URL}/api/post-processing-print-uv/validate-company-number?${params}`)
    return response.data
  },

  // 获取所有可用的纸张类型
  getAllPaperTypes: async (): Promise<string[]> => {
    const response = await axios.get<string[]>(`${API_BASE_URL}/api/post-processing-print-uv/options/paper-types`)
    return response.data || []
  },

  // 检查印刷UV后加工兼容性
  checkCompatibility: async (
    productName: string,
    productModelNumber: string,
    companyNumber: string,
    paperType?: string
  ): Promise<string> => {
    const params = new URLSearchParams({
      productName,
      productModelNumber,
      companyNumber
    })
    if (paperType) {
      params.append('paperType', paperType)
    }
    const response = await axios.get<string>(`${API_BASE_URL}/api/post-processing-print-uv/check-compatibility?${params}`)
    return response.data
  }
}
