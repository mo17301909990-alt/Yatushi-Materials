import request from '../request'

export interface ProductTypeSortConfig {
  id?: number
  productTypeId: number
  enableWearResistantPriority: boolean
  wearResistantPaperTypes: string
  description?: string
  isActive: boolean
  createdAt?: string
  updatedAt?: string
}

export const productTypeSortConfigApi = {
  /**
   * 获取全部配置
   */
  getAll: (): Promise<ProductTypeSortConfig[]> => {
    return request.get('/product-type-sort-config')
  },

  /**
   * 获取全部激活配置
   */
  getAllActive: (): Promise<ProductTypeSortConfig[]> => {
    return request.get('/product-type-sort-config/active')
  },

  /**
   * 根据 ID 获取
   */
  getById: (id: number): Promise<ProductTypeSortConfig> => {
    return request.get(`/product-type-sort-config/${id}`)
  },

  /**
   * 根据产品类型 ID 获取
   */
  getByProductTypeId: (productTypeId: number): Promise<ProductTypeSortConfig | null> => {
    return request.get(`/product-type-sort-config/by-product-type/${productTypeId}`)
  },

  /**
   * 新增配置
   */
  create: (data: Omit<ProductTypeSortConfig, 'id' | 'createdAt' | 'updatedAt'>): Promise<ProductTypeSortConfig> => {
    return request.post('/product-type-sort-config', data)
  },

  /**
   * 更新配置
   */
  update: (id: number, data: Partial<ProductTypeSortConfig>): Promise<ProductTypeSortConfig> => {
    return request.put(`/product-type-sort-config/${id}`, data)
  },

  /**
   * 删除配置
   */
  delete: (id: number): Promise<void> => {
    return request.delete(`/product-type-sort-config/${id}`)
  }
}

