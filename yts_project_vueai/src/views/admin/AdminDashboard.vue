<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">後台管理系統</h1>
        <p class="mt-2 text-gray-600">物料匹配系統管理面板</p>
      </div>

      <!-- 功能卡片网格 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <!-- 用户管理 - 需要用户管理权限 -->
        <div v-permission="'user:view'" class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <div class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197m13.5-9a2.5 2.5 0 11-5 0 2.5 2.5 0 015 0z"></path>
                </svg>
              </div>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">用戶管理</h3>
              <p class="text-sm text-gray-500">管理系統用戶賬戶</p>
            </div>
          </div>
          <div class="mt-4">
            <router-link
              to="/admin/user-management"
              class="w-full bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors text-center block"
            >
              進入用戶管理
            </router-link>
          </div>
        </div>

        <!-- 角色管理 - 需要角色管理权限 -->
        <div v-permission="'role:view'" class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path>
                </svg>
              </div>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">角色管理</h3>
              <p class="text-sm text-gray-500">管理系統角色和權限</p>
            </div>
          </div>
          <div class="mt-4">
            <router-link
              to="/admin/role-management"
              class="w-full bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors text-center block"
            >
              進入角色管理
            </router-link>
          </div>
        </div>

        <!-- 权限管理 - 需要管理员权限 -->
        <div v-admin class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path>
                </svg>
              </div>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">權限管理</h3>
              <p class="text-sm text-gray-500">管理系統權限和菜單配置</p>
            </div>
          </div>
          <div class="mt-4">
            <router-link
              to="/admin/permission-management"
              class="w-full bg-purple-600 text-white px-4 py-2 rounded-md hover:bg-purple-700 transition-colors text-center block"
            >
              進入權限管理
            </router-link>
          </div>
        </div>

        <!-- 物料匹配规则管理 - 需要物料管理权限 -->
        <div v-permission="'material:rule:view'" class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <div class="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
              </div>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">燙金物料匹配規則</h3>
              <p class="text-sm text-gray-500">管理物料匹配規則和配置</p>
            </div>
          </div>
          <div class="mt-4">
            <router-link
              to="/admin/material-rule-management"
              class="w-full bg-yellow-600 text-white px-4 py-2 rounded-md hover:bg-yellow-700 transition-colors text-center block"
            >
              管理規則
            </router-link>
          </div>
        </div>

        <!-- 系统日志 - 需要系统管理权限 -->
        <div v-permission="'system:log:view'" class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <div class="w-12 h-12 bg-red-100 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                </svg>
              </div>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">系統日誌</h3>
              <p class="text-sm text-gray-500">查看系統操作日誌</p>
            </div>
          </div>
          <div class="mt-4">
            <button class="w-full bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition-colors">
              查看日誌
            </button>
          </div>
        </div>

        <!-- 数据备份 - 需要系统管理权限 -->
        <div v-permission="'system:backup:create'" class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <div class="w-12 h-12 bg-indigo-100 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path>
                </svg>
              </div>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">數據備份</h3>
              <p class="text-sm text-gray-500">備份和恢復系統數據</p>
            </div>
          </div>
          <div class="mt-4">
            <button class="w-full bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 transition-colors">
              數據備份
            </button>
          </div>
        </div>

      </div>

      <!-- 系统状态概览 -->
      <div class="mt-8 bg-white rounded-lg shadow-md p-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">系統狀態概覽</h2>
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div class="text-center">
            <div class="text-2xl font-bold text-blue-600">12</div>
            <div class="text-sm text-gray-500">在線用戶</div>
          </div>
          <div class="text-center">
            <div class="text-2xl font-bold text-green-600">1,234</div>
            <div class="text-sm text-gray-500">今日匹配次數</div>
          </div>
          <div class="text-center">
            <div class="text-2xl font-bold text-yellow-600">98.5%</div>
            <div class="text-sm text-gray-500">系統可用性</div>
          </div>
          <div class="text-center">
            <div class="text-2xl font-bold text-purple-600">45</div>
            <div class="text-sm text-gray-500">活躍規則</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 这里可以添加后台管理相关的逻辑
</script>

<style scoped>
/* 可以添加特定的样式 */
</style>
