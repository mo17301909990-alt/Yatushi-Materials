import axios from 'axios'

// 烫金图案区域选项接口
export interface HotStampingPatternAreaOption {
  id: number
  optionName: string
  areaCategory: string
  areaRange: string
  patternType: string
  minSizeMm: number | null
  maxSizeMm: number | null
  description: string | null
  isActive: boolean
  sortOrder: number
  createdAt: string
  updatedAt: string
}

// 创建/更新烫金图案区域选项接口
export interface CreateHotStampingPatternAreaOption {
  optionName: string
  areaCategory: string
  areaRange: string
  patternType: string
  minSizeMm?: number | null
  maxSizeMm?: number | null
  description?: string | null
  isActive?: boolean
  sortOrder?: number
}

// 查询参数接口
export interface HotStampingPatternAreaQueryParams {
  page?: number
  size?: number
  optionName?: string
  areaCategory?: string
  areaRange?: string
  patternType?: string
  isActive?: boolean
  sortBy?: string
  sortOrder?: 'asc' | 'desc'
}

// 分页响应接口
export interface PaginatedResponse<T> {
  items: T[]
  total: number
  page: number
  size: number
  totalPages: number
}

// 烫金图案区域选项API
export const hotStampingPatternAreaOptionsApi = {
  /**
   * 获取所有激活的烫金图案区域选项
   */
  getActiveOptions: async (): Promise<HotStampingPatternAreaOption[]> => {
    const response = await axios.get('/api/api/hot-stamping-pattern-area-options/active')
    return response.data
  },

  /**
   * 获取所有烫金图案区域选项
   */
  getAllOptions: async (): Promise<HotStampingPatternAreaOption[]> => {
    const response = await axios.get('/api/api/hot-stamping-pattern-area-options')
    return response.data
  },

  /**
   * 分页获取烫金图案区域选项
   */
  getOptionsPaginated: async (params: HotStampingPatternAreaQueryParams = {}): Promise<PaginatedResponse<HotStampingPatternAreaOption>> => {
    const response = await axios.get('/api/api/hot-stamping-pattern-area-options/paginated', { params })
    return response.data
  },

  /**
   * 根据ID获取烫金图案区域选项
   */
  getOptionById: async (id: number): Promise<HotStampingPatternAreaOption> => {
    const response = await axios.get(`/api/api/hot-stamping-pattern-area-options/${id}`)
    return response.data
  },

  /**
   * 创建烫金图案区域选项
   */
  createOption: async (option: CreateHotStampingPatternAreaOption): Promise<HotStampingPatternAreaOption> => {
    const response = await axios.post('/api/api/hot-stamping-pattern-area-options', option)
    return response.data
  },

  /**
   * 更新烫金图案区域选项
   */
  updateOption: async (id: number, option: Partial<CreateHotStampingPatternAreaOption>): Promise<HotStampingPatternAreaOption> => {
    const response = await axios.put(`/api/api/hot-stamping-pattern-area-options/${id}`, option)
    return response.data
  },

  /**
   * 删除烫金图案区域选项
   */
  deleteOption: async (id: number): Promise<void> => {
    await axios.delete(`/api/api/hot-stamping-pattern-area-options/${id}`)
  },

  /**
   * 批量删除烫金图案区域选项
   */
  batchDeleteOptions: async (ids: number[]): Promise<void> => {
    await axios.delete('/api/api/hot-stamping-pattern-area-options/batch', { data: { ids } })
  }
}