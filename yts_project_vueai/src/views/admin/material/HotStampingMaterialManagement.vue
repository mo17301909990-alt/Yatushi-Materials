<template>
  <div class="min-h-screen bg-gray-50 p-6">
    <div class="max-w-7xl mx-auto">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">物料信息管理</h1>
        <p class="mt-2 text-gray-600">管理产品信息、规格信息和价格信息</p>
      </div>

      <!-- 搜索和筛选区域 -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-4">
          <!-- 关键词搜索 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">关键词搜索</label>
            <input
              v-model="searchForm.keyword"
              type="text"
              placeholder="搜索燙金紙系列、型号、物料编号、Leo Touch编号、SPNO、颜色等"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <!-- 烫金烫金纸性能类型筛选 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
            <input
              v-model="searchForm.hotStampingPaperType"
              type="text"
              placeholder="输入烫金纸性能类型进行筛选"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <!-- Leo Touch编号搜索 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">Leo Touch编号</label>
            <input
              v-model="searchForm.companyNumber"
              type="text"
              placeholder="搜索Leo Touch编号"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <!-- SPNO搜索 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">SPNO</label>
            <input
              v-model="searchForm.gpNo"
              type="text"
              placeholder="搜索SPNO"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <!-- 牌子筛选 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">牌子</label>
            <input
              v-model="searchForm.paizi"
              type="text"
              placeholder="搜索牌子"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <!-- 颜色筛选 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">颜色</label>
            <input
              v-model="searchForm.color"
              type="text"
              placeholder="搜索颜色"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          </div>

        <!-- 操作按钮区域 -->
        <div class="mt-6 pt-4 border-t border-gray-200">
          <div class="flex items-center gap-3 flex-wrap">
            <button
              @click="searchProducts"
              class="inline-flex items-center px-5 py-2.5 bg-blue-600 text-white text-sm font-medium rounded-lg shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-all duration-200"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
              </svg>
              搜索
            </button>
            <button
              @click="resetSearch"
              class="inline-flex items-center px-5 py-2.5 bg-white text-gray-700 text-sm font-medium rounded-lg shadow-sm border border-gray-300 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 transition-all duration-200"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
              </svg>
              重置
            </button>
            <button
              @click="showAddDialog = true"
              class="inline-flex items-center px-5 py-2.5 bg-green-600 text-white text-sm font-medium rounded-lg shadow-sm hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2 transition-all duration-200"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
              添加产品
            </button>
            <button
              @click="openBatchEditDialog"
              :disabled="selectedProductIds.length === 0"
              class="inline-flex items-center px-5 py-2.5 bg-yellow-600 text-white text-sm font-medium rounded-lg shadow-sm hover:bg-yellow-700 focus:outline-none focus:ring-2 focus:ring-yellow-500 focus:ring-offset-2 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"></path>
              </svg>
              批量修改 ({{ selectedProductIds.length }})
            </button>
            <button
              @click="handleBatchDelete"
              :disabled="selectedProductIds.length === 0"
              class="inline-flex items-center px-5 py-2.5 bg-red-600 text-white text-sm font-medium rounded-lg shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"></path>
              </svg>
              批量删除 ({{ selectedProductIds.length }})
            </button>
            <button
              @click="exportData"
              class="inline-flex items-center px-5 py-2.5 bg-indigo-600 text-white text-sm font-medium rounded-lg shadow-sm hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 transition-all duration-200"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
              </svg>
              导出数据
            </button>
            <button
              @click="showImportDialog = true"
              class="inline-flex items-center px-5 py-2.5 bg-purple-600 text-white text-sm font-medium rounded-lg shadow-sm hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-all duration-200"
            >
              <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path>
              </svg>
              导入数据
            </button>
          </div>
        </div>
      </div>

      <!-- 产品列表 -->
      <div class="bg-white rounded-lg shadow-sm border border-gray-200">
        <div class="px-4 py-3 border-b border-gray-200">
          <h2 class="text-base font-semibold text-gray-900">产品列表</h2>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  <input type="checkbox" v-model="selectAllProducts" @change="toggleSelectAll" class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                </th>
                <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  产品信息
                </th>
                <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  型号/物料编号
                </th>
                <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Leo Touch编号/SPNO
                </th>
                <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  规格信息
                </th>
                <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  价格
                </th>
                <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  烫金纸性能类型
                </th>
                <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  牌子/封度
                </th>
                <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  物料状态
                </th>
                <th class="px-3 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  操作
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="product in paginatedData" :key="product.id" class="hover:bg-gray-50">
                <td class="px-3 py-2 whitespace-nowrap">
                  <input type="checkbox" :value="product.id" v-model="selectedProductIds" class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded" />
                </td>
                <td class="px-3 py-2 whitespace-nowrap">
                  <div>
                    <div class="text-xs font-medium text-gray-900">{{ product.name }}</div>
                    <div class="text-xs text-gray-500" v-if="product.description">
                      {{ product.description.length > 50 ? product.description.substring(0, 50) + '...' : product.description }}
                    </div>
                  </div>
                </td>
                <td class="px-3 py-2 whitespace-nowrap">
                  <div class="text-xs text-gray-900">{{ product.modelNumber }}</div>
                  <div class="text-xs text-gray-500">{{ product.materialNumber }}</div>
                </td>
                <td class="px-3 py-2 whitespace-nowrap">
                  <div class="text-xs text-gray-900">{{ product.companyNumber || '-' }}</div>
                  <div class="text-xs text-gray-500">{{ product.gpNo || '-' }}</div>
                </td>
                <td class="px-3 py-2 whitespace-nowrap">
                  <div class="text-xs text-gray-900">{{ product.color || '-' }}</div>
                  <div class="text-xs text-gray-500">{{ product.size || '-' }}</div>
                  <div class="text-xs text-gray-500">{{ product.tightness || '-' }}</div>
                </td>
                <td class="px-3 py-2 whitespace-nowrap">
                  <div class="text-xs font-semibold text-green-600">
                    {{ product.price ? `¥${product.price}` : '-' }}
                  </div>
                </td>
                <td class="px-3 py-2 whitespace-nowrap text-xs text-gray-900">
                  {{ product.hotStampingPaperType || '-' }}
                </td>
                <td class="px-3 py-2 whitespace-nowrap">
                  <div class="text-xs text-gray-900">{{ product.paizi || '-' }}</div>
                  <div class="text-xs text-gray-500">{{ product.fengdu || '-' }}</div>
                </td>
                <td class="px-3 py-2 whitespace-nowrap">
                  <span
                    :class="[
                      'px-1.5 py-0.5 text-xs font-medium rounded-full',
                      product.status === '可用' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                    ]"
                  >
                    {{ product.status || '可用' }}
                  </span>
                </td>
                <td class="px-3 py-2 whitespace-nowrap text-xs font-medium space-x-1">
                  <button
                    @click="viewProductDetails(product)"
                    class="text-blue-600 hover:text-blue-900 hover:underline"
                  >
                    详情
                  </button>
                  <button
                    @click="editProduct(product)"
                    class="text-indigo-600 hover:text-indigo-900 hover:underline"
                  >
                    编辑
                  </button>
                  <button
                    @click="deleteProduct(product.id as number)"
                    class="text-red-600 hover:text-red-900 hover:underline"
                  >
                    删除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 空状态 -->
        <div v-if="filteredData.length === 0" class="text-center py-12">
          <div class="text-gray-500">暂无产品数据</div>
        </div>

        <!-- 分页 -->
        <div v-if="filteredData.length > 0" class="bg-white px-4 py-3 flex items-center justify-between border-t border-gray-200 sm:px-6">
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
                      ? 'z-10 bg-blue-50 border-blue-500 text-blue-600'
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

      <!-- 添加/编辑产品对话框 -->
      <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              {{ showAddDialog ? '添加产品' : '编辑产品' }}
            </h3>

            <form @submit.prevent="saveProduct" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <!-- 燙金紙系列 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">燙金紙系列 *</label>
                  <input
                    v-model="productForm.name"
                    type="text"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <!-- 型号 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">型号 *</label>
                  <input
                    v-model="productForm.modelNumber"
                    type="text"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <!-- 物料编号 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料编号 *</label>
                  <input
                    v-model="productForm.materialNumber"
                    type="text"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <!-- 烫金烫金纸性能类型 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">烫金纸性能类型</label>
                  <input
                    v-model="productForm.hotStampingPaperType"
                    type="text"
                    placeholder="请输入烫金纸性能类型"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                <!-- 牌子 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">牌子</label>
                  <input
                    v-model="productForm.paizi"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="请输入牌子"
                  />
                </div>

                <!-- 封度 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">封度</label>
                  <input
                    v-model="productForm.fengdu"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="请输入封度"
                  />
                </div>

                <!-- 单位 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">单位</label>
                  <input
                    v-model="productForm.danwei"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                    placeholder="请输入单位"
                  />
                </div>

                <!-- 是否常用 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">是否常用</label>
                  <select
                    v-model="productForm.isChangyong"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">请选择</option>
                    <option value="常用">常用</option>
                    <option value="不常用">不常用</option>
                    <option value="跟JOB">跟JOB</option>
                  </select>
                </div>

                <!-- 是否街货 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">是否街货</label>
                  <div class="flex items-center">
                    <input
                      v-model="productForm.isJiehuo"
                      type="checkbox"
                      class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                    />
                    <span class="ml-2 text-sm text-gray-700">是街货</span>
                  </div>
                </div>

                <!-- 物料状态 -->
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料状态 *</label>
                  <select
                    v-model="productForm.status"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="可用">可用</option>
                    <option value="废弃">废弃</option>
                  </select>
                </div>
              </div>

              <!-- 描述 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">说明</label>
                <textarea
                  v-model="productForm.description"
                  rows="3"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                ></textarea>
              </div>

              <!-- 概述 -->
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">概述</label>
                <textarea
                  v-model="productForm.gaishu"
                  rows="3"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                ></textarea>
              </div>

              <!-- Leo Touch 和 SPNO 信息 -->
              <div class="border-t pt-4">
                <h4 class="text-md font-semibold text-gray-900 mb-3">Leo Touch 信息</h4>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <!-- Leo Touch编号 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">Leo Touch编号</label>
                    <input
                      v-model="productForm.companyNumber"
                      type="text"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="请输入Leo Touch编号"
                    />
                  </div>

                  <!-- SPNO -->
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">SPNO</label>
                    <input
                      v-model="productForm.gpNo"
                      type="text"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="请输入SPNO"
                    />
                  </div>
                </div>
              </div>

              <!-- 规格信息 -->
              <div class="border-t pt-4">
                <h4 class="text-md font-semibold text-gray-900 mb-3">规格信息</h4>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <!-- 颜色 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">颜色</label>
                    <input
                      v-model="productForm.color"
                      type="text"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="请输入颜色"
                    />
                  </div>

                  <!-- 尺寸 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">尺寸</label>
                    <input
                      v-model="productForm.size"
                      type="text"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="请输入尺寸"
                    />
                  </div>

                  <!-- 松紧度 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">松紧度</label>
                    <input
                      v-model="productForm.tightness"
                      type="text"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="请输入松紧度"
                    />
                  </div>

                  <!-- 性能 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">性能</label>
                    <input
                      v-model="productForm.performance"
                      type="text"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="请输入性能"
                    />
                  </div>

                  <!-- 温度范围 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">温度范围</label>
                    <input
                      v-model="productForm.temperatureRange"
                      type="text"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="请输入温度范围"
                    />
                  </div>
                </div>
              </div>

              <!-- 价格信息 -->
              <div class="border-t pt-4">
                <h4 class="text-md font-semibold text-gray-900 mb-3">价格信息</h4>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <!-- 价格 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-700 mb-1">价格</label>
                    <input
                      v-model="productForm.price"
                      type="number"
                      step="0.01"
                      class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                      placeholder="请输入价格"
                    />
                  </div>
                </div>
              </div>

              <!-- 按钮 -->
              <div class="flex justify-end space-x-3 pt-4">
                <button
                  type="button"
                  @click="closeDialog"
                  class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-500"
                >
                  取消
                </button>
                <button
                  type="submit"
                  class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  {{ showAddDialog ? '添加' : '保存' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 产品详情对话框 -->
      <div v-if="showDetailsDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-6xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex justify-between items-center mb-4">
              <h3 class="text-lg font-medium text-gray-900">产品详情</h3>
              <button
                @click="showDetailsDialog = false"
                class="text-gray-400 hover:text-gray-600"
              >
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <div v-if="selectedProduct" class="space-y-6">
              <!-- 基本信息 -->
              <div class="bg-gray-50 p-4 rounded-lg">
                <h4 class="text-md font-semibold text-gray-900 mb-3">基本信息</h4>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div>
                    <span class="text-sm font-medium text-gray-500">燙金紙系列:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.name }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">型号:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.modelNumber }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">物料编号:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.materialNumber }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">Leo Touch编号:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.companyNumber || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">SPNO:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.gpNo || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">价格:</span>
                    <span class="ml-2 text-sm font-semibold text-green-600">{{ selectedProduct.price ? `¥${selectedProduct.price}` : '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">烫金纸性能类型:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.hotStampingPaperType || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">牌子:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.paizi || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">封度:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.fengdu || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">单位:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.danwei || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">是否常用:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.isChangyong || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">是否街货:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.isJiehuo ? '是' : '否' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">物料状态:</span>
                    <span
                      :class="[
                        'ml-2 px-2 py-1 text-xs font-medium rounded-full',
                        (selectedProduct.status || '可用') === '可用' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                      ]"
                    >
                      {{ selectedProduct.status || '可用' }}
                    </span>
                  </div>
                </div>
                <div v-if="selectedProduct.description" class="mt-3">
                  <span class="text-sm font-medium text-gray-500">描述:</span>
                  <p class="mt-1 text-sm text-gray-900">{{ selectedProduct.description }}</p>
                </div>
                <div v-if="selectedProduct.gaishu" class="mt-3">
                  <span class="text-sm font-medium text-gray-500">概述:</span>
                  <p class="mt-1 text-sm text-gray-900">{{ selectedProduct.gaishu }}</p>
                </div>
              </div>

              <!-- 规格信息 -->
              <div class="bg-gray-50 p-4 rounded-lg">
                <h4 class="text-md font-semibold text-gray-900 mb-3">规格信息</h4>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div>
                    <span class="text-sm font-medium text-gray-500">颜色:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.color || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">尺寸:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.size || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">松紧度:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.tightness || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">性能:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.performance || '-' }}</span>
                  </div>
                  <div>
                    <span class="text-sm font-medium text-gray-500">温度范围:</span>
                    <span class="ml-2 text-sm text-gray-900">{{ selectedProduct.temperatureRange || '-' }}</span>
                  </div>
                </div>
              </div>


              <!-- 价格信息 -->
              <div class="bg-gray-50 p-4 rounded-lg">
                <h4 class="text-md font-semibold text-gray-900 mb-3">价格信息</h4>
                <div v-if="selectedProduct.pricingList && selectedProduct.pricingList.length > 0" class="space-y-2">
                  <div
                    v-for="pricing in selectedProduct.pricingList"
                    :key="pricing.id"
                    class="bg-white p-3 rounded border border-gray-200"
                  >
                    <div class="text-sm">
                      <span class="font-medium">价格:</span> 
                      <span class="text-lg font-semibold text-green-600">¥{{ pricing.price || 0 }}</span>
                    </div>
                  </div>
                </div>
                <div v-else class="text-gray-500 text-sm">暂无价格信息</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 批量修改对话框 -->
  <div v-if="showBatchEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full" style="z-index: 9999; position: fixed !important;">
    <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-5xl shadow-lg rounded-md bg-white my-8">
      <div class="mt-3">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-lg font-medium text-gray-900">批量修改产品 (已选择 {{ selectedProductIds.length }} 条)</h3>
          <button @click="closeBatchEditDialog" class="text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </button>
        </div>
        <form @submit.prevent="handleBatchUpdate" class="space-y-6">
          <div class="bg-yellow-50 border-l-4 border-yellow-400 p-4 mb-4">
            <p class="text-sm text-yellow-800">
              <strong>提示：</strong>只填写需要批量修改的字段，留空的字段将不会被修改。支持同时修改多个字段。
            </p>
          </div>
          
          <!-- 基本信息 -->
          <div class="border-b pb-4">
            <h4 class="text-md font-semibold text-gray-900 mb-3">基本信息</h4>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">燙金紙系列</label>
                <input
                  v-model="batchUpdateForm.name"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">烫金纸性能类型</label>
                <input
                  v-model="batchUpdateForm.hotStampingPaperType"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">物料状态</label>
                <select
                  v-model="batchUpdateForm.status"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                >
                  <option value="">不修改</option>
                  <option value="可用">可用</option>
                  <option value="废弃">废弃</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">牌子</label>
                <input
                  v-model="batchUpdateForm.paizi"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">封度</label>
                <input
                  v-model="batchUpdateForm.fengdu"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">单位</label>
                <input
                  v-model="batchUpdateForm.danwei"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">是否常用</label>
                <select
                  v-model="batchUpdateForm.isChangyong"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                >
                  <option value="">不修改</option>
                  <option value="常用">常用</option>
                  <option value="不常用">不常用</option>
                  <option value="跟JOB">跟JOB</option>
                </select>
              </div>
              <div class="flex items-center pt-6">
                <input
                  v-model="batchUpdateForm.isJiehuo"
                  type="checkbox"
                  :true-value="true"
                  :false-value="null"
                  class="h-4 w-4 text-yellow-600 focus:ring-yellow-500 border-gray-300 rounded"
                />
                <label class="ml-2 block text-sm text-gray-900">是否街货（勾选为是，不勾选为不修改）</label>
              </div>
            </div>
          </div>

          <!-- Leo Touch 信息 -->
          <div class="border-b pb-4">
            <h4 class="text-md font-semibold text-gray-900 mb-3">Leo Touch 信息</h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">Leo Touch编号</label>
                <input
                  v-model="batchUpdateForm.companyNumber"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">SPNO</label>
                <input
                  v-model="batchUpdateForm.gpNo"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
            </div>
          </div>

          <!-- 规格信息 -->
          <div class="border-b pb-4">
            <h4 class="text-md font-semibold text-gray-900 mb-3">规格信息</h4>
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">颜色</label>
                <input
                  v-model="batchUpdateForm.color"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">尺寸</label>
                <input
                  v-model="batchUpdateForm.size"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">松紧度</label>
                <input
                  v-model="batchUpdateForm.tightness"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">性能</label>
                <input
                  v-model="batchUpdateForm.performance"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">温度范围</label>
                <input
                  v-model="batchUpdateForm.temperatureRange"
                  type="text"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
            </div>
          </div>

          <!-- 价格信息 -->
          <div class="border-b pb-4">
            <h4 class="text-md font-semibold text-gray-900 mb-3">价格信息</h4>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">价格</label>
                <input
                  v-model.number="batchUpdateForm.price"
                  type="number"
                  step="0.01"
                  min="0"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                />
              </div>
            </div>
          </div>

          <!-- 描述信息 -->
          <div class="pb-4">
            <h4 class="text-md font-semibold text-gray-900 mb-3">描述信息</h4>
            <div class="grid grid-cols-1 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">说明</label>
                <textarea
                  v-model="batchUpdateForm.description"
                  rows="3"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                ></textarea>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-2">概述</label>
                <textarea
                  v-model="batchUpdateForm.gaishu"
                  rows="3"
                  placeholder="留空则不修改"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-yellow-500"
                ></textarea>
              </div>
            </div>
          </div>

          <div class="flex justify-end space-x-3 pt-4 border-t">
            <button type="button" @click="closeBatchEditDialog" class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50">
              取消
            </button>
            <button type="submit" class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-yellow-600 hover:bg-yellow-700">
              确认修改
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>

  <!-- 导入对话框 -->
  <div v-if="showImportDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
    <div class="bg-white rounded-lg shadow-xl max-w-md w-full mx-4">
      <div class="p-6">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">导入产品数据</h3>
        
        <!-- 文件上传 -->
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">选择Excel文件</label>
          <input
            ref="fileInput"
            type="file"
            accept=".xlsx,.xls"
            @change="handleFileSelect"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <p class="text-xs text-gray-500 mt-1">支持 .xlsx 和 .xls 格式，文件大小不超过10MB</p>
        </div>

        <!-- 导入配置 -->
        <div class="mb-4">
          <label class="block text-sm font-medium text-gray-700 mb-2">导入模式</label>
          <select
            v-model="importConfig.importMode"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="SMART">智能模式（推荐）</option>
            <option value="SKIP">跳过冲突数据</option>
            <option value="OVERWRITE">覆盖现有数据</option>
            <option value="CREATE_NEW">创建新记录</option>
          </select>
          <p class="text-xs text-gray-500 mt-1">
            智能模式：有ID且存在则更新，无ID或ID不存在则新增
          </p>
        </div>

        <!-- 导入进度 -->
        <div v-if="isImporting" class="mb-4">
          <div class="bg-blue-50 border border-blue-200 rounded-md p-3">
            <div class="flex items-center">
              <div class="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-600 mr-2"></div>
              <span class="text-sm text-blue-600">正在导入数据...</span>
            </div>
          </div>
        </div>

        <!-- 导入结果 -->
        <div v-if="importResult" class="mb-4">
          <div :class="importResult.success ? 'bg-green-50 border-green-200' : 'bg-yellow-50 border-yellow-200'" 
               class="border rounded-md p-3">
            <div class="flex items-center">
              <svg v-if="importResult.success" class="w-5 h-5 text-green-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"></path>
              </svg>
              <svg v-else class="w-5 h-5 text-yellow-600 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"></path>
              </svg>
              <span :class="importResult.success ? 'text-green-600' : 'text-yellow-600'" class="text-sm font-medium">
                {{ importResult.message }}
              </span>
            </div>
            <div v-if="importResult.totalCount" class="mt-2 text-sm text-gray-600">
              总记录数: {{ importResult.totalCount }} | 
              新增: {{ importResult.createdCount || 0 }} | 
              更新: {{ importResult.updatedCount || 0 }} | 
              失败: {{ importResult.skippedCount || 0 }}
            </div>
          </div>
        </div>

        <!-- 按钮组 -->
        <div class="flex justify-end space-x-3">
          <button
            @click="closeImportDialog"
            class="px-4 py-2 text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200 focus:outline-none focus:ring-2 focus:ring-gray-500"
          >
            取消
          </button>
          <button
            v-if="!importResult"
            @click="handleImport"
            :disabled="!selectedFile || isImporting"
            class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ isImporting ? '导入中...' : '开始导入' }}
          </button>
          <button
            v-else
            @click="closeImportDialog"
            class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500"
          >
            关闭
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { productApi, specificationApi, pricingApi, completeProductInfoApi, type Product, type Specification, type Pricing, type ProductOptions, type CompleteProductInfo } from '../../../api/modules/product'

