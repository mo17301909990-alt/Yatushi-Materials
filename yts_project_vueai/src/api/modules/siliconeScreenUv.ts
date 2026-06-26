import request from '../request';

export interface SiliconeScreenUvProduct {
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

export interface SiliconeScreenUvCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeScreenUvMatchParams {
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

export const siliconeScreenUvApi = {
  getAllProducts() { return request.get('/silicone_screen_uv/products'); },
  getProductById(id: number) { return request.get(`/silicone_screen_uv/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_screen_uv/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeScreenUvProduct) { return request.post('/silicone_screen_uv/products', product); },
  updateProduct(id: number, product: SiliconeScreenUvProduct) { return request.put(`/silicone_screen_uv/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_screen_uv/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_screen_uv/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_screen_uv/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeScreenUvCompatibility) { return request.post('/silicone_screen_uv/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeScreenUvCompatibility) {
    return request.put(`/silicone_screen_uv/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_screen_uv/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeScreenUvCompatibility[]) {
    return request.post('/silicone_screen_uv/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_screen_uv/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeScreenUvMatchParams) {
    return request.post<PagedItems<SiliconeScreenUvProduct>>('/silicone_screen_uv/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_screen_uv/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_screen_uv/products/${id}/detail`); },
};
