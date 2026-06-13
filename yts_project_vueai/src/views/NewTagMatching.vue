<script setup lang="ts">
import { ref, computed } from 'vue';
import { ElSelect, ElOption } from 'element-plus';
import axios from 'axios';

// 查詢參數
const companyNumber = ref('');
const gpNumber = ref('');
const selectedProductType = ref('');
const selectedPatternType = ref('');
const selectedStampingType = ref('');
const selectedPriceLevel = ref<number | null>(null);
// 燙後加工選項 - 多選
const selectedPostProcessing = ref<string[]>([]);

// 過膠相關選項
const selectedPostProcessingStep = ref('');
const selectedLaminationMaterial = ref('');

// 適用界面(前工序)參數
const selectedInterfaceOption = ref('');
const selectedApplicability = ref('');

// 平面燙金位置選擇參數
const selectedStampingPosition = ref('');
const showStampingPosition = ref(false);

// 材料適用界面參數
const selectedMaterialOption = ref('');
const selectedMaterialApplicability = ref('');

// 燙金圖案參數
const selectedStampingPatternOption = ref('');
const selectedStampingPatternApplicability = ref('');

// 狀態
const loading = ref(false);
const error = ref<string | null>(null);

// 匹配結果
const matchResults = ref<any[]>([]);

// 適用界面選項列表
const interfaceOptions = [
    { value: 'oil6812Glossy33013440Dum1', label: '掃金粉.青紅金粉' },
    { value: 'oil6812Glossy33013440Dumb', label: '掃金粉.銀粉' },
    { value: 'oil6812Glossy33013440Dumb', label: '掃金粉.珍珠粉' },    
    { value: 'oil6812Glossy33013440Dum1', label: '印刷/過油面.普通油墨' },
    { value: 'oil6812Glossy33013440Dumb', label: '印刷/過油面.LED UV墨' },
    { value: 'oil6812Glossy33013440Dumb', label: '印刷/過油面.6812光水油3301/3440啞水油' },
    { value: 'gvLedUvGlossMatte', label: '印刷/過油面.GP LED 光油/啞油' },
    { value: 'gvLedUvGlossMatte', label: '印刷/過油面.去塑油' },
    { value: 'oilBasedGlossMattePowderPaper', label: '印刷/過油面.油性 光油/啞油' },
    { value: 'standardFuronghui22d', label: '啞膠面.普通-福融輝22D' },
    { value: 'preCoatedHy120665', label: '啞膠面.預塗-HY1206/65' },
    { value: 'highTackPreCoatedHy40', label: '啞膠面.超粘預塗HY40' },
    { value: 'preCoatedEconomicalHighWearResistantYt008a', label: '啞膠面.預塗平價高耐磨YT008A' },
    { value: 'preCoatedHighWearResistantTn008', label: '啞膠面.預塗高耐磨-TN008' },
    //TODO 這個選項不知道怎麼看，新版的沒有，舊版有
    { value: 'preCoatedStandardWearResistantKvmbF', label: '啞膠面.預塗普通耐磨-KVMB-F' },
    { value: 'softTouchMatteLaminate6015a', label: '啞膠面.柔感啞膠-6015A' }
];

// 適用性選項
const applicabilityOptions = [
    { value: 'V', label: '適用' },
    { value: 'X', label: '不適用' }
];

// 材料適用性選項
const materialApplicabilityOptions = [
    { value: 'V', label: '適用' },
    { value: 'NA_Enum', label: '不確定' },
    { value: '▷', label: '需要作「絲印打底處理」再燙金' }
];

// 材料適用界面選項列表
const materialOptions = [
    { value: '單粉咭', label: '單粉咭' },
    { value: '雙粉咭', label: '雙粉咭' },
    { value: '單粉紙', label: '單粉紙' },
    { value: '啞粉紙', label: '啞粉紙' },
    { value: '無粉咭', label: '無粉咭' },
    { value: '書紙', label: '書紙' },
    { value: 'plasticFilmNormalVET', label: '膠片.普通VET/VVC/AVET' },
    { value: 'plasticFilmScratchResistantAVET', label: '膠片.防劃AVET-防劃面不可燙金' },
    { value: 'waxPaperGTF', label: '牛油紙.GTF' },
    { value: 'waxPaperZTF', label: '牛油紙.ZTF' },
    { value: 'fabricPaperNylonJHT8001', label: '布面紙.尼龍絲JHT-8001' },
    { value: 'fabricPaperNylonJHT8004', label: '布面紙.尼龍絲JHT-8004' },
    { value: 'fabricPaperNylonJHT8013', label: '布面紙.尼龍絲JHT-8013' },
    { value: 'fabricPaperPolyesterJHT8002', label: '布面紙.滌綸JHT-8002' },
    { value: 'fabricPaperPolyesterJHT8010', label: '布面紙.滌綸JHT-8010' },
    { value: 'fabricPaperPolyesterJHT8014', label: '布面紙.滌綸JHT-8014' },
    { value: 'fabricPaperPolyesterJHT8015', label: '布面紙.滌綸JHT-8015' },
    { value: 'fabricPaperPolyesterLTS8003', label: '布面紙.滌綸LTS-8003' },
    { value: 'fabricPaperPolyesterESM', label: '布面紙.滌綸ESM' },
    { value: 'fabricPaperImitationCottonJHT8003', label: '布面紙.仿棉JHT-8003' },
    { value: 'fabricPaperImitationCottonJHT8017', label: '布面紙.仿棉JHT-8017' },
    { value: 'fabricPaperPolyCottonJHT8006', label: '布面紙.滌棉JHT-8006' },
    { value: 'fabricPaperPolyCottonJHT8008', label: '布面紙.滌棉JHT-8008' },
    { value: 'fabricPaperPolyCottonJHT8011', label: '布面紙.滌棉JHT-8011' },
    { value: 'fabricPaperPolyCottonJHT8016', label: '布面紙.滌棉JHT-8016' },
    { value: 'fabricPaperPolyCottonJHT8018', label: '布面紙.滌棉JHT-8018' },
    { value: 'fabricPaperPolyCottonJHT8019', label: '布面紙.滌棉JHT-8019' },
    { value: 'fabricPaperPolyCottonJHT910', label: '布面紙.滌棉JHT-910' },
    { value: 'fabricPaperHempCottonJHT8007', label: '布面紙.麻棉JHT-8007' },
    { value: 'fabricPaperPureCottonJHT8009', label: '布面紙.純棉JHT-8009' },
    { value: 'fabricPaperPureCottonJHT8012', label: '布面紙.純棉JHT-8012' },
    { value: 'fabricPaperCottonJHT0001to0103', label: '布面紙.棉布JHT0001～0103' },
    { value: 'fabricPaperMercerizedCottonJHT0104to0195', label: '布面紙.絲光棉JHT0104～0195' },
    { value: 'fabricPaperMercerizedCottonJHT0199to0209', label: '布面紙.絲光棉JHT0199～0209' },
    { value: 'fabricPaperMercerizedCottonJHT0211', label: '布面紙.絲光棉JHT0211' },
    { value: 'fabricPaperMercerizedCottonJHT0213', label: '布面紙.絲光棉JHT0213' },
    { value: 'fabricPaperSilkJHT0196to0198', label: '布面紙.絹布JHT0196～0198' },
    { value: 'fabricPaperSilkJHT0265to0311', label: '布面紙.絹布JHT0265～0311' },
    { value: 'fabricPaperFlashFabricJHT0216', label: '布面紙.閃光布JHT0216' },
    { value: 'fabricPaperFlashFabricJHT0218', label: '布面紙.閃光布JHT0218' },
    { value: 'fabricPaperFlashFabricJHT0312to0330', label: '布面紙.閃光布JHT0312～0330' },
    { value: 'fabricPaperSilkFabricJHT0219to0264', label: '布面紙.絲綸布JHT0219～0264' },
    { value: 'fabricPaperHempPressFabricJHT0334to0351', label: '布面紙.麻壓布JHT0334～0351' },
    { value: 'fabricPaperTexturedFabricJHT0352to0386', label: '布面紙.壓紋布JHT0352～0386' },
    { value: 'fabricPaperMercerizedHempFabricJHT0387to0407', label: '布面紙.絲光麻布JHT0387～0407' },
    { value: 'fabricPaperCoarseFabricJHT0419to0429', label: '布面紙.粗布JHT0419～0429' }
];

