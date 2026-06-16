#!/usr/bin/env npx tsx
/**
 * flywheel-keeper.ts — 印刷厂项目适配版飞轮健康检查
 *
 * 零外部依赖，只使用 Node.js 内置模块 fs/path。
 *
 * CLI:
 *   npx tsx scripts/flywheel-keeper.ts            # 全量健康检查（含日志新鲜度）
 *   npx tsx scripts/flywheel-keeper.ts --status   # 仅显示状态，不检查新鲜度
 */

import { readFileSync, existsSync } from 'fs';
import { resolve } from 'path';
import { fileURLToPath } from 'url';

// ─── Paths ───────────────────────────────────────────────────────────────────
const __filename = fileURLToPath(import.meta.url);
const __dirname = resolve(__filename, '..');
const PROJECT_ROOT = resolve(__dirname, '..');
const LOOPS_DIR = resolve(PROJECT_ROOT, '.claude', 'loops');
const REGISTRY_PATH = resolve(LOOPS_DIR, 'loop-registry.json');
const DEV_QUALITY_LOG = resolve(LOOPS_DIR, 'dev-quality.log');

// ─── Types ───────────────────────────────────────────────────────────────────
interface LoopConfig {
  id: string;
  name: string;
  trigger: string;
  log: string;
  enabled: boolean;
}

interface LoopResult {
  id: string;
  name: string;
  trigger: string;
  recentTime: string | null;
  status: 'passed' | 'failed' | 'inactive' | 'unknown';
}

// ─── Log parsers ──────────────────────────────────────────────────────────────

/** 解析 dev-quality.log 最后一条检查结果 */
function parseDevQuality(lines: string[]): { time: string | null; status: 'passed' | 'failed' | 'unknown' } {
  for (let i = lines.length - 1; i >= 0; i--) {
    const line = lines[i].trim();
    if (!line || line.startsWith('{')) continue; // 跳过 JSON 错误条目
    const pass = line.match(/^【检查通过】(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})/);
    if (pass) return { time: pass[1], status: 'passed' };
    const fail = line.match(/^【检查失败】(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})/);
    if (fail) return { time: fail[1], status: 'failed' };
  }
  return { time: null, status: 'unknown' };
}

/** 解析通用回路日志的最后一次执行时间戳 */
function parseGeneralLog(lines: string[]): string | null {
  for (let i = lines.length - 1; i >= 0; i--) {
    const m = lines[i].trim().match(/^======\s+\S+\s+(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2})\s+======$/);
    if (m) return m[1];
  }
  return null;
}

/** 检查日志是否在预期时间窗口内 */
function checkFreshness(trigger: string, timeStr: string | null): boolean {
  if (!timeStr) return false;
  if (trigger === 'manual') return true; // manual 回路不做新鲜度判定

  const now = Date.now();
  const last = new Date(timeStr.replace(' ', 'T')).getTime();
  const hoursAgo = (now - last) / 3_600_000;

  // 按触发周期判定
  if (trigger.includes('6h'))   return hoursAgo <= 7;    // 6h + 1h grace
  if (trigger.includes('daily')) return hoursAgo <= 26;  // 24h + 2h grace
  if (trigger.includes('weekly')) return hoursAgo <= 170; // 168h + 2h grace
  return hoursAgo <= 26; // 兜底
}

// ─── Report ────────────────────────────────────────────────────────────────────

function formatTime(d: Date): string {
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

function iconFor(r: LoopResult): string {
  if (r.status === 'inactive') return '⏸';
  if (r.status === 'passed')   return '✅';
  if (r.status === 'failed')   return '❌';
  return '❓';
}

function statusLabel(r: LoopResult, fresh: boolean): string {
  if (r.status === 'inactive') return '未激活';
  if (r.status === 'passed')   return fresh ? '通过' : '通过 ⚠ 过时';
  if (r.status === 'failed')   return '失败';
  return '未知';
}

// ─── Main ──────────────────────────────────────────────────────────────────────

function main(): void {
  const statusOnly = process.argv.slice(2).includes('--status');

  if (!existsSync(REGISTRY_PATH)) {
    console.error(`错误: 未找到回路注册文件 ${REGISTRY_PATH}`);
    process.exit(1);
  }

  let registry: { loops: LoopConfig[] };
  try {
    registry = JSON.parse(readFileSync(REGISTRY_PATH, 'utf-8'));
  } catch {
    console.error('错误: loop-registry.json 解析失败');
    process.exit(1);
  }

  // dev-quality 编译状态
  const devLines = existsSync(DEV_QUALITY_LOG)
    ? readFileSync(DEV_QUALITY_LOG, 'utf-8').split('\n')
    : [];
  const devStatus = parseDevQuality(devLines);

  // 逐回路检查
  const results: LoopResult[] = [];
  for (const loop of registry.loops) {
    if (!loop.enabled) {
      results.push({ id: loop.id, name: loop.name, trigger: loop.trigger, recentTime: null, status: 'inactive' });
      continue;
    }

    const logPath = resolve(PROJECT_ROOT, loop.log);
    const lines = existsSync(logPath) ? readFileSync(logPath, 'utf-8').split('\n') : [];

    let status: LoopResult['status'];
    let time: string | null;

    if (loop.id === 'dev-quality') {
      const p = parseDevQuality(lines);
      time = p.time;
      status = p.time ? p.status : 'unknown';
    } else if (loop.id === 'ai-match-feedback') {
      // manual 回路、仅初始运行一次 → 视为未激活
      time = null;
      status = 'inactive';
    } else {
      time = parseGeneralLog(lines);
      status = time ? 'passed' : 'unknown';
    }

    results.push({ id: loop.id, name: loop.name, trigger: loop.trigger, recentTime: time, status });
  }

  // ── 输出 ──
  console.log('=== 飞轮健康报告 ===');
  console.log(`时间: ${formatTime(new Date())}`);
  console.log();
  console.log('回路状态:');

  for (const r of results) {
    const icon = iconFor(r);
    if (r.status === 'inactive') {
      console.log(`  ${icon} ${r.id} (${r.trigger}) — 未激活`);
      continue;
    }
    const fresh = statusOnly || checkFreshness(r.trigger, r.recentTime);
    const label = statusLabel(r, fresh);
    const timePart = r.recentTime ? `最近: ${r.recentTime}` : '无记录';
    console.log(`  ${icon} ${r.id} (${r.trigger}) — ${timePart} — ${label}`);
  }

  console.log();

  const devLabel =
    devStatus.status === 'passed' ? '✅ 最近一次通过' :
    devStatus.status === 'failed' ? '❌ 最近一次失败' :
    '❓ 未知';
  console.log(`编译状态: ${devLabel}`);
}

main();
