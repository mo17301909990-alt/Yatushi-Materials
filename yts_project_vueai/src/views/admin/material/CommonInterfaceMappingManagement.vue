<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">常用界面映射配置</h1>
            <p class="mt-2 text-gray-600">产品兼容性配置</p>
          </div>
          <div class="flex space-x-3">
            <button
                @click="showAddDialog = true"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-orange-600 hover:bg-orange-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加配置
            </button>
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

      <!-- 搜索和筛选 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列</label>
            <select
              v-model="searchForm.productName"
              @change="onSearchProductNameChange"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
            >
              <option value="">全部燙金紙系列</option>
              <option v-for="product in availableProducts" :key="product" :value="product">
                {{ product }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
            <select
              v-model="searchForm.compatibilityStatus"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
            >
              <option value="">全部状态</option>
              <option value="V">适用</option>
              <option value="X">不适用</option>
              <option value="NA">不确定</option>
              <option value="▷">需要打底处理</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
            <select
              v-model="searchForm.paperType"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
            >
              <option value="">全部烫金纸性能类型</option>
              <option v-for="paperType in availablePaperTypes" :key="paperType" :value="paperType">
                {{ paperType }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
            <select
              v-model="searchForm.status"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
            >
              <option value="">全部状态</option>
              <option value="true">激活</option>
              <option value="false">停用</option>
            </select>
          </div>
          <div class="flex items-end">
            <button
              @click="searchConfigurations"
              class="w-full px-4 py-2 bg-orange-600 text-white rounded-md hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-orange-500"
            >
              搜索
            </button>
          </div>
        </div>
      </div>

      <!-- 配置列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙系列</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="config in paginatedConfigurations" :key="config.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ config.productName }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ config.paperType || '-' }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                      config.compatibilityStatus === 'V' 
                        ? 'bg-green-100 text-green-800' 
                        : config.compatibilityStatus === 'X'
                        ? 'bg-red-100 text-red-800'
                        : config.compatibilityStatus === 'NA'
                        ? 'bg-yellow-100 text-yellow-800'
                        : 'bg-blue-100 text-blue-800'
                    ]"
                  >
                    {{ 
                      config.compatibilityStatus === 'V' ? '适用' : 
                      config.compatibilityStatus === 'X' ? '不适用' :
                      config.compatibilityStatus === 'NA' ? '不确定' : '需要打底处理'
                    }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                      config.isActive !== false
                        ? 'bg-green-100 text-green-800'
                        : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ config.isActive !== false ? '激活' : '停用' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button
                      @click="editConfiguration(config)"
                      class="text-orange-600 hover:text-orange-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="toggleConfigurationStatus(config)"
                      :class="[
                        'hover:underline',
                        config.isActive !== false ? 'text-red-600 hover:text-red-900' : 'text-green-600 hover:text-green-900'
                      ]"
                    >
                      {{ config.isActive !== false ? '停用' : '激活' }}
                    </button>
                    <button
                      @click="deleteConfiguration(config.id)"
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

        <!-- 分页 -->
        <div v-if="totalPages > 1" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
          <div class="flex-1 flex justify-between sm:hidden">
            <button
              @click="goToPreviousPage"
              :disabled="currentPage === 1"
              class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              上一页
            </button>
            <button
              @click="goToNextPage"
              :disabled="currentPage === totalPages"
              class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              下一页
            </button>
          </div>
          <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
            <div>
              <p class="text-sm text-gray-700">
                <span v-if="totalItems > 0">
                  显示第 <span class="font-medium">{{ (currentPage - 1) * pageSize + 1 }}</span> 到
                  <span class="font-medium">{{ Math.min(currentPage * pageSize, totalItems) }}</span> 条，
                  共 <span class="font-medium">{{ totalItems }}</span> 条记录
                </span>
                <span v-else>
                  暂无数据
                </span>
              </p>
            </div>
            <div v-if="totalPages > 1">
              <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                <button
                  @click="goToPreviousPage"
                  :disabled="currentPage === 1"
                  class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <span class="sr-only">上一页</span>
                  <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
                <button
                  v-for="page in visiblePages"
                  :key="page"
                  @click="goToPage(page)"
                  :disabled="page === '...'"
                  :class="[
                    'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                    page === currentPage
                      ? 'z-10 bg-orange-50 border-orange-500 text-orange-600'
                      : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50',
                    page === '...' ? 'cursor-default' : 'cursor-pointer'
                  ]"
                >
                  {{ page }}
                </button>
                <button
                  @click="goToNextPage"
                  :disabled="currentPage === totalPages"
                  class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <span class="sr-only">下一页</span>
                  <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
                  </svg>
                </button>
              </nav>
            </div>
          </div>
        </div>
      </div>

      <!-- 添加/编辑对话框 -->
      <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 md:w-3/4 lg:w-1/2 shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">
                {{ showAddDialog ? '添加常用界面映射' : '编辑常用界面映射' }}
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

            <form @submit.prevent="saveConfiguration" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">烫金类型 *</label>
                  <input
                    type="text"
                    value="燙金後擊凸"
                    disabled
                    class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100 text-gray-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列 *</label>
                  <select
                    v-model="configForm.productName"
                    @change="onProductNameChange"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
                  >
                    <option value="">请选择燙金紙系列</option>
                    <option v-for="product in availableProducts" :key="product" :value="product">
                      {{ product }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型 *</label>
                  <select
                    v-model="configForm.paperType"
                    :disabled="!configForm.productName || loadingPaperTypes"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingPaperTypes ? '加载中...' : !configForm.productName ? '请先选择燙金紙系列' : '请选择烫金纸性能类型' }}
                    </option>
                    <option v-for="paperType in filteredPaperTypes" :key="paperType" :value="paperType">
                      {{ paperType }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
                  <select
                    v-model="configForm.compatibilityStatus"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-orange-500"
                  >
                    <option value="">请选择兼容性状态</option>
                    <option value="V">适用</option>
                    <option value="X">不适用</option>
                    <option value="NA">不确定</option>
                    <option value="▷">需要打底处理</option>
                  </select>
                </div>
                <div class="md:col-span-2">
                  <label class="flex items-center">
                    <input
                      type="checkbox"
                      v-model="configForm.isActive"
                      class="rounded border-gray-300 text-orange-600 shadow-sm focus:border-orange-300 focus:ring focus:ring-orange-200 focus:ring-opacity-50"
                    />
                    <span class="ml-2 text-sm text-gray-700">激活状态</span>
                  </label>
                </div>
              </div>

              <div class="flex justify-end space-x-3 mt-6">
                <button
                  type="button"
                  @click="closeDialog"
                  class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  取消
                </button>
                <button
                  type="submit"
                  class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-orange-600 hover:bg-orange-700"
                >
                  {{ showAddDialog ? '添加' : '保存' }}
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
import { ref, reactive, onMounted, computed } from 'vue'
import { hotStampingTypeOptionsApi, type HotStampingTypeOption } from '../../../api/modules/hotStampingTypeOptions'
import { hotStampingTypeCompatibilityApi, type HotStampingTypeCompatibility } from '../../../api/modules/hotStampingTypeCompatibility'

// 响应式数据
const configurations = ref<HotStampingTypeCompatibility[]>([])
const loading = ref(false)
const hotStampingTypeId = ref<number | null>(null)

// 对话框状态
const showAddDialog = ref(false)
const showEditDialog = ref(false)

// 搜索表单
const searchForm = reactive({
  productName: '',
  compatibilityStatus: '',
  paperType: '',
  status: ''
})

// 配置表单
const configForm = reactive({
  id: undefined as number | undefined,
  productName: '',
  paperType: '',
  compatibilityStatus: '',
  isActive: true
})

// 分页相关数据
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)
const totalPages = ref(0)

