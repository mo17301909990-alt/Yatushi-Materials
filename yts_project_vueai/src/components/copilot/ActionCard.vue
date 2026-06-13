<script setup lang="ts">
defineProps<{
  actions?: { id: string; label: string; primary: boolean }[]
}>()

const emit = defineEmits<{
  (e: 'action', id: string): void
}>()
</script>

<template>
  <div v-if="actions && actions.length > 0" class="rounded-xl border border-gray-200 bg-white shadow-sm px-3 py-2">
    <div class="text-[10px] text-gray-400 mb-1.5 font-medium">推荐操作</div>
    <div class="flex flex-wrap gap-1.5">
      <button
        v-for="(act, i) in actions"
        :key="i"
        class="px-2.5 py-1 rounded-full text-[11px] font-medium transition-colors whitespace-nowrap"
        :class="act.primary
          ? 'bg-indigo-600 text-white hover:bg-indigo-700'
          : 'border border-gray-300 text-gray-600 hover:bg-gray-50'"
        @click="emit('action', act.id)"
      >
        {{ act.label }}
      </button>
    </div>
  </div>
</template>
