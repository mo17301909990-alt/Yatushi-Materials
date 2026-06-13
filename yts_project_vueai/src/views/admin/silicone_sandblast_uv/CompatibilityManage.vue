<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">硅胶磨砂UV兼容性管理</h1>
                <p class="mt-2 text-gray-600">管理所有硅胶磨砂UV产品的后加工工序兼容性</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 所有兼容性列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">兼容性列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">产品ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">后加工工序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">兼容性</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">顺序</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in compatibilityList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.id }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.productId }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.postProcessingStep }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="{
                      'bg-green-100 text-green-800': item.compatibilityStatus === 'V',
                      'bg-red-100 text-red-800': item.compatibilityStatus === 'X',
                      'bg-yellow-100 text-yellow-800': item.compatibilityStatus === '▷'
                    }"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ item.compatibilityStatus === 'V' ? '兼容' : item.compatibilityStatus === 'X' ? '不兼容' : '有条件' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.displayOrder ?? '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button @click="deleteCompatibility(item.id!)" class="text-red-600 hover:text-red-900">删除</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="compatibilityList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暂无兼容性数据</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { siliconeSandblastUvApi, type SiliconeSandblastUvCompatibility } from '../../../api/modules/silicone_sandblast_uv'

const compatibilityList = ref<SiliconeSandblastUvCompatibility[]>([])

const loadCompatibilities = async () => {
  try {
    compatibilityList.value = await siliconeSandblastUvApi.getAllCompatibilities()
  } catch (error) {
    console.error('加载兼容性数据失败:', error)
  }
}

const deleteCompatibility = async (id: number) => {
  if (!confirm('确定删除该兼容性配置？')) return
  try {
    await siliconeSandblastUvApi.deleteCompatibility(id)
    await loadCompatibilities()
  } catch (error) {
    console.error('删除兼容性失败:', error)
    alert('删除失败')
  }
}

onMounted(() => {
  loadCompatibilities()
})
</script>
