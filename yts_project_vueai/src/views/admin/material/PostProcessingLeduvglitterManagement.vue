<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">絲印LED UV灑閃粉配置管理</h1>
            <p class="mt-2 text-gray-600">管理絲印LED UV灑閃粉後加工的配置信息</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="showAddDialog = true"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-purple-600 hover:bg-purple-700"
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
        <div class="grid grid-cols-1 md:grid-cols-6 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">产品系列</label>
            <input
              v-model="searchForm.productName"
              @blur="onSearchProductNameChange"
              type="text"
              placeholder="输入产品系列进行筛选"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
            <select
              v-model="searchForm.paperType"
              :disabled="!searchForm.productName"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
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
            <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
            <select
              v-model="searchForm.compatibilityStatus"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
            >
              <option value="">全部状态</option>
              <option value="V">适用</option>
              <option value="X">不适用</option>
              <option value="NA">不确定</option>
              <option value="▷">需要打底处理</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
            <input
              v-model="searchForm.productModelNumber"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
              placeholder="搜索产品型号"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">布面纸类型</label>
            <select
              v-model.number="searchForm.clothPaperTypeId"
              :disabled="loadingClothPaperTypes"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
            >
              <option value="0">
                {{ loadingClothPaperTypes ? '加载中...' : '全部布面纸类型' }}
              </option>
              <option v-for="option in clothPaperTypeOptions" :key="option.id" :value="option.id">
                {{ option.label }}
              </option>
            </select>
          </div>
          <div class="flex items-end">
            <button
              @click="searchLeduvglitterConfigs"
              class="w-full bg-purple-600 text-white px-4 py-2 rounded-md hover:bg-purple-700 transition-colors"
            >
              搜索
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
          </div>
        </div>
      </div>

      <!-- LED UV灑閃粉配置列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">LED UV灑閃粉配置列表</h3>
        </div>
        
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  <input
                    type="checkbox"
                    :checked="selectedItems.length === paginatedData.length && paginatedData.length > 0"
                    @change="toggleSelectAll"
                    class="rounded border-gray-300 text-indigo-600 focus:ring-indigo-500"
                  />
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙系列</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品型号</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">布面纸类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="config in paginatedData" :key="config.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <input
                    type="checkbox"
                    :value="config.id"
                    v-model="selectedItems"
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
                  <div class="text-sm text-gray-900">{{ config.clothPaperTypeName || config.clothPaperTypeId }}</div>
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
                      @click="editLeduvglitterConfig(config)"
                      class="text-purple-600 hover:text-purple-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="copyLeduvglitterConfig(config)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      复制
                    </button>
                    <button
                      @click="config.id && deleteLeduvglitterConfig(config.id)"
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
        <div v-if="totalItems > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
          <div class="flex-1 flex justify-between sm:hidden">
            <button
              @click="previousPage"
              :disabled="currentPage === 1"
              class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              上一页
            </button>
            <button
              @click="nextPage"
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
              <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                <button
                  @click="previousPage"
                  :disabled="currentPage === 1"
                  class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  上一页
                </button>
                <button
                  v-for="page in visiblePages"
                  :key="page"
                  @click="typeof page === 'number' ? goToPage(page) : null"
                  :disabled="typeof page !== 'number'"
                  :class="[
                    'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                    typeof page === 'number' && page === currentPage
                      ? 'z-10 bg-purple-50 border-purple-500 text-purple-600'
                      : typeof page === 'number'
                      ? 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50 cursor-pointer'
                      : 'bg-white border-gray-300 text-gray-500 cursor-default'
                  ]"
                >
                  {{ page }}
                </button>
                <button
                  @click="nextPage"
                  :disabled="currentPage === totalPages"
                  class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  下一页
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
                {{ showAddDialog ? '添加LED UV灑閃粉配置' : '编辑LED UV灑閃粉配置' }}
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

            <form @submit.prevent="saveLeduvglitterConfig">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列 *</label>
                  <select
                    v-model="leduvglitterConfigForm.productName"
                    @change="onProductNameChange"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                  >
                    <option value="">请选择燙金紙系列</option>
                    <option v-for="product in availableProducts" :key="product" :value="product">
                      {{ product }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙性能類型</label>
                  <select
                    v-model="leduvglitterConfigForm.paperType"
                    :disabled="!leduvglitterConfigForm.productName || loadingPaperTypes"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingPaperTypes ? '加載中...' : !leduvglitterConfigForm.productName ? '請先選擇燙金紙系列' : '請選擇燙金紙性能類型' }}
                    </option>
                    <option v-for="paperType in filteredPaperTypes" :key="paperType" :value="paperType">
                      {{ paperType }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
                  <select
                    v-model="leduvglitterConfigForm.productModelNumber"
                    :disabled="!leduvglitterConfigForm.productName || loadingProductModels"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingProductModels ? '加載中...' : !leduvglitterConfigForm.productName ? '請先選擇燙金紙系列' : '請選擇產品型號' }}
                    </option>
                    <option v-for="model in availableProductModels" :key="model" :value="model">
                      {{ model }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">布面紙類型 *</label>
                  <select
                    v-model.number="leduvglitterConfigForm.clothPaperTypeId"
                    :disabled="loadingClothPaperTypes"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingClothPaperTypes ? '加載中...' : '請選擇布面紙類型' }}
                    </option>
                    <option v-for="option in clothPaperTypeOptions" :key="option.id" :value="option.id">
                      {{ option.label }}
                    </option>
                  </select>
                </div>
                <div class="md:col-span-2">
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
                  <select
                    v-model="leduvglitterConfigForm.compatibilityStatus"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
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
                  class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-purple-600 hover:bg-purple-700"
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
              <h3 class="text-lg font-medium text-gray-900">批量添加配置</h3>
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
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">請先選擇燙金紙性能類型</option>
                  <option v-for="pt in availablePaperTypes" :key="pt" :value="pt">{{ pt }}</option>
                </select>
                <p class="mt-1 text-xs text-gray-500">請先選擇燙金紙性能類型，系統將只顯示支持該類型的產品系列</p>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">選擇產品系列（可多選）*</label>
                <p v-if="!batchAddForm.paperType" class="mb-2 text-sm text-yellow-600">
                  請先選擇燙金紙性能類型
                </p>
                <!-- 搜索输入框 -->
                <div class="mb-2">
                  <input
                    type="text"
                    v-model="batchAddSeriesSearchText"
                    placeholder="搜索產品系列..."
                    :disabled="!batchAddForm.paperType"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  />
                </div>
                <div 
                  class="border border-gray-300 rounded-md p-3 max-h-60 overflow-y-auto"
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
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>

                <!-- 产品型号 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
                  <input
                    v-model="batchEditForm.productModelNumber"
                    type="text"
                    placeholder="留空则不修改"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>

                <!-- 布面纸类型 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">布面纸类型</label>
                  <select
                    v-model="batchEditForm.clothPaperTypeId"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  >
                    <option :value="null">不修改</option>
                    <option v-for="option in clothPaperTypeOptions" :key="option.id" :value="option.id">
                      {{ option.typeName }}
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
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                </div>

                <!-- 兼容性状态 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
                  <select
                    v-model="batchEditForm.compatibilityStatus"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
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
                  class="inline-flex items-center px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-colors duration-200"
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
                @click="importMappings"
                :disabled="!selectedFile"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                開始導入
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
import { postProcessingLeduvglitterApi, type PostProcessingLeduvglitter, type CreatePostProcessingLeduvglitter } from '../../../api/modules/postProcessingLeduvglitter'

