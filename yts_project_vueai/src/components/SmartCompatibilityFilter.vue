<template>
  <div class="bg-white rounded-lg shadow-md p-6">
    <h3 class="text-lg font-medium text-gray-900 mb-4">智能兼容性篩選</h3>
    <p class="text-sm text-gray-600 mb-6">根據產品類型、圖案特徵、燙金類型和工序條件篩選適用的燙金紙性能類型</p>
    
    <!-- 篩選條件 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mb-6">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">產品類型</label>
        <select
          v-model="filterParams.productTypeId"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <option value="">請選擇產品類型</option>
          <option v-for="type in productTypes" :key="type.id" :value="type.id">{{ type.name }}</option>
        </select>
      </div>
      
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">圖案特徵</label>
        <select
          v-model="filterParams.patternCharacteristicId"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <option value="">請選擇圖案特徵</option>
          <option v-for="pattern in patternCharacteristics" :key="pattern.id" :value="pattern.id">{{ pattern.name }}</option>
        </select>
      </div>
      
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">燙金類型</label>
        <select
          v-model="filterParams.hotStampingTypeId"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <option value="">請選擇燙金類型</option>
          <option v-for="type in hotStampingTypes" :key="type.id" :value="type.id">{{ type.name }}</option>
        </select>
      </div>
      
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">前工序</label>
        <select
          v-model="filterParams.preProcessingStepId"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <option value="">請選擇前工序</option>
          <option v-for="step in preProcessingSteps" :key="step.id" :value="step.id">{{ step.name }}</option>
        </select>
      </div>
      
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-2">後工序</label>
        <select
          v-model="filterParams.postProcessingStepId"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
        >
          <option value="">請選擇後工序</option>
          <option v-for="step in postProcessingSteps" :key="step.id" :value="step.id">{{ step.name }}</option>
        </select>
      </div>
      
      <div class="flex items-end">
        <button
          @click="performFilter"
          :disabled="loading"
          class="w-full bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 disabled:opacity-50 transition-colors"
        >
          {{ loading ? '篩選中...' : '開始篩選' }}
        </button>
      </div>
    </div>

    <!-- 篩選結果 -->
    <div v-if="filterResults.length > 0" class="mt-6">
      <h4 class="text-md font-medium text-gray-900 mb-3">篩選結果</h4>
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3">
        <div
          v-for="result in filterResults"
          :key="result"
          class="bg-green-50 border border-green-200 rounded-md p-3"
        >
          <div class="flex items-center">
            <svg class="w-5 h-5 text-green-500 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
            </svg>
            <span class="text-sm font-medium text-green-800">{{ result }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 無結果提示 -->
    <div v-else-if="hasSearched && !loading" class="mt-6">
      <div class="bg-yellow-50 border border-yellow-200 rounded-md p-4">
        <div class="flex items-center">
          <svg class="w-5 h-5 text-yellow-500 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
          </svg>
          <span class="text-sm text-yellow-800">根據當前條件未找到適用的燙金紙性能類型</span>
        </div>
      </div>
    </div>

    <!-- 详细规则信息 -->
    <div v-if="detailedRules.length > 0" class="mt-6">
      <h4 class="text-md font-medium text-gray-900 mb-3">详细兼容性规则</h4>
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品类型</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">图案特征</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金类型</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性</th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">参考编号</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="rule in detailedRules" :key="rule.id">
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ rule.paperPerformance }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ rule.productType }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ rule.patternCharacteristic }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ rule.hotStampingType }}</td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span
                  :class="[
                    'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                    rule.compatibility === 'V' 
                      ? 'bg-green-100 text-green-800' 
                      : 'bg-red-100 text-red-800'
                  ]"
                >
                  {{ rule.compatibility === 'V' ? '兼容' : '不兼容' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ rule.referenceCode || '-' }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { smartCompatibilityApi } from '../api/modules/smartCompatibility'

// 响应式数据
const loading = ref(false)
const hasSearched = ref(false)
const filterResults = ref<string[]>([])
const detailedRules = ref<any[]>([])

// 筛选参数
const filterParams = reactive({
  productTypeId: '',
  patternCharacteristicId: '',
  hotStampingTypeId: '',
  preProcessingStepId: '',
  postProcessingStepId: ''
})

// 选项数据（模拟数据，实际应该从API获取）
const productTypes = ref([
  { id: 1, name: '精平裝/咭書' },
  { id: 2, name: '賀咭/紙袋' },
  { id: 3, name: '包裝' }
])

const patternCharacteristics = ref([
  { id: 1, name: '大面積' },
  { id: 2, name: '中面積' },
  { id: 3, name: '小面積' },
  { id: 4, name: '漸變、網點' },
  { id: 5, name: '細線條、字母' }
])

const hotStampingTypes = ref([
  { id: 1, name: '平面燙金' },
  { id: 2, name: '平面燙金-於中間位' },
  { id: 3, name: '平面燙金-到邊位' },
  { id: 4, name: '立體燙金' },
  { id: 5, name: '磨砂燙金' },
  { id: 6, name: '折光燙金' }
])

const preProcessingSteps = ref([
  { id: 1, name: '前工序1' },
  { id: 2, name: '前工序2' },
  { id: 3, name: '前工序3' }
])

const postProcessingSteps = ref([
  { id: 1, name: '後工序1' },
  { id: 2, name: '後工序2' },
  { id: 3, name: '後工序3' }
])

// 执行筛选
const performFilter = async () => {
  loading.value = true
  hasSearched.value = true
  
  try {
    // 构建查询参数
    const params: any = {}
    if (filterParams.productTypeId) params.productTypeId = parseInt(filterParams.productTypeId)
    if (filterParams.patternCharacteristicId) params.patternCharacteristicId = parseInt(filterParams.patternCharacteristicId)
    if (filterParams.hotStampingTypeId) params.hotStampingTypeId = parseInt(filterParams.hotStampingTypeId)
    if (filterParams.preProcessingStepId) params.preProcessingStepId = parseInt(filterParams.preProcessingStepId)
    if (filterParams.postProcessingStepId) params.postProcessingStepId = parseInt(filterParams.postProcessingStepId)
    
    // 调用API筛选烫金纸性能类型
    const paperPerformanceTypes = await smartCompatibilityApi.filterPaperPerformance(params)
    filterResults.value = paperPerformanceTypes
    
    // 获取详细的兼容性规则
    const rules = await smartCompatibilityApi.getCompatibilityRules(params)
    detailedRules.value = rules
    
  } catch (error) {
    console.error('筛选失败:', error)
    filterResults.value = []
    detailedRules.value = []
  } finally {
    loading.value = false
  }
}
</script>
