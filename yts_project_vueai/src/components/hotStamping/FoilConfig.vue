<script setup lang="ts">
import { ref, onMounted, computed, onUnmounted, nextTick, watch } from 'vue';
import { ElSelect, ElOption, ElCascader, ElCascaderPanel, ElMessage, ElAlert, ElTooltip, ElIcon, ElInput } from 'element-plus';
import { QuestionFilled, Search, ArrowDown } from '@element-plus/icons-vue';
import { preProcessingStepsApi, type PreProcessingStepsOption } from '../../api/modules/preProcessingSteps';
import { productTypeOptionsApi, type ProductTypeOption } from '../../api/modules/productTypeOptions';
import { patternOptionsApi, type HotStampingPatternBase } from '../../api/modules/patternOptions';
import { hotStampingPatternAreaOptionsApi, type HotStampingPatternAreaOption } from '../../api/modules/hotStampingPatternAreaOptions';
import { compatibilityApi } from '../../api/modules/compatibility';
import { clothPaperTypeApi, type ClothPaperType } from '../../api/modules/clothPaperTypes';
import { laminatingApi, type LaminationMaterialOption, type PostProcessingOption } from '../../api/modules/laminating';
import { useHotStampingStore } from '../../stores/hotStamping';
import { useAuthStore } from '../../stores/auth';
import axios from 'axios';

// 查詢參數
const companyNumber = ref('');
const gpNumber = ref('');
const selectedProductType = ref('');
const selectedPatternType = ref('');
const selectedStampingType = ref('');

// 连接状态管理
const isOnline = ref(navigator.onLine);
const lastActivityTime = ref(Date.now());
// 已移除 connectionCheckInterval - 不再需要定期检测
const selectedPriceLevel = ref<number | null>(null);
// 燙後加工選項 - 多選
const selectedPostProcessing = ref<string[]>([]);

// 過膠相關選項
const selectedPostProcessingStep = ref('');
const selectedLaminationMaterial = ref('');
const selectedPostProcessingStepId = ref<number | null>(null);
const selectedLaminationMaterialId = ref<number | null>(null);

// 適用界面(前工序)參數
const selectedInterfaceOption = ref('');
const selectedApplicability = ref('');

// 平面燙金位置選擇參數
const selectedStampingPosition = ref('');
const showStampingPosition = ref(false);

// 級聯選擇器參數
const selectedStampingCascader = ref([]);
const selectedPostProcessingCascader = ref([]);
const selectedInterfaceCascader = ref([]);
const selectedMaterialCascader = ref([]);
const selectedStampingPatternCascader = ref([]);

// 材料適用界面參數
const selectedMaterialOption = ref('');
const selectedMaterialApplicability = ref('');

// 燙金圖案參數
const selectedStampingPatternOption = ref('');
const selectedStampingPatternApplicability = ref('');

// 综合筛选参数
const selectedPaizi = ref('');
const selectedColorNum = ref('');

// 匹配度规则类型：'price' 表示价格匹配度，'tag' 表示标签配置匹配度
// 注意：匹配度规则切换控件已移到 MatchingResult.vue 中，这里保留变量用于查询参数传递
const matchScoreRule = ref('price'); // 默认使用价格匹配度规则

// 状态
const loading = ref(false);
const error = ref<string | null>(null);
const searching = ref(false);
const searchResults = ref<any[]>([]);

// 计算属性：检查是否显示印刷警告
const showPrintingWarning = computed(() => {
  const hasPrinting = selectedPostProcessing.value.some((type: string) =>
    type === 'printing'
  );
  return hasPrinting && !selectedMaterialOption.value;
});

// 计算属性：检查是否显示絲印LED UV灑閃粉警告
const showLedUvGlitterWarning = computed(() => {
  const hasLedUvGlitter = selectedPostProcessing.value.some((type: string) =>
    type === 'ledUvGlitter'
  );
  return hasLedUvGlitter && !selectedMaterialOption.value;
});


// 前工序选项数据
const preProcessingStepsOptions = ref<PreProcessingStepsOption[]>([]);
const loadingPreProcessingSteps = ref(false);

// 产品类型选项数据
const productTypeOptions = ref<ProductTypeOption[]>([]);
const loadingProductType = ref(false);

// 图案选项数据
const patternOptions = ref<HotStampingPatternBase[]>([]);
const loadingPattern = ref(false);

// 燙金圖案（面积/类型）单级选项数据（来自 hot_stamping_patternx_area_options）
const patternAreaOptions = ref<HotStampingPatternAreaOption[]>([]);
const loadingPatternArea = ref(false);
const selectedPatternAreaOptionId = ref<string>('');

// 布料纸类型选项数据
const clothPaperTypes = ref<ClothPaperType[]>([]);
const loadingClothPaperTypes = ref(false);

// 过胶相关数据
const laminationMaterialOptions = ref<LaminationMaterialOption[]>([]);
const postProcessingOptions = ref<PostProcessingOption[]>([]);
const loadingLaminationOptions = ref(false);

// 使用 store
const hotStampingStore = useHotStampingStore();
const authStore = useAuthStore();

// 适用界面选项列表 - 从后端API获取
const interfaceOptions = ref<Array<{value: string, label: string}>>([]);

// 产品类型选项 - 与 NewTagMatching.vue 完全一致
// 产品类型选择已在上面定义

// 烫金图案类型选项 - 与 NewTagMatching.vue 完全一致
// 烫金图案类型选择已在上面定义

// 适用性选项
const applicabilityOptions = [
  { value: 'V', label: '适用' },
  { value: 'X', label: '不适用' }
];

// 材料适用性选项
const materialApplicabilityOptions = [
  { value: 'V', label: '适用' },
  { value: '▷', label: '需要作「絲印打底處理」再烫金' }
];

// 材料适用界面选项列表 - 来自 NewTagMatching.vue
const materialOptions = [
  { value: '單粉咭', label: '單粉咭' },
  { value: '雙粉咭', label: '雙粉咭' },
  { value: '單粉紙', label: '單粉紙' },
  { value: '啞粉紙', label: '啞粉紙' },
  { value: '無粉咭', label: '單粉紙' },
  { value: '書紙', label: '書紙' },
  { value: 'plasticFilmNormalVET', label: '膠片.普通VET/VVC/AVET' },
  { value: 'plasticFilmScratchResistantAVET', label: '膠片.防划AVET-防划面不可烫金' },
  { value: 'waxPaperGTF', label: '牛油紙.GTF' },
  { value: 'waxPaperZTF', label: '牛油紙.ZTF' },
  { value: 'fabricPaperNylonJHT8001', label: '布面紙.尼龍絲JHT-8001' },
  { value: 'fabricPaperNylonJHT8004', label: '布面紙.尼龍絲JHT-8004' },
  { value: 'fabricPaperNylonJHT8013', label: '布面紙.尼龍絲JHT-8013' },
  { value: 'fabricPaperPolyesterJHT8002', label: '布面紙.涤纶JHT-8002' },
  { value: 'fabricPaperPolyesterJHT8010', label: '布面紙.涤纶JHT-8010' },
  { value: 'fabricPaperPolyesterJHT8014', label: '布面紙.涤纶JHT-8014' },
  { value: 'fabricPaperPolyesterJHT8015', label: '布面紙.涤纶JHT-8015' },
  { value: 'fabricPaperPolyesterLTS8003', label: '布面紙.涤纶LTS-8003' },
  { value: 'fabricPaperPolyesterESM', label: '布面紙.涤纶ESM' },
  { value: 'fabricPaperImitationCottonJHT8003', label: '布面紙.仿棉JHT-8003' },
  { value: 'fabricPaperImitationCottonJHT8017', label: '布面紙.仿棉JHT-8017' },
  { value: 'fabricPaperPolyCottonJHT8006', label: '布面紙.涤棉JHT-8006' },
  { value: 'fabricPaperPolyCottonJHT8008', label: '布面紙.涤棉JHT-8008' },
  { value: 'fabricPaperPolyCottonJHT8011', label: '布面紙.涤棉JHT-8011' },
  { value: 'fabricPaperPolyCottonJHT8016', label: '布面紙.涤棉JHT-8016' },
  { value: 'fabricPaperPolyCottonJHT8018', label: '布面紙.涤棉JHT-8018' },
  { value: 'fabricPaperPolyCottonJHT8019', label: '布面紙.涤棉JHT-8019' },
  { value: 'fabricPaperPolyCottonJHT910', label: '布面紙.涤棉JHT-910' },
  { value: 'fabricPaperHempCottonJHT8007', label: '布面紙.麻棉JHT-8007' },
  { value: 'fabricPaperPureCottonJHT8009', label: '布面紙.纯棉JHT-8009' },
  { value: 'fabricPaperPureCottonJHT8012', label: '布面紙.纯棉JHT-8012' },
  { value: 'fabricPaperCottonJHT0001to0103', label: '布面紙.棉布JHT0001～0103' },
  { value: 'fabricPaperMercerizedCottonJHT0104to0195', label: '布面紙.絲光棉JHT0104～0195' },
  { value: 'fabricPaperMercerizedCottonJHT0199to0209', label: '布面紙.絲光棉JHT0199～0209' },
  { value: 'fabricPaperMercerizedCottonJHT0211', label: '布面紙.絲光棉JHT0211' },
  { value: 'fabricPaperMercerizedCottonJHT0213', label: '布面紙.絲光棉JHT0213' },
  { value: 'fabricPaperSilkJHT0196to0198', label: '布面紙.絹布JHT0196～0198' },
  { value: 'fabricPaperSilkJHT0265to0311', label: '布面紙.絹布JHT0265～0311' },
  { value: 'fabricPaperFlashFabricJHT0216', label: '布面紙.闪光布JHT0216' },
  { value: 'fabricPaperFlashFabricJHT0218', label: '布面紙.闪光布JHT0218' },
  { value: 'fabricPaperFlashFabricJHT0312to0330', label: '布面紙.闪光布JHT0312～0330' },
  { value: 'fabricPaperSilkFabricJHT0219to0264', label: '布面紙.絲綸布JHT0219～0264' },
  { value: 'fabricPaperHempPressFabricJHT0334to0351', label: '布面紙.麻压布JHT0334～0351' },
  { value: 'fabricPaperTexturedFabricJHT0352to0386', label: '布面紙.压紋布JHT0352～0386' },
  { value: 'fabricPaperMercerizedHempFabricJHT0387to0407', label: '布面紙.絲光麻布JHT0387～0407' },
  { value: 'fabricPaperCoarseFabricJHT0419to0429', label: '布面紙.粗布JHT0419～0429' }
];

// 烫金图案适用性选项
const stampingPatternApplicabilityOptions = [
  { value: 'V', label: '适用' },
  { value: 'NA_Enum', label: '不确定' }
];

// 烫金图案选项列表 - 来自 NewTagMatching.vue
const stampingPatternOptions = [
  { value: 'gradientAndDots', label: '漸變、網點' },
  { value: 'thinLinesAndLetters', label: '幼細線條、字母' },
  { value: 'mediumSmallAreaThinLinesAndLetters', label: '中小面積細線條、字母' },
  { value: 'mediumLargeAreaThinLinesAndLetters', label: '中大面積、細線條、字母' },
  { value: 'extraLargeArea', label: '大面積' }
];

// 烫金类型适用性选项
const stampingTypeApplicabilityOptions = [
  { value: 'V', label: '适用' },
  { value: 'X', label: '不适用' },
  { value: 'NA_Enum', label: '不确定' }
];

// 烫金类型选项列表 - 从API获取
const stampingTypeOptions = ref<{ value: string; label: string }[]>([]);
const stampingCascaderOptions = ref<any[]>([]);

// 级联选择器配置
const cascaderProps = {
  expandTrigger: 'hover' as const,
  emitPath: true,  // 保持多级路径
  checkStrictly: false,  // 必须选择到叶子节点
  multiple: false  // 单选模式
};

// 加载烫金类型选项
const loadHotStampingTypeOptions = async () => {
  try {
    console.log('开始加载烫金类型选项...');
    const response = await compatibilityApi.getAllHotStampingTypeGroups();
    console.log('API响应:', response);

    // 检查响应是否存在
    if (!response) {
      console.error('API响应为空');
      return;
    }

    // 检查响应数据是否存在（request.ts 的拦截器已经返回了 response.data）
    const groups = response;
    console.log('烫金类型分组数据:', groups);

    if (!Array.isArray(groups)) {
      console.error('分组数据不是数组:', groups);
      return;
    }

    if (groups.length === 0) {
      console.warn('没有找到烫金类型数据');
      return;
    }

    // 构建级联选择器数据
    const cascaderData: any[] = [];
    const simpleOptions: { value: string; label: string }[] = [];

    groups.forEach(group => {
      console.log(`处理烫金类型: ${group.stampingType}`, {
        isDefault: group.isDefault,
        options: group.options,
        optionsLength: group.options ? group.options.length : 0
      });

      if (group.isDefault || !group.options || group.options.length === 0) {
        // 单级选项：使用默认子选项的 id 作为值，确保后续能作为 hotStampingTypeOptionId 传递
        console.log(`  -> 单级选项: ${group.stampingType}`);
        const defaultOption = group.options && group.options.length > 0 ? group.options[0] : null;
        simpleOptions.push({
          value: defaultOption ? defaultOption.id : group.stampingType,
          label: group.stampingType
        });

        // 级联：没有子选项时，直接把 value 设为可用的选项 id（而不是文字），避免出现 NaN/null
        cascaderData.push({
          value: defaultOption ? defaultOption.id : group.stampingType,
          label: group.stampingType,
          children: []
        });
      } else {
        // 多级选项，构建子选项
        console.log(`  -> 多级选项: ${group.stampingType}，子选项数量: ${group.options.length}`);
        const children = group.options.map(option => ({
          value: option.id, // 使用ID作为value
          label: option.displayName // 后端已经处理了显示名称
        }));

        console.log(`  -> 子选项:`, children);
        console.log(`  -> 原始选项数据:`, group.options);

        cascaderData.push({
          value: group.stampingType,
          label: group.stampingType,
          children: children
        });

        // 也添加到简单选项列表（主选项）
        simpleOptions.push({
          value: group.stampingType,
          label: group.stampingType
        });
      }
    });

    stampingTypeOptions.value = simpleOptions;
    stampingCascaderOptions.value = cascaderData;

    console.log('烫金类型选项加载完成:', { simpleOptions, cascaderData });
  } catch (error) {
    console.error('加载烫金类型选项失败:', error);
  }
};

