import { request } from '../request'

// 导入结果类型
export interface ImportResult<T = any> {
  success: boolean
  message: string
  data?: T[]
  errorMessages?: string[]
  totalCount: number
  successCount: number
  errorCount: number
}

// 导入配置类型
export interface ImportConfig {
  conflictStrategy: 'SKIP' | 'OVERWRITE' | 'CREATE_NEW' | 'PROMPT_USER'
  validateData: boolean
  backupBeforeImport: boolean
  maxImportCount: number
}

// 产品导入API
export const productImportApi = {
  /**
   * 导入产品信息
   * @param type 导入类型
   * @param formData 表单数据
   * @param onProgress 进度回调
   * @returns 导入结果
   */
  async importProduct(
    type: 'complete' | 'basic',
    formData: FormData,
    onProgress?: (progress: number) => void
  ): Promise<ImportResult> {
    try {
      // 模拟进度更新
      if (onProgress) {
        onProgress(10)
        await new Promise(resolve => setTimeout(resolve, 100))
        onProgress(30)
        await new Promise(resolve => setTimeout(resolve, 100))
        onProgress(60)
        await new Promise(resolve => setTimeout(resolve, 100))
        onProgress(90)
      }

      const response = await request({
        url: `/api/product-import/${type}`,
        method: 'POST',
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })

      if (onProgress) {
        onProgress(100)
      }

      return response.data
    } catch (error: any) {
      console.error('导入失败:', error)
      throw new Error(error.response?.data?.message || '导入失败')
    }
  },

  /**
   * 验证导入文件
   * @param file 文件对象
   * @returns 验证结果
   */
  async validateFile(file: File): Promise<any> {
    try {
      const formData = new FormData()
      formData.append('file', file)

      const response = await request({
        url: '/api/product-import/validate',
        method: 'POST',
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })

      return response.data
    } catch (error: any) {
      console.error('文件验证失败:', error)
      throw new Error(error.response?.data?.message || '文件验证失败')
    }
  },

  /**
   * 获取导入配置选项
   * @returns 配置选项
   */
  async getConfigOptions(): Promise<any> {
    try {
      const response = await request({
        url: '/api/product-import/config-options',
        method: 'GET'
      })

      return response.data
    } catch (error: any) {
      console.error('获取配置选项失败:', error)
      throw new Error('获取配置选项失败')
    }
  },

  /**
   * 下载导入模板
   * @param type 模板类型
   * @returns 模板文件
   */
  async downloadTemplate(type: 'complete' | 'basic'): Promise<Blob> {
    try {
      const response = await request({
        url: `/api/product-import/template/${type}`,
        method: 'GET',
        responseType: 'blob'
      })

      return response.data
    } catch (error: any) {
      console.error('下载模板失败:', error)
      throw new Error('下载模板失败')
    }
  }
}

// 导出类型
export type { ImportResult, ImportConfig }
