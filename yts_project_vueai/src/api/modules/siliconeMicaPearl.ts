import request from '../request';

export interface SiliconeMicaPearlProduct {
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

export interface SiliconeMicaPearlCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeMicaPearlMatchParams {
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

export const siliconeMicaPearlApi = {
  getAllProducts() { return request.get('/silicone_mica_pearl/products'); },
  getProductById(id: number) { return request.get(`/silicone_mica_pearl/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_mica_pearl/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeMicaPearlProduct) { return request.post('/silicone_mica_pearl/products', product); },
  updateProduct(id: number, product: SiliconeMicaPearlProduct) { return request.put(`/silicone_mica_pearl/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_mica_pearl/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_mica_pearl/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_mica_pearl/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeMicaPearlCompatibility) { return request.post('/silicone_mica_pearl/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeMicaPearlCompatibility) {
    return request.put(`/silicone_mica_pearl/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_mica_pearl/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeMicaPearlCompatibility[]) {
    return request.post('/silicone_mica_pearl/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_mica_pearl/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeMicaPearlMatchParams) {
    return request.post<PagedItems<SiliconeMicaPearlProduct>>('/silicone_mica_pearl/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_mica_pearl/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_mica_pearl/products/${id}/detail`); },
};
