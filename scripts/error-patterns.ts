#!/usr/bin/env tsx
/**
 * error-patterns.ts — 印刷厂项目适配版
 *
 * 零外部依赖，仅使用 Node.js 内置模块。
 * 数据文件：项目 .claude/loops/error-patterns.json
 *
 * 用法:
 *   record   npx tsx scripts/error-patterns.ts record "<症状>" <分类> "<修复>" [文件路径]
 *   match    npx tsx scripts/error-patterns.ts match "<错误文本>" [--shared]
 *   stats    npx tsx scripts/error-patterns.ts stats
 *   confirm  npx tsx scripts/error-patterns.ts confirm <id或症状>
 *   promote  npx tsx scripts/error-patterns.ts promote [--apply]
 */

import { readFileSync, writeFileSync, existsSync, readdirSync, mkdirSync } from "node:fs";
import { resolve, dirname, basename } from "node:path";
import { fileURLToPath } from "node:url";

// ─── 路径 ────────────────────────────────────────────────────

const SCRIPT_DIR = resolve(dirname(fileURLToPath(import.meta.url)));
const PROJECT_ROOT = resolve(SCRIPT_DIR, "..");
const DATA_DIR = resolve(PROJECT_ROOT, ".claude", "loops");
const DATA_FILE = resolve(DATA_DIR, "error-patterns.json");
const SHARED_DIR = resolve(PROJECT_ROOT, "..", "shared", "patterns");
const GOTCHAS_DIR = resolve(PROJECT_ROOT, ".claude", "gotchas");

// 确保数据目录存在
if (!existsSync(DATA_DIR)) {
  mkdirSync(DATA_DIR, { recursive: true });
}

// ─── 类型 ────────────────────────────────────────────────────

interface ErrorPattern {
  id: string;
  symptom: string;
  category: string;
  fix: string;
  file: string;
  module: string;
  count: number;
  firstSeen: string;
  lastSeen: string;
  promotedToGotchas?: boolean;
  fixed?: boolean;
  level?: "error" | "info";
}

// ─── 分类体系 ────────────────────────────────────────────────

const CATEGORIES: Record<string, string> = {
  "compile-error": "编译错误（Java/TypeScript 编译失败）",
  "test-failure": "测试失败（单元测试/集成测试）",
  "db-query": "数据库错误（SQL/连接）",
  "null-safety": "空值处理",
  "type-mismatch": "类型不匹配",
  "import-error": "导入/路径错误",
  "env-config": "环境/配置问题",
  "build-oom": "构建内存不足",
  "data-quality": "数据质量问题",
  "permission": "权限问题",
  "other": "其他",
};

const CATEGORY_KEYS = Object.keys(CATEGORIES);

// ─── 工具函数 ────────────────────────────────────────────────

function now(): string {
  return new Date().toISOString().split("T")[0];
}

function generateId(symptom: string, category: string): string {
  // 取症状前 40 个字符 + 分类，确保可读且稳定
  const slug = symptom
    .toLowerCase()
    .replace(/[^a-z0-9一-鿿]+/g, "-")
    .replace(/^-|-$/g, "")
    .slice(0, 40);
  return `${slug}__${category}`;
}

function load(filePath: string = DATA_FILE): ErrorPattern[] {
  if (!existsSync(filePath)) return [];
  try {
    return JSON.parse(readFileSync(filePath, "utf-8"));
  } catch {
    return [];
  }
}

function save(patterns: ErrorPattern[], filePath: string = DATA_FILE): void {
  const dir = dirname(filePath);
  if (!existsSync(dir)) {
    mkdirSync(dir, { recursive: true });
  }
  writeFileSync(filePath, JSON.stringify(patterns, null, 2));
}

/**
 * 将 CJK 文本和英文单词一起分词
 */
