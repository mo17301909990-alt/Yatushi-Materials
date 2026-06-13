<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useProcessStore } from '../../stores/process';
import type { ProcessType } from '../../types/process';
import { useRouter, useRoute } from 'vue-router';
import { usePermissionStore } from '../../stores/permission';
import MessageNotification from '../message/MessageNotification.vue';

const processStore = useProcessStore();
const router = useRouter();
const route = useRoute();
const permissionStore = usePermissionStore();
const loading = ref(false);

/** 後台入口：管理員角色或具備「系統管理」菜單權限（與庫中 system:management / 角色並行） */
const showBackendEntry = computed(() => {
  if (
    permissionStore.currentUserRoles.length === 0 &&
    permissionStore.currentUserPermissions.length === 0
  ) {
    return false;
  }
  return permissionStore.isAdmin || permissionStore.hasPermission('system:management');
});

/** 與 ProcessConfig.vue 主內容區 v-permission 一致 */
const PROCESS_ENTRY_PERMISSION: Partial<Record<ProcessType, string>> = {
  hotStamping: 'matching:hotstamping:view',
  printing: 'matching:printing:view',
  laminating: 'matching:lamination:view',
  screenPrinting: 'matching:silkscreen:view'
};

function canSeeProcess(processId: ProcessType): boolean {
  if (permissionStore.isAdmin) return true;
  const perm = PROCESS_ENTRY_PERMISSION[processId];
  if (!perm) {
    // 数据源入口（如硅胶）：无专属权限时，已登录用户可见
    return permissionStore.currentUserPermissions.length > 0;
  }
  if (
    permissionStore.currentUserRoles.length === 0 &&
    permissionStore.currentUserPermissions.length === 0
  ) {
    return false;
  }
  return permissionStore.hasPermission(perm);
}

const visibleProcesses = computed(() =>
  processStore.processes.filter((p) => canSeeProcess(p.id))
);

watch(
  visibleProcesses,
  (list) => {
    if (list.length === 0) return;
    const allowed = new Set(list.map((p) => p.id));
    const cur = processStore.currentProcess;
    if (cur === 'techManagement' || cur === 'recommended') return;
    if (!allowed.has(cur)) {
      processStore.setCurrentProcess(list[0].id);
    }
  },
  { immediate: true }
);

const handleProcessChange = async (processId: ProcessType) => {
  loading.value = true;
  processStore.setCurrentProcess(processId);

  setTimeout(() => {
    router.push('/process-config');
    loading.value = false;
  }, 300);
};

// 處理移動端下拉菜單變化
const handleMobileProcessChange = (event: Event) => {
  const target = event.target as HTMLSelectElement;
  if (target && target.value) {
    handleProcessChange(target.value as ProcessType);
  }
};



const handleTechManagement = () => {
  processStore.setCurrentProcess('techManagement');
  router.push('/process-config');
};

/** 匹配偏好 / 標籤匹配 / 技術管理 合併為一個下拉（與原 v-permission 鍵一致；管理員顯示全部） */
const canToolMatchPreference = computed(
  () => permissionStore.isAdmin || permissionStore.hasPermission('match:preference:view')
);
const canToolTagMatching = computed(
  () => permissionStore.isAdmin || permissionStore.hasPermission('matching:tag:select')
);
const canToolTechManagement = computed(
  () => permissionStore.isAdmin || permissionStore.hasPermission('tech:management:view')
);
const showToolNavDropdown = computed(
  () => canToolMatchPreference.value || canToolTagMatching.value || canToolTechManagement.value
);

/** 頂欄「系統操作指引」：與路由 /guide/hot-stamping-material 一致（查看指引 或 燙金物料主數據 或管理員） */
const showGuideNavLink = computed(
  () =>
    permissionStore.isAdmin ||
    permissionStore.hasPermission('system:guide:view') ||
    permissionStore.hasPermission('material:hot-stamping-material:view')
);

type ToolNavValue = '' | 'preference' | 'tag' | 'tech';

const toolNavValue = computed<ToolNavValue>(() => {
  const path = route.path;
  if (path.startsWith('/match-preference')) return 'preference';
  if (path.startsWith('/tag-matching')) return 'tag';
  if (path === '/process-config' && processStore.currentProcess === 'techManagement') return 'tech';
  return '';
});

