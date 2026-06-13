<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <AdminBackToPanel />
          <div>
            <h1 class="text-3xl font-bold text-gray-900">燙金物料匹配規則管理</h1>
          </div>
        </div>
      </div>

      <!-- 管理模組網格 -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <!-- 燙金物料信息管理 -->
        <div
          v-if="showModule('material:hot-stamping-material:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">燙金物料信息</h3>
              <p class="text-sm text-gray-500">管理燙金物料基礎信息</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/hot-stamping-material"
              class="w-full bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 適用界面(前工序)管理 -->
        <div
          v-if="showModule('material:pre-processing-interface:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">適用界面(前工序)</h3>
              <p class="text-sm text-gray-500">管理前工序適用界面配置</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/pre-processing-interface"
              class="w-full bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 產品類型管理 -->
        <div
          v-if="showModule('material:product-type:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 7h.01M7 3h5c.512 0 1.024.195 1.414.586l7 7a2 2 0 010 2.828l-7 7a2 2 0 01-2.828 0l-7-7A1.994 1.994 0 013 12V7a4 4 0 014-4z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">產品類型管理</h3>
              <p class="text-sm text-gray-500">管理產品類型選項</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/product-type"
              class="w-full bg-purple-600 text-white px-4 py-2 rounded-md hover:bg-purple-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 6.燙金圖案(X)(耐磨燙金紙選用)管理 -->
        <div
          v-if="showModule('material:hot-stamping-pattern:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">燙金圖案(X)(耐磨燙金紙選用)</h3>
              <p class="text-sm text-gray-500">管理燙金圖案基礎信息</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/hot-stamping-pattern"
              class="w-full bg-yellow-600 text-white px-4 py-2 rounded-md hover:bg-yellow-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 燙金類型管理 -->
        <div
          v-if="showModule('material:hot-stamping-type:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-red-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11.049 2.927c.3-.921 1.603-.921 1.902 0l1.519 4.674a1 1 0 00.95.69h4.915c.969 0 1.371 1.24.588 1.81l-3.976 2.888a1 1 0 00-.363 1.118l1.518 4.674c.3.922-.755 1.688-1.538 1.118l-3.976-2.888a1 1 0 00-1.176 0l-3.976 2.888c-.783.57-1.838-.197-1.538-1.118l1.518-4.674a1 1 0 00-.363-1.118l-3.976-2.888c-.784-.57-.38-1.81.588-1.81h4.914a1 1 0 00.951-.69l1.519-4.674z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">燙金類型管理</h3>
              <p class="text-sm text-gray-500">管理燙金類型選項</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/hot-stamping-type"
              class="w-full bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 適用界面管理 -->
        <div
          v-if="showModule('material:applicable-interface:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-indigo-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">適用界面管理</h3>
              <p class="text-sm text-gray-500">管理適用界面配置（含系列優先級：SK、L817）</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/applicable-interface"
              class="w-full bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 烫金图案管理 -->
        <div
          v-if="showModule('material:hot-stamping-pattern-area:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-pink-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-pink-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">燙金圖案(常用界面選用)管理</h3>
              <p class="text-sm text-gray-500">管理燙金圖案(常用界面選用)配置</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/hot-stamping-pattern-area"
              class="w-full bg-pink-600 text-white px-4 py-2 rounded-md hover:bg-pink-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 烫后加工管理 -->
        <div
          v-if="showModule('material:post-processing:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-teal-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-teal-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z"></path>
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">燙后加工管理</h3>
              <p class="text-sm text-gray-500">管理燙后加工配置</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/post-processing"
              class="w-full bg-teal-600 text-white px-4 py-2 rounded-md hover:bg-teal-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 耐磨金纸映射管理 -->
        <div
          v-if="showModule('material:smart-compatibility:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow border-2 border-indigo-200"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-indigo-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-indigo-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">耐磨金纸映射管理</h3>
              <p class="text-sm text-gray-500">统一管理燙金工艺兼容性規則</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/smart-compatibility"
              class="w-full bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 矩阵表导出导入 -->
        <div
          v-if="showModule('material:matrix-export-import:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow border-2 border-cyan-200"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-cyan-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-cyan-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 10h16M4 14h16M4 18h16"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">矩阵表导出导入</h3>
              <p class="text-sm text-gray-500">按指引書文件名導出/導入矩陣表（Excel、Word）</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/matrix-export-import"
              class="w-full bg-cyan-600 text-white px-4 py-2 rounded-md hover:bg-cyan-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 注意事项字典管理 -->
        <div
          v-if="showModule('material:notice-dictionary:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow border-2 border-orange-200"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">注意事項字典管理</h3>
              <p class="text-sm text-gray-500">管理所有規則匹配時需要顯示的注意事項</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/material/notice-dictionary"
              class="w-full bg-orange-600 text-white px-4 py-2 rounded-md hover:bg-orange-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- ========== 參考資料字典管理 ========== -->
        <!-- 纸张面管理 -->
        <div
          v-if="showModule('material:reference-paper-surface:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-stone-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-stone-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">紙張面管理</h3>
              <p class="text-sm text-gray-500">管理參考資料紙張面選項</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/reference/paper-surface"
              class="w-full bg-stone-600 text-white px-4 py-2 rounded-md hover:bg-stone-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 油墨面管理 -->
        <div
          v-if="showModule('material:reference-ink-surface:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-amber-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-amber-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">油墨面管理</h3>
              <p class="text-sm text-gray-500">管理參考資料油墨面選項</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/reference/ink-surface"
              class="w-full bg-amber-600 text-white px-4 py-2 rounded-md hover:bg-amber-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 涂层面管理 -->
        <div
          v-if="showModule('material:reference-coating-surface:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-cyan-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-cyan-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">涂層面管理</h3>
              <p class="text-sm text-gray-500">管理參考資料涂層面選項</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/reference/coating-surface"
              class="w-full bg-cyan-600 text-white px-4 py-2 rounded-md hover:bg-cyan-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 写字功能管理 -->
        <div
          v-if="showModule('material:reference-writing-function:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-lime-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-lime-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">寫字功能管理</h3>
              <p class="text-sm text-gray-500">管理參考資料寫字功能選項</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/reference/writing-function"
              class="w-full bg-lime-600 text-white px-4 py-2 rounded-md hover:bg-lime-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 产品家族管理 -->
        <div
          v-if="showModule('material:reference-product-family:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-violet-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-violet-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">產品家族管理</h3>
              <p class="text-sm text-gray-500">管理參考資料產品家族選項</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/reference/product-family"
              class="w-full bg-violet-600 text-white px-4 py-2 rounded-md hover:bg-violet-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- ========== 哑光UV油標準書管理 ========== -->
        <!-- 哑光UV油产品管理 -->
        <div
          v-if="showModule('material:yaguang-uv-oil:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow border-2 border-blue-200"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">啞光UV油標準書</h3>
              <p class="text-sm text-gray-500">管理啞光UV油產品資料</p>
            </div>
          </div>
          <div class="grid grid-cols-2 gap-2">
            <router-link
              to="/admin/material/yaguang-uv-oil/products"
              class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors text-center block text-sm"
            >
              產品管理
            </router-link>
            <router-link
              to="/admin/material/yaguang-uv-oil/compatibilities"
              class="bg-sky-600 text-white px-4 py-2 rounded-md hover:bg-sky-700 transition-colors text-center block text-sm"
            >
              兼容性管理
            </router-link>
          </div>
        </div>

        <!-- 導入備份與恢復（方案 C） -->
        <div
          v-if="showModule('system:backup:create')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-amber-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-amber-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">導入備份與恢復</h3>
              <p class="text-sm text-gray-500">覆蓋式導入前快照，按次恢復</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/import-snapshot-restore"
              class="w-full bg-amber-600 text-white px-4 py-2 rounded-md hover:bg-amber-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>

        <!-- 操作日誌 -->
        <div
          v-if="showModule('system:log:view')"
          class="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow"
        >
          <div class="flex items-center mb-4">
            <div class="w-12 h-12 bg-slate-100 rounded-lg flex items-center justify-center">
              <svg class="w-6 h-6 text-slate-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
              </svg>
            </div>
            <div class="ml-4">
              <h3 class="text-lg font-medium text-gray-900">操作日誌</h3>
              <p class="text-sm text-gray-500">查看誰在何時對哪塊數據做了何種操作</p>
            </div>
          </div>
          <div class="space-y-2">
            <router-link
              to="/admin/operation-log"
              class="w-full bg-slate-600 text-white px-4 py-2 rounded-md hover:bg-slate-700 transition-colors text-center block"
            >
              進入管理
            </router-link>
          </div>
        </div>
      </div>

      <!-- 系統統計總覽 -->
      <div class="mt-8 bg-white rounded-lg shadow-md p-6">
        <h2 class="text-xl font-semibold text-gray-900 mb-4">物料規則統計總覽</h2>
        
        <!-- 错误提示 -->
        <div v-if="error" class="mb-4 p-4 bg-red-50 border border-red-200 rounded-md">
          <div class="flex">
            <svg class="w-5 h-5 text-red-400 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path>
            </svg>
            <p class="text-sm text-red-700">{{ error }}</p>
          </div>
        </div>

        <!-- 统计数据 -->
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div class="text-center">
            <div v-if="loading" class="text-2xl font-bold text-blue-600 animate-pulse">
              <div class="w-12 h-8 bg-blue-200 rounded mx-auto"></div>
            </div>
            <div v-else class="text-2xl font-bold text-blue-600">
              {{ statistics.hotStampingMaterialCount }}
            </div>
            <div class="text-sm text-gray-500">燙金物料數量</div>
          </div>
          <div class="text-center">
            <div v-if="loading" class="text-2xl font-bold text-green-600 animate-pulse">
              <div class="w-12 h-8 bg-green-200 rounded mx-auto"></div>
            </div>
            <div v-else class="text-2xl font-bold text-green-600">
              {{ statistics.productTypeCount }}
            </div>
            <div class="text-sm text-gray-500">產品類型</div>
          </div>
          <div class="text-center">
            <div v-if="loading" class="text-2xl font-bold text-purple-600 animate-pulse">
              <div class="w-12 h-8 bg-purple-200 rounded mx-auto"></div>
            </div>
            <div v-else class="text-2xl font-bold text-purple-600">
              {{ statistics.hotStampingPatternCount }}
            </div>
            <div class="text-sm text-gray-500">燙金圖案</div>
          </div>
          <div class="text-center">
            <div v-if="loading" class="text-2xl font-bold text-yellow-600 animate-pulse">
              <div class="w-12 h-8 bg-yellow-200 rounded mx-auto"></div>
            </div>
            <div v-else class="text-2xl font-bold text-yellow-600">
              {{ statistics.applicableInterfaceCount }}
            </div>
            <div class="text-sm text-gray-500">適用界面</div>
          </div>
        </div>

        <!-- 刷新按钮 -->
        <div class="mt-4 text-center">
          <button
            @click="fetchStatistics"
            :disabled="loading"
            class="inline-flex items-center px-3 py-2 border border-gray-300 shadow-sm text-sm leading-4 font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <svg v-if="loading" class="animate-spin -ml-1 mr-2 h-4 w-4 text-gray-500" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <svg v-else class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
            </svg>
            {{ loading ? '加載中...' : '刷新數據' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import { statisticsApi } from '@/api/modules/materialManagement'
import { usePermissionStore } from '@/stores/permission'
import { userHasPermissionKey } from '@/utils/permissionKeyAliases'

const permissionStore = usePermissionStore()

/**
 * 子模塊卡片：僅管理員或具備該子路由對應 view（含別名）時顯示。
 * material:rule:view 只放開 Hub 路由，不在此處放開全部卡片，避免與子頁權限不一致。
 * RBAC 未就緒時不展示（避免閃現全部卡片）。
 */
function showModule(subRouteViewKey: string): boolean {
  if (!permissionStore.currentUserRbacReady) return false
  if (permissionStore.isAdmin) return true
  return userHasPermissionKey(permissionStore.permissionKeysFlat, subRouteViewKey)
}

const statistics = ref({
  hotStampingMaterialCount: 0,
  productTypeCount: 0,
  hotStampingPatternCount: 0,
  applicableInterfaceCount: 0
})

const loading = ref(true)
const error = ref('')

const fetchStatistics = async () => {
  try {
    loading.value = true
    error.value = ''
    const data = await statisticsApi.getMaterialRuleStatistics()
    statistics.value = data
  } catch (err) {
    console.error('获取统计数据失败:', err)
    error.value = '获取统计数据失败，请稍后重试'
    statistics.value = {
      hotStampingMaterialCount: 0,
      productTypeCount: 0,
      hotStampingPatternCount: 0,
      applicableInterfaceCount: 0
    }
  } finally {
    loading.value = false
  }
}

let refreshTimer: ReturnType<typeof setInterval> | null = null

onMounted(() => {
  void permissionStore.initPermissions()
  fetchStatistics()
  refreshTimer = setInterval(() => {
    fetchStatistics()
  }, 5 * 60 * 1000)
})

onUnmounted(() => {
  if (refreshTimer) clearInterval(refreshTimer)
})
</script>

<style scoped>
/* 可以添加特定的样式 */
</style>

