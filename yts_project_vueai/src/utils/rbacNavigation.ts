import type { RouteRecordNormalized } from 'vue-router';
import { usePermissionStore } from '../stores/permission';

type PS = ReturnType<typeof usePermissionStore>;

const STATIC_ORDER: { path: string; keys: string[] }[] = [
  {
    path: '/process-config',
    keys: [
      'process:hotstamping:view',
      'process:other:view',
      'matching:hotstamping:view',
      'matching:printing:view',
      'matching:lamination:view',
      'matching:silkscreen:view',
      'tech:management:view',
      'matching:tag:select',
      'matching:tag:remove'
    ]
  },
  { path: '/sys/announcement', keys: ['announcement:view'] },
  { path: '/match-preference-config', keys: ['match:preference:view'] },
  { path: '/resource-group-selector', keys: ['resource:group:selector:view'] },
  { path: '/tag-matching', keys: ['matching:tag:select'] }
];

const DENY_SCAN = ['/login', '/register', '/access-denied', '/debug-permissions', '/permission-test', '/permission-debug', '/test-api', '/simple-test'];

/** 後台首頁允許的「高權限角色」（與原 /admin meta 一致） */
const ADMIN_DASHBOARD_PRIVILEGED_ROLES = ['ADMIN', 'OPERATOR', 'SUPER_ADMIN'] as const;

/**
 * 可否進入 /admin 總覽：管理員角色、調試入口 system:management，或具備任一後台子頁 requiredPermissions。
 * 避免僅分配物料等後台能力卻因角色為 USER 被擋在門外。
 */
export function canAccessAdminDashboard(
  permissionStore: PS,
  routes: RouteRecordNormalized[]
): boolean {
  if (permissionStore.isAdmin) return true;
  if (ADMIN_DASHBOARD_PRIVILEGED_ROLES.some((r) => permissionStore.hasRole(r))) return true;
  if (permissionStore.hasPermission('system:management')) return true;

  const keys = new Set<string>();
  for (const r of routes) {
    const p = r.path;
    if (!p || p === '/admin' || !p.startsWith('/admin')) continue;
    const reqPerms = r.meta?.requiredPermissions as string[] | undefined;
    if (reqPerms?.length) reqPerms.forEach((k) => keys.add(k));
  }
  return [...keys].some((k) => permissionStore.hasPermission(k));
}

function isDeniedScanPath(path: string): boolean {
  return DENY_SCAN.some((d) => path === d || path.startsWith(d + '/'));
}

/**
 * 登錄後或權限校驗失敗時，解析當前用戶第一個可進入的路由。
 * 順序：固定首頁候選 → 後端權限行上的 path → 掃描路由表 meta。
 */
export function resolveFallbackRoute(
  permissionStore: PS,
  excludePaths: string[],
  routes: RouteRecordNormalized[]
): string {
  const exclude = new Set(excludePaths.map((p) => p.split('?')[0]));

  for (const item of STATIC_ORDER) {
    if (exclude.has(item.path)) continue;
    if (item.keys.some((k) => permissionStore.hasPermission(k))) {
      return item.path;
    }
  }

  const fromDb = [...permissionStore.currentUserPermissions]
    .filter((p) => p.path && String(p.path).trim().length > 0 && p.permissionKey)
    .sort((a, b) => (a.orderNum ?? 9999) - (b.orderNum ?? 9999));

  for (const p of fromDb) {
    const routePath = String(p.path).split('?')[0].trim();
    if (!routePath || exclude.has(routePath)) continue;
    if (permissionStore.hasPermission(p.permissionKey!)) {
      return routePath;
    }
  }

  for (const r of routes) {
    const path = r.path;
    if (!path || path === '/') continue;
    if (!r.meta?.requiresAuth) continue;
    if (isDeniedScanPath(path)) continue;
    if (exclude.has(path)) continue;

    const reqRoles = r.meta.requiredRoles as string[] | undefined;
    if (reqRoles?.length && !reqRoles.some((rk) => permissionStore.hasRole(rk))) continue;

    const reqPerms = r.meta.requiredPermissions as string[] | undefined;
    if (reqPerms?.length) {
      const ok =
        permissionStore.isAdmin ||
        reqPerms.some((k) => permissionStore.hasPermission(k));
      if (ok) return path;
    }
  }

  return '/access-denied';
}
