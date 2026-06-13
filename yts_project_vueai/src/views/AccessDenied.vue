<script setup lang="ts">
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = useAuthStore();

const goLogout = () => {
  authStore.logout();
  router.replace('/login');
};
</script>

<template>
  <div class="min-h-screen bg-gray-100 flex items-center justify-center px-4">
    <div class="max-w-md w-full bg-white rounded-lg shadow-lg p-8 text-center">
      <div class="mx-auto w-16 h-16 rounded-full bg-amber-100 flex items-center justify-center mb-4">
        <svg class="w-8 h-8 text-amber-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
          />
        </svg>
      </div>
      <h1 class="text-xl font-semibold text-gray-900 mb-2">暫無可用功能</h1>
      <p class="text-gray-600 text-sm mb-6">
        當前帳號未分配任何業務模塊權限，無法使用系統功能。請聯繫管理員在「角色 / 權限管理」中為您授權。
      </p>
      <button
        type="button"
        class="w-full py-2.5 px-4 rounded-md bg-indigo-600 text-white text-sm font-medium hover:bg-indigo-700 transition-colors"
        @click="goLogout"
      >
        退出登錄
      </button>
    </div>
  </div>
</template>
