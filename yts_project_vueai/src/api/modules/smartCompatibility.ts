import request from '../request';

export interface SmartCompatibilityRule {
  id: number;
  productTypeId?: number;
  patternCharacteristicId?: number;
  hotStampingTypeId?: number;
  preProcessingStepId?: number;
  postProcessingStepId?: number;
  paperPerformance: string;
  compatibility: 'V' | 'X';
  patternType?: string;
  lineThickness?: string;
  solidAreaSize?: string;
  referenceCode?: string;
  noticeIds?: number[];
  createdAt: string;
  updatedAt: string;
}

export interface SmartCompatibilityRuleDetail {
  id: number;
  productTypeId?: number;
  patternCharacteristicId?: number;
  hotStampingTypeId?: number;
  preProcessingStepId?: number;
  postProcessingStepId?: number;
  paperPerformance: string;
  productType: string;
  patternCharacteristic: string;
  hotStampingType: string;
  compatibility: 'V' | 'X';
  patternType?: string;
  lineThicknessMin?: number;
  lineThicknessMax?: number;
  solidAreaMin?: number;
  solidAreaMax?: number;
  noticeIds?: number[];
  createdAt: string;
  updatedAt: string;
  // 关联表信息
  productTypeName?: string;
  patternCharacteristicName?: string;
  hotStampingTypeName?: string;
  preProcessingStepName?: string;
  postProcessingStepName?: string;
  // 关联表描述信息
  productTypeDescription?: string;
  patternTypeDescription?: string;
  hotStampingTypeDescription?: string;
  preProcessingStepDescription?: string;
  postProcessingStepDescription?: string;
  // 状态信息
  isActive?: boolean;
  sortOrder?: number;
}

export interface CompatibilityFilter {
  productTypeId?: number;
  patternCharacteristicId?: number;
  hotStampingTypeId?: number;
  paperPerformance?: string;
  compatibility?: 'V' | 'X';
  page?: number;
  size?: number;
}

export interface BatchOperation {
  ids: number[];
  operation: 'edit' | 'copy' | 'delete';
  data?: Partial<SmartCompatibilityRule>;
}

export interface CompatibilityMatrix {
  productTypes: string[];
  patternTypes: string[];
  hotStampingTypes: string[];
  rules: SmartCompatibilityRule[];
}

