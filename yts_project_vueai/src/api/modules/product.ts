import axios from 'axios'

// 产品信息接口
export interface Product {
  id?: number
  name: string
  modelNumber: string
  materialNumber: string
  hotStampingPaperType?: string
  descirption?: string
  gaishu?: string
  fengdu?: string
  paizi?: string
  danwei?: string
  isChangyong?: string
  isJiehuo?: boolean
  createdAt?: string
  updatedAt?: string
  specifications?: Specification[]
  pricingList?: Pricing[]
}

// 完整产品信息接口 - 包含products、leo_gp_numbers、specifications、pricing表的数据
export interface CompleteProductInfo {
  id?: number
  name: string
  modelNumber: string
  materialNumber: string
  hotStampingPaperType?: string
  description?: string  // 产品描述
  gaishu?: string
  price?: number        // 价格信息
  fengdu?: string
  paizi?: string
  danwei?: string
  isChangyong?: string
  isJiehuo?: boolean
  companyNumber?: string  // Leo Touch编号
  gpNo?: string          // SPNO
  color?: string         // 颜色
  tightness?: string     // 金纸松紧度
  performance?: string   // 金纸性能
  size?: string          // 尺寸
  temperatureRange?: string // 温度范围
  status?: '可用' | '废弃'  // 物料状态标记
  projectId?: number
  specifications?: Specification[]  // 规格信息列表
  pricingList?: Pricing[]          // 价格信息列表
}

// 规格信息接口
export interface Specification {
  id?: number
  projectId: number
  color?: string
  color_num?: string  // 颜色编码
  size?: string
  tightness?: string
  temperatureRange?: string
  performance?: string
  createdAt?: string
  updatedAt?: string
}

// 价格信息接口
export interface Pricing {
  id?: number
  projectId: number
  price?: number
  createdAt?: string
  updatedAt?: string
}

// 产品搜索参数接口
export interface ProductSearchParams {
  keyword?: string
  name?: string
  modelNumber?: string
  materialNumber?: string
  hotStampingPaperType?: string
}

// 产品选项接口
export interface ProductOptions {
  hotStampingPaperTypes: string[]
  danweiOptions: string[]
  paiziOptions: string[]
  fengduOptions: string[]
  colorOptions: string[]
  colorNumOptions: string[]  // 颜色编码选项
  sizeOptions: string[]
  tightnessOptions: string[]
  temperatureRangeOptions: string[]
}

const API_BASE_URL = '/api'

