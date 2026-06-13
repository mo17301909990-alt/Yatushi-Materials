import axios from 'axios'
import type {
  HotStampingMaterial,
  ProductType,
  HotStampingTypeOption,
  HotStampingPattern,
  HotStampingPatternAreaOption,
  PreProcessingInterface,
  ApplicableInterface,
  PostProcessingPrintUv,
  PostProcessingPrint,
  PostProcessingLeduvglitter,
  HotStampingMaterialQueryParams,
  ProductTypeQueryParams,
  HotStampingTypeQueryParams,
  HotStampingPatternQueryParams,
  HotStampingPatternAreaQueryParams,
  PreProcessingInterfaceQueryParams,
  ApplicableInterfaceQueryParams,
  PostProcessingQueryParams,
  ApiResponse,
  PaginatedResponse,
  OperationResult
} from '@/types/materialManagement'

// 基礎API配置
const API_BASE_URL = '/api'

// 燙金物料信息管理API
export const hotStampingMaterialApi = {
  // 獲取燙金物料列表
  getMaterials: async (params: HotStampingMaterialQueryParams = {}): Promise<ApiResponse<PaginatedResponse<HotStampingMaterial>>> => {
    const response = await axios.get(`${API_BASE_URL}/hot-stamping-materials`, { params })
    return response.data
  },

  // 獲取單個燙金物料
  getMaterial: async (id: number): Promise<ApiResponse<HotStampingMaterial>> => {
    const response = await axios.get(`${API_BASE_URL}/hot-stamping-materials/${id}`)
    return response.data
  },

  // 創建燙金物料
  createMaterial: async (material: HotStampingMaterial): Promise<ApiResponse<HotStampingMaterial>> => {
    const response = await axios.post(`${API_BASE_URL}/hot-stamping-materials`, material)
    return response.data
  },

  // 更新燙金物料
  updateMaterial: async (id: number, material: HotStampingMaterial): Promise<ApiResponse<HotStampingMaterial>> => {
    const response = await axios.put(`${API_BASE_URL}/hot-stamping-materials/${id}`, material)
    return response.data
  },

  // 删除烫金物料
  deleteMaterial: async (id: number): Promise<ApiResponse<OperationResult>> => {
    const response = await axios.delete(`${API_BASE_URL}/hot-stamping-materials/${id}`)
    return response.data
  }
}

// 产品类型管理API
export const productTypeApi = {
  // 获取产品类型列表
  getProductTypes: async (params: ProductTypeQueryParams = {}): Promise<ApiResponse<PaginatedResponse<ProductType>>> => {
    const response = await axios.get(`${API_BASE_URL}/product-types`, { params })
    return response.data
  },

  // 获取单个产品类型
  getProductType: async (id: number): Promise<ApiResponse<ProductType>> => {
    const response = await axios.get(`${API_BASE_URL}/product-types/${id}`)
    return response.data
  },

  // 创建产品类型
  createProductType: async (productType: ProductType): Promise<ApiResponse<ProductType>> => {
    const response = await axios.post(`${API_BASE_URL}/product-types`, productType)
    return response.data
  },

  // 更新产品类型
  updateProductType: async (id: number, productType: ProductType): Promise<ApiResponse<ProductType>> => {
    const response = await axios.put(`${API_BASE_URL}/product-types/${id}`, productType)
    return response.data
  },

  // 删除产品类型
  deleteProductType: async (id: number): Promise<ApiResponse<OperationResult>> => {
    const response = await axios.delete(`${API_BASE_URL}/product-types/${id}`)
    return response.data
  }
}

