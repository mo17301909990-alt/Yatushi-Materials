<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">硅膠水彩油墨 - 兼容性管理</h1>
                <p class="mt-2 text-gray-600">管理硅膠水彩油墨與後加工工序的兼容性配置</p>
              </div>
              <div class="flex flex-wrap gap-2">
                <button
                  @click="showAddDialog = true"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                  </svg>
                  添加兼容性
                </button>
                <button
                  @click="openBatchAddDialog"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                  </svg>
                  批量添加
                </button>
                <router-link
                  to="/admin/silicone_watercolor_ink/product"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                  </svg>
                  返回產品管理
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 搜索與篩選 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">產品</label>
            <select v-model="searchProductId" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
              <option value="">全部產品</option>
              <option v-for="p in productOptions" :key="p.id" :value="p.id">{{ p.materialName }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">兼容性狀態</label>
            <select v-model="searchStatus" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
              <option value="">全部</option>
              <option value="V">兼容</option>
              <option value="X">不兼容</option>
              <option value="▷">有條件兼容</option>
            </select>
          </div>
        </div>
      </div>

      <!-- 兼容性列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">兼容性列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">產品</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">後加工工序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">兼容性</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">排序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in filteredList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ item.productName || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.postProcessingStep || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="statusClass(item.compatibilityStatus)"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ statusLabel(item.compatibilityStatus) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.displayOrder ?? 0 }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center space-x-2">
                    <button @click="editItem(item)" class="text-green-600 hover:text-green-900">編輯</button>
                    <button @click="deleteItem(item.id as number)" class="text-red-600 hover:text-red-900">刪除</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="filteredList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暫無兼容性數據</div>
        </div>
      </div>

      <!-- 添加/編輯兼容性對話框 -->
      <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-lg shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">
                {{ form.id ? '編輯兼容性' : '添加兼容性' }}
              </h3>
              <button @click="closeForm" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveItem" class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">產品 *</label>
                <select v-model="form.productId" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                  <option :value="undefined">請選擇產品</option>
                  <option v-for="p in productOptions" :key="p.id" :value="p.id">{{ p.materialName }}</option>
                </select>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">後加工工序 *</label>
                <input v-model="form.postProcessingStep" type="text" required placeholder="如：單面印刷、普通燙金"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">兼容性狀態 *</label>
                <select v-model="form.compatibilityStatus" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
                  <option value="▷">有條件兼容</option>
                </select>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">顯示排序</label>
                <input v-model.number="form.displayOrder" type="number" min="0"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>

              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button type="button" @click="closeForm"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
                  取消
                </button>
                <button type="submit"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">
                  {{ form.id ? '更新' : '保存' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 批量添加對話框 -->
      <div v-if="showBatchAddDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">批量添加兼容性</h3>
              <button @click="closeBatchAddDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveBatchAdd" class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">選擇產品（可多選）*</label>
                <div class="border border-gray-300 rounded-md p-3 max-h-48 overflow-y-auto">
                  <label v-for="p in productOptions" :key="p.id" class="flex items-center space-x-2 cursor-pointer hover:bg-gray-50 p-2 rounded">
                    <input type="checkbox" :value="p.id" v-model="batchForm.productIds"
                      class="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded" />
                    <span class="text-sm text-gray-700">{{ p.materialName }}</span>
                  </label>
                  <div v-if="productOptions.length === 0" class="text-center py-4 text-sm text-gray-500">暫無產品</div>
                </div>
                <p class="mt-1 text-xs text-gray-500">已選擇 {{ batchForm.productIds.length }} 個產品</p>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">後加工工序 *</label>
                <input v-model="batchForm.postProcessingStep" type="text" required placeholder="如：單面印刷、普通燙金"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">兼容性狀態 *</label>
                <select v-model="batchForm.compatibilityStatus" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
                  <option value="▷">有條件兼容</option>
                </select>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">顯示排序</label>
                <input v-model.number="batchForm.displayOrder" type="number" min="0"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>

              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button type="button" @click="closeBatchAddDialog"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
                  取消
                </button>
                <button type="submit"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">
                  批量添加
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { siliconeWatercolorInkApi, type SiliconeWatercolorInkProduct, type SiliconeWatercolorInkCompatibility } from '../../../api/modules/silicone_watercolor_ink'

// 數據
const compatibilityList = ref<SiliconeWatercolorInkCompatibility[]>([])
const productOptions = ref<SiliconeWatercolorInkProduct[]>([])

// 搜索篩選
const searchProductId = ref<number | ''>('')
const searchStatus = ref('')

// 表單
const form = ref<SiliconeWatercolorInkCompatibility>({
  productId: undefined,
  postProcessingStep: '',
  compatibilityStatus: 'V',
  displayOrder: 0
})
const batchForm = ref({
  productIds: [] as number[],
  postProcessingStep: '',
  compatibilityStatus: 'V',
  displayOrder: 0
})

// 對話框
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showBatchAddDialog = ref(false)

// 過濾列表
const filteredList = computed(() => {
  let list = compatibilityList.value
  if (searchProductId.value) {
    list = list.filter(item => item.productId === searchProductId.value)
  }
  if (searchStatus.value) {
    list = list.filter(item => item.compatibilityStatus === searchStatus.value)
  }
  return list
})

// 狀態樣式
const statusClass = (status?: string) => {
  switch (status) {
    case 'V': return 'bg-green-100 text-green-800'
    case 'X': return 'bg-red-100 text-red-800'
    case '▷': return 'bg-yellow-100 text-yellow-800'
    default: return 'bg-gray-100 text-gray-800'
  }
}

const statusLabel = (status?: string) => {
  switch (status) {
    case 'V': return '兼容'
    case 'X': return '不兼容'
    case '▷': return '有條件兼容'
    default: return status || '-'
  }
}

// 加載數據
const loadData = async () => {
  try {
    compatibilityList.value = await siliconeWatercolorInkApi.getAllCompatibilities()
    productOptions.value = await siliconeWatercolorInkApi.getAllProducts()
  } catch (error) {
    console.error('加載數據失敗:', error)
  }
}

// 保存
const saveItem = async () => {
  try {
    if (form.value.id) {
      await siliconeWatercolorInkApi.updateCompatibility(form.value.id, form.value)
    } else {
      await siliconeWatercolorInkApi.createCompatibility(form.value)
    }
    closeForm()
    await loadData()
    alert(form.value.id ? '更新成功' : '添加成功')
  } catch (error: any) {
    const msg = error.response?.data?.message || error.message || '保存失敗'
    alert(msg)
  }
}

// 編輯
const editItem = (item: SiliconeWatercolorInkCompatibility) => {
  form.value = { ...item }
  showEditDialog.value = true
}

// 刪除
const deleteItem = async (id: number) => {
  if (!confirm('確定要刪除此兼容性配置嗎？')) return
  try {
    await siliconeWatercolorInkApi.deleteCompatibility(id)
    await loadData()
    alert('刪除成功')
  } catch (error) {
    console.error('刪除失敗:', error)
    alert('刪除失敗')
  }
}

// 批量添加
const saveBatchAdd = async () => {
  if (batchForm.value.productIds.length === 0) {
    alert('請選擇至少一個產品')
    return
  }
  try {
    const items: SiliconeWatercolorInkCompatibility[] = batchForm.value.productIds.map(productId => ({
      productId,
      postProcessingStep: batchForm.value.postProcessingStep,
      compatibilityStatus: batchForm.value.compatibilityStatus,
      displayOrder: batchForm.value.displayOrder
    }))
    await siliconeWatercolorInkApi.batchSaveCompatibility(items)
    closeBatchAddDialog()
    await loadData()
    alert('批量添加成功')
  } catch (error) {
    console.error('批量添加失敗:', error)
    alert('批量添加失敗')
  }
}

// 關閉表單
const closeForm = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  form.value = { productId: undefined, postProcessingStep: '', compatibilityStatus: 'V', displayOrder: 0 }
}

const closeBatchAddDialog = () => {
  showBatchAddDialog.value = false
  batchForm.value = { productIds: [], postProcessingStep: '', compatibilityStatus: 'V', displayOrder: 0 }
}

const openBatchAddDialog = () => {
  batchForm.value = { productIds: [], postProcessingStep: '', compatibilityStatus: 'V', displayOrder: 0 }
  showBatchAddDialog.value = true
}

onMounted(() => {
  loadData()
})
</script>
