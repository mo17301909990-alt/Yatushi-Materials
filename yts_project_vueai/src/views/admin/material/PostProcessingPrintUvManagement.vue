<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">UV印刷配置管理</h1>
            <p class="mt-2 text-gray-600">管理印刷UV後加工的配置信息</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="showAddDialog = true"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加配置
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
              导出
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
        <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700 mb-2">产品系列</label>
            <input
              v-model="searchForm.productName"
              @input="onProductNameInput"
              @focus="showProductNameSuggestions = true"
              @blur="handleProductNameBlur"
              type="text"
              placeholder="输入产品系列进行筛选"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <!-- 产品系列提示下拉框 -->
            <div
              v-if="showProductNameSuggestions && filteredProductNameSuggestions.length > 0"
              class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto"
            >
              <div
                v-for="suggestion in filteredProductNameSuggestions"
                :key="suggestion"
                @mousedown.prevent="selectProductName(suggestion)"
                class="px-4 py-2 hover:bg-blue-50 cursor-pointer text-sm"
              >
                {{ suggestion }}
              </div>
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
            <select
              v-model="searchForm.paperType"
              :disabled="!searchForm.productName"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
            >
              <option value="">
                {{ !searchForm.productName ? '请先选择燙金紙系列' : '全部烫金纸性能类型' }}
              </option>
              <option v-for="paperType in filteredPaperTypes" :key="paperType" :value="paperType">
                {{ paperType }}
              </option>
            </select>
          </div>
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
            <input
              v-model="searchForm.productModelNumber"
              @input="onProductModelNumberInput"
              @focus="showProductModelNumberSuggestions = true"
              @blur="handleProductModelNumberBlur"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="搜索产品型号"
            />
            <!-- 产品型号提示下拉框 -->
            <div
              v-if="showProductModelNumberSuggestions && filteredProductModelNumberSuggestions.length > 0"
              class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto"
            >
              <div
                v-for="suggestion in filteredProductModelNumberSuggestions"
                :key="suggestion"
                @mousedown.prevent="selectProductModelNumber(suggestion)"
                class="px-4 py-2 hover:bg-blue-50 cursor-pointer text-sm"
              >
                {{ suggestion }}
              </div>
            </div>
          </div>
          <div class="relative">
            <label class="block text-sm font-medium text-gray-700 mb-2">公司编号/客户GP号</label>
            <input
              v-model="searchForm.companyNumber"
              @input="onCompanyNumberInput"
              @focus="showCompanyNumberSuggestions = true"
              @blur="handleCompanyNumberBlur"
              type="text"
              placeholder="输入公司编号或客户GP号进行筛选"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
            <!-- 公司编号提示下拉框 -->
            <div
              v-if="showCompanyNumberSuggestions && filteredCompanyNumberSuggestions.length > 0"
              class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto"
            >
              <div
                v-for="suggestion in filteredCompanyNumberSuggestions"
                :key="suggestion"
                @mousedown.prevent="selectCompanyNumber(suggestion)"
                class="px-4 py-2 hover:bg-blue-50 cursor-pointer text-sm"
              >
                {{ suggestion }}
              </div>
            </div>
          </div>
          <div class="flex items-end space-x-2">
            <button
              @click="searchUvPrintConfigs"
              class="flex-1 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors"
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
              @click="openBatchEditStatusDialog"
              class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 mr-2"
            >
              批量修改状态
            </button>
            <button
              @click="clearSelection"
              class="inline-flex items-center px-3 py-2 border border-gray-300 text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
            >
              清除选择
            </button>
          </div>
        </div>
      </div>

      <!-- UV印刷配置列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">UV印刷配置列表</h3>
        </div>
        
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  <input
                    type="checkbox"
                    :checked="selectedItems.length === uvPrintConfigs.length && uvPrintConfigs.length > 0"
                    @change="toggleSelectAll"
                    class="rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                  />
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙系列</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品型号</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">公司编号/客户GP号</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="config in uvPrintConfigs" :key="config.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <input
                    type="checkbox"
                    :checked="selectedItems.includes(config.id!)"
                    @change="toggleSelectItem(config.id!)"
                    class="rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                  />
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ config.productName }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ config.productModelNumber || '-' }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ config.companyNumber }}</div>
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
                      @click="copyUvPrintConfig(config)"
                      class="text-green-600 hover:text-green-900"
                      title="复制"
                    >
                      复制
                    </button>
                    <button
                      @click="editUvPrintConfig(config)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="deleteUvPrintConfig(config.id)"
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
                      ? 'z-10 bg-blue-50 border-blue-500 text-blue-600'
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
                {{ showAddDialog ? '添加UV印刷配置' : '编辑UV印刷配置' }}
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

            <form @submit.prevent="saveUvPrintConfig">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列 *</label>
                  <input
                    v-model="uvPrintConfigForm.productName"
                    @change="onProductNameChange"
                    list="uv-print-product-name-options"
                    required
                    type="text"
                    placeholder="請輸入或選擇燙金紙系列"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <!-- 燙金紙系列候選列表 -->
                  <datalist id="uv-print-product-name-options">
                    <option v-for="product in availableProducts" :key="product" :value="product" />
                  </datalist>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙性能類型</label>
                  <select
                    v-model="uvPrintConfigForm.paperType"
                    :disabled="!uvPrintConfigForm.productName || loadingPaperTypes"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingPaperTypes ? '加載中...' : !uvPrintConfigForm.productName ? '請先選擇燙金紙系列' : '請選擇燙金紙性能類型' }}
                    </option>
                    <option v-for="paperType in filteredPaperTypes" :key="paperType" :value="paperType">
                      {{ paperType }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品型号 *</label>
                  <input
                    v-model="uvPrintConfigForm.productModelNumber"
                    @change="onProductModelNumberChange"
                    :disabled="!uvPrintConfigForm.productName || loadingProductModels"
                    list="uv-print-product-model-options"
                    required
                    type="text"
                    :placeholder="loadingProductModels ? '加載中...' : !uvPrintConfigForm.productName ? '請先選擇燙金紙系列' : '請輸入或選擇產品型號'"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  />
                  <!-- 產品型號候選列表 -->
                  <datalist id="uv-print-product-model-options">
                    <option v-for="model in availableProductModels" :key="model" :value="model" />
                  </datalist>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">公司編號/客戶GP號</label>
                  <select
                    v-model="uvPrintConfigForm.companyNumber"
                    :disabled="!uvPrintConfigForm.productName || loadingCompanyNumbers"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingCompanyNumbers ? '加載中...' : !uvPrintConfigForm.productName ? '請先選擇燙金紙系列' : availableCompanyNumbersForForm.length === 0 ? '該系列/型號下無可用公司編號或客戶GP號' : '請選擇公司編號或客戶GP號（可選）' }}
                    </option>
                    <option v-for="companyNumber in availableCompanyNumbersForForm" :key="companyNumber" :value="companyNumber">
                      {{ companyNumber }}
                    </option>
                  </select>
                  <p v-if="uvPrintConfigForm.productName && availableCompanyNumbersForForm.length === 0 && !loadingCompanyNumbers" class="mt-1 text-xs text-yellow-600">
                    該產品系列/型號下暫無公司編號或客戶GP號
                  </p>
                  <p v-else-if="uvPrintConfigForm.productName" class="mt-1 text-xs text-gray-500">
                    公司編號/客戶GP號為可選項，留空則由系統自動獲取
                  </p>
                </div>
                <div class="md:col-span-2">
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
                  <select
                    v-model="uvPrintConfigForm.compatibilityStatus"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">请选择兼容性状态</option>
                    <option value="V">适用</option>
                    <option value="X">不适用</option>
                    <option value="NA">不确定</option>
                    <option value="▷">需要打底处理</option>
                  </select>
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
                  class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
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
              <h3 class="text-lg font-medium text-gray-900">批量添加UV印刷配置</h3>
              <button @click="closeBatchAddDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">公司編號/客戶GP號 *</label>
                <select
                  v-model="batchAddForm.companyNumber"
                  @change="onBatchAddCompanyNumberChange"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">請先選擇公司編號或客戶GP號</option>
                  <option v-for="companyNumber in allAvailableCompanyNumbers" :key="companyNumber" :value="companyNumber">
                    {{ companyNumber }}
                  </option>
                </select>
                <p class="mt-1 text-xs text-gray-500">請先選擇公司編號或客戶GP號，然後選擇燙金紙性能類型</p>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙性能類型 *</label>
                <select
                  v-model="batchAddForm.paperType"
                  @change="onBatchAddPaperTypeChange"
                  :disabled="!batchAddForm.companyNumber || loadingAvailablePaperTypes"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                >
                  <option value="">
                    {{ loadingAvailablePaperTypes ? '加載中...' : !batchAddForm.companyNumber ? '請先選擇公司編號' : '請選擇燙金紙性能類型' }}
                  </option>
                  <option v-for="pt in availablePaperTypesForBatchAdd" :key="pt" :value="pt">{{ pt }}</option>
                </select>
                <p class="mt-1 text-xs text-gray-500">選擇公司編號後，系統將顯示該公司支持的燙金紙性能類型</p>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">選擇產品系列（可多選）*</label>
                <p v-if="!batchAddForm.companyNumber || !batchAddForm.paperType" class="mb-2 text-sm text-yellow-600">
                  {{ !batchAddForm.companyNumber ? '請先選擇公司編號或客戶GP號' : '請先選擇燙金紙性能類型' }}
                </p>
                <!-- 搜索输入框 -->
                <div class="mb-2">
                  <input
                    type="text"
                    v-model="batchAddSeriesSearchText"
                    placeholder="搜索產品系列..."
                    :disabled="!batchAddForm.companyNumber || !batchAddForm.paperType || loadingBatchAddSeries"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  />
                </div>
                <div 
                  class="border border-gray-300 rounded-md p-3 max-h-60 overflow-y-auto"
                  :class="{ 'bg-gray-50': !batchAddForm.companyNumber || !batchAddForm.paperType }"
                >
                  <div v-if="loadingBatchAddSeries" class="px-2 py-4 text-sm text-gray-500 text-center">
                    加載中...
                  </div>
                  <div v-else class="space-y-2">
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
                        :disabled="!batchAddForm.companyNumber || !batchAddForm.paperType"
                        class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded disabled:opacity-50"
                      />
                      <span class="text-sm text-gray-700">{{ series }}</span>
                    </label>
                    <div v-if="filteredBatchAddSeries.length === 0" class="px-2 py-4 text-sm text-gray-500 text-center">
                      {{ !batchAddForm.companyNumber || !batchAddForm.paperType ? '請先選擇公司編號和燙金紙性能類型' : '未找到匹配的產品系列' }}
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
                        @change="(e) => {
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
                @click="batchAddMappings"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
              >
                批量添加
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 批量修改状态对话框 -->
      <div v-if="showBatchEditStatusDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-md shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">批量修改兼容性狀態</h3>
              <button @click="closeBatchEditStatusDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <div class="mb-4">
              <p class="text-sm text-gray-600">已選中 {{ selectedItems.length }} 條記錄</p>
            </div>

            <div class="mb-4">
              <label class="block text-sm font-medium text-gray-700 mb-2">兼容性狀態 *</label>
              <select
                v-model="batchStatusForm.compatibilityStatus"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
              >
                <option value="V">适用</option>
                <option value="X">不适用</option>
                <option value="NA">不确定</option>
                <option value="▷">需要打底处理</option>
              </select>
            </div>

            <div class="flex justify-end space-x-3">
              <button
                @click="closeBatchEditStatusDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
              <button
                @click="batchUpdateStatus"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
              >
                確認修改
              </button>
            </div>
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
                <div v-if="importResult.errorMessages && importResult.errorMessages.length > 0" class="mt-2">
                  <p class="text-sm font-medium text-red-800">錯誤詳情:</p>
                  <ul class="list-disc list-inside text-sm text-red-700 mt-1">
                    <li v-for="(error, index) in importResult.errorMessages" :key="index">{{ error }}</li>
                  </ul>
                </div>
              </div>

              <div class="flex justify-end space-x-3 pt-4">
                <button
                  @click="closeImportDialog"
                  class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  取消
                </button>
                <button
                  @click="importMappings"
                  :disabled="!selectedFile"
                  class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                >
                  導入
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import axios from 'axios'
import { postProcessingPrintUvApi, type PostProcessingPrintUv, type CreatePostProcessingPrintUv } from '../../../api/modules/postProcessingPrintUv'
import { sharedProductApi } from '../../../api/modules/postProcessingLeduvglitter'

