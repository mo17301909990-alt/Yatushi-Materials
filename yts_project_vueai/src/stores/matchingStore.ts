import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

/**
 * 匹配参数接口
 */
interface MatchingParams {
  surfaceType: string;
  area: string;
  pattern: string;
  stampingType: string;
  hotStampingType: string; // 添加烫金类型(X)字段
  scenario: string;
  tension: string;
  process: string;
  customerModel: string;
  laminatingType: string;
  fabricType: string;
  price: string;
  companyNumber: string; // 添加公司编号字段
  gpNo: string; // 添加客户编号字段
  productType: string; // 添加产品类型字段
  patternCharacteristic: string; // 添加烫金图案特征字段
  materialType: string; // 添加材料适用界面字段
  stampingPatternType: string; // 添加烫金图案字段
  priceLevel: string; // 添加价格优先度字段
  uvPrinting: string; // 添加燙後加工.印刷UV字段
  silk_screen_led_uv_sparkle_powder: string; // 添加燙後加工.絲印LED UV灑閃粉字段（使用后端字段名）
  printing: string; // 添加燙後加工.烫金+印刷字段（使用后端字段名）
  // ID 字段（用于查询参数）
  productTypeId?: number; // 产品类型ID
  preProcessingStepsOptionId?: number; // 前工序选项ID
  patternId?: number; // 烫金图案ID
  patternAreaOptionId?: number; // 燙金圖案（面积/类型）ID
  hotStampingTypeOptionId?: number; // 烫金类型ID
  clothPaperTypeId?: number; // 材料适用界面ID
  // 显示名称字段（用于反馈模板）
  productTypeName?: string; // 产品类型显示名称
  preProcessingStepsName?: string; // 前工序选项显示名称
  patternName?: string; // 烫金图案显示名称
  patternAreaOptionName?: string; // 燙金圖案（面积/类型）显示名称
  hotStampingTypeName?: string; // 烫金类型显示名称（级联路径）
  materialInterfaceName?: string; // 适用界面显示名称（级联路径）
  laminatingName?: string; // 过胶选项显示名称（级联路径）
  // 过胶兼容性相关字段
  postProcessingStepId: number; // 后加工步骤ID
  laminationMaterialId: number; // 过胶材质ID
  interfaceTypeId: number; // 接口类型ID
  laminationCompatibilityStatus: string; // 兼容性状态
  clothPaperCompatibilityStatus?: string; // 适用界面兼容性状态
  laminating?: string; // 过胶标记
  // 烫金类型具体字段
  flatHotStamping: string; // 8.1平面燙金
  embossedGoldStamping: string; // 8.2立體燙金
  refractiveHolographicPatternedTexturedHotStamping: string; // 8.3折光燙金、有紋燙金
  postHotStampingEmbossingDebossing: string; // 8.4燙金後擊凸、壓紋
  // 烫金类型(X)具体字段
  flatHotStampingCenter: string; // 平面烫金-於中間位
  flatHotStampingEdge: string; // 平面烫金-到邊位
  embossedHotStampingWithOverlap: string; // 立體烫金(含烫金+击凸，且重叠)
  matteHotStamping: string; // 磨砂烫金
  refractiveHotStamping: string; // 折光烫金
  // 适用界面(前工序)字段
  oil6812Glossy33013440Dumb: string; // 2.1 油性6812光面+3301+3440哑面
  gvLedUvGlossMatte: string; // 2.2 GV+LED+UV光面+哑面
  oilBasedGlossMattePowderPaper: string; // 2.3 油性光面+哑面+粉纸
  oilBasedGlossMatteNonPowderPaper: string; // 2.4 油性光面+哑面+非粉纸
  standardFuronghui22d: string; // 2.5 标准芙蓉辉22d
  preCoatedHy120665: string; // 2.6 预涂HY120665
  highTackPreCoatedHy40: string; // 2.7 高粘预涂HY40
  preCoatedEconomicalHighWearResistantYt008a: string; // 2.8 预涂经济型高耐磨YT008A
  preCoatedHighWearResistantTn008: string; // 2.9 预涂高耐磨TN008
  preCoatedStandardWearResistantKvmbF: string; // 2.10 预涂标准耐磨KVMB-F
  softTouchMatteLaminate6015a: string; // 2.11 软触感哑膜6015A
}

/**
 * 匹配结果接口
 */
interface MatchResult {
  name: string;
  model: string;
  id: string;
  score: number;
  features: string[];
  scenarios: string[];
  temperature: string;
  width: string;
  length: string;
  tension: string;
  costIndex: number;
}

/**
 * 烫金纸匹配逻辑 Store
 * 专注于烫金纸匹配算法和结果管理
 */
