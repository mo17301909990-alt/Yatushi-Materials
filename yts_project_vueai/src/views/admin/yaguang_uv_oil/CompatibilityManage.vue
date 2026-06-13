<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <div class="flex-1 min-w-0">
            <div class="flex flex-col xl:flex-row xl:items-start xl:justify-between gap-4">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">哑光UV油 - 兼容性管理</h1>
                <p class="mt-2 text-gray-600">管理哑光UV油产品与后加工工序的兼容性矩阵（按工序大类分组）</p>
              </div>
              <div class="flex flex-wrap gap-2 xl:justify-end">
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
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
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

      <!-- 兼容性矩阵展示（按工序大类分组） -->
      <div v-if="selectedProductId" class="space-y-6">
        <div v-for="(group, category) in groupedCompatibilities" :key="category" class="bg-white rounded-lg shadow-md overflow-hidden">
          <div class="px-6 py-4 bg-gray-100 border-b border-gray-200">
            <h3 class="text-lg font-semibold text-gray-800">{{ category }}</h3>
          </div>
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-gray-50">
                <tr>
                  <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">工序步骤</th>
                  <th class="px-4 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">兼容性</th>
                  <th class="px-4 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">排序</th>
                  <th class="px-4 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">操作</th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <tr v-for="item in group" :key="item.id" class="hover:bg-gray-50">
                  <td class="px-4 py-3 text-sm font-medium text-gray-900">{{ item.stepName }}</td>
                  <td class="px-4 py-3 text-center">
                    <span
                      class="px-3 py-1 inline-flex text-sm leading-5 font-semibold rounded-full"
                      :class="statusClass(item.compatibilityStatus)"
                    >
                      {{ statusLabel(item.compatibilityStatus) }}
                    </span>
                  </td>
                  <td class="px-4 py-3 text-center text-sm text-gray-500">{{ item.displayOrder ?? '-' }}</td>
                  <td class="px-4 py-3 text-right text-sm font-medium">
                    <button @click="openEditDialog(item)" class="text-indigo-600 hover:text-indigo-900 mr-3">编辑</button>
                    <button @click="confirmDelete(item)" class="text-red-600 hover:text-red-900">删除</button>
                  </td>
                </tr>
                <tr v-if="group.length === 0">
                  <td colspan="4" class="px-4 py-8 text-center text-gray-500">暂无数据</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- 未选择产品时显示提示 -->
      <div v-if="!selectedProductId" class="bg-white rounded-lg shadow-md p-12 text-center text-gray-500">
        请先选择产品以查看兼容性矩阵
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
              <label class="block text-sm font-medium text-gray-700 mb-1">工序大类 <span class="text-red-500">*</span></label>
              <select v-model="compatibilityForm.processCategory" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                <option value="">-- 请选择 --</option>
                <option v-for="cat in processCategories" :key="cat" :value="cat">{{ cat }}</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">步骤名称 <span class="text-red-500">*</span></label>
              <select v-model="compatibilityForm.stepName" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                <option value="">-- 请选择 --</option>
                <option v-for="s in availableSteps" :key="s" :value="s">{{ s }}</option>
              </select>
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
        <div class="relative bg-white rounded-lg shadow-xl max-w-4xl w-full max-h-[90vh] overflow-y-auto p-6">
          <div class="flex items-center justify-between mb-6">
            <h3 class="text-lg font-medium text-gray-900">批量添加兼容性（完整矩阵）</h3>
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
              <label class="block text-sm font-medium text-gray-700 mb-1">默认兼容性状态</label>
              <select v-model="batchStatus" class="w-full px-3 py-2 border border-gray-300 rounded-md">
                <option value="V">V - 兼容</option>
                <option value="X">X - 不兼容</option>
                <option value="▷">▷ - 有条件兼容</option>
              </select>
            </div>
            <div>
              <div class="flex items-center justify-between mb-2">
                <label class="block text-sm font-medium text-gray-700">完整兼容性矩阵（69个步骤，按工序大类分组）</label>
                <div class="space-x-2">
                  <button @click="setAllV" class="px-3 py-1 text-xs bg-green-100 text-green-700 rounded hover:bg-green-200">全部设为V</button>
                  <button @click="setAllX" class="px-3 py-1 text-xs bg-red-100 text-red-700 rounded hover:bg-red-200">全部设为X</button>
                </div>
              </div>
              <!-- 矩阵编辑区 - 按工序大类分组展示 -->
              <div v-for="(steps, cat) in allStepDefinitions" :key="cat" class="mb-4 border border-gray-200 rounded-lg overflow-hidden">
                <div class="px-4 py-2 bg-gray-100 font-medium text-sm text-gray-700 border-b">{{ cat }}</div>
                <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-2 p-3">
                  <div v-for="step in steps" :key="step" class="flex items-center gap-1">
                    <span class="text-xs text-gray-600 truncate flex-1" :title="step">{{ step }}</span>
                    <select v-model="batchMatrix[cat + '::' + step]" class="text-xs border border-gray-300 rounded px-1 py-0.5 w-14">
                      <option value="">--</option>
                      <option value="V">V</option>
                      <option value="X">X</option>
                      <option value="▷">▷</option>
                    </select>
                  </div>
                </div>
              </div>
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
          <p class="text-sm text-gray-500 mb-6">确定要删除步骤「{{ deleteTarget.stepName }}」的兼容性配置吗？</p>
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
import { ref, computed, onMounted } from 'vue';
import { yaguangUvOilApi, type YaguangUvOilProduct, type YaguangUvOilCompatibility } from '@/api/modules/yaguangUvOil';
import { ElMessage } from 'element-plus';