// 响应式数据
const leduvglitterConfigs = ref<PostProcessingLeduvglitter[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)

// 批量操作相关
const selectedItems = ref<number[]>([])
const showBatchAddDialog = ref(false)
const showBatchEditStatusDialog = ref(false)
const showBatchEditDialog = ref(false)
const showImportDialog = ref(false)

// 批量添加表单
const batchAddForm = reactive({
  paperType: '',
  selectedSeries: [] as string[],
  selectedProducts: {} as Record<string, string[]>, // 每个产品系列对应的产品型号列表
  clothPaperTypeId: 0 as number,
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷'
})

// 批量添加时每个产品系列的产品列表
const batchAddSeriesProducts = ref<Record<string, Array<{ id: number, modelNumber: string, name: string }>>>({})
const batchAddAvailableSeries = ref<string[]>([])
const batchAddSeriesSearchText = ref('')

// 所有可用的性能类型
const availablePaperTypes = ref<string[]>([])

// 批量修改状态表单
const batchStatusForm = reactive({
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷'
})

// 导入相关
const selectedFile = ref<File | null>(null)
const importFileInput = ref<HTMLInputElement | null>(null)
const importResult = ref<{ success: boolean; message: string; totalCount?: number; successCount?: number; failCount?: number; errors?: string[] } | null>(null)

