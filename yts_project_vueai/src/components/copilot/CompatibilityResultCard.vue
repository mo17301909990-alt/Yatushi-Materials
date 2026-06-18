<script setup lang="ts">
import type { CompatibilityResultData } from '@/types/copilot';

defineProps<{
  data?: CompatibilityResultData;
}>();

function statusBadgeClass(status: string): string {
  if (status === 'compatible') return 'bg-green-100 text-green-700 border-green-200';
  if (status === 'incompatible') return 'bg-red-100 text-red-700 border-red-200';
  return 'bg-yellow-100 text-yellow-700 border-yellow-200';
}

function statusIcon(status: string): string {
  if (status === 'compatible') return 'V';
  if (status === 'incompatible') return 'X';
  return '~';
}
</script>

<template>
  <div class="rounded-xl border border-gray-200 bg-white shadow-sm overflow-hidden">
    <div v-if="data?.title" class="px-3 py-2 bg-indigo-50 border-b border-gray-100">
      <span class="text-xs font-semibold text-indigo-700">{{ data.title }}</span>
    </div>
    <div class="divide-y divide-gray-100">
      <div
        v-for="(item, idx) in data?.items ?? []"
        :key="idx"
        class="flex items-center gap-3 px-3 py-2.5"
      >
        <!-- 状态徽标 -->
        <span
          class="shrink-0 w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold border"
          :class="statusBadgeClass(item.status)"
        >
          {{ statusIcon(item.status) }}
        </span>
        <!-- 物料名称 -->
        <span class="flex-1 text-sm text-gray-800 font-medium truncate">
          {{ item.materialName }}
        </span>
        <!-- 生产验证次数 -->
        <span class="shrink-0 text-xs text-gray-400 whitespace-nowrap">
          验证 {{ item.verifyCount }} 次
        </span>
      </div>
    </div>
    <div v-if="!data?.items?.length" class="px-3 py-3 text-center text-xs text-gray-400">
      暂无兼容性数据
    </div>
  </div>
</template>