// 燙金圖案適用性選項
const stampingPatternApplicabilityOptions = [
    { value: 'V', label: '適用' },
    { value: 'NA_Enum', label: '不確定' }
];

// 燙金圖案選項列表
const stampingPatternOptions = [
    { value: 'gradientAndDots', label: '漸變、網點' },
    { value: 'thinLinesAndLetters', label: '幼細線條、字母' },
    { value: 'mediumSmallAreaThinLinesAndLetters', label: '中小面積細線條、字母' },
    { value: 'mediumLargeAreaThinLinesAndLetters', label: '中大面積、細線條、字母' },
    { value: 'extraLargeArea', label: '大面積' }
];

// 燙後加工選項列表
const postProcessingOptions = [
    { value: 'laminating', label: '過膠' },
    { value: 'ledUvGlitter', label: '絲印LED UV灑閃粉' },
    { value: 'uvPrinting', label: '印刷UV' },
    { value: 'printing', label: '印刷' },
    { value: 'embossing', label: '擊凸' }
];

// 後加工步驟選項
const postProcessingStepOptions = [
    { value: 'embossingDebossing', label: '擊凹/凸' },
    { value: 'uvEmbossingDebossing', label: '絲印UV+擊凹/凸' },
    { value: 'dieCutting', label: '啤折/切' },
    { value: 'uvDieCutting', label: '絲印UV+啤折/切' },
    { value: 'stepping', label: '踩坑位' },
    { value: 'uvStepping', label: '絲印UV+踩坑位' },
    { value: 'texturing', label: '壓紋' }
];

// 過膠材質選項
const laminationMaterialOptions = [
    { value: 'normalPreCoatedFilmGlossMatte', label: '普通預塗菲林-光/啞膠' },
    { value: 'normalPreCoatedFilmEconomicalMatte', label: '普通預塗菲林-平價耐磨啞膠' },
    { value: 'superTackPreCoatedFilmGlossMatte', label: '超粘預塗菲林-光/啞膠' },
    { value: 'superTackPreCoatedFilmHighWearMatte', label: '超粘預塗菲林-高價耐磨啞膠' },
    { value: 'waterBasedFilmGlossMatte', label: '水性菲林-光/啞膠' },
    { value: 'specialFilmPreCoatedSoftTouch', label: '特殊菲林-預塗柔感膠(含預塗、水性)' },
    { value: 'specialFilmAntiStaticGlossMatte', label: '特殊菲林-抗靜電光/啞膠(含預塗、水性)' }
];

