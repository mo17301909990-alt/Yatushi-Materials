<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">硅胶皱纹UV兼容性管理</h1>
                <p class="mt-2 text-gray-600">管理硅胶皱纹UV物料与后加工工序的兼容性配置</p>
              </div>
              <div class="flex flex-wrap gap-2 xl:justify-end">
                <button
                  @click="showAddDialog = true"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                  </svg>
                  添加兼容性
                </button>
                <router-link
                  to="/admin/material/silicone-wrinkle-uv"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                  </svg>
                  产品管理
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 搜索与筛选 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">产品名称</label>
            <select
              v-model="searchProductId"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option :value="undefined">全部产品</option>
              <option v-for="p in productOptions" :key="p.id" :value="p.id">
                {{ p.materialName }}
              </option>
            </select>
          </div>
          <div class="flex items-end">
            <button @click="loadCompatibilities" class="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors">
              查询
            </button>
          </div>
        </div>
      </div>

      <!-- 批量操作工具栏 -->
      <div v-if="selectedItems.length > 0" class="bg-indigo-50 border border-indigo-200 rounded-lg p-4 mb-6">
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <span class="text-sm font-medium text-indigo-900 mr-4">已选中 {{ selectedItems.length }} 条记录</span>
            <button @click="clearSelection" class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-red-600 hover:bg-red-700 mr-2">
              批量删除
            </button>
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
                <th class="px-6 py-3 text-left">
                  <input type="checkbox" :checked="selectedItems.length === compatibilityList.length && compatibilityList.length > 0"
                    @change="toggleSelectAll" class="rounded border-gray-300 text-green-600 focus:ring-green-500" />
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">产品名称</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">工序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">兼容性</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">显示顺序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in compatibilityList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <input type="checkbox" :checked="selectedItems.includes(item.id as number)"
                    @change="toggleSelectItem(item.id as number)" class="rounded border-gray-300 text-green-600 focus:ring-green-500" />
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ getProductName(item.productId) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.postProcessingStep || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span :class="compatibilityClass(item.compatibilityStatus)"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full">
                    {{ compatibilityLabel(item.compatibilityStatus) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.displayOrder }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center space-x-2">
                    <button @click="editItem(item)" class="text-green-600 hover:text-green-900">编辑</button>
                    <button @click="deleteItem(item.id as number)" class="text-red-600 hover:text-red-900">删除</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="compatibilityList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暂无兼容性数据</div>
        </div>
      </div>

      <!-- 添加/编辑对话框 -->
      <div v-if="showAddDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">{{ editForm.id ? '编辑兼容性' : '添加兼容性' }}</h3>
              <button @click="closeAddDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>
            <form @submit.prevent="saveCompatibility" class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">产品 *</label>
                <select v-model="editForm.productId" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                  <option :value="0">请选择产品</option>
                  <option v-for="p in productOptions" :key="p.id" :value="p.id">
                    {{ p.materialName }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序 *</label>
                <input v-model="editForm.postProcessingStep" type="text" required placeholder="输入工序名称" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">兼容性状态 *</label>
                <select v-model="editForm.compatibilityStatus" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                  <option value="">请选择</option>
                  <option value="V">V - 兼容</option>
                  <option value="X">X - 不兼容</option>
                  <option value="▷">▷ - 有条件兼容</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">显示顺序</label>
                <input v-model.number="editForm.displayOrder" type="number" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>
              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button type="button" @click="closeAddDialog" class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">取消</button>
                <button type="submit" class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">保存</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { siliconeWrinkleUvApi, type SiliconeWrinkleUvProduct, type SiliconeWrinkleUvCompatibility } from '../../../api/modules/siliconeWrinkleUv'

const compatibilityList = ref<SiliconeWrinkleUvCompatibility[]>([])
const productOptions = ref<SiliconeWrinkleUvProduct[]>([])
const searchProductId = ref<number | undefined>(undefined)
const selectedItems = ref<number[]>([])
const showAddDialog = ref(false)

const editForm = reactive<SiliconeWrinkleUvCompatibility>({
  productId: 0,
  postProcessingStep: '',
  compatibilityStatus: '',
  displayOrder: 0
})

const loadProducts = async () => {
  try {
    productOptions.value = await siliconeWrinkleUvApi.getActiveProducts()
  } catch (error) {
    console.error('加载产品列表失败:', error)
  }
}

const loadCompatibilities = async () => {
  try {
    if (searchProductId.value) {
      compatibilityList.value = await siliconeWrinkleUvApi.getCompatibilitiesByProductId(searchProductId.value)
    } else {
      compatibilityList.value = await siliconeWrinkleUvApi.getAllCompatibilities()
    }
  } catch (error) {
    console.error('加载兼容性列表失败:', error)
  }
}

const getProductName = (productId: number): string => {
  const product = productOptions.value.find(p => p.id === productId)
  return product?.materialName || `ID: ${productId}`
}

const compatibilityClass = (status: string | undefined): string => {
  if (status === 'V') return 'bg-green-100 text-green-800'
  if (status === 'X') return 'bg-red-100 text-red-800'
  if (status === '▷') return 'bg-yellow-100 text-yellow-800'
  return 'bg-gray-100 text-gray-800'
}

const compatibilityLabel = (status: string | undefined): string => {
  if (status === 'V') return '兼容'
  if (status === 'X') return '不兼容'
  if (status === '▷') return '有条件'
  return status || '-'
}

const toggleSelectAll = () => {
  if (selectedItems.value.length === compatibilityList.value.length) {
    selectedItems.value = []
  } else {
    selectedItems.value = compatibilityList.value.map(item => item.id as number)
  }
}

const toggleSelectItem = (id: number) => {
  const index = selectedItems.value.indexOf(id)
  if (index > -1) {
    selectedItems.value.splice(index, 1)
  } else {
    selectedItems.value.push(id)
  }
}

const clearSelection = async () => {
  if (!confirm(`确定删除选中的 ${selectedItems.value.length} 条记录？`)) return
  try {
    await siliconeWrinkleUvApi.batchDeleteCompatibilities(selectedItems.value)
    selectedItems.value = []
    await loadCompatibilities()
    alert('批量删除成功')
  } catch (error) {
    console.error('批量删除失败:', error)
    alert('批量删除失败')
  }
}

const editItem = (item: SiliconeWrinkleUvCompatibility) => {
  Object.assign(editForm, item)
  showAddDialog.value = true
}

const saveCompatibility = async () => {
  try {
    if (editForm.id) {
      await siliconeWrinkleUvApi.updateCompatibility(editForm.id, editForm)
    } else {
      await siliconeWrinkleUvApi.saveCompatibility(editForm)
    }
    closeAddDialog()
    await loadCompatibilities()
    alert('保存成功')
  } catch (error: any) {
    console.error('保存兼容性失败:', error)
    const msg = error.response?.data?.message || error.message || '保存失败'
    alert(msg)
  }
}

const deleteItem = async (id: number) => {
  if (!confirm('确定删除该兼容性配置？')) return
  try {
    await siliconeWrinkleUvApi.deleteCompatibility(id)
    await loadCompatibilities()
    alert('删除成功')
  } catch (error) {
    console.error('删除兼容性失败:', error)
    alert('删除失败')
  }
}

const closeAddDialog = () => {
  showAddDialog.value = false
  Object.assign(editForm, {
    id: undefined,
    productId: 0,
    postProcessingStep: '',
    compatibilityStatus: '',
    displayOrder: 0
  })
}

onMounted(() => {
  loadProducts()
  loadCompatibilities()
})
</script>
