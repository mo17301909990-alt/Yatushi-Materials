import request from '../request';

/** 硅胶产品匹配结果 */
export interface SiliconeProduct {
  id?: number;
  materialCode?: string;
  supplierCode?: string;
  stockCode?: string;
  materialName?: string;
  usage?: string;
  category?: string;
  color?: string;
  thickness?: string;
  shape?: string;
  tester?: string;
  responsiblePerson?: string;
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

  /** 硅胶类型标识 */
  siliconeType?: string;
  /** 硅胶类型中文标签 */
  siliconeTypeLabel?: string;
  /** 兼容性状态（V/X/null），仅单步骤匹配查询时返回 */
  compatibilityStatus?: string;
  /** 多步骤匹配时，每步骤兼容性状态映射 */
  compatibilityStatusMap?: Record<string, string>;
}

/** 硅胶匹配查询参数 */
export interface SiliconeMatchParams {
  types?: string[];
  keyword?: string;
  steps?: string[];
  page?: number;
  size?: number;
}

export interface PagedItems<T> {
  items: T[];
  total: number;
  pageSize: number;
  currentPage: number;
  totalPages: number;
}

/** 后加工工序分类分组 */
export interface StepCategoryGroup {
  category: string;
  steps: string[];
}

/** 硅胶类型选项 */
export interface SiliconeTypeOption {
  value: string;
  label: string;
}

/** 所有硅胶类型选项（前端预置） */
export const SILICONE_TYPE_OPTIONS: SiliconeTypeOption[] = [
  { value: 'glow_ink', label: '发光油墨' },
  { value: 'white_uv', label: '白UV' },
  { value: 'screen_uv', label: '丝印UV' },
  { value: 'coarse_uv', label: '粗纹UV' },
  { value: 'orange_peel_uv', label: '橘皮UV' },
  { value: 'sandblast_uv', label: '喷砂UV' },
  { value: 'wrinkle_uv', label: '皱纹UV' },
  { value: 'watercolor_ink', label: '水彩油墨' },
  { value: 'mica_pearl', label: '云母珍珠' },
  { value: 'glittering_star', label: '闪烁星' },
  { value: 'led_uv_spray', label: 'LED UV喷' },
  { value: 'water_base_transparent', label: '水性透明' },
];

export const siliconeApi = {
  /** 组合匹配查询 */
  match(params: SiliconeMatchParams) {
    return request.post<PagedItems<SiliconeProduct>>('/silicone/match', params);
  },

  /** 获取后加工工序步骤（跨硅胶类型） */
  getSteps(types?: string[]) {
    return request.get<StepCategoryGroup[]>('/silicone/steps', {
      params: types ? { types: types.join(',') } : {},
    });
  },

  /** 获取产品详情（含兼容性列表） */
  getProductDetail(type: string, id: number) {
    return request.get<SiliconeProduct>(`/silicone/products/${type}/${id}/detail`);
  },
};
