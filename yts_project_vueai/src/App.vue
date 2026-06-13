<script setup lang="ts">
import { computed } from 'vue';
import ProcessNavigation from './components/layout/ProcessNavigation.vue';
import { useProcessStore } from './stores/process';
import { useRoute } from 'vue-router';

const processStore = useProcessStore();
const route = useRoute();

// 檢查當前路由是否是登錄或註冊頁
const isAuthPage = computed(() => route.path === '/login' || route.path === '/register');
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <!-- 只在非認證頁顯示導航欄 -->
    <ProcessNavigation v-if="!isAuthPage" />
    <div v-if="!isAuthPage" class="pt-16">
      <main>
        <router-view></router-view>
      </main>
    </div>
    <!-- 認證頁直接顯示路由內容 -->
    <router-view v-if="isAuthPage"></router-view>
  </div>
</template>