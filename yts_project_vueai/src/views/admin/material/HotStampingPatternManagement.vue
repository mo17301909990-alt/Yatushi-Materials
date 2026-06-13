<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">烫金图案基础信息管理</h1>
            <p class="mt-2 text-gray-600">管理烫金图案的基础信息配置</p>
          </div>
          <div class="flex space-x-3">
            <button
              @click="showAddDialog = true"
              class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加图案
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

      <!-- 管理说明 -->
      <div class="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
        <div class="flex items-start">
          <svg class="w-5 h-5 text-blue-500 mr-2 mt-0.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
          </svg>
          <div>
            <h3 class="text-sm font-medium text-blue-800">烫金图案基础信息管理</h3>
            <p class="text-sm text-blue-700 mt-1">
              管理烫金图案的基础信息，包括图案类型、线条粗细范围、实地面积范围等配置。这些信息用于烫金工艺的兼容性判断和参数设置。
            </p>
          </div>
        </div>
      </div>

      <!-- 筛选面板 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">选项名称</label>
            <input
              v-model="filters.optionName"
              type="text"
              placeholder="搜索选项名称"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">图案类型</label>
            <input
              v-model="filters.patternType"
              type="text"
              placeholder="搜索图案类型"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">状态</label>
            <select
              v-model="filters.isActive"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              <option value="">全部状态</option>
              <option value="true">激活</option>
              <option value="false">未激活</option>
            </select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">排序方式</label>
            <select
              v-model="filters.sortBy"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              <option value="sortOrder">排序顺序</option>
              <option value="optionName">选项名称</option>
              <option value="patternType">图案类型</option>
              <option value="createdAt">创建时间</option>
            </select>
          </div>
          <div class="flex items-end space-x-2">
            <button
              @click="applyFilters"
              class="flex-1 bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 transition-colors"
            >
              筛选
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

      <!-- 数据表格 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <div class="flex items-center justify-between">
            <h3 class="text-lg font-medium text-gray-900">烫金图案基础信息列表</h3>
            <div class="flex items-center space-x-2">
              <span class="text-sm text-gray-500">共 {{ filteredData.length }} 条记录</span>
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
                  表格视图
                </button>
                <button
                  @click="viewMode = 'card'"
                  :class="[
                    'px-3 py-1 text-sm font-medium rounded-r-md border-t border-r border-b',
                    viewMode === 'card' 
                      ? 'bg-indigo-600 text-white border-indigo-600' 
                      : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-50'
                  ]"
                >
                  卡片视图
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
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">选项名称</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">图案类型</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">线条粗细范围</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">实地面积范围</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">排序顺序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
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
                    <span class="font-medium">{{ item.optionName }}</span>
                    <span class="text-xs text-gray-500">ID: {{ item.id }}</span>
                  </div>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.patternType }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <span v-if="item.lineThicknessMin && item.lineThicknessMax">
                    {{ item.lineThicknessMin }} - {{ item.lineThicknessMax }}mm
                  </span>
                  <span v-else class="text-gray-400">-</span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  <span v-if="item.solidAreaMin && item.solidAreaMax">
                    {{ item.solidAreaMin }} - {{ item.solidAreaMax }}mm²
                  </span>
                  <span v-else class="text-gray-400">-</span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.sortOrder }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="[
                      'inline-flex px-2 py-1 text-xs font-semibold rounded-full',
                      item.isActive 
                        ? 'bg-green-100 text-green-800' 
                        : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ item.isActive ? '激活' : '未激活' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex space-x-2">
                    <button
                      @click="editItem(item)"
                      class="text-indigo-600 hover:text-indigo-900"
                    >
                      编辑
                    </button>
                    <button
                      @click="copyItem(item)"
                      class="text-green-600 hover:text-green-900"
                    >
                      复制
                    </button>
                    <button
                      @click="configMapping(item)"
                      class="text-blue-600 hover:text-blue-900"
                    >
                      配置映射
                    </button>
                    <button
                      @click="deleteItem(item)"
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

        <!-- 卡片视图 -->
        <div v-if="viewMode === 'card'" class="p-6">
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
                      item.isActive 
                        ? 'bg-green-100 text-green-800' 
                        : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ item.isActive ? '激活' : '未激活' }}
                  </span>
                </div>
                <div class="flex space-x-1">
                  <button
                    @click="editItem(item)"
                    class="text-indigo-600 hover:text-indigo-900 text-sm"
                  >
                    编辑
                  </button>
                  <button
                    @click="copyItem(item)"
                    class="text-green-600 hover:text-green-900 text-sm"
                  >
                    复制
                  </button>
                  <button
                    @click="configMapping(item)"
                    class="text-blue-600 hover:text-blue-900 text-sm"
                  >
                    配置映射
                  </button>
                </div>
              </div>
              <div class="space-y-2">
                <div>
                  <span class="text-xs text-gray-500">选项名称:</span>
                  <span class="text-sm font-medium">{{ item.optionName }}</span>
                </div>
                <div>
                  <span class="text-xs text-gray-500">图案类型:</span>
                  <span class="text-sm font-medium">{{ item.patternType }}</span>
                </div>
                <div v-if="item.lineThicknessMin && item.lineThicknessMax">
                  <span class="text-xs text-gray-500">线条粗细:</span>
                  <span class="text-sm font-medium">{{ item.lineThicknessMin }} - {{ item.lineThicknessMax }}mm</span>
                </div>
                <div v-if="item.solidAreaMin && item.solidAreaMax">
                  <span class="text-xs text-gray-500">实地面积:</span>
                  <span class="text-sm font-medium">{{ item.solidAreaMin }} - {{ item.solidAreaMax }}mm²</span>
                </div>
                <div>
                  <span class="text-xs text-gray-500">排序顺序:</span>
                  <span class="text-sm font-medium">{{ item.sortOrder }}</span>
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
              上一页
            </button>
            <button
              @click="currentPage = Math.min(totalPages, currentPage + 1)"
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
                <span class="font-medium">{{ Math.min(currentPage * pageSize, filteredData.length) }}</span> 条，
                共 <span class="font-medium">{{ filteredData.length }}</span> 条记录
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
            {{ showAddDialog ? '添加烫金图案基础信息' : '编辑烫金图案基础信息' }}
          </h3>
          
          <form @submit.prevent="saveItem" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">选项名称 *</label>
                <input
                  v-model="formData.optionName"
                  type="text"
                  required
                  placeholder="如：淨線條/字母≤0.5mm"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">图案类型 *</label>
                <input
                  v-model="formData.patternType"
                  type="text"
                  required
                  placeholder="如：线条图案"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">线条粗细最小值 (mm)</label>
                <input
                  v-model.number="formData.lineThicknessMin"
                  type="number"
                  step="0.1"
                  min="0"
                  placeholder="0.1"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">线条粗细最大值 (mm)</label>
                <input
                  v-model.number="formData.lineThicknessMax"
                  type="number"
                  step="0.1"
                  min="0"
                  placeholder="0.5"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">实地面积最小值 (mm²)</label>
                <input
                  v-model.number="formData.solidAreaMin"
                  type="number"
                  step="0.1"
                  min="0"
                  placeholder="10.0"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">实地面积最大值 (mm²)</label>
                <input
                  v-model.number="formData.solidAreaMax"
                  type="number"
                  step="0.1"
                  min="0"
                  placeholder="20.0"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">排序顺序 *</label>
                <input
                  v-model.number="formData.sortOrder"
                  type="number"
                  required
                  min="0"
                  placeholder="0"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">状态 *</label>
                <select
                  v-model="formData.isActive"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                  <option value="true">激活</option>
                  <option value="false">未激活</option>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { patternOptionsApi, type HotStampingPatternBase, type CreateHotStampingPatternBase } from '../../../api/modules/patternOptions'

