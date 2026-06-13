import request from '../request';
import type { AxiosResponse } from 'axios';

export interface UvOilMatteProduct {
  id?: number;
  materialCode?: string;
  supplierCode?: string;
  stockCode?: string;
  materialName?: string;
  usage?: string;
  category?: string;
  color?: string;
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
};