// 响应式数据
const products = ref<CompleteProductInfo[]>([])

// 导入相关数据
const showImportDialog = ref(false)
const selectedFile = ref<File | null>(null)
const isImporting = ref(false)
const importResult = ref<any>(null)
const importConfig = reactive({
  importMode: 'SMART'
})
const productOptions = ref<ProductOptions>({
  hotStampingPaperTypes: [],
  danweiOptions: [],
  paiziOptions: [],
  fengduOptions: [],
  colorOptions: [],
  sizeOptions: [],
  tightnessOptions: [],
  temperatureRangeOptions: []
})

// 搜索表单
const searchForm = reactive({
  keyword: '',
  hotStampingPaperType: '',
  paizi: '',
  companyNumber: '',
  gpNo: '',
  color: ''
})

// 产品表单
const productForm = reactive({
  id: undefined as number | undefined,
  name: '',
  modelNumber: '',
  materialNumber: '',
  hotStampingPaperType: '',
  description: '',
  fengdu: '',
  paizi: '',
  gaishu: '',
  danwei: '',
  isChangyong: '',
  isJiehuo: false,
  status: '可用' as '可用' | '废弃',  // 物料状态标记
  // Leo Touch 信息
  companyNumber: '',
  gpNo: '',
  // 规格信息
  color: '',
  size: '',
  tightness: '',
  performance: '',
  temperatureRange: '',
  // 价格信息
  price: undefined as number | undefined
})

