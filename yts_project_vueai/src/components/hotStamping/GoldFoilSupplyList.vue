<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { goldFoilApi } from '@/api';
import type { GoldFoilProduct } from '@/types/goldFoil';
// 導入 Element Plus 組件
import 'element-plus/dist/index.css';
import { ElDialog, ElButton, ElPagination } from 'element-plus';

// 狀態變量
const searchQuery = ref('');
const goldFoilProducts = ref<GoldFoilProduct[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);
const currentPage = ref(1);
const pageSize = ref(15);
const dialogVisible = ref(false);
const currentProduct = ref<GoldFoilProduct | null>(null);
const inquiryDialogVisible = ref(false);

// 兼容性狀態徽標
const compatibilityBadgeIcon = (status: string): string => {
  if (status === 'compatible' || status === '匹配' || status === 'V') return 'V';
  if (status === 'incompatible' || status === '不匹配' || status === 'X') return 'X';
  if (status === 'partial' || status === '部分匹配') return '▷';
  return '—';
};

const compatibilityBadgeClass = (status: string): string => {
  if (status === 'compatible' || status === '匹配' || status === 'V') return 'bg-green-100 text-green-700 border-green-300';
  if (status === 'incompatible' || status === '不匹配' || status === 'X') return 'bg-red-100 text-red-700 border-red-300';
  if (status === 'partial' || status === '部分匹配') return 'bg-yellow-100 text-yellow-700 border-yellow-300';
  return 'bg-gray-100 text-gray-600 border-gray-300';
};

// 格式化價格
const formatPrice = (price: number | null | undefined): string => {
  if (price == null) return '';
  return Number(price).toFixed(2);
};

// 詢價
const showInquiry = () => {
  inquiryDialogVisible.value = true;
};

// 處理空值和特殊值的輔助函數
const formatValue = (value: string | number | null | undefined): string => {
  // 處理空值
  if (value === null || value === undefined || value === '') {
    return '無';
  }

  // 將 NA_Enum 映射為 NA
  if (String(value) === 'NA_Enum') {
    return 'NA';
  }

  // 處理其他特殊值的映射
  // 可以根據需要添加更多的映射規則

  return String(value);
};

// 獲取數據
const fetchGoldFoilProducts = async () => {
  console.log('Starting to fetch products');
  loading.value = true;
  error.value = null;
  try {
    // 獲取數據 - 由於響應攔截器已經處理了響應，所以直接獲取數據
    const data = await goldFoilApi.getAllProducts();
    console.log('API response data:', data);

    // 確保數據是數組
    goldFoilProducts.value = Array.isArray(data) ? data : [];
  } catch (err) {
    console.error('Error fetching products:', err);
    error.value = '獲取燙金紙數據失敗，請稍後重試';
  } finally {
    loading.value = false;
  }
};

// 搜索過濾
const filteredProducts = computed(() => {
  if (!searchQuery.value) return goldFoilProducts.value;

  const query = searchQuery.value.toLowerCase();
  return goldFoilProducts.value.filter(product =>
    (product.name?.toLowerCase() || '').includes(query) ||
    (product.modelNumber?.toLowerCase() || '').includes(query) ||
    (product.materialNumber?.toLowerCase() || '').includes(query) ||
    (product.color?.toLowerCase() || '').includes(query)
  );
});

// 分頁數據
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredProducts.value.slice(start, end);
});

// 計算總頁數
const totalPages = computed(() => {
  return Math.ceil(filteredProducts.value.length / pageSize.value);
});

