<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElSelect, ElOption, ElCollapse, ElCollapseItem } from 'element-plus'
import {
  getProductTypeOptions, getHotStampingPatternBase, getHotStampingTypeOptions,
  getClothPaperTypes, getPreProcessingStepsOptions, getPatternAreaOptions,
  getLaminationMaterialOptions, getPostProcessingStepOptions,
} from '@/api/modules/referenceData'
import { goldFoilApi } from '@/api/modules/goldFoil'
import type {
  ProductTypeOption, HotStampingPatternBaseOption, HotStampingTypeOption,
  ClothPaperTypeOption, PreProcessingStepOption, PatternAreaOption,
  LaminationMaterialOption, PostProcessingStepOption,
} from '@/api/modules/referenceData'

interface TagItem {
  id: string
  label: string
  category: 'quickQuery' | 'productType' | 'stamping' | 'material' | 'postProcess'
  categoryLabel: string
  value: unknown
}

const emit = defineEmits<{
  (e: 'search', params: Record<string, unknown>): void
  (e: 'clear'): void
  (e: 'tag-change', payload: { tags: TagItem[] }): void
}>()

function lookupName(arr: any[], id: number | string | null | undefined, labelField = 'productName'): string {
  if (id == null) return ''
  return arr.find((item: any) => item.id === id)?.[labelField] ?? String(id)
}

const postProcessingLabelMap: Record<string, string> = {
  uvPrinting: '印刷UV',
  silk_screen_led_uv_sparkle_powder: '絲印LED UV灑閃粉',
  printing: '印刷',
  embossing: '擊凸',
  laminating: '過膠',
}

function buildTags(): TagItem[] {
  const tags: TagItem[] = []
  if (companyNumber.value) tags.push({ id: 'company-' + companyNumber.value, label: companyNumber.value, category: 'quickQuery', categoryLabel: '快速查询', value: companyNumber.value })
  if (gpNo.value) tags.push({ id: 'gpNo-' + gpNo.value, label: gpNo.value, category: 'quickQuery', categoryLabel: '快速查询', value: gpNo.value })
  if (paizi.value) tags.push({ id: 'paizi-' + paizi.value, label: paizi.value, category: 'productType', categoryLabel: '产品类型', value: paizi.value })
  if (colorNum.value) tags.push({ id: 'color-' + colorNum.value, label: colorNum.value, category: 'productType', categoryLabel: '产品类型', value: colorNum.value })
  if (priceLevel.value != null) tags.push({ id: 'price-' + priceLevel.value, label: '价格等级 ' + priceLevel.value, category: 'productType', categoryLabel: '产品类型', value: priceLevel.value })
  if (paperPerformance.value) tags.push({ id: 'perf-' + paperPerformance.value, label: paperPerformance.value, category: 'productType', categoryLabel: '产品类型', value: paperPerformance.value })
  if (productTypeId.value) {
    const name = lookupName(productTypes.value, productTypeId.value)
    if (name) tags.push({ id: 'type-' + productTypeId.value, label: name, category: 'productType', categoryLabel: '产品类型', value: productTypeId.value })
  }
  if (patternId.value) {
    const name = lookupName(patternBases.value, patternId.value, 'optionName')
    if (name) tags.push({ id: 'pattern-' + patternId.value, label: name, category: 'stamping', categoryLabel: '烫金图案', value: patternId.value })
  }
  if (hotStampingTypeOptionId.value) {
    const name = lookupName(stampingTypes.value, hotStampingTypeOptionId.value, 'stampingType')
    if (name) tags.push({ id: 'stampType-' + hotStampingTypeOptionId.value, label: name, category: 'stamping', categoryLabel: '烫金类型', value: hotStampingTypeOptionId.value })
  }
  if (clothPaperTypeId.value) {
    const name = lookupName(clothPaperTypes.value, clothPaperTypeId.value, 'typeName')
    if (name) tags.push({ id: 'cloth-' + clothPaperTypeId.value, label: name, category: 'material', categoryLabel: '材料', value: clothPaperTypeId.value })
  }
  for (const item of selectedPostProcessing.value) {
    tags.push({ id: 'pp-' + item, label: postProcessingLabelMap[item] || item, category: 'postProcess', categoryLabel: '后加工', value: item })
  }
  return tags
}

const activeSections = ref(['preprocess', 'productType', 'stamping', 'material', 'postprocess'])
const loading = ref(true)

