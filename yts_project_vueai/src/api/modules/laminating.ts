import axios from 'axios'

// 过胶材料选项接口
export interface LaminationMaterialOption {
  id?: number
  materialName: string
  materialCode: string
  displayOrder?: number
  isActive?: boolean
  description?: string
  pid?: number
  createdAt?: string
  updatedAt?: string
}

// 后处理工序选项接口
export interface PostProcessingOption {
  id?: number
  stepName: string
  stepCode?: string
  displayOrder?: number
  isActive?: boolean
  description?: string
  createdAt?: string
  updatedAt?: string
}

// 过胶兼容性接口
export interface LaminationCompatibility {
  id?: number
  foilSeries: string
  interfaceTypeId?: number
  postProcessingStepId: number
  laminationMaterialId: number
  compatibility: 'V' | 'X'
  productType?: string
  modelNumber?: string
  isJiehuo?: boolean
  createdAt?: string
  updatedAt?: string
  laminationMaterialName?: string
  laminationMaterialDescription?: string
  postProcessingStepName?: string
}

// 分页查询参数接口
export interface LaminationCompatibilityQuery {
  foilSeries?: string
  productType?: string
  modelNumber?: string
  postProcessingStepId?: number
  laminationMaterialId?: number
  compatibility?: 'V' | 'X' | ''
  page?: number
  size?: number
}

// 分页响应接口
export interface LaminationCompatibilityPageResponse {
  list: LaminationCompatibility[]
  total: number
  page: number
  size: number
  totalPages: number
}

const API_BASE_URL = '/api'

