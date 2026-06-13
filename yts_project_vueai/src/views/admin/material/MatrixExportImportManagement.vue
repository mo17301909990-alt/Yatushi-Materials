<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <div class="flex items-start gap-3">
          <AdminBackToPanel />
          <div class="flex-1 min-w-0">
            <div class="flex items-center justify-between">
              <div>
                <h1 class="text-3xl font-bold text-gray-900">矩阵表导出导入</h1>
                <p class="mt-2 text-gray-600">按指引書對應的矩陣表進行 Excel / Word 導出與導入</p>
              </div>
              <router-link
                to="/admin/material-rule-management"
                class="inline-flex items-center px-4 py-2 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
              >
                <svg class="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
                </svg>
                返回
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 矩阵类型列表 -->
      <div class="space-y-6">
        <div
          v-for="item in matrixTypes"
          :key="item.id"
          class="bg-white rounded-lg shadow-md p-6 border border-gray-200"
        >
          <div class="flex items-start justify-between">
            <div class="flex-1">
              <h2 class="text-lg font-semibold text-gray-900">{{ item.title }}</h2>
              <p class="mt-1 text-sm text-gray-500">{{ item.description }}</p>
              <p v-if="item.docName" class="mt-1 text-xs text-gray-400">對應文檔：{{ item.docName }}</p>
            </div>
            <div class="ml-4 flex flex-wrap gap-2">
              <button
                v-if="item.exportExcel"
                type="button"
                @click="exportExcel(item)"
                :disabled="item.exporting"
                class="inline-flex items-center px-3 py-2 border border-green-300 rounded-md text-sm font-medium text-green-700 bg-green-50 hover:bg-green-100 disabled:opacity-50"
              >
                <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 10v6m0 0l-3-3m3 3l3-3m2 8H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                </svg>
                导出 Excel
              </button>
              <button
                v-if="item.exportWord"
                type="button"
                @click="exportWord(item)"
                :disabled="item.exporting"
                class="inline-flex items-center px-3 py-2 border border-blue-300 rounded-md text-sm font-medium text-blue-700 bg-blue-50 hover:bg-blue-100 disabled:opacity-50"
              >
                <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
                </svg>
                导出 Word
              </button>
              <button
                v-if="item.importApi"
                type="button"
                @click="openImportDialog(item)"
                class="inline-flex items-center px-3 py-2 border border-amber-300 rounded-md text-sm font-medium text-amber-700 bg-amber-50 hover:bg-amber-100"
              >
                <svg class="w-4 h-4 mr-1.5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12"></path>
                </svg>
                导入
              </button>
              <span v-else-if="item.comingSoon" class="inline-flex items-center px-3 py-2 text-sm text-gray-400 bg-gray-100 rounded-md">
                敬請期待
              </span>
            </div>
          </div>
        </div>
      </div>

      <!-- 导入对话框 -->
      <div v-if="importDialogVisible" class="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
        <div class="bg-white rounded-lg shadow-xl p-6 w-full max-w-md">
          <h3 class="text-lg font-medium text-gray-900 mb-2">{{ currentImportItem?.title }} - 导入</h3>
          <p class="text-sm text-gray-500 mb-4">支持 .xlsx、.docx，表頭需包含「系列」等關鍵列</p>
          <div class="space-y-4">
            <div v-if="currentImportItem?.exportWordApi" class="flex items-center gap-2 p-3 bg-amber-50 border border-amber-200 rounded-md">
              <span class="text-sm text-amber-800 flex-1">可先下載帶當前數據的 Word，修改後再上傳</span>
              <button
                type="button"
                @click="downloadImportWordTemplate"
                :disabled="downloadingTemplate"
                class="shrink-0 px-3 py-1.5 text-sm font-medium text-amber-800 bg-amber-100 border border-amber-300 rounded hover:bg-amber-200 disabled:opacity-50"
              >
                {{ downloadingTemplate ? '下載中…' : '下載 Word 模板（帶舊數據）' }}
              </button>
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">选择文件</label>
              <input
                type="file"
                ref="importFileInput"
                accept=".xlsx,.docx"
                @change="onImportFileSelect"
                class="w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-cyan-50 file:text-cyan-700 hover:file:bg-cyan-100"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">冲突处理</label>
              <div class="flex space-x-4">
                <label class="inline-flex items-center">
                  <input type="radio" v-model="conflictStrategy" value="skip" class="form-radio text-cyan-600">
                  <span class="ml-2 text-sm text-gray-700">跳过已有记录</span>
                </label>
                <label class="inline-flex items-center">
                  <input type="radio" v-model="conflictStrategy" value="overwrite" class="form-radio text-cyan-600">
                  <span class="ml-2 text-sm text-gray-700">覆盖已有记录</span>
                </label>
              </div>
            </div>
            <div v-if="importResult" class="bg-gray-50 rounded-md p-3 text-sm">
              <p class="font-medium text-gray-900">导入结果</p>
              <p class="text-green-600">新建: {{ importResult.created }}</p>
              <p class="text-blue-600">更新: {{ importResult.updated }}</p>
              <p class="text-yellow-600">跳过: {{ importResult.skipped }}</p>
              <p v-if="importResult.errors > 0" class="text-red-600">错误: {{ importResult.errors }}</p>
              <div v-if="importResult.errorMessages?.length" class="mt-2">
                <p class="text-gray-700 text-xs font-medium mb-1">错误详情（可滚动查看）:</p>
                <div class="text-red-600 text-xs max-h-48 overflow-y-auto border border-red-200 rounded p-2 bg-red-50">
                  <p v-for="(msg, idx) in importResult.errorMessages" :key="idx" class="mb-0.5">{{ msg }}</p>
                </div>
              </div>
            </div>
          </div>
          <div class="mt-6 flex justify-end space-x-3">
            <button
              @click="closeImportDialog"
              class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50"
            >
              {{ importResult ? '关闭' : '取消' }}
            </button>
            <button
              v-if="!importResult"
              @click="executeImport"
              :disabled="!selectedFile || importing"
              class="px-4 py-2 border border-transparent rounded-md text-sm font-medium text-white bg-cyan-600 hover:bg-cyan-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {{ importing ? '导入中...' : '开始导入' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import AdminBackToPanel from '@/components/admin/AdminBackToPanel.vue'
