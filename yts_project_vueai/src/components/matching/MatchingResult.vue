<script setup lang="ts">
import { useHotStampingStore } from '../../stores/hotStamping';
import { useMatchingStore } from '../../stores/matchingStore';
import { useFirstMatchParamsStore } from '../../stores/firstMatchParamsStore';
import { useAuthStore } from '../../stores/auth';
import { computed, ref, watch, onMounted } from 'vue';
import axios from 'axios';
// 導入 Element Plus 組件
import 'element-plus/dist/index.css';
import { ElPagination, ElDialog, ElButton, ElMessage, ElRadio, ElRadioGroup, ElInput } from 'element-plus';
// 導入匹配度計算工具和API
import { getMatchScoreLevel, getMatchScoreColorClass } from '../../utils/matchCalculator';
import { matchPreferenceApi } from '../../api/modules/matchPreference';
import { productApi } from '../../api/modules/product';
import { announcementApi } from '../../api/modules/announcement';
import { userApi } from '../../api/modules/user';
import { preProcessingStepsApi } from '../../api/modules/preProcessingSteps';
import { productTypeOptionsApi } from '../../api/modules/productTypeOptions';
import { patternOptionsApi } from '../../api/modules/patternOptions';
import type { UserMatchPreferencesGrouped } from '../../api/types/matchPreference';

const hotStampingStore = useHotStampingStore();
const matchingStore = useMatchingStore();
const firstMatchParamsStore = useFirstMatchParamsStore();
const authStore = useAuthStore();

// 用戶匹配偏好數據
const userPreferences = ref<UserMatchPreferencesGrouped>({
  color: [],
  size: [],
  tightness: [],
  temperature: [],
  performance: []
});

// 分頁相關狀態
const currentPage = ref(1);
const pageSize = ref(15);
const total = ref(0); // 从后端获取的总数
const loading = ref(false); // 加载状态

// 排序相關狀態
const sortBy = ref<'default' | 'matchScore' | 'price'>('matchScore');
const sortOrder = ref<'asc' | 'desc'>('desc');

// 匹配度规则类型：'price' 表示价格匹配度，'tag' 表示标签配置匹配度
// 已固定为价格匹配度，不再支持切换
const matchScoreRule = ref('price'); // 固定使用价格匹配度规则

// 詳情對話框相關狀態
const dialogVisible = ref(false);
const currentProduct = ref<any>(null);

// 反馈对话框相关状态
const feedbackDialogVisible = ref(false);
const feedbackProduct = ref<any>(null);
const feedbackType = ref<'mismatch' | 'invalid' | 'rule_mismatch'>('mismatch');
const feedbackContent = ref('');
const submittingFeedback = ref(false);

// 獲取用戶匹配偏好數據
const loadUserPreferences = async () => {
  if (!authStore.userInfo?.id) {
    return;
  }

  try {
    const preferences = await matchPreferenceApi.getUserPreferences(authStore.userInfo.id);
    userPreferences.value = preferences;
    console.log('加載用戶匹配偏好:', preferences);
  } catch (error) {
    console.error('加載用戶匹配偏好失敗:', error);
    // 如果加載失敗，使用空的偏好設置
    userPreferences.value = {
      color: [],
      size: [],
      tightness: [],
      temperature: [],
      performance: []
    };
  }
};

// 計算產品匹配度
const calculateProductMatchScore = (product: any) => {
  // 檢查是否有用戶偏好設置
  const hasPreferences = Object.values(userPreferences.value).some(prefs => prefs.length > 0);
  if (!hasPreferences) {
    return null; // 沒有偏好設置時不顯示匹配度
  }

  // 構建產品數據對象，映射字段名
  const productData = {
    color: product.color,
    size: product.size,
    tightness: product.tightness || product.tension, // 鬆緊度可能有不同字段名
    temperature: product.temperatureRange || product.temperature, // 溫度範圍可能有不同字段名
    performance: product.performance || product.features // 特性與場景可能有不同字段名
  };

  // 使用嚴格全等匹配計算匹配度
  const matchedFields: string[] = [];
  const unmatchedFields: string[] = [];

  // 遍歷所有匹配字段
  const MATCH_FIELDS = ['color', 'size', 'tightness', 'temperature', 'performance'] as const;

  for (const field of MATCH_FIELDS) {
    const userValues = userPreferences.value[field] || [];
    const productValue = productData[field];

    // 如果用戶沒有設置該字段的偏好，跳過該字段
    if (userValues.length === 0) {
      continue;
    }

    // 嚴格全等匹配：檢查產品值是否在用戶偏好中
    const isMatched = userValues.some(userValue => {
      const cleanUserValue = String(userValue).trim();
      const cleanProductValue = String(productValue || '').trim();
      return cleanUserValue === cleanProductValue;
    });

    if (isMatched) {
      matchedFields.push(field);
    } else {
      unmatchedFields.push(field);
    }
  }

  // 計算匹配度
  const totalFields = matchedFields.length + unmatchedFields.length;
  const matchedCount = matchedFields.length;
  const matchScore = totalFields > 0 ? Math.round((matchedCount / totalFields) * 100) : 0;

  // 調試信息
  console.log('🔍 嚴格匹配調試信息:');
  console.log('用戶偏好:', userPreferences.value);
  console.log('產品數據:', productData);
  console.log('匹配字段:', matchedFields);
  console.log('未匹配字段:', unmatchedFields);
  console.log('匹配度:', matchScore + '%');
  console.log('詳細對比:');
  for (const field of MATCH_FIELDS) {
    const userValues = userPreferences.value[field] || [];
    const productValue = productData[field];
    if (userValues.length > 0) {
      console.log(`${field}: 用戶偏好[${userValues.join(', ')}] vs 產品值[${productValue}] = ${matchedFields.includes(field) ? '✓匹配' : '✗不匹配'}`);
    }
  }
  console.log('-------------------');

  return {
    matchScore,
    matchedFields,
    unmatchedFields,
    totalFields,
    matchedCount
  };
};