export const useMatchingStore = defineStore('matching', () => {
  // 匹配参数
  const matchingParams = ref<MatchingParams>({
    surfaceType: '',
    area: '',
    pattern: '',
    stampingType: '',
    hotStampingType: '',
    scenario: '',
    tension: '',
    process: '',
    customerModel: '',
    laminatingType: '',
    fabricType: '',
    price: '',
    companyNumber: '',
    gpNo: '',
    productType: '',
    patternCharacteristic: '',
    materialType: '',
    stampingPatternType: '',
    priceLevel: '',
    uvPrinting: '',
    silk_screen_led_uv_sparkle_powder: '',
    printing: '',
    flatHotStamping: '',
    embossedGoldStamping: '',
    refractiveHolographicPatternedTexturedHotStamping: '',
    postHotStampingEmbossingDebossing: '',
    flatHotStampingCenter: '',
    flatHotStampingEdge: '',
    embossedHotStampingWithOverlap: '',
    matteHotStamping: '',
    refractiveHotStamping: '',
    oil6812Glossy33013440Dumb: '',
    gvLedUvGlossMatte: '',
    oilBasedGlossMattePowderPaper: '',
    oilBasedGlossMatteNonPowderPaper: '',
    standardFuronghui22d: '',
    preCoatedHy120665: '',
    highTackPreCoatedHy40: '',
    preCoatedEconomicalHighWearResistantYt008a: '',
    preCoatedHighWearResistantTn008: '',
    preCoatedStandardWearResistantKvmbF: '',
    softTouchMatteLaminate6015a: ''
  });

  // 根据公司编号搜索的匹配结果
  const searchMatchResult = ref<MatchResult | null>(null);

  // 第二次匹配结果
  const secondMatchResult = ref<MatchResult | null>(null);

  // 第二次匹配参数
  const secondMatchParams = ref<any>(null);

  // 第三次匹配参数
  const thirdMatchParams = ref<any>(null);

  // 第三次匹配结果
  const thirdMatchResult = ref<MatchResult | null>(null);

  // 第一次匹配的参数（用于第二次匹配）
  const firstMatchParams = ref<any>(null);

  // 第一次匹配结果
  const firstMatch = computed<MatchResult | null>(() => {
    console.log('Computing firstMatch in matchingStore');
    console.log('searchMatchResult.value:', searchMatchResult.value);
    console.log('firstMatchParams.value:', firstMatchParams.value);

    // 强制触发响应式更新
    const params = firstMatchParams.value;

    // 如果有根据公司编号搜索的匹配结果，优先使用它
    if (searchMatchResult.value) {
      console.log('Returning searchMatchResult.value');
      return searchMatchResult.value;
    }

    // 如果有第一次匹配参数，则创建一个虚拟的匹配结果
    if (params && params.paramName && params.paramValue) {
      console.log('Creating virtual match result from firstMatchParams');
      return {
        name: params.displayName || '未知产品',
        model: params.paramValue,
        id: 'virtual-' + Date.now(),
        score: 100,
        features: ['虚拟匹配结果'],
        scenarios: ['通用'],
        temperature: '110~130℃',
        width: '640mm',
        length: '120m',
        tension: '标准',
        costIndex: 1
      };
    }

    // 如果没有搜索结果，返回 null
    console.log('No match result found, returning null');
    return null;
  });

  // 第二次匹配结果
  const secondMatch = computed<MatchResult | null>(() => {
    // 如果有第二次匹配结果，返回它
    if (secondMatchResult.value) {
      return secondMatchResult.value;
    }

    // 如果没有第二次匹配结果，返回 null
    return null;
  });

  // 第三次匹配结果
  const thirdMatch = computed<MatchResult | null>(() => {
    // 如果有第三次匹配结果，返回它
    if (thirdMatchResult.value) {
      return thirdMatchResult.value;
    }

    // 如果没有第三次匹配结果，返回 null
    return null;
  });

  // 其他匹配结果 - 简化版，返回空数组
  const otherMatches = computed<MatchResult[]>(() => {
    return [];
  });

  /**
   * 更新匹配参数
   * @param params 要更新的参数
   */
  const updateMatchingParams = (params: Partial<MatchingParams>) => {
    matchingParams.value = { ...matchingParams.value, ...params };
  };

  /**
   * 设置搜索匹配结果
   * @param result 搜索匹配结果
   */
  const setSearchMatchResult = (result: MatchResult | null) => {
    searchMatchResult.value = result;
  };

  /**
   * 设置第二次匹配结果
   * @param result 第二次匹配结果
   */
  const setSecondMatchResult = (result: MatchResult | null) => {
    secondMatchResult.value = result;
  };

  /**
   * 设置第二次匹配参数
   * @param params 第二次匹配参数
   */
  const setSecondMatchParams = (params: any) => {
    console.log('保存第二次匹配参数:', params);

    // 使用解构赋值来创建一个新对象，确保响应式更新
    if (params) {
      secondMatchParams.value = { ...params };
    } else {
      secondMatchParams.value = null;
    }

    // 打印日志以便于调试
    console.log('设置后的 secondMatchParams.value:', secondMatchParams.value);
  };

  /**
   * 设置第一次匹配参数（用于第二次匹配）
   * @param params 第一次匹配参数
   */
  const setFirstMatchParams = (params: any) => {
    console.log('保存第一次匹配参数:', params);

    // 使用解构赋值来创建一个新对象，确保响应式更新
    if (params) {
      firstMatchParams.value = { ...params };
    } else {
      firstMatchParams.value = null;
    }

    // 打印日志以便于调试
    console.log('设置后的 firstMatchParams.value:', firstMatchParams.value);

    // 强制触发响应式更新
    setTimeout(() => {
      console.log('延迟检查 firstMatchParams.value:', firstMatchParams.value);
    }, 0);
  };

  /**
   * 设置第三次匹配参数
   * @param params 第三次匹配参数
   */
  const setThirdMatchParams = (params: any) => {
    console.log('保存第三次匹配参数:', params);

    // 使用解构赋值来创建一个新对象，确保响应式更新
    if (params) {
      thirdMatchParams.value = { ...params };
    } else {
      thirdMatchParams.value = null;
    }

    // 打印日志以便于调试
    console.log('设置后的 thirdMatchParams.value:', thirdMatchParams.value);
  };

  /**
   * 设置第三次匹配结果
   * @param result 第三次匹配结果
   */
  const setThirdMatchResult = (result: MatchResult | null) => {
    thirdMatchResult.value = result;
  };

  /**
   * 清除搜索结果
   */
  const clearSearchResult = () => {
    searchMatchResult.value = null;
  };

  /**
   * 清除第二次匹配结果
   */
  const clearSecondMatchResult = () => {
    secondMatchResult.value = null;
  };

  /**
   * 重置所有参数
   */
  const resetAllParams = () => {
    // 重置匹配参数
    matchingParams.value = {
      surfaceType: '',
      area: '',
      pattern: '',
      stampingType: '',
      hotStampingType: '',
      scenario: '',
      tension: '',
      process: '',
      customerModel: '',
      laminatingType: '',
      fabricType: '',
      price: '',
      companyNumber: '',
      gpNo: '',
      productType: '',
      patternCharacteristic: '',
      materialType: '',
      stampingPatternType: '',
      priceLevel: '',
      uvPrinting: '',
      silk_screen_led_uv_sparkle_powder: '',
      printing: '',
      flatHotStamping: '',
      embossedGoldStamping: '',
      refractiveHolographicPatternedTexturedHotStamping: '',
      postHotStampingEmbossingDebossing: '',
      flatHotStampingCenter: '',
      flatHotStampingEdge: '',
      embossedHotStampingWithOverlap: '',
      matteHotStamping: '',
      refractiveHotStamping: '',
      oil6812Glossy33013440Dumb: '',
      gvLedUvGlossMatte: '',
      oilBasedGlossMattePowderPaper: '',
      oilBasedGlossMatteNonPowderPaper: '',
      standardFuronghui22d: '',
      preCoatedHy120665: '',
      highTackPreCoatedHy40: '',
      preCoatedEconomicalHighWearResistantYt008a: '',
      preCoatedHighWearResistantTn008: '',
      preCoatedStandardWearResistantKvmbF: '',
      softTouchMatteLaminate6015a: ''
    };

    // 清除搜索结果
    searchMatchResult.value = null;

    // 清除第二次匹配结果
    secondMatchResult.value = null;

    // 清除第二次匹配参数
    secondMatchParams.value = null;

    // 清除第三次匹配结果
    thirdMatchResult.value = null;

    // 清除第三次匹配参数
    thirdMatchParams.value = null;

    // 清除第一次匹配参数
    firstMatchParams.value = null;
  };

  return {
    matchingParams,
    firstMatch,
    secondMatch,
    thirdMatch,
    otherMatches,
    searchMatchResult,
    secondMatchResult,
    secondMatchParams,
    firstMatchParams,
    thirdMatchParams,
    thirdMatchResult,
    updateMatchingParams,
    setSearchMatchResult,
    setSecondMatchResult,
    setSecondMatchParams,
    setFirstMatchParams,
    setThirdMatchParams,
    setThirdMatchResult,
    clearSearchResult,
    clearSecondMatchResult,
    resetAllParams
  };
});
