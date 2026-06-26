import request from '../request'

/**
 * 耐磨金纸映射接口类型定义
 */
export interface WearResistantGoldPaperMapping {
  id?: number
  productName: string
  productModelNumber?: string
  hotStampingType: string
  goldPaperType: string
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  remarks?: string
  createdAt?: string
  updatedAt?: string
}

export interface CreateWearResistantGoldPaperMapping {
  productName: string
  productModelNumber?: string
  goldPaperType: string
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  remarks?: string
}

export interface UpdateWearResistantGoldPaperMapping {
  id: number
  productName: string
  productModelNumber?: string
  goldPaperType: string
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
  remarks?: string
}

export interface SearchParams {
  productName?: string
  productModelNumber?: string
  goldPaperType?: string
}

export interface PaginationResponse<T> {
  content: T[]
  totalElements: number
  number: number
  size: number
  totalPages: number
  first: boolean
  last: boolean
}

export interface CheckUniqueResponse {
  exists: boolean
  data?: WearResistantGoldPaperMapping
}

export interface BatchUpdateStatusRequest {
  ids: number[]
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷'
}

export interface WearResistantGoldPaperSkipConfig {
  paperTypes: string[]
}

export const wearResistantGoldPaperMappingApi = {
  /**
   * 获取所有映射
   */
  getAllMappings: (): Promise<WearResistantGoldPaperMapping[]> => {
    return request.get('/wear-resistant-gold-paper-mapping')
  },

  /**
   * 分页获取映射
   */
  getMappingsWithPagination: (page: number, size: number): Promise<PaginationResponse<WearResistantGoldPaperMapping>> => {
    return request.get('/wear-resistant-gold-paper-mapping/pagination', {
      params: { page, size }
    })
  },

  /**
   * 根据ID获取映射
   */
  getMappingById: (id: number): Promise<WearResistantGoldPaperMapping> => {
    return request.get(`/wear-resistant-gold-paper-mapping/${id}`)
  },

  /**
   * 创建映射
   */
  createMapping: (data: CreateWearResistantGoldPaperMapping): Promise<WearResistantGoldPaperMapping> => {
    return request.post('/wear-resistant-gold-paper-mapping', data)
  },

  /**
   * 更新映射
   */
  updateMapping: (id: number, data: UpdateWearResistantGoldPaperMapping): Promise<WearResistantGoldPaperMapping> => {
    return request.put(`/wear-resistant-gold-paper-mapping/${id}`, data)
  },

  /**
   * 删除映射
   */
  deleteMapping: (id: number): Promise<void> => {
    return request.delete(`/wear-resistant-gold-paper-mapping/${id}`)
  },

  /**
   * 批量删除映射
   */
  batchDeleteMappings: (ids: number[]): Promise<{ success: boolean; message: string }> => {
    return request.delete('/wear-resistant-gold-paper-mapping/batch', { data: ids })
  },

  /**
   * 搜索映射
   */
  searchMappings: (params: SearchParams): Promise<WearResistantGoldPaperMapping[]> => {
    return request.get('/wear-resistant-gold-paper-mapping/search', { params })
  },

  /**
   * 检查唯一性
   */
  checkUnique: (
    productName: string,
    productModelNumber?: string,
    goldPaperType: string
  ): Promise<CheckUniqueResponse> => {
    return request.get('/wear-resistant-gold-paper-mapping/check-unique', {
      params: { productName, productModelNumber, goldPaperType }
    })
  },

  /**
   * 批量更新兼容性状态
   */
  batchUpdateStatus: (data: BatchUpdateStatusRequest): Promise<{ success: boolean; message: string }> => {
    return request.put('/wear-resistant-gold-paper-mapping/batch/status', data)
  },

  /**
   * 获取所有可用的耐磨金纸类型
   */
  getAllGoldPaperTypes: (): Promise<string[]> => {
    return request.get('/wear-resistant-gold-paper-mapping/options/gold-paper-types')
  },

  /**
   * 获取在 Match 中跳过耐磨映射的烫金纸类型列表
   */
  getSkipPaperTypes: (): Promise<string[]> => {
    return request.get('/wear-resistant-gold-paper-skip-config')
  },

  /**
   * 保存在 Match 中跳过耐磨映射的烫金纸类型列表（全量覆盖）
   */
  saveSkipPaperTypes: (paperTypes: string[]): Promise<void> => {
    return request.put('/wear-resistant-gold-paper-skip-config', paperTypes)
  },

  /**
   * 导出映射到Excel
   */
  exportMappings: (): Promise<Blob> => {
    return request.get('/wear-resistant-gold-paper-mapping/export', {
      responseType: 'blob'
    })
  },

  /**
   * 下载导入模板
   */
  downloadImportTemplate: (): Promise<Blob> => {
    return request.get('/wear-resistant-gold-paper-mapping/import-template', {
      responseType: 'blob'
    })
  },

  /**
   * 导入映射
   */
  importMappings: (file: File): Promise<{
    success: boolean
    message: string
    totalCount?: number
    successCount?: number
    failCount?: number
    errors?: string[]
  }> => {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/wear-resistant-gold-paper-mapping/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}