const API_BASE_URL = '/api'

// 响应式数据
const uvPrintConfigs = ref<PostProcessingPrintUv[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const selectedItems = ref<number[]>([])
const showBatchAddDialog = ref(false)
const showBatchEditStatusDialog = ref(false)
const showImportDialog = ref(false)
const selectedFile = ref<File | null>(null)
const importFileInput = ref<HTMLInputElement | null>(null)
const importResult = ref<any>(null)

// 烫金纸性能类型联动选择相关数据
const availableProducts = ref<string[]>([])
const filteredPaperTypes = ref<string[]>([])
const loadingPaperTypes = ref(false)

// 产品型号联动数据
const availableProductModels = ref<string[]>([])
const loadingProductModels = ref(false)

// 公司编号选项数据
const availableCompanyNumbers = ref<string[]>([])
// 表单中使用的公司编号列表（根据产品系列或型号动态加载）
const availableCompanyNumbersForForm = ref<string[]>([])
const loadingCompanyNumbers = ref(false)

// 自动完成提示相关数据
const showProductNameSuggestions = ref(false)
const showProductModelNumberSuggestions = ref(false)
const showCompanyNumberSuggestions = ref(false)
const allProductNames = ref<string[]>([])
const allProductModelNumbers = ref<string[]>([])
const allCompanyNumbers = ref<string[]>([])

// 分页相关数据
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)
const totalPages = ref(0)