import { commonInterfaceMatrixApi, type MatrixImportResult } from '../../../api/modules/commonInterfaceMatrix'
import { specialInterfaceClothMatrixApi } from '../../../api/modules/specialInterfaceClothMatrix'
import { laminatingMatrixApi } from '../../../api/modules/laminatingMatrix'
import { postProcessingPrintUvMatrixApi } from '../../../api/modules/postProcessingPrintUvMatrix'
import { postProcessingPrintMatrixApi } from '../../../api/modules/postProcessingPrintMatrix'
import { postProcessingLeduvglitterMatrixApi } from '../../../api/modules/postProcessingLeduvglitterMatrix'
import { smartCompatibilityApi } from '../../../api/modules/smartCompatibility'

// 矩阵类型配置（不同文件名/指引書對應一項）
interface MatrixTypeItem {
  id: string
  title: string
  description: string
  docName?: string
  exportExcel?: boolean
  exportWord?: boolean
  importApi?: boolean
  comingSoon?: boolean
  exporting?: boolean
  exportExcelApi?: () => Promise<Blob>
  exportWordApi?: () => Promise<Blob>
  importApiFn?: (file: File, strategy: string) => Promise<MatrixImportResult>
  /** 導出 Excel 時使用的文件名（不含日期與擴展名），不設則用 title */
  exportExcelFilename?: string
  /** 導出 Word 時使用的文件名（不含日期與擴展名），不設則用 title */
  exportWordFilename?: string
}

