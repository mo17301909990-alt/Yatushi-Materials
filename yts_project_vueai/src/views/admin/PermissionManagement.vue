<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">權限管理</h1>
        <p class="mt-2 text-gray-600">管理系統權限和菜單配置</p>
      </div>

      <!-- 操作栏 -->
      <div class="mb-6 flex justify-between items-center">
        <div class="flex space-x-4">
          <button
            @click="showCreatePermissionDialog = true"
            class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors"
          >
            新增權限
          </button>
          <button
            @click="refreshPermissions"
            class="bg-gray-600 text-white px-4 py-2 rounded-md hover:bg-gray-700 transition-colors"
          >
            刷新
          </button>
        </div>
        <div class="flex items-center space-x-4">
          <select
            v-model="filterType"
            class="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="">所有類型</option>
            <option value="MENU">菜單</option>
            <option value="BUTTON">按鈕</option>
          </select>
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索權限..."
            class="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
      </div>

      <!-- 权限列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                權限名稱
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                權限鍵
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                類型
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                路徑
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                狀態
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                創建時間
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                操作
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="permission in filteredPermissions" :key="permission.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {{ permission.permissionName }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ permission.permissionKey }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                <span
                  :class="permission.permissionType === 'MENU' ? 'bg-blue-100 text-blue-800' : 'bg-green-100 text-green-800'"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                >
                  {{ permission.permissionType === 'MENU' ? '菜單' : '按鈕' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ permission.path || '-' }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                <span
                  :class="permission.status === 1 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                >
                  {{ permission.status === 1 ? '啟用' : '禁用' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ formatDate(permission.createdTime) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <div class="flex space-x-2">
                  <button
                    @click="editPermission(permission)"
                    class="text-blue-600 hover:text-blue-900"
                  >
                    編輯
                  </button>
                  <button
                    @click="deletePermission(permission)"
                    class="text-red-600 hover:text-red-900"
                  >
                    刪除
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 创建/编辑权限对话框 -->
      <div
        v-if="showCreatePermissionDialog || showEditPermissionDialog"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
      >
        <div class="relative top-10 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              {{ showCreatePermissionDialog ? '新增權限' : '編輯權限' }}
            </h3>
            
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-700">權限名稱</label>
                <input
                  v-model="permissionForm.permissionName"
                  type="text"
                  class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700">權限鍵</label>
                <input
                  v-model="permissionForm.permissionKey"
                  type="text"
                  class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700">權限類型</label>
                <select
                  v-model="permissionForm.permissionType"
                  class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option value="MENU">菜單</option>
                  <option value="BUTTON">按鈕</option>
                </select>
              </div>
              
              <div v-if="permissionForm.permissionType === 'MENU'">
                <label class="block text-sm font-medium text-gray-700">路徑</label>
                <input
                  v-model="permissionForm.path"
                  type="text"
                  class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              
              <div v-if="permissionForm.permissionType === 'MENU'">
                <label class="block text-sm font-medium text-gray-700">組件</label>
                <input
                  v-model="permissionForm.component"
                  type="text"
                  class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              
              <div v-if="permissionForm.permissionType === 'MENU'">
                <label class="block text-sm font-medium text-gray-700">圖標</label>
                <input
                  v-model="permissionForm.icon"
                  type="text"
                  class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700">排序</label>
                <input
                  v-model.number="permissionForm.orderNum"
                  type="number"
                  class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              
              <div>
                <label class="block text-sm font-medium text-gray-700">狀態</label>
                <select
                  v-model="permissionForm.status"
                  class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option :value="1">啟用</option>
                  <option :value="0">禁用</option>
                </select>
              </div>
            </div>
            
            <div class="flex justify-end space-x-3 mt-6">
              <button
                @click="closePermissionDialog"
                class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
              >
                取消
              </button>
              <button
                @click="savePermission"
                class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700"
              >
                保存
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { permissionApi } from '../../api/modules/permission';
import type { Permission, CreatePermissionRequest, UpdatePermissionRequest } from '../../api/types/permission';

// 响应式数据
const permissions = ref<Permission[]>([]);
const searchKeyword = ref('');
const filterType = ref('');
const showCreatePermissionDialog = ref(false);
const showEditPermissionDialog = ref(false);

// 权限表单
const permissionForm = ref<CreatePermissionRequest>({
  permissionName: '',
  permissionKey: '',
  permissionType: 'BUTTON',
  path: '',
  component: '',
  icon: '',
  orderNum: 0,
  status: 1
});

// 计算属性
const filteredPermissions = computed(() => {
  let result = permissions.value;
  
  if (filterType.value) {
    result = result.filter(permission => permission.permissionType === filterType.value);
  }
  
  if (searchKeyword.value) {
    result = result.filter(permission =>
      permission.permissionName.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
      permission.permissionKey.toLowerCase().includes(searchKeyword.value.toLowerCase())
    );
  }
  
  return result;
});

// 方法
const refreshPermissions = async () => {
  try {
    permissions.value = await permissionApi.getAllPermissions();
  } catch (error) {
    console.error('获取权限列表失败:', error);
  }
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('zh-CN');
};

const editPermission = (permission: Permission) => {
  permissionForm.value = {
    permissionName: permission.permissionName,
    permissionKey: permission.permissionKey,
    permissionType: permission.permissionType,
    path: permission.path || '',
    component: permission.component || '',
    icon: permission.icon || '',
    orderNum: permission.orderNum || 0,
    status: permission.status
  };
  showEditPermissionDialog.value = true;
};

const deletePermission = async (permission: Permission) => {
  if (confirm(`確定要刪除權限 ${permission.permissionName} 嗎？`)) {
    try {
      await permissionApi.deletePermission(permission.id!);
      await refreshPermissions();
    } catch (error) {
      console.error('删除权限失败:', error);
      alert('删除权限失败，请重试');
    }
  }
};

const savePermission = async () => {
  try {
    if (showCreatePermissionDialog.value) {
      await permissionApi.createPermission(permissionForm.value);
    } else {
      const permission = permissions.value.find(p => 
        p.permissionName === permissionForm.value.permissionName
      );
      if (permission) {
        await permissionApi.updatePermission(permission.id!, permissionForm.value as UpdatePermissionRequest);
      }
    }
    closePermissionDialog();
    await refreshPermissions();
  } catch (error) {
    console.error('保存权限失败:', error);
    alert('保存权限失败，请重试');
  }
};

const closePermissionDialog = () => {
  showCreatePermissionDialog.value = false;
  showEditPermissionDialog.value = false;
  permissionForm.value = {
    permissionName: '',
    permissionKey: '',
    permissionType: 'BUTTON',
    path: '',
    component: '',
    icon: '',
    orderNum: 0,
    status: 1
  };
};

// 生命周期
onMounted(async () => {
  await refreshPermissions();
});
</script>

<style scoped>
/* 可以添加特定的样式 */
</style>







