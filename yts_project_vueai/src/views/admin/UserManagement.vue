<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-3xl font-bold text-gray-900">用戶管理</h1>
        <p class="mt-2 text-gray-600">管理系統用戶賬戶和權限</p>
      </div>

      <!-- 操作栏 -->
      <div class="mb-6 flex justify-between items-center">
        <div class="flex space-x-4">
          <button
            @click="showCreateUserDialog = true"
            class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition-colors"
          >
            新增用戶
          </button>
          <button
            @click="refreshUsers"
            class="bg-gray-600 text-white px-4 py-2 rounded-md hover:bg-gray-700 transition-colors"
          >
            刷新
          </button>
        </div>
        <div class="flex items-center space-x-4">
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索用戶..."
            class="px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
      </div>

      <!-- 用户列表 -->
      <div class="bg-white rounded-lg shadow-md overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                用戶名
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                郵箱
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                角色
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
            <tr v-for="user in filteredUsers" :key="user.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {{ user.username }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ user.email }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                <div class="flex flex-wrap gap-1">
                  <span
                    v-for="role in getUserRoles(user.id)"
                    :key="role.id"
                    class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-blue-100 text-blue-800"
                  >
                    {{ role.roleName }}
                  </span>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                <span
                  :class="user.is_active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'"
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium"
                >
                  {{ user.is_active ? '啟用' : '禁用' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ formatDate(user.created_at) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <div class="flex space-x-2">
                  <button
                    @click="editUser(user)"
                    class="text-blue-600 hover:text-blue-900"
                  >
                    編輯
                  </button>
                  <button
                    @click="assignRoles(user)"
                    class="text-green-600 hover:text-green-900"
                  >
                    分配角色
                  </button>
                  <button
                    @click="deleteUser(user)"
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

      <!-- 创建/编辑用户对话框 -->
      <div
        v-if="showCreateUserDialog || showEditUserDialog"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
      >
        <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              {{ showCreateUserDialog ? '新增用戶' : '編輯用戶' }}
            </h3>
            <form @submit.prevent="saveUser">
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">用戶名</label>
                <input
                  v-model="userForm.username"
                  type="text"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">郵箱</label>
                <input
                  v-model="userForm.email"
                  type="email"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div v-if="showCreateUserDialog" class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">密碼</label>
                <input
                  v-model="userForm.password"
                  type="password"
                  required
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>
              <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">狀態</label>
                <select
                  v-model="userForm.is_active"
                  class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  <option :value="true">啟用</option>
                  <option :value="false">禁用</option>
                </select>
              </div>
              <div class="flex justify-end space-x-3">
                <button
                  type="button"
                  @click="closeUserDialog"
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

      <!-- 分配角色对话框 -->
      <div
        v-if="showAssignRolesDialog"
        class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
      >
        <div class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white">
          <div class="mt-3">
            <h3 class="text-lg font-medium text-gray-900 mb-4">
              為 {{ selectedUser?.username }} 分配角色
            </h3>
            <div class="mb-4">
              <div v-for="role in roles" :key="role.id" class="flex items-center mb-2">
                <input
                  :id="`role-${role.id}`"
                  v-model="selectedRoleIds"
                  :value="role.id"
                  type="checkbox"
                  class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
                <label :for="`role-${role.id}`" class="ml-2 text-sm text-gray-700">
                  {{ role.roleName }} ({{ role.roleKey }})
                </label>
              </div>
            </div>
            <div class="flex justify-end space-x-3">
              <button
                @click="closeAssignRolesDialog"
                class="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-100 rounded-md hover:bg-gray-200"
              >
                取消
              </button>
              <button
                @click="saveUserRoles"
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
import { authApi } from '../../api/modules/auth';
import { userApi } from '../../api/modules/user';
import type { UserInfo } from '../../api/types/auth';
import type { Role } from '../../api/types/permission';

// Store
const permissionStore = usePermissionStore();

// 响应式数据
const users = ref<UserInfo[]>([]);
const roles = ref<Role[]>([]);
const userRolesMap = ref<Record<number, Role[]>>({});
const searchKeyword = ref('');
const showCreateUserDialog = ref(false);
const showEditUserDialog = ref(false);
const showAssignRolesDialog = ref(false);
const selectedUser = ref<UserInfo | null>(null);
const selectedRoleIds = ref<number[]>([]);

// 用户表单
const userForm = ref({
  id: null as number | null,
  username: '',
  email: '',
  password: '',
  is_active: true
});

// 计算属性
const filteredUsers = computed(() => {
  if (!searchKeyword.value) return users.value;
  return users.value.filter(user =>
    user.username.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
    user.email.toLowerCase().includes(searchKeyword.value.toLowerCase())
  );
});

// 方法
const refreshUsers = async () => {
  try {
    const response = await userApi.getAllUsers();
    users.value = response.data || [];
    
    // 获取每个用户的角色信息
    for (const user of users.value) {
      if (user.id) {
        try {
          const userRoles = await userApi.getUserRoles(user.id);
          userRolesMap.value[user.id] = userRoles;
        } catch (error) {
          console.error(`获取用户 ${user.id} 角色失败:`, error);
          userRolesMap.value[user.id] = [];
        }
      }
    }
  } catch (error) {
    console.error('获取用户列表失败:', error);
  }
};

const getUserRoles = (userId: number) => {
  // 根据用户ID获取角色信息
  const userRoles = userRolesMap.value[userId] || [];
  return userRoles;
};

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString('zh-CN');
};

const editUser = (user: UserInfo) => {
  userForm.value = {
    id: user.id || null,
    username: user.username,
    email: user.email,
    password: '',
    is_active: true
  };
  showEditUserDialog.value = true;
};

const assignRoles = async (user: UserInfo) => {
  selectedUser.value = user;
  selectedRoleIds.value = [];
  showAssignRolesDialog.value = true;
  
  // 获取用户当前角色
  try {
    const userRoles = await permissionStore.fetchCurrentUserRoles(user.id || 0);
    selectedRoleIds.value = userRoles.map(role => role.id || 0);
  } catch (error) {
    console.error('获取用户角色失败:', error);
  }
};

const deleteUser = async (user: UserInfo) => {
  if (confirm(`確定要刪除用戶 ${user.username} 嗎？`)) {
    try {
      await userApi.deleteUser(user.id!);
      await refreshUsers();
    } catch (error) {
      console.error('删除用户失败:', error);
      alert('删除用户失败，请重试');
    }
  }
};

const saveUser = async () => {
  try {
    if (showCreateUserDialog.value) {
      // 创建用户
      await authApi.register({
        username: userForm.value.username,
        email: userForm.value.email,
        password: userForm.value.password
      });
    } else {
      // 更新用户
      await userApi.updateUser(userForm.value.id!, {
        username: userForm.value.username,
        email: userForm.value.email,
        is_active: userForm.value.is_active
      });
    }
    closeUserDialog();
    await refreshUsers();
  } catch (error) {
    console.error('保存用户失败:', error);
    alert('保存用户失败，请重试');
  }
};

const saveUserRoles = async () => {
  if (!selectedUser.value) return;
  
  try {
    await permissionStore.assignRolesToUser(selectedUser.value.id || 0, selectedRoleIds.value);
    closeAssignRolesDialog();
    refreshUsers();
  } catch (error) {
    console.error('分配角色失败:', error);
  }
};

const closeUserDialog = () => {
  showCreateUserDialog.value = false;
  showEditUserDialog.value = false;
  userForm.value = {
    id: null,
    username: '',
    email: '',
    password: '',
    is_active: true
  };
};

const closeAssignRolesDialog = () => {
  showAssignRolesDialog.value = false;
  selectedUser.value = null;
  selectedRoleIds.value = [];
};

// 生命周期
onMounted(async () => {
  try {
    await permissionStore.fetchRoles();
    roles.value = permissionStore.getAllRoles;
    refreshUsers();
  } catch (error) {
    console.error('初始化失败:', error);
  }
});
</script>

<style scoped>
/* 可以添加特定的样式 */
</style>
