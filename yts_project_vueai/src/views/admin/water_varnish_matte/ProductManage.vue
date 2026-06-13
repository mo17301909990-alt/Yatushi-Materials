<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">UV油_哑光水油产品管理</h1>
                <p class="mt-2 text-gray-600">管理UV油_哑光水油产品的配置信息</p>
              </div>
              <div class="flex flex-wrap gap-2 xl:justify-end">
                <button
                  @click="openAddDialog"
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
            <label class="block text-sm font-medium text-gray-700 mb-2">搜索</label>
            <input
              v-model="searchKeyword"
              placeholder="输入物料名称/编号搜索"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
              @keyup.enter="searchProducts"
            />
          </div>
          <div class="flex items-end space-x-3">
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
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">物料名称</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">物料编号</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">颜色</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">材质</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">测试员</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">状态</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="product in productList" :key="product.id" class="hover:bg-gray-50">
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{{ product.id }}</td>
                <td class="px-4 py-3 whitespace-nowrap text-sm font-medium text-gray-900">{{ product.materialName || '-' }}</td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{{ product.materialCode || '-' }}</td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{{ product.color || '-' }}</td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{{ product.materialType || '-' }}</td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-900">{{ product.responsiblePerson || '-' }}</td>
                <td class="px-4 py-3 whitespace-nowrap">
                  <span
                    :class="product.isActive ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'"
                    class="inline-flex px-2 py-1 text-xs font-semibold rounded-full"
                  >
                    {{ product.isActive ? '激活' : '未激活' }}
                  </span>
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm font-medium space-x-2">
                  <button @click="editProduct(product)" class="text-blue-600 hover:text-blue-900">编辑</button>
                  <button @click="viewCompatibilities(product)" class="text-indigo-600 hover:text-indigo-900">兼容性</button>
                  <button @click="deleteProduct(product.id!)" class="text-red-600 hover:text-red-900">删除</button>
                </td>
              </tr>
              <tr v-if="productList.length === 0">
                <td colspan="8" class="px-4 py-12 text-center text-gray-500">暂无产品数据</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 添加/编辑产品对话框 -->
    <div v-if="showDialog" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-10 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
        <div class="flex items-center justify-between mb-4">
          <h3 class="text-lg font-medium text-gray-900">{{ form.id ? '编辑产品' : '添加产品' }}</h3>
          <button @click="closeDialog" class="text-gray-400 hover:text-gray-600">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </button>
        </div>

        <form @submit.prevent="saveProduct" class="space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">物料名称</label>
              <input v-model="form.materialName" placeholder="物料名称" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">物料编号</label>
              <input v-model="form.materialCode" placeholder="物料编号" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">采购编号</label>
              <input v-model="form.supplierCode" placeholder="采购编号" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">物料编码</label>
              <input v-model="form.stockCode" placeholder="物料编码" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">颜色</label>
              <input v-model="form.color" placeholder="颜色" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">材质</label>
              <input v-model="form.materialType" placeholder="材质" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">测试员</label>
              <input v-model="form.responsiblePerson" placeholder="测试员" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">用纸尺寸-最小</label>
              <input v-model="form.minSheetSize" placeholder="最小" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">用纸尺寸-最大</label>
              <input v-model="form.maxSheetSize" placeholder="最大" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">间距-最小</label>
              <input v-model="form.minSpacing" placeholder="间距最小" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">间距-最大</label>
              <input v-model="form.maxSpacing" placeholder="间距最大" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">光泽</label>
              <input v-model="form.gloss" placeholder="光泽" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500" />
            </div>
            <div class="flex items-end">
              <label class="inline-flex items-center space-x-2">
                <input v-model="form.isActive" type="checkbox" class="rounded border-gray-300 text-green-600 focus:ring-green-500" />
                <span class="text-sm text-gray-700">激活</span>
              </label>
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">物料用途</label>
            <textarea v-model="form.usage" rows="2" placeholder="物料用途" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">结构应用</label>
            <textarea v-model="form.structureApplication" rows="2" placeholder="结构应用" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">适用界面</label>
            <textarea v-model="form.applicableInterface" rows="2" placeholder="适用界面" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">注意事项</label>
            <textarea v-model="form.notes" rows="2" placeholder="注意事项" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">写字功能</label>
            <textarea v-model="form.writingFunction" rows="2" placeholder="写字功能" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"></textarea>
          </div>

          <div class="flex justify-end space-x-3 pt-4 border-t border-gray-200">
            <button type="button" @click="closeDialog" class="px-4 py-2 bg-gray-500 text-white rounded-md hover:bg-gray-600">取消</button>
            <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { waterVarnishMatteApi, type WaterVarnishMatteProduct } from '../../../api/modules/waterVarnishMatte'
import { useRouter } from 'vue-router'

const router = useRouter()

// 列表数据
const productList = ref<WaterVarnishMatteProduct[]>([])
const searchKeyword = ref('')

// 对话框状态
const showDialog = ref(false)
const form = reactive<WaterVarnishMatteProduct>({
  isActive: true
})

// 加载产品列表
const loadProducts = async () => {
  try {
    productList.value = await waterVarnishMatteApi.getAllProductsIncludingInactive()
  } catch (error) {
    console.error('加载产品列表失败:', error)
  }
}

// 搜索
const searchProducts = async () => {
  if (searchKeyword.value.trim()) {
    try {
      productList.value = await waterVarnishMatteApi.searchProducts(searchKeyword.value.trim())
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

// 打开添加对话框
const openAddDialog = () => {
  Object.assign(form, { isActive: true })
  showDialog.value = true
}

const closeDialog = () => {
  showDialog.value = false
  Object.assign(form, { isActive: true })
  delete (form as any).id
}

const editProduct = (product: WaterVarnishMatteProduct) => {
  Object.assign(form, product)
  showDialog.value = true
}

const viewCompatibilities = (product: WaterVarnishMatteProduct) => {
  router.push({ name: 'WaterVarnishMatteCompatibility', query: { productId: product.id } })
}

const saveProduct = async () => {
  try {
    if (form.id) {
      await waterVarnishMatteApi.updateProduct(form.id, { ...form })
    } else {
      await waterVarnishMatteApi.saveProduct({ ...form })
    }
    closeDialog()
    await loadProducts()
  } catch (error) {
    console.error('保存产品失败:', error)
    alert('保存失败')
  }
}

const deleteProduct = async (id: number) => {
  if (!confirm('确定要删除这个产品吗？')) return
  try {
    await waterVarnishMatteApi.deleteProduct(id)
    await loadProducts()
  } catch (error) {
    console.error('删除产品失败:', error)
    alert('删除失败')
  }
}

onMounted(() => {
  loadProducts()
})
</script>
