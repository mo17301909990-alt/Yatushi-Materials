<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="bg-white rounded-lg shadow-md p-6">
        <h1 class="text-2xl font-bold text-gray-900 mb-6">权限测试页面</h1>
        
        <!-- 当前用户信息 -->
        <div class="mb-6 p-4 bg-blue-50 rounded-lg">
          <h2 class="text-lg font-semibold text-blue-900 mb-2">当前用户信息</h2>
          <p><strong>用户ID:</strong> {{ authStore.userInfo?.id || '未登录' }}</p>
          <p><strong>用户名:</strong> {{ authStore.userInfo?.username || '未登录' }}</p>
          <p><strong>角色数量:</strong> {{ permissionStore.currentUserRoles.length }}</p>
          <p><strong>权限数量:</strong> {{ permissionStore.currentUserPermissions.length }}</p>
        </div>

        <!-- 权限测试区域 -->
        <div class="space-y-4">
          <h2 class="text-lg font-semibold text-gray-900">权限测试</h2>
          
          <!-- 系统管理权限 -->
          <div class="p-4 border rounded-lg">
            <h3 class="font-medium text-gray-800 mb-2">系统管理权限 (system:management)</h3>
            <div v-permission="'system:management'" class="p-2 bg-green-100 text-green-800 rounded">
              ✅ 您有系统管理权限
            </div>
            <div v-if="!permissionStore.hasPermission('system:management')" class="p-2 bg-red-100 text-red-800 rounded">
              ❌ 您没有系统管理权限
            </div>
          </div>

          <!-- 用户管理权限 -->
          <div class="p-4 border rounded-lg">
            <h3 class="font-medium text-gray-800 mb-2">用户管理权限 (user:view)</h3>
            <div v-permission="'user:view'" class="p-2 bg-green-100 text-green-800 rounded">
              ✅ 您有用户管理权限
            </div>
            <div v-if="!permissionStore.hasPermission('user:view')" class="p-2 bg-red-100 text-red-800 rounded">
              ❌ 您没有用户管理权限
            </div>
          </div>

          <!-- 角色管理权限 -->
          <div class="p-4 border rounded-lg">
            <h3 class="font-medium text-gray-800 mb-2">角色管理权限 (role:view)</h3>
            <div v-permission="'role:view'" class="p-2 bg-green-100 text-green-800 rounded">
              ✅ 您有角色管理权限
            </div>
            <div v-if="!permissionStore.hasPermission('role:view')" class="p-2 bg-red-100 text-red-800 rounded">
              ❌ 您没有角色管理权限
            </div>
          </div>

          <!-- 管理员角色测试 -->
          <div class="p-4 border rounded-lg">
            <h3 class="font-medium text-gray-800 mb-2">管理员角色 (ADMIN)</h3>
            <div v-role="'ADMIN'" class="p-2 bg-green-100 text-green-800 rounded">
              ✅ 您是管理员
            </div>
            <div v-if="!permissionStore.hasRole('ADMIN')" class="p-2 bg-red-100 text-red-800 rounded">
              ❌ 您不是管理员
            </div>
          </div>

          <!-- 管理员指令测试 -->
          <div class="p-4 border rounded-lg">
            <h3 class="font-medium text-gray-800 mb-2">管理员指令 (v-admin)</h3>
            <div v-admin class="p-2 bg-green-100 text-green-800 rounded">
              ✅ 管理员指令显示
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="mt-6 flex space-x-4">
          <button
            @click="refreshPermissions"
            class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
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

        <!-- 调试信息 -->
        <div class="mt-6 p-4 bg-gray-100 rounded-lg">
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
    currentUserRoles: permissionStore.currentUserRoles,
    currentUserPermissions: permissionStore.currentUserPermissions,
    isAdmin: permissionStore.isAdmin,
    hasSystemManagement: permissionStore.hasPermission('system:management'),
    hasUserView: permissionStore.hasPermission('user:view'),
    hasRoleView: permissionStore.hasPermission('role:view'),
    hasAdminRole: permissionStore.hasRole('ADMIN')
  };
});

const refreshPermissions = async () => {
  try {
    await permissionStore.refreshCurrentUserPermissions();
    console.log('权限已刷新');
  } catch (error) {
    console.error('刷新权限失败:', error);
  }
};

const clearPermissions = () => {
  permissionStore.clearCurrentUserPermissions();
  console.log('权限已清空');
};
</script>
