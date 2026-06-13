import axios from 'axios';

// 产品类型选项接口
export interface ProductTypeOption {
  id: number;
  productName: string;
  displayOrder: number;
  isActive: boolean;
  description?: string;
  createdAt: string;
  updatedAt: string;
}

// 产品类型选项API
export const productTypeOptionsApi = {
  /**
   * 获取所有激活的产品类型选项
   */
  getAllActiveOptions: async (): Promise<ProductTypeOption[]> => {
    const response = await axios.get('/api/product-type-options/active');
    return response.data;
  },

  /**
   * 根据ID获取产品类型选项
   */
  getById: async (id: number): Promise<ProductTypeOption> => {
    const response = await axios.get(`/api/product-type-options/${id}`);
    return response.data;
  },

  /**
   * 获取所有产品类型选项（包括非激活的）
   */
  getAllOptions: async (): Promise<ProductTypeOption[]> => {
    const response = await axios.get('/api/product-type-options/all');
    return response.data;
  },

  /**
   * 创建产品类型选项
   */
  createProductType: async (productType: Omit<ProductTypeOption, 'id' | 'createdAt' | 'updatedAt'>): Promise<ProductTypeOption> => {
    const response = await axios.post('/api/product-type-options', productType);
    return response.data;
  },

  /**
   * 更新产品类型选项
   */
  updateProductType: async (id: number, productType: Partial<ProductTypeOption>): Promise<ProductTypeOption> => {
    const response = await axios.put(`/api/product-type-options/${id}`, productType);
    return response.data;
  },

  /**
   * 删除产品类型选项
   */
  deleteProductType: async (id: number): Promise<void> => {
    await axios.delete(`/api/product-type-options/${id}`);
  }
};
