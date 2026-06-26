import request from '../request';
import type { 
  Permission, 
  CreatePermissionRequest, 
  UpdatePermissionRequest, 
  AssignPermissionsRequest,
  ApiResponse 
} from '../types/permission';

export const permissionApi = {
  // 获取所有权限
  getAllPermissions(): Promise<Permission[]> {
    return request.get('/permissions');
  },

  // 获取菜单权限
  getMenuPermissions(): Promise<Permission[]> {
    return request.get('/permissions/menu');
  },

  // 根据ID获取权限
  getPermissionById(id: number): Promise<Permission> {
    return request.get(`/permissions/${id}`);
  },

  // 创建权限
  createPermission(data: CreatePermissionRequest): Promise<Permission> {
    return request.post('/permissions', data);
  },

  // 更新权限
  updatePermission(id: number, data: UpdatePermissionRequest): Promise<Permission> {
    return request.put(`/permissions/${id}`, data);
  },

  // 删除权限
  deletePermission(id: number): Promise<ApiResponse<any>> {
    return request.delete(`/permissions/${id}`);
  },

  // 根据角色ID获取权限列表
  getPermissionsByRoleId(roleId: number): Promise<Permission[]> {
    return request.get(`/permissions/role/${roleId}`);
  },

  // 根据用户ID获取权限列表
  getPermissionsByUserId(userId: number): Promise<Permission[]> {
    return request.get(`/permissions/user/${userId}`);
  },

  // 为角色分配权限
  assignPermissionsToRole(data: AssignPermissionsRequest): Promise<ApiResponse<any>> {
    return request.post('/permissions/assign', data);
  }
};
