<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">硅胶桔纹UV兼容性管理</h1>
            <p class="mt-2 text-gray-600">管理硅胶桔纹UV(Orange Peel UV)物料的兼容性配置</p>
          </div>
          <div class="flex space-x-3">
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
              to="/admin/material/post-processing/silicone-orange-peel-uv"
              class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
              </svg>
              返回产品管理
            </router-link>
          </div>
        </div>
      </div>

      <!-- 筛选 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">产品筛选</label>
            <select
              v-model="filterProductId"
              @change="loadCompatibilities"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option :value="0">全部产品</option>
              <option v-for="p in productList" :key="p.id" :value="p.id">
                {{ p.materialName || `ID: ${p.id}` }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">工序筛选</label>
            <select
              v-model="filterStep"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">全部工序</option>
              <option v-for="step in allSteps" :key="step" :value="step">{{ step }}</option>
            </select>
          </div>
          <div class="flex items-end">
            <button
              @click="loadCompatibilities"
              class="w-full bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors mr-2"
            >
              筛选
            </button>
            <button
              @click="resetFilter"
              class="w-full bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-colors"
            >
              重置
            </button>
          </div>
        </div>
      </div>

      <!-- 兼容性列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">兼容性配置列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">产品名称</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">后加工工序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">兼容性</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">排序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in filteredList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.id }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ item.productName || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.postProcessingStep }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="item.compatibilityStatus === 'V' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ item.compatibilityStatus === 'V' ? '兼容' : '不兼容' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ item.displayOrder ?? '-' }}</td>
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

        <div v-if="filteredList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暂无兼容性配置数据</div>
        </div>
      </div>

      <!-- 添加/编辑对话框 -->
      <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-lg shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">{{ formData.id ? '编辑兼容性' : '添加兼容性' }}</h3>
              <button @click="closeDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveItem" class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">产品 *</label>
                <select v-model="formData.productId" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                  <option :value="0">请选择产品</option>
                  <option v-for="p in productList" :key="p.id" :value="p.id">
                    {{ p.materialName || `ID: ${p.id}` }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序 *</label>
                <input v-model="formData.postProcessingStep" required type="text" placeholder="请输入工序名称" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">兼容性 *</label>
                <select v-model="formData.compatibilityStatus" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                  <option value="">请选择</option>
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">排序</label>
                <input v-model.number="formData.displayOrder" type="number" placeholder="显示顺序" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>

              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button type="button" @click="closeDialog" class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">取消</button>
                <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">保存</button>
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
import { siliconeOrangePeelUvApi, type SiliconeOrangePeelUvProduct, type SiliconeOrangePeelUvCompatibility } from '../../../api/modules/siliconeOrangePeelUv'

const compatibilityList = ref<SiliconeOrangePeelUvCompatibility[]>([])
const productList = ref<SiliconeOrangePeelUvProduct[]>([])
const allSteps = ref<string[]>([])

const filterProductId = ref(0)
const filterStep = ref('')

const showAddDialog = ref(false)
const showEditDialog = ref(false)

const defaultForm: SiliconeOrangePeelUvCompatibility = {
  productId: 0,
  postProcessingStep: '',
  compatibilityStatus: '',
  displayOrder: 0
}

const formData = reactive<SiliconeOrangePeelUvCompatibility>({ ...defaultForm })

const filteredList = computed(() => {
  return compatibilityList.value.filter(item => {
    if (filterProductId.value > 0 && item.productId !== filterProductId.value) return false
    if (filterStep.value && item.postProcessingStep !== filterStep.value) return false
    return true
  })
})

onMounted(async () => {
  await Promise.all([
    loadProducts(),
    loadCompatibilities()
  ])
})

const loadProducts = async () => {
  try {
    productList.value = await siliconeOrangePeelUvApi.getAllProducts()
  } catch (error) {
    console.error('加载产品失败:', error)
  }
}

const loadCompatibilities = async () => {
  try {
    compatibilityList.value = await siliconeOrangePeelUvApi.getAllCompatibilities()
    // 提取所有工序名称
    const steps = new Set<string>()
    compatibilityList.value.forEach(c => {
      if (c.postProcessingStep) steps.add(c.postProcessingStep)
    })
    allSteps.value = Array.from(steps).sort()
  } catch (error) {
    console.error('加载兼容性失败:', error)
  }
}

const resetFilter = () => {
  filterProductId.value = 0
  filterStep.value = ''
  loadCompatibilities()
}

const editItem = (item: SiliconeOrangePeelUvCompatibility) => {
  Object.assign(formData, { ...defaultForm, ...item })
  showEditDialog.value = true
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(formData, { ...defaultForm })
}

const saveItem = async () => {
  try {
    if (formData.id) {
      await siliconeOrangePeelUvApi.updateCompatibility(formData.id, { ...formData })
    } else {
      await siliconeOrangePeelUvApi.createCompatibility({ ...formData })
    }
    closeDialog()
    await loadCompatibilities()
    alert('保存成功')
  } catch (error) {
    console.error('保存兼容性失败:', error)
    alert('保存失败')
  }
}

const deleteItem = async (id: number) => {
  if (confirm('确定要删除这个兼容性配置吗？')) {
    try {
      await siliconeOrangePeelUvApi.deleteCompatibility(id)
      await loadCompatibilities()
      alert('删除成功')
    } catch (error) {
      console.error('删除兼容性失败:', error)
      alert('删除失败')
    }
  }
}
</script>