// ── 下拉数据 ──
const productTypes = ref<ProductTypeOption[]>([])
const patternBases = ref<HotStampingPatternBaseOption[]>([])
const stampingTypes = ref<HotStampingTypeOption[]>([])
const clothPaperTypes = ref<ClothPaperTypeOption[]>([])
const preProcessSteps = ref<PreProcessingStepOption[]>([])
const patternAreaOptions = ref<PatternAreaOption[]>([])
const laminationMaterials = ref<LaminationMaterialOption[]>([])
const postProcessSteps = ref<PostProcessingStepOption[]>([])

// ── 下拉建议数据 ──
const distinctCompanyNumbers = ref<string[]>([])
const distinctGpNumbers = ref<string[]>([])
const showCompanyDropdown = ref(false)
const showGpDropdown = ref(false)
const companyFocused = ref(false)
const gpFocused = ref(false)

const filteredCompanyNumbers = computed(() =>
  distinctCompanyNumbers.value.filter(n => !companyNumber.value || n.includes(companyNumber.value))
)
const filteredGpNumbers = computed(() =>
  distinctGpNumbers.value.filter(n => !gpNo.value || n.includes(gpNo.value))
)

function selectCompany(val: string) {
  companyNumber.value = val
  showCompanyDropdown.value = false
  handleSearch()
}

function selectGpNo(val: string) {
  gpNo.value = val
  showGpDropdown.value = false
  handleSearch()
}

onMounted(async () => {
  try {
    const [pt, pb, st, ct, pp, pa, lm, ps, dv] = await Promise.all([
      getProductTypeOptions(),
      getHotStampingPatternBase(),
      getHotStampingTypeOptions(),
      getClothPaperTypes(),
      getPreProcessingStepsOptions(),
      getPatternAreaOptions(),
      getLaminationMaterialOptions(),
      getPostProcessingStepOptions(),
      goldFoilApi.getDistinctValues().catch(() => null),
    ])
    productTypes.value = pt
    patternBases.value = pb
    stampingTypes.value = st
    clothPaperTypes.value = ct
    preProcessSteps.value = pp
    patternAreaOptions.value = pa
    laminationMaterials.value = lm
    postProcessSteps.value = ps
    if (dv) {
      distinctCompanyNumbers.value = (dv as any).companyNumbers ?? []
      distinctGpNumbers.value = (dv as any).gpNumbers ?? []
    }
  } catch (e) {
    console.error('加载参考数据失败', e)
  } finally {
    loading.value = false
  }
})

// ── 基本筛选 ──
const companyNumber = ref('')
const gpNo = ref('')
const paizi = ref('')
const colorNum = ref('')
const priceLevel = ref<number | null>(null)
const paperPerformance = ref('')

const paperPerformanceOptions = ['普通金紙', '普通耐磨', '高耐磨']

// ── 前工序 ──
const preProcessingStepsOptionId = ref<number | null>(null)

// ── 材料 ──
const clothPaperTypeId = ref<number | null>(null)
const clothPaperCompatibilityStatus = ref('V')

// ── 烫金 ──
const productTypeId = ref<number | null>(null)
const patternId = ref<number | null>(null)
const hotStampingTypeOptionId = ref<number | null>(null)
const showStampingPosition = ref(false)
const selectedStampingPosition = ref('')
const patternAreaOptionId = ref<number | null>(null)

// 监听烫金类型选择，设置位置显隐
function onStampingTypeChange() {
  const sel = stampingTypes.value.find(s => s.id === hotStampingTypeOptionId.value)
  showStampingPosition.value = sel?.stampingType === '平面燙金'
  if (!showStampingPosition.value) selectedStampingPosition.value = ''
  handleSearch()
}

// ── 后加工 ──
const selectedPostProcessing = ref<string[]>([])
const postProcessingStepId = ref<number | null>(null)
const laminationMaterialId = ref<number | null>(null)

const postProcessingOptions = [
  { value: 'uvPrinting', label: '印刷UV' },
  { value: 'silk_screen_led_uv_sparkle_powder', label: '絲印LED UV灑閃粉' },
  { value: 'printing', label: '印刷' },
  { value: 'embossing', label: '擊凸' },
  { value: 'laminating', label: '過膠' },
]

