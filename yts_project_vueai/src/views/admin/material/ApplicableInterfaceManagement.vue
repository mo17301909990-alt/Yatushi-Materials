<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">适用界面管理</h1>
            <p class="mt-2 text-gray-600">管理布面纸类型和兼容性配置</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="openAllMappingsDialog"
              class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12l-3 3m0 0l-3-3m3 3V4M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
              查看所有配置映射
            </button>
            <button
              @click="showAddDialog = true"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加布面纸类型
            </button>
            <button
              @click="batchToggleStatus"
              :disabled="selectedItems.length === 0"
              class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7h12m0 0l-4-4m4 4l-4 4m0 6H4m0 0l4 4m-4-4l4-4"></path>
              </svg>
              批量切换状态
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

      <!-- 搜索和筛选 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">类型名称</label>
            <input
              v-model="searchForm.typeName"
              type="text"
              placeholder="搜索类型名称..."
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">分类</label>
            <select
              v-model="searchForm.category"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              <option value="">全部分类</option>
              <option v-for="category in categories" :key="category" :value="category">
                {{ category }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
            <select
              v-model="searchForm.status"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              <option value="">全部状态</option>
              <option value="true">激活</option>
              <option value="false">停用</option>
            </select>
          </div>
        </div>
        <div class="mt-4">
          <button
            @click="searchTypes"
            class="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 transition-colors"
          >
            搜索
          </button>
        </div>
      </div>

      <!-- 布面纸类型列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <h3 class="text-lg font-medium text-gray-900">布面纸类型列表</h3>
            <div class="flex items-center space-x-2">
              <span class="text-sm text-gray-500">已选择 {{ selectedItems.length }} 项</span>
              <button
                @click="selectAll"
                class="text-sm text-indigo-600 hover:text-indigo-800"
              >
                全选
              </button>
              <button
                @click="clearSelection"
                class="text-sm text-gray-500 hover:text-gray-700"
              >
                清空
              </button>
            </div>
          </div>
        </div>
        
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left">
                  <input
                    type="checkbox"
                    :checked="isAllSelected"
                    @change="toggleSelectAll"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">排序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">类型信息</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="type in filteredTypes" :key="type.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <input
                    type="checkbox"
                    :checked="selectedItems.includes(type.id)"
                    @change="toggleSelection(type.id)"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center space-x-2">
                    <input
                      v-model.number="type.sortOrder"
                      type="number"
                      min="1"
                      @blur="updateSortOrder(type)"
                      class="w-16 px-2 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-1 focus:ring-indigo-500"
                    />
                    <button
                      @click="moveUp(type)"
                      class="text-gray-400 hover:text-gray-600"
                      :disabled="type.sortOrder <= 1"
                    >
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 15l7-7 7 7"></path>
                      </svg>
                    </button>
                    <button
                      @click="moveDown(type)"
                      class="text-gray-400 hover:text-gray-600"
                    >
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                      </svg>
                    </button>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div>
                    <div class="text-sm font-medium text-gray-900">{{ type.typeName }}</div>
                    <div class="text-sm text-gray-500">{{ type.category || '未分类' }}</div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                      type.isActive
                        ? 'bg-green-100 text-green-800'
                        : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ type.isActive ? '激活' : '停用' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ formatDate(type.createdAt) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                     <button
                       @click="editType(type)"
                       class="text-indigo-600 hover:text-indigo-900"
                     >
                       编辑
                     </button>
                     <button
                       @click="openMappingDialog(type)"
                       class="text-green-600 hover:text-green-900"
                     >
                       配置映射
                     </button>
                     <button
                       @click="toggleStatus(type)"
                       :class="[
                         'hover:underline',
                         type.isActive ? 'text-red-600 hover:text-red-900' : 'text-green-600 hover:text-green-900'
                       ]"
                     >
                       {{ type.isActive ? '停用' : '激活' }}
                     </button>
                     <button
                       @click="deleteType(type.id)"
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
        <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
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
                显示第 <span class="font-medium">{{ (currentPage - 1) * pageSize + 1 }}</span> 到
                <span class="font-medium">{{ Math.min(currentPage * pageSize, totalItems) }}</span> 条，
                共 <span class="font-medium">{{ totalItems }}</span> 条记录
              </p>
            </div>
            <div>
              <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
                <button
                  @click="previousPage"
                  :disabled="currentPage === 1"
                  class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                >
                  上一页
                </button>
                <button
                  v-for="page in visiblePages"
                  :key="page"
                  @click="goToPage(page)"
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
                  @click="nextPage"
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

    <!-- 映射配置对话框 -->
    <div v-if="showMappingDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-6xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              配置映射 - {{ selectedClothPaperType?.typeName }}
            </h3>
            <button
              @click="closeMappingDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <!-- 搜索和筛选 -->
          <div class="bg-white rounded-lg shadow-md p-6 mb-6">
            <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列</label>
                <div class="relative">
                  <input
                    type="text"
                    v-model="mappingProductSearchText"
                    @input="onMappingProductSearchInput"
                    @focus="showMappingProductDropdown = true"
                    @blur="handleMappingProductInputBlur"
                    placeholder="请输入或选择燙金紙系列"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                  />
                  <div
                    v-if="showMappingProductDropdown && availableProducts.length > 0"
                    class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto"
                  >
                    <div
                      v-for="product in filteredMappingProducts"
                      :key="product"
                      @mousedown.prevent="selectMappingProduct(product)"
                      class="px-3 py-2 cursor-pointer hover:bg-blue-50"
                      :class="{ 'bg-blue-100': mappingSearchForm.productName === product }"
                    >
                      <div class="text-sm text-gray-900">{{ product }}</div>
                    </div>
                    <div v-if="filteredMappingProducts.length === 0" class="px-3 py-2 text-sm text-gray-500">
                      未找到匹配的燙金紙系列
                    </div>
                  </div>
                </div>
                <p v-if="mappingSearchForm.productName" class="mt-1 text-xs text-gray-500">
                  已选择：{{ mappingSearchForm.productName }}
                </p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                <select
                  v-model="mappingSearchForm.paperType"
                  :disabled="!mappingSearchForm.productName"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                >
                  <option value="">
                    {{ !mappingSearchForm.productName ? '请先选择燙金紙系列' : '全部烫金纸性能类型' }}
                  </option>
                  <option v-for="paperType in filteredPaperTypes" :key="paperType" :value="paperType">
                    {{ paperType }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
                <select
                  v-model="mappingSearchForm.compatibilityStatus"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">全部状态</option>
                  <option value="V">适用</option>
                  <option value="X">不适用</option>
                  <option value="NA">不确定</option>
                  <option value="▷">需要打底处理</option>
                </select>
              </div>
              <div class="flex items-end">
                <button
                  @click="searchMappingConfigs"
                  class="w-full bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 transition-colors"
                >
                  搜索
                </button>
              </div>
              <div class="flex items-end">
                <button
                  @click="resetMappingSearch"
                  class="w-full bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-colors"
                >
                  重置
                </button>
              </div>
            </div>
          </div>

          <!-- 映射配置表格 -->
          <div class="bg-white rounded-lg shadow-md overflow-hidden">
            <div class="px-6 py-4 border-b border-gray-200">
              <div class="flex items-center justify-between">
                <h4 class="text-md font-medium text-gray-900">产品兼容性配置</h4>
                <div class="flex space-x-2">
                  <button
                    @click="openAddMappingDialog"
                    class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-green-600 hover:bg-green-700"
                  >
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                    </svg>
                    添加配置
                  </button>
                  <button
                    @click="openBatchAddDialog"
                    class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700"
                  >
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                    </svg>
                    批量添加
                  </button>
                  <button
                    @click="exportMappings"
                    class="inline-flex items-center px-3 py-2 border border-gray-300 text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
                  >
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                    </svg>
                    导出
                  </button>
                  <button
                    @click="openImportDialog"
                    class="inline-flex items-center px-3 py-2 border border-gray-300 text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50"
                  >
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path>
                    </svg>
                    导入
                  </button>
                </div>
              </div>
            </div>

          <!-- 批量操作工具栏 -->
          <div v-if="selectedMappingItems.length > 0" class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-4">
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-4">
                <span class="text-sm font-medium text-blue-900">
                  已选择 <span class="font-bold">{{ selectedMappingItems.length }}</span> 项
                </span>
                <button
                  @click="clearMappingSelection"
                  class="text-sm text-blue-600 hover:text-blue-800"
                >
                  清空选择
                </button>
              </div>
              <div class="flex items-center space-x-2">
                <button
                  @click="openBatchEditStatusDialog"
                  class="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-yellow-600 hover:bg-yellow-700"
                >
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                  </svg>
                  批量修改状态
                </button>
                <button
                  @click="openBatchCopyDialog"
                  class="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700"
                >
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7h11M8 12h11M8 17h11M5 7h.01M5 12h.01M5 17h.01"></path>
                  </svg>
                  批量复制
                </button>
                <button
                  @click="batchDeleteMappings"
                  class="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700"
                >
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                  </svg>
                  批量删除
                </button>
              </div>
            </div>
          </div>
            
            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      <input
                        type="checkbox"
                        :checked="isAllMappingSelected"
                        @change="toggleSelectAllMappings"
                        class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                      />
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙系列</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="mapping in filteredMappingConfigs" :key="mapping.id" class="hover:bg-gray-50">
                    <td class="px-6 py-4 whitespace-nowrap">
                      <input
                        type="checkbox"
                        :checked="selectedMappingItems.includes(mapping.id)"
                        @change="toggleMappingSelection(mapping.id)"
                        class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                      />
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="text-sm font-medium text-gray-900">{{ mapping.productName }}</div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="text-sm text-gray-900">{{ mapping.paperType || '-' }}</div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span
                        :class="[
                          'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                          mapping.compatibilityStatus === 'V'
                            ? 'bg-green-100 text-green-800'
                            : mapping.compatibilityStatus === 'X'
                            ? 'bg-red-100 text-red-800'
                            : mapping.compatibilityStatus === 'NA'
                            ? 'bg-yellow-100 text-yellow-800'
                            : 'bg-blue-100 text-blue-800'
                        ]"
                      >
                        {{ mapping.compatibilityStatus === 'V' ? '适用' : mapping.compatibilityStatus === 'X' ? '不适用' : mapping.compatibilityStatus === 'NA' ? '不确定' : '需要打底处理' }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div class="flex space-x-2">
                        <button
                          @click="copyMapping(mapping)"
                          class="text-green-600 hover:text-green-900"
                          title="复制此映射"
                        >
                          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
                          </svg>
                        </button>
                        <button
                          @click="editMapping(mapping)"
                          class="text-blue-600 hover:text-blue-900"
                          title="编辑"
                        >
                          编辑
                        </button>
                        <button
                          @click="deleteMapping(mapping.id)"
                          class="text-red-600 hover:text-red-900"
                          title="删除"
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
            <div class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
              <div class="flex-1 flex justify-between sm:hidden">
                <button
                  @click="previousMappingPage"
                  :disabled="mappingCurrentPage === 1"
                  class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  上一页
                </button>
                <button
                  @click="nextMappingPage"
                  :disabled="mappingCurrentPage === mappingTotalPages"
                  class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  下一页
                </button>
              </div>
              <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
                <div>
                  <p class="text-sm text-gray-700">
                    显示第
                    <span class="font-medium">{{ (mappingCurrentPage - 1) * mappingPageSize + 1 }}</span>
                    到
                    <span class="font-medium">{{ Math.min(mappingCurrentPage * mappingPageSize, mappingTotalItems) }}</span>
                    条，共
                    <span class="font-medium">{{ mappingTotalItems }}</span>
                    条记录
                  </p>
                </div>
                <div>
                  <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px" aria-label="Pagination">
                    <button
                      @click="previousMappingPage"
                      :disabled="mappingCurrentPage === 1"
                      class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      上一页
                    </button>
                    <button
                      v-for="page in mappingVisiblePages"
                      :key="page"
                      @click="goToMappingPage(page)"
                      :class="[
                        page === mappingCurrentPage
                          ? 'z-10 bg-indigo-50 border-indigo-500 text-indigo-600'
                          : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50',
                        'relative inline-flex items-center px-4 py-2 border text-sm font-medium'
                      ]"
                    >
                      {{ page }}
                    </button>
                    <button
                      @click="nextMappingPage"
                      :disabled="mappingCurrentPage === mappingTotalPages"
                      class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      下一页
                    </button>
                  </nav>
                </div>
              </div>
            </div>
          </div>

          <!-- 添加新映射对话框 -->
          <div v-if="showAddMappingDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
            <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
              <div class="mt-3">
                <div class="flex items-center justify-between mb-4">
                  <h3 class="text-lg font-medium text-gray-900">
                    {{ editingMappingId ? '编辑兼容性配置' : '添加新的兼容性配置' }}
                  </h3>
                  <button
                    @click="closeAddMappingDialog"
                    class="text-gray-400 hover:text-gray-600"
                  >
                    <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                    </svg>
                  </button>
                </div>
                
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列 *</label>
                    <div class="relative">
                      <input
                        type="text"
                        v-model="newMappingProductSearchText"
                        @input="onNewMappingProductSearchInput"
                        @focus="showNewMappingProductDropdown = true"
                        @blur="handleNewMappingProductInputBlur"
                        :placeholder="availableProducts.length === 0 ? '加载中...' : '请输入或选择燙金紙系列'"
                        required
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      />
                      <div
                        v-if="showNewMappingProductDropdown && availableProducts.length > 0"
                        class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto"
                      >
                        <div
                          v-for="product in filteredNewMappingProducts"
                          :key="product"
                          @mousedown.prevent="selectNewMappingProduct(product)"
                          class="px-3 py-2 cursor-pointer hover:bg-blue-50"
                          :class="{ 'bg-blue-100': newMapping.productName === product }"
                        >
                          <div class="text-sm text-gray-900">{{ product }}</div>
                        </div>
                        <div v-if="filteredNewMappingProducts.length === 0" class="px-3 py-2 text-sm text-gray-500">
                          未找到匹配的燙金紙系列
                        </div>
                      </div>
                    </div>
                    <p v-if="newMapping.productName" class="mt-1 text-xs text-gray-500">
                      已选择：{{ newMapping.productName }}
                    </p>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                    <select
                      v-model="newMapping.paperType"
                      :disabled="!newMapping.productName || loadingPaperTypes"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                    >
                      <option value="">
                        {{ loadingPaperTypes ? '加载中...' : !newMapping.productName ? '请先选择燙金紙系列' : '请选择烫金纸性能类型' }}
                      </option>
                      <option v-for="paperType in newMappingPaperTypes" :key="paperType" :value="paperType">
                        {{ paperType }}
                      </option>
                    </select>
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
                    <select
                      v-model="newMapping.compatibilityStatus"
                      required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    >
                      <option value="">请选择兼容性状态</option>
                      <option value="V">适用</option>
                      <option value="X">不适用</option>
                      <option value="NA">不确定</option>
                      <option value="▷">需要打底处理</option>
                    </select>
                  </div>
                </div>
                
                <div class="mt-6 flex justify-end space-x-3">
                  <button
                    @click="closeAddMappingDialog"
                    class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                  >
                    取消
                  </button>
                  <button
                    @click="addMapping"
                    class="px-4 py-2 border border-transparent rounded-md text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
                  >
                    {{ editingMappingId ? '保存配置' : '添加配置' }}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>


    <!-- 添加/编辑布面纸类型对话框 -->
    <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              {{ showAddDialog ? '添加布面纸类型' : '编辑布面纸类型' }}
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

          <form @submit.prevent="saveType" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">类型名称 *</label>
                <input
                  v-model="typeForm.typeName"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">分类</label>
                <input
                  v-model="typeForm.category"
                  type="text"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">排序顺序</label>
                <input
                  v-model.number="typeForm.sortOrder"
                  type="number"
                  min="1"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
                <select
                  v-model="typeForm.isActive"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option :value="true">激活</option>
                  <option :value="false">停用</option>
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

    <!-- 批量添加对话框 -->
    <div v-if="showBatchAddDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">批量添加兼容性配置</h3>
            <button
              @click="closeBatchAddDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          
          <form @submit.prevent="batchAddMappings">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型 *</label>
                <select
                  v-model="batchAddForm.paperType"
                  @change="onBatchAddPaperTypeChange"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">请先选择烫金纸性能类型</option>
                  <option v-for="paperType in availablePaperTypes" :key="paperType" :value="paperType">
                    {{ paperType }}
                  </option>
                </select>
                <p class="mt-1 text-xs text-gray-500">请先选择烫金纸性能类型，系统将只显示支持该类型的产品系列</p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
                <select
                  v-model="batchAddForm.compatibilityStatus"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="V">适用</option>
                  <option value="X">不适用</option>
                  <option value="NA">不确定</option>
                  <option value="▷">需要打底处理</option>
                </select>
              </div>
            </div>

            <div class="mt-4">
              <label class="block text-sm font-medium text-gray-700 mb-2">选择产品系列（可多选）*</label>
              <p v-if="!batchAddForm.paperType" class="mb-2 text-sm text-yellow-600">
                请先选择烫金纸性能类型
              </p>
              <!-- 搜索输入框 -->
              <div class="mb-2">
                <input
                  type="text"
                  v-model="batchAddSeriesSearchText"
                  placeholder="搜索产品系列..."
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
                      class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                    />
                    <span class="text-sm text-gray-700">{{ series }}</span>
                  </label>
                  <div v-if="filteredBatchAddSeries.length === 0" class="px-2 py-4 text-sm text-gray-500 text-center">
                    {{ !batchAddForm.paperType ? '请先选择烫金纸性能类型' : '未找到匹配的产品系列' }}
                  </div>
                </div>
              </div>
              <p class="mt-2 text-xs text-gray-500">
                已选择 <span class="font-bold text-indigo-600">{{ batchAddForm.selectedSeries.length }}</span> 个产品系列
                <span v-if="batchAddSeriesSearchText" class="ml-2">
                  （显示 {{ filteredBatchAddSeries.length }} 个匹配项）
                </span>
              </p>
            </div>

            <div class="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                @click="closeBatchAddDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
              <button
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
              >
                批量添加
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
            <h3 class="text-lg font-medium text-gray-900">导入兼容性配置</h3>
            <button
              @click="closeImportDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">选择Excel文件</label>
              <input
                ref="importFileInput"
                type="file"
                accept=".xlsx,.xls"
                @change="handleFileSelect"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
              <p class="mt-1 text-sm text-gray-500">支持 .xlsx 和 .xls 格式</p>
            </div>
            
            <div class="flex items-center space-x-2">
              <button
                @click="downloadImportTemplate"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                下载模板
              </button>
            </div>
            
            <div v-if="importResult" class="mt-4">
              <div :class="[
                'p-4 rounded-md',
                importResult.success ? 'bg-green-50 text-green-800' : 'bg-red-50 text-red-800'
              ]">
                <p class="font-medium">{{ importResult.message }}</p>
                <div v-if="importResult.details && importResult.details.length > 0" class="mt-2">
                  <p class="text-sm font-medium">错误详情：</p>
                  <ul class="list-disc list-inside text-sm mt-1">
                    <li v-for="(detail, index) in importResult.details" :key="index">{{ detail }}</li>
                  </ul>
                </div>
              </div>
            </div>
          </div>

          <div class="flex justify-end space-x-3 pt-4">
            <button
              @click="closeImportDialog"
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              关闭
            </button>
            <button
              @click="importMappings"
              :disabled="!selectedFile"
              class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              开始导入
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 查看所有配置映射对话框 -->
    <div v-if="showAllMappingsDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[56]">
      <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-7xl shadow-lg rounded-md bg-white my-8">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">查看所有配置映射</h3>
            <button
              @click="closeAllMappingsDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <!-- 筛选区域 -->
          <div class="bg-gray-50 rounded-lg p-4 mb-4">
            <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">布面纸类型</label>
                <input
                  v-model="allMappingsSearchForm.typeName"
                  type="text"
                  placeholder="输入布面纸类型进行筛选"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列</label>
                <input
                  v-model="allMappingsSearchForm.productName"
                  type="text"
                  placeholder="输入燙金紙系列进行筛选"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                <input
                  v-model="allMappingsSearchForm.paperType"
                  type="text"
                  placeholder="输入烫金纸性能类型进行筛选"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
                <select
                  v-model="allMappingsSearchForm.compatibilityStatus"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="">全部状态</option>
                  <option value="V">适用</option>
                  <option value="X">不适用</option>
                  <option value="NA">不确定</option>
                  <option value="▷">需要打底处理</option>
                </select>
              </div>
              <div class="flex items-end">
                <button
                  @click="searchAllMappings"
                  class="w-full px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
                >
                  搜索
                </button>
              </div>
            </div>
            <div class="mt-4 flex justify-end">
              <button
                @click="resetAllMappingsSearch"
                class="ml-2 px-4 py-2 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 transition-colors"
              >
                重置
              </button>
            </div>
          </div>

          <!-- 批量操作工具栏 -->
          <div v-if="selectedAllMappingsItems.length > 0" class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-4">
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-4">
                <span class="text-sm font-medium text-blue-900">
                  已选择 <span class="font-bold">{{ selectedAllMappingsItems.length }}</span> 项
                </span>
                <button
                  @click="clearAllMappingsSelection"
                  class="text-sm text-blue-600 hover:text-blue-800"
                >
                  清空选择
                </button>
              </div>
              <div class="flex items-center space-x-2">
                <button
                  @click="openAllMappingsBatchEditDialog"
                  class="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-yellow-600 hover:bg-yellow-700"
                >
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
                  </svg>
                  批量修改
                </button>
                <button
                  @click="openAllMappingsBatchCopyDialog"
                  class="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700"
                >
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7h11M8 12h11M8 17h11M5 7h.01M5 12h.01M5 17h.01"></path>
                  </svg>
                  批量复制
                </button>
                <button
                  @click="batchDeleteAllMappings"
                  class="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-red-600 hover:bg-red-700"
                >
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                  </svg>
                  批量删除
                </button>
              </div>
            </div>
          </div>

          <!-- 映射列表 -->
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-6 py-3 text-left">
                    <input
                      type="checkbox"
                      :checked="isAllMappingsSelected"
                      @change="toggleSelectAllAllMappings"
                      class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                    />
                  </th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">布面纸类型</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">布面纸分类</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙系列</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="mapping in filteredAllMappings" :key="mapping.id" class="hover:bg-gray-50">
                  <td class="px-6 py-4 whitespace-nowrap">
                    <input
                      type="checkbox"
                      :checked="selectedAllMappingsItems.includes(mapping.id)"
                      @change="toggleAllMappingsSelection(mapping.id)"
                      class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                    />
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.typeName || '-' }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.category || '-' }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.productName }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.paperType || '-' }}</td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span
                      :class="[
                        'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                        mapping.compatibilityStatus === 'V'
                          ? 'bg-green-100 text-green-800'
                          : mapping.compatibilityStatus === 'X'
                          ? 'bg-red-100 text-red-800'
                          : mapping.compatibilityStatus === 'NA'
                          ? 'bg-yellow-100 text-yellow-800'
                          : 'bg-blue-100 text-blue-800'
                      ]"
                    >
                      {{ mapping.compatibilityStatus === 'V' ? '适用' : mapping.compatibilityStatus === 'X' ? '不适用' : mapping.compatibilityStatus === 'NA' ? '不确定' : '需要打底处理' }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button
                      @click="viewMappingDetails(mapping)"
                      class="text-blue-600 hover:text-blue-900 mr-2"
                    >
                      查看详情
                    </button>
                  </td>
                </tr>
                <tr v-if="filteredAllMappings.length === 0">
                  <td colspan="7" class="px-6 py-4 text-center text-sm text-gray-500">
                    暂无数据
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- 分页 -->
          <div v-if="allMappingsTotalItems > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6 mt-4">
            <div class="flex-1 flex justify-between sm:hidden">
              <button
                @click="allMappingsCurrentPage = Math.max(1, allMappingsCurrentPage - 1)"
                :disabled="allMappingsCurrentPage === 1"
                class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
              >
                上一页
              </button>
              <button
                @click="allMappingsCurrentPage = Math.min(allMappingsTotalPages, allMappingsCurrentPage + 1)"
                :disabled="allMappingsCurrentPage === allMappingsTotalPages"
                class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
              >
                下一页
              </button>
            </div>
            <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
              <div>
                <p class="text-sm text-gray-700">
                  显示第 <span class="font-medium">{{ (allMappingsCurrentPage - 1) * allMappingsPageSize + 1 }}</span> 到
                  <span class="font-medium">{{ Math.min(allMappingsCurrentPage * allMappingsPageSize, allMappingsTotalItems) }}</span> 条，
                  共 <span class="font-medium">{{ allMappingsTotalItems }}</span> 条记录
                </p>
              </div>
              <div>
                <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
                  <button
                    @click="allMappingsCurrentPage = Math.max(1, allMappingsCurrentPage - 1)"
                    :disabled="allMappingsCurrentPage === 1"
                    class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                  >
                    上一页
                  </button>
                  <button
                    v-for="page in allMappingsVisiblePages"
                    :key="page"
                    @click="allMappingsCurrentPage = page"
                    :class="[
                      'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                      page === allMappingsCurrentPage
                        ? 'z-10 bg-blue-50 border-blue-500 text-blue-600'
                        : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'
                    ]"
                  >
                    {{ page }}
                  </button>
                  <button
                    @click="allMappingsCurrentPage = Math.min(allMappingsTotalPages, allMappingsCurrentPage + 1)"
                    :disabled="allMappingsCurrentPage === allMappingsTotalPages"
                    class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                  >
                    下一页
                  </button>
                </nav>
              </div>
            </div>
          </div>

          <div class="flex justify-end space-x-3 pt-4 mt-4">
            <button
              @click="closeAllMappingsDialog"
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              关闭
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 查看所有配置映射 - 批量修改对话框 -->
    <div v-if="showAllMappingsBatchEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[57]">
      <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white my-8">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">批量修改映射 (已选择 {{ selectedAllMappingsItems.length }} 条)</h3>
            <button
              @click="closeAllMappingsBatchEditDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <form @submit.prevent="batchUpdateAllMappings" class="space-y-6">
            <div class="bg-yellow-50 border-l-4 border-yellow-400 p-4 mb-4">
              <p class="text-sm text-yellow-800">
                <strong>提示：</strong>只填写需要批量修改的字段，留空的字段将不会被修改。支持同时修改多个字段。
              </p>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列</label>
                <input
                  v-model="allMappingsBatchForm.productName"
                  type="text"
                  list="all-mappings-batch-product-options"
                  placeholder="留空则不修改（支持输入关键字选择系统已有系列）"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
                <datalist id="all-mappings-batch-product-options">
                  <option v-for="product in availableProducts" :key="product" :value="product" />
                </datalist>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                <input
                  v-model="allMappingsBatchForm.paperType"
                  type="text"
                  list="all-mappings-batch-paper-type-options"
                  placeholder="留空则不修改（支持输入关键字选择系统已有类型）"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
                <datalist id="all-mappings-batch-paper-type-options">
                  <option v-for="pt in availablePaperTypes" :key="pt" :value="pt" />
                </datalist>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
                <select
                  v-model="allMappingsBatchForm.compatibilityStatus"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                >
                  <option value="">不修改</option>
                  <option value="V">适用</option>
                  <option value="X">不适用</option>
                  <option value="NA">不确定</option>
                  <option value="▷">需要打底处理</option>
                </select>
              </div>
            </div>

            <div class="flex justify-end space-x-3 pt-4 border-t">
              <button
                type="button"
                @click="closeAllMappingsBatchEditDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
              <button
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-yellow-600 hover:bg-yellow-700"
              >
                确认修改
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 查看所有配置映射 - 批量复制对话框 -->
    <div v-if="showAllMappingsBatchCopyDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[57]">
      <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white my-8">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">批量复制映射 (已选择 {{ selectedAllMappingsItems.length }} 条)</h3>
            <button
              @click="closeAllMappingsBatchCopyDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <form @submit.prevent="confirmAllMappingsBatchCopy" class="space-y-6">
            <div class="bg-blue-50 border-l-4 border-blue-400 p-4 mb-4">
              <p class="text-sm text-blue-800">
                <strong>提示：</strong>将复制选中的 {{ selectedAllMappingsItems.length }} 条配置映射。
                您可以选择修改「目标布面纸类型」「燙金紙系列」「烫金纸性能类型」「兼容性状态」，未填写的字段将保持原值。
              </p>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <!-- 目标布面纸类型 -->
              <div class="md:col-span-2">
                <label class="block text-sm font-medium text-gray-700 mb-2">目标布面纸类型（可选修改）</label>
                <select
                  v-model="allMappingsCopyForm.targetClothPaperTypeId"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option :value="null">保持原布面纸类型</option>
                  <option v-for="type in types" :key="type.id" :value="type.id">
                    {{ type.typeName }}（{{ type.category || '未分类' }}）
                  </option>
                </select>
              </div>

              <!-- 燙金紙系列 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列（可选修改）</label>
                <input
                  v-model="allMappingsCopyForm.productName"
                  type="text"
                  list="all-mappings-copy-product-options"
                  placeholder="留空保持原值（支持输入关键字选择系统已有系列）"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <datalist id="all-mappings-copy-product-options">
                  <option v-for="product in availableProducts" :key="product" :value="product" />
                </datalist>
              </div>

              <!-- 烫金纸性能类型 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型（可选修改）</label>
                <input
                  v-model="allMappingsCopyForm.paperType"
                  type="text"
                  list="all-mappings-copy-paper-type-options"
                  placeholder="留空保持原值（支持输入关键字选择系统已有类型）"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <datalist id="all-mappings-copy-paper-type-options">
                  <option v-for="pt in availablePaperTypes" :key="pt" :value="pt" />
                </datalist>
              </div>

              <!-- 兼容性状态 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态（可选修改）</label>
                <select
                  v-model="allMappingsCopyForm.compatibilityStatus"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="">保持原值</option>
                  <option value="V">适用</option>
                  <option value="X">不适用</option>
                  <option value="NA">不确定</option>
                  <option value="▷">需要打底处理</option>
                </select>
              </div>
            </div>

            <div class="flex justify-end space-x-3 pt-4 border-t">
              <button
                type="button"
                @click="closeAllMappingsBatchCopyDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
              <button
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
              >
                确认复制
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 批量复制对话框（单个布面纸类型下的映射） -->
    <div v-if="showBatchCopyDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              批量复制映射 (已选择 {{ selectedMappingItems.length }} 条)
            </h3>
            <button
              @click="closeBatchCopyDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <form @submit.prevent="confirmBatchCopy" class="space-y-4">
            <div class="bg-blue-50 border border-blue-200 rounded-md p-3 mb-4">
              <p class="text-sm text-blue-800">
                <strong>提示：</strong>将复制选中的 {{ selectedMappingItems.length }} 条配置映射。
                您可以选择修改「目标布面纸类型」「燙金紙系列」「烫金纸性能类型」，未填写的字段将保持原值。
              </p>
            </div>

            <div class="space-y-4">
              <!-- 目标布面纸类型 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">目标布面纸类型（可选修改）</label>
                <select
                  v-model="batchCopyForm.targetClothPaperTypeId"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option :value="null">保持原布面纸类型</option>
                  <option v-for="type in types" :key="type.id" :value="type.id">
                    {{ type.typeName }}（{{ type.category || '未分类' }}）
                  </option>
                </select>
              </div>

              <!-- 燙金紙系列 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列（可选修改）</label>
                <input
                  v-model="batchCopyForm.productName"
                  type="text"
                  placeholder="留空保持原值"
                  list="applicable-interface-batch-copy-product-options"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <datalist id="applicable-interface-batch-copy-product-options">
                  <option v-for="product in availableProducts" :key="product" :value="product" />
                </datalist>
              </div>

              <!-- 烫金纸性能类型 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型（可选修改）</label>
                <input
                  v-model="batchCopyForm.paperType"
                  type="text"
                  placeholder="留空保持原值"
                  list="applicable-interface-batch-copy-paper-type-options"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <datalist id="applicable-interface-batch-copy-paper-type-options">
                  <option v-for="pt in availablePaperTypes" :key="pt" :value="pt" />
                </datalist>
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
            </div>

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

    <!-- 批量修改状态对话框 -->
    <div v-if="showBatchEditStatusDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-md shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">批量修改兼容性状态</h3>
            <button
              @click="closeBatchEditStatusDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          
          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
              <select
                v-model="batchStatusForm.compatibilityStatus"
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
              >
                <option value="V">适用</option>
                <option value="X">不适用</option>
                <option value="NA">不确定</option>
                <option value="▷">需要打底处理</option>
              </select>
            </div>
            <p class="text-sm text-gray-500">将为选中的 {{ selectedMappingItems.length }} 个映射设置相同的兼容性状态</p>
          </div>

          <div class="flex justify-end space-x-3 pt-4">
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
              确认修改
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'

