import { Directive } from 'vue';
import { usePermissionStore } from '../stores/permission';
import { useAuthStore } from '../stores/auth';

// 简化的权限指令 - 确保能正常工作
export const vPermission: Directive = {
  mounted(el, binding) {
    const permissionStore = usePermissionStore();
    const authStore = useAuthStore();
    const value = binding.value;
    
    if (!value) return;
    
    // 如果用户未登录，隐藏元素
    if (!authStore.isLoggedIn) {
      el.style.display = 'none';
      return;
    }
    
    // 如果用户已登录，显示元素（简化策略）
    el.style.display = '';
    
    // 异步检查权限
    setTimeout(() => {
      if (permissionStore.currentUserPermissions.length > 0) {
        const hasPermission = permissionStore.hasPermission(value);
        el.style.display = hasPermission ? '' : 'none';
      }
    }, 1500);
  }
};

// 简化的角色指令
export const vRole: Directive = {
  mounted(el, binding) {
    const permissionStore = usePermissionStore();
    const authStore = useAuthStore();
    const value = binding.value;
    
    if (!value) return;
    
    // 如果用户未登录，隐藏元素
    if (!authStore.isLoggedIn) {
      el.style.display = 'none';
      return;
    }
    
    // 如果用户已登录，显示元素（简化策略）
    el.style.display = '';
    
    // 异步检查角色
    setTimeout(() => {
      if (permissionStore.currentUserRoles.length > 0) {
        const hasRole = permissionStore.hasRole(value);
        el.style.display = hasRole ? '' : 'none';
      }
    }, 1500);
  }
};

// 简化的管理员指令
export const vAdmin: Directive = {
  mounted(el) {
    const permissionStore = usePermissionStore();
    const authStore = useAuthStore();
    
    // 如果用户未登录，隐藏元素
    if (!authStore.isLoggedIn) {
      el.style.display = 'none';
      return;
    }
    
    // 如果用户已登录，显示元素（简化策略）
    el.style.display = '';
    
    // 异步检查管理员权限
    setTimeout(() => {
      if (permissionStore.currentUserRoles.length > 0) {
        const isAdmin = permissionStore.isAdmin;
        el.style.display = isAdmin ? '' : 'none';
      }
    }, 1500);
  }
};