// 獲取當前匹配階段 - 根據有效參數數量確定匹配類型
const getCurrentMatchStage = computed(() => {
  const allParams = matchingStore.matchingParams;

  // 定義參數分組 - 避免重複計數相關參數
  const parameterGroups = {
    // 燙金類型組 - 這些參數作為一組計算
    stampingTypeGroup: ['stampingType', 'flatHotStamping', 'embossedGoldStamping', 'refractiveHolographicPatternedTexturedHotStamping', 'postHotStampingEmbossingDebossing'],
    // 適用界面(前工序)組 - 這些參數作為一組計算
    interfaceGroup: ['oil6812Glossy33013440Dumb', 'gvLedUvGlossMatte', 'oilBasedGlossMattePowderPaper', 'oilBasedGlossMatteNonPowderPaper', 'standardFuronghui22d', 'preCoatedHy120665', 'highTackPreCoatedHy40', 'preCoatedEconomicalHighWearResistantYt008a', 'preCoatedHighWearResistantTn008', 'preCoatedStandardWearResistantKvmbF', 'softTouchMatteLaminate6015a'],
    // 6.適用界面組 - 這些參數作為一組計算
    materialGroup: ['plasticFilmNormalVET', 'plasticFilmScratchResistantAVET', 'waxPaperGTF', 'waxPaperZTF', 'fabricPaperNylonJHT8001', 'fabricPaperNylonJHT8004', 'fabricPaperNylonJHT8013', 'fabricPaperPolyesterJHT8002', 'fabricPaperPolyesterJHT8010', 'fabricPaperPolyesterJHT8014', 'fabricPaperPolyesterJHT8015', 'fabricPaperPolyesterLTS8003', 'fabricPaperPolyesterESM', 'fabricPaperImitationCottonJHT8003', 'fabricPaperImitationCottonJHT8017', 'fabricPaperPolyCottonJHT8006', 'fabricPaperPolyCottonJHT8008'],
    // 7.燙金圖案組 - 這些參數作為一組計算
    stampingPatternGroup: ['gradientAndDots', 'thinLinesAndLetters', 'mediumSmallAreaThinLinesAndLetters', 'mediumLargeAreaThinLinesAndLetters', 'extraLargeArea'],
    // 其他獨立參數
    independentParams: ['surfaceType', 'area', 'pattern', 'scenario', 'tension', 'process', 'customerModel', 'laminatingType', 'fabricType', 'price', 'companyNumber', 'gpNo', 'productType', 'patternType', 'materialType', 'stampingPatternType', 'priceLevel', 'uvPrinting', 'silk_screen_led_uv_sparkle_powder', 'printing']
  };

  let paramCount = 0;
  const selectedParams = [];

  // 檢查燙金類型組 - 如果有任何一個燙金類型相關參數有值，則計為1個參數
  const hasStampingTypeParam = parameterGroups.stampingTypeGroup.some(key => {
    const value = allParams[key as keyof typeof allParams];
    return value && value !== '' && value !== null && value !== undefined;
  });

  if (hasStampingTypeParam) {
    paramCount++;
    selectedParams.push('燙金類型');
  }

  // 檢查適用界面(前工序)組 - 如果有任何一個適用界面參數有值，則計為1個參數
  const hasInterfaceParam = parameterGroups.interfaceGroup.some(key => {
    const value = allParams[key as keyof typeof allParams];
    return value && value !== '' && value !== null && value !== undefined;
  });

  if (hasInterfaceParam) {
    paramCount++;
    selectedParams.push('適用界面(前工序)');
  }

  // 檢查6.適用界面組 - 如果有任何一個材料適用界面參數有值，則計為1個參數
  const hasMaterialParam = parameterGroups.materialGroup.some(key => {
    const value = allParams[key as keyof typeof allParams];
    return value && value !== '' && value !== null && value !== undefined;
  });

  if (hasMaterialParam) {
    paramCount++;
    selectedParams.push('適用界面');
  }

  // 檢查7.燙金圖案組 - 如果有任何一個燙金圖案參數有值，則計為1個參數
  const hasStampingPatternParam = parameterGroups.stampingPatternGroup.some(key => {
    const value = allParams[key as keyof typeof allParams];
    return value && value !== '' && value !== null && value !== undefined;
  });

  if (hasStampingPatternParam) {
    paramCount++;
    selectedParams.push('燙金圖案');
  }

  // 檢查其他獨立參數
  parameterGroups.independentParams.forEach(key => {
    const value = allParams[key as keyof typeof allParams];
    if (value && value !== '' && value !== null && value !== undefined) {
      paramCount++;
      selectedParams.push(`${key}: ${value}`);
    }
  });

  console.log('getCurrentMatchStage - 有效參數數量:', paramCount);
  console.log('getCurrentMatchStage - 有效參數:', selectedParams);

  return paramCount;
});

// 統一的匹配結果 - 根據參數數量確定匹配類型標題，按優先級顯示可用結果
const allMatchResults = computed(() => {
  const paramCount = getCurrentMatchStage.value;

  console.log('MatchingResult - 參數數量:', paramCount);
  console.log('可用匹配結果:', {
    searchResults: hotStampingStore.searchResults?.length || 0,
    searchMatchResult: hotStampingStore.searchMatchResult ? 1 : 0,
    secondMatchResults: hotStampingStore.secondMatchResults?.length || 0,
    thirdMatchResults: hotStampingStore.thirdMatchResults?.length || 0
  });

  // 根据参数数量确定匹配类型标题和颜色
  const getMatchTypeByParamCount = (count: number) => {
    if (count >= 3) return { source: '第三次匹配', color: 'purple', stage: 3 };
    if (count === 2) return { source: '第二次匹配', color: 'green', stage: 2 };
    if (count === 1) return { source: '第一次匹配', color: 'blue', stage: 1 };
    return { source: '', color: 'gray', stage: 0 };
  };

  // 按优先级获取可用的匹配结果（优先级：第三次 > 第二次 > 第一次）
  let availableResults: any[] = [];

  if (hotStampingStore.thirdMatchResults && hotStampingStore.thirdMatchResults.length > 0) {
    availableResults = hotStampingStore.thirdMatchResults;
  } else if (hotStampingStore.secondMatchResults && hotStampingStore.secondMatchResults.length > 0) {
    availableResults = hotStampingStore.secondMatchResults;
  } else if (hotStampingStore.searchResults && hotStampingStore.searchResults.length > 0) {
    availableResults = hotStampingStore.searchResults;
  } else if (hotStampingStore.searchMatchResult) {
    availableResults = [hotStampingStore.searchMatchResult];
  }

  // 获取匹配类型信息（基于参数数量）
  const matchTypeInfo = getMatchTypeByParamCount(paramCount);

  // 返回结果
  return {
    results: availableResults,
    source: matchTypeInfo.source,
    color: matchTypeInfo.color,
    stage: matchTypeInfo.stage
  };
});

// 检查是否有匹配结果
const hasMatchResults = computed(() => {
  return allMatchResults.value.results.length > 0;
});

// 监听匹配结果变化，重置页码
watch(() => allMatchResults.value.results, (newResults, oldResults) => {
  // 当匹配结果变化时，重置当前页码为1
  if (newResults.length !== oldResults?.length || JSON.stringify(newResults) !== JSON.stringify(oldResults)) {
    currentPage.value = 1;
  }
}, { deep: true });

