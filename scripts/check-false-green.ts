#!/usr/bin/env npx tsx
/**
 * check-false-green.ts — False-Green 检测脚本
 *
 * 扫描前端测试文件，识别"假绿"模式（测试通过但实际未有效断言）。
 *
 * 检测规则：
 *   1. 零 expect 调用的 test block
 *   2. 仅使用弱断言的 test block（toEqual 除外）
 *      弱断言列表：not.toBeNull / toBeTruthy / toBeDefined / toBeUndefined / toBeFalsy
 *   3. 空 describe 块（内部无 it/test）
 *
 * 用法:
 *   npx tsx scripts/check-false-green.ts                      # 扫描全部测试文件
 *   npx tsx scripts/check-false-green.ts --json               # JSON 格式输出
 *   npx tsx scripts/check-false-green.ts --verbose            # 详细输出（含具体行号）
 *   npx tsx scripts/check-false-green.ts --file <相对路径>     # 只检测单个文件
 *   npx tsx scripts/check-false-green.ts --exit-code           # 有 flagged 文件时 exit 1
 *
 * 依赖: 仅 Node.js 内置模块（fs / path / util）
 *
 * 灵感: ERPAI reality-checker
 */

import { readFileSync, readdirSync, existsSync } from "node:fs";
import { resolve, relative, extname } from "node:path";

// ─── 类型定义 ──────────────────────────────────────────────────

interface Location {
  line: number;
  column: number;
}

interface FlaggedTest {
  name: string;
  reason: string;
  location?: Location;
}

interface FileReport {
  file: string;
  totalTests: number;
  flaggedTests: FlaggedTest[];
  emptyDescribes: { name: string; location?: Location }[];
}

interface ReportSummary {
  totalFiles: number;
  totalTests: number;
  totalFlagged: number;
  totalEmptyDescribes: number;
}

// ─── 配置 ────────────────────────────────────────────────────

/** 弱断言：这些断言本身不能有效验证结果，容易产生假绿 */
const WEAK_ASSERTIONS = new Set([
  "not.toBeNull",
  ".toBeTruthy",
  ".toBeDefined",
  ".toBeUndefined",
  ".toBeFalsy",
]);

/** 强断言（除了 not 前缀的，其他含 toBe 的比弱断言更严格） */
const STRONG_ASSERTIONS = new Set([
  ".toBe(",
  ".toEqual(",
  ".toStrictEqual(",
  ".toContain(",
  ".toHaveLength(",
  ".toThrow(",
  ".toMatch(",
  ".toBeGreaterThan(",
  ".toBeLessThan(",
  ".toBeGreaterThanOrEqual(",
  ".toBeLessThanOrEqual(",
  ".toHaveBeenCalled(",
  ".toHaveBeenCalledWith(",
  ".toHaveBeenCalledTimes(",
  ".toHaveProperty(",
  ".resolves.",
  ".rejects.",
]);

// ─── 匹配辅助函数 ─────────────────────────────────────────────

/**
 * 在行的内容中检查是否有链式断言，以及是否弱断言
 */
function extractExpectCalls(
  lines: string[],
  startLine: number,
  endLine: number,
): { expects: number; weakOnly: boolean; weakNames: string[] } {
  let expects = 0;
  let weakOnly = true;
  const weakNames: string[] = [];

  for (let i = startLine; i <= endLine && i < lines.length; i++) {
    const line = lines[i];
    const trimmed = line.trim();

    // 跳过注释
    if (trimmed.startsWith("//") || trimmed.startsWith("*") || trimmed.startsWith("/*")) {
      continue;
    }

    // 检测 expect(...) 调用
    if (!trimmed.includes("expect(")) continue;

    // 多行 expect 可能跨行，粗略统计
    expects++;

    // 判断是否为弱断言
    const isWeak = Array.from(WEAK_ASSERTIONS).some((w) => trimmed.includes(w));

    if (isWeak) {
      for (const w of WEAK_ASSERTIONS) {
        if (trimmed.includes(w)) {
          weakNames.push(w);
          break;
        }
      }
    }

    // 判断是否包含至少一个强断言
    const hasStrong = Array.from(STRONG_ASSERTIONS).some((s) => trimmed.includes(s));

    if (hasStrong) {
      weakOnly = false;
    } else if (!isWeak) {
      // 有 expect 但既不在弱断言也不在强断言列表中 -> 可能是自定义匹配器
      // 保守处理：不标记为弱
      weakOnly = false;
    }
  }

  return { expects, weakOnly, weakNames };
}

