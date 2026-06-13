<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
      <h1 class="text-3xl font-bold text-gray-900 mb-8">权限调试页面</h1>
      
      <!-- 用户信息 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">用户信息</h2>
        <div class="space-y-2">
          <p><strong>登录状态:</strong> {{ authStore.isLoggedIn ? '已登录' : '未登录' }}</p>
          <p><strong>用户ID:</strong> {{ authStore.userInfo?.id || '无' }}</p>
          <p><strong>用户名:</strong> {{ authStore.userInfo?.username || '无' }}</p>
        </div>
      </div>
      
      <!-- 角色信息 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">角色信息</h2>
        <div class="space-y-2">
          <p><strong>角色数量:</strong> {{ permissionStore.currentUserRoles.length }}</p>
          <div v-if="permissionStore.currentUserRoles.length > 0">
            <p><strong>角色列表:</strong></p>
            <ul class="list-disc list-inside ml-4">
              <li v-for="role in permissionStore.currentUserRoles" :key="role.id">
                {{ role.roleName }} ({{ role.roleKey }})
              </li>
            </ul>
          </div>
          <p v-else class="text-red-600">无角色数据</p>
        </div>
      </div>
      
      <!-- 权限信息 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">权限信息</h2>
        <div class="space-y-2">
          <p><strong>权限数量:</strong> {{ permissionStore.currentUserPermissions.length }}</p>
          <div v-if="permissionStore.currentUserPermissions.length > 0">
            <p><strong>权限列表:</strong></p>
            <ul class="list-disc list-inside ml-4 max-h-40 overflow-y-auto">
              <li v-for="permission in permissionStore.currentUserPermissions" :key="permission.id">
                {{ permission.permissionName }} ({{ permission.permissionKey }})
              </li>
            </ul>
          </div>
          <p v-else class="text-red-600">无权限数据</p>
        </div>
      </div>
      
      <!-- 操作按钮 -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">操作</h2>
        <div class="space-x-4">
          <button
            @click="refreshPermissions"
            class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
          >
            刷新权限
          </button>
          <button
            @click="initPermissions"
            class="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700"
          >
            重新初始化权限
          </button>
          <button
            @click="clearPermissions"
            class="bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700"
          >
            清空权限数据
          </button>
        </div>
      </div>
      
      <!-- 权限测试 -->
      <div class="bg-white rounded-lg shadow-md p-6 mt-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">权限测试</h2>
        <div class="space-y-4">
          <div v-permission="'user:view'" class="p-4 bg-blue-100 rounded">
            用户管理权限测试 - 如果您能看到这个，说明有 user:view 权限
          </div>
          <div v-permission="'role:view'" class="p-4 bg-green-100 rounded">
            角色管理权限测试 - 如果您能看到这个，说明有 role:view 权限
          </div>
          <div v-admin class="p-4 bg-purple-100 rounded">
            管理员权限测试 - 如果您能看到这个，说明是管理员
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '../stores/auth';
import { usePermissionStore } from '../stores/permission';

const authStore = useAuthStore();
const permissionStore = usePermissionStore();

const refreshPermissions = async () => {
  try {
    await permissionStore.refreshCurrentUserPermissions();
    console.log('权限已刷新');
  } catch (error) {
    console.error('刷新权限失败:', error);
  }
};

const initPermissions = async () => {
  try {
    await permissionStore.initPermissions();
    console.log('权限已重新初始化');
  } catch (error) {
    console.error('初始化权限失败:', error);
  }
};

const clearPermissions = () => {
  permissionStore.currentUserRoles = [];
  permissionStore.currentUserPermissions = [];
  console.log('权限数据已清空');
};
</script>