// 排序后的结果
const sortedResults = computed(() => {
  const results = allMatchResults.value.results;
  if (!results || results.length === 0) {
    return [];
  }

  // 为每个产品计算匹配度并缓存结果
  const resultsWithMatch = results.map(item => ({
    ...item,
    matchResult: calculateProductMatchScore(item)
  }));

  // 根据排序方式排序
  if (sortBy.value === 'matchScore') {
    return resultsWithMatch.sort((a, b) => {
      const scoreA = a.matchResult?.matchScore || 0;
      const scoreB = b.matchResult?.matchScore || 0;

      if (sortOrder.value === 'desc') {
        return scoreB - scoreA;
      } else {
        return scoreA - scoreB;
      }
    });
  }

  if (sortBy.value === 'price') {
    return resultsWithMatch.sort((a, b) => {
      const priceA = parseFloat(a.price) || 0;
      const priceB = parseFloat(b.price) || 0;

      if (priceA !== priceB) {
        // 价格不同时按价格排序
        if (sortOrder.value === 'desc') {
          return priceB - priceA;
        } else {
          return priceA - priceB;
        }
      } else {
        // 价格相同时按匹配度从高到低排序
        const scoreA = a.matchResult?.matchScore || 0;
        const scoreB = b.matchResult?.matchScore || 0;
        return scoreB - scoreA;
      }
    });
  }

  return resultsWithMatch; // 默认排序
});

// 分页数据 - 直接使用后端返回的数据（后端已排序和分页）
const paginatedResults = computed(() => {
  // 后端已经完成排序和分页，直接使用返回的数据
  return allMatchResults.value.results.map(enhanceSingleProduct);
});

// 监听store中的分页信息变化
watch(() => hotStampingStore.getPaginationInfo(), (paginationInfo) => {
  if (paginationInfo) {
    total.value = paginationInfo.total;
    pageSize.value = paginationInfo.pageSize;
    currentPage.value = paginationInfo.currentPage;
    console.log('分页信息更新:', paginationInfo);
  }
}, { immediate: true });

// 获取当前匹配参数的显示信息
const currentMatchParams = computed(() => {
  const matchingParams = matchingStore.matchingParams;
  const firstMatchParams = firstMatchParamsStore.params;
  const params = [];
  const addedLabels = new Set(); // 用于去重

  // 首先添加 firstMatchParams 中的参数（如果存在）
  if (firstMatchParams && firstMatchParams.displayName && firstMatchParams.paramValue) {
    let displayValue = firstMatchParams.paramValue;

    // 处理特殊的显示值
    if (firstMatchParams.paramValue === 'V') {
      displayValue = '适用';
    } else if (firstMatchParams.paramValue === 'X') {
      displayValue = '不适用';
    } else if (firstMatchParams.displayValue) {
      // 如果有 displayValue 字段，优先使用它（用于产品类型和烫金图案类型）
      displayValue = firstMatchParams.displayValue;
    } else if (firstMatchParams.paramName === 'stampingType') {
      // 烫金类型的特殊处理
      const stampingTypeLabels: { [key: string]: string } = {
        'flatHotStamping': '平面燙金',
        'embossedGoldStamping': '立體燙金',
        'refractiveHolographicPatternedTexturedHotStamping': '折光燙金、有紋燙金',
        'postHotStampingEmbossingDebossing': '燙金後擊凸、壓紋'
      };
      displayValue = stampingTypeLabels[firstMatchParams.paramValue] || firstMatchParams.paramValue;
    }

    params.push({ label: firstMatchParams.displayName, value: displayValue });
    addedLabels.add(firstMatchParams.displayName);
  }

  // 然后添加 matchingParams 中的所有参数（避免重复）
  if (matchingParams.companyNumber && !addedLabels.has('公司编号')) {
    params.push({ label: '公司编号', value: matchingParams.companyNumber });
    addedLabels.add('公司编号');
  }
  if (matchingParams.productType && !addedLabels.has('产品类型')) {
    params.push({ label: '产品类型', value: matchingParams.productType });
    addedLabels.add('产品类型');
  }
  if (matchingParams.patternCharacteristic && !addedLabels.has('烫金图案类型')) {
    params.push({ label: '烫金图案类型', value: matchingParams.patternCharacteristic });
    addedLabels.add('烫金图案类型');
  }
  if (matchingParams.materialType && !addedLabels.has('材料适用界面')) {
    params.push({ label: '材料适用界面', value: matchingParams.materialType });
    addedLabels.add('材料适用界面');
  }
  if (matchingParams.stampingPatternType && !addedLabels.has('烫金图案')) {
    params.push({ label: '烫金图案', value: matchingParams.stampingPatternType });
    addedLabels.add('烫金图案');
  }

  // 烫金类型 - 检查所有烫金类型相关字段，优先显示 stampingType
  if (matchingParams.stampingType && !addedLabels.has('烫金类型')) {
    // 根据 stampingType 的值显示对应的中文名称
    const stampingTypeLabels: { [key: string]: string } = {
      'flatHotStamping': '平面燙金',
      'embossedGoldStamping': '立體燙金',
      'refractiveHolographicPatternedTexturedHotStamping': '折光燙金、有紋燙金',
      'postHotStampingEmbossingDebossing': '燙金後擊凸、壓紋'
    };
    const displayValue = stampingTypeLabels[matchingParams.stampingType] || matchingParams.stampingType;
    params.push({ label: '烫金类型', value: displayValue });
    addedLabels.add('烫金类型');
  }

  if (matchingParams.priceLevel && !addedLabels.has('价格优先度')) {
    params.push({ label: '价格优先度', value: matchingParams.priceLevel });
    addedLabels.add('价格优先度');
  }
  if (matchingParams.uvPrinting && !addedLabels.has('印刷UV')) {
    params.push({
      label: '印刷UV',
      value: matchingParams.uvPrinting === 'V' ? '适用' : matchingParams.uvPrinting === 'X' ? '不适用' : matchingParams.uvPrinting
    });
    addedLabels.add('印刷UV');
  }
  if (matchingParams.silk_screen_led_uv_sparkle_powder && !addedLabels.has('絲印LED UV灑閃粉')) {
    params.push({
      label: '絲印LED UV灑閃粉',
      value: matchingParams.silk_screen_led_uv_sparkle_powder === 'V' ? '适用' : matchingParams.silk_screen_led_uv_sparkle_powder === 'X' ? '不适用' : matchingParams.silk_screen_led_uv_sparkle_powder
    });
    addedLabels.add('絲印LED UV灑閃粉');
  }
  if (matchingParams.printing && !addedLabels.has('烫金+印刷')) {
    params.push({
      label: '烫金+印刷',
      value: matchingParams.printing === 'V' ? '适用' : matchingParams.printing === 'X' ? '不适用' : matchingParams.printing
    });
    addedLabels.add('烫金+印刷');
  }

  return params;
});



