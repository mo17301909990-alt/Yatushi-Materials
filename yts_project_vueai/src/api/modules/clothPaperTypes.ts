import request from '../request';

// 布料纸类型接口
export interface ClothPaperType {
  id: number;
  typeName: string;
  category: string;
  sortOrder: number;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

// 布料纸类型API
export const clothPaperTypeApi = {
  // 获取所有激活的布料纸类型
  getActiveClothPaperTypes: (): Promise<ClothPaperType[]> => {
    return request.get('/cloth-paper-types/active');
  }
};