const matrixTypes = ref<MatrixTypeItem[]>([
  {
    id: 'wear-resistant-gold-paper',
    title: '耐磨燙金紙選用指引書',
    description: '產品類型、圖案特徵、燙金類型、前工序及耐磨燙金紙映射配置，與「智能兼容」頁導出全部一致',
    docName: '《耐磨燙金紙選用－指引書》',
    exportExcel: true,
    exportWord: true,
    importApi: true,
    exportExcelApi: () => smartCompatibilityApi.exportAllRules(),
    exportWordApi: () => smartCompatibilityApi.exportAllRulesWord(),
    importApiFn: (file, strategy) => smartCompatibilityApi.importMatrix(file, strategy as 'skip' | 'overwrite'),
    exportExcelFilename: '耐磨燙金紙選用規則',
    exportWordFilename: '耐磨燙金紙選用規則'
  },
  {
    id: 'common-interface',
    title: '常用界面燙印性 組合應用表',
    description: '燙金紙－常用界面燙印性（類型、系列、燙金圖案、燙金類型、常用界面、特殊界面）',
    docName: '《燙金：常用界面燙印性-指引書》',
    exportExcel: true,
    exportWord: true,
    importApi: true,
    exportExcelApi: () => commonInterfaceMatrixApi.exportExcel(),
    exportWordApi: () => commonInterfaceMatrixApi.exportWord(),
    importApiFn: (file, strategy) => commonInterfaceMatrixApi.importMatrix(file, strategy as 'skip' | 'overwrite')
  },
  {
    id: 'special-interface-cloth',
    title: '「布面紙+燙金」－組合應用表',
    description: '特殊界面布面紙與燙金紙系列的燙印性（材質、物料型號、燙金紙系列）',
    docName: '《特殊界面燙金：布面紙燙印性－指引書》',
    exportExcel: true,
    exportWord: true,
    importApi: true,
    exportExcelApi: () => specialInterfaceClothMatrixApi.exportExcel(),
    exportWordApi: () => specialInterfaceClothMatrixApi.exportWord(),
    importApiFn: (file, strategy) => specialInterfaceClothMatrixApi.importMatrix(file, strategy as 'skip' | 'overwrite')
  },
  {
    id: 'laminating-matrix',
    title: '燙金後過膠－應用限制',
    description: '後加工序燙金後過膠矩陣（適用界面、過膠種類、燙後過膠後加工 × 燙金紙選用）',
    docName: '《燙後加工："燙金 + 過膠"－組合應用指引書》',
    exportExcel: true,
    exportWord: true,
    importApi: true,
    exportExcelApi: () => laminatingMatrixApi.exportExcel(),
    exportWordApi: () => laminatingMatrixApi.exportWord(),
    importApiFn: (file, strategy) => laminatingMatrixApi.importMatrix(file, strategy as 'skip' | 'overwrite')
  },
  {
    id: 'print-uv-matrix',
    title: '燙金 + 印刷UV',
    description: '印刷UV 配置（普通全纸/镭射金纸/客人指定金纸：公司編號、供應商型號、結果）',
    docName: '《燙後加工－「燙金 + 印刷UV」指引書》',
    exportExcel: true,
    exportWord: true,
    importApi: true,
    exportExcelApi: () => postProcessingPrintUvMatrixApi.exportExcel(),
    exportWordApi: () => postProcessingPrintUvMatrixApi.exportWord(),
    importApiFn: (file, strategy) => postProcessingPrintUvMatrixApi.importMatrix(file, strategy as 'skip' | 'overwrite')
  },
  {
    id: 'print-matrix',
    title: '燙金 + 印刷',
    description: '印刷配置（界面、金纸选用、兼容性狀態），對應印刷配置管理',
    docName: '《燙後加工－「燙金 + 印刷」指引書》',
    exportExcel: true,
    exportWord: true,
    importApi: true,
    exportExcelApi: () => postProcessingPrintMatrixApi.exportExcel(),
    exportWordApi: () => postProcessingPrintMatrixApi.exportWord(),
    importApiFn: (file, strategy) => postProcessingPrintMatrixApi.importMatrix(file, strategy as 'skip' | 'overwrite')
  },
  {
    id: 'leduvglitter-matrix',
    title: '燙金 + 絲印LED UV灑閃粉',
    description: '配對燙金紙型號矩陣（加工重疊組合、燙金界面 × 普通/耐磨/鐳射燙金紙系列）',
    docName: '配對燙金紙型號（加工重疊組合：燙金 + 絲印LED UV灑閃粉）',
    exportExcel: true,
    exportWord: true,
    importApi: true,
    exportExcelApi: () => postProcessingLeduvglitterMatrixApi.exportExcel(),
    exportWordApi: () => postProcessingLeduvglitterMatrixApi.exportWord(),
    importApiFn: (file, strategy) => postProcessingLeduvglitterMatrixApi.importMatrix(file, strategy as 'skip' | 'overwrite'),
    exportExcelFilename: '燙金_絲印LED_UV灑閃粉_配對燙金紙型號',
    exportWordFilename: '燙金_絲印LED_UV灑閃粉_配對燙金紙型號'
  }
])

