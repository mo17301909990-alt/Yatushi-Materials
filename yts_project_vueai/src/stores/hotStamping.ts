import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import { useMatchingStore } from './matchingStore';

// 产品接口定义
export interface Product {
  name: string;
  model: string;
  id: string;
  type: string;
  color?: string;
  temperature: string;
  tension: string;
  width: string;
  length: string;
  scenarios: string[];
  features: string[];
  validSurfaces: string[];
  validAreas: string[];
  costIndex: number;
  laminatingCompatibility: {
    status: 'all' | 'partial' | 'none';
    compatibleTypes?: string[];
  };
  uvPrintingCompatible: boolean;
  ledUvGlitterCompatible: boolean;
  printingCompatible: boolean;
  flatStampingCompatible: boolean;
  reliefStampingCompatible: boolean;
  matteStampingCompatible: boolean;
  glitterStampingCompatible: boolean;
}

/**
 * 烫金纸主 Store
 * 作为集成点，将数据管理和匹配逻辑组合在一起
 * 提供向后兼容的 API
 */
export const useHotStampingStore = defineStore('hotStamping', () => {
  const matchingStore = useMatchingStore();

  // 搜索结果数组
  const searchResults = ref<any[]>([]);

  // 第二次匹配结果数组
  const secondMatchResults = ref<any[]>([]);

  // 第三次匹配结果数组
  const thirdMatchResults = ref<any[]>([]);

  // 分页信息
  const paginationInfo = ref<{
    total: number;
    pageSize: number;
    currentPage: number;
    totalPages?: number;
  } | null>(null);

  // 最后一次查询参数（用于分页查询）
  const lastQueryParams = ref<any>(null);

  // 兼容旧的API
  const bestMatch = computed(() => matchingStore.firstMatch);

  // 模拟产品数据
  const mockProducts: Record<string, Product[]> = {
    normal: [
      {
        name: '普通金纸A',
        model: 'PG-001',
        id: 'MAT001',
        type: '普通金纸',
        color: 'gold',
        temperature: '110~130℃',
        tension: '标准',
        width: '640mm',
        length: '120m',
        scenarios: ['贺卡', '包装'],
        features: ['普通烫金', '适合细线条'],
        validSurfaces: ['淨紙面', '單粉紙'],
        validAreas: ['中小面积'],
        costIndex: 1,
        laminatingCompatibility: { status: 'partial', compatibleTypes: ['普通预涂膜'] },
        uvPrintingCompatible: true,
        ledUvGlitterCompatible: false,
        printingCompatible: true,
        flatStampingCompatible: true,
        reliefStampingCompatible: false,
        matteStampingCompatible: false,
        glitterStampingCompatible: false
      }
    ],
    normalWearResistant: [
      {
        name: '普通耐磨金纸A',
        model: 'PWR-001',
        id: 'MAT002',
        type: '普通耐磨',
        color: 'gold',
        temperature: '115~135℃',
        tension: '标准',
        width: '640mm',
        length: '120m',
        scenarios: ['包装', '精平装'],
        features: ['普通耐磨', '适合中等面积'],
        validSurfaces: ['淨紙面', '單粉紙', '雙粉紙'],
        validAreas: ['中面积', '大面积'],
        costIndex: 2,
        laminatingCompatibility: { status: 'all', compatibleTypes: ['普通预涂膜', 'UV过胶'] },
        uvPrintingCompatible: true,
        ledUvGlitterCompatible: true,
        printingCompatible: true,
        flatStampingCompatible: true,
        reliefStampingCompatible: true,
        matteStampingCompatible: false,
        glitterStampingCompatible: false
      }
    ],
    highWearResistant: [
      {
        name: '高耐磨金纸A',
        model: 'HWR-001',
        id: 'MAT003',
        type: '高耐磨',
        color: 'gold',
        temperature: '120~140℃',
        tension: '紧身',
        width: '640mm',
        length: '150m',
        scenarios: ['精平装', '综合'],
        features: ['高耐磨', '适合大面积'],
        validSurfaces: ['淨紙面', '單粉紙', '雙粉紙', '特种纸'],
        validAreas: ['大面积', '超大面积'],
        costIndex: 3,
        laminatingCompatibility: { status: 'all', compatibleTypes: ['普通预涂膜', 'UV过胶', 'LED-UV过胶'] },
        uvPrintingCompatible: true,
        ledUvGlitterCompatible: true,
        printingCompatible: true,
        flatStampingCompatible: true,
        reliefStampingCompatible: true,
        matteStampingCompatible: true,
        glitterStampingCompatible: true
      }
    ]
  };

  // 添加缺失的方法
  const getPapersByType = (type: string): Product[] => {
    return mockProducts[type] || [];
  };

  const updatePaper = (type: string, updatedPaper: Product) => {
    const papers = mockProducts[type];
    if (papers) {
      const index = papers.findIndex(p => p.id === updatedPaper.id);
      if (index !== -1) {
        papers[index] = { ...updatedPaper };
      }
    }
  };

  // 使用 computed 替代 getter，确保 Pinia setup store 正确处理响应式委托
  const matchingParams = computed(() => matchingStore.matchingParams);
  const firstMatch = computed(() => {
    console.log('hotStampingStore.firstMatch getter called');
    console.log('matchingStore.firstMatchParams:', matchingStore.firstMatchParams);
    const result = matchingStore.firstMatch;
    console.log('hotStampingStore.firstMatch returning:', result);
    return result;
  });
  const secondMatch = computed(() => matchingStore.secondMatch);
  const secondMatchParams = computed(() => {
    console.log('hotStampingStore.secondMatchParams getter called');
    console.log('matchingStore.secondMatchParams:', matchingStore.secondMatchParams);
    const result = matchingStore.secondMatchParams;
    console.log('hotStampingStore.secondMatchParams returning:', result);
    return result;
  });
  const firstMatchParams = computed(() => matchingStore.firstMatchParams);
  const thirdMatch = computed(() => matchingStore.thirdMatch);
  const thirdMatchParams = computed(() => matchingStore.thirdMatchParams);
  const otherMatches = computed(() => matchingStore.otherMatches);
  const searchMatchResult = computed({
    get: () => {
      console.log('hotStampingStore.searchMatchResult getter called');
      const result = matchingStore.searchMatchResult;
      console.log('hotStampingStore.searchMatchResult returning:', result);
      return result;
    },
    set: (value) => {
      console.log('hotStampingStore.searchMatchResult setter called with:', value);
      matchingStore.setSearchMatchResult(value);
    },
  });

  return {
    // 从 matchingStore 导出的属性和方法（computed 确保响应式）
    matchingParams,
    firstMatch,
    secondMatch,
    secondMatchParams,
    firstMatchParams,
    thirdMatch,
    thirdMatchParams,
    otherMatches,
    searchMatchResult,
    updateMatchingParams: matchingStore.updateMatchingParams,
    clearSearchResult: matchingStore.clearSearchResult,
    setSecondMatchResult: matchingStore.setSecondMatchResult,
    setSecondMatchParams: matchingStore.setSecondMatchParams,
    setFirstMatchParams: matchingStore.setFirstMatchParams,
    setThirdMatchParams: matchingStore.setThirdMatchParams,
    setThirdMatchResult: matchingStore.setThirdMatchResult,
    clearSecondMatchResult: matchingStore.clearSecondMatchResult,
    resetAllParams: () => {
      console.log('hotStampingStore.resetAllParams called');
      console.log('Before reset - searchResults:', searchResults.value);
      console.log('Before reset - secondMatchResults:', secondMatchResults.value);
      console.log('Before reset - searchMatchResult:', matchingStore.searchMatchResult);

      // 调用 matchingStore 的 resetAllParams 方法
      matchingStore.resetAllParams();

      // 清空搜索结果数组
      console.log('清空第一次匹配结果');
      searchResults.value = [];

      // 确保搜索结果完全清空
      console.log('强制清空 searchResults');
      setTimeout(() => {
        searchResults.value = [];
        console.log('After forced clear - searchResults:', searchResults.value);
      }, 0);

      // 清空第二次匹配结果数组
      secondMatchResults.value = [];

      // 清空第三次匹配结果数组
      thirdMatchResults.value = [];

      console.log('After reset - searchResults:', searchResults.value);
      console.log('After reset - secondMatchResults:', secondMatchResults.value);
      console.log('After reset - thirdMatchResults:', thirdMatchResults.value);
      console.log('After reset - searchMatchResult:', matchingStore.searchMatchResult);
    },

    // 移除了对goldFoilDataStore的依赖，因为我们直接从API获取数据

    // 搜索结果数组
    searchResults,

    // 第二次匹配结果数组
    secondMatchResults,

    // 第三次匹配结果数组
    thirdMatchResults,

    // 兼容旧的API
    bestMatch,

    // 新增的方法
    getPapersByType,
    updatePaper,

    // 设置搜索结果
    setSearchResults: (results: any[]) => {
      searchResults.value = results;
      console.log('设置搜索结果:', results);
    },

    // 设置分页信息
    setPaginationInfo: (info: { total: number; pageSize: number; currentPage: number; totalPages?: number }) => {
      paginationInfo.value = info;
      console.log('设置分页信息:', info);
    },

    // 获取分页信息
    getPaginationInfo: () => paginationInfo.value,

    // 设置最后一次查询参数
    setLastQueryParams: (params: any) => {
      lastQueryParams.value = params;
      console.log('设置查询参数:', params);
    },

    // 获取最后一次查询参数
    getLastQueryParams: () => lastQueryParams.value
  };
});