const onToolNavChange = (event: Event) => {
  const el = event.target as HTMLSelectElement;
  const v = el.value as ToolNavValue;
  if (v === 'preference') {
    router.push('/match-preference-config');
  } else if (v === 'tag') {
    router.push('/tag-matching');
  } else if (v === 'tech') {
    handleTechManagement();
  }
  el.blur();
};

const logout = () => {
  localStorage.removeItem('isLoggedIn');
  router.push('/login');
};
</script>

<template>
  <nav class="bg-indigo-600 fixed w-full z-10">
    <div class="max-w-full mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <!-- 左側區域：系統標題 + 匹配系統按鈕 -->
        <div class="flex items-center space-x-6">
          <!-- 系統標題 -->
          <div class="flex-shrink-0">
            <h1 class="text-white text-xl font-bold">印刷廠物料匹配系統</h1>
          </div>

          <!-- 匹配系統：無對應工藝權限則不顯示（與 ProcessConfig 內容區一致） -->
          <div v-if="visibleProcesses.length > 0" class="hidden lg:flex items-center space-x-1">
            <button
              v-for="process in visibleProcesses"
              :key="process.id"
              @click="handleProcessChange(process.id)"
              :disabled="loading"
              :class="[
                'nav-button px-4 py-2 rounded-md text-sm font-medium transition-all duration-300 ease-in-out whitespace-nowrap relative',
                processStore.currentProcess === process.id
                  ? 'active bg-indigo-700 text-white shadow-lg transform scale-105'
                  : 'text-white hover:bg-indigo-500 hover:shadow-md hover:transform hover:scale-102',
                loading ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'
              ]"
            >
              <span class="relative z-10">{{ process.name }}</span>
              <!-- 加载指示器 -->
              <div
                v-if="loading && processStore.currentProcess === process.id"
                class="absolute inset-0 flex items-center justify-center"
              >
                <div class="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
              </div>
            </button>
          </div>

          <!-- 移動端：僅列出有權限的工藝 -->
          <div v-if="visibleProcesses.length > 0" class="lg:hidden">
            <select
              :value="processStore.currentProcess"
              @change="handleMobileProcessChange"
              class="bg-indigo-700 text-white border-indigo-500 rounded-md text-sm px-2 py-1 focus:outline-none focus:ring-2 focus:ring-indigo-300"
            >
              <option
                v-for="process in visibleProcesses"
                :key="process.id"
                :value="process.id"
              >
                {{ process.name }}
              </option>
            </select>
          </div>
        </div>

        <!--
          右側入口與路由 / 權限目錄一致：在「角色管理」中為角色勾選對應 permission_key 即可控制可見性。
          （庫表 permissions.parent_id 用於後台權限樹層級；頂欄不讀庫，由下列鍵與角色-權限關聯決定。）
        -->
        <div class="flex items-center space-x-2">
          <span v-permission="'announcement:view'" class="inline-flex items-center">
            <MessageNotification />
          </span>

          <router-link
            v-if="showGuideNavLink"
            to="/guide/hot-stamping-material"
            class="text-white hover:bg-indigo-500 hover:shadow-md hover:transform hover:scale-105 px-3 py-2 rounded-md text-sm font-medium transition-all duration-300 ease-in-out whitespace-nowrap"
            active-class="bg-indigo-700 shadow-lg"
          >
            系統操作指引
          </router-link>

          <router-link
            to="/agent/process"
            class="text-white hover:bg-indigo-500 hover:shadow-md hover:transform hover:scale-105 px-3 py-2 rounded-md text-sm font-medium transition-all duration-300 ease-in-out whitespace-nowrap"
            active-class="bg-indigo-700 shadow-lg"
          >
            工艺 Agent
          </router-link>

          <router-link
            to="/agent/chat"
            class="text-white hover:bg-indigo-500 hover:shadow-md hover:transform hover:scale-105 px-3 py-2 rounded-md text-sm font-medium transition-all duration-300 ease-in-out whitespace-nowrap"
            active-class="bg-indigo-700 shadow-lg"
          >
            Agent 集群
          </router-link>

          <router-link
            to="/smart-version"
            class="bg-gradient-to-r from-purple-500 to-pink-500 text-white hover:from-purple-600 hover:to-pink-600 shadow-md hover:shadow-lg px-4 py-2 rounded-md text-sm font-medium transition-all duration-300 ease-in-out whitespace-nowrap animate-pulse"
            active-class="bg-indigo-700 shadow-lg"
          >
            ✨ AI 智能版
          </router-link>

          <div v-if="showToolNavDropdown" class="relative inline-flex items-center">
            <label class="sr-only" for="tool-nav-select">匹配與技術功能</label>
            <select
              id="tool-nav-select"
              :value="toolNavValue"
              aria-label="匹配與技術"
              @change="onToolNavChange"
              class="bg-indigo-700 text-white border border-indigo-400/80 rounded-md text-sm font-medium pl-2 pr-7 py-2 max-w-[11rem] sm:max-w-none focus:outline-none focus:ring-2 focus:ring-indigo-300 cursor-pointer appearance-none bg-[length:1rem] bg-[right_0.35rem_center] bg-no-repeat"
              style="background-image: url('data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 fill=%22none%22 viewBox=%220 0 24 24%22 stroke=%22white%22%3E%3Cpath stroke-linecap=%22round%22 stroke-linejoin=%22round%22 stroke-width=%222%22 d=%22M19 9l-7 7-7-7%22/%3E%3C/svg%3E')"
            >
              <option value="" disabled>匹配與技術</option>
              <option v-if="canToolMatchPreference" value="preference">匹配偏好</option>
              <option v-if="canToolTagMatching" value="tag">標籤匹配</option>
              <option v-if="canToolTechManagement" value="tech">技術管理</option>
            </select>
          </div>

          <router-link
            v-permission="'resource:group:selector:view'"
            to="/resource-group-selector"
            class="text-white hover:bg-indigo-500 hover:shadow-md hover:transform hover:scale-105 px-3 py-2 rounded-md text-sm font-medium transition-all duration-300 ease-in-out whitespace-nowrap"
            active-class="bg-indigo-700 shadow-lg"
          >
            資源組選擇工具
          </router-link>

          <router-link
            v-if="showBackendEntry"
            to="/admin"
            class="text-white hover:bg-indigo-500 hover:shadow-md hover:transform hover:scale-105 px-3 py-2 rounded-md text-sm font-medium transition-all duration-300 ease-in-out whitespace-nowrap"
            active-class="bg-indigo-700 shadow-lg"
          >
            後台管理
          </router-link>
          <button
            @click="logout"
            class="text-white hover:bg-red-500 hover:shadow-md hover:transform hover:scale-105 px-3 py-2 rounded-md text-sm font-medium transition-all duration-300 ease-in-out whitespace-nowrap"
          >
            退出登錄
          </button>
        </div>
      </div>
    </div>
  </nav>
