<script setup lang="ts">
import type { CompatibilityResultData, UnifiedCompatibility, CompatibilityItem } from '@/types/copilot';

defineProps<{
  data?: CompatibilityResultData;
}>();

function isUnifiedItem(item: CompatibilityItem | UnifiedCompatibility): item is UnifiedCompatibility {
  return 'compatibilityStatus' in item;
}

/** 将兼容性状态值转为显示符号 */
function statusIcon(status: string): string {
  if (status === 'V' || status === 'compatible') return 'V';
  if (status === 'X' || status === 'incompatible') return 'X';
  if (status === '▷' || status === 'partial') return '▷';
  return '?';
}

/** 兼容性状态彩色背景色映射 */
function statusBgClass(status: string): string {
  if (status === 'V' || status === 'compatible') return 'bg-green-100';
  if (status === 'X' || status === 'incompatible') return 'bg-red-100';
  if (status === '▷' || status === 'partial') return 'bg-yellow-100';
  return 'bg-gray-100';
}

/** 兼容性状态彩色文字色映射 */
function statusTextClass(status: string): string {
  if (status === 'V' || status === 'compatible') return 'text-green-700';
  if (status === 'X' || status === 'incompatible') return 'text-red-700';
  if (status === '▷' || status === 'partial') return 'text-yellow-700';
  return 'text-gray-500';
}

/** 兼容性状态彩色边框映射 */
function statusBorderClass(status: string): string {
  if (status === 'V' || status === 'compatible') return 'border-green-200';
  if (status === 'X' || status === 'incompatible') return 'border-red-200';
  if (status === '▷' || status === 'partial') return 'border-yellow-200';
  return 'border-gray-200';
}
</script>

<template>
  <div class="rounded-xl border border-gray-200 bg-white shadow-sm overflow-hidden">
    <div v-if="data?.title" class="px-4 py-2.5 bg-indigo-50 border-b border-gray-100">
      <span class="text-xs font-semibold text-indigo-700">{{ data.title }}</span>
    </div>

    <!-- Unified compatibility format -->
    <div v-if="data?.items?.length && isUnifiedItem(data.items[0])" class="divide-y divide-gray-100">
      <div
        v-for="(item, idx) in data.items"
        :key="idx"
        class="flex items-start gap-3 px-4 py-3"
      >
        <!-- 状态彩色徽标 -->
        <span
          class="shrink-0 inline-flex items-center justify-center w-7 h-7 rounded-full text-xs font-bold border"
          :class="[statusBgClass((item as UnifiedCompatibility).compatibilityStatus || ''), statusTextClass((item as UnifiedCompatibility).compatibilityStatus || ''), statusBorderClass((item as UnifiedCompatibility).compatibilityStatus || '')]"
        >
          {{ statusIcon((item as UnifiedCompatibility).compatibilityStatus || '') }}
        </span>

        <div class="flex-1 min-w-0">
          <!-- 物料名称（加粗）+ 物料编码 -->
          <div class="flex items-baseline gap-2 flex-wrap">
            <span class="text-sm font-semibold text-gray-900 truncate max-w-[260px] sm:max-w-[360px]">
              {{ (item as UnifiedCompatibility).materialName }}
            </span>
            <span v-if="(item as UnifiedCompatibility).materialCode" class="text-xs text-gray-400 shrink-0 font-mono">
              {{ (item as UnifiedCompatibility).materialCode }}
            </span>
          </div>

          <!-- 模块类型 + 工序 -->
          <div v-if="(item as UnifiedCompatibility).moduleType || (item as UnifiedCompatibility).processOperation" class="flex items-center gap-1.5 mt-1 flex-wrap">
            <span v-if="(item as UnifiedCompatibility).moduleType" class="text-xs text-indigo-600 bg-indigo-50 px-1.5 py-0.5 rounded">
              {{ (item as UnifiedCompatibility).moduleType }}
            </span>
            <span v-if="(item as UnifiedCompatibility).processOperation" class="text-xs text-gray-500">
              · {{ (item as UnifiedCompatibility).processOperation }}
            </span>
          </div>

          <!-- 条件描述 -->
          <div v-if="(item as UnifiedCompatibility).conditionDesc" class="text-xs text-gray-400 mt-1">
            {{ (item as UnifiedCompatibility).conditionDesc }}
          </div>
        </div>
      </div>
    </div>

    <!-- Legacy compatibility format -->
    <div v-else class="divide-y divide-gray-100">
      <div
        v-for="(item, idx) in data?.items ?? []"
        :key="idx"
        class="flex items-center gap-3 px-4 py-3"
      >
        <!-- 状态彩色徽标 -->
        <span
          class="shrink-0 inline-flex items-center justify-center w-7 h-7 rounded-full text-xs font-bold border"
          :class="[statusBgClass((item as CompatibilityItem).status), statusTextClass((item as CompatibilityItem).status), statusBorderClass((item as CompatibilityItem).status)]"
        >
          {{ statusIcon((item as CompatibilityItem).status) }}
        </span>

        <!-- 物料名称（加粗） -->
        <span class="flex-1 text-sm font-semibold text-gray-900 truncate max-w-[260px] sm:max-w-[360px]">
          {{ (item as CompatibilityItem).materialName }}
        </span>

        <!-- 验证次数 -->
        <span class="shrink-0 text-xs text-gray-400 whitespace-nowrap">
          验证 {{ (item as CompatibilityItem).verifyCount }} 次
        </span>
      </div>
    </div>

    <div v-if="!data?.items?.length" class="px-4 py-4 text-center text-xs text-gray-400">
      暂无兼容性数据
    </div>
  </div>
</template>
