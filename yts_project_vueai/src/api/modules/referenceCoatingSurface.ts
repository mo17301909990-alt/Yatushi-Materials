import request from '../request'
import type { CoatingSurface } from '@/types/materialManagement'

// 重新导出类型
export type { CoatingSurface }

/**
 * 後加工塗層面字典 API
 */
export const coatingSurfaceApi = {
  /**
   * 获取所有涂层面字典
   */
  getAll: () => {
    return request.get<CoatingSurface[]>('/reference/coating-surface')
  },

  /**
   * 根据ID获取
   */
  getById: (id: number) => {
    return request.get<CoatingSurface>(`/reference/coating-surface/${id}`)
  },

  /**
   * 根据分类获取
   */
  getByCategory: (category: string) => {
    return request.get<CoatingSurface[]>(`/reference/coating-surface/category/${category}`)
  },

  /**
   * 关键字搜索
   */
  search: (keyword: string) => {
    return request.get<CoatingSurface[]>('/reference/coating-surface/search', { params: { keyword } })
  },

  /**
   * 创建
   */
  create: (data: Partial<CoatingSurface>) => {
    return request.post<CoatingSurface>('/reference/coating-surface', data)
  },

  /**
   * 更新
   */
  update: (id: number, data: Partial<CoatingSurface>) => {
    return request.put<CoatingSurface>(`/reference/coating-surface/${id}`, data)
  },

  /**
   * 删除
   */
  delete: (id: number) => {
    return request.delete(`/reference/coating-surface/${id}`)
  }
}

export default coatingSurfaceApi
