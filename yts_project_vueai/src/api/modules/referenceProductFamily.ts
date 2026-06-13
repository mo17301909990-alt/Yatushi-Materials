import request from '../request'

export interface ReferenceProductFamily {
  id?: number
  category: string
  subCategory?: string
  detailName: string
  displayOrder?: number
  createdAt?: string
  updatedAt?: string
}

/** 获取所有产品家族 */
export function getAllProductFamilies(): Promise<ReferenceProductFamily[]> {
  return request.get('/reference/product-family')
}

/** 根据ID获取 */
export function getProductFamilyById(id: number): Promise<ReferenceProductFamily> {
  return request.get(`/reference/product-family/${id}`)
}

/** 根据大类查询 */
export function getProductFamilyByCategory(category: string): Promise<ReferenceProductFamily[]> {
  return request.get(`/reference/product-family/category/${category}`)
}

/** 获取所有大类列表 */
export function getDistinctCategories(): Promise<string[]> {
  return request.get('/reference/product-family/categories')
}

/** 获取子类列表 */
export function getDistinctSubCategories(category?: string): Promise<string[]> {
  return request.get('/reference/product-family/sub-categories', { params: { category } })
}

/** 创建 */
export function createProductFamily(data: ReferenceProductFamily): Promise<ReferenceProductFamily> {
  return request.post('/reference/product-family', data)
}

/** 更新 */
export function updateProductFamily(id: number, data: ReferenceProductFamily): Promise<ReferenceProductFamily> {
  return request.put(`/reference/product-family/${id}`, data)
}

/** 删除 */
export function deleteProductFamily(id: number): Promise<void> {
  return request.delete(`/reference/product-family/${id}`)
}
