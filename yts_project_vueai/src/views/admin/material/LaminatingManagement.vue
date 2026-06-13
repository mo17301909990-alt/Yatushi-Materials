<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">過膠選項管理</h1>
            <p class="mt-2 text-gray-600">管理過膠後加工的配置信息</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="showAddDialog = true"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加過膠配置
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
            <button
              @click="showManageMaterials = true"
              class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
              </svg>
              管理過膠材料
            </button>
            <button
              @click="showManageSteps = true"
              class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
              </svg>
              管理過膠后工藝
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
          <!-- 产品系列 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">产品系列</label>
            <input
              v-model="searchProductName"
              @blur="onSearchProductNameChange"
              type="text"
              placeholder="输入产品系列进行筛选"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>

          <!-- 产品型号 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
            <select
              v-model="searchForm.modelNumber"
              :disabled="!searchProductName || loadingProductModels"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
            >
              <option value="">
                {{ loadingProductModels ? '加載中...' : !searchProductName ? '請先輸入產品系列' : '全部產品型號' }}
              </option>
              <option v-for="model in availableProductModels" :key="model" :value="model">
                {{ model }}
              </option>
            </select>
          </div>

          <!-- 燙金紙性能類型 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙性能類型</label>
            <select
              v-model="searchForm.productType"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">全部</option>
              <option value="普通金紙">普通金紙</option>
              <option value="普通耐磨">普通耐磨</option>
              <option value="高耐磨">高耐磨</option>
            </select>
          </div>

          <!-- 过胶材料 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">过胶材料</label>
            <select
              v-model="searchForm.laminationMaterialId"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">全部</option>
              <option v-for="material in materialOptions" :key="material.id" :value="material.id">
                {{ material.materialName }}
              </option>
            </select>
          </div>

          <!-- 过胶后工藝（post_processing_step_id） -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">过胶后工藝</label>
            <select
              v-model="searchForm.postProcessingStepId"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">全部</option>
              <option v-for="step in processingOptions" :key="step.id" :value="step.id">
                {{ step.stepName }}
              </option>
            </select>
          </div>

          <!-- 兼容性 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">兼容性</label>
            <select
              v-model="searchForm.compatibility"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">全部</option>
              <option value="V">兼容</option>
              <option value="X">不兼容</option>
            </select>
          </div>
        </div>

        <div class="flex items-end space-x-3 mt-4">
          <button
            @click="searchCompatibility"
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
              @click="batchDeleteCompatibility"
              class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-red-600 hover:bg-red-700 mr-2"
            >
              批量删除
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

      <!-- 过胶兼容性列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">过胶兼容性列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left">
                  <input
                    type="checkbox"
                    :checked="selectedItems.length === compatibilityList.length && compatibilityList.length > 0"
                    @change="toggleSelectAll"
                    class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                  />
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  产品系列
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  型号
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  產品類型
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  过胶材料
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  过胶后工藝
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  兼容性
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  街货
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  操作
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in compatibilityList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <input
                    type="checkbox"
                    :checked="selectedItems.includes(item.id as number)"
                    @change="toggleSelectItem(item.id as number)"
                    class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                  />
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {{ item.foilSeries }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ item.modelNumber || '-' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ item.productType || '-' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <div>
                    <div class="font-medium">{{ item.laminationMaterialName || '-' }}</div>
                    <div v-if="item.laminationMaterialDescription" class="text-xs text-gray-500 mt-1">
                      {{ item.laminationMaterialDescription }}
                    </div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ item.postProcessingStepName || '-' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span 
                    :class="item.compatibility === 'V' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ item.compatibility === 'V' ? '兼容' : '不兼容' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <span 
                    :class="item.isJiehuo ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ item.isJiehuo ? '是' : '否' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center space-x-2">
                    <button
                      @click="copyCompatibility(item)"
                      class="text-blue-600 hover:text-blue-900"
                      title="复制"
                    >
                      复制
                    </button>
                    <button
                      @click="editCompatibilityItem(item)"
                      class="text-green-600 hover:text-green-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="deleteCompatibility(item.id as number)"
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
        <div v-if="compatibilityList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暂无兼容性配置数据</div>
        </div>

        <!-- 分页 -->
        <div v-if="pagination.totalPages > 1" class="px-6 py-4 border-t border-gray-200">
          <div class="flex items-center justify-between">
            <div class="flex-1 flex justify-between sm:hidden">
              <button
                @click="changePage(pagination.page - 1)"
                :disabled="pagination.page <= 1"
                class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                上一页
              </button>
              <button
                @click="changePage(pagination.page + 1)"
                :disabled="pagination.page >= pagination.totalPages"
                class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                下一页
              </button>
            </div>
            <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
              <div>
                <p class="text-sm text-gray-700">
                  <span v-if="pagination.total > 0">
                    显示第 <span class="font-medium">{{ (pagination.page - 1) * pagination.size + 1 }}</span> 到
                    <span class="font-medium">{{ Math.min(pagination.page * pagination.size, pagination.total) }}</span> 条，
                    共 <span class="font-medium">{{ pagination.total }}</span> 条记录
                  </span>
                  <span v-else>
                    暂无数据
                  </span>
                </p>
              </div>
              <div>
                <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
                  <button
                    @click="changePage(pagination.page - 1)"
                    :disabled="pagination.page <= 1"
                    class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    上一页
                  </button>
                  <button
                    v-for="page in visiblePages"
                    :key="page"
                    @click="changePage(page)"
                    :class="page === pagination.page ? 'bg-green-50 border-green-500 text-green-600' : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'"
                    class="relative inline-flex items-center px-4 py-2 border text-sm font-medium"
                  >
                    {{ page }}
                  </button>
                  <button
                    @click="changePage(pagination.page + 1)"
                    :disabled="pagination.page >= pagination.totalPages"
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

      <!-- 添加/编辑兼容性对话框 -->
      <div v-if="showAddDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">添加过胶兼容性规则</h3>
              <button @click="closeAddDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveNewCompatibility" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <!-- 产品名称（可搜索下拉框） -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">产品系列 *</label>
                  <div class="relative">
                    <input
                      type="text"
                      v-model="productSeriesSearchText"
                      @input="onProductSeriesSearchInput"
                      @focus="showProductSeriesDropdown = true"
                      @blur="handleProductSeriesInputBlur"
                      placeholder="请输入或选择产品系列"
                      required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <div
                      v-if="showProductSeriesDropdown && filteredProductSeries.length > 0"
                      class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto"
                      @mousedown.prevent
                    >
                      <div
                        v-for="series in filteredProductSeries"
                        :key="series"
                        @mousedown.prevent="selectProductSeries(series)"
                        class="px-3 py-2 cursor-pointer hover:bg-blue-50"
                        :class="{ 'bg-blue-100': newCompatibility.foilSeries === series }"
                      >
                        <div class="text-sm text-gray-900">{{ series }}</div>
                      </div>
                      <div v-if="filteredProductSeries.length === 0" class="px-3 py-2 text-sm text-gray-500">
                        未找到匹配的产品系列
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 产品型号 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">产品型号</label>
                  <select
                    v-model="newCompatibility.modelNumber"
                    @change="onProductModelChange"
                    :disabled="!newCompatibility.foilSeries || loadingProductModels"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingProductModels ? '加載中...' : !newCompatibility.foilSeries ? '請先選擇產品名稱' : '請選擇產品型號（可選）' }}
                    </option>
                    <option v-for="model in availableProductModels" :key="model" :value="model">
                      {{ model }}
                    </option>
                  </select>
                </div>

                <!-- 燙金紙性能類型 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">燙金紙性能類型 *</label>
                  <select
                    v-model="newCompatibility.productType"
                    :disabled="!newCompatibility.foilSeries || loadingProductTypes"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingProductTypes ? '加载中...' : !newCompatibility.foilSeries ? '请先选择产品系列' : '请选择燙金紙性能類型' }}
                    </option>
                    <option v-for="type in availableProductTypes" :key="type" :value="type">
                      {{ type }}
                    </option>
                  </select>
                </div>

                <!-- 过胶材料 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">过胶材料 *</label>
                  <select
                    v-model="newCompatibility.laminationMaterialId"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">请选择过胶材料</option>
                    <option v-for="material in materialOptions" :key="material.id" :value="material.id">
                      {{ material.materialName }}
                    </option>
                  </select>
                </div>

                <!-- 过胶后工藝（post_processing_step_id） -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">过胶后工藝 *</label>
                  <select
                    v-model="newCompatibility.postProcessingStepId"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">请选择过胶后工藝</option>
                    <option v-for="step in processingOptions" :key="step.id" :value="step.id">
                      {{ step.stepName }}
                    </option>
                  </select>
                </div>

                <!-- 兼容性 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">兼容性 *</label>
                  <select
                    v-model="newCompatibility.compatibility"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">请选择兼容性</option>
                    <option value="V">兼容</option>
                    <option value="X">不兼容</option>
                  </select>
                </div>

                <!-- 街货 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">街货</label>
                  <div class="flex items-center space-x-2">
                    <input
                      v-model="newCompatibility.isJiehuo"
                      type="checkbox"
                      class="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded"
                    />
                    <span class="text-sm text-gray-700">是否街货</span>
                  </div>
                </div>
              </div>

              <!-- 按钮 -->
              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button
                  type="button"
                  @click="closeAddDialog"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 transition-colors duration-200"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                  </svg>
                  取消
                </button>
                <button
                  type="submit"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 transition-colors duration-200"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                  </svg>
                  保存
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 编辑兼容性对话框 -->
      <div v-if="showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">编辑过胶兼容性规则</h3>
              <button @click="closeEditDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveEditCompatibility" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <!-- 产品系列（可搜索下拉框） -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">产品系列 *</label>
                  <div class="relative">
                    <input
                      type="text"
                      v-model="editProductSeriesSearchText"
                      @input="onEditProductSeriesSearchInput"
                      @focus="showEditProductSeriesDropdown = true"
                      @blur="handleEditProductSeriesInputBlur"
                      placeholder="请输入或选择产品系列"
                      required
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                    <div
                      v-if="showEditProductSeriesDropdown && filteredEditProductSeries.length > 0"
                      class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto"
                      @mousedown.prevent
                    >
                      <div
                        v-for="series in filteredEditProductSeries"
                        :key="series"
                        @mousedown.prevent="selectEditProductSeries(series)"
                        class="px-3 py-2 cursor-pointer hover:bg-blue-50"
                        :class="{ 'bg-blue-100': editCompatibility.foilSeries === series }"
                      >
                        <div class="text-sm text-gray-900">{{ series }}</div>
                      </div>
                      <div v-if="filteredEditProductSeries.length === 0" class="px-3 py-2 text-sm text-gray-500">
                        未找到匹配的产品系列
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 产品型号 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">产品型号</label>
                  <select
                    v-model="editCompatibility.modelNumber"
                    @change="onEditProductModelChange"
                    :disabled="!editCompatibility.foilSeries || loadingProductModels"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingProductModels ? '加載中...' : !editCompatibility.foilSeries ? '請先選擇產品系列' : '請選擇產品型號（可選）' }}
                    </option>
                    <option v-for="model in availableProductModels" :key="model" :value="model">
                      {{ model }}
                    </option>
                  </select>
                </div>

                <!-- 燙金紙性能類型 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">燙金紙性能類型 *</label>
                  <select
                    v-model="editCompatibility.productType"
                    :disabled="!editCompatibility.foilSeries || loadingProductTypes"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingProductTypes ? '加载中...' : !editCompatibility.foilSeries ? '请先选择产品系列' : '请选择燙金紙性能類型' }}
                    </option>
                    <option v-for="type in availableProductTypes" :key="type" :value="type">
                      {{ type }}
                    </option>
                  </select>
                </div>

                <!-- 过胶材料 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">过胶材料 *</label>
                  <select
                    v-model="editCompatibility.laminationMaterialId"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">请选择过胶材料</option>
                    <option v-for="material in materialOptions" :key="material.id" :value="material.id">
                      {{ material.materialName }}
                    </option>
                  </select>
                </div>

                <!-- 过胶后工藝（post_processing_step_id） -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">过胶后工藝 *</label>
                  <select
                    v-model="editCompatibility.postProcessingStepId"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">请选择过胶后工藝</option>
                    <option v-for="step in processingOptions" :key="step.id" :value="step.id">
                      {{ step.stepName }}
                    </option>
                  </select>
                </div>

                <!-- 兼容性 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">兼容性 *</label>
                  <select
                    v-model="editCompatibility.compatibility"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">请选择兼容性</option>
                    <option value="V">兼容</option>
                    <option value="X">不兼容</option>
                  </select>
                </div>

                <!-- 街货 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">街货</label>
                  <div class="flex items-center space-x-2">
                    <input
                      v-model="editCompatibility.isJiehuo"
                      type="checkbox"
                      class="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded"
                    />
                    <span class="text-sm text-gray-700">是否街货</span>
                  </div>
                </div>
              </div>

              <!-- 按钮 -->
              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button
                  type="button"
                  @click="closeEditDialog"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 transition-colors duration-200"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                  </svg>
                  取消
                </button>
                <button
                  type="submit"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 transition-colors duration-200"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                  </svg>
                  保存
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 管理过胶材料 对话框 -->
      <div v-if="showManageMaterials" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-8 mx-auto p-6 border w-11/12 max-w-6xl shadow-lg rounded-md bg-white">
          <div class="flex items-center justify-between mb-6">
            <div>
              <h3 class="text-lg font-medium text-gray-900">管理过胶材料</h3>
              <p class="text-sm text-gray-500 mt-1">管理过胶材料的配置信息</p>
            </div>
            <button @click="showManageMaterials = false" class="text-gray-400 hover:text-gray-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
            </button>
          </div>

          <!-- 搜索和筛选 -->
          <div class="mb-6 bg-white rounded-lg shadow-md p-6">
            <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">搜索材料</label>
                <input 
                  v-model="materialSearch" 
                  placeholder="输入材料名称" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" 
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">状态筛选</label>
                <select 
                  v-model="materialStatusFilter" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="">全部状态</option>
                  <option value="true">启用</option>
                  <option value="false">禁用</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">排序方式</label>
                <select 
                  v-model="materialSortBy" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="displayOrder">显示顺序</option>
                  <option value="materialName">材料名称</option>
                  <option value="isActive">启用状态</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">排序方向</label>
                <select 
                  v-model="materialSortOrder" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="asc">升序</option>
                  <option value="desc">降序</option>
                </select>
              </div>
              <div class="flex items-end">
                <button 
                  @click="clearMaterialSearch" 
                  class="w-full bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-colors"
                >
                  清除筛选
                </button>
              </div>
            </div>
          </div>

          <!-- 添加/编辑表单 -->
          <div class="mb-6 bg-white border rounded-lg p-4">
            <h4 class="text-lg font-medium text-gray-900 mb-4">{{ materialForm.id ? '编辑材料' : '添加新材料' }}</h4>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">材料名称 *</label>
                <input 
                  v-model="materialForm.materialName" 
                  placeholder="请输入材料名称" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" 
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">显示顺序</label>
                <input 
                  v-model.number="materialForm.displayOrder" 
                  type="number" 
                  placeholder="显示顺序" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" 
                />
              </div>
              <div class="flex items-end">
                <label class="inline-flex items-center space-x-2 text-sm text-gray-700">
                  <input 
                    v-model="materialForm.isActive" 
                    type="checkbox" 
                    class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                  />
                  <span>启用状态</span>
                </label>
              </div>
            </div>
            <div class="mt-4">
              <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
              <textarea 
                v-model="materialForm.description" 
                placeholder="请输入材料描述" 
                rows="2"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              ></textarea>
            </div>
            <div class="mt-4 flex space-x-3">
              <button 
                @click="saveMaterial()" 
                class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors"
              >
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                {{ materialForm.id ? '更新材料' : '添加材料' }}
              </button>
              <button 
                v-if="materialForm.id" 
                @click="resetMaterialForm()" 
                class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition-colors"
              >
                取消编辑
              </button>
            </div>
          </div>

          <!-- 批量操作工具栏 -->
          <div v-if="selectedMaterials.length > 0" class="mb-4 bg-gray-50 border rounded-lg p-4">
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-4">
                <span class="text-sm font-medium text-gray-900">已选择 {{ selectedMaterials.length }} 项</span>
                <div class="flex items-center space-x-2">
                  <button 
                    @click="batchEnableMaterials" 
                    class="px-3 py-1.5 text-sm bg-green-600 text-white rounded-md hover:bg-green-700"
                  >
                    批量启用
                  </button>
                  <button 
                    @click="batchDisableMaterials" 
                    class="px-3 py-1.5 text-sm bg-yellow-600 text-white rounded-md hover:bg-yellow-700"
                  >
                    批量禁用
                  </button>
                  <button 
                    @click="batchDeleteMaterials" 
                    class="px-3 py-1.5 text-sm bg-red-600 text-white rounded-md hover:bg-red-700"
                  >
                    批量删除
                  </button>
                  <button 
                    @click="exportMaterials" 
                    class="px-3 py-1.5 text-sm bg-blue-600 text-white rounded-md hover:bg-blue-700 flex items-center"
                  >
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                    </svg>
                    导出
                  </button>
                </div>
              </div>
              <button 
                @click="clearSelectedMaterials" 
                class="text-gray-600 hover:text-gray-800 text-sm"
              >
                取消选择
              </button>
            </div>
          </div>

          <!-- 数据表格 -->
          <div class="overflow-x-auto border rounded-md">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-4 py-2 text-left">
                    <input 
                      type="checkbox" 
                      :checked="selectedMaterials.length === filteredMaterialOptions.length && filteredMaterialOptions.length > 0"
                      @change="toggleSelectAllMaterials"
                      class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                    />
                  </th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">名称</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">顺序</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">启用</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">描述</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="m in filteredMaterialOptions" :key="m.id">
                  <td class="px-4 py-2 text-sm">
                    <input 
                      type="checkbox" 
                      :checked="selectedMaterials.includes(m.id as number)"
                      @change="toggleSelectMaterial(m.id as number)"
                      class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                    />
                  </td>
                  <td class="px-4 py-2 text-sm">{{ m.materialName }}</td>
                  <td class="px-4 py-2 text-sm">{{ m.displayOrder ?? '-' }}</td>
                  <td class="px-4 py-2 text-sm">{{ m.isActive ? '是' : '否' }}</td>
                  <td class="px-4 py-2 text-sm">{{ m.description ?? '-' }}</td>
                  <td class="px-4 py-2 text-sm space-x-3">
                    <button @click="editMaterial(m)" class="text-blue-600 hover:text-blue-800">编辑</button>
                    <button @click="deleteMaterial(m.id as number)" class="text-red-600 hover:text-red-800">删除</button>
                  </td>
                </tr>
              </tbody>
            </table>
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
                    v-model="batchEditForm.foilSeries"
                    type="text"
                    placeholder="留空则不修改"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <!-- 产品型号 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
                  <input
                    v-model="batchEditForm.modelNumber"
                    type="text"
                    placeholder="留空则不修改"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <!-- 烫金纸性能类型 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                  <input
                    v-model="batchEditForm.productType"
                    type="text"
                    placeholder="留空则不修改"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>

                <!-- 过胶材料 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">过胶材料</label>
                  <select
                    v-model="batchEditForm.laminationMaterialId"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option :value="null">不修改</option>
                    <option v-for="material in materialOptions" :key="material.id" :value="material.id">
                      {{ material.description }}
                    </option>
                  </select>
                </div>

                <!-- 过胶后工艺 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">过胶后工艺</label>
                  <select
                    v-model="batchEditForm.postProcessingStepId"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option :value="null">不修改</option>
                    <option v-for="option in processingOptions" :key="option.id" :value="option.id">
                      {{ option.stepName }}
                    </option>
                  </select>
                </div>

                <!-- 兼容性状态 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
                  <select
                    v-model="batchEditForm.compatibility"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">不修改</option>
                    <option value="V">兼容</option>
                    <option value="X">不兼容</option>
                  </select>
                </div>

                <!-- 街货 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">街货</label>
                  <select
                    v-model="batchEditForm.isJiehuo"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option :value="null">不修改</option>
                    <option :value="true">是</option>
                    <option :value="false">否</option>
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

      <!-- 批量添加对话框 -->
      <div v-if="showBatchAddDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">批量添加过胶兼容性配置</h3>
              <button @click="closeBatchAddDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveBatchAdd" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <!-- 燙金紙性能類型 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">燙金紙性能類型 *</label>
                  <select
                    v-model="batchAddForm.productType"
                    @change="onBatchAddProductTypeChange"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="">请选择燙金紙性能類型</option>
                    <option value="普通金紙">普通金紙</option>
                    <option value="普通耐磨">普通耐磨</option>
                    <option value="高耐磨">高耐磨</option>
                  </select>
                </div>

                <!-- 过胶材料 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">过胶材料 *</label>
                  <select
                    v-model="batchAddForm.laminationMaterialId"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option :value="0">请选择过胶材料</option>
                    <option v-for="material in materialOptions" :key="material.id" :value="material.id">
                      {{ material.materialName }}
                    </option>
                  </select>
                </div>

                <!-- 过胶后工藝 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">过胶后工藝</label>
                  <select
                    v-model="batchAddForm.postProcessingStepId"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option :value="0">请选择过胶后工藝（可选）</option>
                    <option v-for="step in processingOptions" :key="step.id" :value="step.id">
                      {{ step.stepName }}
                    </option>
                  </select>
                </div>

                <!-- 兼容性 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">兼容性 *</label>
                  <select
                    v-model="batchAddForm.compatibility"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    <option value="V">兼容</option>
                    <option value="X">不兼容</option>
                  </select>
                </div>

                <!-- 街货 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">街货</label>
                  <div class="flex items-center space-x-2">
                    <input
                      v-model="batchAddForm.isJiehuo"
                      type="checkbox"
                      class="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded"
                    />
                    <span class="text-sm text-gray-700">是否街货</span>
                  </div>
                </div>
              </div>

              <!-- 产品系列选择 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">选择产品系列（可多选）*</label>
                <p v-if="!batchAddForm.productType" class="mb-2 text-sm text-yellow-600">
                  请先选择燙金紙性能類型
                </p>
                <!-- 搜索输入框 -->
                <div class="mb-2">
                  <input
                    type="text"
                    v-model="batchAddSeriesSearchText"
                    placeholder="搜索产品系列..."
                    :disabled="!batchAddForm.productType"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  />
                </div>
                <div 
                  class="border border-gray-300 rounded-md p-3 max-h-60 overflow-y-auto"
                  :class="{ 'bg-gray-50': !batchAddForm.productType }"
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
                        :disabled="!batchAddForm.productType"
                        class="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded disabled:opacity-50"
                      />
                      <span class="text-sm text-gray-700">{{ series }}</span>
                    </label>
                    <div v-if="filteredBatchAddSeries.length === 0" class="px-2 py-4 text-sm text-gray-500 text-center">
                      {{ !batchAddForm.productType ? '请先选择燙金紙性能類型' : '未找到匹配的产品系列' }}
                    </div>
                  </div>
                </div>
                <p class="mt-2 text-xs text-gray-500">
                  已选择 <span class="font-bold text-green-600">{{ batchAddForm.selectedSeries.length }}</span> 个产品系列
                  <span v-if="batchAddSeriesSearchText" class="ml-2">
                    （显示 {{ filteredBatchAddSeries.length }} 个匹配项）
                  </span>
                </p>
              </div>

              <!-- 为每个选中的产品系列显示产品型号选择 -->
              <div v-for="seriesName in batchAddForm.selectedSeries" :key="seriesName" class="mt-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">
                  产品系列「{{ seriesName }}」的产品型号选择（可选）
                </label>
                <p class="mb-2 text-xs text-gray-500">
                  如果不选择任何产品型号，则创建该产品系列的通用配置
                </p>
                <!-- 搜索输入框 -->
                <div class="mb-2" v-if="batchAddSeriesProducts[seriesName] && batchAddSeriesProducts[seriesName].length > 0">
                  <input
                    type="text"
                    v-model="batchAddModelSearchTexts[seriesName]"
                    placeholder="搜索产品型号..."
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>
                <div 
                  v-if="batchAddSeriesProducts[seriesName] && batchAddSeriesProducts[seriesName].length > 0"
                  class="border border-gray-300 rounded-md p-3 max-h-48 overflow-y-auto"
                >
                  <div class="space-y-2">
                    <label
                      v-for="product in filteredBatchAddProducts(seriesName)"
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
                        class="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded"
                      />
                      <span class="text-sm text-gray-700">
                        {{ product.modelNumber ? `${product.modelNumber}${product.name ? ' - ' + product.name : ''}` : product.name || `产品ID: ${product.id}` }}
                      </span>
                    </label>
                    <div v-if="filteredBatchAddProducts(seriesName).length === 0" class="px-2 py-4 text-sm text-gray-500 text-center">
                      未找到匹配的产品型号
                    </div>
                  </div>
                  <p class="mt-2 text-xs text-gray-500">
                    已选择 <span class="font-bold text-green-600">
                      {{ (batchAddForm.selectedProducts[seriesName] || []).length }}
                    </span> 个产品型号
                    <span v-if="(batchAddForm.selectedProducts[seriesName] || []).length === 0" class="text-yellow-600">
                      （将创建该产品系列的通用配置）
                    </span>
                    <span v-if="batchAddModelSearchTexts[seriesName]" class="ml-2 text-gray-500">
                      （显示 {{ filteredBatchAddProducts(seriesName).length }} 个匹配项）
                    </span>
                  </p>
                </div>
                <div v-else class="border border-gray-300 rounded-md p-3 bg-gray-50">
                  <p class="text-sm text-gray-500 text-center">
                    {{ batchAddSeriesProducts[seriesName] === undefined ? '加载中...' : '该产品系列下暂无产品型号' }}
                  </p>
                </div>
              </div>

              <!-- 按钮 -->
              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button
                  type="button"
                  @click="closeBatchAddDialog"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 transition-colors duration-200"
                >
                  取消
                </button>
                <button
                  type="submit"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 transition-colors duration-200"
                >
                  批量添加
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
              <button @click="closeBatchEditStatusDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <div class="mb-4">
              <p class="text-sm text-gray-600">已选中 <span class="font-medium text-gray-900">{{ selectedItems.length }}</span> 条记录</p>
            </div>

            <form @submit.prevent="saveBatchEditStatus" class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">兼容性状态 *</label>
                <select
                  v-model="batchEditStatusForm.compatibility"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
                </select>
              </div>

              <!-- 按钮 -->
              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button
                  type="button"
                  @click="closeBatchEditStatusDialog"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 transition-colors duration-200"
                >
                  取消
                </button>
                <button
                  type="submit"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 transition-colors duration-200"
                >
                  确认修改
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
              <h3 class="text-lg font-medium text-gray-900">导入过胶兼容性配置</h3>
              <button @click="closeImportDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <div class="space-y-4">
              <!-- 模板下载 -->
              <div class="bg-blue-50 border border-blue-200 rounded-lg p-4">
                <div class="flex items-center justify-between">
                  <div>
                    <h4 class="text-sm font-medium text-blue-900">导入模板</h4>
                    <p class="text-xs text-blue-700 mt-1">下载Excel导入模板，按照模板格式填写数据后导入</p>
                  </div>
                  <button
                    @click="downloadImportTemplate"
                    class="inline-flex items-center px-3 py-2 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700"
                  >
                    <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                    </svg>
                    下载模板
                  </button>
                </div>
              </div>

              <!-- 文件选择 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">选择Excel文件</label>
                <input
                  type="file"
                  @change="handleFileSelect"
                  accept=".xlsx,.xls"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
                <p v-if="importFile" class="mt-2 text-sm text-gray-600">已选择：{{ importFile.name }}</p>
              </div>

              <!-- 导入结果 -->
              <div v-if="importResult" class="border rounded-lg p-4" :class="importResult.success ? 'bg-green-50 border-green-200' : 'bg-red-50 border-red-200'">
                <h4 class="text-sm font-medium mb-2" :class="importResult.success ? 'text-green-900' : 'text-red-900'">
                  {{ importResult.success ? '导入成功' : '导入完成（有错误）' }}
                </h4>
                <p class="text-sm mb-2" :class="importResult.success ? 'text-green-700' : 'text-red-700'">
                  {{ importResult.message }}
                </p>
                <div class="text-sm space-y-1">
                  <p>总数：{{ importResult.totalCount }}</p>
                  <p class="text-green-700">成功：{{ importResult.successCount }}</p>
                  <p class="text-red-700">失败：{{ importResult.failCount }}</p>
                </div>
                <div v-if="importResult.errorMessages && importResult.errorMessages.length > 0" class="mt-3 max-h-40 overflow-auto">
                  <p class="text-xs font-medium text-red-900 mb-1">错误详情：</p>
                  <ul class="text-xs text-red-700 space-y-1">
                    <li v-for="(error, index) in importResult.errorMessages" :key="index">• {{ error }}</li>
                  </ul>
                </div>
              </div>

              <!-- 按钮 -->
              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button
                  type="button"
                  @click="closeImportDialog"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500 transition-colors duration-200"
                >
                  关闭
                </button>
                <button
                  @click="executeImport"
                  :disabled="!importFile || importing"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <span v-if="importing">导入中...</span>
                  <span v-else>开始导入</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 管理過膠后工藝 对话框 -->
      <div v-if="showManageSteps" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-8 mx-auto p-6 border w-11/12 max-w-6xl shadow-lg rounded-md bg-white">
          <div class="flex items-center justify-between mb-6">
            <div>
              <h3 class="text-lg font-medium text-gray-900">管理過膠后工藝</h3>
              <p class="text-sm text-gray-500 mt-1">管理過膠后工藝的配置信息</p>
            </div>
            <button @click="showManageSteps = false" class="text-gray-400 hover:text-gray-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
            </button>
          </div>

          <!-- 搜索和筛选 -->
          <div class="mb-6 bg-white rounded-lg shadow-md p-6">
            <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">搜索后工藝</label>
                <input 
                  v-model="stepSearch" 
                  placeholder="输入后工藝名称" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" 
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">状态筛选</label>
                <select 
                  v-model="stepStatusFilter" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="">全部状态</option>
                  <option value="true">启用</option>
                  <option value="false">禁用</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">排序方式</label>
                <select 
                  v-model="stepSortBy" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="displayOrder">显示顺序</option>
                  <option value="stepName">后工藝名称</option>
                  <option value="isActive">启用状态</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">排序方向</label>
                <select 
                  v-model="stepSortOrder" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="asc">升序</option>
                  <option value="desc">降序</option>
                </select>
              </div>
              <div class="flex items-end">
                <button 
                  @click="clearStepSearch" 
                  class="w-full bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-colors"
                >
                  清除筛选
                </button>
              </div>
            </div>
          </div>

          <!-- 添加/编辑表单 -->
          <div class="mb-6 bg-white border rounded-lg p-4">
            <h4 class="text-lg font-medium text-gray-900 mb-4">{{ stepForm.id ? '编辑后工藝' : '添加后工藝' }}</h4>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">后工藝名称 *</label>
                <input 
                  v-model="stepForm.stepName" 
                  placeholder="请输入后工藝名称" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" 
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">显示顺序</label>
                <input 
                  v-model.number="stepForm.displayOrder" 
                  type="number" 
                  placeholder="显示顺序" 
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" 
                />
              </div>
              <div class="flex items-end">
                <label class="inline-flex items-center space-x-2 text-sm text-gray-700">
                  <input 
                    v-model="stepForm.isActive" 
                    type="checkbox" 
                    class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                  />
                  <span>启用状态</span>
                </label>
              </div>
            </div>
            <div class="mt-4">
              <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
              <textarea 
                v-model="stepForm.description" 
                placeholder="请输入后工藝描述" 
                rows="2"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              ></textarea>
            </div>
            <div class="mt-4 flex space-x-3">
              <button 
                @click="saveStep()" 
                class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors"
              >
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7"></path>
                </svg>
                {{ stepForm.id ? '更新后工藝' : '添加后工藝' }}
              </button>
              <button 
                v-if="stepForm.id" 
                @click="resetStepForm()" 
                class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition-colors"
              >
                取消编辑
              </button>
            </div>
          </div>

          <!-- 批量操作工具栏 -->
          <div v-if="selectedSteps.length > 0" class="mb-4 bg-gray-50 border rounded-lg p-4">
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-4">
                <span class="text-sm font-medium text-gray-900">已选择 {{ selectedSteps.length }} 项</span>
                <div class="flex items-center space-x-2">
                  <button 
                    @click="batchEnableSteps" 
                    class="px-3 py-1.5 text-sm bg-green-600 text-white rounded-md hover:bg-green-700"
                  >
                    批量启用
                  </button>
                  <button 
                    @click="batchDisableSteps" 
                    class="px-3 py-1.5 text-sm bg-yellow-600 text-white rounded-md hover:bg-yellow-700"
                  >
                    批量禁用
                  </button>
                  <button 
                    @click="batchDeleteSteps" 
                    class="px-3 py-1.5 text-sm bg-red-600 text-white rounded-md hover:bg-red-700"
                  >
                    批量删除
                  </button>
                </div>
              </div>
              <button 
                @click="clearSelectedSteps" 
                class="text-gray-600 hover:text-gray-800 text-sm"
              >
                取消选择
              </button>
            </div>
          </div>

          <!-- 数据表格 -->
          <div class="overflow-x-auto border rounded-md">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-4 py-2 text-left">
                    <input 
                      type="checkbox" 
                      :checked="selectedSteps.length === filteredProcessingOptions.length && filteredProcessingOptions.length > 0"
                      @change="toggleSelectAllSteps"
                      class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                    />
                  </th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">名称</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">顺序</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">启用</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">描述</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="s in filteredProcessingOptions" :key="s.id">
                  <td class="px-4 py-2 text-sm">
                    <input 
                      type="checkbox" 
                      :checked="selectedSteps.includes(s.id as number)"
                      @change="toggleSelectStep(s.id as number)"
                      class="rounded border-gray-300 text-green-600 focus:ring-green-500"
                    />
                  </td>
                  <td class="px-4 py-2 text-sm">{{ s.stepName }}</td>
                  <td class="px-4 py-2 text-sm">{{ s.displayOrder ?? '-' }}</td>
                  <td class="px-4 py-2 text-sm">{{ s.isActive ? '是' : '否' }}</td>
                  <td class="px-4 py-2 text-sm">{{ s.description ?? '-' }}</td>
                  <td class="px-4 py-2 text-sm space-x-3">
                    <button @click="editStep(s)" class="text-blue-600 hover:text-blue-800">编辑</button>
                    <button @click="deleteStep(s.id as number)" class="text-red-600 hover:text-red-800">删除</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { laminatingApi, type LaminationCompatibility, type LaminationMaterialOption, type LaminationCompatibilityQuery, type PostProcessingOption } from '../../../api/modules/laminating'
