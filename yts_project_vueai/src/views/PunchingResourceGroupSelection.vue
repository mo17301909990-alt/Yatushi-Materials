<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">啤機資源組選擇</h1>
        <p class="text-gray-600">根據物料規格參數及工藝匹配適用資源組並判斷難度等級</p>
      </div>

      <!-- 主要内容区域 -->
      <div class="bg-white rounded-lg shadow-md p-6">
        <!-- 输入表单区域 -->
        <div class="mb-8">
          <h2 class="text-xl font-semibold text-gray-800 mb-4">物料規格參數</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <!-- 紙張類型 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                紙張類型 <span class="text-red-500">*</span>
              </label>
              <select
                v-model="materialSpecs.paperType"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">請選擇紙張類型</option>
                <option v-for="type in paperTypes" :key="type" :value="type">
                  {{ type }}
                </option>
              </select>
            </div>

            <!-- 紙張厚度 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                紙張厚度 (g/m²) <span class="text-red-500">*</span>
              </label>
              <input
                type="number"
                v-model.number="materialSpecs.paperThickness"
                placeholder="請輸入紙張厚度"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <!-- 開數 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                開數 <span class="text-red-500">*</span>
              </label>
              <select
                v-model="materialSpecs.paperSize"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">請選擇開數</option>
                <option v-for="size in paperSizes" :key="size" :value="size">
                  {{ size }}
                </option>
              </select>
            </div>

            <!-- 刀模複雜度 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                刀模複雜度 <span class="text-red-500">*</span>
              </label>
              <select
                v-model="materialSpecs.dieComplexity"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">請選擇刀模複雜度</option>
                <option value="simple">簡單</option>
                <option value="medium">中等</option>
                <option value="complex">複雜</option>
                <option value="very-complex">非常複雜</option>
              </select>
            </div>

            <!-- 模切類型 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                模切類型 <span class="text-red-500">*</span>
              </label>
              <select
                v-model="materialSpecs.punchingType"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">請選擇模切類型</option>
                <option value="full-cut">全切</option>
                <option value="half-cut">半切</option>
                <option value="embossing">壓凸</option>
                <option value="debossing">壓凹</option>
                <option value="mixed">混合</option>
              </select>
            </div>

            <!-- 訂單數量 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                訂單數量
              </label>
              <input
                type="number"
                v-model.number="materialSpecs.orderQuantity"
                placeholder="請輸入訂單數量"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <!-- 尺寸規格 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                成品尺寸 (長 x 寬 mm)
              </label>
              <div class="flex gap-2">
                <input
                  type="number"
                  v-model.number="materialSpecs.length"
                  placeholder="長"
                  class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <span class="self-center text-gray-500">×</span>
                <input
                  type="number"
                  v-model.number="materialSpecs.width"
                  placeholder="寬"
                  class="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
            </div>

            <!-- 特殊工藝 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">
                特殊工藝
              </label>
              <select
                v-model="materialSpecs.specialProcess"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">無特殊工藝</option>
                <option value="uv">UV</option>
                <option value="lamination">覆膜</option>
                <option value="hot-stamping">燙金</option>
                <option value="foil-stamping">燙箔</option>
                <option value="other">其他</option>
              </select>
            </div>

            <!-- 拼版只數 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">拼版只數</label>
              <input type="number" v-model.number="materialSpecs.impositionCount" min="1"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>

            <!-- 短邊/長邊尺寸 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">短邊尺寸(mm)</label>
              <input type="number" v-model.number="materialSpecs.shortEdgeMm"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">長邊尺寸(mm)</label>
              <input type="number" v-model.number="materialSpecs.longEdgeMm"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
          </div>
        </div>

        <!-- 後加工/結構/清廢 參數分組 -->
        <div class="mb-8">
          <h2 class="text-xl font-semibold text-gray-800 mb-4">後加工 / 結構 / 清廢 參數</h2>
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <!-- 過膠面積百分比 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">過膠面積(%)</label>
              <input type="number" min="0" max="100" v-model.number="materialSpecs.laminationAreaPercent"
                placeholder="0-100"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>

            <!-- 雙面過油/UV -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">是否雙面過油</label>
              <select v-model="materialSpecs.doubleSidedVarnish" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option :value="false">否</option>
                <option :value="true">是</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">是否雙面過UV</label>
              <select v-model="materialSpecs.doubleSidedUV" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option :value="false">否</option>
                <option :value="true">是</option>
              </select>
            </div>

            <!-- 絲印 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">是否有絲印</label>
              <select v-model="materialSpecs.hasScreenPrint" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option :value="false">否</option>
                <option :value="true">是</option>
              </select>
            </div>
            <div v-if="materialSpecs.hasScreenPrint">
              <label class="block text-sm font-medium text-gray-700 mb-2">絲印網目數</label>
              <input type="number" v-model.number="materialSpecs.screenMesh" min="0"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div v-if="materialSpecs.hasScreenPrint">
              <label class="block text-sm font-medium text-gray-700 mb-2">絲印油墨</label>
              <select v-model="materialSpecs.screenInkType" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option value="">未選擇</option>
                <option value="water">水性</option>
                <option value="oil">油性</option>
              </select>
            </div>

            <!-- 產品結構 / 風琴袋 / 散件比例 / 駁圖 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">產品結構</label>
              <select v-model="materialSpecs.productStructure" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option value="">未選擇</option>
                <option value="book">書刊內文/封面</option>
                <option value="color-box">彩盒</option>
                <option value="corrugated-box">坑盒/坑紙</option>
                <option value="gusset-bag">風琴袋</option>
                <option value="popup-book">立體書</option>
                <option value="sheet">單頁/平張</option>
                <option value="other">其他</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">是否有風琴袋</label>
              <select v-model="materialSpecs.hasGussetBag" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option :value="false">否</option>
                <option :value="true">是</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">立體書散件占紙張(%)</label>
              <input type="number" min="0" max="100" v-model.number="materialSpecs.popupLoosePartsPercent"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">是否有駁圖</label>
              <select v-model="materialSpecs.hasSplicing" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option :value="false">否</option>
                <option :value="true">是</option>
              </select>
            </div>
            <div v-if="materialSpecs.hasSplicing">
              <label class="block text-sm font-medium text-gray-700 mb-2">駁圖長度(mm)</label>
              <input type="number" v-model.number="materialSpecs.splicingLengthMm"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>

            <!-- 清廢/邊位/定位/清潔面積 -->
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">清廢最小窿徑(mm)</label>
              <input type="number" v-model.number="materialSpecs.minWasteHoleSizeMm"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">清廢孔數(個)</label>
              <input type="number" v-model.number="materialSpecs.wasteHoleCount" min="0"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">紙邊邊位(mm)</label>
              <input type="number" v-model.number="materialSpecs.paperEdgeMarginMm"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">是否需定位</label>
              <select v-model="materialSpecs.needRegistration" class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500">
                <option :value="false">否</option>
                <option :value="true">是</option>
              </select>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">清潔面積(%)</label>
              <input type="number" min="0" max="100" v-model.number="materialSpecs.cleaningAreaPercent"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" />
            </div>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="flex justify-end gap-4 mb-8">
          <button
            @click="resetForm"
            class="px-6 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
          >
            重置
          </button>
          <button
            @click="matchResourceGroup"
            :disabled="!canMatch || matching"
            class="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors"
          >
            <span v-if="matching">匹配中...</span>
            <span v-else>匹配資源組</span>
          </button>
        </div>

        <!-- 结果显示区域 -->
        <div v-if="matchingResult" class="mt-8 border-t pt-6">
          <h2 class="text-xl font-semibold text-gray-800 mb-4">匹配結果</h2>
          
          <!-- 資源組信息 -->
          <div class="mb-6">
            <h3 class="text-lg font-medium text-gray-700 mb-3">適用資源組</h3>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div
                v-for="(group, index) in matchingResult.resourceGroups"
                :key="index"
                class="bg-blue-50 border border-blue-200 rounded-lg p-4"
              >
                <div class="flex items-center justify-between mb-2">
                  <span class="font-semibold text-blue-900">{{ group.name }}</span>
                  <span class="text-sm px-2 py-1 bg-blue-200 text-blue-800 rounded">
                    {{ group.priority }}優先
                  </span>
                </div>
                <div class="text-sm text-gray-600 space-y-1">
                  <p><span class="font-medium">產能:</span> {{ group.capacity }} 張/小時</p>
                  <p><span class="font-medium">準備時間:</span> {{ group.setupTime }} 分鐘</p>
                  <p v-if="group.remarks" class="text-gray-500 italic">{{ group.remarks }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 難度等級 -->
          <div class="mb-6">
            <h3 class="text-lg font-medium text-gray-700 mb-3">難度等級</h3>
            <div
              class="rounded-lg p-4 border-2"
              :class="difficultyClass"
            >
              <div class="flex items-center justify-between">
                <div>
                  <span class="text-2xl font-bold">{{ matchingResult.difficulty.level }}</span>
                  <p class="text-sm text-gray-600 mt-1">{{ matchingResult.difficulty.description }}</p>
                </div>
                <div class="text-right">
                  <p class="text-sm text-gray-600">產能修正係數</p>
                  <p class="text-2xl font-bold">{{ matchingResult.difficulty.capacityFactor }}x</p>
                </div>
              </div>
              <div v-if="matchingResult.difficulty.reasons.length > 0" class="mt-3 pt-3 border-t">
                <p class="text-sm font-medium mb-2">判斷依據:</p>
                <ul class="text-sm space-y-1">
                  <li v-for="(reason, index) in matchingResult.difficulty.reasons" :key="index" class="flex items-start">
                    <span class="mr-2">•</span>
                    <span>{{ reason }}</span>
                  </li>
                </ul>
              </div>
            </div>
          </div>

          <!-- 詳細建議 -->
          <div v-if="matchingResult.suggestions.length > 0" class="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
            <h3 class="text-lg font-medium text-yellow-900 mb-2">生產建議</h3>
            <ul class="text-sm text-yellow-800 space-y-1">
              <li v-for="(suggestion, index) in matchingResult.suggestions" :key="index" class="flex items-start">
                <span class="mr-2">•</span>
                <span>{{ suggestion }}</span>
              </li>
            </ul>
          </div>
        </div>

        <!-- 无结果提示 -->
        <div v-if="showNoResult" class="mt-8 text-center py-8 bg-gray-50 rounded-lg">
          <p class="text-gray-600">暫無匹配結果，請檢查輸入參數或聯繫管理員</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';

// 物料規格參數
const materialSpecs = ref({
  paperType: '',
  paperThickness: null as number | null,
  paperSize: '',
  dieComplexity: '',
  punchingType: '',
  orderQuantity: null as number | null,
  length: null as number | null,
  width: null as number | null,
    specialProcess: '',
    impositionCount: null as number | null,
    shortEdgeMm: null as number | null,
    longEdgeMm: null as number | null,
    laminationAreaPercent: null as number | null,
    doubleSidedVarnish: false,
    doubleSidedUV: false,
    hasScreenPrint: false,
    screenMesh: null as number | null,
    screenInkType: '',
    productStructure: '',
    hasGussetBag: false,
    popupLoosePartsPercent: null as number | null,
    hasSplicing: false,
    splicingLengthMm: null as number | null,
    minWasteHoleSizeMm: null as number | null,
    wasteHoleCount: null as number | null,
    paperEdgeMarginMm: null as number | null,
    needRegistration: false,
    cleaningAreaPercent: null as number | null
});

// 紙張類型選項
const paperTypes = [
  '單粉咭',
  '雙粉咭',
  '單粉紙',
  '啞粉紙',
  '無粉咭',
  '書紙',
  '白卡紙',
  '銅版紙',
  '膠片',
  '其他'
];

// 開數選項
const paperSizes = [
  '對開',
  '四開',
  '八開',
  '十六開',
  '三十二開',
  '其他'
];

// 匹配狀態
const matching = ref(false);
const matchingResult = ref<any>(null);
const showNoResult = ref(false);

// 檢查是否可以匹配
const canMatch = computed(() => {
  return !!(
    materialSpecs.value.paperType &&
    materialSpecs.value.paperThickness &&
    materialSpecs.value.paperSize &&
    materialSpecs.value.dieComplexity &&
    materialSpecs.value.punchingType
  );
});

// 難度等級樣式類
const difficultyClass = computed(() => {
  if (!matchingResult.value) return '';
  
  const level = matchingResult.value.difficulty.level;
  if (level === '易') {
    return 'bg-green-50 border-green-300 text-green-900';
  } else if (level === '中') {
    return 'bg-yellow-50 border-yellow-300 text-yellow-900';
  } else {
    return 'bg-red-50 border-red-300 text-red-900';
  }
});

// 重置表單
const resetForm = () => {
  materialSpecs.value = {
    paperType: '',
    paperThickness: null,
    paperSize: '',
    dieComplexity: '',
    punchingType: '',
    orderQuantity: null,
    length: null,
    width: null,
    specialProcess: ''
  };
  matchingResult.value = null;
  showNoResult.value = false;
};

// 匹配資源組（模擬邏輯）
const matchResourceGroup = async () => {
  if (!canMatch.value) return;

  matching.value = true;
  showNoResult.value = false;

  // 模擬API調用延遲
  await new Promise(resolve => setTimeout(resolve, 1000));

  try {
    // 這裡應該調用實際的API，目前使用模擬數據
    const result = simulateResourceGroupMatching(materialSpecs.value);
    
    if (result) {
      matchingResult.value = result;
      showNoResult.value = false;
    } else {
      matchingResult.value = null;
      showNoResult.value = true;
    }
  } catch (error) {
    console.error('匹配失敗:', error);
    matchingResult.value = null;
    showNoResult.value = true;
  } finally {
    matching.value = false;
  }
};

// 模擬資源組匹配邏輯（實際應該從後端API獲取）
const simulateResourceGroupMatching = (specs: any) => {
  // 簡單的匹配邏輯示例
  const resourceGroups: any[] = [];
  let difficultyScore = 2; // 0:易 1:中 2:難 3:高難 4:特難
  let capacityFactor = 1.0;
  let prepHoursAdd = 0.0;
  const reasons: string[] = [];

  // 根據刀模複雜度判斷難度
  if (specs.dieComplexity === 'simple') {
    difficultyScore -= 1;
    capacityFactor *= 1.2;
    reasons.push('刀模複雜度較低，生產效率高');
  } else if (specs.dieComplexity === 'complex' || specs.dieComplexity === 'very-complex') {
    difficultyScore += 1;
    capacityFactor *= 0.7;
    reasons.push('刀模複雜度較高，需要更多調整時間');
  } else {
    reasons.push('刀模複雜度中等，正常生產效率');
  }

  // 根據紙張厚度調整難度
  if (specs.paperThickness) {
    if (specs.paperThickness > 400) {
      capacityFactor *= 0.8;
      reasons.push('紙張厚度較大，需要更多壓力');
      difficultyScore += 1;
    } else if (specs.paperThickness < 200) {
      reasons.push('紙張厚度適中');
    }
  }

  // 根據模切類型調整
  if (specs.punchingType === 'mixed') {
    capacityFactor *= 0.85;
    difficultyScore += 1;
    reasons.push('混合模切類型，工藝複雜');
  }

  // 尺寸/拼版
  if (specs.shortEdgeMm && specs.shortEdgeMm > 450) { difficultyScore += 1; reasons.push('短邊>450mm'); }
  if (specs.longEdgeMm && specs.longEdgeMm > 600) { difficultyScore += 1; reasons.push('長邊>600mm'); }
  if (specs.impositionCount && specs.impositionCount >= 12) { difficultyScore += 1; reasons.push('拼版只數≥12'); }

  // 過膠面積
  if (specs.laminationAreaPercent != null) {
    if (specs.laminationAreaPercent > 80) { capacityFactor *= 0.7; prepHoursAdd += 0.4; difficultyScore += 2; reasons.push('過膠面積>80%'); }
    else if (specs.laminationAreaPercent > 50) { capacityFactor *= 0.85; prepHoursAdd += 0.2; difficultyScore += 1; reasons.push('過膠面積>50%'); }
  }

  // 雙面過油/UV
  if (specs.doubleSidedVarnish) { difficultyScore += 1; prepHoursAdd += 0.2; reasons.push('雙面過油'); }
  if (specs.doubleSidedUV) { difficultyScore += 1; prepHoursAdd += 0.2; reasons.push('雙面過UV'); }

  // 絲印
  if (specs.hasScreenPrint) {
    if ((specs.screenMesh && specs.screenMesh >= 200) || specs.screenInkType === 'oil') {
      difficultyScore += 1; prepHoursAdd += 0.3; reasons.push('絲印高網目/油性');
    } else {
      difficultyScore += 0.5; reasons.push('絲印');
    }
  }

  // 清廢與邊位
  if (specs.minWasteHoleSizeMm != null && specs.wasteHoleCount != null) {
    if (specs.minWasteHoleSizeMm < 3 && specs.wasteHoleCount >= 20) { difficultyScore += 1; capacityFactor *= 0.8; reasons.push('清廢小孔<3mm且數量≥20'); }
  }
  if ((specs.paperEdgeMarginMm != null && specs.paperEdgeMarginMm <= 3) || specs.needRegistration) { difficultyScore += 1; prepHoursAdd += 0.2; reasons.push('邊位≤3mm或需定位'); }
  if (specs.cleaningAreaPercent != null && specs.cleaningAreaPercent > 40) { difficultyScore += 1; prepHoursAdd += 0.3; reasons.push('清潔面積>40%'); }

  // 結構
  if (specs.hasGussetBag) { difficultyScore += 1; prepHoursAdd += 0.4; reasons.push('風琴袋結構'); }
  if (specs.productStructure === 'popup-book') {
    if (specs.popupLoosePartsPercent != null) {
      if (specs.popupLoosePartsPercent >= 35) { difficultyScore += 2; capacityFactor *= 0.8; reasons.push('立體書散件≥35%'); }
      else if (specs.popupLoosePartsPercent >= 20) { difficultyScore += 1; reasons.push('立體書散件≥20%'); }
    } else {
      difficultyScore += 1; reasons.push('立體書');
    }
  }
  if (specs.hasSplicing && specs.splicingLengthMm && specs.splicingLengthMm > 200) { difficultyScore += 1; reasons.push('大面積駁圖'); }

  // 根據開數匹配資源組
  if (specs.paperSize === '對開' || specs.paperSize === '四開') {
    resourceGroups.push({ name: '首源組', priority: '第一', capacity: Math.floor(2500 * capacityFactor), setupTime: 0.5 + prepHoursAdd, remarks: '標準大機' });
    resourceGroups.push({ name: '富源組', priority: '第二', capacity: Math.floor(1800 * capacityFactor), setupTime: 0.8 + prepHoursAdd, remarks: '替代資源組' });
  } else {
    resourceGroups.push({ name: '首源組', priority: '第一', capacity: Math.floor(2800 * capacityFactor), setupTime: 0.5 + prepHoursAdd, remarks: '中小型開數' });
    resourceGroups.push({ name: '富源組', priority: '第二', capacity: Math.floor(2000 * capacityFactor), setupTime: 0.8 + prepHoursAdd, remarks: '替代資源組' });
  }

  // 如果有特殊工藝，添加備選資源組
  if (specs.specialProcess) {
    resourceGroups.push({ name: '手啤/自動手啤', priority: '備選', capacity: Math.floor(600 * capacityFactor), setupTime: 1.5 + prepHoursAdd, remarks: '手工作業' });
    if (difficultyScore >= 3) {
      resourceGroups.push({ name: 'CSI', priority: '備選', capacity: Math.floor(300 * capacityFactor), setupTime: 3.67 + prepHoursAdd, remarks: '特殊結構處理' });
    }
  }

  const suggestions: string[] = [];
  
  const level = mapScoreToLevel(difficultyScore);
  if (level === '特難' || level === '高難') {
    suggestions.push('建議預留更多生產時間');
    suggestions.push('需要經驗豐富的操作人員');
  } else if (level === '中') {
    suggestions.push('正常生產流程，注意質量控制');
  } else {
    suggestions.push('可以考慮提高生產速度');
  }

  return {
    resourceGroups,
    difficulty: {
      level,
      description: getDifficultyDescription(level),
      capacityFactor: capacityFactor.toFixed(2),
      reasons
    },
    suggestions
  };
};

const mapScoreToLevel = (score: number) => {
  if (score >= 4) return '特難';
  if (score >= 3) return '高難';
  if (score >= 2) return '難';
  if (score >= 1) return '中';
  return '易';
};

// 獲取難度描述
const getDifficultyDescription = (level: string) => {
  const descriptions: Record<string, string> = {
    '易': '生產難度較低，可以快速完成',
    '中': '生產難度中等，需要正常工藝流程',
    '難': '生產難度較高，需要更多時間和經驗'
  };
  return descriptions[level] || '未知難度';
};
</script>

<style scoped>
/* 可以添加自定義樣式 */
</style>