// 燙後加工级联选择器数据（响应式）
const postProcessingCascaderOptions = ref([
  {
    value: 'laminating',
    label: '过胶',
    children: [
      {
        value: 'embossingDebossing',
        label: '擊凹/凸',
        children: [
          { value: 'normalPreCoatedFilmGlossMatte', label: '普通预涂菲林-光/啞膠' },
          { value: 'normalPreCoatedFilmEconomicalMatte', label: '普通预涂菲林-平价耐磨啞膠' },
          { value: 'superTackPreCoatedFilmGlossMatte', label: '超粘预涂菲林-光/啞膠' },
          { value: 'superTackPreCoatedFilmHighWearMatte', label: '超粘预涂菲林-高價耐磨啞膠' },
          { value: 'waterBasedFilmGlossMatte', label: '水性菲林-光/啞膠' },
          { value: 'specialFilmPreCoatedSoftTouch', label: '特殊菲林-預塗柔感膠(含預塗、水性)' },
          { value: 'specialFilmAntiStaticGlossMatte', label: '特殊菲林-抗靜電光/啞膠(含預塗、水性)' }
        ]
      },
      {
        value: 'uvEmbossingDebossing',
        label: '絲印UV+擊凹/凸',
        children: [
          { value: 'normalPreCoatedFilmGlossMatte', label: '普通预涂菲林-光/啞膠' },
          { value: 'normalPreCoatedFilmEconomicalMatte', label: '普通预涂菲林-平价耐磨啞膠' },
          { value: 'superTackPreCoatedFilmGlossMatte', label: '超粘预涂菲林-光/啞膠' },
          { value: 'superTackPreCoatedFilmHighWearMatte', label: '超粘预涂菲林-高價耐磨啞膠' },
          { value: 'waterBasedFilmGlossMatte', label: '水性菲林-光/啞膠' },
          { value: 'specialFilmPreCoatedSoftTouch', label: '特殊菲林-預塗柔感膠(含預塗、水性)' },
          { value: 'specialFilmAntiStaticGlossMatte', label: '特殊菲林-抗靜電光/啞膠(含預塗、水性)' }
        ]
      },
      {
        value: 'dieCutting',
        label: '啤折/切',
        children: [
          { value: 'normalPreCoatedFilmGlossMatte', label: '普通预涂菲林-光/啞膠' },
          { value: 'normalPreCoatedFilmEconomicalMatte', label: '普通预涂菲林-平价耐磨啞膠' },
          { value: 'superTackPreCoatedFilmGlossMatte', label: '超粘预涂菲林-光/啞膠' },
          { value: 'superTackPreCoatedFilmHighWearMatte', label: '超粘预涂菲林-高價耐磨啞膠' },
          { value: 'waterBasedFilmGlossMatte', label: '水性菲林-光/啞膠' },
          { value: 'specialFilmPreCoatedSoftTouch', label: '特殊菲林-預塗柔感膠(含預塗、水性)' },
          { value: 'specialFilmAntiStaticGlossMatte', label: '特殊菲林-抗靜電光/啞膠(含預塗、水性)' }
        ]
      },
      {
        value: 'uvDieCutting',
        label: '絲印UV+啤折/切',
        children: [
          { value: 'normalPreCoatedFilmGlossMatte', label: '普通预涂菲林-光/啞膠' },
          { value: 'normalPreCoatedFilmEconomicalMatte', label: '普通预涂菲林-平价耐磨啞膠' },
          { value: 'superTackPreCoatedFilmGlossMatte', label: '超粘预涂菲林-光/啞膠' },
          { value: 'superTackPreCoatedFilmHighWearMatte', label: '超粘预涂菲林-高價耐磨啞膠' },
          { value: 'waterBasedFilmGlossMatte', label: '水性菲林-光/啞膠' },
          { value: 'specialFilmPreCoatedSoftTouch', label: '特殊菲林-預塗柔感膠(含預塗、水性)' },
          { value: 'specialFilmAntiStaticGlossMatte', label: '特殊菲林-抗靜電光/啞膠(含預塗、水性)' }
        ]
      },
      {
        value: 'stepping',
        label: '踩坑位',
        children: [
          { value: 'normalPreCoatedFilmGlossMatte', label: '普通预涂菲林-光/啞膠' },
          { value: 'normalPreCoatedFilmEconomicalMatte', label: '普通预涂菲林-平价耐磨啞膠' },
          { value: 'superTackPreCoatedFilmGlossMatte', label: '超粘预涂菲林-光/啞膠' },
          { value: 'superTackPreCoatedFilmHighWearMatte', label: '超粘预涂菲林-高價耐磨啞膠' },
          { value: 'waterBasedFilmGlossMatte', label: '水性菲林-光/啞膠' },
          { value: 'specialFilmPreCoatedSoftTouch', label: '特殊菲林-預塗柔感膠(含預塗、水性)' },
          { value: 'specialFilmAntiStaticGlossMatte', label: '特殊菲林-抗靜電光/啞膠(含預塗、水性)' }
        ]
      },
      {
        value: 'uvStepping',
        label: '絲印UV+踩坑位',
        children: [
          { value: 'normalPreCoatedFilmGlossMatte', label: '普通预涂菲林-光/啞膠' },
          { value: 'normalPreCoatedFilmEconomicalMatte', label: '普通预涂菲林-平价耐磨啞膠' },
          { value: 'superTackPreCoatedFilmGlossMatte', label: '超粘预涂菲林-光/啞膠' },
          { value: 'superTackPreCoatedFilmHighWearMatte', label: '超粘预涂菲林-高價耐磨啞膠' },
          { value: 'waterBasedFilmGlossMatte', label: '水性菲林-光/啞膠' },
          { value: 'specialFilmPreCoatedSoftTouch', label: '特殊菲林-預塗柔感膠(含預塗、水性)' },
          { value: 'specialFilmAntiStaticGlossMatte', label: '特殊菲林-抗靜電光/啞膠(含預塗、水性)' }
        ]
      },
      {
        value: 'texturing',
        label: '壓紋',
        children: [
          { value: 'normalPreCoatedFilmGlossMatte', label: '普通预涂菲林-光/啞膠' },
          { value: 'normalPreCoatedFilmEconomicalMatte', label: '普通预涂菲林-平价耐磨啞膠' },
          { value: 'superTackPreCoatedFilmGlossMatte', label: '超粘预涂菲林-光/啞膠' },
          { value: 'superTackPreCoatedFilmHighWearMatte', label: '超粘预涂菲林-高價耐磨啞膠' },
          { value: 'waterBasedFilmGlossMatte', label: '水性菲林-光/啞膠' },
          { value: 'specialFilmPreCoatedSoftTouch', label: '特殊菲林-預塗柔感膠(含預塗、水性)' },
          { value: 'specialFilmAntiStaticGlossMatte', label: '特殊菲林-抗靜電光/啞膠(含預塗、水性)' }
        ]
      }
    ]
  },
  {
    value: 'ledUvGlitter',
    label: '絲印LED UV灑閃粉',
    children: []
  },
  {
    value: 'uvPrinting',
    label: '印刷UV',
    children: []
  },
  {
    value: 'printing',
    label: '印刷',
    children: []
  },
  {
    value: 'embossing',
    label: '击凸',
    children: []
  }
]);

// 適用界面级联选择器数据
const interfaceCascaderOptions = [
  {
    value: 'coating',
    label: '塗布',
    children: [
      { value: 'uvCoating', label: 'UV塗布' },
      { value: 'waterBasedCoating', label: '水性塗布' },
      { value: 'oilBasedCoating', label: '油性塗布' }
    ]
  },
  {
    value: 'printing',
    label: '印刷',
    children: [
      { value: 'offsetPrinting', label: '膠印' },
      { value: 'digitalPrinting', label: '數碼印刷' },
      { value: 'screenPrinting', label: '絲印' }
    ]
  },
  {
    value: 'laminating',
    label: '過膠',
    children: [
      { value: 'glossLaminating', label: '光膠' },
      { value: 'matteLaminating', label: '啞膠' },
      { value: 'softTouchLaminating', label: '柔感膠' }
    ]
  }
];

// 材料適用界面级联选择器数据 - 动态从API获取
const materialCascaderOptions = ref<any[]>([]);
// 材料搜索关键词（用于保持层级结构的搜索）
const materialSearchKeyword = ref<string>('');
// 是否显示材料选择器的下拉面板
const showMaterialDropdown = ref(false);
// 材料选择器输入框的显示值
const materialInputDisplayValue = ref<string>('');
// 材料 cascader 的 ref，用于控制下拉框的打开
const materialCascaderRef = ref<any>(null);

// 烫金类型搜索关键词（用于保持层级结构的搜索）
const stampingSearchKeyword = ref<string>('');
// 是否显示烫金类型选择器的下拉面板
const showStampingDropdown = ref(false);
// 烫金类型选择器输入框的显示值
const stampingInputDisplayValue = ref<string>('');
// 烫金类型 cascader 的 ref，用于控制下拉框的打开
const stampingCascaderRef = ref<any>(null);

// 燙後加工選項搜索关键词（用于保持层级结构的搜索）
const postProcessingSearchKeyword = ref<string>('');
// 是否显示燙後加工選項选择器的下拉面板
const showPostProcessingDropdown = ref(false);
// 燙後加工選項选择器输入框的显示值
const postProcessingInputDisplayValue = ref<string>('');
// 燙後加工選項 cascader 的 ref，用于控制下拉框的打开
const postProcessingCascaderRef = ref<any>(null);

// 处理材料输入框聚焦事件
const handleMaterialInputFocus = () => {
  // 如果有选中值，聚焦时清空显示值，显示搜索关键词
  if (materialInputDisplayValue.value) {
    materialInputDisplayValue.value = '';
  }
  showMaterialDropdown.value = true;
};

// 处理材料输入框点击事件
const handleMaterialInputClick = () => {
  showMaterialDropdown.value = true;
};

// 打开 cascader 下拉面板（使用 CascaderPanel 时不需要手动打开，它直接显示）
const openCascaderDropdown = () => {
  // CascaderPanel 不需要手动打开，它直接显示面板
};

// 处理材料输入框输入事件
const handleMaterialInputInput = (value: string) => {
  // 如果有选中值，输入时清空选中值
  if (materialInputDisplayValue.value) {
    selectedMaterialCascader.value = [];
    materialInputDisplayValue.value = '';
  }
  materialSearchKeyword.value = value;
  // 输入时自动打开下拉面板
  if (!showMaterialDropdown.value) {
    showMaterialDropdown.value = true;
  }
};

// 更新材料输入框的显示值
const updateMaterialInputDisplayValue = () => {
  if (!selectedMaterialCascader.value || selectedMaterialCascader.value.length === 0) {
    materialInputDisplayValue.value = '';
    return;
  }

  const path = selectedMaterialCascader.value;

  // 兼容两种结构：
  // 1) [typeId] 或 [typeId, status]
  // 2) ['fabricPaperGroup', typeId] 或 ['fabricPaperGroup', typeId, status]
  let typeId: string | undefined;
  let statusValue: string | undefined;
  let typeOption: any | undefined;

  if (path[0] === 'fabricPaperGroup') {
    typeId = path[1];
    statusValue = path[2];
    const fabricRoot = materialCascaderOptions.value.find((opt: any) => opt.value === 'fabricPaperGroup');
    typeOption = fabricRoot?.children?.find((child: any) => child.value === typeId);
  } else {
    typeId = path[0];
    statusValue = path[1];
    typeOption = materialCascaderOptions.value.find((opt: any) => opt.value === typeId);
  }

  if (!typeOption) {
    materialInputDisplayValue.value = '';
    return;
  }

  // 只有一级（没有子节点或配置为单状态叶子节点，或者未显式选择状态）
  if (!typeOption.children || typeOption.children.length <= 1 || !statusValue) {
    materialInputDisplayValue.value = typeOption.label;
    return;
  }

  // 有二级状态的情况
  const statusOption = typeOption.children.find((child: any) => child.value === statusValue);
  if (statusOption) {
    materialInputDisplayValue.value = `${typeOption.label} / ${statusOption.label}`;
    return;
  }

  materialInputDisplayValue.value = typeOption.label;
};