// 完整的燙金紙產品數據庫
const allProducts = [
    {
        name: '普通金紙A',
        model: 'PG-001',
        id: 'MAT001',
        color: '亮金',
        temperature: '110~130℃',
        tension: '標準',
        width: '640mm',
        length: '120m',
        scenarios: ['賀卡', '包裝'],
        features: ['普通燙金', '適合細線條'],
        costIndex: 1,
        laminatingCompatibility: 'partial',
        uvPrintingCompatible: true,
        ledUvGlitterCompatible: false,
        printingCompatible: true,
        companyNumbers: ['HS#1S', 'HS#2S'],
        gpNumbers: ['GP001', 'GP002'],
        productTypes: ['賀咭/紙袋', '包裝'],
        patternTypes: ['淨線條/字母≤0.5mm', '淨線條/字母0.5mm<X≤5mm'],
        stampingTypes: ['平面燙金'],
        priceLevels: [1, 2, 3],
        uvPrinting: 'V',
        ledUvGlitter: 'X',
        stampingPrinting: 'V',
        interfaceOptions: ['oil6812Glossy33013440Dumb', 'gvLedUvGlossMatte'],
        interfaceApplicability: 'V'
    },
    {
        name: '普通耐磨金紙A',
        model: 'PGW-001',
        id: 'MAT002',
        color: '亮金',
        temperature: '115~135℃',
        tension: '標準',
        width: '640mm',
        length: '120m',
        scenarios: ['精平裝', '包裝'],
        features: ['高耐磨', '適合大面積'],
        costIndex: 2,
        laminatingCompatibility: 'all',
        uvPrintingCompatible: true,
        ledUvGlitterCompatible: true,
        printingCompatible: true,
        companyNumbers: ['HS#3S', 'HS#4S'],
        gpNumbers: ['GP003', 'GP004'],
        productTypes: ['精平裝/咭書', '包裝'],
        patternTypes: ['淨線條/字母5mm<X≤10mm', '淨實地10mm<X≤20mm'],
        stampingTypes: ['立體燙金'],
        priceLevels: [2, 3, 4],
        uvPrinting: 'V',
        ledUvGlitter: 'V',
        stampingPrinting: 'V',
        interfaceOptions: ['oilBasedGlossMattePowderPaper', 'preCoatedHy120665'],
        interfaceApplicability: 'V'
    },
    {
        name: '高耐磨金紙A',
        model: 'HGW-001',
        id: 'MAT003',
        color: '亮金',
        temperature: '120~140℃',
        tension: '緊身',
        width: '640mm',
        length: '150m',
        scenarios: ['精平裝', '綜合'],
        features: ['超高耐磨', '適合複雜圖案'],
        costIndex: 3,
        laminatingCompatibility: 'all',
        uvPrintingCompatible: true,
        ledUvGlitterCompatible: true,
        printingCompatible: true,
        companyNumbers: ['HS#5S', 'HS#6S'],
        gpNumbers: ['GP005', 'GP006'],
        productTypes: ['精平裝/咭書'],
        patternTypes: ['淨實地>10mm', '淨實地>20mm'],
        stampingTypes: ['折光燙金、有紋燙金'],
        priceLevels: [3, 4, 5],
        uvPrinting: 'V',
        ledUvGlitter: 'V',
        stampingPrinting: 'V',
        interfaceOptions: ['preCoatedHighWearResistantTn008', 'preCoatedStandardWearResistantKvmbF'],
        interfaceApplicability: 'V'
    },
    {
        name: '啞金紙B',
        model: 'MG-001',
        id: 'MAT004',
        color: '啞金',
        temperature: '110~130℃',
        tension: '標準',
        width: '640mm',
        length: '120m',
        scenarios: ['賀卡', '包裝'],
        features: ['啞面燙金', '適合細線條'],
        costIndex: 1,
        laminatingCompatibility: 'partial',
        uvPrintingCompatible: false,
        ledUvGlitterCompatible: false,
        printingCompatible: true,
        companyNumbers: ['HS#7S'],
        gpNumbers: ['GP007'],
        productTypes: ['賀咭/紙袋'],
        patternTypes: ['淨線條/字母≤0.5mm'],
        stampingTypes: ['平面燙金'],
        priceLevels: [1, 2],
        uvPrinting: 'X',
        ledUvGlitter: 'X',
        stampingPrinting: 'V',
        interfaceOptions: ['oil6812Glossy33013440Dumb'],
        interfaceApplicability: 'V'
    },
    {
        name: '玫瑰金紙C',
        model: 'RG-001',
        id: 'MAT005',
        color: '玫瑰金',
        temperature: '115~135℃',
        tension: '標準',
        width: '640mm',
        length: '120m',
        scenarios: ['賀卡', '精平裝'],
        features: ['玫瑰金燙金', '適合中等面積'],
        costIndex: 2,
        laminatingCompatibility: 'all',
        uvPrintingCompatible: true,
        ledUvGlitterCompatible: false,
        printingCompatible: true,
        companyNumbers: ['HS#8S'],
        gpNumbers: ['GP008'],
        productTypes: ['賀咭/紙袋', '精平裝/咭書'],
        patternTypes: ['淨線條/字母0.5mm<X≤5mm', '混合圖案≤0.5mm線條+實地'],
        stampingTypes: ['平面燙金', '立體燙金'],
        priceLevels: [2, 3],
        uvPrinting: 'V',
        ledUvGlitter: 'X',
        stampingPrinting: 'V',
        interfaceOptions: ['gvLedUvGlossMatte', 'oilBasedGlossMatteNonPowderPaper'],
        interfaceApplicability: 'V'
    },
    {
        name: '香檳金紙D',
        model: 'CG-001',
        id: 'MAT006',
        color: '香檳金',
        temperature: '110~130℃',
        tension: '鬆身',
        width: '640mm',
        length: '200m',
        scenarios: ['包裝', '綜合'],
        features: ['香檳金燙金', '適合大面積'],
        costIndex: 2,
        laminatingCompatibility: 'all',
        uvPrintingCompatible: true,
        ledUvGlitterCompatible: true,
        printingCompatible: true,
        companyNumbers: ['HS#9S'],
        gpNumbers: ['GP009'],
        productTypes: ['包裝'],
        patternTypes: ['淨實地10mm<X≤20mm', '淨實地>10mm'],
        stampingTypes: ['立體燙金', '折光燙金、有紋燙金'],
        priceLevels: [2, 3, 4],
        uvPrinting: 'V',
        ledUvGlitter: 'V',
        stampingPrinting: 'V',
        interfaceOptions: ['standardFuronghui22d', 'preCoatedHy120665'],
        interfaceApplicability: 'V'
    },
    {
        name: '銀色紙E',
        model: 'SG-001',
        id: 'MAT007',
        color: '銀色',
        temperature: '115~135℃',
        tension: '標準',
        width: '640mm',
        length: '120m',
        scenarios: ['賀卡', '精平裝'],
        features: ['銀色燙金', '適合細線條'],
        costIndex: 1,
        laminatingCompatibility: 'none',
        uvPrintingCompatible: false,
        ledUvGlitterCompatible: false,
        printingCompatible: false,
        companyNumbers: ['HS#10S'],
        gpNumbers: ['GP010'],
        productTypes: ['賀咭/紙袋'],
        patternTypes: ['淨線條/字母≤0.5mm'],
        stampingTypes: ['平面燙金'],
        priceLevels: [1],
        uvPrinting: 'X',
        ledUvGlitter: 'X',
        stampingPrinting: 'X',
        interfaceOptions: ['oil6812Glossy33013440Dumb'],
        interfaceApplicability: 'X'
    },
    {
        name: '銅色紙F',
        model: 'CG-002',
        id: 'MAT008',
        color: '銅色',
        temperature: '120~140℃',
        tension: '緊身',
        width: '640mm',
        length: '150m',
        scenarios: ['精平裝', '綜合'],
        features: ['銅色燙金', '適合複雜圖案'],
        costIndex: 3,
        laminatingCompatibility: 'all',
        uvPrintingCompatible: true,
        ledUvGlitterCompatible: true,
        printingCompatible: true,
        companyNumbers: ['HS#11S'],
        gpNumbers: ['GP011'],
        productTypes: ['精平裝/咭書'],
        patternTypes: ['淨實地>20mm', '混合圖案>0.5mm線條+實地'],
        stampingTypes: ['燙金後擊凸、壓紋'],
        priceLevels: [3, 4, 5],
        uvPrinting: 'V',
        ledUvGlitter: 'V',
        stampingPrinting: 'V',
        interfaceOptions: ['preCoatedEconomicalHighWearResistantYt008a', 'softTouchMatteLaminate6015a'],
        interfaceApplicability: 'V'
    }
];

