import { defineStore } from 'pinia';
import { ref } from 'vue';

/**
 * 专门用于存储第一次匹配参数的 Store
 */
export const useFirstMatchParamsStore = defineStore('firstMatchParams', () => {
  // 第一次匹配参数
  const params = ref<any>(null);

  // 设置第一次匹配参数
  const setParams = (newParams: any) => {
    console.log('firstMatchParamsStore.setParams called with:', newParams);

    if (newParams) {
      // 使用 reactive 包装对象，确保响应式更新
      params.value = { ...newParams };

      // 强制触发响应式更新
      setTimeout(() => {
        console.log('firstMatchParamsStore.params after timeout:', params.value);
      }, 0);
    } else {
      params.value = null;
    }

    console.log('firstMatchParamsStore.params after setting:', params.value);
  };

  // 获取第一次匹配参数
  const getParams = () => {
    console.log('firstMatchParamsStore.getParams called, returning:', params.value);
    return params.value;
  };

  // 清除第一次匹配参数
  const clearParams = () => {
    console.log('firstMatchParamsStore.clearParams called');
    params.value = null;
    console.log('firstMatchParamsStore.params after clearing:', params.value);
  };

  return {
    params,
    setParams,
    getParams,
    clearParams
  };
});