function buildParams(): Record<string, unknown> {
  const params: Record<string, unknown> = {}

  // 基本
  if (companyNumber.value) params.companyNumber = companyNumber.value
  if (gpNo.value) params.gpNo = gpNo.value
  if (paizi.value) params.paizi = paizi.value
  if (colorNum.value) params.colorNum = colorNum.value
  if (priceLevel.value) params.priceLevel = priceLevel.value
  if (paperPerformance.value) params.paperPerformance = paperPerformance.value
  if (productTypeId.value) params.productTypeId = productTypeId.value

  // 前工序
  if (preProcessingStepsOptionId.value) params.preProcessingStepsOptionId = preProcessingStepsOptionId.value

  // 材料
  if (clothPaperTypeId.value) {
    params.clothPaperTypeId = clothPaperTypeId.value
    params.clothPaperCompatibilityStatus = clothPaperCompatibilityStatus.value || 'V'
  }

  // 烫金
  if (productTypeId.value) params.productTypeId = productTypeId.value
  if (patternId.value) params.patternId = patternId.value
  if (hotStampingTypeOptionId.value) params.hotStampingTypeOptionId = hotStampingTypeOptionId.value
  if (selectedStampingPosition.value) {
    params.flatHotStamping = selectedStampingPosition.value === '於中間位' ? 'V' : 'X'
  }
  if (patternAreaOptionId.value) params.patternAreaOptionId = patternAreaOptionId.value

  // 后加工
  if (selectedPostProcessing.value.includes('uvPrinting')) params.uvPrinting = 'V'
  if (selectedPostProcessing.value.includes('silk_screen_led_uv_sparkle_powder')) params.silk_screen_led_uv_sparkle_powder = 'V'
  if (selectedPostProcessing.value.includes('printing')) params.printing = 'V'
  if (selectedPostProcessing.value.includes('embossing')) params.embossing = 'V'

  // 过胶关联（仅当选中过胶时发送 ID）
  if (selectedPostProcessing.value.includes('laminating')) {
    if (postProcessingStepId.value) params.postProcessingStepId = postProcessingStepId.value
    if (laminationMaterialId.value) params.laminationMaterialId = laminationMaterialId.value
  }

  return params
}

function handleSearch() {
  emit('search', buildParams())
  emit('tag-change', { tags: buildTags() })
}

function handleClear() {
  companyNumber.value = ''
  gpNo.value = ''
  paizi.value = ''
  colorNum.value = ''
  priceLevel.value = null
  paperPerformance.value = ''
  productTypeId.value = null
  preProcessingStepsOptionId.value = null
  clothPaperTypeId.value = null
  clothPaperCompatibilityStatus.value = 'V'
  patternId.value = null
  hotStampingTypeOptionId.value = null
  selectedStampingPosition.value = ''
  showStampingPosition.value = false
  patternAreaOptionId.value = null
  selectedPostProcessing.value = []
  postProcessingStepId.value = null
  laminationMaterialId.value = null
  emit('clear')
  emit('tag-change', { tags: [] })
}
</script>