// 導出時給每項加 loading
function setExporting(id: string, value: boolean) {
  const item = matrixTypes.value.find(i => i.id === id)
  if (item) item.exporting = value
}

function downloadBlob(blob: Blob, filename: string) {
  const url = window.URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = filename
  document.body.appendChild(a)
  a.click()
  window.URL.revokeObjectURL(url)
  document.body.removeChild(a)
}

async function exportExcel(item: MatrixTypeItem) {
  if (!item.exportExcelApi) return
  setExporting(item.id, true)
  try {
    const blob = await item.exportExcelApi()
    const date = new Date().toISOString().slice(0, 10)
    const baseName = item.exportExcelFilename ?? item.title.replace(/\s+/g, '_')
    downloadBlob(blob, `${baseName}_${date}.xlsx`)
  } catch (e) {
    console.error(e)
    alert('导出 Excel 失败，请稍后重试')
  } finally {
    setExporting(item.id, false)
  }
}

async function exportWord(item: MatrixTypeItem) {
  if (!item.exportWordApi) return
  setExporting(item.id, true)
  try {
    const blob = await item.exportWordApi()
    const date = new Date().toISOString().slice(0, 10)
    const baseName = item.exportWordFilename ?? item.title.replace(/\s+/g, '_')
    downloadBlob(blob, `${baseName}_${date}.docx`)
  } catch (e: unknown) {
    console.error(e)
    const err = e as { message?: string; code?: string }
    const msg = err?.code === 'ECONNABORTED'
      ? '导出超时，数据较多时生成 Word 较慢，请稍后重试或联系管理员'
      : (err?.message ? `导出 Word 失败：${err.message}` : '导出 Word 失败，请稍后重试')
    alert(msg)
  } finally {
    setExporting(item.id, false)
  }
}

// 导入对话框
const importDialogVisible = ref(false)
const currentImportItem = ref<MatrixTypeItem | null>(null)
const selectedFile = ref<File | null>(null)
const conflictStrategy = ref<'skip' | 'overwrite'>('skip')
const importing = ref(false)
const importResult = ref<MatrixImportResult | null>(null)
const importFileInput = ref<HTMLInputElement | null>(null)
const downloadingTemplate = ref(false)

function openImportDialog(item: MatrixTypeItem) {
  if (!item.importApiFn) return
  currentImportItem.value = item
  selectedFile.value = null
  importResult.value = null
  conflictStrategy.value = 'skip'
  importDialogVisible.value = true
}

function closeImportDialog() {
  importDialogVisible.value = false
  currentImportItem.value = null
  selectedFile.value = null
  importResult.value = null
}

function onImportFileSelect(event: Event) {
  const target = event.target as HTMLInputElement
  if (target.files?.length) selectedFile.value = target.files[0]
}

async function downloadImportWordTemplate() {
  const item = currentImportItem.value
  if (!item?.exportWordApi) return
  downloadingTemplate.value = true
  try {
    const blob = await item.exportWordApi()
    const date = new Date().toISOString().slice(0, 10)
    const baseName = item.exportWordFilename ?? item.title.replace(/\s+/g, '_')
    downloadBlob(blob, `${baseName}_导入模板_${date}.docx`)
  } catch (e: unknown) {
    console.error(e)
    const err = e as { message?: string; code?: string }
    const msg = err?.code === 'ECONNABORTED'
      ? '下載超時，請稍後重試'
      : (err?.message ? `下載失敗：${err.message}` : '下載 Word 模板失敗')
    alert(msg)
  } finally {
    downloadingTemplate.value = false
  }
}

async function executeImport() {
  const item = currentImportItem.value
  if (!item?.importApiFn || !selectedFile.value) return
  importing.value = true
  try {
    const result = await item.importApiFn(selectedFile.value, conflictStrategy.value)
    importResult.value = result
  } catch (e: any) {
    console.error(e)
    let msg = e?.response?.data?.error ?? e?.message ?? '导入失败'
    if (msg.includes('服务器未响应') || msg.includes('timeout') || e?.code === 'ECONNABORTED') {
      msg = '导入超时或服务器未响应。请尝试：1) 缩小文件或减少数据行；2) 检查后端服务与日志；3) 稍后重试。'
    }
    alert(msg)
  } finally {
    importing.value = false
  }
}
</script>
