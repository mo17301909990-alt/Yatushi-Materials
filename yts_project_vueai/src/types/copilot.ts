import type { GoldFoilProduct } from './goldFoil'
import type { ChannelType, CardPayload, ActionButton } from './channel'

// 来源引用
export interface SourceRef {
  title: string;
  confidence?: number;
}

export interface ChatMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  timestamp: number;
  suggestions?: string[];
  products?: GoldFoilProduct[];
  filteredTotal?: number; // 匹配 API 返回的真实筛选总数
  sources?: SourceRef[];
  channel?: ChannelType
  cardPayload?: CardPayload
  actions?: ActionButton[]
  isStreaming?: boolean;
  cardType?: MessageCardType;
  cardData?: ProductCardData | CompatibilityTableData | ActionCardData | AdminPreviewData | AdminResultData;
}

export type AgentRole = 'general' | 'matching' | 'guide'

export type ConversationStage =
  | 'idle'
  | 'selecting-product'
  | 'selecting-pattern'
  | 'selecting-stamping'
  | 'selecting-paper'
  | 'selecting-preprocess'
  | 'selecting-postprocess'
  | 'complete'

export const GUIDE_STAGES: { stage: ConversationStage; label: string }[] = [
  { stage: 'selecting-product', label: '選擇產品類型' },
  { stage: 'selecting-pattern', label: '選擇燙金圖案' },
  { stage: 'selecting-stamping', label: '選擇燙金類型' },
  { stage: 'selecting-paper', label: '選擇紙張類型' },
  { stage: 'selecting-preprocess', label: '選擇前處理' },
  { stage: 'selecting-postprocess', label: '選擇後加工' },
]

export const STAGE_SYSTEM_PROMPTS: Record<ConversationStage, string> = {
  idle: '',
  'selecting-product': '你是印刷工藝選型助手。當前引導步驟：選擇產品類型。請向用戶介紹精平裝/咭書(對應ID:1)、賀咭/紙袋(對應ID:2)、包裝(對應ID:3)三種產品類型的特點，並引導用戶選擇一個。',
  'selecting-pattern': '你是印刷工藝選型助手。當前引導步驟：選擇燙金圖案類型。請向用戶介紹不同圖案類型（淨線條、混合圖案、淨實地等）的特點，引導用戶選擇。',
  'selecting-stamping': '你是印刷工藝選型助手。當前引導步驟：選擇燙金類型。請介紹平面燙金、立體燙金、磨砂燙金等選項，引導用戶選擇。',
  'selecting-paper': '你是印刷工藝選型助手。當前引導步驟：選擇布料紙類型。引導用戶選擇適用的布料紙材料。',
  'selecting-preprocess': '你是印刷工藝選型助手。當前引導步驟：選擇前處理工序。引導用戶選擇適用的前處理選項。',
  'selecting-postprocess': '你是印刷工藝選型助手。當前引導步驟：選擇後加工工序。引導用戶選擇過膠、UV印刷、擊凸等選項。',
  complete: '你是印刷工藝選型助手。引導已完成，請總結用戶的選擇並給出綜合工藝建議。',
}

// ====== 交互式消息卡片类型 ======
export type MessageCardType = 'text' | 'product_card' | 'compatibility_table' | 'action_buttons'
  | 'admin_preview' | 'admin_result' | 'admin_dialog'

export interface AdminPreviewData {
  entityType: string
  entityId: number
  currentValues: Record<string, unknown>
  proposedChanges: Record<string, unknown>
  risk: string
}

export interface AdminResultData {
  recordId: number
  entityType: string
  entityId: number
  status: string
  changeSet?: string
}

export interface AdminDialogData {
  title: string
  message: string
  risk: string
}

export interface ProductCardData {
  materialNumber: string
  color: string
  size: string
  tightness: string
  /** e.g. { hotStamping: 'compatible', screenPrinting: 'incompatible', laminating: 'compatible', postProcessing: 'partial' } */
  compatibility: Record<string, 'compatible' | 'incompatible' | 'partial'>
  postProcesses: string[]
}

export interface CompatibilityTableData {
  headers: string[]
  rows: string[][]
}

export interface ActionCardData {
  actions: { id: string; label: string; primary: boolean }[]
}

export const ROLE_LABELS: Record<AgentRole, string> = {
  general: '通用助手',
  matching: '匹配專家',
  guide: '工藝引導',
}

export const ROLE_DEFAULTS: Record<AgentRole, string> = {
  general: '你是印刷廠管理系統的 Copilot 助手，回答用戶關於系統使用、工藝流程、物料管理的問題。回答簡潔準確。',
  matching: '你是燙金紙匹配專家。根據用戶描述的需求，推導出篩選參數幫助用戶找到合適的燙金紙。分析用戶需求時，在回覆末尾用 ===PARAMS=== 包裹 JSON 格式的推薦參數。注意參數使用 Integer ID 格式而非文本，例如：===PARAMS==={ "productTypeId": 1, "hotStampingTypeOptionId": 3 }===PARAMS===。可用的參數包括：productTypeId(產品類型ID)、patternId(燙金圖案ID)、hotStampingTypeOptionId(燙金類型ID)、preProcessingStepsOptionId(前工序ID)、clothPaperTypeId(布料紙ID)、patternAreaOptionId(圖案區域ID)、postProcessingStepId(後加工步驟ID)、laminationMaterialId(過膠材質ID)、priceLevel(價格優先度)、paperPerformance(紙性能)、paizi(牌子)、colorNum(顏色編碼)。',
  guide: '你是印刷工藝選型引導專家。按照分步引導流程幫助用戶完成工藝選型。每一步只問一個問題，收集完整後總結。',
}
