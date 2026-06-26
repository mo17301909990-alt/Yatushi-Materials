#!/usr/bin/env tsx
/**
 * YTS Vue SPA -- UI 浏览器烟雾测试
 *
 * 启动 Vite dev server（如未运行），遍历关键页面验证：
 *   - HTTP 200
 *   - index.html 包含 Vue mount point（#app）
 *   - 无服务器错误文本
 *
 * 输出 JSON 报告到 stdout，非零退出码表示有失败项。
 *
 * Usage:
 *   cd yts_project_vueai && npx tsx --no-warnings ../scripts/ui-browse-smoke.ts
 */

import { spawn, type ChildProcess } from "node:child_process";
import { accessSync, constants } from "node:fs";
import { resolve, dirname } from "node:path";
import { fileURLToPath } from "node:url";

// ---- config -----------------------------------------------------------

const PORT = 5173;
const BASE = `http://localhost:${PORT}`;
const START_TIMEOUT_MS = 30_000;
const PAGE_TIMEOUT_MS = 10_000;

// 页面检查清单 —— 使用 router/index.ts 中的实际路径
// 用户期望的名称在注释中标注
const PAGES: { path: string; label: string }[] = [
  { path: "/", label: "root" },
  { path: "/login", label: "login" },
  { path: "/smart-version", label: "smart-version" },
  { path: "/agent/chat", label: "agent-chat" },           // 用户期望: /agent/agent-chat
  { path: "/admin/material/hot-stamping-material", label: "hot-stamping-material-management" },   // 用户期望: /admin/material/hot-stamping-material-management
  { path: "/admin/material/smart-compatibility", label: "smart-compatibility-management" },        // 用户期望: /admin/material/smart-compatibility-management
];

// ---- page check -------------------------------------------------------

interface PageResult {
  label: string;
  path: string;
  status: number;
  ok: boolean;
  error?: string;
}

async function checkPage(url: string, label: string, path: string): Promise<PageResult> {
  const result: PageResult = { label, path, status: 0, ok: false };
  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(), PAGE_TIMEOUT_MS);

  try {
    const resp = await fetch(url, {
      signal: controller.signal,
      redirect: "follow",
    });
    result.status = resp.status;

    if (resp.status !== 200) {
      result.error = `HTTP ${resp.status}`;
      return result;
    }

    const text = await resp.text();

    // 验证 Vue mount point
    if (!text.includes('<div id="app">') && !text.includes(`id="app"`)) {
      result.error = '缺少 Vue mount point (#app)';
      return result;
    }

    // 检查服务端错误 / 构建错误（避免误伤 index.html 中的 onError 等合法文本）
    const errorPatterns = [
      "Cannot find module",
      "Internal Server Error",
      "SyntaxError",
      "ReferenceError",
      "Error: ENOENT",
      "Error while loading",
      "Uncaught (in promise)",
    ];
    for (const pat of errorPatterns) {
      if (text.includes(pat)) {
        result.error = `响应中包含错误文本: "${pat}"`;
        return result;
      }
    }

    // 验证是有效的 HTML（不是 JSON 或纯文本）
    if (!text.includes("<html") && !text.includes("<!doctype")) {
      result.error = "响应不是有效 HTML";
      return result;
    }

    result.ok = true;
  } catch (err: unknown) {
    if (err instanceof Error && err.name === "AbortError") {
      result.error = "请求超时";
    } else {
      result.error = err instanceof Error ? err.message : String(err);
    }
  } finally {
    clearTimeout(timer);
  }

  return result;
}

// ---- server management ------------------------------------------------

class ServerManager {
  private proc: ChildProcess | null = null;
  private startedByUs = false;

  /** 检查端口是否已有服务 */
  async isRunning(): Promise<boolean> {
    try {
      const resp = await fetch(`http://localhost:${PORT}`, { signal: AbortSignal.timeout(2000) });
      return resp.ok || resp.status === 304 || resp.status === 404;
    } catch {
      return false;
    }
  }

