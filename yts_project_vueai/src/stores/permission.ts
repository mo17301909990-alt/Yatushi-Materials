import { defineStore } from 'pinia';
import { roleApi } from '../api/modules/role';
import { permissionApi } from '../api/modules/permission';
import type { Role, Permission } from '../api/types/permission';
import { useAuthStore } from './auth';

export const usePermissionStore = defineStore('permission', {
  state: () => ({
    // 角色相关状态
    roles: [] as Role[],
    currentUserRoles: [] as Role[],
    
    // 权限相关状态
    permissions: [] as Permission[],
    menuPermissions: [] as Permission[],
    currentUserPermissions: [] as Permission[],
    
    // 加载状态
    loading: {
      roles: false,
      permissions: false,
      menuPermissions: false
    },

    // RBAC 是否已完成初始化
    rbacReady: false
  }),

  getters: {
    // 获取所有角色
    getAllRoles: (state) => state.roles,
    
    // 获取当前用户角色
    getCurrentUserRoles: (state) => state.currentUserRoles,
    
    // 获取所有权限
    getAllPermissions: (state) => state.permissions,
    
    // 获取菜单权限
    getMenuPermissions: (state) => state.menuPermissions,
    
    // 获取当前用户权限
    getCurrentUserPermissions: (state) => state.currentUserPermissions,
    
    // 检查用户是否有指定权限
    hasPermission: (state) => (permissionKey: string) => {
      return state.currentUserPermissions.some(permission => 
        permission.permissionKey === permissionKey
      );
    },
    
    // 检查用户是否有指定角色
    hasRole: (state) => (roleKey: string) => {
      return state.currentUserRoles.some(role => role.roleKey === roleKey);
    },
    
    // 检查用户是否有管理员权限
    isAdmin: (state) => {
      return state.currentUserRoles.some(role => 
        role.roleKey === 'ADMIN' || role.roleKey === 'SUPER_ADMIN'
      );
    }
  },

  actions: {
    // 初始化权限系统
    async initPermissions() {
      try {
        // 获取当前登录用户信息
        const authStore = useAuthStore();
        if (authStore.isLoggedIn && authStore.userInfo?.id) {
          // 添加超时处理，避免长时间等待
          const timeoutPromise = new Promise((_, reject) => {
            setTimeout(() => reject(new Error('权限初始化超时')), 15000); // 15秒超时
          });
          
          const initPromise = Promise.all([
            this.fetchCurrentUserRoles(authStore.userInfo.id),
            this.fetchCurrentUserPermissions(authStore.userInfo.id)
          ]);
          
          await Promise.race([initPromise, timeoutPromise]);
        } else {
          console.log('用户未登录，跳过权限初始化');
          // 未登录时清空权限数据
          this.currentUserRoles = [];
          this.currentUserPermissions = [];
        }
      } catch (error: any) {
        console.error('初始化权限系统失败:', error);
        // 如果是超时错误，给出更明确的提示
        if (error.message === '权限初始化超时') {
          console.warn('权限初始化超时，可能是网络连接问题或后端响应慢');
        }
        // 初始化失败时，清空权限数据（安全策略）
        this.currentUserRoles = [];
        this.currentUserPermissions = [];
        // 不抛出错误，避免阻塞应用启动
      } finally {
        this.rbacReady = true;
      }
    },

    // 刷新当前用户权限
    async refreshCurrentUserPermissions() {
      try {
        const authStore = useAuthStore();
        if (authStore.isLoggedIn && authStore.userInfo?.id) {
          await this.fetchCurrentUserRoles(authStore.userInfo.id);
          await this.fetchCurrentUserPermissions(authStore.userInfo.id);
          console.log('用户权限已刷新');
        }
      } catch (error) {
        console.error('刷新用户权限失败:', error);
        // 刷新失败时，清空权限数据（安全策略）
        this.currentUserRoles = [];
        this.currentUserPermissions = [];
      }
    },

    // 强制清空当前用户权限
    clearCurrentUserPermissions() {
      this.currentUserRoles = [];
      this.currentUserPermissions = [];
      console.log('用户权限已强制清空');
    },

    // 获取所有角色
    async fetchRoles() {
      this.loading.roles = true;
      try {
        const response = await roleApi.getAllRoles();
        this.roles = response || [];
      } catch (error) {
        console.error('获取角色列表失败:', error);
        this.roles = [];
      } finally {
        this.loading.roles = false;
      }
    },

    // 获取所有权限
    async fetchPermissions() {
      this.loading.permissions = true;
      try {
        const response = await permissionApi.getAllPermissions();
        this.permissions = response || [];
      } catch (error) {
        console.error('获取权限列表失败:', error);
        this.permissions = [];
      } finally {
        this.loading.permissions = false;
      }
    },

    // 获取菜单权限
    async fetchMenuPermissions() {
      this.loading.menuPermissions = true;
      try {
        this.menuPermissions = await permissionApi.getMenuPermissions();
      } catch (error) {
        console.error('获取菜单权限失败:', error);
        throw error;
      } finally {
        this.loading.menuPermissions = false;
      }
    },

    // 获取当前用户角色
    async fetchCurrentUserRoles(userId: number) {
      try {
        const roles = await roleApi.getRolesByUserId(userId);
        this.currentUserRoles = roles || [];
      } catch (error) {
        console.error('获取用户角色失败:', error);
        this.currentUserRoles = [];
        throw error;
      }
    },

    // 获取当前用户权限
    async fetchCurrentUserPermissions(userId: number) {
      try {
        const permissions = await permissionApi.getPermissionsByUserId(userId);
        this.currentUserPermissions = permissions || [];
      } catch (error) {
        console.error('获取用户权限失败:', error);
        this.currentUserPermissions = [];
        throw error;
      }
    },

    // 创建角色
    async createRole(roleData: any) {
      try {
        const newRole = await roleApi.createRole(roleData);
        this.roles.push(newRole);
        return newRole;
      } catch (error) {
        console.error('创建角色失败:', error);
        throw error;
      }
    },

    // 更新角色
    async updateRole(id: number, roleData: any) {
      try {
        const updatedRole = await roleApi.updateRole(id, roleData);
        const index = this.roles.findIndex(role => role.id === id);
        if (index !== -1) {
          this.roles[index] = updatedRole;
        }
        return updatedRole;
      } catch (error) {
        console.error('更新角色失败:', error);
        throw error;
      }
    },

    // 删除角色
    async deleteRole(id: number) {
      try {
        await roleApi.deleteRole(id);
        this.roles = this.roles.filter(role => role.id !== id);
      } catch (error) {
        console.error('删除角色失败:', error);
        throw error;
      }
    },

    // 创建权限
    async createPermission(permissionData: any) {
      try {
        const newPermission = await permissionApi.createPermission(permissionData);
        this.permissions.push(newPermission);
        return newPermission;
      } catch (error) {
        console.error('创建权限失败:', error);
        throw error;
      }
    },

    // 更新权限
    async updatePermission(id: number, permissionData: any) {
      try {
        const updatedPermission = await permissionApi.updatePermission(id, permissionData);
        const index = this.permissions.findIndex(permission => permission.id === id);
        if (index !== -1) {
          this.permissions[index] = updatedPermission;
        }
        return updatedPermission;
      } catch (error) {
        console.error('更新权限失败:', error);
        throw error;
      }
    },

    // 删除权限
    async deletePermission(id: number) {
      try {
        await permissionApi.deletePermission(id);
        this.permissions = this.permissions.filter(permission => permission.id !== id);
      } catch (error) {
        console.error('删除权限失败:', error);
        throw error;
      }
    },

    // 为用户分配角色
    async assignRolesToUser(userId: number, roleIds: number[]) {
      try {
        await roleApi.assignRolesToUser({ userId, roleIds });
        // 如果当前用户是自己，重新获取角色
        // 这里可以添加逻辑来检查当前用户是否需要刷新权限
        // await this.fetchCurrentUserRoles(userId);
        // await this.fetchCurrentUserPermissions(userId);
      } catch (error) {
        console.error('分配角色失败:', error);
        throw error;
      }
    },

    // 为角色分配权限
    async assignPermissionsToRole(roleId: number, permissionIds: number[]) {
      try {
        await permissionApi.assignPermissionsToRole({ roleId, permissionIds });
      } catch (error) {
        console.error('分配权限失败:', error);
        throw error;
      }
    },

    // 清空权限状态
    clearPermissionState() {
      this.roles = [];
      this.currentUserRoles = [];
      this.permissions = [];
      this.menuPermissions = [];
      this.currentUserPermissions = [];
    }
  }
});
