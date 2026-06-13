import request from '../request';

export interface YaguangUvOilProduct {
  id?: number;
  materialCode?: string;
  supplierCode?: string;
  stockCode?: string;
  materialName?: string;
  usage?: string;
  category?: string;
  thickness?: string;
  size?: string;
  color?: string;
  shape?: string;
  responsiblePerson?: string;
  minSheetSize?: string;
  maxSheetSize?: string;
  minDot?: string;
  maxDot?: string;
  minLine?: string;
  maxLine?: string;
  minSpacing?: string;
  maxSpacing?: string;
  minPatternArea?: string;
  maxPatternArea?: string;
  applicableProduct?: string;
  structureSingleSide?: string;
  structureDoubleSide?: string;
  structureCover?: string;
  structureSpine?: string;
  structureDeboss?: string;
  structureInnerPage?: string;
  substrateThickness?: string;
  paperSurfaceApplicable?: string;
  paperSurfaceInapplicable?: string;
  inkSurfaceApplicable?: string;
  inkSurfaceInapplicable?: string;
  coatingSurfaceApplicable?: string;
  coatingSurfaceInapplicable?: string;
  writingStandardPen?: string;
  writingCustomerPen?: string;
  notes?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface YaguangUvOilCompatibility {
  id?: number;
  productId?: number;
  processCategory?: string;
  stepName?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export const yaguangUvOilApi = {
  // ========== 产品管理 ==========

  /** 获取所有产品 */
  getAllProducts() {
    return request.get('/yaguang_uv_oil/products');
  },

  /** 根据ID获取产品 */
  getProductById(id: number) {
    return request.get(`/yaguang_uv_oil/products/${id}`);
  },

  /** 搜索产品 */
  searchProducts(keyword: string) {
    return request.get('/yaguang_uv_oil/products/search', { params: { keyword } });
  },

  /** 新增产品 */
  createProduct(product: YaguangUvOilProduct) {
    return request.post('/yaguang_uv_oil/products', product);
  },

  /** 更新产品 */
  updateProduct(id: number, product: YaguangUvOilProduct) {
    return request.put(`/yaguang_uv_oil/products/${id}`, product);
  },

  /** 删除产品 */
  deleteProduct(id: number) {
    return request.delete(`/yaguang_uv_oil/products/${id}`);
  },

  // ========== 兼容性管理 ==========

  /** 根据产品ID获取兼容性列表 */
  getCompatibilitiesByProductId(productId: number) {
    return request.get('/yaguang_uv_oil/compatibilities', { params: { productId } });
  },

  /** 根据ID获取兼容性 */
  getCompatibilityById(id: number) {
    return request.get(`/yaguang_uv_oil/compatibilities/${id}`);
  },

  /** 新增兼容性 */
  createCompatibility(compatibility: YaguangUvOilCompatibility) {
    return request.post('/yaguang_uv_oil/compatibilities', compatibility);
  },

  /** 更新兼容性 */
  updateCompatibility(id: number, compatibility: YaguangUvOilCompatibility) {
    return request.put(`/yaguang_uv_oil/compatibilities/${id}`, compatibility);
  },

  /** 删除兼容性 */
  deleteCompatibility(id: number) {
    return request.delete(`/yaguang_uv_oil/compatibilities/${id}`);
  },

  /** 批量保存兼容性 */
  batchSaveCompatibilities(compatibilities: YaguangUvOilCompatibility[]) {
    return request.post('/yaguang_uv_oil/compatibilities/batch', compatibilities);
  },

  /** 批量删除兼容性 */
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/yaguang_uv_oil/compatibilities/batch', { data: ids });
  },
};
