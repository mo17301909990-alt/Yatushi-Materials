import request from '../request';

export interface LeoNonFlatProduct {
  id?: number;
  materialCode?: string;
  supplierCode?: string;
  stockCode?: string;
  materialName?: string;
  usage?: string;
  category?: string;
  color?: string;
  responsiblePerson?: string;
  thickness?: string;
  shape?: string;
  tester?: string;
  minSheetSize?: string;
  maxSheetSize?: string;
  minSpacing?: string;
  maxSpacing?: string;
  designInfo?: string;
  applicableInterface?: string;
  notes?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface LeoNonFlatCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  stepCategory?: string;
  remark?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface LeoNonFlatMatchParams {
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

export const leoNonFlatApi = {
  getAllProducts() { return request.get('/leo_non_flat/products'); },
  getProductById(id: number) { return request.get(`/leo_non_flat/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/leo_non_flat/products/search', { params: { keyword } }); },
  createProduct(product: LeoNonFlatProduct) { return request.post('/leo_non_flat/products', product); },
  updateProduct(id: number, product: LeoNonFlatProduct) { return request.put(`/leo_non_flat/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/leo_non_flat/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/leo_non_flat/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/leo_non_flat/compatibilities/${id}`); },
  createCompatibility(compatibility: LeoNonFlatCompatibility) { return request.post('/leo_non_flat/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: LeoNonFlatCompatibility) {
    return request.put(`/leo_non_flat/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/leo_non_flat/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: LeoNonFlatCompatibility[]) {
    return request.post('/leo_non_flat/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/leo_non_flat/compatibilities/batch', { data: ids });
  },

  match(params: LeoNonFlatMatchParams) {
    return request.post<PagedItems<LeoNonFlatProduct>>('/leo_non_flat/match', params);
  },
  getSteps() { return request.get<string[]>('/leo_non_flat/steps'); },
  getProductDetail(id: number) { return request.get(`/leo_non_flat/products/${id}/detail`); },
};
