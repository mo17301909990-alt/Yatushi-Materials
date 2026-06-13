<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">燙後加工管理</h1>
            <p class="mt-2 text-gray-600">管理燙後加工配置和選項</p>
          </div>
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

      <!-- 加工类型选择 -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <div class="text-center mb-8">
          <h2 class="text-2xl font-semibold text-gray-900 mb-4">選擇燙後加工類型</h2>
          <p class="text-gray-600">請選擇您要管理的燙後加工類型</p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 max-w-4xl mx-auto">
          <!-- 过胶 -->
          <button
            @click="selectProcessingType('過膠')"
            class="group p-6 border-2 border-gray-200 rounded-lg hover:border-teal-500 hover:bg-teal-50 transition-all duration-200 text-left"
          >
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 bg-teal-100 rounded-lg flex items-center justify-center group-hover:bg-teal-200 transition-colors">
                <svg class="w-6 h-6 text-teal-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 4V2a1 1 0 011-1h8a1 1 0 011 1v2m-9 0h10m-10 0a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V6a2 2 0 00-2-2M9 12l2 2 4-4"></path>
                </svg>
              </div>
              <div class="ml-4">
                <h3 class="text-lg font-medium text-gray-900 group-hover:text-teal-700">過膠</h3>
                <p class="text-sm text-gray-500">過膠加工配置管理</p>
              </div>
            </div>
          </button>

          <!-- 絲印LED UV灑閃粉 -->
          <button
            @click="selectProcessingType('絲印LED UV灑閃粉')"
            class="group p-6 border-2 border-gray-200 rounded-lg hover:border-purple-500 hover:bg-purple-50 transition-all duration-200 text-left"
          >
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center group-hover:bg-purple-200 transition-colors">
                <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z"></path>
                </svg>
              </div>
              <div class="ml-4">
                <h3 class="text-lg font-medium text-gray-900 group-hover:text-purple-700">絲印LED UV灑閃粉</h3>
                <p class="text-sm text-gray-500">絲印LED UV灑閃粉配置管理</p>
              </div>
            </div>
          </button>

          <!-- 印刷UV -->
          <button
            @click="selectProcessingType('印刷UV')"
            class="group p-6 border-2 border-gray-200 rounded-lg hover:border-blue-500 hover:bg-blue-50 transition-all duration-200 text-left"
          >
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center group-hover:bg-blue-200 transition-colors">
                <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path>
                </svg>
              </div>
              <div class="ml-4">
                <h3 class="text-lg font-medium text-gray-900 group-hover:text-blue-700">印刷UV</h3>
                <p class="text-sm text-gray-500">印刷UV配置管理</p>
              </div>
            </div>
          </button>

          <!-- 印刷 -->
          <button
            @click="selectProcessingType('印刷')"
            class="group p-6 border-2 border-gray-200 rounded-lg hover:border-green-500 hover:bg-green-50 transition-all duration-200 text-left"
          >
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center group-hover:bg-green-200 transition-colors">
                <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                </svg>
              </div>
              <div class="ml-4">
                <h3 class="text-lg font-medium text-gray-900 group-hover:text-green-700">印刷</h3>
                <p class="text-sm text-gray-500">印刷配置管理</p>
              </div>
            </div>
          </button>

          <!-- 击凸 -->
          <button
            @click="selectProcessingType('擊凸')"
            class="group p-6 border-2 border-gray-200 rounded-lg hover:border-orange-500 hover:bg-orange-50 transition-all duration-200 text-left"
          >
            <div class="flex items-center mb-4">
              <div class="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center group-hover:bg-orange-200 transition-colors">
                <svg class="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3"></path>
                </svg>
              </div>
              <div class="ml-4">
                <h3 class="text-lg font-medium text-gray-900 group-hover:text-orange-700">擊凸</h3>
                <p class="text-sm text-gray-500">擊凸配置管理</p>
              </div>
            </div>
          </button>
        </div>
      </div>

      <!-- 击凸映射类型选择对话框 -->
      <div v-if="showEmbossingMappingDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-6">
              <h3 class="text-lg font-medium text-gray-900">選擇映射類型 - 平面燙金</h3>
              <button
                @click="closeEmbossingMappingDialog"
                class="text-gray-400 hover:text-gray-600"
              >
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <div class="text-center mb-6">
              <p class="text-gray-600">請選擇要配置的映射類型：</p>
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <!-- 常用界面映射 -->
              <button
                @click="selectCommonInterfaceMapping"
                class="group p-6 border-2 border-gray-200 rounded-lg hover:border-orange-500 hover:bg-orange-50 transition-all duration-200 text-left relative"
              >
                <div class="flex items-center mb-4">
                  <div class="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center group-hover:bg-orange-200 transition-colors">
                    <svg class="w-6 h-6 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                  </div>
                  <div class="ml-4">
                    <h3 class="text-lg font-medium text-gray-900 group-hover:text-orange-700">常用界面映射</h3>
                    <p class="text-sm text-gray-500">配置常用界面的兼容性映射</p>
                  </div>
                </div>
                <!-- 快速统计信息 -->

              </button>

              <!-- 耐磨金纸映射 -->
              <button
                @click="selectWearResistantGoldPaperMapping"
                class="group p-6 border-2 border-gray-200 rounded-lg hover:border-yellow-500 hover:bg-yellow-50 transition-all duration-200 text-left relative"
              >
                <div class="flex items-center mb-4">
                  <div class="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center group-hover:bg-yellow-200 transition-colors">
                    <svg class="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4"></path>
                    </svg>
                  </div>
                  <div class="ml-4">
                    <h3 class="text-lg font-medium text-gray-900 group-hover:text-yellow-700">耐磨金紙映射</h3>
                    <p class="text-sm text-gray-500">配置耐磨金紙的兼容性映射，固定燙金類型為：燙金後擊凸</p>
                  </div>
                </div>
                <!-- 快速统计信息 -->

              </button>
            </div>

            <div class="flex justify-end pt-6">
              <button
                @click="closeEmbossingMappingDialog"
                class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                取消
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

