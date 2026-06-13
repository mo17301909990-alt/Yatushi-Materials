<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-[1800px] mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题和返回按钮 -->
      <div class="mb-8">
        <div class="flex items-center gap-4 mb-4">
          <button
            @click="goBack"
            class="flex items-center gap-2 text-gray-600 hover:text-gray-900 transition-colors"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18"></path>
            </svg>
            返回
          </button>
        </div>
        <h1 class="text-3xl font-bold text-gray-900">資源組詳情</h1>
        <div class="mt-2 text-gray-600">
          <p><span class="font-semibold">工作中心:</span> {{ workCenterInfo?.workCenter }} - {{ workCenterInfo?.name }}</p>
          <p><span class="font-semibold">資源組編碼:</span> {{ resourceGroup?.resourceGroupCode }}</p>
          <p><span class="font-semibold">資源組名稱:</span> {{ resourceGroup?.resourceGroupName }}</p>
        </div>
      </div>

      <!-- 操作栏 -->
      <div class="mb-6 flex justify-between items-center">
        <div class="flex space-x-4">
          <button
            @click="showCreateDialog = true"
            class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors flex items-center gap-2"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
            </svg>
            新增資源
          </button>
          <button
            @click="refreshData"
            class="bg-gray-600 text-white px-4 py-2 rounded-md hover:bg-gray-700 transition-colors flex items-center gap-2"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"></path>
            </svg>
            刷新
          </button>
        </div>
        <div class="flex items-center space-x-4">
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索資源編碼、名稱..."
            class="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-64"
          />
        </div>
      </div>

      <!-- 资源列表表格 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  資源編碼
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  名稱
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  資源類型
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  資產
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  排序規則
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  選擇規則
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  分層規則1
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  分層規則2
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  分層規則3
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  選擇值
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  必須完成
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  即時數
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  延務延遲
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider sticky right-0 bg-gray-50 z-10">
                  操作
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="resource in filteredResources" :key="resource.id" class="hover:bg-gray-50">
                <td class="px-4 py-3 whitespace-nowrap text-sm font-medium text-gray-900">
                  {{ resource.resourceCode }}
                </td>
                <td class="px-4 py-3 text-sm text-gray-500 max-w-xs">
                  {{ resource.name }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.resourceType }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.asset }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.sortingRule }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.selectionRule }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.layeringRule1 }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.layeringRule2 }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.layeringRule3 }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.selectionValue }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.mustComplete }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.instantaneousCount }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ resource.extensionDelay }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm font-medium sticky right-0 bg-white z-10">
                  <div class="flex space-x-2">
                    <button
                      @click="editResource(resource)"
                      class="text-blue-600 hover:text-blue-900"
                      title="編輯"
                    >
                      編輯
                    </button>
                    <button
                      @click="deleteResource(resource)"
                      class="text-red-600 hover:text-red-900"
                      title="刪除"
                    >
                      刪除
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-if="filteredResources.length === 0" class="text-center py-8 text-gray-500">
          暫無資源數據
        </div>
      </div>

      <!-- 创建/编辑资源对话框 -->
      <div
        v-if="showCreateDialog || showEditDialog"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
        @click.self="closeDialog"
      >
        <div class="relative top-10 mx-auto p-6 border w-[90%] max-w-4xl shadow-lg rounded-md bg-white max-h-[90vh] overflow-y-auto">
          <div class="mt-3">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              {{ showCreateDialog ? '新增資源' : '編輯資源' }}
            </h3>
            <form @submit.prevent="saveResource" class="space-y-4">
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">資源編碼 *</label>
                  <input
                    v-model="formData.resourceCode"
                    type="text"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">名稱 *</label>
                  <input
                    v-model="formData.name"
                    type="text"
                    required
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">資源類型</label>
                  <input
                    v-model="formData.resourceType"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">資產</label>
                  <select
                    v-model="formData.asset"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="M">M</option>
                    <option value="N">N</option>
                    <option value="Y">Y</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">排序規則</label>
                  <input
                    v-model="formData.sortingRule"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">選擇規則</label>
                  <input
                    v-model="formData.selectionRule"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">分層規則1</label>
                  <input
                    v-model="formData.layeringRule1"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">分層規則2</label>
                  <input
                    v-model="formData.layeringRule2"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">分層規則3</label>
                  <input
                    v-model="formData.layeringRule3"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">選擇值</label>
                  <select
                    v-model="formData.selectionValue"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="0N">0N</option>
                    <option value="0Y">0Y</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">必須完成</label>
                  <input
                    v-model.number="formData.mustComplete"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">即時數</label>
                  <input
                    v-model.number="formData.instantaneousCount"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">延務延遲</label>
                  <input
                    v-model.number="formData.extensionDelay"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
              </div>
              <div class="flex justify-end space-x-3 pt-4">
                <button
                  type="button"
                  @click="closeDialog"
                  class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
                >
                  取消
                </button>
                <button
                  type="submit"
                  class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700"
                >
                  保存
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import type { Resource, ResourceGroup, WorkCenterInfo } from '../../api/modules/workCenter';

