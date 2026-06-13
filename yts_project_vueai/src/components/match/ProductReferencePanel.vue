<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { goldFoilApi } from '@/api/modules/goldFoil'
import type { GoldFoilProduct } from '@/types/goldFoil'

const products = ref<GoldFoilProduct[]>([])
const loading = ref(true)
const searchQuery = ref('')
const expandedId = ref<string | null>(null)
const pageSize = ref(50)
const currentPage = ref(1)

const filteredProducts = computed(() => {
  let list = products.value
  if (searchQuery.value.trim()) {
    const q = searchQuery.value.trim().toLowerCase()
    list = list.filter(p =>
      (p.modelNumber?.toLowerCase() || '').includes(q) ||
      (p.name?.toLowerCase() || '').includes(q) ||
      (p.companyNumber?.toLowerCase() || '').includes(q) ||
      (p.gpNo?.toLowerCase() || '').includes(q) ||
      (p.color?.toLowerCase() || '').includes(q)
    )
  }
  return list
})

const totalPages = computed(() => Math.ceil(filteredProducts.value.length / pageSize.value))

const pagedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredProducts.value.slice(start, start + pageSize.value)
})

function toggleExpand(gpNo: string) {
  expandedId.value = expandedId.value === gpNo ? null : gpNo
}

onMounted(async () => {
  try {
    const res = await goldFoilApi.getAllProducts() as any
    products.value = Array.isArray(res) ? res : res?.data ?? []
  } catch (e) {
    console.error('加载产品目录失败', e)
    products.value = []
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="h-full flex flex-col">
    <!-- 头部 -->
    <div class="shrink-0 px-3 py-3 border-b border-gray-100">
      <div class="flex items-center gap-2 mb-2">
        <svg class="w-4 h-4 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
        </svg>
        <h3 class="text-sm font-semibold text-gray-800">产品目录</h3>
        <span v-if="!loading" class="text-[10px] text-gray-400">({{ filteredProducts.length }})</span>
      </div>
      <input
        v-model="searchQuery"
        placeholder="筛选型号/名称/公司..."
        class="w-full px-2.5 py-1.5 text-xs border border-gray-200 rounded focus:outline-none focus:ring-1 focus:ring-indigo-400 bg-gray-50"
      />
    </div>

    <!-- 加载 -->
    <div v-if="loading" class="flex-1 flex items-center justify-center">
      <div class="flex items-center gap-2 text-gray-400 text-xs">
        <svg class="animate-spin h-3 w-3" viewBox="0 0 24 24" fill="none">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
        </svg>
        加载中...
      </div>
    </div>

    <!-- 列表 -->
    <div v-else class="flex-1 overflow-y-auto">
      <div v-for="p in pagedProducts" :key="p.gpNo || p.modelNumber" class="border-b border-gray-50 last:border-b-0">
        <!-- 概要行 -->
        <div
          class="flex items-center justify-between px-3 py-2 cursor-pointer hover:bg-gray-50 transition-colors"
          @click="toggleExpand(p.gpNo || '')"
        >
          <div class="min-w-0 flex-1">
            <div class="text-xs font-medium text-gray-900 truncate">{{ p.name }}</div>
            <div class="text-[10px] text-gray-400 truncate">
              {{ p.modelNumber || '' }}<span v-if="p.modelNumber && p.companyNumber"> | </span>{{ p.companyNumber || '' }}
            </div>
          </div>
          <svg
            class="w-3 h-3 text-gray-300 shrink-0 ml-2 transition-transform"
            :class="expandedId === (p.gpNo || '') ? 'rotate-180' : ''"
            fill="none" stroke="currentColor" viewBox="0 0 24 24"
          >
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
          </svg>
        </div>

        <!-- 详细资料 -->
        <div v-if="expandedId === (p.gpNo || '')" class="px-3 pb-3 space-y-1.5 bg-gray-50/50">
          <div class="grid grid-cols-2 gap-x-3 gap-y-1 text-[10px]">
            <div><span class="text-gray-400">物料編號:</span> <span class="text-gray-700">{{ p.materialNumber || '-' }}</span></div>
            <div><span class="text-gray-400">顏色:</span> <span class="text-gray-700">{{ p.color || '-' }}</span></div>
            <div><span class="text-gray-400">尺寸:</span> <span class="text-gray-700">{{ p.size || '-' }}</span></div>
            <div><span class="text-gray-400">鬆緊度:</span> <span class="text-gray-700">{{ p.tightness || '-' }}</span></div>
            <div><span class="text-gray-400">性能:</span> <span class="text-gray-700">{{ p.performance || '-' }}</span></div>
            <div><span class="text-gray-400">溫度:</span> <span class="text-gray-700">{{ p.temperatureRange || '-' }}</span></div>
            <div><span class="text-gray-400">價格:</span> <span class="text-gray-700">{{ p.price ?? '-' }}</span></div>
            <div><span class="text-gray-400">編號:</span> <span class="text-gray-700">{{ p.gpNo || '-' }}</span></div>
          </div>

          <div class="flex flex-wrap gap-1 pt-1">
            <span v-if="p.flatHotStamping" class="px-1.5 py-0.5 rounded text-[10px] bg-indigo-50 text-indigo-600">平面燙金: {{ p.flatHotStamping }}</span>
            <span v-if="p.embossedGoldStamping" class="px-1.5 py-0.5 rounded text-[10px] bg-indigo-50 text-indigo-600">立體燙金: {{ p.embossedGoldStamping }}</span>
            <span v-if="p.uvPrinting === 'V'" class="px-1.5 py-0.5 rounded text-[10px] bg-green-50 text-green-600">UV 適用</span>
            <span v-if="p.ledUvGlitter === 'V'" class="px-1.5 py-0.5 rounded text-[10px] bg-green-50 text-green-600">LED UV 適用</span>
            <span v-if="p.stampingPrinting === 'V'" class="px-1.5 py-0.5 rounded text-[10px] bg-green-50 text-green-600">燙金+印刷 適用</span>
            <span v-if="p.uvPrinting === 'X'" class="px-1.5 py-0.5 rounded text-[10px] bg-red-50 text-red-500">UV 不適用</span>
            <span v-if="p.ledUvGlitter === 'X'" class="px-1.5 py-0.5 rounded text-[10px] bg-red-50 text-red-500">LED UV 不適用</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div v-if="totalPages > 1" class="shrink-0 border-t border-gray-100 px-3 py-2 flex items-center justify-between text-xs text-gray-400">
      <button
        :disabled="currentPage <= 1"
        class="px-2 py-1 rounded hover:bg-gray-100 disabled:opacity-30 disabled:cursor-not-allowed"
        @click="currentPage = Math.max(1, currentPage - 1)"
      >上一页</button>
      <span>{{ currentPage }} / {{ totalPages }}</span>
      <button
        :disabled="currentPage >= totalPages"
        class="px-2 py-1 rounded hover:bg-gray-100 disabled:opacity-30 disabled:cursor-not-allowed"
        @click="currentPage = Math.min(totalPages, currentPage + 1)"
      >下一页</button>
    </div>
  </div>
</template>
