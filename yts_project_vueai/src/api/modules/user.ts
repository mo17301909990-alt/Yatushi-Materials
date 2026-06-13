import request from '../request';
import type { UserInfo } from '../types/auth';
import type { ApiResponse } from '../types/auth';

export const userApi = {
  // 获取所有用户
  getAllUsers(): Promise<{ data: UserInfo[]; message: string }> {
    return request.get('/users/list');
  },

  // 根据ID获取用户
  getUserById(id: number): Promise<{ data: UserInfo; message: string }> {
    return request.get(`/users/${id}`);
  },

  // 更新用户
  updateUser(id: number, userData: Partial<UserInfo>): Promise<{ data: UserInfo; message: string }> {
    return request.put(`/users/${id}`, userData);
  },

  // 删除用户
  deleteUser(id: number): Promise<{ message: string }> {
    return request.delete(`/users/${id}`);
  },

  // 获取用户角色
  getUserRoles(userId: number): Promise<any[]> {
    return request.get(`/users/${userId}/roles`);
  },

  // 获取管理员用户列表
  getAdminUsers(): Promise<{ data: UserInfo[]; message: string }> {
    return request.get('/users/admins');
  }
};
