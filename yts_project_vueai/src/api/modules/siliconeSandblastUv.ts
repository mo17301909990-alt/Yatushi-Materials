import request from '../request';

export interface SiliconeSandblastUvProduct {
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

export interface SiliconeSandblastUvCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeSandblastUvMatchParams {
  keyword?: string;
  stepName?: string;
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

export const siliconeSandblastUvApi = {
  getAllProducts() { return request.get('/silicone_sandblast_uv/products'); },
  getProductById(id: number) { return request.get(`/silicone_sandblast_uv/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_sandblast_uv/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeSandblastUvProduct) { return request.post('/silicone_sandblast_uv/products', product); },
  updateProduct(id: number, product: SiliconeSandblastUvProduct) { return request.put(`/silicone_sandblast_uv/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_sandblast_uv/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_sandblast_uv/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_sandblast_uv/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeSandblastUvCompatibility) { return request.post('/silicone_sandblast_uv/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeSandblastUvCompatibility) {
    return request.put(`/silicone_sandblast_uv/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_sandblast_uv/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeSandblastUvCompatibility[]) {
    return request.post('/silicone_sandblast_uv/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_sandblast_uv/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeSandblastUvMatchParams) {
    return request.post<PagedItems<SiliconeSandblastUvProduct>>('/silicone_sandblast_uv/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_sandblast_uv/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_sandblast_uv/products/${id}/detail`); },
};
