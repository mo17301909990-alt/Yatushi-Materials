<template>
    <div class="min-h-screen flex flex-col bg-gray-50">
      <!-- 注册表单区域 -->
      <div class="flex-grow flex items-center justify-center px-4 sm:px-6 lg:px-8">
        <div class="max-w-md w-full bg-white p-8 rounded-lg shadow-md">
          <div>
            <h2 class="text-center text-2xl font-bold text-gray-900">用戶註冊</h2>
            <p class="mt-2 text-center text-sm text-gray-600">
              請填寫註冊信息
            </p>
          </div>
          <form class="mt-8 space-y-6" @submit.prevent="register">
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
              <input
                id="password"
                v-model="form.password"
                type="password"
                required
                class="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                placeholder="請輸入密碼"
              />
            </div>
            <div>
              <label for="email" class="block text-sm font-medium text-gray-700">郵箱</label>
              <input
                id="email"
                v-model="form.email"
                type="email"
                required
                class="mt-1 appearance-none block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                placeholder="請輸入郵箱"
              />
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
                {{ loading ? '註冊中...' : '註冊' }}
              </button>
            </div>
            <div class="text-center">
              <router-link to="/login" class="text-sm text-indigo-600 hover:text-indigo-500">
                已有賬號？點擊登錄
              </router-link>
            </div>
          </form>
        </div>
      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, reactive } from 'vue';
  import { useRouter } from 'vue-router';
  import { useAuthStore } from '@/stores/auth';
  import type { RegisterParams } from '@/api/types/auth';
  
  const router = useRouter();
  const authStore = useAuthStore();
  const loading = ref(false);
  const errorMessage = ref('');
  
  const form = reactive<RegisterParams>({
    username: '',
    password: '',
    email: ''
  });
  
  const register = async () => {
    loading.value = true;
    errorMessage.value = '';
    
    if (!form.username || !form.password || !form.email) {
      errorMessage.value = '請填寫所有必填項';
      loading.value = false;
      return;
    }
  
    try {
      await authStore.register(form);
      router.push('/login');
    } catch (error: any) {
        errorMessage.value = error.response?.data?.message || error.message || '註冊失敗，請重試';
    } finally {
      loading.value = false;
    }
  };
  </script>