/**
 * scripts/smoke-routes.ts — 路由静态分析工具
 *
 * 功能:
 *  1. 解析 src/router/index.ts 中的所有路由
 *  2. 检查每条路由 component 指向的 .vue 文件是否存在
 *  3. 解析 ProcessNavigation.vue 中的 router-link to= 路径
 *  4. 检查导航链接是否有对应的路由注册
 *  5. 输出 JSON 报告
 *
 * 用法:
 *  node --import tsx scripts/smoke-routes.ts
 */

import * as fs from 'node:fs';
import * as path from 'node:path';

const PROJECT_ROOT = path.resolve(import.meta.dirname, '..');
const ROUTER_FILE = path.join(PROJECT_ROOT, 'src/router/index.ts');
const NAV_FILE = path.join(PROJECT_ROOT, 'src/components/layout/ProcessNavigation.vue');

interface RouteEntry {
  path: string;
  name?: string;
  componentPath?: string;
  line: number;
}

interface MissingEntry {
  route: string;
  file: string;
  line: number;
}

interface Report {
  routeCount: number;
  missingComponents: MissingEntry[];
  navLinksMissingRoute: MissingEntry[];
}

// ── 1. 解析路由文件 ──────────────────────────────────────────

function parseRoutes(content: string): RouteEntry[] {
  const routes: RouteEntry[] = [];
  // 匹配形如:
  //   {
  //     path: '/foo',
  //     name: 'Foo',
  //     component: FooComponent,    // 或带 meta 等
  //   }
  const lines = content.split('\n');

  let current: Partial<RouteEntry> | null = null;

  for (let i = 0; i < lines.length; i++) {
    const raw = lines[i];
    const lineNum = i + 1;

    // 匹配 path 声明
    const pathMatch = raw.match(/^\s*path:\s*'([^']+)'/);
    if (pathMatch) {
      current = { path: pathMatch[1], line: lineNum };
      continue;
    }

    // 匹配 component 声明 — 变量引用形式: component: FooBar,
    const compMatch = raw.match(/^\s*component:\s*(\w+),?\s*$/);
    if (compMatch && current) {
      current.componentPath = compMatch[1];
      continue;
    }

    // 闭合大括号 — 提交当前路由
    if (raw.trim() === '},' || raw.trim() === '}') {
      if (current && current.path) {
        routes.push(current as RouteEntry);
      }
      current = null;
    }
  }

  // 用 import 映射把组件变量名解析为文件路径
  const compVarMap = buildComponentVarMap(content);
  for (const r of routes) {
    if (r.componentPath && compVarMap[r.componentPath]) {
      r.componentPath = compVarMap[r.componentPath];
    }
  }

  return routes;
}

function buildComponentVarMap(content: string): Record<string, string> {
  const map: Record<string, string> = {};
  // 匹配形如:
  //   import FooBar from '../views/FooBar.vue'
  //   import FooBar from '../views/FooBar.vue'
  const importRegex = /import\s+(\w+)\s+from\s+['"]((?:\.\.\/)?(?:[^'"]+\.vue))['"]/g;
  let m: RegExpExecArray | null;
  while ((m = importRegex.exec(content)) !== null) {
    const varName = m[1];
    let importPath = m[2];
    // 转换为绝对路径
    if (importPath.startsWith('../')) {
      // router/index.ts 在 src/router/ 下, 所以 ../ 对应 src/
      importPath = path.resolve(PROJECT_ROOT, 'src', importPath.replace(/^\.\.\//, ''));
    } else {
      importPath = path.resolve(PROJECT_ROOT, 'src', importPath);
    }
    map[varName] = importPath;
  }
  return map;
}

// ── 2. 解析导航文件提取 router-link to= ─────────────────────

function parseNavLinks(content: string): { path: string; file: string; line: number }[] {
  const links: { path: string; file: string; line: number }[] = [];
  const lines = content.split('\n');

  // 匹配 <router-link ... to="/some-path" ...>
  // 或 <router-link ... :to="'...'" ...> (带上括号内字符串)
  for (let i = 0; i < lines.length; i++) {
    const raw = lines[i];
    const lineNum = i + 1;

    // to="/foo/bar"
    const literalMatch = raw.match(/to=['"]([^'"]+)['"]/);
    if (literalMatch) {
      links.push({
        path: literalMatch[1],
        file: NAV_FILE,
        line: lineNum,
      });
    }
  }

  return links;
}

// ── 3. 辅助: 路由路径转正则/规范化匹配 ──────────────────────

/**
 * 检查导航链接路径是否有对应的路由注册。
 * 支持正常路径匹配和带参数路由的简单前缀匹配。
 */
function hasMatchingRoute(navPath: string, routes: RouteEntry[]): boolean {
  return routes.some((r) => {
    // 精确匹配
    if (r.path === navPath) return true;
    // 带参数的路由: /admin/resource-group-detail/:workCenterId/:resourceGroupId
    // 导航 to 是 /admin/resource-group-detail/xxx/yyy
    const rSegments = r.path.split('/');
    const navSegments = navPath.split('/');
    if (rSegments.length !== navSegments.length) return false;
    return rSegments.every((seg, idx) => seg.startsWith(':') || seg === navSegments[idx]);
  });
}

// ── 4. Main ──────────────────────────────────────────────────

function main(): void {
  // 读取文件
  const routerContent = fs.readFileSync(ROUTER_FILE, 'utf-8');
  const navContent = fs.readFileSync(NAV_FILE, 'utf-8');

  // 解析路由
  const routes = parseRoutes(routerContent);
  const routeCount = routes.length;

  // 检查 component 文件是否存在
  const missingComponents: MissingEntry[] = [];
  for (const r of routes) {
    if (r.componentPath) {
      const resolvedPath = r.componentPath;
      if (!fs.existsSync(resolvedPath)) {
        missingComponents.push({
          route: `${r.path} (${r.name || 'unnamed'})`,
          file: resolvedPath,
          line: r.line,
        });
      }
    }
  }

  // 解析导航链接
  const navLinks = parseNavLinks(navContent);

  // 收集路由所有 path（去除动态参数部分用于匹配）
  const routePaths = routes.map((r) => r.path);

  // 检查导航链接是否有对应路由
  const navLinksMissingRoute: MissingEntry[] = [];
  for (const link of navLinks) {
    if (!hasMatchingRoute(link.path, routes)) {
      navLinksMissingRoute.push({
        route: link.path,
        file: link.file,
        line: link.line,
      });
    }
  }

  // 输出报告
  const report: Report = {
    routeCount,
    missingComponents,
    navLinksMissingRoute,
  };

  console.log(JSON.stringify(report, null, 2));
}

main();