// 路由实例
const router = useRouter()

// 击凸映射类型选择对话框状态
const showEmbossingMappingDialog = ref(false)

// 统计数据
const commonInterfaceStats = ref({
  configured: 0,
  pending: 0
})

const wearResistantStats = ref({
  configured: 0,
  pending: 0
})

// 加载统计数据
const loadStats = async () => {
  try {
    // TODO: 如果后端有统计数据API，可以在这里调用
    // 暂时使用模拟数据
    commonInterfaceStats.value = {
      configured: 12,
      pending: 3
    }
    
    wearResistantStats.value = {
      configured: 8,
      pending: 5
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
    // 如果加载失败，使用默认模拟数据
    commonInterfaceStats.value = {
      configured: 12,
      pending: 3
    }
    wearResistantStats.value = {
      configured: 8,
      pending: 5
    }
  }
}

// 选择加工类型
const selectProcessingType = (type: string) => {
  console.log('選擇的加工類型:', type)
  
  // 根据不同的加工类型跳转到不同的页面或显示不同的内容
  switch (type) {
    case '過膠':
      // 跳轉到過膠管理頁面
      router.push({
        path: '/admin/material/post-processing/laminating',
        query: { type: '過膠' }
      })
      break
    case '絲印LED UV灑閃粉':
      // 跳转到絲印LED UV灑閃粉管理页面
      router.push({
        path: '/admin/material/post-processing/leduvglitter',
        query: { type: '絲印LED UV灑閃粉' }
      })
      break
    case '印刷UV':
      // 跳转到印刷UV管理页面
      router.push({
        path: '/admin/material/post-processing/uv',
        query: { type: '印刷UV' }
      })
      break
    case '印刷':
      // 跳转到印刷管理页面
      router.push({
        path: '/admin/material/post-processing/printing',
        query: { type: '印刷' }
      })
      break
    case '擊凸':
      // 顯示擊凸映射類型選擇對話框
      showEmbossingMappingDialog.value = true
      break
    default:
      console.log('未知的加工類型:', type)
  }
}

// 關閉擊凸映射類型選擇對話框
const closeEmbossingMappingDialog = () => {
  showEmbossingMappingDialog.value = false
}

// 選擇常用界面映射
const selectCommonInterfaceMapping = () => {
  closeEmbossingMappingDialog()
  router.push({
    path: '/admin/material/post-processing/embossing/common-interface',
    query: { type: '常用界面映射' }
  })
}

// 選擇耐磨金紙映射
const selectWearResistantGoldPaperMapping = async () => {
  closeEmbossingMappingDialog()
  
  try {
    // 查找"燙金後擊凸"烫金类型ID
    const { hotStampingTypeOptionsApi } = await import('../../../api/modules/hotStampingTypeOptions')
    const allTypes = await hotStampingTypeOptionsApi.getAllTypes()
    const targetType = allTypes.find(type => 
      type.stampingType === '燙金後擊凸' && 
      (type.positionType === null || type.positionType === undefined || type.positionType === '')
    )
    
    if (targetType) {
      // 直接跳转到耐磨金纸映射管理页面，传递烫金类型ID
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

// 页面加载时获取统计数据
onMounted(() => {
  loadStats()
})
</script>
