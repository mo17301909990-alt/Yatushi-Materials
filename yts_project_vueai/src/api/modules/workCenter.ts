import request from '../request';

// 工作中心资源组数据类型
export interface WorkCenterResourceGroup {
  id?: number;
  workCenter: string; // 工作中心代码
  name: string; // 名称
  schedulingGranularity: string; // 排產粒度
  department: string; // 部門
  departmentDescription: string; // 部門說明
  calendar: string; // 日历
  backflush: string; // 反冲
  indirectCostBasis: string; // 间接费基准
  schedulingDriver: string; // 排产驱动因素
  efficiency: number; // 效率
  queueHours: number; // 排队时数
  completionHours: number; // 完成时数
  controlPoint: number; // 控制点
  outsourcing: number; // 外协
  resourceGroupCount: number; // 資源組 (個數)
  resourceCount: number; // 資源 (個數)
  forecastResourceGroupCount: number; // 預測資源組 (個數)
  forecastResourceCount: number; // 預測資源 (個數)
  transferHours: number; // 移送時數
  remarks?: string; // 備注
}

export const workCenterApi = {
  // 获取所有工作中心资源组
  getAllWorkCenters(): Promise<{ data: WorkCenterResourceGroup[]; message: string }> {
    return request.get('/work-centers');
  },

  // 根据ID获取工作中心资源组
  getWorkCenterById(id: number): Promise<{ data: WorkCenterResourceGroup; message: string }> {
    return request.get(`/work-centers/${id}`);
  },

  // 创建工作中心资源组
  createWorkCenter(workCenter: Omit<WorkCenterResourceGroup, 'id'>): Promise<{ data: WorkCenterResourceGroup; message: string }> {
    return request.post('/work-centers', workCenter);
  },

  // 更新工作中心资源组
  updateWorkCenter(id: number, workCenter: Partial<WorkCenterResourceGroup>): Promise<{ data: WorkCenterResourceGroup; message: string }> {
    return request.put(`/work-centers/${id}`, workCenter);
  },

  // 删除工作中心资源组
  deleteWorkCenter(id: number): Promise<{ message: string }> {
    return request.delete(`/work-centers/${id}`);
  },

  // 搜索工作中心资源组
  searchWorkCenters(keyword: string): Promise<{ data: WorkCenterResourceGroup[]; message: string }> {
    return request.get('/work-centers/search', { params: { keyword } });
  }
};

// 资源类型
export interface Resource {
  id?: number;
  resourceCode: string; // 資源編碼
  name: string; // 名稱
  resourceType: string; // 資源類型
  asset: string; // 資產
  sortingRule: string; // 排序規則
  selectionRule: string; // 選擇規則
  layeringRule1: string; // 分層規則1
  layeringRule2: string; // 分層規則2
  layeringRule3: string; // 分層規則3
  selectionValue: string; // 選擇值
  mustComplete: number; // 必須完成
  instantaneousCount: number; // 即時數
  extensionDelay: number; // 延務延遲
}

// 资源组类型
export interface ResourceGroup {
  id?: number;
  workCenter: string; // 工作中心
  resourceGroupCode: string; // 資源組編碼
  resourceGroupName: string; // 資源組名稱
  forecastResourceGroupCode?: string; // 預測資源組編碼
  forecastResourceGroupName?: string; // 預測資源組名稱
  resources?: Resource[]; // 資源列表
}

// 工作中心信息类型
export interface WorkCenterInfo {
  workCenter: string;
  name: string;
}

