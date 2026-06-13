<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">硅胶磨砂UV产品管理</h1>
                <p class="mt-2 text-gray-600">管理硅胶磨砂UV(Sandblast Frost)产品及兼容性配置</p>
              </div>
              <div class="flex flex-wrap gap-2">
                <button
                  @click="showAddDialog = true"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                  </svg>
                  添加产品
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 搜索 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">搜索产品</label>
            <input
              v-model="searchKeyword"
              type="text"
              placeholder="输入物料名称/编码搜索"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
        </div>
        <div class="flex items-center space-x-3 mt-4">
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

      <!-- 产品列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="px-6 py-4 border-b border-gray-200">
          <h3 class="text-lg font-medium text-gray-900">产品列表</h3>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">物料名称</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">物料编码</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">库存编码</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">颜色</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in productList" :key="item.id" class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.id }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{{ item.materialName || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.materialCode || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.stockCode || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.color || '-' }}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="item.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ item.isActive ? '激活' : '禁用' }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <div class="flex items-center space-x-2">
                    <button @click="editProduct(item)" class="text-green-600 hover:text-green-900">编辑</button>
                    <button @click="deleteProduct(item.id!)" class="text-red-600 hover:text-red-900">删除</button>
                    <button @click="showCompatibility(item)" class="text-blue-600 hover:text-blue-900">兼容性</button>
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

      <!-- 添加/编辑产品对话框 -->
      <div v-if="showAddDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex items-center justify-between mb-4">
              <h3 class="text-lg font-medium text-gray-900">{{ editForm.id ? '编辑产品' : '添加产品' }}</h3>
              <button @click="closeAddDialog" class="text-gray-400 hover:text-gray-600">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                </svg>
              </button>
            </div>

            <form @submit.prevent="saveProduct" class="space-y-4">
              <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料名称</label>
                  <input v-model="editForm.materialName" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">物料编码</label>
                  <input v-model="editForm.materialCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">供应商编码</label>
                  <input v-model="editForm.supplierCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">库存编码</label>
                  <input v-model="editForm.stockCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">材质</label>
                  <input v-model="editForm.category" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">颜色</label>
                  <input v-model="editForm.color" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">测试员</label>
                  <input v-model="editForm.responsiblePerson" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">用纸最小尺寸(mm)</label>
                  <input v-model="editForm.minSheetSize" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">用纸最大尺寸(mm)</label>
                  <input v-model="editForm.maxSheetSize" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-1">间距最小(mm)</label>
                  <input v-model="editForm.minSpacing" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
                </div>
                <div class="flex items-end">
                  <label class="inline-flex items-center space-x-2 text-sm text-gray-700">
                    <input v-model="editForm.isActive" type="checkbox" class="rounded border-gray-300 text-green-600 focus:ring-green-500" />
                    <span>激活</span>
                  </label>
                </div>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">物料用途</label>
                <textarea v-model="editForm.usage" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">设计信息</label>
                <textarea v-model="editForm.designInfo" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">适用界面</label>
                <textarea v-model="editForm.applicableInterface" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">备注</label>
                <textarea v-model="editForm.notes" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
              </div>
              <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
                <button type="button" @click="closeAddDialog" class="inline-flex items-center px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">取消</button>
                <button type="submit" class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">保存</button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- 兼容性管理对话框 -->
      <div v-if="showCompatibilityDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
        <div class="relative top-8 mx-auto p-6 border w-11/12 max-w-6xl shadow-lg rounded-md bg-white">
          <div class="flex items-center justify-between mb-6">
            <div>
              <h3 class="text-lg font-medium text-gray-900">兼容性配置 - {{ selectedProduct?.materialName || '' }}</h3>
              <p class="text-sm text-gray-500 mt-1">管理该产品的后加工工序兼容性</p>
            </div>
            <button @click="closeCompatibilityDialog" class="text-gray-400 hover:text-gray-600">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"/></svg>
            </button>
          </div>

          <!-- 添加兼容性 -->
          <div class="mb-4 bg-gray-50 rounded-lg p-4">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序 *</label>
                <input v-model="newCompatibility.postProcessingStep" type="text" placeholder="工序名称" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">兼容性状态 *</label>
                <select v-model="newCompatibility.compatibilityStatus" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500">
                  <option value="">请选择</option>
                  <option value="V">兼容</option>
                  <option value="X">不兼容</option>
                  <option value="▷">有条件兼容</option>
                </select>
              </div>
              <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">显示顺序</label>
                <input v-model.number="newCompatibility.displayOrder" type="number" placeholder="顺序" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
              </div>
            </div>
            <div class="mt-3">
              <button @click="addCompatibility" class="inline-flex items-center px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 text-sm">
                添加兼容性
              </button>
            </div>
          </div>

          <!-- 兼容性列表 -->
          <div class="overflow-x-auto border rounded-md">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">工序名称</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">兼容性</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">顺序</th>
                  <th class="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="item in compatibilityList" :key="item.id">
                  <td class="px-4 py-2 text-sm">{{ item.postProcessingStep }}</td>
                  <td class="px-4 py-2 text-sm">
                    <span :class="{
                      'bg-green-100 text-green-800': item.compatibilityStatus === 'V',
                      'bg-red-100 text-red-800': item.compatibilityStatus === 'X',
                      'bg-yellow-100 text-yellow-800': item.compatibilityStatus === '▷'
                    }" class="inline-flex px-2 py-1 text-xs font-semibold rounded-full">
                      {{ item.compatibilityStatus === 'V' ? '兼容' : item.compatibilityStatus === 'X' ? '不兼容' : '有条件' }}
                    </span>
                  </td>
                  <td class="px-4 py-2 text-sm">{{ item.displayOrder ?? '-' }}</td>
                  <td class="px-4 py-2 text-sm">
                    <button @click="deleteCompatibility(item.id!)" class="text-red-600 hover:text-red-800">删除</button>
                  </td>
                </tr>
              </tbody>
            </table>
            <div v-if="compatibilityList.length === 0" class="text-center py-8 text-gray-500">暂无兼容性配置</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { siliconeSandblastUvApi, type SiliconeSandblastUvProduct, type SiliconeSandblastUvCompatibility } from '../../../api/modules/silicone_sandblast_uv'

