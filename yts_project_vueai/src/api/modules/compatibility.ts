import request from '../request';
import type { 
  CompatibilityQueryParams, 
  HotStampingCompatibility, 
  CompatibilityMatchResult,
  CompatibilityFilterOptions,
  PaperPerformanceType,
  ProductType,
  HotStampingType,
  LaminationCompatibility,
  LaminationMaterialOptions,
  PostProcessingOptions,
  LaminationCompatibilityQueryParams,
  LaminationFilterOptions
} from '@/types/compatibility';

/**
 * 烫金工艺兼容性API
 */
export const compatibilityApi = {
  /**
   * 获取兼容的烫金类型
   */
  getCompatibleHotStampingTypes(params: CompatibilityQueryParams) {
    return request.post<HotStampingCompatibility[]>('/compatibility/hot-stamping-types', params);
  },

  /**
   * 智能推荐烫金类型
   */
  getRecommendedHotStampingTypes(params: CompatibilityQueryParams) {
    return request.post<string[]>('/compatibility/recommend', params);
  },

  /**
   * 验证烫金类型兼容性
   */
  validateCompatibility(params: CompatibilityQueryParams, hotStampingType: string) {
    return request.post<boolean>(`/compatibility/validate?hotStampingType=${hotStampingType}`, params);
  },

  /**
   * 获取所有烫金纸性能类型
   */
  getAllPaperPerformanceTypes() {
    return request.get<PaperPerformanceType[]>('/compatibility/paper-performance-types');
  },

  /**
   * 获取所有产品类型
   */
  getAllProductTypes() {
    return request.get<ProductType[]>('/compatibility/product-types');
  },

  /**
   * 获取所有烫金类型
   */
  getAllHotStampingTypes() {
    return request.get<HotStampingType[]>('/compatibility/hot-stamping-types');
  },

  /**
   * 获取兼容性筛选选项
   */
  getFilterOptions() {
    return request.get<CompatibilityFilterOptions>('/compatibility/filter-options');
  },

  // ========== 过胶兼容性相关API ==========

  /**
   * 根据参数查询过胶兼容性
   */
  getLaminationCompatibility(params: LaminationCompatibilityQueryParams) {
    return request.post<LaminationCompatibility[]>('/compatibility/lamination-compatibility', params);
  },

  /**
   * @deprecated interfaceTypeId 字段已废弃（后端固定为 0），将在下一版本移除
   * 根据烫金纸系列和后加工步骤查询兼容的过胶材质
   */
  getCompatibleLaminationMaterials(foilSeries: string, postProcessingStepId?: number, _interfaceTypeId?: number) {
    const params = new URLSearchParams();
    params.append('foilSeries', foilSeries);
    if (postProcessingStepId) params.append('postProcessingStepId', postProcessingStepId.toString());

    return request.get<LaminationMaterialOptions[]>(`/compatibility/compatible-materials?${params.toString()}`);
  },

  /**
   * @deprecated interfaceTypeId 字段已废弃（后端固定为 0），将在下一版本移除
   * 根据烫金纸系列和过胶材质查询兼容的后加工步骤
   */
  getCompatiblePostProcessingSteps(foilSeries: string, laminationMaterialId?: number, _interfaceTypeId?: number) {
    const params = new URLSearchParams();
    params.append('foilSeries', foilSeries);
    if (laminationMaterialId) params.append('laminationMaterialId', laminationMaterialId.toString());

    return request.get<PostProcessingOptions[]>(`/compatibility/compatible-post-processing?${params.toString()}`);
  },

  /**
   * 获取所有过胶材质选项
   */
  getAllLaminationMaterials() {
    return request.get<LaminationMaterialOptions[]>('/compatibility/lamination-materials');
  },

  /**
   * 获取所有后加工步骤选项
   */
  getAllPostProcessingSteps() {
    return request.get<PostProcessingOptions[]>('/compatibility/post-processing-steps');
  },

  /**
   * @deprecated interfaceTypeId 字段已废弃（后端固定为 0），将在下一版本移除
   * 检查过胶兼容性
   */
  checkLaminationCompatibility(foilSeries: string, postProcessingStepId: number, laminationMaterialId: number, _interfaceTypeId?: number) {
    const params = new URLSearchParams();
    params.append('foilSeries', foilSeries);
    params.append('postProcessingStepId', postProcessingStepId.toString());
    params.append('laminationMaterialId', laminationMaterialId.toString());

    return request.get<'V' | 'X'>(`/compatibility/check-lamination-compatibility?${params.toString()}`);
  },

  /**
   * 获取所有烫金类型分组（支持多级下拉框）
   */
  getAllHotStampingTypeGroups() {
    return request.get<{
      stampingType: string;
      isDefault: boolean;
      options: {
        id: number;
        displayName: string;
        positionType: string | null;
        description: string;
        sortOrder: number;
      }[] | null;
    }[]>('/compatibility/hot-stamping-type-groups');
  },

  /**
   * 根据烫金类型获取位置选项
   */
  getPositionOptionsByStampingType(stampingType: string) {
    return request.get<{
      id: number;
      stampingType: string;
      positionType: string | null;
      description: string;
      isActive: boolean;
      sortOrder: number;
      createdAt: string;
      updatedAt: string;
    }[]>(`/compatibility/position-options/${stampingType}`);
  }
};