// 处理材料输入框清除事件
const handleMaterialInputClear = () => {
  materialSearchKeyword.value = '';
  selectedMaterialCascader.value = [];
  materialInputDisplayValue.value = '';
  handleMaterialCascaderClear();
};

// 处理烫金类型输入框聚焦事件
const handleStampingInputFocus = () => {
  // 如果有选中值，聚焦时清空显示值，显示搜索关键词
  if (stampingInputDisplayValue.value) {
    stampingInputDisplayValue.value = '';
  }
  showStampingDropdown.value = true;
};

// 处理烫金类型输入框点击事件
const handleStampingInputClick = () => {
  showStampingDropdown.value = true;
};

// 处理烫金类型输入框输入事件
const handleStampingInputInput = (value: string) => {
  // 如果有选中值，输入时清空选中值
  if (stampingInputDisplayValue.value) {
    selectedStampingCascader.value = [];
    stampingInputDisplayValue.value = '';
  }
  stampingSearchKeyword.value = value;
  // 输入时自动打开下拉面板
  if (!showStampingDropdown.value) {
    showStampingDropdown.value = true;
  }
};

// 更新烫金类型输入框的显示值
const updateStampingInputDisplayValue = () => {
  if (selectedStampingCascader.value && selectedStampingCascader.value.length > 0) {
    const [parentValue, childValue] = selectedStampingCascader.value;
    const parentOption = stampingCascaderOptions.value.find((opt: any) => opt.value === parentValue);
    if (parentOption) {
      if (childValue) {
        const childOption = parentOption.children?.find((child: any) => child.value === childValue);
        if (childOption) {
          stampingInputDisplayValue.value = `${parentOption.label} / ${childOption.label}`;
          return;
        }
      } else {
        // 如果没有子选项，只显示父选项
        stampingInputDisplayValue.value = parentOption.label;
        return;
      }
    }
  }
  stampingInputDisplayValue.value = '';
};

// 处理烫金类型输入框清除事件
const handleStampingInputClear = () => {
  stampingSearchKeyword.value = '';
  selectedStampingCascader.value = [];
  stampingInputDisplayValue.value = '';
  handleStampingCascaderClear();
};

// 处理燙後加工選項输入框聚焦事件
const handlePostProcessingInputFocus = () => {
  // 如果有选中值，聚焦时清空显示值，显示搜索关键词
  if (postProcessingInputDisplayValue.value) {
    postProcessingInputDisplayValue.value = '';
  }
  showPostProcessingDropdown.value = true;
};

// 处理燙後加工選項输入框点击事件
const handlePostProcessingInputClick = () => {
  showPostProcessingDropdown.value = true;
};

// 处理燙後加工選項输入框输入事件
const handlePostProcessingInputInput = (value: string) => {
  // 如果有选中值，输入时清空选中值
  if (postProcessingInputDisplayValue.value) {
    selectedPostProcessingCascader.value = [];
    postProcessingInputDisplayValue.value = '';
  }
  postProcessingSearchKeyword.value = value;
  // 输入时自动打开下拉面板
  if (!showPostProcessingDropdown.value) {
    showPostProcessingDropdown.value = true;
  }
};

// 更新燙後加工選項输入框的显示值
const updatePostProcessingInputDisplayValue = () => {
  if (selectedPostProcessingCascader.value && selectedPostProcessingCascader.value.length > 0) {
    // 构建显示路径
    const path = selectedPostProcessingCascader.value;
    const labels: string[] = [];
    
    // 第一级 - 主加工类型
    if (path.length >= 1) {
      const firstOption = postProcessingCascaderOptions.value.find((opt: any) => opt.value === path[0]);
      if (firstOption) {
        labels.push(firstOption.label);
        
        // 第二级 - 后加工步骤
        if (path.length >= 2 && firstOption.children) {
          const secondOption = firstOption.children.find((child: any) => child.value === path[1]);
          if (secondOption) {
            labels.push(secondOption.label);
            
            // 第三级 - 过胶材质
            if (path.length >= 3 && secondOption.children) {
              const thirdOption = secondOption.children.find((child: any) => child.value === path[2]);
              if (thirdOption) {
                labels.push(thirdOption.label);
              }
            }
          }
        }
      }
    }
    
    postProcessingInputDisplayValue.value = labels.length > 0 ? labels.join(' / ') : '';
  } else {
    postProcessingInputDisplayValue.value = '';
  }
};

// 处理燙後加工選項输入框清除事件
const handlePostProcessingInputClear = () => {
  postProcessingSearchKeyword.value = '';
  selectedPostProcessingCascader.value = [];
  postProcessingInputDisplayValue.value = '';
  handlePostProcessingCascaderClear();
};

// 旧的图案级联已废弃，改为单级下拉，数据由后端提供

// 价格优先度选项列表
const priceLevelOptions = [
  { value: 1, label: '1' },
  { value: 2, label: '2' },
  { value: 3, label: '3' },
  { value: 4, label: '4' },
  { value: 5, label: '5' },
  { value: 6, label: '6' },
  { value: 7, label: '7' },
  { value: 8, label: '8' },
  { value: 9, label: '9' },
  { value: 10, label: '10' }
];

// 燙後加工选项列表（静态选项）
const postProcessingStaticOptions = [
  { value: 'laminating', label: '过胶' },
  { value: 'ledUvGlitter', label: '絲印LED UV灑閃粉' },
  { value: 'uvPrinting', label: '印刷UV' },
  { value: 'printing', label: '印刷' },
  { value: 'embossing', label: '击凸' }
];

// 后加工步骤选项
const postProcessingStepOptions = [
  { value: 'embossingDebossing', label: '擊凹/凸' },
  { value: 'uvEmbossingDebossing', label: '絲印UV+擊凹/凸' },
  { value: 'dieCutting', label: '啤折/切' },
  { value: 'uvDieCutting', label: '絲印UV+啤折/切' },
  { value: 'stepping', label: '踩坑位' },
  { value: 'uvStepping', label: '絲印UV+踩坑位' },
  { value: 'texturing', label: '壓紋' }
];

// 过胶材质选项（静态选项）
const laminationMaterialStaticOptions = [
  { value: 'normalPreCoatedFilmGlossMatte', label: '普通预涂菲林-光/啞膠' },
  { value: 'normalPreCoatedFilmEconomicalMatte', label: '普通预涂菲林-平价耐磨啞膠' },
  { value: 'superTackPreCoatedFilmGlossMatte', label: '超粘预涂菲林-光/啞膠' },
  { value: 'superTackPreCoatedFilmHighWearMatte', label: '超粘预涂菲林-高價耐磨啞膠' },
  { value: 'waterBasedFilmGlossMatte', label: '水性菲林-光/啞膠' },
  { value: 'specialFilmPreCoatedSoftTouch', label: '特殊菲林-預塗柔感膠(含預塗、水性)' },
  { value: 'specialFilmAntiStaticGlossMatte', label: '特殊菲林-抗靜電光/啞膠(含預塗、水性)' }
];

// 处理函数 - 按照 NewTagMatching.vue 的方式实现
const handleProductTypeChange = async () => {
  console.log('产品类型选择:', selectedProductType.value);

  if (!selectedProductType.value) {
    // 清空图案选项并隐藏区块
    patternOptions.value = [];
    selectedPatternType.value = '';
    return;
  }

  // 根据选择的产品类型名称找到对应的ID
  const selectedProductTypeOption = productTypeOptions.value.find(
      option => option.productName === selectedProductType.value
  );

  if (selectedProductTypeOption) {
    console.log('选择的产品类型ID:', selectedProductTypeOption.id);

    // 切换产品类型时，先清空当前图案选择，避免残留
    selectedPatternType.value = '';
    patternOptions.value = [];

    // 并行触发查询和图案选项加载
    await Promise.all([
      searchWithProductType(selectedProductTypeOption.id),
      loadPatternOptionsByProductTypeId(selectedProductTypeOption.id)
    ]);
  }
};

const handlePatternTypeChange = async () => {
  console.log('烫金图案类型选择:', selectedPatternType.value);

  if (!selectedPatternType.value) {
    return;
  }

  // 根据选择的图案名称找到对应的ID
  const selectedPatternOption = patternOptions.value.find(
      option => option.optionName === selectedPatternType.value
  );

  if (selectedPatternOption) {
    console.log('选择的图案ID:', selectedPatternOption.id);

    // 触发基于图案的查询
    await searchWithPatternType(selectedPatternOption.id);
  }
};

const handleStampingTypeChange = () => {
  console.log('烫金类型选择:', selectedStampingType.value);
  // 如果选择了平面燙金，显示位置选择
  showStampingPosition.value = selectedStampingType.value === '平面燙金';
};

// 级联选择器处理函数
const handleStampingCascaderChange = async (value: any) => {
  console.log('级联选择器选择:', value);
  if (value && value.length > 0) {
    selectedStampingType.value = value[0];
    if (value.length > 1) {
      selectedStampingPosition.value = value[1];
      // 使用烫金类型选项ID进行查询
      await searchWithHotStampingTypeOption(parseInt(value[1]));
    } else {
      selectedStampingPosition.value = '';
      // 如果没有子选项，使用主选项进行查询
      await searchWithHotStampingTypeOption(parseInt(value[0]));
    }
    
    // 更新显示值
    updateStampingInputDisplayValue();
    // 关闭下拉面板
    showStampingDropdown.value = false;
    // 清空搜索关键词
    stampingSearchKeyword.value = '';
  } else {
    selectedStampingType.value = '';
    selectedStampingPosition.value = '';
  }
};

// 烫金类型级联选择器清除处理函数
const handleStampingCascaderClear = async () => {
  console.log('烫金类型级联选择器被清除');
  await clearStampingType();
};

// 燙後加工级联选择器处理函数
const handlePostProcessingCascaderChange = async (value: any) => {
  console.log('燙後加工级联选择器选择:', value);
  console.log('当前过胶相关数据状态:', {
    postProcessingOptionsCount: postProcessingOptions.value.length,
    laminationMaterialOptionsCount: laminationMaterialOptions.value.length,
    postProcessingOptions: postProcessingOptions.value,
    laminationMaterialOptions: laminationMaterialOptions.value
  });
  // 处理单选结果
  if (value && value.length > 0) {
    // 获取选择的加工类型（第一个元素）
    const processingType = value[0];
    selectedPostProcessing.value = [processingType];

    // 检查选择的加工类型
    const hasPrinting = processingType === 'printing';
    const hasUvPrinting = processingType === 'uvPrinting';
    const hasLedUvGlitter = processingType === 'ledUvGlitter';
    const hasEmbossing = processingType === 'embossing';
    const hasLaminating = processingType === 'laminating';

    console.log('调试信息:', {
      originalValue: value,
      processingType,
      hasPrinting,
      hasUvPrinting,
      hasLedUvGlitter,
      hasLaminating,
      selectedMaterialOption: selectedMaterialOption.value,
      companyNumber: companyNumber.value
    });

    // 如果选择了印刷但没有选择适用界面，显示提示
    if (hasPrinting && !selectedMaterialOption.value) {
      console.log('显示印刷警告提示');
      ElMessage.warning('选择印刷选项时，请先选择 7. 適用界面');
    } else if (hasPrinting && selectedMaterialOption.value) {
      // 如果选择了印刷且已选择适用界面，自动触发搜索
      console.log('印刷选项已选择，自动触发搜索');
      const queryParams = buildQueryParams();
      await executeQuery(queryParams, 'postProcessing');
    }

    // 如果选择了印刷UV，自动触发搜索
    if (hasUvPrinting) {
      console.log('印刷UV选项已选择，自动触发搜索');
      const queryParams = buildQueryParams();
      await executeQuery(queryParams, 'postProcessing');
    }

    // 如果选择了击凸，自动触发搜索
    if (hasEmbossing) {
      console.log('击凸选项已选择，自动触发搜索');
      const queryParams = buildQueryParams();
      await executeQuery(queryParams, 'postProcessing');
    }

    // 如果选择了絲印LED UV灑閃粉但没有选择适用界面，显示提示
    if (hasLedUvGlitter && !selectedMaterialOption.value) {
      console.log('显示絲印LED UV灑閃粉警告提示');
      ElMessage.warning('选择絲印LED UV灑閃粉选项时，请先选择 7. 適用界面');
    } else if (hasLedUvGlitter && selectedMaterialOption.value) {
      // 如果选择了絲印LED UV灑閃粉且已选择适用界面，自动触发搜索
      console.log('絲印LED UV灑閃粉选项已选择，自动触发搜索');
      const queryParams = buildQueryParams();
      await executeQuery(queryParams, 'postProcessing');
    }

    // 过胶选项的查询将在处理完子选项后触发

    // 处理过胶相关的子选项
    console.log('开始处理过胶选项，原始value:', value);
    
    // 如果选择了过胶，处理子选项
    if (hasLaminating && value.length >= 2) {
      // 提取后加工步骤ID（第二级）
      selectedPostProcessingStepId.value = value[1];
      // 根据ID找到对应的步骤名称
      const step = postProcessingOptions.value.find(option => option.id === value[1]);
      if (step) {
        selectedPostProcessingStep.value = step.stepName;
      }

      // 提取过胶材质ID（第三级）
      if (value.length >= 3) {
        selectedLaminationMaterialId.value = value[2];
        // 根据ID找到对应的材质名称
        const material = laminationMaterialOptions.value.find(option => option.id === value[2]);
        if (material) {
          selectedLaminationMaterial.value = material.materialName;
        }
      }

      console.log('过胶选项处理结果:', {
        stepId: selectedPostProcessingStepId.value,
        stepName: selectedPostProcessingStep.value,
        materialId: selectedLaminationMaterialId.value,
        materialName: selectedLaminationMaterial.value
      });

      // 如果选择了具体的步骤和材质，触发过胶兼容性查询
      if (selectedPostProcessingStepId.value && selectedLaminationMaterialId.value) {
        console.log('过胶步骤和材质已选择，触发兼容性查询');
        const queryParams = buildQueryParams();
        await executeQuery(queryParams, 'laminating');
      }
    }
    
    // 更新显示值
    updatePostProcessingInputDisplayValue();
    // 关闭下拉面板
    showPostProcessingDropdown.value = false;
    // 清空搜索关键词
    postProcessingSearchKeyword.value = '';
  } else {
    selectedPostProcessing.value = [];
    selectedPostProcessingStep.value = '';
    selectedLaminationMaterial.value = '';
    selectedPostProcessingStepId.value = null;
    selectedLaminationMaterialId.value = null;
  }
};