<template>
  <div class="h-full overflow-y-auto bg-white border-r border-gray-200">
    <div class="p-3 space-y-1">
      <!-- 快速查询：公司编号 / 客户编号（独立于折叠面板之外，一直可见） -->
      <div class="mb-3 bg-blue-50 p-3 rounded-lg border border-blue-200">
        <div class="flex items-center justify-between mb-2">
          <div class="flex items-center gap-1.5">
            <svg class="w-3.5 h-3.5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
            <span class="text-xs font-semibold text-blue-700">快速查詢</span>
          </div>
          <button
            v-if="companyNumber || gpNo"
            class="text-[10px] px-2 py-0.5 rounded bg-red-100 text-red-600 hover:bg-red-200 transition-colors"
            @click="handleClear"
          >清空</button>
        </div>
        <div class="space-y-1.5">
          <!-- 公司编号 带下拉建议 -->
          <div class="relative">
            <input
              v-model="companyNumber"
              placeholder="輸入公司編號查詢"
              class="w-full px-2.5 py-1.5 text-xs border border-blue-200 rounded focus:outline-none focus:ring-1 focus:ring-blue-400 bg-white"
              @input="handleSearch"
              @focus="showCompanyDropdown = true; companyFocused = true"
              @blur="setTimeout(() => showCompanyDropdown = false, 200)"
            />
            <ul
              v-if="showCompanyDropdown && filteredCompanyNumbers.length > 0"
              class="absolute z-20 left-0 right-0 mt-0.5 bg-white border border-blue-200 rounded shadow-lg max-h-40 overflow-y-auto text-xs"
            >
              <li
                v-for="n in filteredCompanyNumbers"
                :key="n"
                class="px-2.5 py-1.5 hover:bg-blue-50 cursor-pointer text-gray-700 truncate"
                @mousedown.prevent="selectCompany(n)"
              >{{ n }}</li>
            </ul>
          </div>
          <!-- 型号 带下拉建议 -->
          <div class="relative">
            <input
              v-model="gpNo"
              placeholder="輸入燙金紙型號查詢"
              class="w-full px-2.5 py-1.5 text-xs border border-blue-200 rounded focus:outline-none focus:ring-1 focus:ring-blue-400 bg-white"
              @input="handleSearch"
              @focus="showGpDropdown = true; gpFocused = true"
              @blur="setTimeout(() => showGpDropdown = false, 200)"
            />
            <ul
              v-if="showGpDropdown && filteredGpNumbers.length > 0"
              class="absolute z-20 left-0 right-0 mt-0.5 bg-white border border-blue-200 rounded shadow-lg max-h-40 overflow-y-auto text-xs"
            >
              <li
                v-for="n in filteredGpNumbers"
                :key="n"
                class="px-2.5 py-1.5 hover:bg-blue-50 cursor-pointer text-gray-700 truncate"
                @mousedown.prevent="selectGpNo(n)"
              >{{ n }}</li>
            </ul>
          </div>
        </div>
      </div>

      <h2 class="text-sm font-semibold text-gray-800 mb-3 flex items-center gap-1.5">
        <svg class="w-3.5 h-3.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
        </svg>
        篩選條件
        <span v-if="loading" class="text-[10px] text-gray-400 ml-1">載入中...</span>
      </h2>

      <ElCollapse v-model="activeSections" class="filter-collapse">
        <!-- 1. 前工序 / 适用界面（老版第3步） -->
        <ElCollapseItem title="① 適用界面(前工序)" name="preprocess">
          <div class="space-y-2">
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">前工序選項</label>
              <el-select v-model="preProcessingStepsOptionId" placeholder="選擇前工序" class="w-full" size="small" @change="handleSearch" clearable>
                <el-option v-for="o in preProcessSteps" :key="o.id" :label="o.preProcessingStepsName" :value="o.id" />
              </el-select>
            </div>
          </div>
        </ElCollapseItem>

        <!-- 2. 产品类型（老版第4步） -->
        <ElCollapseItem title="② 產品類型" name="productType">
          <div class="space-y-2">
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">產品類型</label>
              <el-select v-model="productTypeId" placeholder="請選擇" class="w-full" size="small" @change="handleSearch" clearable>
                <el-option v-for="o in productTypes" :key="o.id" :label="o.productName" :value="o.id" />
              </el-select>
            </div>
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">牌子</label>
              <input v-model="paizi" placeholder="輸入牌子" class="w-full px-2.5 py-1.5 text-xs border border-gray-300 rounded focus:outline-none focus:ring-1 focus:ring-indigo-400" @input="handleSearch" />
            </div>
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">顏色編碼</label>
              <input v-model="colorNum" placeholder="輸入顏色編碼" class="w-full px-2.5 py-1.5 text-xs border border-gray-300 rounded focus:outline-none focus:ring-1 focus:ring-indigo-400" @input="handleSearch" />
            </div>
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">價格優先度 (1-10)</label>
              <el-select v-model="priceLevel" placeholder="請選擇" class="w-full" size="small" @change="handleSearch" clearable>
                <el-option v-for="i in 10" :key="i" :label="String(i)" :value="i" />
              </el-select>
            </div>
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">燙金紙性能</label>
              <el-select v-model="paperPerformance" placeholder="請選擇" class="w-full" size="small" @change="handleSearch" clearable>
                <el-option v-for="o in paperPerformanceOptions" :key="o" :label="o" :value="o" />
              </el-select>
            </div>
          </div>
        </ElCollapseItem>

        <!-- 3. 烫金（图案 + 类型，老版第5-6步） -->
        <ElCollapseItem title="③ 燙金（圖案/類型）" name="stamping">
          <div class="space-y-2.5">
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">燙金圖案類型</label>
              <el-select v-model="patternId" placeholder="請選擇" class="w-full" size="small" @change="handleSearch" clearable>
                <el-option v-for="o in patternBases" :key="o.id" :label="o.optionName" :value="o.id" />
              </el-select>
            </div>
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">燙金類型</label>
              <el-select v-model="hotStampingTypeOptionId" placeholder="請選擇" class="w-full" size="small" @change="onStampingTypeChange" clearable>
                <el-option v-for="o in stampingTypes" :key="o.id" :label="o.stampingType + (o.positionType ? ' (' + o.positionType + ')' : '')" :value="o.id" />
              </el-select>
            </div>
            <div v-if="showStampingPosition">
              <label class="block text-xs text-gray-500 mb-0.5">平面燙金位置</label>
              <el-select v-model="selectedStampingPosition" placeholder="請選擇位置" class="w-full" size="small" @change="handleSearch" clearable>
                <el-option value="於中間位" label="於中間位" />
                <el-option value="到邊位" label="到邊位" />
              </el-select>
            </div>
            <div class="border-t border-gray-100 pt-2 mt-1">
              <label class="block text-xs text-gray-500 mb-0.5">圖案區域選項</label>
              <el-select v-model="patternAreaOptionId" placeholder="請選擇" class="w-full" size="small" @change="handleSearch" clearable>
                <el-option v-for="o in patternAreaOptions" :key="o.id" :label="o.optionName" :value="o.id" />
              </el-select>
            </div>
          </div>
        </ElCollapseItem>

        <!-- 4. 材料（老版第8步） -->
        <ElCollapseItem title="④ 材料適用界面" name="material">
          <div class="space-y-2">
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">布料紙類型</label>
              <el-select v-model="clothPaperTypeId" placeholder="選擇布料紙" class="w-full" size="small" @change="handleSearch" clearable filterable>
                <el-option v-for="o in clothPaperTypes" :key="o.id" :label="o.typeName" :value="o.id" />
              </el-select>
            </div>
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">兼容性狀態</label>
              <el-select v-model="clothPaperCompatibilityStatus" class="w-full" size="small" @change="handleSearch">
                <el-option value="V" label="適用" />
                <el-option value="X" label="不適用" />
              </el-select>
            </div>
          </div>
        </ElCollapseItem>

        <!-- 5. 烫后加工（老版第9步） -->
        <ElCollapseItem title="⑤ 燙後加工" name="postprocess">
          <div class="space-y-2.5">
            <div>
              <label class="block text-xs text-gray-500 mb-0.5">加工選項</label>
              <el-select v-model="selectedPostProcessing" placeholder="請選擇" class="w-full" size="small" multiple collapse-tags @change="handleSearch">
                <el-option v-for="o in postProcessingOptions" :key="o.value" :label="o.label" :value="o.value" />
              </el-select>
            </div>
            <template v-if="selectedPostProcessing.includes('laminating')">
              <div>
                <label class="block text-xs text-gray-500 mb-0.5">過膠後工藝</label>
                <el-select v-model="postProcessingStepId" placeholder="請選擇" class="w-full" size="small" @change="handleSearch" clearable>
                  <el-option v-for="o in postProcessSteps" :key="o.id" :label="o.stepName" :value="o.id" />
                </el-select>
              </div>
              <div>
                <label class="block text-xs text-gray-500 mb-0.5">過膠材質</label>
                <el-select v-model="laminationMaterialId" placeholder="請選擇" class="w-full" size="small" @change="handleSearch" clearable>
                  <el-option v-for="o in laminationMaterials" :key="o.id" :label="o.materialName" :value="o.id" />
                </el-select>
              </div>
            </template>
          </div>
        </ElCollapseItem>
      </ElCollapse>

      <div class="pt-3">
        <button @click="handleSearch"
          class="w-full py-2 bg-indigo-600 text-white text-xs rounded-lg hover:bg-indigo-700 transition-colors font-medium">
          查詢匹配
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.filter-collapse :deep(.el-collapse-item__header) {
  font-size: 0.75rem;
  font-weight: 600;
  padding-left: 0;
  height: 32px;
}
.filter-collapse :deep(.el-collapse-item__content) {
  padding-bottom: 8px;
}
.filter-collapse :deep(.el-collapse-item__wrap) {
  border-bottom: none;
}
.filter-collapse :deep(.el-collapse) {
  border-top: none;
  border-bottom: none;
}
</style>