export const smartCompatibilityApi = {
  // 获取兼容性规则列表
  getRules: (params: CompatibilityFilter): Promise<{ items: SmartCompatibilityRule[]; total: number }> => {
    return request.get('/smart-compatibility/rules', { params });
  },

  // 获取单个兼容性规则
  getRule: (id: number): Promise<SmartCompatibilityRule> => {
    return request.get(`/smart-compatibility/rules/${id}`);
  },

  // 创建兼容性规则
  createRule: (data: Omit<SmartCompatibilityRule, 'id' | 'createdAt' | 'updatedAt'>): Promise<SmartCompatibilityRule> => {
    return request.post('/smart-compatibility/rules', data);
  },

  // 更新兼容性规则
  updateRule: (id: number, data: Partial<SmartCompatibilityRule>): Promise<SmartCompatibilityRule> => {
    return request.put(`/smart-compatibility/rules/${id}`, data);
  },

  // 删除兼容性规则
  deleteRule: (id: number): Promise<void> => {
    return request.delete(`/smart-compatibility/rules/${id}`);
  },

  // 批量操作
  batchOperation: (operation: BatchOperation): Promise<void> => {
    return request.post('/smart-compatibility/batch', operation);
  },

  // 获取兼容性矩阵
  getMatrix: (): Promise<CompatibilityMatrix> => {
    return request.get('/smart-compatibility/matrix');
  },

  // 获取选项数据
  getOptions: (): Promise<{
    productTypes: string[];
    patternTypes: string[];
    hotStampingTypes: string[];
  }> => {
    return request.get('/smart-compatibility/options');
  },

  // 验证兼容性规则
  validateRule: (data: Partial<SmartCompatibilityRule>): Promise<{
    isValid: boolean;
    conflicts: SmartCompatibilityRule[];
    suggestions: SmartCompatibilityRule[];
  }> => {
    return request.post('/smart-compatibility/validate', data);
  },

  // 导入兼容性规则
  importRules: (file: File): Promise<{ success: number; failed: number; errors: string[] }> => {
    const formData = new FormData();
    formData.append('file', file);
    return request.post('/smart-compatibility/import', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },

  // 导出兼容性规则
  exportRules: (filters?: CompatibilityFilter): Promise<Blob> => {
    return request.get('/smart-compatibility/export', {
      params: filters,
      responseType: 'blob',
    });
  },

  // 导出全部兼容性规则为 Excel（產品類型、圖案特徵、燙金類型、前工序及映射配置）
  exportAllRules: (): Promise<Blob> => {
    return request.get('/smart-compatibility/export-all', {
      responseType: 'blob',
    });
  },

  // 导出全部兼容性规则为 Word（矩阵结构）
  exportAllRulesWord: (): Promise<Blob> => {
    return request.get('/smart-compatibility/export-all-word', {
      responseType: 'blob',
    });
  },

  // 复制兼容性规则
  copyRule: (id: number, modifications?: Partial<SmartCompatibilityRule>): Promise<SmartCompatibilityRule> => {
    return request.post(`/smart-compatibility/rules/${id}/copy`, modifications);
  },

  // 获取规则统计信息
  getStatistics: (): Promise<{
    totalRules: number;
    compatibleRules: number;
    incompatibleRules: number;
    productTypeStats: { [key: string]: number };
    patternTypeStats: { [key: string]: number };
    hotStampingTypeStats: { [key: string]: number };
  }> => {
    return request.get('/smart-compatibility/statistics');
  },

  // 搜索兼容性规则
  searchRules: (query: string): Promise<SmartCompatibilityRule[]> => {
    return request.get('/smart-compatibility/search', {
      params: { q: query },
    });
  },

  // 获取规则预览
  getRulePreview: (data: Partial<SmartCompatibilityRule>): Promise<{
    preview: SmartCompatibilityRule;
    affectedRules: SmartCompatibilityRule[];
    impact: string;
  }> => {
    return request.post('/smart-compatibility/preview', data);
  },

  // 核心业务接口：根据多个ID字段筛选烫金纸性能类型
  filterPaperPerformance: (params: {
    productTypeId?: number;
    patternCharacteristicId?: number;
    hotStampingTypeId?: number;
    preProcessingStepId?: number;
    postProcessingStepId?: number;
  }): Promise<string[]> => {
    return request.get('/smart-compatibility/filter-paper-performance', { params });
  },

  // 获取完整的兼容性规则
  getCompatibilityRules: (params: {
    productTypeId?: number;
    patternCharacteristicId?: number;
    hotStampingTypeId?: number;
    preProcessingStepId?: number;
    postProcessingStepId?: number;
  }): Promise<SmartCompatibilityRule[]> => {
    return request.get('/smart-compatibility/compatibility-rules', { params });
  },

  // 获取完整的兼容性规则列表（包含关联表信息）
  getCompatibilityRulesWithDetails: (): Promise<SmartCompatibilityRuleDetail[]> => {
    return request.get('/smart-compatibility/rules-with-details');
  },

  /** 從矩陣文件導入耐磨燙金紙選用規則（.xlsx / .docx / .doc），與導出結構一致 */
  importMatrix: (file: File, conflictStrategy: 'overwrite' | 'skip' = 'skip'): Promise<{
    totalRows: number;
    created: number;
    updated: number;
    skipped: number;
    errors: number;
    errorMessages?: string[];
  }> => {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('conflictStrategy', conflictStrategy);
    return request.post('/smart-compatibility/import-matrix', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
      timeout: 120000,
    });
  },
};
                                                