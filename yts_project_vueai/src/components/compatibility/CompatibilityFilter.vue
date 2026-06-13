<template>
  <div class="compatibility-filter bg-white rounded-lg shadow-md p-6">
    <h3 class="text-lg font-semibold text-gray-800 mb-4">烫金工艺兼容性筛选</h3>
    
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
      <!-- 烫金纸性能类型 -->
      <div class="form-group">
        <label class="block text-sm font-medium text-gray-700 mb-2">
          烫金纸性能类型 *
        </label>
        <el-select
          v-model="filterParams.paperPerformance"
          placeholder="请选择性能类型"
          class="w-full"
          @change="handleFilterChange"
        >
          <el-option
            v-for="type in paperPerformanceTypes"
            :key="type"
            :label="type"
            :value="type"
          />
        </el-select>
      </div>

      <!-- 产品类型 -->
      <div class="form-group">
        <label class="block text-sm font-medium text-gray-700 mb-2">
          产品类型 *
        </label>
        <el-select
          v-model="filterParams.productType"
          placeholder="请选择产品类型"
          class="w-full"
          @change="handleFilterChange"
        >
          <el-option
            v-for="type in productTypes"
            :key="type"
            :label="type"
            :value="type"
          />
        </el-select>
      </div>

      <!-- 图案类型 -->
      <div class="form-group">
        <label class="block text-sm font-medium text-gray-700 mb-2">
          图案类型
        </label>
        <el-select
          v-model="filterParams.patternType"
          placeholder="请选择图案类型"
          class="w-full"
          @change="handlePatternTypeChange"
        >
          <el-option label="线条图案" value="LINE" />
          <el-option label="实地图案" value="SOLID" />
          <el-option label="混合图案" value="MIXED" />
          <el-option label="不区分" value="ALL" />
        </el-select>
      </div>

      <!-- 线条粗细 -->
      <div v-if="showLineThickness" class="form-group">
        <label class="block text-sm font-medium text-gray-700 mb-2">
          线条粗细 (mm)
        </label>
        <el-input-number
          v-model="filterParams.lineThickness"
          :min="0"
          :max="50"
          :step="0.1"
          :precision="1"
          placeholder="请输入线条粗细"
          class="w-full"
          @change="handleFilterChange"
        />
      </div>

      <!-- 实地尺寸 -->
      <div v-if="showSolidAreaSize" class="form-group">
        <label class="block text-sm font-medium text-gray-700 mb-2">
          实地尺寸 (mm)
        </label>
        <el-input-number
          v-model="filterParams.solidAreaSize"
          :min="0"
          :max="100"
          :step="1"
          placeholder="请输入实地尺寸"
          class="w-full"
          @change="handleFilterChange"
        />
      </div>

      <!-- 混合图案选项 -->
      <div v-if="filterParams.patternType === 'MIXED'" class="form-group">
        <label class="block text-sm font-medium text-gray-700 mb-2">
          混合图案选项
        </label>
        <el-checkbox
          v-model="filterParams.isMixedPattern"
          @change="handleFilterChange"
        >
          包含混合图案
        </el-checkbox>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="flex justify-end space-x-3 mt-6">
      <el-button @click="resetFilter">重置</el-button>
      <el-button type="primary" @click="searchCompatibility" :loading="loading">
        查询兼容性
      </el-button>
    </div>

    <!-- 验证错误提示 -->
    <div v-if="validationErrors.length > 0" class="mt-4">
      <el-alert
        v-for="error in validationErrors"
        :key="error"
        :title="error"
        type="error"
        :closable="false"
        class="mb-2"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { compatibilityApi } from '@/api/modules/compatibility';
import { CompatibilityValidator } from '@/utils/compatibilityCalculator';
import type { 
  CompatibilityQueryParams, 
  PaperPerformanceType, 
  ProductType,
  PatternType 
} from '@/types/compatibility';

// Props
const emit = defineEmits<{
  (e: 'compatibility-result', result: any): void;
  (e: 'filter-change', params: CompatibilityQueryParams): void;
}>();

// 响应式数据
const loading = ref(false);
const validationErrors = ref<string[]>([]);

// 筛选参数
const filterParams = ref<CompatibilityQueryParams>({
  patternType: 'LINE',
  isMixedPattern: false
});

// 选项数据
const paperPerformanceTypes = ref<PaperPerformanceType[]>([]);
const productTypes = ref<ProductType[]>([]);

// 计算属性
const showLineThickness = computed(() => {
  return filterParams.value.patternType === 'LINE' || filterParams.value.patternType === 'MIXED';
});

const showSolidAreaSize = computed(() => {
  return filterParams.value.patternType === 'SOLID' || filterParams.value.patternType === 'MIXED';
});

// 方法
const loadFilterOptions = async () => {
  try {
    const [paperTypes, productTypesData] = await Promise.all([
      compatibilityApi.getAllPaperPerformanceTypes(),
      compatibilityApi.getAllProductTypes()
    ]);
    
    paperPerformanceTypes.value = paperTypes.data;
    productTypes.value = productTypesData.data;
  } catch (error) {
    console.error('加载筛选选项失败:', error);
    ElMessage.error('加载筛选选项失败');
  }
};

const handlePatternTypeChange = () => {
  // 重置相关参数
  if (filterParams.value.patternType !== 'MIXED') {
    filterParams.value.isMixedPattern = false;
  }
  if (filterParams.value.patternType === 'LINE') {
    filterParams.value.solidAreaSize = undefined;
  }
  if (filterParams.value.patternType === 'SOLID') {
    filterParams.value.lineThickness = undefined;
  }
  
  handleFilterChange();
};

const handleFilterChange = () => {
  validationErrors.value = [];
  emit('filter-change', filterParams.value);
};

const searchCompatibility = async () => {
  // 验证参数
  const validation = CompatibilityValidator.validateParams(filterParams.value);
  if (!validation.isValid) {
    validationErrors.value = validation.errors;
    return;
  }

  const patternValidation = CompatibilityValidator.validatePatternParams(filterParams.value);
  if (!patternValidation.isValid) {
    validationErrors.value = patternValidation.warnings;
    return;
  }

  loading.value = true;
  try {
    const response = await compatibilityApi.getRecommendedHotStampingTypes(filterParams.value);
    emit('compatibility-result', response.data);
    ElMessage.success('兼容性查询成功');
  } catch (error) {
    console.error('兼容性查询失败:', error);
    ElMessage.error('兼容性查询失败');
  } finally {
    loading.value = false;
  }
};

const resetFilter = () => {
  filterParams.value = {
    patternType: 'LINE',
    isMixedPattern: false
  };
  validationErrors.value = [];
  emit('filter-change', filterParams.value);
};

// 生命周期
onMounted(() => {
  loadFilterOptions();
});

// 监听参数变化
watch(filterParams, (newParams) => {
  handleFilterChange();
}, { deep: true });
</script>

<style scoped>
.form-group {
  @apply space-y-2;
}

.compatibility-filter {
  @apply border border-gray-200;
}
</style>