// 烫金类型管理API
export const hotStampingTypeApi = {
  // 获取烫金类型列表
  getHotStampingTypes: async (params: HotStampingTypeQueryParams = {}): Promise<ApiResponse<PaginatedResponse<HotStampingTypeOption>>> => {
    const response = await axios.get(`${API_BASE_URL}/hot-stamping-types`, { params })
    return response.data
  },

  // 获取单个烫金类型
  getHotStampingType: async (id: number): Promise<ApiResponse<HotStampingTypeOption>> => {
    const response = await axios.get(`${API_BASE_URL}/hot-stamping-types/${id}`)
    return response.data
  },

  // 创建烫金类型
  createHotStampingType: async (hotStampingType: HotStampingTypeOption): Promise<ApiResponse<HotStampingTypeOption>> => {
    const response = await axios.post(`${API_BASE_URL}/hot-stamping-types`, hotStampingType)
    return response.data
  },

  // 更新烫金类型
  updateHotStampingType: async (id: number, hotStampingType: HotStampingTypeOption): Promise<ApiResponse<HotStampingTypeOption>> => {
    const response = await axios.put(`${API_BASE_URL}/hot-stamping-types/${id}`, hotStampingType)
    return response.data
  },

  // 删除烫金类型
  deleteHotStampingType: async (id: number): Promise<ApiResponse<OperationResult>> => {
    const response = await axios.delete(`${API_BASE_URL}/hot-stamping-types/${id}`)
    return response.data
  }
}

// 烫金图案管理API
export const hotStampingPatternApi = {
  // 获取烫金图案列表
  getPatterns: async (params: HotStampingPatternQueryParams = {}): Promise<ApiResponse<PaginatedResponse<HotStampingPattern>>> => {
    const response = await axios.get(`${API_BASE_URL}/hot-stamping-patterns`, { params })
    return response.data
  },

  // 获取单个烫金图案
  getPattern: async (id: number): Promise<ApiResponse<HotStampingPattern>> => {
    const response = await axios.get(`${API_BASE_URL}/hot-stamping-patterns/${id}`)
    return response.data
  },

  // 创建烫金图案
  createPattern: async (pattern: HotStampingPattern): Promise<ApiResponse<HotStampingPattern>> => {
    const response = await axios.post(`${API_BASE_URL}/hot-stamping-patterns`, pattern)
    return response.data
  },

  // 更新烫金图案
  updatePattern: async (id: number, pattern: HotStampingPattern): Promise<ApiResponse<HotStampingPattern>> => {
    const response = await axios.put(`${API_BASE_URL}/hot-stamping-patterns/${id}`, pattern)
    return response.data
  },

  // 删除烫金图案
  deletePattern: async (id: number): Promise<ApiResponse<OperationResult>> => {
    const response = await axios.delete(`${API_BASE_URL}/hot-stamping-patterns/${id}`)
    return response.data
  }
}

// 烫金图案区域管理API
export const hotStampingPatternAreaApi = {
  // 获取烫金图案区域列表
  getPatternAreas: async (params: HotStampingPatternAreaQueryParams = {}): Promise<ApiResponse<PaginatedResponse<HotStampingPatternAreaOption>>> => {
    const response = await axios.get(`${API_BASE_URL}/hot-stamping-pattern-areas`, { params })
    return response.data
  },

  // 获取单个烫金图案区域
  getPatternArea: async (id: number): Promise<ApiResponse<HotStampingPatternAreaOption>> => {
    const response = await axios.get(`${API_BASE_URL}/hot-stamping-pattern-areas/${id}`)
    return response.data
  },

  // 创建烫金图案区域
  createPatternArea: async (patternArea: HotStampingPatternAreaOption): Promise<ApiResponse<HotStampingPatternAreaOption>> => {
    const response = await axios.post(`${API_BASE_URL}/hot-stamping-pattern-areas`, patternArea)
    return response.data
  },

  // 更新烫金图案区域
  updatePatternArea: async (id: number, patternArea: HotStampingPatternAreaOption): Promise<ApiResponse<HotStampingPatternAreaOption>> => {
    const response = await axios.put(`${API_BASE_URL}/hot-stamping-pattern-areas/${id}`, patternArea)
    return response.data
  },

  // 删除烫金图案区域
  deletePatternArea: async (id: number): Promise<ApiResponse<OperationResult>> => {
    const response = await axios.delete(`${API_BASE_URL}/hot-stamping-pattern-areas/${id}`)
    return response.data
  }
}