// 分页相关数据
const currentPage = ref(1)
const pageSize = ref(10)

// 烫金纸性能类型联动选择相关数据
const availableProducts = ref<string[]>([])
const filteredPaperTypes = ref<string[]>([])
const loadingPaperTypes = ref(false)

// 布面纸类型选项数据
const clothPaperTypeOptions = ref<Array<{id: number, label: string}>>([])
const loadingClothPaperTypes = ref(false)

// 产品型号联动数据
const availableProductModels = ref<string[]>([])
const loadingProductModels = ref(false)

// 搜索表单
const searchForm = reactive({
  productName: '',
  productModelNumber: '',
  clothPaperTypeId: 0,
  paperType: '',
  compatibilityStatus: ''
})

// LED UV灑閃粉配置表单
const leduvglitterConfigForm = reactive({
  id: null as number | null,
  productName: '',
  productModelNumber: '',
  clothPaperTypeId: 0 as number,
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷',
  paperType: ''
})

// 前端筛选计算属性
const filteredData = computed(() => {
  let data = leduvglitterConfigs.value

  // 燙金紙系列筛选
  if (searchForm.productName) {
    data = data.filter(item => 
      item.productName && item.productName.toLowerCase().includes(searchForm.productName.toLowerCase())
    )
  }

  // 产品型号筛选
  if (searchForm.productModelNumber) {
    data = data.filter(item => 
      item.productModelNumber && item.productModelNumber.toLowerCase().includes(searchForm.productModelNumber.toLowerCase())
    )
  }

  // 布面纸类型筛选
  if (searchForm.clothPaperTypeId && searchForm.clothPaperTypeId > 0) {
    data = data.filter(item => item.clothPaperTypeId === searchForm.clothPaperTypeId)
  }

  // 烫金纸性能类型筛选
  if (searchForm.paperType) {
    data = data.filter(item => 
      item.paperType && item.paperType.toLowerCase().includes(searchForm.paperType.toLowerCase())
    )
  }

  // 兼容性状态筛选
  if (searchForm.compatibilityStatus) {
    data = data.filter(item => item.compatibilityStatus === searchForm.compatibilityStatus)
  }

  return data
})

// 分页相关计算属性
const totalItems = computed(() => filteredData.value.length)
const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value))

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredData.value.slice(start, end)
})

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

