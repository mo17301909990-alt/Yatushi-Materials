<script setup lang="ts">
import { ref, computed } from 'vue'
import type { GoldFoilProduct } from '@/types/goldFoil'
import MatchResultCard from './MatchResultCard.vue'
import { ElPagination, ElSelect, ElOption } from 'element-plus'

const props = defineProps<{
  products: GoldFoilProduct[]
  loading: boolean
  total: number
  matchTags?: any[]
}>()

const emit = defineEmits<{
  (e: 'page-change', page: number, size: number): void
  (e: 'remove-tag', index: number): void
}>()

const currentPage = ref(1)
const pageSize = ref(15)
const sortBy = ref('match-score')

function tagClass(category: string): string {
  const m: Record<string, string> = {
    quickQuery: 'bg-blue-50 text-blue-700 border-blue-200',
    productType: 'bg-purple-50 text-purple-700 border-purple-200',
    stamping: 'bg-orange-50 text-orange-700 border-orange-200',
    material: 'bg-teal-50 text-teal-700 border-teal-200',
    postProcess: 'bg-pink-50 text-pink-700 border-pink-200',
  }
  return m[category] || 'bg-gray-50 text-gray-600 border-gray-200'
}

const hasResults = computed(() => props.products.length > 0)

const sortedProducts = computed(() => {
  const list = [...props.products]
  if (sortBy.value === 'match-score') {
    list.sort((a, b) => ((b as any).matchScore ?? 0) - ((a as any).matchScore ?? 0))
  } else if (sortBy.value === 'price-asc') {
    list.sort((a, b) => (a.price ?? 0) - (b.price ?? 0) || ((b as any).matchScore ?? 0) - ((a as any).matchScore ?? 0))
  } else if (sortBy.value === 'price-desc') {
    list.sort((a, b) => (b.price ?? 0) - (a.price ?? 0) || ((b as any).matchScore ?? 0) - ((a as any).matchScore ?? 0))
  } else if (sortBy.value === 'name') {
    list.sort((a, b) => (a.name || '').localeCompare(b.name || ''))
  }
  return list
})

function handlePageChange(page: number) {
  currentPage.value = page
  emit('page-change', page, pageSize.value)
}

function handleSizeChange(size: number) {
  pageSize.value = size
  currentPage.value = 1
  emit('page-change', 1, size)
}
</script>

<template>
  <div class="h-full flex flex-col">
    <!-- 標題欄 -->
    <div class="shrink-0 flex items-center justify-between px-4 py-3 border-b border-gray-100 bg-white">
      <div class="flex items-center gap-2">
        <h2 class="text-sm font-semibold text-gray-800">匹配結果</h2>
        <span v-if="total > 0" class="text-xs text-gray-400">({{ total }} 條)</span>
      </div>
      <!-- B4: 排序 -->
      <div v-if="hasResults" class="flex items-center gap-2">
        <span class="text-xs text-gray-400">排序:</span>
        <el-select v-model="sortBy" size="small" class="w-28">
          <el-option value="match-score" label="匹配度" />
          <el-option value="default" label="默認" />
          <el-option value="price-asc" label="價格 ↑" />
          <el-option value="price-desc" label="價格 ↓" />
          <el-option value="name" label="名稱" />
        </el-select>
      </div>
    </div>

    <!-- 匹配序列条 -->
    <div v-if="matchTags && matchTags.length > 0" class="shrink-0 px-4 py-2 border-b border-gray-100 bg-gray-50/50">
      <div class="text-xs text-gray-500 mb-1.5">匹配序列: ({{ matchTags.length }} 个条件)</div>
      <div class="flex flex-wrap gap-1.5">
        <span v-for="(tag, i) in matchTags" :key="tag.id"
          class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-xs border"
          :class="tagClass(tag.category)">
          <span class="font-medium mr-0.5">{{ i + 1 }}.</span>
          <span>{{ tag.label }}</span>
          <button class="ml-0.5 hover:opacity-60 text-xs leading-none" @click="$emit('remove-tag', i)">✕</button>
        </span>
      </div>
    </div>

    <!-- 加載狀態 -->
    <div v-if="loading" class="flex-1 flex items-center justify-center">
      <div class="flex items-center gap-2 text-gray-400 text-sm">
        <svg class="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
        </svg>
        正在查詢...
      </div>
    </div>

    <!-- 空白狀態 -->
    <div v-else-if="!hasResults" class="flex-1 flex items-center justify-center">
      <div class="text-center">
        <svg class="mx-auto h-10 w-10 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>
        <p class="mt-2 text-sm text-gray-400">請在左側設定篩選條件後查詢</p>
      </div>
    </div>

    <!-- 結果列表 -->
    <div v-else class="flex-1 overflow-y-auto px-4 py-3 space-y-3">
      <MatchResultCard v-for="(p, i) in sortedProducts" :key="p.gpNo || i" :product="p" />
    </div>

    <!-- 分頁 -->
    <div v-if="total > 0" class="shrink-0 border-t border-gray-100 bg-white px-4 py-2 flex justify-center">
      <ElPagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[15, 30, 50]"
        layout="sizes, prev, pager, next"
        small
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>
