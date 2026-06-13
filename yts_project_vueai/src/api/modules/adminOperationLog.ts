import request from '../request'

export interface AdminOperationLogItem {
  id: number
  operatorId?: number
  operatorName?: string
  operatedAt: string
  operationType: string
  module: string
  targetType?: string
  targetId?: string
  targetLabel?: string
  changeSummary?: string
  changeDetail?: string
  ip?: string
  userAgent?: string
}

export interface OperationLogListParams {
  operatorId?: number
  module?: string
  operationType?: string
  from?: string
  to?: string
  pageNo?: number
  pageSize?: number
}

export interface OperationLogListResult {
  list: AdminOperationLogItem[]
  total: number
  pageNo: number
  pageSize: number
  totalPages: number
}

export const adminOperationLogApi = {
  list: async (params?: OperationLogListParams): Promise<OperationLogListResult> => {
    const response = await request({
      url: '/admin-operation-log/list',
      method: 'GET',
      params
    })
    const data = response as OperationLogListResult
    return {
      list: Array.isArray(data?.list) ? data.list : [],
      total: data?.total ?? 0,
      pageNo: data?.pageNo ?? 1,
      pageSize: data?.pageSize ?? 20,
      totalPages: data?.totalPages ?? 0
    }
  }
}