// 打开详情对话框
const openDetails = (product: any) => {
  currentProduct.value = product;
  dialogVisible.value = true;
};

// 复制到剪贴板
const copyToClipboard = async (text: string) => {
  try {
    await navigator.clipboard.writeText(text);
    // 显示成功提示
    ElMessage.success('物料編號已複製到剪貼板');
    console.log('已复制到剪贴板:', text);
  } catch (err) {
    console.error('复制失败:', err);
    // 降级方案：使用传统的复制方法
    try {
      const textArea = document.createElement('textarea');
      textArea.value = text;
      document.body.appendChild(textArea);
      textArea.select();
      document.execCommand('copy');
      document.body.removeChild(textArea);
      ElMessage.success('物料編號已複製到剪貼板');
    } catch (fallbackErr) {
      console.error('降级复制也失败:', fallbackErr);
      ElMessage.error('複製失敗，請手動複製');
    }
  }
};

// 关闭详情对话框
const closeDetails = () => {
  dialogVisible.value = false;
  currentProduct.value = null;
};

// 处理空值和特殊值的辅助函数
const formatValue = (value: string | number | null | undefined): string => {
  // 处理空值
  if (value === null || value === undefined || value === '') {
    return '无';
  }

  // 将 NA_Enum 映射为 NA
  if (String(value) === 'NA_Enum') {
    return 'NA';
  }

  return String(value);
};

// 格式化值用于显示（不显示"无"）
const formatValueForDisplay = (value: string | number | null | undefined): string => {
  // 处理空值
  if (value === null || value === undefined || value === '') {
    return '';
  }

  // 将 NA_Enum 映射为 NA
  if (String(value) === 'NA_Enum') {
    return 'NA';
  }

  return String(value);
};

// 增强单个产品数据的方法
const enhanceSingleProduct = (product: any) => {
  // 基础数据增强
  const enhanced: any = {
    ...product,
    materialNumber: product.materialNumber || product.id || product.material_number || '无',
    modelNumber: product.modelNumber || product.model || product.model_number || '无',
    companyNumber: product.companyNumber || product.company_number || '无',
    gpNo: product.gpNo || product.gp_no || '无',
    color: product.color || '无',
    tightness: product.tightness || product.tension || '标准偏紧',
    performance: product.performance || product.features || '非耐磨',
    size: product.size || '无',
    temperatureRange: product.temperatureRange || product.temperature || '无',
    hotStampingPaperType: product.hotStampingPaperType || product.hot_stamping_paper_type || '无',
    name: product.name || '无'
  };
  
  // 处理匹配度：优先使用后端返回的matchScore，如果没有则使用前端计算
  // 注意：matchScore可能是0，所以需要检查是否为undefined/null
  if (product.matchScore !== null && product.matchScore !== undefined && product.matchScore !== '') {
    // 后端已计算匹配度，使用后端值（确保是数字类型）
    const score = typeof product.matchScore === 'number' ? product.matchScore : parseInt(product.matchScore);
    enhanced.matchResult = {
      matchScore: isNaN(score) ? 0 : score,
      matchedFields: [],
      unmatchedFields: [],
      totalFields: 0,
      matchedCount: 0
    };
    console.log('使用后端匹配度:', product.name, 'matchScore:', score);
  } else {
    // 后端未计算匹配度，使用前端计算（作为后备方案）
    enhanced.matchResult = calculateProductMatchScore(enhanced);
    if (enhanced.matchResult) {
      console.log('使用前端计算匹配度:', product.name, 'matchScore:', enhanced.matchResult.matchScore);
    } else {
      console.log('无法计算匹配度:', product.name, 'matchScore字段:', product.matchScore);
    }
  }
  
  return enhanced;
};

// 生成产品描述
const getProductDescription = (item: any) => {
  // 优先显示数据库中的description字段
  if (item.descirption && item.descirption.trim()) {
    return item.descirption;
  }
  
  // 如果description为空，则使用拼接的方式作为备选
  const parts = [];
  
  // 烫金纸类型
  const paperType = formatValueForDisplay(item.hotStampingPaperType);
  if (paperType) {
    parts.push(paperType);
  }
  
  // 型号
  const model = formatValueForDisplay(item.modelNumber);
  if (model) {
    parts.push(model);
  }
  
  // Leo Touch编号
  const company = formatValueForDisplay(item.companyNumber);
  if (company) {
    parts.push(company);
  }
  
  // GP NO
  const gpNo = formatValueForDisplay(item.gpNo);
  if (gpNo) {
    parts.push(gpNo);
  }
  
  // 尺寸
  const size = formatValueForDisplay(item.size);
  if (size) {
    parts.push(size);
  }
  
  return parts.length > 0 ? parts.join(',') : '暂无描述';
};

// 当前产品的匹配度结果
const currentProductMatchResult = computed(() => {
  if (!currentProduct.value) return null;
  return calculateProductMatchScore(currentProduct.value);
});

// 字段名翻译映射
const fieldNameMap: Record<string, string> = {
  color: '颜色',
  size: '尺寸',
  tightness: '松紧度',
  temperature: '温度',
  performance: '特性与场景'
};

// 翻译字段名为中文
const translateFieldNames = (fieldNames: string[]): string[] => {
  return fieldNames.map(fieldName => fieldNameMap[fieldName] || fieldName);
};

// 切换匹配度排序
const toggleMatchScoreSort = async () => {
  if (sortBy.value === 'matchScore') {
    // 如果当前是匹配度排序，切换升序/降序
    sortOrder.value = sortOrder.value === 'desc' ? 'asc' : 'desc';
  } else {
    // 切换到匹配度排序
    sortBy.value = 'matchScore';
    sortOrder.value = 'desc';
  }
  
  // 重置到第一页并重新查询（传递排序参数给后端）
  currentPage.value = 1;
  await fetchPageData(1, pageSize.value);
};

// 切换价格排序
const togglePriceSort = async () => {
  if (sortBy.value === 'price') {
    // 如果当前是价格排序，切换升序/降序
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    // 切换到价格排序（默认升序，价格从低到高）
    sortBy.value = 'price';
    sortOrder.value = 'asc';
  }
  
  // 重置到第一页并重新查询（传递排序参数给后端）
  currentPage.value = 1;
  await fetchPageData(1, pageSize.value);
};

// 匹配度规则切换处理函数（已禁用，只使用价格匹配度）
// const handleMatchScoreRuleChange = async () => {
//   console.log('匹配度规则切换:', matchScoreRule.value);
//   
//   // 重置到第一页并重新查询
//   currentPage.value = 1;
//   await fetchPageData(1, pageSize.value);
// };

// 处理页面变化 - 调用后端API
const handlePageChange = async (page: number) => {
  currentPage.value = page;
  console.log('页面变化到:', page);
  await fetchPageData(page, pageSize.value);
};

