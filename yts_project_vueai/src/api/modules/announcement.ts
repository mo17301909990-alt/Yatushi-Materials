import request from '../request';

// 消息类型定义
export interface SysAnnouncement {
  id?: string;
  title: string;
  msgContent: string;
  sender?: string;
  priority?: string;
  msgCategory?: string;  // 1:通知公告, 2:系统消息
  msgType?: string;  // ALL:全体用户, USER:指定用户
  sendStatus?: number;  // 0:未发布, 1:已发布, 2:撤回
  sendTime?: string;
  startTime?: string;
  endTime?: string;
  busType?: string;
  busId?: string;
  userIds?: string;  // 指定用户ID列表，逗号分隔
  createBy?: string;
  createTime?: string;
  updateBy?: string;
  updateTime?: string;
  readFlag?: number;  // 0:未读, 1:已读（查询用户消息时返回）
  readTime?: string;  // 阅读时间（查询用户消息时返回）
}

export interface AnnouncementListParams {
  title?: string;
  msgCategory?: string;
  sendStatus?: number;
  pageNo?: number;
  pageSize?: number;
}

export interface UserAnnouncementListParams {
  userId: number;
  msgCategory?: string;
  readFlag?: number;  // 0:未读, 1:已读
  startTime?: string;  // 开始时间（筛选创建时间）
  endTime?: string;    // 结束时间（筛选创建时间）
  pageNo?: number;
  pageSize?: number;
}

export interface ApiResponse<T = any> {
  success: boolean;
  message?: string;
  result?: T;
}

export interface PageResult<T = any> {
  records: T[];
  total: number;
  size: number;
  current: number;
}

export const announcementApi = {
  // 创建消息（草稿）
  add(announcement: SysAnnouncement): Promise<ApiResponse<SysAnnouncement>> {
    return request.post('/sys/announcement/add', announcement);
  },

  // 更新消息（仅草稿）
  edit(announcement: SysAnnouncement): Promise<ApiResponse> {
    return request.put('/sys/announcement/edit', announcement);
  },

  // 删除消息
  delete(id: string): Promise<ApiResponse> {
    return request.delete(`/sys/announcement/delete?id=${id}`);
  },

  // 根据ID查询消息
  queryById(id: string): Promise<ApiResponse<SysAnnouncement>> {
    return request.get(`/sys/announcement/queryById?id=${id}`);
  },

  // 查询消息列表（管理员）
  list(params: AnnouncementListParams): Promise<ApiResponse<PageResult<SysAnnouncement>>> {
    const queryParams = new URLSearchParams();
    if (params.title) queryParams.append('title', params.title);
    if (params.msgCategory) queryParams.append('msgCategory', params.msgCategory);
    if (params.sendStatus !== undefined) queryParams.append('sendStatus', params.sendStatus.toString());
    queryParams.append('pageNo', (params.pageNo || 1).toString());
    queryParams.append('pageSize', (params.pageSize || 10).toString());
    return request.get(`/sys/announcement/list?${queryParams.toString()}`);
  },

  // 发布消息
  doRelease(id: string): Promise<ApiResponse<{ deliveryCount: number; pushCount: number }>> {
    return request.post('/sys/announcement/doRelease', { id });
  },

  // 撤回消息
  doRevoke(id: string): Promise<ApiResponse> {
    return request.post('/sys/announcement/doRevoke', { id });
  },

  // 查询用户消息列表
  listByUser(params: UserAnnouncementListParams): Promise<ApiResponse<PageResult<SysAnnouncement>>> {
    const queryParams = new URLSearchParams();
    queryParams.append('userId', params.userId.toString());
    if (params.msgCategory) queryParams.append('msgCategory', params.msgCategory);
    if (params.readFlag !== undefined) queryParams.append('readFlag', params.readFlag.toString());
    if (params.startTime) queryParams.append('startTime', params.startTime);
    if (params.endTime) queryParams.append('endTime', params.endTime);
    queryParams.append('pageNo', (params.pageNo || 1).toString());
    queryParams.append('pageSize', (params.pageSize || 10).toString());
    return request.get(`/sys/announcement/listByUser?${queryParams.toString()}`);
  },

  // 获取用户未读消息数量
  getUnreadCount(userId: number): Promise<ApiResponse<{ unreadCount: number }>> {
    return request.get(`/sys/announcement/unread/count?userId=${userId}`);
  },

  // 标记消息为已读
  read(id: string, userId: number): Promise<ApiResponse> {
    return request.put(`/sys/announcement/read/${id}?userId=${userId}`);
  },

  // 批量标记为已读
  batchRead(ids: string[], userId: number): Promise<ApiResponse> {
    return request.put('/sys/announcement/batchRead', { ids, userId });
  },

  // 用户删除消息
  deleteByUser(id: string, userId: number): Promise<ApiResponse> {
    return request.delete(`/sys/announcement/deleteByUser?id=${id}&userId=${userId}`);
  }
};

