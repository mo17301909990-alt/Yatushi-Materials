<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import MatchingResult from '../matching/MatchingResult.vue';
import GoldFoilSupplyList from '../hotStamping/GoldFoilSupplyList.vue';
import First_foilConfig from '../hotStamping/FoilConfig.vue';
import { useHotStampingStore } from '../../stores/hotStamping';
import { useFirstMatchParamsStore } from '../../stores/firstMatchParamsStore';

const router = useRouter();

const hotStampingStore = useHotStampingStore();
const firstMatchParamsStore = useFirstMatchParamsStore();

// 引用组件实例
const firstFoilConfigRef = ref<InstanceType<typeof First_foilConfig> | null>(null);

// 处理查询结果，传递给MatchingResult
const handleSearchResults = (data: { results: any[], total?: number, currentPage?: number, pageSize?: number, queryType: string, queryParams: any }) => {
  console.log('ProcessSidebar收到查询结果:', data);
  // 将查询参数存储到store，供MatchingResult分页使用
  if (data.queryParams) {
    hotStampingStore.setLastQueryParams(data.queryParams);
  }
};

// 清空参数并重置匹配结果
const resetAllParams = () => {
  console.log('resetAllParams called in ProcessSidebar.vue');

  if (confirm('確定要清空所有參數嗎？這將會重置所有匹配結果。')) {
    console.log('User confirmed reset');

    // 重置 store 中的参数
    console.log('Resetting store params');
    hotStampingStore.resetAllParams();

    // 清空 firstMatchParamsStore
    console.log('Clearing firstMatchParamsStore');
    firstMatchParamsStore.clearParams();

    // 清空所有表单参数
    if (firstFoilConfigRef.value) {
      console.log('Clearing all form params');
      firstFoilConfigRef.value.clearAllParams();
    }

    // 无论当前匹配类型是什么，都清空所有匹配结果
    console.log('Clearing all match results');
    hotStampingStore.searchResults = [];
    hotStampingStore.secondMatchResults = [];
    hotStampingStore.thirdMatchResults = [];
    hotStampingStore.searchMatchResult = null;
    hotStampingStore.setThirdMatchResult(null);

    console.log('Reset completed');
  }
};
</script>

<template>
  <div class="flex">
    <!-- 左侧参数配置 -->
    <div class="w-80 bg-white shadow-lg min-h-screen p-6">
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-lg font-semibold">燙金參數配置</h2>
        <button @click="resetAllParams"
          class="text-sm px-3 py-1 bg-red-50 text-red-600 hover:bg-red-100 rounded-md transition-colors">
          清空參數
        </button>
      </div>




      <!-- 匹配參數配置 -->
      <div class="space-y-6">
        <First_foilConfig ref="firstFoilConfigRef" @searchResults="handleSearchResults" />
      </div>
    </div>

    <!-- 中間匹配結果 -->
    <div class="flex-1 p-6">
      <MatchingResult />
    </div>

    <!-- 右側燙金紙供應型號列表 -->
    <GoldFoilSupplyList />
  </div>
</template>