// 产品管理API
export const productApi = {
  // 获取所有产品
  getAllProducts: async (): Promise<Product[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management`)
    return response.data
  },

  // 根据ID获取产品
  getProductById: async (id: number): Promise<Product> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/${id}`)
    return response.data
  },

  // 搜索产品
  searchProducts: async (params: ProductSearchParams): Promise<Product[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/search`, { params })
    return response.data
  },

  // 根据燙金紙系列查询
  getProductsByName: async (name: string): Promise<Product[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/search/name`, { 
      params: { name } 
    })
    return response.data
  },

  // 根据型号查询
  getProductsByModelNumber: async (modelNumber: string): Promise<Product[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/search/model`, { 
      params: { modelNumber } 
    })
    return response.data
  },

  // 根据物料编号查询
  getProductsByMaterialNumber: async (materialNumber: string): Promise<Product[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/search/material`, { 
      params: { materialNumber } 
    })
    return response.data
  },

  // 根据烫金纸张类型查询
  getProductsByPaperType: async (hotStampingPaperType: string): Promise<Product[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/search/paper-type`, { 
      params: { hotStampingPaperType } 
    })
    return response.data
  },

  // 创建产品
  createProduct: async (product: Product): Promise<Product> => {
    const response = await axios.post(`${API_BASE_URL}/product-management`, product)
    return response.data
  },

  // 更新产品
  updateProduct: async (id: number, product: Product): Promise<Product> => {
    const response = await axios.put(`${API_BASE_URL}/product-management/${id}`, product)
    return response.data
  },

  // 删除产品
  deleteProduct: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/product-management/${id}`)
  },

  // 批量删除产品
  batchDeleteProducts: async (ids: number[]): Promise<{ success: boolean; message: string }> => {
    const response = await axios.post(`${API_BASE_URL}/product-management/batch/delete`, { ids })
    return response.data
  },

  // 批量更新产品
  batchUpdateProducts: async (ids: number[], updateFields: Record<string, any>): Promise<{ success: boolean; message: string }> => {
    const response = await axios.put(`${API_BASE_URL}/product-management/batch/update`, {
      ids,
      updateFields
    })
    return response.data
  },

  // 获取所有选项
  getProductOptions: async (): Promise<ProductOptions> => {
    const [
      hotStampingPaperTypes,
      danweiOptions,
      paiziOptions,
      fengduOptions,
      colorOptions,
      colorNumOptions,
      sizeOptions,
      tightnessOptions,
      temperatureRangeOptions
    ] = await Promise.all([
      axios.get(`${API_BASE_URL}/product-management/options/paper-types`),
      axios.get(`${API_BASE_URL}/product-management/options/danwei`),
      axios.get(`${API_BASE_URL}/product-management/options/paizi`),
      axios.get(`${API_BASE_URL}/product-management/options/fengdu`),
      axios.get(`${API_BASE_URL}/product-management/options/colors`),
      axios.get(`${API_BASE_URL}/product-management/options/color-num`),
      axios.get(`${API_BASE_URL}/product-management/options/sizes`),
      axios.get(`${API_BASE_URL}/product-management/options/tightness`),
      axios.get(`${API_BASE_URL}/product-management/options/temperature-ranges`)
    ])

    return {
      hotStampingPaperTypes: hotStampingPaperTypes.data,
      danweiOptions: danweiOptions.data,
      paiziOptions: paiziOptions.data,
      fengduOptions: fengduOptions.data,
      colorOptions: colorOptions.data,
      colorNumOptions: colorNumOptions.data,
      sizeOptions: sizeOptions.data,
      tightnessOptions: tightnessOptions.data,
      temperatureRangeOptions: temperatureRangeOptions.data
    }
  },

  // ========== 完整产品信息API ==========

  // 获取所有完整产品信息
  getAllCompleteProductInfo: async (): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete`)
    return response.data
  },

  // 根据产品ID获取完整产品信息
  getCompleteProductInfoById: async (id: number): Promise<CompleteProductInfo> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/${id}`)
    return response.data
  },

  // 根据物料编号获取完整产品信息
  getCompleteProductInfoByMaterialNumber: async (materialNumber: string): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/material`, { 
      params: { materialNumber } 
    })
    return response.data
  },

  // 根据型号获取完整产品信息
  getCompleteProductInfoByModelNumber: async (modelNumber: string): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/model`, { 
      params: { modelNumber } 
    })
    return response.data
  },

  // 根据Leo Touch编号获取完整产品信息
  getCompleteProductInfoByCompanyNumber: async (companyNumber: string): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/company-number`, { 
      params: { companyNumber } 
    })
    return response.data
  },

  // 根据SPNO获取完整产品信息
  getCompleteProductInfoByGpNo: async (gpNo: string): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/gp-no`, { 
      params: { gpNo } 
    })
    return response.data
  },

  // 根据烫金纸张类型获取完整产品信息
  getCompleteProductInfoByHotStampingPaperType: async (hotStampingPaperType: string): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/paper-type`, { 
      params: { hotStampingPaperType } 
    })
    return response.data
  },

  // 根据牌子和颜色编码获取完整产品信息
  getCompleteProductInfoByPaiziAndColorNum: async (paizi?: string, colorNum?: string, fuzzy: boolean = true): Promise<CompleteProductInfo[]> => {
    const params: any = {}
    if (paizi) params.paizi = paizi
    if (colorNum) params.colorNum = colorNum
    if (fuzzy) params.fuzzy = fuzzy
    
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/paizi-color`, { 
      params 
    })
    return response.data
  }
}

