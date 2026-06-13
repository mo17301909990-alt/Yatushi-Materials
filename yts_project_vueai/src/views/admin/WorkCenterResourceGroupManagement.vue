<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-[1800px] mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">資源組管理</h1>
        <p class="mt-2 text-gray-600">管理工作中心資源組配置信息</p>
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
            新增資源組
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
            placeholder="搜索工作中心、名稱..."
            class="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 w-64"
          />
          <select
            v-model="filterDepartment"
            class="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">全部部門</option>
            <option v-for="dept in departments" :key="dept" :value="dept">{{ dept }}</option>
          </select>
        </div>
      </div>

      <!-- 数据表格 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider sticky left-0 bg-gray-50 z-10" style="min-width: 90px;">
                  工作中心
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 130px;">
                  名稱
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 110px;">
                  排產粒度
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 80px;">
                  部門
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 200px;">
                  部門說明
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 70px;">
                  日历
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 100px;">
                  反冲
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 120px;">
                  间接费基准
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 120px;">
                  排产驱动因素
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 70px;">
                  效率
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 90px;">
                  排队时数
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 90px;">
                  完成时数
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 80px;">
                  控制点
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 70px;">
                  外协
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 120px;">
                  資源組(個數)
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 100px;">
                  資源(個數)
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 140px;">
                  預測資源組(個數)
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 120px;">
                  預測資源(個數)
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 90px;">
                  移送時數
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider" style="min-width: 120px;">
                  備注
                </th>
                <th class="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider sticky right-0 bg-gray-50 z-10" style="min-width: 160px;">
                  操作
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-for="item in filteredData" :key="item.id" class="hover:bg-gray-50">
                <td class="px-4 py-3 whitespace-nowrap text-sm font-medium text-gray-900 sticky left-0 bg-white z-10">
                  {{ item.workCenter }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.name }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.schedulingGranularity }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.department }}
                </td>
                <td class="px-4 py-3 text-sm text-gray-500" style="min-width: 200px;">
                  {{ item.departmentDescription }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.calendar }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.backflush }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.indirectCostBasis }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.schedulingDriver }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  <span :class="getEfficiencyClass(item.efficiency)">
                    {{ item.efficiency }}
                  </span>
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.queueHours }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.completionHours }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.controlPoint }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.outsourcing }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  <span :class="getResourceGroupClass(item.resourceGroupCount)">
                    {{ item.resourceGroupCount }}
                  </span>
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.resourceCount }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.forecastResourceGroupCount }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.forecastResourceCount }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm text-gray-500">
                  {{ item.transferHours }}
                </td>
                <td class="px-4 py-3 text-sm text-gray-500" style="min-width: 120px;">
                  {{ item.remarks || '-' }}
                </td>
                <td class="px-4 py-3 whitespace-nowrap text-sm font-medium sticky right-0 bg-white z-10" style="min-width: 160px;">
                  <div class="flex space-x-2">
                    <button
                      @click="viewResources(item)"
                      class="text-green-600 hover:text-green-900 whitespace-nowrap"
                      title="查看資源"
                    >
                      查看資源
                    </button>
                    <button
                      @click="editItem(item)"
                      class="text-blue-600 hover:text-blue-900 whitespace-nowrap"
                      title="編輯"
                    >
                      編輯
                    </button>
                    <button
                      @click="deleteItem(item)"
                      class="text-red-600 hover:text-red-900 whitespace-nowrap"
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
      </div>

      <!-- 创建/编辑对话框 -->
      <div
        v-if="showCreateDialog || showEditDialog"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
        @click.self="closeDialog"
      >
        <div class="relative top-10 mx-auto p-6 border w-[90%] max-w-6xl shadow-lg rounded-md bg-white max-h-[90vh] overflow-y-auto">
          <div class="mt-3">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              {{ showCreateDialog ? '新增資源組' : '編輯資源組' }}
            </h3>
            <form @submit.prevent="saveItem" class="space-y-4">
              <div class="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">工作中心 *</label>
                  <input
                    v-model="formData.workCenter"
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
                  <label class="block text-sm font-medium text-gray-700 mb-2">排產粒度</label>
                  <select
                    v-model="formData.schedulingGranularity"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="到機">到機</option>
                    <option value="到線(工序)">到線(工序)</option>
                    <option value="到毛書線">到毛書線</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">部門</label>
                  <input
                    v-model="formData.department"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">部門說明</label>
                  <input
                    v-model="formData.departmentDescription"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">日历</label>
                  <input
                    v-model="formData.calendar"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">反冲</label>
                  <select
                    v-model="formData.backflush"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="兩者都不">兩者都不</option>
                    <option value="兩者">兩者</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">间接费基准</label>
                  <input
                    v-model="formData.indirectCostBasis"
                    type="text"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">排产驱动因素</label>
                  <select
                    v-model="formData.schedulingDriver"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="設備">設備</option>
                    <option value="人工">人工</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">效率</label>
                  <input
                    v-model.number="formData.efficiency"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">排队时数</label>
                  <input
                    v-model.number="formData.queueHours"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">完成时数</label>
                  <input
                    v-model.number="formData.completionHours"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">控制点</label>
                  <input
                    v-model.number="formData.controlPoint"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">外协</label>
                  <input
                    v-model.number="formData.outsourcing"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">資源組(個數)</label>
                  <input
                    v-model.number="formData.resourceGroupCount"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">資源(個數)</label>
                  <input
                    v-model.number="formData.resourceCount"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">預測資源組(個數)</label>
                  <input
                    v-model.number="formData.forecastResourceGroupCount"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">預測資源(個數)</label>
                  <input
                    v-model.number="formData.forecastResourceCount"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-700 mb-2">移送時數</label>
                  <input
                    v-model.number="formData.transferHours"
                    type="number"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>
                <div class="md:col-span-2 lg:col-span-4">
                  <label class="block text-sm font-medium text-gray-700 mb-2">備注</label>
                  <textarea
                    v-model="formData.remarks"
                    rows="3"
                    class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  ></textarea>
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
import { useRouter } from 'vue-router';
import type { WorkCenterResourceGroup } from '../../api/modules/workCenter';