// 过胶管理API
export const laminatingApi = {
  // ========== 过胶材料选项管理 ==========
  
  // 获取所有过胶材料选项
  getAllMaterialOptions: async (): Promise<LaminationMaterialOption[]> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/options/materials`)
    return response.data
  },

  // 根据ID获取过胶材料选项
  getMaterialOptionById: async (id: number): Promise<LaminationMaterialOption> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/options/materials/${id}`)
    return response.data
  },

  // 保存过胶材料选项
  saveMaterialOption: async (option: LaminationMaterialOption): Promise<LaminationMaterialOption> => {
    const response = await axios.post(`${API_BASE_URL}/laminating/options/materials`, option)
    return response.data
  },

  // 更新过胶材料选项
  updateMaterialOption: async (id: number, option: LaminationMaterialOption): Promise<LaminationMaterialOption> => {
    const response = await axios.put(`${API_BASE_URL}/laminating/options/materials/${id}`, option)
    return response.data
  },

  // 删除过胶材料选项
  deleteMaterialOption: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/laminating/options/materials/${id}`)
  },

  // ========== 后处理工序选项管理 ==========
  
  // 获取所有后处理工序选项
  getAllProcessingOptions: async (): Promise<PostProcessingOption[]> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/options/steps`)
    return response.data
  },

  // 根据工序名称获取后处理工序选项
  getProcessingOptionByStepName: async (stepName: string): Promise<PostProcessingOption> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/options/steps/by-name/${stepName}`)
    return response.data
  },

  // 保存后处理工序选项
  saveProcessingOption: async (option: PostProcessingOption): Promise<PostProcessingOption> => {
    const response = await axios.post(`${API_BASE_URL}/laminating/options/steps`, option)
    return response.data
  },

  // 更新后处理工序选项
  updateProcessingOption: async (id: number, option: PostProcessingOption): Promise<PostProcessingOption> => {
    const response = await axios.put(`${API_BASE_URL}/laminating/options/steps/${id}`, option)
    return response.data
  },

  // 删除后处理工序选项
  deleteProcessingOption: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/laminating/options/steps/${id}`)
  },

  // ========== 过胶兼容性管理 ==========
  
  // 获取过胶兼容性列表（带分页）
  getCompatibilityList: async (query: LaminationCompatibilityQuery): Promise<LaminationCompatibilityPageResponse> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/compatibility`, { params: query })
    return response.data
  },

  // 根据ID获取过胶兼容性
  getCompatibilityById: async (id: number): Promise<LaminationCompatibility> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/compatibility/${id}`)
    return response.data
  },

  // 保存过胶兼容性
  saveCompatibility: async (compatibility: LaminationCompatibility): Promise<LaminationCompatibility> => {
    const response = await axios.post(`${API_BASE_URL}/laminating/compatibility`, compatibility)
    return response.data
  },

  // 更新过胶兼容性
  updateCompatibility: async (id: number, compatibility: LaminationCompatibility): Promise<LaminationCompatibility> => {
    const response = await axios.put(`${API_BASE_URL}/laminating/compatibility/${id}`, compatibility)
    return response.data
  },

  // 批量保存过胶兼容性
  batchSaveCompatibility: async (compatibilities: LaminationCompatibility[]): Promise<void> => {
    await axios.post(`${API_BASE_URL}/laminating/compatibility/batch`, compatibilities)
  },

  // 删除过胶兼容性
  deleteCompatibility: async (id: number): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/laminating/compatibility/${id}`)
  },

  // 批量删除过胶兼容性
  batchDeleteCompatibility: async (ids: number[]): Promise<void> => {
    await axios.delete(`${API_BASE_URL}/laminating/compatibility/batch`, { data: ids })
  },

  // ========== 选项数据获取 ==========
  
  // 获取所有产品系列名称
  getAllFoilSeries: async (): Promise<string[]> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/options/foil-series`)
    return response.data
  },

  // 获取所有产品类型
  getAllProductTypes: async (): Promise<string[]> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/options/product-types`)
    return response.data
  },

  // 获取所有型号
  getAllModelNumbers: async (): Promise<string[]> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/options/model-numbers`)
    return response.data
  },

  // 获取"过胶"工序ID
  getLaminatingStepId: async (): Promise<number | null> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/options/laminating-step-id`)
    return response.data.stepId
  },

  // ========== 批量操作和导入导出 ==========
  
  // 批量更新兼容性状态
  batchUpdateStatus: async (ids: number[], compatibility: 'V' | 'X'): Promise<{ success: boolean; message: string }> => {
    const response = await axios.put(`${API_BASE_URL}/laminating/compatibility/batch/status`, {
      ids,
      compatibility
    })
    return response.data
  },

  // 检查唯一性
  checkUnique: async (
    foilSeries: string,
    modelNumber: string | undefined,
    productType: string,
    laminationMaterialId: number,
    postProcessingStepId: number | undefined
  ): Promise<LaminationCompatibility | null> => {
    try {
      const params: any = {
        foilSeries,
        productType,
        laminationMaterialId
      }
      if (modelNumber) params.modelNumber = modelNumber
      if (postProcessingStepId) params.postProcessingStepId = postProcessingStepId
      
      const response = await axios.get(`${API_BASE_URL}/laminating/compatibility/check-unique`, { params })
      return response.data
    } catch (error: any) {
      if (error.response?.status === 404) {
        return null
      }
      throw error
    }
  },

  // 导出兼容性配置
  exportCompatibility: async (query?: LaminationCompatibilityQuery): Promise<Blob> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/compatibility/export`, {
      params: query,
      responseType: 'blob'
    })
    return response.data
  },

  // 导入兼容性配置
  importCompatibility: async (file: File): Promise<{
    success: boolean
    totalCount: number
    successCount: number
    failCount: number
    message: string
    errorMessages?: string[]
  }> => {
    const formData = new FormData()
    formData.append('file', file)
    const response = await axios.post(`${API_BASE_URL}/laminating/compatibility/import`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
    return response.data
  },

  // 下载导入模板
  downloadImportTemplate: async (): Promise<Blob> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/compatibility/import-template`, {
      responseType: 'blob'
    })
    return response.data
  },

  // 导出过胶材料选项
  exportMaterialOptions: async (): Promise<Blob> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/options/materials/export`, {
      responseType: 'blob'
    })
    return response.data
  },

  // 导出后处理工序选项
  exportProcessingOptions: async (): Promise<Blob> => {
    const response = await axios.get(`${API_BASE_URL}/laminating/options/steps/export`, {
      responseType: 'blob'
    })
    return response.data
  }
}
