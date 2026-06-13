<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">燙金類型管理</h1>
            <p class="mt-2 text-gray-600">管理燙金工藝的類型選項和配置</p>
            <div class="mt-3 bg-yellow-50 border-l-4 border-yellow-400 text-sm text-yellow-800 p-3 rounded-r">
              <p class="font-semibold mb-1">篩選規則提示</p>
              <p>若某個燙金類型同時在「耐磨映射」與「常用界面映射」中配置，系統僅保留兩者交集；若只配置其中一種，則按該映射篩選；兩者皆無時不會返回任何結果。</p>
            </div>
          </div>
          <div class="flex space-x-3">
            <button
              @click="openAllMappingsDialog"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
              </svg>
              查看全部配置
            </button>
            <button
              @click="showAddDialog = true"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-red-600 hover:bg-red-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加燙金類型
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
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">燙金類型</label>
            <select
              v-model="searchForm.stampingType"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
            >
              <option value="">全部燙金類型</option>
              <option
                v-for="option in stampingTypeOptions"
                :key="option.value"
                :value="option.value"
              >
                {{ option.label }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">位置类型</label>
            <select
              v-model="searchForm.positionType"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
              :disabled="!searchForm.stampingType"
            >
              <option value="">全部位置类型</option>
              <option
                v-for="option in positionTypeOptions"
                :key="option.value"
                :value="option.value"
              >
                {{ option.label }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
            <select
              v-model="searchForm.isActive"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
            >
              <option value="">全部状态</option>
              <option value="true">激活</option>
              <option value="false">未激活</option>
            </select>
          </div>
          <div class="flex items-end">
            <button
              @click="searchHotStampingTypes"
              class="w-full bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition-colors"
            >
              搜索
            </button>
          </div>
        </div>
      </div>

      <!-- 烫金类型列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">烫金类型列表</h3>
        </div>
        
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">位置类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">描述</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">排序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="hotStampingType in filteredHotStampingTypes" :key="hotStampingType.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ hotStampingType.stampingType }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ hotStampingType.positionType || '默认' }}</div>
                </td>
                <td class="px-6 py-4">
                  <div class="text-sm text-gray-900">{{ hotStampingType.description || '-' }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                      hotStampingType.isActive
                        ? 'bg-green-100 text-green-800'
                        : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ hotStampingType.isActive ? '激活' : '未激活' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ hotStampingType.sortOrder }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button
                      @click="editHotStampingType(hotStampingType)"
                      class="text-red-600 hover:text-red-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="openMappingTypeDialog(hotStampingType)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      配置映射
                    </button>
                    <button
                      @click="toggleStatus(hotStampingType)"
                      :class="[
                        'hover:underline',
                        hotStampingType.isActive ? 'text-red-600 hover:text-red-900' : 'text-green-600 hover:text-green-900'
                      ]"
                    >
                      {{ hotStampingType.isActive ? '禁用' : '启用' }}
                    </button>
                    <button
                      @click="deleteHotStampingType(hotStampingType.id)"
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
          <div class="text-gray-500">暂无烫金类型数据</div>
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
    </div>

    <!-- 添加/编辑烫金类型对话框 -->
    <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              {{ showAddDialog ? '添加烫金类型' : '编辑烫金类型' }}
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

          <form @submit.prevent="saveHotStampingType" class="space-y-4">
            <div class="grid grid-cols-1 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金类型名称 *</label>
                <input
                  v-model="hotStampingTypeForm.stampingType"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">位置类型</label>
                <input
                  v-model="hotStampingTypeForm.positionType"
                  type="text"
                  placeholder="留空表示默认位置"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">描述</label>
                <textarea
                  v-model="hotStampingTypeForm.description"
                  rows="3"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                ></textarea>
              </div>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">排序顺序</label>
                  <input
                    v-model="hotStampingTypeForm.sortOrder"
                    type="number"
                    min="0"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-red-500"
                  />
                </div>
                <div class="flex items-center">
                  <input
                    v-model="hotStampingTypeForm.isActive"
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
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
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

    <!-- 映射类型选择对话框 -->
    <div v-if="showMappingTypeDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-md shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              选择映射类型 - {{ selectedHotStampingType ? `${selectedHotStampingType.stampingType} - ${selectedHotStampingType.positionType || '默认'}` : '' }}
            </h3>
            <button
              @click="closeMappingTypeDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <div class="space-y-4">
            <p class="text-sm text-gray-600">请选择要配置的映射类型：</p>
            
            <div class="space-y-3">
              <button
                @click="selectMappingType('common')"
                class="w-full p-4 border-2 border-gray-200 rounded-lg hover:border-blue-500 hover:bg-blue-50 transition-colors text-left"
              >
                <div class="flex items-center">
                  <div class="flex-shrink-0">
                    <svg class="w-8 h-8 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                  </div>
                  <div class="ml-4">
                    <h4 class="text-lg font-medium text-gray-900">常用界面映射</h4>
                    <p class="text-sm text-gray-500">配置常用界面的兼容性映射</p>
                  </div>
                </div>
              </button>

              <button
                @click="selectMappingType('durable')"
                class="w-full p-4 border-2 border-gray-200 rounded-lg hover:border-green-500 hover:bg-green-50 transition-colors text-left"
              >
                <div class="flex items-center">
                  <div class="flex-shrink-0">
                    <svg class="w-8 h-8 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z"></path>
                    </svg>
                  </div>
                  <div class="ml-4">
                    <h4 class="text-lg font-medium text-gray-900">耐磨金纸映射</h4>
                    <p class="text-sm text-gray-500">配置耐磨金纸的兼容性映射</p>
                  </div>
                </div>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 常用界面映射配置对话框 -->
    <div v-if="showCommonMappingDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[55]">
      <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-6xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              常用界面映射配置 - {{ selectedHotStampingType ? `${selectedHotStampingType.stampingType} - ${selectedHotStampingType.positionType || '默认'}` : '' }}
            </h3>
            <button
              @click="closeCommonMappingDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <!-- 映射列表 -->
          <div class="mb-4">
            <div class="flex items-center justify-between mb-4">
              <h4 class="text-md font-medium text-gray-700">产品兼容性配置</h4>
              <div class="flex space-x-2">
                <button
                  @click="openBatchCopyDialog"
                  :disabled="selectedCommonMappingIds.length === 0"
                  class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-purple-600 hover:bg-purple-700 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
                  </svg>
                  批量复制
                </button>
                <button
                  @click="handleBatchDelete"
                  :disabled="selectedCommonMappingIds.length === 0"
                  class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-red-600 hover:bg-red-700 disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
                  </svg>
                  批量删除 ({{ selectedCommonMappingIds.length }})
                </button>
              <button
                @click="openAddMappingDialog"
                class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700"
              >
                <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                </svg>
                添加映射
              </button>
              </div>
            </div>

            <!-- 映射列表搜索筛选 -->
            <div class="bg-gray-50 rounded-lg p-4 mb-4">
              <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列</label>
                  <select
                    v-model="commonMappingSearchForm.productName"
                    @change="onProductNameChange"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">全部燙金紙系列</option>
                    <option v-for="product in availableProducts" :key="product" :value="product">
                      {{ product }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                  <select
                    v-model="commonMappingSearchForm.paperType"
                    :disabled="!commonMappingSearchForm.productName"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ !commonMappingSearchForm.productName ? '请先选择燙金紙系列' : '全部烫金纸性能类型' }}
                    </option>
                    <option v-for="paperType in filteredPaperTypes" :key="paperType" :value="paperType">
                      {{ paperType }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
                  <select
                    v-model="commonMappingSearchForm.compatibilityStatus"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">全部状态</option>
                    <option value="V">适用</option>
                    <option value="X">不适用</option>
                    <option value="NA">不确定</option>
                    <option value="▷">需要打底处理</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
                  <select
                    v-model="commonMappingSearchForm.status"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">全部状态</option>
                    <option value="true">激活</option>
                    <option value="false">停用</option>
                  </select>
                </div>
                <div class="flex items-end">
                  <button
                    @click="searchCommonMappingConfigs"
                    class="w-full bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors"
                  >
                    搜索
                  </button>
                </div>
              </div>
            </div>

            <div class="overflow-x-auto">
              <table class="min-w-full divide-y divide-gray-200">
                <thead class="bg-gray-50">
                  <tr>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      <input type="checkbox" v-model="selectAllCommonMappings" @change="toggleSelectAllCommonMappings" class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙系列</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="mapping in filteredCommonMappingConfigs" :key="mapping.id" class="hover:bg-gray-50">
                    <td class="px-6 py-4 whitespace-nowrap">
                      <input type="checkbox" :value="mapping.id" v-model="selectedCommonMappingIds" class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
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
                        {{ 
                          mapping.compatibilityStatus === 'V' ? '适用' : 
                          mapping.compatibilityStatus === 'X' ? '不适用' :
                          mapping.compatibilityStatus === 'NA' ? '不确定' : '需要打底处理'
                        }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span
                        :class="[
                          'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                          mapping.isActive !== false
                            ? 'bg-green-100 text-green-800'
                            : 'bg-red-100 text-red-800'
                        ]"
                      >
                        {{ mapping.isActive !== false ? '激活' : '停用' }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div class="flex space-x-2">
                        <button
                          @click="editCommonMapping(mapping)"
                          class="text-blue-600 hover:text-blue-900"
                        >
                          编辑
                        </button>
                        <button
                          @click="toggleMappingStatus(mapping)"
                          :class="[
                            'hover:underline',
                            mapping.isActive !== false ? 'text-red-600 hover:text-red-900' : 'text-green-600 hover:text-green-900'
                          ]"
                        >
                          {{ mapping.isActive !== false ? '停用' : '激活' }}
                        </button>
                        <button
                          @click="deleteCommonMapping(mapping.id || 0)"
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
            <div v-if="commonMappingTotalItems > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
              <div class="flex-1 flex justify-between sm:hidden">
                <button
                  @click="previousCommonMappingPage"
                  :disabled="commonMappingCurrentPage === 1"
                  class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
                >
                  上一页
                </button>
                <button
                  @click="nextCommonMappingPage"
                  :disabled="commonMappingCurrentPage === commonMappingTotalPages"
                  class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
                >
                  下一页
                </button>
              </div>
              <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
                <div>
                  <p class="text-sm text-gray-700">
                    显示第 <span class="font-medium">{{ (commonMappingCurrentPage - 1) * commonMappingPageSize + 1 }}</span> 到
                    <span class="font-medium">{{ Math.min(commonMappingCurrentPage * commonMappingPageSize, commonMappingTotalItems) }}</span> 条，
                    共 <span class="font-medium">{{ commonMappingTotalItems }}</span> 条记录
                  </p>
                </div>
                <div>
                  <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
                    <button
                      @click="previousCommonMappingPage"
                      :disabled="commonMappingCurrentPage === 1"
                      class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                    >
                      上一页
                    </button>
                    <button
                      v-for="page in commonMappingVisiblePages"
                      :key="page"
                      @click="goToCommonMappingPage(page)"
                      :disabled="page === '...'"
                      :class="[
                        'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                        page === commonMappingCurrentPage
                          ? 'z-10 bg-blue-50 border-blue-500 text-blue-600'
                          : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50',
                        page === '...' ? 'cursor-default' : 'cursor-pointer'
                      ]"
                    >
                      {{ page }}
                    </button>
                    <button
                      @click="nextCommonMappingPage"
                      :disabled="commonMappingCurrentPage === commonMappingTotalPages"
                      class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                    >
                      下一页
                    </button>
                  </nav>
                </div>
              </div>
            </div>
          </div>

          <div class="flex justify-end space-x-3 pt-4">
            <button
              @click="closeCommonMappingDialog"
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              关闭
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 添加/编辑映射对话框 -->
    <div v-if="showAddMappingDialog || showEditMappingDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[60]">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              {{ showAddMappingDialog ? '添加兼容性映射' : '编辑兼容性映射' }}
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

          <form @submit.prevent="saveMapping" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金类型 *</label>
                <input
                  :value="selectedHotStampingType ? `${selectedHotStampingType.stampingType} - ${selectedHotStampingType.positionType || '默认'}` : '请先选择烫金类型'"
                  readonly
                  class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100 text-gray-600 cursor-not-allowed"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列 *</label>
                <div class="relative">
                  <input
                    type="text"
                    v-model="mappingProductSearchText"
                    @input="onMappingProductSearchInput"
                    @focus="showMappingProductDropdown = true"
                    @blur="handleMappingProductInputBlur"
                    placeholder="请输入或选择燙金紙系列"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
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
                      :class="{ 'bg-blue-100': mappingForm.productName === product }"
                    >
                      <div class="text-sm text-gray-900">{{ product }}</div>
                    </div>
                    <div v-if="filteredMappingProducts.length === 0" class="px-3 py-2 text-sm text-gray-500">
                      未找到匹配的燙金紙系列
                    </div>
                  </div>
                </div>
                <p v-if="mappingForm.productName" class="mt-1 text-xs text-gray-500">
                  已选择：{{ mappingForm.productName }}
                </p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                <select
                  v-model="mappingForm.paperType"
                  :disabled="!mappingForm.productName || loadingPaperTypes"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                >
                  <option value="">
                    {{ loadingPaperTypes ? '加载中...' : !mappingForm.productName ? '请先选择燙金紙系列' : '请选择烫金纸性能类型' }}
                  </option>
                  <option v-for="paperType in mappingPaperTypes" :key="paperType" :value="paperType">
                    {{ paperType }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
                <select
                  v-model="mappingForm.compatibilityStatus"
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
              <div class="flex items-center">
                <input
                  v-model="mappingForm.isActive"
                  type="checkbox"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
                <label class="ml-2 block text-sm text-gray-900">激活状态</label>
              </div>
            </div>

            <div class="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                @click="closeMappingDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
              <button
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
              >
                {{ showAddMappingDialog ? '添加' : '保存' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 批量复制映射对话框 -->
    <div v-if="showBatchCopyDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[70]">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">批量复制常用界面映射</h3>
            <button @click="closeBatchCopyDialog" class="text-gray-400 hover:text-gray-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          <form @submit.prevent="performBatchCopy" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">选择目标烫金类型 *</label>
              <select
                v-model="targetHotStampingTypeIds"
                multiple
                required
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 h-48"
              >
                <option v-for="type in availableHotStampingTypes" :key="type.id" :value="type.id">
                  {{ type.stampingType }} - {{ type.positionType || '默认' }}
                </option>
              </select>
              <p class="mt-1 text-sm text-gray-500">按住 Ctrl/Cmd 键可选择多个目标。已选择 {{ targetHotStampingTypeIds.length }} 个目标。</p>
            </div>
            <div class="flex justify-end space-x-3 pt-4">
              <button type="button" @click="closeBatchCopyDialog" class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
                取消
              </button>
              <button type="submit" class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-purple-600 hover:bg-purple-700">
                复制
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 查看全部配置对话框 -->
    <div v-if="showAllMappingsDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[56]">
      <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-7xl shadow-lg rounded-md bg-white my-8">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">查看全部配置</h3>
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
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金类型</label>
                <input
                  v-model="allMappingsSearchForm.hotStampingTypeName"
                  type="text"
                  placeholder="输入烫金类型进行筛选"
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
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金类型</th>
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
                      @change="toggleAllMappingsSelection(mapping.id!)"
                      class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                    />
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.hotStampingTypeName || '-' }}</td>
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
                    @click="allMappingsCurrentPage = typeof page === 'number' ? page : allMappingsCurrentPage"
                    :disabled="page === '...'"
                    :class="[
                      'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                      page === allMappingsCurrentPage
                        ? 'z-10 bg-blue-50 border-blue-500 text-blue-600'
                        : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50',
                      page === '...' ? 'cursor-default' : 'cursor-pointer'
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
        </div>
      </div>
    </div>

    <!-- 批量修改对话框 -->
    <div v-if="showAllMappingsBatchEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[60]">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">批量修改配置</h3>
            <button
              @click="closeAllMappingsBatchEditDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          <form @submit.prevent="batchUpdateAllMappings" class="space-y-4">
            <div class="bg-yellow-50 border-l-4 border-yellow-400 p-4 mb-4">
              <p class="text-sm text-yellow-700">
                提示：留空的字段将不会被修改。将修改 <span class="font-bold">{{ selectedAllMappingsItems.length }}</span> 条记录。
              </p>
            </div>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列</label>
                <input
                  v-model="allMappingsBatchForm.productName"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                <input
                  v-model="allMappingsBatchForm.paperType"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态</label>
                <select
                  v-model="allMappingsBatchForm.compatibilityStatus"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="">不修改</option>
                  <option value="V">适用</option>
                  <option value="X">不适用</option>
                  <option value="NA">不确定</option>
                  <option value="▷">需要打底处理</option>
                </select>
              </div>
            </div>
            <div class="flex justify-end space-x-3 pt-4">
              <button type="button" @click="closeAllMappingsBatchEditDialog" class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
                取消
              </button>
              <button type="submit" class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700">
                保存
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { hotStampingTypeOptionsApi, type HotStampingTypeOption, type CreateHotStampingTypeOption } from '../../../api/modules/hotStampingTypeOptions'
import { compatibilityApi } from '../../../api/modules/compatibility'
import { hotStampingTypeCompatibilityApi, type HotStampingTypeCompatibility } from '../../../api/modules/hotStampingTypeCompatibility'

// 路由实例
const router = useRouter()

// 响应式数据
const hotStampingTypes = ref<HotStampingTypeOption[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showMappingTypeDialog = ref(false)
const selectedHotStampingType = ref<HotStampingTypeOption | null>(null)

// 主要列表分页状态
const currentPage = ref(1)
const pageSize = ref(10)

// 常用界面映射相关数据
const showCommonMappingDialog = ref(false)
const showAddMappingDialog = ref(false)
const showEditMappingDialog = ref(false)
const showBatchCopyDialog = ref(false)
const commonMappingConfigs = ref<HotStampingTypeCompatibility[]>([])
const availableProducts = ref<string[]>([])
const availablePaperTypes = ref<string[]>([])
const filteredPaperTypes = ref<string[]>([])
const mappingPaperTypes = ref<string[]>([])
const loadingPaperTypes = ref(false)

// 映射对话框中的产品系列搜索
const mappingProductSearchText = ref('')
const showMappingProductDropdown = ref(false)
const selectedCommonMappingIds = ref<number[]>([])
const selectAllCommonMappings = ref(false)
const targetHotStampingTypeIds = ref<number[]>([])
const availableHotStampingTypes = ref<HotStampingTypeOption[]>([])

// 常用界面映射配置分页和筛选相关数据
const commonMappingSearchForm = reactive({
  productName: '',
  compatibilityStatus: '',
  paperType: '',
  status: ''
})
const commonMappingCurrentPage = ref(1)
const commonMappingPageSize = ref(10)
const commonMappingTotalItems = ref(0)

// 查看全部配置相关数据
const showAllMappingsDialog = ref(false)
const allMappings = ref<HotStampingTypeCompatibility[]>([])
const allMappingsSearchForm = reactive({
  hotStampingTypeName: '',
  productName: '',
  paperType: '',
  compatibilityStatus: ''
})
const allMappingsCurrentPage = ref(1)
const allMappingsPageSize = ref(10)
const allMappingsTotalItems = ref(0)
const selectedAllMappingsItems = ref<number[]>([])
const showAllMappingsBatchEditDialog = ref(false)
const allMappingsBatchForm = reactive({
  productName: '',
  paperType: '',
  compatibilityStatus: '' as 'V' | 'X' | 'NA' | '▷' | ''
})

// 映射表单数据
const mappingForm = reactive({
  id: undefined as number | undefined,
  hotStampingTypeId: undefined as number | undefined,
  productName: '',
  paperType: '',
  compatibilityStatus: '' as 'V' | 'X' | 'NA' | '▷' | '',
  isActive: true
})

// 下拉框选项数据
const stampingTypeOptions = ref<{ value: string; label: string }[]>([])
const positionTypeOptions = ref<{ value: string; label: string }[]>([])

// 搜索表单
const searchForm = reactive({
  stampingType: '',
  positionType: '',
  isActive: ''
})

// 烫金类型表单
const hotStampingTypeForm = reactive({
  id: null,
  stampingType: '',
  positionType: '',
  description: '',
  isActive: true,
  sortOrder: 0
})

// 方法
const loadHotStampingTypes = async () => {
  loading.value = true
  try {
    // 调用真实的API
    const response = await hotStampingTypeOptionsApi.getAllTypes()
    hotStampingTypes.value = response || []
  } catch (error) {
    console.error('加载烫金类型失败:', error)
    hotStampingTypes.value = []
    alert('加载烫金类型失败，请刷新页面重试')
  } finally {
    loading.value = false
  }
}

// 加载烫金类型选项（用于下拉框）
const loadStampingTypeOptions = async () => {
  try {
    // 从现有数据中提取唯一的烫金类型
    const uniqueTypes = new Set<string>()
    hotStampingTypes.value.forEach(item => {
      uniqueTypes.add(item.stampingType)
    })
    stampingTypeOptions.value = Array.from(uniqueTypes).map(type => ({
      value: type,
      label: type
    }))
    
    console.log('烫金类型选项:', stampingTypeOptions.value)
  } catch (error) {
    console.error('加载烫金类型选项失败:', error)
    stampingTypeOptions.value = []
  }
}

// 根据烫金类型加载位置选项
const loadPositionOptions = async (stampingType: string) => {
  if (!stampingType) {
    positionTypeOptions.value = []
    return
  }
  
  try {
    const response = await compatibilityApi.getPositionOptionsByStampingType(stampingType)
    positionTypeOptions.value = response.data.map((item: any) => ({
      value: item.positionType || '默认',
      label: item.positionType || '默认'
    }))
    
    console.log('位置类型选项:', positionTypeOptions.value)
  } catch (error) {
    console.error('加载位置选项失败:', error)
    // 如果API调用失败，从现有数据中提取
    const uniquePositions = new Set<string>()
    hotStampingTypes.value
      .filter(item => item.stampingType === stampingType)
      .forEach(item => {
        if (item.positionType) {
          uniquePositions.add(item.positionType)
        }
      })
    positionTypeOptions.value = Array.from(uniquePositions).map(position => ({
      value: position,
      label: position
    }))
  }
}

// 筛选后的数据
const filteredData = computed(() => {
  let filtered = hotStampingTypes.value

  // 按烫金类型筛选
  if (searchForm.stampingType) {
    filtered = filtered.filter(item => item.stampingType === searchForm.stampingType)
  }

  // 按位置类型筛选
  if (searchForm.positionType) {
    filtered = filtered.filter(item => item.positionType === searchForm.positionType)
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
const filteredHotStampingTypes = paginatedData

// 当筛选结果或分页大小变化时，自动纠正当前页，避免越界
watch([filteredData, pageSize], () => {
  const total = filteredData.value.length
  const maxPage = Math.max(1, Math.ceil(total / pageSize.value))
  if (currentPage.value > maxPage) {
    currentPage.value = maxPage
  }
})

const searchHotStampingTypes = () => {
  // 筛选逻辑已经通过计算属性实现，这里不需要重新加载数据
  currentPage.value = 1 // 搜索后重置到第一页
  console.log('筛选条件:', searchForm)
  console.log('筛选结果:', filteredHotStampingTypes.value)
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

// 常用界面映射配置筛选和分页计算属性
const filteredCommonMappingConfigs = computed(() => {
  let filtered = commonMappingConfigs.value

  // 按产品名称筛选
  if (commonMappingSearchForm.productName) {
    filtered = filtered.filter(mapping => 
      mapping.productName === commonMappingSearchForm.productName
    )
  }

  // 按烫金纸性能类型筛选
  if (commonMappingSearchForm.paperType) {
    filtered = filtered.filter(mapping => 
      mapping.paperType === commonMappingSearchForm.paperType
    )
  }

  // 按兼容性状态筛选
  if (commonMappingSearchForm.compatibilityStatus) {
    filtered = filtered.filter(mapping => 
      mapping.compatibilityStatus === commonMappingSearchForm.compatibilityStatus
    )
  }

  // 按状态筛选
  if (commonMappingSearchForm.status) {
    const isActive = commonMappingSearchForm.status === 'true'
    filtered = filtered.filter(mapping => 
      (mapping.isActive !== false) === isActive
    )
  }

  // 分页
  const start = (commonMappingCurrentPage.value - 1) * commonMappingPageSize.value
  const end = start + commonMappingPageSize.value
  commonMappingTotalItems.value = filtered.length
  return filtered.slice(start, end)
})

const commonMappingTotalPages = computed(() => Math.ceil(commonMappingTotalItems.value / commonMappingPageSize.value))

const commonMappingVisiblePages = computed(() => {
  const pages = []
  const total = commonMappingTotalPages.value
  const current = commonMappingCurrentPage.value
  
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

// 常用界面映射配置搜索和分页方法
const searchCommonMappingConfigs = () => {
  commonMappingCurrentPage.value = 1
}

const previousCommonMappingPage = () => {
  if (commonMappingCurrentPage.value > 1) {
    commonMappingCurrentPage.value--
  }
}

const nextCommonMappingPage = () => {
  if (commonMappingCurrentPage.value < commonMappingTotalPages.value) {
    commonMappingCurrentPage.value++
  }
}

const goToCommonMappingPage = (page: number) => {
  if (page >= 1 && page <= commonMappingTotalPages.value) {
    commonMappingCurrentPage.value = page
  }
}

const editHotStampingType = (hotStampingType: HotStampingTypeOption) => {
  Object.assign(hotStampingTypeForm, hotStampingType)
  showEditDialog.value = true
}

const toggleStatus = async (hotStampingType: HotStampingTypeOption) => {
  try {
    await hotStampingTypeOptionsApi.updateType(hotStampingType.id, { isActive: !hotStampingType.isActive })
    await loadHotStampingTypes()
  } catch (error) {
    console.error('更新状态失败:', error)
    // 如果API调用失败，使用本地数据更新
    hotStampingType.isActive = !hotStampingType.isActive
  }
}

const deleteHotStampingType = async (id: number) => {
  if (confirm('确定要删除这个烫金类型吗？')) {
    try {
      await hotStampingTypeOptionsApi.deleteType(id)
      await loadHotStampingTypes()
    } catch (error) {
      console.error('删除烫金类型失败:', error)
    }
  }
}

const saveHotStampingType = async () => {
  try {
    if (showAddDialog.value) {
      await hotStampingTypeOptionsApi.createType(hotStampingTypeForm as CreateHotStampingTypeOption)
    } else {
      await hotStampingTypeOptionsApi.updateType(hotStampingTypeForm.id!, hotStampingTypeForm)
    }
    closeDialog()
    await loadHotStampingTypes()
  } catch (error) {
    console.error('保存烫金类型失败:', error)
  }
}

const openMappingTypeDialog = (hotStampingType: HotStampingTypeOption) => {
  selectedHotStampingType.value = hotStampingType
  showMappingTypeDialog.value = true
}


const closeMappingTypeDialog = () => {
  showMappingTypeDialog.value = false
  selectedHotStampingType.value = null
}

const selectMappingType = (type: 'common' | 'durable') => {
  if (!selectedHotStampingType.value) {
    console.error('没有选中的烫金类型')
    return
  }
  
  console.log('选择映射类型:', type, '选中的烫金类型:', selectedHotStampingType.value)
  
  if (type === 'durable') {
    // 耐磨金纸映射 - 跳转到原逻辑
    router.push({
      path: '/admin/material/smart-compatibility',
      query: { hotStampingTypeId: selectedHotStampingType.value.id }
    })
    // 跳转后清空选中的烫金类型
    closeMappingTypeDialog()
  } else if (type === 'common') {
    // 常用界面映射 - 打开映射配置对话框
    console.log('打开常用界面映射对话框，烫金类型ID:', selectedHotStampingType.value.id)
    showCommonMappingDialog.value = true
    loadCommonMappingConfigs(selectedHotStampingType.value.id)
    loadAvailableProducts()
    loadAvailablePaperTypes()
    // 只关闭映射类型选择对话框，但保留选中的烫金类型
    showMappingTypeDialog.value = false
  }
}

// 常用界面映射相关方法
const closeCommonMappingDialog = () => {
  showCommonMappingDialog.value = false
  showAddMappingDialog.value = false
  showEditMappingDialog.value = false
  commonMappingConfigs.value = []
  // 清空选中的烫金类型
  selectedHotStampingType.value = null
  Object.assign(commonMappingSearchForm, {
    productName: '',
    compatibilityStatus: '',
    paperType: '',
    status: ''
  })
  Object.assign(mappingForm, {
    id: undefined,
    productName: '',
    paperType: '',
    compatibilityStatus: '',
    isActive: true
  })
}

const loadCommonMappingConfigs = async (hotStampingTypeId: number) => {
  try {
    console.log('开始加载常用界面映射配置，hotStampingTypeId:', hotStampingTypeId)
    console.log('API URL:', `/api/hot-stamping-type-compatibility/hot-stamping-type/${hotStampingTypeId}`)
    
    const data = await hotStampingTypeCompatibilityApi.getCompatibilityByHotStampingTypeId(hotStampingTypeId)
    console.log('获取到的数据:', data)
    console.log('数据类型:', typeof data, '数组长度:', Array.isArray(data) ? data.length : '不是数组')
    
    commonMappingConfigs.value = data
    console.log('设置后的 commonMappingConfigs.value:', commonMappingConfigs.value)
    console.log('设置后的数组长度:', commonMappingConfigs.value.length)
    
    // 重置分页和搜索状态
    commonMappingCurrentPage.value = 1
    Object.assign(commonMappingSearchForm, {
      productName: '',
      compatibilityStatus: '',
      paperType: ''
    })
  } catch (error) {
    console.error('加载常用界面映射配置失败:', error)
    console.error('错误详情:', error)
    commonMappingConfigs.value = []
  }
}

const loadAvailableProducts = async () => {
  try {
    console.log('开始加载产品列表...')
    const products = await hotStampingTypeCompatibilityApi.getAllProductNames()
    console.log('加载到的产品列表:', products)
    availableProducts.value = products
  } catch (error) {
    console.error('加载产品列表失败:', error)
    availableProducts.value = []
  }
}

const loadAvailablePaperTypes = async () => {
  try {
    availablePaperTypes.value = await hotStampingTypeCompatibilityApi.getAllPaperTypes()
  } catch (error) {
    console.error('加载烫金纸性能类型列表失败:', error)
    availablePaperTypes.value = []
  }
}

// 联动选择：产品名称变化时加载对应的烫金纸性能类型
const onProductNameChange = async () => {
  if (commonMappingSearchForm.productName) {
    loadingPaperTypes.value = true
    try {
      console.log('正在加载产品名称对应的烫金纸性能类型:', commonMappingSearchForm.productName)
      // 根据产品名称获取对应的烫金纸性能类型
      const paperTypes = await hotStampingTypeCompatibilityApi.getPaperTypesByProductName(commonMappingSearchForm.productName)
      console.log('获取到的烫金纸性能类型:', paperTypes)
      filteredPaperTypes.value = paperTypes
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
  commonMappingSearchForm.paperType = ''
}

// 映射对话框中的产品系列过滤
const filteredMappingProducts = computed(() => {
  if (!mappingProductSearchText.value || mappingProductSearchText.value.trim() === '') {
    return availableProducts.value
  }
  
  const searchText = mappingProductSearchText.value.toLowerCase().trim()
  return availableProducts.value.filter(product => 
    product && product.toLowerCase().includes(searchText)
  )
})

// 映射对话框中的产品系列搜索相关方法
const onMappingProductSearchInput = () => {
  showMappingProductDropdown.value = true
}

const selectMappingProduct = async (product: string) => {
  mappingForm.productName = product
  mappingProductSearchText.value = product
  showMappingProductDropdown.value = false
  await onMappingProductNameChange()
}

const handleMappingProductInputBlur = () => {
  setTimeout(() => {
    showMappingProductDropdown.value = false
    // 如果输入框有值但没有匹配到产品，尝试精确匹配
    if (mappingProductSearchText.value && !mappingForm.productName) {
      const exactMatch = availableProducts.value.find(p => 
        p === mappingProductSearchText.value.trim()
      )
      if (exactMatch) {
        selectMappingProduct(exactMatch)
      } else {
        // 如果没有精确匹配，使用输入值进行模糊搜索
        mappingForm.productName = mappingProductSearchText.value.trim()
        onMappingProductNameChange()
      }
    } else if (!mappingForm.productName) {
      // 如果没有选择产品，清空输入
      mappingProductSearchText.value = ''
    } else {
      // 如果已选择产品，同步搜索文本
      mappingProductSearchText.value = mappingForm.productName
    }
  }, 200)
}

// 映射表单中的产品名称变化
const onMappingProductNameChange = async () => {
  if (mappingForm.productName) {
    loadingPaperTypes.value = true
    try {
      console.log('映射表单：正在加载产品名称对应的烫金纸性能类型:', mappingForm.productName)
      // 根据产品名称获取对应的烫金纸性能类型
      const paperTypes = await hotStampingTypeCompatibilityApi.getPaperTypesByProductName(mappingForm.productName)
      console.log('映射表单：获取到的烫金纸性能类型:', paperTypes)
      mappingPaperTypes.value = paperTypes
    } catch (error) {
      console.error('映射表单：加载烫金纸性能类型失败:', error)
      mappingPaperTypes.value = []
    } finally {
      loadingPaperTypes.value = false
    }
  } else {
    mappingPaperTypes.value = []
  }
  // 注意：编辑时不清空已选择的烫金纸性能类型
  if (showAddMappingDialog.value) {
    mappingForm.paperType = ''
  }
}

const saveCommonMapping = async (mapping: HotStampingTypeCompatibility) => {
  try {
    if (mapping.id) {
      await hotStampingTypeCompatibilityApi.updateCompatibility(mapping.id, mapping)
      console.log('常用界面映射配置保存成功')
    }
  } catch (error) {
    console.error('保存常用界面映射配置失败:', error)
  }
}

const deleteCommonMapping = async (id: number) => {
  if (confirm('确定要删除这个映射配置吗？')) {
    try {
      await hotStampingTypeCompatibilityApi.deleteCompatibility(id)
      if (selectedHotStampingType.value) {
        await loadCommonMappingConfigs(selectedHotStampingType.value.id)
      }
    } catch (error) {
      console.error('删除常用界面映射配置失败:', error)
    }
  }
}

// 编辑映射
const editCommonMapping = async (mapping: HotStampingTypeCompatibility) => {
  Object.assign(mappingForm, {
    id: mapping.id,
    hotStampingTypeId: mapping.hotStampingTypeId,
    productName: mapping.productName,
    paperType: mapping.paperType || '',
    compatibilityStatus: mapping.compatibilityStatus,
    isActive: mapping.isActive !== false
  })
  
  // 设置搜索文本
  mappingProductSearchText.value = mapping.productName || ''
  showMappingProductDropdown.value = false
  
  // 如果已有产品名称，加载对应的烫金纸性能类型
  if (mapping.productName) {
    await onMappingProductNameChange()
  }
  
  showEditMappingDialog.value = true
}

// 切换映射状态
const toggleMappingStatus = async (mapping: HotStampingTypeCompatibility) => {
  try {
    const updatedMapping = {
      ...mapping,
      isActive: !(mapping.isActive !== false)
    }
    await hotStampingTypeCompatibilityApi.updateCompatibility(mapping.id!, updatedMapping)
    if (selectedHotStampingType.value) {
      await loadCommonMappingConfigs(selectedHotStampingType.value.id)
    }
  } catch (error) {
    console.error('切换映射状态失败:', error)
  }
}

// 全选/取消全选常用界面映射
const toggleSelectAllCommonMappings = () => {
  if (selectAllCommonMappings.value) {
    selectedCommonMappingIds.value = filteredCommonMappingConfigs.value
      .map(m => m.id)
      .filter((id): id is number => id !== undefined) as number[]
  } else {
    selectedCommonMappingIds.value = []
  }
}

// 监听选中项变化，更新全选状态
watch(selectedCommonMappingIds, (newVal) => {
  const filteredIds = filteredCommonMappingConfigs.value
    .map(m => m.id)
    .filter((id): id is number => id !== undefined) as number[]
  selectAllCommonMappings.value = filteredIds.length > 0 && 
    filteredIds.every(id => newVal.includes(id))
}, { deep: true })

// 打开批量复制对话框
const openBatchCopyDialog = async () => {
  if (selectedCommonMappingIds.value.length === 0) {
    alert('请先选择要复制的映射')
    return
  }
  
  try {
    // 加载所有可用的烫金类型（排除当前选中的烫金类型）
    const allTypes = await hotStampingTypeOptionsApi.getAllTypes()
    availableHotStampingTypes.value = allTypes.filter(
      type => !selectedHotStampingType.value || type.id !== selectedHotStampingType.value.id
    )
    targetHotStampingTypeIds.value = []
    showBatchCopyDialog.value = true
  } catch (error) {
    console.error('加载烫金类型列表失败:', error)
    alert('加载烫金类型列表失败: ' + (error as Error).message)
  }
}

// 关闭批量复制对话框
const closeBatchCopyDialog = () => {
  showBatchCopyDialog.value = false
  targetHotStampingTypeIds.value = []
}

// 执行批量复制
const performBatchCopy = async () => {
  if (selectedCommonMappingIds.value.length === 0) {
    alert('请先选择要复制的映射')
    return
  }
  
  if (targetHotStampingTypeIds.value.length === 0) {
    alert('请选择至少一个目标烫金类型')
    return
  }
  
  try {
    const result = await hotStampingTypeCompatibilityApi.batchCopyCompatibility(
      selectedCommonMappingIds.value,
      targetHotStampingTypeIds.value
    )
    
    alert(`${result.message}\n成功: ${result.successCount} 条，失败: ${result.failCount} 条`)
    
    // 关闭对话框并刷新列表
    closeBatchCopyDialog()
    selectedCommonMappingIds.value = []
    selectAllCommonMappings.value = false
    
    if (selectedHotStampingType.value) {
      await loadCommonMappingConfigs(selectedHotStampingType.value.id)
    }
  } catch (error) {
    console.error('批量复制失败:', error)
    alert('批量复制失败: ' + (error as Error).message)
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedCommonMappingIds.value.length === 0) {
    alert('请先选择要删除的映射')
    return
  }
  
  if (!confirm(`确定要删除选中的 ${selectedCommonMappingIds.value.length} 条映射吗？此操作不可恢复！`)) {
    return
  }
  
  try {
    const result = await hotStampingTypeCompatibilityApi.batchDeleteCompatibility(selectedCommonMappingIds.value)
    if (result.success) {
      alert(result.message)
      selectedCommonMappingIds.value = []
      selectAllCommonMappings.value = false
      
      if (selectedHotStampingType.value) {
        await loadCommonMappingConfigs(selectedHotStampingType.value.id)
      }
    } else {
      alert('批量删除失败: ' + result.message)
    }
  } catch (error) {
    console.error('批量删除失败:', error)
    alert('批量删除失败: ' + (error as Error).message)
  }
}

// 保存映射
const saveMapping = async () => {
  console.log('保存映射 - 表单数据:', mappingForm)
  console.log('保存映射 - 选中的烫金类型:', selectedHotStampingType.value)
  console.log('保存映射 - 可用产品列表:', availableProducts.value)
  
  if (!mappingForm.hotStampingTypeId || !mappingForm.productName || !mappingForm.compatibilityStatus) {
    const missingFields = []
    if (!mappingForm.hotStampingTypeId) missingFields.push('烫金类型')
    if (!mappingForm.productName) missingFields.push('燙金紙系列')
    if (!mappingForm.compatibilityStatus) missingFields.push('兼容性状态')
    
    console.error('缺少必填字段:', missingFields)
    alert(`请填写必填字段: ${missingFields.join(', ')}`)
    return
  }

  try {
    const mappingData = {
      productName: mappingForm.productName,
      hotStampingTypeId: mappingForm.hotStampingTypeId,
      compatibilityStatus: mappingForm.compatibilityStatus,
      paperType: mappingForm.paperType,
      isActive: mappingForm.isActive
    }

    if (showAddMappingDialog.value) {
      await hotStampingTypeCompatibilityApi.createCompatibility(mappingData)
    } else {
      await hotStampingTypeCompatibilityApi.updateCompatibility(mappingForm.id!, mappingData)
    }

    // 重新加载映射配置列表
    if (mappingForm.hotStampingTypeId) {
      await loadCommonMappingConfigs(mappingForm.hotStampingTypeId)
    }
    closeMappingDialog()
  } catch (error: any) {
    console.error('保存映射失败:', error)
    
    // 处理后端返回的错误信息
    if (error.response && error.response.data && error.response.data.error) {
      alert(error.response.data.error)
    } else if (error.response && error.response.status === 400) {
      alert('该燙金紙系列和烫金类型的组合已存在，请选择其他组合')
    } else {
      alert('保存失败，请重试')
    }
  }
}

// 打开添加映射对话框
const openAddMappingDialog = async () => {
  console.log('打开添加映射对话框')
  console.log('当前可用产品列表:', availableProducts.value)
  console.log('选中的烫金类型:', selectedHotStampingType.value)
  
  // 确保数据已加载
  if (availableProducts.value.length === 0) {
    console.log('产品列表为空，重新加载...')
    await loadAvailableProducts()
  }
  
  // 自动设置烫金类型ID
  if (selectedHotStampingType.value) {
    mappingForm.hotStampingTypeId = selectedHotStampingType.value.id
    console.log('自动设置烫金类型ID:', mappingForm.hotStampingTypeId)
  }
  
  // 重置搜索文本和下拉状态
  mappingProductSearchText.value = ''
  showMappingProductDropdown.value = false
  mappingPaperTypes.value = []
  
  showAddMappingDialog.value = true
}

// 关闭映射对话框
const closeMappingDialog = () => {
  showAddMappingDialog.value = false
  showEditMappingDialog.value = false
  Object.assign(mappingForm, {
    id: undefined,
    hotStampingTypeId: undefined,
    productName: '',
    paperType: '',
    compatibilityStatus: '',
    isActive: true
  })
  mappingProductSearchText.value = ''
  showMappingProductDropdown.value = false
  mappingPaperTypes.value = []
}

const saveAllCommonMappings = async () => {
  try {
    const updates = commonMappingConfigs.value.map(mapping => ({
      id: mapping.id,
      productName: mapping.productName,
      hotStampingTypeId: mapping.hotStampingTypeId,
      compatibilityStatus: mapping.compatibilityStatus,
      paperType: mapping.paperType
    }))

    // 批量更新所有映射配置
    const updatePromises = updates.map(update => 
      hotStampingTypeCompatibilityApi.updateCompatibility(update.id!, update)
    )
    
    await Promise.all(updatePromises)
    console.log('所有常用界面映射配置保存成功')
    
    // 重新加载配置
    if (selectedHotStampingType.value) {
      await loadCommonMappingConfigs(selectedHotStampingType.value.id)
    }
  } catch (error) {
    console.error('批量保存常用界面映射配置失败:', error)
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(hotStampingTypeForm, {
    id: null,
    stampingType: '',
    positionType: '',
    description: '',
    isActive: true,
    sortOrder: 0
  })
}

// 监听烫金类型变化，实现联动效果
watch(() => searchForm.stampingType, (newStampingType) => {
  // 清空位置类型选择
  searchForm.positionType = ''
  // 加载对应的位置选项
  loadPositionOptions(newStampingType)
})

// 导出数据
const exportData = async () => {
  try {
    // 调用导出API
    const response = await fetch('/api/hot-stamping-type-options/export', {
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
    let fileName = '烫金类型选项.xlsx'
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

// 查看全部配置相关方法
const openAllMappingsDialog = async () => {
  showAllMappingsDialog.value = true
  await loadAllMappings()
  Object.assign(allMappingsSearchForm, {
    hotStampingTypeName: '',
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
    const data = await hotStampingTypeCompatibilityApi.getAllCompatibility()
    allMappings.value = data
    allMappingsTotalItems.value = data.length
  } catch (error) {
    console.error('加载所有配置失败:', error)
    alert('加载所有配置失败')
    allMappings.value = []
  }
}

const filteredAllMappings = computed(() => {
  let filtered = [...allMappings.value]
  
  if (allMappingsSearchForm.hotStampingTypeName) {
    const searchText = allMappingsSearchForm.hotStampingTypeName.toLowerCase().trim()
    filtered = filtered.filter(m => 
      m.hotStampingTypeName && m.hotStampingTypeName.toLowerCase().includes(searchText)
    )
  }
  
  if (allMappingsSearchForm.productName) {
    const searchText = allMappingsSearchForm.productName.toLowerCase().trim()
    filtered = filtered.filter(m => 
      m.productName && m.productName.toLowerCase().includes(searchText)
    )
  }
  
  if (allMappingsSearchForm.paperType) {
    const searchText = allMappingsSearchForm.paperType.toLowerCase().trim()
    filtered = filtered.filter(m => 
      m.paperType && m.paperType.toLowerCase().includes(searchText)
    )
  }
  
  if (allMappingsSearchForm.compatibilityStatus) {
    filtered = filtered.filter(m => 
      m.compatibilityStatus === allMappingsSearchForm.compatibilityStatus
    )
  }
  
  // 排序
  filtered.sort((a, b) => {
    const nameA = (a.hotStampingTypeName || '').localeCompare(b.hotStampingTypeName || '')
    if (nameA !== 0) return nameA
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
  const pages: (number | string)[] = []
  const start = Math.max(1, allMappingsCurrentPage.value - 2)
  const end = Math.min(allMappingsTotalPages.value, start + 4)
  
  if (start > 1) {
    pages.push(1)
    if (start > 2) pages.push('...')
  }
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  
  if (end < allMappingsTotalPages.value) {
    if (end < allMappingsTotalPages.value - 1) pages.push('...')
    pages.push(allMappingsTotalPages.value)
  }
  
  return pages
})

const isAllMappingsSelected = computed(() => {
  const currentPageIds = filteredAllMappings.value.map(m => m.id).filter((id): id is number => id !== undefined)
  return currentPageIds.length > 0 && currentPageIds.every(id => selectedAllMappingsItems.value.includes(id))
})

const searchAllMappings = () => {
  allMappingsCurrentPage.value = 1
}

const resetAllMappingsSearch = () => {
  Object.assign(allMappingsSearchForm, {
    hotStampingTypeName: '',
    productName: '',
    paperType: '',
    compatibilityStatus: ''
  })
  allMappingsCurrentPage.value = 1
}

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
    const currentPageIds = filteredAllMappings.value.map(m => m.id).filter((id): id is number => id !== undefined)
    selectedAllMappingsItems.value = [...new Set([...selectedAllMappingsItems.value, ...currentPageIds])]
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
  
  try {
    const updateFields: any = {}
    if (allMappingsBatchForm.productName) {
      updateFields.productName = allMappingsBatchForm.productName
    }
    if (allMappingsBatchForm.paperType) {
      updateFields.paperType = allMappingsBatchForm.paperType
    }
    if (allMappingsBatchForm.compatibilityStatus) {
      updateFields.compatibilityStatus = allMappingsBatchForm.compatibilityStatus
    }
    
    if (Object.keys(updateFields).length === 0) {
      alert('请至少填写一个要修改的字段')
      return
    }
    
    // 批量更新选中的映射
    const updatePromises = selectedAllMappingsItems.value.map(id => {
      const mapping = allMappings.value.find(m => m.id === id)
      if (!mapping) return Promise.resolve()
      
      const updateData = {
        ...mapping,
        ...updateFields
      }
      return hotStampingTypeCompatibilityApi.updateCompatibility(id, updateData)
    })
    
    await Promise.all(updatePromises)
    alert(`成功更新 ${selectedAllMappingsItems.value.length} 条记录`)
    
    // 重新加载数据
    await loadAllMappings()
    closeAllMappingsBatchEditDialog()
    clearAllMappingsSelection()
  } catch (error: any) {
    console.error('批量更新失败:', error)
    const errorMessage = error.response?.data?.error || error.message || '批量更新失败'
    alert(errorMessage)
  }
}

const batchDeleteAllMappings = async () => {
  if (selectedAllMappingsItems.value.length === 0) {
    alert('请选择要删除的映射')
    return
  }
  
  if (!confirm(`确定要删除选中的 ${selectedAllMappingsItems.value.length} 条映射吗？`)) {
    return
  }
  
  try {
    const result = await hotStampingTypeCompatibilityApi.batchDeleteCompatibility(selectedAllMappingsItems.value)
    if (result.success) {
      alert(result.message)
      await loadAllMappings()
      clearAllMappingsSelection()
    } else {
      alert(result.message || '批量删除失败')
    }
  } catch (error: any) {
    console.error('批量删除失败:', error)
    const errorMessage = error.response?.data?.message || error.message || '批量删除失败'
    alert(errorMessage)
  }
}

const viewMappingDetails = (mapping: HotStampingTypeCompatibility) => {
  // 找到对应的烫金类型
  const hotStampingType = hotStampingTypes.value.find(t => t.id === mapping.hotStampingTypeId)
  if (hotStampingType) {
    selectedHotStampingType.value = hotStampingType
    showCommonMappingDialog.value = true
    loadCommonMappingConfigs(mapping.hotStampingTypeId!)
    loadAvailableProducts()
    loadAvailablePaperTypes()
    showAllMappingsDialog.value = false
  } else {
    alert('无法找到对应的烫金类型配置')
  }
}

// 生命周期
onMounted(async () => {
  await loadHotStampingTypes()
  await loadStampingTypeOptions()
})
</script>

<style scoped>
/* 可以添加特定的样式 */
</style>

