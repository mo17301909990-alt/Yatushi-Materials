<script setup lang="ts">
import { computed, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useProcessStore } from '../stores/process';
import { usePermissionStore } from '../stores/permission';
import ProcessSidebar from '../components/layout/ProcessSidebar.vue';
import TechManagementContent from '@/components/tech/TechManagementContent.vue';

const router = useRouter();

const processStore = useProcessStore();
const permissionStore = usePermissionStore();

// 根據當前工藝獲取對應的頁面信息
const currentProcessInfo = computed(() => {
  const process = processStore.processes.find(p => p.id === processStore.currentProcess);
  return process || processStore.processes[0];
});

// 判斷是否為燙金系統（已開發）
const isHotStampingSystem = computed(() => {
  return processStore.currentProcess === 'hotStamping';
});

// 判斷是否為技術管理
const isTechManagement = computed(() => {
  return processStore.currentProcess === 'techManagement';
});

// 調試信息
watch(() => processStore.currentProcess, (newValue) => {
  console.log('Current process changed to:', newValue);
  console.log('isTechManagement:', isTechManagement.value);
}, { immediate: true });
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <!-- 燙金系統顯示完整功能 - 需要工藝配置權限 -->
    <ProcessSidebar v-if="isHotStampingSystem" v-permission="'process:hotstamping:view'" />

    <!-- 技術管理顯示技術管理內容 - 需要技術管理權限 -->
    <TechManagementContent v-else-if="isTechManagement" v-permission="'tech:management:view'" />

    <!-- 其他系統顯示開發中頁面 - 需要查看權限 -->
    <div v-else v-permission="'process:other:view'" class="flex items-center justify-center min-h-screen">
      <div class="text-center bg-white rounded-lg shadow-lg p-12 max-w-md mx-4">
        <div class="mb-6">
          <div class="w-20 h-20 bg-indigo-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <svg class="w-10 h-10 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z"></path>
            </svg>
          </div>
          <h2 class="text-2xl font-bold text-gray-900 mb-2">
            {{ currentProcessInfo.name }}
          </h2>
          <p class="text-gray-600 mb-6">
            {{ currentProcessInfo.description }}
          </p>
        </div>

        <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
          <div class="flex items-center">
            <svg class="w-5 h-5 text-yellow-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
            </svg>
            <span class="text-yellow-800 font-medium">系統開發中</span>
          </div>
        </div>

        <p class="text-gray-500 text-sm">
          此功能模塊正在開發中，敬請期待！<br>
          如有疑問，請聯繫系統管理員。
        </p>

        <div class="mt-8 space-y-3">
          <!-- 返回燙金系統按鈕 - 需要燙金權限 -->
          <button
            v-permission="'process:hotstamping:view'"
            @click="processStore.setCurrentProcess('hotStamping')"
            class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-2 px-6 rounded-lg transition-colors duration-200"
          >
            返回燙金系統
          </button>
          
          <!-- 管理員返回管理後台 -->
          <button
            v-admin
            @click="router.push('/admin')"
            class="w-full bg-purple-600 hover:bg-purple-700 text-white font-medium py-2 px-6 rounded-lg transition-colors duration-200"
          >
            管理後台
          </button>
        </div>
      </div>
    </div>
  </div>
</template>