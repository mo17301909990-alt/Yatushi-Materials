import request from '../request';

export interface SiliconeOrangePeelUvProduct {
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

export interface SiliconeOrangePeelUvCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeOrangePeelUvMatchParams {
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

export const siliconeOrangePeelUvApi = {
  getAllProducts() { return request.get('/silicone_orange_peel_uv/products'); },
  getProductById(id: number) { return request.get(`/silicone_orange_peel_uv/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_orange_peel_uv/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeOrangePeelUvProduct) { return request.post('/silicone_orange_peel_uv/products', product); },
  updateProduct(id: number, product: SiliconeOrangePeelUvProduct) { return request.put(`/silicone_orange_peel_uv/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_orange_peel_uv/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_orange_peel_uv/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_orange_peel_uv/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeOrangePeelUvCompatibility) { return request.post('/silicone_orange_peel_uv/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeOrangePeelUvCompatibility) {
    return request.put(`/silicone_orange_peel_uv/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_orange_peel_uv/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeOrangePeelUvCompatibility[]) {
    return request.post('/silicone_orange_peel_uv/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_orange_peel_uv/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeOrangePeelUvMatchParams) {
    return request.post<PagedItems<SiliconeOrangePeelUvProduct>>('/silicone_orange_peel_uv/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_orange_peel_uv/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_orange_peel_uv/products/${id}/detail`); },
};
