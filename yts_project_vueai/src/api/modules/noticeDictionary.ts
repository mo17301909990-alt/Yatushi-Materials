import request from '../request'
import type { NoticeDictionary } from '@/types/materialManagement'

// 重新导出类型
export type { NoticeDictionary }

/**
 * 注意事项字典 API
 */
export const noticeDictionaryApi = {
  /**
   * 获取所有注意事项
   */
  getAll: () => {
    return request.get<NoticeDictionary[]>('/notice-dictionary')
  },

  /**
   * 获取所有启用的注意事项
   */
  getAllActive: () => {
    return request.get<NoticeDictionary[]>('/notice-dictionary/active')
  },

  /**
   * 根据ID获取注意事项
   */
  getById: (id: number) => {
    return request.get<NoticeDictionary>(`/notice-dictionary/${id}`)
  },

  /**
   * 根据编码获取注意事项
   */
  getByCode: (code: string) => {
    return request.get<NoticeDictionary>(`/notice-dictionary/code/${code}`)
  },

  /**
   * 根据分类获取注意事项
   */
  getByCategory: (category: string) => {
    return request.get<NoticeDictionary[]>(`/notice-dictionary/category/${category}`)
  },

  /**
   * 根据ID列表获取注意事项
   */
  getByIds: (ids: number[]) => {
    return request.post<NoticeDictionary[]>('/notice-dictionary/by-ids', ids)
  },

  /**
   * 创建注意事项
   */
  create: (notice: Partial<NoticeDictionary>) => {
    return request.post<NoticeDictionary>('/notice-dictionary', notice)
  },

  /**
   * 更新注意事项
   */
  update: (id: number, notice: Partial<NoticeDictionary>) => {
    return request.put<NoticeDictionary>(`/notice-dictionary/${id}`, notice)
  },

  /**
   * 删除注意事项
   */
  delete: (id: number) => {
    return request.delete(`/notice-dictionary/${id}`)
  }
}

export default noticeDictionaryApi

