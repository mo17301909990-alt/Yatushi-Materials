import axios from 'axios';

// 烫金类型选项接口
export interface HotStampingTypeOption {
  id: number;
  stampingType: string;
  positionType?: string;
  description?: string;
  isActive: boolean;
  sortOrder: number;
  createdAt: string;
  updatedAt: string;
}

// 创建/更新烫金类型选项接口
export interface CreateHotStampingTypeOption {
  stampingType: string;
  positionType?: string;
  description?: string;
  isActive: boolean;
  sortOrder: number;
}

// 查询参数接口
export interface HotStampingTypeOptionQueryParams {
  page?: number;
  size?: number;
  stampingType?: string;
  positionType?: string;
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

// 烫金类型选项API
export const hotStampingTypeOptionsApi = {
  /**
   * 获取所有激活的烫金类型选项
   */
  getActiveTypes: async (): Promise<HotStampingTypeOption[]> => {
    const response = await axios.get('/api/api/hot-stamping-type-options/active');
    return response.data;
  },

  /**
   * 获取所有烫金类型选项
   */
  getAllTypes: async (): Promise<HotStampingTypeOption[]> => {
    const response = await axios.get('/api/api/hot-stamping-type-options');
    return response.data;
  },

  /**
   * 分页获取烫金类型选项
   */
  getTypesPaginated: async (params: HotStampingTypeOptionQueryParams = {}): Promise<PaginatedResponse<HotStampingTypeOption>> => {
    const response = await axios.get('/api/hot-stamping-type-options/paginated', { params });
    return response.data;
  },

  /**
   * 根据ID获取烫金类型选项
   */
  getTypeById: async (id: number): Promise<HotStampingTypeOption> => {
    const response = await axios.get(`/api/api/hot-stamping-type-options/${id}`);
    return response.data;
  },

  /**
   * 创建烫金类型选项
   */
  createType: async (type: CreateHotStampingTypeOption): Promise<HotStampingTypeOption> => {
    const response = await axios.post('/api/api/hot-stamping-type-options', type);
    return response.data;
  },

  /**
   * 更新烫金类型选项
   */
  updateType: async (id: number, type: Partial<CreateHotStampingTypeOption>): Promise<HotStampingTypeOption> => {
    const response = await axios.put(`/api/api/hot-stamping-type-options/${id}`, type);
    return response.data;
  },

  /**
   * 删除烫金类型选项
   */
  deleteType: async (id: number): Promise<void> => {
    await axios.delete(`/api/api/hot-stamping-type-options/${id}`);
  },

  /**
   * 批量删除烫金类型选项
   */
  batchDeleteTypes: async (ids: number[]): Promise<void> => {
    await axios.delete('/api/api/hot-stamping-type-options/batch', { data: { ids } });
  }
};

