import request from '../request';

export interface LeoFlatProduct {
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

export interface LeoFlatCompatibility {
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

export interface LeoFlatMatchParams {
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

export const leoFlatApi = {
  getAllProducts() { return request.get('/leo_flat/products'); },
  getProductById(id: number) { return request.get(`/leo_flat/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/leo_flat/products/search', { params: { keyword } }); },
  createProduct(product: LeoFlatProduct) { return request.post('/leo_flat/products', product); },
  updateProduct(id: number, product: LeoFlatProduct) { return request.put(`/leo_flat/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/leo_flat/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/leo_flat/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/leo_flat/compatibilities/${id}`); },
  createCompatibility(compatibility: LeoFlatCompatibility) { return request.post('/leo_flat/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: LeoFlatCompatibility) {
    return request.put(`/leo_flat/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/leo_flat/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: LeoFlatCompatibility[]) {
    return request.post('/leo_flat/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/leo_flat/compatibilities/batch', { data: ids });
  },

  match(params: LeoFlatMatchParams) {
    return request.post<PagedItems<LeoFlatProduct>>('/leo_flat/match', params);
  },
  getSteps() { return request.get<string[]>('/leo_flat/steps'); },
  getProductDetail(id: number) { return request.get(`/leo_flat/products/${id}/detail`); },
};