const router = useRouter();

// 模拟数据
const mockData: WorkCenterResourceGroup[] = [
  {
    id: 1,
    workCenter: 'WC01',
    name: '滾筒切紙',
    schedulingGranularity: '到機',
    department: 'DP01',
    departmentDescription: '製造中心_印刷部',
    calendar: 'DSC',
    backflush: '兩者',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 6,
    completionHours: 2,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 8,
    resourceCount: 26,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 22,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 2,
    workCenter: 'WC02A',
    name: '印刷-普通油墨',
    schedulingGranularity: '到機',
    department: 'DP01',
    departmentDescription: '製造中心_印刷部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 9,
    completionHours: 48,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 8,
    resourceCount: 21,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 17,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 3,
    workCenter: 'WC02B',
    name: '印刷-普通油墨',
    schedulingGranularity: '到機',
    department: 'DP01',
    departmentDescription: '製造中心_印刷部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 9,
    completionHours: 48,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 8,
    resourceCount: 15,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 24,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 4,
    workCenter: 'WC02C',
    name: '印刷-普通油墨',
    schedulingGranularity: '到機',
    department: 'DP01',
    departmentDescription: '製造中心_印刷部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 9,
    completionHours: 1,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 11,
    resourceCount: 25,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 14,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 5,
    workCenter: 'WC02D',
    name: '印刷-普通油墨',
    schedulingGranularity: '到機',
    department: 'DP01',
    departmentDescription: '製造中心_印刷部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 9,
    completionHours: 1,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 10,
    resourceCount: 14,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 11,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 6,
    workCenter: 'WC02E',
    name: '印刷-普通油墨',
    schedulingGranularity: '到機',
    department: 'DP01',
    departmentDescription: '製造中心_印刷部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 9,
    completionHours: 1,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 11,
    resourceCount: 34,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 18,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 7,
    workCenter: 'WC02F',
    name: '印刷-普通油墨',
    schedulingGranularity: '到機',
    department: 'DP01',
    departmentDescription: '製造中心_印刷部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 9,
    completionHours: 1,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 10,
    resourceCount: 18,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 9,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 8,
    workCenter: 'WC02G',
    name: '印刷-普通油墨',
    schedulingGranularity: '到機',
    department: 'DP01',
    departmentDescription: '製造中心_印刷部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 9,
    completionHours: 1,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 9,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 5,
    transferHours: 4,
    remarks: '增加完成日'
  },
  {
    id: 9,
    workCenter: 'WC03',
    name: '過膠',
    schedulingGranularity: '到機',
    department: 'DP02',
    departmentDescription: '製造中心_后加工部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 72,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 5,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 11,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 10,
    workCenter: 'WC04',
    name: '燙金',
    schedulingGranularity: '到機',
    department: 'DP02',
    departmentDescription: '製造中心_后加工部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 4,
    resourceCount: 13,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 36,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 11,
    workCenter: 'WC05',
    name: '絲印',
    schedulingGranularity: '到機',
    department: 'DP02',
    departmentDescription: '製造中心_后加工部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 7,
    resourceCount: 41,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 25,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 12,
    workCenter: 'WC06',
    name: '啤機',
    schedulingGranularity: '到機',
    department: 'DP02',
    departmentDescription: '製造中心_后加工部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 4,
    resourceCount: 27,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 101,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 13,
    workCenter: 'WC07',
    name: '模切',
    schedulingGranularity: '到機',
    department: 'DP02',
    departmentDescription: '製造中心_后加工部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 6,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 158,
    forecastResourceGroupCount: 0,
    forecastResourceCount: 0,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 14,
    workCenter: 'WC08',
    name: '裱紙',
    schedulingGranularity: '到機',
    department: 'DP02',
    departmentDescription: '製造中心_后加工部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 11,
    resourceCount: 17,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 12,
    transferHours: 4,
    remarks: '增加完成日'
  },
  {
    id: 15,
    workCenter: 'WC09',
    name: '摺頁',
    schedulingGranularity: '到機',
    department: 'DP03',
    departmentDescription: '製造中心_精平裝裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 46,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 6,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 16,
    workCenter: 'WC10',
    name: '鎖線',
    schedulingGranularity: '到線(工序)',
    department: 'DP03',
    departmentDescription: '製造中心_精平裝裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 12,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 73,
    transferHours: 4,
    remarks: '增加完成日'
  },
  {
    id: 17,
    workCenter: 'WC11',
    name: '膠裝',
    schedulingGranularity: '到機',
    department: 'DP03',
    departmentDescription: '製造中心_精平裝裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 16,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 16,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 18,
    workCenter: 'WC12',
    name: '精裝',
    schedulingGranularity: '到機',
    department: 'DP03',
    departmentDescription: '製造中心_精平裝裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 4,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 4,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 19,
    workCenter: 'WC13',
    name: '平裝',
    schedulingGranularity: '到機',
    department: 'DP03',
    departmentDescription: '製造中心_精平裝裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 220,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 10,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 20,
    workCenter: 'WC14',
    name: '騎馬釘',
    schedulingGranularity: '到毛書線',
    department: 'DP03',
    departmentDescription: '製造中心_精平裝裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 3,
    resourceCount: 201,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 7,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 21,
    workCenter: 'WC15',
    name: '配頁',
    schedulingGranularity: '到機',
    department: 'DP03',
    departmentDescription: '製造中心_精平裝裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 3,
    resourceCount: 27,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 15,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 22,
    workCenter: 'WC16',
    name: '綜合裝配',
    schedulingGranularity: '到機',
    department: 'DP04',
    departmentDescription: '製造中心_綜合裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 8,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 8,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 23,
    workCenter: 'WC17',
    name: '咭書裝配',
    schedulingGranularity: '到機',
    department: 'DP05',
    departmentDescription: '製造中心_賀咭紙袋裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 7,
    resourceCount: 219,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 219,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 24,
    workCenter: 'WC18',
    name: '紙袋裝配',
    schedulingGranularity: '到機',
    department: 'DP05',
    departmentDescription: '製造中心_賀咭紙袋裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 200,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 200,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 25,
    workCenter: 'WC19',
    name: '賀咭裝配',
    schedulingGranularity: '到機',
    department: 'DP05',
    departmentDescription: '製造中心_賀咭紙袋裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 4,
    resourceCount: 27,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 27,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 26,
    workCenter: 'WC20',
    name: '其他裝配',
    schedulingGranularity: '到機',
    department: 'DP05',
    departmentDescription: '製造中心_賀咭紙袋裝配部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 3,
    resourceCount: 17,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 17,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 27,
    workCenter: 'WC21',
    name: '包裝',
    schedulingGranularity: '到機',
    department: 'DP06',
    departmentDescription: '製造中心_包裝部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 46,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 46,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 28,
    workCenter: 'WC22',
    name: '手工作業',
    schedulingGranularity: '到線(工序)',
    department: 'DP07',
    departmentDescription: '製造中心_手工作業部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '人工',
    efficiency: 83,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 5,
    resourceCount: 12,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 12,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 29,
    workCenter: 'WC23A',
    name: '手工作業',
    schedulingGranularity: '到線(工序)',
    department: 'DP07',
    departmentDescription: '製造中心_手工作業部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '人工',
    efficiency: 88,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 2,
    resourceCount: 73,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 73,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 30,
    workCenter: 'WC23B',
    name: '手工作業',
    schedulingGranularity: '到線(工序)',
    department: 'DP07',
    departmentDescription: '製造中心_手工作業部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 95,
    queueHours: 48,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 6,
    resourceCount: 16,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 16,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 31,
    workCenter: 'WC24',
    name: '手工作業',
    schedulingGranularity: '到線(工序)',
    department: 'DP07',
    departmentDescription: '製造中心_手工作業部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 85,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 2,
    resourceCount: 4,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 4,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 32,
    workCenter: 'WC25',
    name: '手工作業',
    schedulingGranularity: '到線(工序)',
    department: 'DP07',
    departmentDescription: '製造中心_手工作業部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 65,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 2,
    resourceCount: 10,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 10,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 33,
    workCenter: 'WC26',
    name: '手工作業',
    schedulingGranularity: '到線(工序)',
    department: 'DP07',
    departmentDescription: '製造中心_手工作業部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 70,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 2,
    resourceCount: 7,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 7,
    transferHours: 4,
    remarks: ''
  },
  {
    id: 34,
    workCenter: 'WC27',
    name: '手工作業',
    schedulingGranularity: '到線(工序)',
    department: 'DP07',
    departmentDescription: '製造中心_手工作業部',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '人工',
    efficiency: 83,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 2,
    resourceCount: 15,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 15,
    transferHours: 4,
    remarks: ''
  }
];

