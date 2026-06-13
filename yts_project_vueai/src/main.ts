import { createApp } from 'vue';
import { createPinia } from 'pinia';
import router from './router';
import './style.css';
import App from './App.vue';
import { useAuthStore } from './stores/auth';
import { usePermissionStore } from './stores/permission';
import { vPermission, vRole, vAdmin } from './directives/permission-simple';

// 導入 Element Plus
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import zhCn from 'element-plus/es/locale/lang/zh-cn';

const app = createApp(App);
const pinia = createPinia();
app.use(pinia);
app.use(router);
app.use(ElementPlus, {
  locale: zhCn
});

// 注册权限指令
app.directive('permission', vPermission);
app.directive('role', vRole);
app.directive('admin', vAdmin);

// 初始化权限系统
const permissionStore = usePermissionStore();
// 简化权限初始化
setTimeout(() => {
  console.log('开始初始化权限系统...');
  permissionStore.initPermissions().then(() => {
    console.log('权限系统初始化完成 - 角色:', permissionStore.currentUserRoles.length, '权限:', permissionStore.currentUserPermissions.length);
  }).catch((error) => {
    console.error('权限系统初始化失败:', error);
  });
}, 500);

// 添加用戶活動監聽，在用戶活動時自動刷新登錄狀態
const userActivityEvents = ['mousedown', 'mousemove', 'keydown', 'scroll', 'touchstart'];

// 防抖函數，避免頻繁刷新
let refreshTimeout: number | null = null;
const refreshLoginState = () => {
  if (refreshTimeout) {
    clearTimeout(refreshTimeout);
  }

  refreshTimeout = window.setTimeout(() => {
    const authStore = useAuthStore();
    if (authStore.isLoggedIn) {
      authStore.refreshLoginState();
    }
  }, 5000); // 5秒防抖
};

// 添加事件監聽
userActivityEvents.forEach(event => {
  window.addEventListener(event, refreshLoginState);
});

app.mount('#app');