// 对话框状态
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showDetailsDialog = ref(false)
const showBatchEditDialog = ref(false)
const selectedProduct = ref<CompleteProductInfo | null>(null)

// 批量操作相关
const selectedProductIds = ref<number[]>([])
const selectAllProducts = ref(false)

// 批量修改表单
const batchUpdateForm = reactive({
  // 基本信息
  name: '', // 燙金紙系列
  hotStampingPaperType: '',
  status: '',
  paizi: '',
  fengdu: '',
  danwei: '',
  isChangyong: '',
  isJiehuo: null as boolean | null,
  // Leo Touch 信息
  companyNumber: '',
  gpNo: '',
  // 规格信息
  color: '',
  size: '',
  tightness: '',
  performance: '',
  temperatureRange: '',
  // 价格信息
  price: undefined as number | undefined,
  // 描述信息
  description: '',
  gaishu: ''
})

// 分页状态
const currentPage = ref(1)
const pageSize = ref(10)

// 计算属性
const filteredData = computed(() => {
  let data = products.value

  // 关键词搜索
  if (searchForm.keyword) {
    const keyword = searchForm.keyword.toLowerCase()
    data = data.filter(item => 
      (item.name?.toLowerCase() || '').includes(keyword) ||
      (item.modelNumber?.toLowerCase() || '').includes(keyword) ||
      (item.materialNumber?.toLowerCase() || '').includes(keyword) ||
      (item.companyNumber?.toLowerCase() || '').includes(keyword) ||
      (item.gpNo?.toLowerCase() || '').includes(keyword) ||
      (item.color?.toLowerCase() || '').includes(keyword)
    )
  }

  // 烫金烫金纸性能类型筛选
  if (searchForm.hotStampingPaperType) {
    data = data.filter(item => 
      item.hotStampingPaperType && 
      item.hotStampingPaperType.toLowerCase().includes(searchForm.hotStampingPaperType.toLowerCase())
    )
  }

  // 牌子筛选（模糊搜索）
  if (searchForm.paizi) {
    data = data.filter(item => 
      item.paizi && item.paizi.toLowerCase().includes(searchForm.paizi.toLowerCase())
    )
  }

  // Leo Touch编号筛选
  if (searchForm.companyNumber) {
    data = data.filter(item => item.companyNumber?.includes(searchForm.companyNumber))
  }

  // SPNO筛选
  if (searchForm.gpNo) {
    data = data.filter(item => item.gpNo?.includes(searchForm.gpNo))
  }

  // 颜色筛选
  if (searchForm.color) {
    data = data.filter(item => item.color?.includes(searchForm.color))
  }

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

// 加载产品列表
const loadProducts = async () => {
  try {
    products.value = await completeProductInfoApi.getAllCompleteProductInfo()
  } catch (error) {
    console.error('加载产品列表失败:', error)
    alert('加载产品列表失败')
  }
}

// 加载选项数据
const loadOptions = async () => {
  try {
    productOptions.value = await productApi.getProductOptions()
  } catch (error) {
    console.error('加载选项数据失败:', error)
  }
}

// 搜索产品
const searchProducts = async () => {
  try {
    if (searchForm.keyword) {
      // 关键词搜索，尝试多个字段
      const results = await Promise.all([
        completeProductInfoApi.searchCompleteProductInfoByMaterialNumber(searchForm.keyword),
        completeProductInfoApi.searchCompleteProductInfoByModelNumber(searchForm.keyword),
        completeProductInfoApi.searchCompleteProductInfoByCompanyNumber(searchForm.keyword),
        completeProductInfoApi.searchCompleteProductInfoByGpNo(searchForm.keyword)
      ])
      // 合并结果并去重
      const allResults = results.reduce((acc, curr) => acc.concat(curr), [])
      const uniqueResults = allResults.filter((item, index, self) => 
        index === self.findIndex(t => t.id === item.id)
      )
      products.value = uniqueResults
    } else if (searchForm.hotStampingPaperType) {
      products.value = await completeProductInfoApi.searchCompleteProductInfoByHotStampingPaperType(searchForm.hotStampingPaperType)
    } else if (searchForm.companyNumber) {
      products.value = await completeProductInfoApi.searchCompleteProductInfoByCompanyNumber(searchForm.companyNumber)
    } else if (searchForm.gpNo) {
      products.value = await completeProductInfoApi.searchCompleteProductInfoByGpNo(searchForm.gpNo)
    } else {
      await loadProducts()
    }
    // 搜索后重置到第一页
    currentPage.value = 1
  } catch (error) {
    console.error('搜索产品失败:', error)
    alert('搜索产品失败')
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.keyword = ''
  searchForm.hotStampingPaperType = ''
  searchForm.paizi = ''
  searchForm.companyNumber = ''
  searchForm.gpNo = ''
  searchForm.color = ''
  currentPage.value = 1
  loadProducts()
}

// 查看产品详情
const viewProductDetails = async (product: CompleteProductInfo) => {
  try {
    // 获取完整的产品信息
    const fullProduct = await completeProductInfoApi.getCompleteProductInfoById(product.id as number)
    const specifications = await specificationApi.getSpecificationsByProductId(product.id as number)
    const pricingList = await pricingApi.getPricingByProductId(product.id as number)
    
    selectedProduct.value = {
      ...fullProduct,
      specifications,
      pricingList
    }
    showDetailsDialog.value = true
  } catch (error) {
    console.error('获取产品详情失败:', error)
    alert('获取产品详情失败')
  }
}

// 编辑产品
const editProduct = (product: CompleteProductInfo) => {
  Object.assign(productForm, product)
  showEditDialog.value = true
}

// 删除产品
const deleteProduct = async (id: number) => {
  if (confirm('确定要删除这个产品吗？')) {
    try {
      await productApi.deleteProduct(id)
      await loadProducts()
      alert('删除成功')
    } catch (error) {
      console.error('删除产品失败:', error)
      alert('删除产品失败')
    }
  }
}

// 保存产品
const saveProduct = async () => {
  try {
    if (showAddDialog.value) {
      await completeProductInfoApi.saveCompleteProductInfo(productForm)
      alert('添加成功')
    } else {
      await completeProductInfoApi.updateCompleteProductInfo(productForm.id as number, productForm)
      alert('更新成功')
    }
    
    closeDialog()
    await loadProducts()
    // 保存后重置到第一页
    currentPage.value = 1
  } catch (error) {
    console.error('保存产品失败:', error)
    alert('保存产品失败')
  }
}

// 关闭对话框
const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(productForm, {
    id: undefined,
    name: '',
    modelNumber: '',
    materialNumber: '',
    hotStampingPaperType: '',
    description: '',
    fengdu: '',
    paizi: '',
    gaishu: '',
    danwei: '',
    isChangyong: '',
    isJiehuo: false,
    status: '可用' as '可用' | '废弃',
    // Leo Touch 信息
    companyNumber: '',
    gpNo: '',
    // 规格信息
    color: '',
    size: '',
    tightness: '',
    performance: '',
    temperatureRange: '',
    // 价格信息
    price: undefined
  })
}

// 添加规格
const addSpecification = () => {
  // TODO: 实现添加规格功能
  alert('添加规格功能待实现')
}

// 编辑规格
const editSpecification = (_spec: Specification) => {
  // TODO: 实现编辑规格功能
  alert('编辑规格功能待实现')
}

// 删除规格
const deleteSpecification = async (id: number) => {
  if (confirm('确定要删除这个规格吗？')) {
    try {
      await specificationApi.deleteSpecification(id)
      // 重新加载产品详情
      if (selectedProduct.value) {
        await viewProductDetails(selectedProduct.value)
      }
      alert('删除成功')
    } catch (error) {
      console.error('删除规格失败:', error)
      alert('删除规格失败')
    }
  }
}

// 添加价格
const addPricing = () => {
  // TODO: 实现添加价格功能
  alert('添加价格功能待实现')
}

// 编辑价格
const editPricing = (_pricing: Pricing) => {
  // TODO: 实现编辑价格功能
  alert('编辑价格功能待实现')
}

// 删除价格
const deletePricing = async (id: number) => {
  if (confirm('确定要删除这个价格吗？')) {
    try {
      await pricingApi.deletePricing(id)
      // 重新加载产品详情
      if (selectedProduct.value) {
        await viewProductDetails(selectedProduct.value)
      }
      alert('删除成功')
    } catch (error) {
      console.error('删除价格失败:', error)
      alert('删除价格失败')
    }
  }
}

// 导出数据
const exportData = async () => {
  try {
    // 调用导出API
    const response = await fetch('/api/api/product-management/complete/export', {
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
    let fileName = '物料信息管理.xlsx'
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
const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files[0]) {
    selectedFile.value = target.files[0]
    importResult.value = null
  }
}

const handleImport = async () => {
  if (!selectedFile.value) {
    alert('请先选择要导入的文件')
    return
  }

  try {
    isImporting.value = true
    importResult.value = null

    // 构建表单数据
    const formData = new FormData()
    formData.append('file', selectedFile.value)
    formData.append('importMode', importConfig.importMode)

    // 调用导入API
    const response = await fetch('/api/api/product-import/complete', {
      method: 'POST',
      body: formData,
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    if (!response.ok) {
      throw new Error('导入失败')
    }

    const result = await response.json()
    importResult.value = result

    // 重新加载数据
    await loadProducts()
    
    // 导入结果会通过 importResult 在对话框中显示

  } catch (error: any) {
    console.error('导入失败:', error)
    alert('导入失败: ' + error.message)
  } finally {
    isImporting.value = false
  }
}

const closeImportDialog = () => {
  showImportDialog.value = false
  selectedFile.value = null
  importResult.value = null
  importConfig.importMode = 'SMART'
}

// 组件挂载时加载数据
// 全选/取消全选
const toggleSelectAll = () => {
  if (selectAllProducts.value) {
    selectedProductIds.value = paginatedData.value
      .map(p => p.id)
      .filter((id): id is number => id !== undefined) as number[]
  } else {
    selectedProductIds.value = []
  }
}

// 监听选中项变化，更新全选状态
watch(selectedProductIds, (newVal) => {
  const pageIds = paginatedData.value
    .map(p => p.id)
    .filter((id): id is number => id !== undefined) as number[]
  selectAllProducts.value = pageIds.length > 0 && 
    pageIds.every(id => newVal.includes(id))
}, { deep: true })

// 打开批量修改对话框
const openBatchEditDialog = async () => {
  if (selectedProductIds.value.length === 0) {
    alert('请先选择要修改的产品')
    return
  }
  
  // 关闭其他对话框
  showAddDialog.value = false
  showEditDialog.value = false
  showDetailsDialog.value = false
  showImportDialog.value = false
  
  // 确保选项数据已加载
  if (!productOptions.value.hotStampingPaperTypes || productOptions.value.hotStampingPaperTypes.length === 0) {
    await loadOptions()
  }
  
  // 重置表单
  Object.assign(batchUpdateForm, {
    name: '',
    hotStampingPaperType: '',
    status: '',
    paizi: '',
    fengdu: '',
    danwei: '',
    isChangyong: '',
    isJiehuo: null,
    companyNumber: '',
    gpNo: '',
    color: '',
    size: '',
    tightness: '',
    performance: '',
    temperatureRange: '',
    price: undefined,
    description: '',
    gaishu: ''
  })
  
  showBatchEditDialog.value = true
  await nextTick()
}

// 关闭批量修改对话框
const closeBatchEditDialog = () => {
  showBatchEditDialog.value = false
  Object.assign(batchUpdateForm, {
    name: '',
    hotStampingPaperType: '',
    status: '',
    paizi: '',
    fengdu: '',
    danwei: '',
    isChangyong: '',
    isJiehuo: null,
    companyNumber: '',
    gpNo: '',
    color: '',
    size: '',
    tightness: '',
    performance: '',
    temperatureRange: '',
    price: undefined,
    description: '',
    gaishu: ''
  })
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedProductIds.value.length === 0) {
    alert('请先选择要删除的产品')
    return
  }
  
  if (!confirm(`确定要删除选中的 ${selectedProductIds.value.length} 条产品吗？此操作不可恢复！`)) {
    return
  }
  
  try {
    const result = await productApi.batchDeleteProducts(selectedProductIds.value)
    if (result.success) {
      alert(result.message)
      selectedProductIds.value = []
      selectAllProducts.value = false
      await loadProducts()
    } else {
      alert('批量删除失败: ' + result.message)
    }
  } catch (error) {
    console.error('批量删除失败:', error)
    alert('批量删除失败: ' + (error as Error).message)
  }
}

// 批量更新
const handleBatchUpdate = async () => {
  if (selectedProductIds.value.length === 0) {
    alert('请先选择要修改的产品')
    return
  }
  
  // 构建更新字段对象（只包含非空字段）
  const updateFields: Record<string, any> = {}
  
  // 基本信息
  if (batchUpdateForm.name) {
    updateFields.name = batchUpdateForm.name
  }
  if (batchUpdateForm.hotStampingPaperType) {
    updateFields.hotStampingPaperType = batchUpdateForm.hotStampingPaperType
  }
  if (batchUpdateForm.status) {
    updateFields.status = batchUpdateForm.status
  }
  if (batchUpdateForm.paizi) {
    updateFields.paizi = batchUpdateForm.paizi
  }
  if (batchUpdateForm.fengdu) {
    updateFields.fengdu = batchUpdateForm.fengdu
  }
  if (batchUpdateForm.danwei) {
    updateFields.danwei = batchUpdateForm.danwei
  }
  if (batchUpdateForm.isChangyong) {
    updateFields.isChangyong = batchUpdateForm.isChangyong
  }
  if (batchUpdateForm.isJiehuo !== null) {
    updateFields.isJiehuo = batchUpdateForm.isJiehuo
  }
  
  // Leo Touch 信息
  if (batchUpdateForm.companyNumber) {
    updateFields.companyNumber = batchUpdateForm.companyNumber
  }
  if (batchUpdateForm.gpNo) {
    updateFields.gpNo = batchUpdateForm.gpNo
  }
  
  // 规格信息
  if (batchUpdateForm.color) {
    updateFields.color = batchUpdateForm.color
  }
  if (batchUpdateForm.size) {
    updateFields.size = batchUpdateForm.size
  }
  if (batchUpdateForm.tightness) {
    updateFields.tightness = batchUpdateForm.tightness
  }
  if (batchUpdateForm.performance) {
    updateFields.performance = batchUpdateForm.performance
  }
  if (batchUpdateForm.temperatureRange) {
    updateFields.temperatureRange = batchUpdateForm.temperatureRange
  }
  
  // 价格信息
  if (batchUpdateForm.price !== undefined && batchUpdateForm.price !== null) {
    updateFields.price = batchUpdateForm.price
  }
  
  // 描述信息
  if (batchUpdateForm.description) {
    updateFields.description = batchUpdateForm.description
  }
  if (batchUpdateForm.gaishu) {
    updateFields.gaishu = batchUpdateForm.gaishu
  }
  
  // 检查是否有要更新的字段
  if (Object.keys(updateFields).length === 0) {
    alert('请至少选择一个要修改的字段')
    return
  }
  
  if (!confirm(`确定要批量修改选中的 ${selectedProductIds.value.length} 条产品吗？`)) {
    return
  }
  
  try {
    const result = await productApi.batchUpdateProducts(selectedProductIds.value, updateFields)
    if (result.success) {
      alert(result.message)
      closeBatchEditDialog()
      selectedProductIds.value = []
      selectAllProducts.value = false
      await loadProducts()
    } else {
      alert('批量修改失败: ' + result.message)
    }
  } catch (error) {
    console.error('批量修改失败:', error)
    alert('批量修改失败: ' + (error as Error).message)
  }
}

onMounted(() => {
  loadProducts()
  loadOptions()
})
</script>