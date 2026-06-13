import { Directive } from 'vue';
import { usePermissionStore } from '../stores/permission';
import { useAuthStore } from '../stores/auth';

// 全局权限检查函数，用于强制刷新所有权限指令
let globalPermissionCheckCallback: (() => void) | null = null;

export function setGlobalPermissionCheckCallback(callback: () => void) {
  globalPermissionCheckCallback = callback;
}

export function triggerGlobalPermissionCheck() {
  if (globalPermissionCheckCallback) {
    globalPermissionCheckCallback();
  }
}

// 权限指令
export const vPermission: Directive = {
  mounted(el, binding) {
    const permissionStore = usePermissionStore();
    const { value } = binding;
    
    if (value) {
      // 延迟检查权限，给权限数据加载时间
      setTimeout(() => {
        checkPermission(el, value, permissionStore);
      }, 100);
    }
  },
  
  updated(el, binding) {
    const permissionStore = usePermissionStore();
    const { value } = binding;
    
    if (value) {
      checkPermission(el, value, permissionStore);
    }
  }
};

// 权限检查函数 - 添加重试计数避免无限等待
function checkPermission(el: HTMLElement, permissionKey: string, permissionStore: any, retryCount = 0) {
  const MAX_RETRIES = 3; // 最大重试次数
  const RETRY_DELAY = 1000; // 重试延迟（毫秒）
  
  console.log(`检查权限: ${permissionKey}, 权限数量: ${permissionStore.currentUserPermissions.length}, 重试次数: ${retryCount}`);
  
  // 检查权限数据是否已加载
  if (permissionStore.currentUserPermissions.length === 0) {
    // 如果超过最大重试次数，根据登录状态决定是否显示
    if (retryCount >= MAX_RETRIES) {
      const authStore = useAuthStore();
      if (authStore.isLoggedIn) {
        console.warn(`权限数据加载失败（已重试${MAX_RETRIES}次），用户已登录，显示元素: ${permissionKey}`);
        el.style.display = '';
      } else {
        console.log(`权限数据为空且用户未登录，隐藏元素: ${permissionKey}`);
        el.style.display = 'none';
      }
      return;
    }
    
    // 权限数据为空，延迟重试
    console.log(`权限数据未加载，延迟重试 (${retryCount + 1}/${MAX_RETRIES}): ${permissionKey}`);
    setTimeout(() => {
      checkPermission(el, permissionKey, permissionStore, retryCount + 1);
    }, RETRY_DELAY);
    return;
  }
  
  const hasPermission = permissionStore.hasPermission(permissionKey);
  console.log(`权限检查结果: ${permissionKey} = ${hasPermission}`);
  
  if (!hasPermission) {
    el.style.display = 'none';
  } else {
    el.style.display = '';
  }
}

// 角色指令
export const vRole: Directive = {
  mounted(el, binding) {
    const permissionStore = usePermissionStore();
    const { value } = binding;
    
    if (value) {
      // 延迟检查角色，给角色数据加载时间
      setTimeout(() => {
        checkRole(el, value, permissionStore);
      }, 100);
    }
  },
  
  updated(el, binding) {
    const permissionStore = usePermissionStore();
    const { value } = binding;
    
    if (value) {
      checkRole(el, value, permissionStore);
    }
  }
};

// 角色检查函数 - 添加重试计数避免无限等待
function checkRole(el: HTMLElement, roleKey: string, permissionStore: any, retryCount = 0) {
  const MAX_RETRIES = 3; // 最大重试次数
  const RETRY_DELAY = 1000; // 重试延迟（毫秒）
  
  console.log(`检查角色: ${roleKey}, 角色数量: ${permissionStore.currentUserRoles.length}, 重试次数: ${retryCount}`);
  
  // 检查角色数据是否已加载
  if (permissionStore.currentUserRoles.length === 0) {
    // 如果超过最大重试次数，根据登录状态决定是否显示
    if (retryCount >= MAX_RETRIES) {
      const authStore = useAuthStore();
      if (authStore.isLoggedIn) {
        console.warn(`角色数据加载失败（已重试${MAX_RETRIES}次），用户已登录，显示元素: ${roleKey}`);
        el.style.display = '';
      } else {
        console.log(`角色数据为空且用户未登录，隐藏元素: ${roleKey}`);
        el.style.display = 'none';
      }
      return;
    }
    
    // 角色数据为空，延迟重试
    console.log(`角色数据未加载，延迟重试 (${retryCount + 1}/${MAX_RETRIES}): ${roleKey}`);
    setTimeout(() => {
      checkRole(el, roleKey, permissionStore, retryCount + 1);
    }, RETRY_DELAY);
    return;
  }
  
  const hasRole = permissionStore.hasRole(roleKey);
  console.log(`角色检查结果: ${roleKey} = ${hasRole}`);
  
  if (!hasRole) {
    el.style.display = 'none';
  } else {
    el.style.display = '';
  }
}

// 管理员指令
export const vAdmin: Directive = {
  mounted(el) {
    const permissionStore = usePermissionStore();
    checkAdmin(el, permissionStore);
  },
  
  updated(el) {
    const permissionStore = usePermissionStore();
    checkAdmin(el, permissionStore);
  }
};

// 管理员检查函数 - 添加重试计数避免无限等待
function checkAdmin(el: HTMLElement, permissionStore: any, retryCount = 0) {
  const MAX_RETRIES = 3; // 最大重试次数
  const RETRY_DELAY = 1000; // 重试延迟（毫秒）
  
  console.log(`检查管理员权限, 角色数量: ${permissionStore.currentUserRoles.length}, 重试次数: ${retryCount}`);
  
  // 检查角色数据是否已加载
  if (permissionStore.currentUserRoles.length === 0) {
    // 如果超过最大重试次数，根据登录状态决定是否显示
    if (retryCount >= MAX_RETRIES) {
      const authStore = useAuthStore();
      if (authStore.isLoggedIn) {
        console.warn(`角色数据加载失败（已重试${MAX_RETRIES}次），用户已登录，显示管理员元素`);
        el.style.display = '';
      } else {
        console.log(`角色数据为空且用户未登录，隐藏管理员元素`);
        el.style.display = 'none';
      }
      return;
    }
    
    // 角色数据为空，延迟重试
    console.log(`角色数据未加载，延迟重试管理员权限 (${retryCount + 1}/${MAX_RETRIES})`);
    setTimeout(() => {
      checkAdmin(el, permissionStore, retryCount + 1);
    }, RETRY_DELAY);
    return;
  }
  
  const isAdmin = permissionStore.isAdmin;
  console.log(`管理员检查结果: ${isAdmin}`);
  
  if (!isAdmin) {
    el.style.display = 'none';
  } else {
    el.style.display = '';
  }
}
