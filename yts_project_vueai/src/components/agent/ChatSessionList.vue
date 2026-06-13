<script setup lang="ts">
import { ref, computed } from 'vue'
import { useAgentChatStore } from '@/stores/agentChat'

const store = useAgentChatStore()
const searchQuery = ref('')
const showNewInput = ref(false)
const newName = ref('')

const filteredConversations = computed(() => {
  if (!searchQuery.value.trim()) return store.conversations
  const q = searchQuery.value.trim().toLowerCase()
  return store.conversations.filter(c => c.name.toLowerCase().includes(q))
})

function createNew() {
  const name = newName.value.trim()
  if (!name) return
  store.createConversation(name)
  newName.value = ''
  showNewInput.value = false
}

function getLastMessage(conv: typeof store.conversations[0]): string {
  if (conv.messages.length === 0) return ''
  const last = conv.messages[conv.messages.length - 1]
  const text = last.content.replace(/[*#\[\]`>|~]/g, '').trim()
  return text.length > 30 ? text.slice(0, 30) + '...' : text
}

function formatTime(ts: number): string {
  const d = new Date(ts)
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 172800000) return '昨天'
  return `${d.getMonth() + 1}/${d.getDate()}`
}
</script>

<template>
  <div class="w-[280px] h-full flex flex-col bg-gray-50 border-r border-gray-200">
    <!-- 搜索 -->
    <div class="shrink-0 px-3 pt-3 pb-2">
      <input
        v-model="searchQuery"
        placeholder="搜索会话..."
        class="w-full px-2.5 py-1.5 text-xs border border-gray-200 rounded-lg focus:outline-none focus:ring-1 focus:ring-indigo-400 bg-white"
      />
    </div>

    <!-- 新建 -->
    <div class="shrink-0 px-3 pb-2">
      <button
        class="w-full flex items-center justify-center gap-1 px-3 py-1.5 text-xs rounded-lg border border-dashed border-gray-300 text-gray-500 hover:border-indigo-400 hover:text-indigo-600 hover:bg-indigo-50 transition-colors"
        @click="showNewInput = !showNewInput"
      >
        <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
        </svg>
        新建会话
      </button>
    </div>

    <!-- 新建输入 -->
    <div v-if="showNewInput" class="shrink-0 px-3 pb-2">
      <div class="flex gap-1">
        <input
          v-model="newName"
          placeholder="输入会话名称"
          class="flex-1 px-2 py-1 text-xs border border-indigo-200 rounded focus:outline-none focus:ring-1 focus:ring-indigo-400"
          @keydown.enter="createNew"
        />
        <button
          class="px-2 py-1 text-xs bg-indigo-600 text-white rounded hover:bg-indigo-700 disabled:opacity-40"
          :disabled="!newName.trim()"
          @click="createNew"
        >确定</button>
      </div>
    </div>

    <!-- 列表 -->
    <div class="flex-1 overflow-y-auto">
      <div
        v-for="conv in filteredConversations"
        :key="conv.id"
        class="flex items-center gap-2.5 px-3 py-2.5 cursor-pointer transition-colors border-b border-gray-100 last:border-b-0"
        :class="conv.id === store.activeId ? 'bg-indigo-50 border-l-2 border-l-indigo-500' : 'hover:bg-gray-100 border-l-2 border-l-transparent'"
        @click="store.switchConversation(conv.id)"
      >
        <div class="w-8 h-8 rounded-full flex items-center justify-center text-base shrink-0 bg-white shadow-sm border border-gray-200">
          {{ conv.avatar }}
        </div>
        <div class="flex-1 min-w-0">
          <div class="flex items-center justify-between">
            <span class="text-sm font-medium text-gray-900 truncate">{{ conv.name }}</span>
            <span class="text-[10px] text-gray-400 shrink-0 ml-1">{{ formatTime(conv.updatedAt) }}</span>
          </div>
          <div class="text-[11px] text-gray-400 truncate mt-0.5">{{ getLastMessage(conv) }}</div>
        </div>
      </div>
    </div>
  </div>
</template>
