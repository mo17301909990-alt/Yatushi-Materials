import axios from 'axios'

export interface BatchNoticeUpdateRequest {
  ids: number[]
  noticeIds: number[]
  mode: 'append' | 'replace' | 'clear'
}

export interface BatchNoticeUpdateResponse {
  success: boolean
  message: string
  updatedCount?: number
}

/**
 * 批量更新注意事项API
 */
export const batchNoticeApi = {
  /**
   * 批量更新指定规则类型的注意事项
   * @param ruleType 规则类型
   * @param request 批量更新请求
   */
  batchUpdateNotices: async (ruleType: string, request: BatchNoticeUpdateRequest): Promise<BatchNoticeUpdateResponse> => {
    const response = await axios.post<BatchNoticeUpdateResponse>(
      `/api/batch-notice/update/${ruleType}`,
      request
    )
    return response.data
  }
}

// 规则类型常量
export const RULE_TYPES = {
  POST_PROCESSING_PRINT: 'post_processing_print',
  POST_PROCESSING_LEDUVGLITTER: 'post_processing_leduvglitter',
  POST_PROCESSING_PRINT_UV: 'post_processing_print_uv',
  LAMINATION_COMPATIBILITY: 'lamination_compatibility',
  WEAR_RESISTANT_GOLD_PAPER_MAPPING: 'wear_resistant_gold_paper_mapping',
  HOT_STAMPING_PATTERN_BASE: 'hot_stamping_pattern_base',
  HOT_STAMPING_TYPE_OPTIONS: 'hot_stamping_type_options',
  HOT_STAMPING_PATTERN_AREA_OPTION: 'hot_stamping_pattern_area_option',
  PRODUCT_TYPE_OPTIONS: 'product_type_options',
  CLOTH_PAPER_TYPE: 'cloth_paper_type',
  PRE_PROCESSING_STEPS: 'pre_processing_steps',
  CLOTH_PAPER_COMPATIBILITY: 'cloth_paper_compatibility'
} as const

export type RuleType = typeof RULE_TYPES[keyof typeof RULE_TYPES]

