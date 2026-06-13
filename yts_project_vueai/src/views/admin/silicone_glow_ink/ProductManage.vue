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
                <h1 class="text-3xl font-bold text-gray-900">硅胶发光油墨产品管理</h1>
                <p class="mt-2 text-gray-600">管理硅胶发光油墨产品信息</p>
              </div>
              <div class="flex flex-wrap gap-2 xl:justify-end">
                <button
                  @click="showAddDialog = true"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                  </svg>
                  添加产品
                </button>
                <router-link
                  to="/admin/material/silicone-glow-ink/compatibility"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
                  </svg>
                  兼容性管理
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 搜索栏 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">搜索</label>
            <input
              v-model="searchKeyword"
              @input="onSearch"
              type="text"
              placeholder="输入物料名称/编码/颜色搜索"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
        </div>
        <div class="flex items-end space-x-3 mt-4">
          <button
            @click="searchProducts"
            class="flex-1 bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors"
          >
            搜索
          </button>
          <button
            @click="resetSearch"
            class="flex-1 bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-colors"
          >
            重置
          </button>
        </div>
      </div>

      <!-- 产品列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">硅胶发光油墨产品列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">物料编码</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">物料名称</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">物料型号/编号</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">颜色</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">材质</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in productList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.materialCode || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ item.materialName || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.stockCode || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.color || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.category || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="item.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ item.isActive ? '启用' : '禁用' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center space-x-2">
                    <button @click="editProduct(item)" class="text-green-600 hover:text-green-900">编辑</button>
                    <button @click="deleteProduct(item.id as number)" class="text-red-600 hover:text-red-900">删除</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 空状态 -->
        <div v-if="productList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暂无产品数据</div>
        </div>
      </div>

      <!-- 添加/编辑对话框 -->
      <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">{{ showEditDialog ? '编辑产品' : '添加产品' }}</h3>
              <button @click="closeDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveProduct" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料编码</label>
                  <input v-model="form.materialCode" type="text" placeholder="物料编码"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">供应商编号</label>
                  <input v-model="form.supplierCode" type="text" placeholder="供应商编号"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料型号/编号 *</label>
                  <input v-model="form.stockCode" type="text" placeholder="物料型号/编号" required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料名称 *</label>
                  <input v-model="form.materialName" type="text" placeholder="物料名称" required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">材质</label>
                  <input v-model="form.category" type="text" placeholder="材质"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">颜色</label>
                  <input v-model="form.color" type="text" placeholder="颜色"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">测试员</label>
                  <input v-model="form.responsiblePerson" type="text" placeholder="测试员"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">用纸尺寸(最小)</label>
                  <input v-model="form.minSheetSize" type="text" placeholder="最小尺寸"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">用纸尺寸(最大)</label>
                  <input v-model="form.maxSheetSize" type="text" placeholder="最大尺寸"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">物料用途</label>
                <textarea v-model="form.usage" placeholder="物料用途" rows="2"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">设计限制信息</label>
                <textarea v-model="form.designInfo" placeholder="设计限制信息(点/线/间距等)" rows="2"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">适用界面</label>
                <textarea v-model="form.applicableInterface" placeholder="适用界面(纸张面/印刷油墨面/后加工涂层面)" rows="2"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">注意事项</label>
                <textarea v-model="form.notes" placeholder="注意事项、限制的备注与说明" rows="2"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div class="flex items-center">
                <input v-model="form.isActive" type="checkbox" class="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded" />
                <label class="ml-2 text-sm text-gray-700">启用</label>
              </div>

              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button type="button" @click="closeDialog"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition-colors">
                  取消
                </button>
                <button type="submit"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors">
                  保存
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
import { ref, onMounted } from 'vue'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import { siliconeGlowInkApi } from '@/api/modules/silicone_glow_ink'
import type { SiliconeGlowInkProduct } from '@/api/modules/silicone_glow_ink'

const productList = ref<SiliconeGlowInkProduct[]>([])
const searchKeyword = ref('')
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const form = ref<SiliconeGlowInkProduct>({
  materialCode: '',
  supplierCode: '',
  stockCode: '',
  materialName: '',
  usage: '',
  category: '',
  color: '',
  responsiblePerson: '',
  minSheetSize: '',
  maxSheetSize: '',
  minSpacing: '',
  maxSpacing: '',
  designInfo: '',
  applicableInterface: '',
  notes: '',
  isActive: true
})

let searchTimer: ReturnType<typeof setTimeout> | null = null

const onSearch = () => {
  if (searchTimer) clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    searchProducts()
  }, 300)
}

const searchProducts = async () => {
  try {
    if (searchKeyword.value.trim()) {
      productList.value = await siliconeGlowInkApi.searchProducts(searchKeyword.value.trim())
    } else {
      productList.value = await siliconeGlowInkApi.getAllProducts()
    }
  } catch (err) {
    console.error('搜索产品失败:', err)
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  searchProducts()
}

const loadProducts = async () => {
  try {
    productList.value = await siliconeGlowInkApi.getAllProducts()
  } catch (err) {
    console.error('获取产品列表失败:', err)
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  form.value = {
    materialCode: '',
    supplierCode: '',
    stockCode: '',
    materialName: '',
    usage: '',
    category: '',
    color: '',
    responsiblePerson: '',
    minSheetSize: '',
    maxSheetSize: '',
    minSpacing: '',
    maxSpacing: '',
    designInfo: '',
    applicableInterface: '',
    notes: '',
    isActive: true
  }
}

const saveProduct = async () => {
  try {
    if (showEditDialog.value && form.value.id) {
      await siliconeGlowInkApi.updateProduct(form.value.id, form.value)
    } else {
      await siliconeGlowInkApi.createProduct(form.value)
    }
    closeDialog()
    await loadProducts()
  } catch (err) {
    console.error('保存产品失败:', err)
    alert('保存失败: ' + (err as any).response?.data?.message || (err as Error).message)
  }
}

const editProduct = (item: SiliconeGlowInkProduct) => {
  form.value = { ...item }
  showEditDialog.value = true
}

const deleteProduct = async (id: number) => {
  if (!confirm('确定删除该产品吗？')) return
  try {
    await siliconeGlowInkApi.deleteProduct(id)
    await loadProducts()
  } catch (err) {
    console.error('删除产品失败:', err)
    alert('删除失败')
  }
}

onMounted(() => {
  loadProducts()
})
</script>
