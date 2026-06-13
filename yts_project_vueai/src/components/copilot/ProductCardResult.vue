<script setup lang="ts">
defineProps<{
  data?: {
    materialNumber?: string
    color?: string
    size?: string
    tightness?: string
    compatibility?: Record<string, string>
    postProcesses?: string[]
  }
}>()

const COMPAT_LABELS: Record<string, string> = {
  hotStamping: '烫金',
  screenPrinting: '丝印',
  laminating: '过胶',
  postProcessing: '后加工',
}

function compatBadgeClass(val?: string): string {
  if (val === 'compatible') return 'bg-green-100 text-green-700'
  if (val === 'incompatible') return 'bg-red-100 text-red-700'
  if (val === 'partial') return 'bg-amber-100 text-amber-700'
  return 'bg-gray-100 text-gray-400'
}

function compatLabel(val?: string): string {
  if (val === 'compatible') return '兼容'
  if (val === 'incompatible') return '不兼容'
  if (val === 'partial') return '部分兼容'
  return '未知'
}
</script>

<template>
  <div class="rounded-xl border border-gray-200 bg-white shadow-sm overflow-hidden">
    <!-- 头部 -->
    <div v-if="data?.materialNumber" class="px-3 py-2 bg-gray-50 border-b border-gray-100">
      <div class="flex items-center justify-between">
        <span class="text-sm font-semibold text-gray-800">{{ data.materialNumber }}</span>
        <span class="text-[10px] px-1.5 py-0.5 rounded-full bg-indigo-100 text-indigo-600 font-medium">物料信息</span>
      </div>
    </div>

    <!-- 详细信息 -->
    <div class="px-3 py-2 space-y-1.5" v-if="data">
      <!-- 颜色 / 尺寸 / 松紧度 -->
      <div class="grid grid-cols-3 gap-2 text-xs">
        <div>
          <span class="text-gray-400">颜色</span>
          <p class="text-gray-700 font-medium">{{ data.color || '-' }}</p>
        </div>
        <div>
          <span class="text-gray-400">尺寸</span>
          <p class="text-gray-700 font-medium">{{ data.size || '-' }}</p>
        </div>
        <div>
          <span class="text-gray-400">松紧度</span>
          <p class="text-gray-700 font-medium">{{ data.tightness || '-' }}</p>
        </div>
      </div>

      <!-- 兼容性标签 -->
      <div v-if="data.compatibility && Object.keys(data.compatibility).length > 0" class="pt-1 border-t border-gray-50">
        <div class="flex flex-wrap gap-1.5">
          <span
            v-for="(val, key) in data.compatibility"
            :key="key"
            class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-medium"
            :class="compatBadgeClass(val)"
          >
            {{ COMPAT_LABELS[key] || key }}
            <span class="opacity-70">({{ compatLabel(val) }})</span>
          </span>
        </div>
      </div>

      <!-- 后加工列表 -->
      <div v-if="data.postProcesses && data.postProcesses.length > 0" class="pt-1 border-t border-gray-50">
        <span class="text-[10px] text-gray-400">后加工：</span>
        <span class="inline-flex flex-wrap gap-1">
          <span
            v-for="(pp, i) in data.postProcesses"
            :key="i"
            class="px-1.5 py-0.5 rounded text-[10px] bg-gray-100 text-gray-500"
          >{{ pp }}</span>
        </span>
      </div>
    </div>

    <!-- 空态 -->
    <div v-else class="px-3 py-4 text-center text-xs text-gray-400">
      暂无物料信息
    </div>
  </div>
</template>