import { sharedProductApi } from '../../../api/modules/postProcessingLeduvglitter'

// 响应式数据
const compatibilityList = ref<(LaminationCompatibility & { _changed?: boolean })[]>([])
const materialOptions = ref<LaminationMaterialOption[]>([])
// 燙金紙性能類型選項已改為固定選項，不再需要動態加載
const processingOptions = ref<PostProcessingOption[]>([])

// 联动选择相关数据
const availableProducts = ref<string[]>([])
const availableProductModels = ref<string[]>([])
const loadingProductModels = ref(false)
const availableProductTypes = ref<string[]>([])
const loadingProductTypes = ref(false)

// 添加对话框的产品系列搜索
const productSeriesSearchText = ref('')
const showProductSeriesDropdown = ref(false)

// 编辑对话框的产品系列搜索
const editProductSeriesSearchText = ref('')
const showEditProductSeriesDropdown = ref(false)

// 搜索表单
const searchForm = reactive<LaminationCompatibilityQuery>({
  foilSeries: '',
  productType: '',
  modelNumber: '',
  laminationMaterialId: undefined,
  compatibility: '',
  page: 1,
  size: 10
})

// 添加产品名称搜索字段
const searchProductName = ref('')

// 分页信息
const pagination = reactive({
  total: 0,
  page: 1,
  size: 10,
  totalPages: 0
})