// 加载LED UV灑閃粉配置列表（加载所有数据用于前端筛选）
const loadLeduvglitterConfigs = async () => {
  try {
    loading.value = true
    const response = await postProcessingLeduvglitterApi.getAllLeduvglitterConfigs()
    leduvglitterConfigs.value = response
  } catch (error) {
    console.error('加载LED UV灑閃粉配置失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索LED UV灑閃粉配置（前端筛选）
const searchLeduvglitterConfigs = () => {
  // 前端筛选逻辑已经通过计算属性实现，这里只需要重置到第一页
  currentPage.value = 1
  console.log('筛选条件:', searchForm)
  console.log('筛选结果:', filteredData.value)
}

// 分页导航方法（前端分页）
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

// 编辑LED UV灑閃粉配置
const editLeduvglitterConfig = (config: PostProcessingLeduvglitter) => {
  Object.assign(leduvglitterConfigForm, {
    id: config.id,
    productName: config.productName,
    productModelNumber: config.productModelNumber,
    clothPaperTypeId: config.clothPaperTypeId,
    compatibilityStatus: config.compatibilityStatus,
    paperType: config.paperType || ''
  })
  showEditDialog.value = true
  
  // 如果配置中有产品名称，加载对应的烫金纸性能类型
  if (config.productName) {
    onProductNameChange()
  }
}

// 删除LED UV灑閃粉配置
const deleteLeduvglitterConfig = async (id: number) => {
  if (confirm('确定要删除这个LED UV灑閃粉配置吗？')) {
    try {
      await postProcessingLeduvglitterApi.deleteLeduvglitterConfig(id)
      await loadLeduvglitterConfigs()
    } catch (error) {
      console.error('删除LED UV灑閃粉配置失败:', error)
    }
  }
}

// 保存LED UV灑閃粉配置
const saveLeduvglitterConfig = async () => {
  try {
    if (showAddDialog.value) {
      const createData: CreatePostProcessingLeduvglitter = {
        productName: leduvglitterConfigForm.productName,
        productModelNumber: leduvglitterConfigForm.productModelNumber,
        clothPaperTypeId: leduvglitterConfigForm.clothPaperTypeId,
        compatibilityStatus: leduvglitterConfigForm.compatibilityStatus,
        paperType: leduvglitterConfigForm.paperType
      }
      await postProcessingLeduvglitterApi.createLeduvglitterConfig(createData)
    } else {
      if (leduvglitterConfigForm.id) {
        await postProcessingLeduvglitterApi.updateLeduvglitterConfig(leduvglitterConfigForm.id, leduvglitterConfigForm)
      }
    }
    
    await loadLeduvglitterConfigs()
    closeDialog()
  } catch (error) {
    console.error('保存LED UV灑閃粉配置失败:', error)
  }
}

// 关闭对话框
const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(leduvglitterConfigForm, {
    id: null,
    productName: '',
    productModelNumber: '',
    clothPaperTypeId: 0,
    compatibilityStatus: 'NA',
    paperType: ''
  })
  filteredPaperTypes.value = []
  availableProductModels.value = []
}

// 加载产品列表
const loadAvailableProducts = async () => {
  try {
    const response = await fetch('/api/api/product-management/names')
    if (response.ok) {
      availableProducts.value = await response.json()
    } else {
      console.error('加载产品列表失败')
      availableProducts.value = []
    }
  } catch (error) {
    console.error('加载产品列表失败:', error)
    availableProducts.value = []
  }
}

// 联动选择：产品名称变化时加载对应的烫金纸性能类型和产品型号
const onProductNameChange = async () => {
  if (leduvglitterConfigForm.productName) {
    // 加载烫金纸性能类型
    loadingPaperTypes.value = true
    try {
      filteredPaperTypes.value = await postProcessingLeduvglitterApi.getPaperTypesByProductName(leduvglitterConfigForm.productName)
    } catch (error) {
      console.error('加载烫金纸性能类型失败:', error)
      filteredPaperTypes.value = []
    } finally {
      loadingPaperTypes.value = false
    }

    // 加载产品型号
    loadingProductModels.value = true
    try {
      availableProductModels.value = await postProcessingLeduvglitterApi.getProductModelNumbersByProductName(leduvglitterConfigForm.productName)
    } catch (error) {
      console.error('加载产品型号失败:', error)
      availableProductModels.value = []
    } finally {
      loadingProductModels.value = false
    }
  } else {
    filteredPaperTypes.value = []
    availableProductModels.value = []
  }
}

// 筛选部分的联动选择：产品名称变化时加载对应的烫金纸性能类型
const onSearchProductNameChange = async () => {
  if (searchForm.productName) {
    loadingPaperTypes.value = true
    try {
      filteredPaperTypes.value = await postProcessingLeduvglitterApi.getPaperTypesByProductName(searchForm.productName)
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

// 加载布面纸类型选项
const loadClothPaperTypeOptions = async () => {
  try {
    loadingClothPaperTypes.value = true
    clothPaperTypeOptions.value = await postProcessingLeduvglitterApi.getClothPaperTypeOptions()
  } catch (error) {
    console.error('加载布面纸类型选项失败:', error)
    clothPaperTypeOptions.value = []
  } finally {
    loadingClothPaperTypes.value = false
  }
}

// 选中项管理
const toggleSelectAll = () => {
  if (selectedItems.value.length === paginatedData.value.length) {
    selectedItems.value = []
  } else {
    selectedItems.value = paginatedData.value.map(item => item.id!).filter(id => id !== undefined)
  }
}

const clearSelection = () => {
  selectedItems.value = []
}

// 复制配置
const copyLeduvglitterConfig = (config: PostProcessingLeduvglitter) => {
  Object.assign(leduvglitterConfigForm, {
    id: null,
    productName: config.productName,
    productModelNumber: config.productModelNumber || '',
    clothPaperTypeId: config.clothPaperTypeId,
    compatibilityStatus: config.compatibilityStatus,
    paperType: config.paperType || ''
  })
  showAddDialog.value = true
  
  if (config.productName) {
    onProductNameChange()
  }
}

// 批量添加对话框
const openBatchAddDialog = async () => {
  showBatchAddDialog.value = true
  batchAddForm.paperType = ''
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddSeriesProducts.value = {}
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
  
  // 加载所有可用的性能类型
  await loadAvailablePaperTypes()
}

const closeBatchAddDialog = () => {
  showBatchAddDialog.value = false
  Object.assign(batchAddForm, {
    paperType: '',
    selectedSeries: [],
    selectedProducts: {},
    clothPaperTypeId: 0,
    compatibilityStatus: 'NA'
  })
  batchAddSeriesProducts.value = {}
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
}

// 加载所有可用的性能类型
const loadAvailablePaperTypes = async () => {
  try {
    // 使用与适用界面管理相同的 API 调用方式
    const response = await fetch('/api/api/product-management/options/paper-types')
    if (response.ok) {
      const data = await response.json()
      availablePaperTypes.value = data || []
    } else {
      console.error('加载烫金纸性能类型列表失败，状态码:', response.status)
      availablePaperTypes.value = []
    }
  } catch (error) {
    console.error('加载烫金纸性能类型列表失败:', error)
    availablePaperTypes.value = []
  }
}

// 批量添加时选择性能类型
const onBatchAddPaperTypeChange = async () => {
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddSeriesProducts.value = {}
  batchAddSeriesSearchText.value = ''
  
  if (batchAddForm.paperType) {
    // 加载支持该性能类型的系列
    try {
      const response = await fetch(`/api/api/products/series-by-paper-type/${encodeURIComponent(batchAddForm.paperType)}`)
      if (response.ok) {
        batchAddAvailableSeries.value = await response.json()
      } else {
        batchAddAvailableSeries.value = []
      }
    } catch (error) {
      console.error('加载产品系列失败:', error)
      batchAddAvailableSeries.value = []
    }
  } else {
    batchAddAvailableSeries.value = []
  }
}

// 批量添加对话框中产品系列变化处理
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
}

// 批量添加映射
const batchAddMappings = async () => {
  if (!batchAddForm.paperType) {
    alert('請先選擇燙金紙性能類型')
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
        const mapping = {
          productName: seriesName,
          productModelNumber: null,
          clothPaperTypeId: batchAddForm.clothPaperTypeId,
          compatibilityStatus: batchAddForm.compatibilityStatus,
          paperType: batchAddForm.paperType || null
        }
        
        // 处理单个映射
        await processMapping(mapping, seriesName, { successCount, failCount, skipCount }, errors, skipped)
      } else {
        // 为每个选中的产品型号创建映射
        for (const modelNumber of selectedProductModels) {
          const mapping = {
            productName: seriesName,
            productModelNumber: modelNumber,
            clothPaperTypeId: batchAddForm.clothPaperTypeId,
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
      await loadLeduvglitterConfigs()
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
    const checkUrl = `/api/api/post-processing-leduvglitter/check-unique?` +
      `productName=${encodeURIComponent(mapping.productName)}&` +
      `productModelNumber=${encodeURIComponent(mapping.productModelNumber || '')}&` +
      `clothPaperTypeId=${mapping.clothPaperTypeId}&` +
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
          `布面紙類型ID: ${mapping.clothPaperTypeId}\n` +
          `性能類型: ${mapping.paperType || '(空)'}\n` +
          `當前狀態: ${getCompatibilityStatusText(existing.compatibilityStatus)}\n\n` +
          `是否更新為: ${getCompatibilityStatusText(mapping.compatibilityStatus)}?`
        )
        
        if (shouldUpdate) {
          // 更新现有记录
          const updateResponse = await fetch(`/api/api/post-processing-leduvglitter/${existing.id}`, {
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
        // 不存在，创建新记录
        const createResponse = await fetch('/api/api/post-processing-leduvglitter', {
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
      // 不存在，创建新记录
      const createResponse = await fetch('/api/api/post-processing-leduvglitter', {
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

// 批量修改对话框
const batchEditForm = reactive({
  productName: '',
  productModelNumber: '',
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
        const item = paginatedData.value.find(c => c.id === id)
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
        
        const response = await fetch(`/api/api/post-processing-leduvglitter/${id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(updateData)
        })
        
        if (!response.ok) {
          throw new Error('更新失败')
        }
        
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
      await loadLeduvglitterConfigs()
    }
  } catch (error) {
    console.error('批量修改失败:', error)
    alert('批量修改失败，请重试')
  }
}

// 导入对话框
const openImportDialog = () => {
  showImportDialog.value = true
  selectedFile.value = null
  importResult.value = null
}

const closeImportDialog = () => {
  showImportDialog.value = false
  selectedFile.value = null
  importResult.value = null
  if (importFileInput.value) {
    importFileInput.value.value = ''
  }
}

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    selectedFile.value = target.files[0]
  }
}

const downloadImportTemplate = async () => {
  try {
    const response = await fetch('/api/api/post-processing-leduvglitter/import-template', {
      method: 'GET'
    })
    
    if (!response.ok) {
      throw new Error('下载模板失败')
    }
    
    const contentDisposition = response.headers.get('Content-Disposition')
    let fileName = '絲印LED UV灑閃粉配置導入模板.xlsx'
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
  
  try {
    const formData = new FormData()
    formData.append('file', selectedFile.value)
    
    const response = await fetch('/api/api/post-processing-leduvglitter/import', {
      method: 'POST',
      body: formData
    })
    
    const result = await response.json()
    importResult.value = result
    
    if (result.success) {
      await loadLeduvglitterConfigs()
      currentPage.value = 1
      // 清空搜索条件
      Object.assign(searchForm, {
        productName: '',
        productModelNumber: '',
        clothPaperTypeId: 0,
        paperType: '',
        compatibilityStatus: ''
      })
    }
  } catch (error) {
    console.error('导入失败:', error)
    importResult.value = {
      success: false,
      message: '導入失敗: ' + (error as Error).message
    }
  }
}

// 导出数据
const exportData = async () => {
  try {
    const response = await fetch('/api/api/post-processing-leduvglitter/export', {
      method: 'GET'
    })
    
    if (!response.ok) {
      throw new Error('导出失败')
    }
    
    const contentDisposition = response.headers.get('Content-Disposition')
    let fileName = '絲印LED UV灑閃粉配置.xlsx'
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
    
    alert('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    alert('导出失败，请重试')
  }
}

// 批量添加系列搜索过滤
const filteredBatchAddSeries = computed(() => {
  if (!batchAddSeriesSearchText.value) {
    return batchAddAvailableSeries.value
  }
  const searchText = batchAddSeriesSearchText.value.toLowerCase()
  return batchAddAvailableSeries.value.filter(series => 
    series.toLowerCase().includes(searchText)
  )
})

// 组件挂载时加载数据
onMounted(() => {
  loadLeduvglitterConfigs()
  loadAvailableProducts()
  loadClothPaperTypeOptions()
})
</script>