// 选项数据
const availableProducts = ref<string[]>([])
const availablePaperTypes = ref<string[]>([])
const filteredPaperTypes = ref<string[]>([])
const loadingPaperTypes = ref(false)

// 查找"燙金後擊凸"烫金类型ID
const findHotStampingTypeId = async (): Promise<number | null> => {
  try {
    const allTypes = await hotStampingTypeOptionsApi.getAllTypes()
    const targetType = allTypes.find(type => 
      type.stampingType === '燙金後擊凸' && 
      (type.positionType === null || type.positionType === undefined || type.positionType === '')
    )
    return targetType ? targetType.id : null
  } catch (error) {
    console.error('查找烫金类型ID失败:', error)
    return null
  }
}

// 计算属性
const filteredConfigurations = computed(() => {
  let filtered = configurations.value

  if (searchForm.productName) {
    filtered = filtered.filter(config => config.productName === searchForm.productName)
  }
  if (searchForm.compatibilityStatus) {
    filtered = filtered.filter(config => config.compatibilityStatus === searchForm.compatibilityStatus)
  }
  if (searchForm.paperType) {
    filtered = filtered.filter(config => config.paperType === searchForm.paperType)
  }
  if (searchForm.status) {
    const isActive = searchForm.status === 'true'
    filtered = filtered.filter(config => (config.isActive !== false) === isActive)
  }

  // 更新分页信息
  totalItems.value = filtered.length
  totalPages.value = Math.ceil(totalItems.value / pageSize.value)
  
  // 如果当前页超出了总页数，重置到第一页
  if (currentPage.value > totalPages.value && totalPages.value > 0) {
    currentPage.value = 1
  }

  return filtered
})

