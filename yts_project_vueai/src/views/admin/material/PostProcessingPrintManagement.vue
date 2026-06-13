<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">印刷配置管理</h1>
            <p class="mt-2 text-gray-600">管理印刷後加工的配置信息</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="showAddDialog = true"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加印刷配置
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
            <button
              @click="exportData"
              class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
              </svg>
              导出数据
            </button>
            <button
              @click="openImportDialog"
              class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path>
              </svg>
              导入
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

      <!-- 搜索和篩選 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-6 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">产品系列</label>
            <input
              v-model="searchForm.productName"
              @blur="onSearchProductNameChange"
              type="text"
              placeholder="输入产品系列进行筛选"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
            <select
              v-model="searchForm.paperType"
              :disabled="!searchForm.productName"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
            >
              <option value="">
                {{ !searchForm.productName ? '请先选择燙金紙系列' : '全部烫金纸性能类型' }}
              </option>
              <option v-for="paperType in filteredPaperTypes" :key="paperType" :value="paperType">
                {{ paperType }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
            <input
              v-model="searchForm.productModelNumber"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              placeholder="搜索产品型号"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">颜色</label>
            <input
              v-model="searchForm.color"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              placeholder="搜索颜色"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">布面纸类型</label>
            <select
              v-model="searchForm.clothPaperType"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">全部布面纸类型</option>
              <option v-for="clothType in availableClothPaperTypes" :key="clothType" :value="clothType">
                {{ clothType }}
              </option>
            </select>
          </div>
          <div class="flex items-end space-x-2">
            <button
              @click="searchPrintConfigs"
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
      </div>

      <!-- 批量操作工具栏 -->
      <div v-if="selectedItems.length > 0" class="bg-indigo-50 border border-indigo-200 rounded-lg p-4 mb-6">
        <div class="flex items-center justify-between">
          <div class="flex items-center">
            <span class="text-sm font-medium text-indigo-900 mr-4">
              已选中 {{ selectedItems.length }} 条记录
            </span>
            <button
              @click="clearSelection"
              class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 mr-2"
            >
              清空选择
            </button>
            <button
              @click="openBatchEditDialog"
              class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 mr-2"
            >
              批量修改
            </button>
            <button
              @click="batchCopyPrintConfigs"
              class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 mr-2"
            >
              批量复制
            </button>
            <button
              @click="batchDeletePrintConfigs"
              class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-red-600 hover:bg-red-700 mr-2"
            >
              批量删除
            </button>
          </div>
        </div>
      </div>

      <!-- 印刷配置列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">印刷配置列表</h3>
        </div>
        
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left">
                  <input
                    type="checkbox"
                    :checked="selectedItems.length === printConfigs.length && printConfigs.length > 0"
                    @change="toggleSelectAll"
                    class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                  />
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙系列</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品型号</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">颜色</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">布面纸类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="config in printConfigs" :key="config.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <input
                    type="checkbox"
                    :checked="selectedItems.includes(config.id as number)"
                    @change="toggleSelectItem(config.id as number)"
                    class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                  />
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ config.productName }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ config.productModelNumber }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ config.color }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ getClothPaperTypeName(config.clothPaperTypeId) }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                        :class="getCompatibilityStatusClass(config.compatibilityStatus)">
                    {{ getCompatibilityStatusText(config.compatibilityStatus) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ config.paperType || '-' }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button
                      @click="copyPrintConfig(config)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      复制
                    </button>
                    <button
                      @click="editPrintConfig(config)"
                      class="text-green-600 hover:text-green-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="deletePrintConfig(config.id!)"
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
        
        <!-- 分页控件 -->
        <div v-if="totalPages > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
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
                显示第
                <span class="font-medium">{{ (currentPage - 1) * pageSize + 1 }}</span>
                到
                <span class="font-medium">{{ Math.min(currentPage * pageSize, totalItems) }}</span>
                条，共
                <span class="font-medium">{{ totalItems }}</span>
                条记录
              </p>
            </div>
            <div>
              <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="分页">
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
                  @click="typeof page === 'number' ? goToPage(page) : null"
                  :disabled="typeof page !== 'number'"
                  :class="[
                    'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                    typeof page === 'number' && page === currentPage
                      ? 'z-10 bg-green-50 border-green-500 text-green-600'
                      : typeof page === 'number'
                      ? 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'
                      : 'bg-white border-gray-300 text-gray-500 cursor-default'
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
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">
                {{ showAddDialog ? '添加印刷配置' : '编辑印刷配置' }}
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

            <form @submit.prevent="savePrintConfig">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列</label>
                  <select
                    v-model="printConfigForm.productName"
                    @change="onProductNameChange"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">请选择燙金紙系列</option>
                    <option v-for="product in availableProducts" :key="product" :value="product">
                      {{ product }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙性能類型 *</label>
                  <select
                    v-model="printConfigForm.paperType"
                    :disabled="!printConfigForm.productName || loadingPaperTypes"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingPaperTypes ? '加載中...' : !printConfigForm.productName ? '請先選擇燙金紙系列' : '請選擇燙金紙性能類型' }}
                    </option>
                    <option v-for="paperType in filteredPaperTypes" :key="paperType" :value="paperType">
                      {{ paperType }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
                  <select
                    v-model="printConfigForm.productModelNumber"
                    @change="onProductModelNumberChange"
                    :disabled="!printConfigForm.productName || loadingProductModels"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingProductModels ? '加載中...' : !printConfigForm.productName ? '請先選擇燙金紙系列' : '請選擇產品型號' }}
                    </option>
                    <option v-for="model in availableProductModels" :key="model" :value="model">
                      {{ model }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">颜色</label>
                  <select
                    v-model="printConfigForm.color"
                    :disabled="(!printConfigForm.productName && !printConfigForm.productModelNumber) || loadingColors"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingColors ? '加載中...' : (!printConfigForm.productName && !printConfigForm.productModelNumber) ? '請先選擇燙金紙系列或產品型號' : '請選擇顏色' }}
                    </option>
                    <option v-for="color in availableColors" :key="color" :value="color">
                      {{ color }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">布面紙類型</label>
                  <select
                    v-model.number="printConfigForm.clothPaperTypeId"
                    :disabled="loadingClothPaperTypes"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingClothPaperTypes ? '加載中...' : '請選擇布面紙類型' }}
                    </option>
                    <option v-for="option in clothPaperTypeOptions" :key="option.id" :value="option.id">
                      {{ option.label }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
                  <select
                    v-model="printConfigForm.compatibilityStatus"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="V">适用</option>
                    <option value="X">不适用</option>
                    <option value="NA">不确定</option>
                    <option value="▷">需要打底处理</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙性能類型</label>
                  <input
                    v-model="printConfigForm.paperType"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                    placeholder="請輸入燙金紙性能類型"
                  />
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
                  class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  {{ showAddDialog ? '添加' : '保存' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 批量添加对话框 -->
      <div v-if="showBatchAddDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">批量添加印刷配置</h3>
              <button @click="closeBatchAddDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙性能類型 *</label>
                <select
                  v-model="batchAddForm.paperType"
                  @change="onBatchAddPaperTypeChange"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">請選擇燙金紙性能類型</option>
                  <option v-for="paperType in availablePaperTypes" :key="paperType" :value="paperType">
                    {{ paperType }}
                  </option>
                </select>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">產品系列 *</label>
                <input
                  v-model="batchAddSeriesSearchText"
                  type="text"
                  placeholder="搜索產品系列..."
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
                <div
                  class="border border-gray-300 rounded-md p-3 max-h-48 overflow-y-auto mt-2"
                  :class="{ 'bg-gray-50': !batchAddForm.paperType }"
                >
                  <div class="space-y-2">
                    <label
                      v-for="series in filteredBatchAddSeries"
                      :key="series"
                      class="flex items-center space-x-2 cursor-pointer hover:bg-gray-50 p-2 rounded"
                    >
                      <input
                        type="checkbox"
                        :value="series"
                        v-model="batchAddForm.selectedSeries"
                        @change="onBatchAddSeriesChange(series)"
                        :disabled="!batchAddForm.paperType"
                        class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded disabled:opacity-50"
                      />
                      <span class="text-sm text-gray-700">{{ series }}</span>
                    </label>
                    <div v-if="filteredBatchAddSeries.length === 0" class="px-2 py-4 text-sm text-gray-500 text-center">
                      {{ !batchAddForm.paperType ? '請先選擇燙金紙性能類型' : '未找到匹配的產品系列' }}
                    </div>
                  </div>
                </div>
                <p class="mt-2 text-xs text-gray-500">
                  已選擇 <span class="font-bold text-indigo-600">{{ batchAddForm.selectedSeries.length }}</span> 個產品系列
                  <span v-if="batchAddSeriesSearchText" class="ml-2">
                    （顯示 {{ filteredBatchAddSeries.length }} 個匹配項）
                  </span>
                </p>
              </div>

              <!-- 为每个选中的产品系列显示产品型号选择 -->
              <div v-for="seriesName in batchAddForm.selectedSeries" :key="seriesName" class="mt-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  產品系列「{{ seriesName }}」的產品型號選擇（可選）
                </label>
                <p class="mb-2 text-xs text-gray-500">
                  如果不選擇任何產品型號，則表示該產品系列全部適用
                </p>
                <div 
                  v-if="batchAddSeriesProducts[seriesName] && batchAddSeriesProducts[seriesName].length > 0"
                  class="border border-gray-300 rounded-md p-3 max-h-48 overflow-y-auto"
                >
                  <div class="space-y-2">
                    <label
                      v-for="product in batchAddSeriesProducts[seriesName]"
                      :key="product.modelNumber || product.id"
                      class="flex items-center space-x-2 cursor-pointer hover:bg-gray-50 p-2 rounded"
                    >
                      <input
                        type="checkbox"
                        :value="product.modelNumber"
                        :checked="(batchAddForm.selectedProducts[seriesName] || []).includes(product.modelNumber)"
                        @change="async (e) => {
                          if (!batchAddForm.selectedProducts[seriesName]) {
                            batchAddForm.selectedProducts[seriesName] = []
                          }
                          if (e.target.checked) {
                            if (!batchAddForm.selectedProducts[seriesName].includes(product.modelNumber)) {
                              batchAddForm.selectedProducts[seriesName].push(product.modelNumber)
                            }
                          } else {
                            const index = batchAddForm.selectedProducts[seriesName].indexOf(product.modelNumber)
                            if (index > -1) {
                              batchAddForm.selectedProducts[seriesName].splice(index, 1)
                            }
                          }
                          // 产品型号选择变化时，重新加载颜色选项
                          await loadBatchAddColors()
                        }"
                        class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                      />
                      <span class="text-sm text-gray-700">
                        {{ product.modelNumber ? `${product.modelNumber}${product.name ? ' - ' + product.name : ''}` : product.name || `產品ID: ${product.id}` }}
                      </span>
                    </label>
                  </div>
                  <p class="mt-2 text-xs text-gray-500">
                    已選擇 <span class="font-bold text-indigo-600">
                      {{ (batchAddForm.selectedProducts[seriesName] || []).length }}
                    </span> 個產品型號
                    <span v-if="(batchAddForm.selectedProducts[seriesName] || []).length === 0" class="text-yellow-600">
                      （將創建該產品系列的通用映射）
                    </span>
                  </p>
                </div>
                <div v-else class="border border-gray-300 rounded-md p-3 bg-gray-50">
                  <p class="text-sm text-gray-500 text-center">
                    {{ batchAddSeriesProducts[seriesName] === undefined ? '加載中...' : '該產品系列下暫無產品' }}
                  </p>
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">顏色（可選）</label>
                <select
                  v-model="batchAddForm.color"
                  :disabled="loadingBatchAddColors || (batchAddForm.selectedSeries.length === 0 && Object.keys(batchAddForm.selectedProducts).length === 0)"
                  @change="onBatchAddColorChange"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                >
                  <option value="">
                    {{ loadingBatchAddColors ? '加載中...' : (batchAddForm.selectedSeries.length === 0 && Object.keys(batchAddForm.selectedProducts).length === 0) ? '請先選擇產品系列或型號' : '不選擇顏色（通用）' }}
                  </option>
                  <option v-for="color in batchAddAvailableColors" :key="color" :value="color">
                    {{ color }}
                  </option>
                </select>
                <p class="mt-1 text-xs text-gray-500">
                  如果不選擇顏色，則表示該配置適用於所有顏色
                </p>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">布面紙類型 *</label>
                <select
                  v-model.number="batchAddForm.clothPaperTypeId"
                  :disabled="loadingClothPaperTypes"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="0">請選擇布面紙類型</option>
                  <option v-for="option in clothPaperTypeOptions" :key="option.id" :value="option.id">
                    {{ option.label }}
                  </option>
                </select>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性狀態 *</label>
                <select
                  v-model="batchAddForm.compatibilityStatus"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="V">适用</option>
                  <option value="X">不适用</option>
                  <option value="NA">不确定</option>
                  <option value="▷">需要打底处理</option>
                </select>
              </div>
            </div>

            <div class="flex justify-end space-x-3 pt-4">
              <button
                @click="closeBatchAddDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
              <button
                @click="saveBatchAdd"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
              >
                批量添加
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 批量复制对话框 -->
      <div v-if="showBatchCopyDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white my-8">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">批量复制映射 (已选择 {{ selectedItems.length }} 条)</h3>
              <button @click="closeBatchCopyDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="confirmBatchCopy" class="space-y-4">
              <div class="bg-blue-50 border border-blue-200 rounded-md p-3 mb-4">
                <p class="text-sm text-blue-800">
                  <strong>提示：</strong>将复制选中的 {{ selectedItems.length }} 条记录。您可以选择修改某些字段，未修改的字段将保持原值。
                </p>
              </div>

              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <!-- 布面纸类型 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">布面纸类型（可选修改）</label>
                  <select
                    v-model="batchCopyForm.clothPaperTypeId"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option :value="null">保持原值</option>
                    <option v-for="option in clothPaperTypeOptions" :key="option.id" :value="option.id">
                      {{ option.label }}
                    </option>
                  </select>
                </div>

                <!-- 兼容性状态 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态（可选修改）</label>
                  <select
                    v-model="batchCopyForm.compatibilityStatus"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">保持原值</option>
                    <option value="V">适用</option>
                    <option value="X">不适用</option>
                    <option value="NA">不确定</option>
                    <option value="▷">需要打底处理</option>
                  </select>
                </div>

                <!-- 产品系列 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品系列（可选修改）</label>
                  <input
                    v-model="batchCopyForm.productName"
                    type="text"
                    placeholder="留空保持原值"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <!-- 产品型号 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品型号（可选修改）</label>
                  <input
                    v-model="batchCopyForm.productModelNumber"
                    type="text"
                    placeholder="留空保持原值"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <!-- 颜色 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">颜色（可选修改）</label>
                  <input
                    v-model="batchCopyForm.color"
                    type="text"
                    placeholder="留空保持原值"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <!-- 烫金纸性能类型 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型（可选修改）</label>
                  <input
                    v-model="batchCopyForm.paperType"
                    type="text"
                    placeholder="留空保持原值"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
              </div>

              <!-- 按钮 -->
              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button
                  type="button"
                  @click="closeBatchCopyDialog"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 transition-colors duration-200"
                >
                  取消
                </button>
                <button
                  type="submit"
                  class="inline-flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 transition-colors duration-200"
                >
                  确认复制
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 批量修改对话框 -->
      <div v-if="showBatchEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white my-8">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">批量修改映射 (已选择 {{ selectedItems.length }} 条)</h3>
              <button @click="closeBatchEditDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveBatchEdit" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <!-- 产品系列 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品系列</label>
                  <input
                    v-model="batchEditForm.productName"
                    type="text"
                    placeholder="留空则不修改"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <!-- 产品型号 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
                  <input
                    v-model="batchEditForm.productModelNumber"
                    type="text"
                    placeholder="留空则不修改"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <!-- 颜色 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">颜色</label>
                  <input
                    v-model="batchEditForm.color"
                    type="text"
                    placeholder="留空则不修改"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <!-- 布面纸类型 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">布面纸类型</label>
                  <select
                    v-model="batchEditForm.clothPaperTypeId"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option :value="null">不修改</option>
                    <option v-for="option in clothPaperTypeOptions" :key="option.id" :value="option.id">
                      {{ option.label }}
                    </option>
                  </select>
                </div>

                <!-- 烫金纸性能类型 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                  <input
                    v-model="batchEditForm.paperType"
                    type="text"
                    placeholder="留空则不修改"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <!-- 兼容性状态 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
                  <select
                    v-model="batchEditForm.compatibilityStatus"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">不修改</option>
                    <option value="V">适用</option>
                    <option value="X">不适用</option>
                    <option value="NA">不确定</option>
                    <option value="▷">需要打底处理</option>
                  </select>
                </div>
              </div>

              <!-- 提示信息 -->
              <div class="bg-blue-50 border border-blue-200 rounded-md p-3">
                <p class="text-sm text-blue-800">
                  <strong>提示：</strong>留空的字段将不会被修改。只有填写或选择的字段才会应用到选中的记录。
                </p>
              </div>

              <!-- 按钮 -->
              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button
                  type="button"
                  @click="closeBatchEditDialog"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 transition-colors duration-200"
                >
                  取消
                </button>
                <button
                  type="submit"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 transition-colors duration-200"
                >
                  保存
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 导入对话框 -->
      <div v-if="showImportDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">導入配置</h3>
              <button @click="closeImportDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <div class="space-y-4">
              <div>
                <button
                  @click="downloadImportTemplate"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                  </svg>
                  下載導入模板
                </button>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">選擇Excel文件</label>
                <input
                  ref="importFileInput"
                  type="file"
                  accept=".xlsx,.xls"
                  @change="handleFileSelect"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
                <p v-if="selectedFile" class="mt-2 text-sm text-gray-600">已選擇: {{ selectedFile.name }}</p>
              </div>

              <div v-if="importResult" class="p-4 rounded-md" :class="importResult.success ? 'bg-green-50 border border-green-200' : 'bg-red-50 border border-red-200'">
                <p class="text-sm font-medium" :class="importResult.success ? 'text-green-800' : 'text-red-800'">
                  {{ importResult.message }}
                </p>
                <div v-if="importResult.totalCount !== undefined" class="mt-2 text-sm" :class="importResult.success ? 'text-green-700' : 'text-red-700'">
                  <p>總數: {{ importResult.totalCount }}, 成功: {{ importResult.successCount }}, 失敗: {{ importResult.failCount }}</p>
                </div>
                <div v-if="importResult.errors && importResult.errors.length > 0" class="mt-2">
                  <p class="text-sm font-medium text-red-800">錯誤詳情:</p>
                  <ul class="mt-1 text-sm text-red-700 list-disc list-inside max-h-40 overflow-auto">
                    <li v-for="(error, index) in importResult.errors" :key="index">{{ error }}</li>
                  </ul>
                </div>
              </div>
            </div>

            <div class="flex justify-end space-x-3 pt-4">
              <button
                @click="closeImportDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                關閉
              </button>
              <button
                @click="executeImport"
                :disabled="!selectedFile || importing"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {{ importing ? '導入中...' : '開始導入' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { postProcessingPrintApi, type PostProcessingPrint, type CreatePostProcessingPrint } from '../../../api/modules/postProcessingPrint'
import { sharedProductApi, postProcessingLeduvglitterApi } from '../../../api/modules/postProcessingLeduvglitter'

// 响应式数据
const printConfigs = ref<PostProcessingPrint[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)

// 批量操作相关
const selectedItems = ref<number[]>([])
const showBatchAddDialog = ref(false)
const showBatchEditStatusDialog = ref(false)
const showBatchEditDialog = ref(false)
const showBatchCopyDialog = ref(false)
const showImportDialog = ref(false)

// 批量添加表单
const batchAddForm = reactive({
  paperType: '',
  selectedSeries: [] as string[],
  selectedProducts: {} as Record<string, string[]>, // 每个产品系列对应的产品型号列表
  clothPaperTypeId: 0 as number,
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷',
  color: '' as string // 颜色（可选）
})

// 批量添加时每个产品系列的产品列表
const batchAddSeriesProducts = ref<Record<string, Array<{ id: number, modelNumber: string, name: string }>>>({})
const batchAddAvailableSeries = ref<string[]>([])
const batchAddSeriesSearchText = ref('')

// 所有可用的性能类型
const availablePaperTypes = ref<string[]>([])

// 批量添加时的颜色选项
const batchAddAvailableColors = ref<string[]>([])
const loadingBatchAddColors = ref(false)

// 批量修改状态表单
const batchStatusForm = reactive({
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷'
})

// 导入相关
const selectedFile = ref<File | null>(null)
const importFileInput = ref<HTMLInputElement | null>(null)
const importResult = ref<{ success: boolean; message: string; totalCount?: number; successCount?: number; failCount?: number; errors?: string[] } | null>(null)
const importing = ref(false)

// 烫金纸性能类型联动选择相关数据
const availableProducts = ref<string[]>([])
const filteredPaperTypes = ref<string[]>([])
const loadingPaperTypes = ref(false)

// 产品型号联动数据
const availableProductModels = ref<string[]>([])
const loadingProductModels = ref(false)

// 布面纸类型选项数据
const clothPaperTypeOptions = ref<Array<{id: number, label: string}>>([])
const availableClothPaperTypes = ref<string[]>([])
const loadingClothPaperTypes = ref(false)

// 颜色选项数据
const availableColors = ref<string[]>([])
const loadingColors = ref(false)

// 分页相关数据
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)
const totalPages = ref(0)

// 搜索表单
const searchForm = reactive({
  productName: '',
  productModelNumber: '',
  color: '',
  paperType: '',
  clothPaperType: ''
})

// 印刷配置表单
const printConfigForm = reactive({
  id: null as number | null,
  productName: '',
  productModelNumber: '',
  color: '',
  clothPaperTypeId: 0,
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷',
  paperType: ''
})

// 获取兼容性状态文本
const getCompatibilityStatusText = (status: string) => {
  const statusMap: { [key: string]: string } = {
    'V': '适用',
    'X': '不适用',
    'NA': '不确定',
    '▷': '需要打底处理'
  }
  return statusMap[status] || status
}

// 获取布面纸类型名称
const getClothPaperTypeName = (clothPaperTypeId: number) => {
  const option = clothPaperTypeOptions.value.find(opt => opt.id === clothPaperTypeId)
  return option ? option.label : `ID: ${clothPaperTypeId}`
}

// 获取兼容性状态样式
const getCompatibilityStatusClass = (status: string) => {
  const classMap: { [key: string]: string } = {
    'V': 'bg-green-100 text-green-800',
    'X': 'bg-red-100 text-red-800',
    'NA': 'bg-yellow-100 text-yellow-800',
    '▷': 'bg-blue-100 text-blue-800'
  }
  return classMap[status] || 'bg-gray-100 text-gray-800'
}

// 加载印刷配置列表
const loadPrintConfigs = async () => {
  try {
    loading.value = true
    const response = await postProcessingPrintApi.getPrintConfigsWithPagination(currentPage.value, pageSize.value)
    printConfigs.value = response.content
    totalItems.value = response.totalElements
    totalPages.value = response.totalPages
  } catch (error) {
    console.error('加载印刷配置失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载可用的产品名称
const loadAvailableProducts = async () => {
  try {
    availableProducts.value = await sharedProductApi.getAllProductNames()
  } catch (error) {
    console.error('加载产品名称失败:', error)
  }
}

// 加载布面纸类型选项
const loadClothPaperTypeOptions = async () => {
  try {
    loadingClothPaperTypes.value = true
    clothPaperTypeOptions.value = await postProcessingLeduvglitterApi.getClothPaperTypeOptions()
    // 同时填充筛选用的布面纸类型列表
    availableClothPaperTypes.value = clothPaperTypeOptions.value.map(option => option.label)
  } catch (error) {
    console.error('加载布面纸类型选项失败:', error)
  } finally {
    loadingClothPaperTypes.value = false
  }
}

// 当产品名称改变时，加载对应的烫金纸性能类型和产品型号
const onProductNameChange = async () => {
  if (printConfigForm.productName) {
    // 清空之前的选择
    printConfigForm.paperType = ''
    printConfigForm.productModelNumber = ''
    printConfigForm.color = ''
    
    // 加载烫金纸性能类型
    loadingPaperTypes.value = true
    try {
      filteredPaperTypes.value = await sharedProductApi.getPaperTypesByProductName(printConfigForm.productName)
    } catch (error) {
      console.error('加载烫金纸性能类型失败:', error)
      filteredPaperTypes.value = []
    } finally {
      loadingPaperTypes.value = false
    }
    
    // 加载产品型号
    loadingProductModels.value = true
    try {
      availableProductModels.value = await sharedProductApi.getProductModelNumbersByProductName(printConfigForm.productName)
    } catch (error) {
      console.error('加载产品型号失败:', error)
      availableProductModels.value = []
    } finally {
      loadingProductModels.value = false
    }
    
    // 加载颜色选项
    await loadColors()
  } else {
    // 清空所有相关数据
    filteredPaperTypes.value = []
    availableProductModels.value = []
    availableColors.value = []
    printConfigForm.paperType = ''
    printConfigForm.productModelNumber = ''
    printConfigForm.color = ''
  }
}

// 智能加载颜色选项的函数
const loadColors = async () => {
  loadingColors.value = true
  try {
    if (printConfigForm.productName && printConfigForm.productModelNumber) {
      // 两个都选择了：使用最精确的查询
      availableColors.value = await postProcessingPrintApi.getColorsByProductAndModel(printConfigForm.productName, printConfigForm.productModelNumber)
    } else if (printConfigForm.productName) {
      // 只选择了燙金紙系列：显示该系列的所有颜色
      availableColors.value = await postProcessingPrintApi.getColorsByProductName(printConfigForm.productName)
    } else if (printConfigForm.productModelNumber) {
      // 只选择了产品型号：显示该型号的所有颜色
      availableColors.value = await postProcessingPrintApi.getColorsByProductModelNumber(printConfigForm.productModelNumber)
    } else {
      // 都没有选择：清空颜色选项
      availableColors.value = []
    }
  } catch (error) {
    console.error('加载颜色选项失败:', error)
    availableColors.value = []
  } finally {
    loadingColors.value = false
  }
}

// 当产品型号改变时，重新加载颜色选项
const onProductModelNumberChange = async () => {
  printConfigForm.color = '' // 清空之前的选择
  await loadColors()
}

// 搜索表单的产品名称改变处理
const onSearchProductNameChange = async () => {
  if (searchForm.productName) {
    loadingPaperTypes.value = true
    try {
      filteredPaperTypes.value = await sharedProductApi.getPaperTypesByProductName(searchForm.productName)
    } catch (error) {
      console.error('筛选：加载烫金纸性能类型失败:', error)
      filteredPaperTypes.value = []
    } finally {
      loadingPaperTypes.value = false
    }
  } else {
    filteredPaperTypes.value = []
  }
  // 清空烫金纸性能类型选择
  searchForm.paperType = ''
}

// 分页计算属性
const visiblePages = computed(() => {
  const total = totalPages.value
  const current = currentPage.value
  const pages = []
  
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

// 搜索印刷配置
const searchPrintConfigs = async () => {
  try {
    loading.value = true
    console.log('搜索参数:', searchForm)
    
    // 检查是否有任何搜索条件
    const hasSearchConditions = searchForm.productName || 
                               searchForm.productModelNumber || 
                               searchForm.color || 
                               searchForm.clothPaperType ||
                               searchForm.paperType
    
    if (!hasSearchConditions) {
      await loadPrintConfigs()
      return
    }
    
    // 获取所有数据然后在前端进行筛选
    const allConfigs = await postProcessingPrintApi.getAllPrintConfigs()
    let filteredConfigs = allConfigs
    
    // 按产品名称筛选
    if (searchForm.productName) {
      filteredConfigs = filteredConfigs.filter(config => 
        config.productName && config.productName.includes(searchForm.productName)
      )
    }
    
    // 按产品型号筛选
    if (searchForm.productModelNumber) {
      filteredConfigs = filteredConfigs.filter(config => 
        config.productModelNumber && config.productModelNumber.includes(searchForm.productModelNumber)
      )
    }
    
    // 按颜色筛选
    if (searchForm.color) {
      filteredConfigs = filteredConfigs.filter(config => 
        config.color && config.color.includes(searchForm.color)
      )
    }
    
    // 按布面纸类型筛选
    if (searchForm.clothPaperType) {
      // 需要根据布面纸类型名称找到对应的ID
      const clothPaperTypeOption = clothPaperTypeOptions.value.find(option => 
        option.label === searchForm.clothPaperType
      )
      if (clothPaperTypeOption) {
        filteredConfigs = filteredConfigs.filter(config => 
          config.clothPaperTypeId === clothPaperTypeOption.id
        )
      }
    }
    
    // 按烫金纸性能类型筛选
    if (searchForm.paperType) {
      filteredConfigs = filteredConfigs.filter(config => 
        config.paperType && config.paperType.includes(searchForm.paperType)
      )
    }
    
    // 更新分页信息
    totalItems.value = filteredConfigs.length
    totalPages.value = Math.ceil(filteredConfigs.length / pageSize.value)
    
    // 如果当前页超出了总页数，重置到第一页
    if (currentPage.value > totalPages.value && totalPages.value > 0) {
      currentPage.value = 1
    }
    
    // 应用分页
    const startIndex = (currentPage.value - 1) * pageSize.value
    const endIndex = startIndex + pageSize.value
    printConfigs.value = filteredConfigs.slice(startIndex, endIndex)
    
    console.log('筛选结果:', filteredConfigs.length, '条记录，当前页:', currentPage.value, '显示:', printConfigs.value.length, '条')
    console.log('分页信息 - 总条数:', totalItems.value, '总页数:', totalPages.value, '每页条数:', pageSize.value)
    
  } catch (error) {
    console.error('搜索印刷配置失败:', error)
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = async () => {
  // 清空所有搜索条件
  searchForm.productName = ''
  searchForm.productModelNumber = ''
  searchForm.color = ''
  searchForm.clothPaperType = ''
  searchForm.paperType = ''
  
  // 重置分页到第一页
  currentPage.value = 1
  
  // 重新加载所有数据
  await loadPrintConfigs()
}

// 编辑印刷配置
const editPrintConfig = async (config: PostProcessingPrint) => {
  
  // 先赋值表单数据
  Object.assign(printConfigForm, {
    id: config.id,
    productName: config.productName,
    productModelNumber: config.productModelNumber,
    color: config.color,
    clothPaperTypeId: config.clothPaperTypeId,
    compatibilityStatus: config.compatibilityStatus,
    paperType: config.paperType || ''
  })
  
  // 重新加载相关的选项数据
  // 加载烫金纸性能类型选项（如果有产品名称）
  if (config.productName) {
    loadingPaperTypes.value = true
    try {
      filteredPaperTypes.value = await sharedProductApi.getPaperTypesByProductName(config.productName)
    } catch (error) {
      console.error('编辑时加载烫金纸性能类型失败:', error)
      filteredPaperTypes.value = []
    } finally {
      loadingPaperTypes.value = false
    }
  }
  
  // 加载产品型号选项（如果有产品名称）
  if (config.productName) {
    loadingProductModels.value = true
    try {
      availableProductModels.value = await sharedProductApi.getProductModelNumbersByProductName(config.productName)
    } catch (error) {
      console.error('编辑时加载产品型号失败:', error)
      availableProductModels.value = []
    } finally {
      loadingProductModels.value = false
    }
  }
  
  // 加载颜色选项（基于产品名称和型号）
  await loadColors()
  
  showEditDialog.value = true
}

// 删除印刷配置
const deletePrintConfig = async (id: number) => {
  if (confirm('确定要删除这个印刷配置吗？')) {
    try {
      await postProcessingPrintApi.deletePrintConfig(id)
      await loadDataWithCurrentFilter()
    } catch (error) {
      console.error('删除印刷配置失败:', error)
    }
  }
}

// 保存印刷配置
const savePrintConfig = async () => {
  try {
    if (showAddDialog.value) {
      // 检查唯一性
      const existing = await postProcessingPrintApi.checkUnique(
        printConfigForm.productName,
        printConfigForm.productModelNumber || undefined,
        printConfigForm.color || undefined,
        printConfigForm.clothPaperTypeId,
        printConfigForm.paperType || undefined
      )
      
      if (existing) {
        alert('该配置已存在，请勿重复添加')
        return
      }
      
      const createData: CreatePostProcessingPrint = {
        productName: printConfigForm.productName,
        productModelNumber: printConfigForm.productModelNumber,
        color: printConfigForm.color,
        clothPaperTypeId: printConfigForm.clothPaperTypeId,
        compatibilityStatus: printConfigForm.compatibilityStatus,
        paperType: printConfigForm.paperType
      }
      await postProcessingPrintApi.createPrintConfig(createData)
    } else {
      // 编辑时检查唯一性（排除当前记录）
      const existing = await postProcessingPrintApi.checkUnique(
        printConfigForm.productName,
        printConfigForm.productModelNumber || undefined,
        printConfigForm.color || undefined,
        printConfigForm.clothPaperTypeId,
        printConfigForm.paperType || undefined
      )
      
      if (existing && existing.id !== printConfigForm.id) {
        alert('该配置已存在，请修改唯一键字段（燙金紙系列、产品型号、颜色、布面纸类型、烫金纸性能类型）')
        return
      }
      
      await postProcessingPrintApi.updatePrintConfig(printConfigForm.id!, printConfigForm)
    }
    
    await loadDataWithCurrentFilter()
    closeDialog()
  } catch (error: any) {
    console.error('保存印刷配置失败:', error)
    if (error.response?.status === 400 && error.response?.data?.message) {
      alert(error.response.data.message)
    } else {
      alert('保存失败')
    }
  }
}

// 关闭对话框
const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(printConfigForm, {
    id: null,
    productName: '',
    productModelNumber: '',
    color: '',
    clothPaperTypeId: 0,
    compatibilityStatus: 'NA',
    paperType: ''
  })
  // 清空联动选择数据
  availableProductModels.value = []
  filteredPaperTypes.value = []
  availableColors.value = []
}

// 分页导航方法
const goToPreviousPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--
    loadDataWithCurrentFilter()
  }
}

const goToNextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
    loadDataWithCurrentFilter()
  }
}

const goToPage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page
    loadDataWithCurrentFilter()
  }
}

// 根据当前筛选条件加载数据
const loadDataWithCurrentFilter = async () => {
  // 检查是否有筛选条件
  const hasSearchConditions = searchForm.productName || 
                             searchForm.productModelNumber || 
                             searchForm.color || 
                             searchForm.clothPaperType ||
                             searchForm.paperType
  
  if (hasSearchConditions) {
    // 如果有筛选条件，重新执行搜索
    await searchPrintConfigs()
  } else {
    // 如果没有筛选条件，正常加载数据
    await loadPrintConfigs()
  }
}

// 复选框选择相关函数
const toggleSelectItem = (id: number) => {
  const index = selectedItems.value.indexOf(id)
  if (index > -1) {
    selectedItems.value.splice(index, 1)
  } else {
    selectedItems.value.push(id)
  }
}

const toggleSelectAll = () => {
  if (selectedItems.value.length === printConfigs.value.length) {
    selectedItems.value = []
  } else {
    selectedItems.value = printConfigs.value.map(config => config.id as number).filter(id => id !== undefined)
  }
}

const clearSelection = () => {
  selectedItems.value = []
}

// 加载产品名称对应的烫金纸性能类型
const loadPaperTypesByProductName = async (productName: string) => {
  if (!productName) {
    filteredPaperTypes.value = []
    return
  }
  
  loadingPaperTypes.value = true
  try {
    filteredPaperTypes.value = await sharedProductApi.getPaperTypesByProductName(productName)
  } catch (error) {
    console.error('加载烫金纸性能类型失败:', error)
    filteredPaperTypes.value = []
  } finally {
    loadingPaperTypes.value = false
  }
}

// 加载产品名称对应的产品型号
const loadProductModelsByProductName = async (productName: string) => {
  if (!productName) {
    availableProductModels.value = []
    return
  }
  
  loadingProductModels.value = true
  try {
    availableProductModels.value = await sharedProductApi.getProductModelNumbersByProductName(productName)
  } catch (error) {
    console.error('加载产品型号失败:', error)
    availableProductModels.value = []
  } finally {
    loadingProductModels.value = false
  }
}

// 复制配置
const copyPrintConfig = async (config: PostProcessingPrint) => {
  printConfigForm.id = null
  printConfigForm.productName = config.productName
  printConfigForm.productModelNumber = config.productModelNumber
  printConfigForm.color = config.color
  printConfigForm.clothPaperTypeId = config.clothPaperTypeId
  printConfigForm.compatibilityStatus = config.compatibilityStatus
  printConfigForm.paperType = config.paperType || ''
  
  // 加载相关选项
  if (config.productName) {
    await loadPaperTypesByProductName(config.productName)
    await loadProductModelsByProductName(config.productName)
  }
  await loadColors()
  
  showAddDialog.value = true
}

// 批量添加相关函数
const filteredBatchAddSeries = computed(() => {
  if (!batchAddSeriesSearchText.value) {
    return batchAddAvailableSeries.value
  }
  const searchText = batchAddSeriesSearchText.value.toLowerCase()
  return batchAddAvailableSeries.value.filter(series => 
    series.toLowerCase().includes(searchText)
  )
})

const openBatchAddDialog = async () => {
  // 加载所有可用的性能类型
  try {
    const paperTypes = await sharedProductApi.getAllPaperTypes()
    availablePaperTypes.value = paperTypes
  } catch (error) {
    console.error('加载性能类型失败:', error)
    availablePaperTypes.value = []
  }
  
  // 重置表单
  batchAddForm.paperType = ''
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddForm.clothPaperTypeId = 0
  batchAddForm.compatibilityStatus = 'NA'
  batchAddForm.color = ''
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
  batchAddSeriesProducts.value = {}
  batchAddAvailableColors.value = []
  
  showBatchAddDialog.value = true
}

const closeBatchAddDialog = () => {
  showBatchAddDialog.value = false
  batchAddForm.paperType = ''
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddForm.clothPaperTypeId = 0
  batchAddForm.color = ''
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
  batchAddSeriesProducts.value = {}
  batchAddAvailableColors.value = []
}

const onBatchAddPaperTypeChange = async () => {
  if (!batchAddForm.paperType) {
    batchAddAvailableSeries.value = []
    batchAddForm.selectedSeries = []
    batchAddSeriesProducts.value = {}
    batchAddForm.selectedProducts = {}
    return
  }
  
  try {
    const response = await fetch(`/api/api/products/series-by-paper-type/${encodeURIComponent(batchAddForm.paperType)}`)
    if (response.ok) {
      const series = await response.json()
      batchAddAvailableSeries.value = series
    } else {
      batchAddAvailableSeries.value = []
    }
  } catch (error) {
    console.error('加载产品系列失败:', error)
    batchAddAvailableSeries.value = []
    }
    
  // 清空之前的选择
  batchAddForm.selectedSeries = []
  batchAddSeriesProducts.value = {}
  batchAddForm.selectedProducts = {}
}

const onBatchAddSeriesChange = async (seriesName: string) => {
  const isSelected = batchAddForm.selectedSeries.includes(seriesName)
  
  if (isSelected) {
    // 选择了产品系列，加载该系列的产品列表
    if (!batchAddSeriesProducts.value[seriesName]) {
      try {
        const response = await fetch(`/api/api/products/series/${encodeURIComponent(seriesName)}`)
        if (response.ok) {
          const products = await response.json()
          batchAddSeriesProducts.value[seriesName] = products.map((p: any) => ({
            id: p.id,
            modelNumber: p.model_number || p.modelNumber || '',
            name: p.name || ''
          }))
          // 初始化该系列的产品型号选择数组
          if (!batchAddForm.selectedProducts[seriesName]) {
            batchAddForm.selectedProducts[seriesName] = []
          }
        } else {
          batchAddSeriesProducts.value[seriesName] = []
        }
      } catch (error) {
        console.error(`加载产品系列 ${seriesName} 的产品列表失败:`, error)
        batchAddSeriesProducts.value[seriesName] = []
      }
    }
  } else {
    // 取消选择产品系列，清除该系列的产品数据
    delete batchAddSeriesProducts.value[seriesName]
    delete batchAddForm.selectedProducts[seriesName]
  }
  
  // 重新加载颜色选项
  await loadBatchAddColors()
}

// 加载批量添加时的颜色选项
const loadBatchAddColors = async () => {
  // 颜色选项应该是所有选中系列/型号的交集（即所有选中的系列/型号都支持的颜色）
  // 这样可以避免添加无效数据（某个颜色只属于某个系列，但被应用到所有系列）
  
  if (batchAddForm.selectedSeries.length === 0) {
    batchAddAvailableColors.value = []
    return
  }
  
  loadingBatchAddColors.value = true
  try {
    // 收集所有需要检查的颜色集合
    const colorSets: Set<string>[] = []
    
    // 检查是否有选中的产品型号
    const hasSelectedProducts = Object.values(batchAddForm.selectedProducts).some(
      products => products && products.length > 0
    )
    
    if (hasSelectedProducts) {
      // 如果有选中的产品型号，根据系列和型号加载颜色
      for (const seriesName of batchAddForm.selectedSeries) {
        const selectedProducts = batchAddForm.selectedProducts[seriesName] || []
        
        if (selectedProducts.length > 0) {
          // 为每个选中的型号加载颜色
          for (const modelNumber of selectedProducts) {
            try {
              const colors = await postProcessingPrintApi.getColorsByProductAndModel(seriesName, modelNumber)
              colorSets.push(new Set(colors))
            } catch (error) {
              console.error(`加载颜色失败 (${seriesName}, ${modelNumber}):`, error)
              // 如果某个型号加载失败，添加空集合，这样交集结果也会是空的
              colorSets.push(new Set<string>())
            }
          }
        } else {
          // 如果没有选择型号，根据系列加载颜色
          try {
            const colors = await postProcessingPrintApi.getColorsByProductName(seriesName)
            colorSets.push(new Set(colors))
          } catch (error) {
            console.error(`加载颜色失败 (${seriesName}):`, error)
            colorSets.push(new Set<string>())
          }
        }
      }
    } else {
      // 如果没有选择任何型号，根据系列加载颜色
      for (const seriesName of batchAddForm.selectedSeries) {
        try {
          const colors = await postProcessingPrintApi.getColorsByProductName(seriesName)
          colorSets.push(new Set(colors))
        } catch (error) {
          console.error(`加载颜色失败 (${seriesName}):`, error)
          colorSets.push(new Set<string>())
        }
      }
    }
    
    // 计算交集：找出所有集合中都存在的颜色
    if (colorSets.length === 0) {
      batchAddAvailableColors.value = []
    } else if (colorSets.length === 1) {
      // 只有一个集合，直接使用
      batchAddAvailableColors.value = Array.from(colorSets[0]).sort()
    } else {
      // 多个集合，计算交集
      const intersection = new Set<string>()
      const firstSet = colorSets[0]
      
      // 遍历第一个集合中的每个颜色
      for (const color of firstSet) {
        // 检查这个颜色是否在所有其他集合中都存在
        const existsInAll = colorSets.slice(1).every(set => set.has(color))
        if (existsInAll) {
          intersection.add(color)
        }
      }
      
      batchAddAvailableColors.value = Array.from(intersection).sort()
    }
  } catch (error) {
    console.error('加载批量添加颜色选项失败:', error)
    batchAddAvailableColors.value = []
  } finally {
    loadingBatchAddColors.value = false
  }
}

// 批量添加时颜色变化处理（暂时不需要特殊处理，但保留接口）
const onBatchAddColorChange = () => {
  // 颜色变化时不需要特殊处理
}

const saveBatchAdd = async () => {
  if (!batchAddForm.paperType) {
    alert('請選擇燙金紙性能類型')
    return
  }
  
  if (batchAddForm.selectedSeries.length === 0) {
    alert('請至少選擇一個產品系列')
    return
  }
  
  if (!batchAddForm.clothPaperTypeId || batchAddForm.clothPaperTypeId === 0) {
    alert('請選擇布面紙類型')
    return
  }
  
  try {
    const configsToAdd: CreatePostProcessingPrint[] = []
    
    for (const seriesName of batchAddForm.selectedSeries) {
      const selectedProducts = batchAddForm.selectedProducts[seriesName] || []
      
      if (selectedProducts.length === 0) {
        // 如果没有选择产品型号，创建通用映射（productModelNumber为空）
        configsToAdd.push({
          productName: seriesName,
          productModelNumber: '',
          color: batchAddForm.color || '',
          clothPaperTypeId: batchAddForm.clothPaperTypeId,
          compatibilityStatus: batchAddForm.compatibilityStatus,
          paperType: batchAddForm.paperType
        })
      } else {
        // 为每个选中的产品型号创建配置
        for (const modelNumber of selectedProducts) {
          configsToAdd.push({
            productName: seriesName,
            productModelNumber: modelNumber,
            color: batchAddForm.color || '',
            clothPaperTypeId: batchAddForm.clothPaperTypeId,
            compatibilityStatus: batchAddForm.compatibilityStatus,
            paperType: batchAddForm.paperType
          })
        }
      }
    }
    
    // 批量创建配置
    for (const config of configsToAdd) {
      try {
        // 检查唯一性
        const existing = await postProcessingPrintApi.checkUnique(
          config.productName,
          config.productModelNumber || undefined,
          config.color || undefined,
          config.clothPaperTypeId,
          config.paperType || undefined
        )
        
        if (existing) {
          console.warn(`配置已存在，跳过: ${config.productName} - ${config.productModelNumber || '(通用)'}`)
          continue
        }
        
        await postProcessingPrintApi.createPrintConfig(config)
      } catch (error: any) {
        console.error('批量添加配置失败:', error)
        if (error.response?.status === 400 && error.response?.data?.message) {
          console.warn(`配置已存在或验证失败: ${error.response.data.message}`)
        }
      }
    }
    
    alert(`成功添加 ${configsToAdd.length} 条配置`)
    await loadDataWithCurrentFilter()
    closeBatchAddDialog()
  } catch (error) {
    console.error('批量添加失败:', error)
    alert('批量添加失败，请检查控制台')
  }
}

// 批量编辑状态相关函数
// 批量修改对话框
const batchEditForm = reactive({
  productName: '',
  productModelNumber: '',
  color: '',
  clothPaperTypeId: null as number | null,
  paperType: '',
  compatibilityStatus: '' as '' | 'V' | 'X' | 'NA' | '▷'
})

// 批量复制表单
const batchCopyForm = reactive({
  productName: '',
  productModelNumber: '',
  color: '',
  clothPaperTypeId: null as number | null,
  paperType: '',
  compatibilityStatus: '' as '' | 'V' | 'X' | 'NA' | '▷'
})

const openBatchEditDialog = () => {
  if (selectedItems.value.length === 0) {
    alert('请先选择要修改的记录')
    return
  }
  showBatchEditDialog.value = true
  // 重置表单
  Object.assign(batchEditForm, {
    productName: '',
    productModelNumber: '',
    color: '',
    clothPaperTypeId: null,
    paperType: '',
    compatibilityStatus: ''
  })
}

const closeBatchEditDialog = () => {
  showBatchEditDialog.value = false
}

const saveBatchEdit = async () => {
  if (selectedItems.value.length === 0) {
    alert('请先选择要修改的记录')
    return
  }
  
  // 构建更新字段对象（只包含非空字段）
  const updateFields: Record<string, any> = {}
  
  if (batchEditForm.productName && batchEditForm.productName.trim()) {
    updateFields.productName = batchEditForm.productName.trim()
  }
  if (batchEditForm.productModelNumber && batchEditForm.productModelNumber.trim()) {
    updateFields.productModelNumber = batchEditForm.productModelNumber.trim()
  }
  if (batchEditForm.color && batchEditForm.color.trim()) {
    updateFields.color = batchEditForm.color.trim()
  }
  if (batchEditForm.clothPaperTypeId !== null) {
    updateFields.clothPaperTypeId = batchEditForm.clothPaperTypeId
  }
  if (batchEditForm.paperType && batchEditForm.paperType.trim()) {
    updateFields.paperType = batchEditForm.paperType.trim()
  }
  if (batchEditForm.compatibilityStatus !== '') {
    updateFields.compatibilityStatus = batchEditForm.compatibilityStatus
  }
  
  if (Object.keys(updateFields).length === 0) {
    alert('请至少填写一个要修改的字段')
    return
  }
  
  try {
    // 批量更新每条记录
    let successCount = 0
    let failCount = 0
    const errors: string[] = []
    
    for (const id of selectedItems.value) {
      try {
        // 获取当前记录
        const item = printConfigs.value.find(c => c.id === id)
        if (!item) {
          failCount++
          errors.push(`ID ${id}: 记录不存在`)
          continue
        }
        
        // 构建更新数据
        const updateData = {
          ...item,
          ...updateFields
        }
        
        await postProcessingPrintApi.updatePrintConfig(id, updateData)
        successCount++
      } catch (error: any) {
        failCount++
        const errorMessage = error.response?.data?.message || error.message || '更新失败'
        errors.push(`ID ${id}: ${errorMessage}`)
      }
    }
    
    if (failCount === 0) {
      alert(`批量修改成功！成功修改 ${successCount} 条记录`)
    } else {
      let message = `批量修改完成！\n成功: ${successCount} 条`
      if (failCount > 0) {
        message += `\n失败: ${failCount} 条`
        if (errors.length > 0 && errors.length <= 10) {
          message += `\n\n失败详情:\n${errors.join('\n')}`
        }
      }
      alert(message)
    }
    
    if (successCount > 0) {
      closeBatchEditDialog()
      clearSelection()
      await loadDataWithCurrentFilter()
    }
  } catch (error) {
    console.error('批量修改失败:', error)
    alert('批量修改失败，请重试')
  }
}

const openBatchEditStatusDialog = () => {
  if (selectedItems.value.length === 0) {
    alert('請先選擇要修改的記錄')
    return
  }
  batchStatusForm.compatibilityStatus = 'NA'
  showBatchEditStatusDialog.value = true
}

const closeBatchEditStatusDialog = () => {
  showBatchEditStatusDialog.value = false
}

const saveBatchEditStatus = async () => {
  try {
    const result = await postProcessingPrintApi.batchUpdateStatus(
      selectedItems.value,
      batchStatusForm.compatibilityStatus
    )
    
    if (result.success) {
      alert(result.message || '批量修改成功')
      await loadDataWithCurrentFilter()
      clearSelection()
      closeBatchEditStatusDialog()
    } else {
      alert(result.message || '批量修改失败')
    }
  } catch (error: any) {
    console.error('批量修改状态失败:', error)
    alert(error.response?.data?.message || '批量修改失败')
      }
    }
    
// 批量复制 - 打开对话框
const batchCopyPrintConfigs = () => {
  if (selectedItems.value.length === 0) {
    alert('请先选择要复制的记录')
    return
  }
  // 清空复制表单
  Object.assign(batchCopyForm, {
    productName: '',
    productModelNumber: '',
    color: '',
    clothPaperTypeId: null,
    paperType: '',
    compatibilityStatus: ''
  })
  showBatchCopyDialog.value = true
}

// 关闭批量复制对话框
const closeBatchCopyDialog = () => {
  showBatchCopyDialog.value = false
  Object.assign(batchCopyForm, {
    productName: '',
    productModelNumber: '',
    color: '',
    clothPaperTypeId: null,
    paperType: '',
    compatibilityStatus: ''
  })
}

// 确认批量复制
const confirmBatchCopy = async () => {
  try {
    let successCount = 0
    let failCount = 0
    const errors: string[] = []
    
    for (const id of selectedItems.value) {
      try {
        // 获取当前记录
        const item = printConfigs.value.find(c => c.id === id)
        if (!item) {
          failCount++
          errors.push(`ID ${id}: 记录不存在`)
          continue
        }
        
        // 构建复制数据，应用用户选择的修改
        // 如果用户填写了字段，使用用户的值；否则保持原值
        const createData: CreatePostProcessingPrint = {
          productName: batchCopyForm.productName && batchCopyForm.productName.trim() !== '' 
            ? batchCopyForm.productName.trim() 
            : item.productName,
          productModelNumber: batchCopyForm.productModelNumber && batchCopyForm.productModelNumber.trim() !== '' 
            ? batchCopyForm.productModelNumber.trim() 
            : item.productModelNumber,
          color: batchCopyForm.color && batchCopyForm.color.trim() !== '' 
            ? batchCopyForm.color.trim() 
            : item.color,
          clothPaperTypeId: batchCopyForm.clothPaperTypeId !== null 
            ? batchCopyForm.clothPaperTypeId 
            : item.clothPaperTypeId,
          compatibilityStatus: batchCopyForm.compatibilityStatus !== '' 
            ? batchCopyForm.compatibilityStatus 
            : item.compatibilityStatus,
          paperType: batchCopyForm.paperType && batchCopyForm.paperType.trim() !== '' 
            ? batchCopyForm.paperType.trim() 
            : item.paperType
        }
        
        // 检查唯一性（复制时检查是否已存在相同的记录）
        const existing = await postProcessingPrintApi.checkUnique(
          createData.productName,
          createData.productModelNumber || undefined,
          createData.color || undefined,
          createData.clothPaperTypeId,
          createData.paperType || undefined
        )
        
        if (existing) {
          failCount++
          errors.push(`ID ${id}: 该配置已存在，跳过复制`)
          continue
        }
        
        // 创建新记录（复制）
        await postProcessingPrintApi.createPrintConfig(createData)
        successCount++
      } catch (error: any) {
        failCount++
        const errorMessage = error.response?.data?.message || error.message || '复制失败'
        errors.push(`ID ${id}: ${errorMessage}`)
      }
    }
    
    // 显示结果
    if (failCount === 0) {
      alert(`批量复制成功！成功复制 ${successCount} 条记录`)
    } else {
      let message = `批量复制完成！\n成功: ${successCount} 条`
      if (failCount > 0) {
        message += `\n失败: ${failCount} 条`
        if (errors.length > 0 && errors.length <= 10) {
          message += `\n\n失败详情:\n${errors.join('\n')}`
        } else if (errors.length > 10) {
          message += `\n\n失败详情（前10条）:\n${errors.slice(0, 10).join('\n')}\n...还有 ${errors.length - 10} 条错误`
        }
      }
      alert(message)
    }
    
    if (successCount > 0) {
      closeBatchCopyDialog()
      clearSelection()
      await loadDataWithCurrentFilter()
    }
  } catch (error) {
    console.error('批量复制失败:', error)
    alert('批量复制失败，请重试')
  }
}
    
// 批量删除
const batchDeletePrintConfigs = async () => {
  if (selectedItems.value.length === 0) {
    alert('請先選擇要刪除的記錄')
    return
  }
  
  if (!confirm(`確定要刪除選中的 ${selectedItems.value.length} 條記錄嗎？`)) {
    return
  }
  
  try {
    const result = await postProcessingPrintApi.batchDeletePrintConfigs(selectedItems.value)
    
    if (result.success) {
      alert(result.message || '批量刪除成功')
      await loadDataWithCurrentFilter()
      clearSelection()
    } else {
      alert(result.message || '批量刪除失败')
    }
  } catch (error: any) {
    console.error('批量刪除失败:', error)
    alert(error.response?.data?.message || '批量刪除失败')
  }
}

// 导入相关函数
const openImportDialog = () => {
  selectedFile.value = null
  importResult.value = null
  if (importFileInput.value) {
    importFileInput.value.value = ''
  }
  showImportDialog.value = true
}

const closeImportDialog = () => {
  showImportDialog.value = false
  selectedFile.value = null
  importResult.value = null
}

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    selectedFile.value = target.files[0]
    importResult.value = null
  }
}

const downloadImportTemplate = async () => {
  try {
    const blob = await postProcessingPrintApi.downloadImportTemplate()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '印刷配置导入模板.xlsx'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (error) {
    console.error('下载导入模板失败:', error)
    alert('下载导入模板失败')
  }
}

const executeImport = async () => {
  if (!selectedFile.value) {
    alert('請選擇要導入的文件')
    return
  }
  
  try {
    importing.value = true
    importResult.value = null
    
    const result = await postProcessingPrintApi.importPrintConfigs(selectedFile.value)
    
    importResult.value = {
      success: result.success,
      message: result.message,
      totalCount: result.totalCount,
      successCount: result.successCount,
      failCount: result.failCount,
      errors: result.errorMessages
    }
    
    if (result.success) {
      await loadDataWithCurrentFilter()
    }
  } catch (error: any) {
    console.error('导入失败:', error)
    importResult.value = {
      success: false,
      message: error.response?.data?.message || '导入失败',
      errors: [error.message]
    }
  } finally {
    importing.value = false
  }
}

// 导出数据
const exportData = async () => {
  try {
    const blob = await postProcessingPrintApi.exportPrintConfigs()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `印刷配置_${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (error) {
    console.error('导出失败:', error)
    alert('导出失败')
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadPrintConfigs()
  loadAvailableProducts()
  loadClothPaperTypeOptions()
})
</script>
