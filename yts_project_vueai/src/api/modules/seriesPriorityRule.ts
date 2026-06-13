import request from '../request';

/** 系列优先级规则（用于 7.適用界面 如珍珠紙面/掃金粉面的 SK、L817 排序） */
export interface SeriesPriorityRule {
  id: number;
  ruleCode?: string;
  ruleName: string;
  sortOrder: number;
  isActive: boolean;
  items?: SeriesPriorityRuleItem[];
}

/** 档位：优先级 + 系列名（可任意配置，如 SK、L817 等）或“其余按价格” */
export interface SeriesPriorityRuleItem {
  id?: number;
  ruleId?: number;
  priorityOrder: number;
  seriesNames?: string;
  isPriceFallback?: boolean;
  remark?: string;
}

export const seriesPriorityRuleApi = {
  list: (): Promise<SeriesPriorityRule[]> =>
    request.get('/series-priority-rules'),

  getWithItems: (id: number): Promise<SeriesPriorityRule> =>
    request.get(`/series-priority-rules/${id}`),

  create: (body: { ruleName: string; sortOrder?: number; isActive?: boolean }): Promise<SeriesPriorityRule> =>
    request.post('/series-priority-rules', body),

  update: (id: number, body: { ruleName?: string; sortOrder?: number; isActive?: boolean }): Promise<void> =>
    request.put(`/series-priority-rules/${id}`, body),

  delete: (id: number): Promise<void> =>
    request.delete(`/series-priority-rules/${id}`),

  saveItems: (id: number, items: SeriesPriorityRuleItem[]): Promise<void> =>
    request.put(`/series-priority-rules/${id}/items`, items),
};