// 当前页显示的数据
const paginatedConfigurations = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredConfigurations.value.slice(start, end)
})

const visiblePages = computed(() => {
  const total = totalPages.value
  const current = currentPage.value
  const pages: (number | string)[] = []
  
  if (total <= 7) {
    for (let i = 1; i <= total; i++) {
      pages.push(i)
    }
  } else {
    if (current <= 4) {
      for (let i = 1; i <= 5; i++) {
        pages.push(i)
      }
      pages.push('...')
      pages.push(total)
    } else if (current >= total - 3) {
      pages.push(1)
      pages.push('...')
      for (let i = total - 4; i <= total; i++) {
        pages.push(i)
      }
    } else {
      pages.push(1)
      pages.push('...')
      for (let i = current - 1; i <= current + 1; i++) {
        pages.push(i)
      }
      pages.push('...')
      pages.push(total)
    }
  }
  
  return pages
})

// 加载产品配置列表
const loadProductConfigurations = async () => {
  try {
    loading.value = true
    
    // 首先查找"燙金後擊凸"烫金类型ID
    const typeId = await findHotStampingTypeId()
    if (!typeId) {
      console.error('未找到"燙金後擊凸"烫金类型')
      configurations.value = []
      return
    }
    
    hotStampingTypeId.value = typeId
    console.log('找到烫金类型ID:', typeId)
    
    // 使用找到的ID获取兼容性配置
    const data = await hotStampingTypeCompatibilityApi.getCompatibilityByHotStampingTypeId(typeId)
    console.log('获取到的兼容性配置数据:', data)
    configurations.value = data
  } catch (error) {
    console.error('加载产品配置失败:', error)
    configurations.value = []
  } finally {
    loading.value = false
  }
}


// 搜索配置
const searchConfigurations = () => {
  currentPage.value = 1
  // 搜索逻辑已在computed中实现，会自动更新分页信息
}

// 分页方法
const goToPage = (page: number | string) => {
  if (typeof page === 'number') {
    currentPage.value = page
  }
}

const goToPreviousPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

const goToNextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

// 编辑配置
const editConfiguration = (config: HotStampingTypeCompatibility) => {
  Object.assign(configForm, {
    id: config.id,
    productName: config.productName,
    paperType: config.paperType,
    compatibilityStatus: config.compatibilityStatus,
    isActive: config.isActive !== false
  })
  showEditDialog.value = true
}

