import request from '../request';

export interface SiliconeWatercolorInkProduct {
  id?: number
  materialCode?: string
  supplierCode?: string
  stockCode?: string
  materialName?: string
  usage?: string
  category?: string
  color?: string
  responsiblePerson?: string
  minSheetSize?: string
  maxSheetSize?: string
  minSpacing?: string
  maxSpacing?: string
  designInfo?: string
  applicableInterface?: string
  notes?: string
  isActive?: boolean
  createdAt?: string
  updatedAt?: string
}

export interface SiliconeWatercolorInkCompatibility {
  id?: number
  productId?: number
  productName?: string
  postProcessingStep?: string
  compatibilityStatus?: string
  displayOrder?: number
  createdAt?: string
}

export const siliconeWatercolorInkApi = {
  // ========== 产品管理 ==========

  /** 获取所有产品 */
  getAllProducts() {
    return request.get<SiliconeWatercolorInkProduct[]>('/silicone_watercolor_ink/products')
  },

  /** 搜索产品 */
  searchProducts(keyword?: string) {
    return request.get<SiliconeWatercolorInkProduct[]>('/silicone_watercolor_ink/products/search', {
      params: { keyword }
    })
  },

  /** 根据ID获取产品 */
  getProductById(id: number) {
    return request.get<SiliconeWatercolorInkProduct>(`/silicone_watercolor_ink/products/${id}`)
  },

  /** 新增产品 */
  createProduct(product: SiliconeWatercolorInkProduct) {
    return request.post<SiliconeWatercolorInkProduct>('/silicone_watercolor_ink/products', product)
  },

  /** 更新产品 */
  updateProduct(id: number, product: SiliconeWatercolorInkProduct) {
    return request.put<SiliconeWatercolorInkProduct>(`/silicone_watercolor_ink/products/${id}`, product)
  },

  /** 删除产品 */
  deleteProduct(id: number) {
    return request.delete(`/silicone_watercolor_ink/products/${id}`)
  },

  // ========== 兼容性管理 ==========

  /** 获取所有兼容性列表 */
  getAllCompatibilities() {
    return request.get<SiliconeWatercolorInkCompatibility[]>('/silicone_watercolor_ink/compatibilities')
  },

  /** 根据产品ID获取兼容性列表 */
  getCompatibilitiesByProductId(productId: number) {
    return request.get<SiliconeWatercolorInkCompatibility[]>(
      `/silicone_watercolor_ink/compatibilities/by-product/${productId}`
    )
  },

  /** 根据ID获取兼容性 */
  getCompatibilityById(id: number) {
    return request.get<SiliconeWatercolorInkCompatibility>(
      `/silicone_watercolor_ink/compatibilities/${id}`
    )
  },

  /** 新增兼容性 */
  createCompatibility(compatibility: SiliconeWatercolorInkCompatibility) {
    return request.post<SiliconeWatercolorInkCompatibility>(
      '/silicone_watercolor_ink/compatibilities', compatibility
    )
  },

  /** 更新兼容性 */
  updateCompatibility(id: number, compatibility: SiliconeWatercolorInkCompatibility) {
    return request.put<SiliconeWatercolorInkCompatibility>(
      `/silicone_watercolor_ink/compatibilities/${id}`, compatibility
    )
  },

  /** 批量保存兼容性 */
  batchSaveCompatibility(compatibilities: SiliconeWatercolorInkCompatibility[]) {
    return request.post('/silicone_watercolor_ink/compatibilities/batch', compatibilities)
  },

  /** 删除兼容性 */
  deleteCompatibility(id: number) {
    return request.delete(`/silicone_watercolor_ink/compatibilities/${id}`)
  }
}
