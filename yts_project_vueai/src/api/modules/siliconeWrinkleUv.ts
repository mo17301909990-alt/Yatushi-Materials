import request from '../request';

export interface SiliconeWrinkleUvProduct {
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

export interface SiliconeWrinkleUvCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeWrinkleUvMatchParams {
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

export const siliconeWrinkleUvApi = {
  getAllProducts() { return request.get('/silicone_wrinkle_uv/products'); },
  getProductById(id: number) { return request.get(`/silicone_wrinkle_uv/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_wrinkle_uv/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeWrinkleUvProduct) { return request.post('/silicone_wrinkle_uv/products', product); },
  updateProduct(id: number, product: SiliconeWrinkleUvProduct) { return request.put(`/silicone_wrinkle_uv/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_wrinkle_uv/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_wrinkle_uv/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_wrinkle_uv/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeWrinkleUvCompatibility) { return request.post('/silicone_wrinkle_uv/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeWrinkleUvCompatibility) {
    return request.put(`/silicone_wrinkle_uv/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_wrinkle_uv/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeWrinkleUvCompatibility[]) {
    return request.post('/silicone_wrinkle_uv/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_wrinkle_uv/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeWrinkleUvMatchParams) {
    return request.post<PagedItems<SiliconeWrinkleUvProduct>>('/silicone_wrinkle_uv/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_wrinkle_uv/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_wrinkle_uv/products/${id}/detail`); },
};