// 響應式分頁器配置
const paginationConfig = computed(() => {
  const total = totalPages.value;

  // 根據總頁數動態調整配置
  if (total <= 1) {
    // 只有一頁時不顯示分頁器
    return {
      layout: '',
      pagerCount: 1,
      showJumper: false,
      show: false
    };
  } else if (total <= 3) {
    // 頁數少時，顯示完整佈局
    return {
      layout: 'prev, pager, next',
      pagerCount: total,
      showJumper: false,
      show: true
    };
  } else if (total <= 5) {
    // 中等頁數，緊湊佈局
    return {
      layout: 'prev, pager, next',
      pagerCount: 3,
      showJumper: false,
      show: true
    };
  } else if (total <= 10) {
    // 較多頁數，顯示跳轉
    return {
      layout: 'prev, pager, next',
      pagerCount: 3,
      showJumper: true,
      show: true
    };
  } else {
    // 頁數很多時，最緊湊佈局
    return {
      layout: 'prev, pager, next',
      pagerCount: 2,
      showJumper: true,
      show: true
    };
  }
});

// 打開詳情對話框
const openDetails = (product: GoldFoilProduct) => {
  currentProduct.value = product;
  dialogVisible.value = true;
};

// 關閉詳情對話框
const closeDetails = () => {
  dialogVisible.value = false;
  currentProduct.value = null;
};

// 搜索時重置頁碼
const handleSearch = () => {
  currentPage.value = 1;
};

// 分頁器變化時的調試信息
const handlePageChange = (page: number) => {
  console.log(`分頁器切換到第 ${page} 頁，總頁數: ${totalPages.value}`);
  console.log('當前分頁器配置:', paginationConfig.value);
};

onMounted(() => {
  console.log('Component mounted');
  fetchGoldFoilProducts();
});
</script>

