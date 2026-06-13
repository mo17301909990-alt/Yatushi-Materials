<template>
  <div class="relative">
    <button
      @click="handleToggleDropdown"
      class="relative p-2 text-white hover:text-gray-200 focus:outline-none"
    >
      <svg
        class="w-6 h-6"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
        />
      </svg>
      <span
        v-if="unreadCount > 0"
        class="absolute top-0 right-0 block h-4 w-4 rounded-full bg-red-500 text-white text-xs flex items-center justify-center"
      >
        {{ unreadCount > 99 ? '99+' : unreadCount }}
      </span>
    </button>

    <!-- 下拉菜单 -->
    <div
      v-if="showDropdown"
      class="absolute right-0 mt-2 w-80 bg-white rounded-lg shadow-lg z-50 border border-gray-200"
      @click.stop
    >
      <div class="p-4 border-b border-gray-200">
        <div class="flex justify-between items-center">
          <h3 class="text-lg font-semibold text-gray-900">消息通知</h3>
          <button
            @click="showDropdown = false"
            class="text-gray-400 hover:text-gray-600"
          >
            ✕
          </button>
        </div>
        <p class="text-sm text-gray-600 mt-1">
          未读消息: {{ unreadCount }} 条
        </p>
      </div>
      <div class="max-h-96 overflow-y-auto">
        <div v-if="recentMessages.length === 0" class="p-4 text-center text-gray-500">
          暂无消息
        </div>
        <div
          v-for="message in recentMessages"
          :key="message.id"
          class="p-4 border-b border-gray-100 hover:bg-gray-50 cursor-pointer"
          @click="handleMessageClick(message)"
        >
          <div class="flex items-start space-x-3">
            <div class="flex-1">
              <div class="flex items-center space-x-2 flex-wrap">
                <h4
                  :class="[
                    'text-sm font-medium',
                    message.readFlag === 0 ? 'text-gray-900 font-bold' : 'text-gray-700'
                  ]"
                >
                  {{ message.title }}
                </h4>
                <!-- 消息类型标签 -->
                <span
                  :class="[
                    'px-2 py-0.5 text-xs font-medium rounded-full flex-shrink-0',
                    message.msgCategory === '1' 
                      ? 'bg-blue-100 text-blue-700' 
                      : 'bg-purple-100 text-purple-700'
                  ]"
                >
                  {{ message.msgCategory === '1' ? '通知公告' : '系统消息' }}
                </span>
                <!-- 三天内的通知公告特殊标记（仅通知公告显示） -->
                <span
                  v-if="message.msgCategory === '1' && isWithinThreeDays(message.createTime)"
                  class="px-2 py-0.5 text-xs font-medium bg-orange-100 text-orange-700 rounded-full flex-shrink-0"
                >
                  新
                </span>
              </div>
              <p class="mt-1 text-xs text-gray-400">
                {{ formatTime(message.createTime) }}
              </p>
            </div>
            <span
              v-if="message.readFlag === 0"
              class="flex-shrink-0 w-2 h-2 bg-red-500 rounded-full mt-1"
            ></span>
          </div>
        </div>
      </div>
      <div class="p-4 border-t border-gray-200">
        <button
          @click="goToMessageCenter"
          class="w-full text-center text-blue-600 hover:text-blue-800 text-sm font-medium"
        >
          查看全部消息 →
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { useAnnouncementStore } from '@/stores/announcement';
import { useAuthStore } from '@/stores/auth';
import type { SysAnnouncement } from '@/api/modules/announcement';

const router = useRouter();
const announcementStore = useAnnouncementStore();
const authStore = useAuthStore();

const showDropdown = ref(false);
const recentMessages = ref<SysAnnouncement[]>([]);

const unreadCount = computed(() => announcementStore.unreadCount);

const loadRecentMessages = async () => {
  if (!authStore.userInfo?.id) {
    return;
  }
  try {
    // 加载所有类型的未读消息（通知公告和系统消息）
    const result = await announcementStore.fetchMessages({
      userId: authStore.userInfo.id,
      readFlag: 0, // 只显示未读消息
      pageNo: 1,
      pageSize: 5
    });
    if (result) {
      recentMessages.value = result.records;
    }
  } catch (error) {
    console.error('加载最近消息失败:', error);
  }
};

const handleMessageClick = async (message: SysAnnouncement) => {
  // 如果未读，标记为已读（所有消息类型都支持）
  if ((message.readFlag === 0 || message.readFlag === undefined) && message.id) {
    try {
      await announcementStore.markAsRead(message.id);
      // 标记已读后立即刷新未读数量
      await announcementStore.fetchUnreadCount();
      // 更新本地消息状态
      const msg = recentMessages.value.find(m => m.id === message.id);
      if (msg) {
        msg.readFlag = 1;
        msg.readTime = new Date().toISOString();
      }
      // 从未读列表中移除
      recentMessages.value = recentMessages.value.filter(m => m.id !== message.id);
    } catch (error) {
      console.error('标记已读失败:', error);
    }
  }
  // 如果有业务跳转，跳转到业务页面
  if (message.busType && message.busId) {
    const route = announcementStore.navigateToBusiness(message.busType, message.busId);
    if (route) {
      router.push(route);
    }
  } else {
    // 否则跳转到消息中心
    router.push('/sys/announcement');
  }
  showDropdown.value = false;
};

const handleToggleDropdown = async () => {
  showDropdown.value = !showDropdown.value;
  if (showDropdown.value) {
    // 打开下拉菜单时刷新未读数量和最近消息
    await announcementStore.fetchUnreadCount();
    await loadRecentMessages();
  }
};

const goToMessageCenter = () => {
  router.push('/sys/announcement');
  showDropdown.value = false;
};

const formatTime = (time?: string) => {
  if (!time) return '';
  const date = new Date(time);
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(diff / 3600000);
  const days = Math.floor(diff / 86400000);

  if (minutes < 1) return '刚刚';
  if (minutes < 60) return `${minutes}分钟前`;
  if (hours < 24) return `${hours}小时前`;
  if (days < 7) return `${days}天前`;
  return date.toLocaleDateString('zh-CN');
};

// 判断消息是否在三天内
const isWithinThreeDays = (time?: string): boolean => {
  if (!time) return false;
  const messageDate = new Date(time);
  const now = new Date();
  const diffTime = now.getTime() - messageDate.getTime();
  const diffDays = diffTime / (1000 * 60 * 60 * 24); // 转换为天数
  return diffDays >= 0 && diffDays <= 3;
};

// 点击外部关闭下拉菜单
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  if (!target.closest('.relative')) {
    showDropdown.value = false;
  }
};

let refreshInterval: number | null = null;

onMounted(() => {
  // 初始化时只加载系统消息的未读数量
  announcementStore.fetchUnreadCount();
  // 不自动加载最近消息，等用户打开下拉菜单时再加载
  // 每30秒刷新一次未读数量
  refreshInterval = window.setInterval(() => {
    announcementStore.fetchUnreadCount();
    // 如果下拉菜单打开，也刷新最近消息
    if (showDropdown.value) {
      loadRecentMessages();
    }
  }, 30000);
  document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
  if (refreshInterval !== null) {
    clearInterval(refreshInterval);
  }
  document.removeEventListener('click', handleClickOutside);
});
</script>

