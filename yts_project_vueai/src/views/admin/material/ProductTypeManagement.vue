<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">產品類型管理</h1>
            <p class="mt-2 text-gray-600">管理印刷產品的類型分類和配置</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="showAddDialog = true"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-purple-600 hover:bg-purple-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加產品類型
            </button>
            <button
              @click="exportData"
              class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
              </svg>
              导出数据
            </button>
            <router-link
              to="/admin/material-rule-management"
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

      <!-- 搜索和篩選 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">產品類型名稱</label>
            <input
              v-model="searchForm.name"
              type="text"
              placeholder="搜索產品類型"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">狀態</label>
            <select
              v-model="searchForm.isActive"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
            >
              <option value="">全部状态</option>
              <option value="true">激活</option>
              <option value="false">未激活</option>
            </select>
          </div>
          <div class="flex items-end">
            <button
              @click="searchProductTypes"
              class="w-full bg-purple-600 text-white px-4 py-2 rounded-md hover:bg-purple-700 transition-colors"
            >
              搜索
            </button>
          </div>
        </div>
      </div>

      <!-- 产品类型列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">产品类型列表</h3>
        </div>
        
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">描述</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">排序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="productType in paginatedData" :key="productType.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ productType.name }}</div>
                </td>
                <td class="px-6 py-4">
                  <div class="text-sm text-gray-900">{{ productType.description || '-' }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                      productType.isActive
                        ? 'bg-green-100 text-green-800'
                        : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ productType.isActive ? '激活' : '未激活' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ productType.sortOrder }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ formatDate(productType.createdAt) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button
                      @click="editProductType(productType)"
                      class="text-purple-600 hover:text-purple-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="configMapping(productType)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      配置映射
                    </button>
                    <button
                      @click="toggleStatus(productType)"
                      :class="[
                        'hover:underline',
                        productType.isActive ? 'text-red-600 hover:text-red-900' : 'text-green-600 hover:text-green-900'
                      ]"
                    >
                      {{ productType.isActive ? '禁用' : '启用' }}
                    </button>
                    <button
                      @click="deleteProductType(productType.id)"
                      class="text-red-600 hover:text-red-900"
                    >
                      删除
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 空状态 -->
        <div v-if="filteredData.length === 0" class="text-center py-12">
          <div class="text-gray-500">暂无产品类型数据</div>
        </div>

        <!-- 分页 -->
        <div v-if="filteredData.length > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
          <div class="flex-1 flex justify-between sm:hidden">
            <button
              @click="previousPage"
              :disabled="currentPage === 1"
              class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
            >
              上一页
            </button>
            <button
              @click="nextPage"
              :disabled="currentPage === totalPages"
              class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
            >
              下一页
            </button>
          </div>
          <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
            <div>
              <p class="text-sm text-gray-700">
                显示第
                <span class="font-medium">{{ (currentPage - 1) * pageSize + 1 }}</span>
                到
                <span class="font-medium">{{ Math.min(currentPage * pageSize, filteredData.length) }}</span>
                条，共
                <span class="font-medium">{{ filteredData.length }}</span>
                条记录
              </p>
            </div>
            <div>
              <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                <button
                  @click="previousPage"
                  :disabled="currentPage === 1"
                  class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                >
                  <span class="sr-only">上一页</span>
                  <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
                <button
                  v-for="page in visiblePages"
                  :key="page"
                  @click="goToPage(page)"
                  :class="[
                    'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                    page === currentPage
                      ? 'z-10 bg-purple-50 border-purple-500 text-purple-600'
                      : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'
                  ]"
                >
                  {{ page }}
                </button>
                <button
                  @click="nextPage"
                  :disabled="currentPage === totalPages"
                  class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                >
                  <span class="sr-only">下一页</span>
                  <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 添加/编辑产品类型对话框 -->
    <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              {{ showAddDialog ? '添加产品类型' : '编辑产品类型' }}
            </h3>
            <button
              @click="closeDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <form @submit.prevent="saveProductType" class="space-y-4">
            <div class="grid grid-cols-1 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">产品类型名称 *</label>
                <input
                  v-model="productTypeForm.name"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">描述</label>
                <textarea
                  v-model="productTypeForm.description"
                  rows="3"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                ></textarea>
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">排序顺序</label>
                  <input
                    v-model="productTypeForm.sortOrder"
                    type="number"
                    min="0"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                  />
                </div>
                <div class="flex items-center">
                  <input
                    v-model="productTypeForm.isActive"
                    type="checkbox"
                    class="h-4 w-4 text-purple-600 focus:ring-purple-500 border-gray-300 rounded"
                  />
                  <label class="ml-2 block text-sm text-gray-900">激活状态</label>
                </div>
              </div>
            </div>

            <div class="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                @click="closeDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
              <button
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-purple-600 hover:bg-purple-700"
              >
                {{ showAddDialog ? '添加' : '保存' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { productTypeOptionsApi, type ProductTypeOption } from '../../../api/modules/productTypeOptions'

// 路由实例
const router = useRouter()

// 响应式数据
const productTypes = ref([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)

// 分页状态
const currentPage = ref(1)
const pageSize = ref(10)

// 搜索表单
const searchForm = reactive({
  name: '',
  isActive: ''
})

// 产品类型表单
const productTypeForm = reactive({
  id: null,
  name: '',
  description: '',
  isActive: true,
  sortOrder: 0
})

// 计算属性 - 分页相关
const filteredData = computed(() => {
  let data = productTypes.value

  // 按名称筛选
  if (searchForm.name) {
    data = data.filter(item => 
      item.name.toLowerCase().includes(searchForm.name.toLowerCase())
    )
  }

  // 按状态筛选
  if (searchForm.isActive !== '') {
    const isActive = searchForm.isActive === 'true'
    data = data.filter(item => item.isActive === isActive)
  }

  return data
})

const totalPages = computed(() => Math.ceil(filteredData.value.length / pageSize.value))

const visiblePages = computed(() => {
  const pages = []
  const start = Math.max(1, currentPage.value - 2)
  const end = Math.min(totalPages.value, start + 4)
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredData.value.slice(start, end)
})

// 当筛选结果或分页大小变化时，自动纠正当前页，避免越界
watch([filteredData, pageSize], () => {
  const total = filteredData.value.length
  const maxPage = Math.max(1, Math.ceil(total / pageSize.value))
  if (currentPage.value > maxPage) {
    currentPage.value = maxPage
  }
})

// 方法
const loadProductTypes = async () => {
  loading.value = true
  try {
    // 调用实际的API
    const response = await productTypeOptionsApi.getAllOptions()
    productTypes.value = response.map(item => ({
      id: item.id,
      name: item.productName,
      description: item.description,
      isActive: item.isActive,
      sortOrder: item.displayOrder,
      createdAt: item.createdAt
    }))
  } catch (error) {
    console.error('加载产品类型失败:', error)
    productTypes.value = []
    alert('加载产品类型失败，请刷新页面重试')
  } finally {
    loading.value = false
  }
}

const searchProductTypes = () => {
  currentPage.value = 1 // 搜索后重置到第一页
  loadProductTypes()
}

// 分页相关方法
const previousPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

const goToPage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
  }
}

const editProductType = (productType: any) => {
  Object.assign(productTypeForm, productType)
  showEditDialog.value = true
}

const configMapping = (productType: any) => {
  // 跳转到耐磨金纸映射管理页面，并传递产品类型ID参数
  router.push({
    path: '/admin/material/smart-compatibility',
    query: { productTypeId: productType.id }
  })
}

const toggleStatus = async (productType: any) => {
  try {
    await productTypeOptionsApi.updateProductType(productType.id, { 
      productName: productType.name,
      description: productType.description,
      isActive: !productType.isActive,
      displayOrder: productType.sortOrder
    })
    productType.isActive = !productType.isActive
    await loadProductTypes() // 重新加载数据
  } catch (error) {
    console.error('更新状态失败:', error)
  }
}

const deleteProductType = async (id: number) => {
  if (confirm('确定要删除这个产品类型吗？')) {
    try {
      await productTypeOptionsApi.deleteProductType(id)
      await loadProductTypes()
    } catch (error) {
      console.error('删除产品类型失败:', error)
    }
  }
}

const saveProductType = async () => {
  try {
    if (showAddDialog.value) {
      await productTypeOptionsApi.createProductType({
        productName: productTypeForm.name,
        description: productTypeForm.description,
        isActive: productTypeForm.isActive,
        displayOrder: productTypeForm.sortOrder
      })
    } else {
      await productTypeOptionsApi.updateProductType(productTypeForm.id, {
        productName: productTypeForm.name,
        description: productTypeForm.description,
        isActive: productTypeForm.isActive,
        displayOrder: productTypeForm.sortOrder
      })
    }
    closeDialog()
    await loadProductTypes()
  } catch (error) {
    console.error('保存产品类型失败:', error)
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(productTypeForm, {
    id: null,
    name: '',
    description: '',
    isActive: true,
    sortOrder: 0
  })
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 导出数据
const exportData = async () => {
  try {
    // 调用导出API
    const response = await fetch('/product-type-options/export', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    if (!response.ok) {
      throw new Error('导出失败')
    }
    
    // 获取文件名
    const contentDisposition = response.headers.get('Content-Disposition')
    let fileName = '产品类型选项.xlsx'
    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
      if (fileNameMatch && fileNameMatch[1]) {
        fileName = decodeURIComponent(fileNameMatch[1].replace(/['"]/g, ''))
      }
    }
    
    // 下载文件
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = fileName
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    alert('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    alert('导出失败，请重试')
  }
}

// 生命周期
onMounted(() => {
  loadProductTypes()
})
</script>

<style scoped>
/* 可以添加特定的样式 */
</style>