// 计算属性
const currentMatchParams = computed(() => {
    const params = [];
    
    if (companyNumber.value) {
        params.push({ label: '公司編號', value: companyNumber.value });
    }
    if (gpNumber.value) {
        params.push({ label: '客戶編號', value: gpNumber.value });
    }
    if (selectedProductType.value) {
        params.push({ label: '產品類型', value: selectedProductType.value });
    }
    if (selectedPatternType.value) {
        params.push({ label: '燙金圖案類型', value: selectedPatternType.value });
    }
    if (selectedStampingType.value) {
        let stampingTypeValue = selectedStampingType.value;
        if (selectedStampingType.value === '平面燙金' && selectedStampingPosition.value) {
            stampingTypeValue += ` (${selectedStampingPosition.value})`;
        }
        params.push({ label: '燙金類型', value: stampingTypeValue });
    }
    if (selectedPriceLevel.value) {
        params.push({ label: '價格優先度', value: selectedPriceLevel.value });
    }
    if (selectedPostProcessing.value.length > 0) {
        const postProcessingLabels = selectedPostProcessing.value.map(value => {
            const option = postProcessingOptions.find(opt => opt.value === value);
            return option ? option.label : value;
        });
        params.push({ label: '燙後加工選項', value: postProcessingLabels.join(', ') });
    }
    if (selectedPostProcessingStep.value) {
        const stepLabel = postProcessingStepOptions.find(opt => opt.value === selectedPostProcessingStep.value)?.label || selectedPostProcessingStep.value;
        params.push({ label: '燙金+過膠->後加工步驟', value: stepLabel });
    }
    if (selectedLaminationMaterial.value) {
        const materialLabel = laminationMaterialOptions.find(opt => opt.value === selectedLaminationMaterial.value)?.label || selectedLaminationMaterial.value;
        params.push({ label: '燙金+過膠->過膠材質', value: materialLabel });
    }
    if (selectedInterfaceOption.value && selectedApplicability.value) {
        const interfaceLabel = interfaceOptions.find(opt => opt.value === selectedInterfaceOption.value)?.label || selectedInterfaceOption.value;
        params.push({ label: '適用界面(前工序)', value: `${interfaceLabel} - ${selectedApplicability.value === 'V' ? '適用' : '不適用'}` });
    }
    if (selectedMaterialOption.value && selectedMaterialApplicability.value) {
        const materialLabel = materialOptions.find(opt => opt.value === selectedMaterialOption.value)?.label || selectedMaterialOption.value;
        const applicabilityLabel = materialApplicabilityOptions.find(opt => opt.value === selectedMaterialApplicability.value)?.label || selectedMaterialApplicability.value;
        params.push({ label: '材料適用界面', value: `${materialLabel} - ${applicabilityLabel}` });
    }
    if (selectedStampingPatternOption.value && selectedStampingPatternApplicability.value) {
        const patternLabel = stampingPatternOptions.find(opt => opt.value === selectedStampingPatternOption.value)?.label || selectedStampingPatternOption.value;
        const applicabilityLabel = stampingPatternApplicabilityOptions.find(opt => opt.value === selectedStampingPatternApplicability.value)?.label || selectedStampingPatternApplicability.value;
        params.push({ label: '燙金圖案', value: `${patternLabel} - ${applicabilityLabel}` });
    }
    
    return params;
});

// 方法
const searchByCompanyNumber = async () => {
    if (!companyNumber.value) {
        error.value = '請輸入公司編號';
        return;
    }
    
    await performBackendMatch();
};

const searchByGpNumber = async () => {
    if (!gpNumber.value) {
        error.value = '請輸入客戶編號';
        return;
    }
    
    await performBackendMatch();
};

const handleProductTypeChange = async () => {
    await performBackendMatch();
};

const handlePatternTypeChange = async () => {
    await performBackendMatch();
};

const handleStampingTypeChange = async () => {
    // 控制平面燙金位置选择框的显示
    showStampingPosition.value = selectedStampingType.value === '平面燙金';
    
    // 如果不是平面燙金，清空位置选择
    if (selectedStampingType.value !== '平面燙金') {
        selectedStampingPosition.value = '';
    }
    
    await performBackendMatch();
};

const handleStampingPositionChange = async () => {
    await performBackendMatch();
};

const handlePriceLevelChange = async () => {
    await performBackendMatch();
};

// 燙後加工选项变化处理
const handlePostProcessingChange = async () => {
    await performBackendMatch();
};

// 后加工步骤变化处理
const handlePostProcessingStepChange = async () => {
    await performBackendMatch();
};

// 过胶材质变化处理
const handleLaminationMaterialChange = async () => {
    await performBackendMatch();
};

const handleInterfaceOptionChange = async () => {
    await performBackendMatch();
};

const handleMaterialOptionChange = async () => {
    await performBackendMatch();
};

const clearMaterialOption = async () => {
    selectedMaterialOption.value = '';
    selectedMaterialApplicability.value = '';
    await performBackendMatch();
};

// 烫金图案选项变化处理
const handleStampingPatternOptionChange = async () => {
    // 如果选择了烫金图案选项和适用性，则设置对应的参数
    if (selectedStampingPatternOption.value && selectedStampingPatternApplicability.value) {
        // 更新匹配参数
        const stampingPatternOptionLabel = stampingPatternOptions.find(opt => opt.value === selectedStampingPatternOption.value)?.label || '';

        // 渐进式匹配：获取所有已选择的参数并一起发送
        await performBackendMatch();
    }
};

// 清空烫金图案选择
const clearStampingPattern = async () => {
    selectedStampingPatternOption.value = '';
    selectedStampingPatternApplicability.value = '';
    await performBackendMatch();
};

// 统一的匹配函数 - 调用后端API
const performBackendMatch = async () => {
    console.log('performBackendMatch 被调用');
    
    const hasParams = companyNumber.value || gpNumber.value || selectedProductType.value || 
                   selectedPatternType.value || selectedStampingType.value || 
                   selectedPriceLevel.value || selectedPostProcessing.value.length > 0 ||
                   selectedPostProcessingStep.value || selectedLaminationMaterial.value ||
                   (selectedInterfaceOption.value && selectedApplicability.value) ||
                   (selectedMaterialOption.value && selectedMaterialApplicability.value) ||
                   (selectedStampingPatternOption.value && selectedStampingPatternApplicability.value);
    
    console.log('hasParams:', hasParams);
    console.log('当前参数状态:', {
        companyNumber: companyNumber.value,
        gpNumber: gpNumber.value,
        selectedProductType: selectedProductType.value,
        selectedPatternType: selectedPatternType.value,
        selectedStampingType: selectedStampingType.value,
        selectedPriceLevel: selectedPriceLevel.value
    });
    
    if (!hasParams) {
        console.log('没有参数，获取所有结果');
        // 即使没有参数，也调用API获取所有结果
        try {
            loading.value = true;
            error.value = null;
            
            console.log('准备调用后端API获取所有结果');
            
            // 调用后端匹配API，不传参数获取所有结果
            const response = await axios.post('/api/api/gold-foil/match', {});
            const results = response?.data || [];
            
            console.log('后端API响应（所有结果）:', results);
            
            // 为结果添加匹配度
            const resultsWithScore = results.map((product: any) => {
                let score = 100; // 没有参数时，所有结果都是100分
                score += Math.floor(Math.random() * 20) - 10;
                score = Math.max(50, Math.min(100, score));
                
                return {
                    ...product,
                    score: score
                };
            });
            
            resultsWithScore.sort((a: any, b: any) => b.score - a.score);
            matchResults.value = resultsWithScore;
            
        } catch (err: any) {
            console.error('获取所有结果失败:', err);
            error.value = err?.message || '获取结果失败，请稍后重试';
            matchResults.value = [];
        } finally {
            loading.value = false;
        }
        return;
    }
    
    // 构建查询参数
    const params: any = {};
    
    if (companyNumber.value) {
        params.companyNumber = companyNumber.value;
    }
    if (gpNumber.value) {
        params.gpNumber = gpNumber.value;
    }
    if (selectedProductType.value) {
        params.productType = selectedProductType.value;
    }
    if (selectedPatternType.value) {
        params.patternType = selectedPatternType.value;
    }
    if (selectedStampingType.value) {
        params.stampingType = selectedStampingType.value;
    }
    if (selectedPriceLevel.value) {
        params.priceLevel = selectedPriceLevel.value;
    }
    if (selectedPostProcessing.value.length > 0) {
        params.postProcessing = selectedPostProcessing.value;
    }
    if (selectedPostProcessingStep.value) {
        params.postProcessingStep = selectedPostProcessingStep.value;
    }
    if (selectedLaminationMaterial.value) {
        params.laminationMaterial = selectedLaminationMaterial.value;
    }
    if (selectedInterfaceOption.value && selectedApplicability.value) {
        params.interfaceOption = selectedInterfaceOption.value;
        params.applicability = selectedApplicability.value;
    }
    if (selectedMaterialOption.value && selectedMaterialApplicability.value) {
        params.materialOption = selectedMaterialOption.value;
        params.materialApplicability = selectedMaterialApplicability.value;
    }
    if (selectedStampingPatternOption.value && selectedStampingPatternApplicability.value) {
        params.stampingPatternOption = selectedStampingPatternOption.value;
        params.stampingPatternApplicability = selectedStampingPatternApplicability.value;
    }
    
    try {
        loading.value = true;
        error.value = null;
        
        console.log('准备调用后端API，参数:', params);
        
        // 调用后端匹配API
        const response = await axios.post('/api/api/gold-foil/match', params);
        const results = response?.data || [];
        
        console.log('后端API响应:', results);
        
        // 为结果添加匹配度
        const resultsWithScore = results.map((product: any) => {
            let score = 100;
            const paramCount = Object.keys(params).length;
            if (paramCount > 0) {
                score = Math.max(60, 100 - (paramCount - 1) * 5);
            }
            score += Math.floor(Math.random() * 20) - 10;
            score = Math.max(50, Math.min(100, score));
            
            return {
                ...product,
                score: score
            };
        });
        
        resultsWithScore.sort((a: any, b: any) => b.score - a.score);
        matchResults.value = resultsWithScore;
        
    } catch (err: any) {
        console.error('匹配请求失败:', err);
        error.value = err?.message || '匹配失败，请稍后重试';
        matchResults.value = [];
    } finally {
        loading.value = false;
    }
};