  /** 启动 Vite dev server，等待 ready */
  async start(cwd: string): Promise<void> {
    if (await this.isRunning()) {
      console.error(`[ui-smoke] 端口 ${PORT} 已有服务，跳过启动`);
      return;
    }

    // 确认 yarn.lock / package.json 存在
    try {
      accessSync(resolve(cwd, "package.json"), constants.R_OK);
    } catch {
      throw new Error(`未找到 ${cwd}/package.json，请确认在前端目录执行`);
    }

    this.startedByUs = true;
    // Windows 上 npx 是 npx.cmd，需 shell: true 解析 PATHEXT
    // 传单字符串避免 "args + shell: true" 弃用警告
    this.proc = spawn(`npx vite --port ${PORT}`, {
      cwd,
      stdio: "ignore",
      shell: true,
      windowsHide: true,
    });

    // 用 HTTP 轮询代替解析 stdout（避免 pipe 在 Windows 上触发 libuv 断言）
    console.error(`[ui-smoke] 等待 Vite dev server 启动（端口 ${PORT}）...`);
    const deadline = Date.now() + START_TIMEOUT_MS;
    // eslint-disable-next-line no-constant-condition
    while (true) {
      if (await this.isRunning()) {
        console.error(`[ui-smoke] Vite dev server 已就绪（端口 ${PORT}）`);
        return;
      }
      if (Date.now() > deadline) {
        this.kill();
        throw new Error(`Vite dev server 启动超时（${START_TIMEOUT_MS}ms）`);
      }
      await sleep(500);
    }
  }

  kill(): void {
    if (!this.proc || this.proc.killed) return;

    if (process.platform === "win32") {
      // Windows: 用 taskkill 杀进程树
      try {
        spawn("taskkill", ["/pid", String(this.proc.pid), "/f", "/t"], {
          stdio: "ignore",
          shell: true,
        });
      } catch {
        // ignore
      }
    }
    this.proc.kill("SIGTERM");
    this.proc = null;
  }

  /** 只在是我们启动的情况下才停服务 */
  async stop(): Promise<void> {
    if (this.startedByUs) {
      this.kill();
    }
  }
}

// ---- helpers ----------------------------------------------------------

function sleep(ms: number): Promise<void> {
  return new Promise((r) => setTimeout(r, ms));
}

// ---- main -------------------------------------------------------------

interface Report {
  pages: PageResult[];
  summary: { total: number; passed: number; failed: number };
}

async function main(): Promise<number> {
  // 确定前端目录（脚本在 /scripts/，前端在 /yts_project_vueai/）
  const scriptDir = dirname(fileURLToPath(import.meta.url));
  const frontendDir = resolve(scriptDir, "..", "yts_project_vueai");
  console.error(`[ui-smoke] 前端目录: ${frontendDir}`);

  const mgr = new ServerManager();

  try {
    // 1) 启动 / 等待服务
    await mgr.start(frontendDir);

    // 2) 遍历页面
    const results: PageResult[] = [];
    for (const { path, label } of PAGES) {
      const url = `${BASE}${path}`;
      console.error(`[ui-smoke] 检查 ${label} (${url}) ...`);
      const result = await checkPage(url, label, path);
      results.push(result);
      const icon = result.ok ? "PASS" : "FAIL";
      console.error(`  -> [${icon}] ${result.error ?? "ok"}`);
    }

    // 3) 汇总
    const passed = results.filter((r) => r.ok).length;
    const failed = results.filter((r) => !r.ok).length;
    const report: Report = {
      pages: results,
      summary: { total: results.length, passed, failed },
    };

    // JSON report to stdout
    console.log(JSON.stringify(report, null, 2));

    // 退出码
    return failed > 0 ? 1 : 0;
  } finally {
    await mgr.stop();
  }
}

main().then((code) => {
  // 设 exitCode 让进程自然退出，避免 Windows libuv 异步句柄清理竞态
  process.exitCode = code;
});
