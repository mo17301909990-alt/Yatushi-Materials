<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">硅胶桔纹UV物料管理</h1>
            <p class="mt-2 text-gray-600">管理硅胶桔纹UV(Orange Peel UV)物料的配置信息</p>
          </div>
          <div class="flex space-x-3">
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
              to="/admin/material/post-processing/silicone-orange-peel-uv/compatibility"
              class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
              </svg>
              兼容性管理
            </router-link>
            <router-link
              to="/admin/material/post-processing"
              class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
              </svg>
              返回
            </router-link>
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
              placeholder="输入物料名称/编码/颜色搜索"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          <div class="flex items-end">
            <button
              @click="searchProducts"
              class="w-full bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors mr-2"
            >
              搜索
            </button>
            <button
              @click="resetSearch"
              class="w-full bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-colors"
            >
              重置
            </button>
          </div>
        </div>
      </div>

      <!-- 产品列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">物料产品列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">物料名称</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">物料编码</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">颜色</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">测试员</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in productList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.id }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ item.materialName || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ item.materialCode || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.color || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{{ item.responsiblePerson || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="item.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ item.isActive ? '激活' : '未激活' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center space-x-2">
                    <button @click="viewProduct(item)" class="text-blue-600 hover:text-blue-900">查看</button>
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

      <!-- 添加/编辑对话框 -->
      <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">{{ formData.id ? '编辑产品' : '添加产品' }}</h3>
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
                  <input v-model="formData.materialCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">供应商型号</label>
                  <input v-model="formData.supplierCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">测试单号</label>
                  <input v-model="formData.stockCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料名称 *</label>
                  <input v-model="formData.materialName" required type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">颜色</label>
                  <input v-model="formData.color" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">形状</label>
                  <input v-model="formData.shape" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">材质</label>
                  <input v-model="formData.materialType" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">厚度</label>
                  <input v-model="formData.thickness" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">尺寸</label>
                  <input v-model="formData.sizeInfo" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">测试员</label>
                  <input v-model="formData.responsiblePerson" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div class="flex items-end">
                  <label class="inline-flex items-center space-x-2 text-sm text-gray-700">
                    <input v-model="formData.isActive" type="checkbox" class="rounded border-gray-300 text-green-600 focus:ring-green-500" />
                    <span>激活</span>
                  </label>
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">物料用途</label>
                <textarea v-model="formData.usageText" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">设计限制</label>
                <textarea v-model="formData.designInfo" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">适用界面</label>
                <textarea v-model="formData.applicableInterface" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">注意事项</label>
                <textarea v-model="formData.notes" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button type="button" @click="closeDialog" class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">取消</button>
                <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">保存</button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 查看详情对话框 -->
      <div v-if="showViewDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">产品详情</h3>
            <button @click="showViewDialog = false" class="text-gray-400 hover:text-gray-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          <div class="space-y-3">
            <div class="grid grid-cols-2 gap-4">
              <div><span class="font-medium text-gray-700">ID:</span> {{ viewData.id }}</div>
              <div><span class="font-medium text-gray-700">物料名称:</span> {{ viewData.materialName }}</div>
              <div><span class="font-medium text-gray-700">物料编码:</span> {{ viewData.materialCode || '-' }}</div>
              <div><span class="font-medium text-gray-700">颜色:</span> {{ viewData.color || '-' }}</div>
              <div><span class="font-medium text-gray-700">材质:</span> {{ viewData.materialType || '-' }}</div>
              <div><span class="font-medium text-gray-700">测试员:</span> {{ viewData.responsiblePerson || '-' }}</div>
            </div>
            <div v-if="viewData.usageText">
              <span class="font-medium text-gray-700">物料用途:</span>
              <p class="mt-1 text-gray-600 whitespace-pre-wrap">{{ viewData.usageText }}</p>
            </div>
            <div v-if="viewData.designInfo">
              <span class="font-medium text-gray-700">设计限制:</span>
              <p class="mt-1 text-gray-600 whitespace-pre-wrap">{{ viewData.designInfo }}</p>
            </div>
            <div v-if="viewData.applicableInterface">
              <span class="font-medium text-gray-700">适用界面:</span>
              <p class="mt-1 text-gray-600 whitespace-pre-wrap">{{ viewData.applicableInterface }}</p>
            </div>
            <div v-if="viewData.notes">
              <span class="font-medium text-gray-700">注意事项:</span>
              <p class="mt-1 text-gray-600 whitespace-pre-wrap">{{ viewData.notes }}</p>
            </div>
          </div>
          <div class="flex justify-end mt-4">
            <button @click="showViewDialog = false" class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">关闭</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { siliconeOrangePeelUvApi, type SiliconeOrangePeelUvProduct } from '../../../api/modules/siliconeOrangePeelUv'

const productList = ref<SiliconeOrangePeelUvProduct[]>([])
const searchKeyword = ref('')
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showViewDialog = ref(false)

const defaultForm: SiliconeOrangePeelUvProduct = {
  materialCode: '',
  supplierCode: '',
  stockCode: '',
  materialName: '',
  usageText: '',
  materialType: '',
  thickness: '',
  sizeInfo: '',
  color: '',
  shape: '',
  responsiblePerson: '',
  designInfo: '',
  applicableInterface: '',
  notes: '',
  isActive: true
}

const formData = reactive<SiliconeOrangePeelUvProduct>({ ...defaultForm })
const viewData = reactive<SiliconeOrangePeelUvProduct>({})

onMounted(() => {
  loadProducts()
})

const loadProducts = async () => {
  try {
    productList.value = await siliconeOrangePeelUvApi.getAllProducts()
  } catch (error) {
    console.error('加载产品失败:', error)
  }
}

const searchProducts = async () => {
  if (searchKeyword.value.trim()) {
    try {
      productList.value = await siliconeOrangePeelUvApi.searchProducts(searchKeyword.value.trim())
    } catch (error) {
      console.error('搜索产品失败:', error)
    }
  } else {
    await loadProducts()
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  loadProducts()
}

const viewProduct = (item: SiliconeOrangePeelUvProduct) => {
  Object.assign(viewData, item)
  showViewDialog.value = true
}

const editProduct = (item: SiliconeOrangePeelUvProduct) => {
  Object.assign(formData, { ...defaultForm, ...item })
  showEditDialog.value = true
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(formData, { ...defaultForm })
}

const saveProduct = async () => {
  try {
    if (formData.id) {
      await siliconeOrangePeelUvApi.updateProduct(formData.id, { ...formData })
    } else {
      await siliconeOrangePeelUvApi.createProduct({ ...formData })
    }
    closeDialog()
    await loadProducts()
    alert('保存成功')
  } catch (error) {
    console.error('保存产品失败:', error)
    alert('保存失败')
  }
}

const deleteProduct = async (id: number) => {
  if (confirm('确定要删除这个产品吗？')) {
    try {
      await siliconeOrangePeelUvApi.deleteProduct(id)
      await loadProducts()
      alert('删除成功')
    } catch (error) {
      console.error('删除产品失败:', error)
      alert('删除失败')
    }
  }
}
</script>
