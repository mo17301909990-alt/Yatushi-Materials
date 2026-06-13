<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">燙金圖案區域管理</h1>
            <p class="mt-2 text-gray-600">管理燙金圖案的區域選項和配置</p>
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
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 hover:bg-red-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加圖案區域
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
        <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">選項名稱</label>
            <input
              v-model="searchForm.optionName"
              type="text"
              placeholder="搜索選項名稱"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">區域類別</label>
            <input
              v-model="searchForm.areaCategory"
              type="text"
              placeholder="搜索區域類別"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">图案类型</label>
            <input
              v-model="searchForm.patternType"
              type="text"
              placeholder="搜索图案类型"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
            <select
              v-model="searchForm.isActive"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
            >
              <option value="">全部状态</option>
              <option value="true">激活</option>
              <option value="false">禁用</option>
            </select>
          </div>
          <div class="flex items-end">
            <button
              @click="searchPatternAreas"
              class="w-full bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition-colors"
            >
              搜索
            </button>
          </div>
        </div>
      </div>

      <!-- 数据表格 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">图案区域列表</h3>
        </div>
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">選項名稱</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">區域類別</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">区域范围</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">图案类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">尺寸范围(mm)</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">排序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="patternArea in filteredPatternAreas" :key="patternArea.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ patternArea.optionName }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ patternArea.areaCategory }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ patternArea.areaRange }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ patternArea.patternType }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">
                    {{ patternArea.minSizeMm && patternArea.maxSizeMm ? `${patternArea.minSizeMm}-${patternArea.maxSizeMm}` : '-' }}
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="patternArea.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ patternArea.isActive ? '激活' : '禁用' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ patternArea.sortOrder }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button
                      @click="editPatternArea(patternArea)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="configMapping(patternArea)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      配置映射
                    </button>
                    <button
                      @click="toggleStatus(patternArea)"
                      :class="patternArea.isActive ? 'text-yellow-600 hover:text-yellow-900' : 'text-green-600 hover:text-green-900'"
                    >
                      {{ patternArea.isActive ? '禁用' : '启用' }}
                    </button>
                    <button
                      @click="deletePatternArea(patternArea.id)"
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
          <div class="text-gray-500">暂无图案区域数据</div>
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
                      ? 'z-10 bg-red-50 border-red-500 text-red-600'
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

      <!-- 添加/编辑对话框 -->
      <div
        v-if="showAddDialog || showEditDialog"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
      >
        <div class="relative top-20 mx-auto p-5 border w-11/12 md:w-3/4 lg:w-1/2 shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              {{ showAddDialog ? '添加图案区域' : '编辑图案区域' }}
            </h3>
            <form @submit.prevent="savePatternArea" class="space-y-4">
              <div class="grid grid-cols-1 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">選項名稱 *</label>
                  <input
                    v-model="patternAreaForm.optionName"
                    type="text"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">區域類別 *</label>
                  <input
                    v-model="patternAreaForm.areaCategory"
                    type="text"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">区域范围 *</label>
                  <input
                    v-model="patternAreaForm.areaRange"
                    type="text"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">图案类型 *</label>
                  <input
                    v-model="patternAreaForm.patternType"
                    type="text"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                  />
                </div>
                <div class="grid grid-cols-2 gap-4">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">最小尺寸(mm)</label>
                    <input
                      v-model.number="patternAreaForm.minSizeMm"
                      type="number"
                      min="0"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                    />
                  </div>
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">最大尺寸(mm)</label>
                    <input
                      v-model.number="patternAreaForm.maxSizeMm"
                      type="number"
                      min="0"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                    />
                  </div>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">描述</label>
                  <textarea
                    v-model="patternAreaForm.description"
                    rows="3"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                  ></textarea>
                </div>
                <div class="grid grid-cols-2 gap-4">
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-2">排序顺序</label>
                    <input
                      v-model.number="patternAreaForm.sortOrder"
                      type="number"
                      min="0"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                    />
                  </div>
                  <div class="flex items-center">
                    <input
                      v-model="patternAreaForm.isActive"
                      type="checkbox"
                      class="h-4 w-4 text-red-600 focus:ring-red-500 border-gray-300 rounded"
                    />
                    <label class="ml-2 block text-sm text-gray-900">激活状态</label>
                  </div>
                </div>
              </div>
              <div class="flex justify-end space-x-3 pt-4">
                <button
                  type="button"
                  @click="closeDialog"
                  class="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  取消
                </button>
                <button
                  type="submit"
                  class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 hover:bg-red-700"
                >
                  {{ showAddDialog ? '添加' : '保存' }}
                </button>
              </div>
            </form>
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
              配置映射 - {{ selectedPatternArea?.optionName }}
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

          <!-- 映射配置搜索筛选面板 -->
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
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
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
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
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
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
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
                  class="w-full bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition-colors"
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
                <div class="flex items-center space-x-3">
                  <button
                    @click="openAddMappingDialog"
                    class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-red-600 hover:bg-red-700"
                  >
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                    </svg>
                    添加配置
                  </button>
                  <button
                    @click="openBatchAddDialog"
                    class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-red-600 hover:bg-red-700"
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
            <div v-if="selectedMappingItems.length > 0" class="bg-blue-50 border border-blue-200 rounded-lg p-4 mx-6 my-4">
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
                        class="h-4 w-4 text-red-600 focus:ring-red-500 border-gray-300 rounded"
                      />
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙系列</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
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
                        class="h-4 w-4 text-red-600 focus:ring-red-500 border-gray-300 rounded"
                      />
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="text-sm font-medium text-gray-900">{{ mapping.productName }}</div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span
                        :class="[
                          'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                          mapping.compatibilityStatus === 'V' ? 'bg-green-100 text-green-800' :
                          mapping.compatibilityStatus === 'X' ? 'bg-red-100 text-red-800' :
                          mapping.compatibilityStatus === 'NA' ? 'bg-yellow-100 text-yellow-800' :
                          mapping.compatibilityStatus === '▷' ? 'bg-blue-100 text-blue-800' :
                          'bg-gray-100 text-gray-800'
                        ]"
                      >
                        {{ mapping.compatibilityStatus === 'V' ? '适用' :
                           mapping.compatibilityStatus === 'X' ? '不适用' :
                           mapping.compatibilityStatus === 'NA' ? '不确定' :
                           mapping.compatibilityStatus === '▷' ? '需要打底处理' : '未知' }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <div class="text-sm text-gray-900">{{ mapping.paperType || '-' }}</div>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div class="flex space-x-2">
                        <button
                          @click="editMapping(mapping)"
                          class="text-blue-600 hover:text-blue-900"
                        >
                          编辑
                        </button>
                        <button
                          @click="copyMapping(mapping)"
                          class="text-green-600 hover:text-green-900"
                        >
                          复制
                        </button>
                        <button
                          @click="deleteMapping(mapping.id || 0)"
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
            <div v-if="mappingTotalItems > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
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
                        'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                        page === mappingCurrentPage
                          ? 'z-10 bg-red-50 border-red-500 text-red-600'
                          : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'
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
                  placeholder="搜索或选择燙金紙系列..."
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                />
                <div
                  v-if="showNewMappingProductDropdown && filteredNewMappingProducts && filteredNewMappingProducts.length > 0"
                  class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-y-auto"
                >
                  <div
                    v-for="product in filteredNewMappingProducts"
                    :key="product"
                    @mousedown.prevent="selectNewMappingProduct(product)"
                    class="px-3 py-2 cursor-pointer hover:bg-blue-50"
                    :class="{ 'bg-blue-100': newMapping.productName === product }"
                  >
                    {{ product }}
                  </div>
                </div>
                <div v-if="showNewMappingProductDropdown && (!filteredNewMappingProducts || filteredNewMappingProducts.length === 0)" class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg">
                  <div class="px-3 py-2 text-sm text-gray-500">
                    未找到匹配的燙金紙系列
                  </div>
                </div>
              </div>
              <p v-if="newMapping.productName" class="mt-1 text-xs text-gray-500">
                已选择: {{ newMapping.productName }}
              </p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
              <select
                v-model="newMapping.paperType"
                :disabled="!newMapping.productName"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
              >
                <option value="">
                  {{ !newMapping.productName ? '请先选择燙金紙系列' : '请选择烫金纸性能类型' }}
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
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
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
              class="px-4 py-2 border border-transparent rounded-md text-sm font-medium text-white bg-red-600 hover:bg-red-700"
            >
              {{ editingMappingId ? '保存配置' : '添加配置' }}
            </button>
          </div>
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
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
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
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
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
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
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
                      class="h-4 w-4 text-red-600 focus:ring-red-500 border-gray-300 rounded"
                    />
                    <span class="text-sm text-gray-700">{{ series }}</span>
                  </label>
                  <div v-if="filteredBatchAddSeries.length === 0" class="px-2 py-4 text-sm text-gray-500 text-center">
                    {{ !batchAddForm.paperType ? '请先选择烫金纸性能类型' : '未找到匹配的产品系列' }}
                  </div>
                </div>
              </div>
              <p class="mt-2 text-xs text-gray-500">
                已选择 <span class="font-bold text-red-600">{{ batchAddForm.selectedSeries.length }}</span> 个产品系列
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
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 hover:bg-red-700"
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
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
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
              class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 hover:bg-red-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              开始导入
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 批量修改状态对话框 -->
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
                <label class="block text-sm font-medium text-gray-700 mb-2">图案区域</label>
                <input
                  v-model="allMappingsSearchForm.optionName"
                  type="text"
                  placeholder="输入图案区域进行筛选"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列</label>
                <input
                  v-model="allMappingsSearchForm.productName"
                  type="text"
                  placeholder="输入燙金紙系列进行筛选"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                <input
                  v-model="allMappingsSearchForm.paperType"
                  type="text"
                  placeholder="输入烫金纸性能类型进行筛选"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
                <select
                  v-model="allMappingsSearchForm.compatibilityStatus"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
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
                  class="w-full px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 transition-colors"
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
                      class="h-4 w-4 text-red-600 focus:ring-red-500 border-gray-300 rounded"
                    />
                  </th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">图案区域</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙系列</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="mapping in filteredAllMappings" :key="`${mapping.id}-${allMappingsSearchForm.productName}-${allMappingsCurrentPage}`" class="hover:bg-gray-50">
                  <td class="px-6 py-4 whitespace-nowrap">
                    <input
                      type="checkbox"
                      :checked="selectedAllMappingsItems.includes(mapping.id!)"
                      @change="toggleAllMappingsSelection(mapping.id!)"
                      class="h-4 w-4 text-red-600 focus:ring-red-500 border-gray-300 rounded"
                    />
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.optionName || '-' }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.productName }}
                    <!-- 调试信息 -->
                    <span v-if="allMappingsSearchForm.productName && allMappingsSearchForm.productName.trim() === 'SY6+' && (mapping.productName === 'SY6' || mapping.productName === 'SSY6')" class="text-red-500 font-bold">[不应该显示]</span>
                  </td>
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
                      class="text-red-600 hover:text-red-900 mr-2"
                    >
                      查看详情
                    </button>
                  </td>
                </tr>
                <tr v-if="filteredAllMappings.length === 0">
                  <td colspan="6" class="px-6 py-4 text-center text-sm text-gray-500">
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
                        ? 'z-10 bg-red-50 border-red-500 text-red-600'
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
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                <input
                  v-model="allMappingsBatchForm.paperType"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
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
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
              >
                <option value="V">适用</option>
                <option value="X">不适用</option>
                <option value="NA">不确定</option>
                <option value="▷">需要打底处理</option>
              </select>
            </div>
            <p class="text-sm text-gray-500">
              将为选中的 <span class="font-bold">{{ selectedMappingItems.length }}</span> 条记录更新状态
            </p>
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
              class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 hover:bg-red-700"
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
import { ref, reactive, onMounted, computed, watch, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { hotStampingPatternAreaOptionsApi, type HotStampingPatternAreaOption, type CreateHotStampingPatternAreaOption } from '../../../api/modules/hotStampingPatternAreaOptions'
import { hotStampingPatternAreaCompatibilityApi, type HotStampingPatternAreaCompatibility } from '../../../api/modules/hotStampingPatternAreaCompatibility'

// 路由实例
// const router = useRouter()

// 响应式数据
const patternAreas = ref<HotStampingPatternAreaOption[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showMappingDialog = ref(false)
const showAddMappingDialog = ref(false)
const showBatchAddDialog = ref(false)
const showImportDialog = ref(false)
const showBatchEditStatusDialog = ref(false)

// 查看所有配置映射相关数据
const showAllMappingsDialog = ref(false)
const allMappings = ref<any[]>([])
const allMappingsCurrentPage = ref(1)
const allMappingsPageSize = ref(20)
const allMappingsTotalItems = ref(0)
const selectedAllMappingsItems = ref<number[]>([])
const showAllMappingsBatchEditDialog = ref(false)
const allMappingsBatchForm = reactive({
  productName: '',
  paperType: '',
  compatibilityStatus: ''
})
const allMappingsSearchForm = reactive({
  optionName: '',
  productName: '',
  paperType: '',
  compatibilityStatus: ''
})

// 主要列表分页状态
const currentPage = ref(1)
const pageSize = ref(10)


// 映射配置相关数据
const selectedPatternArea = ref<HotStampingPatternAreaOption | null>(null)
const mappingConfigs = ref<HotStampingPatternAreaCompatibility[]>([])
const availableProducts = ref<string[]>([])
const newMapping = reactive({
  productName: '',
  compatibilityStatus: 'NA',
  paperType: ''
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

// 配置映射对话框中的产品系列搜索
const mappingProductSearchText = ref('')
const showMappingProductDropdown = ref(false)

// 批量操作相关数据
const selectedMappingItems = ref<number[]>([])

// 烫金纸性能类型联动选择相关数据
const availablePaperTypes = ref<string[]>([])
const filteredPaperTypes = ref<string[]>([])
const newMappingPaperTypes = ref<string[]>([])
const loadingPaperTypes = ref(false)

// 批量添加相关数据
const batchAddForm = reactive({
  selectedSeries: [] as string[],
  paperType: '',
  compatibilityStatus: 'NA'
})
const batchAddAvailableSeries = ref<string[]>([])
const batchAddSeriesSearchText = ref('')

// 导入/导出相关数据
const selectedFile = ref<File | null>(null)
const importFileInput = ref<HTMLInputElement | null>(null)
const importResult = ref<{ success: boolean, message: string, details?: string[] } | null>(null)

// 批量修改状态相关数据
const batchStatusForm = reactive({
  compatibilityStatus: 'NA'
})

// 燙金紙系列搜索相关数据
const newMappingProductSearchText = ref('')
const showNewMappingProductDropdown = ref(false)

// 搜索表单
const searchForm = reactive({
  optionName: '',
  areaCategory: '',
  patternType: '',
  isActive: ''
})

// 图案区域表单
const patternAreaForm = reactive({
  id: null as number | null,
  optionName: '',
  areaCategory: '',
  areaRange: '',
  patternType: '',
  minSizeMm: null as number | null,
  maxSizeMm: null as number | null,
  description: '',
  isActive: true,
  sortOrder: 0
})

// 计算属性
// 配置映射对话框中的产品系列过滤
const filteredMappingProducts = computed(() => {
  if (!mappingProductSearchText.value || mappingProductSearchText.value.trim() === '') {
    return availableProducts.value
  }
  
  const searchText = mappingProductSearchText.value.toLowerCase().trim()
  return availableProducts.value.filter(product => 
    product && product.toLowerCase().includes(searchText)
  )
})

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

  // 更新总数
  mappingTotalItems.value = filtered.length

  // 分页
  const start = (mappingCurrentPage.value - 1) * mappingPageSize.value
  const end = start + mappingPageSize.value
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

const isAllMappingSelected = computed(() => {
  return filteredMappingConfigs.value.length > 0 && 
         filteredMappingConfigs.value.every(mapping => selectedMappingItems.value.includes(mapping.id))
})

// 新映射对话框中的产品搜索过滤
const filteredNewMappingProducts = computed(() => {
  if (!availableProducts.value || !Array.isArray(availableProducts.value)) {
    return []
  }
  
  if (!newMappingProductSearchText.value || newMappingProductSearchText.value.trim() === '') {
    return availableProducts.value
  }
  
  const searchText = newMappingProductSearchText.value.toLowerCase().trim()
  return availableProducts.value.filter(product => 
    product && product.toLowerCase().includes(searchText)
  )
})

// 批量添加对话框中的产品系列过滤
const filteredBatchAddSeries = computed(() => {
  if (!batchAddForm.paperType) {
    return []
  }
  
  let filtered = batchAddAvailableSeries.value
  
  if (batchAddSeriesSearchText.value && batchAddSeriesSearchText.value.trim() !== '') {
    const searchText = batchAddSeriesSearchText.value.toLowerCase().trim()
    filtered = filtered.filter(series => series.toLowerCase().includes(searchText))
  }
  
  return filtered
})

// 方法
const loadPatternAreas = async () => {
  loading.value = true
  try {
    // 调用真实的API
    const response = await hotStampingPatternAreaOptionsApi.getAllOptions()
    patternAreas.value = response || []
  } catch (error) {
    console.error('加载图案区域失败:', error)
    patternAreas.value = []
    alert('加载图案区域失败，请刷新页面重试')
  } finally {
    loading.value = false
  }
}

// 筛选后的数据
const filteredData = computed(() => {
  let filtered = patternAreas.value

  // 按选项名称筛选
  if (searchForm.optionName) {
    filtered = filtered.filter(item =>
      item.optionName.toLowerCase().includes(searchForm.optionName.toLowerCase())
    )
  }

  // 按区域类别筛选
  if (searchForm.areaCategory) {
    filtered = filtered.filter(item =>
      item.areaCategory.toLowerCase().includes(searchForm.areaCategory.toLowerCase())
    )
  }

  // 按图案类型筛选
  if (searchForm.patternType) {
    filtered = filtered.filter(item =>
      item.patternType.toLowerCase().includes(searchForm.patternType.toLowerCase())
    )
  }

  // 按状态筛选
  if (searchForm.isActive !== '') {
    const isActive = searchForm.isActive === 'true'
    filtered = filtered.filter(item => item.isActive === isActive)
  }

  return filtered
})

// 分页相关计算属性
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

// 保持向后兼容
const filteredPatternAreas = paginatedData

// 当筛选结果或分页大小变化时，自动纠正当前页，避免越界
watch([filteredData, pageSize], () => {
  const total = filteredData.value.length
  const maxPage = Math.max(1, Math.ceil(total / pageSize.value))
  if (currentPage.value > maxPage) {
    currentPage.value = maxPage
  }
})

const searchPatternAreas = () => {
  // 筛选逻辑已经通过计算属性实现，这里不需要重新加载数据
  currentPage.value = 1 // 搜索后重置到第一页
  console.log('筛选条件:', searchForm)
  console.log('筛选结果:', filteredPatternAreas.value)
}

// 主要列表分页相关方法
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

const editPatternArea = (patternArea: HotStampingPatternAreaOption) => {
  Object.assign(patternAreaForm, patternArea)
  showEditDialog.value = true
}

const toggleStatus = async (patternArea: HotStampingPatternAreaOption) => {
  try {
    await hotStampingPatternAreaOptionsApi.updateOption(patternArea.id, { isActive: !patternArea.isActive })
    await loadPatternAreas()
  } catch (error) {
    console.error('更新状态失败:', error)
    // 如果API调用失败，使用本地数据更新
    patternArea.isActive = !patternArea.isActive
  }
}

const deletePatternArea = async (id: number) => {
  if (confirm('确定要删除这个图案区域吗？')) {
    try {
      await hotStampingPatternAreaOptionsApi.deleteOption(id)
      await loadPatternAreas()
    } catch (error) {
      console.error('删除图案区域失败:', error)
    }
  }
}

const savePatternArea = async () => {
  try {
    if (showAddDialog.value) {
      await hotStampingPatternAreaOptionsApi.createOption(patternAreaForm as CreateHotStampingPatternAreaOption)
    } else {
      await hotStampingPatternAreaOptionsApi.updateOption(patternAreaForm.id!, patternAreaForm)
    }
    closeDialog()
    await loadPatternAreas()
  } catch (error) {
    console.error('保存图案区域失败:', error)
  }
}

const configMapping = async (patternArea: HotStampingPatternAreaOption) => {
  selectedPatternArea.value = patternArea
  showMappingDialog.value = true
  await loadMappingConfigs(patternArea.id)
  await loadAvailableProducts()
  await loadAvailablePaperTypes()
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(patternAreaForm, {
    id: null,
    optionName: '',
    areaCategory: '',
    areaRange: '',
    patternType: '',
    minSizeMm: null,
    maxSizeMm: null,
    description: '',
    isActive: true,
    sortOrder: 0
  })
}

// 映射配置相关方法
const closeMappingDialog = () => {
  showMappingDialog.value = false
  selectedPatternArea.value = null
  mappingConfigs.value = []
  selectedMappingItems.value = []
  mappingCurrentPage.value = 1
  Object.assign(mappingSearchForm, {
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
  mappingProductSearchText.value = ''
  showMappingProductDropdown.value = false
  filteredPaperTypes.value = []
  Object.assign(newMapping, {
    productName: '',
    compatibilityStatus: 'NA',
    paperType: ''
  })
  filteredPaperTypes.value = []
}

const loadMappingConfigs = async (patternAreaId: number) => {
  try {
    mappingConfigs.value = await hotStampingPatternAreaCompatibilityApi.getCompatibilityByPatternAreaId(patternAreaId)
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
    const response = await fetch('/api/api/product-management/options/paper-types')
    if (response.ok) {
      const paperTypes = await response.json()
      availablePaperTypes.value = paperTypes || []
    } else {
      console.error('加载烫金纸性能类型失败')
      availablePaperTypes.value = []
    }
  } catch (error) {
    console.error('加载烫金纸性能类型失败:', error)
    availablePaperTypes.value = []
  }
}

// 搜索和分页方法
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
    alert('请选择要删除的映射配置')
    return
  }

  if (confirm(`确定要删除选中的 ${selectedMappingItems.value.length} 个映射配置吗？`)) {
    try {
      const deletePromises = selectedMappingItems.value.map(id => 
        hotStampingPatternAreaCompatibilityApi.deleteCompatibility(id)
      )
      await Promise.all(deletePromises)
      
      if (selectedPatternArea.value) {
        await loadMappingConfigs(selectedPatternArea.value.id)
      }
      selectedMappingItems.value = []
    } catch (error) {
      console.error('批量删除映射配置失败:', error)
      alert('批量删除失败，请重试')
    }
  }
}

// 对话框方法
const openAddMappingDialog = async () => {
  editingMappingId.value = null
  newMappingProductSearchText.value = ''
  showNewMappingProductDropdown.value = false
  Object.assign(newMapping, {
    productName: '',
    compatibilityStatus: 'NA',
    paperType: ''
  })
  newMappingPaperTypes.value = []
  showAddMappingDialog.value = true
  
  if (availableProducts.value.length === 0) {
    await loadAvailableProducts()
  }
}

const closeAddMappingDialog = () => {
  showAddMappingDialog.value = false
  editingMappingId.value = null
  newMappingProductSearchText.value = ''
  showNewMappingProductDropdown.value = false
  Object.assign(newMapping, {
    productName: '',
    compatibilityStatus: 'NA',
    paperType: ''
  })
  newMappingPaperTypes.value = []
}

// 编辑映射
const editMapping = (mapping: HotStampingPatternAreaCompatibility) => {
  editingMappingId.value = mapping.id
  newMappingProductSearchText.value = mapping.productName
  Object.assign(newMapping, {
    productName: mapping.productName,
    paperType: mapping.paperType || '',
    compatibilityStatus: mapping.compatibilityStatus
  })
  showAddMappingDialog.value = true
  
  if (mapping.productName) {
    onNewMappingProductNameChange()
  }
}

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

// 联动选择方法
const onSearchProductNameChange = async () => {
  if (mappingSearchForm.productName) {
    loadingPaperTypes.value = true
    try {
      const response = await fetch(`/api/api/product-management/search/name?name=${encodeURIComponent(mappingSearchForm.productName)}`)
      if (response.ok) {
        const products = await response.json()
        const paperTypes = [...new Set(products
          .map((product: any) => product.hotStampingPaperType)
          .filter((type: string) => type && type.trim() !== '')
        )] as string[]
        filteredPaperTypes.value = paperTypes
      } else {
        console.error('加载烫金纸性能类型失败')
        filteredPaperTypes.value = []
      }
    } catch (error) {
      console.error('加载烫金纸性能类型失败:', error)
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

const onNewMappingProductNameChange = async () => {
  if (newMapping.productName) {
    loadingPaperTypes.value = true
    try {
      const response = await fetch(`/api/api/product-management/search/name?name=${encodeURIComponent(newMapping.productName)}`)
      if (response.ok) {
        const products = await response.json()
        const paperTypes = [...new Set(products
          .map((product: any) => product.hotStampingPaperType)
          .filter((type: string) => type && type.trim() !== '')
        )] as string[]
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
  } else {
    newMappingPaperTypes.value = []
  }
}

const saveMapping = async (mapping: HotStampingPatternAreaCompatibility) => {
  try {
    if (mapping.id) {
      await hotStampingPatternAreaCompatibilityApi.updateCompatibility(mapping.id, mapping)
      console.log('映射配置保存成功')
    }
  } catch (error) {
    console.error('保存映射配置失败:', error)
  }
}

const deleteMapping = async (id: number) => {
  if (confirm('确定要删除这个映射配置吗？')) {
    try {
      await hotStampingPatternAreaCompatibilityApi.deleteCompatibility(id)
      if (selectedPatternArea.value) {
        await loadMappingConfigs(selectedPatternArea.value.id)
      }
    } catch (error) {
      console.error('删除映射配置失败:', error)
    }
  }
}

const addMapping = async () => {
  if (!newMapping.productName || !selectedPatternArea.value) {
    alert('请选择产品')
    return
  }

  try {
    const mappingData = {
      productName: newMapping.productName,
      hotStampingPatternxAreaId: selectedPatternArea.value.id,
      compatibilityStatus: newMapping.compatibilityStatus,
      paperType: newMapping.paperType
    }

    if (editingMappingId.value) {
      // 编辑模式：使用 PUT 请求
      await hotStampingPatternAreaCompatibilityApi.updateCompatibility(editingMappingId.value, mappingData)
    } else {
      // 新增模式：使用 POST 请求
      await hotStampingPatternAreaCompatibilityApi.createCompatibility(mappingData)
    }

    if (selectedPatternArea.value) {
      await loadMappingConfigs(selectedPatternArea.value.id)
    }
    closeAddMappingDialog()
  } catch (error: any) {
    console.error('保存映射配置失败:', error)
    
    // 处理重复数据错误
    if (error.response?.data?.error || error.response?.status === 400) {
      alert('该燙金紙系列和烫金图案区域的组合已存在，请选择其他组合')
    } else {
      alert('保存映射配置失败，请重试')
    }
  }
}

// 清空选择
const clearMappingSelection = () => {
  selectedMappingItems.value = []
}

// 复制映射
const copyMapping = (mapping: HotStampingPatternAreaCompatibility) => {
  editingMappingId.value = null
  newMappingProductSearchText.value = mapping.productName
  Object.assign(newMapping, {
    productName: mapping.productName,
    compatibilityStatus: mapping.compatibilityStatus,
    paperType: mapping.paperType || ''
  })
  showAddMappingDialog.value = true
  
  if (mapping.productName) {
    onNewMappingProductNameChange()
  }
}

// 燙金紙系列搜索相关方法
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
  // 延迟关闭，以便点击下拉选项时能触发
  setTimeout(() => {
    showNewMappingProductDropdown.value = false
  }, 200)
}

// 批量添加相关方法
const openBatchAddDialog = async () => {
  if (!selectedPatternArea.value) {
    alert('请先选择图案区域')
    return
  }
  
  Object.assign(batchAddForm, {
    selectedSeries: [],
    paperType: '',
    compatibilityStatus: 'NA'
  })
  
  await loadAvailablePaperTypes()
  
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
  
  showBatchAddDialog.value = true
}

const closeBatchAddDialog = () => {
  showBatchAddDialog.value = false
  Object.assign(batchAddForm, {
    selectedSeries: [],
    paperType: '',
    compatibilityStatus: 'NA'
  })
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
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
  batchAddForm.selectedSeries = []
  batchAddSeriesSearchText.value = ''
  
  if (batchAddForm.paperType) {
    await loadSeriesByPaperType(batchAddForm.paperType)
  } else {
    batchAddAvailableSeries.value = []
  }
}

const batchAddMappings = async () => {
  if (!selectedPatternArea.value) {
    alert('请先选择图案区域')
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
    const compatibilities = batchAddForm.selectedSeries.map(seriesName => ({
      productName: seriesName,
      hotStampingPatternxAreaId: selectedPatternArea.value!.id,
      paperType: batchAddForm.paperType,
      compatibilityStatus: batchAddForm.compatibilityStatus
    }))
    
    let successCount = 0
    let failCount = 0
    const errors: string[] = []
    
    for (const compatibility of compatibilities) {
      try {
        const response = await fetch('/api/api/hot-stamping-pattern-area-compatibility', {
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
      if (selectedPatternArea.value) {
        await loadMappingConfigs(selectedPatternArea.value.id)
      }
      closeBatchAddDialog()
    }
  } catch (error) {
    console.error('批量添加映射配置失败:', error)
    alert('批量添加失败，请重试')
  }
}

// 导出相关方法
const exportMappings = async () => {
  if (!selectedPatternArea.value) {
    alert('请先选择图案区域')
    return
  }
  
  try {
    const response = await fetch(
      `/api/api/hot-stamping-pattern-area-compatibility/export?patternAreaId=${selectedPatternArea.value.id}`
    )
    
    if (!response.ok) {
      throw new Error('导出失败')
    }
    
    const contentDisposition = response.headers.get('Content-Disposition')
    let fileName = '烫金图案区域映射配置.xlsx'
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
    console.error('导出失败:', error)
    alert('导出失败，请重试')
  }
}

// 导入相关方法
const openImportDialog = () => {
  if (!selectedPatternArea.value) {
    alert('请先选择图案区域')
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
    const response = await fetch('/api/api/hot-stamping-pattern-area-compatibility/import-template', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
      }
    })
    
    if (!response.ok) {
      throw new Error('下载模板失败')
    }
    
    const contentDisposition = response.headers.get('Content-Disposition')
    let fileName = '烫金图案区域映射配置导入模板.xlsx'
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
  
  if (!selectedPatternArea.value) {
    alert('请先选择图案区域')
    return
  }
  
  try {
    const formData = new FormData()
    formData.append('file', selectedFile.value)
    formData.append('patternAreaId', selectedPatternArea.value.id.toString())
    
    const response = await fetch('/api/api/hot-stamping-pattern-area-compatibility/import', {
      method: 'POST',
      body: formData
    })
    
    const result = await response.json()
    importResult.value = {
      success: result.success,
      message: result.message,
      details: result.details || []
    }
    
    // 无论成功还是部分成功，都刷新列表
    if (selectedPatternArea.value) {
      await loadMappingConfigs(selectedPatternArea.value.id)
      // 重置分页到第一页
      mappingCurrentPage.value = 1
      // 重置搜索条件
      resetMappingSearch()
    }
    
    if (result.success) {
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
        if (mapping) {
          const updatedMapping = { ...mapping, compatibilityStatus: batchStatusForm.compatibilityStatus }
          await hotStampingPatternAreaCompatibilityApi.updateCompatibility(id, updatedMapping)
          successCount++
        }
      } catch (error) {
        failCount++
        errors.push(`ID ${id}: 更新失败`)
      }
    }
    
    if (failCount === 0) {
      alert(`批量修改完成！成功修改 ${successCount} 条记录`)
    } else {
      const errorMessages = errors.join('\n')
      alert(`批量修改完成！成功：${successCount} 条，失败：${failCount} 条\n\n失败详情：\n${errorMessages}`)
    }
    
    if (successCount > 0) {
      if (selectedPatternArea.value) {
        await loadMappingConfigs(selectedPatternArea.value.id)
      }
      selectedMappingItems.value = []
      closeBatchEditStatusDialog()
    }
  } catch (error) {
    console.error('批量修改状态失败:', error)
    alert('批量修改失败，请重试')
  }
}

// 生命周期
// 查看所有配置映射的计算属性
const filteredAllMappings = computed(() => {
  let filtered = allMappings.value

  // 图案区域筛选（支持模糊匹配）
  if (allMappingsSearchForm.optionName) {
    const optionName = allMappingsSearchForm.optionName.trim().toLowerCase()
    if (optionName) {
      filtered = filtered.filter(mapping => 
        mapping.optionName && mapping.optionName.trim().toLowerCase().includes(optionName)
      )
    }
  }

  // 燙金紙系列筛选（支持模糊匹配）
  if (allMappingsSearchForm.productName) {
    const searchText = allMappingsSearchForm.productName.trim()
    if (searchText) {
      const productName = searchText.toLowerCase()
      filtered = filtered.filter(mapping => {
        if (!mapping.productName) return false
        // 确保转换为字符串并去除所有空白字符
        const mappingName = String(mapping.productName).replace(/\s+/g, '').toLowerCase()
        const searchName = productName.replace(/\s+/g, '')
        // 使用 includes 进行子串匹配
        return mappingName.includes(searchName)
      })
    }
  }

  // 烫金纸性能类型筛选（支持模糊匹配）
  if (allMappingsSearchForm.paperType) {
    const paperType = allMappingsSearchForm.paperType.trim().toLowerCase()
    if (paperType) {
      filtered = filtered.filter(mapping => 
        mapping.paperType && mapping.paperType.trim().toLowerCase().includes(paperType)
      )
    }
  }

  // 兼容性状态筛选
  if (allMappingsSearchForm.compatibilityStatus !== '') {
    filtered = filtered.filter(mapping => 
      mapping.compatibilityStatus === allMappingsSearchForm.compatibilityStatus
    )
  }

  // 按图案区域和燙金紙系列排序
  filtered.sort((a, b) => {
    if (a.optionName !== b.optionName) {
      return (a.optionName || '').localeCompare(b.optionName || '')
    }
    return (a.productName || '').localeCompare(b.productName || '')
  })

  // 调试：检查筛选后的结果
  if (allMappingsSearchForm.productName && allMappingsSearchForm.productName.trim() === 'SY6+') {
    const productNames = filtered.map(m => m.productName).filter(Boolean)
    console.log('筛选后的产品系列列表:', productNames)
    console.log('筛选后的总数:', filtered.length)
    console.log('是否包含SY6:', productNames.some(name => name && name.trim().toLowerCase() === 'sy6'))
    console.log('是否包含SSY6:', productNames.some(name => name && name.trim().toLowerCase() === 'ssy6'))
  }

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
  await loadAllMappings()
  // 重置搜索表单
  Object.assign(allMappingsSearchForm, {
    optionName: '',
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
    // 直接使用后端API获取所有映射（后端已包含optionName）
    const allMappingsData = await hotStampingPatternAreaCompatibilityApi.getAllCompatibility()
    
    allMappings.value = allMappingsData
    allMappingsTotalItems.value = allMappingsData.length
  } catch (error) {
    console.error('加载所有配置映射失败:', error)
    alert('加载所有配置映射失败')
  }
}

const searchAllMappings = () => {
  allMappingsCurrentPage.value = 1
  // 强制触发响应式更新
  nextTick(() => {
    console.log('搜索后，filteredAllMappings 长度:', filteredAllMappings.value.length)
    console.log('搜索后，第一项的产品系列:', filteredAllMappings.value[0]?.productName)
  })
}

const resetAllMappingsSearch = () => {
  Object.assign(allMappingsSearchForm, {
    optionName: '',
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
  allMappingsCurrentPage.value = 1
}

const viewMappingDetails = (mapping: any) => {
  // 找到对应的图案区域
  const patternArea = patternAreas.value.find(p => p.optionName === mapping.optionName)
  if (patternArea) {
    // 打开该图案区域的配置映射对话框
    configMapping(patternArea)
  } else {
    alert('无法找到对应的图案区域配置')
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
      
      return hotStampingPatternAreaCompatibilityApi.updateCompatibility(id, finalData)
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
      hotStampingPatternAreaCompatibilityApi.deleteCompatibility(id)
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

onMounted(() => {
  loadPatternAreas()
})
</script>

<style scoped>
/* 可以添加特定的样式 */
</style>