// 前工序界面管理API
export const preProcessingInterfaceApi = {
  // 获取前工序界面列表
  getInterfaces: async (params: PreProcessingInterfaceQueryParams = {}): Promise<ApiResponse<PaginatedResponse<PreProcessingInterface>>> => {
    const response = await axios.get(`${API_BASE_URL}/pre-processing-interfaces`, { params })
    return response.data
  },

  // 获取单个前工序界面
  getInterface: async (id: number): Promise<ApiResponse<PreProcessingInterface>> => {
    const response = await axios.get(`${API_BASE_URL}/pre-processing-interfaces/${id}`)
    return response.data
  },

  // 创建前工序界面
  createInterface: async (interface_: PreProcessingInterface): Promise<ApiResponse<PreProcessingInterface>> => {
    const response = await axios.post(`${API_BASE_URL}/pre-processing-interfaces`, interface_)
    return response.data
  },

  // 更新前工序界面
  updateInterface: async (id: number, interface_: PreProcessingInterface): Promise<ApiResponse<PreProcessingInterface>> => {
    const response = await axios.put(`${API_BASE_URL}/pre-processing-interfaces/${id}`, interface_)
    return response.data
  },

  // 删除前工序界面
  deleteInterface: async (id: number): Promise<ApiResponse<OperationResult>> => {
    const response = await axios.delete(`${API_BASE_URL}/pre-processing-interfaces/${id}`)
    return response.data
  }
}

// 适用界面管理API
export const applicableInterfaceApi = {
  // 获取适用界面列表
  getInterfaces: async (params: ApplicableInterfaceQueryParams = {}): Promise<ApiResponse<PaginatedResponse<ApplicableInterface>>> => {
    const response = await axios.get(`${API_BASE_URL}/applicable-interfaces`, { params })
    return response.data
  },

  // 获取单个适用界面
  getInterface: async (id: number): Promise<ApiResponse<ApplicableInterface>> => {
    const response = await axios.get(`${API_BASE_URL}/applicable-interfaces/${id}`)
    return response.data
  },

  // 创建适用界面
  createInterface: async (interface_: ApplicableInterface): Promise<ApiResponse<ApplicableInterface>> => {
    const response = await axios.post(`${API_BASE_URL}/applicable-interfaces`, interface_)
    return response.data
  },

  // 更新适用界面
  updateInterface: async (id: number, interface_: ApplicableInterface): Promise<ApiResponse<ApplicableInterface>> => {
    const response = await axios.put(`${API_BASE_URL}/applicable-interfaces/${id}`, interface_)
    return response.data
  },

  // 删除适用界面
  deleteInterface: async (id: number): Promise<ApiResponse<OperationResult>> => {
    const response = await axios.delete(`${API_BASE_URL}/applicable-interfaces/${id}`)
    return response.data
  }
}