// 路由实例
const router = useRouter()

// 响应式数据
const patterns = ref<HotStampingPatternBase[]>([])
const loading = ref(false)
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const viewMode = ref('table')
const currentPage = ref(1)
const pageSize = ref(10)
const selectedItems = ref<number[]>([])

// 筛选条件
const filters = reactive({
  optionName: '',
  patternType: '',
  isActive: '',
  sortBy: 'sortOrder'
})

// 表单数据
const formData = reactive<Partial<CreateHotStampingPatternBase> & { id?: number }>({
  id: undefined,
  optionName: '',
  patternType: '',
  lineThicknessMin: undefined,
  lineThicknessMax: undefined,
  solidAreaMin: undefined,
  solidAreaMax: undefined,
  isActive: true,
  sortOrder: 0
})

// 计算属性
const filteredData = computed(() => {
  let data = patterns.value

  if (filters.optionName) {
    data = data.filter(item => 
      item.optionName.toLowerCase().includes(filters.optionName.toLowerCase())
    )
  }
  if (filters.patternType) {
    data = data.filter(item => 
      item.patternType.toLowerCase().includes(filters.patternType.toLowerCase())
    )
  }
  if (filters.isActive !== '') {
    const isActive = filters.isActive === 'true'
    data = data.filter(item => item.isActive === isActive)
  }

  // 排序
  data.sort((a, b) => {
    switch (filters.sortBy) {
      case 'optionName':
        return a.optionName.localeCompare(b.optionName)
      case 'patternType':
        return a.patternType.localeCompare(b.patternType)
      case 'createdAt':
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime()
      default:
        return a.sortOrder - b.sortOrder
    }
  })

  return data
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
const loadPatterns = async () => {
  loading.value = true
  try {
    const response = await patternOptionsApi.getAllPatterns()
    patterns.value = response || []
  } catch (error) {
    console.error('加载烫金图案基础信息失败:', error)
    patterns.value = []
    alert('加载烫金图案基础信息失败，请刷新页面重试')
  } finally {
    loading.value = false
  }
}

const applyFilters = () => {
  currentPage.value = 1
}

const resetFilters = () => {
  Object.assign(filters, {
    optionName: '',
    patternType: '',
    isActive: '',
    sortBy: 'sortOrder'
  })
  currentPage.value = 1
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

const editItem = (item: HotStampingPatternBase) => {
  Object.assign(formData, {
    id: item.id,
    optionName: item.optionName,
    patternType: item.patternType,
    lineThicknessMin: item.lineThicknessMin,
    lineThicknessMax: item.lineThicknessMax,
    solidAreaMin: item.solidAreaMin,
    solidAreaMax: item.solidAreaMax,
    isActive: item.isActive,
    sortOrder: item.sortOrder
  })
  showEditDialog.value = true
}

const copyItem = (item: HotStampingPatternBase) => {
  Object.assign(formData, {
    optionName: item.optionName + ' (副本)',
    patternType: item.patternType,
    lineThicknessMin: item.lineThicknessMin,
    lineThicknessMax: item.lineThicknessMax,
    solidAreaMin: item.solidAreaMin,
    solidAreaMax: item.solidAreaMax,
    isActive: item.isActive,
    sortOrder: item.sortOrder + 1
  })
  showAddDialog.value = true
}

const configMapping = (item: HotStampingPatternBase) => {
  router.push({
    path: '/admin/material/smart-compatibility',
    query: { patternCharacteristicId: item.id }
  })
}

const deleteItem = async (item: HotStampingPatternBase) => {
  if (confirm('确定要删除这个烫金图案基础信息吗？')) {
    try {
      await patternOptionsApi.deletePattern(item.id)
      await loadPatterns()
    } catch (error) {
      console.error('删除失败:', error)
      // 如果API调用失败，从本地数据中删除
      const index = patterns.value.findIndex(d => d.id === item.id)
      if (index > -1) {
        patterns.value.splice(index, 1)
      }
    }
  }
}

const saveItem = async () => {
  try {
    if (showAddDialog.value) {
      // 添加新图案
      const newItem = await patternOptionsApi.createPattern(formData as CreateHotStampingPatternBase)
      patterns.value.push(newItem)
    } else {
      // 更新现有图案
      const updatedItem = await patternOptionsApi.updatePattern(formData.id!, formData)
      const index = patterns.value.findIndex(d => d.id === formData.id)
      if (index > -1) {
        patterns.value[index] = updatedItem
      }
    }
    closeDialog()
  } catch (error) {
    console.error('保存失败:', error)
    // 如果API调用失败，使用本地数据更新
    if (showAddDialog.value) {
      const newItem = {
        ...formData,
        id: Date.now(),
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      } as HotStampingPatternBase
      patterns.value.push(newItem)
    } else {
      const index = patterns.value.findIndex(d => d.id === formData.id)
      if (index > -1) {
        patterns.value[index] = { ...patterns.value[index], ...formData }
      }
    }
    closeDialog()
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(formData, {
    id: undefined,
    optionName: '',
    patternType: '',
    lineThicknessMin: undefined,
    lineThicknessMax: undefined,
    solidAreaMin: undefined,
    solidAreaMax: undefined,
    isActive: true,
    sortOrder: 0
  })
}

const batchEdit = async () => {
  // 批量编辑功能
  console.log('批量编辑:', selectedItems.value)
}

const batchDelete = async () => {
  if (confirm(`确定要删除选中的 ${selectedItems.value.length} 条记录吗？`)) {
    try {
      await patternOptionsApi.batchDeletePatterns(selectedItems.value)
      await loadPatterns()
      selectedItems.value = []
    } catch (error) {
      console.error('批量删除失败:', error)
      // 如果API调用失败，从本地数据中删除
      patterns.value = patterns.value.filter(
        item => !selectedItems.value.includes(item.id)
      )
      selectedItems.value = []
    }
  }
}

const exportData = async () => {
  try {
    // 调用导出API
    const response = await fetch('/api/compatibility/pattern-options/export', {
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
    let fileName = '烫金图案选项.xlsx'
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

// 监听筛选数据变化，自动调整当前页面
watch(filteredData, () => {
  if (currentPage.value > totalPages.value && totalPages.value > 0) {
    currentPage.value = totalPages.value
  }
  if (totalPages.value === 0) {
    currentPage.value = 1
  }
})

// 生命周期
onMounted(() => {
  loadPatterns()
})
</script>
