<template>
  <div class="min-h-screen bg-gray-100 pt-20 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <div class="mb-6">
        <h1 class="text-2xl font-bold text-gray-900">LEO 书板贴纸</h1>
        <p class="mt-1 text-sm text-gray-500">搜索LEO书板贴纸产品，按后加工工序筛选兼容性</p>
      </div>

      <div class="bg-white rounded-lg shadow px-5 py-5 mb-6">
        <div class="flex flex-wrap items-end gap-4">
          <div class="flex-1 min-w-[200px]">
            <label class="block text-sm font-medium text-gray-700 mb-1">关键词搜索</label>
            <el-input v-model="keyword" placeholder="物料名称 / 编码 / 型号" clearable @keyup.enter="onSearch" />
          </div>
          <div class="w-56">
            <label class="block text-sm font-medium text-gray-700 mb-1">后加工工序</label>
            <el-select v-model="selectedStep" placeholder="全部工序" clearable class="w-full" @change="onSearch">
              <el-option v-for="step in steps" :key="step" :label="step" :value="step" />
            </el-select>
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1 invisible">搜索</label>
            <el-button type="primary" @click="onSearch">
              <el-icon class="mr-1"><Search /></el-icon> 搜索
            </el-button>
          </div>
        </div>
      </div>

      <div v-if="loading" class="flex justify-center py-12">
        <el-icon class="is-loading text-2xl text-indigo-500"><Loading /></el-icon>
        <span class="ml-3 text-gray-500">加载中...</span>
      </div>

      <div v-if="!loading && pagedResult" class="flex items-center justify-between mb-4">
        <p class="text-sm text-gray-500">
          共 <span class="font-medium text-gray-700">{{ pagedResult.total }}</span> 条结果
          <span v-if="keyword || selectedStep" class="ml-2">
            <el-tag v-if="keyword" closable size="small" type="info" @close="keyword = ''; onSearch()">
              关键词: {{ keyword }}
            </el-tag>
            <el-tag v-if="selectedStep" closable size="small" type="info" class="ml-1" @close="selectedStep = ''; onSearch()">
              工序: {{ selectedStep }}
            </el-tag>
          </span>
        </p>
      </div>

      <div v-if="!loading && pagedResult && pagedResult.items.length === 0"
           class="bg-white rounded-lg shadow py-16 text-center">
        <el-empty description="暂无匹配结果">
          <template #image>
            <svg class="w-24 h-24 mx-auto text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"
                d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
          </template>
          <p class="text-gray-400 mt-2">{{ keyword || selectedStep ? '未找到匹配条件的产品' : '暂无数据' }}</p>
        </el-empty>
      </div>

      <div v-if="!loading && pagedResult && pagedResult.items.length > 0" class="space-y-4">
        <div v-for="product in pagedResult.items" :key="product.id"
             class="bg-white rounded-lg shadow-sm border border-gray-200 hover:shadow-md transition-shadow">
          <div class="px-5 py-4 flex items-start justify-between cursor-pointer" @click="toggleExpand(product.id!)">
            <div class="flex-1 min-w-0">
              <div class="flex items-center gap-3">
                <h3 class="text-base font-semibold text-gray-900 truncate">{{ product.materialName || '-' }}</h3>
                <el-tag v-if="product.isActive === false" size="small" type="danger">停用</el-tag>
              </div>
              <div class="mt-1 flex flex-wrap gap-x-4 gap-y-1 text-sm text-gray-500">
                <span>编码: {{ product.materialCode || '-' }}</span>
                <span>型号: {{ product.stockCode || '-' }}</span>
                <span>颜色: {{ product.color || '-' }}</span>
                <span>材质: {{ product.category || '-' }}</span>
                <span>厚度: {{ product.thickness || '-' }}</span>
                <span>克重: {{ product.shape || '-' }}</span>
              </div>
            </div>
            <div class="flex items-center gap-2 ml-4">
              <el-tag v-if="selectedStep && productCompatStatus[product.id!]"
                      :type="productCompatStatus[product.id!] === 'V' ? 'success' : 'danger'"
                      size="small">
                {{ productCompatStatus[product.id!] === 'V' ? 'V 适用' : 'X 不适用' }}
              </el-tag>
              <el-icon class="text-gray-400 transition-transform duration-200"
                       :class="{ 'rotate-180': expandedId === product.id }">
                <ArrowDown />
              </el-icon>
            </div>
          </div>

          <div v-if="expandedId === product.id" class="border-t border-gray-100 px-5 py-4 bg-gray-50 rounded-b-lg">
            <div v-if="detailLoading" class="flex items-center justify-center py-4">
              <el-icon class="is-loading text-indigo-500"><Loading /></el-icon>
              <span class="ml-2 text-sm text-gray-400">加载详情...</span>
            </div>
            <div v-else-if="currentDetail">
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                <div><label class="text-xs text-gray-400">物料名称</label><p class="text-sm text-gray-700">{{ currentDetail.product?.materialName || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">物料编码</label><p class="text-sm text-gray-700">{{ currentDetail.product?.materialCode || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">供应商编码</label><p class="text-sm text-gray-700">{{ currentDetail.product?.supplierCode || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">存仓物料号</label><p class="text-sm text-gray-700">{{ currentDetail.product?.stockCode || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">颜色</label><p class="text-sm text-gray-700">{{ currentDetail.product?.color || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">材质分类</label><p class="text-sm text-gray-700">{{ currentDetail.product?.category || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">厚度</label><p class="text-sm text-gray-700">{{ currentDetail.product?.thickness || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">形状</label><p class="text-sm text-gray-700">{{ currentDetail.product?.shape || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">检测员</label><p class="text-sm text-gray-700">{{ currentDetail.product?.tester || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">用纸尺寸</label><p class="text-sm text-gray-700">{{ currentDetail.product?.minSheetSize || '-' }} ~ {{ currentDetail.product?.maxSheetSize || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">间距(最小)</label><p class="text-sm text-gray-700">{{ currentDetail.product?.minSpacing || '-' }}</p></div>
                <div><label class="text-xs text-gray-400">间距(最大)</label><p class="text-sm text-gray-700">{{ currentDetail.product?.maxSpacing || '-' }}</p></div>
              </div>
              <div v-if="currentDetail.product?.usage" class="mb-4">
                <label class="text-xs text-gray-400">物料用途</label><p class="text-sm text-gray-700">{{ currentDetail.product.usage }}</p>
              </div>
              <div v-if="currentDetail.product?.designInfo" class="mb-4">
                <label class="text-xs text-gray-400">设计限制信息</label><p class="text-sm text-gray-700">{{ currentDetail.product.designInfo }}</p>
              </div>
              <div v-if="currentDetail.product?.applicableInterface" class="mb-4">
                <label class="text-xs text-gray-400">适用界面</label><p class="text-sm text-gray-700">{{ currentDetail.product.applicableInterface }}</p>
              </div>
              <div v-if="currentDetail.product?.notes" class="mb-4">
                <label class="text-xs text-gray-400">备注</label><p class="text-sm text-gray-700">{{ currentDetail.product.notes }}</p>
              </div>
              <div v-if="currentDetail.compatibilities && currentDetail.compatibilities.length > 0">
                <h4 class="text-sm font-medium text-gray-700 mb-3">后加工工序兼容性</h4>
                <div class="flex flex-wrap gap-2">
                  <div v-for="comp in currentDetail.compatibilities" :key="comp.id"
                       class="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-sm"
                       :class="statusBadgeClass(comp.compatibilityStatus)">
                    <span>{{ comp.postProcessingStep }}</span>
                    <span class="font-bold">{{ comp.compatibilityStatus === 'V' ? 'V' : comp.compatibilityStatus === 'X' ? 'X' : comp.compatibilityStatus === '△' ? '△' : comp.compatibilityStatus }}</span>
                  </div>
                </div>
              </div>
              <div v-else class="text-sm text-gray-400 italic">暂无兼容性数据</div>
            </div>
            <div v-else class="text-sm text-gray-400 italic py-4 text-center">加载详情失败</div>
          </div>
        </div>
      </div>

      <div v-if="pagedResult && pagedResult.totalPages > 1" class="flex justify-center mt-6 pb-8">
        <el-pagination v-model:current-page="currentPage" :page-size="pageSize" :total="pagedResult.total"
          layout="prev, pager, next, total" background @current-change="onPageChange" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { leoBookBoardApi, type LeoBookBoardProduct, type PagedItems } from '@/api/modules/leoBookBoard';
import { Search, Loading, ArrowDown } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';

const keyword = ref('');
const selectedStep = ref('');
const steps = ref<string[]>([]);
const loading = ref(false);
const pageSize = ref(15);
const currentPage = ref(1);
const pagedResult = ref<PagedItems<LeoBookBoardProduct> | null>(null);
const expandedId = ref<number | null>(null);
const currentDetail = ref<any>(null);
const detailLoading = ref(false);
const productCompatStatus = ref<Record<number, string>>({});

onMounted(() => { loadSteps(); loadMatch(); });

async function loadSteps() {
  try { const res: any = await leoBookBoardApi.getSteps(); steps.value = res || []; } catch {}
}

async function loadMatch() {
  loading.value = true;
  productCompatStatus.value = {};
  try {
    const res: any = await leoBookBoardApi.match({
      keyword: keyword.value || undefined,
      stepName: selectedStep.value || undefined,
      page: currentPage.value,
      size: pageSize.value,
    });
    pagedResult.value = res || null;
    if (res?.items && selectedStep.value) {
      loadCompatibilitiesForProducts(res.items);
    }
  } catch {
    ElMessage.error('查询失败，请重试');
    pagedResult.value = { items: [], total: 0, pageSize: pageSize.value, currentPage: currentPage.value, totalPages: 0 };
  } finally { loading.value = false; }
}

async function loadCompatibilitiesForProducts(products: { id?: number }[]) {
  const map: Record<number, string> = {};
  if (!selectedStep.value) { productCompatStatus.value = {}; return; }
  for (const product of products) {
    if (!product.id) continue;
    try {
      const res: any = await leoBookBoardApi.getCompatibilitiesByProductId(product.id);
      const comps: any[] = res || [];
      const matched = comps.find((c: any) => c.postProcessingStep === selectedStep.value);
      if (matched) { map[product.id] = matched.compatibilityStatus; }
    } catch { /* skip */ }
  }
  productCompatStatus.value = map;
}

function onSearch() { currentPage.value = 1; loadMatch(); }

function onPageChange() { loadMatch(); expandedId.value = null; currentDetail.value = null; }

async function toggleExpand(id: number) {
  if (expandedId.value === id) { expandedId.value = null; currentDetail.value = null; return; }
  expandedId.value = id;
  detailLoading.value = true;
  currentDetail.value = null;
  try { const res: any = await leoBookBoardApi.getProductDetail(id); currentDetail.value = res || null; }
  catch { ElMessage.error('加载产品详情失败'); currentDetail.value = null; }
  finally { detailLoading.value = false; }
}

function statusBadgeClass(status?: string): string {
  switch (status) {
    case 'V': return 'bg-green-100 text-green-800';
    case 'X': return 'bg-red-100 text-red-800';
    case '△': case '▷': return 'bg-yellow-100 text-yellow-800';
    default: return 'bg-gray-100 text-gray-600';
  }
}
</script>

<style scoped>
.rotate-180 { transform: rotate(180deg); }
</style>
