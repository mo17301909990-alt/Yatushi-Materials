<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <AdminBackToPanel />
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">硅胶粗纹UV兼容性管理</h1>
                <p class="mt-2 text-gray-600">管理硅胶粗纹UV产品与后加工工序的兼容性配置</p>
              </div>
              <div class="flex flex-wrap gap-2 xl:justify-end">
                <button
                  @click="showAddDialog = true"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                  </svg>
                  添加兼容性
                </button>
                <router-link
                  to="/admin/silicone_coarse_uv/product"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                  </svg>
                  产品管理
                </router-link>
                <router-link
                  to="/admin/material/post-processing"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                  </svg>
                  返回
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 搜索和筛选 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">产品</label>
            <select
              v-model="searchProductId"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option :value="0">全部产品</option>
              <option v-for="p in productOptions" :key="p.id" :value="p.id">
                {{ p.materialName }}
              </option>
            </select>
          </div>
          <div class="flex items-end space-x-3">
            <button
              @click="loadData"
              class="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors"
            >
              查询
            </button>
            <button
              @click="resetFilter"
              class="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-colors"
            >
              重置
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
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">后加工工序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">显示顺序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in compatibilityList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {{ item.productName || '-' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ item.postProcessingStep || '-' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="getStatusClass(item.compatibilityStatus)"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ getStatusText(item.compatibilityStatus) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ item.displayOrder ?? '-' }}
                </td>
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
    </div>

    <!-- 添加/编辑对话框 -->
    <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-lg font-medium text-gray-900">
            {{ form.id ? '编辑兼容性' : '添加兼容性' }}
          </h3>
          <button @click="closeDialog" class="text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <form @submit.prevent="saveItem" class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">产品 *</label>
              <select v-model.number="form.productId" required
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              >
                <option :value="0">请选择产品</option>
                <option v-for="p in productOptions" :key="p.id" :value="p.id">
                  {{ p.materialName }}
                </option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序 *</label>
              <input v-model="form.postProcessingStep" required
                placeholder="例如：普通烫金、击凸、过胶"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">兼容性状态 *</label>
              <select v-model="form.compatibilityStatus" required
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              >
                <option value="">请选择</option>
                <option value="V">兼容 (V)</option>
                <option value="X">不兼容 (X)</option>
                <option value="">未标注</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">显示顺序</label>
              <input v-model.number="form.displayOrder" type="number"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              />
            </div>
          </div>

          <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
            <button type="button" @click="closeDialog"
              class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
              取消
            </button>
            <button type="submit"
              class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">
              保存
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import {
  siliconeCoarseUvApi,
  type SiliconeCoarseUvProduct,
  type SiliconeCoarseUvCompatibility
} from '../../../api/modules/silicone_coarse_uv'

const compatibilityList = ref<SiliconeCoarseUvCompatibility[]>([])
const productOptions = ref<SiliconeCoarseUvProduct[]>([])
const searchProductId = ref(0)
const showAddDialog = ref(false)
const showEditDialog = ref(false)

const form = reactive<SiliconeCoarseUvCompatibility>({
  productId: 0,
  postProcessingStep: '',
  compatibilityStatus: '',
  displayOrder: 0
})

const getStatusClass = (status?: string) => {
  if (status === 'V') return 'bg-green-100 text-green-800'
  if (status === 'X') return 'bg-red-100 text-red-800'
  return 'bg-gray-100 text-gray-800'
}

const getStatusText = (status?: string) => {
  if (status === 'V') return '兼容'
  if (status === 'X') return '不兼容'
  return '未标注'
}

const loadProducts = async () => {
  try {
    productOptions.value = await siliconeCoarseUvApi.getAllProducts()
  } catch (error) {
    console.error('加载产品列表失败:', error)
  }
}

const loadData = async () => {
  try {
    if (searchProductId.value && searchProductId.value > 0) {
      compatibilityList.value = await siliconeCoarseUvApi.getCompatibilitiesByProductId(searchProductId.value)
    } else {
      compatibilityList.value = await siliconeCoarseUvApi.getAllCompatibilities()
    }
  } catch (error) {
    console.error('加载兼容性数据失败:', error)
  }
}

const resetFilter = () => {
  searchProductId.value = 0
  loadData()
}

const editItem = (item: SiliconeCoarseUvCompatibility) => {
  Object.assign(form, {
    id: item.id,
    productId: item.productId,
    postProcessingStep: item.postProcessingStep,
    compatibilityStatus: item.compatibilityStatus,
    displayOrder: item.displayOrder ?? 0
  })
  showEditDialog.value = true
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(form, {
    id: undefined,
    productId: 0,
    postProcessingStep: '',
    compatibilityStatus: '',
    displayOrder: 0
  })
}

const saveItem = async () => {
  try {
    if (!form.productId || form.productId === 0) {
      alert('请选择产品')
      return
    }
    if (!form.postProcessingStep) {
      alert('请输入后加工工序')
      return
    }

    if (form.id) {
      await siliconeCoarseUvApi.updateCompatibility(form.id, form)
    } else {
      await siliconeCoarseUvApi.createCompatibility(form)
    }
    closeDialog()
    await loadData()
    alert('保存成功')
  } catch (error: any) {
    console.error('保存兼容性失败:', error)
    alert(error.response?.data?.message || '保存失败')
  }
}

const deleteItem = async (id: number) => {
  if (!confirm('确定要删除这个兼容性配置吗？')) return
  try {
    const result = await siliconeCoarseUvApi.deleteCompatibility(id)
    if (result.success) {
      await loadData()
      alert('删除成功')
    } else {
      alert(result.message)
    }
  } catch (error: any) {
    console.error('删除兼容性失败:', error)
    alert(error.response?.data?.message || '删除失败')
  }
}

onMounted(async () => {
  await loadProducts()
  await loadData()
})
</script>