/**
 * 查找文件中所有的 it/test 块和 describe 块
 */
function findTestBlocks(content: string): {
  blocks: { name: string; start: number; end: number; type: "it" | "test" }[];
  describes: { name: string; start: number; end: number; innerItCount: number }[];
} {
  const lines = content.split("\n");
  const blocks: { name: string; start: number; end: number; type: "it" | "test" }[] = [];
  const describes: { name: string; start: number; end: number; innerItCount: number; hasChildDescribe: boolean }[] = [];

  // 匹配 it('...') 或 it("...") 或 test('...') 或 test("...")
  // 也处理 it(`...`) 模板字符串（含 ${...} 插值）
  // 使用 String.raw 避免转义地狱
  // 匹配 it(...), it.each(...)(...), test(...), test.each(...)(...)
  const blockRegex = /^(?:it|test)(?:\.each\([^)]*\))?\s*\(\s*[`'"]/;
  function extractTestName(line: string): string {
    const m = line.match(/^(?:it|test)(?:\.each\([^)]*\))?\s*\(\s*(['"`])/);
    if (!m) return "<unnamed>";
    const quote = m[1];
    const rest = line.slice(m[0].length);
    if (quote === "`") {
      // 模板字符串: 匹配到下一个反引号，忽略嵌套的 ${}
      let depth = 0;
      let name = "";
      for (const ch of rest) {
        if (ch === "$" && rest[rest.indexOf(ch) + 1] === "{" && depth === 0) {
          // start of interpolation, just include it
          name += ch;
        } else if (ch === "`" && depth === 0) {
          break;
        } else {
          name += ch;
        }
      }
      return name.trim().replace(/\${[^}]*}/g, "…").replace(/,?\s*\)\s*=>\s*{?\s*$/, "").trim() || "<unnamed>";
    }
    // 普通引号: 匹配到下一个同种引号
    const escIdx = rest.indexOf(quote);
    return escIdx === -1 ? "<unnamed>" : rest.slice(0, escIdx).trim();
  }
  function extractDescribeName(line: string): string {
    const m = line.match(/^describe\s*\(\s*(['"`])/);
    if (!m) return "<unnamed>";
    const quote = m[1];
    const rest = line.slice(m[0].length);
    if (quote === "`") {
      let name = "";
      for (const ch of rest) {
        if (ch === "`") break;
        name += ch;
      }
      return name.trim().replace(/\${[^}]*}/g, "…").trim() || "<unnamed>";
    }
    const escIdx = rest.indexOf(quote);
    return escIdx === -1 ? "<unnamed>" : rest.slice(0, escIdx).trim();
  }

  // 用简单的缩进解析：it/test 后面跟着 => 或 function 的 {}
  // 这用于寻找块结束位置

  function findBlockEnd(
    content: string,
    openLineIdx: number,
    lines: string[],
  ): number {
    const openLine = lines[openLineIdx];
    // 去除模板字符串中的 ${}（如 `${files.length}`）和普通字符串字面量后计数
    const stripString = (s: string) => s.replace(/`[^`]*`/g, "").replace(/['"][^'"]*['"]/g, "");
    const openBracesInLine = (stripString(openLine).match(/\{/g) || []).length;
    let braceDepth = openBracesInLine;

    // 如果在同一行关闭了，找 } 后的 ); 或 },
    if (braceDepth === 0) return openLineIdx;

    for (let i = openLineIdx + 1; i < lines.length; i++) {
      const clean = stripString(lines[i]);
      const opens = (clean.match(/\{/g) || []).length;
      const closes = (clean.match(/\}/g) || []).length;
      braceDepth = braceDepth + opens - closes;

      if (braceDepth <= 0) {
        // 检查后面是否紧跟 );
        let j = i + 1;
        while (j < lines.length && lines[j].trim() === "") j++;
        if (j < lines.length && lines[j].trim() === ");") return j;
        return i;
      }
    }
    return lines.length - 1;
  }

  // 栈式解析
  const describeStack: { name: string; start: number; end: number; itCount: number; hasChildDescribe: boolean; closingDepth: number }[] = [];

  // 遍历行，计算括号深度
  let braceDepth = 0;

  for (let i = 0; i < lines.length; i++) {
    const line = lines[i];
    const trimmed = line.trim();

    // 跳过注释行和空白行
    if (trimmed.startsWith("//") || trimmed === "") continue;

    // 计算此行中的 { 和 } 数量变化
    const opens = (line.match(/\{/g) || []).length;
    const closes = (line.match(/\}/g) || []).length;

    // 匹配 describe 开始 (在深度变化前处理)
    if (/^describe\s*\(/.test(trimmed)) {
      const name = extractDescribeName(trimmed);
      // closingDepth = 当前 braceDepth (还没加上本行 opens)，即 describe 体 { 之后的深度
      const bodyDepth = braceDepth + opens; // opens are the { in `=> {`
      describeStack.push({ name, start: i, end: lines.length - 1, itCount: 0, hasChildDescribe: false, closingDepth: bodyDepth });
      braceDepth += opens - closes;
      continue;
    }

    // 匹配 it/test 开始（含 it.each / test.each）
    if (blockRegex.test(trimmed)) {
      const name = extractTestName(trimmed);

      // 判断是否单行: it('...', () => { ...一行内结束... });
      // 需要排除模板字符串 ${...} 中的大括号
      const strippedLine = trimmed.replace(/`[^`]*`/g, "").replace(/['"][^'"]*['"]/g, "");
      const endBrace = strippedLine.lastIndexOf("}");
      if (endBrace !== -1 && strippedLine.includes("=>") && strippedLine.includes("{")) {
        // 单行箭头函数块
        blocks.push({ name, start: i, end: i, type: trimmed.startsWith("it") ? "it" : "test" });
        if (describeStack.length > 0) {
          describeStack[describeStack.length - 1].itCount++;
        }
        continue;
      }

      const endLine = findBlockEnd(content, i, lines);
      blocks.push({ name, start: i, end: endLine, type: trimmed.startsWith("it") ? "it" : "test" });
      if (describeStack.length > 0) {
        describeStack[describeStack.length - 1].itCount++;
      }
      continue;
    }

    // 非 describe 行：更新 braceDepth
    if (!(/^describe\s*\(/.test(trimmed) || blockRegex.test(trimmed))) {
      braceDepth += opens - closes;

      // 当 braceDepth 低于 describe 的起始深度时，弹出已关闭的 describe
      // 每个 describe 在其起始行有 opens 个 {，起始深度 = push 时的 braceDepth
      while (describeStack.length > 0 && braceDepth < describeStack[describeStack.length - 1].closingDepth) {
        const top = describeStack.pop()!;
        top.end = i;
        describes.push({ name: top.name, start: top.start, end: top.end, innerItCount: top.itCount, hasChildDescribe: top.hasChildDescribe });
        if (describeStack.length > 0) {
          describeStack[describeStack.length - 1].hasChildDescribe = true;
        }
      }
    } else if (blockRegex.test(trimmed)) {
      // it/test 行也需要更新 braceDepth (适用于同一行有 { 的情况)
      braceDepth += opens - closes;
    }
  }

  // 清理未关闭的 describe
  while (describeStack.length > 0) {
    const top = describeStack.pop()!;
    describes.push({ name: top.name, start: top.start, end: top.end, innerItCount: top.itCount, hasChildDescribe: top.hasChildDescribe });
  }

  return { blocks, describes };
}

// ─── Core Detection ────────────────────────────────────────────

function scanFile(filePath: string): FileReport {
  const content = readFileSync(filePath, "utf-8");
  const lines = content.split("\n");
  const { blocks, describes } = findTestBlocks(content);

  const flaggedTests: FlaggedTest[] = [];
  const emptyDescribes: { name: string; location?: Location }[] = [];

  // 检测空 describe（不含 it/test 且不含子 describe）
  for (const d of describes) {
    if (d.innerItCount === 0 && !d.hasChildDescribe) {
      emptyDescribes.push({
        name: d.name,
        location: { line: d.start + 1, column: 1 },
      });
    }
  }

  // 检测每个 test block
  for (const block of blocks) {
    const { expects, weakOnly, weakNames } = extractExpectCalls(lines, block.start, block.end);

    if (expects === 0) {
      flaggedTests.push({
        name: block.name,
        reason: "零 expect 调用 — 测试内无任何断言",
        location: { line: block.start + 1, column: 1 },
      });
    } else if (weakOnly && expects > 0) {
      flaggedTests.push({
        name: block.name,
        reason: `仅使用弱断言 [${weakNames.join(", ")}] — 无法有效验证正确性，容易产生「假绿」`,
        location: { line: block.start + 1, column: 1 },
      });
    }
  }

  const shortPath = relative(resolve(__dirname, ".."), filePath).replace(/\\/g, "/");

  return {
    file: shortPath,
    totalTests: blocks.length,
    flaggedTests,
    emptyDescribes,
  };
}

/**
 * 递归查找测试文件
 */
function findTestFiles(dir: string): string[] {
  const results: string[] = [];

  if (!existsSync(dir)) {
    return results;
  }

  const entries = readdirSync(dir, { withFileTypes: true });

  for (const entry of entries) {
    const fullPath = resolve(dir, entry.name);

    if (entry.isDirectory()) {
      // 跳过 node_modules 和 dist
      if (entry.name === "node_modules" || entry.name === "dist") continue;
      results.push(...findTestFiles(fullPath));
    } else if (entry.isFile()) {
      const ext = extname(entry.name);
      if ((ext === ".ts" || ext === ".tsx") && entry.name.endsWith(".spec.ts") || entry.name.endsWith(".test.ts")) {
        results.push(fullPath);
      }
    }
  }

  return results;
}

// ─── 输出格式化 ────────────────────────────────────────────────

function printHumanSummary(reports: FileReport[]): void {
  const flaggedFiles = reports.filter((r) => r.flaggedTests.length > 0 || r.emptyDescribes.length > 0);
  const totalTests = reports.reduce((s, r) => s + r.totalTests, 0);
  const totalFlagged = reports.reduce((s, r) => s + r.flaggedTests.length, 0);
  const totalEmpty = reports.reduce((s, r) => s + r.emptyDescribes.length, 0);

  const summaryLine = [
    `共扫描 ${reports.length} 个测试文件`,
    `${totalTests} 个测试用例`,
    `发现 ${totalFlagged} 个「假绿」测试`,
    `${totalEmpty} 个空 describe`,
  ].join("，");

  console.log(`\n${"═".repeat(56)}`);
  console.log(`  False-Green 检测报告`);
  console.log(`${"═".repeat(56)}`);
  console.log(`  ${summaryLine}`);
  console.log(`${"═".repeat(56)}`);

  if (flaggedFiles.length === 0) {
    console.log(`\n  ✓ 全部测试通过有效断言检查！`);
    return;
  }

  for (const report of flaggedFiles) {
    console.log(`\n  📄 ${report.file}`);
    console.log(`  ${"─".repeat(50)}`);

    for (const ft of report.flaggedTests) {
      console.log(`    [假绿] "${ft.name}"`);
      console.log(`           原因: ${ft.reason}`);
      if (ft.location) {
        console.log(`           位置: 第 ${ft.location.line} 行`);
      }
    }

    for (const ed of report.emptyDescribes) {
      console.log(`    [空块] "${ed.name}"`);
      if (ed.location) {
        console.log(`           位置: 第 ${ed.location.line} 行`);
      }
    }
  }

  const total = `${"═".repeat(56)}`;
  console.log(`\n${total}`);
  console.log(`  总计: ${reports.length} 文件, ${totalTests} 用例`);
  console.log(`  假绿: ${totalFlagged}, 空 describe: ${totalEmpty}`);
  if (totalFlagged > 0) {
    console.log(`  建议: 对假绿测试补充强断言 (如 toEqual, toContain, toHaveLength)`);
  }
  console.log(`${total}\n`);
}

function printJsonSummary(reports: FileReport[]): void {
  const summary: ReportSummary = {
    totalFiles: reports.length,
    totalTests: reports.reduce((s, r) => s + r.totalTests, 0),
    totalFlagged: reports.reduce((s, r) => s + r.flaggedTests.length, 0),
    totalEmptyDescribes: reports.reduce((s, r) => s + r.emptyDescribes.length, 0),
  };

  const output = {
    summary,
    reports: reports.filter((r) => r.flaggedTests.length > 0 || r.emptyDescribes.length > 0),
  };

  console.log(JSON.stringify(output, null, 2));
}

function printVerbose(reports: FileReport[]): void {
  // 先打印报告
  printHumanSummary(reports);

  // 再打印每个 flagged 文件的具体代码片段
  console.log(`\n${"▔".repeat(56)}`);
  console.log(`  详细代码上下文`);
  console.log(`${"▔".repeat(56)}`);

  const rootDir = resolve(__dirname, "..");

  for (const report of reports) {
    if (report.flaggedTests.length === 0 && report.emptyDescribes.length === 0) continue;

    const fullPath = resolve(rootDir, report.file);
    if (!existsSync(fullPath)) continue;

    const content = readFileSync(fullPath, "utf-8");
    const lines = content.split("\n");
    const flaggedLines = new Set<number>();

    for (const ft of report.flaggedTests) {
      if (ft.location) flaggedLines.add(ft.location.line);
    }
    for (const ed of report.emptyDescribes) {
      if (ed.location) flaggedLines.add(ed.location.line);
    }

    console.log(`\n  📄 ${report.file}`);
    console.log(`  ${"─".repeat(50)}`);

    // 显示 flagged 行周围 3 行上下文
    const shownRanges = new Set<string>();

    for (const lineNum of flaggedLines) {
      const start = Math.max(0, lineNum - 3);
      const end = Math.min(lines.length, lineNum + 2);

      const key = `${start}-${end}`;
      if (shownRanges.has(key)) continue;
      shownRanges.add(key);

      console.log(`  ${"·".repeat(40)}`);
      for (let i = start; i < end; i++) {
        const marker = flaggedLines.has(i + 1) ? ">>>" : "   ";
        const lineContent = lines[i] || "";
        console.log(`  ${marker} ${(i + 1).toString().padStart(4)} | ${lineContent}`);
      }
    }
  }
}

// ─── 主入口 ─────────────────────────────────────────────────────

function main(): void {
  const args = process.argv.slice(2);
  const isJson = args.includes("--json");
  const isVerbose = args.includes("--verbose");
  const exitCode = args.includes("--exit-code");
  const singleFileIdx = args.indexOf("--file");

  const rootDir = resolve(__dirname, "..");
  const testBaseDir = resolve(rootDir, "yts_project_vueai", "src");

  // 支持 --file 只跑单个文件
  let singleFile: string | null = null;

  if (singleFileIdx !== -1 && singleFileIdx + 1 < args.length) {
    // --file 可以是相对路径或绝对路径
    const rawPath = args[singleFileIdx + 1];
    if (rawPath.startsWith("/") || rawPath.includes(":")) {
      singleFile = rawPath;
    } else {
      singleFile = resolve(rootDir, rawPath);
    }
  }

  let testFiles: string[];

  if (singleFile) {
    if (!existsSync(singleFile)) {
      console.error(`文件不存在: ${singleFile}`);
      process.exit(1);
    }
    testFiles = [singleFile];
  } else {
    testFiles = findTestFiles(testBaseDir);
  }

  if (testFiles.length === 0) {
    console.log(`未在 ${testBaseDir} 下找到测试文件。`);
    process.exit(0);
  }

  const reports = testFiles.map(scanFile);

  if (isJson) {
    printJsonSummary(reports);
  } else if (isVerbose) {
    printVerbose(reports);
  } else {
    printHumanSummary(reports);
  }

  if (exitCode) {
    const totalFlagged = reports.reduce((s, r) => s + r.flaggedTests.length, 0);
    const totalEmpty = reports.reduce((s, r) => s + r.emptyDescribes.length, 0);
    if (totalFlagged > 0 || totalEmpty > 0) {
      process.exit(1);
    }
  }
}

main();
