<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { usePermissionStore } from '../../stores/permission';
import MessageNotification from '../message/MessageNotification.vue';
import { unifiedSearch } from '@/api/modules/copilot';
import type { UnifiedCompatibility } from '@/types/copilot';

const router = useRouter();
const route = useRoute();
const permissionStore = usePermissionStore();

// ====== 全局搜索 ======
const searchQuery = ref('');
const searchResults = ref<UnifiedCompatibility[]>([]);
const showDropdown = ref(false);
const searching = ref(false);
let searchDebounceTimer: ReturnType<typeof setTimeout> | null = null;

function onSearchInput() {
  if (searchDebounceTimer) clearTimeout(searchDebounceTimer);
  const q = searchQuery.value.trim();
  if (!q) {
    searchResults.value = [];
    showDropdown.value = false;
    return;
  }
  searchDebounceTimer = setTimeout(async () => {
    searching.value = true;
    try {
      const results = await unifiedSearch(q);
      searchResults.value = (results || []).slice(0, 5);
      showDropdown.value = searchResults.value.length > 0;
    } catch {
      searchResults.value = [];
      showDropdown.value = false;
    } finally {
      searching.value = false;
    }
  }, 300);
}

function selectSearchResult(item: UnifiedCompatibility) {
  showDropdown.value = false;
  searchQuery.value = '';
  searchResults.value = [];
  router.push({
    path: '/smart-version',
    query: { search: item.materialName, code: item.materialCode }
  });
}

function handleSearchBlur() {
  setTimeout(() => { showDropdown.value = false; }, 200);
}

function handleSearchFocus() {
  if (searchResults.value.length > 0) {
    showDropdown.value = true;
  }
}

function statusBadgeClass(status: string): string {
  if (status === 'compatible' || status === '匹配') return 'bg-green-100 text-green-700';
  if (status === 'incompatible' || status === '不匹配') return 'bg-red-100 text-red-700';
  if (status === 'partial' || status === '部分匹配') return 'bg-yellow-100 text-yellow-700';
  return 'bg-gray-100 text-gray-600';
}

/** 後台入口：管理員角色或具備「系統管理」菜單權限 */
const showBackendEntry = computed(() => {
  if (
    permissionStore.currentUserRoles.length === 0 &&
    permissionStore.currentUserPermissions.length === 0
  ) {
    return false;
  }
  return permissionStore.isAdmin || permissionStore.hasPermission('system:management');
});

/** 匹配偏好 / 標籤匹配 / 技術管理 合併為一個下拉 */
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

/** 頂欄「系統操作指引」 */
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
    router.push('/process-config');
  }
  el.blur();
};

const logout = () => {
  localStorage.removeItem('isLoggedIn');
  router.push('/login');
};

/** 5 个模块入口 */
const moduleEntries = [
  { path: '/process-config', label: '烫金' },
  { path: '/matching/uv-oil-matte', label: 'UV油墨' },
  { path: '/matching/silicone', label: '硅胶' },
  { path: '/matching/leo', label: 'LEO纸品' },
  { path: '/matching/lamination-material', label: '印刷加工' },
] as const;
</script>

