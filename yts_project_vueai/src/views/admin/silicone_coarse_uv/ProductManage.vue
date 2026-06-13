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
                <h1 class="text-3xl font-bold text-gray-900">硅胶粗纹UV产品管理</h1>
                <p class="mt-2 text-gray-600">管理硅胶粗纹UV产品的配置信息</p>
              </div>
              <div class="flex flex-wrap gap-2 xl:justify-end">
                <button
                  @click="showAddDialog = true"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                  </svg>
                  添加产品
                </button>
                <router-link
                  to="/admin/silicone_coarse_uv/compatibility"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                  </svg>
                  管理兼容性
                </router-link>
                <router-link
                  to="/admin/material/post-processing"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                  </svg>
                  返回
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 搜索 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">关键词搜索</label>
            <input
              v-model="searchKeyword"
              type="text"
              placeholder="输入产品名称、编码进行搜索"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          <div class="flex items-end space-x-3">
            <button
              @click="searchData"
              class="bg-green-600 text-white px-4 py-2 rounded-md hover:bg-green-700 transition-colors"
            >
              搜索
            </button>
            <button
              @click="resetSearch"
              class="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition-colors"
            >
              重置
            </button>
          </div>
        </div>
      </div>

      <!-- 产品列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">产品列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">物料名称</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">物料编码</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">颜色</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">材质</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in productList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {{ item.materialName || '-' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ item.materialCode || '-' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ item.color || '-' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {{ item.category || '-' }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="item.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ item.isActive ? '激活' : '停用' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center space-x-2">
                    <button @click="viewDetail(item)" class="text-blue-600 hover:text-blue-900">详情</button>
                    <button @click="editProduct(item)" class="text-green-600 hover:text-green-900">编辑</button>
                    <button @click="deleteProduct(item.id as number)" class="text-red-600 hover:text-red-900">删除</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="productList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暂无产品数据</div>
        </div>
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">
              {{ form.id ? '编辑产品' : '添加产品' }}
            </h3>
            <button @click="closeDialog" class="text-gray-400 hover:text-gray-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <form @submit.prevent="saveProduct" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">物料名称 *</label>
                <input v-model="form.materialName" required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">物料编码</label>
                <input v-model="form.materialCode"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">供应商物料型号</label>
                <input v-model="form.supplierCode"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">颜色</label>
                <input v-model="form.color"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">材质</label>
                <input v-model="form.category"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">测试员</label>
                <input v-model="form.responsiblePerson"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">用纸尺寸-最小(mm)</label>
                <input v-model="form.minSheetSize"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">用纸尺寸-最大(mm)</label>
                <input v-model="form.maxSheetSize"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">间距-最小(mm)</label>
                <input v-model="form.minSpacing"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">采购申请编号</label>
                <input v-model="form.stockCode"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <div class="flex items-end pb-3">
                <label class="inline-flex items-center space-x-2 text-sm text-gray-700">
                  <input v-model="form.isActive" type="checkbox" class="rounded border-gray-300 text-green-600 focus:ring-green-500" />
                  <span>激活状态</span>
                </label>
              </div>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">物料用途</label>
              <textarea v-model="form.usageText" rows="2"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              ></textarea>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">设计限制</label>
              <textarea v-model="form.designInfo" rows="2"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              ></textarea>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">适用界面</label>
              <textarea v-model="form.applicableInterface" rows="2"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              ></textarea>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">注意事项</label>
              <textarea v-model="form.notes" rows="2"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              ></textarea>
            </div>

            <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
              <button type="button" @click="closeDialog"
                class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
                取消
              </button>
              <button type="submit"
                class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">
                保存
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 详情对话框 -->
    <div v-if="showDetailDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-lg font-medium text-gray-900">产品详情</h3>
          <button @click="showDetailDialog = false" class="text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div><label class="text-sm font-medium text-gray-500">物料名称：</label><span class="text-sm text-gray-900">{{ detailItem.materialName }}</span></div>
          <div><label class="text-sm font-medium text-gray-500">物料编码：</label><span class="text-sm text-gray-900">{{ detailItem.materialCode || '-' }}</span></div>
          <div><label class="text-sm font-medium text-gray-500">供应商型号：</label><span class="text-sm text-gray-900">{{ detailItem.supplierCode || '-' }}</span></div>
          <div><label class="text-sm font-medium text-gray-500">颜色：</label><span class="text-sm text-gray-900">{{ detailItem.color || '-' }}</span></div>
          <div><label class="text-sm font-medium text-gray-500">材质：</label><span class="text-sm text-gray-900">{{ detailItem.category || '-' }}</span></div>
          <div><label class="text-sm font-medium text-gray-500">测试员：</label><span class="text-sm text-gray-900">{{ detailItem.responsiblePerson || '-' }}</span></div>
          <div><label class="text-sm font-medium text-gray-500">最小用纸尺寸：</label><span class="text-sm text-gray-900">{{ detailItem.minSheetSize || '-' }}</span></div>
          <div><label class="text-sm font-medium text-gray-500">最大用纸尺寸：</label><span class="text-sm text-gray-900">{{ detailItem.maxSheetSize || '-' }}</span></div>
          <div><label class="text-sm font-medium text-gray-500">最小间距：</label><span class="text-sm text-gray-900">{{ detailItem.minSpacing || '-' }}</span></div>
          <div><label class="text-sm font-medium text-gray-500">状态：</label><span class="text-sm" :class="detailItem.isActive ? 'text-green-600' : 'text-gray-600'">{{ detailItem.isActive ? '激活' : '停用' }}</span></div>
        </div>
        <div class="mt-4" v-if="detailItem.usageText">
          <label class="text-sm font-medium text-gray-500">物料用途：</label>
          <p class="text-sm text-gray-900 whitespace-pre-wrap mt-1">{{ detailItem.usageText }}</p>
        </div>
        <div class="mt-2" v-if="detailItem.designInfo">
          <label class="text-sm font-medium text-gray-500">设计限制：</label>
          <p class="text-sm text-gray-900 whitespace-pre-wrap mt-1">{{ detailItem.designInfo }}</p>
        </div>
        <div class="mt-2" v-if="detailItem.applicableInterface">
          <label class="text-sm font-medium text-gray-500">适用界面：</label>
          <p class="text-sm text-gray-900 whitespace-pre-wrap mt-1">{{ detailItem.applicableInterface }}</p>
        </div>
        <div class="mt-2" v-if="detailItem.notes">
          <label class="text-sm font-medium text-gray-500">注意事项：</label>
          <p class="text-sm text-gray-900 whitespace-pre-wrap mt-1">{{ detailItem.notes }}</p>
        </div>
        <div class="flex justify-end pt-4 border-t border-gray-200 mt-4">
          <button @click="showDetailDialog = false"
            class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
            关闭
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import { siliconeCoarseUvApi, type SiliconeCoarseUvProduct } from '../../../api/modules/silicone_coarse_uv'

