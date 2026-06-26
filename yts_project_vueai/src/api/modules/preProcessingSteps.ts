import request from '../request';

// 前工序选项接口
export interface PreProcessingStepsOption {
  id: number;
  preProcessingStepsName: string;
  displayOrder: number;
  isActive: boolean;
  description?: string;
  createdAt: string;
  updatedAt: string;
  steps?: string;
  process?: string;
}

export const preProcessingStepsApi = {
  /**
   * 获取所有激活的前工序选项
   * @returns 前工序选项列表
   */
  getAllActiveOptions(): Promise<PreProcessingStepsOption[]> {
    console.log('Calling getAllActiveOptions API');
    return request.get('/pre-processing-steps/options');
  },

  /**
   * 根据ID获取前工序选项
   * @param id 选项ID
   * @returns 前工序选项
   */
  getById(id: number): Promise<PreProcessingStepsOption> {
    console.log('Calling getById API with id:', id);
    return request.get(`/pre-processing-steps/options/${id}`);
  },

  /**
   * 获取所有前工序选项（包括未激活的）
   * @returns 前工序选项列表
   */
  getAllOptions(): Promise<PreProcessingStepsOption[]> {
    console.log('Calling getAllOptions API');
    return request.get('/pre-processing-steps/options/all');
  }
};
