import request from '../request';

/** 过胶材料产品 */
export interface LaminationMaterialProduct {
  id?: number;
  materialCode?: string;
  stockCode?: string;
  materialName: string;
  usageText?: string;
  materialType?: string;
  thicknessFilm?: string;
  thicknessGlue?: string;
  sizeInfo?: string;
  color?: string;
  shape?: string;
  responsiblePerson?: string;
  category?: string;
  isActive?: boolean;
  filmThickness?: string;
  createdAt?: string;
  updatedAt?: string;
}

/** 过胶材料兼容性 */
export interface LaminationMaterialCompatibility {
  id?: number;
  productId: number;
  postProcessingStep: string;
  compatibilityStatus: string;
  remark?: string;
  displayOrder?: number;
  createdAt?: string;
}

// ========== 匹配查询接口 ==========

export interface LaminationMaterialMatchParams {
  keyword?: string;
  /** 材料类型筛选（BOPP/PET/BOPA等，多选） */
  materialTypes?: string[];
  /** 后加工工序步骤名称筛选（多选） */
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

/**
 * 过胶材料产品管理 API
 */
export const laminationMaterialApi = {
  // ========== 产品管理 ==========

  /** 获取所有产品 */
  getProducts() {
    return request.get<LaminationMaterialProduct[]>('/lamination-material/products');
  },

  /** 根据 ID 获取产品 */
  getProductById(id: number) {
    return request.get<LaminationMaterialProduct>(`/lamination-material/products/${id}`);
  },

  /** 搜索产品 */
  searchProducts(keyword: string) {
    return request.get<LaminationMaterialProduct[]>('/lamination-material/products/search', { params: { keyword } });
  },

  /** 新增产品 */
  createProduct(product: LaminationMaterialProduct) {
    return request.post<LaminationMaterialProduct>('/lamination-material/products', product);
  },

  /** 更新产品 */
  updateProduct(id: number, product: LaminationMaterialProduct) {
    return request.put<LaminationMaterialProduct>(`/lamination-material/products/${id}`, product);
  },

  /** 删除产品 */
  deleteProduct(id: number) {
    return request.delete(`/lamination-material/products/${id}`);
  },

  // ========== 兼容性管理 ==========

  /** 获取某产品的兼容性列表 */
  getCompatibilitiesByProductId(productId: number) {
    return request.get<LaminationMaterialCompatibility[]>(`/lamination-material/compatibilities/by-product/${productId}`);
  },

  /** 新增兼容性 */
  createCompatibility(compatibility: LaminationMaterialCompatibility) {
    return request.post<LaminationMaterialCompatibility>('/lamination-material/compatibilities', compatibility);
  },

  /** 更新兼容性 */
  updateCompatibility(id: number, compatibility: LaminationMaterialCompatibility) {
    return request.put<LaminationMaterialCompatibility>(`/lamination-material/compatibilities/${id}`, compatibility);
  },

  /** 删除兼容性 */
  deleteCompatibility(id: number) {
    return request.delete(`/lamination-material/compatibilities/${id}`);
  },

  // ========== 匹配查询（前端匹配页面使用） ==========

  /** 匹配查询：关键词搜索 + 工序筛选 + 分页 */
  match(params: LaminationMaterialMatchParams) {
    return request.post<PagedItems<LaminationMaterialProduct>>('/lamination-material/match', params);
  },

  /** 获取所有后加工工序步骤名称（去重） */
  getSteps() {
    return request.get<string[]>('/lamination-material/steps');
  },

  /** 获取所有材料类型（去重） */
  getMaterialTypes() {
    return request.get<string[]>('/lamination-material/material-types');
  },

  /** 获取产品详情（含兼容性列表） */
  getProductDetail(id: number) {
    return request.get(`/lamination-material/products/${id}/detail`);
  },
};
