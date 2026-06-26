import request from '../request';

export interface LeoBookBoardProduct {
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

export interface LeoBookBoardCompatibility {
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

export interface LeoBookBoardMatchParams {
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

export const leoBookBoardApi = {
  getAllProducts() { return request.get('/leo_book_board/products'); },
  getProductById(id: number) { return request.get(`/leo_book_board/products/${id}`); },
  searchProducts(keyword: string) { return request.get('/leo_book_board/products/search', { params: { keyword } }); },
  createProduct(product: LeoBookBoardProduct) { return request.post('/leo_book_board/products', product); },
  updateProduct(id: number, product: LeoBookBoardProduct) { return request.put(`/leo_book_board/products/${id}`, product); },
  deleteProduct(id: number) { return request.delete(`/leo_book_board/products/${id}`); },

  getCompatibilitiesByProductId(productId: number) {
    return request.get('/leo_book_board/compatibilities', { params: { productId } });
  },
  getCompatibilityById(id: number) { return request.get(`/leo_book_board/compatibilities/${id}`); },
  createCompatibility(compatibility: LeoBookBoardCompatibility) { return request.post('/leo_book_board/compatibilities', compatibility); },
  updateCompatibility(id: number, compatibility: LeoBookBoardCompatibility) {
    return request.put(`/leo_book_board/compatibilities/${id}`, compatibility);
  },
  deleteCompatibility(id: number) { return request.delete(`/leo_book_board/compatibilities/${id}`); },
  batchSaveCompatibilities(compatibilities: LeoBookBoardCompatibility[]) {
    return request.post('/leo_book_board/compatibilities/batch', compatibilities);
  },
  batchDeleteCompatibilities(ids: number[]) {
    return request.delete('/leo_book_board/compatibilities/batch', { data: ids });
  },

  match(params: LeoBookBoardMatchParams) {
    return request.post<PagedItems<LeoBookBoardProduct>>('/leo_book_board/match', params);
  },
  getSteps() { return request.get<string[]>('/leo_book_board/steps'); },
  getProductDetail(id: number) { return request.get(`/leo_book_board/products/${id}/detail`); },
};
