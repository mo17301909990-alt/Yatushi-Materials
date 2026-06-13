<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">角色管理</h1>
        <p class="mt-2 text-gray-600">管理系統角色和權限分配</p>
      </div>

      <!-- 操作栏 -->
      <div class="mb-6 flex justify-between items-center">
        <div class="flex space-x-4">
          <button
            @click="showCreateRoleDialog = true"
            class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors"
          >
            新增角色
          </button>
          <button
            @click="refreshRoles"
            class="bg-gray-600 text-white px-4 py-2 rounded-md hover:bg-gray-700 transition-colors"
          >
            刷新
          </button>
        </div>
      </div>

      <!-- 角色列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                角色名稱
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                角色鍵
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                描述
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
            <tr v-for="role in roles" :key="role.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {{ role.roleName }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ role.roleKey }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ role.description || '-' }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                <span
                  :class="role.status === 1 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                >
                  {{ role.status === 1 ? '啟用' : '禁用' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ formatDate(role.createdTime) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <div class="flex space-x-2">
                  <button
                    @click="editRole(role)"
                    class="text-blue-600 hover:text-blue-900"
                  >
                    編輯
                  </button>
                  <button
                    @click="assignPermissions(role)"
                    class="text-green-600 hover:text-green-900"
                  >
                    分配權限
                  </button>
                  <button
                    @click="deleteRole(role)"
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

      <!-- 创建/编辑角色对话框 -->
      <div
        v-if="showCreateRoleDialog || showEditRoleDialog"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
      >
        <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              {{ showCreateRoleDialog ? '新增角色' : '編輯角色' }}
            </h3>
            <form @submit.prevent="saveRole">
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">角色名稱</label>
                <input
                  v-model="roleForm.roleName"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">角色鍵</label>
                <input
                  v-model="roleForm.roleKey"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">描述</label>
                <textarea
                  v-model="roleForm.description"
                  rows="3"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                ></textarea>
              </div>
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">狀態</label>
                <select
                  v-model="roleForm.status"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option :value="1">啟用</option>
                  <option :value="0">禁用</option>
                </select>
              </div>
              <div class="flex justify-end space-x-3">
                <button
                  type="button"
                  @click="closeRoleDialog"
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

      <!-- 分配权限对话框 -->
      <div
        v-if="showAssignPermissionsDialog"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
      >
        <div class="relative top-10 mx-auto p-5 border w-4/5 max-w-4xl shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <div class="flex justify-between items-center mb-4">
              <h3 class="text-lg font-medium text-gray-900">
                為 {{ selectedRole?.roleName }} 分配權限
              </h3>
              <div class="flex space-x-2">
                <button
                  @click="selectAllPermissions"
                  class="px-3 py-1 text-xs bg-blue-100 text-blue-700 rounded hover:bg-blue-200"
                >
                  全選
                </button>
                <button
                  @click="clearAllPermissions"
                  class="px-3 py-1 text-xs bg-gray-100 text-gray-700 rounded hover:bg-gray-200"
                >
                  清空
                </button>
              </div>
            </div>
            
            <!-- 搜索框 -->
            <div class="mb-4">
              <input
                v-model="permissionSearchKeyword"
                type="text"
                placeholder="搜索權限..."
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            
            <!-- 权限树 -->
            <div class="max-h-96 overflow-y-auto border border-gray-200 rounded-md p-4">
              <div v-for="category in filteredPermissionCategories" :key="category.name" class="mb-6">
                <!-- 分类标题 -->
                <div class="flex items-center mb-3 pb-2 border-b border-gray-200">
                  <div class="flex items-center">
                    <input
                      :id="`category-${category.name}`"
                      v-model="category.selected"
                      @change="toggleCategory(category)"
                      type="checkbox"
                      class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                    />
                    <label :for="`category-${category.name}`" class="ml-2 text-sm font-semibold text-gray-800">
                      {{ category.displayName }}
                    </label>
                  </div>
                  <span class="ml-2 text-xs text-gray-500">
                    ({{ category.selectedCount }}/{{ category.permissions.length }})
                  </span>
                </div>
                
                <!-- 分类下的权限 -->
                <div class="ml-4 space-y-2">
                  <div v-for="permission in category.permissions" :key="permission.id" class="flex items-center">
                    <input
                      :id="`permission-${permission.id}`"
                      v-model="selectedPermissionIds"
                      :value="permission.id"
                      @change="updateCategorySelection(category)"
                      type="checkbox"
                      class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                    />
                    <label :for="`permission-${permission.id}`" class="ml-2 text-sm text-gray-700 flex-1">
                      <span class="font-medium">{{ permission.permissionName }}</span>
                      <span class="text-gray-500 ml-2">({{ permission.permissionKey }})</span>
                    </label>
                    <span 
                      :class="permission.permissionType === 'MENU' ? 'bg-blue-100 text-blue-800' : 'bg-green-100 text-green-800'"
                      class="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium"
                    >
                      {{ permission.permissionType === 'MENU' ? '菜单' : '按钮' }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="flex justify-end space-x-3 mt-6">
              <button
                @click="closeAssignPermissionsDialog"
                class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
              >
                取消
              </button>
              <button
                @click="saveRolePermissions"
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
import { usePermissionStore } from '../../stores/permission';
import { useAuthStore } from '../../stores/auth';
import { permissionApi } from '../../api/modules/permission';
import { roleApi } from '../../api/modules/role';
import type { Role, Permission } from '../../api/types/permission';

// Store
const permissionStore = usePermissionStore();

// 响应式数据
const roles = ref<Role[]>([]);
const menuPermissions = ref<Permission[]>([]);
const allPermissions = ref<Permission[]>([]);
const showCreateRoleDialog = ref(false);
const showEditRoleDialog = ref(false);
const showAssignPermissionsDialog = ref(false);
const selectedRole = ref<Role | null>(null);
const selectedPermissionIds = ref<number[]>([]);
const permissionCategories = ref<any[]>([]);
const permissionSearchKeyword = ref('');

// 角色表单
const roleForm = ref({
  id: null as number | null,
  roleName: '',
  roleKey: '',
  description: '',
  status: 1
});

// 计算属性
const filteredPermissionCategories = computed(() => {
  if (!permissionSearchKeyword.value) {
    return permissionCategories.value;
  }
  
  const keyword = permissionSearchKeyword.value.toLowerCase();
  return permissionCategories.value.map(category => ({
    ...category,
    permissions: category.permissions.filter(permission =>
      permission.permissionName.toLowerCase().includes(keyword) ||
      permission.permissionKey.toLowerCase().includes(keyword)
    )
  })).filter(category => category.permissions.length > 0);
});

// 方法
const refreshRoles = async () => {
  try {
    await permissionStore.fetchRoles();
    roles.value = permissionStore.getAllRoles;
  } catch (error) {
    console.error('获取角色列表失败:', error);
  }
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('zh-CN');
};

const editRole = (role: Role) => {
  roleForm.value = {
    id: role.id || null,
    roleName: role.roleName,
    roleKey: role.roleKey,
    description: role.description || '',
    status: role.status
  };
  showEditRoleDialog.value = true;
};

const assignPermissions = async (role: Role) => {
  selectedRole.value = role;
  selectedPermissionIds.value = [];
  showAssignPermissionsDialog.value = true;
  
  // 获取角色当前权限
  try {
    const rolePermissions = await permissionApi.getPermissionsByRoleId(role.id!);
    selectedPermissionIds.value = rolePermissions.map(permission => permission.id || 0);
    updateCategoryCounts();
  } catch (error) {
    console.error('获取角色权限失败:', error);
  }
};

const deleteRole = async (role: Role) => {
  if (confirm(`確定要刪除角色 ${role.roleName} 嗎？`)) {
    try {
      await roleApi.deleteRole(role.id!);
      await refreshRoles();
    } catch (error) {
      console.error('删除角色失败:', error);
      alert('删除角色失败，请重试');
    }
  }
};

const saveRole = async () => {
  try {
    if (showCreateRoleDialog.value) {
      await permissionStore.createRole(roleForm.value);
    } else {
      await permissionStore.updateRole(roleForm.value.id!, roleForm.value);
    }
    closeRoleDialog();
    refreshRoles();
  } catch (error) {
    console.error('保存角色失败:', error);
  }
};

const saveRolePermissions = async () => {
  if (!selectedRole.value) return;
  
  try {
    await permissionApi.assignPermissionsToRole({
      roleId: selectedRole.value.id!,
      permissionIds: selectedPermissionIds.value
    });
    
    // 如果修改的是当前用户的角色，先清空权限，然后重新加载
    const authStore = useAuthStore();
    if (authStore.userInfo?.id) {
      const currentUserRoles = await roleApi.getRolesByUserId(authStore.userInfo.id);
      const currentUserRoleIds = currentUserRoles.map(role => role.id!);
      
      if (currentUserRoleIds.includes(selectedRole.value.id!)) {
        console.log('检测到修改当前用户角色权限，强制清空并重新加载');
        permissionStore.clearCurrentUserPermissions();
        
        // 延迟重新加载权限，确保后端数据已更新
        setTimeout(async () => {
          await permissionStore.refreshCurrentUserPermissions();
          console.log('当前用户权限已重新加载');
        }, 1000);
      }
    }
    
    closeAssignPermissionsDialog();
    await refreshRoles();
  } catch (error) {
    console.error('分配权限失败:', error);
    alert('分配权限失败，请重试');
  }
};

const closeRoleDialog = () => {
  showCreateRoleDialog.value = false;
  showEditRoleDialog.value = false;
  roleForm.value = {
    id: null,
    roleName: '',
    roleKey: '',
    description: '',
    status: 1
  };
};

const closeAssignPermissionsDialog = () => {
  showAssignPermissionsDialog.value = false;
  selectedRole.value = null;
  selectedPermissionIds.value = [];
};

// 权限分类相关方法
const createPermissionCategories = () => {
  const categories = [
    {
      name: 'system',
      displayName: '系统管理',
      permissions: allPermissions.value.filter(p => 
        p.permissionKey.startsWith('system:') || 
        p.permissionKey.startsWith('user:') || 
        p.permissionKey.startsWith('role:') || 
        p.permissionKey.startsWith('permission:')
      ),
      selected: false,
      selectedCount: 0
    },
    {
      name: 'process',
      displayName: '工艺配置',
      permissions: allPermissions.value.filter(p => 
        p.permissionKey.startsWith('process:') || 
        p.permissionKey.startsWith('tech:')
      ),
      selected: false,
      selectedCount: 0
    },
    {
      name: 'matching',
      displayName: '标签匹配',
      permissions: allPermissions.value.filter(p => 
        p.permissionKey.startsWith('matching:')
      ),
      selected: false,
      selectedCount: 0
    },
    {
      name: 'material',
      displayName: '物料管理',
      permissions: allPermissions.value.filter(p => 
        p.permissionKey.startsWith('material:')
      ),
      selected: false,
      selectedCount: 0
    },
    {
      name: 'preference',
      displayName: '匹配偏好配置',
      permissions: allPermissions.value.filter(p => 
        p.permissionKey.startsWith('matchPreference:')
      ),
      selected: false,
      selectedCount: 0
    },
    {
      name: 'common',
      displayName: '通用权限',
      permissions: allPermissions.value.filter(p => 
        p.permissionKey.startsWith('home:') || 
        p.permissionKey.startsWith('auth:') || 
        p.permissionKey.startsWith('profile:')
      ),
      selected: false,
      selectedCount: 0
    }
  ];
  
  permissionCategories.value = categories;
  updateCategoryCounts();
};

const updateCategoryCounts = () => {
  permissionCategories.value.forEach(category => {
    category.selectedCount = category.permissions.filter(p => 
      selectedPermissionIds.value.includes(p.id!)
    ).length;
    category.selected = category.selectedCount === category.permissions.length && category.permissions.length > 0;
  });
};

const toggleCategory = (category: any) => {
  if (category.selected) {
    // 选中分类下的所有权限
    category.permissions.forEach((permission: Permission) => {
      if (!selectedPermissionIds.value.includes(permission.id!)) {
        selectedPermissionIds.value.push(permission.id!);
      }
    });
  } else {
    // 取消选中分类下的所有权限
    category.permissions.forEach((permission: Permission) => {
      const index = selectedPermissionIds.value.indexOf(permission.id!);
      if (index > -1) {
        selectedPermissionIds.value.splice(index, 1);
      }
    });
  }
  updateCategoryCounts();
};

const updateCategorySelection = (category: any) => {
  updateCategoryCounts();
};

const selectAllPermissions = () => {
  selectedPermissionIds.value = allPermissions.value.map(p => p.id!);
  updateCategoryCounts();
};

const clearAllPermissions = () => {
  selectedPermissionIds.value = [];
  updateCategoryCounts();
};

// 生命周期
onMounted(async () => {
  try {
    await Promise.all([
      permissionStore.fetchRoles(),
      permissionStore.fetchPermissions(),
      permissionStore.fetchMenuPermissions()
    ]);
    roles.value = permissionStore.getAllRoles;
    allPermissions.value = permissionStore.getAllPermissions;
    menuPermissions.value = permissionStore.getMenuPermissions;
    createPermissionCategories();
  } catch (error) {
    console.error('初始化失败:', error);
  }
});
</script>

<style scoped>
/* 可以添加特定的样式 */
</style>