const productList = ref<SiliconeCoarseUvProduct[]>([])
const searchKeyword = ref('')
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showDetailDialog = ref(false)
const detailItem = ref<SiliconeCoarseUvProduct>({})

const form = reactive<SiliconeCoarseUvProduct>({
  materialName: '',
  materialCode: '',
  supplierCode: '',
  stockCode: '',
  usageText: '',
  category: '',
  color: '',
  responsiblePerson: '',
  minSheetSize: '',
  maxSheetSize: '',
  minSpacing: '',
  designInfo: '',
  applicableInterface: '',
  notes: '',
  isActive: true
})

const loadProducts = async () => {
  try {
    productList.value = await siliconeCoarseUvApi.getAllProducts()
  } catch (error) {
    console.error('加载产品列表失败:', error)
  }
}

const searchData = async () => {
  if (searchKeyword.value.trim()) {
    try {
      productList.value = await siliconeCoarseUvApi.searchProducts(searchKeyword.value.trim())
    } catch (error) {
      console.error('搜索产品失败:', error)
    }
  } else {
    await loadProducts()
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  loadProducts()
}

const viewDetail = (item: SiliconeCoarseUvProduct) => {
  detailItem.value = { ...item }
  showDetailDialog.value = true
}

const editProduct = (item: SiliconeCoarseUvProduct) => {
  Object.assign(form, item)
  showEditDialog.value = true
}

const closeDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(form, {
    id: undefined,
    materialName: '',
    materialCode: '',
    supplierCode: '',
    stockCode: '',
    usageText: '',
    category: '',
    color: '',
    responsiblePerson: '',
    minSheetSize: '',
    maxSheetSize: '',
    minSpacing: '',
    designInfo: '',
    applicableInterface: '',
    notes: '',
    isActive: true
  })
}

const saveProduct = async () => {
  try {
    if (!form.materialName) {
      alert('请输入物料名称')
      return
    }
    if (form.id) {
      await siliconeCoarseUvApi.updateProduct(form.id, form)
    } else {
      await siliconeCoarseUvApi.createProduct(form)
    }
    closeDialog()
    await loadProducts()
    alert('保存成功')
  } catch (error: any) {
    console.error('保存产品失败:', error)
    alert(error.response?.data?.message || '保存失败')
  }
}

const deleteProduct = async (id: number) => {
  if (!confirm('确定要删除该产品吗？（关联的兼容性数据也将被删除）')) return
  try {
    const result = await siliconeCoarseUvApi.deleteProduct(id)
    if (result.success) {
      await loadProducts()
      alert('删除成功')
    } else {
      alert(result.message)
    }
  } catch (error: any) {
    console.error('删除产品失败:', error)
    alert(error.response?.data?.message || '删除失败')
  }
}

onMounted(() => {
  loadProducts()
})
</script>