// 定义接口
interface ClothPaperType {
  id: number
  typeName: string
  category: string
  sortOrder: number
  isActive: boolean
  createdAt: string
  updatedAt: string
}

// 响应式数据
const types = ref<ClothPaperType[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showMappingDialog = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)
const selectedItems = ref<number[]>([])

// 映射配置相关数据
const selectedClothPaperType = ref<ClothPaperType | null>(null)
const mappingConfigs = ref<any[]>([])
const availableProducts = ref<string[]>([])
const newMapping = reactive({
  productName: '',
  paperType: '',
  compatibilityStatus: 'NA'
})
const editingMappingId = ref<number | null>(null)

// 映射配置搜索和分页相关数据
const mappingSearchForm = reactive({
  productName: '',
  paperType: '',
  compatibilityStatus: ''
})
const mappingCurrentPage = ref(1)
const mappingPageSize = ref(10)
const mappingTotalItems = ref(0)
const showAddMappingDialog = ref(false)
const selectedMappingItems = ref<number[]>([])
const showBatchCopyDialog = ref(false)

// 批量复制表单
const batchCopyForm = reactive({
  targetClothPaperTypeId: null as number | null,
  productName: '',
  paperType: '',
  compatibilityStatus: '' as '' | 'V' | 'X' | 'NA' | '▷'
})

