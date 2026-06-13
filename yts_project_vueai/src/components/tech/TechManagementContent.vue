<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useHotStampingStore, type Product } from '../../stores/hotStamping';

const hotStampingStore = useHotStampingStore();
const editingItem = ref<Product | null>(null);
const showEditModal = ref(false);

// 獲取所有產品數據
const categories = ref<{
  '普通金纸': Product[];
  '普通耐磨': Product[];
  '高耐磨': Product[];
}>({
  '普通金纸': [],
  '普通耐磨': [],
  '高耐磨': []
});

const loadProducts = () => {
  categories.value = {
    '普通金纸': hotStampingStore.getPapersByType('normal'),
    '普通耐磨': hotStampingStore.getPapersByType('normalWearResistant'),
    '高耐磨': hotStampingStore.getPapersByType('highWearResistant')
  };
};

const startEdit = (item: Product) => {
  editingItem.value = { ...item };
  showEditModal.value = true;
};

const saveEdit = () => {
  if (!editingItem.value) return;

  let type: string = 'normal'; // 默認值
  if (editingItem.value.type === '普通金纸') type = 'normal';
  else if (editingItem.value.type === '普通耐磨') type = 'normalWearResistant';
  else if (editingItem.value.type === '高耐磨') type = 'highWearResistant';

  hotStampingStore.updatePaper(type, editingItem.value);
  showEditModal.value = false;
  editingItem.value = null;
  loadProducts(); // 重新加載數據
};

const cancelEdit = () => {
  showEditModal.value = false;
  editingItem.value = null;
};

onMounted(() => {
  console.log('TechManagementContent mounted');
  loadProducts();
});
</script>

