import request from '../request';

export interface SiliconeLedUvSprayProduct {
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

export interface SiliconeLedUvSprayCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeLedUvSprayMatchParams {
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

export const siliconeLedUvSprayApi = {
  getAllProducts() { return request.get('/silicone_led_uv_spray/products'); },
  getProductById(id: number) { return request.get(`/silicone_led_uv_spray/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_led_uv_spray/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeLedUvSprayProduct) { return request.post('/silicone_led_uv_spray/products', product); },
  updateProduct(id: number, product: SiliconeLedUvSprayProduct) { return request.put(`/silicone_led_uv_spray/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_led_uv_spray/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_led_uv_spray/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_led_uv_spray/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeLedUvSprayCompatibility) { return request.post('/silicone_led_uv_spray/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeLedUvSprayCompatibility) {
    return request.put(`/silicone_led_uv_spray/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_led_uv_spray/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeLedUvSprayCompatibility[]) {
    return request.post('/silicone_led_uv_spray/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_led_uv_spray/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeLedUvSprayMatchParams) {
    return request.post<PagedItems<SiliconeLedUvSprayProduct>>('/silicone_led_uv_spray/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_led_uv_spray/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_led_uv_spray/products/${id}/detail`); },
};