// 配置映射对话框中的产品系列搜索
const mappingProductSearchText = ref('')
const showMappingProductDropdown = ref(false)

// 批量添加相关数据
const showBatchAddDialog = ref(false)
const batchAddForm = reactive({
  selectedSeries: [] as string[],
  selectedProducts: {} as Record<string, number[]>, // 每个产品系列对应的产品ID列表
  paperType: '',
  compatibilityStatus: 'NA'
})

// 批量添加时每个产品系列的产品列表
const batchAddSeriesProducts = ref<Record<string, Array<{ id: number, modelNumber: string, name: string }>>>({})

// 批量添加对话框中支持指定烫金纸性能类型的产品系列
const batchAddAvailableSeries = ref<string[]>([])

// 批量添加对话框中的产品系列搜索
const batchAddSeriesSearchText = ref('')

// 导入/导出相关数据
const showImportDialog = ref(false)
const selectedFile = ref<File | null>(null)
const importFileInput = ref<HTMLInputElement | null>(null)
const importResult = ref<{ success: boolean; message: string; details?: string[] } | null>(null)

// 批量修改状态相关数据
const showBatchEditStatusDialog = ref(false)
const batchStatusForm = reactive({
  compatibilityStatus: 'NA'
})

// 烫金纸性能类型相关数据
const availablePaperTypes = ref<string[]>([])
const newMappingPaperTypes = ref<string[]>([])
const filteredPaperTypes = ref<string[]>([])
const loadingPaperTypes = ref(false)