// 燙後加工级联选择器清除处理函数
const handlePostProcessingCascaderClear = async () => {
  console.log('燙後加工级联选择器被清除');
  await clearPostProcessing();
};

const handleStampingPositionChange = () => {
  console.log('平面燙金位置选择:', selectedStampingPosition.value);
};

// 材料適用界面级联选择器处理函数
const handleMaterialCascaderChange = async (value: any) => {
  console.log('材料適用界面级联选择器选择:', value);
  if (value && value.length > 0) {
    selectedMaterialOption.value = value[0];
    if (value.length > 1) {
      selectedMaterialApplicability.value = value[1];
    } else {
      selectedMaterialApplicability.value = '';
    }

    // 更新显示值
    updateMaterialInputDisplayValue();
    // 关闭下拉面板
    showMaterialDropdown.value = false;
    // 清空搜索关键词
    materialSearchKeyword.value = '';

    // 触发布料纸类型查询，传递兼容性状态
    await searchWithClothPaperType(parseInt(value[0]), value[1] || 'V');

    // 如果已经选择了印刷选项，重新触发搜索以应用印刷匹配规则
    const hasPrinting = selectedPostProcessing.value.some((type: string) =>
        type === 'printing'
    );
    if (hasPrinting) {
      console.log('适用界面已选择，重新触发印刷匹配搜索');
      const queryParams = buildQueryParams();
      await executeQuery(queryParams, 'postProcessing');
    }

    // 如果已经选择了絲印LED UV灑閃粉选项，重新触发搜索以应用絲印LED UV灑閃粉匹配规则
    const hasLedUvGlitter = selectedPostProcessing.value.some((type: string) =>
        type === 'ledUvGlitter'
    );
    if (hasLedUvGlitter) {
      console.log('适用界面已选择，重新触发絲印LED UV灑閃粉匹配搜索');
      const queryParams = buildQueryParams();
      await executeQuery(queryParams, 'postProcessing');
    }

    // 如果已经选择了印刷UV选项，重新触发搜索以应用印刷UV匹配规则
    const hasUvPrinting = selectedPostProcessing.value.some((type: string) =>
        type === 'uvPrinting'
    );
    if (hasUvPrinting) {
      console.log('适用界面已选择，重新触发印刷UV匹配搜索');
      const queryParams = buildQueryParams();
      await executeQuery(queryParams, 'postProcessing');
    }
  } else {
    selectedMaterialOption.value = '';
    selectedMaterialApplicability.value = '';
    // 清空结果
    searchResults.value = [];
    hotStampingStore.setSearchResults([]);
    // 通知父组件结果已清空
    emit('searchResults', { results: [], queryType: 'clear', queryParams: {} });
  }
};

// 材料適用界面级联选择器清除处理函数
const handleMaterialCascaderClear = async () => {
  console.log('材料適用界面级联选择器被清除');
  materialInputDisplayValue.value = '';
  materialSearchKeyword.value = '';
  await clearMaterialOption();
};


// 材料適用界面级联选择器过滤函数 - 支持相似性搜索并保持多级结构
const filterMaterialOptions = (node: any, keyword: string): boolean => {
  // 如果没有关键词，显示所有选项
  if (!keyword || keyword.trim() === '') {
    return true;
  }
  
  // 将关键词转换为小写以便不区分大小写搜索
  const lowerKeyword = keyword.toLowerCase().trim();
  
  // 检查当前节点的 label 是否包含关键词（支持模糊匹配）
  // label格式为 "category.typeName"，如 "絲光棉.JHT0213"
  let nodeMatches = false;
  if (node.label) {
    const lowerLabel = String(node.label).toLowerCase();
    if (lowerLabel.includes(lowerKeyword)) {
      nodeMatches = true;
    }
  }
  
  // 检查 value 字段（如果是字符串类型，可能包含关键词）
  if (!nodeMatches && node.value !== undefined && node.value !== null) {
    const valueStr = String(node.value).toLowerCase();
    if (valueStr.includes(lowerKeyword)) {
      nodeMatches = true;
    }
  }
  
  // 如果有子节点，递归检查子节点是否匹配
  let hasMatchingChild = false;
  if (node.children && Array.isArray(node.children) && node.children.length > 0) {
    // 递归检查所有子节点
    for (const child of node.children) {
      if (filterMaterialOptions(child, keyword)) {
        hasMatchingChild = true;
        break; // 找到匹配的子节点即可
      }
    }
  }
  
  // 如果当前节点匹配或子节点匹配，都返回true
  // 这样Element Plus会显示父节点，并且可以展开显示子节点（保持层级结构）
  return nodeMatches || hasMatchingChild;
};

// 根据搜索关键词过滤材料选项（保持层级结构）
// 不使用 filterable，而是手动过滤以保持左右两列的层级显示
const filteredMaterialCascaderOptions = computed(() => {
  if (!materialSearchKeyword.value || materialSearchKeyword.value.trim() === '') {
    return materialCascaderOptions.value;
  }
  
  const keyword = materialSearchKeyword.value.toLowerCase().trim();
  
  // 递归过滤选项，保持层级结构（深拷贝避免修改原数组）
  const filterOptions = (options: any[]): any[] => {
    return options.map(option => {
      // 检查当前节点是否匹配
      const nodeMatches = option.label && 
        String(option.label).toLowerCase().includes(keyword);
      
      // 检查子节点是否匹配
      let filteredChildren: any[] = [];
      if (option.children && Array.isArray(option.children) && option.children.length > 0) {
        filteredChildren = filterOptions(option.children);
      }
      const hasMatchingChild = filteredChildren.length > 0;
      
      // 如果当前节点匹配或子节点匹配，保留该节点
      if (nodeMatches || hasMatchingChild) {
        return {
          ...option,
          children: hasMatchingChild ? filteredChildren : option.children
        };
      }
      
      return null;
    }).filter((item): item is any => item !== null);
  };
  
  return filterOptions(materialCascaderOptions.value);
});

// 根据搜索关键词过滤烫金类型选项（保持层级结构）
const filteredStampingCascaderOptions = computed(() => {
  if (!stampingSearchKeyword.value || stampingSearchKeyword.value.trim() === '') {
    return stampingCascaderOptions.value;
  }
  
  const keyword = stampingSearchKeyword.value.toLowerCase().trim();
  
  // 递归过滤选项，保持层级结构（深拷贝避免修改原数组）
  const filterOptions = (options: any[]): any[] => {
    return options.map(option => {
      // 检查当前节点是否匹配
      const nodeMatches = option.label && 
        String(option.label).toLowerCase().includes(keyword);
      
      // 检查子节点是否匹配
      let filteredChildren: any[] = [];
      if (option.children && Array.isArray(option.children) && option.children.length > 0) {
        filteredChildren = filterOptions(option.children);
      }
      const hasMatchingChild = filteredChildren.length > 0;
      
      // 如果当前节点匹配或子节点匹配，保留该节点
      if (nodeMatches || hasMatchingChild) {
        return {
          ...option,
          children: hasMatchingChild ? filteredChildren : option.children
        };
      }
      
      return null;
    }).filter((item): item is any => item !== null);
  };
  
  return filterOptions(stampingCascaderOptions.value);
});

// 根据搜索关键词过滤燙後加工選項（保持层级结构）
const filteredPostProcessingCascaderOptions = computed(() => {
  if (!postProcessingSearchKeyword.value || postProcessingSearchKeyword.value.trim() === '') {
    return postProcessingCascaderOptions.value;
  }
  
  const keyword = postProcessingSearchKeyword.value.toLowerCase().trim();
  
  // 递归过滤选项，保持层级结构（深拷贝避免修改原数组）
  const filterOptions = (options: any[]): any[] => {
    return options.map(option => {
      // 检查当前节点是否匹配
      const nodeMatches = option.label && 
        String(option.label).toLowerCase().includes(keyword);
      
      // 检查子节点是否匹配
      let filteredChildren: any[] = [];
      if (option.children && Array.isArray(option.children) && option.children.length > 0) {
        filteredChildren = filterOptions(option.children);
      }
      const hasMatchingChild = filteredChildren.length > 0;
      
      // 如果当前节点匹配或子节点匹配，保留该节点
      if (nodeMatches || hasMatchingChild) {
        return {
          ...option,
          children: hasMatchingChild ? filteredChildren : option.children
        };
      }
      
      return null;
    }).filter((item): item is any => item !== null);
  };
  
  return filterOptions(postProcessingCascaderOptions.value);
});

// 燙金圖案（面积/类型）单选处理函数
const handlePatternAreaOptionChange = async () => {
  if (!selectedPatternAreaOptionId.value) return;
  await searchWithPatternAreaOption(parseInt(selectedPatternAreaOptionId.value));
};

const handlePriceLevelChange = (priceLevel: number) => {
  console.log('价格优先度选择:', priceLevel);
};

const handlePostProcessingChange = () => {
  console.log('燙後加工选择:', selectedPostProcessing.value);
};

const handlePostProcessingStepChange = (stepId: string) => {
  console.log('后加工步骤选择:', stepId);
};

const handleLaminationMaterialChange = (materialId: string) => {
  console.log('过胶材质选择:', materialId);
};

const handleInterfaceOptionChange = async () => {
  console.log('适用界面选择:', selectedInterfaceOption.value);

  if (!selectedInterfaceOption.value) {
    return;
  }

  // 根据选择的ID找到对应的选项信息
  const selectedOption = preProcessingStepsOptions.value.find(option => option.id.toString() === selectedInterfaceOption.value);
  if (selectedOption) {
    console.log('选择的选项详情:', selectedOption);

    // 触发查询
    await searchWithPreProcessingOption(selectedOption.id);
  }
};

const handleMaterialOptionChange = () => {
  console.log('材料适用界面选择:', selectedMaterialOption.value, selectedMaterialApplicability.value);
};

const handleStampingPatternOptionChange = () => {
  console.log('烫金图案选择:', selectedStampingPatternOption.value, selectedStampingPatternApplicability.value);
};

// 检查是否有有效的查询参数
const hasValidQueryParams = () => {
  // 检查所有可能的查询参数是否有值
  return !!(
    companyNumber.value ||
    gpNumber.value ||
    selectedInterfaceOption.value ||
    selectedProductType.value ||
    selectedPatternType.value ||
    (selectedStampingCascader.value && selectedStampingCascader.value.length > 0) ||
    (selectedMaterialCascader.value && selectedMaterialCascader.value.length > 0) ||
    selectedPatternAreaOptionId.value ||
    (selectedPostProcessingCascader.value && selectedPostProcessingCascader.value.length > 0) ||
    selectedPaizi.value ||
    selectedColorNum.value
  );
};

// 清空结果显示
const clearSearchResults = () => {
  searchResults.value = [];
  hotStampingStore.setSearchResults([]);
  // 通知父组件结果已清空
  emit('searchResults', { results: [], queryType: 'clear', queryParams: {} });
};

// 清空函数 - 按照 NewTagMatching.vue 的方式实现
const clearProductType = async () => {
  console.log('clearProductType 被调用');
  // 清空产品类型本身
  selectedProductType.value = '';
  // 清空图案选项与选择，并隐藏区块
  selectedPatternType.value = '';
  patternOptions.value = [];

  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }

  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空产品类型后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearProductType');
};

const clearPatternType = async () => {
  console.log('clearPatternType 被调用');
  selectedPatternType.value = '';
  console.log('selectedPatternType 已清空:', selectedPatternType.value);

  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }

  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearPatternType');
};

const clearStampingType = async () => {
  console.log('clearStampingType 被调用');
  selectedStampingType.value = '';
  selectedStampingPosition.value = '';
  showStampingPosition.value = false;
  selectedStampingCascader.value = [];

  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }

  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearStampingType');
};

const clearPriceLevel = async () => {
  selectedPriceLevel.value = null;
  
  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }
  
  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空价格等级后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearPriceLevel');
};

const clearPostProcessing = async () => {
  console.log('clearPostProcessing 被调用');
  selectedPostProcessing.value = [];
  selectedPostProcessingStep.value = '';
  selectedLaminationMaterial.value = '';
  selectedPostProcessingCascader.value = [];

  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }

  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearPostProcessing');
};

