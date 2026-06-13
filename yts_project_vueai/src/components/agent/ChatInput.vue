<script setup lang="ts">
import { ref } from 'vue'

const emit = defineEmits<{
  (e: 'send', text: string): void
}>()

defineProps<{
  disabled?: boolean
}>()

const inputText = ref('')

function handleSend() {
  const text = inputText.value.trim()
  if (!text) return
  inputText.value = ''
  emit('send', text)
}

function handleKeydown(e: KeyboardEvent) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    handleSend()
  }
}
</script>

<template>
  <div class="border-t border-gray-100 px-3 py-3 shrink-0">
    <div class="flex items-end gap-2">
      <textarea
        v-model="inputText"
        rows="1"
        class="flex-1 resize-none rounded-lg border border-gray-200 px-3 py-2 text-sm outline-none focus:border-blue-400 focus:ring-1 focus:ring-blue-400 transition-colors"
        placeholder="输入消息..."
        :disabled="disabled"
        @keydown="handleKeydown"
      ></textarea>
      <button
        class="shrink-0 w-9 h-9 rounded-lg flex items-center justify-center transition-colors text-white"
        :class="
          disabled || !inputText.trim()
            ? 'bg-gray-300 cursor-not-allowed'
            : 'bg-blue-500 hover:bg-blue-600'
        "
        :disabled="disabled || !inputText.trim()"
        @click="handleSend"
      >
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor" class="w-4 h-4">
          <path d="M3.478 2.404a.75.75 0 0 0-.926.941l2.432 7.905H13.5a.75.75 0 0 1 0 1.5H4.984l-2.432 7.905a.75.75 0 0 0 .926.94 60.519 60.519 0 0 0 18.445-8.986.75.75 0 0 0 0-1.218A60.517 60.517 0 0 0 3.478 2.404Z"/>
        </svg>
      </button>
    </div>
  </div>
</template>