<template>
  <div class="min-h-screen bg-gray-100 pt-20 py-6 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <div class="bg-white rounded-lg shadow px-5 py-6 sm:px-6">
        <div class="flex justify-between items-center mb-6">
          <h1 class="text-2xl font-bold">燙金工藝參數管理</h1>
        </div>

        <!-- 燙金紙列表 -->
        <div class="space-y-8">
          <div v-for="(items, category) in categories" :key="category" class="border-t pt-4">
            <h2 class="text-xl font-semibold mb-4">{{ category }}</h2>
            <div class="grid grid-cols-1 gap-6">
              <div v-for="item in items" :key="item.name"
                class="bg-gray-50 rounded-lg p-6 border border-gray-200">
                <div class="flex justify-between items-start mb-4">
                  <div class="max-h-40 overflow-y-auto pr-4 flex-1">
                    <table class="min-w-full">
                      <tbody>
                        <tr>
                          <td class="py-1 text-gray-600 w-20">名稱:</td>
                          <td class="py-1">{{ item.name }}</td>
                        </tr>
                        <tr>
                          <td class="py-1 text-gray-600">型號:</td>
                          <td class="py-1">{{ item.model }}</td>
                        </tr>
                        <tr>
                          <td class="py-1 text-gray-600">物料號:</td>
                          <td class="py-1">{{ item.id }}</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <button
                    @click="startEdit(item)"
                    class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 ml-4"
                  >
                    編輯
                  </button>
                </div>
                
                <div class="grid grid-cols-2 gap-4 text-sm">
                  <div>
                    <h4 class="font-medium text-gray-700 mb-2">基本信息</h4>
                    <div class="space-y-1 text-gray-600">
                      <p>溫度範圍: {{ item.temperature }}</p>
                      <p>鬆緊度: {{ item.tension }}</p>
                      <p>規格: {{ item.width }} × {{ item.length }}</p>
                    </div>
                  </div>
                  
                  <div>
                    <h4 class="font-medium text-gray-700 mb-2">應用場景</h4>
                    <div class="flex flex-wrap gap-1">
                      <span v-for="scenario in item.scenarios" :key="scenario"
                        class="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs">
                        {{ scenario }}
                      </span>
                    </div>
                  </div>
                  
                  <div class="col-span-2">
                    <h4 class="font-medium text-gray-700 mb-2">特性</h4>
                    <div class="flex flex-wrap gap-1">
                      <span v-for="feature in item.features" :key="feature"
                        class="px-2 py-1 bg-blue-100 text-blue-800 rounded-full text-xs">
                        {{ feature }}
                      </span>
                    </div>
                  </div>
                  
                  <div>
                    <h4 class="font-medium text-gray-700 mb-2">適用紙面</h4>
                    <div class="flex flex-wrap gap-1">
                      <span v-for="surface in item.validSurfaces" :key="surface"
                        class="px-2 py-1 bg-purple-100 text-purple-800 rounded-full text-xs">
                        {{ surface }}
                      </span>
                    </div>
                  </div>
                  
                  <div>
                    <h4 class="font-medium text-gray-700 mb-2">適用面積</h4>
                    <div class="flex flex-wrap gap-1">
                      <span v-for="area in item.validAreas" :key="area"
                        class="px-2 py-1 bg-yellow-100 text-yellow-800 rounded-full text-xs">
                        {{ area }}
                      </span>
                    </div>
                  </div>

                  <div class="col-span-2">
                    <h4 class="font-medium text-gray-700 mb-2">兼容性信息</h4>
                    <div class="space-y-2">
                      <!-- 过胶兼容性 -->
                      <div class="flex items-center gap-2">
                        <span class="text-gray-600">過膠兼容性:</span>
                        <span :class="{
                          'text-green-600': item.laminatingCompatibility.status === 'all',
                          'text-yellow-600': item.laminatingCompatibility.status === 'partial',
                          'text-red-600': item.laminatingCompatibility.status === 'none'
                        }">
                          {{ item.laminatingCompatibility.status === 'all' ? '全兼容' : 
                             item.laminatingCompatibility.status === 'partial' ? '部分兼容' : '不兼容' }}
                        </span>
                        <div v-if="item.laminatingCompatibility.compatibleTypes?.length" class="flex flex-wrap gap-1">
                          <span v-for="type in item.laminatingCompatibility.compatibleTypes" :key="type"
                            class="px-2 py-1 bg-gray-100 text-gray-800 rounded-full text-xs">
                            {{ type }}
                          </span>
                        </div>
                      </div>

                      <!-- 其他兼容性 -->
                      <div class="grid grid-cols-2 gap-2">
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">UV印刷:</span>
                          <span :class="item.uvPrintingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ item.uvPrintingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">LED UV閃光:</span>
                          <span :class="item.ledUvGlitterCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ item.ledUvGlitterCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">印刷:</span>
                          <span :class="item.printingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ item.printingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">平面燙金:</span>
                          <span :class="item.flatStampingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ item.flatStampingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">浮雕燙金:</span>
                          <span :class="item.reliefStampingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ item.reliefStampingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">啞面燙金:</span>
                          <span :class="item.matteStampingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ item.matteStampingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                        <div class="flex items-center gap-2">
                          <span class="text-gray-600">閃光燙金:</span>
                          <span :class="item.glitterStampingCompatible ? 'text-green-600' : 'text-red-600'">
                            {{ item.glitterStampingCompatible ? '兼容' : '不兼容' }}
                          </span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑模态框 -->
    <div v-if="showEditModal" class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50">
      <div class="relative top-20 mx-auto p-5 border w-11/12 max-w-4xl shadow-lg rounded-md bg-white">
        <div class="mt-3">
          <div class="flex justify-between items-center mb-6">
            <h3 class="text-lg font-medium text-gray-900">編輯燙金紙參數</h3>
            <button @click="cancelEdit" class="text-gray-400 hover:text-gray-600">
              <span class="sr-only">關閉</span>
              <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <div v-if="editingItem" class="space-y-6 max-h-96 overflow-y-auto">
            <!-- 基本信息 -->
            <div>
              <h4 class="font-medium mb-4">基本信息</h4>
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700">溫度範圍</label>
                  <input
                    v-model="editingItem.temperature"
                    type="text"
                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700">鬆緊度</label>
                  <select
                    v-model="editingItem.tension"
                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                  >
                    <option value="紧身">緊身</option>
                    <option value="标准">標準</option>
                    <option value="松身">鬆身</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700">寬度</label>
                  <input
                    v-model="editingItem.width"
                    type="text"
                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700">長度</label>
                  <input
                    v-model="editingItem.length"
                    type="text"
                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                  />
                </div>
              </div>
            </div>

            <!-- 应用场景 -->
            <div>
              <h4 class="font-medium mb-4">應用場景</h4>
              <div class="flex flex-wrap gap-4">
                <div v-for="scenario in ['賀卡', '包裝', '精平裝', '卡書', '綜合']" :key="scenario"
                  class="flex items-center">
                  <input
                    type="checkbox"
                    :value="scenario"
                    v-model="editingItem.scenarios"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                  <label class="ml-2 text-sm text-gray-700">{{ scenario }}</label>
                </div>
              </div>
            </div>

            <!-- 兼容性设置 -->
            <div>
              <h4 class="font-medium mb-4">兼容性設置</h4>
              <div class="grid grid-cols-2 gap-4">
                <div class="flex items-center">
                  <input
                    type="checkbox"
                    v-model="editingItem.uvPrintingCompatible"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                  <label class="ml-2 text-sm text-gray-700">UV印刷兼容</label>
                </div>
                <div class="flex items-center">
                  <input
                    type="checkbox"
                    v-model="editingItem.printingCompatible"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                  <label class="ml-2 text-sm text-gray-700">印刷兼容</label>
                </div>
                <div class="flex items-center">
                  <input
                    type="checkbox"
                    v-model="editingItem.flatStampingCompatible"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                  <label class="ml-2 text-sm text-gray-700">平面燙金兼容</label>
                </div>
                <div class="flex items-center">
                  <input
                    type="checkbox"
                    v-model="editingItem.reliefStampingCompatible"
                    class="h-4 w-4 text-indigo-600 focus:ring-indigo-500 border-gray-300 rounded"
                  />
                  <label class="ml-2 text-sm text-gray-700">浮雕燙金兼容</label>
                </div>
              </div>
            </div>
          </div>

          <div class="mt-6 flex justify-end space-x-3">
            <button
              @click="cancelEdit"
              class="px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              取消
            </button>
            <button
              @click="saveEdit"
              class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700"
            >
              保存
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
