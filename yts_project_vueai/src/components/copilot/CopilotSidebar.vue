<script setup lang="ts">
import { ref, watch, nextTick, computed } from 'vue';
import { useCopilotStore } from '@/stores/copilot';
import CopilotChat from './CopilotChat.vue';
import type { AgentRole } from '@/types/copilot';
import { ROLE_LABELS, GUIDE_STAGES } from '@/types/copilot';

const store = useCopilotStore();
const chatContainer = ref<HTMLDivElement | null>(null);
const inputText = ref('');

const roles: { key: AgentRole; emoji: string }[] = [
  { key: 'general', emoji: '💬' },
  { key: 'matching', emoji: '🎯' },
  { key: 'guide', emoji: '🧭' },
];

const guideProgress = computed(() => {
  if (store.agentRole !== 'guide' || store.stage === 'idle') return null;
  const total = GUIDE_STAGES.length;
  const current = Math.min(store.guideStepIndex, total);
  return { current, total };
});

function scrollToBottom() {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
    }
  });
}

watch(() => store.messages.length, () => scrollToBottom());

function handleSend() {
  const text = inputText.value.trim();
  if (!text || store.processing) return;
  inputText.value = '';
  store.sendMessage(text);
  scrollToBottom();
}

function handleKeydown(e: KeyboardEvent) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault();
    handleSend();
  }
}

function handleSuggestion(text: string) {
  store.sendMessage(text);
}
</script>