const route = useRoute();
const router = useRouter();

// 从路由参数获取资源组ID和工作中心ID
const resourceGroupId = ref<string>(route.params.resourceGroupId as string);
const workCenterId = ref<string>(route.params.workCenterId as string);

// 响应式数据
const resources = ref<Resource[]>([]);
const resourceGroup = ref<ResourceGroup | null>(null);
const workCenterInfo = ref<WorkCenterInfo | null>(null);
const searchKeyword = ref('');
const showCreateDialog = ref(false);
const showEditDialog = ref(false);
const selectedResource = ref<Resource | null>(null);

// 表单数据
const formData = ref<Omit<Resource, 'id'>>({
  resourceCode: '',
  name: '',
  resourceType: 'M',
  asset: 'M',
  sortingRule: '全局排序规则',
  selectionRule: '排序规则',
  layeringRule1: '排序规则',
  layeringRule2: '排序规则',
  layeringRule3: '排序规则',
  selectionValue: '0N',
  mustComplete: 0,
  instantaneousCount: 0,
  extensionDelay: 0
});

// 计算属性
const filteredResources = computed(() => {
  if (!searchKeyword.value) return resources.value;
  const keyword = searchKeyword.value.toLowerCase();
  return resources.value.filter(resource =>
    resource.resourceCode.toLowerCase().includes(keyword) ||
    resource.name.toLowerCase().includes(keyword)
  );
});

// 方法
const goBack = () => {
  router.push('/admin/work-center-resource-group-management');
};

const refreshData = () => {
  // 模拟API调用 - 根据资源组ID获取资源列表
  loadResourceGroupData();
};

const loadResourceGroupData = async () => {
  // 这里应该调用API获取数据，现在使用模拟数据
  // 根据resourceGroupId从模拟数据中查找
  try {
    const { mockResourceGroups } = await import('./mockResourceData');
    const groupData = mockResourceGroups.find(g => g.resourceGroupCode === resourceGroupId.value);
    
    if (groupData) {
      resourceGroup.value = groupData;
      resources.value = groupData.resources || [];
      
      // 查找工作中心信息
      const workCenterData = getMockWorkCenterData();
      workCenterInfo.value = workCenterData.find(w => w.workCenter === groupData.workCenter) || null;
    }
  } catch (error) {
    console.error('加载资源组数据失败:', error);
  }
};

