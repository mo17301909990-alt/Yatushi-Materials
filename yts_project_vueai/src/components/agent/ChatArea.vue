<script setup lang="ts">
import { ref, watch, nextTick } from 'vue'
import { useAgentChatStore } from '@/stores/agentChat'
import CopilotChat from '@/components/copilot/CopilotChat.vue'
import AdminRiskConfirmDialog from '@/components/admin/AdminRiskConfirmDialog.vue'
import TypingIndicator from './TypingIndicator.vue'
import ChatInput from './ChatInput.vue'
import type { AdminPreviewData } from '@/types/copilot'

const store = useAgentChatStore()
const chatContainer = ref<HTMLDivElement | null>(null)

// 高风险确认弹窗
const showConfirmDialog = ref(false)
const pendingMessageId = ref<string | null>(null)
const pendingRisk = ref<string>('')

function scrollToBottom() {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  })
}

watch(() => store.activeConversation?.messages.length, () => scrollToBottom())

function handleSend(text: string) {
  store.sendMessage(text)
  scrollToBottom()
}

function handleSuggestion(text: string) {
  store.sendMessage(text)
}

/** 管理员卡片：确认执行（高风险时先弹窗） */
function handleAdminConfirm(messageId: string) {
  const conv = store.activeConversation
  if (!conv) return
  const msg = conv.messages.find(m => m.id === messageId)
  if (!msg) return

  const data = msg.cardData as AdminPreviewData | undefined
  if (!data) return

  if (data.risk === 'high') {
    pendingMessageId.value = messageId
    pendingRisk.value = 'high'
    showConfirmDialog.value = true
  } else {
    store.executeAdminChange(messageId, true)
  }
}

/** 高风险弹窗确认 */
function handleRiskConfirmed(reason: string) {
  showConfirmDialog.value = false
  if (pendingMessageId.value) {
    store.executeAdminChange(pendingMessageId.value, true, reason)
    pendingMessageId.value = null
  }
}

/** 管理员卡片：回滚 */
function handleAdminRollback(recordId: number) {
  store.sendMessage(`回滚：${recordId}`)
}
</script>

<template>
  <div class="flex-1 h-full flex flex-col bg-white" v-if="store.activeConversation">
    <!-- 头部 -->
    <div class="shrink-0 flex items-center gap-2.5 px-4 py-3 border-b border-gray-100">
      <div class="w-7 h-7 rounded-full flex items-center justify-center text-sm bg-white shadow-sm border border-gray-200">
        {{ store.activeConversation.avatar }}
      </div>
      <div class="flex-1 min-w-0">
        <h3 class="text-sm font-semibold text-gray-900">{{ store.activeConversation.name }}</h3>
        <p class="text-[10px] text-gray-400">
          {{ store.activeConversation.agentType === 'general' ? '工艺知识问答' : store.activeConversation.agentType === 'matching' ? '物料匹配查询' : store.activeConversation.agentType === 'guide' ? '生产流程引导' : '管理后台变更' }}
        </p>
      </div>
      <button
        class="text-[10px] px-2 py-1 rounded text-gray-400 hover:text-red-500 hover:bg-red-50 transition-colors"
        @click="store.clearActiveMessages()"
      >清空</button>
    </div>

    <!-- 消息列表 -->
    <div
      ref="chatContainer"
      class="flex-1 overflow-y-auto py-3 px-4 space-y-3 scroll-smooth"
    >
      <CopilotChat
        v-for="msg in store.activeConversation.messages"
        :key="msg.id"
        :message="msg"
        @suggestion-click="handleSuggestion"
        @admin-confirm="handleAdminConfirm"
        @admin-cancel="() => {}"
        @admin-rollback="handleAdminRollback"
      />

      <TypingIndicator v-if="store.activeConversation.processing" />
    </div>

    <!-- 高风险确认弹窗 -->
    <AdminRiskConfirmDialog
      v-if="showConfirmDialog"
      title="高风险变更确认"
      message="此变更价格变动幅度超过 10%，请确认执行。"
      :risk="pendingRisk"
      @confirm="handleRiskConfirmed"
      @cancel="() => { showConfirmDialog = false; pendingMessageId = null }"
    />

    <!-- 输入区 -->
    <ChatInput
      :disabled="store.activeConversation.processing"
      @send="handleSend"
    />
  </div>

  <!-- 无会话 -->
  <div v-else class="flex-1 h-full flex items-center justify-center bg-gray-50">
    <div class="text-center">
      <svg class="mx-auto h-12 w-12 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
      </svg>
      <p class="mt-2 text-sm text-gray-400">选择一个会话开始</p>
    </div>
  </div>
</template>
