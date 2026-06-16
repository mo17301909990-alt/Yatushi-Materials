import { createRouter, createWebHistory } from 'vue-router'
import ProcessConfig from '../views/ProcessConfig.vue'
import TagMatching from '../views/TagMatching.vue'
import Login from '../views/auth/Login.vue'
import Register from '../views/auth/Register.vue'
import MatchingResult from '../components/matching/MatchingResult.vue'
import MatchPreferenceConfig from '../views/matchPreference/MatchPreferenceConfig.vue'
import AdminDashboard from '../views/admin/AdminDashboard.vue'
import MaterialRuleManagement from '../views/admin/MaterialRuleManagement.vue'
import HotStampingMaterialManagement from '../views/admin/material/HotStampingMaterialManagement.vue'
import ProductTypeManagement from '../views/admin/material/ProductTypeManagement.vue'
import HotStampingTypeManagement from '../views/admin/material/HotStampingTypeManagement.vue'
import PreProcessingInterfaceManagement from '../views/admin/material/PreProcessingInterfaceManagement.vue'
import ApplicableInterfaceManagement from '../views/admin/material/ApplicableInterfaceManagement.vue'
import SmartCompatibilityManagement from '../views/admin/material/SmartCompatibilityManagement.vue'
import HotStampingPatternManagement from '../views/admin/material/HotStampingPatternManagement.vue'
import HotStampingPatternAreaManagement from '../views/admin/material/HotStampingPatternAreaManagement.vue'
import PostProcessingManagement from '../views/admin/material/PostProcessingManagement.vue'
import PostProcessingPrintManagement from '../views/admin/material/PostProcessingPrintManagement.vue'
import PostProcessingPrintUvManagement from '../views/admin/material/PostProcessingPrintUvManagement.vue'
import PostProcessingLeduvglitterManagement from '../views/admin/material/PostProcessingLeduvglitterManagement.vue'
import CommonInterfaceMappingManagement from '../views/admin/material/CommonInterfaceMappingManagement.vue'
import WearResistantGoldPaperMappingManagement from '../views/admin/material/WearResistantGoldPaperMappingManagement.vue'
import LaminatingManagement from '../views/admin/material/LaminatingManagement.vue'
import LaminationMaterialManagement from '../views/admin/material/LaminationMaterialManagement.vue'
import UserManagement from '../views/admin/UserManagement.vue'
import RoleManagement from '../views/admin/RoleManagement.vue'
import PermissionManagement from '../views/admin/PermissionManagement.vue'
import DebugPermissions from '../views/DebugPermissions.vue'
import PermissionTest from '../views/PermissionTest.vue'
import PermissionDebug from '../views/PermissionDebug.vue'
import ApiTest from '../views/ApiTest.vue'
import SimpleTest from '../views/SimpleTest.vue'
import PunchingResourceGroupSelection from '../views/PunchingResourceGroupSelection.vue'
import MountingResourceGroupSelection from '../views/MountingResourceGroupSelection.vue'
import SilkScreenResourceGroupSelection from '../views/SilkScreenResourceGroupSelection.vue'
import WorkCenterResourceGroupManagement from '../views/admin/WorkCenterResourceGroupManagement.vue'
import ResourceGroupDetail from '../views/admin/ResourceGroupDetail.vue'
import MessageCenter from '../views/message/MessageCenter.vue'
import ResourceGroupSelector from '../views/ResourceGroupSelector.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/admin/material/post-processing/laminating',
      name: 'LaminatingManagement',
      component: LaminatingManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/lamination-material',
      name: 'LaminationMaterialManagement',
      component: LaminationMaterialManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/process-config',
      name: 'ProcessConfig',
      component: ProcessConfig,
      meta: { requiresAuth: true }
    },


    {
      path: '/match-preference-config',
      name: 'MatchPreferenceConfig',
      component: MatchPreferenceConfig,
      meta: { requiresAuth: true }
    },
    {
      path: '/tag-matching',
      name: 'TagMatching',
      component: TagMatching,
      meta: { requiresAuth: true }
    },
    {
      path: '/matching-result',
      name: 'MatchingResult',
      component: MatchingResult,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin',
      name: 'AdminDashboard',
      component: AdminDashboard,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material-rule-management',
      name: 'MaterialRuleManagement',
      component: MaterialRuleManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/hot-stamping-material',
      name: 'HotStampingMaterialManagement',
      component: HotStampingMaterialManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/product-type',
      name: 'ProductTypeManagement',
      component: ProductTypeManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/hot-stamping-type',
      name: 'HotStampingTypeManagement',
      component: HotStampingTypeManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/pre-processing-interface',
      name: 'PreProcessingInterfaceManagement',
      component: PreProcessingInterfaceManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/applicable-interface',
      name: 'ApplicableInterfaceManagement',
      component: ApplicableInterfaceManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/smart-compatibility',
      name: 'SmartCompatibilityManagement',
      component: SmartCompatibilityManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/hot-stamping-pattern',
      name: 'HotStampingPatternManagement',
      component: HotStampingPatternManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/hot-stamping-pattern-area',
      name: 'HotStampingPatternAreaManagement',
      component: HotStampingPatternAreaManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/post-processing',
      name: 'PostProcessingManagement',
      component: PostProcessingManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/post-processing/printing',
      name: 'PostProcessingPrintManagement',
      component: PostProcessingPrintManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/post-processing/uv',
      name: 'PostProcessingPrintUvManagement',
      component: PostProcessingPrintUvManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/post-processing/leduvglitter',
      name: 'PostProcessingLeduvglitterManagement',
      component: PostProcessingLeduvglitterManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/post-processing/embossing/common-interface',
      name: 'CommonInterfaceMappingManagement',
      component: CommonInterfaceMappingManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/material/post-processing/embossing/wear-resistant-gold-paper',
      name: 'WearResistantGoldPaperMappingManagement',
      component: WearResistantGoldPaperMappingManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/user-management',
      name: 'UserManagement',
      component: UserManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/role-management',
      name: 'RoleManagement',
      component: RoleManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/permission-management',
      name: 'PermissionManagement',
      component: PermissionManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/debug-permissions',
      name: 'DebugPermissions',
      component: DebugPermissions,
      meta: { requiresAuth: false }
    },
    {
      path: '/permission-test',
      name: 'PermissionTest',
      component: PermissionTest,
      meta: { requiresAuth: false }
    },
    {
      path: '/permission-debug',
      name: 'PermissionDebug',
      component: PermissionDebug,
      meta: { requiresAuth: false }
    },
    {
      path: '/test-api',
      name: 'ApiTest',
      component: ApiTest,
      meta: { requiresAuth: false }
    },
    {
      path: '/simple-test',
      name: 'SimpleTest',
      component: SimpleTest,
      meta: { requiresAuth: false }
    },
    {
      path: '/punching-resource-group-selection',
      name: 'PunchingResourceGroupSelection',
      component: PunchingResourceGroupSelection,
      meta: { requiresAuth: true }
    },
    {
      path: '/mounting-resource-group-selection',
      name: 'MountingResourceGroupSelection',
      component: MountingResourceGroupSelection,
      meta: { requiresAuth: true }
    },
    {
      path: '/silkscreen-resource-group-selection',
      name: 'SilkScreenResourceGroupSelection',
      component: SilkScreenResourceGroupSelection,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/work-center-resource-group-management',
      name: 'WorkCenterResourceGroupManagement',
      component: WorkCenterResourceGroupManagement,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin/resource-group-detail/:workCenterId/:resourceGroupId',
      name: 'ResourceGroupDetail',
      component: ResourceGroupDetail,
      meta: { requiresAuth: true }
    },
    {
      path: '/sys/announcement',
      name: 'MessageCenter',
      component: MessageCenter,
      meta: { requiresAuth: true }
    },
    {
      path: '/resource-group-selector',
      name: 'ResourceGroupSelector',
      component: ResourceGroupSelector,
      meta: { requiresAuth: true }
    },
  ]
})

import { useAuthStore } from '../stores/auth';

router.beforeEach((to, _from, next) => {
  // 初始化 auth store
  const authStore = useAuthStore();

  // 確保先初始化用戶狀態
  authStore.initUserState();

  // 從 store 中獲取登錄狀態
  const isLoggedIn = authStore.isLoggedIn;

  console.log(`路由守衛: 路由到 ${to.path}, 登錄狀態: ${isLoggedIn}`);

  if (to.meta.requiresAuth && !isLoggedIn) {
    console.log('需要登錄權限，重定向到登錄頁面');
    next('/login');
  } else if (to.path === '/login' && isLoggedIn) {
    console.log('已登錄，重定向到首頁');
    next('/process-config');
  } else {
    next();
  }
});

export default router