// 响应式数据
const workCenters = ref<WorkCenterResourceGroup[]>([]);
const searchKeyword = ref('');
const filterDepartment = ref('');
const showCreateDialog = ref(false);
const showEditDialog = ref(false);
const selectedItem = ref<WorkCenterResourceGroup | null>(null);

// 表单数据
const formData = ref<Omit<WorkCenterResourceGroup, 'id'>>({
  workCenter: '',
  name: '',
  schedulingGranularity: '到機',
  department: '',
  departmentDescription: '',
  calendar: 'DSC',
  backflush: '兩者都不',
  indirectCostBasis: 'MLC',
  schedulingDriver: '設備',
  efficiency: 100,
  queueHours: 36,
  completionHours: 0,
  controlPoint: 1,
  outsourcing: 0,
  resourceGroupCount: 1,
  resourceCount: 0,
  forecastResourceGroupCount: 1,
  forecastResourceCount: 0,
  transferHours: 4,
  remarks: ''
});

// 计算属性
const departments = computed(() => {
  const deptSet = new Set(workCenters.value.map(item => item.department));
  return Array.from(deptSet).sort();
});

const filteredData = computed(() => {
  let result = workCenters.value;

  // 搜索过滤
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase();
    result = result.filter(item =>
      item.workCenter.toLowerCase().includes(keyword) ||
      item.name.toLowerCase().includes(keyword) ||
      item.departmentDescription.toLowerCase().includes(keyword)
    );
  }

  // 部门过滤
  if (filterDepartment.value) {
    result = result.filter(item => item.department === filterDepartment.value);
  }

  return result;
});