// 规格管理API
export const specificationApi = {
  // 根据产品ID获取规格
  getSpecificationsByProductId: async (productId: number): Promise<Specification[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/${productId}/specifications`)
    return response.data
  },

  // 创建规格
  createSpecification: async (specification: Specification): Promise<Specification> => {
    const response = await axios.post(`${API_BASE_URL}/product-management/specifications`, specification)
    return response.data
  },

  // 更新规格
  updateSpecification: async (id: number, specification: Specification): Promise<Specification> => {
    const response = await axios.put(`${API_BASE_URL}/product-management/specifications/${id}`, specification)
    return response.data
  },

  // 删除规格
  deleteSpecification: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/product-management/specifications/${id}`)
  }
}

// 价格管理API
export const pricingApi = {
  // 根据产品ID获取价格
  getPricingByProductId: async (productId: number): Promise<Pricing[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/${productId}/pricing`)
    return response.data
  },

  // 创建价格
  createPricing: async (pricing: Pricing): Promise<Pricing> => {
    const response = await axios.post(`${API_BASE_URL}/product-management/pricing`, pricing)
    return response.data
  },

  // 更新价格
  updatePricing: async (id: number, pricing: Pricing): Promise<Pricing> => {
    const response = await axios.put(`${API_BASE_URL}/product-management/pricing/${id}`, pricing)
    return response.data
  },

  // 删除价格
  deletePricing: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/product-management/pricing/${id}`)
  }
}

// 完整产品信息管理API
export const completeProductInfoApi = {
  // 获取所有完整产品信息
  getAllCompleteProductInfo: async (): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete`)
    return response.data
  },

  // 根据ID获取完整产品信息
  getCompleteProductInfoById: async (id: number): Promise<CompleteProductInfo> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/${id}`)
    return response.data
  },

  // 根据物料编号搜索完整产品信息
  searchCompleteProductInfoByMaterialNumber: async (materialNumber: string): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/material?materialNumber=${materialNumber}`)
    return response.data
  },

  // 根据型号搜索完整产品信息
  searchCompleteProductInfoByModelNumber: async (modelNumber: string): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/model?modelNumber=${modelNumber}`)
    return response.data
  },

  // 根据Leo Touch编号搜索完整产品信息
  searchCompleteProductInfoByCompanyNumber: async (companyNumber: string): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/company-number?companyNumber=${companyNumber}`)
    return response.data
  },

  // 根据SPNO搜索完整产品信息
  searchCompleteProductInfoByGpNo: async (gpNo: string): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/gp-no?gpNo=${gpNo}`)
    return response.data
  },

  // 根据烫金纸张类型搜索完整产品信息
  searchCompleteProductInfoByHotStampingPaperType: async (hotStampingPaperType: string): Promise<CompleteProductInfo[]> => {
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/paper-type?hotStampingPaperType=${hotStampingPaperType}`)
    return response.data
  },

  // 根据牌子和颜色编码搜索完整产品信息
  searchCompleteProductInfoByPaiziAndColorNum: async (paizi?: string, colorNum?: string, fuzzy: boolean = true): Promise<CompleteProductInfo[]> => {
    const params: any = {}
    if (paizi) params.paizi = paizi
    if (colorNum) params.colorNum = colorNum
    if (fuzzy) params.fuzzy = fuzzy
    
    const response = await axios.get(`${API_BASE_URL}/product-management/complete/search/paizi-color`, { params })
    return response.data
  },

  // 保存完整产品信息
  saveCompleteProductInfo: async (completeProductInfo: CompleteProductInfo): Promise<CompleteProductInfo> => {
    const response = await axios.post(`${API_BASE_URL}/product-management/complete`, completeProductInfo)
    return response.data
  },

  // 更新完整产品信息
  updateCompleteProductInfo: async (id: number, completeProductInfo: CompleteProductInfo): Promise<CompleteProductInfo> => {
    const response = await axios.put(`${API_BASE_URL}/product-management/complete/${id}`, completeProductInfo)
    return response.data
  }
}