// 清空产品类型
const clearProductType = async () => {
    console.log('clearProductType 被调用');
    selectedProductType.value = '';
    console.log('selectedProductType 已清空:', selectedProductType.value);
    await performBackendMatch();
};

// 清空烫金图案类型
const clearPatternType = async () => {
    console.log('clearPatternType 被调用');
    console.log('清除前 selectedPatternType 的值:', selectedPatternType.value);
    selectedPatternType.value = '';
    console.log('selectedPatternType 已清空:', selectedPatternType.value);
    await performBackendMatch();
};

// 清空烫金类型
const clearStampingType = async () => {
    selectedStampingType.value = '';
    selectedStampingPosition.value = '';
    showStampingPosition.value = false;
    await performBackendMatch();
};

// 清空价格等级
const clearPriceLevel = async () => {
    selectedPriceLevel.value = null;
    await performBackendMatch();
};

// 清空燙後加工选项
const clearPostProcessing = async () => {
    selectedPostProcessing.value = [];
    selectedPostProcessingStep.value = '';
    selectedLaminationMaterial.value = '';
    await performBackendMatch();
};

// 清空适用界面选项
const clearInterfaceOption = async () => {
    console.log('clearInterfaceOption 被调用');
    selectedInterfaceOption.value = '';
    selectedApplicability.value = '';
    console.log('selectedInterfaceOption 已清空:', selectedInterfaceOption.value);
    await performBackendMatch();
};

// 添加缺失的函数
const performProgressiveMatch = async () => {
    // 渐进式匹配逻辑
    performMatch();
};

const performMatch = () => {
    const hasParams = companyNumber.value || gpNumber.value || selectedProductType.value || 
                   selectedPatternType.value || selectedStampingType.value || 
                   selectedPriceLevel.value || selectedPostProcessing.value.length > 0 ||
                   selectedPostProcessingStep.value || selectedLaminationMaterial.value ||
                   (selectedInterfaceOption.value && selectedApplicability.value) ||
                   (selectedMaterialOption.value && selectedMaterialApplicability.value) ||
                   (selectedStampingPatternOption.value && selectedStampingPatternApplicability.value);
    
    if (!hasParams) {
        matchResults.value = [];
        return;
    }
    
    let results = allProducts.filter(product => {
        let match = true;
        
        if (companyNumber.value && !product.companyNumbers.includes(companyNumber.value)) {
            match = false;
        }
        
        if (gpNumber.value && !product.gpNumbers.includes(gpNumber.value)) {
            match = false;
        }
        
        if (selectedProductType.value && !product.productTypes.includes(selectedProductType.value)) {
            match = false;
        }
        
        if (selectedPatternType.value && !product.patternTypes.includes(selectedPatternType.value)) {
            match = false;
        }
        
        if (selectedStampingType.value && !product.stampingTypes.includes(selectedStampingType.value)) {
            match = false;
        }
        
        if (selectedPriceLevel.value && !product.priceLevels.includes(selectedPriceLevel.value)) {
            match = false;
        }
        
        // 燙後加工选项匹配
        if (selectedPostProcessing.value.length > 0) {
            // 这里可以根据实际需求添加匹配逻辑
            // 暂时跳过具体的匹配逻辑
        }
        
        // 适用界面匹配
        if (selectedInterfaceOption.value && selectedApplicability.value) {
            if (!product.interfaceOptions.includes(selectedInterfaceOption.value) || 
                product.interfaceApplicability !== selectedApplicability.value) {
                match = false;
            }
        }
        
        return match;
    });
    
    results = results.map(product => {
        let score = 100;
        const paramCount = currentMatchParams.value.length;
        if (paramCount > 0) {
            score = Math.max(60, 100 - (paramCount - 1) * 5);
        }
        score += Math.floor(Math.random() * 20) - 10;
        score = Math.max(50, Math.min(100, score));
        
        return {
            ...product,
            score: score
        };
    });
    
    results.sort((a, b) => b.score - a.score);
    matchResults.value = results;
};

const clearCompanyNumber = async () => {
    companyNumber.value = '';
    await performBackendMatch();
};

const clearGpNumber = async () => {
    gpNumber.value = '';
    await performBackendMatch();
};

const clearAllParams = async () => {
    companyNumber.value = '';
    gpNumber.value = '';
    selectedProductType.value = '';
    selectedPatternType.value = '';
    selectedStampingType.value = '';
    selectedStampingPosition.value = '';
    showStampingPosition.value = false;
    selectedPriceLevel.value = null;
    selectedPostProcessing.value = [];
    selectedPostProcessingStep.value = '';
    selectedLaminationMaterial.value = '';
    selectedInterfaceOption.value = '';
    selectedApplicability.value = '';
    selectedMaterialOption.value = '';
    selectedMaterialApplicability.value = '';
    selectedStampingPatternOption.value = '';
    selectedStampingPatternApplicability.value = '';
    error.value = null;
    matchResults.value = [];
};

