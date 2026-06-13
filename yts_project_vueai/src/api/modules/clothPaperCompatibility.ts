import request from '../request';

export interface ClothPaperCompatibility {
  id: number;
  productName: string;
  clothPaperTypeId: number;
  compatibilityStatus: 'V' | 'X' | 'NA' | '▷';
  paperType?: string;
  clothPaperTypeName?: string;
  clothPaperCategory?: string;
  noticeIds?: number[];
}

export const clothPaperCompatibilityApi = {
  // 根据布面纸类型ID获取兼容性记录（获取该类型映射的所有型号）
  getByClothPaperTypeId: (clothPaperTypeId: number): Promise<ClothPaperCompatibility[]> => {
    return request.get(`/cloth-paper-compatibility/type/${clothPaperTypeId}`);
  },

  // 获取所有兼容性记录
  getAll: (): Promise<ClothPaperCompatibility[]> => {
    return request.get('/cloth-paper-compatibility/all');
  },
};