<template>
  <div class="w-96 bg-white shadow-lg min-h-screen p-4 overflow-y-auto">
    <h2 class="text-lg font-semibold mb-4 text-gray-900">燙金紙供應型號</h2>

    <!-- 搜索框 -->
    <div class="mb-4">
      <input
        v-model="searchQuery"
        placeholder="請輸入燙金紙型號或物料號進行搜索"
        class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
        @input="handleSearch"
      />
    </div>

    <!-- 数据统计 -->
    <div class="mb-4 text-sm text-gray-500">
       <span class="font-medium">{{ filteredProducts.length }}</span> 條記錄
    </div>

    <!-- 卡片列表 -->
    <div v-if="!loading && !error" class="grid gap-3">
      <div v-for="item in paginatedProducts" :key="item.materialNumber"
        class="card-item bg-white rounded-lg border border-gray-200 p-4 text-sm transition-shadow">
        <div class="flex justify-between items-start">
          <div class="flex-1 min-w-0">
            <!-- 型號 + 兼容性徽標 -->
            <div class="flex items-center gap-2">
              <span class="font-semibold text-gray-900 truncate">{{ formatValue(item.modelNumber) }}</span>
              <span v-if="(item as any).compatibilityStatus"
                class="inline-flex items-center justify-center w-5 h-5 rounded-full text-xs font-bold border"
                :class="compatibilityBadgeClass((item as any).compatibilityStatus)">
                {{ compatibilityBadgeIcon((item as any).compatibilityStatus) }}
              </span>
            </div>
            <!-- 系列名 -->
            <div class="text-xs text-gray-500 mt-0.5">系列：{{ formatValue(item.name) }}</div>
            <!-- 價格 -->
            <div v-if="item.price != null && item.price !== ''" class="text-xs text-gray-600 mt-1">
              價格：<span class="text-orange-600 font-medium">¥{{ formatPrice(item.price) }}</span>
            </div>
          </div>
          <!-- 操作按鈕 -->
          <div class="flex items-center space-x-2 shrink-0 ml-3">
            <button
              class="bg-orange-500 hover:bg-orange-600 text-white px-3 py-1 rounded text-sm"
              @click="showInquiry"
            >
              询价
            </button>
            <button
              class="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded text-sm"
              @click="openDetails(item)"
            >
              查看詳情
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页器 -->
    <div class="flex justify-center mt-4 px-2" v-if="paginationConfig.show && filteredProducts.length > pageSize">
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="filteredProducts.length"
          :layout="paginationConfig.layout + (paginationConfig.showJumper ? ', jumper' : '')"
          :pager-count="paginationConfig.pagerCount"
          background
          small
          class="compact-pagination"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="flex justify-center items-center py-8">
      <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-500"></div>
    </div>

    <!-- 错误提示 -->
    <div v-else-if="error" class="text-red-500 text-center py-8">
      {{ error }}
    </div>

    <!-- 无数据提示 -->
    <div v-else-if="filteredProducts.length === 0 && !loading" class="text-gray-500 text-center py-8">
      沒有找到燙金紙產品
    </div>

    <!-- 详情对话框 -->
    <el-dialog v-model="dialogVisible" title="产品详情" width="50%" destroy-on-close center>
      <div v-if="currentProduct" class="space-y-4">

        <!-- 基本信息 -->
        <div class="bg-blue-50 p-4 rounded-lg">
          <h4 class="font-medium text-lg mb-2 text-blue-700">基本信息</h4>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <span class="text-gray-500">物料編號：</span>
              <span>{{ formatValue(currentProduct.materialNumber) }}</span>
            </div>
            <div>
              <span class="text-gray-500">燙金紙型號：</span>
              <span>{{ formatValue(currentProduct.modelNumber) }}</span>
            </div>
            <div>
              <span class="text-gray-500">Leo Touch編號：</span>
              <span>{{ formatValue(currentProduct.companyNumber) }}</span>
            </div>
            <div>
              <span class="text-gray-500">SPNO：</span>
              <span>{{ formatValue(currentProduct.gpNo) }}</span>
            </div>
            <div>
              <span class="text-gray-500">燙金紙類型：</span>
              <span>{{ formatValue(currentProduct.hotStampingPaperType) }}</span>
            </div>
            <div>
              <span class="text-gray-500">系列：</span>
              <span>{{ formatValue(currentProduct.name) }}</span>
            </div>
            <div>
              <span class="text-gray-500">價格：</span>
              <span>{{ formatValue(currentProduct.price) }}</span>
            </div>
          </div>
        </div>

        <!-- 规格信息 -->
        <div class="bg-green-50 p-4 rounded-lg">
          <h4 class="font-medium text-lg mb-2 text-green-700">规格信息</h4>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <span class="text-gray-500">顏色：</span>
              <span>{{ formatValue(currentProduct.color) }}</span>
            </div>
            <div>
              <span class="text-gray-500">金紙鬆緊度：</span>
              <span>{{ formatValue(currentProduct.tightness) }}</span>
            </div>
            <div>
              <span class="text-gray-500">金紙性能：</span>
              <span>{{ formatValue(currentProduct.performance) }}</span>
            </div>
            <div>
              <span class="text-gray-500">尺寸：</span>
              <span>{{ formatValue(currentProduct.size) }}</span>
            </div>
            <div>
              <span class="text-gray-500">温度范围：</span>
              <span>{{ formatValue(currentProduct.temperatureRange) }}°C</span>
            </div>
          </div>
        </div>
        <div class="bg-green-50 p-4 rounded-lg">
          <h4 class="font-medium text-lg mb-2 text-green-700">其他信息</h4>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <span class="text-gray-500">封度:</span>
              <span>{{ formatValue(currentProduct.fengdu) }}</span>
            </div>
            <div>
              <span class="text-gray-500">單位:</span>
              <span>{{ formatValue(currentProduct.danwei) }}</span>
            </div>
            <div>
              <span class="text-gray-500">是否常用:</span>
              <span>{{ formatValue(currentProduct.isChangyong) }}</span>
            </div>
            <div>
              <span class="text-gray-500">是否街貨:</span>
              <span>{{ formatValue(currentProduct.isJiehuo) }}</span>
            </div>
            <div>
              <span class="text-gray-500">概述:</span>
              <span>{{ formatValue(currentProduct.gaishu) }}</span>
            </div>
            <div>
              <span class="text-gray-500">說明:</span>
              <span>{{ formatValue(currentProduct.descirption) }}</span>
            </div>
          </div>
        </div>
        <!-- 其他信息 -->
        <!-- <div class="bg-amber-50 p-4 rounded-lg">
          <h4 class="font-medium text-lg mb-2 text-amber-700">烫金类型信息</h4>
          <div class="grid grid-cols-1 gap-4">
            <div>
              <span class="text-gray-500">平面烫金：</span>
              <span>{{ formatValue(currentProduct.flatHotStamping) }}</span>
            </div>
            <div>
              <span class="text-gray-500">立体烫金：</span>
              <span>{{ formatValue(currentProduct.embossedGoldStamping) }}</span>
            </div>
            <div>
              <span class="text-gray-500">折光/网点/纹理烫金：</span>
              <span>{{ formatValue(currentProduct.refractiveHolographicPatternedTexturedHotStamping) }}</span>
            </div>
            <div>
              <span class="text-gray-500">烫金后压凸/压纹：</span>
              <span>{{ formatValue(currentProduct.postHotStampingEmbossingDebossing) }}</span>
            </div>
          </div>
        </div> -->
      </div>
      <template #footer>
        <div class="flex justify-end">
          <el-button @click="closeDetails">关闭</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 询价对话框 -->
    <el-dialog v-model="inquiryDialogVisible" title="询价" width="30%" destroy-on-close center>
      <div class="text-center py-6">
        <svg class="w-16 h-16 mx-auto mb-4 text-orange-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 100 4 2 2 0 000-4z" />
        </svg>
        <p class="text-gray-700 text-base font-medium">请联系销售获取报价</p>
        <p class="text-gray-400 text-sm mt-2">如需了解具体价格，请致电或联系在线客服</p>
      </div>
      <template #footer>
        <div class="flex justify-center">
          <el-button type="primary" @click="inquiryDialogVisible = false">知道了</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
