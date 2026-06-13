<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <AdminBackToPanel />
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">硅胶发光油墨兼容性管理</h1>
                <p class="mt-2 text-gray-600">管理硅胶发光油墨与后加工工序的兼容性配置</p>
              </div>
              <div class="flex flex-wrap gap-2 xl:justify-end">
                <button
                  @click="showAddDialog = true"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                  </svg>
                  添加兼容性
                </button>
                <router-link
                  to="/admin/material/silicone-glow-ink"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                  </svg>
                  返回产品管理
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 兼容性列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">兼容性配置列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">后加工工序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">排序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in compatibilityList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.productId }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ item.postProcessingStep }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="compatibilityClass(item.compatibilityStatus)"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ compatibilityLabel(item.compatibilityStatus) }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.displayOrder ?? 0 }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center space-x-2">
                    <button @click="editCompatibility(item)" class="text-green-600 hover:text-green-900">编辑</button>
                    <button @click="deleteCompatibility(item.id as number)" class="text-red-600 hover:text-red-900">删除</button>
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
      </div>

      <!-- 添加/编辑对话框 -->
      <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-2xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">{{ showEditDialog ? '编辑兼容性' : '添加兼容性' }}</h3>
              <button @click="closeDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveCompatibility" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">产品ID *</label>
                  <input v-model.number="form.productId" type="number" placeholder="产品ID" required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序 *</label>
                  <input v-model="form.postProcessingStep" type="text" placeholder="后加工工序名称" required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">兼容性状态 *</label>
                  <select v-model="form.compatibilityStatus" required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                    <option value="">请选择兼容性状态</option>
                    <option value="V">V - 兼容</option>
                    <option value="X">X - 不兼容</option>
                    <option value="▷">▷ - 有条件兼容</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">显示排序</label>
                  <input v-model.number="form.displayOrder" type="number" placeholder="显示排序"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
              </div>

              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button type="button" @click="closeDialog"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition-colors">
                  取消
                </button>
                <button type="submit"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors">
                  保存
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import { siliconeGlowInkApi } from '@/api/modules/silicone_glow_ink'
import type { SiliconeGlowInkCompatibility } from '@/api/modules/silicone_glow_ink'

const compatibilityList = ref<SiliconeGlowInkCompatibility[]>([])
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const form = ref<SiliconeGlowInkCompatibility>({
  productId: 0,
  postProcessingStep: '',
  compatibilityStatus: '',
  displayOrder: 0
})

const compatibilityClass = (status: string | undefined) => {
  if (status === 'V') return 'bg-green-100 text-green-800'
  if (status === 'X') return 'bg-red-100 text-red-800'
  if (status === '▷') return 'bg-yellow-100 text-yellow-800'
  return 'bg-gray-100 text-gray-800'
}

const compatibilityLabel = (status: string | undefined) => {
  if (status === 'V') return '兼容 (V)'
  if (status === 'X') return '不兼容 (X)'
  if (status === '▷') return '有条件兼容 (▷)'
  return status || '-'
}

const loadCompatibilities = async () => {
  try {
    compatibilityList.value = await siliconeGlowInkApi.getAllCompatibilities()
  } catch (err) {
    console.error('获取兼容性列表失败:', err)
  }
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  form.value = {
    productId: 0,
    postProcessingStep: '',
    compatibilityStatus: '',
    displayOrder: 0
  }
}

const saveCompatibility = async () => {
  try {
    if (showEditDialog.value && form.value.id) {
      await siliconeGlowInkApi.updateCompatibility(form.value.id, form.value)
    } else {
      await siliconeGlowInkApi.createCompatibility(form.value)
    }
    closeDialog()
    await loadCompatibilities()
  } catch (err) {
    console.error('保存兼容性失败:', err)
    alert('保存失败: ' + (err as any).response?.data?.message || (err as Error).message)
  }
}

const editCompatibility = (item: SiliconeGlowInkCompatibility) => {
  form.value = { ...item }
  showEditDialog.value = true
}

const deleteCompatibility = async (id: number) => {
  if (!confirm('确定删除该兼容性配置吗？')) return
  try {
    await siliconeGlowInkApi.deleteCompatibility(id)
    await loadCompatibilities()
  } catch (err) {
    console.error('删除兼容性失败:', err)
    alert('删除失败')
  }
}

onMounted(() => {
  loadCompatibilities()
})
</script>
