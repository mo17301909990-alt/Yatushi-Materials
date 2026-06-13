import request from '../request';
import type { AgentConfig, MaterialCategory, MaterialStandard } from '@/types/agent';

export const agentApi = {
  /** 获取工艺 Agent 完整配置 */
  getConfig() {
    return request({
      url: '/agent/config',
      method: 'get',
    }) as Promise<AgentConfig>;
  },

  /** 获取所有材料分类（含标准书列表） */
  getCategories() {
    return request({
      url: '/agent/categories',
      method: 'get',
    }) as Promise<MaterialCategory[]>;
  },

  /** 获取指定分类的标准书列表 */
  getStandardsByCategory(categoryId: string) {
    return request({
      url: `/agent/category/${categoryId}/standards`,
      method: 'get',
    }) as Promise<MaterialStandard[]>;
  },
};
