<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">耐磨金纸映射管理</h1>
            <p class="mt-2 text-gray-600">配置耐磨金纸的兼容性映射，固定烫金类型为：燙金後擊凸</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="showAddDialog = true"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-yellow-600 hover:bg-yellow-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加耐磨金纸映射
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
              @click="exportMappings"
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

      <!-- 固定烫金类型提示 -->
      <div class="bg-yellow-50 border border-yellow-200 rounded-lg p-4 mb-6">
        <div class="flex">
          <div class="flex-shrink-0">
            <svg class="h-5 w-5 text-yellow-400" fill="currentColor" viewBox="0 0 20 20">
              <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
            </svg>
          </div>
          <div class="ml-3">
            <h3 class="text-sm font-medium text-yellow-800">固定烫金类型</h3>
            <div class="mt-2 text-sm text-yellow-700">
              <p>此映射类型固定使用烫金类型：<strong>燙金後擊凸</strong></p>
            </div>
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
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
            >
              <option value="">全部</option>
              <option v-for="product in searchProductOptions" :key="product" :value="product">
                {{ product }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
            <select
              v-model="searchForm.productModelNumber"
              :disabled="!searchForm.productName"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
            >
              <option value="">全部</option>
              <option v-for="model in searchProductModelOptions" :key="model" :value="model">
                {{ model }}
              </option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">耐磨金纸类型</label>
            <select
              v-model="searchForm.goldPaperType"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
            >
              <option value="">全部</option>
              <option v-for="type in searchGoldPaperTypeOptions" :key="type" :value="type">
                {{ type }}
              </option>
            </select>
          </div>
          <div class="flex items-end space-x-2">
            <button
              @click="searchWearResistantGoldPaperMappings"
              class="flex-1 bg-yellow-600 text-white px-4 py-2 rounded-md hover:bg-yellow-700 transition-colors"
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
              @click="batchDeleteMappings"
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

      <!-- 耐磨金纸映射列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">耐磨金纸映射列表</h3>
        </div>
        
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left">
                  <input
                    type="checkbox"
                    :checked="selectedItems.length === wearResistantGoldPaperMappings.length && wearResistantGoldPaperMappings.length > 0"
                    @change="toggleSelectAll"
                    class="rounded border-gray-300 text-yellow-600 focus:ring-yellow-500"
                  />
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">燙金紙系列</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品型号</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">耐磨金纸类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">备注</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="mapping in wearResistantGoldPaperMappings" :key="mapping.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <input
                    type="checkbox"
                    :checked="selectedItems.includes(mapping.id as number)"
                    @change="toggleSelectItem(mapping.id as number)"
                    class="rounded border-gray-300 text-yellow-600 focus:ring-yellow-500"
                  />
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm font-medium text-gray-900">{{ mapping.productName }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ mapping.productModelNumber || '-' }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-yellow-100 text-yellow-800">
                    燙金後擊凸
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ mapping.goldPaperType }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                        :class="getCompatibilityStatusClass(mapping.compatibilityStatus)">
                    {{ getCompatibilityStatusText(mapping.compatibilityStatus) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ mapping.remarks || '-' }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button
                      @click="editWearResistantGoldPaperMapping(mapping)"
                      class="text-yellow-600 hover:text-yellow-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="copyWearResistantGoldPaperMapping(mapping)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      复制
                    </button>
                    <button
                      @click="openMappingConfigDialog(mapping)"
                      class="text-indigo-600 hover:text-indigo-900"
                    >
                      配置映射
                    </button>
                    <button
                      @click="deleteWearResistantGoldPaperMapping(mapping.id!)"
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
        <div v-if="totalPages > 0" class="px-6 py-4 border-t border-gray-200 flex items-center justify-between">
          <div class="text-sm text-gray-700">
            显示第 {{ (currentPage - 1) * pageSize + 1 }} 到 {{ Math.min(currentPage * pageSize, totalItems) }} 条，共 {{ totalItems }} 条记录
          </div>
          <div class="flex space-x-2">
            <button
              @click="currentPage = 1"
              :disabled="currentPage === 1"
              class="px-3 py-1 border border-gray-300 rounded-md text-sm disabled:bg-gray-100 disabled:cursor-not-allowed hover:bg-gray-50"
            >
              首页
            </button>
            <button
              @click="currentPage--"
              :disabled="currentPage === 1"
              class="px-3 py-1 border border-gray-300 rounded-md text-sm disabled:bg-gray-100 disabled:cursor-not-allowed hover:bg-gray-50"
            >
              上一页
            </button>
            <span class="px-3 py-1 text-sm text-gray-700">
              第 {{ currentPage }} / {{ totalPages }} 页
            </span>
            <button
              @click="currentPage++"
              :disabled="currentPage >= totalPages"
              class="px-3 py-1 border border-gray-300 rounded-md text-sm disabled:bg-gray-100 disabled:cursor-not-allowed hover:bg-gray-50"
            >
              下一页
            </button>
            <button
              @click="currentPage = totalPages"
              :disabled="currentPage >= totalPages"
              class="px-3 py-1 border border-gray-300 rounded-md text-sm disabled:bg-gray-100 disabled:cursor-not-allowed hover:bg-gray-50"
            >
              末页
            </button>
          </div>
        </div>
        
        <!-- 加载状态 -->
        <div v-if="loading" class="px-6 py-8 text-center text-gray-500">
          加载中...
        </div>
        
        <!-- 空数据提示 -->
        <div v-if="!loading && wearResistantGoldPaperMappings.length === 0" class="px-6 py-8 text-center text-gray-500">
          暂无数据
        </div>
      </div>

      <!-- 添加/编辑对话框 -->
      <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">
                {{ showAddDialog ? '添加耐磨金纸映射' : '编辑耐磨金纸映射' }}
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

            <form @submit.prevent="saveWearResistantGoldPaperMapping">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列 *</label>
                  <select
                    v-model="mappingForm.productName"
                    @change="onProductNameChange"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                  >
                    <option value="">请选择燙金紙系列</option>
                    <option v-for="product in availableProducts" :key="product" :value="product">
                      {{ product }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
                  <select
                    v-model="mappingForm.productModelNumber"
                    :disabled="!mappingForm.productName || loadingProductModels"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingProductModels ? '加载中...' : !mappingForm.productName ? '请先选择燙金紙系列' : '请选择产品型号（可选）' }}
                    </option>
                    <option v-for="model in availableProductModels" :key="model" :value="model">
                      {{ model }}
                    </option>
                  </select>
                  <p class="mt-1 text-xs text-gray-500">留空表示系列级映射（适用于该系列所有型号）</p>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">烫金类型</label>
                  <input
                    type="text"
                    value="燙金後擊凸"
                    disabled
                    class="w-full px-3 py-2 border border-gray-300 rounded-md bg-gray-100 text-gray-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">耐磨金纸类型 *</label>
                  <select
                    v-model="mappingForm.goldPaperType"
                    :disabled="loadingGoldPaperTypes"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  >
                    <option value="">
                      {{ loadingGoldPaperTypes ? '加载中...' : '请选择耐磨金纸类型' }}
                    </option>
                    <option v-for="type in availableGoldPaperTypes" :key="type" :value="type">
                      {{ type }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
                  <select
                    v-model="mappingForm.compatibilityStatus"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                  >
                    <option value="V">适用</option>
                    <option value="X">不适用</option>
                    <option value="NA">不确定</option>
                    <option value="▷">需要打底处理</option>
                  </select>
                </div>
                <div class="md:col-span-2">
                  <label class="block text-sm font-medium text-gray-700 mb-2">备注</label>
                  <textarea
                    v-model="mappingForm.remarks"
                    rows="3"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                    placeholder="请输入备注信息"
                  ></textarea>
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
                  class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-yellow-600 hover:bg-yellow-700"
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
              <h3 class="text-lg font-medium text-gray-900">批量添加耐磨金纸映射</h3>
              <button @click="closeBatchAddDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveBatchAdd" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">耐磨金纸类型 *</label>
                  <select
                    v-model="batchAddForm.goldPaperType"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                  >
                    <option value="">请选择耐磨金纸类型</option>
                    <option v-for="type in availableGoldPaperTypes" :key="type" :value="type">
                      {{ type }}
                    </option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
                  <select
                    v-model="batchAddForm.compatibilityStatus"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                  >
                    <option value="V">适用</option>
                    <option value="X">不适用</option>
                    <option value="NA">不确定</option>
                    <option value="▷">需要打底处理</option>
                  </select>
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">选择产品系列 *</label>
                <div class="mb-2">
                  <input
                    v-model="batchAddSeriesSearchText"
                    type="text"
                    placeholder="搜索产品系列..."
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                  />
                </div>
                <select
                  @change="(e) => { const target = e.target as HTMLSelectElement; if (target.value) { onBatchAddSeriesChange(target.value); target.value = ''; } }"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                >
                  <option value="">请选择产品系列</option>
                  <option v-for="series in filteredBatchAddSeries" :key="series" :value="series">
                    {{ series }}
                  </option>
                </select>
              </div>

              <div v-if="batchAddForm.selectedSeries.length > 0">
                <label class="block text-sm font-medium text-gray-700 mb-2">已选择的产品系列</label>
                <div class="space-y-2">
                  <div v-for="series in batchAddForm.selectedSeries" :key="series" class="border rounded-md p-3">
                    <div class="flex items-center justify-between mb-2">
                      <span class="font-medium">{{ series }}</span>
                      <button
                        type="button"
                        @click="removeBatchAddSeries(series)"
                        class="text-red-600 hover:text-red-800"
                      >
                        移除
                      </button>
                    </div>
                    <div>
                      <label class="block text-sm text-gray-600 mb-1">选择产品型号（可选，留空表示系列级映射）</label>
                      <select
                        v-model="batchAddForm.selectedProducts[series]"
                        multiple
                        class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                        size="5"
                      >
                        <option v-for="model in batchAddSeriesProducts[series]" :key="model" :value="model">
                          {{ model }}
                        </option>
                      </select>
                      <p class="mt-1 text-xs text-gray-500">按住Ctrl/Cmd键可多选，不选表示系列级映射</p>
                    </div>
                  </div>
                </div>
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
                  class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-yellow-600 hover:bg-yellow-700"
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
              <h3 class="text-lg font-medium text-gray-900">批量修改状态</h3>
              <button @click="showBatchEditStatusDialog = false" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="batchUpdateStatus" class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容性状态 *</label>
                <select
                  v-model="batchStatusForm.compatibilityStatus"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                >
                  <option value="V">适用</option>
                  <option value="X">不适用</option>
                  <option value="NA">不确定</option>
                  <option value="▷">需要打底处理</option>
                </select>
              </div>

              <div class="flex justify-end space-x-3 pt-4">
                <button
                  type="button"
                  @click="showBatchEditStatusDialog = false"
                  class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  取消
                </button>
                <button
                  type="submit"
                  class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-yellow-600 hover:bg-yellow-700"
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
              <h3 class="text-lg font-medium text-gray-900">导入耐磨金纸映射</h3>
              <button @click="showImportDialog = false" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <div class="space-y-4">
              <div>
                <button
                  type="button"
                  @click="downloadImportTemplate"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                  </svg>
                  下载导入模板
                </button>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">选择Excel文件</label>
                <input
                  ref="importFileInput"
                  type="file"
                  accept=".xlsx,.xls"
                  @change="handleFileSelect"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
                <p v-if="selectedFile" class="mt-2 text-sm text-gray-600">已选择: {{ selectedFile.name }}</p>
              </div>

              <div v-if="importResult" class="p-4 rounded-md" :class="importResult.success ? 'bg-green-50 border border-green-200' : 'bg-red-50 border border-red-200'">
                <div class="font-medium" :class="importResult.success ? 'text-green-800' : 'text-red-800'">
                  {{ importResult.message }}
                </div>
                <div v-if="importResult.totalCount !== undefined" class="mt-2 text-sm" :class="importResult.success ? 'text-green-700' : 'text-red-700'">
                  <p>总计: {{ importResult.totalCount }} 条</p>
                  <p>成功: {{ importResult.successCount }} 条</p>
                  <p>失败: {{ importResult.failCount }} 条</p>
                </div>
                <div v-if="importResult.errors && importResult.errors.length > 0" class="mt-2">
                  <p class="font-medium text-red-800">错误详情:</p>
                  <ul class="list-disc list-inside text-sm text-red-700 max-h-40 overflow-y-auto">
                    <li v-for="(error, index) in importResult.errors" :key="index">{{ error }}</li>
                  </ul>
                </div>
              </div>

              <div class="flex justify-end space-x-3 pt-4">
                <button
                  type="button"
                  @click="showImportDialog = false"
                  class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  关闭
                </button>
                <button
                  type="button"
                  @click="importMappings"
                  :disabled="!selectedFile || importing"
                  class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-yellow-600 hover:bg-yellow-700 disabled:bg-gray-400 disabled:cursor-not-allowed"
                >
                  {{ importing ? '导入中...' : '导入' }}
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
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { hotStampingTypeOptionsApi, type HotStampingTypeOption } from '../../../api/modules/hotStampingTypeOptions'
import { wearResistantGoldPaperMappingApi, type WearResistantGoldPaperMapping } from '../../../api/modules/wearResistantGoldPaperMapping'
import { sharedProductApi } from '../../../api/modules/postProcessingLeduvglitter'

// 路由实例
const router = useRouter()

// 响应式数据
const wearResistantGoldPaperMappings = ref<WearResistantGoldPaperMapping[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showBatchAddDialog = ref(false)
const showBatchEditStatusDialog = ref(false)
const showImportDialog = ref(false)

// 批量操作相关
const selectedItems = ref<number[]>([])

// 批量添加表单
const batchAddForm = reactive({
  selectedSeries: [] as string[],
  selectedProducts: {} as Record<string, string[]>,
  goldPaperType: '',
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷'
})

// 批量添加时每个产品系列的产品列表
const batchAddSeriesProducts = ref<Record<string, string[]>>({})
const batchAddAvailableSeries = ref<string[]>([])
const batchAddSeriesSearchText = ref('')

// 批量修改状态表单
const batchStatusForm = reactive({
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷'
})

// 导入相关
const selectedFile = ref<File | null>(null)
const importFileInput = ref<HTMLInputElement | null>(null)
const importResult = ref<{ success: boolean; message: string; totalCount?: number; successCount?: number; failCount?: number; errors?: string[] } | null>(null)
const importing = ref(false)

// 分页相关数据
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)
const totalPages = ref(0)

// 搜索表单
const searchForm = reactive({
  productName: '',
  productModelNumber: '',
  goldPaperType: ''
})

// 映射表单
const mappingForm = reactive({
  id: null as number | null,
  productName: '',
  productModelNumber: '',
  hotStampingType: '燙金後擊凸', // 固定值
  goldPaperType: '',
  compatibilityStatus: 'NA' as 'V' | 'X' | 'NA' | '▷',
  remarks: ''
})

// 选项数据
const availableProducts = ref<string[]>([])
const availableProductModels = ref<string[]>([])
const availableGoldPaperTypes = ref<string[]>([])
const loadingProductModels = ref(false)
const loadingGoldPaperTypes = ref(false)

// 搜索选项数据
const searchProductOptions = ref<string[]>([])
const searchProductModelOptions = ref<string[]>([])
const searchGoldPaperTypeOptions = ref<string[]>([])

// 跳过耐磨映射配置
const allHotStampingPaperTypes = ref<string[]>([])
const skipPaperTypes = ref<string[]>([])
const savingSkipConfig = ref(false)

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

// 加载耐磨金纸映射列表
const loadWearResistantGoldPaperMappings = async () => {
  try {
    loading.value = true
    const response = await wearResistantGoldPaperMappingApi.getMappingsWithPagination(currentPage.value, pageSize.value)
    wearResistantGoldPaperMappings.value = response.content
    totalItems.value = response.totalElements
    totalPages.value = response.totalPages
  } catch (error: any) {
    console.error('加载耐磨金纸映射失败:', error)
    alert(error.message || '加载数据失败，请重试')
  } finally {
    loading.value = false
  }
}

// 搜索耐磨金纸映射
const searchWearResistantGoldPaperMappings = async () => {
  try {
    loading.value = true
    currentPage.value = 1 // 搜索时重置到第一页
    const results = await wearResistantGoldPaperMappingApi.searchMappings({
      productName: searchForm.productName || undefined,
      productModelNumber: searchForm.productModelNumber || undefined,
      goldPaperType: searchForm.goldPaperType || undefined
    })
    wearResistantGoldPaperMappings.value = results
    totalItems.value = results.length
    totalPages.value = Math.ceil(results.length / pageSize.value)
  } catch (error: any) {
    console.error('搜索耐磨金纸映射失败:', error)
    alert(error.message || '搜索失败，请重试')
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.productName = ''
  searchForm.productModelNumber = ''
  searchForm.goldPaperType = ''
  currentPage.value = 1
  loadWearResistantGoldPaperMappings()
}

// 编辑耐磨金纸映射
const editWearResistantGoldPaperMapping = async (mapping: WearResistantGoldPaperMapping) => {
  Object.assign(mappingForm, {
    id: mapping.id,
    productName: mapping.productName,
    productModelNumber: mapping.productModelNumber || '',
    hotStampingType: mapping.hotStampingType,
    goldPaperType: mapping.goldPaperType,
    compatibilityStatus: mapping.compatibilityStatus,
    remarks: mapping.remarks || ''
  })
  
  // 加载该产品系列的产品型号选项
  if (mapping.productName) {
    await onProductNameChange()
  }
  
  showEditDialog.value = true
}

// 删除耐磨金纸映射
const deleteWearResistantGoldPaperMapping = async (id: number) => {
  if (confirm('确定要删除这个耐磨金纸映射吗？')) {
    try {
      await wearResistantGoldPaperMappingApi.deleteMapping(id!)
      alert('删除成功')
      await loadWearResistantGoldPaperMappings()
    } catch (error: any) {
      console.error('删除耐磨金纸映射失败:', error)
      alert(error.message || '删除失败，请重试')
    }
  }
}

// 保存耐磨金纸映射
const saveWearResistantGoldPaperMapping = async () => {
  try {
    // 检查唯一性
    const uniqueCheck = await wearResistantGoldPaperMappingApi.checkUnique(
      mappingForm.productName,
      mappingForm.productModelNumber || undefined,
      mappingForm.goldPaperType
    )
    
    if (showAddDialog.value) {
      if (uniqueCheck.exists) {
        alert('该映射已存在，请勿重复添加')
        return
      }
      await wearResistantGoldPaperMappingApi.createMapping({
        productName: mappingForm.productName,
        productModelNumber: mappingForm.productModelNumber || undefined,
        goldPaperType: mappingForm.goldPaperType,
        compatibilityStatus: mappingForm.compatibilityStatus,
        remarks: mappingForm.remarks || undefined
      })
      alert('添加成功')
    } else {
      if (uniqueCheck.exists && uniqueCheck.data?.id !== mappingForm.id) {
        alert('该映射已存在，请修改唯一键字段')
        return
      }
      await wearResistantGoldPaperMappingApi.updateMapping(mappingForm.id!, {
        id: mappingForm.id!,
        productName: mappingForm.productName,
        productModelNumber: mappingForm.productModelNumber || undefined,
        goldPaperType: mappingForm.goldPaperType,
        compatibilityStatus: mappingForm.compatibilityStatus,
        remarks: mappingForm.remarks || undefined
      })
      alert('更新成功')
    }
    
    await loadWearResistantGoldPaperMappings()
    closeDialog()
  } catch (error: any) {
    console.error('保存耐磨金纸映射失败:', error)
    alert(error.message || '保存失败，请重试')
  }
}

// 复制映射
const copyWearResistantGoldPaperMapping = (mapping: WearResistantGoldPaperMapping) => {
  Object.assign(mappingForm, {
    id: null,
    productName: mapping.productName,
    productModelNumber: mapping.productModelNumber,
    hotStampingType: '燙金後擊凸',
    goldPaperType: mapping.goldPaperType,
    compatibilityStatus: mapping.compatibilityStatus,
    remarks: mapping.remarks || ''
  })
  showAddDialog.value = true
}

// 关闭对话框
const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(mappingForm, {
    id: null,
    productName: '',
    productModelNumber: '',
    hotStampingType: '燙金後擊凸',
    goldPaperType: '',
    compatibilityStatus: 'NA',
    remarks: ''
  })
  availableProductModels.value = []
}

// 映射配置相关方法 - 参考烫金类型管理页面的做法
const openMappingConfigDialog = async (mapping: WearResistantGoldPaperMapping) => {
  try {
    // 查找"燙金後擊凸"烫金类型ID
    const allTypes = await hotStampingTypeOptionsApi.getAllTypes()
    const targetType = allTypes.find(type => 
      type.stampingType === '燙金後擊凸' && 
      (type.positionType === null || type.positionType === undefined || type.positionType === '')
    )
    
    if (targetType) {
      // 跳转到耐磨金纸映射管理页面，传递烫金类型ID
      router.push({
        path: '/admin/material/smart-compatibility',
        query: { hotStampingTypeId: targetType.id }
      })
    } else {
      alert('未找到"燙金後擊凸"烫金类型')
    }
  } catch (error) {
    console.error('查找烫金类型失败:', error)
    alert('查找烫金类型失败，请重试')
  }
}

// 加载选项数据
const loadOptions = async () => {
  try {
    // 加载产品系列
    availableProducts.value = await sharedProductApi.getAllProductNames()
    searchProductOptions.value = availableProducts.value
    
    // 加载所有烫金纸类型（来自产品选项，用于跳过配置）
    const productOptions = await productApi.getProductOptions()
    allHotStampingPaperTypes.value = productOptions.hotStampingPaperTypes || []
    
    // 加载耐磨金纸类型
    loadingGoldPaperTypes.value = true
    availableGoldPaperTypes.value = await wearResistantGoldPaperMappingApi.getAllGoldPaperTypes()
    searchGoldPaperTypeOptions.value = availableGoldPaperTypes.value
    loadingGoldPaperTypes.value = false

    // 加载当前跳过配置
    skipPaperTypes.value = await wearResistantGoldPaperMappingApi.getSkipPaperTypes()
  } catch (error) {
    console.error('加载选项数据失败:', error)
    loadingGoldPaperTypes.value = false
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

// 当选择产品系列时，加载对应的产品型号
const onProductNameChange = async () => {
  if (!mappingForm.productName) {
    availableProductModels.value = []
    mappingForm.productModelNumber = ''
    return
  }
  
  try {
    loadingProductModels.value = true
    availableProductModels.value = await sharedProductApi.getProductModelNumbersByProductName(mappingForm.productName)
  } catch (error) {
    console.error('加载产品型号失败:', error)
    availableProductModels.value = []
  } finally {
    loadingProductModels.value = false
  }
}

// 当搜索表单中的产品系列改变时，加载对应的产品型号选项
const onSearchProductNameChange = async () => {
  if (!searchForm.productName) {
    searchProductModelOptions.value = []
    searchForm.productModelNumber = '' // 清空产品型号选择
    return
  }
  
  try {
    searchProductModelOptions.value = await sharedProductApi.getProductModelNumbersByProductName(searchForm.productName)
    // 如果当前选择的产品型号不在新列表中，清空选择
    if (searchForm.productModelNumber && !searchProductModelOptions.value.includes(searchForm.productModelNumber)) {
      searchForm.productModelNumber = ''
    }
  } catch (error) {
    console.error('加载搜索产品型号失败:', error)
    searchProductModelOptions.value = []
  }
}

// 监听页码变化
watch(currentPage, () => {
  loadWearResistantGoldPaperMappings()
})

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
  if (selectedItems.value.length === wearResistantGoldPaperMappings.value.length) {
    selectedItems.value = []
  } else {
    selectedItems.value = wearResistantGoldPaperMappings.value.map(m => m.id as number).filter(id => id !== undefined)
  }
}

const clearSelection = () => {
  selectedItems.value = []
}

// 批量删除
const batchDeleteMappings = async () => {
  if (selectedItems.value.length === 0) {
    alert('请选择要删除的记录')
    return
  }
  
  if (!confirm(`确定要删除选中的 ${selectedItems.value.length} 条记录吗？`)) {
    return
  }
  
  try {
    await wearResistantGoldPaperMappingApi.batchDeleteMappings(selectedItems.value)
    alert('批量删除成功')
    selectedItems.value = []
    await loadWearResistantGoldPaperMappings()
  } catch (error: any) {
    console.error('批量删除失败:', error)
    alert(error.message || '批量删除失败，请重试')
  }
}

// 打开批量修改状态对话框
const openBatchEditStatusDialog = () => {
  if (selectedItems.value.length === 0) {
    alert('请选择要修改的记录')
    return
  }
  batchStatusForm.compatibilityStatus = 'NA'
  showBatchEditStatusDialog.value = true
}

// 批量修改状态
const batchUpdateStatus = async () => {
  if (selectedItems.value.length === 0) {
    alert('请选择要修改的记录')
    return
  }
  
  try {
    await wearResistantGoldPaperMappingApi.batchUpdateStatus({
      ids: selectedItems.value,
      compatibilityStatus: batchStatusForm.compatibilityStatus
    })
    alert('批量修改状态成功')
    selectedItems.value = []
    showBatchEditStatusDialog.value = false
    await loadWearResistantGoldPaperMappings()
  } catch (error: any) {
    console.error('批量修改状态失败:', error)
    alert(error.message || '批量修改状态失败，请重试')
  }
}

// 批量添加相关
const openBatchAddDialog = async () => {
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddForm.goldPaperType = ''
  batchAddForm.compatibilityStatus = 'NA'
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = availableProducts.value
  batchAddSeriesProducts.value = {}
  showBatchAddDialog.value = true
}

const closeBatchAddDialog = () => {
  showBatchAddDialog.value = false
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddForm.goldPaperType = ''
  batchAddSeriesSearchText.value = ''
  batchAddSeriesProducts.value = {}
}

const onBatchAddSeriesChange = async (series: string) => {
  if (!batchAddForm.selectedSeries.includes(series)) {
    batchAddForm.selectedSeries.push(series)
    // 加载该系列的产品型号
    try {
      const models = await sharedProductApi.getProductModelNumbersByProductName(series)
      batchAddSeriesProducts.value[series] = models
      batchAddForm.selectedProducts[series] = []
    } catch (error) {
      console.error('加载产品型号失败:', error)
      batchAddSeriesProducts.value[series] = []
    }
  }
}

const removeBatchAddSeries = (series: string) => {
  const index = batchAddForm.selectedSeries.indexOf(series)
  if (index > -1) {
    batchAddForm.selectedSeries.splice(index, 1)
    delete batchAddForm.selectedProducts[series]
    delete batchAddSeriesProducts.value[series]
  }
}

const filteredBatchAddSeries = computed(() => {
  if (!batchAddSeriesSearchText.value) {
    return batchAddAvailableSeries.value.filter(s => !batchAddForm.selectedSeries.includes(s))
  }
  const searchText = batchAddSeriesSearchText.value.toLowerCase()
  return batchAddAvailableSeries.value.filter(s => 
    !batchAddForm.selectedSeries.includes(s) && s.toLowerCase().includes(searchText)
  )
})

const saveBatchAdd = async () => {
  if (batchAddForm.selectedSeries.length === 0) {
    alert('请至少选择一个产品系列')
    return
  }
  
  if (!batchAddForm.goldPaperType) {
    alert('请选择耐磨金纸类型')
    return
  }
  
  try {
    let successCount = 0
    let failCount = 0
    
    for (const series of batchAddForm.selectedSeries) {
      const models = batchAddForm.selectedProducts[series] || []
      
      if (models.length === 0) {
        // 系列级映射
        try {
          await wearResistantGoldPaperMappingApi.createMapping({
            productName: series,
            productModelNumber: undefined,
            goldPaperType: batchAddForm.goldPaperType,
            compatibilityStatus: batchAddForm.compatibilityStatus,
            remarks: undefined
          })
          successCount++
        } catch (error: any) {
          console.error(`批量添加失败 [${series}]:`, error)
          failCount++
        }
      } else {
        // 型号级映射
        for (const model of models) {
          try {
            await wearResistantGoldPaperMappingApi.createMapping({
              productName: series,
              productModelNumber: model,
              goldPaperType: batchAddForm.goldPaperType,
              compatibilityStatus: batchAddForm.compatibilityStatus,
              remarks: undefined
            })
            successCount++
          } catch (error: any) {
            console.error(`批量添加失败 [${series} - ${model}]:`, error)
            failCount++
          }
        }
      }
    }
    
    alert(`批量添加完成：成功 ${successCount} 条，失败 ${failCount} 条`)
    closeBatchAddDialog()
    await loadWearResistantGoldPaperMappings()
  } catch (error: any) {
    console.error('批量添加失败:', error)
    alert(error.message || '批量添加失败，请重试')
  }
}

// 导出功能
const exportMappings = async () => {
  try {
    const blob = await wearResistantGoldPaperMappingApi.exportMappings()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `耐磨金纸映射_${new Date().toISOString().slice(0, 10)}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    alert('导出成功')
  } catch (error: any) {
    console.error('导出失败:', error)
    alert(error.message || '导出失败，请重试')
  }
}

// 导入功能
const openImportDialog = () => {
  selectedFile.value = null
  importResult.value = null
  if (importFileInput.value) {
    importFileInput.value.value = ''
  }
  showImportDialog.value = true
}

const downloadImportTemplate = async () => {
  try {
    const blob = await wearResistantGoldPaperMappingApi.downloadImportTemplate()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '耐磨金纸映射导入模板.xlsx'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (error: any) {
    console.error('下载模板失败:', error)
    alert(error.message || '下载模板失败，请重试')
  }
}

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    selectedFile.value = target.files[0]
  }
}

const importMappings = async () => {
  if (!selectedFile.value) {
    alert('请选择要导入的文件')
    return
  }
  
  const fileName = selectedFile.value.name
  if (!fileName.endsWith('.xlsx') && !fileName.endsWith('.xls')) {
    alert('文件格式不正确，请上传Excel文件')
    return
  }
  
  try {
    importing.value = true
    const result = await wearResistantGoldPaperMappingApi.importMappings(selectedFile.value)
    importResult.value = result
    
    if (result.success) {
      await loadWearResistantGoldPaperMappings()
    }
  } catch (error: any) {
    console.error('导入失败:', error)
    importResult.value = {
      success: false,
      message: error.message || '导入失败，请重试'
    }
  } finally {
    importing.value = false
  }
}

// 组件挂载时加载数据
onMounted(async () => {
  await loadOptions()
  await loadWearResistantGoldPaperMappings()
})
</script>