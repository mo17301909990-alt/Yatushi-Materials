<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 頁面標題 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">UV油/哑光UV油 - 兼容性管理</h1>
                <p class="mt-2 text-gray-600">管理UV油与哑光UV油产品与后加工工序的兼容性配置</p>
              </div>
              <div class="flex flex-wrap gap-2 xl:justify-end">
                <button
                  @click="openAddDialog"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                  </svg>
                  添加兼容性
                </button>
                <button
                  @click="openBatchAddDialog"
                  class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
                >
                  <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
                  </svg>
                  批量添加
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 筛选 -->
      <div class="bg-white rounded-lg shadow-md p-6 mb-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-2">选择产品</label>
            <select
              v-model="selectedProductId"
              @change="loadCompatibilities"
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-green-500"
            >
              <option value="">-- 请选择产品 --</option>
              <option v-for="p in products" :key="p.id" :value="p.id">{{ p.materialName }} ({{ p.stockCode || p.materialCode }})</option>
            </select>
          </div>
        </div>
      </div>

      <!-- 兼容性列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">产品</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">后加工工序</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性状态</th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">显示顺序</th>
                <th class="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in compatibilities" :key="item.id" class="hover:bg-gray-50">
                <td class="px-4 py-3 text-sm text-gray-500">{{ item.id }}</td>
                <td class="px-4 py-3 text-sm text-gray-900">{{ item.productName || '-' }}</td>
                <td class="px-4 py-3 text-sm text-gray-500">{{ item.postProcessingStep || '-' }}</td>
                <td class="px-4 py-3 text-sm">
                  <span
                    class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                    :class="statusClass(item.compatibilityStatus)"
                  >
                    {{ statusLabel(item.compatibilityStatus) }}
                  </span>
                </td>
                <td class="px-4 py-3 text-sm text-gray-500">{{ item.displayOrder ?? '-' }}</td>
                <td class="px-4 py-3 text-right text-sm font-medium">
                  <button @click="openEditDialog(item)" class="text-indigo-600 hover:text-indigo-900 mr-3">编辑</button>
                  <button @click="confirmDelete(item)" class="text-red-600 hover:text-red-900">删除</button>
                </td>
              </tr>
              <tr v-if="compatibilities.length === 0">
                <td colspan="6" class="px-4 py-8 text-center text-gray-500">请先选择产品，或暂无兼容性数据</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 新增/编辑兼容性对话框 -->
    <div v-if="showDialog" class="fixed inset-0 z-50 overflow-y-auto" @click.self="closeDialog">
      <div class="flex items-center justify-center min-h-screen px-4">
        <div class="fixed inset-0 bg-black opacity-40"></div>
        <div class="relative bg-white rounded-lg shadow-xl max-w-lg w-full p-6">
          <div class="flex items-center justify-between mb-6">
            <h3 class="text-lg font-medium text-gray-900">{{ isEditing ? '编辑兼容性' : '新增兼容性' }}</h3>
            <button @click="closeDialog" class="text-gray-400 hover:text-gray-500">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">产品 <span class="text-red-500">*</span></label>
              <select v-model="compatibilityForm.productId" class="w-full px-3 py-2 border border-gray-300 rounded-md" :disabled="isEditing">
                <option value="">-- 请选择产品 --</option>
                <option v-for="p in products" :key="p.id" :value="p.id">{{ p.materialName }} ({{ p.stockCode || p.materialCode }})</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序 <span class="text-red-500">*</span></label>
              <input v-model="compatibilityForm.postProcessingStep" type="text" placeholder="如：单面过胶、烫金、击凸等" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">兼容性状态 <span class="text-red-500">*</span></label>
              <select v-model="compatibilityForm.compatibilityStatus" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                <option value="V">V - 兼容</option>
                <option value="X">X - 不兼容</option>
                <option value="▷">▷ - 有条件兼容</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">显示顺序</label>
              <input v-model.number="compatibilityForm.displayOrder" type="number" min="0" class="w-full px-3 py-2 border border-gray-300 rounded-md" />
            </div>
          </div>

          <div class="mt-6 flex justify-end gap-3">
            <button @click="closeDialog" class="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50">取消</button>
            <button @click="saveCompatibility" class="px-4 py-2 bg-green-600 text-white rounded-md text-sm hover:bg-green-700">保存</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 批量添加对话框 -->
    <div v-if="showBatchDialog" class="fixed inset-0 z-50 overflow-y-auto" @click.self="showBatchDialog = false">
      <div class="flex items-center justify-center min-h-screen px-4">
        <div class="fixed inset-0 bg-black opacity-40"></div>
        <div class="relative bg-white rounded-lg shadow-xl max-w-3xl w-full max-h-[90vh] overflow-y-auto p-6">
          <div class="flex items-center justify-between mb-6">
            <h3 class="text-lg font-medium text-gray-900">批量添加兼容性</h3>
            <button @click="showBatchDialog = false" class="text-gray-400 hover:text-gray-500">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
              </svg>
            </button>
          </div>

          <div class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">选择产品 <span class="text-red-500">*</span></label>
              <select v-model="batchProductId" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                <option value="">-- 请选择产品 --</option>
                <option v-for="p in products" :key="p.id" :value="p.id">{{ p.materialName }} ({{ p.stockCode || p.materialCode }})</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序列表（每行一个） <span class="text-red-500">*</span></label>
              <textarea
                v-model="batchStepsText"
                rows="8"
                placeholder="单面过胶&#10;烫金&#10;击凸&#10;机啤&#10;..."
                class="w-full px-3 py-2 border border-gray-300 rounded-md"
              ></textarea>
              <p class="mt-1 text-xs text-gray-500">每行输入一个后加工工序名称</p>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">兼容性状态 <span class="text-red-500">*</span></label>
              <select v-model="batchStatus" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                <option value="V">V - 兼容</option>
                <option value="X">X - 不兼容</option>
                <option value="▷">▷ - 有条件兼容</option>
              </select>
            </div>
          </div>

          <div class="mt-6 flex justify-end gap-3">
            <button @click="showBatchDialog = false" class="px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-50">取消</button>
            <button @click="batchSave" class="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm hover:bg-indigo-700">批量保存</button>
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
          <p class="text-sm text-gray-500 mb-6">确定要删除「{{ deleteTarget.postProcessingStep }}」的兼容性配置吗？</p>
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
import { uvOilMatteApi, type UvOilMatteProduct, type UvOilMatteCompatibility } from '@/api/modules/uvOilMatte';
import { ElMessage } from 'element-plus';

