<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">UV油_哑光水油兼容性管理</h1>
                <p class="mt-2 text-gray-600">管理UV油_哑光水油产品的后加工兼容性配置</p>
              </div>
              <div class="flex flex-wrap gap-2 xl:justify-end">
                <button
                  @click="openAddDialog"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                  </svg>
                  添加兼容性
                </button>
                <router-link
                  to="/admin/material/water-varnish-matte/products"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  返回产品管理
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 当前产品信息 -->
      <div v-if="currentProduct" class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
        <div class="flex items-center justify-between">
          <div>
            <span class="text-sm font-medium text-blue-900">当前产品：</span>
            <span class="text-sm text-blue-700">{{ currentProduct.materialName }} ({{ currentProduct.materialCode }})</span>
          </div>
          <button @click="clearProductFilter" class="text-sm text-blue-600 hover:text-blue-800">清除筛选</button>
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
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">产品名称</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">后加工工序</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">兼容性状态</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">显示顺序</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in compatibilityList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{{ item.id }}</td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{{ getProductName(item.productId) }}</td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{{ item.postProcessingStep || '-' }}</td>
                <td class="px-4 py-3 whitespace-nowrap">
                  <span
                    :class="statusClass(item.compatibilityStatus)"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ statusLabel(item.compatibilityStatus) }}
                  </span>
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{{ item.displayOrder ?? '-' }}</td>
                <td class="px-4 py-3 whitespace-nowrap text-sm font-medium space-x-2">
                  <button @click="editCompatibility(item)" class="text-blue-600 hover:text-blue-900">编辑</button>
                  <button @click="deleteCompatibility(item.id!)" class="text-red-600 hover:text-red-900">删除</button>
                </td>
              </tr>
              <tr v-if="compatibilityList.length === 0">
                <td colspan="6" class="px-4 py-12 text-center text-gray-500">暂无兼容性数据</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 添加/编辑兼容性对话框 -->
    <div v-if="showDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-lg font-medium text-gray-900">{{ form.id ? '编辑兼容性' : '添加兼容性' }}</h3>
          <button @click="closeDialog" class="text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </button>
        </div>

        <form @submit.prevent="saveCompatibility" class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">产品 *</label>
              <select v-model="form.productId" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                <option :value="undefined">请选择产品</option>
                <option v-for="p in productList" :key="p.id" :value="p.id">{{ p.materialName }} ({{ p.materialCode }})</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序 *</label>
              <select v-model="form.postProcessingStep" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                <option value="">请选择工序</option>
                <option v-for="step in processingSteps" :key="step" :value="step">{{ step }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">兼容性状态 *</label>
              <select v-model="form.compatibilityStatus" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                <option value="">请选择</option>
                <option value="V">兼容 (V)</option>
                <option value="X">不兼容 (X)</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">显示顺序</label>
              <input v-model.number="form.displayOrder" type="number" placeholder="0" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
          </div>

          <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
            <button type="button" @click="closeDialog" class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">取消</button>
            <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { waterVarnishMatteApi, type WaterVarnishMatteProduct, type WaterVarnishMatteCompatibility } from '../../../api/modules/waterVarnishMatte'

const route = useRoute()

// 数据
const compatibilityList = ref<WaterVarnishMatteCompatibility[]>([])
const productList = ref<WaterVarnishMatteProduct[]>([])
const processingSteps = ref<string[]>([])
const currentProduct = ref<WaterVarnishMatteProduct | null>(null)

// 对话框状态
const showDialog = ref(false)
const form = reactive<Partial<WaterVarnishMatteCompatibility>>({
  compatibilityStatus: 'V',
  displayOrder: 0
})

const editForm = reactive<Partial<WaterVarnishMatteCompatibility>>({})

// 获取产品名称
const getProductName = (productId?: number) => {
  if (!productId) return '-'
  const product = productList.value.find(p => p.id === productId)
  return product ? product.materialName || `ID: ${productId}` : `ID: ${productId}`
}

// 状态相关
const statusClass = (status?: string) => {
  if (status === 'V') return 'bg-green-100 text-green-800'
  if (status === 'X') return 'bg-red-100 text-red-800'
  return 'bg-gray-100 text-gray-800'
}

const statusLabel = (status?: string) => {
  if (status === 'V') return '兼容'
  if (status === 'X') return '不兼容'
  return status || '-'
}

// 加载列表
const loadCompatibilities = async () => {
  try {
    const productId = route.query.productId ? Number(route.query.productId) : undefined
    compatibilityList.value = await waterVarnishMatteApi.getCompatibilities(productId)

    if (productId) {
      currentProduct.value = productList.value.find(p => p.id === productId) || null
    } else {
      currentProduct.value = null
    }
  } catch (error) {
    console.error('加载兼容性列表失败:', error)
  }
}

const loadProducts = async () => {
  try {
    productList.value = await waterVarnishMatteApi.getAllProductsIncludingInactive()
  } catch (error) {
    console.error('加载产品列表失败:', error)
  }
}

const loadProcessingSteps = async () => {
  try {
    processingSteps.value = await waterVarnishMatteApi.getAllPostProcessingSteps()
  } catch (error) {
    console.error('加载工序步骤失败:', error)
  }
}

const clearProductFilter = () => {
  const { products: _products, ...rest } = route.query as any
  // 重新加载全部
  loadCompatibilities()
}

// 对话框操作
const openAddDialog = () => {
  Object.assign(form, {
    productId: currentProduct.value?.id || undefined,
    compatibilityStatus: 'V',
    displayOrder: 0,
    postProcessingStep: ''
  })
  showDialog.value = true
}

const closeDialog = () => {
  showDialog.value = false
  Object.assign(form, {})
  delete (form as any).id
}

const editCompatibility = (item: WaterVarnishMatteCompatibility) => {
  Object.assign(form, item)
  showDialog.value = true
}

const saveCompatibility = async () => {
  try {
    if (!form.productId || !form.postProcessingStep || !form.compatibilityStatus) {
      alert('请填写必填字段')
      return
    }

    const data: WaterVarnishMatteCompatibility = {
      productId: form.productId,
      postProcessingStep: form.postProcessingStep,
      compatibilityStatus: form.compatibilityStatus,
      displayOrder: form.displayOrder || 0
    }

    if ((form as any).id) {
      await waterVarnishMatteApi.updateCompatibility((form as any).id, { ...data, id: (form as any).id })
    } else {
      await waterVarnishMatteApi.saveCompatibility(data)
    }

    closeDialog()
    await loadCompatibilities()
  } catch (error: any) {
    const msg = error?.response?.data?.message || error.message || '保存失败'
    alert(msg)
  }
}

const deleteCompatibility = async (id: number) => {
  if (!confirm('确定要删除这个兼容性配置吗？')) return
  try {
    await waterVarnishMatteApi.deleteCompatibility(id)
    await loadCompatibilities()
  } catch (error) {
    console.error('删除兼容性失败:', error)
    alert('删除失败')
  }
}

onMounted(async () => {
  await loadProducts()
  await loadProcessingSteps()
  await loadCompatibilities()
})
</script>
