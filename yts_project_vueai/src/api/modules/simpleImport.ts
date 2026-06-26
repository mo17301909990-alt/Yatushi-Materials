// 简单的导入API模块
export const simpleImportApi = {
  /**
   * 导入产品信息
   */
  async importProduct(type: string, formData: FormData): Promise<any> {
    const response = await fetch(`/product-import/${type}`, {
      method: 'POST',
      body: formData,
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    if (!response.ok) {
      throw new Error('导入失败')
    }

    return await response.json()
  },

  /**
   * 验证文件
   */
  async validateFile(file: File): Promise<any> {
    const formData = new FormData()
    formData.append('file', file)

    const response = await fetch('/product-import/validate', {
      method: 'POST',
      body: formData,
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    if (!response.ok) {
      throw new Error('文件验证失败')
    }

    return await response.json()
  },

  /**
   * 获取配置选项
   */
  async getConfigOptions(): Promise<any> {
    const response = await fetch('/product-import/config-options', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    if (!response.ok) {
      throw new Error('获取配置选项失败')
    }

    return await response.json()
  }
}