<template>
  <div
    class="h-full bg-white border-l border-gray-200 transition-all duration-300 overflow-hidden flex flex-col"
    :class="store.collapsed ? 'w-12' : 'w-full'"
  >
    <!-- 折叠状态 -->
    <template v-if="store.collapsed">
      <div class="flex flex-col items-center h-full py-3 gap-3">
        <button
          v-for="r in roles" :key="r.key"
          class="w-9 h-9 flex items-center justify-center rounded-lg hover:bg-gray-100 text-lg"
          :title="ROLE_LABELS[r.key]"
          :class="{ 'ring-2 ring-indigo-400': store.agentRole === r.key }"
          @click="store.setAgentRole(r.key)"
        >
          {{ r.emoji }}
        </button>
        <div class="mt-auto">
          <button
            class="w-8 h-8 flex items-center justify-center rounded-lg hover:bg-gray-100 text-gray-500 text-sm"
            title="展开 AI 助手"
            @click="store.toggleCollapse()"
          >
            ◀
          </button>
        </div>
      </div>
    </template>

    <!-- 展开状态 -->
    <template v-else>
      <!-- 标题栏 + Agent 状态 -->
      <div class="shrink-0 border-b border-gray-100">
        <div class="flex items-center justify-between px-4 py-3">
          <div class="flex items-center gap-2">
            <span class="relative flex h-2.5 w-2.5">
              <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-green-400 opacity-75"></span>
              <span class="relative inline-flex rounded-full h-2.5 w-2.5 bg-green-500"></span>
            </span>
            <h3 class="text-sm font-semibold text-gray-800">AI 助手</h3>
          </div>
          <button
            class="w-7 h-7 flex items-center justify-center rounded hover:bg-gray-100 text-gray-400 hover:text-gray-600 text-xs"
            @click="store.toggleCollapse()"
          >
            ▶
          </button>
        </div>
        <!-- Agent 角色 tabs -->
        <div class="flex px-3 pb-3 gap-1">
          <button
            v-for="r in roles"
            :key="r.key"
            class="flex-1 py-1.5 rounded-md text-xs font-medium transition-colors"
            :class="store.agentRole === r.key
              ? 'bg-indigo-100 text-indigo-700'
              : 'bg-gray-50 text-gray-500 hover:bg-gray-100'"
            @click="store.setAgentRole(r.key)"
          >
            {{ r.emoji }} {{ ROLE_LABELS[r.key] }}
          </button>
        </div>
      </div>

      <!-- 引导进度条（含步骤明细） -->
      <div v-if="guideProgress" class="shrink-0 px-3 py-2 border-b border-gray-100">
        <div class="flex items-center gap-1.5 mb-2">
          <span class="text-xs font-medium text-indigo-600">工藝選型引導</span>
          <span class="text-xs text-gray-400">{{ guideProgress.current }}/{{ guideProgress.total }}</span>
        </div>
        <!-- 步骤列表 -->
        <div class="flex flex-col gap-1">
          <div
            v-for="(g, idx) in GUIDE_STAGES"
            :key="g.stage"
            class="flex items-center gap-2 text-xs"
            :class="
              idx < store.guideStepIndex ? 'text-green-600' :
              idx === store.guideStepIndex ? 'text-indigo-600 font-medium' :
              'text-gray-300'
            "
          >
            <span class="shrink-0 w-4 h-4 rounded-full flex items-center justify-center text-[10px] border"
              :class="
                idx < store.guideStepIndex ? 'bg-green-500 border-green-500 text-white' :
                idx === store.guideStepIndex ? 'border-indigo-400 text-indigo-600' :
                'border-gray-300 text-gray-300'
              "
            >
              <template v-if="idx < store.guideStepIndex">✓</template>
              <template v-else>{{ idx + 1 }}</template>
            </span>
            <span>{{ g.label }}</span>
          </div>
        </div>
        <div class="w-full h-1 bg-gray-100 rounded-full overflow-hidden mt-2">
          <div
            class="h-full bg-indigo-500 rounded-full transition-all duration-500"
            :style="{ width: `${(guideProgress.current / guideProgress.total) * 100}%` }"
          />
        </div>
      </div>

      <!-- 快速操作面板 -->
      <div class="flex gap-2 px-3 py-3 border-b border-gray-100 shrink-0">
        <button
          class="flex-1 flex items-center justify-center gap-1.5 px-3 py-2 rounded-lg border border-gray-200 bg-white hover:bg-orange-50 hover:border-orange-200 transition-colors text-sm text-gray-700"
          @click="store.sendMessage('推荐热门产品')"
        >
          <span class="text-base">🔥</span>
          <span>热门推荐</span>
        </button>
        <button
          class="flex-1 flex items-center justify-center gap-1.5 px-3 py-2 rounded-lg border border-gray-200 bg-white hover:bg-green-50 hover:border-green-200 transition-colors text-sm text-gray-700"
          @click="store.sendMessage('帮我匹配物料')"
        >
          <span class="text-base">🎯</span>
          <span>物料匹配</span>
        </button>
        <button
          class="flex-1 flex items-center justify-center gap-1.5 px-3 py-2 rounded-lg border border-gray-200 bg-white hover:bg-blue-50 hover:border-blue-200 transition-colors text-sm text-gray-700"
          @click="store.sendMessage('使用技巧')"
        >
          <span class="text-base">💡</span>
          <span>使用技巧</span>
        </button>
      </div>

      <!-- 聊天区 -->
      <div
        ref="chatContainer"
        class="flex-1 overflow-y-auto py-3 px-3 space-y-3 scroll-smooth"
      >
        <CopilotChat
          v-for="msg in store.messages"
          :key="msg.id"
          :message="msg"
          @suggestion-click="handleSuggestion"
        />

        <div v-if="store.processing" class="flex items-center gap-2 px-1">
          <span class="w-1.5 h-1.5 rounded-full bg-blue-400 animate-bounce" style="animation-delay:0ms"></span>
          <span class="w-1.5 h-1.5 rounded-full bg-blue-400 animate-bounce" style="animation-delay:150ms"></span>
          <span class="w-1.5 h-1.5 rounded-full bg-blue-400 animate-bounce" style="animation-delay:300ms"></span>
        </div>
      </div>

      <!-- 快捷提问 -->
      <div class="flex gap-1.5 px-3 py-2 border-b border-gray-100 shrink-0 flex-wrap">
        <button
          v-for="q in ['🔥 熱門推薦', '🎯 幫我匹配物料', '💡 使用技巧', '📋 工藝建議']"
          :key="q"
          class="px-2.5 py-1 text-xs rounded-full border border-gray-200 bg-white text-gray-600 hover:bg-gray-50 hover:border-gray-300 transition-colors whitespace-nowrap"
          :disabled="store.processing"
          @click="store.sendMessage(q.replace(/^[^\s]+\s/, ''))"
        >
          {{ q }}
        </button>
      </div>

      <!-- 输入区 -->
      <div class="border-t border-gray-100 px-3 py-3 shrink-0">
        <div class="flex items-end gap-2">
          <textarea
            v-model="inputText"
            rows="1"
            class="flex-1 resize-none rounded-lg border border-gray-200 px-3 py-2 text-sm outline-none focus:border-blue-400 focus:ring-1 focus:ring-blue-400 transition-colors"
            placeholder="输入消息..."
            :disabled="store.processing"
            @keydown="handleKeydown"
          ></textarea>
          <button
            class="shrink-0 w-9 h-9 rounded-lg flex items-center justify-center transition-colors text-white"
            :class="
              store.processing || !inputText.trim()
                ? 'bg-gray-300 cursor-not-allowed'
                : 'bg-blue-500 hover:bg-blue-600'
            "
            :disabled="store.processing || !inputText.trim()"
            @click="handleSend"
          >
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-4 h-4">
              <path d="M3.478 2.404a.75.75 0 0 0-.926.941l2.432 7.905H13.5a.75.75 0 0 1 0 1.5H4.984l-2.432 7.905a.75.75 0 0 0 .926.94 60.519 60.519 0 0 0 18.445-8.986.75.75 0 0 0 0-1.218A60.517 60.517 0 0 0 3.478 2.404Z"/>
            </svg>
          </button>
        </div>
      </div>
    </template>
  </div>
</template>

<style scoped>
.overflow-y-auto::-webkit-scrollbar {
  width: 4px;
}
.overflow-y-auto::-webkit-scrollbar-track {
  background: transparent;
}
.overflow-y-auto::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 2px;
}

@keyframes bounce {
  0%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-4px); }
}
.animate-bounce {
  animation: bounce 1s ease-in-out infinite;
}
</style>