const getMockWorkCenterData = (): WorkCenterInfo[] => {
  return [
    { workCenter: 'WC01', name: '滾筒切紙' },
    { workCenter: 'WC02A', name: '印刷-普通油墨' },
    { workCenter: 'WC02B', name: '印刷-普通油墨' },
    { workCenter: 'WC02C', name: '印刷-普通油墨' },
    { workCenter: 'WC02D', name: '印刷-普通油墨' },
    { workCenter: 'WC02E', name: '印刷-普通油墨' },
    { workCenter: 'WC02F', name: '印刷-普通油墨' },
    { workCenter: 'WC02G', name: '印刷-普通油墨' },
    { workCenter: 'WC03', name: '過膠' },
    { workCenter: 'WC04', name: '燙金' },
    { workCenter: 'WC05', name: '絲印' },
    { workCenter: 'WC06', name: '啤機' },
    { workCenter: 'WC07', name: '模切' },
    { workCenter: 'WC08', name: '裱紙' },
    { workCenter: 'WC09', name: '摺頁' },
    { workCenter: 'WC10', name: '鎖線' },
    { workCenter: 'WC11', name: '膠裝' },
    { workCenter: 'WC12', name: '精裝' },
    { workCenter: 'WC13', name: '平裝' },
    { workCenter: 'WC14', name: '騎馬釘' },
    { workCenter: 'WC15', name: '配頁' },
    { workCenter: 'WC16', name: '綜合裝配' },
    { workCenter: 'WC17', name: '咭書裝配' },
    { workCenter: 'WC18', name: '紙袋裝配' },
    { workCenter: 'WC19', name: '賀咭裝配' },
    { workCenter: 'WC20', name: '其他裝配' },
    { workCenter: 'WC21', name: '包裝' },
    { workCenter: 'WC22', name: '手工作業' },
    { workCenter: 'WC23A', name: '手工作業' },
    { workCenter: 'WC23B', name: '手工作業' },
    { workCenter: 'WC24', name: '手工作業' },
    { workCenter: 'WC25', name: '手工作業' },
    { workCenter: 'WC26', name: '手工作業' },
    { workCenter: 'WC27', name: '手工作業' }
  ];
};

const editResource = (resource: Resource) => {
  selectedResource.value = resource;
  formData.value = {
    resourceCode: resource.resourceCode,
    name: resource.name,
    resourceType: resource.resourceType,
    asset: resource.asset,
    sortingRule: resource.sortingRule,
    selectionRule: resource.selectionRule,
    layeringRule1: resource.layeringRule1,
    layeringRule2: resource.layeringRule2,
    layeringRule3: resource.layeringRule3,
    selectionValue: resource.selectionValue,
    mustComplete: resource.mustComplete,
    instantaneousCount: resource.instantaneousCount,
    extensionDelay: resource.extensionDelay
  };
  showEditDialog.value = true;
};

const deleteResource = (resource: Resource) => {
  if (confirm(`確定要刪除資源 ${resource.resourceCode} (${resource.name}) 嗎？`)) {
    const index = resources.value.findIndex(r => r.id === resource.id);
    if (index > -1) {
      resources.value.splice(index, 1);
    }
  }
};

const saveResource = () => {
  if (showCreateDialog.value) {
    // 创建新资源
    const newResource: Resource = {
      id: resources.value.length > 0 
        ? Math.max(...resources.value.map(r => r.id || 0)) + 1 
        : 1,
      ...formData.value
    };
    resources.value.push(newResource);
  } else if (selectedResource.value) {
    // 更新现有资源
    const index = resources.value.findIndex(r => r.id === selectedResource.value?.id);
    if (index > -1) {
      resources.value[index] = {
        ...selectedResource.value,
        ...formData.value
      };
    }
  }
  closeDialog();
};

const closeDialog = () => {
  showCreateDialog.value = false;
  showEditDialog.value = false;
  selectedResource.value = null;
  formData.value = {
    resourceCode: '',
    name: '',
    resourceType: 'M',
    asset: 'M',
    sortingRule: '全局排序规则',
    selectionRule: '排序规则',
    layeringRule1: '排序规则',
    layeringRule2: '排序规则',
    layeringRule3: '排序规则',
    selectionValue: '0N',
    mustComplete: 0,
    instantaneousCount: 0,
    extensionDelay: 0
  };
};

// 生命周期
onMounted(() => {
  loadResourceGroupData();
});
</script>

<style scoped>
.sticky {
  position: sticky;
}
</style>

