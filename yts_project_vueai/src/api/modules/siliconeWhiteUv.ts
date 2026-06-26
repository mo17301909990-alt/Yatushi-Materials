import request from '../request';

export interface SiliconeWhiteUvProduct {
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

export interface SiliconeWhiteUvCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeWhiteUvMatchParams {
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

export const siliconeWhiteUvApi = {
  getAllProducts() { return request.get('/silicone_white_uv/products'); },
  getProductById(id: number) { return request.get(`/silicone_white_uv/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_white_uv/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeWhiteUvProduct) { return request.post('/silicone_white_uv/products', product); },
  updateProduct(id: number, product: SiliconeWhiteUvProduct) { return request.put(`/silicone_white_uv/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_white_uv/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_white_uv/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_white_uv/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeWhiteUvCompatibility) { return request.post('/silicone_white_uv/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeWhiteUvCompatibility) {
    return request.put(`/silicone_white_uv/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_white_uv/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeWhiteUvCompatibility[]) {
    return request.post('/silicone_white_uv/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_white_uv/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeWhiteUvMatchParams) {
    return request.post<PagedItems<SiliconeWhiteUvProduct>>('/silicone_white_uv/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_white_uv/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_white_uv/products/${id}/detail`); },
};
