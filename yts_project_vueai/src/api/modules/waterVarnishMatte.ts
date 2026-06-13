import request from '../request';

// UV油_哑光水油产品接口
export interface WaterVarnishMatteProduct {
  id?: number
  materialCode?: string
  supplierCode?: string
  stockCode?: string
  materialName?: string
  usage?: string
  materialType?: string
  color?: string
  responsiblePerson?: string
  minSheetSize?: string
  maxSheetSize?: string
  minPoint?: string
  maxPoint?: string
  minLine?: string
  maxLine?: string
  minSpacing?: string
  maxSpacing?: string
  minArea?: string
  maxArea?: string
  structureApplication?: string
  applicableInterface?: string
  notes?: string
  gloss?: string
  writingFunction?: string
  isActive?: boolean
  createdAt?: string
  updatedAt?: string
}

// UV油_哑光水油兼容性接口
export interface WaterVarnishMatteCompatibility {
  id?: number
  productId?: number
  postProcessingStep?: string
  compatibilityStatus?: string
  displayOrder?: number
  createdAt?: string
}

export const waterVarnishMatteApi = {

  // ========== 产品管理 ==========

  /** 获取所有产品（激活） */
  getAllProducts() {
    return request.get<WaterVarnishMatteProduct[]>('/water_varnish_matte/products')
  },

  /** 获取所有产品（含未激活） */
  getAllProductsIncludingInactive() {
    return request.get<WaterVarnishMatteProduct[]>('/water_varnish_matte/products/all')
  },

  /** 根据ID获取产品 */
  getProductById(id: number) {
    return request.get<WaterVarnishMatteProduct>(`/water_varnish_matte/products/${id}`)
  },

  /** 搜索产品 */
  searchProducts(keyword: string) {
    return request.get<WaterVarnishMatteProduct[]>('/water_varnish_matte/products/search', { params: { keyword } })
  },

  /** 保存产品 */
  saveProduct(product: WaterVarnishMatteProduct) {
    return request.post<WaterVarnishMatteProduct>('/water_varnish_matte/products', product)
  },

  /** 更新产品 */
  updateProduct(id: number, product: WaterVarnishMatteProduct) {
    return request.put<WaterVarnishMatteProduct>(`/water_varnish_matte/products/${id}`, product)
  },

  /** 删除产品 */
  deleteProduct(id: number) {
    return request.delete(`/water_varnish_matte/products/${id}`)
  },

  // ========== 兼容性管理 ==========

  /** 获取兼容性列表 */
  getCompatibilities(productId?: number) {
    const params: any = {}
    if (productId !== undefined) params.productId = productId
    return request.get<WaterVarnishMatteCompatibility[]>('/water_varnish_matte/compatibilities', { params })
  },

  /** 根据ID获取兼容性 */
  getCompatibilityById(id: number) {
    return request.get<WaterVarnishMatteCompatibility>(`/water_varnish_matte/compatibilities/${id}`)
  },

  /** 保存兼容性 */
  saveCompatibility(compatibility: WaterVarnishMatteCompatibility) {
    return request.post<WaterVarnishMatteCompatibility>('/water_varnish_matte/compatibilities', compatibility)
  },

  /** 更新兼容性 */
  updateCompatibility(id: number, compatibility: WaterVarnishMatteCompatibility) {
    return request.put<WaterVarnishMatteCompatibility>(`/water_varnish_matte/compatibilities/${id}`, compatibility)
  },

  /** 批量保存兼容性 */
  batchSaveCompatibility(compatibilities: WaterVarnishMatteCompatibility[]) {
    return request.post('/water_varnish_matte/compatibilities/batch', compatibilities)
  },

  /** 删除兼容性 */
  deleteCompatibility(id: number) {
    return request.delete(`/water_varnish_matte/compatibilities/${id}`)
  },

  /** 根据产品ID删除兼容性 */
  deleteCompatibilityByProductId(productId: number) {
    return request.delete(`/water_varnish_matte/compatibilities/product/${productId}`)
  },

  /** 获取所有后加工工序步骤名称 */
  getAllPostProcessingSteps() {
    return request.get<string[]>('/water_varnish_matte/post-processing-steps')
  },
}
