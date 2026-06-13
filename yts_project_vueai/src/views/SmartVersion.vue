<script setup lang="ts">
import { ref, watch, computed, onBeforeUnmount } from 'vue'
import { goldFoilApi } from '@/api'
import type { GoldFoilProduct } from '@/types/goldFoil'
import { useCopilotStore } from '@/stores/copilot'
import GoldFoilFilterPanel from '@/components/match/GoldFoilFilterPanel.vue'
import MatchResultList from '@/components/match/MatchResultList.vue'
import CopilotSidebar from '@/components/copilot/CopilotSidebar.vue'
import ProductReferencePanel from '@/components/match/ProductReferencePanel.vue'

const copilot = useCopilotStore()
const products = ref<GoldFoilProduct[]>([])

// ── 三栏拖动调整大小 ──
const leftWidth = ref(288)   // 左栏初始 px
const rightWidth = ref(360)  // 右栏初始 px
const dragging = ref<'left' | 'right' | null>(null)
const MIN_LEFT = 200
const MAX_LEFT = 500
const MIN_RIGHT = 260
const MAX_RIGHT = 600

function onDragStart(side: 'left' | 'right') {
  dragging.value = side
  document.body.style.cursor = 'col-resize'
  document.body.style.userSelect = 'none'
}

function onDragMove(e: MouseEvent) {
  if (!dragging.value) return
  const container = (e.currentTarget as HTMLElement)?.closest('.flex.h-screen') as HTMLElement
  if (!container) return
  const rect = container.getBoundingClientRect()
  const totalW = rect.width

  if (dragging.value === 'left') {
    let w = Math.round(e.clientX - rect.left)
    w = Math.max(MIN_LEFT, Math.min(MAX_LEFT, w))
    // 给中间留至少 400px
    const midMin = 400
    if (totalW - w - rightWidth.value < midMin) w = totalW - rightWidth.value - midMin
    leftWidth.value = Math.max(MIN_LEFT, w)
  } else {
    let w = Math.round(rect.right - e.clientX)
    w = Math.max(MIN_RIGHT, Math.min(MAX_RIGHT, w))
    const midMin = 400
    if (totalW - leftWidth.value - w < midMin) w = totalW - leftWidth.value - midMin
    rightWidth.value = Math.max(MIN_RIGHT, w)
  }
}

function onDragEnd() {
  dragging.value = null
  document.body.style.cursor = ''
  document.body.style.userSelect = ''
}

// 全局 mouseup/mousemove 防止拖出区域丢失事件
onBeforeUnmount(() => {
  document.removeEventListener('mouseup', onDragEnd)
  document.removeEventListener('mousemove', onDragMove)
})
const activeRightTab = ref<'copilot' | 'catalog'>('copilot')
const rightTabs = [
  { key: 'copilot' as const, label: 'AI 助手', icon: '🤖' },
  { key: 'catalog' as const, label: '产品目录', icon: '📋' },
]
const total = ref(0)
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(15)
const currentParams = ref<Record<string, unknown>>({})

// ── 渐进式筛选：匹配序列 ──
interface TagItem {
  id: string
  label: string
  category: string
  categoryLabel: string
  value: unknown
}

const matchTags = ref<TagItem[]>([])

function handleTagChange(e: { tags: TagItem[] }) {
  matchTags.value = e.tags
}

function handleRemoveTag(index: number) {
  matchTags.value.splice(index, 1)
}

// 为每个产品计算匹配度（纯客户端计算）
const productsWithScore = computed(() => {
  const tags = matchTags.value
  if (tags.length === 0) {
    return products.value.map(p => ({ ...p, matchScore: 100 }))
  }
  return products.value.map(product => {
    let matched = 0
    for (const tag of tags) {
      if (isProductMatch(product, tag)) matched++
    }
    return { ...product, matchScore: Math.round((matched / tags.length) * 100) }
  })
})

function isProductMatch(product: any, tag: TagItem): boolean {
  const v = String(tag.value ?? '')
  if (!v) return true
  if (tag.id.startsWith('company-')) return String(product.companyNumber || '').includes(v)
  if (tag.id.startsWith('gpNo-')) return String(product.gpNo || '').includes(v)
  if (tag.id.startsWith('paizi-')) return String(product.name || product.modelNumber || '').toLowerCase().includes(v.toLowerCase())
  if (tag.id.startsWith('color-')) return String(product.color || '').toLowerCase().includes(v.toLowerCase())
  if (tag.id.startsWith('price-')) return true
  if (tag.id.startsWith('perf-')) return String(product.performance || '').includes(v)
  if (tag.id.startsWith('type-')) return String(product.hotStampingPaperType || product.name || '').includes(v)
  if (tag.id.startsWith('stampType-')) {
    if (v.includes('平面')) return String(product.flatHotStamping || '').includes('V')
    if (v.includes('立体') || v.includes('立體')) return String(product.embossedGoldStamping || '').includes('V')
    return true
  }
  if (tag.id.startsWith('pp-')) {
    if (v.includes('UV') || v.includes('uv')) return product.uvPrinting === 'V'
    if (v.includes('印刷')) return product.stampingPrinting === 'V'
    if (v.includes('LED')) return product.ledUvGlitter === 'V'
    if (v.includes('击凸') || v.includes('擊凸')) return String(product.postHotStampingEmbossingDebossing || '').includes('V')
    return true
  }
  return true
}