const clearPostProcessingStep = async () => {
  selectedPostProcessingStep.value = '';
  
  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }
  
  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空后加工步骤后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearPostProcessingStep');
};

const clearLaminationMaterial = async () => {
  selectedLaminationMaterial.value = '';
  
  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }
  
  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空过胶材质后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearLaminationMaterial');
};

const clearInterfaceOption = async () => {
  console.log('clearInterfaceOption 被调用');
  selectedInterfaceOption.value = '';
  selectedApplicability.value = '';

  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }

  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearInterfaceOption');
};

const clearMaterialOption = async () => {
  console.log('clearMaterialOption 被调用');
  selectedMaterialOption.value = '';
  selectedMaterialApplicability.value = '';
  selectedMaterialCascader.value = [];

  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }

  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearMaterialOption');
};

const clearStampingPattern = async () => {
  console.log('clearStampingPattern 被调用');
  selectedStampingPatternOption.value = '';
  selectedStampingPatternApplicability.value = '';
  selectedStampingPatternCascader.value = [];
  selectedPatternAreaOptionId.value = '';

  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }

  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearStampingPattern');
};

// 综合筛选处理函数
const handleComprehensiveFilterChange = async () => {
  console.log('综合筛选变化:', {
    paizi: selectedPaizi.value,
    colorNum: selectedColorNum.value
  });

  // 构建查询参数，集成到现有的渐进式查询中
  const queryParams = buildQueryParams();

  // 添加牌子和颜色编码筛选参数
  if (selectedPaizi.value) {
    queryParams.paizi = selectedPaizi.value;
  }
  if (selectedColorNum.value) {
    queryParams.colorNum = selectedColorNum.value;
  }

  // 使用现有的查询逻辑，而不是独立的API调用
  await executeQuery(queryParams, 'comprehensiveFilter');
};

// 清空综合筛选
const clearComprehensiveFilter = async () => {
  console.log('clearComprehensiveFilter 被调用');
  selectedPaizi.value = '';
  selectedColorNum.value = '';

  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }

  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearComprehensiveFilter');
};


// 统一的查询参数构建函数
const buildQueryParams = (baseParams: any = {}) => {
  const params = {
    companyNumber: companyNumber.value || undefined,
    gpNo: gpNumber.value || undefined,
    ...baseParams
  };

  // 前工序选项
  if (selectedInterfaceOption.value) {
    const selectedPreProcessingOption = preProcessingStepsOptions.value.find(
        option => option.id.toString() === selectedInterfaceOption.value
    );
    if (selectedPreProcessingOption) {
      params.preProcessingStepsOptionId = selectedPreProcessingOption.id;
      params.preProcessingStepsName = selectedPreProcessingOption.preProcessingStepsName; // 添加显示名称
    }
  }

  // 产品类型
  if (selectedProductType.value) {
    const selectedProductTypeOption = productTypeOptions.value.find(
        option => option.productName === selectedProductType.value
    );
    if (selectedProductTypeOption) {
      params.productTypeId = selectedProductTypeOption.id;
      params.productTypeName = selectedProductTypeOption.productName; // 添加显示名称
    }
  }

  // 图案类型
  if (selectedPatternType.value) {
    const selectedPatternOption = patternOptions.value.find(
        option => option.optionName === selectedPatternType.value
    );
    if (selectedPatternOption) {
      params.patternId = selectedPatternOption.id;
      params.patternName = selectedPatternOption.optionName; // 添加显示名称
    }
  }

  // 烫金类型
  if (selectedStampingCascader.value && selectedStampingCascader.value.length > 0) {
    console.log('处理烫金类型参数:', {
      selectedStampingCascader: selectedStampingCascader.value,
      stampingCascaderOptions: stampingCascaderOptions.value
    });
    
    const selectedStampingTypeOption = stampingCascaderOptions.value.find(
        option => option.value === selectedStampingCascader.value[0]
    );
    console.log('找到的一级选项:', selectedStampingTypeOption);
    
    if (selectedStampingTypeOption) {
      // 如果有二级选项
      if (selectedStampingCascader.value.length > 1 && selectedStampingTypeOption.children && selectedStampingTypeOption.children.length > 0) {
      const selectedPositionOption = selectedStampingTypeOption.children.find(
            (child: any) => child.value === selectedStampingCascader.value[1]
      );
        console.log('找到的二级选项:', selectedPositionOption);
        
      if (selectedPositionOption) {
          // 直接使用 selectedStampingCascader.value[1]，因为它本身就是 id
          params.hotStampingTypeOptionId = selectedStampingCascader.value[1];
          // 添加显示路径（包含两级）
          params.hotStampingTypeName = `${selectedStampingTypeOption.label}/${selectedPositionOption.label}`;
          console.log('烫金类型参数已添加（二级）:', {
            hotStampingTypeOptionId: params.hotStampingTypeOptionId,
            hotStampingTypeName: params.hotStampingTypeName
          });
        }
      } else {
        // 只选择了一级选项（没有子选项或只选择了一级）
        params.hotStampingTypeOptionId = selectedStampingTypeOption.value;
        params.hotStampingTypeName = selectedStampingTypeOption.label;
        console.log('烫金类型参数已添加（一级）:', {
          hotStampingTypeOptionId: params.hotStampingTypeOptionId,
          hotStampingTypeName: params.hotStampingTypeName
        });
      }
    }
  }

  // 适用界面
  if (selectedMaterialCascader.value && selectedMaterialCascader.value.length > 0) {
    const path = selectedMaterialCascader.value;

    let typeIdToken: string | undefined;
    let statusValue: string | undefined;
    let typeOption: any | undefined;

    if (path[0] === 'fabricPaperGroup') {
      typeIdToken = path[1];
      statusValue = path[2];
      const fabricRoot = materialCascaderOptions.value.find((opt: any) => opt.value === 'fabricPaperGroup');
      typeOption = fabricRoot?.children?.find((child: any) => child.value === typeIdToken);
    } else {
      typeIdToken = path[0];
      statusValue = path[1];
      typeOption = materialCascaderOptions.value.find((opt: any) => opt.value === typeIdToken);
    }

    if (typeIdToken && typeOption) {
      params.clothPaperTypeId = parseInt(typeIdToken, 10);

      const effectiveStatus =
        statusValue ||
        typeOption.defaultStatus ||
        (typeOption.children && typeOption.children[0]?.value) ||
        'V';

      params.clothPaperCompatibilityStatus = effectiveStatus;

      if (typeOption.children && typeOption.children.length > 1 && statusValue) {
        const statusOption = typeOption.children.find((child: any) => child.value === statusValue);
        if (statusOption) {
          params.materialInterfaceName = `${typeOption.label}/${statusOption.label}`;
          return params; // 修复：应该返回 params 而不是 undefined
        }
      }

      params.materialInterfaceName = typeOption.label;
    }
  }

  // 燙金圖案（面积/类型）
  if (selectedPatternAreaOptionId.value) {
    params.patternAreaOptionId = parseInt(selectedPatternAreaOptionId.value);
    // 添加显示名称
    const selectedPatternAreaOption = patternAreaOptions.value.find(
      option => option.id.toString() === selectedPatternAreaOptionId.value
    );
    if (selectedPatternAreaOption) {
      params.patternAreaOptionName = selectedPatternAreaOption.optionName;
    }
  }

  // 燙後加工選項 - 印刷相关
  if (selectedPostProcessing.value && selectedPostProcessing.value.length > 0) {
    // 检查是否包含印刷选项
    if (selectedPostProcessing.value.includes('printing')) {
      params.printing = 'V';
    }
    if (selectedPostProcessing.value.includes('uvPrinting')) {
      params.uvPrinting = 'V';
    }
    if (selectedPostProcessing.value.includes('ledUvGlitter')) {
      params.silk_screen_led_uv_sparkle_powder = 'V';
    }
    // 检查是否包含击凸选项
    if (selectedPostProcessing.value.includes('embossing')) {
      params.embossing = 'V';
    }
    // 检查是否包含过胶选项
    if (selectedPostProcessing.value.includes('laminating')) {
      params.laminating = 'V';
    }
  }

  // 过胶相关参数
  if (selectedPostProcessing.value.includes('laminating')) {
    // 如果同时选择了步骤和材质，传递具体的ID进行精确匹配
    if (selectedPostProcessingStepId.value && selectedLaminationMaterialId.value) {
      params.postProcessingStepId = selectedPostProcessingStepId.value;
      params.laminationMaterialId = selectedLaminationMaterialId.value;
      
      // 添加过胶显示名称
      const step = postProcessingOptions.value.find(opt => opt.id === selectedPostProcessingStepId.value);
      const material = laminationMaterialOptions.value.find(opt => opt.id === selectedLaminationMaterialId.value);
      if (step && material) {
        params.laminatingName = `过胶/${step.stepName}/${material.description}`;
      }
      
      console.log('传递过胶兼容性参数:', {
        postProcessingStepId: params.postProcessingStepId,
        laminationMaterialId: params.laminationMaterialId,
        laminatingName: params.laminatingName
      });
    } else {
      console.log('过胶选项已选择，但未选择具体步骤和材质，不进行兼容性过滤');
    }
  }

  // 综合筛选参数 - 牌子和颜色编码
  if (selectedPaizi.value) {
    params.paizi = selectedPaizi.value;
  }
  if (selectedColorNum.value) {
    params.colorNum = selectedColorNum.value;
  }

  return params;
};

