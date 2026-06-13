import request from '../request'
import type { InkSurface } from '@/types/materialManagement'

// 重新导出类型
export type { InkSurface }

/**
 * 印刷油墨面字典 API
 */
export const inkSurfaceApi = {
  /**
   * 获取所有油墨面字典
   */
  getAll: () => {
    return request.get<InkSurface[]>('/reference/ink-surface')
  },

  /**
   * 根据ID获取
   */
  getById: (id: number) => {
    return request.get<InkSurface>(`/reference/ink-surface/${id}`)
  },

  /**
   * 根据分类获取
   */
  getByCategory: (category: string) => {
    return request.get<InkSurface[]>(`/reference/ink-surface/category/${category}`)
  },

  /**
   * 创建
   */
  create: (data: Partial<InkSurface>) => {
    return request.post<InkSurface>('/reference/ink-surface', data)
  },

  /**
   * 更新
   */
  update: (id: number, data: Partial<InkSurface>) => {
    return request.put<InkSurface>(`/reference/ink-surface/${id}`, data)
  },

  /**
   * 删除
   */
  delete: (id: number) => {
    return request.delete(`/reference/ink-surface/${id}`)
  }
}

export default inkSurfaceApi
