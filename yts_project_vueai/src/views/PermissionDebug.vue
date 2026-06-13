<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="bg-white rounded-lg shadow-md p-6">
        <h1 class="text-2xl font-bold text-gray-900 mb-6">权限调试页面</h1>
        
        <!-- 当前状态 -->
        <div class="mb-6 p-4 bg-blue-50 rounded-lg">
          <h2 class="text-lg font-semibold text-blue-900 mb-2">当前状态</h2>
          <p><strong>登录状态:</strong> {{ authStore.isLoggedIn ? '已登录' : '未登录' }}</p>
          <p><strong>用户ID:</strong> {{ authStore.userInfo?.id || '无' }}</p>
          <p><strong>用户名:</strong> {{ authStore.userInfo?.username || '无' }}</p>
          <p><strong>角色数量:</strong> {{ permissionStore.currentUserRoles.length }}</p>
          <p><strong>权限数量:</strong> {{ permissionStore.currentUserPermissions.length }}</p>
        </div>

        <!-- 操作按钮 -->
        <div class="mb-6 flex space-x-4">
          <button
            @click="initPermissions"
            class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            初始化权限
          </button>
          <button
            @click="refreshPermissions"
            class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700"
          >
            刷新权限
          </button>
          <button
            @click="clearPermissions"
            class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
          >
            清空权限
          </button>
        </div>

        <!-- 角色信息 -->
        <div class="mb-6 p-4 bg-gray-50 rounded-lg">
          <h3 class="font-medium text-gray-800 mb-2">当前用户角色</h3>
          <div v-if="permissionStore.currentUserRoles.length === 0" class="text-red-600">
            无角色数据
          </div>
          <div v-else>
            <div v-for="role in permissionStore.currentUserRoles" :key="role.id" class="mb-2">
              <span class="font-medium">{{ role.roleName }}</span>
              <span class="text-gray-500 ml-2">({{ role.roleKey }})</span>
            </div>
          </div>
        </div>

        <!-- 权限信息 -->
        <div class="mb-6 p-4 bg-gray-50 rounded-lg">
          <h3 class="font-medium text-gray-800 mb-2">当前用户权限</h3>
          <div v-if="permissionStore.currentUserPermissions.length === 0" class="text-red-600">
            无权限数据
          </div>
          <div v-else>
            <div v-for="permission in permissionStore.currentUserPermissions" :key="permission.id" class="mb-2">
              <span class="font-medium">{{ permission.permissionName }}</span>
              <span class="text-gray-500 ml-2">({{ permission.permissionKey }})</span>
            </div>
          </div>
        </div>

        <!-- 权限测试 -->
        <div class="mb-6 p-4 bg-gray-50 rounded-lg">
          <h3 class="font-medium text-gray-800 mb-2">权限测试</h3>
          
          <div class="mb-4">
            <h4 class="font-medium text-gray-700 mb-2">系统管理权限 (system:management)</h4>
            <div v-permission="'system:management'" class="p-2 bg-green-100 text-green-800 rounded mb-2">
              ✅ 您有系统管理权限
            </div>
            <div v-if="!permissionStore.hasPermission('system:management')" class="p-2 bg-red-100 text-red-800 rounded mb-2">
              ❌ 您没有系统管理权限
            </div>
          </div>

          <div class="mb-4">
            <h4 class="font-medium text-gray-700 mb-2">用户管理权限 (user:view)</h4>
            <div v-permission="'user:view'" class="p-2 bg-green-100 text-green-800 rounded mb-2">
              ✅ 您有用户管理权限
            </div>
            <div v-if="!permissionStore.hasPermission('user:view')" class="p-2 bg-red-100 text-red-800 rounded mb-2">
              ❌ 您没有用户管理权限
            </div>
          </div>

          <div class="mb-4">
            <h4 class="font-medium text-gray-700 mb-2">管理员角色 (ADMIN)</h4>
            <div v-role="'ADMIN'" class="p-2 bg-green-100 text-green-800 rounded mb-2">
              ✅ 您是管理员
            </div>
            <div v-if="!permissionStore.hasRole('ADMIN')" class="p-2 bg-red-100 text-red-800 rounded mb-2">
              ❌ 您不是管理员
            </div>
          </div>
        </div>

        <!-- 调试信息 -->
        <div class="p-4 bg-gray-100 rounded-lg">
          <h3 class="font-medium text-gray-800 mb-2">调试信息</h3>
          <pre class="text-sm text-gray-600 overflow-auto">{{ debugInfo }}</pre>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useAuthStore } from '../stores/auth';
import { usePermissionStore } from '../stores/permission';

const authStore = useAuthStore();
const permissionStore = usePermissionStore();

const debugInfo = computed(() => {
  return {
    authStore: {
      isLoggedIn: authStore.isLoggedIn,
      userInfo: authStore.userInfo
    },
    permissionStore: {
      currentUserRoles: permissionStore.currentUserRoles,
      currentUserPermissions: permissionStore.currentUserPermissions,
      isAdmin: permissionStore.isAdmin,
      hasSystemManagement: permissionStore.hasPermission('system:management'),
      hasUserView: permissionStore.hasPermission('user:view'),
      hasRoleView: permissionStore.hasPermission('role:view'),
      hasAdminRole: permissionStore.hasRole('ADMIN')
    }
  };
});

const initPermissions = async () => {
  try {
    console.log('手动初始化权限...');
    await permissionStore.initPermissions();
    console.log('权限初始化完成');
  } catch (error) {
    console.error('权限初始化失败:', error);
  }
};

const refreshPermissions = async () => {
  try {
    console.log('刷新权限...');
    await permissionStore.refreshCurrentUserPermissions();
    console.log('权限刷新完成');
  } catch (error) {
    console.error('刷新权限失败:', error);
  }
};

const clearPermissions = () => {
  console.log('清空权限...');
  permissionStore.clearCurrentUserPermissions();
  console.log('权限已清空');
};
</script>