function tokenize(text: string): Set<string> {
  const t = text.toLowerCase();
  const tokens = new Set<string>();

  // 英文/数字 token
  for (const w of t.split(/[^a-z0-9]+/)) {
    if (w.length > 0) tokens.add(w);
  }

  // 中文字符按 unigram 加入（细粒度匹配中文错误）
  for (const ch of t) {
    if (/[一-鿿]/.test(ch)) {
      tokens.add(ch);
    }
  }

  return tokens;
}

/**
 * Jaccard 相似度：|intersection| / |union|
 */
function jaccardSimilarity(a: string, b: string): number {
  const setA = tokenize(a);
  const setB = tokenize(b);
  if (setA.size === 0 && setB.size === 0) return 0;

  let intersection = 0;
  for (const t of setA) {
    if (setB.has(t)) intersection++;
  }

  const union = setA.size + setB.size - intersection;
  return union === 0 ? 0 : intersection / union;
}

/**
 * 自动分类：根据错误文本猜测分类
 */
function autoCategorize(text: string): string {
  const t = text.toLowerCase();

  if (/\b(compile|compilation|cannot find symbol|unmapped target|\.tsx?\b)/.test(t)) return "compile-error";
  if (/\b(test|junit|assert|expect\(|test failed|test flaky)\b/.test(t)) return "test-failure";
  if (/\b(sql|database|connection|psql|pg|jdbc|表不存在|column.*not found|relation.*not exist)\b/i.test(t)) return "db-query";
  if (/\b(null|NullPointerException|undefined|nullable|@NotNull|@Nullable|optional)\b/i.test(t)) return "null-safety";
  if (/\b(type.*mismatch|incompatible type|cannot convert|cannot cast|类型.*不匹配)\b/i.test(t)) return "type-mismatch";
  if (/\b(import|module.*not found|cannot find module|no such module|模块.*不存在)\b/i.test(t)) return "import-error";
  if (/\b(env|environment|config|configuration|\.env|application\.properties|未找到.*配置)\b/i.test(t)) return "env-config";
  if (/\b(out of memory|oom|heap|内存不足|heap space)\b/i.test(t)) return "build-oom";
  if (/\b(permission|access denied|forbidden|403|401|未授权|无权限)\b/i.test(t)) return "permission";
  if (/\b(data|quality|重复|duplicate|missing.*field|数据.*异常)\b/i.test(t)) return "data-quality";

  return "other";
}

/**
 * 加载共享目录中的模式（如果存在）
 */
function loadSharedPatterns(): { source: string; patterns: ErrorPattern[] }[] {
  if (!existsSync(SHARED_DIR)) return [];
  const results: { source: string; patterns: ErrorPattern[] }[] = [];

  try {
    const entries = readdirSync(SHARED_DIR, { withFileTypes: true });
    for (const entry of entries) {
      if (entry.isDirectory()) {
        const jsonPath = resolve(SHARED_DIR, entry.name, "error-patterns.json");
        if (existsSync(jsonPath)) {
          const patterns = load(jsonPath);
          if (patterns.length > 0) {
            results.push({ source: entry.name, patterns });
          }
        }
      }
    }
    // 也检查 sharedDir 下直接有没有 error-patterns.json
    const rootJson = resolve(SHARED_DIR, "error-patterns.json");
    if (existsSync(rootJson)) {
      const patterns = load(rootJson);
      if (patterns.length > 0) {
        results.push({ source: "shared", patterns });
      }
    }
  } catch {
    // 忽略共享目录的读取错误
  }

  return results;
}

// ─── 命令实现 ────────────────────────────────────────────────

/**
 * record — 记录错误模式
 */
function record(symptom: string, category: string, fix: string, filePath: string): void {
  const patterns = load();
  const id = generateId(symptom, category);
  const today = now();

  // 检查分类是否合法，否则 autoCategorize
  const finalCategory = CATEGORY_KEYS.includes(category) ? category : autoCategorize(symptom);

  // 自动推断 module 从 filePath
  const module = filePath
    ? filePath.split(/[/\\]/).slice(-2, -1)[0] || ""
    : "";

  const existing = patterns.find((p) => p.id === id);
  if (existing) {
    existing.count++;
    existing.lastSeen = today;
    existing.fix = fix;
    if (filePath) existing.file = filePath;
    if (module) existing.module = module;
    console.log(`已更新：${symptom}（第 ${existing.count} 次出现）`);
  } else {
    patterns.push({
      id,
      symptom,
      category: finalCategory,
      fix,
      file: filePath || "",
      module,
      count: 1,
      firstSeen: today,
      lastSeen: today,
      promotedToGotchas: false,
      fixed: false,
      level: "error",
    });
    console.log(`已记录新模式：${symptom}`);
  }

  save(patterns);
  console.log(`  分类：${CATEGORIES[finalCategory] || finalCategory}`);
}

/**
 * match — 匹配已有模式（Jaccard 相似度 + 词重叠 + 频率加权）
 */
function match(errorText: string, includeShared: boolean = false): void {
  const patterns = load();
  let allPatterns: { pattern: ErrorPattern; source: string }[] = patterns.map((p) => ({
    pattern: p,
    source: "local",
  }));

  if (includeShared) {
    const shared = loadSharedPatterns();
    for (const s of shared) {
      for (const p of s.patterns) {
        allPatterns.push({ pattern: p, source: s.source });
      }
    }
  }

  if (allPatterns.length === 0) {
    console.log("还没有错误模式记录。");
    return;
  }

  const lowerText = errorText.toLowerCase();

  const scored = allPatterns
    .map(({ pattern: p, source }) => {
      let score = 0;

      // 1. Jaccard 相似度（最大贡献 40 分）
      const jaccard = jaccardSimilarity(lowerText, p.symptom + " " + p.category);
      score += jaccard * 40;

      // 2. 精确子串匹配（症状全文包含）（最大贡献 30 分）
      if (lowerText.includes(p.symptom.toLowerCase())) {
        score += 30;
      }

      // 3. 症状关键词命中（每个词 3 分，最多 15 分）
      const symptomWords = p.symptom.toLowerCase().split(/[^a-z0-9一-鿿]+/).filter(Boolean);
      let wordHits = 0;
      for (const w of symptomWords) {
        if (w.length > 1 && lowerText.includes(w)) wordHits++;
      }
      score += Math.min(wordHits * 3, 15);

      // 4. 文件路径匹配（如果提供了 file 字段）
      if (p.file && lowerText.includes(p.file.toLowerCase())) {
        score += 10;
      }

      // 5. 频率加成（高频模式更值得关注）
      score += Math.min(p.count - 1, 5);

      return { pattern: p, score: Math.round(score * 10) / 10, source };
    })
    .filter((s) => s.score > 0)
    .sort((a, b) => b.score - a.score);

  if (scored.length === 0) {
    console.log("未匹配到已知模式。修复后可用 record 命令记录。");
    return;
  }

  // 根据匹配度判定置信度
  const topScore = scored[0].score;
  let confidence = "低";
  if (topScore >= 60) confidence = "高";
  else if (topScore >= 30) confidence = "中";

  const sourceLabel = includeShared ? `（含共享库）` : ``;

  console.log(`\n匹配到 ${scored.length} 个已知模式${sourceLabel}，最高匹配度 ${topScore}（置信度：${confidence}）：\n`);

  const displayCount = Math.min(scored.length, 8);
  for (let i = 0; i < displayCount; i++) {
    const { pattern: p, score, source } = scored[i];
    const sourceTag = source !== "local" ? ` [${source}]` : "";
    const fixedMark = p.fixed ? " [已确认修复]" : "";

    console.log(`  ${i + 1}. ${p.symptom}（匹配度 ${score}）${fixedMark}${sourceTag}`);
    console.log(`     分类：${CATEGORIES[p.category] || p.category}`);
    console.log(`     出现 ${p.count} 次，上次 ${p.lastSeen}`);
    console.log(`     修复：${p.fix}`);
    if (p.file) console.log(`     涉及：${p.file}`);
    console.log();
  }

  if (scored.length > displayCount) {
    console.log(`  ... 还有 ${scored.length - displayCount} 个匹配项`);
  }
}

/**
 * stats — 统计展示
 */
function stats(): void {
  const patterns = load();

  // 加载共享库计数（仅用于展示跨项目统计）
  const sharedEntries = loadSharedPatterns();

  if (patterns.length === 0) {
    console.log("还没有错误模式记录。");
    return;
  }

  const totalCount = patterns.reduce((s, p) => s + p.count, 0);
  const fixedCount = patterns.filter((p) => p.fixed).length;
  const promotedCount = patterns.filter((p) => p.promotedToGotchas).length;

  console.log(`\n═══ 错误模式统计 ═══`);
  console.log(`  模式总数：${patterns.length}`);
  console.log(`  累计出现：${totalCount} 次`);
  console.log(`  已确认修复：${fixedCount}`);
  console.log(`  已提升为 Gotchas：${promotedCount}`);

  if (sharedEntries.length > 0) {
    const sharedTotal = sharedEntries.reduce((s, e) => s + e.patterns.length, 0);
    console.log(`  共享库项目：${sharedEntries.length} 个（${sharedTotal} 条模式）`);
  }

  // 按分类聚合
  const byCategory: Record<string, number> = {};
  for (const p of patterns) {
    const cat = CATEGORIES[p.category] || p.category;
    byCategory[cat] = (byCategory[cat] || 0) + p.count;
  }

  console.log(`\n── 按分类统计 ──`);
  for (const [cat, count] of Object.entries(byCategory).sort((a, b) => b[1] - a[1])) {
    const bar = "█".repeat(Math.min(Math.ceil(count / totalCount * 30), 30));
    console.log(`  ${cat.padEnd(24)} ${count.toString().padStart(4)} ${bar}`);
  }

  // 高频模式 Top 10
  const top = [...patterns].sort((a, b) => b.count - a.count).slice(0, 10);
  if (top.length > 0) {
    console.log(`\n── 高频模式 Top ${top.length} ──`);
    for (const p of top) {
      const fixed = p.fixed ? " [已确认]" : "";
      console.log(`  ${p.count}x  ${p.symptom}${fixed}`);
      console.log(`     修复：${p.fix.length > 60 ? p.fix.slice(0, 60) + "…" : p.fix}`);
    }
  }

  // 未确认的高频模式提醒
  const unconfirmed = patterns.filter((p) => !p.fixed && p.count >= 3);
  if (unconfirmed.length > 0) {
    console.log(`\n⚠️  以下高频模式尚未确认修复有效（confirm 命令确认）：`);
    for (const p of unconfirmed) {
      console.log(`  ${p.count}x  ${p.symptom}（${p.category}）`);
    }
  }

  console.log();
}

/**
 * confirm — 确认修复有效
 */
function confirm(identifier: string): void {
  const patterns = load();
  if (patterns.length === 0) {
    console.log("还没有错误模式记录。");
    return;
  }

  // 先按 ID 精确匹配
  let target = patterns.find((p) => p.id === identifier);

  // 再按 symptom 包含匹配
  if (!target) {
    const lower = identifier.toLowerCase();
    target = patterns.find((p) => p.symptom.toLowerCase().includes(lower));
  }

  // 再按 symptom 模糊搜索取最佳 Jaccard
  if (!target) {
    const candidates = patterns
      .map((p) => ({ pattern: p, score: jaccardSimilarity(identifier, p.symptom) }))
      .filter((c) => c.score > 0.3)
      .sort((a, b) => b.score - a.score);

    if (candidates.length > 0) {
      target = candidates[0].pattern;
      console.log(`模糊匹配：${target.symptom}（相似度 ${Math.round(candidates[0].score * 100)}%）`);
    }
  }

  if (!target) {
    console.log(`未找到匹配 "${identifier}" 的模式。`);
    return;
  }

  if (target.fixed) {
    console.log(`[${target.symptom}] 已经标记为已确认修复。`);
    return;
  }

  target.fixed = true;
  save(patterns);
  console.log(`[${target.symptom}] 已确认修复有效。`);
}

/**
 * promote — 高频模式提升为 Gotchas
 */
function promote(apply: boolean = false): void {
  const patterns = load();
  const candidates = patterns.filter(
    (p) => p.count >= 3 && !p.promotedToGotchas && !p.fixed
  );

  // 也包含已确认修复但未 promote 的
  const fixedCandidates = patterns.filter(
    (p) => p.count >= 3 && !p.promotedToGotchas && p.fixed
  );

  const allCandidates = [...candidates, ...fixedCandidates].sort(
    (a, b) => b.count - a.count
  );

  if (allCandidates.length === 0) {
    console.log("没有需要 promote 的模式（需出现 >= 3 次且尚未 promote）。");

    const promoted = patterns.filter((p) => p.promotedToGotchas);
    if (promoted.length > 0) {
      console.log(`\n已 promote 的模式（${promoted.length} 个）：`);
      for (const p of promoted) {
        console.log(`  ${p.count}x  ${p.symptom}`);
      }
    }
    return;
  }

  console.log(`\n以下 ${allCandidates.length} 个模式达到 promote 阈值（>= 3 次）：\n`);

  for (const p of allCandidates) {
    const fixedLabel = p.fixed ? " [已确认修复]" : " [未确认]";
    console.log(`## Gotcha: ${p.symptom}${fixedLabel}`);
    console.log(`- **分类**: ${CATEGORIES[p.category] || p.category}`);
    console.log(`- **症状**: ${p.symptom}`);
    console.log(`- **修复**: ${p.fix}`);
    if (p.file) console.log(`- **涉及文件**: ${p.file}`);
    console.log(`- **出现次数**: ${p.count}`);
    console.log(`- **首次出现**: ${p.firstSeen}`);
    console.log(`- **最近出现**: ${p.lastSeen}`);
    console.log();
  }

  if (apply) {
    // 确保 gotchas 目录存在
    if (!existsSync(GOTCHAS_DIR)) {
      mkdirSync(GOTCHAS_DIR, { recursive: true });
    }

    // 按分类分组写入 gotchas 文件
    const byCategory: Record<string, ErrorPattern[]> = {};
    for (const p of allCandidates) {
      const cat = p.category;
      if (!byCategory[cat]) byCategory[cat] = [];
      byCategory[cat].push(p);
    }

    for (const [cat, items] of Object.entries(byCategory)) {
      const fileName = `gotchas-${cat}.md`;
      const filePath = resolve(GOTCHAS_DIR, fileName);

      let content = `# Gotchas: ${CATEGORIES[cat] || cat}\n\n`;
      content += `> 高频错误模式，自动从 error-patterns.ts promote 生成。\n`;
      content += `> 生成时间：${now()}\n\n`;

      for (const p of items) {
        content += `## ${p.symptom}\n\n`;
        content += `- **分类**: ${CATEGORIES[p.category] || p.category}\n`;
        content += `- **修复**: ${p.fix}\n`;
        if (p.file) content += `- **涉及文件**: ${p.file}\n`;
        content += `- **出现次数**: ${p.count}\n`;
        content += `- **最近出现**: ${p.lastSeen}\n`;
        content += `- **确认有效**: ${p.fixed ? "是" : "否"}\n\n`;
      }

      writeFileSync(filePath, content);
      console.log(`  已写入：${filePath}`);
    }

    // 标记 promote
    for (const p of allCandidates) {
      const target = patterns.find((pp) => pp.id === p.id);
      if (target) target.promotedToGotchas = true;
    }
    save(patterns);
    console.log(`\n已标记 ${allCandidates.length} 个模式为已 promote。`);
  } else {
    console.log(`提示：使用 --apply 参数将以上 Gotchas 写入 ${GOTCHAS_DIR}/`);
  }
}

// ─── CLI 入口 ────────────────────────────────────────────────

function usage(): void {
  console.log(`
用法:
  record   npx tsx scripts/error-patterns.ts record "<症状>" <分类> "<修复方案>" [文件路径]
  match    npx tsx scripts/error-patterns.ts match "<错误文本>" [--shared]
  stats    npx tsx scripts/error-patterns.ts stats
  confirm  npx tsx scripts/error-patterns.ts confirm <id或症状>
  promote  npx tsx scripts/error-patterns.ts promote [--apply]

分类: ${CATEGORY_KEYS.join(", ")}

示例:
  npx tsx scripts/error-patterns.ts record "Cannot find symbol: Factory" compile-error "排除 poi-ooxml-lite 依赖" "pom.xml"
  npx tsx scripts/error-patterns.ts match "NoSuchFieldError"
  npx tsx scripts/error-patterns.ts stats
`);
}

const cmd = process.argv[2];

switch (cmd) {
  case "record": {
    const args = process.argv.slice(3);
    // 处理引号参数，用正则提取
    // record "<symptom>" <category> "<fix>" [file]
    let symptom = "";
    let category = "";
    let fix = "";
    let filePath = "";

    // 简单参数解析：支持引号包裹的参数
    const fullArgs = process.argv.slice(3).join(" ");
    const quoteMatch = fullArgs.match(/^"([^"]*)"\s+(\S+)\s+"([^"]*)"(?:\s+(.+))?$/);

    if (quoteMatch) {
      symptom = quoteMatch[1];
      category = quoteMatch[2];
      fix = quoteMatch[3];
      filePath = (quoteMatch[4] || "").trim();
    } else {
      // 退化：空格分割，最多 4 个参数
      const parts = process.argv.slice(3);
      if (parts.length >= 3) {
        // 尝试恢复引号包裹：如果第一个参数以引号开头
        const joined = parts.join(" ");
        const simpleMatch = joined.match(/^"([^"]*)"\s+"([^"]*)"\s+"([^"]*)"(?:\s+(.+))?$/);
        if (simpleMatch) {
          symptom = simpleMatch[1];
          category = simpleMatch[2];
          fix = simpleMatch[3];
          filePath = (simpleMatch[4] || "").trim();
        } else {
          symptom = parts[0];
          category = parts[1];
          fix = parts[2];
          filePath = parts.slice(3).join(" ");
        }
      }
    }

    if (!symptom || !category || !fix) {
      console.log("用法：error-patterns.ts record \"<症状>\" <分类> \"<修复方案>\" [文件路径]");
      process.exit(1);
    }
    record(symptom, category, fix, filePath);
    break;
  }

  case "match": {
    const args = process.argv.slice(3);
    const includeShared = args.includes("--shared");
    const errorText = args.filter((a) => a !== "--shared").join(" ");
    if (!errorText) {
      console.log("用法：error-patterns.ts match \"<错误文本>\" [--shared]");
      process.exit(1);
    }
    match(errorText, includeShared);
    break;
  }

  case "confirm": {
    const identifier = process.argv.slice(3).join(" ");
    if (!identifier) {
      console.log("用法：error-patterns.ts confirm <id或症状>");
      process.exit(1);
    }
    confirm(identifier);
    break;
  }

  case "promote": {
    const apply = process.argv.includes("--apply");
    promote(apply);
    break;
  }

  case "stats":
    stats();
    break;

  default:
    usage();
    process.exit(1);
}
