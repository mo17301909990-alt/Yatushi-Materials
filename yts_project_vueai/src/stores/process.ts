import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { ProcessType, Process } from '../types/process';

export const useProcessStore = defineStore('process', () => {
  const currentProcess = ref<ProcessType>('hotStamping');

  const processes: Process[] = [
    {
      id: 'hotStamping',
      name: '烫金匹配系统',
      description: '用于烫金工艺的物料匹配'
    },
    {
      id: 'printing',
      name: '各品牌纸类通用产品指引书',
      description: '各品牌纸类通用产品标准与指引'
    },
    {
      id: 'laminating',
      name: '平面产品贴纸-应用指引书',
      description: '平面贴纸产品应用标准与指引'
    },
    {
      id: 'screenPrinting',
      name: '非平面产品贴纸-应用指引书',
      description: '非平面贴纸产品应用标准与指引'
    },
    {
      id: 'silicone',
      name: '书板贴纸-应用指引书',
      description: '书板贴纸产品应用标准与指引'
    }
  ];

  const setCurrentProcess = (process: ProcessType) => {
    currentProcess.value = process;
  };

  return {
    currentProcess,
    processes,
    setCurrentProcess
  };
});