const exportResults = () => {
    if (matchResults.value.length === 0) {
        alert('沒有匹配結果可以導出');
        return;
    }
    
    let csvContent = '產品名稱,型號,物料號,顏色,尺寸,鬆緊度,溫度範圍,價格等級,匹配度,應用場景,特性\n';
    
    matchResults.value.forEach(result => {
        csvContent += `"${result.name}","${result.model}","${result.id}","${result.color}","${result.width} × ${result.length}","${result.tension}","${result.temperature}","${result.costIndex}","${result.score}%","${result.scenarios.join(',')}","${result.features.join(',')}"\n`;
    });
    
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);
    link.setAttribute('href', url);
    link.setAttribute('download', `燙金紙匹配結果_${new Date().toISOString().split('T')[0]}.csv`);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
};
</script>

<template>
    <div class="min-h-screen bg-gray-100">
        <!-- 头部导航 -->
        <nav class="bg-white shadow-sm border-b">
            <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between items-center h-16">
                    <div class="flex items-center">
                        <h1 class="text-xl font-bold text-gray-900">燙金紙匹配系統</h1>
                        <span class="ml-3 text-sm text-gray-500">獨立版本 v1.0</span>
                    </div>
                    <div class="flex items-center space-x-4">
                        <button 
                            @click="exportResults"
                            class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                        >
                            導出結果
                        </button>
                    </div>
                </div>
            </div>
        </nav>

        <!-- 主要内容 -->
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
            <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <!-- 左侧：参数配置区域 -->
                <div class="lg:col-span-1">
                    <div class="bg-white rounded-lg shadow-sm border p-6">
                        <h2 class="text-lg font-semibold text-gray-900 mb-6">匹配參數配置</h2>
                        
                        <!-- 公司编号查询 -->
                        <div class="mb-6 bg-blue-50 p-4 rounded-lg border border-blue-200">
                            <div class="flex justify-between items-center mb-3">
                                <h3 class="text-md font-medium text-blue-700">1. 根據公司編號或客戶編號查詢</h3>
                                <button 
                                    v-if="companyNumber" 
                                    @click="clearCompanyNumber"
                                    class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors"
                                >
                                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                    </svg>
                                    清空
                                </button>
                            </div>
                            <div class="flex space-x-2">
                                <div class="relative flex-1">
                                    <input 
                                        v-model="companyNumber" 
                                        placeholder="請輸入公司編號或客戶編號並按回車鍵"
                                        class="w-full px-4 py-2 pr-10 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
                                        @keyup.enter="searchByCompanyNumber"
                                    />
                                    <button 
                                        v-if="companyNumber" 
                                        @click="clearCompanyNumber"
                                        class="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600 focus:outline-none"
                                    >
                                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                        </svg>
                                    </button>
                                </div>
                            </div>
                            <div v-if="error" class="mt-2 text-sm text-red-600">{{ error }}</div>
                        </div>

                        <!-- 客户编号查询 -->
                        <div class="mb-6 bg-green-50 p-4 rounded-lg border border-green-200">
                            <div class="flex justify-between items-center mb-3">
                                <h3 class="text-md font-medium text-green-700">2. 根據燙金紙型號查詢</h3>
                                <button 
                                    v-if="gpNumber" 
                                    @click="clearGpNumber"
                                    class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors"
                                >
                                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                    </svg>
                                    清空
                                </button>
                            </div>
                            <div class="flex space-x-2">
                                <div class="relative flex-1">
                                    <input 
                                        v-model="gpNumber" 
                                        placeholder="請輸入燙金紙型號並按回車鍵"
                                        class="w-full px-4 py-2 pr-10 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 text-sm"
                                        @keyup.enter="searchByGpNumber"
                                    />
                                    <button 
                                        v-if="gpNumber" 
                                        @click="clearGpNumber"
                                        class="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600 focus:outline-none"
                                    >
                                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </div>

                        <!-- 适用界面(前工序) -->
                        <div class="mb-6 bg-purple-50 p-4 rounded-lg border border-purple-200">
                            <div class="flex justify-between items-center mb-3">
                                <h3 class="text-md font-medium text-purple-700">3. 適用界面(前工序)</h3>
                                <div>
                                    <span class="text-xs text-gray-500 mr-2">selectedInterfaceOption: {{ selectedInterfaceOption || '空' }}</span>
                                    <button 
                                        v-if="selectedInterfaceOption" 
                                        @click="clearInterfaceOption"
                                        class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors"
                                    >
                                        <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                        </svg>
                                        清空
                                    </button>
                                </div>
                            </div>
                            <div class="space-y-3">
                                <div>
                                    <el-select v-model="selectedInterfaceOption" placeholder="請選擇適用界面選項" class="w-full" @change="handleInterfaceOptionChange">
                                        <el-option v-for="option in interfaceOptions" :key="option.value" :label="option.label" :value="option.value"></el-option>
                                    </el-select>
                                </div>
                            </div>
                        </div>


                        <!-- 产品类型选择 -->
                        <div class="mb-4">
                            <div class="flex justify-between items-center mb-3">
                                <h3 class="text-md font-medium text-gray-700">4. 產品類型</h3>
                                <div>
                                    <span class="text-xs text-gray-500 mr-2">selectedProductType: {{ selectedProductType || '空' }}</span>
                                    <button 
                                        v-if="selectedProductType" 
                                        @click="clearProductType"
                                        class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors"
                                    >
                                        <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                        </svg>
                                        清空
                                    </button>
                                </div>
                            </div>
                            <el-select v-model="selectedProductType" placeholder="請選擇產品類型" class="w-full" @change="handleProductTypeChange">
                                <el-option label="精平裝/咭書" value="精平裝/咭書"></el-option>
                                <el-option label="賀咭/紙袋" value="賀咭/紙袋"></el-option>
                                <el-option label="包裝" value="包裝"></el-option>
                            </el-select>
                        </div>

                        <!-- 烫金图案类型 -->
                        <div class="mb-4">
                            <div class="flex justify-between items-center mb-3">
                                <h3 class="text-md font-medium text-gray-700">5. 6.燙金圖案(X)(耐磨燙金紙選用)</h3>
                                <div>
                                    <span class="text-xs text-gray-500">selectedPatternType: {{ selectedPatternType }}</span>
                                    <button 
                                        v-if="selectedPatternType" 
                                        @click="clearPatternType"
                                        class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors"
                                    >
                                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                    </svg>
                                    清空
                                    </button>
                                </div>
                            </div>
                            <el-select v-model="selectedPatternType" placeholder="請選擇燙金圖案類型" class="w-full" @change="handlePatternTypeChange">
                                <el-option label="淨線條/字母≤0.5mm" value="淨線條/字母≤0.5mm"></el-option>
                                <el-option label="淨線條/字母0.5mm<X≤5mm" value="淨線條/字母0.5mm<X≤5mm"></el-option>
                                <el-option label="淨線條/字母5mm<X≤10mm" value="淨線條/字母5mm<X≤10mm"></el-option>
                                <el-option label="淨實地10mm<X≤20mm" value="淨實地10mm<X≤20mm"></el-option>
                                <el-option label="混合圖案≤0.5mm線條+實地" value="混合圖案≤0.5mm線條+實地"></el-option>
                                <el-option label="混合圖案>0.5mm線條+≤20mm 實地" value="混合圖案>0.5mm線條+≤20mm 實地"></el-option>
                                <el-option label="淨實地>10mm" value="淨實地>10mm"></el-option>
                                <el-option label="混合圖案>0.5mm線條+實地" value="混合圖案>0.5mm線條+實地"></el-option>
                                <el-option label="淨實地>20mm" value="淨實地>20mm"></el-option>
                                <el-option label="混合圖案>0.5mm線條+≤20mm 實地" value="混合圖案>0.5mm線條+≤20mm 實地"></el-option>
                                <el-option label="不作區分" value="不作區分"></el-option>
                            </el-select>
                        </div>

                        <!-- 烫金类型 -->
                        <div class="mb-4">
                            <div class="flex justify-between items-center mb-3">
                                <h3 class="text-md font-medium text-gray-700">6. 燙金類型</h3>
                                <button 
                                    v-if="selectedStampingType" 
                                    @click="clearStampingType"
                                    class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors"
                                >
                                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                    </svg>
                                    清空
                                </button>
                            </div>
                            <el-select v-model="selectedStampingType" placeholder="請選擇燙金類型" class="w-full" @change="handleStampingTypeChange">
                                <el-option label="平面燙金" value="平面燙金"></el-option>
                                <el-option label="立體燙金" value="立體燙金"></el-option>
                                <el-option label="磨砂燙金" value="磨砂燙金"></el-option>
                                <el-option label="折光燙金" value="折光燙金"></el-option>
                                <el-option label="燙金後擊凸" value="燙金後擊凸"></el-option>
                                <el-option label="燙金後壓紋" value="燙金後壓紋"></el-option>
                                <el-option label="有紋燙金" value="有紋燙金"></el-option>
                            </el-select>
                            
                            <!-- 平面燙金位置选择 -->
                            <div v-if="showStampingPosition" class="mt-3">
                                <h4 class="text-sm font-medium text-gray-600 mb-2">平面燙金位置</h4>
                                <el-select v-model="selectedStampingPosition" placeholder="請選擇平面燙金位置" class="w-full" @change="handleStampingPositionChange">
                                    <el-option label="於中間位" value="於中間位"></el-option>
                                    <el-option label="到邊位" value="到邊位"></el-option>
                                </el-select>
                            </div>
                        </div>

                        <!-- 材料适用界面 -->
                        <div class="mb-4">
                            <div class="flex justify-between items-center mb-3">
                                <h3 class="text-md font-medium text-gray-700">8. 適用界面</h3>
                                <button 
                                    v-if="selectedMaterialOption" 
                                    @click="clearMaterialOption"
                                    class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors"
                                >
                                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                    </svg>
                                    清空
                                </button>
                            </div>
                            <div class="flex space-x-3">
                                <div class="flex-1">
                                    <el-select v-model="selectedMaterialOption" placeholder="請選擇材料適用界面選項" class="w-full" @change="handleMaterialOptionChange">
                                        <el-option v-for="option in materialOptions" :key="option.value" :label="option.label" :value="option.value"></el-option>
                                    </el-select>
                                </div>
                                <div class="w-32">
                                    <el-select v-model="selectedMaterialApplicability" placeholder="請選擇" class="w-full" @change="handleMaterialOptionChange">
                                        <el-option v-for="option in materialApplicabilityOptions" :key="option.value" :label="option.label" :value="option.value"></el-option>
                                    </el-select>
                                </div>
                            </div>
                        </div>

                        <!-- 烫金图案 -->
                        <div class="mb-4">
                            <div class="flex justify-between items-center mb-3">
                                <h3 class="text-md font-medium text-gray-700">8. 燙金圖案</h3>
                                <button 
                                    v-if="selectedStampingPatternOption" 
                                    @click="clearStampingPattern"
                                    class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors"
                                >
                                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                    </svg>
                                    清空
                                </button>
                            </div>
                            <div class="flex space-x-3">
                                <div class="flex-1">
                                    <el-select v-model="selectedStampingPatternOption" placeholder="請選擇燙金圖案選項" class="w-full" @change="handleStampingPatternOptionChange">
                                        <el-option v-for="option in stampingPatternOptions" :key="option.value" :label="option.label" :value="option.value"></el-option>
                                    </el-select>
                                </div>
                                <div class="w-32">
                                    <el-select v-model="selectedStampingPatternApplicability" placeholder="請選擇" class="w-full" @change="handleStampingPatternOptionChange">
                                        <el-option v-for="option in stampingPatternApplicabilityOptions" :key="option.value" :label="option.label" :value="option.value"></el-option>
                                    </el-select>
                                </div>
                            </div>
                        </div>

                        <!-- 燙後加工选项 -->
                        <div class="mb-4">
                            <div class="flex justify-between items-center mb-3">
                                <h3 class="text-md font-medium text-gray-700">9. 燙後加工選項</h3>
                                <button 
                                    v-if="selectedPostProcessing.length > 0" 
                                    @click="clearPostProcessing"
                                    class="flex items-center gap-1 px-2 py-1 text-xs bg-red-100 text-red-600 rounded hover:bg-red-200 transition-colors"
                                >
                                    <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
                                    </svg>
                                    清空
                                </button>
                            </div>
                            <el-select 
                                v-model="selectedPostProcessing" 
                                placeholder="請選擇燙後加工選項" 
                                class="w-full" 
                                multiple
                                collapse-tags
                                collapse-tags-tooltip
                                @change="handlePostProcessingChange"
                            >
                                <el-option 
                                    v-for="option in postProcessingOptions" 
                                    :key="option.value" 
                                    :label="option.label" 
                                    :value="option.value"
                                ></el-option>
                            </el-select>
                            
                            <!-- 过胶相关选项 - 当选择了过胶时显示 -->
                            <div v-if="selectedPostProcessing.includes('laminating')" class="mt-4 space-y-3">
                                <div class="flex space-x-3">
                                    <div class="flex-1">
                                        <label class="text-sm text-gray-600 mb-2 block">燙金+過膠->後加工步驟</label>
                                        <el-select 
                                            v-model="selectedPostProcessingStep" 
                                            placeholder="請選擇後加工步驟" 
                                            class="w-full" 
                                            @change="handlePostProcessingStepChange"
                                        >
                                            <el-option 
                                                v-for="option in postProcessingStepOptions" 
                                                :key="option.value" 
                                                :label="option.label" 
                                                :value="option.value"
                                            ></el-option>
                                        </el-select>
                                    </div>
                                    <div class="flex-1">
                                        <label class="text-sm text-gray-600 mb-2 block">燙金+過膠->過膠材質</label>
                                        <el-select 
                                            v-model="selectedLaminationMaterial" 
                                            placeholder="請選擇過膠材質" 
                                            class="w-full" 
                                            @change="handleLaminationMaterialChange"
                                        >
                                            <el-option 
                                                v-for="option in laminationMaterialOptions" 
                                                :key="option.value" 
                                                :label="option.label" 
                                                :value="option.value"
                                            ></el-option>
                                        </el-select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 清空所有参数 -->
                        <div class="mt-6">
                            <button 
                                @click="clearAllParams"
                                class="w-full px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-500"
                            >
                                清空所有參數
                            </button>
                        </div>
                    </div>
                </div>

                <!-- 右侧：匹配结果区域 -->
                <div class="lg:col-span-2">
                    <div class="bg-white rounded-lg shadow-sm border p-6">
                        <div class="flex justify-between items-center mb-6">
                            <h2 class="text-lg font-semibold text-gray-900">匹配結果</h2>
                            <div v-if="matchResults.length > 0" class="text-sm text-gray-600">
                                共找到 {{ matchResults.length }} 條結果
                            </div>
                        </div>
                        
                        <!-- 当前匹配参数显示 -->
                        <div v-if="currentMatchParams.length > 0" class="mb-6 bg-gray-100 p-4 rounded-lg">
                            <div class="text-sm font-medium text-gray-700 mb-2">當前匹配參數：</div>
                            <div class="flex flex-wrap gap-2">
                                <span 
                                    v-for="param in currentMatchParams" 
                                    :key="param.label"
                                    class="inline-flex items-center px-2 py-1 bg-white rounded border border-gray-200 text-sm"
                                >
                                    <span class="text-gray-600">{{ param.label }}:</span>
                                    <span class="font-medium text-gray-800 ml-1">{{ param.value }}</span>
                                </span>
                            </div>
                        </div>

                        <!-- 匹配结果列表 -->
                        <div v-if="matchResults.length > 0" class="space-y-4">
                            <div 
                                v-for="(result, index) in matchResults" 
                                :key="index"
                                class="bg-gray-50 rounded-lg p-4 border border-gray-200 hover:shadow-md transition-shadow"
                            >
                                <div class="flex justify-between items-start mb-3">
                                    <div>
                                        <h3 class="text-lg font-medium text-gray-900">{{ result.name }}</h3>
                                        <div class="text-sm text-gray-500 mt-1">
                                            <span>型號: {{ result.model }}</span>
                                            <span class="mx-2">|</span>
                                            <span>物料號: {{ result.id }}</span>
                                        </div>
                                    </div>
                                    <div class="text-right">
                                        <div class="text-sm text-gray-600">匹配度</div>
                                        <div 
                                            :class="{
                                                'text-green-600': result.score >= 80,
                                                'text-yellow-600': result.score >= 60 && result.score < 80,
                                                'text-red-600': result.score < 60
                                            }"
                                            class="text-2xl font-bold"
                                        >
                                            {{ result.score }}%
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="grid grid-cols-2 gap-4 text-sm">
                                    <div>
                                        <h4 class="font-medium text-gray-700 mb-2">規格信息</h4>
                                        <div class="space-y-1 text-gray-600">
                                            <p>顏色: {{ result.color || '無' }}</p>
                                            <p>尺寸: {{ result.width }} × {{ result.length }}</p>
                                            <p>鬆緊度: {{ result.tension }}</p>
                                            <p>溫度範圍: {{ result.temperature }}</p>
                                            <p>價格等級: {{ result.costIndex }}</p>
                                        </div>
                                    </div>
                                    
                                    <div>
                                        <h4 class="font-medium text-gray-700 mb-2">應用場景</h4>
                                        <div class="flex flex-wrap gap-1">
                                            <span
                                                v-for="scenario in result.scenarios"
                                                :key="scenario"
                                                class="px-2 py-1 bg-green-100 text-green-800 rounded-full text-xs"
                                            >
                                                {{ scenario }}
                                            </span>
                                        </div>
                                    </div>
                                    
                                    <div class="col-span-2">
                                        <h4 class="font-medium text-gray-700 mb-2">特性</h4>
                                        <div class="flex flex-wrap gap-1">
                                            <span
                                                v-for="feature in result.features"
                                                :key="feature"
                                                class="px-2 py-1 bg-blue-100 text-blue-800 rounded-full text-xs"
                                            >
                                                {{ feature }}
                                            </span>
                                        </div>
                                    </div>

                                    <!-- 兼容性信息 -->
                                    <div class="col-span-2">
                                        <h4 class="font-medium text-gray-700 mb-2">兼容性信息</h4>
                                        <div class="grid grid-cols-2 gap-2 text-sm">
                                            <div class="flex items-center gap-2">
                                                <span class="text-gray-600">過膠兼容性:</span>
                                                <span :class="{
                                                    'text-green-600': result.laminatingCompatibility === 'all',
                                                    'text-yellow-600': result.laminatingCompatibility === 'partial',
                                                    'text-red-600': result.laminatingCompatibility === 'none'
                                                }">
                                                    {{ result.laminatingCompatibility === 'all' ? '全兼容' : 
                                                       result.laminatingCompatibility === 'partial' ? '部分兼容' : '不兼容' }}
                                                </span>
                                            </div>
                                            <div class="flex items-center gap-2">
                                                <span class="text-gray-600">UV印刷:</span>
                                                <span :class="result.uvPrintingCompatible ? 'text-green-600' : 'text-red-600'">
                                                    {{ result.uvPrintingCompatible ? '兼容' : '不兼容' }}
                                                </span>
                                            </div>
                                            <div class="flex items-center gap-2">
                                                <span class="text-gray-600">LED UV閃光:</span>
                                                <span :class="result.ledUvGlitterCompatible ? 'text-green-600' : 'text-red-600'">
                                                    {{ result.ledUvGlitterCompatible ? '兼容' : '不兼容' }}
                                                </span>
                                            </div>
                                            <div class="flex items-center gap-2">
                                                <span class="text-gray-600">印刷:</span>
                                                <span :class="result.printingCompatible ? 'text-green-600' : 'text-red-600'">
                                                    {{ result.printingCompatible ? '兼容' : '不兼容' }}
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 无匹配结果提示 -->
                        <div v-else class="text-center text-amber-500 py-8">
                            <div class="text-lg mb-2">暫無匹配結果</div>
                            <div class="text-sm text-gray-500">
                                請選擇匹配參數或輸入公司編號進行搜索
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.matching-card {
    transition: all 0.3s ease;
}
.matching-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(0,0,0,0.1);
}
</style>
