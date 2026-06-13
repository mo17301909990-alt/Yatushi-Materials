import request from '../request'

export interface ImportSnapshot {
  id: number
  snapshotName: string
  tableName: string
  snapshotTableName: string
  operationType: string
  operatedAt: string
  operatorId?: number
  operatorName?: string
  recordCount: number
  remark?: string
}

export const importSnapshotApi = {
  list: async (params?: {
    tableName?: string
    from?: string
    to?: string
    limit?: number
  }): Promise<ImportSnapshot[]> => {
    const response = await request({
      url: '/import-snapshot/list',
      method: 'GET',
      params
    })
    // 后端直接返回 List，request 拦截器已返回 response.data，即数组本身
    if (Array.isArray(response)) return response
    return (response as { data?: ImportSnapshot[] })?.data ?? []
  },

  getById: async (id: number): Promise<ImportSnapshot | null> => {
    const response = await request({
      url: `/import-snapshot/${id}`,
      method: 'GET'
    })
    return (response as ImportSnapshot) ?? null
  },

  restore: async (id: number): Promise<{ success: boolean; message: string }> => {
    const response = await request({
      url: `/import-snapshot/${id}/restore`,
      method: 'POST'
    })
    const data = response as { success?: boolean; message?: string }
    return { success: data?.success ?? false, message: data?.message ?? 'Unknown error' }
  }
}
