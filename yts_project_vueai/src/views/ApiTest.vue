<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="bg-white rounded-lg shadow-md p-6">
        <h1 class="text-2xl font-bold text-gray-900 mb-6">API测试页面</h1>
        
        <!-- 当前用户信息 -->
        <div class="mb-6 p-4 bg-blue-50 rounded-lg">
          <h2 class="text-lg font-semibold text-blue-900 mb-2">当前用户信息</h2>
          <p><strong>登录状态:</strong> {{ authStore.isLoggedIn ? '已登录' : '未登录' }}</p>
          <p><strong>用户ID:</strong> {{ authStore.userInfo?.id || '无' }}</p>
          <p><strong>用户名:</strong> {{ authStore.userInfo?.username || '无' }}</p>
        </div>

        <!-- API测试按钮 -->
        <div class="mb-6 flex flex-wrap gap-4">
          <button
            @click="testUserRoles"
            class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            测试获取用户角色
          </button>
          <button
            @click="testUserPermissions"
            class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700"
          >
            测试获取用户权限
          </button>
          <button
            @click="testAllRoles"
            class="px-4 py-2 bg-purple-600 text-white rounded-md hover:bg-purple-700"
          >
            测试获取所有角色
          </button>
          <button
            @click="testAllPermissions"
            class="px-4 py-2 bg-orange-600 text-white rounded-md hover:bg-orange-700"
          >
            测试获取所有权限
          </button>
          <button
            @click="testProductOptions"
            class="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700"
          >
            测试产品选项API
          </button>
          <button
            @click="testIntegratedFilter"
            class="px-4 py-2 bg-pink-600 text-white rounded-md hover:bg-pink-700"
          >
            测试集成筛选功能
          </button>
        </div>

        <!-- 测试结果 -->
        <div class="mb-6 p-4 bg-gray-50 rounded-lg">
          <h3 class="font-medium text-gray-800 mb-2">测试结果</h3>
          <div v-if="testResults.length === 0" class="text-gray-500">
            暂无测试结果
          </div>
          <div v-else>
            <div v-for="(result, index) in testResults" :key="index" class="mb-4 p-3 border rounded">
              <div class="font-medium text-gray-800 mb-2">{{ result.title }}</div>
              <div :class="result.success ? 'text-green-600' : 'text-red-600'" class="mb-2">
                {{ result.success ? '✅ 成功' : '❌ 失败' }}
              </div>
              <div class="text-sm text-gray-600">
                <strong>响应:</strong>
                <pre class="mt-1 p-2 bg-gray-100 rounded text-xs overflow-auto">{{ result.data }}</pre>
              </div>
            </div>
          </div>
        </div>

        <!-- 控制台日志 -->
        <div class="p-4 bg-gray-100 rounded-lg">
          <h3 class="font-medium text-gray-800 mb-2">控制台日志</h3>
          <p class="text-sm text-gray-600">请打开浏览器开发者工具查看详细的控制台日志</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '../stores/auth';
import { roleApi } from '../api/modules/role';
import { permissionApi } from '../api/modules/permission';
import { productApi, completeProductInfoApi } from '../api/modules/product';
import axios from 'axios';

const authStore = useAuthStore();
const testResults = ref<Array<{title: string, success: boolean, data: any}>>([]);

const addTestResult = (title: string, success: boolean, data: any) => {
  testResults.value.unshift({
    title,
    success,
    data: JSON.stringify(data, null, 2)
  });
};

const testUserRoles = async () => {
  if (!authStore.userInfo?.id) {
    addTestResult('获取用户角色', false, '用户未登录或用户ID不存在');
    return;
  }
  
  try {
    console.log(`测试获取用户 ${authStore.userInfo.id} 的角色...`);
    const roles = await roleApi.getRolesByUserId(authStore.userInfo.id);
    console.log('用户角色结果:', roles);
    addTestResult('获取用户角色', true, roles);
  } catch (error) {
    console.error('获取用户角色失败:', error);
    addTestResult('获取用户角色', false, error);
  }
};

const testUserPermissions = async () => {
  if (!authStore.userInfo?.id) {
    addTestResult('获取用户权限', false, '用户未登录或用户ID不存在');
    return;
  }
  
  try {
    console.log(`测试获取用户 ${authStore.userInfo.id} 的权限...`);
    const permissions = await permissionApi.getPermissionsByUserId(authStore.userInfo.id);
    console.log('用户权限结果:', permissions);
    addTestResult('获取用户权限', true, permissions);
  } catch (error) {
    console.error('获取用户权限失败:', error);
    addTestResult('获取用户权限', false, error);
  }
};

const testAllRoles = async () => {
  try {
    console.log('测试获取所有角色...');
    const roles = await roleApi.getAllRoles();
    console.log('所有角色结果:', roles);
    addTestResult('获取所有角色', true, roles);
  } catch (error) {
    console.error('获取所有角色失败:', error);
    addTestResult('获取所有角色', false, error);
  }
};

const testAllPermissions = async () => {
  try {
    console.log('测试获取所有权限...');
    const permissions = await permissionApi.getAllPermissions();
    console.log('所有权限结果:', permissions);
    addTestResult('获取所有权限', true, permissions);
  } catch (error) {
    console.error('获取所有权限失败:', error);
    addTestResult('获取所有权限', false, error);
  }
};

const testProductOptions = async () => {
  try {
    console.log('测试获取产品选项...');
    const options = await productApi.getProductOptions();
    console.log('产品选项结果:', options);
    addTestResult('获取产品选项', true, options);
  } catch (error) {
    console.error('获取产品选项失败:', error);
    addTestResult('获取产品选项', false, error);
  }
};

const testIntegratedFilter = async () => {
  try {
    console.log('测试集成筛选功能...');
    
    // 测试集成到现有查询系统的筛选功能
    const testParams = {
      paizi: '测试牌子',
      colorNum: '测试颜色编码',
      // 可以添加其他现有的查询参数
      companyNumber: 'TEST001'
    };
    
    // 模拟调用集成查询API
    const response = await axios.post('/api/api/gold-foil/match', testParams);
    console.log('集成筛选结果:', response.data);
    addTestResult('集成筛选功能测试', true, response.data);
    
  } catch (error) {
    console.error('集成筛选测试失败:', error);
    addTestResult('集成筛选功能测试', false, error);
  }
};
</script>





