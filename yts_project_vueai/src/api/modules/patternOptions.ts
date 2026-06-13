import axios from 'axios';

// 烫金图案基础接口
export interface HotStampingPatternBase {
  id: number;
  optionName: string;
  patternType: string;
  lineThicknessMin?: number;
  lineThicknessMax?: number;
  solidAreaMin?: number;
  solidAreaMax?: number;
  isActive: boolean;
  sortOrder: number;
  createdAt: string;
  updatedAt: string;
}

// 创建/更新图案基础信息接口
export interface CreateHotStampingPatternBase {
  optionName: string;
  patternType: string;
  lineThicknessMin?: number;
  lineThicknessMax?: number;
  solidAreaMin?: number;
  solidAreaMax?: number;
  isActive: boolean;
  sortOrder: number;
}

// 查询参数接口
export interface HotStampingPatternBaseQueryParams {
  page?: number;
  size?: number;
  optionName?: string;
  patternType?: string;
  isActive?: boolean;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

// 分页响应接口
export interface PaginatedResponse<T> {
  items: T[];
  total: number;
  page: number;
  size: number;
  totalPages: number;
}

// 图案选项API
export const patternOptionsApi = {
  /**
   * 根据产品类型ID获取图案选项
   */
  getPatternOptionsByProductTypeId: async (productTypeId: number): Promise<HotStampingPatternBase[]> => {
    const response = await axios.get(`/api/api/compatibility/pattern-options/${productTypeId}`);
    return response.data;
  },

  /**
   * 获取所有激活的烫金图案基础信息
   */
  getActivePatterns: async (): Promise<HotStampingPatternBase[]> => {
    const response = await axios.get('/api/api/hot-stamping-pattern-base/active');
    return response.data;
  },

  /**
   * 获取所有烫金图案基础信息
   */
  getAllPatterns: async (): Promise<HotStampingPatternBase[]> => {
    const response = await axios.get('/api/api/hot-stamping-pattern-base');
    return response.data;
  },

  /**
   * 分页获取烫金图案基础信息
   */
  getPatternsPaginated: async (params: HotStampingPatternBaseQueryParams = {}): Promise<PaginatedResponse<HotStampingPatternBase>> => {
    const response = await axios.get('/api/api/hot-stamping-pattern-base/paginated', { params });
    return response.data;
  },

  /**
   * 根据ID获取烫金图案基础信息
   */
  getPatternById: async (id: number): Promise<HotStampingPatternBase> => {
    const response = await axios.get(`/api/api/hot-stamping-pattern-base/${id}`);
    return response.data;
  },

  /**
   * 创建烫金图案基础信息
   */
  createPattern: async (pattern: CreateHotStampingPatternBase): Promise<HotStampingPatternBase> => {
    const response = await axios.post('/api/api/hot-stamping-pattern-base', pattern);
    return response.data;
  },

  /**
   * 更新烫金图案基础信息
   */
  updatePattern: async (id: number, pattern: Partial<CreateHotStampingPatternBase>): Promise<HotStampingPatternBase> => {
    const response = await axios.put(`/api/api/hot-stamping-pattern-base/${id}`, pattern);
    return response.data;
  },

  /**
   * 删除烫金图案基础信息
   */
  deletePattern: async (id: number): Promise<void> => {
    await axios.delete(`/api/api/hot-stamping-pattern-base/${id}`);
  },

  /**
   * 批量删除烫金图案基础信息
   */
  batchDeletePatterns: async (ids: number[]): Promise<void> => {
    await axios.delete('/api/api/hot-stamping-pattern-base/batch', { data: { ids } });
  }
};
