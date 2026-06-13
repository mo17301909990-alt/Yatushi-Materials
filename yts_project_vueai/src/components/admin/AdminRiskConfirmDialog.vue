<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps<{
  title: string
  message: string
  risk: string
}>()

const emit = defineEmits<{
  confirm: [reason: string]
  cancel: []
}>()

const reason = ref('')
</script>

<template>
  <div class="fixed inset-0 z-50 flex items-center justify-center bg-black/40" @click.self="emit('cancel')">
    <div class="bg-white rounded-xl shadow-xl w-[400px] max-w-[90vw] overflow-hidden">
      <div class="bg-red-50 border-b border-red-100 px-4 py-3">
        <div class="flex items-center gap-2">
          <span class="text-lg">⚠️</span>
          <span class="font-semibold text-red-700">{{ title }}</span>
        </div>
      </div>

      <div class="px-4 py-3">
        <p class="text-sm text-gray-600">{{ message }}</p>

        <label class="block mt-3 text-xs text-gray-500">变更原因（选填）</label>
        <input
          v-model="reason"
          placeholder="例如：客户要求调价"
          class="mt-1 w-full px-2.5 py-1.5 text-xs border border-gray-200 rounded-lg focus:outline-none focus:ring-1 focus:ring-red-400"
        />
      </div>

      <div class="flex gap-2 px-4 py-3 border-t border-gray-100">
        <button
          class="flex-1 text-xs py-2 rounded font-medium bg-red-600 text-white hover:bg-red-700 transition-colors"
          @click="emit('confirm', reason)"
        >确认执行高风险操作</button>
        <button
          class="px-4 py-2 text-xs rounded border border-gray-200 text-gray-500 hover:bg-gray-50 transition-colors"
          @click="emit('cancel')"
        >取消</button>
      </div>
    </div>
  </div>
</template>