// 统一的查询执行函数
const executeQuery = async (queryParams: any, queryType: string, retryCount = 0) => {
  const maxRetries = 2; // 最大重试次数
  
  try {
    searching.value = true;
    error.value = null;
    lastActivityTime.value = Date.now(); // 更新活动时间

    // 确保 queryParams 存在，如果不存在则初始化为空对象
    if (!queryParams) {
      queryParams = {};
    }

           // 添加分页参数（默认第一页，每页15条）
           const pageSize = 15;
           const offset = 0;
           queryParams.pageSize = pageSize;
           queryParams.offset = offset;
           
           // 添加排序参数（默认按匹配度降序）
           queryParams.sortBy = 'matchScore';
           queryParams.sortOrder = 'desc';
           
           // 添加用户ID（用于匹配度计算）
           if (authStore.userInfo?.id) {
             queryParams.userId = authStore.userInfo.id;
           }
           
           // 添加匹配度规则类型
           queryParams.matchScoreRule = matchScoreRule.value;

    // 配置axios请求，增加超时时间
    const response = await axios.post('/api/api/gold-foil/match', queryParams, {
      timeout: 300000, // 30秒超时
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    // 处理分页响应
    const responseData = response?.data || {};
    const results = responseData.items || [];
    const total = responseData.total || 0;

    searchResults.value = results;
    hotStampingStore.setSearchResults(results);
    
    // 存储分页信息到store
    if (responseData.total !== undefined) {
      hotStampingStore.setPaginationInfo({
        total: total,
        pageSize: pageSize,
        currentPage: responseData.currentPage || 1,
        totalPages: responseData.totalPages
      });
    }

    emit('searchResults', {
      results,
      total: total,
      currentPage: responseData.currentPage || 1,
      pageSize: pageSize,
      queryType,
      queryParams
    });

    return results;
  } catch (err: any) {
    console.error(`${queryType}查询失败:`, err);
    
    // 检查是否是超时错误或网络错误
    const isTimeoutError = err.code === 'ECONNABORTED' || err.message?.includes('timeout');
    const isNetworkError = err.code === 'ERR_NETWORK' || err.response?.status === 504;
    
    // 如果是超时或网络错误且还有重试次数，则自动重试
    if ((isTimeoutError || isNetworkError) && retryCount < maxRetries) {
      console.log(`第${retryCount + 1}次重试...`);
      await new Promise(resolve => setTimeout(resolve, 1000 * (retryCount + 1))); // 递增延迟
      return executeQuery(queryParams, queryType, retryCount + 1);
    }
    
    // 根据错误类型显示不同的错误信息
    let errorMessage = '查询失败，请稍后重试';
    if (err.response?.status === 504) {
      errorMessage = '服务器超时，请检查网络连接后重试';
    } else if (isTimeoutError) {
      errorMessage = '请求超时，请检查网络连接后重试';
    } else if (isNetworkError) {
      errorMessage = '网络连接失败，请检查网络后重试';
    } else if (err.response?.status >= 500) {
      errorMessage = '服务器错误，请稍后重试';
    } else if (err.response?.status === 401) {
      errorMessage = '登录已过期，请重新登录';
    }
    
    error.value = errorMessage;
    searchResults.value = [];

    emit('searchError', {
      error: errorMessage,
      queryType
    });

    throw err;
  } finally {
    searching.value = false;
  }
};

// 查询函数 - 調用後端匹配接口
const searchByCompanyNumber = async () => {
  if (!companyNumber.value) {
    error.value = '請輸入公司編號';
    return;
  }
  const params = buildQueryParams();
  console.log('公司/客戶編號查詢參數:', params);
  await executeQuery(params, 'companyNumber');
};

const searchByGpNumber = async () => {
  if (!gpNumber.value) {
    error.value = '請輸入燙金紙型號';
    return;
  }
  const params = buildQueryParams();
  console.log('燙金紙型號查詢參數:', params);
  await executeQuery(params, 'gpNumber');
};


const clearCompanyNumber = async () => {
  companyNumber.value = '';
  error.value = null;
  
  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }
  
  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空公司编号后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearCompanyNumber');
};

const clearGpNumber = async () => {
  gpNumber.value = '';
  error.value = null;
  
  // 检查是否还有其他有效参数
  if (!hasValidQueryParams()) {
    console.log('没有其他有效参数，直接清空结果');
    clearSearchResults();
    return;
  }
  
  // 重新发起请求
  const queryParams = buildQueryParams();
  console.log('清空客户编号后重新查询参数:', queryParams);
  await executeQuery(queryParams, 'clearGpNumber');
};

const clearAllParams = () => {
  companyNumber.value = '';
  gpNumber.value = '';
  error.value = null;
  selectedProductType.value = '';
  selectedPatternType.value = '';
  selectedStampingType.value = '';
  selectedStampingPosition.value = '';
  showStampingPosition.value = false;
  selectedPriceLevel.value = null;
  selectedPostProcessing.value = [];
  selectedPostProcessingStep.value = '';
  selectedLaminationMaterial.value = '';
  // 清空过胶相关ID
  selectedPostProcessingStepId.value = null;
  selectedLaminationMaterialId.value = null;
  selectedInterfaceOption.value = '';
  selectedApplicability.value = '';
  selectedMaterialOption.value = '';
  selectedMaterialApplicability.value = '';
  selectedMaterialCascader.value = [];
  selectedStampingPatternOption.value = '';
  selectedStampingPatternApplicability.value = '';
  selectedStampingPatternCascader.value = [];
  // 清空第9项：燙金圖案(常用界面選用)
  selectedPatternAreaOptionId.value = '';
  // 清空第10项：燙後加工選項
  selectedPostProcessingCascader.value = [];
  // 清空级联选择器
  selectedStampingCascader.value = [];
  selectedInterfaceCascader.value = [];
  // 清空综合筛选参数
  selectedPaizi.value = '';
  selectedColorNum.value = '';
  
  // 清空所有参数后，直接清空结果
  clearSearchResults();
};

// 获取前工序选项数据
const loadPreProcessingStepsOptions = async () => {
  try {
    loadingPreProcessingSteps.value = true;
    const options = await preProcessingStepsApi.getAllActiveOptions();
    preProcessingStepsOptions.value = options;

    // 转换为前端需要的格式
    interfaceOptions.value = options.map(option => ({
      value: option.id.toString(),
      label: option.preProcessingStepsName
    }));

    console.log('前工序选项加载成功:', interfaceOptions.value);
  } catch (err: any) {
    console.error('获取前工序选项失败:', err);
    error.value = '获取前工序选项失败，请稍后重试';
  } finally {
    loadingPreProcessingSteps.value = false;
  }
};

// 获取产品类型选项数据
const loadProductTypeOptions = async () => {
  try {
    loadingProductType.value = true;
    const options = await productTypeOptionsApi.getAllActiveOptions();
    productTypeOptions.value = options;

    console.log('产品类型选项加载成功:', productTypeOptions.value);
  } catch (err: any) {
    console.error('获取产品类型选项失败:', err);
    error.value = '获取产品类型选项失败，请稍后重试';
  } finally {
    loadingProductType.value = false;
  }
};

// 获取布料纸类型选项数据
const loadClothPaperTypes = async () => {
  try {
    loadingClothPaperTypes.value = true;
    const types = await clothPaperTypeApi.getActiveClothPaperTypes();
    clothPaperTypes.value = types;

    // 为每个布面纸类型查询其映射中实际存在的兼容性状态（V / ▷）
    const statusMap: Record<number, { hasV: boolean; hasNeedBase: boolean }> = {};

    await Promise.all(
      types.map(async (type) => {
        try {
          const resp = await fetch(`/api/api/cloth-paper-compatibility/type/${type.id}`);
          if (!resp.ok) {
            // 查询失败时，默认只认为有「适用」选项，避免阻塞主流程
            statusMap[type.id] = { hasV: true, hasNeedBase: false };
            return;
          }
          const mappings: any[] = await resp.json();
          const hasV = mappings.some((m) => m.compatibilityStatus === 'V');
          const hasNeedBase = mappings.some((m) => m.compatibilityStatus === '▷');
          statusMap[type.id] = { hasV, hasNeedBase };
        } catch (e) {
          console.error(`加载布面纸类型 ${type.id} 的兼容性状态失败:`, e);
          statusMap[type.id] = { hasV: true, hasNeedBase: false };
        }
      })
    );

    // 构建级联选择器数据：
    // - 如果某个布面纸类型只有一种状态，则做成叶子节点，选中后直接确定状态；
    // - 如果有多种状态，则展示为多级选项（例如：适用 / 需要作「絲印打底處理」再烫金）。
    // - 将「布面纸」相关类型统一归到一级分组「布面紙」下，便于在界面上集中管理。
    const cascaderData: any[] = [];
    const fabricChildren: any[] = [];

    types.forEach((type) => {
      const status = statusMap[type.id] || { hasV: true, hasNeedBase: false };
      const children: any[] = [];
      if (status.hasV) {
        children.push({ value: 'V', label: '适用' });
      }
      if (status.hasNeedBase) {
        children.push({ value: '▷', label: '需要作「絲印打底處理」再烫金' });
      }

      const baseOption: any = {
        value: type.id.toString(), // 使用字符串形式的 id，便于与级联值处理保持一致
        label: `${type.category}.${type.typeName}`,
      };

      if (children.length <= 1) {
        // 只有一种状态：不展示二级选项，直接在一级上隐式带上默认状态
        baseOption.defaultStatus = children[0]?.value || 'V';
      } else {
        // 多种状态：展示二级选项
        baseOption.children = children;
      }

      // 判断是否属于布面纸系列（尼龍絲、涤纶、仿棉、涤棉、麻棉、純棉、棉布、絲光棉、絹布、閃光布、絲綢布、麻壓布、壓紋布、絲光麻布、粗布）
      const fabricKeywords = [
        '尼龍絲',
        '涤纶',
        '滌綸',
        '仿棉',
        '涤棉',
        '滌棉',
        '麻棉',
        '純棉',
        '棉布',
        '絲光棉',
        '絹布',
        '閃光布',
        '絲綢布',
        '麻壓布',
        '壓紋布',
        '絲光麻布',
        '粗布'
      ];

      const isFabricPaper =
        type.category?.includes('布面紙') ||
        fabricKeywords.some((kw) => type.category?.includes(kw) || type.typeName?.includes(kw));

      if (isFabricPaper) {
        fabricChildren.push(baseOption);
      } else {
        cascaderData.push(baseOption);
      }
    });

    if (fabricChildren.length > 0) {
      cascaderData.push({
        value: 'fabricPaperGroup',
        label: '布面紙',
        children: fabricChildren
      });
    }

    materialCascaderOptions.value = cascaderData;

    console.log('布料纸类型选项加载成功:', materialCascaderOptions.value);
    
    // 更新显示值
    updateMaterialInputDisplayValue();
  } catch (err: any) {
    console.error('获取布料纸类型选项失败:', err);
    error.value = '获取布料纸类型选项失败，请稍后重试';
  } finally {
    loadingClothPaperTypes.value = false;
  }
};

// 获取过胶相关数据
const loadLaminationOptions = async () => {
  try {
    loadingLaminationOptions.value = true;
    const [materialOptions, processingOptions] = await Promise.all([
      laminatingApi.getAllMaterialOptions(),
      laminatingApi.getAllProcessingOptions()
    ]);
    laminationMaterialOptions.value = materialOptions;
    postProcessingOptions.value = processingOptions;
    
    // 动态构建过胶级联选择器选项
    buildLaminationCascaderOptions();
    
    console.log('过胶相关数据加载成功:', {
      materialOptions: laminationMaterialOptions.value,
      processingOptions: postProcessingOptions.value
    });
    
    // 测试：手动触发一次级联选择器更新
    setTimeout(() => {
      console.log('延迟检查级联选择器数据:', postProcessingCascaderOptions.value);
    }, 1000);
  } catch (error) {
    console.error('加载过胶相关数据失败:', error);
    ElMessage.error('加载过胶相关数据失败');
  } finally {
    loadingLaminationOptions.value = false;
  }
};

// 动态构建过胶级联选择器选项
const buildLaminationCascaderOptions = () => {
  if (postProcessingOptions.value.length === 0 || laminationMaterialOptions.value.length === 0) {
    console.log('过胶数据未加载完成，使用静态数据作为fallback');
    // 使用静态数据作为fallback
    buildStaticLaminationOptions();
    return;
  }

  // 构建过胶选项的子选项
  const laminationChildren = postProcessingOptions.value.map(step => ({
    value: step.id,
    label: step.stepName,
    children: laminationMaterialOptions.value.map(material => ({
      value: material.id,
      label: material.description
    }))
  }));

  // 更新级联选择器中的过胶选项
  const laminationOption = postProcessingCascaderOptions.value.find(option => option.value === 'laminating');
  if (laminationOption) {
    laminationOption.children = laminationChildren;
  }

  console.log('过胶级联选择器选项已更新:', laminationChildren);
  console.log('更新后的级联选择器选项:', postProcessingCascaderOptions.value);
};

// 构建静态过胶选项（fallback）
const buildStaticLaminationOptions = () => {
  const staticLaminationChildren = [
    {
      value: 1, // 假设的ID
      label: '擊凹/凸',
      children: [
        { value: 1, label: '普通预涂菲林-光/啞膠' },
        { value: 2, label: '普通预涂菲林-平价耐磨啞膠' }
      ]
    },
    {
      value: 2, // 假设的ID
      label: '啤折/切',
      children: [
        { value: 1, label: '普通预涂菲林-光/啞膠' },
        { value: 2, label: '普通预涂菲林-平价耐磨啞膠' }
      ]
    }
  ];

  const laminationOption = postProcessingCascaderOptions.value.find(option => option.value === 'laminating');
  if (laminationOption) {
    laminationOption.children = staticLaminationChildren;
  }

  console.log('使用静态过胶选项:', staticLaminationChildren);
};

// 根据产品类型ID加载图案选项
const loadPatternOptionsByProductTypeId = async (productTypeId: number) => {
  try {
    loadingPattern.value = true;
    const options = await patternOptionsApi.getPatternOptionsByProductTypeId(productTypeId);
    patternOptions.value = options;

    console.log('图案选项加载成功:', patternOptions.value);
  } catch (err: any) {
    console.error('获取图案选项失败:', err);
    error.value = '获取图案选项失败，请稍后重试';
    patternOptions.value = [];
  } finally {
    loadingPattern.value = false;
  }
};

// 加载燙金圖案（面积/类型）单级选项
const loadPatternAreaOptions = async () => {
  try {
    loadingPatternArea.value = true;
    const options = await hotStampingPatternAreaOptionsApi.getActiveOptions();
    patternAreaOptions.value = options;
    console.log('燙金圖案（面积/类型）选项加载成功:', patternAreaOptions.value);
  } catch (err: any) {
    console.error('获取燙金圖案（面积/类型）选项失败:', err);
    patternAreaOptions.value = [];
  } finally {
    loadingPatternArea.value = false;
  }
};

// 使用前工序选项进行查询
const searchWithPreProcessingOption = async (preProcessingStepsOptionId: number) => {
  const queryParams = buildQueryParams({ preProcessingStepsOptionId });
  console.log('发送查询请求:', queryParams);
  await executeQuery(queryParams, 'preProcessingSteps');
};

// 使用产品类型进行查询
const searchWithProductType = async (productTypeId: number) => {
  const queryParams = buildQueryParams({ productTypeId });
  console.log('发送产品类型查询请求:', queryParams);
  await executeQuery(queryParams, 'productType');
};

// 基于图案类型的查询
const searchWithPatternType = async (patternId: number) => {
  const queryParams = buildQueryParams({ patternId });
  console.log('发送图案类型查询请求:', queryParams);
  await executeQuery(queryParams, 'patternType');
};

// 定义事件
const emit = defineEmits<{
  searchResults: [data: { results: any[], queryType: string, queryParams: any }];
  searchError: [data: { error: string, queryType: string }];
}>();


// 组件挂载时加载数据
onMounted(() => {
  // 先构建静态过胶选项，确保界面可用
  buildStaticLaminationOptions();
  
  loadPreProcessingStepsOptions();
  loadProductTypeOptions();
  loadHotStampingTypeOptions();
  loadClothPaperTypes();
  loadPatternAreaOptions();
  loadLaminationOptions();
  
  // 添加点击外部区域关闭下拉面板的监听
  document.addEventListener('click', handleClickOutside);
});

// 监听 selectedMaterialCascader 变化，更新显示值
watch(selectedMaterialCascader, () => {
  updateMaterialInputDisplayValue();
}, { deep: true });

// 监听 showMaterialDropdown 变化，自动打开 cascader 下拉面板
watch(showMaterialDropdown, (newVal) => {
  if (newVal) {
    nextTick(() => {
      openCascaderDropdown();
    });
  }
});

// 监听 selectedStampingCascader 变化，更新显示值
watch(selectedStampingCascader, () => {
  updateStampingInputDisplayValue();
}, { deep: true });

// 监听 showStampingDropdown 变化，自动打开 cascader 下拉面板
watch(showStampingDropdown, (newVal) => {
  if (newVal) {
    nextTick(() => {
      openCascaderDropdown();
    });
  }
});

// 监听 selectedPostProcessingCascader 变化，更新显示值
watch(selectedPostProcessingCascader, () => {
  updatePostProcessingInputDisplayValue();
}, { deep: true });

// 监听 showPostProcessingDropdown 变化，自动打开 cascader 下拉面板
watch(showPostProcessingDropdown, (newVal) => {
  if (newVal) {
    nextTick(() => {
      openCascaderDropdown();
    });
  }
});

// 点击外部区域关闭下拉面板
const handleClickOutside = (event: MouseEvent) => {
  const target = event.target as HTMLElement;
  const materialWrapper = document.querySelector('.material-selector-wrapper');
  const stampingWrapper = document.querySelector('.stamping-selector-wrapper');
  const postProcessingWrapper = document.querySelector('.postprocessing-selector-wrapper');
  
  if (materialWrapper && !materialWrapper.contains(target)) {
    showMaterialDropdown.value = false;
  }
  
  if (stampingWrapper && !stampingWrapper.contains(target)) {
    showStampingDropdown.value = false;
  }
  
  if (postProcessingWrapper && !postProcessingWrapper.contains(target)) {
    showPostProcessingDropdown.value = false;
  }
};


// 基于烫金类型选项的查询
const searchWithHotStampingTypeOption = async (hotStampingTypeOptionId: number) => {
  const queryParams = buildQueryParams({ hotStampingTypeOptionId });
  console.log('发送烫金类型选项查询请求:', queryParams);
  await executeQuery(queryParams, 'hotStampingTypeOption');
};

// 基于燙金圖案（面积/类型）单级选项的查询（固定筛选 V）
const searchWithPatternAreaOption = async (patternAreaOptionId: number) => {
  const queryParams = buildQueryParams({ patternAreaOptionId });
  console.log('发送燙金圖案（面积/类型）查询请求:', queryParams);
  await executeQuery(queryParams, 'patternAreaOption');
};

// 基于布料纸类型的查询
const searchWithClothPaperType = async (clothPaperTypeId: number, compatibilityStatus: string = 'V') => {
  const queryParams = buildQueryParams({
    clothPaperTypeId,
    clothPaperCompatibilityStatus: compatibilityStatus
  });
  console.log('布料纸类型查询参数:', queryParams);
  // 确保 queryParams 存在
  if (!queryParams) {
    console.error('buildQueryParams 返回了 undefined 或 null');
    return;
  }
  await executeQuery(queryParams, 'clothPaperType');
};

// 移除定期网络检测 - 只依赖浏览器的 navigator.onLine 和实际业务请求
// 这样可以避免不必要的 API 请求，减少服务器负载，更适合生产环境
// 网络状态通过以下方式判断：
// 1. 浏览器的 navigator.onLine API（自动更新）
// 2. 实际业务请求的错误处理（executeQuery 中已有网络错误处理）

// 监听网络状态变化
const handleOnline = () => {
  const wasOffline = !isOnline.value;
  isOnline.value = true;
  // 只在之前确实离线时才显示恢复消息
  if (wasOffline) {
    ElMessage.success('网络连接已恢复');
  }
  // 网络恢复后，如果有待处理的查询，自动重试
  if (searching.value) {
    console.log('网络恢复，自动重试查询...');
  }
};

const handleOffline = () => {
  // 只有在浏览器明确报告离线时才显示警告
  if (!navigator.onLine) {
    isOnline.value = false;
    // 延迟显示警告，避免频繁弹出
    setTimeout(() => {
      if (!navigator.onLine && !isOnline.value) {
        ElMessage.warning('网络连接已断开，请检查网络设置');
      }
    }, 1000);
  }
};

// 已移除定期网络检测 - 只依赖浏览器原生 API 和业务请求错误处理
// 不再需要定期发送检测请求，减少服务器负载

// 组件挂载时只监听浏览器原生网络事件
onMounted(() => {
  // 初始化网络状态
  isOnline.value = navigator.onLine;
  lastActivityTime.value = Date.now();
  
  // 监听浏览器原生网络状态变化
  window.addEventListener('online', handleOnline);
  window.addEventListener('offline', handleOffline);
});

// 组件卸载时清理
onUnmounted(() => {
  window.removeEventListener('online', handleOnline);
  window.removeEventListener('offline', handleOffline);
  document.removeEventListener('click', handleClickOutside);
});

// 暴露方法给父组件
defineExpose({
  clearAllParams,
  searchWithPreProcessingOption,
  searchWithProductType,
  searchWithHotStampingTypeOption,
  searchWithClothPaperType
});
</script>

<template>
  <div class="space-y-6">
    <!-- 连接状态指示器 -->
    <div v-if="!isOnline" class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
      <div class="flex items-center text-red-600">
        <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
        </svg>
        <span class="text-sm font-medium">网络连接已断开，请检查网络设置</span>
      </div>
    </div>

    <!-- 公司编号查询 -->
    <div class="mb-6 bg-blue-50 p-4 rounded-lg border border-blue-200">
      <div class="flex justify-between items-center mb-3">
        <h3 class="text-md font-medium text-blue-700">1.根據公司編號或客戶編號查詢</h3>
        <button v-if="companyNumber" @click="clearCompanyNumber"
                class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors">
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          清空
        </button>
      </div>
      <div class="flex space-x-2">
        <div class="relative flex-1">
          <input v-model="companyNumber" placeholder="請輸入公司編號或客戶編號並按回車鍵"
                 class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
                 @keyup.enter="searchByCompanyNumber" />
        </div>
      </div>
      <div v-if="error" class="mt-2 text-sm text-red-600">{{ error }}</div>
    </div>

    <!-- 客户编号查询 -->
    <div class="mb-6 bg-green-50 p-4 rounded-lg border border-green-200">
      <div class="flex justify-between items-center mb-3">
        <h3 class="text-md font-medium text-green-700">2.根據燙金紙型號查詢</h3>
        <button v-if="gpNumber" @click="clearGpNumber"
                class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors">
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          清空
        </button>
      </div>
      <div class="flex space-x-2">
        <div class="relative flex-1">
          <input v-model="gpNumber" placeholder="請輸入燙金紙型號並按回車鍵"
                 class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
                 @keyup.enter="searchByGpNumber" />
        </div>
      </div>
      <div v-if="error" class="mt-2 text-sm text-red-600">{{ error }}</div>
    </div>

    <!-- 适用界面(前工序) -->
    <div class="mb-4">
      <div class="flex justify-between items-center mb-3">
        <h3 class="text-md font-medium text-gray-700">3. 適用界面(前工序)</h3>
        <button v-if="selectedInterfaceOption || selectedApplicability" @click="clearInterfaceOption"
                class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors">
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          清空
        </button>
      </div>
      <div class="flex space-x-3">
        <div class="flex-1 preprocessing-selector-wrapper">
          <div class="preprocessing-search-icon">
            <el-icon class="search-icon"><Search /></el-icon>
          </div>
          <el-select
              v-model="selectedInterfaceOption"
              placeholder="請選擇適前工序選項（可輸入關鍵詞搜索）"
              class="w-full select-with-search-icon"
              filterable
              @change="handleInterfaceOptionChange"
              :loading="loadingPreProcessingSteps"
              :disabled="loadingPreProcessingSteps"
          >
            <el-option
                v-for="option in interfaceOptions"
                :key="option.value"
                :label="option.label"
                :value="option.value"
            />
          </el-select>
        </div>
        <!-- <div class="w-32">
          <el-select v-model="selectedApplicability" placeholder="请选择" class="w-full" @change="handleInterfaceOptionChange">
            <el-option v-for="option in applicabilityOptions" :key="option.value" :label="option.label" :value="option.value" />
          </el-select>
        </div> -->
      </div>

      <!-- 查询状态显示 -->
      <div v-if="searching" class="mt-2 text-sm text-blue-600 flex items-center">
        <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-blue-600" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
          <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
          <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
        </svg>
        正在查询匹配结果...
      </div>
    </div>

    <!-- 产品类型选择 -->
    <div class="mb-4">
      <div class="flex justify-between items-center mb-3">
        <h3 class="text-md font-medium text-gray-700">4. 產品類型</h3>
        <button v-if="selectedProductType" @click="clearProductType"
                class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors">
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          清空
        </button>
      </div>
      <el-select
          v-model="selectedProductType"
          placeholder="請選擇產品類型"
          class="w-full"
          @change="handleProductTypeChange"
          :loading="loadingProductType"
          :disabled="loadingProductType"
      >
        <el-option
            v-for="option in productTypeOptions"
            :key="option.id"
            :label="option.productName"
            :value="option.productName"
        />
      </el-select>
    </div>

    <!-- 烫金图案类型 -->
    <div class="mb-4" v-if="patternOptions.length > 0">
      <div class="flex justify-between items-center mb-3">
        <h3 class="text-md font-medium text-gray-700">5. 燙金圖案(X)(耐磨燙金紙選用)</h3>
        <button v-if="selectedPatternType" @click="clearPatternType"
                class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors">
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          清空
        </button>
      </div>
      <el-select
          v-model="selectedPatternType"
          placeholder="請選擇燙金圖案類型"
          class="w-full"
          @change="handlePatternTypeChange"
          :loading="loadingPattern"
          :disabled="loadingPattern"
      >
        <el-option
            v-for="option in patternOptions"
            :key="option.id"
            :label="option.optionName"
            :value="option.optionName"
        />
      </el-select>
    </div>

    <!-- 烫金类型 -->
    <div class="mb-4">
      <div class="flex justify-between items-center mb-3">
        <h3 class="text-md font-medium text-gray-700">6. 燙金類型</h3>
        <button v-if="selectedStampingCascader.length > 0" @click="clearStampingType"
                class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors">
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          清空
        </button>
      </div>

      <!-- 自定义输入框 - 集成搜索和下拉功能（保持左右两列布局） -->
      <div class="stamping-selector-wrapper">
        <el-input
            :model-value="stampingInputDisplayValue || stampingSearchKeyword"
            :placeholder="stampingInputDisplayValue ? '' : '請選擇燙金類型和位置（可輸入關鍵詞搜索）'"
            class="w-full stamping-selector-input"
            clearable
            @clear="handleStampingInputClear"
            @focus="handleStampingInputFocus"
            @click="handleStampingInputClick"
            @input="handleStampingInputInput"
        >
          <template #prefix>
            <el-icon class="search-icon"><Search /></el-icon>
          </template>
          <template #suffix>
            <el-icon class="arrow-icon" :class="{ 'is-reverse': showStampingDropdown }">
              <ArrowDown />
            </el-icon>
          </template>
        </el-input>
        <!-- 使用绝对定位的 cascader 下拉面板（只显示面板，不显示输入框） -->
        <div v-if="showStampingDropdown" class="stamping-cascader-dropdown">
          <el-cascader-panel
              ref="stampingCascaderRef"
              v-model="selectedStampingCascader"
              :options="filteredStampingCascaderOptions"
              :props="cascaderProps"
              @change="handleStampingCascaderChange"
          ></el-cascader-panel>
        </div>
      </div>
    </div>

    <!-- 适用界面 - 多级下拉框 -->
    <div class="mb-4">
      <div class="flex justify-between items-center mb-3">
        <h3 class="text-md font-medium text-gray-700">7. 適用界面</h3>
        <button v-if="selectedMaterialCascader.length > 0" @click="clearMaterialOption"
                class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors">
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          清空
        </button>
      </div>

      <!-- 自定义输入框 - 集成搜索和下拉功能（保持左右两列布局） -->
      <div class="material-selector-wrapper">
        <el-input
            :model-value="materialInputDisplayValue || materialSearchKeyword"
            :placeholder="materialInputDisplayValue ? '' : '請選擇材料適用界面和適用性（可輸入關鍵詞搜索，如：JHT、适用）'"
            class="w-full material-selector-input"
            clearable
            @clear="handleMaterialInputClear"
            @focus="handleMaterialInputFocus"
            @click="handleMaterialInputClick"
            @input="handleMaterialInputInput"
        >
          <template #prefix>
            <el-icon class="search-icon"><Search /></el-icon>
          </template>
          <template #suffix>
            <el-icon class="arrow-icon" :class="{ 'is-reverse': showMaterialDropdown }">
              <ArrowDown />
            </el-icon>
          </template>
        </el-input>
        <!-- 使用绝对定位的 cascader 下拉面板（只显示面板，不显示输入框） -->
        <div v-if="showMaterialDropdown" class="material-cascader-dropdown">
          <el-cascader-panel
              ref="materialCascaderRef"
              v-model="selectedMaterialCascader"
              :options="filteredMaterialCascaderOptions"
              :props="cascaderProps"
              @change="handleMaterialCascaderChange"
          ></el-cascader-panel>
        </div>
      </div>
    </div>

    <!-- 烫金图案（面积/类型）- 单级下拉 -->
    <div class="mb-4">
      <div class="flex justify-between items-center mb-3">
        <div class="flex items-center gap-1">
          <h3 class="text-md font-medium text-gray-700">8.燙金圖案(常用界面選用)</h3>
          <el-tooltip
              content="大面積：≥100mmx100mm&#10;中面積：100mmx100mm<X≥50mmx50mm&#10;小面積：<50mmx50mm"
              placement="top"
              effect="dark"
          >
            <span class="inline-flex items-center justify-center w-4 h-4 text-xs font-bold text-gray-400 hover:text-gray-600 cursor-help border border-gray-300 rounded-full hover:border-gray-400 transition-all duration-200">
              ?
            </span>
          </el-tooltip>
        </div>
        <button v-if="selectedPatternAreaOptionId" @click="clearStampingPattern"
                class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors">
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          清空
        </button>
      </div>
      <el-select
          v-model="selectedPatternAreaOptionId"
          placeholder="請選擇燙金圖案"
          class="w-full"
          :loading="loadingPatternArea"
          :disabled="loadingPatternArea"
          @change="handlePatternAreaOptionChange"
      >
        <el-option
            v-for="opt in patternAreaOptions"
            :key="opt.id"
            :label="opt.optionName"
            :value="opt.id.toString()"
        />
      </el-select>
    </div>


    <!-- 燙後加工选项 - 多级下拉框 -->
    <div class="mb-4">
      <div class="flex justify-between items-center mb-3">
        <h3 class="text-md font-medium text-gray-700">9. 燙後加工選項</h3>
        <button v-if="selectedPostProcessingCascader.length > 0" @click="clearPostProcessing"
                class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors">
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          清空
        </button>
      </div>

      <!-- 自定义输入框 - 集成搜索和下拉功能（保持左右两列布局） -->
      <div class="postprocessing-selector-wrapper">
        <el-input
            :model-value="postProcessingInputDisplayValue || postProcessingSearchKeyword"
            :placeholder="postProcessingInputDisplayValue ? '' : '請選擇燙後加工選項和具體步驟（可輸入關鍵詞搜索）'"
            class="w-full postprocessing-selector-input"
            clearable
            @clear="handlePostProcessingInputClear"
            @focus="handlePostProcessingInputFocus"
            @click="handlePostProcessingInputClick"
            @input="handlePostProcessingInputInput"
        >
          <template #prefix>
            <el-icon class="search-icon"><Search /></el-icon>
          </template>
          <template #suffix>
            <el-icon class="arrow-icon" :class="{ 'is-reverse': showPostProcessingDropdown }">
              <ArrowDown />
            </el-icon>
          </template>
        </el-input>
        <!-- 使用绝对定位的 cascader 下拉面板（只显示面板，不显示输入框） -->
        <div v-if="showPostProcessingDropdown" class="postprocessing-cascader-dropdown">
          <el-cascader-panel
              ref="postProcessingCascaderRef"
              v-model="selectedPostProcessingCascader"
              :options="filteredPostProcessingCascaderOptions"
              :props="cascaderProps"
              @change="handlePostProcessingCascaderChange"
          ></el-cascader-panel>
        </div>
      </div>

      <!-- 印刷选项警告提示 -->
      <el-alert
          v-if="showPrintingWarning"
          title="提示"
          type="warning"
          description="选择印刷选项时，需要同时选择 7. 適用界面 以获得准确的匹配结果"
          :closable="false"
          show-icon
          style="margin-top: 10px;"
      />

      <!-- 絲印LED UV灑閃粉选项警告提示 -->
      <el-alert
          v-if="showLedUvGlitterWarning"
          title="提示"
          type="warning"
          description="选择絲印LED UV灑閃粉选项时，需要同时选择 7. 適用界面 以获得准确的匹配结果"
          :closable="false"
          show-icon
          style="margin-top: 10px;"
      />

    </div>

    <!-- 综合筛选 - 牌子和颜色编码 -->
    <div class="mb-4 bg-purple-50 p-4 rounded-lg border border-purple-200">
      <div class="flex justify-between items-center mb-3">
        <h3 class="text-md font-medium text-purple-700">10. 根據牌子及顏色編碼查詢</h3>
        <button v-if="selectedPaizi || selectedColorNum" @click="clearComprehensiveFilter"
                class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors">
          <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
          清空
        </button>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <!-- 牌子输入 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">牌子</label>
          <div class="relative">
            <input
                v-model="selectedPaizi"
                type="text"
                placeholder="請輸入牌子名稱"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 text-sm"
                @keyup.enter="handleComprehensiveFilterChange"
            />
          </div>
        </div>

        <!-- 颜色编码输入 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">顏色編碼</label>
          <div class="relative">
            <input
                v-model="selectedColorNum"
                type="text"
                placeholder="請輸入顏色編碼"
                class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500 text-sm"
                @keyup.enter="handleComprehensiveFilterChange"
            />
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>
/* 前工序选择器样式 */
.preprocessing-selector-wrapper {
  position: relative;
}

.preprocessing-search-icon {
  position: absolute;
  left: 11px;
  top: 50%;
  transform: translateY(-50%);
  z-index: 999;
  pointer-events: none;
  display: flex;
  align-items: center;
  color: var(--el-text-color-placeholder);
}

.preprocessing-search-icon .search-icon {
  font-size: 14px;
}

.preprocessing-selector-wrapper :deep(.el-select) {
  position: relative;
}

.preprocessing-selector-wrapper :deep(.el-input__wrapper) {
  padding-left: 35px !important;
}

.preprocessing-selector-wrapper :deep(.el-input__inner) {
  padding-left: 35px !important;
}

.preprocessing-selector-wrapper :deep(.el-select__wrapper) {
  padding-left: 35px !important;
}

/* 材料选择器样式 */
.material-selector-wrapper {
  position: relative;
}

.material-selector-input {
  cursor: pointer;
}

.material-selector-input :deep(.el-input__inner) {
  cursor: pointer;
}

.material-cascader-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 2000;
  margin-top: 4px;
  background: white;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  box-shadow: var(--el-box-shadow-light);
  overflow: visible;
  width: max-content;
  min-width: 200px;
}