<template>
  <nav class="bg-indigo-600 fixed w-full z-10">
    <div class="max-w-full mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 第一行：標題 + 5 模块入口 + 右侧功能 -->
      <div class="flex items-center justify-between h-14">
        <!-- 左側：標題 + 模块入口 + 搜索 -->
        <div class="flex items-center space-x-4 overflow-x-auto">
          <div class="flex-shrink-0">
            <h1 class="text-white text-lg lg:text-xl font-bold truncate whitespace-nowrap">印刷廠物料系統</h1>
          </div>

          <!-- 5 个模块入口按钮（桌面端） -->
          <div class="items-center space-x-1 hidden lg:flex">
            <router-link
              to="/process-config"
              class="px-3 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-all whitespace-nowrap text-indigo-100 hover:bg-indigo-500 hover:text-white"
              active-class="bg-indigo-700 text-white shadow-lg"
            >
              烫金
            </router-link>
            <router-link
              to="/matching/uv-oil-matte"
              class="px-3 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-all whitespace-nowrap text-indigo-100 hover:bg-indigo-500 hover:text-white"
              active-class="bg-indigo-700 text-white shadow-lg"
            >
              UV油墨
            </router-link>
            <router-link
              to="/matching/silicone"
              class="px-3 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-all whitespace-nowrap text-indigo-100 hover:bg-indigo-500 hover:text-white"
              active-class="bg-indigo-700 text-white shadow-lg"
            >
              硅胶
            </router-link>
            <router-link
              to="/matching/leo"
              class="px-3 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-all whitespace-nowrap text-indigo-100 hover:bg-indigo-500 hover:text-white"
              active-class="bg-indigo-700 text-white shadow-lg"
            >
              LEO纸品
            </router-link>
            <router-link
              to="/matching/lamination-material"
              class="px-3 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-all whitespace-nowrap text-indigo-100 hover:bg-indigo-500 hover:text-white"
              active-class="bg-indigo-700 text-white shadow-lg"
            >
              印刷加工
            </router-link>
          </div>

          <!-- 移动端模块下拉 -->
          <div class="lg:hidden">
            <select
              :value="route.path"
              @change="(e) => { const el = e.target as HTMLSelectElement; if (el.value) router.push(el.value); el.blur(); }"
              class="bg-indigo-700 text-white border-indigo-500 rounded-md text-xs px-1.5 py-1 focus:outline-none focus:ring-2 focus:ring-indigo-300"
            >
              <option value="" disabled>选择模块</option>
              <option value="/process-config">烫金</option>
              <option value="/matching/uv-oil-matte">UV油墨</option>
              <option value="/matching/silicone">硅胶</option>
              <option value="/matching/leo">LEO纸品</option>
              <option value="/matching/lamination-material">印刷加工</option>
            </select>
          </div>

          <!-- 全局搜索框 -->
          <div class="relative hidden lg:block">
            <div class="flex items-center">
              <svg class="absolute left-2.5 w-4 h-4 text-indigo-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
              <input
                v-model="searchQuery"
                placeholder="搜索物料、型号..."
                class="w-48 lg:w-56 bg-indigo-500/40 text-white placeholder-indigo-200/70 rounded-md pl-8 pr-3 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300 focus:bg-indigo-500/60 transition-colors"
                @input="onSearchInput"
                @focus="handleSearchFocus"
                @blur="handleSearchBlur"
              />
              <svg v-if="searching" class="absolute right-2.5 w-4 h-4 text-indigo-200 animate-spin" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
              </svg>
            </div>

            <!-- 搜索下拉 -->
            <div
              v-if="showDropdown && searchResults.length > 0"
              class="absolute top-full left-0 right-0 mt-1 bg-white rounded-lg shadow-xl border border-gray-200 overflow-hidden z-50"
            >
              <div
                v-for="(item, idx) in searchResults"
                :key="idx"
                class="px-3 py-2.5 hover:bg-indigo-50 cursor-pointer border-b border-gray-100 last:border-b-0 transition-colors"
                @mousedown.prevent="selectSearchResult(item)"
              >
                <div class="flex items-center justify-between">
                  <span class="text-sm text-gray-800 font-medium truncate">{{ item.materialName || '—' }}</span>
                  <span v-if="item.compatibilityStatus" class="ml-2 inline-flex items-center px-1.5 py-0.5 rounded-full text-xs font-medium"
                    :class="statusBadgeClass(item.compatibilityStatus)"
                  >{{ item.compatibilityStatus }}</span>
                </div>
                <div class="text-xs text-gray-400 mt-0.5">
                  {{ item.materialCode || item.moduleType || '' }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 右側：用戶操作 -->
        <div class="flex items-center space-x-1 lg:space-x-2 flex-shrink-0">
          <span v-permission="'announcement:view'" class="inline-flex items-center">
            <MessageNotification />
          </span>

          <router-link
            v-if="showGuideNavLink"
            to="/guide/hot-stamping-material"
            class="text-white hover:bg-indigo-500 px-2 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-colors whitespace-nowrap"
            active-class="bg-indigo-700"
          >
            操作指引
          </router-link>

          <router-link
            to="/agent/process"
            class="text-white hover:bg-indigo-500 px-2 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-colors whitespace-nowrap"
            active-class="bg-indigo-700"
          >
            工艺 Agent
          </router-link>

          <router-link
            to="/agent/chat"
            class="text-white hover:bg-indigo-500 px-2 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-colors whitespace-nowrap"
            active-class="bg-indigo-700"
          >
            Agent 集群
          </router-link>

          <router-link
            to="/smart-version"
            class="bg-gradient-to-r from-purple-500 to-pink-500 text-white hover:from-purple-600 hover:to-pink-600 px-3 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-colors whitespace-nowrap"
            active-class="bg-indigo-700"
          >
            AI 智能版
          </router-link>

          <div v-if="showToolNavDropdown" class="relative inline-flex items-center">
            <select
              id="tool-nav-select"
              :value="toolNavValue"
              aria-label="匹配與技術"
              @change="onToolNavChange"
              class="bg-indigo-700 text-white border border-indigo-400/80 rounded-md text-xs lg:text-sm font-medium pl-1.5 pr-5 py-1.5 max-w-[8rem] lg:max-w-none focus:outline-none focus:ring-2 focus:ring-indigo-300 cursor-pointer appearance-none bg-[length:0.8rem] bg-[right_0.25rem_center] bg-no-repeat"
              style="background-image: url('data:image/svg+xml,%3Csvg xmlns=%22http://www.w3.org/2000/svg%22 fill=%22none%22 viewBox=%220 0 24 24%22 stroke=%22white%22%3E%3Cpath stroke-linecap=%22round%22 stroke-linejoin=%22round%22 stroke-width=%222%22 d=%22M19 9l-7 7-7-7%22/%3E%3C/svg%3E')"
            >
              <option value="" disabled>工具</option>
              <option v-if="canToolMatchPreference" value="preference">匹配偏好</option>
              <option v-if="canToolTagMatching" value="tag">標籤匹配</option>
              <option v-if="canToolTechManagement" value="tech">技術管理</option>
            </select>
          </div>

          <router-link
            v-permission="'resource:group:selector:view'"
            to="/resource-group-selector"
            class="text-white hover:bg-indigo-500 px-2 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-colors whitespace-nowrap hidden lg:inline-block"
            active-class="bg-indigo-700"
          >
            资源组
          </router-link>

          <router-link
            v-if="showBackendEntry"
            to="/admin"
            class="text-white hover:bg-indigo-500 px-2 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-colors whitespace-nowrap"
            active-class="bg-indigo-700"
          >
            后台管理
          </router-link>

          <button
            @click="logout"
            class="text-white hover:bg-red-500 px-2 py-1.5 rounded-md text-xs lg:text-sm font-medium transition-colors whitespace-nowrap"
          >
            退出
          </button>
        </div>
      </div>
    </div>
  </nav>
</template>