// 删除配置
const deleteConfiguration = async (id: number | undefined) => {
  if (!id) return
  if (confirm('确定要删除这个配置吗？')) {
    try {
      await hotStampingTypeCompatibilityApi.deleteCompatibility(id)
      await loadProductConfigurations()
      console.log('删除配置成功:', id)
    } catch (error) {
      console.error('删除配置失败:', error)
      alert('删除失败，请重试')
    }
  }
}

// 切换配置状态
const toggleConfigurationStatus = async (config: HotStampingTypeCompatibility) => {
  try {
    config.isActive = !config.isActive
    if (config.id) {
      await hotStampingTypeCompatibilityApi.updateCompatibility(config.id, config)
    }
  } catch (error) {
    console.error('切换状态失败:', error)
    config.isActive = !config.isActive // 回滚状态
    alert('操作失败，请重试')
  }
}

// 保存配置
const saveConfiguration = async () => {
  try {
    if (configForm.id) {
      // 更新
      const updateData = {
        id: configForm.id,
        productName: configForm.productName,
        paperType: configForm.paperType,
        compatibilityStatus: configForm.compatibilityStatus as "V" | "X" | "NA" | "▷",
        isActive: configForm.isActive
      }
      await hotStampingTypeCompatibilityApi.updateCompatibility(configForm.id, updateData)
    } else {
      // 创建
      const createData = {
        productName: configForm.productName,
        paperType: configForm.paperType,
        compatibilityStatus: configForm.compatibilityStatus as "V" | "X" | "NA" | "▷",
        isActive: configForm.isActive,
        hotStampingTypeId: hotStampingTypeId.value!
      }
      await hotStampingTypeCompatibilityApi.createCompatibility(createData)
    }
    await loadProductConfigurations()
    closeDialog()
  } catch (error) {
    console.error('保存配置失败:', error)
    alert('保存失败，请重试')
  }
}

// 关闭对话框
const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(configForm, {
    id: undefined,
    productName: '',
    paperType: '',
    compatibilityStatus: '',
    isActive: true
  })
}

// 产品名称变化处理
const onProductNameChange = async () => {
  if (configForm.productName) {
    loadingPaperTypes.value = true
    try {
      filteredPaperTypes.value = await hotStampingTypeCompatibilityApi.getPaperTypesByProductName(configForm.productName)
    } catch (error) {
      console.error('加载烫金纸性能类型失败:', error)
      filteredPaperTypes.value = []
    } finally {
      loadingPaperTypes.value = false
    }
  } else {
    filteredPaperTypes.value = []
  }
}

// 搜索产品名称变化处理
const onSearchProductNameChange = async () => {
  if (searchForm.productName) {
    loadingPaperTypes.value = true
    try {
      availablePaperTypes.value = await hotStampingTypeCompatibilityApi.getPaperTypesByProductName(searchForm.productName)
    } catch (error) {
      console.error('加载烫金纸性能类型失败:', error)
      availablePaperTypes.value = []
    } finally {
      loadingPaperTypes.value = false
    }
  } else {
    availablePaperTypes.value = []
  }
}

// 加载可用产品
const loadAvailableProducts = async () => {
  try {
    availableProducts.value = await hotStampingTypeCompatibilityApi.getAllProductNames()
  } catch (error) {
    console.error('加载产品列表失败:', error)
    availableProducts.value = []
  }
}

// 加载可用烫金纸性能类型
const loadAvailablePaperTypes = async () => {
  try {
    availablePaperTypes.value = await hotStampingTypeCompatibilityApi.getAllPaperTypes()
  } catch (error) {
    console.error('加载烫金纸性能类型列表失败:', error)
    availablePaperTypes.value = []
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadProductConfigurations()
  loadAvailableProducts()
  loadAvailablePaperTypes()
})
</script>