// 处理每页条数变化 - 调用后端API
const handleSizeChange = async (size: number) => {
  pageSize.value = size;
  currentPage.value = 1; // 重置到第一页
  console.log('每页条数变化到:', size);
  await fetchPageData(1, size);
};

// 调用后端API获取分页数据
const fetchPageData = async (page: number, size: number) => {
  const queryParams = hotStampingStore.getLastQueryParams();
  if (!queryParams) {
    console.warn('没有查询参数，无法分页查询');
    ElMessage.warning('请先进行查询');
    return;
  }

  loading.value = true;
  try {
    const params = { ...queryParams };
    params.pageSize = size;
    params.offset = (page - 1) * size;
    
    // 传递排序参数给后端
    params.sortBy = sortBy.value;
    params.sortOrder = sortOrder.value;
    
    // 传递用户ID（用于匹配度计算）
    if (authStore.userInfo?.id) {
      params.userId = authStore.userInfo.id;
    }
    
    // 传递匹配度规则类型
    params.matchScoreRule = matchScoreRule.value;
    
    console.log('查询参数（包含排序、userId和matchScoreRule）:', params); // 调试日志

    const response = await axios.post('/api/api/gold-foil/match', params, {
      timeout: 300000,
      headers: { 'Content-Type': 'application/json' }
    });

    const responseData = response?.data || {};
    const items = responseData.items || [];
    const totalCount = responseData.total || 0;

    // 更新store
    hotStampingStore.setSearchResults(items);
    hotStampingStore.setPaginationInfo({
      total: totalCount,
      pageSize: size,
      currentPage: page,
      totalPages: responseData.totalPages
    });
  } catch (error) {
    console.error('分页查询失败:', error);
    ElMessage.error('查询失败，请稍后重试');
  } finally {
    loading.value = false;
  }
};


// 添加测试数据
const addTestData = () => {
  const testData = [];
  for (let i = 1; i <= 25; i++) {
    testData.push({
      id: i,
      materialNumber: `TEST${i.toString().padStart(3, '0')}`,
      modelNumber: `SY${600 + i}`,
      companyNumber: `HS#${i}G`,
      gpNo: `#${(600 + i).toString().padStart(4, '0')}`,
      color: i % 2 === 0 ? '金色' : '银色',
      tightness: i % 3 === 0 ? '标准偏紧' : '标准',
      performance: i % 4 === 0 ? '耐磨' : '非耐磨',
      size: `${100 + i}mm`,
      temperatureRange: `${120 + i}`,
      hotStampingPaperType: i % 2 === 0 ? '热转印纸' : '烫金纸',
      name: `测试产品${i}`
    });
  }
  
  // 设置测试数据到store
  hotStampingStore.setSearchResults(testData);
  console.log('添加了25条测试数据');
};

// 打开反馈对话框
const openFeedback = (product: any) => {
  feedbackProduct.value = product;
  feedbackType.value = 'mismatch';
  
  // 预填充模板
  updateFeedbackTemplate();
  
  feedbackDialogVisible.value = true;
};

