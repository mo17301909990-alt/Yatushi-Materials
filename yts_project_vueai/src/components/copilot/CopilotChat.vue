<script setup lang="ts">
import { marked } from 'marked';
import type { ChatMessage } from '@/types/copilot';
import MatchResultCard from '@/components/match/MatchResultCard.vue';
import ProductCardResult from './ProductCardResult.vue';
import CompatibilityTableCard from './CompatibilityTableCard.vue';
import ActionCard from './ActionCard.vue';
import CompatibilityResultCard from './CompatibilityResultCard.vue';
import AdminChangePreview from '@/components/admin/AdminChangePreview.vue';
import AdminChangeResult from '@/components/admin/AdminChangeResult.vue';

defineProps<{
  message: ChatMessage;
}>();

const emit = defineEmits<{
  (e: 'suggestion-click', text: string): void;
  (e: 'admin-confirm', messageId: string): void;
  (e: 'admin-cancel', messageId: string): void;
  (e: 'admin-rollback', recordId: number): void;
}>();

function renderMarkdown(text: string): string {
  return marked.parse(text, { async: false, breaks: true }) as string;
}

function formatTime(ts: number): string {
  const d = new Date(ts);
  const h = d.getHours().toString().padStart(2, '0');
  const m = d.getMinutes().toString().padStart(2, '0');
  return `${h}:${m}`;
}
</script>

