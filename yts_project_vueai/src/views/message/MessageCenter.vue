<template>
  <div class="min-h-screen bg-white py-6">
    <div class="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-6 flex justify-between items-center">
        <h1 class="text-2xl font-semibold text-gray-900">消息中心</h1>
        <button
          v-permission="'announcement:create'"
          @click="showPublishDialog = true"
          class="text-sm text-blue-600 hover:text-blue-700 font-medium"
        >
          + 发布公告
        </button>
      </div>

      <!-- Tab 切换 -->
      <div class="mb-4 border-b border-gray-200">
        <nav class="flex space-x-6">
          <button
            v-for="tab in tabs"
            :key="tab.key"
            @click="handleTabChange(tab.key)"
            :class="[
              'pb-3 px-1 text-sm font-medium border-b-2 transition-colors',
              activeTab === tab.key
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            ]"
          >
            {{ tab.label }}
          </button>
        </nav>
      </div>

      <!-- 筛选栏 -->
      <div class="mb-4 space-y-3">
        <div class="flex justify-between items-center">
          <div class="flex items-center space-x-4">
            <!-- 所有消息类型都支持已读/未读筛选 -->
            <select
              v-model="readFilter"
              @change="handleFilterChange"
              class="text-sm text-gray-700 border-0 bg-transparent cursor-pointer focus:outline-none focus:ring-0"
            >
              <option value="">全部</option>
              <option value="0">未读</option>
              <option value="1">已读</option>
            </select>
            <label class="flex items-center space-x-2 text-sm text-gray-700 cursor-pointer">
              <input
                type="checkbox"
                :checked="readFilter === '0'"
                @change="handleUnreadOnlyChange"
                class="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
              />
              <span>只看未读</span>
            </label>
            <button
              @click="markAllAsRead"
              class="text-sm text-blue-600 hover:text-blue-700 font-medium"
            >
              全部转为已读
            </button>
          </div>
        </div>
        <!-- 时间筛选 -->
        <div class="flex items-center space-x-4">
          <label class="text-sm text-gray-700">时间筛选：</label>
          <!-- 快捷选项 -->
          <button
            @click="setTimeRange('7days')"
            class="text-sm px-3 py-1 border border-gray-300 rounded hover:bg-gray-50 text-gray-700"
          >
            最近7天
          </button>
          <button
            @click="setTimeRange('30days')"
            class="text-sm px-3 py-1 border border-gray-300 rounded hover:bg-gray-50 text-gray-700"
          >
            最近30天
          </button>
          <span class="text-sm text-gray-400">|</span>
          <!-- 自定义日期选择 -->
          <input
            v-model="timeFilter.startTime"
            type="date"
            @change="handleFilterChange"
            class="text-sm px-3 py-1 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
            placeholder="开始日期"
          />
          <span class="text-sm text-gray-500">至</span>
          <input
            v-model="timeFilter.endTime"
            type="date"
            @change="handleFilterChange"
            class="text-sm px-3 py-1 border border-gray-300 rounded focus:outline-none focus:border-blue-500"
            placeholder="结束日期"
          />
          <button
            v-if="timeFilter.startTime || timeFilter.endTime"
            @click="clearTimeFilter"
            class="text-sm text-gray-600 hover:text-gray-700"
          >
            清除
          </button>
        </div>
      </div>

      <!-- 消息列表 -->
      <div class="bg-white border border-gray-200 rounded">
        <div v-if="loading" class="p-8 text-center">
          <div class="inline-block animate-spin rounded-full h-6 w-6 border-2 border-gray-300 border-t-blue-600"></div>
          <p class="mt-2 text-sm text-gray-500">加载中...</p>
        </div>
        <div v-else-if="messages.length === 0" class="p-8 text-center text-gray-500 text-sm">
          暂无消息
        </div>
        <div v-else class="divide-y divide-gray-200">
          <div
            v-for="message in messages"
            :key="message.id"
            :data-message-id="message.id"
            class="hover:bg-gray-50 transition-colors"
          >
            <div class="px-4 py-3 flex items-center justify-between">
              <div class="flex items-center flex-1 min-w-0">
                <!-- 未读消息的红色圆点（所有消息类型都显示） -->
                <div
                  v-if="message.readFlag === 0"
                  class="w-2 h-2 bg-red-600 rounded-full mr-3 flex-shrink-0"
                ></div>
                <!-- 已读消息：不显示圆点，但保留间距以对齐 -->
                <div
                  v-else
                  class="w-2 h-2 mr-3 flex-shrink-0"
                ></div>
                <div class="flex items-center space-x-2 flex-1 min-w-0">
                  <h3
                    :class="[
                      'text-sm truncate',
                      // 未读消息加粗
                      message.readFlag === 0 ? 'font-semibold text-gray-900' : 'font-normal text-gray-700'
                    ]"
                  >
                    {{ message.title }}
                  </h3>
                  <!-- 三天内的通知公告特殊标记（仅通知公告显示） -->
                  <span
                    v-if="message.msgCategory === '1' && isWithinThreeDays(message.createTime)"
                    class="px-2 py-0.5 text-xs font-medium bg-orange-100 text-orange-700 rounded-full flex-shrink-0"
                  >
                    新
                  </span>
                </div>
              </div>
              <div class="flex items-center space-x-3 ml-4 flex-shrink-0">
                <span class="text-xs text-gray-500">{{ formatTimeShort(message.createTime) }}</span>
                <button
                  v-if="message.id"
                  @click="toggleMessageContent(message.id)"
                  class="text-gray-400 hover:text-gray-600 transition-transform"
                  :class="{ 'rotate-180': expandedMessages.has(message.id) }"
                >
                  <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                  </svg>
                </button>
              </div>
            </div>
            <!-- 展开的消息内容 -->
            <div
              v-if="message.id && expandedMessages.has(message.id)"
              class="px-4 pb-3 border-t border-gray-100"
            >
              <div class="pt-3 text-sm text-gray-600 leading-relaxed whitespace-pre-wrap" v-html="message.msgContent"></div>
              <div class="mt-3 flex items-center justify-between text-xs text-gray-500">
                <span>发送人: {{ message.sender || '系统' }}</span>
                <div class="flex items-center space-x-3">
                  <!-- 只有系统消息才显示已读/未读状态 -->
                  <template v-if="activeTab === '2'">
                    <span v-if="message.readFlag === 1 && message.readTime" class="text-green-600">
                      已读于: {{ formatTimeShort(message.readTime) }}
                    </span>
                    <span v-else class="text-red-600">未读</span>
                  </template>
                  <!-- 管理员编辑按钮 -->
                  <button
                    v-permission="'announcement:edit'"
                    v-if="message.id"
                    @click="handleEdit(message)"
                    class="text-blue-600 hover:text-blue-700"
                  >
                    编辑
                  </button>

                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="total > 0" class="mt-4 flex justify-center">
        <div class="flex items-center space-x-2 text-sm text-gray-600">
          <button
            @click="changePage(currentPage - 1)"
            :disabled="currentPage === 1"
            class="px-3 py-1 disabled:opacity-50 disabled:cursor-not-allowed hover:text-gray-900"
          >
            上一页
          </button>
          <span>
            第 {{ currentPage }} 页，共 {{ Math.ceil(total / pageSize) }} 页
          </span>
          <button
            @click="changePage(currentPage + 1)"
            :disabled="currentPage >= Math.ceil(total / pageSize)"
            class="px-3 py-1 disabled:opacity-50 disabled:cursor-not-allowed hover:text-gray-900"
          >
            下一页
          </button>
        </div>
      </div>

      <!-- 消息详情对话框 -->
      <div
        v-if="showDetailDialog"
        class="fixed inset-0 bg-black bg-opacity-60 backdrop-blur-sm flex items-center justify-center z-50 p-4"
        @click="showDetailDialog = false"
      >
        <div
          class="bg-white rounded-2xl shadow-2xl max-w-3xl w-full max-h-[85vh] overflow-hidden flex flex-col"
          @click.stop
        >
          <div class="bg-gradient-to-r from-blue-500 to-indigo-600 px-6 py-4 flex justify-between items-center">
            <h2 class="text-2xl font-bold text-white">{{ currentMessage?.title }}</h2>
            <button
              @click="showDetailDialog = false"
              class="text-white hover:bg-white/20 rounded-full p-2 transition-colors"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          <div class="p-6 overflow-y-auto flex-1">
            <div class="bg-gray-50 rounded-lg p-4 mb-6 space-y-2">
              <div class="flex items-center space-x-2 text-gray-700">
                <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0M4 7a4 4 0 108 0M4 7a4 4 0 108 0m-4 4v6m0 0v-6m0 6h6m-6 0H4"></path>
                </svg>
                <span class="font-medium">发送人:</span>
                <span>{{ currentMessage?.sender || '系统' }}</span>
              </div>
              <div class="flex items-center space-x-2 text-gray-700">
                <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                </svg>
                <span class="font-medium">发布时间:</span>
                <span>{{ formatTime(currentMessage?.createTime) }}</span>
              </div>
              <!-- 只有系统消息才显示已读/未读状态 -->
              <template v-if="activeTab === '2'">
                <div v-if="currentMessage?.readFlag === 1 && currentMessage?.readTime" class="flex items-center space-x-2 text-green-600">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                  </svg>
                  <span class="font-medium">已读于:</span>
                  <span>{{ formatTime(currentMessage.readTime) }}</span>
                </div>
                <div v-else class="flex items-center space-x-2 text-red-600">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                  </svg>
                  <span class="font-medium">未读</span>
                </div>
              </template>
            </div>
            <div class="prose max-w-none text-gray-700 leading-relaxed" v-html="currentMessage?.msgContent"></div>
          </div>
          <div class="px-6 py-4 bg-gray-50 border-t border-gray-200 flex justify-end space-x-3">
            <button
              v-if="currentMessage?.busType && currentMessage?.busId"
              @click="navigateToBusiness(currentMessage.busType, currentMessage.busId)"
              class="bg-gradient-to-r from-green-500 to-emerald-600 text-white px-6 py-2.5 rounded-lg hover:from-green-600 hover:to-emerald-700 transition-all duration-200 shadow-md hover:shadow-lg font-medium"
            >
              查看业务详情
            </button>
            <button
              @click="showDetailDialog = false"
              class="px-6 py-2.5 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors font-medium"
            >
              关闭
            </button>
          </div>
        </div>
      </div>

      <!-- 发布公告对话框 -->
      <div
        v-if="showPublishDialog"
        class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4"
        @click="handleClosePublishDialog"
      >
        <div
          class="bg-white border border-gray-200 max-w-5xl w-full max-h-[90vh] overflow-hidden flex flex-col"
          @click.stop
        >
          <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
            <h2 class="text-lg font-semibold text-gray-900">{{ editingMessageId ? '编辑' : '新增' }}</h2>
            <button
              @click="handleClosePublishDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          <div class="p-6 overflow-y-auto flex-1">
            <form @submit.prevent="handlePublish" class="space-y-4">
              <!-- 消息类型 -->
              <div>
                <label class="block text-sm text-gray-700 mb-2">
                  消息类型 <span class="text-red-500">*</span>
                </label>
                <div class="flex space-x-6">
                  <label class="flex items-center">
                    <input
                      type="radio"
                      v-model="publishForm.msgCategory"
                      value="2"
                      class="mr-2"
                    />
                    <span class="text-sm text-gray-700">系统消息</span>
                  </label>
                  <label class="flex items-center">
                    <input
                      type="radio"
                      v-model="publishForm.msgCategory"
                      value="1"
                      class="mr-2"
                    />
                    <span class="text-sm text-gray-700">通知公告</span>
                  </label>
                </div>
              </div>

              <!-- 通告标题 -->
              <div>
                <label class="block text-sm text-gray-700 mb-1">
                  通告标题 <span class="text-red-500">*</span>
                </label>
                <input
                  v-model="publishForm.title"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:border-blue-500"
                  placeholder="请输入标题"
                />
              </div>

              <!-- 两列布局 -->
              <div class="grid grid-cols-2 gap-6">
                <!-- 左列 -->
                <div class="space-y-4">
                  <!-- 接收用户 -->
                  <div>
                    <label class="block text-sm text-gray-700 mb-2">
                      接收用户 <span class="text-red-500">*</span>
                    </label>
                    <div class="flex space-x-6">
                      <label class="flex items-center">
                        <input
                          type="radio"
                          v-model="publishForm.msgType"
                          value="ALL"
                          @change="handleScopeChange"
                          class="mr-2"
                        />
                        <span class="text-sm text-gray-700">全体用户</span>
                      </label>
                      <label class="flex items-center">
                        <input
                          type="radio"
                          v-model="publishForm.msgType"
                          value="USER"
                          @change="handleScopeChange"
                          class="mr-2"
                        />
                        <span class="text-sm text-gray-700">指定用户</span>
                      </label>
                    </div>
                  </div>


                  <!-- 指定用户选择 -->
                  <div v-if="publishForm.msgType === 'USER'">
                    <label class="block text-sm text-gray-700 mb-1">
                      指定用户 <span class="text-red-500">*</span>
                    </label>
                    <div class="flex space-x-2">
                      <input
                        v-model="publishForm.userIds"
                        type="text"
                        class="flex-1 px-3 py-2 border border-gray-300 focus:outline-none focus:border-blue-500"
                        placeholder="请选择指定用户"
                      />
                      <button
                        type="button"
                        @click="showUserSelector = true"
                        class="px-4 py-2 border border-gray-300 hover:bg-gray-50 text-sm"
                      >
                        选择
                      </button>
                    </div>
                  </div>

                  <!-- 优先级别 -->
                  <div>
                    <label class="block text-sm text-gray-700 mb-2">
                      优先级别
                    </label>
                    <div class="flex space-x-6">
                      <label class="flex items-center">
                        <input
                          type="radio"
                          v-model="publishForm.priority"
                          value="HIGH"
                          class="mr-2"
                        />
                        <span class="text-sm text-gray-700">高</span>
                      </label>
                      <label class="flex items-center">
                        <input
                          type="radio"
                          v-model="publishForm.priority"
                          value="NORMAL"
                          class="mr-2"
                        />
                        <span class="text-sm text-gray-700">中</span>
                      </label>
                      <label class="flex items-center">
                        <input
                          type="radio"
                          v-model="publishForm.priority"
                          value="LOW"
                          class="mr-2"
                        />
                        <span class="text-sm text-gray-700">低</span>
                      </label>
                    </div>
                  </div>
                </div>

                <!-- 右列 -->
                <div class="space-y-4">
                  <!-- 是否置顶 -->
                  <div>
                    <label class="block text-sm text-gray-700 mb-2">
                      是否置顶
                    </label>
                    <div class="flex items-center space-x-2">
                      <label class="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          v-model="publishForm.isPinned"
                          class="sr-only peer"
                        />
                        <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
                        <span class="ml-3 text-sm text-gray-700">{{ publishForm.isPinned ? '是' : '否' }}</span>
                      </label>
                    </div>
                  </div>

                  <!-- 公告模版 -->
                  <div>
                    <label class="block text-sm text-gray-700 mb-1">
                      公告模版
                    </label>
                    <select
                      v-model="publishForm.templateId"
                      class="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:border-blue-500 bg-white"
                    >
                      <option value="">请选择消息模版</option>
                      <!-- 模版选项将在后续实现 -->
                    </select>
                  </div>

                  <!-- 发送人（只读） -->
                  <div>
                    <label class="block text-sm text-gray-700 mb-1">
                      发送人
                    </label>
                    <input
                      :value="authStore.userInfo?.username || '系统'"
                      type="text"
                      readonly
                      disabled
                      class="w-full px-3 py-2 border border-gray-300 bg-gray-50 text-gray-600 cursor-not-allowed"
                    />
                  </div>

                  <!-- 开始时间和结束时间（仅通知公告显示） -->
                  <div v-if="publishForm.msgCategory === '1'" class="grid grid-cols-2 gap-4">
                    <div>
                      <label class="block text-sm text-gray-700 mb-1">
                        开始时间
                      </label>
                      <input
                        v-model="publishForm.startTime"
                        type="datetime-local"
                        class="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:border-blue-500"
                      />
                    </div>
                    <div>
                      <label class="block text-sm text-gray-700 mb-1">
                        结束时间
                      </label>
                      <input
                        v-model="publishForm.endTime"
                        type="datetime-local"
                        class="w-full px-3 py-2 border border-gray-300 focus:outline-none focus:border-blue-500"
                      />
                    </div>
                  </div>
                </div>
              </div>

              <!-- 通告内容（富文本编辑器） -->
              <div>
                <label class="block text-sm text-gray-700 mb-1">
                  通告内容 <span class="text-red-500">*</span>
                </label>
                <div class="border border-gray-300 rounded quill-editor-container">
                  <QuillEditor
                    v-model:content="publishForm.msgContent"
                    contentType="html"
                    :options="editorOptions"
                  />
                </div>
              </div>
            </form>
          </div>
          <div class="px-6 py-4 border-t border-gray-200 flex justify-end space-x-3">
            <button
              type="button"
              @click="handleClosePublishDialog"
              class="px-4 py-2 text-sm text-gray-700 hover:text-gray-900"
            >
              取消
            </button>
            <button
              type="button"
              @click="editingMessageId ? handleUpdate() : handlePublish()"
              :disabled="publishing"
              class="px-4 py-2 text-sm bg-green-600 text-white hover:bg-green-700 disabled:opacity-50"
            >
              {{ publishing ? (editingMessageId ? '更新中...' : '发布中...') : (editingMessageId ? '更新' : '确认') }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAnnouncementStore } from '@/stores/announcement';
