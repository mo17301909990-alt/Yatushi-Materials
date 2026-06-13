<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">硅胶皱纹UV产品管理</h1>
                <p class="mt-2 text-gray-600">管理硅胶皱纹UV物料标准书产品信息</p>
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
                  to="/admin/material/silicone-wrinkle-uv/compatibility"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
                  </svg>
                  兼容性管理
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 搜索 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">搜索</label>
            <input
              v-model="searchKeyword"
              @keyup.enter="searchProducts"
              type="text"
              placeholder="输入物料名称/编码搜索"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
        </div>
        <div class="flex items-end space-x-3 mt-4">
          <button
            @click="searchProducts"
            class="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors"
          >
            搜索
          </button>
          <button
            @click="resetSearch"
            class="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-colors"
          >
            重置
          </button>
        </div>
      </div>

      <!-- 产品列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">产品列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">物料编码</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">物料名称</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">材质类别</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">颜色</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">测试员</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">启用</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in productList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.materialCode || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ item.materialName || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.category || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.color || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.responsiblePerson || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm">
                  <span
                    :class="item.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ item.isActive ? '是' : '否' }}
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

        <div v-if="productList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暂无产品数据</div>
        </div>
      </div>

      <!-- 添加/编辑产品对话框 -->
      <div v-if="showAddDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">{{ editForm.id ? '编辑产品' : '添加产品' }}</h3>
              <button @click="closeAddDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveProduct" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料编码</label>
                  <input v-model="editForm.materialCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">供应商编码</label>
                  <input v-model="editForm.supplierCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">库存编码</label>
                  <input v-model="editForm.stockCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料名称</label>
                  <input v-model="editForm.materialName" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料用途</label>
                  <input v-model="editForm.usage" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">材质类别</label>
                  <input v-model="editForm.category" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">颜色</label>
                  <input v-model="editForm.color" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">测试员</label>
                  <input v-model="editForm.responsiblePerson" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">最小用纸尺寸</label>
                  <input v-model="editForm.minSheetSize" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">最大用纸尺寸</label>
                  <input v-model="editForm.maxSheetSize" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">最小间距</label>
                  <input v-model="editForm.minSpacing" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div class="flex items-end">
                  <label class="inline-flex items-center space-x-2 text-sm text-gray-700">
                    <input v-model="editForm.isActive" type="checkbox" class="rounded border-gray-300 text-green-600 focus:ring-green-500" />
                    <span>启用状态</span>
                  </label>
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">设计信息</label>
                <textarea v-model="editForm.designInfo" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">适用界面</label>
                <textarea v-model="editForm.applicableInterface" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">注意事项</label>
                <textarea v-model="editForm.notes" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
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
import { ref, reactive, onMounted } from 'vue'
import { siliconeWrinkleUvApi, type SiliconeWrinkleUvProduct } from '../../../api/modules/siliconeWrinkleUv'

const productList = ref<SiliconeWrinkleUvProduct[]>([])
const searchKeyword = ref('')
const showAddDialog = ref(false)

const editForm = reactive<SiliconeWrinkleUvProduct>({
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
  designInfo: '',
  applicableInterface: '',
  notes: '',
  isActive: true
})

const loadProducts = async () => {
  try {
    productList.value = await siliconeWrinkleUvApi.getAllProducts()
  } catch (error) {
    console.error('加载产品列表失败:', error)
  }
}

const searchProducts = async () => {
  try {
    if (searchKeyword.value.trim()) {
      productList.value = await siliconeWrinkleUvApi.searchProducts(searchKeyword.value.trim())
    } else {
      await loadProducts()
    }
  } catch (error) {
    console.error('搜索产品失败:', error)
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  loadProducts()
}

const editProduct = (product: SiliconeWrinkleUvProduct) => {
  Object.assign(editForm, product)
  showAddDialog.value = true
}

const saveProduct = async () => {
  try {
    if (editForm.id) {
      await siliconeWrinkleUvApi.updateProduct(editForm.id, editForm)
    } else {
      await siliconeWrinkleUvApi.saveProduct(editForm)
    }
    closeAddDialog()
    await loadProducts()
    alert('保存成功')
  } catch (error) {
    console.error('保存产品失败:', error)
    alert('保存失败')
  }
}

const deleteProduct = async (id: number) => {
  if (!confirm('确定删除该产品？')) return
  try {
    await siliconeWrinkleUvApi.deleteProduct(id)
    await loadProducts()
    alert('删除成功')
  } catch (error) {
    console.error('删除产品失败:', error)
    alert('删除失败')
  }
}

const closeAddDialog = () => {
  showAddDialog.value = false
  Object.assign(editForm, {
    id: undefined,
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
    designInfo: '',
    applicableInterface: '',
    notes: '',
    isActive: true
  })
}

onMounted(() => {
  loadProducts()
})
</script>
