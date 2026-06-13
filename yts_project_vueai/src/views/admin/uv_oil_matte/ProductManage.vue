<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">UV油/哑光UV油 - 产品管理</h1>
                <p class="mt-2 text-gray-600">管理UV油与哑光UV油的物料产品信息</p>
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
              @input="onSearch"
              type="text"
              placeholder="按物料名称/编码/备注搜索"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
        </div>
      </div>

      <!-- 产品列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">物料名称</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">物料编码</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">存仓物料号</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">颜色</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">材质</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">测试员</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">状态</th>
                <th class="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="product in products" :key="product.id" class="hover:bg-gray-50">
                <td class="px-4 py-3 text-sm text-gray-500">{{ product.id }}</td>
                <td class="px-4 py-3 text-sm font-medium text-gray-900">{{ product.materialName || '-' }}</td>
                <td class="px-4 py-3 text-sm text-gray-500">{{ product.materialCode || '-' }}</td>
                <td class="px-4 py-3 text-sm text-gray-500">{{ product.stockCode || '-' }}</td>
                <td class="px-4 py-3 text-sm text-gray-500">{{ product.color || '-' }}</td>
                <td class="px-4 py-3 text-sm text-gray-500">{{ product.category || '-' }}</td>
                <td class="px-4 py-3 text-sm text-gray-500">{{ product.responsiblePerson || '-' }}</td>
                <td class="px-4 py-3 text-sm">
                  <span
                    class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                    :class="product.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                  >
                    {{ product.isActive ? '激活' : '停用' }}
                  </span>
                </td>
                <td class="px-4 py-3 text-right text-sm font-medium">
                  <button @click="openEditDialog(product)" class="text-indigo-600 hover:text-indigo-900 mr-3">编辑</button>
                  <button @click="confirmDelete(product)" class="text-red-600 hover:text-red-900">删除</button>
                </td>
              </tr>
              <tr v-if="products.length === 0">
                <td colspan="9" class="px-4 py-8 text-center text-gray-500">暂无数据</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 新增/编辑产品对话框 -->
    <div v-if="showDialog" class="fixed inset-0 z-50 overflow-y-auto" @click.self="closeDialog">
      <div class="flex items-center justify-center min-h-screen px-4">
        <div class="fixed inset-0 bg-black opacity-40"></div>
        <div class="relative bg-white rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] overflow-y-auto p-6">
          <div class="flex items-center justify-between mb-6">
            <h3 class="text-lg font-medium text-gray-900">{{ isEditing ? '编辑产品' : '新增产品' }}</h3>
            <button @click="closeDialog" class="text-gray-400 hover:text-gray-500">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">物料名称</label>
              <input v-model="form.materialName" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">物料编码</label>
              <input v-model="form.materialCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">供应商编码</label>
              <input v-model="form.supplierCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">存仓物料号</label>
              <input v-model="form.stockCode" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">颜色</label>
              <input v-model="form.color" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">材质分类</label>
              <input v-model="form.category" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">测试员</label>
              <input v-model="form.responsiblePerson" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">状态</label>
              <select v-model="form.isActive" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                <option :value="true">激活</option>
                <option :value="false">停用</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">用纸尺寸(最小mm)</label>
              <input v-model="form.minSheetSize" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">用纸尺寸(最大mm)</label>
              <input v-model="form.maxSheetSize" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">间距(最小mm)</label>
              <input v-model="form.minSpacing" type="text" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-1">物料用途</label>
              <textarea v-model="form.usage" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md"></textarea>
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-1">设计限制信息</label>
              <textarea v-model="form.designInfo" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md"></textarea>
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-1">适用界面</label>
              <textarea v-model="form.applicableInterface" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md"></textarea>
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-gray-700 mb-1">备注说明</label>
              <textarea v-model="form.notes" rows="2" class="w-full px-3 py-2 border border-gray-300 rounded-md"></textarea>
            </div>
          </div>

          <div class="mt-6 flex justify-end gap-3">
            <button @click="closeDialog" class="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50">取消</button>
            <button @click="saveProduct" class="px-4 py-2 bg-green-600 text-white rounded-md text-sm hover:bg-green-700">保存</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 删除确认对话框 -->
    <div v-if="deleteTarget" class="fixed inset-0 z-50 overflow-y-auto" @click.self="deleteTarget = null">
      <div class="flex items-center justify-center min-h-screen px-4">
        <div class="fixed inset-0 bg-black opacity-40"></div>
        <div class="relative bg-white rounded-lg shadow-xl max-w-md w-full p-6">
          <h3 class="text-lg font-medium text-gray-900 mb-4">确认删除</h3>
          <p class="text-sm text-gray-500 mb-6">确定要删除「{{ deleteTarget.materialName }}」吗？此操作不可撤销。</p>
          <div class="flex justify-end gap-3">
            <button @click="deleteTarget = null" class="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50">取消</button>
            <button @click="doDelete" class="px-4 py-2 bg-red-600 text-white rounded-md text-sm hover:bg-red-700">确认删除</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { uvOilMatteApi, type UvOilMatteProduct } from '@/api/modules/uvOilMatte';
import { ElMessage } from 'element-plus';

const products = ref<UvOilMatteProduct[]>([]);
const searchKeyword = ref('');
const showDialog = ref(false);
const isEditing = ref(false);
const deleteTarget = ref<UvOilMatteProduct | null>(null);

const defaultForm: UvOilMatteProduct = {
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
  isActive: true,
};

const form = ref<UvOilMatteProduct>({ ...defaultForm });

onMounted(() => {
  loadProducts();
});

let searchTimer: ReturnType<typeof setTimeout> | null = null;

function loadProducts() {
  const keyword = searchKeyword.value.trim();
  if (keyword) {
    uvOilMatteApi.searchProducts(keyword).then((res: any) => {
      products.value = res.data;
    });
  } else {
    uvOilMatteApi.getAllProducts().then((res: any) => {
      products.value = res.data;
    });
  }
}

function onSearch() {
  if (searchTimer) clearTimeout(searchTimer);
  searchTimer = setTimeout(() => {
    loadProducts();
  }, 300);
}

function openAddDialog() {
  isEditing.value = false;
  form.value = { ...defaultForm };
  showDialog.value = true;
}

function openEditDialog(product: UvOilMatteProduct) {
  isEditing.value = true;
  form.value = { ...product };
  showDialog.value = true;
}

function closeDialog() {
  showDialog.value = false;
}

function saveProduct() {
  const apiCall = isEditing.value
    ? uvOilMatteApi.updateProduct(form.value.id!, form.value)
    : uvOilMatteApi.createProduct(form.value);

  apiCall.then(() => {
    ElMessage.success(isEditing.value ? '更新成功' : '新增成功');
    closeDialog();
    loadProducts();
  }).catch(() => {
    ElMessage.error('操作失败');
  });
}

function confirmDelete(product: UvOilMatteProduct) {
  deleteTarget.value = product;
}

function doDelete() {
  if (!deleteTarget.value?.id) return;
  uvOilMatteApi.deleteProduct(deleteTarget.value.id).then(() => {
    ElMessage.success('删除成功');
    deleteTarget.value = null;
    loadProducts();
  }).catch(() => {
    ElMessage.error('删除失败');
  });
}
</script>
