// 角色相关类型定义
export interface Role {
  id?: number;
  roleName: string;
  roleKey: string;
  description?: string;
  status: number; // 0-禁用 1-启用
  createdTime?: string;
  updatedTime?: string;
}

// 权限相关类型定义
export interface Permission {
  id?: number;
  permissionName: string;
  permissionKey: string;
  permissionType: 'MENU' | 'BUTTON'; // MENU-菜单 BUTTON-按钮
  path?: string; // 菜单路径
  component?: string; // 组件路径
  icon?: string; // 图标
  parentId?: number; // 父级ID
  orderNum?: number; // 排序
  status: number; // 0-禁用 1-启用
  createdTime?: string;
  updatedTime?: string;
  children?: Permission[]; // 子权限（用于树形结构）
}

// 用户角色关联
export interface UserRole {
  id?: number;
  userId: number;
  roleId: number;
  createdTime?: string;
}

// 角色权限关联
export interface RolePermission {
  id?: number;
  roleId: number;
  permissionId: number;
  createdTime?: string;
}

// API请求参数
export interface CreateRoleRequest {
  roleName: string;
  roleKey: string;
  description?: string;
  status?: number;
}

export interface UpdateRoleRequest extends CreateRoleRequest {
  id: number;
}

export interface AssignRolesRequest {
  userId: number;
  roleIds: number[];
}

export interface CreatePermissionRequest {
  permissionName: string;
  permissionKey: string;
  permissionType: 'MENU' | 'BUTTON';
  path?: string;
  component?: string;
  icon?: string;
  parentId?: number;
  orderNum?: number;
  status?: number;
}

export interface UpdatePermissionRequest extends CreatePermissionRequest {
  id: number;
}

export interface AssignPermissionsRequest {
  roleId: number;
  permissionIds: number[];
}

// API响应格式
export interface ApiResponse<T> {
  data?: T;
  message?: string;
  code?: number;
}