async function doSearch(params: Record<string, unknown>, page = 1, size = 15) {
  currentParams.value = params
  currentPage.value = page
  pageSize.value = size
  loading.value = true
  try {
    const offset = (page - 1) * size
    const body = { ...params, offset, pageSize: size }
    const res = await goldFoilApi.getMatchProducts(body) as any
    let list: GoldFoilProduct[] = []
    if (Array.isArray(res)) {
      list = res
      total.value = res.length
    } else if (res?.products?.items) {
      list = res.products.items
      total.value = res.products.total ?? res.products.items.length
    } else {
      list = []
      total.value = 0
    }
    products.value = list
    return list
  } catch {
    products.value = []
    total.value = 0
    return []
  } finally {
    loading.value = false
  }
}

function handleSearch(params: Record<string, unknown>) {
  doSearch(params)
}

function handleClear() {
  products.value = []
  total.value = 0
  currentParams.value = {}
}

function handlePageChange(page: number, size: number) {
  doSearch(currentParams.value, page, size)
}

// B1: 单向联动 + A4: AI 触发后推内嵌结果
watch(() => copilot.filterParams, async (params) => {
  if (Object.keys(params).length > 0) {
    const list = await doSearch(params)
    // A4: 如果挂着 pendingInlineResults，把结果推进聊天气泡
    if (copilot.pendingInlineResults) {
      copilot.pendingInlineResults = null
      if (list.length > 0) {
        copilot.pushInlineResults(list, '')
      }
    }
  }
}, { deep: true })
</script>

<template>
  <div
    class="flex h-screen w-full overflow-hidden bg-gray-50"
    :class="{ 'select-none': dragging }"
    @mousemove="onDragMove"
    @mouseup="onDragEnd"
  >
    <!-- 左：篩選面板 -->
    <div :style="{ width: leftWidth + 'px' }" class="shrink-0 h-full overflow-hidden">
      <GoldFoilFilterPanel @search="handleSearch" @clear="handleClear" @tag-change="handleTagChange" />
    </div>

    <!-- 左拖拽条 -->
    <div
      class="w-1 shrink-0 cursor-col-resize hover:bg-indigo-400 active:bg-indigo-500 transition-colors relative group"
      @mousedown.prevent="onDragStart('left')"
    >
      <div class="absolute inset-y-0 -left-1 -right-1"></div>
    </div>

    <!-- 中：匹配結果 -->
    <div class="flex-1 min-w-0 h-full">
      <MatchResultList
        :products="productsWithScore"
        :loading="loading"
        :total="total"
        :match-tags="matchTags"
        @page-change="handlePageChange"
        @remove-tag="handleRemoveTag"
      />
    </div>

    <!-- 右拖拽条 -->
    <div
      class="w-1 shrink-0 cursor-col-resize hover:bg-indigo-400 active:bg-indigo-500 transition-colors relative group"
      @mousedown.prevent="onDragStart('right')"
    >
      <div class="absolute inset-y-0 -left-1 -right-1"></div>
    </div>

    <!-- 右：AI + 产品目录（标签切换） -->
    <div :style="{ width: rightWidth + 'px' }" class="shrink-0 h-full flex flex-col bg-white border-l border-gray-200">
      <div class="flex border-b border-gray-100 shrink-0">
        <button
          v-for="tab in rightTabs"
          :key="tab.key"
          class="flex-1 flex items-center justify-center gap-1.5 px-3 py-2 text-xs font-medium transition-colors border-b-2"
          :class="activeRightTab === tab.key ? 'border-indigo-600 text-indigo-600' : 'border-transparent text-gray-400 hover:text-gray-600'"
          @click="activeRightTab = tab.key"
        >
          <span v-html="tab.icon"></span>
          {{ tab.label }}
        </button>
      </div>
      <div class="flex-1 min-h-0">
        <CopilotSidebar v-if="activeRightTab === 'copilot'" />
        <ProductReferencePanel v-else />
      </div>
    </div>
  </div>
</template>