// 方法
const refreshData = () => {
  // 模拟API调用，实际使用时替换为真实API
  workCenters.value = [...mockData];
};

const getEfficiencyClass = (efficiency: number) => {
  if (efficiency < 70) return 'text-red-600 font-semibold';
  if (efficiency < 85) return 'text-orange-600 font-semibold';
  if (efficiency < 100) return 'text-yellow-600 font-semibold';
  return 'text-gray-500';
};

const getResourceGroupClass = (count: number) => {
  if (count > 5) return 'text-blue-600 font-semibold';
  return 'text-gray-500';
};

const viewResources = async (item: WorkCenterResourceGroup) => {
  // 根据工作中心查找对应的资源组编码
  // 这里需要根据实际数据结构来获取第一个资源组编码
  // 暂时使用工作中心代码来查找对应的资源组
  try {
    const { mockResourceGroups } = await import('./mockResourceData');
    const resourceGroup = mockResourceGroups.find(
      rg => rg.workCenter === item.workCenter
    );
    
    if (resourceGroup) {
      router.push({
        name: 'ResourceGroupDetail',
        params: {
          workCenterId: item.workCenter,
          resourceGroupId: resourceGroup.resourceGroupCode
        }
      });
    } else {
      // 如果没有找到资源组，使用工作中心代码作为资源组编码
      router.push({
        name: 'ResourceGroupDetail',
        params: {
          workCenterId: item.workCenter,
          resourceGroupId: `${item.workCenter}-RSC001`
        }
      });
    }
  } catch (error) {
    console.error('跳转失败:', error);
    // 如果导入失败，使用默认资源组编码
    router.push({
      name: 'ResourceGroupDetail',
      params: {
        workCenterId: item.workCenter,
        resourceGroupId: `${item.workCenter}-RSC001`
      }
    });
  }
};

