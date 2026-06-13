<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="bg-white rounded-lg shadow-md p-6">
        <h1 class="text-2xl font-bold text-gray-900 mb-6">简单测试页面（无权限控制）</h1>
        
        <!-- 基本信息 -->
        <div class="mb-6 p-4 bg-blue-50 rounded-lg">
          <h2 class="text-lg font-semibold text-blue-900 mb-2">系统状态</h2>
          <p><strong>页面加载时间:</strong> {{ loadTime }}</p>
          <p><strong>当前时间:</strong> {{ currentTime }}</p>
        </div>

        <!-- 用户信息 -->
        <div class="mb-6 p-4 bg-green-50 rounded-lg">
          <h2 class="text-lg font-semibold text-green-900 mb-2">用户信息</h2>
          <p><strong>登录状态:</strong> {{ authStore.isLoggedIn ? '已登录' : '未登录' }}</p>
          <p><strong>用户ID:</strong> {{ authStore.userInfo?.id || '无' }}</p>
          <p><strong>用户名:</strong> {{ authStore.userInfo?.username || '无' }}</p>
        </div>

        <!-- 权限信息 -->
        <div class="mb-6 p-4 bg-yellow-50 rounded-lg">
          <h2 class="text-lg font-semibold text-yellow-900 mb-2">权限信息</h2>
          <p><strong>角色数量:</strong> {{ permissionStore.currentUserRoles.length }}</p>
          <p><strong>权限数量:</strong> {{ permissionStore.currentUserPermissions.length }}</p>
          <p><strong>是否管理员:</strong> {{ permissionStore.isAdmin ? '是' : '否' }}</p>
        </div>

        <!-- 测试按钮 -->
        <div class="mb-6 flex space-x-4">
          <button
            @click="testPermissionLoad"
            class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            测试权限加载
          </button>
          <button
            @click="refreshPage"
            class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700"
          >
            刷新页面
          </button>
        </div>

        <!-- 测试结果 -->
        <div class="p-4 bg-gray-100 rounded-lg">
          <h3 class="font-medium text-gray-800 mb-2">测试结果</h3>
          <div v-if="testResults.length === 0" class="text-gray-500">
            暂无测试结果
          </div>
          <div v-else>
            <div v-for="(result, index) in testResults" :key="index" class="mb-2 p-2 bg-white rounded">
              <span class="font-medium">{{ result.time }}:</span> {{ result.message }}
            </div>
          </div>
        </div>

        <!-- 导航链接 -->
        <div class="mt-6 p-4 bg-purple-50 rounded-lg">
          <h3 class="font-medium text-purple-900 mb-2">导航链接</h3>
          <div class="flex flex-wrap gap-2">
            <router-link to="/test-api" class="px-3 py-1 bg-purple-600 text-white rounded hover:bg-purple-700">
              API测试
            </router-link>
            <router-link to="/permission-debug" class="px-3 py-1 bg-purple-600 text-white rounded hover:bg-purple-700">
              权限调试
            </router-link>
            <router-link to="/permission-test" class="px-3 py-1 bg-purple-600 text-white rounded hover:bg-purple-700">
              权限测试
            </router-link>
            <router-link to="/admin" class="px-3 py-1 bg-purple-600 text-white rounded hover:bg-purple-700">
              管理面板
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useAuthStore } from '../stores/auth';
import { usePermissionStore } from '../stores/permission';

const authStore = useAuthStore();
const permissionStore = usePermissionStore();

const loadTime = ref('');
const currentTime = ref('');
const testResults = ref<Array<{time: string, message: string}>>([]);

const addTestResult = (message: string) => {
  const now = new Date();
  testResults.value.unshift({
    time: now.toLocaleTimeString(),
    message
  });
};

const testPermissionLoad = async () => {
  addTestResult('开始测试权限加载...');
  
  try {
    await permissionStore.initPermissions();
    addTestResult(`权限加载完成 - 角色: ${permissionStore.currentUserRoles.length}, 权限: ${permissionStore.currentUserPermissions.length}`);
  } catch (error) {
    addTestResult(`权限加载失败: ${error}`);
  }
};

const refreshPage = () => {
  window.location.reload();
};

const updateTime = () => {
  currentTime.value = new Date().toLocaleString();
};

onMounted(() => {
  loadTime.value = new Date().toLocaleString();
  updateTime();
  
  // 每秒更新时间
  setInterval(updateTime, 1000);
  
  addTestResult('页面加载完成');
});
</script>







