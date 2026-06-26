import request from '../request';

export interface SiliconeGlowInkProduct {
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

export interface SiliconeGlowInkCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeGlowInkMatchParams {
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

export const siliconeGlowInkApi = {
  getAllProducts() { return request.get('/silicone_glow_ink/products'); },
  getProductById(id: number) { return request.get(`/silicone_glow_ink/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_glow_ink/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeGlowInkProduct) { return request.post('/silicone_glow_ink/products', product); },
  updateProduct(id: number, product: SiliconeGlowInkProduct) { return request.put(`/silicone_glow_ink/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_glow_ink/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_glow_ink/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_glow_ink/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeGlowInkCompatibility) { return request.post('/silicone_glow_ink/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeGlowInkCompatibility) {
    return request.put(`/silicone_glow_ink/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_glow_ink/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeGlowInkCompatibility[]) {
    return request.post('/silicone_glow_ink/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_glow_ink/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeGlowInkMatchParams) {
    return request.post<PagedItems<SiliconeGlowInkProduct>>('/silicone_glow_ink/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_glow_ink/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_glow_ink/products/${id}/detail`); },
};