/* Cascader 面板样式 - 根据内容自适应宽度 */
.material-cascader-dropdown :deep(.el-cascader-panel) {
  border: none;
  box-shadow: none;
  background: transparent;
  width: max-content;
  display: flex;
}

/* 所有菜单列根据内容自适应宽度 */
.material-cascader-dropdown :deep(.el-cascader-menu) {
  width: max-content;
  min-width: 150px;
  flex-shrink: 0;
}

/* 有内容的列显示右边框 */
.material-cascader-dropdown :deep(.el-cascader-menu:not(:empty)) {
  border-right: 1px solid var(--el-border-color-lighter);
}

/* 第一列（主选项列）根据内容自适应 */
.material-cascader-dropdown :deep(.el-cascader-menu:first-child) {
  width: max-content;
  min-width: 150px;
}

/* 第二列及后续列根据内容自适应，空列隐藏 */
.material-cascader-dropdown :deep(.el-cascader-menu:nth-child(n+2)) {
  width: max-content;
  min-width: 0;
}

/* 空的列不显示 */
.material-cascader-dropdown :deep(.el-cascader-menu:empty) {
  display: none;
  width: 0;
  min-width: 0;
  border: none;
  padding: 0;
  margin: 0;
}

/* 有内容的列显示并设置最小宽度 */
.material-cascader-dropdown :deep(.el-cascader-menu:not(:empty)) {
  display: block;
}

