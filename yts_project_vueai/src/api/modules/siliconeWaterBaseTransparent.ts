import request from '../request';

export interface SiliconeWaterBaseTransparentProduct {
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

export interface SiliconeWaterBaseTransparentCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeWaterBaseTransparentMatchParams {
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

export const siliconeWaterBaseTransparentApi = {
  getAllProducts() { return request.get('/silicone_water_base_transparent/products'); },
  getProductById(id: number) { return request.get(`/silicone_water_base_transparent/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_water_base_transparent/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeWaterBaseTransparentProduct) { return request.post('/silicone_water_base_transparent/products', product); },
  updateProduct(id: number, product: SiliconeWaterBaseTransparentProduct) { return request.put(`/silicone_water_base_transparent/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_water_base_transparent/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_water_base_transparent/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_water_base_transparent/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeWaterBaseTransparentCompatibility) { return request.post('/silicone_water_base_transparent/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeWaterBaseTransparentCompatibility) {
    return request.put(`/silicone_water_base_transparent/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_water_base_transparent/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeWaterBaseTransparentCompatibility[]) {
    return request.post('/silicone_water_base_transparent/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_water_base_transparent/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeWaterBaseTransparentMatchParams) {
    return request.post<PagedItems<SiliconeWaterBaseTransparentProduct>>('/silicone_water_base_transparent/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_water_base_transparent/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_water_base_transparent/products/${id}/detail`); },
};
