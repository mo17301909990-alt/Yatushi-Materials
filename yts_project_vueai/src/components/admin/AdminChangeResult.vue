<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  recordId: number
  entityType: string
  entityId: number
  status: string
  changeSet?: string
}>()

const emit = defineEmits<{
  rollback: [recordId: number]
}>()

const typeLabel = computed(() => {
  const map: Record<string, string> = { pricing: '定价', specification: '规格', product: '物料' }
  return map[props.entityType] || props.entityType
})

const statusIcon = computed(() => {
  return props.status === 'rolled_back' ? '↩️' : '✅'
})
</script>

<template>
  <div class="rounded-lg border border-green-200 bg-green-50 p-3">
    <div class="flex items-center justify-between mb-1.5">
      <span class="text-sm font-medium text-gray-900">
        {{ statusIcon }} 变更已{{ status === 'rolled_back' ? '回滚' : '执行' }}
      </span>
      <span class="text-xs text-gray-400">#{{ recordId }}</span>
    </div>

    <div class="text-xs text-gray-600 space-y-0.5">
      <p>{{ typeLabel }} #{{ entityId }}</p>
      <p class="text-gray-400">状态：{{ status }}</p>
    </div>

    <button
      v-if="status === 'committed'"
      class="mt-2 text-xs px-2.5 py-1 rounded border border-orange-200 text-orange-600 hover:bg-orange-50 transition-colors"
      @click="emit('rollback', recordId)"
    >
      ↩️ 回滚此变更
    </button>
  </div>
</template>
