import request from '../request';

/** LEO纸品 统一产品视图 */
export interface LeoProductVO {
  id?: number;
  sourceType?: 'book_board' | 'flat' | 'non_flat';
  materialCode?: string;
  supplierCode?: string;
  stockCode?: string;
  materialName?: string;
  usage?: string;
  category?: string;
  color?: string;
  responsiblePerson?: string;
  /** 厚度 */
  thickness?: string;
  /** 形状/克重 */
  shape?: string;
  /** 检测员 */
  tester?: string;
  minSheetSize?: string;
  maxSheetSize?: string;
  minSpacing?: string;
  maxSpacing?: string;
  designInfo?: string;
  applicableInterface?: string;
  notes?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
  /** 多步骤匹配时，每步骤的兼容性状态映射 */
  compatibilityStatusMap?: Record<string, string>;
}

/** LEO纸品 组合匹配查询参数 */
export interface LeoMatchParams {
  /** 贴纸类型：book_board / flat / non_flat，默认全部 */
  types?: string[];
  /** 后加工工序步骤列表（多步骤 INTERSECT 查询） */
  steps?: string[];
  /** 搜索关键词 */
  keyword?: string;
  /** 当前页码 */
  page?: number;
  /** 每页条数 */
  size?: number;
}

/** 后加工工序分类分组 */
export interface StepCategoryGroup {
  category: string;
  steps: string[];
}

/** 分页结果 */
export interface PagedItems<T> {
  items: T[];
  total: number;
  pageSize: number;
  currentPage: number;
  totalPages: number;
}

export const leoApi = {
  /** 组合匹配查询 */
  match(params: LeoMatchParams) {
    return request.post<PagedItems<LeoProductVO>>('/leo/match', params);
  },

  /** 获取所有后加工工序（按大类分组） */
  getSteps() {
    return request.get<StepCategoryGroup[]>('/leo/steps');
  },
};
