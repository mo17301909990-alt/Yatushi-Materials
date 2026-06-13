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

/**
 * 过胶材料产品管理 API
 */
export const laminationMaterialApi = {
  // ========== 产品管理 ==========

  /** 获取所有产品 */
  getProducts() {
    return request.get<LaminationMaterialProduct[]>('/api/lamination-material/products');
  },

  /** 根据 ID 获取产品 */
  getProductById(id: number) {
    return request.get<LaminationMaterialProduct>(`/api/lamination-material/products/${id}`);
  },

  /** 搜索产品 */
  searchProducts(keyword: string) {
    return request.get<LaminationMaterialProduct[]>('/api/lamination-material/products/search', { params: { keyword } });
  },

  /** 新增产品 */
  createProduct(product: LaminationMaterialProduct) {
    return request.post<LaminationMaterialProduct>('/api/lamination-material/products', product);
  },

  /** 更新产品 */
  updateProduct(id: number, product: LaminationMaterialProduct) {
    return request.put<LaminationMaterialProduct>(`/api/lamination-material/products/${id}`, product);
  },

  /** 删除产品 */
  deleteProduct(id: number) {
    return request.delete(`/api/lamination-material/products/${id}`);
  },

  // ========== 兼容性管理 ==========

  /** 获取某产品的兼容性列表 */
  getCompatibilitiesByProductId(productId: number) {
    return request.get<LaminationMaterialCompatibility[]>(`/api/lamination-material/compatibilities/by-product/${productId}`);
  },

  /** 新增兼容性 */
  createCompatibility(compatibility: LaminationMaterialCompatibility) {
    return request.post<LaminationMaterialCompatibility>('/api/lamination-material/compatibilities', compatibility);
  },

  /** 更新兼容性 */
  updateCompatibility(id: number, compatibility: LaminationMaterialCompatibility) {
    return request.put<LaminationMaterialCompatibility>(`/api/lamination-material/compatibilities/${id}`, compatibility);
  },

  /** 删除兼容性 */
  deleteCompatibility(id: number) {
    return request.delete(`/api/lamination-material/compatibilities/${id}`);
  }
};