// 新映射对话框中的产品搜索相关数据
const newMappingProductSearchText = ref('')
const showNewMappingProductDropdown = ref(false)

// 搜索表单
const searchForm = reactive({
  typeName: '',
  category: '',
  status: ''
})

// 布面纸类型表单
const typeForm = reactive({
  id: null,
  typeName: '',
  category: '',
  sortOrder: null,
  isActive: true
})

// 计算属性
const totalPages = computed(() => Math.ceil(totalItems.value / pageSize.value))

const visiblePages = computed(() => {
  const pages = []
  const start = Math.max(1, currentPage.value - 2)
  const end = Math.min(totalPages.value, start + 4)
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

const categories = computed(() => {
  const categorySet = new Set<string>()
  types.value.forEach(type => {
    if (type.category) {
      categorySet.add(type.category)
    }
  })
  return Array.from(categorySet).sort()
})

const filteredTypes = computed(() => {
  let filtered = types.value

  if (searchForm.typeName) {
    filtered = filtered.filter(type => 
      type.typeName.toLowerCase().includes(searchForm.typeName.toLowerCase())
    )
  }

  if (searchForm.category) {
    filtered = filtered.filter(type => type.category === searchForm.category)
  }

  if (searchForm.status !== '') {
    const isActive = searchForm.status === 'true'
    filtered = filtered.filter(type => type.isActive === isActive)
  }

  // 按排序顺序排序
  filtered.sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))

  // 分页
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  totalItems.value = filtered.length
  return filtered.slice(start, end)
})

const isAllSelected = computed(() => {
  return filteredTypes.value.length > 0 && 
         filteredTypes.value.every(type => selectedItems.value.includes(type.id))
})

// 配置映射对话框中的产品系列过滤
const filteredMappingProducts = computed(() => {
  if (!mappingProductSearchText.value || mappingProductSearchText.value.trim() === '') {
    return availableProducts.value
  }
  
  const searchText = mappingProductSearchText.value.toLowerCase().trim()
  return availableProducts.value.filter(product => 
    product.toLowerCase().includes(searchText)
  )
})

// 映射配置筛选和分页计算属性
const filteredMappingConfigs = computed(() => {
  let filtered = mappingConfigs.value

  if (mappingSearchForm.productName) {
    // 改为模糊匹配
    const searchText = mappingSearchForm.productName.toLowerCase().trim()
    filtered = filtered.filter(mapping => 
      mapping.productName && mapping.productName.toLowerCase().includes(searchText)
    )
  }

  if (mappingSearchForm.paperType) {
    filtered = filtered.filter(mapping => 
      mapping.paperType === mappingSearchForm.paperType
    )
  }

  if (mappingSearchForm.compatibilityStatus) {
    filtered = filtered.filter(mapping => 
      mapping.compatibilityStatus === mappingSearchForm.compatibilityStatus
    )
  }

  // 分页
  const start = (mappingCurrentPage.value - 1) * mappingPageSize.value
  const end = start + mappingPageSize.value
  mappingTotalItems.value = filtered.length
  return filtered.slice(start, end)
})

const mappingTotalPages = computed(() => {
  return Math.ceil(mappingTotalItems.value / mappingPageSize.value)
})

const mappingVisiblePages = computed(() => {
  const total = mappingTotalPages.value
  const current = mappingCurrentPage.value
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

// 批量操作相关计算属性
const isAllMappingSelected = computed(() => {
  return filteredMappingConfigs.value.length > 0 && 
         filteredMappingConfigs.value.every(mapping => selectedMappingItems.value.includes(mapping.id))
})

// 新映射对话框中的产品搜索过滤
const filteredNewMappingProducts = computed(() => {
  if (!newMappingProductSearchText.value || newMappingProductSearchText.value.trim() === '') {
    return availableProducts.value
  }
  
  const searchText = newMappingProductSearchText.value.toLowerCase().trim()
  return availableProducts.value.filter(product => 
    product.toLowerCase().includes(searchText)
  )
})

// 批量添加对话框中的产品系列过滤（同时考虑烫金纸性能类型和搜索文本）
const filteredBatchAddSeries = computed(() => {
  // 如果没有选择烫金纸性能类型，返回空数组
  if (!batchAddForm.paperType) {
    return []
  }
  
  // 先根据烫金纸性能类型过滤
  let filtered = batchAddAvailableSeries.value
  
  // 再根据搜索文本过滤
  if (batchAddSeriesSearchText.value && batchAddSeriesSearchText.value.trim() !== '') {
    const searchText = batchAddSeriesSearchText.value.toLowerCase().trim()
    filtered = filtered.filter(series => series.toLowerCase().includes(searchText))
  }
  
  return filtered
})

// 方法
const loadTypes = async () => {
  loading.value = true
  try {
    const response = await fetch('/api/api/cloth-paper-types/all')
    if (response.ok) {
      types.value = await response.json()
    } else {
      console.error('加载布面纸类型失败')
    }
  } catch (error) {
    console.error('加载布面纸类型失败:', error)
  } finally {
    loading.value = false
  }
}

const searchTypes = () => {
  currentPage.value = 1
  clearSelection()
}

const editType = (type: any) => {
  Object.assign(typeForm, type)
  showEditDialog.value = true
}

const deleteType = async (id: number) => {
  if (confirm('确定要删除这个布面纸类型吗？')) {
    try {
      const response = await fetch(`/api/api/cloth-paper-types/${id}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        await loadTypes()
      } else {
        console.error('删除失败')
      }
    } catch (error) {
      console.error('删除布面纸类型失败:', error)
    }
  }
}

const toggleStatus = async (type: any) => {
  try {
    const response = await fetch(`/api/api/cloth-paper-types/${type.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...type,
        isActive: !type.isActive
      })
    })
    if (response.ok) {
      await loadTypes()
    } else {
      console.error('更新状态失败')
    }
  } catch (error) {
    console.error('更新状态失败:', error)
  }
}

const updateSortOrder = async (type: any) => {
  try {
    const response = await fetch(`/api/api/cloth-paper-types/${type.id}/order`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        sortOrder: type.sortOrder
      })
    })
    if (response.ok) {
      await loadTypes()
    } else {
      console.error('更新排序顺序失败')
    }
  } catch (error) {
    console.error('更新排序顺序失败:', error)
  }
}

