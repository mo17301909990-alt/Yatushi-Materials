import request from '../request';

export interface SiliconeWatercolorInkProduct {
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

export interface SiliconeWatercolorInkCompatibility {
  id?: number;
  productId?: number;
  postProcessingStep?: string;
  compatibilityStatus?: string;
  displayOrder?: number;
  createdAt?: string;
  productName?: string;
}

export interface SiliconeWatercolorInkMatchParams {
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

export const siliconeWatercolorInkApi = {
  getAllProducts() { return request.get('/silicone_watercolor_ink/products'); },
  getProductById(id: number) { return request.get(`/silicone_watercolor_ink/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/silicone_watercolor_ink/products/search', { params: { keyword } }); },
  createProduct(product: SiliconeWatercolorInkProduct) { return request.post('/silicone_watercolor_ink/products', product); },
  updateProduct(id: number, product: SiliconeWatercolorInkProduct) { return request.put(`/silicone_watercolor_ink/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/silicone_watercolor_ink/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/silicone_watercolor_ink/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/silicone_watercolor_ink/compatibilities/${id}`); },
  createCompatibility(compatibility: SiliconeWatercolorInkCompatibility) { return request.post('/silicone_watercolor_ink/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: SiliconeWatercolorInkCompatibility) {
    return request.put(`/silicone_watercolor_ink/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/silicone_watercolor_ink/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: SiliconeWatercolorInkCompatibility[]) {
    return request.post('/silicone_watercolor_ink/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/silicone_watercolor_ink/compatibilities/batch', { data: ids });
  },

  match(params: SiliconeWatercolorInkMatchParams) {
    return request.post<PagedItems<SiliconeWatercolorInkProduct>>('/silicone_watercolor_ink/match', params);
  },
  getSteps() { return request.get<string[]>('/silicone_watercolor_ink/steps'); },
  getProductDetail(id: number) { return request.get(`/silicone_watercolor_ink/products/${id}/detail`); },
};
