import request from '../request';

export interface SiliconeGlitteringStarProduct {
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

export interface SiliconeGlitteringStarCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeGlitteringStarMatchParams {
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

export const siliconeGlitteringStarApi = {
  getAllProducts() { return request.get('/silicone_glittering_star/products'); },
  getProductById(id: number) { return request.get(`/silicone_glittering_star/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_glittering_star/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeGlitteringStarProduct) { return request.post('/silicone_glittering_star/products', product); },
  updateProduct(id: number, product: SiliconeGlitteringStarProduct) { return request.put(`/silicone_glittering_star/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_glittering_star/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_glittering_star/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_glittering_star/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeGlitteringStarCompatibility) { return request.post('/silicone_glittering_star/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeGlitteringStarCompatibility) {
    return request.put(`/silicone_glittering_star/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_glittering_star/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeGlitteringStarCompatibility[]) {
    return request.post('/silicone_glittering_star/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_glittering_star/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeGlitteringStarMatchParams) {
    return request.post<PagedItems<SiliconeGlitteringStarProduct>>('/silicone_glittering_star/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_glittering_star/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_glittering_star/products/${id}/detail`); },
};
