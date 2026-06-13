<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">硅膠白UV物料管理</h1>
        <p class="mt-2 text-gray-600">管理硅膠白UV物料產品與兼容性配置</p>
      </div>

      <!-- 操作按鈕 -->
      <div class="mb-6 flex flex-wrap gap-2">
        <button
          @click="showAddDialog = true"
          class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
        >
          <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
          </svg>
          添加產品
        </button>
        <button
          @click="refreshData"
          class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
        >
          <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
          </svg>
          刷新
        </button>
      </div>

      <!-- 搜索 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="flex gap-4">
          <div class="flex-1">
            <input
              v-model="searchKeyword"
              @keyup.enter="searchProducts"
              type="text"
              placeholder="搜索產品名稱、物料編碼..."
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          <button
            @click="searchProducts"
            class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 transition-colors"
          >
            搜索
          </button>
          <button
            @click="resetSearch"
            class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600 transition-colors"
          >
            重置
          </button>
        </div>
      </div>

      <!-- 產品列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">產品列表 ({{ productList.length }})</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">物料編碼</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">物料名稱</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">物料用途</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">顏色</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">測試員</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">狀態</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="product in productList" :key="product.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.materialCode || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ product.materialName || '-' }}</td>
                <td class="px-6 py-4 text-sm text-gray-900 max-w-xs truncate">{{ product.usage || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.color || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ product.responsiblePerson || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="product.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ product.isActive ? '激活' : '停用' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center space-x-2">
                    <button @click="viewCompatibility(product)" class="text-indigo-600 hover:text-indigo-900">兼容性</button>
                    <button @click="editProduct(product)" class="text-blue-600 hover:text-blue-900">編輯</button>
                    <button @click="deleteProduct(product.id!)" class="text-red-600 hover:text-red-900">刪除</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- 空狀態 -->
        <div v-if="productList.length === 0" class="text-center py-12">
          <div class="text-gray-500">暫無產品數據</div>
        </div>
      </div>

      <!-- 添加/編輯產品對話框 -->
      <div v-if="showAddDialog || showEditDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white my-8">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">{{ showEditDialog ? '編輯產品' : '添加產品' }}</h3>
              <button @click="closeProductDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveProduct" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料編碼</label>
                  <input v-model="productForm.materialCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">採購部申請編號</label>
                  <input v-model="productForm.supplierCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料型號/編號</label>
                  <input v-model="productForm.stockCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料名稱 *</label>
                  <input v-model="productForm.materialName" type="text" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料用途</label>
                  <input v-model="productForm.usage" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">材質</label>
                  <input v-model="productForm.category" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">顏色</label>
                  <input v-model="productForm.color" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">測試員</label>
                  <input v-model="productForm.responsiblePerson" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">最小用紙尺寸</label>
                  <input v-model="productForm.minSheetSize" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">最大用紙尺寸</label>
                  <input v-model="productForm.maxSheetSize" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">最小間距(mm)</label>
                  <input v-model="productForm.minSpacing" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div class="flex items-end">
                  <label class="inline-flex items-center space-x-2 text-sm text-gray-700">
                    <input v-model="productForm.isActive" type="checkbox" class="rounded border-gray-300 text-green-600 focus:ring-green-500" />
                    <span>啟用狀態</span>
                  </label>
                </div>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">設計限制</label>
                <textarea v-model="productForm.designInfo" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">適用界面</label>
                <textarea v-model="productForm.applicableInterface" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">注意事項</label>
                <textarea v-model="productForm.notes" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>

              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button type="button" @click="closeProductDialog" class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">取消</button>
                <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">保存</button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 兼容性管理對話框 -->
      <div v-if="showCompatibilityDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-8 mx-auto p-6 border w-11/12 max-w-6xl shadow-lg rounded-md bg-white">
          <div class="flex items-center justify-between mb-6">
            <div>
              <h3 class="text-lg font-medium text-gray-900">兼容性管理 - {{ currentProduct?.materialName }}</h3>
              <p class="text-sm text-gray-500 mt-1">物料編碼: {{ currentProduct?.materialCode }}</p>
            </div>
            <button @click="closeCompatibilityDialog" class="text-gray-400 hover:text-gray-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
            </button>
          </div>

          <!-- 添加兼容性 -->
          <div class="mb-6 bg-white border rounded-lg p-4">
            <h4 class="text-lg font-medium text-gray-900 mb-4">{{ editingCompatibility ? '編輯兼容性' : '添加兼容性' }}</h4>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">後加工工序 *</label>
                <input v-model="compatibilityForm.postProcessingStep" type="text" placeholder="如：局部UV、燙金" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">兼容性狀態 *</label>
                <select v-model="compatibilityForm.compatibilityStatus" required class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">顯示順序</label>
                <input v-model.number="compatibilityForm.displayOrder" type="number" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>
            </div>
            <div class="mt-4 flex space-x-3">
              <button @click="saveCompatibility" class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">
                {{ editingCompatibility ? '更新' : '添加' }}
              </button>
              <button v-if="editingCompatibility" @click="cancelEditCompatibility" class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">取消編輯</button>
            </div>
          </div>

          <!-- 兼容性列表 -->
          <div class="overflow-x-auto border rounded-md">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">後加工工序</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">兼容性</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">顯示順序</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="item in compatibilityList" :key="item.id" class="hover:bg-gray-50">
                  <td class="px-4 py-2 text-sm">{{ item.postProcessingStep }}</td>
                  <td class="px-4 py-2">
                    <span :class="item.compatibilityStatus === 'V' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'" class="inline-flex px-2 py-1 text-xs font-semibold rounded-full">
                      {{ item.compatibilityStatus === 'V' ? '兼容' : '不兼容' }}
                    </span>
                  </td>
                  <td class="px-4 py-2 text-sm">{{ item.displayOrder ?? '-' }}</td>
                  <td class="px-4 py-2 text-sm space-x-3">
                    <button @click="editCompatibility(item)" class="text-blue-600 hover:text-blue-800">編輯</button>
                    <button @click="deleteCompatibility(item.id!)" class="text-red-600 hover:text-red-800">刪除</button>
                  </td>
                </tr>
              </tbody>
            </table>
            <div v-if="compatibilityList.length === 0" class="text-center py-8 text-gray-500">暫無兼容性數據</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { siliconeWhiteUvApi, type SiliconeWhiteUvProduct, type SiliconeWhiteUvCompatibility } from '../../../api/modules/silicone_white_uv'

const productList = ref<SiliconeWhiteUvProduct[]>([])
const compatibilityList = ref<SiliconeWhiteUvCompatibility[]>([])
const currentProduct = ref<SiliconeWhiteUvProduct | null>(null)
const searchKeyword = ref('')
const showAddDialog = ref(false)
const showEditDialog = ref(false)
const showCompatibilityDialog = ref(false)
const editingCompatibility = ref(false)

const productForm = reactive<SiliconeWhiteUvProduct>({
  materialCode: '',
  supplierCode: '',
  stockCode: '',
  materialName: '',
  usage: '',
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

const compatibilityForm = reactive<SiliconeWhiteUvCompatibility>({
  productId: undefined,
  postProcessingStep: '',
  compatibilityStatus: 'V',
  displayOrder: 0
})

onMounted(() => {
  loadProducts()
})

const loadProducts = async () => {
  try {
    productList.value = await siliconeWhiteUvApi.getAllProducts()
  } catch (error) {
    console.error('加載產品失敗:', error)
  }
}

const searchProducts = async () => {
  if (!searchKeyword.value.trim()) {
    await loadProducts()
    return
  }
  try {
    productList.value = await siliconeWhiteUvApi.searchProducts(searchKeyword.value.trim())
  } catch (error) {
    console.error('搜索產品失敗:', error)
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  loadProducts()
}

const refreshData = () => {
  loadProducts()
}

const saveProduct = async () => {
  try {
    if (showEditDialog.value && productForm.id) {
      await siliconeWhiteUvApi.updateProduct(productForm.id, { ...productForm })
    } else {
      await siliconeWhiteUvApi.createProduct({ ...productForm })
    }
    closeProductDialog()
    await loadProducts()
    alert(showEditDialog.value ? '更新成功' : '添加成功')
  } catch (error) {
    console.error('保存產品失敗:', error)
    alert('保存失敗')
  }
}

const editProduct = (product: SiliconeWhiteUvProduct) => {
  Object.assign(productForm, {
    id: product.id,
    materialCode: product.materialCode || '',
    supplierCode: product.supplierCode || '',
    stockCode: product.stockCode || '',
    materialName: product.materialName || '',
    usage: product.usage || '',
    category: product.category || '',
    color: product.color || '',
    responsiblePerson: product.responsiblePerson || '',
    minSheetSize: product.minSheetSize || '',
    maxSheetSize: product.maxSheetSize || '',
    minSpacing: product.minSpacing || '',
    designInfo: product.designInfo || '',
    applicableInterface: product.applicableInterface || '',
    notes: product.notes || '',
    isActive: product.isActive
  })
  showEditDialog.value = true
}

const deleteProduct = async (id: number) => {
  if (!confirm('確定要刪除此產品嗎？')) return
  try {
    await siliconeWhiteUvApi.deleteProduct(id)
    await loadProducts()
    alert('刪除成功')
  } catch (error) {
    console.error('刪除產品失敗:', error)
    alert('刪除失敗')
  }
}

const closeProductDialog = () => {
  showAddDialog.value = false
  showEditDialog.value = false
  Object.assign(productForm, {
    id: undefined,
    materialCode: '',
    supplierCode: '',
    stockCode: '',
    materialName: '',
    usage: '',
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

const viewCompatibility = async (product: SiliconeWhiteUvProduct) => {
  currentProduct.value = product
  showCompatibilityDialog.value = true
  await loadCompatibilities(product.id!)
}

const loadCompatibilities = async (productId: number) => {
  try {
    compatibilityList.value = await siliconeWhiteUvApi.getCompatibilitiesByProductId(productId)
  } catch (error) {
    console.error('加載兼容性失敗:', error)
  }
}

const saveCompatibility = async () => {
  if (!currentProduct.value?.id) return
  try {
    compatibilityForm.productId = currentProduct.value.id
    if (editingCompatibility.value && compatibilityForm.id) {
      await siliconeWhiteUvApi.updateCompatibility(compatibilityForm.id, { ...compatibilityForm })
    } else {
      await siliconeWhiteUvApi.createCompatibility({ ...compatibilityForm })
    }
    cancelEditCompatibility()
    await loadCompatibilities(currentProduct.value.id)
    alert(editingCompatibility.value ? '更新成功' : '添加成功')
  } catch (error) {
    console.error('保存兼容性失敗:', error)
    alert('保存失敗')
  }
}

const editCompatibility = (item: SiliconeWhiteUvCompatibility) => {
  Object.assign(compatibilityForm, {
    id: item.id,
    productId: item.productId,
    postProcessingStep: item.postProcessingStep || '',
    compatibilityStatus: item.compatibilityStatus || 'V',
    displayOrder: item.displayOrder || 0
  })
  editingCompatibility.value = true
}

const cancelEditCompatibility = () => {
  editingCompatibility.value = false
  Object.assign(compatibilityForm, {
    id: undefined,
    productId: currentProduct.value?.id,
    postProcessingStep: '',
    compatibilityStatus: 'V',
    displayOrder: 0
  })
}

const deleteCompatibility = async (id: number) => {
  if (!confirm('確定要刪除此兼容性配置嗎？')) return
  try {
    await siliconeWhiteUvApi.deleteCompatibility(id)
    if (currentProduct.value?.id) {
      await loadCompatibilities(currentProduct.value.id)
    }
    alert('刪除成功')
  } catch (error) {
    console.error('刪除兼容性失敗:', error)
    alert('刪除失敗')
  }
}

const closeCompatibilityDialog = () => {
  showCompatibilityDialog.value = false
  currentProduct.value = null
  compatibilityList.value = []
  cancelEditCompatibility()
}
</script>
