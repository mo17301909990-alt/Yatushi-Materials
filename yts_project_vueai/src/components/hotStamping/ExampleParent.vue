<template>
  <div class="p-6">
    <h2 class="text-xl font-bold mb-4">前工序匹配示例</h2>
    
    <!-- 配置组件 -->
    <FoilConfig 
      ref="foilConfigRef"
      @searchResults="handleSearchResults"
      @searchError="handleSearchError"
    />
    
    <!-- 匹配结果显示 -->
    <div v-if="matchResults.length > 0" class="mt-8">
      <h3 class="text-lg font-semibold mb-4">匹配结果 ({{ matchResults.length }} 个)</h3>
      <div class="grid gap-4">
        <div 
          v-for="(product, index) in matchResults" 
          :key="index"
          class="border rounded-lg p-4 bg-white shadow-sm"
        >
          <div class="flex justify-between items-start">
            <div>
              <h4 class="font-medium text-gray-900">{{ product.name }}</h4>
              <p class="text-sm text-gray-600">型号: {{ product.modelNumber }}</p>
              <p class="text-sm text-gray-600">材质编号: {{ product.materialNumber }}</p>
            </div>
            <div class="text-right">
              <p class="text-sm text-gray-500">公司编号: {{ product.companyNumber }}</p>
              <p class="text-sm text-gray-500">客户编号: {{ product.gpNo }}</p>
            </div>
          </div>
          
          <div class="mt-3 grid grid-cols-2 gap-4 text-sm">
            <div>
              <span class="font-medium">颜色:</span> {{ product.color }}
            </div>
            <div>
              <span class="font-medium">尺寸:</span> {{ product.size }}
            </div>
            <div>
              <span class="font-medium">紧度:</span> {{ product.tightness }}
            </div>
            <div>
              <span class="font-medium">温度范围:</span> {{ product.temperatureRange }}
            </div>
          </div>
          
          <div class="mt-3 text-sm">
            <span class="font-medium">性能:</span> {{ product.performance }}
          </div>
          
          <div class="mt-3 flex justify-between items-center">
            <div class="text-sm text-gray-600">
              <span class="font-medium">平面烫金:</span> {{ product.flatHotStamping }}
            </div>
            <div class="text-lg font-bold text-green-600">
              ¥{{ product.price }}
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 错误显示 -->
    <div v-if="errorMessage" class="mt-4 p-4 bg-red-50 border border-red-200 rounded-lg">
      <p class="text-red-600">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import FoilConfig from './FoilConfig.vue';

// 匹配结果
const matchResults = ref<any[]>([]);
const errorMessage = ref<string>('');
const foilConfigRef = ref();

// 处理查询结果
const handleSearchResults = (data: { results: any[], queryType: string, queryParams: any }) => {
  console.log('收到查询结果:', data);
  matchResults.value = data.results;
  errorMessage.value = '';
};

// 处理查询错误
const handleSearchError = (data: { error: string, queryType: string }) => {
  console.error('查询错误:', data);
  errorMessage.value = data.error;
  matchResults.value = [];
};
</script>