/* 第二列及后续列有内容时设置最小宽度 */
.material-cascader-dropdown :deep(.el-cascader-menu:nth-child(n+2):not(:empty)) {
  min-width: 200px;
}

/* 菜单项文本不换行，完整显示 */
.material-cascader-dropdown :deep(.el-cascader-menu__item) {
  white-space: nowrap;
  overflow: visible;
  padding-right: 20px;
  padding-left: 20px;
}

/* 最后一列不显示右边框 */
.material-cascader-dropdown :deep(.el-cascader-menu:last-child:not(:empty)) {
  border-right: none;
}

/* 当下一列为空时，当前列也不显示右边框 */
.material-cascader-dropdown :deep(.el-cascader-menu:not(:empty) + .el-cascader-menu:empty) {
  display: none;
}

/* 烫金类型选择器样式 */
.stamping-selector-wrapper {
  position: relative;
}

.stamping-selector-input {
  cursor: pointer;
}

.stamping-selector-input :deep(.el-input__inner) {
  cursor: pointer;
}

.stamping-cascader-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 2000;
  margin-top: 4px;
  background: white;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  box-shadow: var(--el-box-shadow-light);
  overflow: visible;
  width: max-content;
  min-width: 200px;
}

/* Cascader 面板样式 - 根据内容自适应宽度 */
.stamping-cascader-dropdown :deep(.el-cascader-panel) {
  border: none;
  box-shadow: none;
  background: transparent;
  width: max-content;
  display: flex;
}

/* 所有菜单列根据内容自适应宽度 */
.stamping-cascader-dropdown :deep(.el-cascader-menu) {
  width: max-content;
  min-width: 150px;
  flex-shrink: 0;
}

/* 有内容的列显示右边框 */
.stamping-cascader-dropdown :deep(.el-cascader-menu:not(:empty)) {
  border-right: 1px solid var(--el-border-color-lighter);
}

/* 第一列（主选项列）根据内容自适应 */
.stamping-cascader-dropdown :deep(.el-cascader-menu:first-child) {
  width: max-content;
  min-width: 150px;
}

/* 第二列及后续列根据内容自适应，空列隐藏 */
.stamping-cascader-dropdown :deep(.el-cascader-menu:nth-child(n+2)) {
  width: max-content;
  min-width: 0;
}

/* 空的列不显示 */
.stamping-cascader-dropdown :deep(.el-cascader-menu:empty) {
  display: none;
  width: 0;
  min-width: 0;
  border: none;
  padding: 0;
  margin: 0;
}

/* 有内容的列显示并设置最小宽度 */
.stamping-cascader-dropdown :deep(.el-cascader-menu:not(:empty)) {
  display: block;
}

/* 第二列及后续列有内容时设置最小宽度 */
.stamping-cascader-dropdown :deep(.el-cascader-menu:nth-child(n+2):not(:empty)) {
  min-width: 200px;
}

/* 菜单项文本不换行，完整显示 */
.stamping-cascader-dropdown :deep(.el-cascader-menu__item) {
  white-space: nowrap;
  overflow: visible;
  padding-right: 20px;
  padding-left: 20px;
}

/* 最后一列不显示右边框 */
.stamping-cascader-dropdown :deep(.el-cascader-menu:last-child:not(:empty)) {
  border-right: none;
}

/* 当下一列为空时，当前列也不显示右边框 */
.stamping-cascader-dropdown :deep(.el-cascader-menu:not(:empty) + .el-cascader-menu:empty) {
  display: none;
}

/* 燙後加工選項选择器样式 */
.postprocessing-selector-wrapper {
  position: relative;
}

.postprocessing-selector-input {
  cursor: pointer;
}

.postprocessing-selector-input :deep(.el-input__inner) {
  cursor: pointer;
}

.postprocessing-cascader-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 2000;
  margin-top: 4px;
  background: white;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  box-shadow: var(--el-box-shadow-light);
  overflow: visible;
  width: max-content;
  min-width: 200px;
}

/* Cascader 面板样式 - 根据内容自适应宽度 */
.postprocessing-cascader-dropdown :deep(.el-cascader-panel) {
  border: none;
  box-shadow: none;
  background: transparent;
  width: max-content;
  display: flex;
}

/* 所有菜单列根据内容自适应宽度 */
.postprocessing-cascader-dropdown :deep(.el-cascader-menu) {
  width: max-content;
  min-width: 150px;
  flex-shrink: 0;
}

/* 有内容的列显示右边框 */
.postprocessing-cascader-dropdown :deep(.el-cascader-menu:not(:empty)) {
  border-right: 1px solid var(--el-border-color-lighter);
}

/* 第一列（主选项列）根据内容自适应 */
.postprocessing-cascader-dropdown :deep(.el-cascader-menu:first-child) {
  width: max-content;
  min-width: 150px;
}

/* 第二列及后续列根据内容自适应，空列隐藏 */
.postprocessing-cascader-dropdown :deep(.el-cascader-menu:nth-child(n+2)) {
  width: max-content;
  min-width: 0;
}

/* 空的列不显示 */
.postprocessing-cascader-dropdown :deep(.el-cascader-menu:empty) {
  display: none;
  width: 0;
  min-width: 0;
  border: none;
  padding: 0;
  margin: 0;
}

/* 有内容的列显示并设置最小宽度 */
.postprocessing-cascader-dropdown :deep(.el-cascader-menu:not(:empty)) {
  display: block;
}

/* 第二列及后续列有内容时设置最小宽度 */
.postprocessing-cascader-dropdown :deep(.el-cascader-menu:nth-child(n+2):not(:empty)) {
  min-width: 200px;
}

/* 菜单项文本不换行，完整显示 */
.postprocessing-cascader-dropdown :deep(.el-cascader-menu__item) {
  white-space: nowrap;
  overflow: visible;
  padding-right: 20px;
  padding-left: 20px;
}

/* 最后一列不显示右边框 */
.postprocessing-cascader-dropdown :deep(.el-cascader-menu:last-child:not(:empty)) {
  border-right: none;
}

/* 当下一列为空时，当前列也不显示右边框 */
.postprocessing-cascader-dropdown :deep(.el-cascader-menu:not(:empty) + .el-cascader-menu:empty) {
  display: none;
}

/* 通用图标样式 */
.arrow-icon {
  font-size: 14px;
  color: var(--el-text-color-placeholder);
  transition: transform 0.3s;
}

.arrow-icon.is-reverse {
  transform: rotate(180deg);
}

.search-icon {
  font-size: 14px;
  color: var(--el-text-color-placeholder);
}
</style>