/* 分页器容器样式 */
.pagination-container {
  width: 100%;
  max-width: 360px; /* 适应 w-96 容器 */
  overflow: hidden;
}

/* 紧凑分页器样式 */
.compact-pagination {
  --el-pagination-font-size: 12px;
  --el-pagination-button-width: 28px;
  --el-pagination-button-height: 28px;
}

/* 分页器按钮样式优化 */
:deep(.el-pagination) {
  justify-content: center;
  flex-wrap: nowrap;
}

:deep(.el-pagination .btn-prev),
:deep(.el-pagination .btn-next) {
  min-width: 28px !important;
  padding: 0 8px !important;
}

:deep(.el-pagination .el-pager li) {
  min-width: 28px !important;
  height: 28px !important;
  line-height: 28px !important;
  font-size: 12px !important;
  margin: 0 2px !important;
}

:deep(.el-pagination .el-pagination__jump) {
  margin-left: 8px !important;
}

:deep(.el-pagination .el-input__inner) {
  height: 28px !important;
  width: 40px !important;
  font-size: 12px !important;
}

/* 响应式处理 */
@media (max-width: 400px) {
  .pagination-container {
    max-width: 320px;
  }

  :deep(.el-pagination .btn-prev),
  :deep(.el-pagination .btn-next) {
    min-width: 24px !important;
    padding: 0 4px !important;
  }

  :deep(.el-pagination .el-pager li) {
    min-width: 24px !important;
    height: 24px !important;
    line-height: 24px !important;
    font-size: 11px !important;
    margin: 0 1px !important;
  }
}

/* 确保分页器不会溢出 */
:deep(.el-pagination) {
  overflow: hidden;
  white-space: nowrap;
}

/* 隐藏跳转输入框的标签文字以节省空间 */
:deep(.el-pagination__jump .el-pagination__goto) {
  display: none;
}

:deep(.el-pagination__jump .el-pagination__classifier) {
  display: none;
}

/* 卡片樣式 */
.card-item {
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.card-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
</style>