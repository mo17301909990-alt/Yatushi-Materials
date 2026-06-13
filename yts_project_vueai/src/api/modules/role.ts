import request from '../request';
import type { 
  Role, 
  CreateRoleRequest, 
  UpdateRoleRequest, 
  AssignRolesRequest,
  ApiResponse 
} from '../types/permission';

export const roleApi = {
  // 获取所有角色
  getAllRoles(): Promise<Role[]> {
    return request.get('/api/roles');
  },

  // 根据ID获取角色
  getRoleById(id: number): Promise<Role> {
    return request.get(`/api/roles/${id}`);
  },

  // 创建角色
  createRole(data: CreateRoleRequest): Promise<Role> {
    return request.post('/api/roles', data);
  },

  // 更新角色
  updateRole(id: number, data: UpdateRoleRequest): Promise<Role> {
    return request.put(`/api/roles/${id}`, data);
  },

  // 删除角色
  deleteRole(id: number): Promise<ApiResponse<any>> {
    return request.delete(`/api/roles/${id}`);
  },

  // 根据用户ID获取角色列表
  getRolesByUserId(userId: number): Promise<Role[]> {
    return request.get(`/api/roles/user/${userId}`);
  },

  // 为用户分配角色
  assignRolesToUser(data: AssignRolesRequest): Promise<ApiResponse<any>> {
    return request.post('/api/roles/assign', data);
  }
};