import { useAuthStore } from '@/stores/auth';
import { usePermissionStore } from '@/stores/permission';
import { announcementApi } from '@/api/modules/announcement';
import type { SysAnnouncement } from '@/api/modules/announcement';
import { QuillEditor } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';

const router = useRouter();
const route = useRoute();
const announcementStore = useAnnouncementStore();
const authStore = useAuthStore();
const permissionStore = usePermissionStore();

const isAdmin = computed(() => permissionStore.isAdmin);

const tabs = [
  { key: '1', label: '通知公告' },
  { key: '2', label: '系统消息' }
];

const activeTab = ref('1');
const readFilter = ref('');
const timeFilter = ref({
  startTime: '',
  endTime: ''
});
const messages = ref<SysAnnouncement[]>([]);
const selectedMessages = ref<string[]>([]);
const loading = ref(false);
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const showDetailDialog = ref(false);
const currentMessage = ref<SysAnnouncement | null>(null);
const showPublishDialog = ref(false);
const publishing = ref(false);
const isAdminMode = ref(false);
const expandedMessages = ref<Set<string>>(new Set());
const editingMessageId = ref<string | null>(null);

const publishForm = ref({
  title: '',
  msgContent: '',
  msgCategory: '1', // 1-通知公告, 2-系统消息
  msgType: 'ALL', // ALL-全体用户, USER-指定用户
  priority: 'NORMAL', // LOW-低, NORMAL-中, HIGH-高
  sender: '',
  userIds: '',
  startTime: '',
  endTime: '',
  busType: '',
  busId: '',
    isPinned: false, // 是否置顶
    templateId: '' // 公告模版ID
});

