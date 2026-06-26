<script setup lang="ts">
import { computed, ref, onMounted } from 'vue';
import ProcessNavigation from './components/layout/ProcessNavigation.vue';
import { useProcessStore } from './stores/process';
import { useRoute } from 'vue-router';

const processStore = useProcessStore();
const route = useRoute();

// 檢查當前路由是否是登錄或註冊頁
const isAuthPage = computed(() => route.path === '/login' || route.path === '/register');

// ====== 新用户引导 ======
const showGuide = ref(false);

onMounted(() => {
  const seen = localStorage.getItem('yts_guide_seen');
  if (!seen) {
    showGuide.value = true;
  }
});

function dismissGuide() {
  showGuide.value = false;
  localStorage.setItem('yts_guide_seen', 'true');
}
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <!-- 只在非認證頁顯示導航欄 -->
    <ProcessNavigation v-if="!isAuthPage" />
    <div v-if="!isAuthPage" class="pt-20">
      <main>
        <router-view></router-view>
      </main>
    </div>
    <!-- 認證頁直接顯示路由內容 -->
    <router-view v-if="isAuthPage"></router-view>

    <!-- 首次使用引导弹窗 -->
    <div
      v-if="showGuide && !isAuthPage"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/40"
    >
      <div class="bg-white rounded-2xl shadow-2xl max-w-md w-full mx-4 overflow-hidden">
        <!-- 头部 -->
        <div class="bg-gradient-to-r from-indigo-500 to-purple-600 px-6 py-8 text-center">
          <div class="w-16 h-16 mx-auto mb-3 bg-white/20 rounded-full flex items-center justify-center">
            <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
            </svg>
          </div>
          <h2 class="text-white text-xl font-bold">欢迎使用印刷厂物料匹配系统</h2>
          <p class="text-indigo-100 text-sm mt-1">快速上手，高效匹配物料</p>
        </div>

        <!-- 引导项 -->
        <div class="px-6 py-5 space-y-4">
          <div class="flex items-start space-x-3">
            <span class="flex-shrink-0 w-8 h-8 bg-indigo-100 rounded-full flex items-center justify-center text-indigo-600 font-bold text-sm">1</span>
            <div>
              <p class="text-gray-800 font-medium">顶部搜索框可快速查找物料</p>
              <p class="text-gray-500 text-sm mt-0.5">输入物料名称、型号或编码，一键搜索全局数据</p>
            </div>
          </div>
          <div class="flex items-start space-x-3">
            <span class="flex-shrink-0 w-8 h-8 bg-indigo-100 rounded-full flex items-center justify-center text-indigo-600 font-bold text-sm">2</span>
            <div>
              <p class="text-gray-800 font-medium">左侧品类列表切换不同工艺</p>
              <p class="text-gray-500 text-sm mt-0.5">烫金、UV油墨、过胶材料等品类快速切换</p>
            </div>
          </div>
          <div class="flex items-start space-x-3">
            <span class="flex-shrink-0 w-8 h-8 bg-indigo-100 rounded-full flex items-center justify-center text-indigo-600 font-bold text-sm">3</span>
            <div>
              <p class="text-gray-800 font-medium">AI 助手在智能版页面</p>
              <p class="text-gray-500 text-sm mt-0.5">点击导航栏「AI 智能版」体验智能匹配与工艺引导</p>
            </div>
          </div>
        </div>

        <!-- 底部按钮 -->
        <div class="px-6 py-4 bg-gray-50 border-t border-gray-100">
          <button
            class="w-full bg-gradient-to-r from-indigo-500 to-purple-600 text-white font-medium py-2.5 rounded-lg hover:from-indigo-600 hover:to-purple-700 transition-all duration-200 shadow-md hover:shadow-lg"
            @click="dismissGuide"
          >
            知道了，开始使用
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
