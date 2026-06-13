<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">硅膠水彩油墨 - 產品管理</h1>
                <p class="mt-2 text-gray-600">管理硅膠水彩油墨(Watercolor Ink)的產品信息</p>
              </div>
              <div class="flex flex-wrap gap-2">
                <button
                  @click="showAddDialog = true"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                  </svg>
                  添加產品
                </button>
                <router-link
                  to="/admin/silicone_watercolor_ink/compatibility"
                  class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
                  </svg>
                  兼容性管理
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 搜索欄 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">搜索</label>
            <input
              v-model="searchKeyword"
              type="text"
              placeholder="輸入物料名稱/編碼/供應商型號"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
        </div>
        <div class="flex items-end space-x-3 mt-4">
          <button
            @click="searchProducts"
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

      <!-- 產品列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">產品列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">物料名稱</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">物料編碼</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">供應商型號</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">材質</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">顏色</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">測試員</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">狀態</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="product in productList" :key="product.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.id }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ product.materialName || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.materialCode || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.supplierCode || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.category || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.color || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.responsiblePerson || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="product.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ product.isActive ? '啟用' : '禁用' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center space-x-2">
                    <button @click="editProduct(product)" class="text-green-600 hover:text-green-900">編輯</button>
                    <button @click="viewProduct(product)" class="text-blue-600 hover:text-blue-900">詳情</button>
                    <button @click="deleteProduct(product.id as number)" class="text-red-600 hover:text-red-900">刪除</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <div v-if="productList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暫無產品數據</div>
        </div>
      </div>

      <!-- 添加/編輯產品對話框 -->
      <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white my-8">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">
                {{ form.id ? '編輯產品' : '添加產品' }}
              </h3>
              <button @click="closeForm" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveProduct" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料名稱</label>
                  <input v-model="form.materialName" type="text" placeholder="如：水彩墨"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料編碼</label>
                  <input v-model="form.materialCode" type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">供應商型號</label>
                  <input v-model="form.supplierCode" type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">庫存編碼</label>
                  <input v-model="form.stockCode" type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">材質</label>
                  <input v-model="form.category" type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">顏色</label>
                  <input v-model="form.color" type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">測試員</label>
                  <input v-model="form.responsiblePerson" type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">用紙尺寸-最小(mm)</label>
                  <input v-model="form.minSheetSize" type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">用紙尺寸-最大(mm)</label>
                  <input v-model="form.maxSheetSize" type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">間距-最小(mm)</label>
                  <input v-model="form.minSpacing" type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">間距-最大(mm)</label>
                  <input v-model="form.maxSpacing" type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div class="flex items-end">
                  <label class="inline-flex items-center space-x-2 text-sm text-gray-700">
                    <input v-model="form.isActive" type="checkbox" class="rounded border-gray-300 text-green-600 focus:ring-green-500" />
                    <span>啟用狀態</span>
                  </label>
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">物料用途</label>
                <textarea v-model="form.usage" rows="2"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  placeholder="物料的用途描述"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">設計限制信息</label>
                <textarea v-model="form.designInfo" rows="2"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  placeholder="點、線、間距、結構應用等設計限制"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">適用界面</label>
                <textarea v-model="form.applicableInterface" rows="2"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  placeholder="紙張面/印刷油墨面/後加工涂層面的適用情況"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">注意事項</label>
                <textarea v-model="form.notes" rows="3"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
                  placeholder="注意事項、限制的備注與說明"></textarea>
              </div>

              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button type="button" @click="closeForm"
                  class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">
                  取消
                </button>
                <button type="submit"
                  class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">
                  {{ form.id ? '更新' : '保存' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 產品詳情對話框 -->
      <div v-if="showDetailDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white my-8">
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-lg font-medium text-gray-900">產品詳情</h3>
            <button @click="showDetailDialog = false" class="text-gray-400 hover:text-gray-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>
          <div v-if="detailProduct" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div><span class="text-sm font-medium text-gray-500">物料名稱：</span><span class="text-sm text-gray-900">{{ detailProduct.materialName || '-' }}</span></div>
              <div><span class="text-sm font-medium text-gray-500">物料編碼：</span><span class="text-sm text-gray-900">{{ detailProduct.materialCode || '-' }}</span></div>
              <div><span class="text-sm font-medium text-gray-500">供應商型號：</span><span class="text-sm text-gray-900">{{ detailProduct.supplierCode || '-' }}</span></div>
              <div><span class="text-sm font-medium text-gray-500">材質：</span><span class="text-sm text-gray-900">{{ detailProduct.category || '-' }}</span></div>
              <div><span class="text-sm font-medium text-gray-500">顏色：</span><span class="text-sm text-gray-900">{{ detailProduct.color || '-' }}</span></div>
              <div><span class="text-sm font-medium text-gray-500">測試員：</span><span class="text-sm text-gray-900">{{ detailProduct.responsiblePerson || '-' }}</span></div>
              <div><span class="text-sm font-medium text-gray-500">用紙尺寸：</span><span class="text-sm text-gray-900">{{ detailProduct.minSheetSize || '-' }} ~ {{ detailProduct.maxSheetSize || '-' }}</span></div>
              <div><span class="text-sm font-medium text-gray-500">間距：</span><span class="text-sm text-gray-900">{{ detailProduct.minSpacing || '-' }} ~ {{ detailProduct.maxSpacing || '-' }}</span></div>
              <div><span class="text-sm font-medium text-gray-500">狀態：</span><span class="text-sm" :class="detailProduct.isActive ? 'text-green-600' : 'text-gray-600'">{{ detailProduct.isActive ? '啟用' : '禁用' }}</span></div>
            </div>
            <div>
              <span class="text-sm font-medium text-gray-500">物料用途：</span>
              <p class="text-sm text-gray-900 mt-1 whitespace-pre-wrap">{{ detailProduct.usage || '-' }}</p>
            </div>
            <div>
              <span class="text-sm font-medium text-gray-500">設計限制：</span>
              <p class="text-sm text-gray-900 mt-1 whitespace-pre-wrap">{{ detailProduct.designInfo || '-' }}</p>
            </div>
            <div>
              <span class="text-sm font-medium text-gray-500">適用界面：</span>
              <p class="text-sm text-gray-900 mt-1 whitespace-pre-wrap">{{ detailProduct.applicableInterface || '-' }}</p>
            </div>
            <div>
              <span class="text-sm font-medium text-gray-500">注意事項：</span>
              <p class="text-sm text-gray-900 mt-1 whitespace-pre-wrap">{{ detailProduct.notes || '-' }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { siliconeWatercolorInkApi, type SiliconeWatercolorInkProduct } from '../../../api/modules/silicone_watercolor_ink'

// 數據
const productList = ref<SiliconeWatercolorInkProduct[]>([])
const searchKeyword = ref('')

// 表單
const form = ref<SiliconeWatercolorInkProduct>({ isActive: true })
const detailProduct = ref<SiliconeWatercolorInkProduct | null>(null)

// 對話框
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showDetailDialog = ref(false)

// 加載產品列表
const loadProducts = async () => {
  try {
    productList.value = await siliconeWatercolorInkApi.getAllProducts()
  } catch (error) {
    console.error('加載產品列表失敗:', error)
  }
}

// 搜索
const searchProducts = async () => {
  try {
    if (searchKeyword.value) {
      productList.value = await siliconeWatercolorInkApi.searchProducts(searchKeyword.value)
    } else {
      await loadProducts()
    }
  } catch (error) {
    console.error('搜索產品失敗:', error)
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  loadProducts()
}

// 新增
const saveProduct = async () => {
  try {
    if (form.value.id) {
      await siliconeWatercolorInkApi.updateProduct(form.value.id, form.value)
    } else {
      await siliconeWatercolorInkApi.createProduct(form.value)
    }
    closeForm()
    await loadProducts()
    alert(form.value.id ? '更新成功' : '添加成功')
  } catch (error) {
    console.error('保存產品失敗:', error)
    alert('保存失敗')
  }
}

// 編輯
const editProduct = (product: SiliconeWatercolorInkProduct) => {
  form.value = { ...product, isActive: product.isActive ?? true }
  showEditDialog.value = true
}

// 詳情
const viewProduct = (product: SiliconeWatercolorInkProduct) => {
  detailProduct.value = product
  showDetailDialog.value = true
}

// 刪除
const deleteProduct = async (id: number) => {
  if (!confirm('確定要刪除此產品嗎？')) return
  try {
    await siliconeWatercolorInkApi.deleteProduct(id)
    await loadProducts()
    alert('刪除成功')
  } catch (error) {
    console.error('刪除產品失敗:', error)
    alert('刪除失敗')
  }
}

// 關閉表單
const closeForm = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  form.value = { isActive: true }
}

onMounted(() => {
  loadProducts()
})
</script>