const showUserSelector = ref(false);

// 富文本编辑器配置
const editorOptions = {
  placeholder: '请输入通告内容',
  theme: 'snow',
  modules: {
    toolbar: [
      [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
      [{ 'size': ['small', false, 'large', 'huge'] }],
      ['bold', 'italic', 'underline', 'strike'],
      [{ 'color': [] }, { 'background': [] }],
      [{ 'list': 'ordered'}, { 'list': 'bullet' }],
      [{ 'align': [] }],
      ['link', 'image'],
      ['clean']
    ]
  }
};

const loadMessages = async () => {
  if (!authStore.userInfo?.id) {
    return;
  }
  loading.value = true;
  try {
    // 修复：正确处理 readFlag 参数，空字符串时传递 undefined，否则转换为数字
    let readFlagValue: number | undefined = undefined;
    if (readFilter.value !== '' && readFilter.value !== null) {
      const parsed = parseInt(readFilter.value);
      if (!isNaN(parsed)) {
        readFlagValue = parsed;
      }
    }
    
    const result = await announcementStore.fetchMessages({
      userId: authStore.userInfo.id,
      msgCategory: activeTab.value,
      readFlag: readFlagValue,
      pageNo: currentPage.value,
      pageSize: pageSize.value
    });
    if (result) {
      messages.value = result.records;
      total.value = result.total;
    }
  } catch (error) {
    console.error('加载消息失败:', error);
  } finally {
    loading.value = false;
  }
};

// markAsRead 函数已移除，现在只在查看详情时自动标记为已读

const batchMarkAsRead = async () => {
  if (selectedMessages.value.length === 0) {
    return;
  }
  try {
    await announcementStore.batchMarkAsRead(selectedMessages.value);
    selectedMessages.value = [];
    await loadMessages();
    await announcementStore.fetchUnreadCount();
  } catch (error) {
    console.error('批量标记已读失败:', error);
  }
};

const deleteMessage = async (id: string) => {
  if (!confirm('确定要删除这条消息吗？')) {
    return;
  }
  try {
    await announcementStore.deleteMessage(id);
    await loadMessages();
    await announcementStore.fetchUnreadCount();
  } catch (error) {
    console.error('删除消息失败:', error);
  }
};

const viewDetail = async (message: SysAnnouncement) => {
  currentMessage.value = message;
  showDetailDialog.value = true;
  // 所有消息类型都需要标记为已读
  if ((message.readFlag === 0 || message.readFlag === undefined) && message.id) {
    try {
      await announcementStore.markAsRead(message.id);
      // 更新本地状态
      message.readFlag = 1;
      message.readTime = new Date().toISOString();
      // 同时更新 currentMessage 的状态
      if (currentMessage.value) {
        currentMessage.value.readFlag = 1;
        currentMessage.value.readTime = new Date().toISOString();
      }
      // 更新未读数量
      await announcementStore.fetchUnreadCount();
      // 如果当前在未读筛选模式下，需要刷新列表
      if (readFilter.value === '0') {
        await loadMessages();
      }
    } catch (error) {
      console.error('标记已读失败:', error);
      alert('标记已读失败，请稍后重试');
    }
  }
};

const navigateToBusiness = (busType: string, busId: string) => {
  const route = announcementStore.navigateToBusiness(busType, busId);
  if (route) {
    router.push(route);
  } else {
    alert('暂不支持此业务类型的跳转');
  }
};

const changePage = (page: number) => {
  currentPage.value = page;
  if (isAdminMode.value) {
    loadAdminMessages();
  } else {
    loadMessages();
  }
};

const handleTabChange = (tabKey: string) => {
  activeTab.value = tabKey;
  currentPage.value = 1; // 重置到第一页
  // 切换标签时不清空筛选，保持用户的筛选状态
  if (isAdminMode.value) {
    loadAdminMessages();
  } else {
    loadMessages();
  }
};

const handleFilterChange = () => {
  currentPage.value = 1; // 重置到第一页
  if (isAdminMode.value) {
    loadAdminMessages();
  } else {
    loadMessages();
  }
};

const setTimeRange = (range: string) => {
  const today = new Date();
  const endDate = new Date(today);
  endDate.setHours(23, 59, 59, 999); // 设置为今天的结束时间
  
  let startDate = new Date(today);
  
  if (range === '7days') {
    startDate.setDate(today.getDate() - 7);
  } else if (range === '30days') {
    startDate.setDate(today.getDate() - 30);
  }
  
  startDate.setHours(0, 0, 0, 0); // 设置为开始日期的开始时间
  
  // 格式化为 YYYY-MM-DD 格式
  timeFilter.value.startTime = startDate.toISOString().split('T')[0];
  timeFilter.value.endTime = endDate.toISOString().split('T')[0];
  
  handleFilterChange();
};

const clearTimeFilter = () => {
  timeFilter.value.startTime = '';
  timeFilter.value.endTime = '';
  handleFilterChange();
};

const handleRefresh = () => {
  if (isAdminMode.value) {
    loadAdminMessages();
  } else {
    loadMessages();
  }
  announcementStore.fetchUnreadCount();
};

const formatTime = (time?: string) => {
  if (!time) return '';
  return new Date(time).toLocaleString('zh-CN');
};

const formatTimeShort = (time?: string) => {
  if (!time) return '';
  const date = new Date(time);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}`;
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

const handleUnreadOnlyChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.checked) {
    readFilter.value = '0';
  } else {
    readFilter.value = '';
  }
  handleFilterChange();
};

const markAllAsRead = async () => {
  if (!authStore.userInfo?.id) return;
  
  // 获取所有未读消息的ID（所有消息类型都支持）
  const unreadMessages = messages.value.filter(
    msg => msg.readFlag === 0 || msg.readFlag === undefined
  );
  
  if (unreadMessages.length === 0) {
    alert('没有未读消息');
    return;
  }
  
  if (!confirm(`确定要将 ${unreadMessages.length} 条未读消息全部标记为已读吗？`)) {
    return;
  }
  
  try {
    const messageIds = unreadMessages.map(msg => msg.id!).filter(Boolean);
    if (messageIds.length > 0) {
      await announcementStore.batchMarkAsRead(messageIds);
      await loadMessages();
      await announcementStore.fetchUnreadCount();
    }
  } catch (error) {
    console.error('批量标记已读失败:', error);
    alert('批量标记已读失败，请稍后重试');
  }
};

const toggleMessageContent = async (messageId: string) => {
  if (expandedMessages.value.has(messageId)) {
    // 收起
    expandedMessages.value.delete(messageId);
  } else {
    // 展开
    expandedMessages.value.add(messageId);
    
    // 找到对应的消息并标记为已读（所有消息类型都支持）
    const message = messages.value.find(m => m.id === messageId);
    if (message && (message.readFlag === 0 || message.readFlag === undefined) && message.id) {
      try {
        await announcementStore.markAsRead(message.id);
        // 更新本地状态
        message.readFlag = 1;
        message.readTime = new Date().toISOString();
        // 更新未读数量
        await announcementStore.fetchUnreadCount();
        // 如果当前在未读筛选模式下，需要刷新列表
        if (readFilter.value === '0') {
          await loadMessages();
        }
      } catch (error) {
        console.error('标记已读失败:', error);
      }
    }
  }
};

const handleScopeChange = () => {
  if (publishForm.value.msgType === 'ALL') {
    publishForm.value.userIds = '';
  }
};

const handleSaveDraft = async () => {
  if (!publishForm.value.title || !publishForm.value.msgContent) {
    alert('请填写标题和内容');
    return;
  }

  publishing.value = true;
  try {
    const announcement: SysAnnouncement = {
      title: publishForm.value.title,
      msgContent: publishForm.value.msgContent,
      msgCategory: publishForm.value.msgCategory,
      msgType: publishForm.value.msgType,
      priority: publishForm.value.priority,
      sender: authStore.userInfo?.username || '系统', // 使用当前登录用户
      userIds: publishForm.value.msgType === 'USER' ? publishForm.value.userIds : undefined,
      // 只有通知公告（msgCategory='1'）才设置开始和结束时间
      startTime: publishForm.value.msgCategory === '1' ? (publishForm.value.startTime || undefined) : undefined,
      endTime: publishForm.value.msgCategory === '1' ? (publishForm.value.endTime || undefined) : undefined,
      busType: undefined, // 不再使用
      busId: undefined, // 不再使用
      sendStatus: 0  // 草稿状态
    };

    const res = await announcementApi.add(announcement);
    if (res.success) {
      alert('草稿保存成功！');
      showPublishDialog.value = false;
      resetPublishForm();
      if (isAdminMode.value) {
        loadAdminMessages();
      }
    } else {
      alert(res.message || '保存失败');
    }
  } catch (error: any) {
    console.error('保存草稿失败:', error);
    alert(error.response?.data?.message || '保存草稿失败');
  } finally {
    publishing.value = false;
  }
};

const handlePublish = async () => {
  if (!publishForm.value.title || !publishForm.value.msgContent) {
    alert('请填写标题和内容');
    return;
  }

  publishing.value = true;
  try {
    // 先创建消息
    const announcement: SysAnnouncement = {
      title: publishForm.value.title,
      msgContent: publishForm.value.msgContent,
      msgCategory: publishForm.value.msgCategory,
      msgType: publishForm.value.msgType,
      priority: publishForm.value.priority,
      sender: authStore.userInfo?.username || '系统', // 使用当前登录用户
      userIds: publishForm.value.msgType === 'USER' ? publishForm.value.userIds : undefined,
      // 只有通知公告（msgCategory='1'）才设置开始和结束时间
      startTime: publishForm.value.msgCategory === '1' ? (publishForm.value.startTime || undefined) : undefined,
      endTime: publishForm.value.msgCategory === '1' ? (publishForm.value.endTime || undefined) : undefined,
      busType: undefined, // 不再使用
      busId: undefined, // 不再使用
      sendStatus: 0  // 先保存为草稿
    };

    const addRes = await announcementApi.add(announcement);
    if (!addRes.success || !addRes.result?.id) {
      throw new Error(addRes.message || '创建消息失败');
    }

    // 然后发布消息
    const releaseRes = await announcementApi.doRelease(addRes.result.id);
    if (releaseRes.success) {
      alert(`发布成功！已推送给 ${releaseRes.result?.deliveryCount || 0} 位用户`);
      showPublishDialog.value = false;
      resetPublishForm();
      await loadMessages();
      await announcementStore.fetchUnreadCount();
      if (isAdminMode.value) {
        loadAdminMessages();
      }
    } else {
      throw new Error(releaseRes.message || '发布失败');
    }
  } catch (error: any) {
    console.error('发布公告失败:', error);
    alert(error.response?.data?.message || error.message || '发布公告失败');
  } finally {
    publishing.value = false;
  }
};

const handleEdit = async (message: SysAnnouncement) => {
  if (!message.id) return;
  
  // 加载消息详情
  try {
    const res = await announcementApi.queryById(message.id);
    if (res.success && res.result) {
      const msg = res.result;
      // 填充表单
      publishForm.value = {
        title: msg.title || '',
        msgContent: msg.msgContent || '',
        msgCategory: msg.msgCategory || '1',
        msgType: msg.msgType || 'ALL',
        priority: msg.priority || 'NORMAL',
        sender: msg.sender || '',
        userIds: msg.userIds || '',
        startTime: msg.startTime ? new Date(msg.startTime).toISOString().slice(0, 16) : '',
        endTime: msg.endTime ? new Date(msg.endTime).toISOString().slice(0, 16) : '',
        busType: msg.busType || '',
        busId: msg.busId || '',
        isPinned: false,
        templateId: ''
      };
      editingMessageId.value = message.id;
      showPublishDialog.value = true;
    }
  } catch (error: any) {
    console.error('加载消息详情失败:', error);
    alert('加载消息详情失败');
  }
};

const handleUpdate = async () => {
  if (!editingMessageId.value) return;
  if (!publishForm.value.title || !publishForm.value.msgContent) {
    alert('请填写标题和内容');
    return;
  }

  publishing.value = true;
  try {
    const announcement: SysAnnouncement = {
      id: editingMessageId.value,
      title: publishForm.value.title,
      msgContent: publishForm.value.msgContent,
      msgCategory: publishForm.value.msgCategory,
      msgType: publishForm.value.msgType,
      priority: publishForm.value.priority,
      sender: authStore.userInfo?.username || '系统',
      userIds: publishForm.value.msgType === 'USER' ? publishForm.value.userIds : undefined,
      startTime: publishForm.value.msgCategory === '1' ? (publishForm.value.startTime || undefined) : undefined,
      endTime: publishForm.value.msgCategory === '1' ? (publishForm.value.endTime || undefined) : undefined,
      busType: undefined,
      busId: undefined
    };

    const res = await announcementApi.edit(announcement);
    if (res.success) {
      alert('更新成功！');
      showPublishDialog.value = false;
      editingMessageId.value = null;
      resetPublishForm();
      await loadMessages();
      if (isAdminMode.value) {
        loadAdminMessages();
      }
    } else {
      throw new Error(res.message || '更新失败');
    }
  } catch (error: any) {
    console.error('更新公告失败:', error);
    alert(error.response?.data?.message || error.message || '更新公告失败');
  } finally {
    publishing.value = false;
  }
};

const handleClosePublishDialog = () => {
  showPublishDialog.value = false;
  editingMessageId.value = null;
  resetPublishForm();
};

const resetPublishForm = () => {
  publishForm.value = {
    title: '',
    msgContent: '',
    msgCategory: '1',
    msgType: 'ALL',
    priority: 'NORMAL',
    sender: '', // 不再使用，实际使用 authStore.userInfo?.username
    userIds: '',
    startTime: '',
    endTime: '',
    busType: '', // 不再使用
    busId: '', // 不再使用
    isPinned: false,
    templateId: ''
  };
  editingMessageId.value = null;
};

const loadAdminMessages = async () => {
  if (!authStore.userInfo?.id) {
    return;
  }
  isAdminMode.value = true;
  loading.value = true;
  try {
    // 只有系统消息（msgCategory='2'）才需要 readFlag 筛选
    // 通知公告不需要已读/未读功能
    let readFlagValue: number | undefined = undefined;
    if (activeTab.value === '2' && readFilter.value !== '' && readFilter.value !== null) {
      const parsed = parseInt(readFilter.value);
      if (!isNaN(parsed)) {
        readFlagValue = parsed;
      }
    }
    
    const result = await announcementStore.fetchMessages({
      userId: authStore.userInfo.id,
      msgCategory: activeTab.value,
      readFlag: readFlagValue,
      pageNo: currentPage.value,
      pageSize: pageSize.value
    });
    if (result) {
      messages.value = result.records;
      total.value = result.total;
    }
  } catch (error) {
    console.error('加载管理员消息列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 根据路由查询参数自动展开消息内容
const openMessageById = async (messageId: string) => {
  // 先尝试在当前消息列表中找到该消息
  let message = messages.value.find(m => m.id === messageId);
  
  if (message) {
    // 如果消息在当前列表中，直接展开
    if (!expandedMessages.value.has(messageId)) {
      await toggleMessageContent(messageId);
    }
    // 清除查询参数，避免刷新时重复打开
    router.replace({ path: route.path, query: {} });
    // 滚动到该消息位置
    setTimeout(() => {
      const element = document.querySelector(`[data-message-id="${messageId}"]`);
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
    }, 100);
  } else {
    // 如果消息不在当前列表中，需要加载它
    try {
      const res = await announcementApi.queryById(messageId);
      if (res.success && res.result) {
        const msg = res.result;
        // 根据消息类型切换到对应的标签页
        if (msg.msgCategory && activeTab.value !== msg.msgCategory) {
          activeTab.value = msg.msgCategory;
          await handleTabChange(msg.msgCategory);
        }
        // 等待列表加载完成后，再次尝试展开
        setTimeout(async () => {
          message = messages.value.find(m => m.id === messageId);
          if (message) {
            if (!expandedMessages.value.has(messageId)) {
              await toggleMessageContent(messageId);
            }
            // 滚动到该消息位置
            setTimeout(() => {
              const element = document.querySelector(`[data-message-id="${messageId}"]`);
              if (element) {
                element.scrollIntoView({ behavior: 'smooth', block: 'center' });
              }
            }, 100);
          }
        }, 500);
        // 清除查询参数
        router.replace({ path: route.path, query: {} });
      }
    } catch (error) {
      console.error('加载消息失败:', error);
    }
  }
};

onMounted(async () => {
  await permissionStore.initPermissions();
  if (isAdmin.value) {
    // 管理员默认显示管理视图
    loadAdminMessages();
  } else {
    loadMessages();
  }
  announcementStore.fetchUnreadCount();
  
  // 检查路由查询参数，如果有 messageId，自动打开详情
  const messageId = route.query.messageId as string;
  if (messageId) {
    // 等待消息列表加载完成后再打开详情
    setTimeout(() => {
      openMessageById(messageId);
    }, 500);
  }
});

// 监听路由变化，如果查询参数中有 messageId，自动打开详情
watch(() => route.query.messageId, (messageId) => {
  if (messageId && typeof messageId === 'string') {
    setTimeout(() => {
      openMessageById(messageId);
    }, 300);
  }
});
</script>

<style scoped>
.quill-editor-container :deep(.ql-container) {
  min-height: 300px;
  max-height: 400px;
  overflow-y: auto;
}

.quill-editor-container :deep(.ql-editor) {
  min-height: 300px;
}
</style>