<template>
  <div
    class="flex"
    :class="message.role === 'user' ? 'justify-end' : 'justify-start'"
  >
    <div class="max-w-[85%] space-y-1">
      <!-- 消息气泡 -->
      <div
        class="px-3 py-2 rounded-2xl text-sm leading-relaxed break-words"
        :class="
          message.role === 'user'
            ? 'bg-blue-500 text-white rounded-br-md'
            : 'bg-gray-100 text-gray-800 rounded-bl-md'
        "
      >
        <template v-if="message.role === 'user'">{{ message.content }}</template>
        <div v-else class="markdown-body" v-html="renderMarkdown(message.content)"></div>
        <!-- 流式光标：正在输出时显示闪烁指示器 -->
        <span v-if="message.isStreaming" class="streaming-cursor inline-block ml-0.5">▎</span>
      </div>

      <!-- A4: 内嵌结果卡片 -->
      <div v-if="message.products && message.products.length > 0" class="space-y-2 px-0.5">
        <MatchResultCard v-for="(p, i) in message.products" :key="i" :product="p" />
        <div class="text-xs text-gray-400 px-1">
          共 {{ message.filteredTotal ?? message.products.length }} 個匹配結果
          <span v-if="message.filteredTotal && message.filteredTotal > message.products.length" class="ml-1 text-blue-400">
            （顯示前 {{ message.products.length }} 個）
          </span>
        </div>
      </div>

      <!-- 来源引用标签 -->
      <div
        v-if="message.role === 'assistant' && message.sources && message.sources.length > 0"
        class="flex flex-wrap gap-1 px-1"
      >
        <span
          v-for="(src, idx) in message.sources"
          :key="idx"
          class="inline-flex items-center gap-0.5 px-1.5 py-0.5 text-xs rounded-full bg-gray-50 text-gray-400 border border-gray-200"
        >
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" class="w-3 h-3">
            <path fill-rule="evenodd" d="M4.5 2A1.5 1.5 0 003 3.5v13A1.5 1.5 0 004.5 18h11a1.5 1.5 0 001.5-1.5V7.621a1.5 1.5 0 00-.44-1.06l-4.12-4.122A1.5 1.5 0 0011.378 2H4.5zm2.25 8.5a.75.75 0 000 1.5h6.5a.75.75 0 000-1.5h-6.5zm0 3a.75.75 0 000 1.5h6.5a.75.75 0 000-1.5h-6.5z" clip-rule="evenodd" />
          </svg>
          {{ src.title }}
          <span v-if="src.confidence !== undefined" class="ml-0.5 text-[10px] text-gray-300">
            {{ Math.round(src.confidence * 100) }}%
          </span>
        </span>
      </div>

      <div class="text-[10px] text-gray-400 px-1" :class="message.role === 'user' ? 'text-right' : 'text-left'">
        {{ formatTime(message.timestamp) }}
      </div>

      <!-- 卡片类型渲染 -->
      <div v-if="message.cardType === 'product_card'" class="px-0.5 mt-1">
        <ProductCardResult :data="message.cardData as any" />
      </div>
      <div v-else-if="message.cardType === 'compatibility_table'" class="px-0.5 mt-1">
        <CompatibilityTableCard :headers="(message.cardData as any)?.headers" :rows="(message.cardData as any)?.rows" />
      </div>
      <div v-else-if="message.cardType === 'action_buttons'" class="px-0.5 mt-1">
        <ActionCard :actions="(message.cardData as any)?.actions" @action="(id: string) => emit('suggestion-click', id)" />
      </div>
      <div v-else-if="message.cardType === 'compatibility'" class="px-0.5 mt-1">
        <CompatibilityResultCard :data="message.cardData as any" />
      </div>

      <!-- 管理员变更卡片 -->
      <div v-else-if="message.cardType === 'admin_preview' && message.cardData" class="px-0.5 mt-1">
        <AdminChangePreview
          :entity-type="(message.cardData as any).entityType"
          :entity-id="(message.cardData as any).entityId"
          :current-values="(message.cardData as any).currentValues"
          :proposed-changes="(message.cardData as any).proposedChanges"
          :risk="(message.cardData as any).risk"
          @confirm="emit('admin-confirm', message.id)"
          @cancel="emit('admin-cancel', message.id)"
        />
      </div>
      <div v-else-if="message.cardType === 'admin_result' && message.cardData" class="px-0.5 mt-1">
        <AdminChangeResult
          :record-id="(message.cardData as any).recordId"
          :entity-type="(message.cardData as any).entityType"
          :entity-id="(message.cardData as any).entityId"
          :status="(message.cardData as any).status"
          :change-set="(message.cardData as any).changeSet"
          @rollback="(id: number) => emit('admin-rollback', id)"
        />
      </div>

      <!-- 建议按钮 -->
      <div
        v-if="message.role === 'assistant' && message.suggestions?.length"
        class="flex flex-wrap gap-1.5 px-1"
      >
        <button
          v-for="(sug, idx) in message.suggestions"
          :key="idx"
          class="px-2.5 py-1 text-xs rounded-full border border-blue-200 bg-blue-50 text-blue-600 hover:bg-blue-100 hover:border-blue-300 transition-colors whitespace-nowrap"
          @click="emit('suggestion-click', sug)"
        >
          {{ sug }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.markdown-body :deep(p) {
  margin: 0.25rem 0;
}
.markdown-body :deep(p:first-child) {
  margin-top: 0;
}
.markdown-body :deep(p:last-child) {
  margin-bottom: 0;
}
.markdown-body :deep(ul),
.markdown-body :deep(ol) {
  padding-left: 1.25rem;
  margin: 0.25rem 0;
}
.markdown-body :deep(li) {
  margin: 0.125rem 0;
}
.markdown-body :deep(code) {
  background: rgba(0,0,0,0.06);
  padding: 0.125rem 0.375rem;
  border-radius: 3px;
  font-size: 0.8125rem;
}
.markdown-body :deep(pre) {
  background: rgba(0,0,0,0.06);
  padding: 0.625rem;
  border-radius: 6px;
  overflow-x: auto;
  font-size: 0.8125rem;
  margin: 0.375rem 0;
}
.markdown-body :deep(pre code) {
  background: none;
  padding: 0;
}
.markdown-body :deep(table) {
  border-collapse: collapse;
  width: 100%;
  margin: 0.375rem 0;
  font-size: 0.8125rem;
}
.markdown-body :deep(th),
.markdown-body :deep(td) {
  border: 1px solid #d1d5db;
  padding: 0.25rem 0.5rem;
  text-align: left;
}
.markdown-body :deep(th) {
  background: rgba(0,0,0,0.04);
  font-weight: 600;
}
.markdown-body :deep(strong) {
  font-weight: 600;
}
.markdown-body :deep(a) {
  color: #2563eb;
  text-decoration: underline;
}
.markdown-body :deep(hr) {
  border: none;
  border-top: 1px solid #e5e7eb;
  margin: 0.5rem 0;
}

.streaming-cursor {
  animation: blink 1s step-end infinite;
}
@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}
</style>
