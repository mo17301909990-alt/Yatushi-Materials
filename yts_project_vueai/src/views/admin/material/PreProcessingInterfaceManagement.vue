<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">前工序适用界面管理</h1>
            <p class="mt-2 text-gray-600">管理前工序适用界面的配置和选项</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="openAddDialog"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加前工序
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
            <button
              @click="openAllMappingsDialog"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
              </svg>
              查看所有配置映射
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

      <!-- 搜索和筛选 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">前工序类型</label>
            <select
              v-model="searchForm.preprocessingType"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">全部类型</option>
              <option v-for="type in preprocessingTypes" :key="type" :value="type">{{ type }}</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
            <select
              v-model="searchForm.status"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">全部状态</option>
              <option value="true">激活</option>
              <option value="false">停用</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">工艺类型</label>
            <select
              v-model="searchForm.process"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">全部工艺</option>
              <option v-for="process in filteredProcessTypes" :key="process" :value="process">{{ process }}</option>
            </select>
          </div>
          <div class="flex items-end">
            <button
              @click="searchOptions"
              class="w-full bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors"
            >
              搜索
            </button>
          </div>
        </div>
      </div>

      <!-- 前工序列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <h3 class="text-lg font-medium text-gray-900">前工序列表</h3>
            <div class="flex items-center space-x-2">
              <span class="text-sm text-gray-500">已选择 {{ selectedItems.length }} 项</span>
              <button
                @click="selectAll"
                class="text-sm text-green-600 hover:text-green-800"
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
                    class="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded"
                  />
                </th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">显示顺序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">前工序信息</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">工艺信息</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">创建时间</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="option in filteredOptions" :key="option.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap">
                  <input
                    type="checkbox"
                    :checked="selectedItems.includes(option.id)"
                    @change="toggleSelection(option.id)"
                    class="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded"
                  />
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="flex items-center space-x-2">
                    <input
                      v-model.number="option.displayOrder"
                      type="number"
                      min="1"
                      @blur="updateDisplayOrder(option)"
                      class="w-16 px-2 py-1 text-sm border border-gray-300 rounded focus:outline-none focus:ring-1 focus:ring-green-500"
                    />
                    <button
                      @click="moveUp(option)"
                      class="text-gray-400 hover:text-gray-600"
                      :disabled="option.displayOrder <= 1"
                    >
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 15l7-7 7 7"></path>
                      </svg>
                    </button>
                    <button
                      @click="moveDown(option)"
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
                    <div class="text-sm font-medium text-gray-900">{{ option.preProcessingStepsName }}</div>
                    <div class="text-sm text-gray-500">{{ option.description || '暂无描述' }}</div>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <div class="text-sm text-gray-900">{{ option.process || '未设置' }}</div>
                  <div class="text-sm text-gray-500">{{ option.steps || '未设置步骤' }}</div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                      option.isActive
                        ? 'bg-green-100 text-green-800'
                        : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ option.isActive ? '激活' : '停用' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ formatDate(option.createdAt) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button
                      @click="editOption(option)"
                      class="text-green-600 hover:text-green-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="configureMapping(option)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      配置映射
                    </button>
                    <button
                      @click="toggleStatus(option)"
                      :class="[
                        'hover:underline',
                        option.isActive ? 'text-red-600 hover:text-red-900' : 'text-green-600 hover:text-green-900'
                      ]"
                    >
                      {{ option.isActive ? '停用' : '激活' }}
                    </button>
                    <button
                      @click="deleteOption(option.id)"
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
                      ? 'z-10 bg-green-50 border-green-500 text-green-600'
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

    <!-- 添加/编辑前工序对话框 -->
    <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              {{ showAddDialog ? '添加前工序' : '编辑前工序' }}
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

          <form @submit.prevent="saveOption" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">显示顺序</label>
                <input
                  v-model.number="optionForm.displayOrder"
                  type="number"
                  min="1"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">工艺类型 *</label>
                <input
                  v-model="optionForm.process"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
                <select
                  v-model="optionForm.isActive"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option :value="true">激活</option>
                  <option :value="false">停用</option>
                </select>
              </div>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">前工序类型</label>
              <textarea
                v-model="optionForm.steps"
                rows="3"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                placeholder="描述前工序的具体步骤..."
              ></textarea>
            </div>
            
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">详细描述</label>
              <textarea
                v-model="optionForm.description"
                rows="3"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                placeholder="详细描述前工序的用途和特点..."
              ></textarea>
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

    <!-- 映射配置对话框 -->
    <div v-if="showMappingDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[55]">
      <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-6xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              配置映射 - {{ currentPreProcessingOption?.preProcessingStepsName }}
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

          <!-- 映射列表 -->
          <div class="mb-4">
            <div class="flex items-center justify-between mb-4">
              <h4 class="text-md font-medium text-gray-700">前工序步骤映射</h4>
              <div class="flex space-x-2">
              <button
                @click="addStep"
                class="inline-flex items-center px-3 py-2 border border-transparent text-sm leading-4 font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700"
              >
                <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                </svg>
                添加映射
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
                    @click="openBatchEditOrderDialog"
                    class="inline-flex items-center px-3 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-purple-600 hover:bg-purple-700"
                  >
                    <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
                    </svg>
                    批量修改顺序
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

            <!-- 映射列表搜索筛选 -->
            <div class="bg-gray-50 rounded-lg p-4 mb-4">
              <div class="grid grid-cols-1 md:grid-cols-5 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">步骤名称</label>
                  <input
                    v-model="mappingSearchForm.stepName"
                    type="text"
                    placeholder="搜索步骤名称"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品系列</label>
                  <input
                    v-model="mappingSearchForm.seriesName"
                    type="text"
                    placeholder="输入产品系列进行筛选"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
                  <input
                    v-model="mappingSearchForm.productModelNumber"
                    type="text"
                    placeholder="输入产品型号进行筛选"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
                  <select
                    v-model="mappingSearchForm.status"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">全部状态</option>
                    <option value="true">激活</option>
                    <option value="false">停用</option>
                  </select>
                </div>
                <div class="flex items-end">
                  <button
                    @click="searchMappingSteps"
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
                    <th class="px-6 py-3 text-left">
                      <input
                        type="checkbox"
                        :checked="isAllMappingSelected"
                        @change="toggleSelectAllMappings"
                        class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                    </th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">步骤名称</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品系列</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品型号</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">步骤顺序</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">适用状态</th>
                    <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                  </tr>
                </thead>
                <tbody class="bg-white divide-y divide-gray-200">
                  <tr v-for="step in filteredMappingSteps" :key="step.id" class="hover:bg-gray-50">
                    <td class="px-6 py-4 whitespace-nowrap">
                      <input
                        type="checkbox"
                        :checked="selectedMappingItems.includes(step.id)"
                        @change="toggleMappingSelection(step.id)"
                        class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                      />
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ step.stepName }}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ step.seriesName }}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {{ step.productModelNumber || (step.productId ? `产品ID: ${step.productId}` : '-') }}
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ step.paperType || '-' }}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ step.stepOrder }}</td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span
                        :class="[
                          'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                          step.isActive
                            ? 'bg-green-100 text-green-800'
                            : 'bg-red-100 text-red-800'
                        ]"
                      >
                        {{ step.isActive ? '激活' : '停用' }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap">
                      <span
                        :class="[
                          'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                          step.status === 'V'
                            ? 'bg-green-100 text-green-800'
                            : step.status === 'X'
                            ? 'bg-red-100 text-red-800'
                            : step.status === 'NA'
                            ? 'bg-yellow-100 text-yellow-800'
                            : step.status === '▷'
                            ? 'bg-blue-100 text-blue-800'
                            : 'bg-gray-100 text-gray-800'
                        ]"
                      >
                        {{ step.status === 'V' ? '适用' : step.status === 'X' ? '不适用' : step.status === 'NA' ? '不确定' : step.status === '▷' ? '需要打底处理' : step.status || '-' }}
                      </span>
                    </td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div class="flex space-x-2">
                        <button
                          @click="copyMapping(step)"
                          class="text-green-600 hover:text-green-900"
                          title="复制此映射"
                        >
                          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
                          </svg>
                        </button>
                        <button
                          @click="editStep(step)"
                          class="text-blue-600 hover:text-blue-900"
                          title="编辑"
                        >
                          编辑
                        </button>
                        <button
                          @click="deleteStep(step.id)"
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

            <!-- 映射列表分页 -->
            <div v-if="mappingTotalItems > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
              <div class="flex-1 flex justify-between sm:hidden">
                <button
                  @click="mappingCurrentPage = Math.max(1, mappingCurrentPage - 1)"
                  :disabled="mappingCurrentPage === 1"
                  class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
                >
                  上一页
                </button>
                <button
                  @click="mappingCurrentPage = Math.min(mappingTotalPages, mappingCurrentPage + 1)"
                  :disabled="mappingCurrentPage === mappingTotalPages"
                  class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 disabled:opacity-50"
                >
                  下一页
                </button>
              </div>
              <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
                <div>
                  <p class="text-sm text-gray-700">
                    显示第 <span class="font-medium">{{ (mappingCurrentPage - 1) * mappingPageSize + 1 }}</span> 到
                    <span class="font-medium">{{ Math.min(mappingCurrentPage * mappingPageSize, mappingTotalItems) }}</span> 条，
                    共 <span class="font-medium">{{ mappingTotalItems }}</span> 条记录
                  </p>
                </div>
                <div>
                  <nav class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px">
                    <button
                      @click="mappingCurrentPage = Math.max(1, mappingCurrentPage - 1)"
                      :disabled="mappingCurrentPage === 1"
                      class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50 disabled:opacity-50"
                    >
                      上一页
                    </button>
                    <button
                      v-for="page in mappingVisiblePages"
                      :key="page"
                      @click="mappingCurrentPage = page"
                      :class="[
                        'relative inline-flex items-center px-4 py-2 border text-sm font-medium',
                        page === mappingCurrentPage
                          ? 'z-10 bg-blue-50 border-blue-500 text-blue-600'
                          : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'
                      ]"
                    >
                      {{ page }}
                    </button>
                    <button
                      @click="mappingCurrentPage = Math.min(mappingTotalPages, mappingCurrentPage + 1)"
                      :disabled="mappingCurrentPage === mappingTotalPages"
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
              @click="closeMappingDialog"
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              关闭
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
                <label class="block text-sm font-medium text-gray-700 mb-2">前工序类型</label>
                <select
                  v-model="allMappingsSearchForm.stepName"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="">全部前工序</option>
                  <option v-for="option in options" :key="option.id" :value="option.preProcessingStepsName">
                    {{ option.preProcessingStepsName }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">产品系列</label>
                <input
                  v-model="allMappingsSearchForm.seriesName"
                  type="text"
                  placeholder="输入产品系列进行筛选"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
                <input
                  v-model="allMappingsSearchForm.productModelNumber"
                  type="text"
                  placeholder="输入产品型号进行筛选"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                <input
                  v-model="allMappingsSearchForm.paperType"
                  type="text"
                  placeholder="搜索烫金纸性能类型"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
                <select
                  v-model="allMappingsSearchForm.status"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="">全部状态</option>
                  <option value="true">激活</option>
                  <option value="false">停用</option>
                </select>
              </div>
            </div>
            <div class="mt-4 flex justify-end">
              <button
                @click="searchAllMappings"
                class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
              >
                搜索
              </button>
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
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">前工序类型</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品系列</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品型号</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">烫金纸性能类型</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">步骤顺序</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                  <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">适用状态</th>
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
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.stepName }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.seriesName }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {{ mapping.productModelNumber || (mapping.productId ? `产品ID: ${mapping.productId}` : '-') }}
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.paperType || '-' }}</td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ mapping.stepOrder }}</td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span
                      :class="[
                        'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                        mapping.isActive
                          ? 'bg-green-100 text-green-800'
                          : 'bg-red-100 text-red-800'
                      ]"
                    >
                      {{ mapping.isActive ? '激活' : '停用' }}
                    </span>
                  </td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <span
                      :class="[
                        'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                        mapping.status === 'V'
                          ? 'bg-green-100 text-green-800'
                          : mapping.status === 'X'
                          ? 'bg-red-100 text-red-800'
                          : mapping.status === 'NA'
                          ? 'bg-yellow-100 text-yellow-800'
                          : mapping.status === '▷'
                          ? 'bg-blue-100 text-blue-800'
                          : 'bg-gray-100 text-gray-800'
                      ]"
                    >
                      {{ mapping.status === 'V' ? '适用' : mapping.status === 'X' ? '不适用' : mapping.status === 'NA' ? '不确定' : mapping.status === '▷' ? '需要打底处理' : mapping.status || '-' }}
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
                  <td colspan="9" class="px-6 py-4 text-center text-sm text-gray-500">
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
                <label class="block text-sm font-medium text-gray-700 mb-2">前工序类型</label>
                <select
                  v-model="allMappingsBatchForm.stepName"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                >
                  <option value="">不修改</option>
                  <option v-for="option in options" :key="option.id" :value="option.preProcessingStepsName">
                    {{ option.preProcessingStepsName }}
                  </option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">产品系列</label>
                <input
                  v-model="allMappingsBatchForm.seriesName"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
                <input
                  v-model="allMappingsBatchForm.productModelNumber"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
                <p class="mt-1 text-xs text-gray-500">输入产品型号或产品名称</p>
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
                <label class="block text-sm font-medium text-gray-700 mb-2">步骤顺序</label>
                <input
                  v-model.number="allMappingsBatchForm.stepOrder"
                  type="number"
                  min="1"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
                <select
                  v-model="allMappingsBatchForm.isActive"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                >
                  <option :value="null">不修改</option>
                  <option :value="true">激活</option>
                  <option :value="false">停用</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容状态</label>
                <select
                  v-model="allMappingsBatchForm.status"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                >
                  <option value="">不修改</option>
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
                  <option value="N">不确定</option>
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

    <!-- 添加/编辑前工序步骤对话框 -->
    <div v-if="showAddStepDialog || showEditStepDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[60]">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              {{ showAddStepDialog ? '添加前工序步骤映射' : '编辑前工序步骤映射' }}
            </h3>
            <button
              @click="closeStepDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <form @submit.prevent="saveStep" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">步骤名称 *</label>
                <input
                  v-model="stepForm.stepName"
                  type="text"
                  required
                  readonly
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 bg-gray-100 cursor-not-allowed"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">产品系列 *</label>
                <div class="relative">
                  <input
                    type="text"
                    v-model="seriesSearchText"
                    @input="onSeriesSearchInput"
                    @focus="showSeriesDropdown = true"
                    @blur="handleSeriesInputBlur"
                    :placeholder="productSeries.length === 0 ? '加载中...' : '请输入或选择产品系列'"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                  <div
                    v-if="showSeriesDropdown && productSeries.length > 0"
                    class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto"
                  >
                    <div
                      v-for="series in filteredSeriesBySearch"
                      :key="series"
                      @mousedown.prevent="selectSeries(series)"
                      class="px-3 py-2 cursor-pointer hover:bg-blue-50"
                      :class="{ 'bg-blue-100': stepForm.seriesName === series }"
                    >
                      <div class="text-sm text-gray-900">{{ series }}</div>
                    </div>
                    <div v-if="filteredSeriesBySearch.length === 0" class="px-3 py-2 text-sm text-gray-500">
                      未找到匹配的产品系列
                    </div>
                  </div>
                </div>
                <p v-if="stepForm.seriesName" class="mt-1 text-xs text-gray-500">
                  已选择：{{ stepForm.seriesName }}
                </p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">产品型号</label>
                <div class="relative">
                  <input
                    type="text"
                    v-model="productSearchText"
                    @input="onProductSearchInput"
                    @focus="showProductDropdown = true"
                    @blur="handleProductInputBlur"
                  :disabled="loadingProducts || !stepForm.seriesName || filteredProducts.length === 0"
                    :placeholder="loadingProducts ? '加载中...' : !stepForm.seriesName ? '请先选择产品系列' : filteredProducts.length === 0 ? '该系列暂无产品型号' : '请输入或选择产品型号'"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-gray-100 disabled:cursor-not-allowed"
                  />
                  <div
                    v-if="showProductDropdown && filteredProducts.length > 0"
                    class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto"
                  >
                    <div
                      v-for="product in filteredProductsBySearch"
                      :key="product.id"
                      @mousedown.prevent="selectProduct(product)"
                      class="px-3 py-2 cursor-pointer hover:bg-blue-50"
                      :class="{ 'bg-blue-100': stepForm.productId === product.id }"
                    >
                      <div class="text-sm text-gray-900">
                    {{ product.modelNumber || product.name || `产品${product.id}` }}
                      </div>
                      <div v-if="product.name && product.modelNumber" class="text-xs text-gray-500">
                        {{ product.name }}
                      </div>
                    </div>
                    <div v-if="filteredProductsBySearch.length === 0" class="px-3 py-2 text-sm text-gray-500">
                      未找到匹配的产品型号
                    </div>
                  </div>
                </div>
                <p v-if="stepForm.productId" class="mt-1 text-xs text-gray-500">
                  已选择：{{ getSelectedProductDisplayName() }}
                </p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                <div class="relative">
                  <input
                    type="text"
                    v-model="stepForm.paperType"
                    :placeholder="loadingPaperTypes ? '加载中...' : '请输入或选择烫金纸性能类型'"
                    @focus="showPaperTypeDropdown = true"
                    @blur="handlePaperTypeInputBlur"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    list="paperTypeOptions"
                  />
                  <datalist id="paperTypeOptions">
                    <option v-for="paperType in paperTypes" :key="paperType" :value="paperType">
                      {{ paperType }}
                    </option>
                  </datalist>
                  <div
                    v-if="showPaperTypeDropdown && paperTypes.length > 0"
                    class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-auto"
                  >
                    <div
                      v-for="paperType in paperTypes"
                      :key="paperType"
                      @mousedown.prevent="selectPaperType(paperType)"
                      class="px-3 py-2 cursor-pointer hover:bg-blue-50"
                      :class="{ 'bg-blue-100': stepForm.paperType === paperType }"
                    >
                      <div class="text-sm text-gray-900">{{ paperType }}</div>
                    </div>
                  </div>
                </div>
                <p v-if="stepForm.paperType" class="mt-1 text-xs text-gray-500">
                  已输入：{{ stepForm.paperType }}
                </p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">步骤顺序</label>
                <input
                  v-model.number="stepForm.stepOrder"
                  type="number"
                  min="1"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">激活状态</label>
                <select
                  v-model="stepForm.isActive"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option :value="true">激活</option>
                  <option :value="false">停用</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容状态</label>
                <select
                  v-model="stepForm.status"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
                </select>
              </div>
            </div>
            

            <div class="flex justify-end space-x-3 pt-4">
              <button
                type="button"
                @click="closeStepDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
              <button
                type="submit"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700"
              >
                {{ showAddStepDialog ? '添加' : '保存' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 批量修改状态对话框 -->
    <div v-if="showBatchEditStatusDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[60]">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-md shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">批量修改状态</h3>
            <button
              @click="closeBatchEditStatusDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <div class="mb-4">
            <p class="text-sm text-gray-600 mb-4">
              将为选中的 <span class="font-bold text-blue-600">{{ selectedMappingItems.length }}</span> 个映射修改状态
            </p>
            <label class="block text-sm font-medium text-gray-700 mb-2">新状态 *</label>
            <select
              v-model="batchStatusForm.isActive"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
            >
              <option :value="true">激活</option>
              <option :value="false">停用</option>
            </select>
          </div>

          <div class="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              @click="closeBatchEditStatusDialog"
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              取消
            </button>
            <button
              @click="batchUpdateStatus"
              class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-yellow-600 hover:bg-yellow-700"
            >
              确认修改
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 批量修改步骤顺序对话框 -->
    <div v-if="showBatchEditOrderDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[60]">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-md shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">批量修改步骤顺序</h3>
            <button
              @click="closeBatchEditOrderDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <div class="mb-4">
            <p class="text-sm text-gray-600 mb-4">
              将为选中的 <span class="font-bold text-blue-600">{{ selectedMappingItems.length }}</span> 个映射修改步骤顺序
            </p>
            <label class="block text-sm font-medium text-gray-700 mb-2">新步骤顺序 *</label>
            <input
              v-model.number="batchOrderForm.stepOrder"
              type="number"
              min="1"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
              placeholder="请输入步骤顺序"
            />
            <p class="mt-2 text-xs text-gray-500">所有选中的映射将被设置为相同的步骤顺序</p>
          </div>

          <div class="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              @click="closeBatchEditOrderDialog"
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              取消
            </button>
            <button
              @click="batchUpdateOrder"
              class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-purple-600 hover:bg-purple-700"
            >
              确认修改
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 批量添加映射对话框 -->
    <div v-if="showBatchAddDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[60]">
      <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-3xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">批量添加映射</h3>
            <button
              @click="closeBatchAddDialog"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <form @submit.prevent="batchAddMappings" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">步骤名称 *</label>
                <input
                  :value="currentPreProcessingOption?.preProcessingStepsName || batchAddForm.stepName"
                  type="text"
                  required
                  readonly
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-gray-100 cursor-not-allowed"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">步骤顺序 *</label>
                <input
                  v-model.number="batchAddForm.stepOrder"
                  type="number"
                  min="1"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型 *</label>
                <select
                  v-model="batchAddForm.paperType"
                  @change="onBatchAddPaperTypeChange"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="">请先选择烫金纸性能类型</option>
                  <option v-for="type in allPaperTypes" :key="type" :value="type">{{ type }}</option>
                </select>
                <p class="mt-1 text-xs text-gray-500">请先选择烫金纸性能类型，系统将只显示支持该类型的产品系列</p>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">激活状态 *</label>
                <select
                  v-model="batchAddForm.isActive"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option :value="true">激活</option>
                  <option :value="false">停用</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">兼容状态 *</label>
                <select
                  v-model="batchAddForm.status"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
                </select>
              </div>
            </div>

            <div>
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
                      @change="onBatchAddSeriesChange(series)"
                      class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                    />
                    <span class="text-sm text-gray-700">{{ series }}</span>
                  </label>
                  <div v-if="filteredBatchAddSeries.length === 0" class="px-2 py-4 text-sm text-gray-500 text-center">
                    未找到匹配的产品系列
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

            <!-- 为每个选中的产品系列显示产品选择 -->
            <div v-for="seriesName in batchAddForm.selectedSeries" :key="seriesName" class="mt-4">
              <label class="block text-sm font-medium text-gray-700 mb-2">
                产品系列「{{ seriesName }}」的产品选择（可选）
              </label>
              <p class="mb-2 text-xs text-gray-500">
                如果不选择任何产品，则表示该产品系列全部适用
              </p>
              <div 
                v-if="batchAddSeriesProducts[seriesName] && batchAddSeriesProducts[seriesName].length > 0"
                class="border border-gray-300 rounded-md p-3 max-h-48 overflow-y-auto"
              >
                <div class="space-y-2">
                  <label
                    v-for="product in batchAddSeriesProducts[seriesName]"
                    :key="product.id"
                    class="flex items-center space-x-2 cursor-pointer hover:bg-gray-50 p-2 rounded"
                  >
                    <input
                      type="checkbox"
                      :value="product.id"
                      :checked="(batchAddForm.selectedProducts[seriesName] || []).includes(product.id)"
                      @change="(e) => {
                        if (!batchAddForm.selectedProducts[seriesName]) {
                          batchAddForm.selectedProducts[seriesName] = []
                        }
                        if (e.target.checked) {
                          if (!batchAddForm.selectedProducts[seriesName].includes(product.id)) {
                            batchAddForm.selectedProducts[seriesName].push(product.id)
                          }
                        } else {
                          const index = batchAddForm.selectedProducts[seriesName].indexOf(product.id)
                          if (index > -1) {
                            batchAddForm.selectedProducts[seriesName].splice(index, 1)
                          }
                        }
                      }"
                      class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                    />
                    <span class="text-sm text-gray-700">
                      {{ product.modelNumber || product.name || `产品ID: ${product.id}` }}
                    </span>
                  </label>
                </div>
                <p class="mt-2 text-xs text-gray-500">
                  已选择 <span class="font-bold text-indigo-600">
                    {{ (batchAddForm.selectedProducts[seriesName] || []).length }}
                  </span> 个产品
                  <span v-if="(batchAddForm.selectedProducts[seriesName] || []).length === 0" class="text-yellow-600">
                    （将创建该产品系列的通用映射）
                  </span>
                </p>
              </div>
              <div v-else class="border border-gray-300 rounded-md p-3 bg-gray-50">
                <p class="text-sm text-gray-500 text-center">
                  {{ batchAddSeriesProducts[seriesName] === undefined ? '加载中...' : '该产品系列下暂无产品' }}
                </p>
              </div>
            </div>

            <div class="bg-yellow-50 border border-yellow-200 rounded-md p-3">
              <p class="text-sm text-yellow-800">
                <strong>提示：</strong>将为选中的 {{ batchAddForm.selectedSeries.length }} 个产品系列批量创建映射配置。
                每个产品系列将使用相同的配置参数，但产品型号需要单独选择（如果产品系列下有多个型号）。
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
                :disabled="batchAddForm.selectedSeries.length === 0"
                class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                批量添加
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 导入映射对话框 -->
    <div v-if="showImportDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-[60]">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">导入映射配置</h3>
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
              <label class="block text-sm font-medium text-gray-700 mb-2">选择Excel文件 *</label>
              <input
                type="file"
                ref="importFileInput"
                @change="handleFileSelect"
                accept=".xlsx,.xls"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
              />
              <p class="mt-2 text-xs text-gray-500">
                支持 .xlsx 和 .xls 格式，请下载模板文件查看格式要求
              </p>
            </div>

            <div class="flex items-center space-x-4">
              <button
                @click="downloadImportTemplate"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                <svg class="w-4 h-4 inline mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                </svg>
                下载模板
              </button>
            </div>

            <div v-if="importResult" class="mt-4 p-4 rounded-md" :class="importResult.success ? 'bg-green-50 border border-green-200' : 'bg-red-50 border border-red-200'">
              <p class="text-sm font-medium" :class="importResult.success ? 'text-green-800' : 'text-red-800'">
                {{ importResult.message }}
              </p>
              <div v-if="importResult.details" class="mt-2 text-xs" :class="importResult.success ? 'text-green-700' : 'text-red-700'">
                <p v-for="(detail, index) in importResult.details" :key="index">{{ detail }}</p>
              </div>
            </div>

            <div class="flex justify-end space-x-3 pt-4">
              <button
                type="button"
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
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue'

// 定义接口
interface PreProcessingOption {
  id: number
  preProcessingStepsName: string
  displayOrder: number
  isActive: boolean
  description: string
  steps: string
  process: string
  createdAt: string
  updatedAt: string
}

// 响应式数据
const options = ref<PreProcessingOption[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showMappingDialog = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)
const selectedItems = ref<number[]>([])

// 映射配置相关数据
const currentPreProcessingOption = ref<any>(null)
const preProcessingSteps = ref<any[]>([])
const showAddStepDialog = ref(false)
const showEditStepDialog = ref(false)

// 批量操作相关数据
const selectedMappingItems = ref<number[]>([])
const showBatchEditStatusDialog = ref(false)
const showBatchEditOrderDialog = ref(false)
const batchStatusForm = reactive({
  isActive: true
})
const batchOrderForm = reactive({
  stepOrder: 1
})

// 批量添加相关数据
const showBatchAddDialog = ref(false)
const batchAddForm = reactive({
  stepName: '',
  selectedSeries: [] as string[],
  selectedProducts: {} as Record<string, number[]>, // 每个产品系列对应的产品ID列表
  stepOrder: 1,
  paperType: '',
  isActive: true,
  status: 'V'
})

// 批量添加时每个产品系列的产品列表
const batchAddSeriesProducts = ref<Record<string, Array<{ id: number, modelNumber: string, name: string }>>>({})

// 导入/导出相关数据
const showImportDialog = ref(false)
const selectedFile = ref<File | null>(null)
const importFileInput = ref<HTMLInputElement | null>(null)
const importResult = ref<{ success: boolean; message: string; details?: string[] } | null>(null)
const allPaperTypes = ref<string[]>([])

// 映射列表分页相关数据
const mappingCurrentPage = ref(1)
const mappingPageSize = ref(10)
const mappingTotalItems = ref(0)

// 产品相关数据
const productSeries = ref<string[]>([])
const paperTypes = ref<string[]>([])
const loadingPaperTypes = ref(false)
const productInfoMap = ref<Map<number, { modelNumber: string, name: string }>>(new Map())
const allProducts = ref<Array<{ id: number, modelNumber: string, name: string, seriesName: string }>>([])
const filteredProducts = ref<Array<{ id: number, modelNumber: string, name: string, seriesName: string }>>([])
const searchFilteredProducts = ref<Array<{ id: number, modelNumber: string, name: string, seriesName: string }>>([])
const loadingProducts = ref(false)

// 产品型号搜索相关
const productSearchText = ref('')
const showProductDropdown = ref(false)

// 产品系列搜索相关
const seriesSearchText = ref('')
const showSeriesDropdown = ref(false)

// 烫金纸性能类型搜索相关
const showPaperTypeDropdown = ref(false)

// 批量添加对话框中的产品系列搜索
const batchAddSeriesSearchText = ref('')

// 前工序类型选项
const preprocessingTypes = ref<string[]>([])
// 工艺类型选项
const processTypes = ref<string[]>([])
// 原始数据存储，用于联动查询
const allPreprocessingData = ref<any[]>([])

// 搜索表单
const searchForm = reactive({
  preprocessingType: '',
  status: '',
  process: ''
})

// 映射搜索表单
const mappingSearchForm = reactive({
  stepName: '',
  seriesName: '',
  productModelNumber: '',
  status: ''
})

// 查看所有配置映射相关数据
const showAllMappingsDialog = ref(false)
const allMappings = ref<any[]>([])
const allMappingsCurrentPage = ref(1)
const allMappingsPageSize = ref(20)
const allMappingsTotalItems = ref(0)
const selectedAllMappingsItems = ref<number[]>([])
const showAllMappingsBatchEditDialog = ref(false)
const allMappingsBatchForm = reactive({
  stepName: '',
  seriesName: '',
  productModelNumber: '',
  paperType: '',
  stepOrder: null as number | null,
  isActive: null as boolean | null,
  status: ''
})
const allMappingsSearchForm = reactive({
  stepName: '',
  seriesName: '',
  productModelNumber: '',
  paperType: '',
  status: ''
})

// 前工序表单
const optionForm = reactive({
  id: null,
  preProcessingStepsName: '',
  displayOrder: null,
  isActive: true,
  description: '',
  steps: '',
  process: ''
})

// 前工序步骤表单
const stepForm = reactive({
  id: null,
  stepName: '',
  seriesName: '',
  productId: null,
  stepOrder: 1,
  isActive: true,
  status: '',
  clothPaperId: null,
  paperType: '',
  stepId: null
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

// 根据前工序类型筛选的工艺类型选项
const filteredProcessTypes = computed(() => {
  if (!searchForm.preprocessingType) {
    return processTypes.value
  }
  
  // 从原始数据中筛选出属于该前工序类型的工艺类型
  const filteredData = allPreprocessingData.value.filter(item => 
    item.steps === searchForm.preprocessingType
  )
  
  // 提取唯一的工艺类型
  const uniqueProcessTypes = [...new Set(filteredData.map(item => item.process).filter(Boolean))]
  
  console.log(`前工序类型 "${searchForm.preprocessingType}" 对应的工艺类型:`, uniqueProcessTypes)
  return uniqueProcessTypes
})

const filteredOptions = computed(() => {
  let filtered = options.value

  if (searchForm.preprocessingType) {
    filtered = filtered.filter(option => 
      option.steps === searchForm.preprocessingType
    )
  }

  if (searchForm.status !== '') {
    const isActive = searchForm.status === 'true'
    filtered = filtered.filter(option => option.isActive === isActive)
  }

  if (searchForm.process) {
    filtered = filtered.filter(option => 
      option.process && option.process === searchForm.process
    )
  }

  // 按显示顺序排序
  filtered.sort((a, b) => (a.displayOrder || 0) - (b.displayOrder || 0))

  // 分页
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  totalItems.value = filtered.length
  return filtered.slice(start, end)
})

const isAllSelected = computed(() => {
  return filteredOptions.value.length > 0 && 
         filteredOptions.value.every(option => selectedItems.value.includes(option.id))
})

// 批量选择映射相关计算属性
const isAllMappingSelected = computed(() => {
  return filteredMappingSteps.value.length > 0 && 
         filteredMappingSteps.value.every(step => selectedMappingItems.value.includes(step.id))
})

// 映射列表分页计算属性
const filteredMappingSteps = computed(() => {
  let filtered = preProcessingSteps.value

  // 步骤名称筛选
  if (mappingSearchForm.stepName) {
    const stepName = mappingSearchForm.stepName.toLowerCase()
    filtered = filtered.filter(step => 
      step.stepName && step.stepName.toLowerCase().includes(stepName)
    )
  }

  // 产品系列筛选（支持模糊匹配）
  if (mappingSearchForm.seriesName) {
    const seriesName = mappingSearchForm.seriesName.toLowerCase()
    filtered = filtered.filter(step => 
      step.seriesName && step.seriesName.toLowerCase().includes(seriesName)
    )
  }

  // 产品型号筛选（支持模糊匹配）
  if (mappingSearchForm.productModelNumber) {
    const productModelNumber = mappingSearchForm.productModelNumber.toLowerCase()
    filtered = filtered.filter(step => {
      const productInfo = productInfoMap.value.get(step.productId)
      if (!productInfo) return false
      const modelNumber = (productInfo.modelNumber || '').toLowerCase()
      const productName = (productInfo.name || '').toLowerCase()
      const stepModelNumber = (step.productModelNumber || '').toLowerCase()
      return modelNumber.includes(productModelNumber) || 
             productName.includes(productModelNumber) ||
             stepModelNumber.includes(productModelNumber)
    })
  }

  // 状态筛选
  if (mappingSearchForm.status !== '') {
    const isActive = mappingSearchForm.status === 'true'
    filtered = filtered.filter(step => step.isActive === isActive)
  }

  // 按步骤顺序排序
  filtered.sort((a, b) => (a.stepOrder || 0) - (b.stepOrder || 0))

  // 分页
  const start = (mappingCurrentPage.value - 1) * mappingPageSize.value
  const end = start + mappingPageSize.value
  mappingTotalItems.value = filtered.length
  return filtered.slice(start, end)
})

const mappingTotalPages = computed(() => Math.ceil(mappingTotalItems.value / mappingPageSize.value))

const mappingVisiblePages = computed(() => {
  const pages = []
  const start = Math.max(1, mappingCurrentPage.value - 2)
  const end = Math.min(mappingTotalPages.value, start + 4)
  
  for (let i = start; i <= end; i++) {
    pages.push(i)
  }
  return pages
})

// 查看所有配置映射的计算属性
const filteredAllMappings = computed(() => {
  let filtered = allMappings.value

  // 前工序类型筛选
  if (allMappingsSearchForm.stepName) {
    filtered = filtered.filter(mapping => 
      mapping.stepName && mapping.stepName === allMappingsSearchForm.stepName
    )
  }

  // 产品系列筛选（支持模糊匹配）
  if (allMappingsSearchForm.seriesName) {
    const seriesName = allMappingsSearchForm.seriesName.toLowerCase()
    filtered = filtered.filter(mapping => 
      mapping.seriesName && mapping.seriesName.toLowerCase().includes(seriesName)
    )
  }

  // 产品型号筛选（支持模糊匹配）
  if (allMappingsSearchForm.productModelNumber) {
    const productModelNumber = allMappingsSearchForm.productModelNumber.toLowerCase()
    filtered = filtered.filter(mapping => {
      const modelNumber = (mapping.productModelNumber || '').toLowerCase()
      const productName = (mapping.productName || '').toLowerCase()
      return modelNumber.includes(productModelNumber) || 
             productName.includes(productModelNumber)
    })
  }

  // 烫金纸性能类型筛选
  if (allMappingsSearchForm.paperType) {
    const paperType = allMappingsSearchForm.paperType.toLowerCase()
    filtered = filtered.filter(mapping => 
      mapping.paperType && mapping.paperType.toLowerCase().includes(paperType)
    )
  }

  // 状态筛选
  if (allMappingsSearchForm.status !== '') {
    const isActive = allMappingsSearchForm.status === 'true'
    filtered = filtered.filter(mapping => mapping.isActive === isActive)
  }

  // 按前工序类型和步骤顺序排序
  filtered.sort((a, b) => {
    if (a.stepName !== b.stepName) {
      return (a.stepName || '').localeCompare(b.stepName || '')
    }
    return (a.stepOrder || 0) - (b.stepOrder || 0)
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

// 方法
const loadOptions = async () => {
  loading.value = true
  try {
    const response = await fetch('/api/api/pre-processing-steps/options/all')
    if (response.ok) {
      options.value = await response.json()
    } else {
      console.error('加载前工序选项失败')
    }
  } catch (error) {
    console.error('加载前工序选项失败:', error)
  } finally {
    loading.value = false
  }
}

const searchOptions = () => {
  currentPage.value = 1
  clearSelection()
}

const editOption = (option: any) => {
  console.log('编辑选项数据:', option)
  Object.assign(optionForm, option)
  // 将preProcessingStepsName的值赋给process字段用于编辑
  optionForm.process = option.preProcessingStepsName || option.process
  console.log('编辑表单数据:', optionForm)
  showEditDialog.value = true
}

const configureMapping = (option: any) => {
  currentPreProcessingOption.value = option
  showMappingDialog.value = true
  loadPreProcessingSteps(option.id)
}

const deleteOption = async (id: number) => {
  if (confirm('确定要删除这个前工序选项吗？')) {
    try {
      const response = await fetch(`/api/api/pre-processing-steps/options/${id}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        await loadOptions()
      } else {
        console.error('删除失败')
      }
    } catch (error) {
      console.error('删除前工序选项失败:', error)
    }
  }
}

const toggleStatus = async (option: any) => {
  try {
    const response = await fetch(`/api/api/pre-processing-steps/options/${option.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...option,
        isActive: !option.isActive
      })
    })
    if (response.ok) {
      await loadOptions()
    } else {
      console.error('更新状态失败')
    }
  } catch (error) {
    console.error('更新状态失败:', error)
  }
}

const updateDisplayOrder = async (option: any) => {
  try {
    const response = await fetch(`/api/api/pre-processing-steps/options/${option.id}/order`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        displayOrder: option.displayOrder
      })
    })
    if (response.ok) {
      await loadOptions()
    } else {
      console.error('更新显示顺序失败')
    }
  } catch (error) {
    console.error('更新显示顺序失败:', error)
  }
}

const moveUp = async (option: any) => {
  if (option.displayOrder > 1) {
    option.displayOrder--
    await updateDisplayOrder(option)
  }
}

const moveDown = async (option: any) => {
  option.displayOrder++
  await updateDisplayOrder(option)
}

const saveOption = async () => {
  try {
    const url = showAddDialog.value 
      ? '/api/api/pre-processing-steps/options'
      : `/api/api/pre-processing-steps/options/${optionForm.id}`
    
    const method = showAddDialog.value ? 'POST' : 'PUT'
    
    // 准备保存的数据，将process字段的值赋给preProcessingStepsName
    const saveData = {
      ...optionForm,
      preProcessingStepsName: optionForm.process || optionForm.preProcessingStepsName
    }
    console.log('保存数据:', saveData)
    
    const response = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(saveData)
    })
    
    if (response.ok) {
      const result = await response.json()
      closeDialog()
      await loadOptions()
      alert('保存成功')
    } else {
      // 尝试解析错误信息
      try {
        const errorData = await response.json()
        const errorMessage = errorData.message || errorData.error || '保存失败，请重试'
        alert(errorMessage)
      } catch (parseError) {
        // 如果无法解析JSON，显示默认错误信息
        alert('保存失败，请检查输入数据是否正确')
      }
    }
  } catch (error) {
    console.error('保存前工序选项失败:', error)
    alert('保存失败: ' + (error instanceof Error ? error.message : '未知错误'))
  }
}

const openAddDialog = () => {
  // 重置表单数据
  Object.assign(optionForm, {
    id: null,
    preProcessingStepsName: '',
    displayOrder: null,
    isActive: true,
    description: '',
    steps: '',
    process: ''
  })
  showAddDialog.value = true
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(optionForm, {
    id: null,
    preProcessingStepsName: '',
    displayOrder: null,
    isActive: true,
    description: '',
    steps: '',
    process: ''
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
  selectedItems.value = filteredOptions.value.map(option => option.id)
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
  
  const activeCount = options.value
    .filter(option => selectedItems.value.includes(option.id))
    .filter(option => option.isActive).length
  
  const newStatus = activeCount === selectedItems.value.length ? false : true
  
  try {
    const response = await fetch('/api/api/pre-processing-steps/options/batch-status', {
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
      await loadOptions()
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
const loadPreProcessingSteps = async (stepId: number) => {
  try {
    const response = await fetch(`/api/api/pre-processing-steps/options/${stepId}/steps`)
    if (response.ok) {
      preProcessingSteps.value = await response.json()
      // 后端已经返回了产品信息（productModelNumber），不需要额外加载
      // 但为了兼容性，仍然可以缓存到 productInfoMap 中
      preProcessingSteps.value.forEach((step: any) => {
        if (step.productId && step.productModelNumber) {
          productInfoMap.value.set(step.productId, {
            modelNumber: step.productModelNumber || '',
            name: step.seriesName || '' // 使用 seriesName 替代 productName
          })
        }
      })
      // 重置映射列表分页状态
      mappingCurrentPage.value = 1
      // 清空搜索表单
      Object.assign(mappingSearchForm, {
        stepName: '',
        seriesName: '',
        productModelNumber: '',
        status: ''
      })
      // 清空搜索筛选的产品列表
      searchFilteredProducts.value = []
      // 清空批量选择
      selectedMappingItems.value = []
    } else {
      console.error('加载前工序步骤失败')
    }
  } catch (error) {
    console.error('加载前工序步骤失败:', error)
  }
}

const addStep = async () => {
  Object.assign(stepForm, {
    id: null,
    stepName: currentPreProcessingOption.value.preProcessingStepsName, // 使用当前选中的步骤名称
    seriesName: '',
    productId: null,
    stepOrder: preProcessingSteps.value.length + 1,
    isActive: true,
    status: 'V',  // 默认设置为兼容状态
    clothPaperId: null,
    paperType: '',
    stepId: currentPreProcessingOption.value.id
  })
  // 清空烫金纸性能类型选项
  paperTypes.value = []
  // 加载产品列表
  await loadProductList()
  showAddStepDialog.value = true
}

const editStep = async (step: any) => {
  console.log('编辑步骤数据:', step)
  
  // 先清空表单，然后赋值
  Object.assign(stepForm, {
    id: null,
    stepName: '',
    seriesName: '',
    productId: null,
    stepOrder: 1,
    isActive: true,
    status: 'V',  // 默认设置为兼容状态
    clothPaperId: null,
    paperType: '',
    stepId: null
  })
  
  // 然后赋值编辑的数据
  Object.assign(stepForm, step)
  console.log('表单数据赋值后:', stepForm)
  
  // 加载产品列表
  await loadProductList()
  
  // 如果已有产品系列，筛选对应的产品型号并加载烫金纸性能类型
  if (step.seriesName) {
    // 设置产品系列搜索文本
    seriesSearchText.value = step.seriesName
    
    // 筛选产品型号
    filterProductsBySeries(step.seriesName)
    
    // 设置产品搜索文本
    if (step.productId) {
      const product = allProducts.value.find(p => p.id === step.productId)
      if (product) {
        productSearchText.value = product.modelNumber || product.name || `产品${product.id}`
      }
    }
    
    const currentPaperType = step.paperType || ''
    console.log('当前烫金纸性能类型:', currentPaperType)
    await loadPaperTypes(step.seriesName, currentPaperType)
    console.log('烫金纸性能类型加载后，表单烫金纸性能类型:', stepForm.paperType)
  } else {
    paperTypes.value = []
    filteredProducts.value = []
    productSearchText.value = ''
    seriesSearchText.value = ''
  }
  showEditStepDialog.value = true
}

const deleteStep = async (id: number) => {
  if (confirm('确定要删除这个映射关系吗？')) {
    try {
      const response = await fetch(`/api/api/pre-processing-steps/steps/${id}`, {
        method: 'DELETE'
      })
      if (response.ok) {
        await loadPreProcessingSteps(currentPreProcessingOption.value.id)
      } else {
        console.error('删除前工序步骤失败')
      }
    } catch (error) {
      console.error('删除前工序步骤失败:', error)
    }
  }
}

const saveStep = async () => {
  try {
    const url = stepForm.id 
      ? `/api/api/pre-processing-steps/steps/${stepForm.id}`
      : '/api/api/pre-processing-steps/steps'
    
    const method = stepForm.id ? 'PUT' : 'POST'
    
    const response = await fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(stepForm)
    })
    
    if (response.ok) {
      closeStepDialog()
      await loadPreProcessingSteps(currentPreProcessingOption.value.id)
      alert('保存成功')
    } else {
      // 尝试解析错误消息
      try {
        const errorData = await response.json()
        const errorMessage = errorData.message || '保存失败'
        alert(errorMessage)
      } catch {
        alert('保存失败，请重试')
      }
      console.error('保存前工序步骤失败')
    }
  } catch (error) {
    console.error('保存前工序步骤失败:', error)
    alert('保存失败，请重试')
  }
}

const closeStepDialog = () => {
  showAddStepDialog.value = false
  showEditStepDialog.value = false
  Object.assign(stepForm, {
    id: null,
    stepName: '',
    seriesName: '',
    productId: null,
    stepOrder: 1,
    isActive: true,
    status: 'V',  // 默认设置为兼容状态
    clothPaperId: null,
    paperType: '',
    stepId: null
  })
  // 清空烫金纸性能类型选项
  paperTypes.value = []
  // 清空产品搜索
  productSearchText.value = ''
  showProductDropdown.value = false
  // 清空产品系列搜索
  seriesSearchText.value = ''
  showSeriesDropdown.value = false
  // 清空烫金纸性能类型下拉
  showPaperTypeDropdown.value = false
}

const closeMappingDialog = () => {
  showMappingDialog.value = false
  currentPreProcessingOption.value = null
  preProcessingSteps.value = []
  selectedMappingItems.value = []
}

// 查看所有配置映射相关方法
const openAllMappingsDialog = async () => {
  showAllMappingsDialog.value = true
  await loadAllMappings()
  // 确保产品系列已加载
  if (productSeries.value.length === 0) {
    await loadProductSeries()
  }
  // 重置搜索表单
  Object.assign(allMappingsSearchForm, {
    stepName: '',
    seriesName: '',
    productModelNumber: '',
    paperType: '',
    status: ''
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
    // 获取所有前工序选项
    const optionsResponse = await fetch('/api/api/pre-processing-steps/options/all')
    if (!optionsResponse.ok) {
      console.error('加载前工序选项失败')
      return
    }
    const allOptions = await optionsResponse.json()
    
    // 获取所有前工序选项的映射
    const allMappingsData: any[] = []
    for (const option of allOptions) {
      try {
        const stepsResponse = await fetch(`/api/api/pre-processing-steps/options/${option.id}/steps`)
        if (stepsResponse.ok) {
          const steps = await stepsResponse.json()
          // 为每个步骤添加前工序类型名称
          steps.forEach((step: any) => {
            allMappingsData.push({
              ...step,
              stepName: option.preProcessingStepsName || step.stepName
            })
          })
        }
      } catch (error) {
        console.error(`加载前工序 ${option.id} 的映射失败:`, error)
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
  // 筛选逻辑已在 computed 中实现
}

const resetAllMappingsSearch = () => {
  Object.assign(allMappingsSearchForm, {
    stepName: '',
    seriesName: '',
    productModelNumber: '',
    paperType: '',
    status: ''
  })
  allMappingsCurrentPage.value = 1
}

const viewMappingDetails = (mapping: any) => {
  // 找到对应的前工序选项
  const option = options.value.find(opt => opt.preProcessingStepsName === mapping.stepName)
  if (option) {
    // 打开该前工序的配置映射对话框
    configureMapping(option)
  } else {
    alert('无法找到对应的前工序配置')
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

const isAllMappingsSelected = computed(() => {
  const currentPageIds = filteredAllMappings.value.map(m => m.id).filter((id): id is number => id !== undefined)
  return currentPageIds.length > 0 && currentPageIds.every(id => selectedAllMappingsItems.value.includes(id))
})

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
    stepName: '',
    seriesName: '',
    productModelNumber: '',
    paperType: '',
    stepOrder: null,
    isActive: null,
    status: ''
  })
  showAllMappingsBatchEditDialog.value = true
}

const closeAllMappingsBatchEditDialog = () => {
  showAllMappingsBatchEditDialog.value = false
  Object.assign(allMappingsBatchForm, {
    stepName: '',
    seriesName: '',
    productModelNumber: '',
    paperType: '',
    stepOrder: null,
    isActive: null,
    status: ''
  })
}

const batchUpdateAllMappings = async () => {
  if (selectedAllMappingsItems.value.length === 0) {
    alert('请选择要修改的映射')
    return
  }
  
  // 检查是否有任何字段需要更新
  const hasUpdate = allMappingsBatchForm.stepName !== '' ||
                   allMappingsBatchForm.seriesName !== '' ||
                   allMappingsBatchForm.productModelNumber !== '' ||
                   allMappingsBatchForm.paperType !== '' ||
                   allMappingsBatchForm.stepOrder !== null ||
                   allMappingsBatchForm.isActive !== null ||
                   allMappingsBatchForm.status !== ''
  
  if (!hasUpdate) {
    alert('请至少填写一个要修改的字段')
    return
  }
  
  // 验证步骤顺序
  if (allMappingsBatchForm.stepOrder !== null && allMappingsBatchForm.stepOrder < 1) {
    alert('步骤顺序必须大于等于1')
    return
  }
  
  try {
    const updatePromises = selectedAllMappingsItems.value.map(async id => {
      const mapping = allMappings.value.find(m => m.id === id)
      if (!mapping) return Promise.resolve()
      
      // 构建更新对象，只包含需要更新的字段
      const updateData: any = {}
      
      // 前工序类型
      if (allMappingsBatchForm.stepName !== '') {
        // 需要找到对应的前工序选项ID
        const option = options.value.find(opt => opt.preProcessingStepsName === allMappingsBatchForm.stepName)
        if (option) {
          updateData.preProcessingStepId = option.id
        }
      }
      
      // 产品系列
      if (allMappingsBatchForm.seriesName !== '') {
        updateData.seriesName = allMappingsBatchForm.seriesName
        // 如果产品系列改变了，需要清空productId，因为原来的productId可能不属于新的系列
        // 除非同时指定了新的产品型号
        if (!allMappingsBatchForm.productModelNumber) {
          updateData.productId = null
        }
      }
      
      // 产品型号 - 需要根据型号或名称查找产品ID
      if (allMappingsBatchForm.productModelNumber !== '') {
        // 如果同时指定了产品系列，需要在指定系列下查找产品
        const searchSeriesName = allMappingsBatchForm.seriesName || mapping.seriesName
        let product = null
        
        if (searchSeriesName) {
          // 在指定系列下查找产品
          product = allProducts.value.find(p => 
            p.seriesName === searchSeriesName &&
            (p.modelNumber === allMappingsBatchForm.productModelNumber || 
             p.name === allMappingsBatchForm.productModelNumber)
          )
        } else {
          // 没有指定系列，在所有产品中查找
          product = allProducts.value.find(p => 
            p.modelNumber === allMappingsBatchForm.productModelNumber || 
            p.name === allMappingsBatchForm.productModelNumber
          )
        }
        
        if (product) {
          updateData.productId = product.id
          // 如果找到了产品，确保产品系列匹配
          if (!allMappingsBatchForm.seriesName && product.seriesName) {
            updateData.seriesName = product.seriesName
          }
        } else {
          // 如果找不到产品，清空productId（表示通用映射）
          updateData.productId = null
          console.warn(`未找到产品: ${allMappingsBatchForm.productModelNumber}，将设置为通用映射`)
        }
      }
      
      // 烫金纸性能类型
      if (allMappingsBatchForm.paperType !== '') {
        updateData.paperType = allMappingsBatchForm.paperType
      }
      
      // 步骤顺序
      if (allMappingsBatchForm.stepOrder !== null) {
        updateData.stepOrder = allMappingsBatchForm.stepOrder
      }
      
      // 状态
      if (allMappingsBatchForm.isActive !== null) {
        updateData.isActive = allMappingsBatchForm.isActive
      }
      
      // 兼容状态
      if (allMappingsBatchForm.status !== '') {
        updateData.status = allMappingsBatchForm.status
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
      
      return fetch(`/api/api/pre-processing-steps/steps/${id}`, {
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
      fetch(`/api/api/pre-processing-steps/steps/${id}`, {
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

// 产品相关方法
const loadProductSeries = async () => {
  try {
    console.log('开始加载产品系列...')
    const response = await fetch('/api/api/product-management/names')
    console.log('产品系列API响应状态:', response.status)
    if (response.ok) {
      const data = await response.json()
      console.log('加载到的产品系列:', data)
      productSeries.value = data
    } else {
      console.error('加载产品系列失败，状态码:', response.status)
      productSeries.value = []
      alert('加载产品系列失败，请刷新页面重试')
    }
  } catch (error) {
    console.error('加载产品系列失败:', error)
    productSeries.value = []
    alert('加载产品系列失败，请刷新页面重试')
  }
}

// 加载产品列表
const loadProductList = async () => {
  loadingProducts.value = true
  try {
    console.log('开始加载产品列表...')
    const response = await fetch('/api/api/product-management')
    console.log('产品列表API响应状态:', response.status)
    if (response.ok) {
      const data = await response.json()
      console.log('加载到的产品列表:', data)
      allProducts.value = data.map((product: any) => ({
        id: product.id,
        modelNumber: product.model_number || product.modelNumber || '',
        name: product.name || '',
        seriesName: product.name || '' // 使用name作为产品系列，因为产品系列是通过name字段获取的
      }))
    } else {
      console.error('加载产品列表失败，状态码:', response.status)
      allProducts.value = []
      alert('加载产品列表失败，请刷新页面重试')
    }
  } catch (error) {
    console.error('加载产品列表失败:', error)
    allProducts.value = []
    alert('加载产品列表失败，请刷新页面重试')
  } finally {
    loadingProducts.value = false
  }
}

// 获取产品信息
const getProductInfo = async (productId: number) => {
  if (!productId) return null
  
  // 如果已经缓存了，直接返回
  if (productInfoMap.value.has(productId)) {
    return productInfoMap.value.get(productId)
  }
  
  try {
    // 使用 /api/api/products/{id} 接口获取产品信息
    const response = await fetch(`/api/api/products/${productId}`)
    if (response.ok) {
      const product = await response.json()
      const productInfo = {
        modelNumber: product.model_number || product.modelNumber || '',
        name: product.name || ''
      }
      productInfoMap.value.set(productId, productInfo)
      return productInfo
    } else {
      console.error('获取产品信息失败，产品ID:', productId, '状态码:', response.status)
      return null
    }
  } catch (error) {
    console.error('获取产品信息失败:', error)
    return null
  }
}

// 批量加载产品信息
const loadProductInfos = async (steps: any[]) => {
  const productIds = steps
    .map(step => step.productId)
    .filter(id => id && !productInfoMap.value.has(id))
  
  if (productIds.length === 0) return
  
  try {
    // 批量获取产品信息
    const promises = productIds.map(id => getProductInfo(id))
    await Promise.all(promises)
  } catch (error) {
    console.error('批量加载产品信息失败:', error)
  }
}

// 获取产品型号显示
const getProductModelNumber = (productId: number | null | undefined) => {
  if (!productId) return '-'
  
  const productInfo = productInfoMap.value.get(productId)
  if (productInfo) {
    // 优先返回 modelNumber，如果没有则返回 name
    return productInfo.modelNumber || productInfo.name || `产品ID: ${productId}`
  }
  
  // 如果缓存中没有，尝试异步加载（但不会立即返回，需要等待下次渲染）
  if (productId && !productInfoMap.value.has(productId)) {
    getProductInfo(productId).then(() => {
      // 触发响应式更新
    })
  }
  
  return '加载中...'
}

// 根据产品系列筛选产品型号
const filterProductsBySeries = (seriesName: string) => {
  if (!seriesName) {
    filteredProducts.value = []
    return
  }
  
  filteredProducts.value = allProducts.value.filter(product => 
    product.seriesName === seriesName
  )
  console.log(`产品系列 "${seriesName}" 对应的产品型号:`, filteredProducts.value)
  
  // 重置搜索文本
  productSearchText.value = ''
}

// 根据搜索文本过滤产品型号
const filteredProductsBySearch = computed(() => {
  if (!productSearchText.value || productSearchText.value.trim() === '') {
    return filteredProducts.value
  }
  
  const searchText = productSearchText.value.toLowerCase().trim()
  return filteredProducts.value.filter(product => {
    const modelNumber = (product.modelNumber || '').toLowerCase()
    const name = (product.name || '').toLowerCase()
    const productId = `产品${product.id}`.toLowerCase()
    
    return modelNumber.includes(searchText) || 
           name.includes(searchText) || 
           productId.includes(searchText)
  })
})

// 产品搜索输入处理
const onProductSearchInput = () => {
  showProductDropdown.value = true
}

// 选择产品
const selectProduct = (product: any) => {
  stepForm.productId = product.id
  productSearchText.value = product.modelNumber || product.name || `产品${product.id}`
  showProductDropdown.value = false
  onProductIdChange()
}

// 处理产品输入框失焦
const handleProductInputBlur = () => {
  // 延迟隐藏下拉框，以便点击选项时能触发选择
  setTimeout(() => {
    showProductDropdown.value = false
    // 如果输入框有值但没有匹配到产品，尝试精确匹配
    if (productSearchText.value && !stepForm.productId) {
      const exactMatch = filteredProducts.value.find(product => {
        const modelNumber = product.modelNumber || ''
        const name = product.name || ''
        const searchText = productSearchText.value.trim()
        return modelNumber === searchText || name === searchText
      })
      if (exactMatch) {
        selectProduct(exactMatch)
      } else {
        // 如果没有精确匹配，清空输入
        productSearchText.value = ''
      }
    } else if (!stepForm.productId) {
      // 如果没有选择产品，清空输入
      productSearchText.value = ''
    }
  }, 200)
}

// 获取已选择产品的显示名称
const getSelectedProductDisplayName = () => {
  if (!stepForm.productId) return ''
  const product = filteredProducts.value.find(p => p.id === stepForm.productId)
  if (product) {
    return product.modelNumber || product.name || `产品${product.id}`
  }
  return ''
}

// 根据搜索文本过滤产品系列
const filteredSeriesBySearch = computed(() => {
  if (!seriesSearchText.value || seriesSearchText.value.trim() === '') {
    return productSeries.value
  }
  
  const searchText = seriesSearchText.value.toLowerCase().trim()
  return productSeries.value.filter(series => 
    series.toLowerCase().includes(searchText)
  )
})

// 批量添加对话框中支持指定烫金纸性能类型的产品系列
const batchAddAvailableSeries = ref<string[]>([])

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
    filtered = filtered.filter(series => 
      series.toLowerCase().includes(searchText)
    )
  }
  
  return filtered
})

// 产品系列搜索输入处理
const onSeriesSearchInput = () => {
  showSeriesDropdown.value = true
}

// 选择产品系列
const selectSeries = async (series: string) => {
  stepForm.seriesName = series
  seriesSearchText.value = series
  showSeriesDropdown.value = false
  await onSeriesNameChange()
}

// 处理产品系列输入框失焦
const handleSeriesInputBlur = () => {
  // 延迟隐藏下拉框，以便点击选项时能触发选择
  setTimeout(() => {
    showSeriesDropdown.value = false
    // 如果输入框有值但没有匹配到产品系列，尝试精确匹配
    if (seriesSearchText.value && !stepForm.seriesName) {
      const exactMatch = productSeries.value.find(series => 
        series.toLowerCase() === seriesSearchText.value.toLowerCase().trim()
      )
      if (exactMatch) {
        selectSeries(exactMatch)
      } else {
        // 如果没有精确匹配，清空输入
        seriesSearchText.value = ''
      }
    } else if (!stepForm.seriesName) {
      // 如果没有选择产品系列，清空输入
      seriesSearchText.value = ''
    } else {
      // 如果已选择，更新搜索文本为选中的值
      seriesSearchText.value = stepForm.seriesName
    }
  }, 200)
}

// 选择烫金纸性能类型
const selectPaperType = (paperType: string) => {
  stepForm.paperType = paperType
  showPaperTypeDropdown.value = false
}

// 处理烫金纸性能类型输入框失焦
const handlePaperTypeInputBlur = () => {
  // 延迟隐藏下拉框，以便点击选项时能触发选择
  setTimeout(() => {
    showPaperTypeDropdown.value = false
    // 如果输入框有值但没有匹配到烫金纸性能类型，尝试精确匹配
    if (stepForm.paperType && stepForm.paperType.trim() !== '') {
      const exactMatch = paperTypes.value.find(type => 
        type.toLowerCase() === stepForm.paperType.toLowerCase().trim()
      )
      if (!exactMatch) {
        // 如果没有精确匹配，保持用户输入的值（允许自定义输入）
        // 不做任何处理，保留用户输入
      }
    }
  }, 200)
}

// 搜索区域的产品系列变化处理
const onSearchSeriesChange = () => {
  // 清空产品型号选择
  mappingSearchForm.productModelNumber = ''
  
  // 根据产品系列筛选产品型号
  if (mappingSearchForm.seriesName) {
    searchFilteredProducts.value = allProducts.value.filter(product => 
      product.seriesName === mappingSearchForm.seriesName
    )
  } else {
    searchFilteredProducts.value = []
  }
  console.log(`搜索区域产品系列 "${mappingSearchForm.seriesName}" 对应的产品型号:`, searchFilteredProducts.value)
}

// 加载前工序类型和工艺类型选项
const loadPreprocessingTypes = async () => {
  try {
    console.log('开始加载前工序类型和工艺类型...')
    const response = await fetch('/api/api/pre-processing-steps/options/all')
    if (response.ok) {
      const data = await response.json()
      console.log('加载到的前工序数据:', data)
      
      // 保存原始数据用于联动查询
      allPreprocessingData.value = data
      
      // 从所有前工序选项中提取唯一的steps值作为前工序类型选项
      const preprocessingTypeOptions = [...new Set(data.map((item: any) => item.steps).filter(Boolean))] as string[]
      // 从所有前工序选项中提取唯一的process值作为工艺类型选项
      const processTypeOptions = [...new Set(data.map((item: any) => item.process).filter(Boolean))] as string[]
      
      preprocessingTypes.value = preprocessingTypeOptions
      processTypes.value = processTypeOptions
      console.log('提取的前工序类型(steps):', preprocessingTypeOptions)
      console.log('提取的工艺类型(process):', processTypeOptions)
    } else {
      console.error('加载前工序类型失败，状态码:', response.status)
      allPreprocessingData.value = []
      preprocessingTypes.value = []
      processTypes.value = []
      alert('加载前工序类型失败，请刷新页面重试')
    }
  } catch (error) {
    console.error('加载前工序类型失败:', error)
    allPreprocessingData.value = []
    preprocessingTypes.value = []
    processTypes.value = []
    alert('加载前工序类型失败，请刷新页面重试')
  }
}

const loadPaperTypes = async (seriesName: string, currentPaperType: string = '') => {
  console.log('加载烫金纸性能类型 - 产品系列:', seriesName, '当前烫金纸性能类型:', currentPaperType)
  
  if (!seriesName) {
    paperTypes.value = []
    return
  }
  
  loadingPaperTypes.value = true
  try {
    const response = await fetch(`/api/api/products/paper-types/${encodeURIComponent(seriesName)}`)
    if (response.ok) {
      const loadedPaperTypes = await response.json()
      console.log('加载到的烫金纸性能类型:', loadedPaperTypes)
      paperTypes.value = loadedPaperTypes
      
      // 如果有指定的烫金纸性能类型，设置为选中状态
      if (currentPaperType) {
        console.log('设置烫金纸性能类型为:', currentPaperType)
        stepForm.paperType = currentPaperType
      }
    } else {
      console.error('加载烫金纸性能类型失败，状态码:', response.status)
      paperTypes.value = []
      alert('加载烫金纸性能类型失败，请刷新页面重试')
    }
  } catch (error) {
    console.error('加载烫金纸性能类型失败:', error)
    paperTypes.value = []
    alert('加载烫金纸性能类型失败，请刷新页面重试')
  } finally {
    loadingPaperTypes.value = false
  }
}

const loadPaperTypesByProductId = async (productId: number) => {
  console.log('根据产品ID加载烫金纸性能类型:', productId)
  
  if (!productId) {
    paperTypes.value = []
    return
  }
  
  loadingPaperTypes.value = true
  try {
    const response = await fetch(`/api/api/products/${productId}`)
    if (response.ok) {
      const product = await response.json()
      console.log('获取到的产品信息:', product)
      
      // 从产品信息中获取烫金纸性能类型
      if (product.hot_stamping_paper_type) {
        paperTypes.value = [product.hot_stamping_paper_type]
        stepForm.paperType = product.hot_stamping_paper_type
        console.log('设置烫金纸性能类型为:', product.hot_stamping_paper_type)
      } else {
        paperTypes.value = []
        stepForm.paperType = ''
        console.log('该产品没有烫金纸性能类型信息')
      }
    } else {
      console.error('获取产品信息失败，状态码:', response.status)
      paperTypes.value = []
    }
  } catch (error) {
    console.error('获取产品信息失败:', error)
    paperTypes.value = []
  } finally {
    loadingPaperTypes.value = false
  }
}

const onSeriesNameChange = async () => {
  // 当产品系列改变时，清空产品型号和烫金纸性能类型并重新加载
  // 但如果是编辑模式，需要保持当前的值
  const currentPaperType = stepForm.paperType
  
  // 更新产品系列搜索文本
  if (stepForm.seriesName) {
    seriesSearchText.value = stepForm.seriesName
  }
  
  // 清空产品型号选择
  stepForm.productId = null
  stepForm.paperType = ''
  productSearchText.value = ''
  showProductDropdown.value = false
  
  // 根据产品系列筛选产品型号
  filterProductsBySeries(stepForm.seriesName)
  
  // 加载烫金纸性能类型
  if (stepForm.seriesName) {
    await loadPaperTypes(stepForm.seriesName, currentPaperType)
  } else {
    paperTypes.value = []
  }
}

const onProductIdChange = async () => {
  // 当产品型号改变时，查询该产品型号的烫金纸性能类型
  if (stepForm.productId) {
    console.log('产品型号改变，查询产品烫金纸性能类型:', stepForm.productId)
    await loadPaperTypesByProductId(stepForm.productId)
  } else {
    // 如果没有选择产品型号，则查询产品系列的烫金纸性能类型
    if (stepForm.seriesName) {
      await loadPaperTypes(stepForm.seriesName, '')
    }
  }
}

// 搜索映射步骤
const searchMappingSteps = () => {
  mappingCurrentPage.value = 1
}

// 批量操作相关方法
const toggleMappingSelection = (stepId: number) => {
  const index = selectedMappingItems.value.indexOf(stepId)
  if (index > -1) {
    selectedMappingItems.value.splice(index, 1)
  } else {
    selectedMappingItems.value.push(stepId)
  }
}

const toggleSelectAllMappings = () => {
  if (isAllMappingSelected.value) {
    selectedMappingItems.value = []
  } else {
    selectedMappingItems.value = filteredMappingSteps.value.map(step => step.id)
  }
}

const clearMappingSelection = () => {
  selectedMappingItems.value = []
}

// 批量删除映射
const batchDeleteMappings = async () => {
  if (selectedMappingItems.value.length === 0) {
    alert('请选择要删除的映射')
    return
  }
  
  if (!confirm(`确定要删除选中的 ${selectedMappingItems.value.length} 个映射吗？此操作不可恢复！`)) {
    return
  }
  
  try {
    const deletePromises = selectedMappingItems.value.map(id => 
      fetch(`/api/api/pre-processing-steps/steps/${id}`, {
        method: 'DELETE'
      })
    )
    
    await Promise.all(deletePromises)
    
    // 重新加载数据
    if (currentPreProcessingOption.value) {
      await loadPreProcessingSteps(currentPreProcessingOption.value.id)
    }
    
    selectedMappingItems.value = []
    alert('批量删除成功')
  } catch (error) {
    console.error('批量删除失败:', error)
    alert('批量删除失败，请重试')
  }
}

// 打开批量修改状态对话框
const openBatchEditStatusDialog = () => {
  if (selectedMappingItems.value.length === 0) {
    alert('请选择要修改的映射')
    return
  }
  showBatchEditStatusDialog.value = true
}

// 关闭批量修改状态对话框
const closeBatchEditStatusDialog = () => {
  showBatchEditStatusDialog.value = false
  batchStatusForm.isActive = true
}

// 批量修改状态
const batchUpdateStatus = async () => {
  if (selectedMappingItems.value.length === 0) {
    alert('请选择要修改的映射')
    return
  }
  
  try {
    const updatePromises = selectedMappingItems.value.map(id => {
      const step = preProcessingSteps.value.find(s => s.id === id)
      if (!step) return Promise.resolve()
      
      return fetch(`/api/api/pre-processing-steps/steps/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...step,
          isActive: batchStatusForm.isActive
        })
      })
    })
    
    await Promise.all(updatePromises)
    
    // 重新加载数据
    if (currentPreProcessingOption.value) {
      await loadPreProcessingSteps(currentPreProcessingOption.value.id)
    }
    
    closeBatchEditStatusDialog()
    selectedMappingItems.value = []
    alert('批量修改状态成功')
  } catch (error) {
    console.error('批量修改状态失败:', error)
    alert('批量修改状态失败，请重试')
  }
}

// 打开批量修改步骤顺序对话框
const openBatchEditOrderDialog = () => {
  if (selectedMappingItems.value.length === 0) {
    alert('请选择要修改的映射')
    return
  }
  showBatchEditOrderDialog.value = true
}

// 关闭批量修改步骤顺序对话框
const closeBatchEditOrderDialog = () => {
  showBatchEditOrderDialog.value = false
  batchOrderForm.stepOrder = 1
}

// 批量修改步骤顺序
const batchUpdateOrder = async () => {
  if (selectedMappingItems.value.length === 0) {
    alert('请选择要修改的映射')
    return
  }
  
  if (!batchOrderForm.stepOrder || batchOrderForm.stepOrder < 1) {
    alert('请输入有效的步骤顺序（大于等于1）')
    return
  }
  
  try {
    const updatePromises = selectedMappingItems.value.map(id => {
      const step = preProcessingSteps.value.find(s => s.id === id)
      if (!step) return Promise.resolve()
      
      return fetch(`/api/api/pre-processing-steps/steps/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...step,
          stepOrder: batchOrderForm.stepOrder
        })
      })
    })
    
    await Promise.all(updatePromises)
    
    // 重新加载数据
    if (currentPreProcessingOption.value) {
      await loadPreProcessingSteps(currentPreProcessingOption.value.id)
    }
    
    closeBatchEditOrderDialog()
    selectedMappingItems.value = []
    alert('批量修改步骤顺序成功')
  } catch (error) {
    console.error('批量修改步骤顺序失败:', error)
    alert('批量修改步骤顺序失败，请重试')
  }
}

// 快速复制映射
const copyMapping = async (step: any) => {
  console.log('复制映射数据:', step)
  
  // 复制映射数据到表单
  Object.assign(stepForm, {
    id: null, // 清空ID，表示这是新记录
    stepName: step.stepName,
    seriesName: step.seriesName,
    productId: step.productId,
    stepOrder: step.stepOrder,
    isActive: step.isActive,
    status: step.status || 'V',
    clothPaperId: step.clothPaperId,
    paperType: step.paperType,
    stepId: currentPreProcessingOption.value?.id || step.stepId
  })
  
  // 加载产品列表
  await loadProductList()
  
  // 如果已有产品系列，筛选对应的产品型号并加载烫金纸性能类型
  if (step.seriesName) {
    filterProductsBySeries(step.seriesName)
    await loadPaperTypes(step.seriesName, step.paperType || '')
  } else {
    paperTypes.value = []
    filteredProducts.value = []
  }
  
  showAddStepDialog.value = true
}

// 批量添加相关方法
const openBatchAddDialog = async () => {
  if (!currentPreProcessingOption.value) {
    alert('请先选择前工序选项')
    return
  }
  
  // 重置表单
  Object.assign(batchAddForm, {
    stepName: currentPreProcessingOption.value.preProcessingStepsName || '',
    selectedSeries: [],
    stepOrder: preProcessingSteps.value.length + 1,
    paperType: '',
    isActive: true,
    status: 'V'
  })
  
  // 加载所有烫金纸性能类型
  await loadAllPaperTypes()
  
  // 重置搜索文本和可用产品系列
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
  batchAddForm.selectedProducts = {}
  batchAddSeriesProducts.value = {}
  
  showBatchAddDialog.value = true
}

const closeBatchAddDialog = () => {
  showBatchAddDialog.value = false
  Object.assign(batchAddForm, {
    stepName: '',
    selectedSeries: [],
    stepOrder: 1,
    paperType: '',
    isActive: true,
    status: 'V'
  })
  // 重置搜索文本和可用产品系列
  batchAddSeriesSearchText.value = ''
  batchAddAvailableSeries.value = []
  batchAddForm.selectedProducts = {}
  batchAddSeriesProducts.value = {}
}

const batchAddMappings = async () => {
  if (batchAddForm.selectedSeries.length === 0) {
    alert('请至少选择一个产品系列')
    return
  }
  
  if (!currentPreProcessingOption.value) {
    alert('前工序选项不存在')
    return
  }
  
  try {
    // 为每个产品系列创建映射
    const createPromises: Promise<any>[] = []
    
    for (const seriesName of batchAddForm.selectedSeries) {
      const selectedProductIds = batchAddForm.selectedProducts[seriesName] || []
      
      if (selectedProductIds.length === 0) {
        // 如果没有选择任何产品，创建产品系列级别的通用映射（product_id 为 null）
        const mappingData = {
          stepName: batchAddForm.stepName,
          seriesName: seriesName,
          productId: null,
          stepOrder: batchAddForm.stepOrder,
          isActive: batchAddForm.isActive,
          status: batchAddForm.status,
          clothPaperId: null,
          paperType: batchAddForm.paperType || '',
          stepId: currentPreProcessingOption.value.id
        }
        
        createPromises.push(
          fetch('/api/api/pre-processing-steps/steps', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(mappingData)
          })
        )
      } else {
        // 为每个选中的产品创建映射
        for (const productId of selectedProductIds) {
          const mappingData = {
            stepName: batchAddForm.stepName,
            seriesName: seriesName,
            productId: productId,
            stepOrder: batchAddForm.stepOrder,
            isActive: batchAddForm.isActive,
            status: batchAddForm.status,
            clothPaperId: null,
            paperType: batchAddForm.paperType || '',
            stepId: currentPreProcessingOption.value.id
          }
          
          createPromises.push(
            fetch('/api/api/pre-processing-steps/steps', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(mappingData)
            })
          )
        }
      }
    }
    
    // 批量创建
    const results = await Promise.allSettled(createPromises)
    const successCount = results.filter(r => {
      if (r.status === 'fulfilled') {
        const response = r.value
        return response.ok
      }
      return false
    }).length
    
    const failResults: Array<{ seriesName: string; productId: number | null; error: string }> = []
    let index = 0
    for (const seriesName of batchAddForm.selectedSeries) {
      const selectedProductIds = batchAddForm.selectedProducts[seriesName] || []
      const count = selectedProductIds.length === 0 ? 1 : selectedProductIds.length
      
      for (let i = 0; i < count; i++) {
        const result = results[index]
        const productId = selectedProductIds.length === 0 ? null : selectedProductIds[i]
        
        if (result.status === 'rejected') {
          failResults.push({ 
            seriesName, 
            productId,
            error: (result.reason as Error)?.message || '未知错误' 
          })
        } else if (result.status === 'fulfilled') {
          const response = result.value
          if (!response.ok) {
            try {
              const errorData = await response.json()
              failResults.push({ 
                seriesName, 
                productId,
                error: errorData.message || `HTTP ${response.status}` 
              })
            } catch {
              failResults.push({ seriesName, productId, error: `HTTP ${response.status}` })
            }
          }
        }
        index++
      }
    }
    
    const failCount = failResults.length
    
    // 重新加载数据
    if (currentPreProcessingOption.value) {
      await loadPreProcessingSteps(currentPreProcessingOption.value.id)
    }
    
    closeBatchAddDialog()
    
    if (failCount === 0) {
      alert(`批量添加成功！共创建 ${successCount} 个映射配置`)
    } else {
      const errorMessages = failResults.map(r => {
        const productInfo = r.productId === null ? '（通用映射）' : `（产品ID: ${r.productId}）`
        return `${r.seriesName}${productInfo}: ${r.error}`
      }).join('\n')
      alert(`批量添加完成！成功：${successCount} 个，失败：${failCount} 个\n\n失败详情：\n${errorMessages}`)
    }
  } catch (error) {
    console.error('批量添加失败:', error)
    alert('批量添加失败，请重试')
  }
}

// 加载所有烫金纸性能类型
const loadAllPaperTypes = async () => {
  try {
    // 从所有产品系列中收集烫金纸性能类型
    const paperTypeSet = new Set<string>()
    
    for (const series of productSeries.value) {
      try {
        const response = await fetch(`/api/api/products/paper-types/${encodeURIComponent(series)}`)
        if (response.ok) {
          const types = await response.json()
          types.forEach((type: string) => paperTypeSet.add(type))
        }
      } catch (error) {
        console.error(`加载 ${series} 的烫金纸性能类型失败:`, error)
      }
    }
    
    allPaperTypes.value = Array.from(paperTypeSet).sort()
  } catch (error) {
    console.error('加载所有烫金纸性能类型失败:', error)
    allPaperTypes.value = []
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
  // 清空之前选择的产品系列和产品
  batchAddForm.selectedSeries = []
  batchAddForm.selectedProducts = {}
  batchAddSeriesProducts.value = {}
  batchAddSeriesSearchText.value = ''
  
  // 加载支持该烫金纸性能类型的产品系列
  if (batchAddForm.paperType) {
    await loadSeriesByPaperType(batchAddForm.paperType)
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
            modelNumber: p.model_number || p.modelNumber,
            name: p.name
          }))
          // 初始化该系列的产品选择数组
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

// 导入/导出相关方法
const openImportDialog = () => {
  importResult.value = null
  selectedFile.value = null
  if (importFileInput.value) {
    importFileInput.value.value = ''
  }
  showImportDialog.value = true
}

const closeImportDialog = () => {
  showImportDialog.value = false
  importResult.value = null
  selectedFile.value = null
  if (importFileInput.value) {
    importFileInput.value.value = ''
  }
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
    const response = await fetch('/api/api/pre-processing-steps/steps/import-template', {
      method: 'GET'
    })
    
    if (!response.ok) {
      // 如果后端没有模板接口，生成一个简单的模板
      generateTemplateFile()
      return
    }
    
    const blob = await response.blob()
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '映射配置导入模板.xlsx'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
  } catch (error) {
    console.error('下载模板失败:', error)
    generateTemplateFile()
  }
}

const generateTemplateFile = () => {
  // 生成简单的CSV模板（如果后端没有Excel模板）
  const headers = ['步骤名称', '产品系列', '产品型号', '烫金纸性能类型', '步骤顺序', '激活状态', '兼容状态']
  const csvContent = headers.join(',') + '\n示例, A19, A19-001, 普通金纸, 1, true, V'
  
  const blob = new Blob(['\ufeff' + csvContent], { type: 'text/csv;charset=utf-8;' })
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = '映射配置导入模板.csv'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  window.URL.revokeObjectURL(url)
  
  alert('已生成CSV模板文件，请使用Excel打开并保存为.xlsx格式')
}

const importMappings = async () => {
  if (!selectedFile.value) {
    alert('请选择要导入的文件')
    return
  }
  
  if (!currentPreProcessingOption.value) {
    alert('前工序选项不存在')
    return
  }
  
  const formData = new FormData()
  formData.append('file', selectedFile.value)
  formData.append('stepId', currentPreProcessingOption.value.id.toString())
  
  try {
    const response = await fetch('/api/api/pre-processing-steps/steps/import', {
      method: 'POST',
      body: formData
    })
    
    const result = await response.json()
    
    if (response.ok) {
      importResult.value = {
        success: true,
        message: result.message || '导入成功',
        details: result.details
      }
      
      // 重新加载数据
      if (currentPreProcessingOption.value) {
        await loadPreProcessingSteps(currentPreProcessingOption.value.id)
      }
      
      // 清空文件选择
      selectedFile.value = null
      if (importFileInput.value) {
        importFileInput.value.value = ''
      }
    } else {
      importResult.value = {
        success: false,
        message: result.message || '导入失败',
        details: result.details || result.errors
      }
    }
  } catch (error) {
    console.error('导入失败:', error)
    importResult.value = {
      success: false,
      message: '导入失败，请检查文件格式是否正确',
      details: [String(error)]
    }
  }
}

const exportMappings = async () => {
  if (!currentPreProcessingOption.value) {
    alert('前工序选项不存在')
    return
  }
  
  try {
    const response = await fetch(`/api/api/pre-processing-steps/steps/export?stepId=${currentPreProcessingOption.value.id}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token') || ''}`
      }
    })
    
    if (!response.ok) {
      throw new Error('导出失败')
    }
    
    // 获取文件名
    const contentDisposition = response.headers.get('Content-Disposition')
    let fileName = '前工序映射配置.xlsx'
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

// 监听前工序类型变化，实现联动效果
watch(() => searchForm.preprocessingType, (newValue) => {
  // 当前工序类型改变时，清空工艺类型选择
  if (newValue !== searchForm.process) {
    searchForm.process = ''
  }
})

// 导出数据
const exportData = async () => {
  try {
    // 调用导出API
    const response = await fetch('/api/pre-processing-steps/options/export', {
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
    let fileName = '前工序选项.xlsx'
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

// 生命周期
onMounted(() => {
  loadOptions()
  loadProductSeries()
  loadPreprocessingTypes()
  loadProductList() // 预加载产品列表，用于批量添加
})
</script>

<style scoped>
/* 可以添加特定的样式 */
</style>