// ========== 兼容性步骤定义（69个步骤，按工序大类分组） ==========
const allStepDefinitions: Record<string, string[]> = {
  '印刷': ['单面印刷', '双面印刷', '连线印刷', '离线印刷', '油性油', '水性油', 'UV油', '扫金粉', '逆向油', '冷烫', '仿皮革', '凸字粉', '凸字粉沟闪粉'],
  '烫金': ['普通烫金', '镭射烫金', '荧光烫金', '折光纹烫金', '立体烫金', '立体烫金击凸', '击凸', '击凹', '立体击凸', '深层击凸', '超级深层击凸', '击纹', '热辣'],
  '过胶': ['单面过胶', '双面过胶', '即涂型过胶', '预涂型过胶'],
  '丝印': ['局部UV', '哑UV', '沟闪粉', '洒闪粉', '凸字油', '有色凸字油', '磨砂UV', '夜光油墨', '皱纹UV', 'LED珍珠', '岩石UV', '橘皮UV', '遇水变透明', '色墨'],
  '植毛': ['普通植毛', '透明植毛', '闪粉植毛', '植毛压纹', '荧光毛', '长毛'],
  '啤': ['机啤', '手啤', '正啤折', '反啤折', '啤切', '啤半穿'],
  '手工': ['压纹', '压竹脊', '烫钻', '粘配件', '涂胶', '闸圆角', '人手扫色边', '机扫色边', '辊金边'],
  '其他': ['镭射介纸', '车线', '粘接', '粘透明可移性贴纸'],
};

const processCategories = Object.keys(allStepDefinitions);

const products = ref<YaguangUvOilProduct[]>([]);
const compatibilities = ref<YaguangUvOilCompatibility[]>([]);
const selectedProductId = ref<number | ''>('');
const showDialog = ref(false);
const showBatchDialog = ref(false);
const isEditing = ref(false);
const deleteTarget = ref<YaguangUvOilCompatibility | null>(null);
const batchProductId = ref<number | ''>('');
const batchStatus = ref('V');
const batchMatrix = ref<Record<string, string>>({});