const moveUp = async (type: any) => {
  if (type.sortOrder > 1) {
    type.sortOrder--
    await updateSortOrder(type)
  }
}

const moveDown = async (type: any) => {
  type.sortOrder++
  await updateSortOrder(type)
}

const saveType = async () => {
  try {
    const url = showAddDialog.value 
      ? '/api/api/cloth-paper-types'
      : `/api/api/cloth-paper-types/${typeForm.id}`
    
    const method = showAddDialog.value ? 'POST' : 'PUT'
    
    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(typeForm)
    })
    
    if (response.ok) {
      closeDialog()
      await loadTypes()
    } else {
      const errorData = await response.json().catch(() => ({}))
      const errorMessage = errorData.error || errorData.message || '保存失败'
      alert(errorMessage)
      console.error('保存失败:', errorData)
    }
  } catch (error) {
    console.error('保存布面纸类型失败:', error)
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(typeForm, {
    id: null,
    typeName: '',
    category: '',
    sortOrder: null,
    isActive: true
  })
}

const toggleSelection = (id: number) => {
  const index = selectedItems.value.indexOf(id)
  if (index > -1) {
    selectedItems.value.splice(index, 1)
  } else {
    selectedItems.value.push(id)
  }
}

const selectAll = () => {
  selectedItems.value = filteredTypes.value.map(type => type.id)
}

const clearSelection = () => {
  selectedItems.value = []
}

const toggleSelectAll = () => {
  if (isAllSelected.value) {
    clearSelection()
  } else {
    selectAll()
  }
}

const batchToggleStatus = async () => {
  if (selectedItems.value.length === 0) return
  
  const activeCount = types.value
    .filter(type => selectedItems.value.includes(type.id))
    .filter(type => type.isActive).length
  
  const newStatus = activeCount === selectedItems.value.length ? false : true
  
  try {
    const response = await fetch('/api/api/cloth-paper-types/batch-status', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ids: selectedItems.value,
        isActive: newStatus
      })
    })
    
    if (response.ok) {
      await loadTypes()
      clearSelection()
    } else {
      console.error('批量更新状态失败')
    }
  } catch (error) {
    console.error('批量更新状态失败:', error)
  }
}

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
  currentPage.value = page
}

const formatDate = (dateString: string) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 映射配置相关方法
const openMappingDialog = async (type: ClothPaperType) => {
  selectedClothPaperType.value = type
  showMappingDialog.value = true
  await loadMappingConfigs(type.id)
  await loadAvailableProducts()
  await loadAvailablePaperTypes()
}

