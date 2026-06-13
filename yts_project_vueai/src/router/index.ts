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
import MatrixExportImportManagement from '../views/admin/material/MatrixExportImportManagement.vue'
import HotStampingPatternManagement from '../views/admin/material/HotStampingPatternManagement.vue'
import HotStampingPatternAreaManagement from '../views/admin/material/HotStampingPatternAreaManagement.vue'
import PostProcessingManagement from '../views/admin/material/PostProcessingManagement.vue'
import PostProcessingPrintManagement from '../views/admin/material/PostProcessingPrintManagement.vue'
import PostProcessingPrintUvManagement from '../views/admin/material/PostProcessingPrintUvManagement.vue'
import PostProcessingLeduvglitterManagement from '../views/admin/material/PostProcessingLeduvglitterManagement.vue'
import CommonInterfaceMappingManagement from '../views/admin/material/CommonInterfaceMappingManagement.vue'
import WearResistantGoldPaperMappingManagement from '../views/admin/material/WearResistantGoldPaperMappingManagement.vue'
import SeriesPriorityRuleManagement from '../views/admin/material/SeriesPriorityRuleManagement.vue'
import LaminatingManagement from '../views/admin/material/LaminatingManagement.vue'
import WaterVarnishMatteProductManage from '../views/admin/water_varnish_matte/ProductManage.vue'
import WaterVarnishMatteCompatibilityManage from '../views/admin/water_varnish_matte/CompatibilityManage.vue'
import UvOilMatteProductManage from '../views/admin/uv_oil_matte/ProductManage.vue'
import UvOilMatteCompatibilityManage from '../views/admin/uv_oil_matte/CompatibilityManage.vue'
import SiliconeWhiteUvProductManage from '../views/admin/silicone_white_uv/ProductManage.vue'
import SiliconeWhiteUvCompatibilityManage from '../views/admin/silicone_white_uv/CompatibilityManage.vue'
import SiliconeSandblastUvProductManage from '../views/admin/silicone_sandblast_uv/ProductManage.vue'
import SiliconeSandblastUvCompatibilityManage from '../views/admin/silicone_sandblast_uv/CompatibilityManage.vue'
import SiliconeWrinkleUvProductManage from '../views/admin/silicone_wrinkle_uv/ProductManage.vue'
import SiliconeWrinkleUvCompatibilityManage from '../views/admin/silicone_wrinkle_uv/CompatibilityManage.vue'
import SiliconeCoarseUvProductManage from '../views/admin/silicone_coarse_uv/ProductManage.vue'
import SiliconeCoarseUvCompatibilityManage from '../views/admin/silicone_coarse_uv/CompatibilityManage.vue'
import SiliconeOrangePeelUvProductManage from '../views/admin/silicone_orange_peel_uv/ProductManage.vue'
import SiliconeOrangePeelUvCompatibilityManage from '../views/admin/silicone_orange_peel_uv/CompatibilityManage.vue'
import SiliconeGlowInkProductManage from '../views/admin/silicone_glow_ink/ProductManage.vue'
import SiliconeGlowInkCompatibilityManage from '../views/admin/silicone_glow_ink/CompatibilityManage.vue'
import SiliconeWatercolorInkProductManage from '../views/admin/silicone_watercolor_ink/ProductManage.vue'
import SiliconeWatercolorInkCompatibilityManage from '../views/admin/silicone_watercolor_ink/CompatibilityManage.vue'
import NoticeDictionaryManagement from '../views/admin/material/NoticeDictionaryManagement.vue'
import PaperSurfaceManagement from '../views/admin/reference/PaperSurfaceManagement.vue'
import WritingFunctionManagement from '../views/admin/reference/WritingFunctionManagement.vue'
import InkSurfaceManagement from '../views/admin/reference/InkSurfaceManagement.vue'
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
import ImportSnapshotRestorePage from '../views/admin/import/ImportSnapshotRestorePage.vue'
import ProductImportManagement from '../views/admin/import/ProductImportManagement.vue'
import OperationLogPage from '../views/admin/OperationLogPage.vue'
import HotStampingMaterialGuide from '../views/guide/HotStampingMaterialGuide.vue'
import AccessDenied from '../views/AccessDenied.vue'
import SmartVersion from '../views/SmartVersion.vue'
import AgentProcess from '../views/agent/AgentProcess.vue'
import AgentChat from '../views/agent/AgentChat.vue'
import ProductFamilyManagement from '../views/admin/reference/ProductFamilyManagement.vue'
import CoatingSurfaceManagement from '../views/admin/reference/CoatingSurfaceManagement.vue'
import YaguangUvOilProductManage from '../views/admin/yaguang_uv_oil/ProductManage.vue'
import YaguangUvOilCompatibilityManage from '../views/admin/yaguang_uv_oil/CompatibilityManage.vue'
import { canAccessAdminDashboard } from '../utils/rbacNavigation'

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
      meta: { requiresAuth: true, requiredPermissions: ['material:laminating:view'] }
    },
    {
      path: '/admin/material/notice-dictionary',
      name: 'NoticeDictionaryManagement',
      component: NoticeDictionaryManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:notice-dictionary:view'] }
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/access-denied',
      name: 'AccessDenied',
      component: AccessDenied,
      meta: { requiresAuth: true }
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
      meta: {
        requiresAuth: true,
        // 與匹配頁能力一致：燙金/其他工藝或標籤匹配任一即可，無需 tech:management:view
        requiredPermissions: [
          'process:hotstamping:view',
          'process:other:view',
          'matching:hotstamping:view',
          'matching:printing:view',
          'matching:lamination:view',
          'matching:silkscreen:view',
          'matching:tag:select',
          'matching:tag:remove'
        ]
      }
    },


    {
      path: '/match-preference-config',
      name: 'MatchPreferenceConfig',
      component: MatchPreferenceConfig,
      meta: { requiresAuth: true, requiredPermissions: ['match:preference:view'] }
    },
    {
      path: '/tag-matching',
      name: 'TagMatching',
      component: TagMatching,
      meta: { requiresAuth: true, requiredPermissions: ['matching:tag:select'] }
    },
    {
      path: '/matching-result',
      name: 'MatchingResult',
      component: MatchingResult,
      meta: { requiresAuth: true, requiredPermissions: ['matching:tag:select'] }
    },
    {
      path: '/admin',
      name: 'AdminDashboard',
      component: AdminDashboard,
      meta: { requiresAuth: true, adminDashboard: true }
    },
    {
      path: '/admin/material-rule-management',
      name: 'MaterialRuleManagement',
      component: MaterialRuleManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:rule:view'] }
    },
    {
      path: '/admin/material/hot-stamping-material',
      name: 'HotStampingMaterialManagement',
      component: HotStampingMaterialManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:hot-stamping-material:view'] }
    },
    {
      path: '/admin/material/product-type',
      name: 'ProductTypeManagement',
      component: ProductTypeManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:product-type:view'] }
    },
    {
      path: '/admin/material/hot-stamping-type',
      name: 'HotStampingTypeManagement',
      component: HotStampingTypeManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:hot-stamping-type:view'] }
    },
    {
      path: '/admin/material/pre-processing-interface',
      name: 'PreProcessingInterfaceManagement',
      component: PreProcessingInterfaceManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:pre-processing-interface:view'] }
    },
    {
      path: '/admin/material/applicable-interface',
      name: 'ApplicableInterfaceManagement',
      component: ApplicableInterfaceManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:applicable-interface:view'] }
    },
    {
      path: '/admin/material/series-priority-rule',
      name: 'SeriesPriorityRuleManagement',
      component: SeriesPriorityRuleManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:series-priority-rule:view'] }
    },
    {
      path: '/admin/material/smart-compatibility',
      name: 'SmartCompatibilityManagement',
      component: SmartCompatibilityManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:smart-compatibility:view'] }
    },
    {
      path: '/admin/material/matrix-export-import',
      name: 'MatrixExportImportManagement',
      component: MatrixExportImportManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:matrix-export-import:view'] }
    },
    {
      path: '/admin/material/hot-stamping-pattern',
      name: 'HotStampingPatternManagement',
      component: HotStampingPatternManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:hot-stamping-pattern:view'] }
    },
    {
      path: '/admin/material/hot-stamping-pattern-area',
      name: 'HotStampingPatternAreaManagement',
      component: HotStampingPatternAreaManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:hot-stamping-pattern-area:view'] }
    },
    {
      path: '/admin/material/post-processing',
      name: 'PostProcessingManagement',
      component: PostProcessingManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:post-processing:view'] }
    },
    {
      path: '/admin/material/post-processing/printing',
      name: 'PostProcessingPrintManagement',
      component: PostProcessingPrintManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:post-processing-print:view'] }
    },
    {
      path: '/admin/material/post-processing/uv',
      name: 'PostProcessingPrintUvManagement',
      component: PostProcessingPrintUvManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:post-processing-uv:view'] }
    },
    {
      path: '/admin/material/post-processing/leduvglitter',
      name: 'PostProcessingLeduvglitterManagement',
      component: PostProcessingLeduvglitterManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:post-processing-leduvglitter:view'] }
    },
    {
      path: '/admin/material/post-processing/embossing/common-interface',
      name: 'CommonInterfaceMappingManagement',
      component: CommonInterfaceMappingManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:embossing-common-interface:view'] }
    },
    {
      path: '/admin/material/post-processing/embossing/wear-resistant-gold-paper',
      name: 'WearResistantGoldPaperMappingManagement',
      component: WearResistantGoldPaperMappingManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:embossing-wear-resistant-gold-paper:view'] }
    },
    {
      path: '/admin/user-management',
      name: 'UserManagement',
      component: UserManagement,
      meta: { requiresAuth: true, requiredPermissions: ['user:view'] }
    },
    {
      path: '/admin/role-management',
      name: 'RoleManagement',
      component: RoleManagement,
      meta: { requiresAuth: true, requiredPermissions: ['role:view'] }
    },
    {
      path: '/admin/permission-management',
      name: 'PermissionManagement',
      component: PermissionManagement,
      // 與後端 /api/permissions 一致：僅要求管理員角色（避免庫中未掛 permission:manage:view 時無法進入配置頁）
      meta: { requiresAuth: true, requiredRoles: ['ADMIN', 'OPERATOR', 'SUPER_ADMIN'] }
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
      meta: { requiresAuth: true, requiredPermissions: ['resource:group:process:view'] }
    },
    {
      path: '/mounting-resource-group-selection',
      name: 'MountingResourceGroupSelection',
      component: MountingResourceGroupSelection,
      meta: { requiresAuth: true, requiredPermissions: ['resource:group:process:view'] }
    },
    {
      path: '/silkscreen-resource-group-selection',
      name: 'SilkScreenResourceGroupSelection',
      component: SilkScreenResourceGroupSelection,
      meta: { requiresAuth: true, requiredPermissions: ['resource:group:process:view'] }
    },
    {
      path: '/admin/work-center-resource-group-management',
      name: 'WorkCenterResourceGroupManagement',
      component: WorkCenterResourceGroupManagement,
      meta: { requiresAuth: true, requiredPermissions: ['workcenter:resource-group:manage:view'] }
    },
    {
      path: '/admin/resource-group-detail/:workCenterId/:resourceGroupId',
      name: 'ResourceGroupDetail',
      component: ResourceGroupDetail,
      meta: { requiresAuth: true, requiredPermissions: ['workcenter:resource-group:manage:view'] }
    },
    {
      path: '/sys/announcement',
      name: 'MessageCenter',
      component: MessageCenter,
      meta: { requiresAuth: true, requiredPermissions: ['announcement:view'] }
    },
    {
      path: '/guide/hot-stamping-material',
      name: 'HotStampingMaterialGuide',
      component: HotStampingMaterialGuide,
      meta: {
        requiresAuth: true,
        // 滿足任一即可：僅看指引 / 僅維護燙金物料主數據 / 兩者兼有
        requiredPermissions: ['system:guide:view', 'material:hot-stamping-material:view']
      }
    },
    {
      path: '/smart-version',
      name: 'SmartVersion',
      component: SmartVersion,
      meta: { requiresAuth: true }
    },
    {
      path: '/agent/process',
      name: 'AgentProcess',
      component: AgentProcess,
      meta: { requiresAuth: true }
    },
    {
      path: '/agent/chat',
      name: 'AgentChat',
      component: AgentChat,
      meta: { requiresAuth: true }
    },
    {
      path: '/resource-group-selector',
      name: 'ResourceGroupSelector',
      component: ResourceGroupSelector,
      meta: { requiresAuth: true, requiredPermissions: ['resource:group:selector:view'] }
    },
    {
      path: '/admin/import-snapshot-restore',
      name: 'ImportSnapshotRestore',
      component: ImportSnapshotRestorePage,
      meta: { requiresAuth: true, requiredPermissions: ['system:backup:create'] }
    },
    {
      path: '/admin/product-import-management',
      name: 'ProductImportManagement',
      component: ProductImportManagement,
      meta: { requiresAuth: true, requiredPermissions: ['product:import:manage:view'] }
    },
    {
      path: '/admin/operation-log',
      name: 'OperationLog',
      component: OperationLogPage,
      meta: { requiresAuth: true, requiredPermissions: ['system:log:view'] }
    },
    {
      path: '/admin/material/water-varnish-matte/products',
      name: 'WaterVarnishMatteProduct',
      component: WaterVarnishMatteProductManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:water-varnish-matte:view'] }
    },
    {
      path: '/admin/material/water-varnish-matte/compatibilities',
      name: 'WaterVarnishMatteCompatibility',
      component: WaterVarnishMatteCompatibilityManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:water-varnish-matte:view'] }
    },
    {
      path: '/admin/material/uv-oil-matte/products',
      name: 'UvOilMatteProduct',
      component: UvOilMatteProductManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:uv-oil-matte:view'] }
    },
    {
      path: '/admin/material/uv-oil-matte/compatibilities',
      name: 'UvOilMatteCompatibility',
      component: UvOilMatteCompatibilityManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:uv-oil-matte:view'] }
    },
    {
      path: '/admin/material/silicone-white-uv/products',
      name: 'SiliconeWhiteUvProduct',
      component: SiliconeWhiteUvProductManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-white-uv:view'] }
    },
    {
      path: '/admin/material/silicone-white-uv/compatibilities',
      name: 'SiliconeWhiteUvCompatibility',
      component: SiliconeWhiteUvCompatibilityManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-white-uv:view'] }
    },
    {
      path: '/admin/material/silicone-sandblast-uv/products',
      name: 'SiliconeSandblastUvProduct',
      component: SiliconeSandblastUvProductManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-sandblast-uv:view'] }
    },
    {
      path: '/admin/material/silicone-sandblast-uv/compatibilities',
      name: 'SiliconeSandblastUvCompatibility',
      component: SiliconeSandblastUvCompatibilityManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-sandblast-uv:view'] }
    },
    {
      path: '/admin/material/silicone-wrinkle-uv/products',
      name: 'SiliconeWrinkleUvProduct',
      component: SiliconeWrinkleUvProductManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-wrinkle-uv:view'] }
    },
    {
      path: '/admin/material/silicone-wrinkle-uv/compatibilities',
      name: 'SiliconeWrinkleUvCompatibility',
      component: SiliconeWrinkleUvCompatibilityManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-wrinkle-uv:view'] }
    },
    {
      path: '/admin/material/silicone-coarse-uv/products',
      name: 'SiliconeCoarseUvProduct',
      component: SiliconeCoarseUvProductManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-coarse-uv:view'] }
    },
    {
      path: '/admin/material/silicone-coarse-uv/compatibilities',
      name: 'SiliconeCoarseUvCompatibility',
      component: SiliconeCoarseUvCompatibilityManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-coarse-uv:view'] }
    },
    {
      path: '/admin/material/silicone-orange-peel-uv/products',
      name: 'SiliconeOrangePeelUvProduct',
      component: SiliconeOrangePeelUvProductManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-orange-peel-uv:view'] }
    },
    {
      path: '/admin/material/silicone-orange-peel-uv/compatibilities',
      name: 'SiliconeOrangePeelUvCompatibility',
      component: SiliconeOrangePeelUvCompatibilityManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-orange-peel-uv:view'] }
    },
    {
      path: '/admin/material/silicone-glow-ink/products',
      name: 'SiliconeGlowInkProduct',
      component: SiliconeGlowInkProductManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-glow-ink:view'] }
    },
    {
      path: '/admin/material/silicone-glow-ink/compatibilities',
      name: 'SiliconeGlowInkCompatibility',
      component: SiliconeGlowInkCompatibilityManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-glow-ink:view'] }
    },
    {
      path: '/admin/material/silicone-watercolor-ink/products',
      name: 'SiliconeWatercolorInkProduct',
      component: SiliconeWatercolorInkProductManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-watercolor-ink:view'] }
    },
    {
      path: '/admin/material/silicone-watercolor-ink/compatibilities',
      name: 'SiliconeWatercolorInkCompatibility',
      component: SiliconeWatercolorInkCompatibilityManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:silicone-watercolor-ink:view'] }
    },
    {
      path: '/admin/reference/paper-surface',
      name: 'PaperSurfaceManagement',
      component: PaperSurfaceManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:reference-paper-surface:view'] }
    },
    {
      path: '/admin/reference/writing-function',
      name: 'WritingFunctionManagement',
      component: WritingFunctionManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:reference-writing-function:view'] }
    },
    {
      path: '/admin/reference/ink-surface',
      name: 'InkSurfaceManagement',
      component: InkSurfaceManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:reference-ink-surface:view'] }
    },
    {
      path: '/admin/reference/product-family',
      name: 'ProductFamilyManagement',
      component: ProductFamilyManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:reference-product-family:view'] }
    },
    {
      path: '/admin/reference/coating-surface',
      name: 'CoatingSurfaceManagement',
      component: CoatingSurfaceManagement,
      meta: { requiresAuth: true, requiredPermissions: ['material:reference-coating-surface:view'] }
    },
    {
      path: '/admin/material/yaguang-uv-oil/products',
      name: 'YaguangUvOilProductManage',
      component: YaguangUvOilProductManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:yaguang-uv-oil:view'] }
    },
    {
      path: '/admin/material/yaguang-uv-oil/compatibilities',
      name: 'YaguangUvOilCompatibilityManage',
      component: YaguangUvOilCompatibilityManage,
      meta: { requiresAuth: true, requiredPermissions: ['material:yaguang-uv-oil:view'] }
    },
  ]
})