// 按 process_category 分组
const groupedCompatibilities = computed(() => {
  const groups: Record<string, YaguangUvOilCompatibility[]> = {};
  for (const item of compatibilities.value) {
    const cat = item.processCategory || '其他';
    if (!groups[cat]) groups[cat] = [];
    groups[cat].push(item);
  }
  return groups;
});

// 根据选中的工序大类筛选可用的步骤名
const availableSteps = computed(() => {
  if (!compatibilityForm.value.processCategory) return [];
  return allStepDefinitions[compatibilityForm.value.processCategory] || [];
});

const defaultCompatibilityForm: YaguangUvOilCompatibility = {
  productId: undefined,
  processCategory: '',
  stepName: '',
  compatibilityStatus: 'V',
  displayOrder: 0,
};

const compatibilityForm = ref<YaguangUvOilCompatibility>({ ...defaultCompatibilityForm });

onMounted(() => {
  loadProducts();
});

function loadProducts() {
  yaguangUvOilApi.getAllProducts().then((res: any) => {
    products.value = res.data;
  });
}

function loadCompatibilities() {
  if (!selectedProductId.value) {
    compatibilities.value = [];
    return;
  }
  yaguangUvOilApi.getCompatibilitiesByProductId(Number(selectedProductId.value)).then((res: any) => {
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

function openEditDialog(item: YaguangUvOilCompatibility) {
  isEditing.value = true;
  compatibilityForm.value = { ...item };
  showDialog.value = true;
}

function closeDialog() {
  showDialog.value = false;
}

function saveCompatibility() {
  const apiCall = isEditing.value
    ? yaguangUvOilApi.updateCompatibility(compatibilityForm.value.id!, compatibilityForm.value)
    : yaguangUvOilApi.createCompatibility(compatibilityForm.value);

  apiCall.then(() => {
    ElMessage.success(isEditing.value ? '更新成功' : '新增成功');
    closeDialog();
    loadCompatibilities();
  }).catch((err: any) => {
    const msg = err?.response?.data?.message || '操作失败';
    ElMessage.error(msg);
  });
}

function confirmDelete(item: YaguangUvOilCompatibility) {
  deleteTarget.value = item;
}

function doDelete() {
  if (!deleteTarget.value?.id) return;
  yaguangUvOilApi.deleteCompatibility(deleteTarget.value.id).then(() => {
    ElMessage.success('删除成功');
    deleteTarget.value = null;
    loadCompatibilities();
  }).catch(() => {
    ElMessage.error('删除失败');
  });
}

function openBatchAddDialog() {
  batchProductId.value = selectedProductId.value || '';
  batchStatus.value = 'V';
  // 初始化所有步骤为空
  batchMatrix.value = {};
  for (const [cat, steps] of Object.entries(allStepDefinitions)) {
    for (const step of steps) {
      batchMatrix.value[cat + '::' + step] = '';
    }
  }
  showBatchDialog.value = true;
}

function setAllV() {
  for (const key of Object.keys(batchMatrix.value)) {
    batchMatrix.value[key] = 'V';
  }
}

function setAllX() {
  for (const key of Object.keys(batchMatrix.value)) {
    batchMatrix.value[key] = 'X';
  }
}

function batchSave() {
  if (!batchProductId.value) {
    ElMessage.warning('请选择产品');
    return;
  }

  const compatibilities: YaguangUvOilCompatibility[] = [];
  let order = 1;

  for (const [cat, steps] of Object.entries(allStepDefinitions)) {
    for (const step of steps) {
      const key = cat + '::' + step;
      const status = batchMatrix.value[key];
      if (status) {
        compatibilities.push({
          productId: Number(batchProductId.value),
          processCategory: cat,
          stepName: step,
          compatibilityStatus: status,
          displayOrder: order++,
        });
      }
    }
  }

  if (compatibilities.length === 0) {
    ElMessage.warning('请至少设置一个步骤的兼容性状态');
    return;
  }

  yaguangUvOilApi.batchSaveCompatibilities(compatibilities).then((res: any) => {
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
