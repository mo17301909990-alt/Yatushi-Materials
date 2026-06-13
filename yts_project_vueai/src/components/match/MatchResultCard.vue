<script setup lang="ts">
import { ref } from 'vue'
import type { GoldFoilProduct } from '@/types/goldFoil'

const props = defineProps<{
  product: GoldFoilProduct
}>()

const feedback = ref<'up' | 'down' | null>(null)
const showDetail = ref(false)

function formatValue(v: string | number | null | undefined): string {
  if (v === null || v === undefined || v === '') return '-'
  return String(v)
}

function compatLabel(v: string | null | undefined): { text: string; cls: string } | null {
  if (!v) return null
  if (v === 'V') return { text: '適用', cls: 'bg-green-50 text-green-700 border-green-200' }
  if (v === 'X') return { text: '不適用', cls: 'bg-red-50 text-red-600 border-red-200' }
  if (v === '▷') return { text: '有條件', cls: 'bg-yellow-50 text-yellow-700 border-yellow-200' }
  return { text: v, cls: 'bg-gray-50 text-gray-600 border-gray-200' }
}

function sendFeedback(type: 'up' | 'down') {
  feedback.value = type
}
</script>

<template>
  <div class="bg-white rounded-lg border border-gray-200 hover:shadow-md hover:border-indigo-200 transition-all duration-200">
    <!-- 概要行 -->
    <div class="p-4">
      <div class="flex justify-between items-start mb-3">
        <div>
          <h3 class="text-base font-semibold text-gray-900">{{ product.name }}</h3>
          <div class="text-xs text-gray-500 mt-0.5 space-x-2">
            <span>型號: {{ formatValue(product.modelNumber) }}</span>
            <span>|</span>
            <span>編號: {{ formatValue(product.gpNo) }}</span>
          </div>
        </div>
        <div class="text-right shrink-0 ml-3">
          <div class="text-xs text-gray-500">價格</div>
          <div class="text-lg font-bold text-indigo-600">{{ product.price ?? '-' }}</div>
          <div v-if="(product as any).matchScore != null" class="text-xs mt-1">
            <span :class="(product as any).matchScore >= 80 ? 'text-green-600' : (product as any).matchScore >= 50 ? 'text-yellow-600' : 'text-red-500'">
              {{ (product as any).matchScore }}% 匹配
            </span>
          </div>
        </div>
      </div>

      <div class="grid grid-cols-2 gap-x-4 gap-y-1 text-xs">
        <div class="flex gap-1"><span class="text-gray-500">公司:</span><span class="text-gray-700 font-medium">{{ formatValue(product.companyNumber) }}</span></div>
        <div class="flex gap-1"><span class="text-gray-500">顏色:</span><span class="text-gray-700 font-medium">{{ formatValue(product.color) }}</span></div>
        <div class="flex gap-1"><span class="text-gray-500">性能:</span><span class="text-gray-700 font-medium">{{ formatValue(product.performance) }}</span></div>
        <div class="flex gap-1"><span class="text-gray-500">溫度:</span><span class="text-gray-700 font-medium">{{ formatValue(product.temperatureRange) }}</span></div>
      </div>

      <!-- 兼容性標籤 -->
      <div class="flex flex-wrap gap-1.5 mt-3">
        <span v-if="product.uvPrinting === 'V'" class="px-2 py-0.5 bg-green-50 text-green-700 rounded-full text-xs border border-green-200">UV 適用</span>
        <span v-else-if="product.uvPrinting" class="px-2 py-0.5 bg-red-50 text-red-600 rounded-full text-xs border border-red-200">UV 不適用</span>
        <span v-if="product.ledUvGlitter === 'V'" class="px-2 py-0.5 bg-green-50 text-green-700 rounded-full text-xs border border-green-200">LED UV 適用</span>
        <span v-else-if="product.ledUvGlitter" class="px-2 py-0.5 bg-red-50 text-red-600 rounded-full text-xs border border-red-200">LED UV 不適用</span>
      </div>

      <!-- 操作栏 -->
      <div class="flex items-center justify-between mt-3 pt-2 border-t border-gray-100">
        <div class="flex items-center gap-2">
          <button
            class="flex items-center gap-1 text-xs px-2 py-1 rounded transition-colors"
            :class="feedback === 'up' ? 'bg-green-100 text-green-700' : 'text-gray-400 hover:text-green-600 hover:bg-green-50'"
            @click="sendFeedback('up')"
          >
            <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905 0 .714-.211 1.412-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.5" /></svg>
            有用
          </button>
          <button
            class="flex items-center gap-1 text-xs px-2 py-1 rounded transition-colors"
            :class="feedback === 'down' ? 'bg-red-100 text-red-700' : 'text-gray-400 hover:text-red-600 hover:bg-red-50'"
            @click="sendFeedback('down')"
          >
            <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14H5.236a2 2 0 01-1.789-2.894l3.5-7A2 2 0 018.736 3h4.018a2 2 0 01.485.06l3.76.94m-7 10v5a2 2 0 002 2h.096c.5 0 .905-.405.905-.904 0-.715.211-1.413.608-2.008L17 13V4m-7 10h2m5-10h2a2 2 0 012 2v6a2 2 0 01-2 2h-2.5" /></svg>
            沒用
          </button>
        </div>
        <button
          class="flex items-center gap-1 text-xs px-3 py-1.5 rounded-md bg-indigo-50 text-indigo-600 hover:bg-indigo-100 transition-colors font-medium"
          @click="showDetail = !showDetail"
        >
          {{ showDetail ? '收起資料' : '詳細資料' }}
          <svg class="w-3.5 h-3.5 transition-transform" :class="showDetail ? 'rotate-180' : ''" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" /></svg>
        </button>
      </div>
    </div>

    <!-- 详细资料 -->
    <div v-if="showDetail" class="border-t border-gray-100 bg-gray-50/50 px-4 py-3 space-y-3 rounded-b-lg">
      <div class="grid grid-cols-2 gap-x-6 gap-y-2 text-xs">
        <div class="flex gap-1"><span class="text-gray-500 shrink-0">物料編號:</span><span class="text-gray-700">{{ formatValue(product.materialNumber) }}</span></div>
        <div class="flex gap-1"><span class="text-gray-500 shrink-0">尺寸:</span><span class="text-gray-700">{{ formatValue(product.size) }}</span></div>
        <div class="flex gap-1"><span class="text-gray-500 shrink-0">鬆緊度:</span><span class="text-gray-700">{{ formatValue(product.tightness) }}</span></div>
        <div class="flex gap-1"><span class="text-gray-500 shrink-0">平面燙金:</span><span class="text-gray-700">{{ formatValue(product.flatHotStamping) }}</span></div>
        <div class="flex gap-1"><span class="text-gray-500 shrink-0">立體燙金:</span><span class="text-gray-700">{{ formatValue(product.embossedGoldStamping) }}</span></div>
        <div class="flex gap-1"><span class="text-gray-500 shrink-0">折光/有紋燙金:</span><span class="text-gray-700">{{ formatValue(product.refractiveHolographicPatternedTexturedHotStamping) }}</span></div>
        <div class="flex gap-1"><span class="text-gray-500 shrink-0">燙金後擊凸:</span><span class="text-gray-700">{{ formatValue(product.postHotStampingEmbossingDebossing) }}</span></div>
      </div>

      <!-- 后加工兼容性 -->
      <div>
        <h4 class="text-xs font-semibold text-gray-700 mb-1.5">燙後加工兼容性</h4>
        <div class="flex flex-wrap gap-2">
          <span v-if="compatLabel(product.uvPrinting)" class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-xs border" :class="compatLabel(product.uvPrinting)!.cls">
            UV 印刷 {{ compatLabel(product.uvPrinting)!.text }}
          </span>
          <span v-if="compatLabel(product.ledUvGlitter)" class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-xs border" :class="compatLabel(product.ledUvGlitter)!.cls">
            LED UV 閃粉 {{ compatLabel(product.ledUvGlitter)!.text }}
          </span>
          <span v-if="compatLabel(product.stampingPrinting)" class="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-xs border" :class="compatLabel(product.stampingPrinting)!.cls">
            燙金+印刷 {{ compatLabel(product.stampingPrinting)!.text }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>
