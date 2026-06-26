import request from '../request';

export interface AdminChangePreview {
  entityType: string
  entityId: number
  currentValues: Record<string, unknown>
  proposedChanges: Record<string, unknown>
  risk: string
}

export interface AdminChangeResult {
  recordId: number
  entityType: string
  entityId: number
  status: string
  message: string
}

export interface AdminChangeRecordInfo {
  id: number
  entityType: string
  entityId: number
  changeSet: string
  snapshotIds: number[]
  operatorId: number
  operatorName: string
  risk: string
  reason: string
  status: string
  createdAt: string
  committedAt: string | null
  rolledBackAt: string | null
  rollbackRecordId: number | null
  errorMessage: string | null
}

export interface AdminChangeQueryResult {
  list: AdminChangeRecordInfo[]
  total: number
  pageNo: number
  pageSize: number
}

export const adminChangeApi = {
  /** 预览变更 */
  preview(params: { entityType: string; entityId: number; changes: Record<string, unknown> }) {
    return request({
      url: '/agent/admin/preview',
      method: 'post',
      data: params,
    }) as Promise<AdminChangePreview>
  },

  /** 执行变更 */
  execute(params: {
    entityType: string
    entityId: number
    changes: Record<string, unknown>
    risk?: string
    reason?: string
    confirmed?: boolean
  }) {
    return request({
      url: '/agent/admin/execute',
      method: 'post',
      data: params,
    }) as Promise<AdminChangeResult>
  },

  /** 回滚变更 */
  rollback(recordId: number) {
    return request({
      url: `/agent/admin/rollback/${recordId}`,
      method: 'post',
    }) as Promise<AdminChangeResult>
  },

  /** 查询变更历史 */
  queryChanges(params: { entityType?: string; status?: string; pageNo?: number; pageSize?: number }) {
    return request({
      url: '/agent/admin/changes',
      method: 'get',
      params,
    }) as Promise<AdminChangeQueryResult>
  },

  /** 查询单条变更详情 */
  getChangeDetail(recordId: number) {
    return request({
      url: `/agent/admin/changes/${recordId}`,
      method: 'get',
    }) as Promise<{ record: AdminChangeRecordInfo; snapshots: unknown[] }>
  },
}