import { useAuthStore } from '../stores/auth';
import { usePermissionStore } from '../stores/permission';
import { resolveFallbackRoute } from '../utils/rbacNavigation';

router.beforeEach(async (to, _from, next) => {
  // 初始化 auth store
  const authStore = useAuthStore();
  const permissionStore = usePermissionStore();

  // 確保先初始化用戶狀態
  authStore.initUserState();

  // 從 store 中獲取登錄狀態
  const isLoggedIn = authStore.isLoggedIn;

  console.log(`路由守衛: 路由到 ${to.path}, 登錄狀態: ${isLoggedIn}`);

  if (to.meta.requiresAuth && !isLoggedIn) {
    console.log('需要登錄權限，重定向到登錄頁面');
    next('/login');
  } else if (to.path === '/login' && isLoggedIn) {
    try {
      await permissionStore.initPermissions();
    } catch (error) {
      console.warn('路由守卫初始化权限失败', error);
    }
    next(resolveFallbackRoute(permissionStore, ['/login'], router.getRoutes()));
  } else {
    if (isLoggedIn && to.meta.requiresAuth) {
      try {
        if (to.path === '/access-denied') {
          await permissionStore.initPermissions();
          const dest = resolveFallbackRoute(permissionStore, ['/access-denied'], router.getRoutes());
          if (dest !== '/access-denied') {
            next(dest);
            return;
          }
        } else if (
          !permissionStore.currentUserRbacReady ||
          (permissionStore.currentUserRoles.length === 0 &&
            permissionStore.currentUserPermissions.length === 0) ||
          (permissionStore.currentUserRoles.length > 0 &&
            permissionStore.currentUserPermissions.length === 0)
        ) {
          await permissionStore.initPermissions();
        }
      } catch (error) {
        console.warn('路由守卫初始化权限失败，按无权限处理', error);
      }

      const requiredRoles = (to.meta as any).requiredRoles as string[] | undefined;
      const requiredPermissions = (to.meta as any).requiredPermissions as string[] | undefined;
      const adminDashboard = Boolean((to.meta as any).adminDashboard);

      if (adminDashboard) {
        if (!canAccessAdminDashboard(permissionStore, router.getRoutes())) {
          console.warn('後台首頁無權限，重定向:', to.path);
          next(resolveFallbackRoute(permissionStore, [to.path], router.getRoutes()));
          return;
        }
      } else if (requiredRoles && requiredRoles.length > 0) {
        const hasAnyRole = requiredRoles.some(roleKey => permissionStore.hasRole(roleKey));
        if (!hasAnyRole) {
          console.warn('角色校验失败，重定向到首页:', to.path, requiredRoles);
          next(resolveFallbackRoute(permissionStore, [to.path], router.getRoutes()));
          return;
        }
      }

      if (requiredPermissions && requiredPermissions.length > 0) {
        const hasAnyPermission =
          permissionStore.isAdmin ||
          requiredPermissions.some((permissionKey) => permissionStore.hasPermission(permissionKey));
        if (!hasAnyPermission) {
          console.warn('权限校验失败，重定向到首页:', to.path, requiredPermissions);
          next(resolveFallbackRoute(permissionStore, [to.path], router.getRoutes()));
          return;
        }
      }
    }
    next();
  }
});

export default router