// 数据
const productList = ref<SiliconeSandblastUvProduct[]>([])
const compatibilityList = ref<SiliconeSandblastUvCompatibility[]>([])
const searchKeyword = ref('')

// 对话框状态
const showAddDialog = ref(false)
const showCompatibilityDialog = ref(false)
const selectedProduct = ref<SiliconeSandblastUvProduct | null>(null)

// 表单
const editForm = reactive<SiliconeSandblastUvProduct>({
  materialName: '',
  materialCode: '',
  supplierCode: '',
  stockCode: '',
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

const newCompatibility = reactive({
  postProcessingStep: '',
  compatibilityStatus: '',
  displayOrder: 0
})

// 加载产品
const loadProducts = async () => {
  try {
    productList.value = await siliconeSandblastUvApi.getAllProducts()
  } catch (error) {
    console.error('加载产品失败:', error)
  }
}

// 搜索
const searchProducts = async () => {
  if (searchKeyword.value.trim()) {
    try {
      productList.value = await siliconeSandblastUvApi.searchProducts(searchKeyword.value.trim())
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

// 保存产品
const saveProduct = async () => {
  try {
    if (editForm.id) {
      await siliconeSandblastUvApi.updateProduct(editForm.id, editForm)
    } else {
      await siliconeSandblastUvApi.saveProduct(editForm)
    }
    closeAddDialog()
    await loadProducts()
    alert('保存成功')
  } catch (error) {
    console.error('保存产品失败:', error)
    alert('保存失败')
  }
}

// 编辑
const editProduct = (item: SiliconeSandblastUvProduct) => {
  Object.assign(editForm, item)
  showAddDialog.value = true
}

// 删除
const deleteProduct = async (id: number) => {
  if (!confirm('确定删除该产品？')) return
  try {
    await siliconeSandblastUvApi.deleteProduct(id)
    await loadProducts()
    alert('删除成功')
  } catch (error) {
    console.error('删除产品失败:', error)
    alert('删除失败')
  }
}

// 关闭添加对话框
const closeAddDialog = () => {
  showAddDialog.value = false
  Object.assign(editForm, {
    id: undefined,
    materialName: '',
    materialCode: '',
    supplierCode: '',
    stockCode: '',
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

// 兼容性管理
const showCompatibility = async (product: SiliconeSandblastUvProduct) => {
  selectedProduct.value = product
  showCompatibilityDialog.value = true
  try {
    compatibilityList.value = await siliconeSandblastUvApi.getCompatibilitiesByProductId(product.id!)
  } catch (error) {
    console.error('加载兼容性失败:', error)
    compatibilityList.value = []
  }
}

const closeCompatibilityDialog = () => {
  showCompatibilityDialog.value = false
  selectedProduct.value = null
  compatibilityList.value = []
  newCompatibility.postProcessingStep = ''
  newCompatibility.compatibilityStatus = ''
  newCompatibility.displayOrder = 0
}

const addCompatibility = async () => {
  if (!newCompatibility.postProcessingStep || !newCompatibility.compatibilityStatus) {
    alert('请填写工序名称和兼容性状态')
    return
  }
  if (!selectedProduct.value?.id) return

  try {
    const item: SiliconeSandblastUvCompatibility = {
      productId: selectedProduct.value.id,
      postProcessingStep: newCompatibility.postProcessingStep,
      compatibilityStatus: newCompatibility.compatibilityStatus,
      displayOrder: newCompatibility.displayOrder || 0
    }
    await siliconeSandblastUvApi.saveCompatibility(item)
    newCompatibility.postProcessingStep = ''
    newCompatibility.compatibilityStatus = ''
    newCompatibility.displayOrder = 0
    compatibilityList.value = await siliconeSandblastUvApi.getCompatibilitiesByProductId(selectedProduct.value.id)
  } catch (error) {
    console.error('添加兼容性失败:', error)
    alert('添加失败')
  }
}

const deleteCompatibility = async (id: number) => {
  if (!confirm('确定删除该兼容性配置？')) return
  try {
    await siliconeSandblastUvApi.deleteCompatibility(id)
    if (selectedProduct.value?.id) {
      compatibilityList.value = await siliconeSandblastUvApi.getCompatibilitiesByProductId(selectedProduct.value.id)
    }
  } catch (error) {
    console.error('删除兼容性失败:', error)
    alert('删除失败')
  }
}

onMounted(() => {
  loadProducts()
})
</script>