const editItem = (item: WorkCenterResourceGroup) => {
  selectedItem.value = item;
  formData.value = {
    workCenter: item.workCenter,
    name: item.name,
    schedulingGranularity: item.schedulingGranularity,
    department: item.department,
    departmentDescription: item.departmentDescription,
    calendar: item.calendar,
    backflush: item.backflush,
    indirectCostBasis: item.indirectCostBasis,
    schedulingDriver: item.schedulingDriver,
    efficiency: item.efficiency,
    queueHours: item.queueHours,
    completionHours: item.completionHours,
    controlPoint: item.controlPoint,
    outsourcing: item.outsourcing,
    resourceGroupCount: item.resourceGroupCount,
    resourceCount: item.resourceCount,
    forecastResourceGroupCount: item.forecastResourceGroupCount,
    forecastResourceCount: item.forecastResourceCount,
    transferHours: item.transferHours,
    remarks: item.remarks || ''
  };
  showEditDialog.value = true;
};

const deleteItem = (item: WorkCenterResourceGroup) => {
  if (confirm(`確定要刪除工作中心 ${item.workCenter} (${item.name}) 嗎？`)) {
    const index = workCenters.value.findIndex(w => w.id === item.id);
    if (index > -1) {
      workCenters.value.splice(index, 1);
    }
  }
};

const saveItem = () => {
  if (showCreateDialog.value) {
    // 创建新项
    const newItem: WorkCenterResourceGroup = {
      id: workCenters.value.length > 0 
        ? Math.max(...workCenters.value.map(w => w.id || 0)) + 1 
        : 1,
      ...formData.value
    };
    workCenters.value.push(newItem);
  } else if (selectedItem.value) {
    // 更新现有项
    const index = workCenters.value.findIndex(w => w.id === selectedItem.value?.id);
    if (index > -1) {
      workCenters.value[index] = {
        ...selectedItem.value,
        ...formData.value
      };
    }
  }
  closeDialog();
};

const closeDialog = () => {
  showCreateDialog.value = false;
  showEditDialog.value = false;
  selectedItem.value = null;
  formData.value = {
    workCenter: '',
    name: '',
    schedulingGranularity: '到機',
    department: '',
    departmentDescription: '',
    calendar: 'DSC',
    backflush: '兩者都不',
    indirectCostBasis: 'MLC',
    schedulingDriver: '設備',
    efficiency: 100,
    queueHours: 36,
    completionHours: 0,
    controlPoint: 1,
    outsourcing: 0,
    resourceGroupCount: 1,
    resourceCount: 0,
    forecastResourceGroupCount: 1,
    forecastResourceCount: 0,
    transferHours: 4,
    remarks: ''
  };
};

// 生命周期
onMounted(() => {
  refreshData();
});
</script>

<style scoped>
/* 表格横向滚动时的粘性列样式 */
.sticky {
  position: sticky;
}
</style>

