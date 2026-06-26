<template>
  <div class="min-h-screen bg-gray-100 pt-20 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <!-- 页面标题 -->
      <div class="mb-6">
        <h1 class="text-2xl font-bold text-gray-900">UV油墨匹配</h1>
        <p class="mt-1 text-sm text-gray-500">搜索UV油墨产品，按后加工工序筛选兼容性</p>
      </div>

      <!-- 搜索与筛选 -->
      <div class="bg-white rounded-lg shadow px-5 py-5 mb-6">
        <div class="flex flex-wrap items-end gap-4">
          <div class="flex-1 min-w-[200px]">
            <label class="block text-sm font-medium text-gray-700 mb-1">关键词搜索</label>
            <el-input
              v-model="keyword"
              placeholder="物料名称 / 编码 / 型号"
              clearable
              @keyup.enter="onSearch"
            />
          </div>
          <div class="min-w-[300px] flex-1">
            <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序（多选）</label>
            <el-select
              v-model="selectedSteps"
              multiple
              placeholder="选择工序，可多选"
              class="w-full"
              @change="onStepChange"
            >
              <el-option-group
                v-for="group in stepOptions"
                :key="group.category"
                :label="group.category"
              >
                <el-option
                  v-for="step in group.steps"
                  :key="step"
                  :label="step"
                  :value="step"
                />
              </el-option-group>
            </el-select>
            <!-- 已选步骤标签 -->
            <div v-if="selectedSteps.length > 0" class="flex flex-wrap gap-1.5 mt-2">
              <el-tag
                v-for="step in selectedSteps"
                :key="step"
                closable
                size="small"
                :type="tagTypeForStep(step)"
                @close="removeStep(step)"
              >
                {{ step }}
              </el-tag>
            </div>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1 invisible">搜索</label>
            <el-button type="primary" @click="onSearch">
              <el-icon class="mr-1"><Search /></el-icon>
              搜索
            </el-button>
          </div>
        </div>
      </div>

      <!-- 提示信息 -->
      <div v-if="!loading && selectedSteps.length === 0" class="bg-blue-50 border border-blue-200 rounded-lg px-4 py-3 mb-4">
        <p class="text-sm text-blue-700">
          <el-icon class="mr-1 align-text-bottom"><InfoFilled /></el-icon>
          请选择至少一个后加工工序以筛选兼容性
        </p>
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
          <span v-if="keyword || selectedSteps.length > 0" class="ml-2">
            <el-tag
              v-if="keyword"
              closable
              size="small"
              type="info"
              @close="keyword = ''; onSearch()"
            >
              关键词: {{ keyword }}
            </el-tag>
            <el-tag
              v-for="step in selectedSteps"
              :key="step"
              closable
              size="small"
              class="ml-1"
              @close="removeStep(step)"
            >
              工序: {{ step }}
            </el-tag>
          </span>
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
            {{ keyword || selectedSteps.length > 0 ? '未找到匹配条件的产品，请调整搜索条件' : '暂无数据，请点击搜索按钮' }}
          </p>
        </el-empty>
      </div>

      <!-- 结果表格 -->
      <div
        v-if="!loading && pagedResult && pagedResult.items.length > 0"
        class="bg-white rounded-lg shadow overflow-hidden"
      >
        <el-table
          :data="pagedResult.items"
          stripe
          style="width: 100%"
          @row-click="toggleExpand"
          row-class-name="cursor-pointer"
        >
          <el-table-column prop="materialCode" label="物料编码" min-width="140" />
          <el-table-column prop="materialName" label="物料名称" min-width="160" show-overflow-tooltip />
          <el-table-column prop="stockCode" label="型号" min-width="110" />
          <el-table-column prop="color" label="颜色" min-width="80" />
          <el-table-column prop="thickness" label="厚度" min-width="80" />
          <el-table-column prop="responsiblePerson" label="测试员" min-width="80" />
          <!-- 多步骤兼容状态列（每步骤一列） -->
          <el-table-column
            v-for="step in selectedSteps"
            :key="step"
            :label="step"
            min-width="90"
            align="center"
          >
            <template #default="{ row }">
              <el-tag
                :type="statusTagType(row.compatibilityStatusMap?.[step])"
                effect="dark"
                size="small"
              >
                {{ row.compatibilityStatusMap?.[step] || '-' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template #default="{ row }">
              <el-button
                link
                type="primary"
                size="small"
                @click.stop="toggleExpand(row.id!)"
              >
                {{ expandedId === row.id ? '收起' : '详情' }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 展开详情区域 -->
        <div v-if="expandedId !== null" class="border-t border-gray-200 px-6 py-4 bg-gray-50">
          <div v-if="detailLoading" class="flex items-center justify-center py-4">
            <el-icon class="is-loading text-indigo-500"><Loading /></el-icon>
            <span class="ml-2 text-sm text-gray-400">加载详情...</span>
          </div>

          <div v-else-if="currentDetail" class="grid grid-cols-1 gap-4">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <label class="text-xs text-gray-400">物料名称</label>
                <p class="text-sm text-gray-700">{{ currentDetail.materialName || '-' }}</p>
              </div>
              <div>
                <label class="text-xs text-gray-400">物料编码</label>
                <p class="text-sm text-gray-700">{{ currentDetail.materialCode || '-' }}</p>
              </div>
              <div>
                <label class="text-xs text-gray-400">供应商编码</label>
                <p class="text-sm text-gray-700">{{ currentDetail.supplierCode || '-' }}</p>
              </div>
              <div>
                <label class="text-xs text-gray-400">存仓物料号</label>
                <p class="text-sm text-gray-700">{{ currentDetail.stockCode || '-' }}</p>
              </div>
              <div>
                <label class="text-xs text-gray-400">颜色</label>
                <p class="text-sm text-gray-700">{{ currentDetail.color || '-' }}</p>
              </div>
              <div>
                <label class="text-xs text-gray-400">材质分类</label>
                <p class="text-sm text-gray-700">{{ currentDetail.category || '-' }}</p>
              </div>
              <div>
                <label class="text-xs text-gray-400">测试员</label>
                <p class="text-sm text-gray-700">{{ currentDetail.responsiblePerson || '-' }}</p>
              </div>
              <div>
                <label class="text-xs text-gray-400">用纸尺寸</label>
                <p class="text-sm text-gray-700">
                  {{ currentDetail.minSheetSize || '-' }} ~ {{ currentDetail.maxSheetSize || '-' }}
                </p>
              </div>
              <div>
                <label class="text-xs text-gray-400">间距(最小)</label>
                <p class="text-sm text-gray-700">{{ currentDetail.minSpacing || '-' }}</p>
              </div>
            </div>

            <div v-if="currentDetail.usage">
              <label class="text-xs text-gray-400">物料用途</label>
              <p class="text-sm text-gray-700">{{ currentDetail.usage }}</p>
            </div>

            <div v-if="currentDetail.designInfo">
              <label class="text-xs text-gray-400">设计限制信息</label>
              <p class="text-sm text-gray-700">{{ currentDetail.designInfo }}</p>
            </div>

            <div v-if="currentDetail.applicableInterface">
              <label class="text-xs text-gray-400">适用界面</label>
              <p class="text-sm text-gray-700">{{ currentDetail.applicableInterface }}</p>
            </div>

            <div v-if="currentDetail.notes">
              <label class="text-xs text-gray-400">备注</label>
              <p class="text-sm text-gray-700">{{ currentDetail.notes }}</p>
            </div>

            <!-- 兼容性列表 -->
            <div v-if="currentDetail.compatibilities && currentDetail.compatibilities.length > 0">
              <h4 class="text-sm font-medium text-gray-700 mb-3">后加工工序兼容性</h4>
              <div class="flex flex-wrap gap-2">
                <div
                  v-for="comp in currentDetail.compatibilities"
                  :key="comp.id"
                  class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-sm"
                  :class="statusBadgeClass(comp.compatibilityStatus)"
                >
                  <span>{{ comp.postProcessingStep }}</span>
                  <span class="font-bold">
                    {{ comp.compatibilityStatus === 'V' ? 'V' : comp.compatibilityStatus === 'X' ? 'X' : comp.compatibilityStatus === '△' ? '△' : comp.compatibilityStatus }}
                  </span>
                </div>
              </div>
            </div>
            <div v-else class="text-sm text-gray-400 italic">暂无兼容性数据</div>
          </div>

          <div v-else class="text-sm text-gray-400 italic py-4 text-center">加载详情失败</div>
        </div>

        <!-- 分页 -->
        <div
          v-if="pagedResult && pagedResult.totalPages > 1"
          class="flex justify-center py-4 border-t border-gray-200"
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
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { uvOilMatteApi, type UvOilMatteProduct, type PagedItems, type StepCategoryGroup } from '@/api/modules/uvOilMatte';
import { Search, Loading, InfoFilled } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

// 搜索条件
const keyword = ref('');
const selectedSteps = ref<string[]>([]); // 多选步骤
const stepCategories = ref<StepCategoryGroup[]>([]);
const loading = ref(false);
const pageSize = ref(15);
const currentPage = ref(1);

/** el-select 分组选项（直接映射 stepCategories） */
const stepOptions = computed(() => stepCategories.value);

/** 预定义颜色轮转，让标签好看一点 */
const TAG_COLORS: Array<'success' | 'warning' | 'info' | 'danger' | ''> = ['', 'success', 'warning', 'info', 'danger'];
function tagTypeForStep(step: string): 'success' | 'warning' | 'info' | 'danger' | '' {
  return TAG_COLORS[Math.abs(hashCode(step)) % TAG_COLORS.length];
}
function hashCode(s: string): number {
  let hash = 0;
  for (let i = 0; i < s.length; i++) {
    hash = ((hash << 5) - hash) + s.charCodeAt(i);
    hash |= 0;
  }
  return hash;
}

/** 兼容性状态 -> el-tag type */
function statusTagType(status?: string): 'success' | 'danger' | 'info' | '' {
  if (status === 'V') return 'success';
  if (status === 'X') return 'danger';
  return 'info';
}

// 结果
const pagedResult = ref<PagedItems<UvOilMatteProduct> | null>(null);

// 展开详情
const expandedId = ref<number | null>(null);
const currentDetail = ref<any>(null);
const detailLoading = ref(false);

onMounted(() => {
  loadSteps();
});

/** 加载工序级联选择选项 */
async function loadSteps() {
  try {
    const res: any = await uvOilMatteApi.getSteps();
    stepCategories.value = res || [];
  } catch {
    // 静默失败，级联为空
  }
}

/** 执行匹配查询 */
async function loadMatch() {
  loading.value = true;
  try {
    const params: any = {
      keyword: keyword.value || undefined,
      page: currentPage.value,
      size: pageSize.value,
    };
    if (selectedSteps.value.length > 0) {
      params.steps = selectedSteps.value;
    }
    const res: any = await uvOilMatteApi.match(params);
    pagedResult.value = res || null;
    // 切换条件时收起详情
    expandedId.value = null;
    currentDetail.value = null;
  } catch {
    ElMessage.error('查询失败，请重试');
    pagedResult.value = { items: [], total: 0, pageSize: pageSize.value, currentPage: currentPage.value, totalPages: 0 };
  } finally {
    loading.value = false;
  }
}

/** 搜索按钮 / 回车触发（无步骤选择时提示但不执行搜索） */
function onSearch() {
  if (selectedSteps.value.length === 0) {
    ElMessage.info('请先选择至少一个后加工工序');
    return;
  }
  currentPage.value = 1;
  loadMatch();
}

/** 步骤选择变化时自动搜索 */
function onStepChange() {
  currentPage.value = 1;
  loadMatch();
}

/** 移除已选步骤 */
function removeStep(step: string) {
  selectedSteps.value = selectedSteps.value.filter(s => s !== step);
  if (selectedSteps.value.length === 0) {
    currentPage.value = 1;
    loadMatch();
  } else {
    onStepChange();
  }
}

/** 切换分页 */
function onPageChange() {
  loadMatch();
  expandedId.value = null;
  currentDetail.value = null;
}

/** 展开/收起产品详情 */
async function toggleExpand(row: any) {
  const id = typeof row === 'number' ? row : row.id;
  if (expandedId.value === id) {
    expandedId.value = null;
    currentDetail.value = null;
    return;
  }
  expandedId.value = id;
  detailLoading.value = true;
  currentDetail.value = null;
  try {
    const res: any = await uvOilMatteApi.getProductDetail(id);
    currentDetail.value = res || null;
  } catch {
    ElMessage.error('加载产品详情失败');
    currentDetail.value = null;
  } finally {
    detailLoading.value = false;
  }
}

/** 兼容性状态徽标样式（详情面板用） */
function statusBadgeClass(status?: string): string {
  switch (status) {
    case 'V':
      return 'bg-green-100 text-green-800';
    case 'X':
      return 'bg-red-100 text-red-800';
    case '△':
    case '▷':
      return 'bg-yellow-100 text-yellow-800';
    default:
      return 'bg-gray-100 text-gray-600';
  }
}
</script>

<style scoped>
:deep(.el-table .cursor-pointer) {
  cursor: pointer;
}
</style>