// 更新反馈模板
const updateFeedbackTemplate = () => {
  if (!feedbackProduct.value) return;
  
  const product = feedbackProduct.value;
  
  if (feedbackType.value === 'mismatch') {
    feedbackContent.value = `物料信息不匹配反馈

物料编号：${formatValue(product.materialNumber)}
型号：${formatValue(product.modelNumber)}
Leo Touch编号：${formatValue(product.companyNumber)}

不匹配的原因：
[请在此填写具体的不匹配原因，例如：颜色、尺寸、性能等参数与实际不符]

建议修改：
[请在此填写建议修改的内容]

其他说明：
[如有其他说明请在此填写]`;
  } else if (feedbackType.value === 'invalid') {
    feedbackContent.value = `物料已失效反馈

物料编号：${formatValue(product.materialNumber)}
型号：${formatValue(product.modelNumber)}
Leo Touch编号：${formatValue(product.companyNumber)}

失效原因：
[请选择：已停产 / 已下架 / 已替代 / 其他]

详细说明：
[请在此填写详细的失效原因说明]

替代物料（如有）：
[如有替代物料，请在此填写替代物料的编号]

其他说明：
[如有其他说明请在此填写]`;
  } else if (feedbackType.value === 'rule_mismatch') {
    // 从 hotStampingStore 获取实际的查询参数
    const lastQueryParams = hotStampingStore.getLastQueryParams();
    let conditionsText = '';
    
    console.log('========== 筛选条件调试信息 ==========');
    console.log('1. hotStampingStore.lastQueryParams:', lastQueryParams);
    console.log('2. matchingStore.matchingParams:', matchingStore.matchingParams);
    console.log('3. firstMatchParamsStore.params:', firstMatchParamsStore.params);
    
    // 详细输出 lastQueryParams 的每个字段
    if (lastQueryParams) {
      console.log('4. lastQueryParams 详细内容:');
      for (const [key, value] of Object.entries(lastQueryParams)) {
        if (value !== null && value !== undefined && value !== '') {
          console.log(`   - ${key}: ${value} (类型: ${typeof value})`);
        }
      }
    }
    console.log('=====================================');
    
    // 从 lastQueryParams 获取并转换ID为显示文本
    const directParams = [];
    const matchingParams = lastQueryParams;
    
    console.log('开始处理筛选条件...');
    
    if (matchingParams) {
      // 处理产品类型（直接使用显示名称，无需调用API）
      if (matchingParams.productTypeId && matchingParams.productTypeName) {
        directParams.push(`  • 產品類型: ${matchingParams.productTypeName}`);
        console.log('添加参数: 产品类型 =', matchingParams.productTypeName);
      }
      
      // 处理前工序选项（直接使用显示名称）
      if (matchingParams.preProcessingStepsOptionId && matchingParams.preProcessingStepsName) {
        directParams.push(`  • 適用界面(前工序): ${matchingParams.preProcessingStepsName}`);
        console.log('添加参数: 适用界面(前工序) =', matchingParams.preProcessingStepsName);
      }
      
      // 处理图案类型（直接使用显示名称）
      if (matchingParams.patternId && matchingParams.patternName) {
        directParams.push(`  • 燙金圖案(X)(耐磨燙金紙選用): ${matchingParams.patternName}`);
        console.log('添加参数: 烫金图案类型 =', matchingParams.patternName);
      }
      
      // 处理燙金圖案（面积/类型）（直接使用显示名称）
      if (matchingParams.patternAreaOptionId && matchingParams.patternAreaOptionName) {
        directParams.push(`  • 燙金圖案(常用界面選用): ${matchingParams.patternAreaOptionName}`);
        console.log('添加参数: 燙金圖案（面积/类型） =', matchingParams.patternAreaOptionName);
      }
      
      // 处理烫金类型（直接使用显示名称）
      if (matchingParams.hotStampingTypeOptionId && matchingParams.hotStampingTypeName) {
        directParams.push(`  • 燙金類型: ${matchingParams.hotStampingTypeName}`);
        console.log('添加参数: 燙金類型 =', matchingParams.hotStampingTypeName);
      }
      
      // 处理适用界面（直接使用显示名称）
      if (matchingParams.clothPaperTypeId && matchingParams.materialInterfaceName) {
        directParams.push(`  • 適用界面: ${matchingParams.materialInterfaceName}`);
        console.log('添加参数: 適用界面 =', matchingParams.materialInterfaceName);
      }
      
      // 处理过胶选项（直接使用显示名称）
      if (matchingParams.laminatingName) {
        directParams.push(`  • ${matchingParams.laminatingName}`);
        console.log('添加参数: 过胶 =', matchingParams.laminatingName);
      }
      
      // 处理其他非ID字段
      for (const [key, value] of Object.entries(matchingParams)) {
        // 跳过ID字段（已经处理过）、对应的name字段和内部字段
        if (key === 'productTypeId' || 
            key === 'preProcessingStepsOptionId' || 
            key === 'patternId' ||
            key === 'patternAreaOptionId' ||
            key === 'hotStampingTypeOptionId' ||
            key === 'clothPaperTypeId' ||
            key === 'postProcessingStepId' ||
            key === 'laminationMaterialId' ||
            key === 'productTypeName' || 
            key === 'preProcessingStepsName' || 
            key === 'patternName' ||
            key === 'patternAreaOptionName' ||
            key === 'hotStampingTypeName' ||
            key === 'materialInterfaceName' ||
            key === 'laminatingName' ||
            key === 'clothPaperCompatibilityStatus' ||  // 已包含在 materialInterfaceName 中
            key === 'laminating' ||  // 已包含在 laminatingName 中
            !value || value === '' || 
            key.startsWith('_') || 
            key === 'pageSize' || 
            key === 'offset' ||
            key === 'sortBy' ||
            key === 'sortOrder' ||
            key === 'userId') {
          continue;
        }
        
        // 字段名映射
        const knownFields: { [key: string]: string } = {
          'companyNumber': '公司编号',
          'gpNo': 'GP编号',
          'priceLevel': '价格优先度',
          'uvPrinting': '印刷UV',
          'printing': '烫金+印刷',
          'silk_screen_led_uv_sparkle_powder': '絲印LED UV灑閃粉',
          'embossing': '击凸',
          'paizi': '牌子',
          'colorNum': '颜色编码'
        };
        
        const label = knownFields[key] || key;
        let displayValue: any = value;
        
        // 处理特殊值
        if (value === 'V') displayValue = '适用';
        else if (value === 'X') displayValue = '不适用';
        else if (typeof value === 'boolean') displayValue = value ? '是' : '否';
        
        directParams.push(`  • ${label}: ${displayValue}`);
        console.log('添加参数:', label, '=', displayValue);
      }
    }
    
    console.log('共收集到参数数量:', directParams.length);
    
    if (directParams.length > 0) {
      conditionsText = directParams.join('\n');
    } else {
      conditionsText = '  [未检测到筛选条件，请确保已选择筛选参数]';
    }
    
    feedbackContent.value = `与筛选规则不匹配反馈

物料编号：${formatValue(product.materialNumber)}
型号：${formatValue(product.modelNumber)}
Leo Touch编号：${formatValue(product.companyNumber)}

当前筛选条件：
${conditionsText}

不匹配说明：
[请在此说明该物料为什么不应该出现在当前筛选结果中，例如：
 - 该物料不符合某个筛选条件
 - 筛选规则有误
 - 该物料的某些属性与筛选条件冲突]

建议处理方式：
[请说明应该如何处理，例如：
 - 调整筛选规则
 - 修改物料属性
 - 从结果中移除该物料]

其他说明：
[如有其他说明请在此填写]`;
  }
};

// 提交反馈
const submitFeedback = async () => {
  if (!feedbackContent.value.trim()) {
    ElMessage.warning('请填写反馈内容');
    return;
  }
  
  if (!authStore.userInfo?.id) {
    ElMessage.error('用户未登录');
    return;
  }
  
  submittingFeedback.value = true;
  
  try {
    // 获取管理员用户列表
    const adminResponse = await userApi.getAdminUsers();
    const adminUsers = adminResponse.data || [];
    
    if (adminUsers.length === 0) {
      ElMessage.warning('未找到管理员用户，无法发送反馈');
      submittingFeedback.value = false;
      return;
    }
    
    // 构建管理员用户ID列表（逗号分隔）
    const adminUserIds = adminUsers.map((user: any) => user.id).join(',');
    
    // 构建消息标题
    let title = '';
    if (feedbackType.value === 'mismatch') {
      title = `【物料信息不匹配】${feedbackProduct.value.materialNumber}`;
    } else if (feedbackType.value === 'invalid') {
      title = `【物料已失效】${feedbackProduct.value.materialNumber}`;
    } else if (feedbackType.value === 'rule_mismatch') {
      title = `【与筛选规则不匹配】${feedbackProduct.value.materialNumber}`;
    }
    
    // 创建消息
    const announcement = {
      title,
      msgContent: feedbackContent.value,
      sender: authStore.userInfo.username || '用户',
      priority: 'NORMAL',
      msgCategory: '2',  // 系统消息
      msgType: 'USER',   // 指定用户
      userIds: adminUserIds,
      busType: 'material_feedback',
      busId: feedbackProduct.value.materialNumber
    };
    
    // 添加消息
    const addResponse = await announcementApi.add(announcement);
    
    if (addResponse.success && addResponse.result?.id) {
      // 发布消息
      await announcementApi.doRelease(addResponse.result.id);
      
      ElMessage.success('反馈已提交，管理员将尽快处理');
      feedbackDialogVisible.value = false;
      
      // 清空反馈内容
      feedbackContent.value = '';
      feedbackProduct.value = null;
    } else {
      throw new Error('创建消息失败');
    }
  } catch (error) {
    console.error('提交反馈失败:', error);
    ElMessage.error('提交反馈失败，请稍后重试');
  } finally {
    submittingFeedback.value = false;
  }
};

// 关闭反馈对话框
const closeFeedback = () => {
  feedbackDialogVisible.value = false;
  feedbackContent.value = '';
  feedbackProduct.value = null;
};

// 组件挂载时加载用户偏好
onMounted(() => {
  loadUserPreferences();
});


</script>