const products = ref<UvOilMatteProduct[]>([]);
const compatibilities = ref<UvOilMatteCompatibility[]>([]);
const selectedProductId = ref<number | ''>('');
const showDialog = ref(false);
const showBatchDialog = ref(false);
const isEditing = ref(false);
const deleteTarget = ref<UvOilMatteCompatibility | null>(null);
const batchProductId = ref<number | ''>('');
const batchStepsText = ref('');
const batchStatus = ref('V');

const defaultCompatibilityForm: UvOilMatteCompatibility = {
  productId: undefined,
  postProcessingStep: '',
  compatibilityStatus: 'V',
  displayOrder: 0,
};

const compatibilityForm = ref<UvOilMatteCompatibility>({ ...defaultCompatibilityForm });

onMounted(() => {
  loadProducts();
});

function loadProducts() {
  uvOilMatteApi.getAllProducts().then((res: any) => {
    products.value = res.data;
  });
}

function loadCompatibilities() {
  if (!selectedProductId.value) {
    compatibilities.value = [];
    return;
  }
  uvOilMatteApi.getCompatibilitiesByProductId(Number(selectedProductId.value)).then((res: any) => {
    compatibilities.value = res.data;
  });
}

function statusClass(status?: string) {
  if (status === 'V') return 'bg-green-100 text-green-800';
  if (status === 'X') return 'bg-red-100 text-red-800';
  if (status === '▷') return 'bg-yellow-100 text-yellow-800';
  return 'bg-gray-100 text-gray-800';
}

function statusLabel(status?: string) {
  if (status === 'V') return 'V - 兼容';
  if (status === 'X') return 'X - 不兼容';
  if (status === '▷') return '▷ - 有条件兼容';
  return status || '-';
}

function openAddDialog() {
  isEditing.value = false;
  compatibilityForm.value = {
    ...defaultCompatibilityForm,
    productId: selectedProductId.value || undefined,
  };
  showDialog.value = true;
}

function openEditDialog(item: UvOilMatteCompatibility) {
  isEditing.value = true;
  compatibilityForm.value = { ...item };
  showDialog.value = true;
}

function closeDialog() {
  showDialog.value = false;
}

function saveCompatibility() {
  const apiCall = isEditing.value
    ? uvOilMatteApi.updateCompatibility(compatibilityForm.value.id!, compatibilityForm.value)
    : uvOilMatteApi.createCompatibility(compatibilityForm.value);

  apiCall.then(() => {
    ElMessage.success(isEditing.value ? '更新成功' : '新增成功');
    closeDialog();
    loadCompatibilities();
  }).catch((err: any) => {
    const msg = err?.response?.data?.message || '操作失败';
    ElMessage.error(msg);
  });
}

function confirmDelete(item: UvOilMatteCompatibility) {
  deleteTarget.value = item;
}

function doDelete() {
  if (!deleteTarget.value?.id) return;
  uvOilMatteApi.deleteCompatibility(deleteTarget.value.id).then(() => {
    ElMessage.success('删除成功');
    deleteTarget.value = null;
    loadCompatibilities();
  }).catch(() => {
    ElMessage.error('删除失败');
  });
}

function openBatchAddDialog() {
  batchProductId.value = selectedProductId.value || '';
  batchStepsText.value = '';
  batchStatus.value = 'V';
  showBatchDialog.value = true;
}

function batchSave() {
  if (!batchProductId.value) {
    ElMessage.warning('请选择产品');
    return;
  }
  const steps = batchStepsText.value
    .split('\n')
    .map(s => s.trim())
    .filter(s => s.length > 0);

  if (steps.length === 0) {
    ElMessage.warning('请输入至少一个后加工工序');
    return;
  }

  const compatibilities: UvOilMatteCompatibility[] = steps.map((step, index) => ({
    productId: Number(batchProductId.value),
    postProcessingStep: step,
    compatibilityStatus: batchStatus.value,
    displayOrder: index + 1,
  }));

  uvOilMatteApi.batchSaveCompatibilities(compatibilities).then((res: any) => {
    if (res.data?.success) {
      ElMessage.success(res.data.message || '批量保存成功');
    } else {
      ElMessage.success('批量保存成功');
    }
    showBatchDialog.value = false;
    loadCompatibilities();
  }).catch((err: any) => {
    const msg = err?.response?.data?.message || '批量保存失败';
    ElMessage.error(msg);
  });
}
</script>
