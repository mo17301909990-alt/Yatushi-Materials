<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">管理</h1>
            <p class="mt-2 text-gray-600">管理烫金工艺的兼容性规则和配置</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="openAddDialog"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加规则
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

      <!-- 规则管理说明 -->
      <div class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
        <div class="flex items-start">
          <svg class="w-5 h-5 text-blue-500 mr-2 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
          </svg>
          <div>
            <h3 class="text-sm font-medium text-blue-800">耐磨燙金紙選用</h3>
            <p class="text-sm text-blue-700 mt-1">
              管理燙金工藝兼容性規則，這些規則用於金紙漸進式查詢。每條規則定義了在特定條件下（產品類型、圖案特徵、燙金類型、工序）適用的燙金紙性能類型。
            </p>
          </div>
        </div>
      </div>

      <!-- 篩選面板 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-7 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">產品類型</label>
          <select
            v-model="filters.productTypeId"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">全部產品類型</option>
            <option v-for="option in productTypeOptions" :key="option.id" :value="option.id">
              {{ option.productName }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">圖案特徵</label>
          <select
            v-model="filters.patternCharacteristicId"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">全部圖案特徵</option>
            <option v-for="option in patternCharacteristicOptions" :key="option.id" :value="option.id">
              {{ option.optionName }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">燙金類型</label>
          <select
            v-model="filters.hotStampingTypeId"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">全部燙金類型</option>
            <option v-for="option in hotStampingTypeOptions" :key="option.id" :value="option.id">
              {{ option.positionType ? `${option.stampingType}-${option.positionType}` : option.stampingType }}
            </option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">前工序</label>
          <select
            v-model="filters.preProcessingStepId"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
          >
            <option value="">全部前工序</option>
            <option v-for="option in preProcessingStepOptions" :key="option.id" :value="option.id">
              {{ option.preProcessingStepsName || option.name }}
            </option>
          </select>
        </div>
          <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙性能</label>
            <input
              v-model="filters.paperPerformance"
              type="text"
              placeholder="烫金纸性能"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">兼容性</label>
            <select
              v-model="filters.compatibility"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              <option value="">全部狀態</option>
              <option value="V">兼容</option>
              <option value="X">不兼容</option>
            </select>
          </div>
          <div class="flex items-end space-x-2">
            <button
              @click="applyFilters"
              class="flex-1 bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 transition-colors"
            >
              篩選
            </button>
            <button
              @click="resetFilters"
              class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
            >
              重置
            </button>
          </div>
        </div>
      </div>

      <!-- 跳过 Match 耐磨映射的烫金纸类型配置 -->
      <div class="bg-indigo-50 border border-indigo-200 rounded-lg p-4 mb-6">
        <div class="flex items-center justify-between mb-3">
          <div>
            <h3 class="text-sm font-medium text-indigo-900">在 Match 中跳过耐磨金纸映射的烫金纸类型</h3>
            <p class="mt-1 text-xs text-indigo-700">
              这里选择的烫金纸类型，在 Match 烫金筛选时不会被耐磨金纸映射规则限制（即使映射为 X / 无记录），但仍会参与其他前后工序/适用界面等规则。
            </p>
          </div>
          <button
            @click="saveSkipPaperTypesConfig"
            :disabled="savingSkipConfig"
            class="inline-flex items-center px-3 py-1.5 border border-transparent text-xs font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <svg v-if="!savingSkipConfig" class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
            </svg>
            <svg v-else class="w-4 h-4 mr-1 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                    d="M4 12a8 8 0 018-8m0 0a8 8 0 018 8m-8-8v4" />
            </svg>
            {{ savingSkipConfig ? '保存中...' : '保存配置' }}
          </button>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label class="block text-xs font-medium text-indigo-900 mb-1">可选烫金纸类型</label>
            <select
              v-model="skipPaperTypes"
              multiple
              class="w-full px-3 py-2 border border-indigo-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
              size="6"
            >
              <option v-for="type in allHotStampingPaperTypes" :key="type" :value="type">
                {{ type }}
              </option>
            </select>
            <p class="mt-1 text-xs text-indigo-600">按住 Ctrl/Cmd 键可多选。</p>
          </div>
          <div>
            <label class="block text-xs font-medium text-indigo-900 mb-1">当前已配置的跳过类型</label>
            <div class="min-h-[2.5rem] border border-dashed border-indigo-300 rounded-md px-3 py-2 text-sm bg-white">
              <span v-if="skipPaperTypes.length === 0" class="text-gray-400">当前未配置任何跳过类型</span>
              <div v-else class="flex flex-wrap gap-2">
                <span
                  v-for="type in skipPaperTypes"
                  :key="type"
                  class="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium bg-indigo-100 text-indigo-800"
                >
                  {{ type }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 批量操作面板 -->
      <div class="bg-white rounded-lg shadow-md p-4 mb-6" v-if="selectedItems.length > 0">
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-4">
            <span class="text-sm text-gray-600">已选择 {{ selectedItems.length }} 项</span>
            <button
              @click="batchEdit"
              class="inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded text-indigo-700 bg-indigo-100 hover:bg-indigo-200"
            >
              批量编辑
            </button>
            <button
              @click="batchCopy"
              class="inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded text-green-700 bg-green-100 hover:bg-green-200"
            >
              批量复制
            </button>
            <button
              @click="batchDelete"
              class="inline-flex items-center px-3 py-1 border border-transparent text-sm font-medium rounded text-red-700 bg-red-100 hover:bg-red-200"
            >
              批量删除
            </button>
          </div>
          <button
            @click="clearSelection"
            class="text-sm text-gray-500 hover:text-gray-700"
          >
            清除选择
          </button>
        </div>
      </div>

      <!-- 兼容性矩阵 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <h3 class="text-lg font-medium text-gray-900">兼容性规则列表</h3>
            <div class="flex items-center space-x-2">
              <span class="text-sm text-gray-500">共 {{ filteredData.length }} 條規則</span>
              <div class="flex rounded-md shadow-sm">
                <button
                  @click="viewMode = 'table'"
                  :class="[
                    'px-3 py-1 text-sm font-medium rounded-l-md border',
                    viewMode === 'table' 
                      ? 'bg-indigo-600 text-white border-indigo-600' 
                      : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-50'
                  ]"
                >
                  表格視圖
                </button>
                <button
                  @click="viewMode = 'matrix'"
                  :class="[
                    'px-3 py-1 text-sm font-medium rounded-r-md border-t border-r border-b',
                    viewMode === 'matrix' 
                      ? 'bg-indigo-600 text-white border-indigo-600' 
                      : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-50'
                  ]"
                >
                  矩陣視圖
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 表格视图 -->
        <div v-if="viewMode === 'table'" class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left">
                  <input
                    type="checkbox"
                    :checked="selectedItems.length === filteredData.length && filteredData.length > 0"
                    @change="toggleSelectAll"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">產品類型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">圖案特徵</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金類型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">前工序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙性能</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in paginatedData" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <input
                    type="checkbox"
                    :checked="selectedItems.includes(item.id)"
                    @change="toggleSelectItem(item.id)"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <div class="flex flex-col">
                    <span class="font-medium">{{ item.productTypeName || item.productType || '-' }}</span>
                    <span class="text-xs text-gray-500">ID: {{ item.productTypeId || '-' }}</span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <div class="flex flex-col">
                    <span class="font-medium">{{ item.patternCharacteristicName || item.patternCharacteristic || '-' }}</span>
                    <span class="text-xs text-gray-500">ID: {{ item.patternCharacteristicId || '-' }}</span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <div class="flex flex-col">
                    <span class="font-medium">{{ item.hotStampingTypeName || item.hotStampingType || '-' }}</span>
                    <span class="text-xs text-gray-500">ID: {{ item.hotStampingTypeId || '-' }}</span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <div class="flex flex-col">
                    <span class="font-medium">{{ item.preProcessingStepName || '-' }}</span>
                    <span class="text-xs text-gray-500">ID: {{ item.preProcessingStepId || '-' }}</span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.paperPerformance || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                      item.compatibility === 'V' 
                        ? 'bg-green-100 text-green-800' 
                        : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ item.compatibility === 'V' ? '兼容' : '不兼容' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button
                      @click="editItem(item)"
                      class="text-indigo-600 hover:text-indigo-900"
                    >
                      編輯
                    </button>
                    <button
                      @click="copyItem(item)"
                      class="text-green-600 hover:text-green-900"
                    >
                      複製
                    </button>
                    <button
                      @click="deleteItem(item)"
                      class="text-red-600 hover:text-red-900"
                    >
                      刪除
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 矩阵视图 -->
        <div v-if="viewMode === 'matrix'" class="p-6">
          <div class="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4">
            <div
              v-for="item in paginatedData"
              :key="item.id"
              class="border rounded-lg p-4 hover:shadow-md transition-shadow"
            >
              <div class="flex items-start justify-between mb-3">
                <div class="flex items-center">
                  <input
                    type="checkbox"
                    :checked="selectedItems.includes(item.id)"
                    @change="toggleSelectItem(item.id)"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded mr-2"
                  />
                  <span
                    :class="[
                      'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                      item.compatibility === 'V' 
                        ? 'bg-green-100 text-green-800' 
                        : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ item.compatibility === 'V' ? '兼容' : '不兼容' }}
                  </span>
                </div>
                <div class="flex space-x-1">
                  <button
                    @click="editItem(item)"
                    class="text-indigo-600 hover:text-indigo-900 text-sm"
                  >
                    編輯
                  </button>
                  <button
                    @click="copyItem(item)"
                    class="text-green-600 hover:text-green-900 text-sm"
                  >
                    複製
                  </button>
                </div>
              </div>
              <div class="space-y-2">
                <div>
                  <span class="text-xs text-gray-500">產品類型:</span>
                  <span class="text-sm font-medium">{{ item.productType }}</span>
                </div>
                <div>
                  <span class="text-xs text-gray-500">燙金圖案:</span>
                  <span class="text-sm font-medium">{{ item.patternCharacteristic }}</span>
                </div>
                <div>
                  <span class="text-xs text-gray-500">燙金類型:</span>
                  <span class="text-sm font-medium">{{ item.hotStampingType }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
          <div class="flex-1 flex justify-between sm:hidden">
            <button
              @click="currentPage = Math.max(1, currentPage - 1)"
              :disabled="currentPage === 1"
              class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
            >
              上一頁
            </button>
            <button
              @click="currentPage = Math.min(totalPages, currentPage + 1)"
              :disabled="currentPage === totalPages"
              class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
            >
              下一頁
            </button>
          </div>
          <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
            <div>
              <p class="text-sm text-gray-700">
                顯示第 <span class="font-medium">{{ (currentPage - 1) * pageSize + 1 }}</span> 到
                <span class="font-medium">{{ Math.min(currentPage * pageSize, filteredData.length) }}</span> 條，
                共 <span class="font-medium">{{ filteredData.length }}</span> 條記錄
              </p>
            </div>
            <div>
              <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
                <button
                  @click="currentPage = Math.max(1, currentPage - 1)"
                  :disabled="currentPage === 1"
                  class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                >
                  上一页
                </button>
                <button
                  v-for="page in visiblePages"
                  :key="page"
                  @click="currentPage = page"
                  :class="[
                    'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                    page === currentPage
                      ? 'z-10 bg-indigo-50 border-indigo-500 text-indigo-600'
                      : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'
                  ]"
                >
                  {{ page }}
                </button>
                <button
                  @click="currentPage = Math.min(totalPages, currentPage + 1)"
                  :disabled="currentPage === totalPages"
                  class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                >
                  下一页
                </button>
              </nav>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 md:w-3/4 lg:w-1/2 shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <h3 class="text-lg font-medium text-gray-900 mb-4">
            {{ showAddDialog ? '添加兼容性規則' : '編輯兼容性規則' }}
          </h3>
          
          <form @submit.prevent="saveItem" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">产品类型</label>
                <select
                  v-model="formData.productTypeId"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">请选择产品类型</option>
                  <option v-for="option in productTypeOptions" :key="option.id" :value="option.id">
                    {{ option.productName || option.product_name }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">图案特征</label>
                <select
                  v-model="formData.patternCharacteristicId"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">请选择图案特征</option>
                  <option v-for="option in patternCharacteristicOptions" :key="option.id" :value="option.id">
                    {{ option.optionName || option.option_name }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金类型</label>
                <select
                  v-model="formData.hotStampingTypeId"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">请选择烫金类型</option>
                  <option v-for="option in hotStampingTypeOptions" :key="option.id" :value="option.id">
                    {{ option.positionType ? `${option.stampingType}-${option.positionType}` : option.stampingType }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">前工序</label>
                <select
                  v-model="formData.preProcessingStepId"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">请选择前工序（可选）</option>
                  <option v-for="option in preProcessingStepOptions" :key="option.id" :value="option.id">
                    {{ option.preProcessingStepsName || option.name }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">後工序</label>
                <select
                  v-model="formData.postProcessingStepId"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">请选择后工序（可选）</option>
                  <option v-for="option in postProcessingStepOptions" :key="option.id" :value="option.id">
                    {{ option.name || option.processName || option.stepName }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙性能類型</label>
                <input
                  v-model="formData.paperPerformance"
                  type="text"
                  required
                  placeholder="如：普通金紙、普通耐磨、高耐磨"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性</label>
                <select
                  v-model="formData.compatibility"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">請選擇兼容性</option>
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
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
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
              >
                {{ showAddDialog ? '添加' : '保存' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 批量复制对话框 -->
    <div v-if="showBatchCopyDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 md:w-3/4 lg:w-1/2 shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <h3 class="text-lg font-medium text-gray-900 mb-4">
            批量复制规则（可选择修改）
          </h3>
          <p class="text-sm text-gray-600 mb-4">
            将复制选中的 {{ selectedItems.length }} 条规则。您可以选择修改某些字段，未修改的字段将保持原值。
          </p>
          
          <form @submit.prevent="confirmBatchCopy" class="space-y-4">
            <div class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-4">
              <p class="text-sm text-blue-800">
                <strong>提示：</strong>只填写需要修改的字段，留空的字段将保持原规则的值。
            </p>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">产品类型（可选修改）</label>
                <select
                  v-model="batchCopyModifications.productTypeId"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option :value="undefined">保持原值</option>
                  <option v-for="option in productTypeOptions" :key="option.id" :value="option.id">
                    {{ option.productName || option.product_name }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">图案特征（可选修改）</label>
                <select
                  v-model="batchCopyModifications.patternCharacteristicId"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option :value="undefined">保持原值</option>
                  <option v-for="option in patternCharacteristicOptions" :key="option.id" :value="option.id">
                    {{ option.optionName || option.option_name }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金类型（可选修改）</label>
                <select
                  v-model="batchCopyModifications.hotStampingTypeId"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option :value="undefined">保持原值</option>
                  <option v-for="option in hotStampingTypeOptions" :key="option.id" :value="option.id">
                    {{ option.positionType ? `${option.stampingType}-${option.positionType}` : option.stampingType }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">前工序（可选修改）</label>
                <select
                  v-model="batchCopyModifications.preProcessingStepId"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option :value="undefined">保持原值</option>
                  <option :value="null">清空（设为空）</option>
                  <option v-for="option in preProcessingStepOptions" :key="option.id" :value="option.id">
                    {{ option.preProcessingStepsName || option.name }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">後工序（可选修改）</label>
                <select
                  v-model="batchCopyModifications.postProcessingStepId"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option :value="undefined">保持原值</option>
                  <option :value="null">清空（设为空）</option>
                  <option v-for="option in postProcessingStepOptions" :key="option.id" :value="option.id">
                    {{ option.name || option.processName || option.stepName }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙性能類型（可选修改）</label>
                <input
                  v-model="batchCopyModifications.paperPerformance"
                  type="text"
                  placeholder="留空保持原值，如：普通金紙、普通耐磨、高耐磨"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性（可选修改）</label>
                <select
                  v-model="batchCopyModifications.compatibility"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option :value="undefined">保持原值</option>
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
                </select>
              </div>
            </div>

            <div class="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                @click="closeBatchCopyDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
              <button
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
              >
                确认复制
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
import { useRoute } from 'vue-router'
import { smartCompatibilityApi, type SmartCompatibilityRule, type SmartCompatibilityRuleDetail, type CompatibilityFilter } from '../../../api/modules/smartCompatibility'
import { preProcessingStepsApi } from '../../../api/modules/preProcessingSteps'
import { productTypeOptionsApi } from '../../../api/modules/productTypeOptions'
import { wearResistantGoldPaperMappingApi } from '../../../api/modules/wearResistantGoldPaperMapping'

// 路由实例
const route = useRoute()

// 响应式数据
const compatibilityData = ref<SmartCompatibilityRuleDetail[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showBatchCopyDialog = ref(false)
const viewMode = ref('table')
const currentPage = ref(1)
const pageSize = ref(10)
const selectedItems = ref<number[]>([])
const totalItems = ref(0)

// 选项数据
const productTypeOptions = ref<any[]>([])
const patternCharacteristicOptions = ref<any[]>([])
const hotStampingTypeOptions = ref<any[]>([])
const preProcessingStepOptions = ref<any[]>([])
const postProcessingStepOptions = ref<any[]>([])

// 跳过耐磨映射配置
const allHotStampingPaperTypes = ref<string[]>([])
const skipPaperTypes = ref<string[]>([])
const savingSkipConfig = ref(false)

// 筛选条件
const filters = reactive({
  productTypeId: '',
  patternCharacteristicId: '',
  hotStampingTypeId: '',
  preProcessingStepId: '',
  paperPerformance: '',
  compatibility: ''
})

// 表单数据
const formData = reactive<Partial<SmartCompatibilityRule>>({
  id: undefined,
  productTypeId: undefined,
  patternCharacteristicId: undefined,
  hotStampingTypeId: undefined,
  preProcessingStepId: undefined,
  postProcessingStepId: undefined,
  paperPerformance: '',
  compatibility: undefined
})

// 批量复制修改数据
const batchCopyModifications = reactive<Partial<SmartCompatibilityRule>>({
  productTypeId: undefined,
  patternCharacteristicId: undefined,
  hotStampingTypeId: undefined,
  preProcessingStepId: undefined,
  postProcessingStepId: undefined,
  paperPerformance: '',
  compatibility: undefined
})


// 计算属性
const filteredData = computed(() => {
  let data = compatibilityData.value
  console.log('🔍 筛选前数据量:', data.length)
  console.log('🔍 当前筛选条件:', filters)

  // 产品类型筛选
  if (filters.productTypeId && filters.productTypeId !== '') {
    const productTypeId = parseInt(filters.productTypeId)
    const beforeCount = data.length
    data = data.filter(item => item.productTypeId === productTypeId)
    console.log(`🔍 产品类型筛选: ${beforeCount} -> ${data.length} (筛选ID: ${productTypeId})`)
  }
  
  // 图案特征筛选
  if (filters.patternCharacteristicId && filters.patternCharacteristicId !== '') {
    const patternCharacteristicId = parseInt(filters.patternCharacteristicId)
    const beforeCount = data.length
    data = data.filter(item => item.patternCharacteristicId === patternCharacteristicId)
    console.log(`🔍 图案特征筛选: ${beforeCount} -> ${data.length} (筛选ID: ${patternCharacteristicId})`)
  }
  
  // 烫金类型筛选
  if (filters.hotStampingTypeId && filters.hotStampingTypeId !== '') {
    const hotStampingTypeId = parseInt(filters.hotStampingTypeId)
    const beforeCount = data.length
    data = data.filter(item => item.hotStampingTypeId === hotStampingTypeId)
    console.log(`🔍 烫金类型筛选: ${beforeCount} -> ${data.length} (筛选ID: ${hotStampingTypeId})`)
  }
  
  // 前工序筛选
  if (filters.preProcessingStepId && filters.preProcessingStepId !== '') {
    const preProcessingStepId = parseInt(filters.preProcessingStepId)
    const beforeCount = data.length
    data = data.filter(item => item.preProcessingStepId === preProcessingStepId)
    console.log(`🔍 前工序筛选: ${beforeCount} -> ${data.length} (筛选ID: ${preProcessingStepId})`)
  }
  
  // 烫金纸性能筛选
  if (filters.paperPerformance && filters.paperPerformance.trim() !== '') {
    const beforeCount = data.length
    data = data.filter(item => 
      item.paperPerformance && 
      item.paperPerformance.toLowerCase().includes(filters.paperPerformance.toLowerCase())
    )
    console.log(`🔍 烫金纸性能筛选: ${beforeCount} -> ${data.length} (关键词: ${filters.paperPerformance})`)
  }
  
  // 兼容性筛选
  if (filters.compatibility && filters.compatibility !== '') {
    const beforeCount = data.length
    data = data.filter(item => item.compatibility === filters.compatibility)
    console.log(`🔍 兼容性筛选: ${beforeCount} -> ${data.length} (状态: ${filters.compatibility})`)
  }

  console.log('🔍 筛选后数据量:', data.length)
  
  // 筛选后也要保持稳定排序
  return data.sort((a, b) => {
    // 1. 按烫金纸性能排序
    if (a.paperPerformance !== b.paperPerformance) {
      return (a.paperPerformance || '').localeCompare(b.paperPerformance || '', 'zh-CN')
    }
    // 2. 按产品类型ID排序
    const aProductTypeId = a.productTypeId || 0
    const bProductTypeId = b.productTypeId || 0
    if (aProductTypeId !== bProductTypeId) {
      return aProductTypeId - bProductTypeId
    }
    // 3. 按图案特征ID排序
    const aPatternId = a.patternCharacteristicId || 0
    const bPatternId = b.patternCharacteristicId || 0
    if (aPatternId !== bPatternId) {
      return aPatternId - bPatternId
    }
    // 4. 按烫金类型ID排序
    const aHotStampingId = a.hotStampingTypeId || 0
    const bHotStampingId = b.hotStampingTypeId || 0
    if (aHotStampingId !== bHotStampingId) {
      return aHotStampingId - bHotStampingId
    }
    // 5. 最后按ID排序（确保完全稳定）
    return (a.id || 0) - (b.id || 0)
  })
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

// 方法
const loadCompatibilityData = async () => {
  loading.value = true
  try {
    // 使用新的API获取包含关联表信息的完整数据
    const response = await smartCompatibilityApi.getCompatibilityRulesWithDetails()
    let data = response || []
    
    // 对数据进行稳定排序，确保顺序不会变化
    data = data.sort((a, b) => {
      // 1. 按烫金纸性能排序
      if (a.paperPerformance !== b.paperPerformance) {
        return (a.paperPerformance || '').localeCompare(b.paperPerformance || '', 'zh-CN')
      }
      // 2. 按产品类型ID排序
      const aProductTypeId = a.productTypeId || 0
      const bProductTypeId = b.productTypeId || 0
      if (aProductTypeId !== bProductTypeId) {
        return aProductTypeId - bProductTypeId
      }
      // 3. 按图案特征ID排序
      const aPatternId = a.patternCharacteristicId || 0
      const bPatternId = b.patternCharacteristicId || 0
      if (aPatternId !== bPatternId) {
        return aPatternId - bPatternId
      }
      // 4. 按烫金类型ID排序
      const aHotStampingId = a.hotStampingTypeId || 0
      const bHotStampingId = b.hotStampingTypeId || 0
      if (aHotStampingId !== bHotStampingId) {
        return aHotStampingId - bHotStampingId
      }
      // 5. 最后按ID排序（确保完全稳定）
      return (a.id || 0) - (b.id || 0)
    })
    
    compatibilityData.value = data
  } catch (error) {
    console.error('加载兼容性数据失败:', error)
    compatibilityData.value = []
    alert('加载兼容性数据失败，请刷新页面重试')
  } finally {
    loading.value = false
  }
}

const applyFilters = () => {
  currentPage.value = 1
  // 不需要重新加载数据，因为筛选是在前端进行的
  // loadCompatibilityData()
}

const resetFilters = () => {
  Object.assign(filters, {
    productTypeId: '',
    patternCharacteristicId: '',
    hotStampingTypeId: '',
    preProcessingStepId: '',
    paperPerformance: '',
    compatibility: ''
  })
  currentPage.value = 1
  // 不需要重新加载数据，因为筛选是在前端进行的
  // loadCompatibilityData()
}

const toggleSelectAll = () => {
  if (selectedItems.value.length === filteredData.value.length) {
    selectedItems.value = []
  } else {
    selectedItems.value = filteredData.value.map(item => item.id)
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

const editItem = (item: SmartCompatibilityRuleDetail) => {
  // 只复制必要的ID字段，不复制展示字段
  Object.assign(formData, {
    id: item.id,
    productTypeId: item.productTypeId,
    patternCharacteristicId: item.patternCharacteristicId,
    hotStampingTypeId: item.hotStampingTypeId,
    preProcessingStepId: item.preProcessingStepId,
    postProcessingStepId: item.postProcessingStepId,
    paperPerformance: item.paperPerformance,
    compatibility: item.compatibility
  })
  showEditDialog.value = true
}

const copyItem = (item: SmartCompatibilityRuleDetail) => {
  // 只复制必要的ID字段，清除id，不复制展示字段
  Object.assign(formData, {
    id: undefined,
    productTypeId: item.productTypeId,
    patternCharacteristicId: item.patternCharacteristicId,
    hotStampingTypeId: item.hotStampingTypeId,
    preProcessingStepId: item.preProcessingStepId,
    postProcessingStepId: item.postProcessingStepId,
    paperPerformance: item.paperPerformance,
    compatibility: item.compatibility
  })
  showAddDialog.value = true
}

const openAddDialog = () => {
  // 清空表单数据
  closeDialog()
  showAddDialog.value = true
}

const deleteItem = async (item: SmartCompatibilityRuleDetail) => {
  if (confirm('确定要删除这个兼容性规则吗？')) {
    try {
      await smartCompatibilityApi.deleteRule(item.id)
      await loadCompatibilityData()
    } catch (error) {
      console.error('删除失败:', error)
      // 如果API调用失败，从本地数据中删除
      const index = compatibilityData.value.findIndex(d => d.id === item.id)
      if (index > -1) {
        compatibilityData.value.splice(index, 1)
      }
    }
  }
}

const saveItem = async () => {
  try {
    // 验证必填字段
    if (!formData.productTypeId || !formData.patternCharacteristicId || 
        !formData.hotStampingTypeId || !formData.paperPerformance || 
        !formData.compatibility) {
      alert('请填写所有必填字段')
      return
    }

    // 前端检查重复（提前提示用户）
    const isDuplicate = compatibilityData.value.some(item => {
      // 如果是编辑模式，排除当前项
      if (showEditDialog.value && formData.id && item.id === formData.id) {
        return false
      }
      // 检查所有关键字段是否相同（包括NULL值的处理）
      const preProcessingMatch = (item.preProcessingStepId === null || item.preProcessingStepId === undefined) 
        ? (formData.preProcessingStepId === null || formData.preProcessingStepId === undefined)
        : item.preProcessingStepId === formData.preProcessingStepId
      
      const postProcessingMatch = (item.postProcessingStepId === null || item.postProcessingStepId === undefined)
        ? (formData.postProcessingStepId === null || formData.postProcessingStepId === undefined)
        : item.postProcessingStepId === formData.postProcessingStepId
      
      return item.productTypeId === formData.productTypeId &&
             item.patternCharacteristicId === formData.patternCharacteristicId &&
             item.hotStampingTypeId === formData.hotStampingTypeId &&
             preProcessingMatch &&
             postProcessingMatch &&
             item.paperPerformance === formData.paperPerformance &&
             item.compatibility === formData.compatibility
    })

    if (isDuplicate) {
      alert('已存在相同的兼容性规则，无法重复添加')
      return
    }

    if (showAddDialog.value) {
      // 添加新规则
      await smartCompatibilityApi.createRule(formData as Omit<SmartCompatibilityRule, 'id' | 'createdAt' | 'updatedAt'>)
      // 保存成功后重新加载数据，确保数据一致性
      await loadCompatibilityData()
    } else {
      // 更新现有规则
      if (!formData.id) {
        alert('规则ID不存在，无法更新')
        return
      }
      await smartCompatibilityApi.updateRule(formData.id, formData)
      // 保存成功后重新加载数据，确保数据一致性
      await loadCompatibilityData()
    }
    closeDialog()
  } catch (error: any) {
    console.error('保存失败:', error)
    // 尝试从错误响应中获取错误信息
    let errorMessage = '保存失败，请检查网络连接或联系管理员'
    if (error?.response?.data?.error) {
      errorMessage = error.response.data.error
    } else if (error?.message) {
      errorMessage = error.message
    }
    alert(errorMessage)
    // 不进行本地数据更新，避免数据不一致
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(formData, {
    id: undefined,
    productTypeId: undefined,
    patternCharacteristicId: undefined,
    hotStampingTypeId: undefined,
    preProcessingStepId: undefined,
    postProcessingStepId: undefined,
    paperPerformance: '',
    compatibility: undefined
  })
}

const batchEdit = async () => {
  try {
    await smartCompatibilityApi.batchOperation({
      ids: selectedItems.value,
      operation: 'edit'
    })
    await loadCompatibilityData()
    selectedItems.value = []
  } catch (error) {
    console.error('批量编辑失败:', error)
  }
}

const batchCopy = () => {
  // 打开批量复制对话框，让用户选择要修改的字段
  if (selectedItems.value.length === 0) {
    alert('请先选择要复制的规则')
    return
  }
  // 清空修改数据
  Object.assign(batchCopyModifications, {
    productTypeId: undefined,
    patternCharacteristicId: undefined,
    hotStampingTypeId: undefined,
    preProcessingStepId: undefined,
    postProcessingStepId: undefined,
    paperPerformance: '',
    compatibility: undefined
  })
  showBatchCopyDialog.value = true
}

const confirmBatchCopy = async () => {
  try {
    // 构建修改数据（只包含用户选择的字段）
    const modifications: any = {}
    if (batchCopyModifications.productTypeId) {
      modifications.productTypeId = batchCopyModifications.productTypeId
    }
    if (batchCopyModifications.patternCharacteristicId) {
      modifications.patternCharacteristicId = batchCopyModifications.patternCharacteristicId
    }
    if (batchCopyModifications.hotStampingTypeId) {
      modifications.hotStampingTypeId = batchCopyModifications.hotStampingTypeId
    }
    if (batchCopyModifications.preProcessingStepId !== undefined) {
      modifications.preProcessingStepId = batchCopyModifications.preProcessingStepId || null
    }
    if (batchCopyModifications.postProcessingStepId !== undefined) {
      modifications.postProcessingStepId = batchCopyModifications.postProcessingStepId || null
    }
    if (batchCopyModifications.paperPerformance) {
      modifications.paperPerformance = batchCopyModifications.paperPerformance
    }
    if (batchCopyModifications.compatibility) {
      modifications.compatibility = batchCopyModifications.compatibility
    }

    await smartCompatibilityApi.batchOperation({
      ids: selectedItems.value,
      operation: 'copy',
      modifications: Object.keys(modifications).length > 0 ? modifications : undefined
    })
    const copiedCount = selectedItems.value.length
    await loadCompatibilityData()
    selectedItems.value = []
    showBatchCopyDialog.value = false
    alert(`成功复制 ${copiedCount} 条规则`)
  } catch (error: any) {
    console.error('批量复制失败:', error)
    let errorMessage = '批量复制失败，请检查网络连接或联系管理员'
    if (error?.response?.data?.error) {
      errorMessage = error.response.data.error
    } else if (error?.message) {
      errorMessage = error.message
    }
    alert(errorMessage)
  }
}

const closeBatchCopyDialog = () => {
  showBatchCopyDialog.value = false
  Object.assign(batchCopyModifications, {
    productTypeId: undefined,
    patternCharacteristicId: undefined,
    hotStampingTypeId: undefined,
    preProcessingStepId: undefined,
    postProcessingStepId: undefined,
    paperPerformance: '',
    compatibility: undefined
  })
}

const batchDelete = async () => {
  if (confirm(`确定要删除选中的 ${selectedItems.value.length} 条规则吗？`)) {
    try {
      await smartCompatibilityApi.batchOperation({
        ids: selectedItems.value,
        operation: 'delete'
      })
      await loadCompatibilityData()
      selectedItems.value = []
    } catch (error) {
      console.error('批量删除失败:', error)
      // 如果API调用失败，从本地数据中删除
      compatibilityData.value = compatibilityData.value.filter(
        item => !selectedItems.value.includes(item.id)
      )
      totalItems.value -= selectedItems.value.length
      selectedItems.value = []
    }
  }
}

const exportData = async () => {
  try {
    const exportFilters: CompatibilityFilter = {
      productTypeId: filters.productTypeId ? parseInt(filters.productTypeId) : undefined,
      patternCharacteristicId: filters.patternCharacteristicId ? parseInt(filters.patternCharacteristicId) : undefined,
      hotStampingTypeId: filters.hotStampingTypeId ? parseInt(filters.hotStampingTypeId) : undefined,
      paperPerformance: filters.paperPerformance || undefined,
      compatibility: filters.compatibility as 'V' | 'X' || undefined
    }
    const blob = await smartCompatibilityApi.exportRules(exportFilters)
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `兼容性规则_${new Date().toISOString().split('T')[0]}.xlsx`
    document.body.appendChild(a)
    a.click()
    window.URL.revokeObjectURL(url)
    document.body.removeChild(a)
  } catch (error) {
    console.error('导出数据失败:', error)
  }
}

// 加载选项数据
const loadOptions = async () => {
  try {
    // 加载产品类型选项
    try {
      const productTypesData = await productTypeOptionsApi.getAllActiveOptions()
      productTypeOptions.value = productTypesData.map(item => ({
        id: item.id,
        productName: item.productName,
        product_name: item.productName // 兼容不同的字段名
      }))
      console.log('产品类型数据:', productTypesData)
    } catch (error) {
      console.error('加载产品类型选项失败:', error)
    }
    
    // 加载图案特征选项
    const patternResponse = await fetch('/api/api/hot-stamping-pattern-base/active')
    if (patternResponse.ok) {
      const data = await patternResponse.json()
      patternCharacteristicOptions.value = data
      console.log('图案特征数据:', data)
    }
    
    // 加载烫金类型选项
    const hotStampingResponse = await fetch('/api/api/hot-stamping-type-options/active')
    if (hotStampingResponse.ok) {
      const data = await hotStampingResponse.json()
      hotStampingTypeOptions.value = data
      console.log('烫金类型数据:', data)
    }
    
    // 加载前工序选项
    try {
      const preProcessingData = await preProcessingStepsApi.getAllActiveOptions()
      preProcessingStepOptions.value = preProcessingData
      console.log('前工序数据:', preProcessingData)
    } catch (error) {
      console.error('加载前工序选项失败:', error)
    }
    
    // 加载后工序选项（合并多种后工序类型）
    const postProcessingOptions = []
    
    // 印刷UV
    const printUvResponse = await fetch('/api/post-processing-print-uv')
    if (printUvResponse.ok) {
      const data = await printUvResponse.json()
      const printUvOptions = data.items?.map((item: any) => ({
        id: item.id,
        name: `印刷UV - ${item.processName || item.name}`,
        type: 'printUv'
      })) || []
      postProcessingOptions.push(...printUvOptions)
    }
    
    // 印刷
    try {
      const printResponse = await fetch('/api/post-processing-print')
      if (printResponse.ok) {
        const data = await printResponse.json()
        const printOptions = data.items?.map((item: any) => ({
          id: item.id,
          name: `印刷 - ${item.processName || item.name}`,
          type: 'print'
        })) || []
        postProcessingOptions.push(...printOptions)
      }
    } catch (error) {
      console.warn('加载印刷后工序失败:', error)
    }
    
    // 絲印LED UV灑閃粉
    try {
      const leduvglitterResponse = await fetch('/api/post-processing-leduvglitter')
      if (leduvglitterResponse.ok) {
        const data = await leduvglitterResponse.json()
        const leduvglitterOptions = data.items?.map((item: any) => ({
          id: item.id,
          name: `絲印LED UV灑閃粉 - ${item.processName || item.name}`,
          type: 'leduvglitter'
        })) || []
        postProcessingOptions.push(...leduvglitterOptions)
      }
    } catch (error) {
      console.warn('加载絲印LED UV灑閃粉后工序失败:', error)
    }
    
    postProcessingStepOptions.value = postProcessingOptions
    console.log('后工序数据:', postProcessingOptions)
    
    // 加载所有烫金纸类型（用于跳过配置）
    // 直接调用 paper-types 接口，避免 getProductOptions 中其他 API 失败导致整体失败
    try {
      const response = await fetch('/api/api/product-management/options/paper-types')
      if (response.ok) {
        const paperTypes = await response.json()
        allHotStampingPaperTypes.value = paperTypes || []
      } else {
        console.warn('加载烫金纸类型失败，状态码:', response.status)
        allHotStampingPaperTypes.value = []
      }
    } catch (error) {
      console.error('加载烫金纸类型失败:', error)
      allHotStampingPaperTypes.value = []
    }
    
    // 加载当前跳过配置
    try {
      skipPaperTypes.value = await wearResistantGoldPaperMappingApi.getSkipPaperTypes()
    } catch (error) {
      console.error('加载跳过配置失败:', error)
      // 如果加载失败，初始化为空数组
      skipPaperTypes.value = []
    }
    
  } catch (error) {
    console.error('加载选项数据失败:', error)
    // 使用默认选项数据
    productTypeOptions.value = [
      { id: 1, product_name: '精平裝/咭書' },
      { id: 2, product_name: '賀咭/紙袋' },
      { id: 3, product_name: '包裝' }
    ]
    patternCharacteristicOptions.value = [
      { id: 1, option_name: '淨線條/字母≤0.5mm' },
      { id: 2, option_name: '淨線條/字母0.5mm<X≤1mm' },
      { id: 3, option_name: '淨實地10mm<X≤20mm' }
    ]
    hotStampingTypeOptions.value = [
      { id: 1, stamping_type: '平面烫金', position_type: '於中間位' },
      { id: 2, stamping_type: '平面烫金', position_type: '到邊位' },
      { id: 3, stamping_type: '立體烫金', position_type: null }
    ]
    preProcessingStepOptions.value = [
      { id: 1, preProcessingStepsName: '普通油墨' },
      { id: 2, preProcessingStepsName: 'UV油墨' },
      { id: 3, preProcessingStepsName: '水性油墨' }
    ]
    postProcessingStepOptions.value = [
      { id: 1, name: '印刷UV - 普通印刷' },
      { id: 2, name: '印刷 - 彩色印刷' },
      { id: 3, name: '絲印LED UV灑閃粉 - 特殊效果' }
    ]
  }
}

// 保存跳过耐磨映射配置
const saveSkipPaperTypesConfig = async () => {
  try {
    savingSkipConfig.value = true
    await wearResistantGoldPaperMappingApi.saveSkipPaperTypes(skipPaperTypes.value)
    alert('已保存跳过 Match 耐磨映射的烫金纸类型配置')
  } catch (error: any) {
    console.error('保存跳过配置失败:', error)
    alert(error?.message || '保存跳过配置失败，请重试')
  } finally {
    savingSkipConfig.value = false
  }
}

// 监听筛选数据变化，自动调整当前页面
watch(filteredData, () => {
  // 当筛选后的数据变化时，如果当前页面超出了总页数，则调整到最后一页
  if (currentPage.value > totalPages.value && totalPages.value > 0) {
    currentPage.value = totalPages.value
  }
  // 如果筛选后没有数据，重置到第一页
  if (totalPages.value === 0) {
    currentPage.value = 1
  }
})

// 生命周期
onMounted(async () => {
  // 先加载选项数据
  await loadOptions()
  // 再加载兼容性数据
  await loadCompatibilityData()
  
  // 检查URL参数中是否有产品类型ID
  const productTypeId = route.query.productTypeId
  if (productTypeId) {
    // 设置筛选条件中的产品类型ID
    filters.productTypeId = String(productTypeId)
    // 应用筛选（不需要重新加载数据，因为数据已经加载了）
    currentPage.value = 1
  }
  
  // 检查URL参数中是否有图案特征ID
  const patternCharacteristicId = route.query.patternCharacteristicId
  if (patternCharacteristicId) {
    // 设置筛选条件中的图案特征ID
    filters.patternCharacteristicId = String(patternCharacteristicId)
    // 应用筛选（不需要重新加载数据，因为数据已经加载了）
    currentPage.value = 1
  }
  
  // 检查URL参数中是否有烫金类型ID
  const hotStampingTypeId = route.query.hotStampingTypeId
  if (hotStampingTypeId) {
    // 设置筛选条件中的烫金类型ID
    filters.hotStampingTypeId = String(hotStampingTypeId)
    // 应用筛选（不需要重新加载数据，因为数据已经加载了）
    currentPage.value = 1
  }
})
</script>
