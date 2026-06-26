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
      name: '印刷匹配系统',
      description: '用于印刷工艺的物料匹配'
    },
    {
      id: 'laminating',
      name: '过胶匹配系统',
      description: '用于过胶工艺的物料匹配'
    },
    {
      id: 'screenPrinting',
      name: '丝印匹配系统',
      description: '用于丝印工艺的物料匹配'
    },
    {
      id: 'silicone',
      name: '书板贴纸指引',
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