// 烫后加工管理API
export const postProcessingApi = {
  // 印刷UV管理
  printUv: {
    // 获取印刷UV列表
    getList: async (params: PostProcessingQueryParams = {}): Promise<ApiResponse<PaginatedResponse<PostProcessingPrintUv>>> => {
      const response = await axios.get(`${API_BASE_URL}/post-processing-print-uv`, { params })
      return response.data
    },

    // 获取单个印刷UV
    get: async (id: number): Promise<ApiResponse<PostProcessingPrintUv>> => {
      const response = await axios.get(`${API_BASE_URL}/post-processing-print-uv/${id}`)
      return response.data
    },

    // 创建印刷UV
    create: async (printUv: PostProcessingPrintUv): Promise<ApiResponse<PostProcessingPrintUv>> => {
      const response = await axios.post(`${API_BASE_URL}/post-processing-print-uv`, printUv)
      return response.data
    },

    // 更新印刷UV
    update: async (id: number, printUv: PostProcessingPrintUv): Promise<ApiResponse<PostProcessingPrintUv>> => {
      const response = await axios.put(`${API_BASE_URL}/post-processing-print-uv/${id}`, printUv)
      return response.data
    },

    // 删除印刷UV
    delete: async (id: number): Promise<ApiResponse<OperationResult>> => {
      const response = await axios.delete(`${API_BASE_URL}/post-processing-print-uv/${id}`)
      return response.data
    }
  },

  // 印刷管理
  print: {
    // 获取印刷列表
    getList: async (params: PostProcessingQueryParams = {}): Promise<ApiResponse<PaginatedResponse<PostProcessingPrint>>> => {
      const response = await axios.get(`${API_BASE_URL}/post-processing-print`, { params })
      return response.data
    },

    // 获取单个印刷
    get: async (id: number): Promise<ApiResponse<PostProcessingPrint>> => {
      const response = await axios.get(`${API_BASE_URL}/post-processing-print/${id}`)
      return response.data
    },

    // 创建印刷
    create: async (print: PostProcessingPrint): Promise<ApiResponse<PostProcessingPrint>> => {
      const response = await axios.post(`${API_BASE_URL}/post-processing-print`, print)
      return response.data
    },

    // 更新印刷
    update: async (id: number, print: PostProcessingPrint): Promise<ApiResponse<PostProcessingPrint>> => {
      const response = await axios.put(`${API_BASE_URL}/post-processing-print/${id}`, print)
      return response.data
    },

    // 删除印刷
    delete: async (id: number): Promise<ApiResponse<OperationResult>> => {
      const response = await axios.delete(`${API_BASE_URL}/post-processing-print/${id}`)
      return response.data
    }
  },

  // 絲印LED UV灑閃粉管理
  leduvglitter: {
    // 获取絲印LED UV灑閃粉列表
    getList: async (params: PostProcessingQueryParams = {}): Promise<ApiResponse<PaginatedResponse<PostProcessingLeduvglitter>>> => {
      const response = await axios.get(`${API_BASE_URL}/post-processing-leduvglitter`, { params })
      return response.data
    },

    // 获取单个絲印LED UV灑閃粉
    get: async (id: number): Promise<ApiResponse<PostProcessingLeduvglitter>> => {
      const response = await axios.get(`${API_BASE_URL}/post-processing-leduvglitter/${id}`)
      return response.data
    },

    // 创建絲印LED UV灑閃粉
    create: async (leduvglitter: PostProcessingLeduvglitter): Promise<ApiResponse<PostProcessingLeduvglitter>> => {
      const response = await axios.post(`${API_BASE_URL}/post-processing-leduvglitter`, leduvglitter)
      return response.data
    },

    // 更新絲印LED UV灑閃粉
    update: async (id: number, leduvglitter: PostProcessingLeduvglitter): Promise<ApiResponse<PostProcessingLeduvglitter>> => {
      const response = await axios.put(`${API_BASE_URL}/post-processing-leduvglitter/${id}`, leduvglitter)
      return response.data
    },

    // 删除絲印LED UV灑閃粉
    delete: async (id: number): Promise<ApiResponse<OperationResult>> => {
      const response = await axios.delete(`${API_BASE_URL}/post-processing-leduvglitter/${id}`)
      return response.data
    }
  }
}

// 统计概览API
export const statisticsApi = {
  // 获取物料规则统计概览
  getMaterialRuleStatistics: async (): Promise<{
    hotStampingMaterialCount: number
    productTypeCount: number
    hotStampingPatternCount: number
    applicableInterfaceCount: number
  }> => {
    try {
      // 使用现有的后端API接口获取统计数据
      const [
        productsResponse,
        productTypesResponse,
        patternsResponse,
        interfacesResponse
      ] = await Promise.all([
        // 使用ProductController获取产品数量
        axios.get(`${API_BASE_URL}/api/product-management`),
        // 使用HotStampingCompatibilityController获取产品类型
        axios.get(`${API_BASE_URL}/api/compatibility/product-types`),
        // 使用HotStampingPatternBaseController获取图案数量
        axios.get(`${API_BASE_URL}/api/hot-stamping-pattern-base`),
        // 使用HotStampingCompatibilityController获取适用界面
        axios.get(`${API_BASE_URL}/api/compatibility/paper-performance-types`)
      ])

      return {
        hotStampingMaterialCount: Array.isArray(productsResponse.data) ? productsResponse.data.length : 0,
        productTypeCount: Array.isArray(productTypesResponse.data) ? productTypesResponse.data.length : 0,
        hotStampingPatternCount: Array.isArray(patternsResponse.data) ? patternsResponse.data.length : 0,
        applicableInterfaceCount: Array.isArray(interfacesResponse.data) ? interfacesResponse.data.length : 0
      }
    } catch (error) {
      console.error('获取统计数据失败:', error)
      // 返回默认值
      return {
        hotStampingMaterialCount: 0,
        productTypeCount: 0,
        hotStampingPatternCount: 0,
        applicableInterfaceCount: 0
      }
    }
  }
}