const closeMappingDialog = () => {
  showMappingDialog.value = false
  selectedClothPaperType.value = null
  mappingConfigs.value = []
  Object.assign(newMapping, {
    productName: '',
    paperType: '',
    compatibilityStatus: 'NA'
  })
  // 重置搜索和分页状态
  mappingCurrentPage.value = 1
  selectedMappingItems.value = []
  Object.assign(mappingSearchForm, {
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
  mappingProductSearchText.value = ''
  showMappingProductDropdown.value = false
  filteredPaperTypes.value = []
}

// 映射配置搜索和分页方法
const searchMappingConfigs = () => {
  mappingCurrentPage.value = 1
}

const resetMappingSearch = () => {
  Object.assign(mappingSearchForm, {
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
  mappingProductSearchText.value = ''
  showMappingProductDropdown.value = false
  filteredPaperTypes.value = []
  mappingCurrentPage.value = 1
}

const previousMappingPage = () => {
  if (mappingCurrentPage.value > 1) {
    mappingCurrentPage.value--
  }
}

const nextMappingPage = () => {
  if (mappingCurrentPage.value < mappingTotalPages.value) {
    mappingCurrentPage.value++
  }
}

const goToMappingPage = (page: number) => {
  if (page >= 1 && page <= mappingTotalPages.value) {
    mappingCurrentPage.value = page
  }
}

// 添加映射对话框方法
const openAddMappingDialog = () => {
  showAddMappingDialog.value = true
  Object.assign(newMapping, {
    productName: '',
    paperType: '',
    compatibilityStatus: 'NA'
  })
  newMappingProductSearchText.value = ''
  showNewMappingProductDropdown.value = false
}

const closeAddMappingDialog = () => {
  showAddMappingDialog.value = false
  editingMappingId.value = null
  Object.assign(newMapping, {
    productName: '',
    paperType: '',
    compatibilityStatus: 'NA'
  })
  newMappingProductSearchText.value = ''
  showNewMappingProductDropdown.value = false
}

// 新映射对话框中的产品搜索相关方法
const onNewMappingProductSearchInput = () => {
  showNewMappingProductDropdown.value = true
}

const selectNewMappingProduct = (product: string) => {
  newMapping.productName = product
  newMappingProductSearchText.value = product
  showNewMappingProductDropdown.value = false
  onNewMappingProductNameChange()
}

const handleNewMappingProductInputBlur = () => {
  setTimeout(() => {
    showNewMappingProductDropdown.value = false
    // 如果输入框有值但没有匹配到产品，尝试精确匹配
    if (newMappingProductSearchText.value && !newMapping.productName) {
      const exactMatch = availableProducts.value.find(product => 
        product === newMappingProductSearchText.value.trim()
      )
      if (exactMatch) {
        selectNewMappingProduct(exactMatch)
      } else {
        // 如果没有精确匹配，清空输入
        newMappingProductSearchText.value = ''
      }
    } else if (!newMapping.productName) {
      // 如果没有选择产品，清空输入
      newMappingProductSearchText.value = ''
    } else {
      // 如果已选择产品，同步搜索文本
      newMappingProductSearchText.value = newMapping.productName
    }
  }, 200)
}

// 批量操作方法
const toggleMappingSelection = (mappingId: number) => {
  const index = selectedMappingItems.value.indexOf(mappingId)
  if (index > -1) {
    selectedMappingItems.value.splice(index, 1)
  } else {
    selectedMappingItems.value.push(mappingId)
  }
}

const toggleSelectAllMappings = () => {
  if (isAllMappingSelected.value) {
    selectedMappingItems.value = []
  } else {
    selectedMappingItems.value = filteredMappingConfigs.value.map(mapping => mapping.id)
  }
}

const batchDeleteMappings = async () => {
  if (selectedMappingItems.value.length === 0) {
    alert('请选择要删除的配置')
    return
  }
  
  if (!confirm(`确定要删除选中的 ${selectedMappingItems.value.length} 个配置吗？`)) {
    return
  }
  
  try {
    const deletePromises = selectedMappingItems.value.map(id => 
      fetch(`/api/api/cloth-paper-compatibility/${id}`, {
        method: 'DELETE'
      })
    )
    
    await Promise.all(deletePromises)
    
    // 重新加载数据
    if (selectedClothPaperType.value) {
      await loadMappingConfigs(selectedClothPaperType.value.id)
    }
    
    selectedMappingItems.value = []
    alert('批量删除成功')
  } catch (error) {
    console.error('批量删除失败:', error)
    alert('批量删除失败，请重试')
  }
}

// 打开批量复制对话框
const openBatchCopyDialog = () => {
  if (selectedMappingItems.value.length === 0) {
    alert('请先选择要复制的映射配置')
    return
  }
  Object.assign(batchCopyForm, {
    targetClothPaperTypeId: null,
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
  showBatchCopyDialog.value = true
}

// 关闭批量复制对话框
const closeBatchCopyDialog = () => {
  showBatchCopyDialog.value = false
  Object.assign(batchCopyForm, {
    targetClothPaperTypeId: null,
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
}

// 确认批量复制
const confirmBatchCopy = async () => {
  if (selectedMappingItems.value.length === 0) {
    alert('请先选择要复制的映射配置')
    return
  }

  try {
    let successCount = 0
    let failCount = 0
    const errors: string[] = []

    for (const id of selectedMappingItems.value) {
      try {
        const item = mappingConfigs.value.find(m => m.id === id)
        if (!item) {
          failCount++
          errors.push(`ID ${id}: 映射不存在`)
          continue
        }

        const targetClothPaperTypeId =
          batchCopyForm.targetClothPaperTypeId !== null
            ? batchCopyForm.targetClothPaperTypeId
            : selectedClothPaperType.value?.id

        if (!targetClothPaperTypeId) {
          failCount++
          errors.push(`ID ${id}: 目标布面纸类型缺失`)
          continue
        }

        const createData = {
          clothPaperTypeId: targetClothPaperTypeId,
          productName:
            batchCopyForm.productName && batchCopyForm.productName.trim() !== ''
              ? batchCopyForm.productName.trim()
              : item.productName,
          paperType:
            batchCopyForm.paperType && batchCopyForm.paperType.trim() !== ''
              ? batchCopyForm.paperType.trim()
              : item.paperType,
          compatibilityStatus:
            batchCopyForm.compatibilityStatus !== ''
              ? batchCopyForm.compatibilityStatus
              : item.compatibilityStatus
        }

        // 唯一性检查：避免同一布面纸类型下重复 productName+paperType 组合
        const duplicate = mappingConfigs.value.find(m =>
          m.clothPaperTypeId === createData.clothPaperTypeId &&
          m.productName === createData.productName &&
          (m.paperType || '') === (createData.paperType || '')
        )
        if (duplicate) {
          failCount++
          errors.push(
            `ID ${id}: 目标布面纸类型下已存在相同「燙金紙系列+烫金纸性能类型」配置，跳过复制`
          )
          continue
        }

        const response = await fetch('/api/api/cloth-paper-compatibility', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(createData)
        })

        if (!response.ok) {
          const errorText = await response.text()
          throw new Error(errorText || `HTTP ${response.status}`)
        }

        successCount++
      } catch (error: any) {
        failCount++
        errors.push(`ID ${id}: ${error.message || '复制失败'}`)
      }
    }

    if (failCount === 0) {
      alert(`批量复制成功！成功复制 ${successCount} 条映射配置`)
    } else {
      let message = `批量复制完成！\n成功: ${successCount} 条`
      if (failCount > 0) {
        message += `\n失败: ${failCount} 条`
        if (errors.length > 0 && errors.length <= 10) {
          message += `\n\n失败详情:\n${errors.join('\n')}`
        }
      }
      alert(message)
    }

    if (successCount > 0 && selectedClothPaperType.value) {
      await loadMappingConfigs(selectedClothPaperType.value.id)
      showBatchCopyDialog.value = false
    }
  } catch (error) {
    console.error('批量复制失败:', error)
    alert('批量复制失败，请重试')
  }
}
const loadMappingConfigs = async (clothPaperTypeId: number) => {
  try {
    const response = await fetch(`/api/api/cloth-paper-compatibility/type/${clothPaperTypeId}`)
    if (response.ok) {
      mappingConfigs.value = await response.json()
    } else {
      console.error('加载映射配置失败')
      mappingConfigs.value = []
    }
  } catch (error) {
    console.error('加载映射配置失败:', error)
    mappingConfigs.value = []
  }
}

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

const loadAvailablePaperTypes = async () => {
  try {
    // 使用与 hot-stamping-type 页面相同的 API 调用方式
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

// 联动选择：搜索筛选中的产品名称变化时加载对应的烫金纸性能类型
// 配置映射对话框中的产品系列搜索相关方法
const onMappingProductSearchInput = () => {
  showMappingProductDropdown.value = true
}

const selectMappingProduct = async (product: string) => {
  mappingSearchForm.productName = product
  mappingProductSearchText.value = product
  showMappingProductDropdown.value = false
  await onSearchProductNameChange()
}

const handleMappingProductInputBlur = () => {
  setTimeout(() => {
    showMappingProductDropdown.value = false
    // 如果输入框有值但没有匹配到产品，尝试精确匹配
    if (mappingProductSearchText.value && !mappingSearchForm.productName) {
      const exactMatch = availableProducts.value.find(product => 
        product === mappingProductSearchText.value.trim()
      )
      if (exactMatch) {
        selectMappingProduct(exactMatch)
      } else {
        // 如果没有精确匹配，使用输入值进行模糊搜索
        mappingSearchForm.productName = mappingProductSearchText.value.trim()
        onSearchProductNameChange()
      }
    } else if (!mappingSearchForm.productName) {
      // 如果没有选择产品，清空输入
      mappingProductSearchText.value = ''
    } else {
      // 如果已选择产品，同步搜索文本
      mappingProductSearchText.value = mappingSearchForm.productName
    }
  }, 200)
}

const onSearchProductNameChange = async () => {
  if (mappingSearchForm.productName) {
    loadingPaperTypes.value = true
    try {
      console.log('搜索筛选：正在加载产品名称对应的烫金纸性能类型:', mappingSearchForm.productName)
      // 使用与 hot-stamping-type 页面相同的逻辑
      const response = await fetch(`/api/api/product-management/search/name?name=${encodeURIComponent(mappingSearchForm.productName)}`)
      if (response.ok) {
        const products = await response.json()
        // 提取所有不重复的烫金纸性能类型，与 hot-stamping-type 页面保持一致
        const paperTypes = [...new Set(products
          .map((product: any) => product.hotStampingPaperType)
          .filter((type: string) => type && type.trim() !== '')
        )] as string[]
        console.log('搜索筛选：获取到的烫金纸性能类型:', paperTypes)
        filteredPaperTypes.value = paperTypes
      } else {
        console.error('搜索筛选：加载烫金纸性能类型失败')
        filteredPaperTypes.value = []
      }
    } catch (error) {
      console.error('搜索筛选：加载烫金纸性能类型失败:', error)
      filteredPaperTypes.value = []
    } finally {
      loadingPaperTypes.value = false
    }
  } else {
    filteredPaperTypes.value = []
  }
  // 清空烫金纸性能类型选择
  mappingSearchForm.paperType = ''
}

// 联动选择：添加映射对话框中的产品名称变化时加载对应的烫金纸性能类型
const onNewMappingProductNameChange = async () => {
  if (!newMapping.productName) {
    newMappingPaperTypes.value = []
    newMapping.paperType = ''
    return
  }

  loadingPaperTypes.value = true
  try {
    console.log('正在加载产品名称对应的烫金纸性能类型:', newMapping.productName)
    // 使用与 hot-stamping-type 页面相同的逻辑
    const response = await fetch(`/api/api/product-management/search/name?name=${encodeURIComponent(newMapping.productName)}`)
    if (response.ok) {
      const products = await response.json()
      // 提取所有不重复的烫金纸性能类型，与 hot-stamping-type 页面保持一致
      const paperTypes = [...new Set(products
        .map((product: any) => product.hotStampingPaperType)
        .filter((type: string) => type && type.trim() !== '')
      )] as string[]
      console.log('获取到的烫金纸性能类型:', paperTypes)
      newMappingPaperTypes.value = paperTypes
    } else {
      console.error('加载烫金纸性能类型失败')
      newMappingPaperTypes.value = []
    }
  } catch (error) {
    console.error('加载烫金纸性能类型失败:', error)
    newMappingPaperTypes.value = []
  } finally {
    loadingPaperTypes.value = false
  }
}

// 编辑映射
const editMapping = (mapping: any) => {
  // 设置编辑ID
  editingMappingId.value = mapping.id
  
  // 将映射数据复制到 newMapping 表单中
  Object.assign(newMapping, {
    productName: mapping.productName,
    paperType: mapping.paperType || '',
    compatibilityStatus: mapping.compatibilityStatus
  })
  
  // 设置产品搜索文本
  newMappingProductSearchText.value = mapping.productName || ''
  showNewMappingProductDropdown.value = false
  
  // 打开编辑对话框
  showAddMappingDialog.value = true
  
  // 加载对应的烫金纸性能类型选项
  if (mapping.productName) {
    onNewMappingProductNameChange()
  }
}

// 自动保存映射（兼容性状态修改时自动保存）
const autoSaveMapping = async (mapping: any) => {
  try {
    const response = await fetch(`/api/api/cloth-paper-compatibility/${mapping.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...mapping,
        compatibilityStatus: mapping.compatibilityStatus
      })
    })
    if (response.ok) {
      console.log('映射配置自动保存成功')
      // 可选：显示成功提示
      // alert('保存成功')
    } else {
      // 处理后端返回的错误信息
      const errorData = await response.json().catch(() => ({}))
      if (errorData.error) {
        alert(errorData.error)
      } else if (response.status === 400) {
        alert('该燙金紙系列和布面纸类型的组合已存在，请选择其他组合')
      } else {
        alert('保存映射配置失败')
      }
    }
  } catch (error: any) {
    console.error('自动保存映射配置失败:', error)
    alert('保存映射配置失败，请重试')
  }
}

// 复制映射
const copyMapping = (mapping: any) => {
  // 将映射数据复制到 newMapping 表单中
  Object.assign(newMapping, {
    productName: mapping.productName,
    paperType: mapping.paperType || '',
    compatibilityStatus: mapping.compatibilityStatus
  })
  
  // 设置产品搜索文本
  newMappingProductSearchText.value = mapping.productName || ''
  showNewMappingProductDropdown.value = false
  
  // 清空编辑ID，表示这是新增
  editingMappingId.value = null
  
  // 打开添加对话框
  showAddMappingDialog.value = true
  
  // 加载对应的烫金纸性能类型选项
  if (mapping.productName) {
    onNewMappingProductNameChange()
  }
}

const deleteMapping = async (id: number) => {
  if (confirm('确定要删除这个映射配置吗？')) {
    try {
      const response = await fetch(`/api/api/cloth-paper-compatibility/${id}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        await loadMappingConfigs(selectedClothPaperType.value!.id)
      } else {
        console.error('删除映射配置失败')
      }
    } catch (error) {
      console.error('删除映射配置失败:', error)
    }
  }
}

const addMapping = async () => {
  if (!newMapping.productName || !selectedClothPaperType.value) {
    alert('请选择产品')
    return
  }

  try {
    const mappingData = {
      productName: newMapping.productName,
      clothPaperTypeId: selectedClothPaperType.value.id,
      compatibilityStatus: newMapping.compatibilityStatus,
      paperType: newMapping.paperType
    }

    let response
    if (editingMappingId.value) {
      // 编辑模式：使用 PUT 请求
      response = await fetch(`/api/api/cloth-paper-compatibility/${editingMappingId.value}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(mappingData)
      })
    } else {
      // 新增模式：使用 POST 请求
      response = await fetch('/api/api/cloth-paper-compatibility', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(mappingData)
      })
    }
    
    if (response.ok) {
      await loadMappingConfigs(selectedClothPaperType.value.id)
      closeAddMappingDialog()
    } else {
      // 处理后端返回的错误信息
      const errorData = await response.json().catch(() => ({}))
      if (errorData.error) {
        alert(errorData.error)
      } else if (response.status === 400) {
        alert('该燙金紙系列和布面纸类型的组合已存在，请选择其他组合')
      } else {
        alert('添加映射配置失败')
      }
    }
  } catch (error: any) {
    console.error('添加映射配置失败:', error)
    alert('添加映射配置失败，请重试')
  }
}

// 批量添加相关方法
const openBatchAddDialog = async () => {
  if (!selectedClothPaperType.value) {
    alert('请先选择布面纸类型')
    return
  }
  
  // 重置表单
  Object.assign(batchAddForm, {
    selectedSeries: [],
    selectedProducts: {},
    paperType: '',
    compatibilityStatus: 'NA'
  })
  
  // 加载所有烫金纸性能类型
  await loadAvailablePaperTypes()
  
  // 重置搜索文本和可用产品系列
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
  
  showBatchAddDialog.value = true
}

const closeBatchAddDialog = () => {
  showBatchAddDialog.value = false
  Object.assign(batchAddForm, {
    selectedSeries: [],
    selectedProducts: {},
    paperType: '',
    compatibilityStatus: 'NA'
  })
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
}

const batchAddMappings = async () => {
  if (!selectedClothPaperType.value) {
    alert('请先选择布面纸类型')
    return
  }
  
  if (!batchAddForm.paperType) {
    alert('请先选择烫金纸性能类型')
    return
  }
  
  if (batchAddForm.selectedSeries.length === 0) {
    alert('请至少选择一个产品系列')
    return
  }
  
  try {
    // 为每个选中的产品系列创建映射（只映射到系列，不涉及具体产品）
    const compatibilities = batchAddForm.selectedSeries.map(seriesName => ({
      productName: seriesName,
      clothPaperTypeId: selectedClothPaperType.value!.id,
      paperType: batchAddForm.paperType,
      compatibilityStatus: batchAddForm.compatibilityStatus
    }))
    
    let successCount = 0
    let failCount = 0
    const errors: string[] = []
    
    for (const compatibility of compatibilities) {
      try {
        // 检查是否已存在
        const existing = await fetch(
          `/api/api/cloth-paper-compatibility/project/${encodeURIComponent(compatibility.productName)}/type/${compatibility.clothPaperTypeId}`
        )
        
        if (existing.ok) {
          // 已存在，更新
          const existingData = await existing.json()
          const response = await fetch(`/api/api/cloth-paper-compatibility/${existingData.id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(compatibility)
          })
          if (response.ok) {
            successCount++
          } else {
            failCount++
            const errorData = await response.json().catch(() => ({}))
            errors.push(`${compatibility.productName}: ${errorData.error || '更新失败'}`)
          }
        } else {
          // 不存在，创建
          const response = await fetch('/api/api/cloth-paper-compatibility', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(compatibility)
          })
          if (response.ok) {
            successCount++
          } else {
            failCount++
            const errorData = await response.json().catch(() => ({}))
            errors.push(`${compatibility.productName}: ${errorData.error || '创建失败'}`)
          }
        }
      } catch (error) {
        failCount++
        errors.push(`${compatibility.productName}: ${error}`)
      }
    }
    
    if (failCount === 0) {
      alert(`批量添加完成！成功添加 ${successCount} 条映射配置`)
    } else {
      const errorMessages = errors.join('\n')
      alert(`批量添加完成！成功：${successCount} 条，失败：${failCount} 条\n\n失败详情：\n${errorMessages}`)
    }
    
    if (successCount > 0) {
      await loadMappingConfigs(selectedClothPaperType.value.id)
      closeBatchAddDialog()
    }
  } catch (error) {
    console.error('批量添加映射配置失败:', error)
    alert('批量添加失败，请重试')
  }
}

// 根据烫金纸性能类型加载支持的产品系列
const loadSeriesByPaperType = async (paperType: string) => {
  if (!paperType) {
    batchAddAvailableSeries.value = []
    return
  }
  
  try {
    const response = await fetch(`/api/api/products/series-by-paper-type/${encodeURIComponent(paperType)}`)
    if (response.ok) {
      const series = await response.json()
      batchAddAvailableSeries.value = series || []
    } else {
      console.error('获取支持该烫金纸性能类型的产品系列失败，状态码:', response.status)
      batchAddAvailableSeries.value = []
    }
  } catch (error) {
    console.error('获取支持该烫金纸性能类型的产品系列失败:', error)
    batchAddAvailableSeries.value = []
  }
}

// 批量添加对话框中烫金纸性能类型变化处理
const onBatchAddPaperTypeChange = async () => {
  // 清空之前选择的产品系列
  batchAddForm.selectedSeries = []
  batchAddSeriesSearchText.value = ''
  
  // 加载支持该烫金纸性能类型的产品系列
  if (batchAddForm.paperType) {
    await loadSeriesByPaperType(batchAddForm.paperType)
  } else {
    batchAddAvailableSeries.value = []
  }
}

// 导出相关方法
const exportMappings = async () => {
  if (!selectedClothPaperType.value) {
    alert('请先选择布面纸类型')
    return
  }
  
  try {
    const response = await fetch(
      `/api/api/cloth-paper-compatibility/export?clothPaperTypeId=${selectedClothPaperType.value.id}`,
      {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
        }
      }
    )
    
    if (!response.ok) {
      throw new Error('导出失败')
    }
    
    // 获取文件名
    const contentDisposition = response.headers.get('Content-Disposition')
    let fileName = '适用界面映射配置.xlsx'
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

// 导入相关方法
const openImportDialog = () => {
  if (!selectedClothPaperType.value) {
    alert('请先选择布面纸类型')
    return
  }
  selectedFile.value = null
  importResult.value = null
  showImportDialog.value = true
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
    const response = await fetch('/api/api/cloth-paper-compatibility/import-template', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
      }
    })
    
    if (!response.ok) {
      throw new Error('下载模板失败')
    }
    
    const contentDisposition = response.headers.get('Content-Disposition')
    let fileName = '适用界面映射配置导入模板.xlsx'
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
    alert('请选择要导入的文件')
    return
  }
  
  if (!selectedClothPaperType.value) {
    alert('请先选择布面纸类型')
    return
  }
  
  try {
    const formData = new FormData()
    formData.append('file', selectedFile.value)
    formData.append('clothPaperTypeId', selectedClothPaperType.value.id.toString())
    
    const response = await fetch('/api/api/cloth-paper-compatibility/import', {
      method: 'POST',
      body: formData
    })
    
    const result = await response.json()
    importResult.value = {
      success: result.success,
      message: result.message,
      details: result.details || []
    }
    
    if (result.success) {
      await loadMappingConfigs(selectedClothPaperType.value.id)
      setTimeout(() => {
        closeImportDialog()
      }, 2000)
    }
  } catch (error) {
    console.error('导入失败:', error)
    importResult.value = {
      success: false,
      message: '导入失败: ' + (error as Error).message,
      details: []
    }
  }
}

// 批量修改状态相关方法
const openBatchEditStatusDialog = () => {
  if (selectedMappingItems.value.length === 0) {
    alert('请选择要修改的映射')
    return
  }
  batchStatusForm.compatibilityStatus = 'NA'
  showBatchEditStatusDialog.value = true
}

const closeBatchEditStatusDialog = () => {
  showBatchEditStatusDialog.value = false
  batchStatusForm.compatibilityStatus = 'NA'
}

const batchUpdateStatus = async () => {
  if (selectedMappingItems.value.length === 0) {
    alert('请选择要修改的映射')
    return
  }
  
  try {
    let successCount = 0
    let failCount = 0
    const errors: string[] = []
    
    for (const id of selectedMappingItems.value) {
      try {
        const mapping = mappingConfigs.value.find(m => m.id === id)
        if (!mapping) {
          failCount++
          errors.push(`ID ${id}: 未找到映射`)
          continue
        }
        
        const response = await fetch(`/api/api/cloth-paper-compatibility/${id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            ...mapping,
            compatibilityStatus: batchStatusForm.compatibilityStatus
          })
        })
        
        if (response.ok) {
          successCount++
        } else {
          failCount++
          errors.push(`${mapping.productName}: 更新失败`)
        }
      } catch (error) {
        failCount++
        errors.push(`ID ${id}: ${error}`)
      }
    }
    
    alert(`批量修改状态完成！成功：${successCount} 条，失败：${failCount} 条${errors.length > 0 ? '\n' + errors.join('\n') : ''}`)
    
    if (successCount > 0 && selectedClothPaperType.value) {
      await loadMappingConfigs(selectedClothPaperType.value.id)
      closeBatchEditStatusDialog()
      selectedMappingItems.value = []
    }
  } catch (error) {
    console.error('批量修改状态失败:', error)
    alert('批量修改状态失败，请重试')
  }
}

// 清空映射选择
const clearMappingSelection = () => {
  selectedMappingItems.value = []
}

// 查看所有配置映射相关数据
const showAllMappingsDialog = ref(false)
const allMappings = ref<any[]>([])
const allMappingsCurrentPage = ref(1)
const allMappingsPageSize = ref(20)
const allMappingsTotalItems = ref(0)
const selectedAllMappingsItems = ref<number[]>([])
const showAllMappingsBatchEditDialog = ref(false)
const showAllMappingsBatchCopyDialog = ref(false)
const allMappingsBatchForm = reactive({
  productName: '',
  paperType: '',
  compatibilityStatus: ''
})
const allMappingsCopyForm = reactive({
  targetClothPaperTypeId: null as number | null,
  productName: '',
  paperType: '',
  compatibilityStatus: '' as '' | 'V' | 'X' | 'NA' | '▷'
})
const allMappingsSearchForm = reactive({
  typeName: '',
  productName: '',
  paperType: '',
  compatibilityStatus: ''
})

// 查看所有配置映射的计算属性
const filteredAllMappings = computed(() => {
  let filtered = allMappings.value

  // 布面纸类型筛选（支持模糊匹配）
  if (allMappingsSearchForm.typeName) {
    const typeName = allMappingsSearchForm.typeName.toLowerCase()
    filtered = filtered.filter(mapping => 
      mapping.typeName && mapping.typeName.toLowerCase().includes(typeName)
    )
  }

  // 燙金紙系列筛选（支持模糊匹配）
  if (allMappingsSearchForm.productName) {
    const productName = allMappingsSearchForm.productName.toLowerCase()
    filtered = filtered.filter(mapping => 
      mapping.productName && mapping.productName.toLowerCase().includes(productName)
    )
  }

  // 烫金纸性能类型筛选（支持模糊匹配）
  if (allMappingsSearchForm.paperType) {
    const paperType = allMappingsSearchForm.paperType.toLowerCase()
    filtered = filtered.filter(mapping => 
      mapping.paperType && mapping.paperType.toLowerCase().includes(paperType)
    )
  }

  // 兼容性状态筛选
  if (allMappingsSearchForm.compatibilityStatus !== '') {
    filtered = filtered.filter(mapping => 
      mapping.compatibilityStatus === allMappingsSearchForm.compatibilityStatus
    )
  }

  // 按布面纸类型和燙金紙系列排序
  filtered.sort((a, b) => {
    if (a.typeName !== b.typeName) {
      return (a.typeName || '').localeCompare(b.typeName || '')
    }
    return (a.productName || '').localeCompare(b.productName || '')
  })

  // 分页
  const start = (allMappingsCurrentPage.value - 1) * allMappingsPageSize.value
  const end = start + allMappingsPageSize.value
  allMappingsTotalItems.value = filtered.length
  return filtered.slice(start, end)
})

const allMappingsTotalPages = computed(() => Math.ceil(allMappingsTotalItems.value / allMappingsPageSize.value))

const allMappingsVisiblePages = computed(() => {
  const pages = []
  const start = Math.max(1, allMappingsCurrentPage.value - 2)
  const end = Math.min(allMappingsTotalPages.value, start + 4)
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

const isAllMappingsSelected = computed(() => {
  const currentPageIds = filteredAllMappings.value.map(m => m.id).filter((id): id is number => id !== undefined)
  return currentPageIds.length > 0 && currentPageIds.every(id => selectedAllMappingsItems.value.includes(id))
})

// 查看所有配置映射相关方法
const openAllMappingsDialog = async () => {
  showAllMappingsDialog.value = true
  // 确保有可用的系列和性能类型数据，供批量修改/复制时模糊选择和校验
  if (availableProducts.value.length === 0) {
    await loadAvailableProducts()
  }
  if (availablePaperTypes.value.length === 0) {
    await loadAvailablePaperTypes()
  }
  await loadAllMappings()
  // 重置搜索表单
  Object.assign(allMappingsSearchForm, {
    typeName: '',
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
  allMappingsCurrentPage.value = 1
  selectedAllMappingsItems.value = []
}

const closeAllMappingsDialog = () => {
  showAllMappingsDialog.value = false
  allMappings.value = []
  allMappingsCurrentPage.value = 1
  selectedAllMappingsItems.value = []
}

const loadAllMappings = async () => {
  try {
    // 获取所有布面纸类型
    const typesResponse = await fetch('/api/api/cloth-paper-types/all')
    if (!typesResponse.ok) {
      console.error('加载布面纸类型失败')
      return
    }
    const allTypes = await typesResponse.json()
    
    // 获取所有布面纸类型的映射
    const allMappingsData: any[] = []
    for (const type of allTypes) {
      try {
        const mappingsResponse = await fetch(`/api/api/cloth-paper-compatibility/type/${type.id}`)
        if (mappingsResponse.ok) {
          const mappings = await mappingsResponse.json()
          // 为每个映射添加布面纸类型名称和分类
          mappings.forEach((mapping: any) => {
            allMappingsData.push({
              ...mapping,
              typeName: type.typeName || mapping.typeName,
              category: type.category || mapping.category || mapping.clothPaperCategory || '-'
            })
          })
        }
      } catch (error) {
        console.error(`加载布面纸类型 ${type.id} 的映射失败:`, error)
      }
    }
    
    allMappings.value = allMappingsData
    allMappingsTotalItems.value = allMappingsData.length
  } catch (error) {
    console.error('加载所有配置映射失败:', error)
    alert('加载所有配置映射失败')
  }
}

const searchAllMappings = () => {
  allMappingsCurrentPage.value = 1
}

const resetAllMappingsSearch = () => {
  Object.assign(allMappingsSearchForm, {
    typeName: '',
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
  allMappingsCurrentPage.value = 1
}

const viewMappingDetails = (mapping: any) => {
  // 找到对应的布面纸类型
  const type = types.value.find(t => t.typeName === mapping.typeName)
  if (type) {
    // 打开该布面纸类型的配置映射对话框
    openMappingDialog(type)
  } else {
    alert('无法找到对应的布面纸类型配置')
  }
}

// 查看所有配置映射的批量操作相关方法
const toggleAllMappingsSelection = (id: number) => {
  const index = selectedAllMappingsItems.value.indexOf(id)
  if (index > -1) {
    selectedAllMappingsItems.value.splice(index, 1)
  } else {
    selectedAllMappingsItems.value.push(id)
  }
}

const toggleSelectAllAllMappings = () => {
  if (isAllMappingsSelected.value) {
    selectedAllMappingsItems.value = []
  } else {
    selectedAllMappingsItems.value = filteredAllMappings.value.map(m => m.id).filter((id): id is number => id !== undefined)
  }
}

const clearAllMappingsSelection = () => {
  selectedAllMappingsItems.value = []
}

const openAllMappingsBatchEditDialog = () => {
  if (selectedAllMappingsItems.value.length === 0) {
    alert('请选择要修改的映射')
    return
  }
  // 重置表单
  Object.assign(allMappingsBatchForm, {
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
  showAllMappingsBatchEditDialog.value = true
}

const closeAllMappingsBatchEditDialog = () => {
  showAllMappingsBatchEditDialog.value = false
  Object.assign(allMappingsBatchForm, {
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
}

const openAllMappingsBatchCopyDialog = () => {
  if (selectedAllMappingsItems.value.length === 0) {
    alert('请选择要复制的映射')
    return
  }
  Object.assign(allMappingsCopyForm, {
    targetClothPaperTypeId: null,
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
  showAllMappingsBatchCopyDialog.value = true
}

const closeAllMappingsBatchCopyDialog = () => {
  showAllMappingsBatchCopyDialog.value = false
}

const confirmAllMappingsBatchCopy = async () => {
  if (selectedAllMappingsItems.value.length === 0) {
    alert('请选择要复制的映射')
    return
  }

  // 校验输入的燙金紙系列和烫金纸性能类型是否存在于系统中
  if (allMappingsCopyForm.productName) {
    const name = allMappingsCopyForm.productName.trim()
    if (name && !availableProducts.value.includes(name)) {
      alert(`燙金紙系列「${name}」不存在于系统，请从下拉建议中选择`)
      return
    }
  }

  if (allMappingsCopyForm.paperType) {
    const pt = allMappingsCopyForm.paperType.trim()
    if (pt && !availablePaperTypes.value.includes(pt)) {
      alert(`烫金纸性能类型「${pt}」不存在于系统，请从下拉建议中选择`)
      return
    }
  }

  try {
    let successCount = 0
    let failCount = 0
    const errors: string[] = []

    for (const id of selectedAllMappingsItems.value) {
      try {
        const item: any = allMappings.value.find(m => m.id === id)
        if (!item) {
          failCount++
          errors.push(`ID ${id}: 映射不存在`)
          continue
        }

        const sourceClothPaperTypeId = item.clothPaperTypeId || item.clothPaperTypeID || null
        const targetClothPaperTypeId =
          allMappingsCopyForm.targetClothPaperTypeId !== null
            ? allMappingsCopyForm.targetClothPaperTypeId
            : sourceClothPaperTypeId

        if (!targetClothPaperTypeId) {
          failCount++
          errors.push(`ID ${id}: 无法确定目标布面纸类型`)
          continue
        }

        const createData = {
          clothPaperTypeId: targetClothPaperTypeId,
          productName:
            allMappingsCopyForm.productName && allMappingsCopyForm.productName.trim() !== ''
              ? allMappingsCopyForm.productName.trim()
              : item.productName,
          paperType:
            allMappingsCopyForm.paperType && allMappingsCopyForm.paperType.trim() !== ''
              ? allMappingsCopyForm.paperType.trim()
              : item.paperType,
          compatibilityStatus:
            allMappingsCopyForm.compatibilityStatus !== ''
              ? allMappingsCopyForm.compatibilityStatus
              : item.compatibilityStatus
        }

        const duplicate = allMappings.value.find((m: any) =>
          (m.clothPaperTypeId || m.clothPaperTypeID) === createData.clothPaperTypeId &&
          m.productName === createData.productName &&
          (m.paperType || '') === (createData.paperType || '')
        )

        if (duplicate) {
          failCount++
          errors.push(`ID ${id}: 目标布面纸类型下已存在相同「燙金紙系列+烫金纸性能类型」配置，跳过复制`)
          continue
        }

        const response = await fetch('/api/api/cloth-paper-compatibility', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(createData)
        })

        if (!response.ok) {
          const errorText = await response.text()
          throw new Error(errorText || `HTTP ${response.status}`)
        }

        successCount++
      } catch (error: any) {
        failCount++
        errors.push(`ID ${id}: ${error.message || '复制失败'}`)
      }
    }

    if (failCount === 0) {
      alert(`批量复制成功！成功复制 ${successCount} 条映射配置`)
    } else {
      let message = `批量复制完成！\n成功: ${successCount} 条`
      if (failCount > 0) {
        message += `\n失败: ${failCount} 条`
        if (errors.length > 0 && errors.length <= 10) {
          message += `\n\n失败详情:\n${errors.join('\n')}`
        }
      }
      alert(message)
    }

    if (successCount > 0) {
      await loadAllMappings()
      showAllMappingsBatchCopyDialog.value = false
    }
  } catch (error) {
    console.error('批量复制失败:', error)
    alert('批量复制失败，请重试')
  }
}

const batchUpdateAllMappings = async () => {
  if (selectedAllMappingsItems.value.length === 0) {
    alert('请选择要修改的映射')
    return
  }
  
  // 检查是否有任何字段需要更新
  const hasUpdate = allMappingsBatchForm.productName !== '' ||
                   allMappingsBatchForm.paperType !== '' ||
                   allMappingsBatchForm.compatibilityStatus !== ''
  
  if (!hasUpdate) {
    alert('请至少填写一个要修改的字段')
    return
  }

  // 校验输入的燙金紙系列和烫金纸性能类型是否存在于系统中
  if (allMappingsBatchForm.productName) {
    const name = allMappingsBatchForm.productName.trim()
    if (name && !availableProducts.value.includes(name)) {
      alert(`燙金紙系列「${name}」不存在于系统，请从下拉建议中选择`)
      return
    }
  }

  if (allMappingsBatchForm.paperType) {
    const pt = allMappingsBatchForm.paperType.trim()
    if (pt && !availablePaperTypes.value.includes(pt)) {
      alert(`烫金纸性能类型「${pt}」不存在于系统，请从下拉建议中选择`)
      return
    }
  }
  
  try {
    const updatePromises = selectedAllMappingsItems.value.map(async id => {
      const mapping = allMappings.value.find(m => m.id === id)
      if (!mapping) return Promise.resolve()
      
      // 构建更新对象，只包含需要更新的字段
      const updateData: any = {}
      
      // 燙金紙系列
      if (allMappingsBatchForm.productName !== '') {
        updateData.productName = allMappingsBatchForm.productName
      }
      
      // 烫金纸性能类型
      if (allMappingsBatchForm.paperType !== '') {
        updateData.paperType = allMappingsBatchForm.paperType
      }
      
      // 兼容性状态
      if (allMappingsBatchForm.compatibilityStatus !== '') {
        updateData.compatibilityStatus = allMappingsBatchForm.compatibilityStatus
      }
      
      // 如果没有任何更新字段，跳过
      if (Object.keys(updateData).length === 0) {
        return Promise.resolve()
      }
      
      // 合并原有数据
      const finalData = {
        ...mapping,
        ...updateData
      }
      
      return fetch(`/api/api/cloth-paper-compatibility/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(finalData)
      })
    })
    
    await Promise.all(updatePromises)
    
    const updatedCount = selectedAllMappingsItems.value.length
    
    // 重新加载数据
    await loadAllMappings()
    
    closeAllMappingsBatchEditDialog()
    selectedAllMappingsItems.value = []
    alert(`成功批量修改 ${updatedCount} 条映射`)
  } catch (error) {
    console.error('批量修改失败:', error)
    alert('批量修改失败，请重试')
  }
}

const batchDeleteAllMappings = async () => {
  if (selectedAllMappingsItems.value.length === 0) {
    alert('请选择要删除的映射')
    return
  }
  
  if (!confirm(`确定要删除选中的 ${selectedAllMappingsItems.value.length} 个映射吗？此操作不可恢复！`)) {
    return
  }
  
  try {
    const deletePromises = selectedAllMappingsItems.value.map(id => 
      fetch(`/api/api/cloth-paper-compatibility/${id}`, {
        method: 'DELETE'
      })
    )
    
    await Promise.all(deletePromises)
    
    // 重新加载数据
    await loadAllMappings()
    
    selectedAllMappingsItems.value = []
    alert('批量删除成功')
  } catch (error) {
    console.error('批量删除失败:', error)
    alert('批量删除失败，请重试')
  }
}

// 生命周期
onMounted(() => {
  loadTypes()
})
</script>

<style scoped>
/* 可以添加特定的样式 */
</style>
