<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">硅膠白UV兼容性管理</h1>
        <p class="mt-2 text-gray-600">查看所有硅膠白UV產品的兼容性配置</p>
      </div>

      <!-- 篩選條件 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">產品</label>
            <select v-model="filterProductId" @change="loadData" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
              <option :value="undefined">全部產品</option>
              <option v-for="product in productOptions" :key="product.id" :value="product.id">{{ product.materialName }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">兼容性狀態</label>
            <select v-model="filterStatus" @change="loadData" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
              <option value="">全部</option>
              <option value="V">兼容</option>
              <option value="X">不兼容</option>
            </select>
          </div>
          <div class="flex items-end">
            <button @click="resetFilter" class="w-full bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-colors">
              重置篩選
            </button>
          </div>
        </div>
      </div>

      <!-- 兼容性列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">兼容性列表 ({{ compatibilityList.length }})</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">產品名稱</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">後加工工序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">顯示順序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in filteredList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ item.productName || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.postProcessingStep }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="item.compatibilityStatus === 'V' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'" class="inline-flex px-2 py-1 text-xs font-semibold rounded-full">
                    {{ item.compatibilityStatus === 'V' ? '兼容' : '不兼容' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.displayOrder ?? '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button @click="deleteItem(item.id!)" class="text-red-600 hover:text-red-900">刪除</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="filteredList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暫無兼容性數據</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { siliconeWhiteUvApi, type SiliconeWhiteUvProduct, type SiliconeWhiteUvCompatibility } from '../../../api/modules/silicone_white_uv'

const compatibilityList = ref<SiliconeWhiteUvCompatibility[]>([])
const productOptions = ref<SiliconeWhiteUvProduct[]>([])
const filterProductId = ref<number | undefined>(undefined)
const filterStatus = ref('')

const filteredList = computed(() => {
  let list = compatibilityList.value
  if (filterProductId.value !== undefined) {
    list = list.filter(item => item.productId === filterProductId.value)
  }
  if (filterStatus.value) {
    list = list.filter(item => item.compatibilityStatus === filterStatus.value)
  }
  return list
})

onMounted(async () => {
  await Promise.all([loadData(), loadProducts()])
})

const loadData = async () => {
  try {
    if (filterProductId.value !== undefined) {
      compatibilityList.value = await siliconeWhiteUvApi.getCompatibilitiesByProductId(filterProductId.value)
    } else {
      compatibilityList.value = await siliconeWhiteUvApi.getCompatibilitiesByProductId()
    }
  } catch (error) {
    console.error('加載兼容性數據失敗:', error)
  }
}

const loadProducts = async () => {
  try {
    productOptions.value = await siliconeWhiteUvApi.getAllProducts()
  } catch (error) {
    console.error('加載產品列表失敗:', error)
  }
}

const resetFilter = () => {
  filterProductId.value = undefined
  filterStatus.value = ''
  loadData()
}

const deleteItem = async (id: number) => {
  if (!confirm('確定要刪除此兼容性配置嗎？')) return
  try {
    await siliconeWhiteUvApi.deleteCompatibility(id)
    await loadData()
    alert('刪除成功')
  } catch (error) {
    console.error('刪除失敗:', error)
    alert('刪除失敗')
  }
}
</script>
