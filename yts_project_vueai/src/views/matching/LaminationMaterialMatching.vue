<template>
  <div class="min-h-screen bg-gray-100 pt-20 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- 页面标题 -->
      <div class="mb-6">
        <h1 class="text-2xl font-bold text-gray-900">过胶材料匹配</h1>
        <p class="mt-1 text-sm text-gray-500">按材料类型和后加工工序搜索匹配的过胶材料</p>
      </div>

      <!-- 搜索与筛选 -->
      <div class="bg-white rounded-lg shadow px-5 py-5 mb-6">
        <div class="flex flex-wrap items-end gap-4">
          <!-- 关键词搜索 -->
          <div class="flex-1 min-w-[200px]">
            <label class="block text-sm font-medium text-gray-700 mb-1">关键词搜索</label>
            <el-input
              v-model="keyword"
              placeholder="物料名称 / 编码 / 型号"
              clearable
              @keyup.enter="onSearch"
            />
          </div>

          <!-- 材料类型多选 -->
          <div class="w-56">
            <label class="block text-sm font-medium text-gray-700 mb-1">材料类型</label>
            <el-select
              v-model="selectedMaterialTypes"
              multiple
              collapse-tags
              collapse-tags-tooltip
              placeholder="全部类型"
              clearable
              class="w-full"
              @change="onSearch"
            >
              <el-option
                v-for="type in materialTypes"
                :key="type"
                :label="type"
                :value="type"
              />
            </el-select>
          </div>

          <!-- 后加工工序多选 -->
          <div class="w-72">
            <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序</label>
            <el-select
              v-model="selectedSteps"
              multiple
              collapse-tags
              collapse-tags-tooltip
              placeholder="全部工序"
              clearable
              class="w-full"
              @change="onSearch"
            >
              <el-option-group
                v-for="group in groupedSteps"
                :key="group.label"
                :label="group.label"
              >
                <el-option
                  v-for="step in group.steps"
                  :key="step"
                  :label="step"
                  :value="step"
                />
              </el-option-group>
            </el-select>
          </div>

          <!-- 搜索按钮 -->
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1 invisible">搜索</label>
            <el-button type="primary" @click="onSearch">
              <el-icon class="mr-1"><Search /></el-icon>
              搜索
            </el-button>
          </div>
        </div>

        <!-- 已选条件标签 -->
        <div v-if="selectedMaterialTypes.length > 0 || selectedSteps.length > 0" class="flex flex-wrap items-center gap-2 mt-3 pt-3 border-t border-gray-100">
          <span class="text-xs text-gray-400">已选条件：</span>
          <el-tag
            v-for="type in selectedMaterialTypes"
            :key="'mt-' + type"
            closable
            size="small"
            type="primary"
            @close="removeMaterialType(type)"
          >
            类型: {{ type }}
          </el-tag>
          <el-tag
            v-for="step in selectedSteps"
            :key="'st-' + step"
            closable
            size="small"
            type="success"
            @close="removeStep(step)"
          >
            工序: {{ step }}
          </el-tag>
          <el-button link type="primary" size="small" @click="clearAllFilters">清空全部</el-button>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="flex justify-center py-12">
        <el-icon class="is-loading text-2xl text-indigo-500"><Loading /></el-icon>
        <span class="ml-3 text-gray-500">加载中...</span>
      </div>

      <!-- 结果统计 -->
      <div v-if="!loading && pagedResult" class="flex items-center justify-between mb-4">
        <p class="text-sm text-gray-500">
          共 <span class="font-medium text-gray-700">{{ pagedResult.total }}</span> 条结果
        </p>
      </div>

      <!-- 空状态 -->
      <div
        v-if="!loading && pagedResult && pagedResult.items.length === 0"
        class="bg-white rounded-lg shadow py-16 text-center"
      >
        <el-empty description="暂无匹配结果">
          <template #image>
            <svg class="w-24 h-24 mx-auto text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </template>
          <p class="text-gray-400 mt-2">
            {{ hasActiveFilters ? '未找到匹配条件的产品，请调整搜索条件' : '暂无数据，请点击搜索按钮' }}
          </p>
        </el-empty>
      </div>

      <!-- 结果表格 -->
      <div
        v-if="!loading && pagedResult && pagedResult.items.length > 0"
        class="bg-white rounded-lg shadow overflow-hidden"
      >
        <el-table :data="pagedResult.items" stripe style="width: 100%">
          <el-table-column label="物料编码" min-width="140">
            <template #default="{ row }">
              {{ row.materialCode ?? '-' }}
            </template>
          </el-table-column>
          <el-table-column label="物料名称" min-width="160">
            <template #default="{ row }">
              {{ row.materialName ?? '-' }}
            </template>
          </el-table-column>
          <el-table-column label="材料类型" min-width="100">
            <template #default="{ row }">
              <el-tag size="small" effect="plain">{{ row.materialType ?? '-' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="膜厚" min-width="90">
            <template #default="{ row }">
              {{ row.thicknessFilm ?? '-' }}
            </template>
          </el-table-column>
          <el-table-column label="胶厚" min-width="90">
            <template #default="{ row }">
              {{ row.thicknessGlue ?? '-' }}
            </template>
          </el-table-column>
          <el-table-column label="形状" min-width="90">
            <template #default="{ row }">
              {{ row.shape ?? '-' }}
            </template>
          </el-table-column>
          <el-table-column label="颜色" min-width="90">
            <template #default="{ row }">
              {{ row.color ?? '-' }}
            </template>
          </el-table-column>
          <el-table-column label="兼容工序" min-width="280">
            <template #default="{ row }">
              <div class="flex flex-wrap gap-1">
                <template v-if="compatibilityMap.get(row.id!) && compatibilityMap.get(row.id!)!.length > 0">
                  <span
                    v-for="comp in compatibilityMap.get(row.id!)"
                    :key="comp.id"
                    class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium"
                    :class="statusBadgeClass(comp.compatibilityStatus)"
                  >
                    {{ comp.postProcessingStep }}: {{ comp.compatibilityStatus }}
                  </span>
                </template>
                <span v-else class="text-gray-400 text-xs">加载中...</span>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页 -->
      <div
        v-if="pagedResult && pagedResult.totalPages > 1"
        class="flex justify-center mt-6 pb-8"
      >
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="pagedResult.total"
          layout="prev, pager, next, total"
          background
          @current-change="onPageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { laminationMaterialApi, type LaminationMaterialProduct, type PagedItems } from '@/api/modules/laminationMaterial';
import { Search, Loading } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

// ========== 搜索条件 ==========
const keyword = ref('');
const selectedMaterialTypes = ref<string[]>([]);
const selectedSteps = ref<string[]>([]);
const materialTypes = ref<string[]>([]);
const steps = ref<string[]>([]);
const loading = ref(false);
const pageSize = ref(15);
const currentPage = ref(1);

// ========== 结果 ==========
const pagedResult = ref<PagedItems<LaminationMaterialProduct> | null>(null);

// 兼容性数据：productId -> compatibility[]
const compatibilityMap = ref<Map<number, any[]>>(new Map());

// ========== 计算属性 ==========

const hasActiveFilters = computed(() =>
  keyword.value !== '' || selectedMaterialTypes.value.length > 0 || selectedSteps.value.length > 0
);

// 工序分组配置
const stepGroupConfig = [
  { label: '烫金', keywords: ['烫金'] },
  { label: 'UV', keywords: ['UV', 'uv', 'Uv'] },
  { label: '丝印', keywords: ['丝印'] },
  { label: '过胶', keywords: ['過膠', '过胶', '即塗型過膠', '預塗型過膠'] },
  { label: '裱纸', keywords: ['裱纸', '裱紙'] },
  { label: '印刷', keywords: ['印刷', '油墨'] },
  { label: '击凸/击凹', keywords: ['擊凸', '击凸', '擊凹', '击凹', '壓紋', '压纹', '擊紋', '击纹'] },
  { label: '啤切', keywords: ['啤', '切'] },
  { label: '植毛/闪粉', keywords: ['植毛', '閃粉', '闪粉', '金粉', '金邊', '金边'] },
  { label: '后加工', keywords: ['後加工', '后加工', '后道'] },
  { label: '其他', keywords: [] as string[] },
];

/** 对步骤进行分组 */
const groupedSteps = computed(() => {
  const groups = stepGroupConfig.map(g => ({ label: g.label, steps: [] as string[] }));
  steps.value.forEach(step => {
    let matched = false;
    for (let i = 0; i < stepGroupConfig.length - 1; i++) {
      if (stepGroupConfig[i].keywords.some(kw => step.includes(kw))) {
        groups[i].steps.push(step);
        matched = true;
        break;
      }
    }
    if (!matched) {
      groups[groups.length - 1].steps.push(step);
    }
  });
  return groups.filter(g => g.steps.length > 0);
});

// ========== 生命周期 ==========

onMounted(() => {
  loadMaterialTypes();
  loadSteps();
  loadMatch();
});

// ========== 数据加载 ==========

/** 加载材料类型选项 */
async function loadMaterialTypes() {
  try {
    const res: any = await laminationMaterialApi.getMaterialTypes();
    materialTypes.value = res || [];
  } catch {
    // 静默失败
  }
}

/** 加载工序下拉选项 */
async function loadSteps() {
  try {
    const res: any = await laminationMaterialApi.getSteps();
    steps.value = res || [];
  } catch {
    // 静默失败
  }
}

/** 执行匹配查询 */
async function loadMatch() {
  loading.value = true;
  compatibilityMap.value = new Map();
  try {
    const params: any = {
      page: currentPage.value,
      size: pageSize.value,
    };
    if (keyword.value) params.keyword = keyword.value;
    if (selectedMaterialTypes.value.length > 0) params.materialTypes = [...selectedMaterialTypes.value];
    if (selectedSteps.value.length > 0) params.steps = [...selectedSteps.value];

    const res: any = await laminationMaterialApi.match(params);
    pagedResult.value = res || null;
    // 加载兼容性数据
    if (res && res.items && res.items.length > 0) {
      await loadCompatibilities(res.items);
    }
  } catch {
    ElMessage.error('查询失败，请重试');
    pagedResult.value = { items: [], total: 0, pageSize: pageSize.value, currentPage: currentPage.value, totalPages: 0 };
  } finally {
    loading.value = false;
  }
}

/** 批量加载兼容性数据 */
async function loadCompatibilities(items: LaminationMaterialProduct[]) {
  const promises = items.map(async (product) => {
    if (!product.id) return;
    try {
      const detail: any = await laminationMaterialApi.getProductDetail(product.id);
      if (detail && detail.compatibilities) {
        compatibilityMap.value.set(product.id, detail.compatibilities);
      } else {
        compatibilityMap.value.set(product.id, []);
      }
    } catch {
      compatibilityMap.value.set(product.id, []);
    }
  });
  await Promise.all(promises);
}

// ========== 事件处理 ==========

function onSearch() {
  currentPage.value = 1;
  loadMatch();
}

function onPageChange() {
  loadMatch();
}

function removeMaterialType(type: string) {
  selectedMaterialTypes.value = selectedMaterialTypes.value.filter(t => t !== type);
  onSearch();
}

function removeStep(step: string) {
  selectedSteps.value = selectedSteps.value.filter(s => s !== step);
  onSearch();
}

function clearAllFilters() {
  selectedMaterialTypes.value = [];
  selectedSteps.value = [];
  keyword.value = '';
  onSearch();
}

/** 兼容性状态徽标样式 */
function statusBadgeClass(status?: string): string {
  switch (status) {
    case 'V':
      return 'bg-green-100 text-green-700';
    case 'X':
      return 'bg-red-100 text-red-700';
    case '△':
    case '▷':
      return 'bg-yellow-100 text-yellow-700';
    default:
      return 'bg-gray-100 text-gray-600';
  }
}
</script>