// 新增兼容性表单
const newCompatibility = reactive<LaminationCompatibility>({
  foilSeries: '',
  modelNumber: '',
  productType: '',
  laminationMaterialId: 0,
  compatibility: 'V',
  postProcessingStepId: 0,
  isJiehuo: false
})

// 编辑兼容性表单
const editCompatibility = reactive<LaminationCompatibility>({
  id: undefined,
  foilSeries: '',
  modelNumber: '',
  productType: '',
  laminationMaterialId: 0,
  compatibility: 'V',
  postProcessingStepId: 0,
  isJiehuo: false
})

// 对话框状态
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showManageMaterials = ref(false)
const showManageSteps = ref(false)
const showBatchAddDialog = ref(false)
const showBatchEditStatusDialog = ref(false)
const showBatchEditDialog = ref(false)
const showImportDialog = ref(false)

// 批量选择相关
const selectedItems = ref<number[]>([])

// 过胶后工藝ID
const laminatingStepId = ref<number | null>(null)

// 计算可见页码
const visiblePages = computed(() => {
  const pages = []
  const start = Math.max(1, pagination.page - 2)
  const end = Math.min(pagination.totalPages, pagination.page + 2)
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

// 计算兼容性统计
const compatibilityStats = computed(() => {
  const compatible = compatibilityList.value.filter(item => item.compatibility === 'V').length
  const incompatible = compatibilityList.value.filter(item => item.compatibility === 'X').length
  return {
    compatible,
    incompatible
  }
})


// 加载过胶后工藝ID（已废弃，现在直接使用用户选择的postProcessingStepId）
const loadLaminatingStepId = async () => {
  // 不再需要自动设置postProcessingStepId，让用户自己选择
  console.log('loadLaminatingStepId已废弃，现在使用用户选择的postProcessingStepId')
}

// 加载材料选项
const loadMaterialOptions = async () => {
  try {
    materialOptions.value = await laminatingApi.getAllMaterialOptions()
  } catch (error) {
    console.error('加载材料选项失败:', error)
  }
}

// 根据产品系列加载燙金紙性能類型
const loadProductTypesBySeries = async (seriesName: string) => {
  try {
    console.log('正在加载产品系列对应的燙金紙性能類型:', seriesName)
    // 使用纸张类型API获取烫金纸性能类型
    const response = await fetch(`/api/api/products/paper-types/${encodeURIComponent(seriesName)}`)
    if (response.ok) {
      const paperTypes = await response.json()
      console.log('获取到的燙金紙性能類型:', paperTypes)
      availableProductTypes.value = paperTypes
    } else {
      console.error('加载燙金紙性能類型失败，状态码:', response.status)
      availableProductTypes.value = []
    }
  } catch (error) {
    console.error('加载燙金紙性能類型失败:', error)
    availableProductTypes.value = []
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

// 当产品名称改变时，加载对应的产品型号
const onProductNameChange = async () => {
  if (newCompatibility.foilSeries) {
    // 清空之前的选择
    newCompatibility.modelNumber = ''
    newCompatibility.productType = ''
    
    // 并行加载产品型号和燙金紙性能類型
    loadingProductModels.value = true
    loadingProductTypes.value = true
    
    try {
      const [models, productTypes] = await Promise.all([
        sharedProductApi.getProductModelNumbersByProductName(newCompatibility.foilSeries),
        loadProductTypesBySeries(newCompatibility.foilSeries)
      ])
      availableProductModels.value = models
    } catch (error) {
      console.error('加载产品数据失败:', error)
      availableProductModels.value = []
      availableProductTypes.value = []
    } finally {
      loadingProductModels.value = false
      loadingProductTypes.value = false
    }
  } else {
    // 清空所有相关数据
    availableProductModels.value = []
    availableProductTypes.value = []
    newCompatibility.modelNumber = ''
    newCompatibility.productType = ''
  }
}

// 产品型号变化时的处理
const onProductModelChange = async () => {
  if (newCompatibility.modelNumber && newCompatibility.foilSeries) {
    loadingProductTypes.value = true
    try {
      console.log('正在加载产品型号对应的燙金紙性能類型:', newCompatibility.modelNumber)
      await loadProductTypesByModel(newCompatibility.foilSeries, newCompatibility.modelNumber)
    } catch (error) {
      console.error('加载燙金紙性能類型失败:', error)
      availableProductTypes.value = []
    } finally {
      loadingProductTypes.value = false
    }
  } else {
    // 如果没有选择产品型号，则根据产品系列加载
    if (newCompatibility.foilSeries) {
      await loadProductTypesBySeries(newCompatibility.foilSeries)
    } else {
      availableProductTypes.value = []
    }
  }
}

// 根据产品型号加载燙金紙性能類型
const loadProductTypesByModel = async (seriesName: string, modelNumber: string) => {
  try {
    console.log('正在加载产品型号对应的燙金紙性能類型:', seriesName, modelNumber)
    // 首先通过型号搜索找到产品ID
    const searchResponse = await fetch(`/api/api/product-management/search/model?modelNumber=${encodeURIComponent(modelNumber)}`)
    if (searchResponse.ok) {
      const products = await searchResponse.json()
      // 找到匹配的产品
      const matchedProduct = products.find((product: any) => 
        product.name === seriesName && product.modelNumber === modelNumber
      )
      if (matchedProduct && matchedProduct.id) {
        // 使用产品ID直接获取产品信息
        const productResponse = await fetch(`/api/api/products/${matchedProduct.id}`)
        if (productResponse.ok) {
          const product = await productResponse.json()
          console.log('获取到的产品信息:', product)
          
          // 从产品信息中获取烫金纸性能类型
          if (product.hot_stamping_paper_type) {
            availableProductTypes.value = [product.hot_stamping_paper_type]
            console.log('获取到的燙金紙性能類型:', product.hot_stamping_paper_type)
          } else {
            availableProductTypes.value = []
            console.log('该产品没有燙金紙性能類型信息')
          }
        } else {
          console.error('获取产品信息失败，状态码:', productResponse.status)
          availableProductTypes.value = []
        }
      } else {
        availableProductTypes.value = []
        console.log('未找到匹配的产品')
      }
    } else {
      console.error('搜索产品失败，状态码:', searchResponse.status)
      availableProductTypes.value = []
    }
  } catch (error) {
    console.error('加载燙金紙性能類型失败:', error)
    availableProductTypes.value = []
  }
}

// 搜索时产品名称改变的处理
const onSearchProductNameChange = async () => {
  if (searchProductName.value) {
    // 清空之前的选择
    searchForm.modelNumber = ''
    
    // 加载产品型号
    loadingProductModels.value = true
    try {
      availableProductModels.value = await sharedProductApi.getProductModelNumbersByProductName(searchProductName.value)
    } catch (error) {
      console.error('搜索时加载产品型号失败:', error)
      availableProductModels.value = []
    } finally {
      loadingProductModels.value = false
    }
  } else {
    // 清空所有相关数据
    availableProductModels.value = []
    searchForm.modelNumber = ''
  }
}

// 加载过胶后工藝选项
const loadProcessingOptions = async () => {
  try {
    processingOptions.value = await laminatingApi.getAllProcessingOptions()
  } catch (error) {
    console.error('加载过胶后工藝选项失败:', error)
  }
}

// 搜索兼容性配置
const searchCompatibility = async () => {
  try {
    const query: LaminationCompatibilityQuery = {
      ...searchForm,
      foilSeries: searchProductName.value || searchForm.foilSeries, // 优先使用产品名称搜索
      postProcessingStepId: searchForm.postProcessingStepId || undefined
    }
    
    const response = await laminatingApi.getCompatibilityList(query)
    compatibilityList.value = response.list
    
    pagination.total = response.total
    pagination.page = response.page
    pagination.size = response.size
    pagination.totalPages = response.totalPages
  } catch (error) {
    console.error('搜索兼容性配置失败:', error)
    alert('搜索兼容性配置失败')
  }
}

// 重置搜索
const resetSearch = () => {
  searchProductName.value = ''
  searchForm.foilSeries = ''
  searchForm.productType = ''
  searchForm.modelNumber = ''
  searchForm.laminationMaterialId = undefined
  searchForm.compatibility = ''
  searchForm.page = 1
  availableProductModels.value = []
  searchCompatibility()
}




// 删除兼容性配置
const deleteCompatibility = async (id: number) => {
  if (confirm('确定要删除这个兼容性配置吗？')) {
    try {
      await laminatingApi.deleteCompatibility(id)
      await searchCompatibility()
      alert('删除成功')
    } catch (error) {
      console.error('删除兼容性配置失败:', error)
      alert('删除兼容性配置失败')
    }
  }
}

// 保存新的兼容性配置
const saveNewCompatibility = async () => {
  try {
    // 检查必填字段
    if (!newCompatibility.foilSeries) {
      alert('请选择产品系列')
      return
    }
    if (!newCompatibility.productType) {
      alert('请选择燙金紙性能類型')
      return
    }
    if (!newCompatibility.laminationMaterialId) {
      alert('请选择过胶材料')
      return
    }
    
    // 检查唯一性
    const existing = await laminatingApi.checkUnique(
      newCompatibility.foilSeries,
      newCompatibility.modelNumber || undefined,
      newCompatibility.productType,
      newCompatibility.laminationMaterialId,
      (newCompatibility.postProcessingStepId && newCompatibility.postProcessingStepId !== 0) ? newCompatibility.postProcessingStepId : undefined
    )
    
    if (existing) {
      alert('该配置已存在，请勿重复添加')
      return
    }
    
    const compatibility: LaminationCompatibility = {
      ...newCompatibility,
      postProcessingStepId: (newCompatibility.postProcessingStepId && newCompatibility.postProcessingStepId !== 0) ? newCompatibility.postProcessingStepId : 0,
      interfaceTypeId: 0
    }
    
    await laminatingApi.saveCompatibility(compatibility)
    closeAddDialog()
    await searchCompatibility()
    alert('添加成功')
  } catch (error: any) {
    console.error('添加兼容性配置失败:', error)
    const errorMessage = error.response?.data?.message || error.message || '添加兼容性配置失败'
    alert(errorMessage)
  }
}

// 编辑兼容性配置
const editCompatibilityItem = async (item: LaminationCompatibility) => {
  // 赋值表单数据
  Object.assign(editCompatibility, {
    id: item.id,
    foilSeries: item.foilSeries,
    modelNumber: item.modelNumber,
    productType: item.productType,
    laminationMaterialId: item.laminationMaterialId,
    compatibility: item.compatibility,
    postProcessingStepId: item.postProcessingStepId,
    isJiehuo: item.isJiehuo || false
  })
  
  // 设置编辑对话框的产品系列搜索文本
  editProductSeriesSearchText.value = item.foilSeries || ''
  showEditProductSeriesDropdown.value = false
  
  // 如果已有产品系列，加载对应的产品型号和燙金紙性能類型
  if (item.foilSeries) {
    await onEditProductNameChange()
    // 如果已有产品型号，加载对应的燙金紙性能類型
    if (item.modelNumber) {
      await onEditProductModelChange()
    }
  }
  
  showEditDialog.value = true
}

// 保存编辑的兼容性配置
const saveEditCompatibility = async () => {
  try {
    // 检查必填字段
    if (!editCompatibility.foilSeries) {
      alert('请选择产品系列')
      return
    }
    if (!editCompatibility.productType) {
      alert('请选择燙金紙性能類型')
      return
    }
    if (!editCompatibility.laminationMaterialId) {
      alert('请选择过胶材料')
      return
    }
    
    // 检查唯一性（排除当前记录）
    const existing = await laminatingApi.checkUnique(
      editCompatibility.foilSeries,
      editCompatibility.modelNumber || undefined,
      editCompatibility.productType,
      editCompatibility.laminationMaterialId,
      (editCompatibility.postProcessingStepId && editCompatibility.postProcessingStepId !== 0) ? editCompatibility.postProcessingStepId : undefined
    )
    
    // 如果存在且不是当前记录，则冲突
    if (existing && existing.id !== editCompatibility.id) {
      alert('该配置已存在，请修改唯一键字段（产品系列、产品型号、产品类型、过胶材料、后处理步骤）')
      return
    }
    
    const compatibility: LaminationCompatibility = {
      ...editCompatibility,
      postProcessingStepId: (editCompatibility.postProcessingStepId && editCompatibility.postProcessingStepId !== 0) ? editCompatibility.postProcessingStepId : 0,
      interfaceTypeId: 0
    }
    
    await laminatingApi.updateCompatibility(compatibility.id!, compatibility)
    closeEditDialog()
    await searchCompatibility()
    alert('更新成功')
    } catch (error: any) {
    console.error('更新兼容性配置失败:', error)
    const errorMessage = error.response?.data?.message || error.message || '更新兼容性配置失败'
    alert(errorMessage)
  }
}

// 关闭编辑对话框
const closeEditDialog = () => {
  showEditDialog.value = false
  Object.assign(editCompatibility, {
    id: undefined,
    foilSeries: '',
    modelNumber: '',
    productType: '',
    laminationMaterialId: 0,
    compatibility: 'V',
    postProcessingStepId: 0,
    isJiehuo: false
  })
  // 清空联动选择数据
  availableProductModels.value = []
  availableProductTypes.value = []
  editProductSeriesSearchText.value = ''
  showEditProductSeriesDropdown.value = false
}

// 编辑对话框的产品系列过滤
const filteredEditProductSeries = computed(() => {
  if (!editProductSeriesSearchText.value || editProductSeriesSearchText.value.trim() === '') {
    return availableProducts.value
  }
  
  const searchText = editProductSeriesSearchText.value.toLowerCase().trim()
  return availableProducts.value.filter(product => 
    product && product.toLowerCase().includes(searchText)
  )
})

// 编辑对话框的产品系列搜索相关方法
const onEditProductSeriesSearchInput = () => {
  showEditProductSeriesDropdown.value = true
}

const selectEditProductSeries = async (series: string) => {
  editCompatibility.foilSeries = series
  editProductSeriesSearchText.value = series
  showEditProductSeriesDropdown.value = false
  await onEditProductNameChange()
}

const handleEditProductSeriesInputBlur = () => {
  setTimeout(() => {
    showEditProductSeriesDropdown.value = false
    // 如果输入框有值但没有匹配到产品，尝试精确匹配
    if (editProductSeriesSearchText.value && !editCompatibility.foilSeries) {
      const exactMatch = availableProducts.value.find(p => 
        p === editProductSeriesSearchText.value.trim()
      )
      if (exactMatch) {
        selectEditProductSeries(exactMatch)
      } else {
        // 如果没有精确匹配，使用输入值
        editCompatibility.foilSeries = editProductSeriesSearchText.value.trim()
        onEditProductNameChange()
      }
    } else if (!editCompatibility.foilSeries) {
      // 如果没有选择产品，清空输入
      editProductSeriesSearchText.value = ''
    } else {
      // 如果已选择产品，同步搜索文本
      editProductSeriesSearchText.value = editCompatibility.foilSeries
    }
  }, 200)
}

// 编辑时产品名称改变的处理
const onEditProductNameChange = async () => {
  if (editCompatibility.foilSeries) {
    // 清空之前的选择
    editCompatibility.modelNumber = ''
    editCompatibility.productType = ''
    
    // 同时加载产品型号和燙金紙性能類型
    loadingProductModels.value = true
    loadingProductTypes.value = true
    try {
      const [models] = await Promise.all([
        sharedProductApi.getProductModelNumbersByProductName(editCompatibility.foilSeries),
        loadProductTypesBySeries(editCompatibility.foilSeries)
      ])
      availableProductModels.value = models
    } catch (error) {
      console.error('编辑时加载产品数据失败:', error)
      availableProductModels.value = []
      availableProductTypes.value = []
    } finally {
      loadingProductModels.value = false
      loadingProductTypes.value = false
    }
  } else {
    // 清空所有相关数据
    availableProductModels.value = []
    availableProductTypes.value = []
    editCompatibility.modelNumber = ''
    editCompatibility.productType = ''
  }
}

// 编辑对话框的产品型号变化时的处理
const onEditProductModelChange = async () => {
  if (editCompatibility.modelNumber && editCompatibility.foilSeries) {
    loadingProductTypes.value = true
    try {
      console.log('正在加载产品型号对应的燙金紙性能類型:', editCompatibility.modelNumber)
      await loadProductTypesByModel(editCompatibility.foilSeries, editCompatibility.modelNumber)
    } catch (error) {
      console.error('加载燙金紙性能類型失败:', error)
      availableProductTypes.value = []
    } finally {
      loadingProductTypes.value = false
    }
  } else {
    // 如果没有选择产品型号，则根据产品系列加载
    if (editCompatibility.foilSeries) {
      await loadProductTypesBySeries(editCompatibility.foilSeries)
    } else {
      availableProductTypes.value = []
    }
  }
}

// 关闭添加对话框
const closeAddDialog = () => {
  showAddDialog.value = false
  Object.assign(newCompatibility, {
    foilSeries: '',
    modelNumber: '',
    productType: '',
    laminationMaterialId: 0,
    compatibility: 'V',
    postProcessingStepId: laminatingStepId.value || 0,
    isJiehuo: false
  })
  // 清空联动选择数据
  availableProductModels.value = []
  availableProductTypes.value = []
  productSeriesSearchText.value = ''
}

// 切换页码
const changePage = (page: number) => {
  if (page >= 1 && page <= pagination.totalPages) {
    searchForm.page = page
    searchCompatibility()
  }
}

// 导出数据
const exportData = async () => {
  try {
    // 构建查询参数
    const query: LaminationCompatibilityQuery = {}
    if (searchForm.foilSeries) query.foilSeries = searchForm.foilSeries
    if (searchForm.productType) query.productType = searchForm.productType
    if (searchForm.modelNumber) query.modelNumber = searchForm.modelNumber
    if (searchForm.postProcessingStepId) query.postProcessingStepId = searchForm.postProcessingStepId
    if (searchForm.laminationMaterialId) query.laminationMaterialId = searchForm.laminationMaterialId
    if (searchForm.compatibility) query.compatibility = searchForm.compatibility
    
    // 调用导出API
    const blob = await laminatingApi.exportCompatibility(query)
    
    // 生成文件名
    const fileName = `過膠兼容性配置_${new Date().toISOString().split('T')[0]}.xlsx`
    
    // 下载文件
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

// ===== 过胶材料管理 =====
const materialForm = reactive<LaminationMaterialOption>({
  materialName: '',
  materialCode: '', // 保留字段但不显示，用于兼容API
  displayOrder: 0,
  isActive: true,
  description: ''
})

// 材料管理相关状态
const materialSearch = ref('')
const materialStatusFilter = ref('')
const materialSortBy = ref('displayOrder')
const materialSortOrder = ref('asc')
const selectedMaterials = ref<number[]>([])
const savingMaterial = ref(false)

// 计算过滤后的材料选项
const filteredMaterialOptions = computed(() => {
  let filtered = [...materialOptions.value]
  
  // 搜索过滤
  if (materialSearch.value) {
    const search = materialSearch.value.toLowerCase()
    filtered = filtered.filter(m => 
      m.materialName?.toLowerCase().includes(search)
    )
  }
  
  // 状态过滤
  if (materialStatusFilter.value) {
    const isActive = materialStatusFilter.value === 'true'
    filtered = filtered.filter(m => m.isActive === isActive)
  }
  
  // 排序
  filtered.sort((a, b) => {
    let aValue = a[materialSortBy.value as keyof LaminationMaterialOption]
    let bValue = b[materialSortBy.value as keyof LaminationMaterialOption]
    
    if (typeof aValue === 'string' && typeof bValue === 'string') {
      aValue = aValue.toLowerCase()
      bValue = bValue.toLowerCase()
    }
    
    if (aValue < bValue) return materialSortOrder.value === 'asc' ? -1 : 1
    if (aValue > bValue) return materialSortOrder.value === 'asc' ? 1 : -1
    return 0
  })
  
  return filtered
})

const resetMaterialForm = () => {
  Object.assign(materialForm, {
    id: undefined,
    materialName: '',
    materialCode: '', // 保留字段但不显示
    displayOrder: 0,
    isActive: true,
    description: ''
  })
}

const editMaterial = (m: LaminationMaterialOption) => {
  Object.assign(materialForm, m)
}

const saveMaterial = async () => {
  try {
    if (!materialForm.materialName) {
      alert('请填写材料名称')
      return
    }
    
    // 自动生成编码（如果需要）
    if (!materialForm.materialCode) {
      materialForm.materialCode = materialForm.materialName
    }
    
    savingMaterial.value = true
    if (materialForm.id) {
      await laminatingApi.updateMaterialOption(materialForm.id as number, materialForm)
    } else {
      await laminatingApi.saveMaterialOption(materialForm)
    }
    await loadMaterialOptions()
    resetMaterialForm()
    alert('保存成功')
  } catch (e) {
    console.error(e)
    alert('保存失败')
  } finally {
    savingMaterial.value = false
  }
}

// 材料搜索和筛选相关方法
const clearMaterialSearch = () => {
  materialSearch.value = ''
  materialStatusFilter.value = ''
  materialSortBy.value = 'displayOrder'
  materialSortOrder.value = 'asc'
}

const sortMaterials = (field: string) => {
  if (materialSortBy.value === field) {
    materialSortOrder.value = materialSortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    materialSortBy.value = field
    materialSortOrder.value = 'asc'
  }
}

// 批量操作相关方法
const toggleSelectMaterial = (id: number) => {
  const index = selectedMaterials.value.indexOf(id)
  if (index > -1) {
    selectedMaterials.value.splice(index, 1)
  } else {
    selectedMaterials.value.push(id)
  }
}

const toggleSelectAllMaterials = () => {
  if (selectedMaterials.value.length === filteredMaterialOptions.value.length) {
    selectedMaterials.value = []
  } else {
    selectedMaterials.value = filteredMaterialOptions.value.map(m => m.id as number)
  }
}

const clearSelectedMaterials = () => {
  selectedMaterials.value = []
}

const batchEnableMaterials = async () => {
  if (selectedMaterials.value.length === 0) return
  
  if (confirm(`确定要启用选中的 ${selectedMaterials.value.length} 个材料吗？`)) {
    try {
      for (const id of selectedMaterials.value) {
        const material = materialOptions.value.find(m => m.id === id)
        if (material) {
          await laminatingApi.updateMaterialOption(id, { ...material, isActive: true })
        }
      }
      await loadMaterialOptions()
      clearSelectedMaterials()
      alert('批量启用成功')
    } catch (e) {
      console.error(e)
      alert('批量启用失败')
    }
  }
}

const batchDisableMaterials = async () => {
  if (selectedMaterials.value.length === 0) return
  
  if (confirm(`确定要禁用选中的 ${selectedMaterials.value.length} 个材料吗？`)) {
    try {
      for (const id of selectedMaterials.value) {
        const material = materialOptions.value.find(m => m.id === id)
        if (material) {
          await laminatingApi.updateMaterialOption(id, { ...material, isActive: false })
        }
      }
      await loadMaterialOptions()
      clearSelectedMaterials()
      alert('批量禁用成功')
    } catch (e) {
      console.error(e)
      alert('批量禁用失败')
    }
  }
}

const batchDeleteMaterials = async () => {
  if (selectedMaterials.value.length === 0) return
  
  if (confirm(`确定要删除选中的 ${selectedMaterials.value.length} 个材料吗？此操作不可撤销！`)) {
    try {
      for (const id of selectedMaterials.value) {
        await laminatingApi.deleteMaterialOption(id)
      }
      await loadMaterialOptions()
      clearSelectedMaterials()
      alert('批量删除成功')
    } catch (e) {
      console.error(e)
      alert('批量删除失败')
    }
  }
}

const exportMaterials = async () => {
  try {
    // 调用导出API
    const blob = await laminatingApi.exportMaterialOptions()
    
    // 生成文件名
    const fileName = `过胶材料选项_${new Date().toISOString().split('T')[0]}.xlsx`
    
    // 下载文件
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

const deleteMaterial = async (id: number) => {
  if (!confirm('确定删除该材料？')) return
  try {
    await laminatingApi.deleteMaterialOption(id)
    await loadMaterialOptions()
    alert('删除成功')
  } catch (e) {
    console.error(e)
    alert('删除失败')
  }
}

// ===== 过胶后工藝管理 =====
const stepForm = reactive<PostProcessingOption>({
  stepName: '',
  stepCode: '', // 保留字段但不显示，用于兼容API
  displayOrder: 0,
  isActive: true,
  description: ''
})

// 后工藝管理相关状态
const stepSearch = ref('')
const stepStatusFilter = ref('')
const stepSortBy = ref('displayOrder')
const stepSortOrder = ref('asc')
const selectedSteps = ref<number[]>([])

// 计算过滤后的后工藝选项
const filteredProcessingOptions = computed(() => {
  let filtered = [...processingOptions.value]
  
  // 搜索过滤
  if (stepSearch.value) {
    const search = stepSearch.value.toLowerCase()
    filtered = filtered.filter(s => 
      s.stepName?.toLowerCase().includes(search)
    )
  }
  
  // 状态过滤
  if (stepStatusFilter.value) {
    const isActive = stepStatusFilter.value === 'true'
    filtered = filtered.filter(s => s.isActive === isActive)
  }
  
  // 排序
  filtered.sort((a, b) => {
    let aValue = a[stepSortBy.value as keyof PostProcessingOption]
    let bValue = b[stepSortBy.value as keyof PostProcessingOption]
    
    if (typeof aValue === 'string' && typeof bValue === 'string') {
      aValue = aValue.toLowerCase()
      bValue = bValue.toLowerCase()
    }
    
    if (aValue < bValue) return stepSortOrder.value === 'asc' ? -1 : 1
    if (aValue > bValue) return stepSortOrder.value === 'asc' ? 1 : -1
    return 0
  })
  
  return filtered
})

const resetStepForm = () => {
  Object.assign(stepForm, {
    id: undefined,
    stepName: '',
    stepCode: '', // 保留字段但不显示
    displayOrder: 0,
    isActive: true,
    description: ''
  })
}

const editStep = (s: PostProcessingOption) => {
  Object.assign(stepForm, s)
}

const saveStep = async () => {
  try {
    if (!stepForm.stepName) {
      alert('请填写后工藝名称')
      return
    }
    
    // 自动生成编码（如果需要）
    if (!stepForm.stepCode) {
      stepForm.stepCode = stepForm.stepName
    }
    if (stepForm.id) {
      await laminatingApi.updateProcessingOption(stepForm.id as number, stepForm)
    } else {
      await laminatingApi.saveProcessingOption(stepForm)
    }
    await loadProcessingOptions()
    resetStepForm()
    alert('保存成功')
  } catch (e) {
    console.error(e)
    alert('保存失败')
  }
}

// 后工藝搜索和筛选相关方法
const clearStepSearch = () => {
  stepSearch.value = ''
  stepStatusFilter.value = ''
  stepSortBy.value = 'displayOrder'
  stepSortOrder.value = 'asc'
}

// 后工藝批量操作相关方法
const toggleSelectStep = (id: number) => {
  const index = selectedSteps.value.indexOf(id)
  if (index > -1) {
    selectedSteps.value.splice(index, 1)
  } else {
    selectedSteps.value.push(id)
  }
}

const toggleSelectAllSteps = () => {
  if (selectedSteps.value.length === filteredProcessingOptions.value.length) {
    selectedSteps.value = []
  } else {
    selectedSteps.value = filteredProcessingOptions.value.map(s => s.id as number)
  }
}

const clearSelectedSteps = () => {
  selectedSteps.value = []
}

const batchEnableSteps = async () => {
  if (selectedSteps.value.length === 0) return
  
  if (confirm(`确定要启用选中的 ${selectedSteps.value.length} 个后工藝吗？`)) {
    try {
      for (const id of selectedSteps.value) {
        const step = processingOptions.value.find(s => s.id === id)
        if (step) {
          await laminatingApi.updateProcessingOption(id, { ...step, isActive: true })
        }
      }
      await loadProcessingOptions()
      clearSelectedSteps()
      alert('批量启用成功')
    } catch (e) {
      console.error(e)
      alert('批量启用失败')
    }
  }
}

const batchDisableSteps = async () => {
  if (selectedSteps.value.length === 0) return
  
  if (confirm(`确定要禁用选中的 ${selectedSteps.value.length} 个后工藝吗？`)) {
    try {
      for (const id of selectedSteps.value) {
        const step = processingOptions.value.find(s => s.id === id)
        if (step) {
          await laminatingApi.updateProcessingOption(id, { ...step, isActive: false })
        }
      }
      await loadProcessingOptions()
      clearSelectedSteps()
      alert('批量禁用成功')
    } catch (e) {
      console.error(e)
      alert('批量禁用失败')
    }
  }
}

const batchDeleteSteps = async () => {
  if (selectedSteps.value.length === 0) return
  
  if (confirm(`确定要删除选中的 ${selectedSteps.value.length} 个后工藝吗？此操作不可撤销！`)) {
    try {
      for (const id of selectedSteps.value) {
        await laminatingApi.deleteProcessingOption(id)
      }
      await loadProcessingOptions()
      clearSelectedSteps()
      alert('批量删除成功')
    } catch (e) {
      console.error(e)
      alert('批量删除失败')
    }
  }
}

const deleteStep = async (id: number) => {
  if (!confirm('确定删除该后工藝？')) return
  try {
    await laminatingApi.deleteProcessingOption(id)
    await loadProcessingOptions()
    alert('删除成功')
  } catch (e) {
    console.error(e)
    alert('删除失败')
  }
}

// ===== 批量操作相关 =====

// 切换选择项
const toggleSelectItem = (id: number) => {
  const index = selectedItems.value.indexOf(id)
  if (index > -1) {
    selectedItems.value.splice(index, 1)
  } else {
    selectedItems.value.push(id)
  }
}

// 全选/取消全选
const toggleSelectAll = () => {
  if (selectedItems.value.length === compatibilityList.value.length) {
    selectedItems.value = []
  } else {
    selectedItems.value = compatibilityList.value.map(item => item.id as number).filter(id => id !== undefined)
  }
}

// 清除选择
const clearSelection = () => {
  selectedItems.value = []
}

// 批量删除兼容性配置
const batchDeleteCompatibility = async () => {
  if (selectedItems.value.length === 0) {
    alert('请选择要删除的记录')
    return
  }
  
  if (!confirm(`确定要删除选中的 ${selectedItems.value.length} 条记录吗？此操作不可恢复！`)) {
    return
  }
  
  try {
    await laminatingApi.batchDeleteCompatibility(selectedItems.value)
    selectedItems.value = []
    await searchCompatibility()
    alert('批量删除成功')
  } catch (error: any) {
    console.error('批量删除失败:', error)
    const errorMessage = error.response?.data?.message || error.message || '批量删除失败'
    alert(errorMessage)
  }
}

// ===== 批量添加对话框 =====
const batchAddForm = reactive({
  productType: '', // 燙金紙性能類型
  selectedSeries: [] as string[], // 选中的产品系列
  selectedProducts: {} as Record<string, string[]>, // 每个产品系列对应的产品型号列表
  laminationMaterialId: 0,
  postProcessingStepId: 0,
  compatibility: 'V' as 'V' | 'X',
  isJiehuo: false
})

// 批量添加时可用系列列表（根据性能类型筛选）
const batchAddAvailableSeries = ref<string[]>([])
const batchAddSeriesSearchText = ref('')

// 批量添加时每个系列对应的产品列表（包含完整产品信息）
const batchAddSeriesProducts = ref<Record<string, Array<{ id: number, modelNumber: string, name: string }>>>({})
const batchAddLoadingSeries = ref(false)

// 批量添加时每个系列的产品型号搜索文本
const batchAddModelSearchTexts = ref<Record<string, string>>({})

// 根据性能类型筛选产品系列（优化：使用后端接口直接获取，避免循环调用）
const loadSeriesByProductType = async (productType: string) => {
  if (!productType) {
    batchAddAvailableSeries.value = []
    return
  }
  
  try {
    batchAddLoadingSeries.value = true
    // 直接使用后端接口获取支持该性能类型的产品系列列表
    const response = await fetch(`/api/api/products/series-by-paper-type/${encodeURIComponent(productType)}`)
    if (response.ok) {
      const series = await response.json()
      batchAddAvailableSeries.value = series || []
    } else {
      console.error('获取支持该性能类型的产品系列失败，状态码:', response.status)
      batchAddAvailableSeries.value = []
    }
  } catch (error) {
    console.error('加载产品系列失败:', error)
    batchAddAvailableSeries.value = []
  } finally {
    batchAddLoadingSeries.value = false
  }
}

// 批量添加时根据系列加载产品列表（参考絲印LED UV灑閃粉配置管理）
const loadProductsForBatchAddSeries = async (series: string) => {
  if (!series) {
    return
  }
  
  // 如果已经加载过，不再重复加载
  if (batchAddSeriesProducts.value[series] !== undefined) {
    return
  }
  
  try {
    const response = await fetch(`/api/api/products/series/${encodeURIComponent(series)}`)
    if (response.ok) {
      const products = await response.json()
      batchAddSeriesProducts.value[series] = products.map((p: any) => ({
        id: p.id,
        modelNumber: p.model_number || p.modelNumber || '',
        name: p.name || ''
      }))
      // 初始化该系列的产品型号选择数组
      if (!batchAddForm.selectedProducts[series]) {
        batchAddForm.selectedProducts[series] = []
      }
    } else {
      batchAddSeriesProducts.value[series] = []
    }
  } catch (error) {
    console.error(`加载产品系列 ${series} 的产品列表失败:`, error)
    batchAddSeriesProducts.value[series] = []
  }
}

// 批量添加时过滤系列（支持搜索）
const filteredBatchAddSeries = computed(() => {
  if (!batchAddSeriesSearchText.value) {
    return batchAddAvailableSeries.value
  }
  const search = batchAddSeriesSearchText.value.toLowerCase()
  return batchAddAvailableSeries.value.filter(series => series.toLowerCase().includes(search))
})

// 批量添加时过滤产品型号（支持搜索）
const filteredBatchAddProducts = (seriesName: string) => {
  const products = batchAddSeriesProducts.value[seriesName] || []
  const searchText = batchAddModelSearchTexts.value[seriesName] || ''
  
  if (!searchText) {
    return products
  }
  
  const search = searchText.toLowerCase()
  return products.filter(product => {
    const modelNumber = (product.modelNumber || '').toLowerCase()
    const name = (product.name || '').toLowerCase()
    return modelNumber.includes(search) || name.includes(search)
  })
}

// 打开批量添加对话框
const openBatchAddDialog = () => {
  showBatchAddDialog.value = true
  // 重置表单
  Object.assign(batchAddForm, {
    productType: '',
    selectedSeries: [],
    seriesModelMap: {},
    laminationMaterialId: 0,
    postProcessingStepId: 0,
    compatibility: 'V',
    isJiehuo: false
  })
  batchAddAvailableSeries.value = []
  batchAddSeriesSearchText.value = ''
  batchAddSeriesProducts.value = {}
  batchAddModelSearchTexts.value = {}
  // 清空已选择的系列和产品型号
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
}

// 关闭批量添加对话框
const closeBatchAddDialog = () => {
  showBatchAddDialog.value = false
  // 清空搜索文本
  batchAddSeriesSearchText.value = ''
  batchAddModelSearchTexts.value = {}
}

// 批量添加时性能类型改变
const onBatchAddProductTypeChange = async () => {
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddSeriesProducts.value = {}
  if (batchAddForm.productType) {
    await loadSeriesByProductType(batchAddForm.productType)
  } else {
    batchAddAvailableSeries.value = []
  }
}

// 批量添加时系列选择改变（参考絲印LED UV灑閃粉配置管理）
const onBatchAddSeriesChange = async (seriesName: string) => {
  const isSelected = batchAddForm.selectedSeries.includes(seriesName)
  
  if (isSelected) {
    // 选择了产品系列，加载该系列的产品列表
    if (!batchAddSeriesProducts.value[seriesName]) {
      await loadProductsForBatchAddSeries(seriesName)
    }
  } else {
    // 取消选择产品系列，清除该系列的产品数据
    delete batchAddSeriesProducts.value[seriesName]
    delete batchAddForm.selectedProducts[seriesName]
    delete batchAddModelSearchTexts.value[seriesName]
  }
}

// 保存批量添加
const saveBatchAdd = async () => {
  if (!batchAddForm.productType) {
    alert('请选择燙金紙性能類型')
    return
  }
  
  if (batchAddForm.selectedSeries.length === 0) {
    alert('请至少选择一个产品系列')
    return
  }
  
  if (!batchAddForm.laminationMaterialId) {
    alert('请选择过胶材料')
    return
  }
  
  if (!batchAddForm.compatibility) {
    alert('请选择兼容性状态')
    return
  }
  
  try {
    const compatibilities: LaminationCompatibility[] = []
    const errors: string[] = []
    const skipped: string[] = []
    let successCount = 0
    let failCount = 0
    let skipCount = 0
    
    // 为每个选中的产品系列创建配置
    for (const seriesName of batchAddForm.selectedSeries) {
      const selectedProductModels = batchAddForm.selectedProducts[seriesName] || []
      
      if (selectedProductModels.length === 0) {
        // 如果没有选择任何产品型号，创建系列级别的配置
        const existing = await laminatingApi.checkUnique(
          seriesName,
          undefined,
          batchAddForm.productType,
          batchAddForm.laminationMaterialId,
          batchAddForm.postProcessingStepId || undefined
        )
        
        if (existing) {
          skipped.push(seriesName)
          skipCount++
          continue
        }
        
        compatibilities.push({
          foilSeries: seriesName,
          modelNumber: undefined,
          productType: batchAddForm.productType,
          laminationMaterialId: batchAddForm.laminationMaterialId,
          postProcessingStepId: batchAddForm.postProcessingStepId || 0,
          compatibility: batchAddForm.compatibility,
          isJiehuo: batchAddForm.isJiehuo,
          interfaceTypeId: 0
        })
      } else {
        // 为每个选中的产品型号创建配置
        for (const modelNumber of selectedProductModels) {
          const existing = await laminatingApi.checkUnique(
            seriesName,
            modelNumber,
            batchAddForm.productType,
            batchAddForm.laminationMaterialId,
            batchAddForm.postProcessingStepId || undefined
          )
          
          if (existing) {
            skipped.push(`${seriesName} - ${modelNumber}`)
            skipCount++
            continue
          }
          
          compatibilities.push({
            foilSeries: seriesName,
            modelNumber: modelNumber,
            productType: batchAddForm.productType,
            laminationMaterialId: batchAddForm.laminationMaterialId,
            postProcessingStepId: batchAddForm.postProcessingStepId || 0,
            compatibility: batchAddForm.compatibility,
            isJiehuo: batchAddForm.isJiehuo,
            interfaceTypeId: 0
          })
        }
      }
    }
    
    if (compatibilities.length === 0) {
      alert('没有可添加的配置（所有配置都已存在）')
      return
    }
    
    try {
      await laminatingApi.batchSaveCompatibility(compatibilities)
      successCount = compatibilities.length
    } catch (error) {
      console.error('批量保存失败:', error)
      failCount = compatibilities.length
    }
    
    // 显示结果
    if (failCount === 0 && skipCount === 0) {
      alert(`批量添加完成！成功添加 ${successCount} 条配置`)
    } else {
      let message = `批量添加完成！\n成功: ${successCount} 条`
      if (skipCount > 0) {
        message += `\n跳过: ${skipCount} 条 (已存在且未更新)`
        if (skipped.length > 0 && skipped.length <= 10) {
          message += `\n跳过的配置: ${skipped.join(', ')}`
        } else if (skipped.length > 10) {
          message += `\n跳过的配置: ${skipped.slice(0, 10).join(', ')} 等 ${skipped.length} 条`
        }
      }
      if (failCount > 0) {
        message += `\n失败: ${failCount} 条`
        if (errors.length > 0 && errors.length <= 10) {
          message += `\n\n失败详情:\n${errors.join('\n')}`
        }
      }
      alert(message)
    }
    
    if (successCount > 0) {
      closeBatchAddDialog()
      await searchCompatibility()
    }
  } catch (error) {
    console.error('批量添加失败:', error)
    alert('批量添加失败，请重试')
  }
}

// ===== 批量修改对话框 =====
const batchEditForm = reactive({
  foilSeries: '',
  modelNumber: '',
  productType: '',
  laminationMaterialId: null as number | null,
  postProcessingStepId: null as number | null,
  compatibility: '' as '' | 'V' | 'X',
  isJiehuo: null as boolean | null
})

// 打开批量修改对话框
const openBatchEditDialog = () => {
  if (selectedItems.value.length === 0) {
    alert('请先选择要修改的记录')
    return
  }
  showBatchEditDialog.value = true
  // 重置表单
  Object.assign(batchEditForm, {
    foilSeries: '',
    modelNumber: '',
    productType: '',
    laminationMaterialId: null,
    postProcessingStepId: null,
    compatibility: '',
    isJiehuo: null
  })
}

// 关闭批量修改对话框
const closeBatchEditDialog = () => {
  showBatchEditDialog.value = false
}

// 保存批量修改
const saveBatchEdit = async () => {
  if (selectedItems.value.length === 0) {
    alert('请先选择要修改的记录')
    return
  }
  
  // 构建更新字段对象（只包含非空字段）
  const updateFields: Record<string, any> = {}
  
  if (batchEditForm.foilSeries && batchEditForm.foilSeries.trim()) {
    updateFields.foilSeries = batchEditForm.foilSeries.trim()
  }
  if (batchEditForm.modelNumber && batchEditForm.modelNumber.trim()) {
    updateFields.modelNumber = batchEditForm.modelNumber.trim()
  }
  if (batchEditForm.productType && batchEditForm.productType.trim()) {
    updateFields.productType = batchEditForm.productType.trim()
  }
  if (batchEditForm.laminationMaterialId !== null) {
    updateFields.laminationMaterialId = batchEditForm.laminationMaterialId
  }
  if (batchEditForm.postProcessingStepId !== null) {
    updateFields.postProcessingStepId = batchEditForm.postProcessingStepId
  }
  if (batchEditForm.compatibility !== '') {
    updateFields.compatibility = batchEditForm.compatibility
  }
  if (batchEditForm.isJiehuo !== null) {
    updateFields.isJiehuo = batchEditForm.isJiehuo
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
        const item = compatibilityList.value.find(c => c.id === id)
        if (!item) {
          failCount++
          errors.push(`ID ${id}: 记录不存在`)
          continue
        }
        
        // 构建更新数据
        const updateData: Partial<LaminationCompatibility> = {
          ...item,
          ...updateFields
        }
        
        await laminatingApi.updateCompatibility(id, updateData as LaminationCompatibility)
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
      await searchCompatibility()
    }
  } catch (error) {
    console.error('批量修改失败:', error)
    alert('批量修改失败，请重试')
  }
}

// ===== 导入对话框 =====
const importFile = ref<File | null>(null)
const importResult = ref<{
  success: boolean
  totalCount: number
  successCount: number
  failCount: number
  message: string
  errorMessages?: string[]
} | null>(null)
const importing = ref(false)

// 打开导入对话框
const openImportDialog = () => {
  showImportDialog.value = true
  importFile.value = null
  importResult.value = null
}

// 关闭导入对话框
const closeImportDialog = () => {
  showImportDialog.value = false
  importFile.value = null
  importResult.value = null
}

// 选择文件
const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    importFile.value = target.files[0]
  }
}

// 下载导入模板
const downloadImportTemplate = async () => {
  try {
    const blob = await laminatingApi.downloadImportTemplate()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '過膠兼容性配置導入模板.xlsx'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (error) {
    console.error('下载模板失败:', error)
    alert('下载模板失败，请重试')
  }
}

// 执行导入
const executeImport = async () => {
  if (!importFile.value) {
    alert('请选择要导入的文件')
    return
  }
  
  importing.value = true
  try {
    const result = await laminatingApi.importCompatibility(importFile.value)
    importResult.value = result
    
    if (result.success) {
      // 延迟关闭对话框并刷新列表
      setTimeout(async () => {
        closeImportDialog()
        await searchCompatibility()
      }, 2000)
    }
  } catch (error) {
    console.error('导入失败:', error)
    alert('导入失败，请重试')
  } finally {
    importing.value = false
  }
}

// ===== 复制功能 =====
const copyCompatibility = (item: LaminationCompatibility) => {
  // 填充到添加表单
  Object.assign(newCompatibility, {
    foilSeries: item.foilSeries,
    modelNumber: item.modelNumber || '',
    productType: item.productType || '',
    laminationMaterialId: item.laminationMaterialId,
    postProcessingStepId: item.postProcessingStepId,
    compatibility: item.compatibility,
    isJiehuo: item.isJiehuo || false
  })
  
  // 设置产品系列搜索文本
  productSeriesSearchText.value = item.foilSeries
  
  // 加载相关的选项数据
  if (item.foilSeries) {
    onProductNameChange()
  }
  
  // 打开添加对话框
  showAddDialog.value = true
}

// 过滤产品系列
const filteredProductSeries = computed(() => {
  if (!productSeriesSearchText.value) {
    return availableProducts.value
  }
  const search = productSeriesSearchText.value.toLowerCase()
  return availableProducts.value.filter(series => series.toLowerCase().includes(search))
})

// 产品系列搜索输入处理
const onProductSeriesSearchInput = () => {
  showProductSeriesDropdown.value = true
}

// 初始化产品系列搜索文本
const initProductSeriesSearchText = () => {
  if (newCompatibility.foilSeries) {
    productSeriesSearchText.value = newCompatibility.foilSeries
  }
}

// 处理产品系列输入框失焦
let blurTimeout: ReturnType<typeof setTimeout> | null = null
const handleProductSeriesInputBlur = () => {
  // 延迟关闭，以便点击下拉项时能触发选择
  blurTimeout = setTimeout(() => {
    showProductSeriesDropdown.value = false
    if (productSeriesSearchText.value && !newCompatibility.foilSeries) {
      const exactMatch = availableProducts.value.find(s => s === productSeriesSearchText.value)
      if (exactMatch) {
        selectProductSeries(exactMatch)
      } else {
        productSeriesSearchText.value = newCompatibility.foilSeries || ''
      }
    } else if (!newCompatibility.foilSeries) {
      productSeriesSearchText.value = ''
    } else {
      // 如果已选择，更新搜索文本为选中的值
      productSeriesSearchText.value = newCompatibility.foilSeries
    }
  }, 200)
}

// 选择产品系列（改进版，确保能关闭下拉框）
const selectProductSeries = (series: string) => {
  // 清除失焦延迟
  if (blurTimeout) {
    clearTimeout(blurTimeout)
    blurTimeout = null
  }
  
  newCompatibility.foilSeries = series
  productSeriesSearchText.value = series
  showProductSeriesDropdown.value = false
  onProductNameChange()
}

// 组件挂载时加载数据
onMounted(async () => {
  // 加载数据
  await loadLaminatingStepId()
  await loadMaterialOptions()
  await loadProcessingOptions()
  await loadAvailableProducts()
  await searchCompatibility()
})
</script>


