<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  entityType: string
  entityId: number
  currentValues: Record<string, unknown>
  proposedChanges: Record<string, unknown>
  risk: string
}>()

const emit = defineEmits<{
  confirm: []
  cancel: []
}>()

const typeLabel = computed(() => {
  const map: Record<string, string> = { pricing: '定价', specification: '规格', product: '物料' }
  return map[props.entityType] || props.entityType
})

const riskConfig = computed(() => {
  switch (props.risk) {
    case 'high': return { label: '高风险', color: 'text-red-600', bg: 'bg-red-50 border-red-200', badge: 'bg-red-100 text-red-700' }
    case 'medium': return { label: '中风险', color: 'text-yellow-600', bg: 'bg-yellow-50 border-yellow-200', badge: 'bg-yellow-100 text-yellow-700' }
    default: return { label: '低风险', color: 'text-green-600', bg: 'bg-green-50 border-green-200', badge: 'bg-green-100 text-green-700' }
  }
})

const changes = computed(() => {
  return Object.entries(props.proposedChanges).map(([key, newVal]) => {
    const oldVal = props.currentValues[key]
    let pct = ''
    if (typeof oldVal === 'number' && typeof newVal === 'number' && oldVal !== 0) {
      const diff = ((newVal as number) - oldVal) / oldVal
      pct = (diff >= 0 ? '+' : '') + (diff * 100).toFixed(1) + '%'
    }
    return { field: key, old: oldVal, new: newVal, pct }
  })
})
</script>

<template>
  <div class="rounded-lg border overflow-hidden" :class="riskConfig.bg">
    <div class="p-3">
      <div class="flex items-center justify-between mb-2">
        <span class="text-sm font-medium text-gray-900">
          📋 变更预览 — {{ typeLabel }} #{{ entityId }}
        </span>
        <span class="text-xs font-medium px-2 py-0.5 rounded-full" :class="riskConfig.badge">
          {{ riskConfig.label }}
        </span>
      </div>

      <div class="space-y-1.5 text-xs">
        <div
          v-for="c in changes"
          :key="c.field"
          class="flex items-center gap-2 py-1 px-2 rounded bg-white/60"
        >
          <span class="w-16 text-gray-500 shrink-0">{{ c.field }}</span>
          <span class="text-gray-400 line-through">{{ c.old }}</span>
          <svg class="w-3.5 h-3.5 text-gray-300 shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
          </svg>
          <span class="font-medium text-gray-900">{{ c.new }}</span>
          <span v-if="c.pct" class="text-gray-400">({{ c.pct }})</span>
        </div>
      </div>

      <div class="flex gap-2 mt-3">
        <button
          class="flex-1 text-xs py-1.5 rounded font-medium transition-colors"
          :class="risk === 'high'
            ? 'bg-red-600 text-white hover:bg-red-700'
            : 'bg-indigo-600 text-white hover:bg-indigo-700'"
          @click="emit('confirm')"
        >
          {{ risk === 'high' ? '⚠️ 高风险，确认执行' : '✅ 确认执行' }}
        </button>
        <button
          class="px-3 py-1.5 text-xs rounded border border-gray-200 text-gray-500 hover:bg-gray-50 transition-colors"
          @click="emit('cancel')"
        >取消</button>
      </div>
    </div>
  </div>
</template>
