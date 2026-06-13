import request from '../request';

export interface SiliconeOrangePeelUvProduct {
  id?: number;
  materialCode?: string;
  supplierCode?: string;
  stockCode?: string;
  materialName?: string;
  usageText?: string;
  materialType?: string;
  thickness?: string;
  sizeInfo?: string;
  color?: string;
  shape?: string;
  responsiblePerson?: string;
  minPaperSize?: string;
  maxPaperSize?: string;
  minDot?: string;
  maxDot?: string;
  minLine?: string;
  maxLine?: string;
  minSpacing?: string;
  maxSpacing?: string;
  minPatternArea?: string;
  maxPatternArea?: string;
  structureApplication?: string;
  singleSide?: string;
  doubleSide?: string;
  coverPage?: string;
  bookSpine?: string;
  pitPosition?: string;
  innerPage?: string;
  interfacePaperBaseThickness?: string;
  interfacePaperSuitable?: string;
  interfacePaperUnsuitable?: string;
  interfaceInkSuitable?: string;
  interfaceInkUnsuitable?: string;
  interfaceCoatingSuitable?: string;
  interfaceCoatingUnsuitable?: string;
  designInfo?: string;
  applicableInterface?: string;
  notes?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface SiliconeOrangePeelUvCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export const siliconeOrangePeelUvApi = {
  // ========== 产品管理 ==========

  /** 获取所有激活的产品 */
  getActiveProducts() {
    return request.get('/api/silicone_orange_peel_uv/products');
  },

  /** 获取所有产品（含未激活） */
  getAllProducts() {
    return request.get('/api/silicone_orange_peel_uv/products/all');
  },

  /** 根据ID获取产品 */
  getProductById(id: number) {
    return request.get(`/api/silicone_orange_peel_uv/products/${id}`);
  },

  /** 搜索产品 */
  searchProducts(keyword: string) {
    return request.get('/api/silicone_orange_peel_uv/products/search', { params: { keyword } });
  },

  /** 新增产品 */
  createProduct(data: SiliconeOrangePeelUvProduct) {
    return request.post('/api/silicone_orange_peel_uv/products', data);
  },

  /** 更新产品 */
  updateProduct(id: number, data: SiliconeOrangePeelUvProduct) {
    return request.put(`/api/silicone_orange_peel_uv/products/${id}`, data);
  },

  /** 删除产品 */
  deleteProduct(id: number) {
    return request.delete(`/api/silicone_orange_peel_uv/products/${id}`);
  },

  // ========== 兼容性管理 ==========

  /** 获取所有兼容性列表 */
  getAllCompatibilities() {
    return request.get('/api/silicone_orange_peel_uv/compatibilities');
  },

  /** 根据产品ID获取兼容性列表 */
  getCompatibilitiesByProductId(productId: number) {
    return request.get(`/api/silicone_orange_peel_uv/compatibilities/product/${productId}`);
  },

  /** 根据ID获取兼容性 */
  getCompatibilityById(id: number) {
    return request.get(`/api/silicone_orange_peel_uv/compatibilities/${id}`);
  },

  /** 新增兼容性 */
  createCompatibility(data: SiliconeOrangePeelUvCompatibility) {
    return request.post('/api/silicone_orange_peel_uv/compatibilities', data);
  },

  /** 更新兼容性 */
  updateCompatibility(id: number, data: SiliconeOrangePeelUvCompatibility) {
    return request.put(`/api/silicone_orange_peel_uv/compatibilities/${id}`, data);
  },

  /** 批量保存兼容性 */
  batchSaveCompatibilities(data: SiliconeOrangePeelUvCompatibility[]) {
    return request.post('/api/silicone_orange_peel_uv/compatibilities/batch', data);
  },

  /** 删除兼容性 */
  deleteCompatibility(id: number) {
    return request.delete(`/api/silicone_orange_peel_uv/compatibilities/${id}`);
  },

  /** 根据产品ID删除所有兼容性 */
  deleteCompatibilitiesByProductId(productId: number) {
    return request.delete(`/api/silicone_orange_peel_uv/compatibilities/product/${productId}`);
  },

  /** 获取所有后加工工序名称 */
  getAllPostProcessingSteps() {
    return request.get('/api/silicone_orange_peel_uv/post-processing-steps');
  }
};