<template>
  <div class="bg-white rounded-lg shadow-lg p-6">
    <h2 class="text-xl font-bold mb-4">匹配結果</h2>

    <!-- 統一的匹配結果區域 -->
    <div class="border rounded-lg p-4">
      <div class="mb-4">
        <div class="flex justify-between items-center mb-3">
          <h3 class="text-lg font-medium"
              :class="{
                'text-purple-600': allMatchResults.color === 'purple',
                'text-green-600': allMatchResults.color === 'green',
                'text-blue-600': allMatchResults.color === 'blue',
                'text-gray-600': allMatchResults.color === 'gray'
              }">
            {{ allMatchResults.source || '匹配結果' }}
            <span v-if="allMatchResults.source" class="text-sm text-gray-500 ml-2">
              ({{ allMatchResults.results.length }} 條結果)
            </span>
            <span v-if="getCurrentMatchStage > 0" class="text-xs bg-gray-200 px-2 py-1 rounded ml-2">
              {{ getCurrentMatchStage }} 個參數
            </span>
          </h3>
        </div>

        <!-- 匹配參數顯示 -->
        <div v-if="currentMatchParams.length > 0" class="text-sm text-gray-600 bg-gray-100 p-3 rounded-md">
          <div class="font-medium text-gray-700 mb-2">當前匹配參數：</div>
          <div class="flex flex-wrap gap-2">
            <span v-for="param in currentMatchParams" :key="param.label"
                  class="inline-flex items-center px-2 py-1 bg-white rounded border border-gray-200">
              <span class="text-gray-600">{{ param.label }}:</span>
              <span class="font-medium text-gray-800 ml-1">{{ param.value }}</span>
            </span>
          </div>
        </div>
      </div>

      <!-- 匹配結果列表 -->
      <div v-if="hasMatchResults" class="mb-6">

        <!-- 排序控制 -->
        <div class="mb-4 flex justify-between items-center">
          <div class="text-sm text-gray-600">
            共找到 <span class="font-medium"
                        :class="{
                          'text-purple-600': allMatchResults.color === 'purple',
                          'text-green-600': allMatchResults.color === 'green',
                          'text-blue-600': allMatchResults.color === 'blue',
                          'text-gray-600': allMatchResults.color === 'gray'
                        }">{{ total }}</span> 條匹配結果
          </div>
          <div class="flex space-x-2 items-center">
            <!-- 匹配度规则切换（已隐藏，只使用价格匹配度） -->
            <!-- <div class="relative mr-2">
              <select
                v-model="matchScoreRule"
                @change="handleMatchScoreRuleChange"
                class="text-xs px-2 py-1 rounded border border-gray-300 bg-white text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-1 focus:ring-indigo-500"
              >
                <option value="price">价格匹配度</option>
                <option value="tag">标签匹配度</option>
              </select>
            </div> -->
            
            <!-- 匹配度排序按钮 -->
            <button
              @click="toggleMatchScoreSort"
              class="text-sm px-3 py-1 rounded transition-colors mr-2"
              :class="{ 
                'bg-indigo-200 text-indigo-800': sortBy === 'matchScore',
                'bg-indigo-100 text-indigo-700 hover:bg-indigo-200': sortBy !== 'matchScore'
              }"
            >
              {{ sortBy === 'matchScore' ? (sortOrder === 'desc' ? '↓匹配度' : '↑匹配度') : '按匹配度排序' }}
            </button>
            
            <!-- 价格排序按钮 - 已隐藏 -->
            <!-- <button
              @click="togglePriceSort"
              class="text-sm px-3 py-1 rounded transition-colors"
              :class="{ 
                'bg-green-200 text-green-800': sortBy === 'price',
                'bg-green-100 text-green-700 hover:bg-green-200': sortBy !== 'price'
              }"
            >
              {{ sortBy === 'price' ? (sortOrder === 'asc' ? '↑价格(低→高)' : '↓价格(高→低)') : '按价格排序' }}
            </button> -->
          </div>
        </div>

        <!-- 卡片列表 -->
        <div class="space-y-4 max-h-[600px] overflow-y-auto">
          <div v-for="(item, index) in paginatedResults" :key="index" 
               class="bg-white border border-gray-200 rounded-lg p-4 shadow-sm hover:shadow-md transition-shadow">
            
            <!-- 卡片头部 -->
            <div class="flex justify-between items-center mb-3">
              <div class="flex items-center space-x-3">
                <span class="font-medium text-gray-900">物料編號: {{ formatValue(item.materialNumber) }}</span>
                <button
                  @click="copyToClipboard(item.materialNumber)"
                  class="px-2 py-1 text-xs bg-gray-100 text-gray-600 rounded hover:bg-gray-200 transition-colors focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-50"
                  title="點擊複製物料編號"
                >
                  複製
                </button>
              </div>
              <div class="text-right">
                <div class="text-sm text-gray-600">匹配度</div>
                <div class="text-2xl font-bold"
                     :class="{
                       'text-green-600': item.matchResult && item.matchResult.matchScore >= 80,
                       'text-yellow-600': item.matchResult && item.matchResult.matchScore >= 60 && item.matchResult.matchScore < 80,
                       'text-red-600': item.matchResult && item.matchResult.matchScore < 60,
                       'text-gray-400': !item.matchResult
                     }">
                  {{ item.matchResult ? item.matchResult.matchScore + '%' : '未设置' }}
                </div>
                <!-- 无价格产品提示 -->
                <div v-if="!item.price && item.matchResult && item.matchResult.matchScore === 0" 
                     class="text-xs text-orange-600 mt-1">
                  ⚠️ 未配置价格参数
                </div>
              </div>
            </div>

            <!-- 产品描述 -->
            <div class="mb-3 text-sm text-gray-700 whitespace-nowrap overflow-hidden">
              {{ getProductDescription(item) }}
            </div>

            <!-- 详细信息 -->
            <div class="space-y-1 text-sm">
              <div class="flex items-center">
                <span class="text-gray-500 mr-2 whitespace-nowrap">燙金紙型號:</span>
                <span class="text-gray-900">{{ formatValue(item.modelNumber) }}</span>
              </div>
              <div class="flex items-center">
                <span class="text-gray-500 mr-2 whitespace-nowrap">Leo Touch 編號:</span>
                <span class="text-gray-900 whitespace-nowrap">{{ formatValue(item.companyNumber) }}<span style="margin-left: 20px;" class="text-gray-500 mr-2 whitespace-nowrap"> GP NO:</span> {{ formatValue(item.gpNo) || '無' }}</span>
              </div>
              <div class="flex items-center">
                <span class="text-gray-500 mr-2 whitespace-nowrap">顏色:</span>
                <span class="text-gray-900">{{ formatValue(item.color) }}</span>
                <span style="margin-left: 20px;" class="text-gray-500 mr-2 whitespace-nowrap">價格:</span> <span class="text-gray-900">{{ formatValue(item.price) || '無' }}</span>
              </div>
              <div class="flex items-center justify-between">
                <div class="flex items-center">
                  <span class="text-gray-500 mr-2 whitespace-nowrap">燙金紙類型:</span>
                  <span class="text-gray-900">{{ formatValue(item.hotStampingPaperType) }}</span>
                  <span  style="margin-left: 20px;" class="text-gray-900">{{ formatValue(item.tightness)  }}</span>
                  <span  style="margin-left: 20px;" class="text-gray-900">{{ formatValue(item.performance)  }}</span>
                </div>
                <div class="flex space-x-2">
                  <button
                    @click="openFeedback(item)"
                    class="px-3 py-2 rounded text-sm bg-orange-500 text-white hover:bg-orange-600 transition-colors"
                    title="反馈物料问题"
                  >
                    反馈
                  </button>
                <button
                  @click="openDetails(item)"
                  class="px-4 py-2 rounded text-sm text-white hover:opacity-90 transition-colors"
                  :class="{
                    'bg-green-500 hover:bg-green-600': item.matchResult && item.matchResult.matchScore >= 80,
                    'bg-yellow-500 hover:bg-yellow-600': item.matchResult && item.matchResult.matchScore >= 60 && item.matchResult.matchScore < 80,
                    'bg-red-500 hover:bg-red-600': item.matchResult && item.matchResult.matchScore < 60,
                    'bg-gray-500 hover:bg-gray-600': !item.matchResult
                  }"
                >
                  詳情
                </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页器 -->
        <div class="mt-4 flex justify-between items-center" v-if="total > 0">
          <div class="text-sm text-gray-600">
            当前显示第 <span class="font-medium">{{ currentPage }}</span> 页
            ，共 <span class="font-medium">{{ Math.ceil(total / pageSize) }}</span> 页
            ，共 <span class="font-medium">{{ total }}</span> 条记录
          </div>
          <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            :page-sizes="[10, 15, 20, 30, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :pager-count="5"
            background
            @current-change="handlePageChange"
            @size-change="handleSizeChange"
          />
        </div>
      </div>

      <!-- 无匹配结果提示 -->
      <div v-else class="text-center text-amber-500 py-8 font-medium">
        <div class="text-lg mb-2">
          {{ getCurrentMatchStage > 0 ? `${allMatchResults.source}暫無結果` : '暫無匹配結果' }}
        </div>
        <div class="text-sm text-gray-500">
          {{ getCurrentMatchStage > 0 ? '請嘗試調整匹配參數或選擇其他參數組合' : '請選擇匹配參數或輸入公司編號進行搜尋' }}
        </div>
      </div>
    </div>

    <!-- 反馈对话框 -->
    <el-dialog v-model="feedbackDialogVisible" title="物料反馈" width="600px" destroy-on-close center>
      <div v-if="feedbackProduct" class="space-y-4">
        <!-- 反馈类型选择 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">反馈类型</label>
          <el-radio-group v-model="feedbackType" @change="updateFeedbackTemplate()">
            <el-radio value="mismatch">物料信息不匹配</el-radio>
            <el-radio value="invalid">物料已失效</el-radio>
            <el-radio value="rule_mismatch">与筛选规则不匹配</el-radio>
          </el-radio-group>
        </div>

        <!-- 物料信息 -->
        <div class="bg-gray-50 p-3 rounded">
          <div class="text-sm text-gray-600">
            <div><span class="font-medium">物料编号：</span>{{ formatValue(feedbackProduct.materialNumber) }}</div>
            <div><span class="font-medium">型号：</span>{{ formatValue(feedbackProduct.modelNumber) }}</div>
            <div><span class="font-medium">Leo Touch编号：</span>{{ formatValue(feedbackProduct.companyNumber) }}</div>
          </div>
        </div>

        <!-- 反馈内容 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">
            反馈内容 <span class="text-gray-500 text-xs">(已预填充模板，您可以修改)</span>
          </label>
          <el-input
            v-model="feedbackContent"
            type="textarea"
            :rows="12"
            placeholder="请填写反馈内容"
          />
        </div>
      </div>
      <template #footer>
        <div class="flex justify-end space-x-2">
          <el-button @click="closeFeedback" :disabled="submittingFeedback">取消</el-button>
          <el-button type="primary" @click="submitFeedback" :loading="submittingFeedback">
            提交反馈
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="dialogVisible" title="产品详情" width="50%" destroy-on-close center>
      <div v-if="currentProduct" class="space-y-4">

        <!-- 匹配度信息 -->
        <div v-if="currentProductMatchResult" class="bg-indigo-50 p-4 rounded-lg border border-indigo-200">
          <h4 class="font-medium text-lg mb-3 text-indigo-700 flex items-center">
            🎯 匹配度分析
            <span
              :class="[
                'ml-3 text-2xl font-bold',
                getMatchScoreColorClass(currentProductMatchResult.matchScore)
              ]"
            >
              {{ currentProductMatchResult.matchScore }}%
            </span>
            <span
              :class="[
                'ml-2 text-sm px-3 py-1 rounded-full',
                currentProductMatchResult.matchScore >= 80 ? 'bg-green-100 text-green-700' :
                currentProductMatchResult.matchScore >= 60 ? 'bg-yellow-100 text-yellow-700' :
                currentProductMatchResult.matchScore >= 40 ? 'bg-orange-100 text-orange-700' :
                'bg-red-100 text-red-700'
              ]"
            >
              {{ getMatchScoreLevel(currentProductMatchResult.matchScore) }}
            </span>
          </h4>
          <!-- 无价格产品提示 -->
          <div v-if="!currentProduct.price && currentProductMatchResult.matchScore === 0" 
               class="bg-orange-50 border border-orange-200 rounded-lg p-3 mb-3">
            <div class="flex items-center text-orange-700">
              <span class="text-lg mr-2">⚠️</span>
              <span class="text-sm">该产品没有配置价格参数，所以匹配度为0</span>
            </div>
          </div>
          <div class="grid grid-cols-2 gap-4 text-sm">
            <div>
              <span class="text-gray-600">匹配字段数：</span>
              <span class="font-medium">{{ currentProductMatchResult.matchedCount }}/{{ currentProductMatchResult.totalFields }}</span>
            </div>
            <div v-if="currentProductMatchResult.matchedFields.length > 0">
              <span class="text-gray-600">匹配字段：</span>
              <span class="font-medium text-green-600">{{ translateFieldNames(currentProductMatchResult.matchedFields).join(', ') }}</span>
            </div>
            <div v-if="currentProductMatchResult.unmatchedFields.length > 0" class="col-span-2">
              <span class="text-gray-600">未匹配字段：</span>
              <span class="font-medium text-red-600">{{ translateFieldNames(currentProductMatchResult.unmatchedFields).join(', ') }}</span>
            </div>
          </div>
        </div>
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
  </div>
</template>