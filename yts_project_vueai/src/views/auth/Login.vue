<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import type { LoginParams } from '@/api/types/auth';

const router = useRouter();
const authStore = useAuthStore();
const loading = ref(false);
const errorMessage = ref('');
const showPassword = ref(false);

const form = reactive<LoginParams>({
  username: '',
  password: ''
});

const login = async () => {
  loading.value = true;
  errorMessage.value = '';
  
  if (!form.username || !form.password) {
    errorMessage.value = '請輸入用戶名和密碼';
    loading.value = false;
    return;
  }

  try {
    await authStore.login(form);
    router.push('/process-config');
  } catch (error: any) {
    errorMessage.value = error.message || '登錄失敗，請檢查用戶名和密碼';
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="min-h-screen flex flex-col bg-gray-100">
    <!-- 顶部标题栏 -->
    <div class="bg-indigo-600 text-white py-4 px-6">
      <h1 class="text-xl font-bold">印刷廠物料匹配系統</h1>
    </div>
    
    <!-- 登录表单区域 -->
    <div class="flex-grow flex items-center justify-center px-4 sm:px-6 lg:px-8">
      <div class="max-w-md w-full bg-white p-8 rounded-lg shadow-md">
        <div>
          <h2 class="text-center text-2xl font-bold text-gray-900">系統登錄</h2>
          <p class="mt-2 text-center text-sm text-gray-600">
            請輸入您的賬號和密碼
          </p>
        </div>
        <form class="mt-8 space-y-6" @submit.prevent="login">
          <div>
            <label for="username" class="block text-sm font-medium text-gray-700">用戶名</label>
            <input
              id="username"
              v-model="form.username"
              type="text"
              required
              class="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              placeholder="請輸入用戶名"
            />
          </div>
          <div>
            <label for="password" class="block text-sm font-medium text-gray-700">密碼</label>
            <div class="relative">
              <input
                id="password"
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                required
                class="mt-1 appearance-none block w-full px-3 py-2 pr-10 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                placeholder="請輸入密碼"
              />
              <button
                type="button"
                @click="showPassword = !showPassword"
                class="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-400 hover:text-gray-600 mt-1"
              >
                <svg v-if="!showPassword" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                </svg>
                <svg v-else class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21" />
                </svg>
              </button>
            </div>
          </div>

          <div v-if="errorMessage" class="text-red-500 text-sm text-center">
            {{ errorMessage }}
          </div>

          <div>
            <button
              type="submit"
              :disabled="loading"
              class="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
              <span v-if="loading" class="mr-2">
                <svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
              </span>
              {{ loading ? '登錄中...' : '登錄' }}
            </button>
            <!-- 在登录表单底部添加 -->
            <div class="text-center mt-4">
              <router-link to="/register" class="text-sm text-indigo-600 hover:text-indigo-500">
              沒有賬號？點擊註冊
              </router-link>
            </div>
          </div>
        </form>
      </div>
    </div>

    <!-- 底部版权信息 -->
    <div class="bg-white py-3 text-center text-gray-500 text-sm">
      © 2025 印刷廠物料匹配系統 版權所有
    </div>
  </div>
</template>