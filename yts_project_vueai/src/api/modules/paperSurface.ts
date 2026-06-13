import request from '../request'
import type { PaperSurface } from '@/types/materialManagement'

// 重新导出类型
export type { PaperSurface }

/**
 * 纸張面字典 API
 */
export const paperSurfaceApi = {
  /**
   * 获取所有纸张面字典
   */
  getAll: () => {
    return request.get<PaperSurface[]>('/reference/paper-surface')
  },

  /**
   * 根据ID获取
   */
  getById: (id: number) => {
    return request.get<PaperSurface>(`/reference/paper-surface/${id}`)
  },

  /**
   * 根据分类获取
   */
  getByCategory: (category: string) => {
    return request.get<PaperSurface[]>(`/reference/paper-surface/category/${category}`)
  },

  /**
   * 创建
   */
  create: (data: Partial<PaperSurface>) => {
    return request.post<PaperSurface>('/reference/paper-surface', data)
  },

  /**
   * 更新
   */
  update: (id: number, data: Partial<PaperSurface>) => {
    return request.put<PaperSurface>(`/reference/paper-surface/${id}`, data)
  },

  /**
   * 删除
   */
  delete: (id: number) => {
    return request.delete(`/reference/paper-surface/${id}`)
  }
}

export default paperSurfaceApi