</template>

<style scoped>
/* 导航按钮动画效果 */
.nav-button {
  position: relative;
  overflow: hidden;
}

.nav-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transition: left 0.5s;
}

.nav-button:hover::before {
  left: 100%;
}

/* 活跃状态的特殊效果 */
.nav-button.active {
  background: linear-gradient(135deg, #4f46e5, #6366f1);
  box-shadow: 0 4px 15px rgba(79, 70, 229, 0.4);
}

/* 加载动画 */
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.animate-spin {
  animation: spin 1s linear infinite;
}

/* 微交互效果 */
.hover\:scale-102:hover {
  transform: scale(1.02);
}

.hover\:scale-105:hover {
  transform: scale(1.05);
}

/* 确保导航栏在小屏幕下也能正常显示 */
@media (max-width: 1024px) {
  .flex.items-center.space-x-6 {
    gap: 1rem;
  }

  .flex.items-center.space-x-3 {
    gap: 0.5rem;
  }
}

/* 优化按钮在小屏幕下的显示 */
@media (max-width: 768px) {
  .text-xl {
    font-size: 1.125rem; /* 18px */
  }

  .px-3 {
    padding-left: 0.5rem;
    padding-right: 0.5rem;
  }

  .text-sm {
    font-size: 0.75rem; /* 12px */
  }
}

/* 确保移动端下拉菜单样式 */
select option {
  background-color: #4f46e5;
  color: white;
}

/* 防止按钮文字换行 */
.whitespace-nowrap {
  white-space: nowrap;
}

/* 优化系统标题在小屏幕下的显示 */
@media (max-width: 640px) {
  h1 {
    font-size: 1rem; /* 16px */
  }
}
</style>