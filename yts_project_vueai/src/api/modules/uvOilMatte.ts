import request from '../request';

export interface UvOilMatteProduct {
  id?: number;
  materialCode?: string;
  supplierCode?: string;
  stockCode?: string;
  materialName?: string;
  usage?: string;
  category?: string;
  color?: string;
  /** 厚度 */
  thickness?: string;
  /** 形状 */
  shape?: string;
  responsiblePerson?: string;
  minSheetSize?: string;
  maxSheetSize?: string;
  minSpacing?: string;
  designInfo?: string;
  applicableInterface?: string;
  notes?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
  /** 兼容性状态（V/X/null），仅匹配查询时返回 */
  compatibilityStatus?: string;
  /** 多步骤匹配时，每步骤兼容性状态映射 */
  compatibilityStatusMap?: Record<string, string>;
}

export interface UvOilMatteCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

/** 后加工工序分类分组 */
export interface StepCategoryGroup {
  /** 大类名称（印刷/烫金/过胶/丝印/植毛/啤/手工/其他） */
  category: string;
  /** 该大类下的所有具体步骤 */
  steps: string[];
}

// ========== 匹配查询接口 ==========

export interface UvOilMatteMatchParams {
  keyword?: string;
  stepName?: string;
  /** 多步骤 INTERSECT 查询（与 stepName 二选一） */
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

export const uvOilMatteApi = {
  // ========== 产品管理 ==========

  /** 获取所有产品 */
  getAllProducts() {
    return request.get('/uv_oil_matte/products');
  },

  /** 根据ID获取产品 */
  getProductById(id: number) {
    return request.get(`/uv_oil_matte/products/${id}`);
  },

  /** 搜索产品 */
  searchProducts(keyword: string) {
    return request.get('/uv_oil_matte/products/search', { params: { keyword } });
  },

  /** 新增产品 */
  createProduct(product: UvOilMatteProduct) {
    return request.post('/uv_oil_matte/products', product);
  },

  /** 更新产品 */
  updateProduct(id: number, product: UvOilMatteProduct) {
    return request.put(`/uv_oil_matte/products/${id}`, product);
  },

  /** 删除产品 */
  deleteProduct(id: number) {
    return request.delete(`/uv_oil_matte/products/${id}`);
  },

  // ========== 兼容性管理 ==========

  /** 根据产品ID获取兼容性列表 */
  getCompatibilitiesByProductId(productId: number) {
    return request.get('/uv_oil_matte/compatibilities', { params: { productId } });
  },

  /** 根据ID获取兼容性 */
  getCompatibilityById(id: number) {
    return request.get(`/uv_oil_matte/compatibilities/${id}`);
  },

  /** 新增兼容性 */
  createCompatibility(compatibility: UvOilMatteCompatibility) {
    return request.post('/uv_oil_matte/compatibilities', compatibility);
  },

  /** 更新兼容性 */
  updateCompatibility(id: number, compatibility: UvOilMatteCompatibility) {
    return request.put(`/uv_oil_matte/compatibilities/${id}`, compatibility);
  },

  /** 删除兼容性 */
  deleteCompatibility(id: number) {
    return request.delete(`/uv_oil_matte/compatibilities/${id}`);
  },

  /** 批量保存兼容性 */
  batchSaveCompatibilities(compatibilities: UvOilMatteCompatibility[]) {
    return request.post('/uv_oil_matte/compatibilities/batch', compatibilities);
  },

  /** 批量删除兼容性 */
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/uv_oil_matte/compatibilities/batch', { data: ids });
  },

  // ========== 匹配查询（前端匹配页面使用） ==========

  /** 匹配查询：关键词搜索 + 工序筛选 + 分页 */
  match(params: UvOilMatteMatchParams) {
    return request.post<PagedItems<UvOilMatteProduct>>('/uv_oil_matte/match', params);
  },

  /** 获取后加工工序步骤（按大类分组） */
  getSteps() {
    return request.get<StepCategoryGroup[]>('/uv_oil_matte/steps');
  },

  /** 获取产品详情（含兼容性列表） */
  getProductDetail(id: number) {
    return request.get(`/uv_oil_matte/products/${id}/detail`);
  },
};
