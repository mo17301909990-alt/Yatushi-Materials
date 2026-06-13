import request from '../request'

export interface ReferenceWritingFunction {
  id: number
  category: string
  detailName: string
  displayOrder: number
  createdAt?: string
  updatedAt?: string
}

/**
 * 表面写字功能字典 API
 */
export const referenceWritingFunctionApi = {
  /**
   * 获取所有写字功能字典
   */
  getAll: () => {
    return request.get<ReferenceWritingFunction[]>('/reference/writing-function')
  },

  /**
   * 根据ID查询
   */
  getById: (id: number) => {
    return request.get<ReferenceWritingFunction>(`/reference/writing-function/${id}`)
  },

  /**
   * 根据分类查询
   */
  getByCategory: (category: string) => {
    return request.get<ReferenceWritingFunction[]>(`/reference/writing-function/category/${category}`)
  },

  /**
   * 创建
   */
  create: (data: Partial<ReferenceWritingFunction>) => {
    return request.post<ReferenceWritingFunction>('/reference/writing-function', data)
  },

  /**
   * 更新
   */
  update: (id: number, data: Partial<ReferenceWritingFunction>) => {
    return request.put<ReferenceWritingFunction>(`/reference/writing-function/${id}`, data)
  },

  /**
   * 删除
   */
  delete: (id: number) => {
    return request.delete(`/reference/writing-function/${id}`)
  }
}

export default referenceWritingFunctionApi