// 搜索表单
const searchForm = reactive({
  productName: '',
  productModelNumber: '',
  companyNumber: '',
  paperType: ''
})

// UV印刷配置表单
const uvPrintConfigForm = reactive({
  id: null as number | null,
  productName: '',
  productModelNumber: '',
  companyNumber: '',
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

// 加载UV印刷配置列表
const loadUvPrintConfigs = async () => {
  try {
    loading.value = true
    const response = await postProcessingPrintUvApi.getUvPrintConfigsWithPagination(currentPage.value, pageSize.value)
    uvPrintConfigs.value = response.content
    totalItems.value = response.totalElements
    totalPages.value = response.totalPages
  } catch (error) {
    console.error('加载UV印刷配置失败:', error)
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

// 加载可用的公司编号
const loadAvailableCompanyNumbers = async () => {
  try {
    availableCompanyNumbers.value = await postProcessingPrintUvApi.getAllCompanyNumbers()
  } catch (error) {
    console.error('加载公司编号失败:', error)
  }
}

// 加载公司编号列表（根据产品系列或型号）
const loadCompanyNumbersForForm = async () => {
  if (!uvPrintConfigForm.productName) {
    availableCompanyNumbersForForm.value = []
    uvPrintConfigForm.companyNumber = ''
    return
  }
  
  loadingCompanyNumbers.value = true
  try {
    if (uvPrintConfigForm.productModelNumber) {
      // 如果选择了产品型号，加载该型号对应的公司编号
      availableCompanyNumbersForForm.value = await postProcessingPrintUvApi.getCompanyNumbersByProductModel(
        uvPrintConfigForm.productModelNumber,
        uvPrintConfigForm.productName
      )
    } else {
      // 如果只选择了产品系列，加载该系列对应的所有公司编号
      availableCompanyNumbersForForm.value = await postProcessingPrintUvApi.getCompanyNumbersByProductName(
        uvPrintConfigForm.productName
      )
    }
    
    // 如果当前选择的公司编号不在新列表中，清空选择
    if (uvPrintConfigForm.companyNumber && !availableCompanyNumbersForForm.value.includes(uvPrintConfigForm.companyNumber)) {
      uvPrintConfigForm.companyNumber = ''
    }
  } catch (error) {
    console.error('加载公司编号失败:', error)
    availableCompanyNumbersForForm.value = []
  } finally {
    loadingCompanyNumbers.value = false
  }
}

// 当产品名称改变时，加载对应的烫金纸性能类型和产品型号
const onProductNameChange = async () => {
  if (uvPrintConfigForm.productName) {
    // 清空之前的选择
    uvPrintConfigForm.paperType = ''
    uvPrintConfigForm.productModelNumber = ''
    uvPrintConfigForm.companyNumber = ''
    
    // 加载烫金纸性能类型
    loadingPaperTypes.value = true
    try {
      filteredPaperTypes.value = await sharedProductApi.getPaperTypesByProductName(uvPrintConfigForm.productName)
    } catch (error) {
      console.error('加载烫金纸性能类型失败:', error)
      filteredPaperTypes.value = []
    } finally {
      loadingPaperTypes.value = false
    }
    
    // 加载产品型号
    loadingProductModels.value = true
    try {
      availableProductModels.value = await sharedProductApi.getProductModelNumbersByProductName(uvPrintConfigForm.productName)
    } catch (error) {
      console.error('加载产品型号失败:', error)
      availableProductModels.value = []
    } finally {
      loadingProductModels.value = false
    }
    
    // 加载公司编号（基于产品系列）
    await loadCompanyNumbersForForm()
  } else {
    // 清空所有相关数据
    filteredPaperTypes.value = []
    availableProductModels.value = []
    availableCompanyNumbersForForm.value = []
    uvPrintConfigForm.paperType = ''
    uvPrintConfigForm.productModelNumber = ''
    uvPrintConfigForm.companyNumber = ''
  }
}

// 当产品型号改变时，重新加载公司编号
const onProductModelNumberChange = async () => {
  uvPrintConfigForm.companyNumber = '' // 清空之前的选择
  await loadCompanyNumbersForForm()
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

// 产品系列输入处理
const onProductNameInput = () => {
  showProductNameSuggestions.value = true
}

// 产品系列提示过滤
const filteredProductNameSuggestions = computed(() => {
  if (!searchForm.productName || searchForm.productName.trim() === '') {
    return allProductNames.value.slice(0, 10) // 显示前10个
  }
  const searchText = searchForm.productName.trim().toLowerCase()
  return allProductNames.value
    .filter(name => name.toLowerCase().includes(searchText))
    .slice(0, 10) // 最多显示10个
})

// 选择产品系列
const selectProductName = (name: string) => {
  searchForm.productName = name
  showProductNameSuggestions.value = false
  onSearchProductNameChange()
}

// 处理产品系列输入框失焦
const handleProductNameBlur = () => {
  setTimeout(() => {
    showProductNameSuggestions.value = false
  }, 200)
}

// 产品型号输入处理
const onProductModelNumberInput = () => {
  showProductModelNumberSuggestions.value = true
}

// 产品型号提示过滤
const filteredProductModelNumberSuggestions = computed(() => {
  if (!searchForm.productModelNumber || searchForm.productModelNumber.trim() === '') {
    return allProductModelNumbers.value.slice(0, 10) // 显示前10个
  }
  const searchText = searchForm.productModelNumber.trim().toLowerCase()
  return allProductModelNumbers.value
    .filter(model => model && model.toLowerCase().includes(searchText))
    .slice(0, 10) // 最多显示10个
})

// 选择产品型号
const selectProductModelNumber = (model: string) => {
  searchForm.productModelNumber = model
  showProductModelNumberSuggestions.value = false
}

// 处理产品型号输入框失焦
const handleProductModelNumberBlur = () => {
  setTimeout(() => {
    showProductModelNumberSuggestions.value = false
  }, 200)
}

// 公司编号输入处理
const onCompanyNumberInput = () => {
  showCompanyNumberSuggestions.value = true
}

// 公司编号提示过滤
const filteredCompanyNumberSuggestions = computed(() => {
  if (!searchForm.companyNumber || searchForm.companyNumber.trim() === '') {
    return allCompanyNumbers.value.slice(0, 10) // 显示前10个
  }
  const searchText = searchForm.companyNumber.trim().toLowerCase()
  return allCompanyNumbers.value
    .filter(number => number && number.toLowerCase().includes(searchText))
    .slice(0, 10) // 最多显示10个
})

// 选择公司编号
const selectCompanyNumber = (number: string) => {
  searchForm.companyNumber = number
  showCompanyNumberSuggestions.value = false
}

// 处理公司编号输入框失焦
const handleCompanyNumberBlur = () => {
  setTimeout(() => {
    showCompanyNumberSuggestions.value = false
  }, 200)
}

// 加载所有产品系列（用于提示）
const loadAllProductNames = async () => {
  try {
    allProductNames.value = await sharedProductApi.getAllProductNames()
  } catch (error) {
    console.error('加载产品系列失败:', error)
    allProductNames.value = []
  }
}

// 加载所有产品型号（用于提示）
const loadAllProductModelNumbers = async () => {
  try {
    allProductModelNumbers.value = await postProcessingPrintUvApi.getAllProductModelNumbers()
  } catch (error) {
    console.error('加载产品型号失败:', error)
    allProductModelNumbers.value = []
  }
}

// 加载所有公司编号（用于提示）
const loadAllCompanyNumbersForSuggestions = async () => {
  try {
    allCompanyNumbers.value = await postProcessingPrintUvApi.getAllCompanyNumbers()
  } catch (error) {
    console.error('加载公司编号失败:', error)
    allCompanyNumbers.value = []
  }
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

// 搜索UV印刷配置（支持多条件模糊筛选）
const searchUvPrintConfigs = async () => {
  try {
    loading.value = true
    console.log('搜索参数:', searchForm)
    
    // 检查是否有任何搜索条件
    const hasSearchConditions = searchForm.productName || 
                               searchForm.productModelNumber || 
                               searchForm.companyNumber ||
                               searchForm.paperType
    
    if (!hasSearchConditions) {
      await loadUvPrintConfigs()
      return
    }
    
    // 获取所有数据然后在前端进行模糊筛选
    const allConfigs = await postProcessingPrintUvApi.getAllUvPrintConfigs()
    let filteredConfigs = allConfigs
    
    // 按产品系列模糊筛选
    if (searchForm.productName && searchForm.productName.trim() !== '') {
      const searchText = searchForm.productName.trim().toLowerCase()
      filteredConfigs = filteredConfigs.filter(config => 
        config.productName && config.productName.toLowerCase().includes(searchText)
      )
    }
    
    // 按产品型号模糊筛选
    if (searchForm.productModelNumber && searchForm.productModelNumber.trim() !== '') {
      const searchText = searchForm.productModelNumber.trim().toLowerCase()
      filteredConfigs = filteredConfigs.filter(config => 
        config.productModelNumber && config.productModelNumber.toLowerCase().includes(searchText)
      )
    }
    
    // 按公司编号模糊筛选
    if (searchForm.companyNumber && searchForm.companyNumber.trim() !== '') {
      const searchText = searchForm.companyNumber.trim().toLowerCase()
      filteredConfigs = filteredConfigs.filter(config => 
        config.companyNumber && config.companyNumber.toLowerCase().includes(searchText)
      )
    }
    
    // 按烫金纸性能类型模糊筛选
    if (searchForm.paperType && searchForm.paperType.trim() !== '') {
      const searchText = searchForm.paperType.trim().toLowerCase()
      filteredConfigs = filteredConfigs.filter(config => 
        config.paperType && config.paperType.toLowerCase().includes(searchText)
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
    uvPrintConfigs.value = filteredConfigs.slice(startIndex, endIndex)
    
    console.log('筛选结果:', filteredConfigs.length, '条记录，当前页:', currentPage.value, '显示:', uvPrintConfigs.value.length, '条')
    
  } catch (error) {
    console.error('搜索UV印刷配置失败:', error)
  } finally {
    loading.value = false
  }
}

// 编辑UV印刷配置
const editUvPrintConfig = async (config: PostProcessingPrintUv) => {
  // 先赋值表单数据
  Object.assign(uvPrintConfigForm, {
    id: config.id,
    productName: config.productName,
    productModelNumber: config.productModelNumber,
    compatibilityStatus: config.compatibilityStatus,
    paperType: config.paperType || ''
  })
  
  // 加载联动数据
  if (config.productName && config.productName.trim() !== '') {
    // 如果有产品名称，加载对应的联动数据
    loadingPaperTypes.value = true
    try {
      filteredPaperTypes.value = await sharedProductApi.getPaperTypesByProductName(config.productName)
    } catch (error) {
      console.error('编辑时加载烫金纸性能类型失败:', error)
      filteredPaperTypes.value = []
    } finally {
      loadingPaperTypes.value = false
    }
    
    loadingProductModels.value = true
    try {
      availableProductModels.value = await sharedProductApi.getProductModelNumbersByProductName(config.productName)
    } catch (error) {
      console.error('编辑时加载产品型号失败:', error)
      availableProductModels.value = []
    } finally {
      loadingProductModels.value = false
    }
    
    // 加载公司编号列表
    await loadCompanyNumbersForForm()
  } else {
    // 如果没有产品名称，尝试从产品型号推断产品名称
    if (config.productModelNumber && config.productModelNumber.trim() !== '') {
      // 尝试通过产品型号找到对应的产品名称
      try {
        const response = await axios.get(`${API_BASE_URL}/api/product-management/complete/search/model`, {
          params: { modelNumber: config.productModelNumber }
        })
        const productInfoList = response.data || []
        if (productInfoList.length > 0 && productInfoList[0].name) {
          const productName = productInfoList[0].name
          // 更新表单中的产品名称
          uvPrintConfigForm.productName = productName
          
          // 加载对应的联动数据
          loadingPaperTypes.value = true
          try {
            filteredPaperTypes.value = await sharedProductApi.getPaperTypesByProductName(productName)
          } catch (error) {
            console.error('通过产品型号加载烫金纸性能类型失败:', error)
            filteredPaperTypes.value = []
          } finally {
            loadingPaperTypes.value = false
          }
          
          loadingProductModels.value = true
          try {
            availableProductModels.value = await sharedProductApi.getProductModelNumbersByProductName(productName)
          } catch (error) {
            console.error('通过产品型号加载产品型号失败:', error)
            availableProductModels.value = []
          } finally {
            loadingProductModels.value = false
          }
          
          // 加载公司编号列表
          await loadCompanyNumbersForForm()
        } else {
          // 如果无法找到产品名称，加载所有选项
          await loadAllOptions()
        }
      } catch (error) {
        console.error('通过产品型号查找产品信息失败:', error)
        await loadAllOptions()
      }
    } else {
      // 如果产品型号也为空，加载所有选项
      await loadAllOptions()
    }
  }
  
  showEditDialog.value = true
}

// 加载所有选项的辅助函数
const loadAllOptions = async () => {
  loadingPaperTypes.value = true
  try {
    const allPaperTypes = await postProcessingPrintUvApi.getAllPaperTypes()
    filteredPaperTypes.value = allPaperTypes
  } catch (error) {
    console.error('编辑时加载所有烫金纸性能类型失败:', error)
    filteredPaperTypes.value = []
  } finally {
    loadingPaperTypes.value = false
  }
  
  loadingProductModels.value = true
  try {
    const allProductModels = await postProcessingPrintUvApi.getAllProductModelNumbers()
    availableProductModels.value = allProductModels
  } catch (error) {
    console.error('编辑时加载所有产品型号失败:', error)
    availableProductModels.value = []
  } finally {
    loadingProductModels.value = false
  }
}

// 删除UV印刷配置
const deleteUvPrintConfig = async (id: number) => {
  if (confirm('确定要删除这个UV印刷配置吗？')) {
    try {
      await postProcessingPrintUvApi.deleteUvPrintConfig(id)
      await loadDataWithCurrentFilter()
    } catch (error) {
      console.error('删除UV印刷配置失败:', error)
    }
  }
}

// 保存UV印刷配置
const saveUvPrintConfig = async () => {
  try {
    // 验证公司编号是否属于所选的产品系列或型号
    if (uvPrintConfigForm.companyNumber) {
      const validationResult = await postProcessingPrintUvApi.validateCompanyNumber(
        uvPrintConfigForm.companyNumber,
        uvPrintConfigForm.productName,
        uvPrintConfigForm.productModelNumber || undefined
      )
      
      if (!validationResult.valid) {
        alert(validationResult.message || '公司編號驗證失敗')
        return
      }
    }
    
    if (showAddDialog.value) {
      const createData: CreatePostProcessingPrintUv = {
        productName: uvPrintConfigForm.productName,
        productModelNumber: uvPrintConfigForm.productModelNumber,
        companyNumber: uvPrintConfigForm.companyNumber || '', // 允许为空
        compatibilityStatus: uvPrintConfigForm.compatibilityStatus,
        paperType: uvPrintConfigForm.paperType
      }
      await postProcessingPrintUvApi.createUvPrintConfig(createData)
    } else {
      const updateData = {
        ...uvPrintConfigForm,
        companyNumber: uvPrintConfigForm.companyNumber || '' // 允许为空
      }
      await postProcessingPrintUvApi.updateUvPrintConfig(uvPrintConfigForm.id!, updateData)
    }
    
    await loadDataWithCurrentFilter()
    closeDialog()
  } catch (error) {
    console.error('保存UV印刷配置失败:', error)
    alert('保存失敗，請檢查輸入的數據')
  }
}

// 关闭对话框
const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(uvPrintConfigForm, {
    id: null,
    productName: '',
    productModelNumber: '',
    companyNumber: '',
    compatibilityStatus: 'NA',
    paperType: ''
  })
  // 清空联动选择数据
  availableCompanyNumbersForForm.value = []
  availableProductModels.value = []
  filteredPaperTypes.value = []
}

// 重置搜索
const resetSearch = async () => {
  // 清空所有搜索条件
  searchForm.productName = ''
  searchForm.productModelNumber = ''
  searchForm.companyNumber = ''
  searchForm.paperType = ''
  
  // 隐藏所有提示框
  showProductNameSuggestions.value = false
  showProductModelNumberSuggestions.value = false
  showCompanyNumberSuggestions.value = false
  
  // 重置分页到第一页
  currentPage.value = 1
  
  // 重新加载所有数据
  await loadUvPrintConfigs()
}

// 根据当前筛选条件加载数据
const loadDataWithCurrentFilter = async () => {
  // 检查是否有筛选条件
  const hasSearchConditions = searchForm.productName || 
                             searchForm.productModelNumber || 
                             searchForm.companyNumber ||
                             searchForm.paperType
  
  if (hasSearchConditions) {
    // 如果有筛选条件，重新执行搜索
    await searchUvPrintConfigs()
  } else {
    // 如果没有筛选条件，正常加载数据
    await loadUvPrintConfigs()
  }
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

// 导出数据
const exportData = async () => {
  try {
    // 调用导出API
    const response = await fetch('/api/api/post-processing-print-uv/export', {
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
    let fileName = 'UV印刷配置.xlsx'
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

// 选择相关方法
const toggleSelectAll = () => {
  if (selectedItems.value.length === uvPrintConfigs.value.length) {
    selectedItems.value = []
  } else {
    selectedItems.value = uvPrintConfigs.value.map(c => c.id!).filter(id => id != null)
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

const clearSelection = () => {
  selectedItems.value = []
}

// 复制配置
const copyUvPrintConfig = (config: PostProcessingPrintUv) => {
  Object.assign(uvPrintConfigForm, {
    id: null,
    productName: config.productName,
    productModelNumber: config.productModelNumber,
    compatibilityStatus: config.compatibilityStatus,
    paperType: config.paperType || ''
  })
  showAddDialog.value = true
  if (config.productName) {
    onProductNameChange()
  }
}

// 批量添加相关数据
const batchAddForm = reactive({
  paperType: '',
  selectedSeries: [] as string[],
  selectedProducts: {} as Record<string, string[]>,
  companyNumber: '',
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷'
})

// 批量添加中使用的数据
const allAvailableCompanyNumbers = ref<string[]>([]) // 所有可用的公司编号
const availablePaperTypesForBatchAdd = ref<string[]>([]) // 根据公司编号筛选的可用性能类型
const loadingAvailablePaperTypes = ref(false) // 加载性能类型的状态
const batchAddAvailableSeries = ref<string[]>([]) // 根据公司编号和性能类型筛选的产品系列
const loadingBatchAddSeries = ref(false) // 加载产品系列的状态
const batchAddSeriesSearchText = ref('')
const batchAddSeriesProducts = ref<Record<string, any[]>>({})

// 批量修改状态表单
const batchStatusForm = reactive({
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷'
})

// 批量添加对话框
const openBatchAddDialog = async () => {
  showBatchAddDialog.value = true
  batchAddForm.paperType = ''
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddForm.companyNumber = ''
  batchAddForm.compatibilityStatus = 'NA'
  batchAddSeriesProducts.value = {}
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
  availablePaperTypesForBatchAdd.value = []
  
  // 加载所有可用的公司编号
  await loadAllCompanyNumbers()
}

const closeBatchAddDialog = () => {
  showBatchAddDialog.value = false
  Object.assign(batchAddForm, {
    paperType: '',
    selectedSeries: [],
    selectedProducts: {},
    companyNumber: '',
    compatibilityStatus: 'NA'
  })
  availablePaperTypesForBatchAdd.value = []
  batchAddSeriesProducts.value = {}
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
}

// 加载所有可用的公司编号
const loadAllCompanyNumbers = async () => {
  try {
    allAvailableCompanyNumbers.value = await postProcessingPrintUvApi.getAllCompanyNumbers()
  } catch (error) {
    console.error('加载公司编号失败:', error)
    allAvailableCompanyNumbers.value = []
  }
}

// 根据公司编号加载可用的烫金纸性能类型
const loadPaperTypesByCompanyNumber = async (companyNumber: string) => {
  if (!companyNumber) {
    availablePaperTypesForBatchAdd.value = []
    return
  }
  
  loadingAvailablePaperTypes.value = true
  try {
    // 根据公司编号获取所有产品，然后提取性能类型
    const response = await fetch(`/api/api/product-management/complete/search/company-number?companyNumber=${encodeURIComponent(companyNumber)}`)
    if (response.ok) {
      const products = await response.json()
      const paperTypes = new Set<string>()
      products.forEach((p: any) => {
        if (p.hotStampingPaperType) {
          paperTypes.add(p.hotStampingPaperType)
        }
      })
      availablePaperTypesForBatchAdd.value = Array.from(paperTypes).sort()
    } else {
      availablePaperTypesForBatchAdd.value = []
    }
  } catch (error) {
    console.error('加载烫金纸性能类型失败:', error)
    availablePaperTypesForBatchAdd.value = []
  } finally {
    loadingAvailablePaperTypes.value = false
  }
}

// 批量添加时选择公司编号
const onBatchAddCompanyNumberChange = async () => {
  batchAddForm.paperType = ''
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddSeriesProducts.value = {}
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
  availablePaperTypesForBatchAdd.value = []
  
  if (batchAddForm.companyNumber) {
    await loadPaperTypesByCompanyNumber(batchAddForm.companyNumber)
  }
}

// 批量添加时选择性能类型
const onBatchAddPaperTypeChange = async () => {
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddSeriesProducts.value = {}
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
  
  if (batchAddForm.companyNumber && batchAddForm.paperType) {
    // 根据公司编号和性能类型加载产品系列
    await loadSeriesByCompanyAndPaperType()
  }
}

// 根据公司编号和性能类型加载产品系列
const loadSeriesByCompanyAndPaperType = async () => {
  if (!batchAddForm.companyNumber || !batchAddForm.paperType) {
    batchAddAvailableSeries.value = []
    return
  }
  
  loadingBatchAddSeries.value = true
  try {
    const response = await fetch(`/api/api/products/series-by-company-and-paper-type?companyNumber=${encodeURIComponent(batchAddForm.companyNumber)}&paperType=${encodeURIComponent(batchAddForm.paperType)}`)
    if (response.ok) {
      batchAddAvailableSeries.value = await response.json()
    } else {
      batchAddAvailableSeries.value = []
    }
  } catch (error) {
    console.error('加载产品系列失败:', error)
    batchAddAvailableSeries.value = []
  } finally {
    loadingBatchAddSeries.value = false
  }
}

// 批量添加对话框中产品系列变化处理
const onBatchAddSeriesChange = async (seriesName: string) => {
  const isSelected = batchAddForm.selectedSeries.includes(seriesName)
  
  if (isSelected) {
    // 选择了产品系列，根据公司编号、性能类型和产品系列加载产品列表
    if (!batchAddSeriesProducts.value[seriesName]) {
      // 确保公司编号和性能类型都已选择
      if (!batchAddForm.companyNumber || !batchAddForm.paperType) {
        console.warn('公司编号或性能类型未选择，无法加载产品型号')
        batchAddSeriesProducts.value[seriesName] = []
        return
      }
      
      try {
        const response = await fetch(
          `/api/api/products/products-by-company-papertype-series?companyNumber=${encodeURIComponent(batchAddForm.companyNumber)}&paperType=${encodeURIComponent(batchAddForm.paperType)}&seriesName=${encodeURIComponent(seriesName)}`
        )
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
}

const filteredBatchAddSeries = computed(() => {
  if (!batchAddSeriesSearchText.value) {
    return batchAddAvailableSeries.value
  }
  const search = batchAddSeriesSearchText.value.toLowerCase()
  return batchAddAvailableSeries.value.filter(series => series.toLowerCase().includes(search))
})

// 批量添加映射
const batchAddMappings = async () => {
  if (!batchAddForm.companyNumber) {
    alert('請先選擇公司編號')
    return
  }
  
  if (!batchAddForm.paperType) {
    alert('請先選擇燙金紙性能類型')
    return
  }
  
  if (batchAddForm.selectedSeries.length === 0) {
    alert('請至少選擇一個產品系列')
    return
  }
  
  try {
    let successCount = 0
    let failCount = 0
    let skipCount = 0
    const errors: string[] = []
    const skipped: string[] = []
    
    // 为每个选中的产品系列创建映射
    for (const seriesName of batchAddForm.selectedSeries) {
      const selectedProductModels = batchAddForm.selectedProducts[seriesName] || []
      
      if (selectedProductModels.length === 0) {
        // 如果没有选择任何产品型号，创建系列级别的配置
        // 验证公司编号是否属于该产品系列
        const validationResult = await postProcessingPrintUvApi.validateCompanyNumber(
          batchAddForm.companyNumber,
          seriesName,
          undefined
        )
        
        if (!validationResult.valid) {
          errors.push(`${seriesName}: ${validationResult.message || '公司編號驗證失敗'}`)
          failCount++
          continue
        }
        
        const mapping = {
          productName: seriesName,
          productModelNumber: null,
          companyNumber: batchAddForm.companyNumber,
          compatibilityStatus: batchAddForm.compatibilityStatus,
          paperType: batchAddForm.paperType || null
        }
        
        // 处理单个映射
        await processMapping(mapping, seriesName, { successCount, failCount, skipCount }, errors, skipped)
      } else {
        // 为每个选中的产品型号创建映射
        for (const modelNumber of selectedProductModels) {
          // 验证公司编号是否属于该产品型号
          const validationResult = await postProcessingPrintUvApi.validateCompanyNumber(
            batchAddForm.companyNumber,
            seriesName,
            modelNumber
          )
          
          if (!validationResult.valid) {
            errors.push(`${seriesName} - ${modelNumber}: ${validationResult.message || '公司編號驗證失敗'}`)
            failCount++
            continue
          }
          
          const mapping = {
            productName: seriesName,
            productModelNumber: modelNumber,
            companyNumber: batchAddForm.companyNumber,
            compatibilityStatus: batchAddForm.compatibilityStatus,
            paperType: batchAddForm.paperType || null
          }
          
          // 处理单个映射
          await processMapping(mapping, `${seriesName} - ${modelNumber}`, { successCount, failCount, skipCount }, errors, skipped)
        }
      }
    }
    
    // 显示结果
    if (failCount === 0 && skipCount === 0) {
      alert(`批量添加完成！成功添加 ${successCount} 條映射配置`)
    } else {
      let message = `批量添加完成！\n成功: ${successCount} 條`
      if (skipCount > 0) {
        message += `\n跳過: ${skipCount} 條 (已存在且未更新)`
        if (skipped.length > 0) {
          message += `\n跳過的配置: ${skipped.join(', ')}`
        }
      }
      if (failCount > 0) {
        message += `\n失敗: ${failCount} 條`
        if (errors.length > 0) {
          message += `\n\n失敗詳情:\n${errors.join('\n')}`
        }
      }
      alert(message)
    }
    
    if (successCount > 0) {
      await loadDataWithCurrentFilter()
      closeBatchAddDialog()
    }
  } catch (error) {
    console.error('批量添加失败:', error)
    alert('批量添加失败，请重试')
  }
}

// 处理单个映射的创建或更新
const processMapping = async (
  mapping: any,
  displayName: string,
  counters: { successCount: number, failCount: number, skipCount: number },
  errors: string[],
  skipped: string[]
) => {
  try {
    // 检查是否已存在（根据唯一键）
    // 如果提供了 companyNumber，使用它；否则让后端根据产品信息自动获取
    const checkUrl = `/api/api/post-processing-print-uv/check-unique?` +
      `productName=${encodeURIComponent(mapping.productName)}&` +
      `productModelNumber=${encodeURIComponent(mapping.productModelNumber || '')}&` +
      (mapping.companyNumber ? `companyNumber=${encodeURIComponent(mapping.companyNumber)}&` : '') +
      (mapping.paperType ? `paperType=${encodeURIComponent(mapping.paperType)}` : '')
    
    const checkResponse = await fetch(checkUrl)
    
    if (checkResponse.ok) {
      // 已存在，询问是否更新
      const existing = await checkResponse.json()
      if (existing && existing.id) {
        const shouldUpdate = confirm(
          `配置已存在：\n` +
          `產品系列: ${mapping.productName}\n` +
          `產品型號: ${mapping.productModelNumber || '(系列級別)'}\n` +
          `公司編號: ${existing.companyNumber || '(自動獲取)'}\n` +
          `性能類型: ${mapping.paperType || '(空)'}\n` +
          `當前狀態: ${getCompatibilityStatusText(existing.compatibilityStatus)}\n\n` +
          `是否更新為: ${getCompatibilityStatusText(mapping.compatibilityStatus)}?`
        )
        
        if (shouldUpdate) {
          // 更新现有记录（不传递 companyNumber，让后端自动获取）
          const updateResponse = await fetch(`/api/api/post-processing-print-uv/${existing.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(mapping)
          })
          
          if (updateResponse.ok) {
            counters.successCount++
          } else {
            counters.failCount++
            const errorData = await updateResponse.json().catch(() => ({}))
            errors.push(`${displayName}: ${errorData.message || '更新失敗'}`)
          }
        } else {
          counters.skipCount++
          skipped.push(displayName)
        }
      } else {
        // 不存在，创建新记录（不传递 companyNumber，让后端自动获取）
        const createResponse = await fetch('/api/api/post-processing-print-uv', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(mapping)
        })
        
        if (createResponse.ok) {
          counters.successCount++
        } else {
          counters.failCount++
          const errorData = await createResponse.json().catch(() => ({}))
          errors.push(`${displayName}: ${errorData.message || '創建失敗'}`)
        }
      }
    } else {
      // 不存在，创建新记录（不传递 companyNumber，让后端自动获取）
      const createResponse = await fetch('/api/api/post-processing-print-uv', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(mapping)
      })
      
      if (createResponse.ok) {
        counters.successCount++
      } else {
        counters.failCount++
        const errorData = await createResponse.json().catch(() => ({}))
        errors.push(`${displayName}: ${errorData.message || '創建失敗'}`)
      }
    }
  } catch (error) {
    counters.failCount++
    errors.push(`${displayName}: ${(error as Error).message}`)
  }
}

// 批量修改状态相关方法
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

const batchUpdateStatus = async () => {
  if (selectedItems.value.length === 0) {
    alert('請先選擇要修改的記錄')
    return
  }
  if (!batchStatusForm.compatibilityStatus) {
    alert('請選擇兼容性狀態')
    return
  }

  try {
    const response = await axios.put('/api/api/post-processing-print-uv/batch/status', {
      ids: selectedItems.value,
      compatibilityStatus: batchStatusForm.compatibilityStatus
    })
    
    if (response.data.success) {
      alert(response.data.message)
      await loadDataWithCurrentFilter()
      clearSelection()
      closeBatchEditStatusDialog()
    } else {
      alert(response.data.message || '批量修改失败')
    }
  } catch (error: any) {
    console.error('批量修改状态失败:', error)
    alert(error.response?.data?.message || '批量修改失败，请重试')
  }
}

// 导入相关方法
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
  }
}

const downloadImportTemplate = async () => {
  try {
    const response = await fetch('/api/api/post-processing-print-uv/import-template', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    if (!response.ok) {
      throw new Error('下载模板失败')
    }
    
    const contentDisposition = response.headers.get('Content-Disposition')
    let fileName = 'UV印刷配置導入模板.xlsx'
    if (contentDisposition) {
      const fileNameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
      if (fileNameMatch && fileNameMatch[1]) {
        fileName = decodeURIComponent(fileNameMatch[1].replace(/['"]/g, ''))
      }
    }
    
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = fileName
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (error) {
    console.error('下载模板失败:', error)
    alert('下载模板失败，请重试')
  }
}

const importMappings = async () => {
  if (!selectedFile.value) {
    alert('請選擇要導入的文件')
    return
  }

  const formData = new FormData()
  formData.append('file', selectedFile.value)

  try {
    const response = await axios.post('/api/api/post-processing-print-uv/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })

    importResult.value = response.data
    if (response.data.success) {
      await loadDataWithCurrentFilter()
      currentPage.value = 1
    }
  } catch (error: any) {
    console.error('导入失败:', error)
    importResult.value = {
      success: false,
      message: error.response?.data?.message || '导入失败，请重试',
      totalCount: 0,
      successCount: 0,
      failCount: 0,
      errorMessages: []
    }
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadUvPrintConfigs()
  loadAvailableProducts()
  loadAvailableCompanyNumbers()
  // 加载提示数据
  loadAllProductNames()
  loadAllProductModelNumbers()
  loadAllCompanyNumbersForSuggestions()
})
</script>
