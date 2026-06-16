export const meta = {
  name: 'etl-pipeline-phase0',
  description: 'Phase 0: 逐个文件深度审计 — 对 G 盘所有 Type A xlsx 运行审计脚本，输出每文件的数据行数、步骤数、V/X 分布',
  phases: [
    { title: 'Audit 硅胶', detail: '13 files' },
    { title: 'Audit UV油墨', detail: '2 files' },
    { title: 'Audit 印刷加工+LEO纸品', detail: '4 files' },
    { title: 'Synthesize', detail: '汇总审计结果 → 更新计划 + 记录文件' },
  ],
}

// ========== Shared: Python audit script ==========
const AUDIT_SCRIPT = `
import openpyxl, os, re, sys, json
from collections import Counter

def audit(path):
    wb = openpyxl.load_workbook(path, data_only=True)
    ws = wb.active

    # Find step header row
    best_row, best_n = 0, 0
    for r in range(12, min(35, ws.max_row)):
        n = sum(1 for c in range(30, min(151, ws.max_column+1)) if ws.cell(row=r, column=c).value)
        if n > best_n:
            best_n, best_row = n, r

    # Step count
    steps = sum(1 for c in range(30, ws.max_column+1) if ws.cell(row=best_row, column=c).value)
    maxc = max(c for c in range(30, ws.max_column+1) if ws.cell(row=best_row, column=c).value)

    # Data rows
    data = []
    for r in range(best_row + 1, ws.max_row + 1):
        v6 = ws.cell(row=r, column=6).value
        v9 = ws.cell(row=r, column=9).value
        if not v9: continue
        code = str(v6 or '').strip()
        name = str(v9).strip()
        if code.startswith('#'):
            data.append({'row': r, 'code': code[1:], 'name': name, 'method': '#', 'c10': str(ws.cell(row=r,column=10).value or '').strip()[:30]})
        elif re.match(r'^[A-Za-z]{2,4}-\\d{4,6}$', code):
            data.append({'row': r, 'code': code, 'name': name, 'method': 'regex', 'c10': str(ws.cell(row=r,column=10).value or '').strip()[:30]})

    # Compatibility on first data row
    compat = {}
    if data:
        r = data[0]['row']
        statuses = [str(ws.cell(row=r, column=c).value or '').strip() for c in range(30, ws.max_column+1)]
        c = Counter(statuses)
        compat = {'V': c.get('V', 0), 'X': c.get('X', 0), 'other': {k: v for k, v in c.items() if k not in ('V', 'X', '', '/')}}

    # Multi-level header (rows above step_row)
    headers = {}
    for hr in [best_row - 2, best_row - 1, best_row]:
        if hr < 1: continue
        hcells = [(c, str(ws.cell(row=hr, column=c).value or '').replace('\\n',' / ')[:40]) for c in range(30, 75) if ws.cell(row=hr, column=c).value]
        if hcells: headers[hr] = hcells

    result = {
        'file': os.path.basename(path),
        'sheet': ws.title,
        'rows': ws.max_row,
        'cols': ws.max_column,
        'merged': len(ws.merged_cells.ranges),
        'header_row': best_row,
        'steps': steps,
        'max_compat_col': maxc,
        'data_count': len(data),
        'data_by_hash': sum(1 for d in data if d['method'] == '#'),
        'data_by_regex': sum(1 for d in data if d['method'] == 'regex'),
        'first_few': data[:3],
        'compat': compat,
        'headers': headers,
    }
    wb.close()
    return result

if len(sys.argv) < 2:
    print('Usage: script.py <directory>')
    sys.exit(1)

base = sys.argv[1]
files = [f for f in os.listdir(base) if f.endswith('.xlsx') and not f.startswith('~$')]
files.sort()
results = {}
for f in files:
    path = os.path.join(base, f)
    try:
        r = audit(path)
        results[f] = r
        print(f'[OK] {f}: {r["data_count"]} data rows, {r["steps"]} steps, header=R{r["header_row"]}')
    except Exception as e:
        results[f] = {'file': f, 'error': str(e)}
        print(f'[ERR] {f}: {e}')

print('===JSON_START===')
print(json.dumps(results, ensure_ascii=False, indent=2))
print('===JSON_END===')
`

async function runAudit(agentName, dirName, baseDir) {
  const path = `${baseDir}/${dirName}`;
  return agent(
    `Run this Python audit script against the xlsx files in directory "${path}":

\`\`\`python
${AUDIT_SCRIPT}
\`\`\`

Run it as: /e/anaconda/python <script> "${path}"

Parse the JSON output between ===JSON_START=== and ===JSON_END=== markers.
Return ONLY the JSON object (parsed). Do NOT summarize or modify it.`,
    {
      label: `audit:${dirName}`,
      phase: `Audit ${dirName}`,
      schema: {
        type: 'object',
        properties: {
          files: {
            type: 'object',
            description: 'Map of filename → audit result with data_count, steps, compat, etc.',
            additionalProperties: {
              type: 'object',
              properties: {
                data_count: { type: 'number' },
                steps: { type: 'number' },
                header_row: { type: 'number' },
                data_by_hash: { type: 'number' },
                data_by_regex: { type: 'number' },
                compat: { type: 'object' },
                error: { type: 'string' },
              },
              required: ['data_count', 'steps'],
            },
          },
        },
        required: ['files'],
      },
    }
  );
}

// ========== Main ==========
const BASE = 'G:/雅图仕/download';

phase('Audit 硅胶');
const siliconeResult = await runAudit('硅胶', '硅胶', BASE);

phase('Audit UV油墨');
const uvResult = await runAudit('UV油墨', 'UV油墨', BASE);

phase('Audit 印刷加工+LEO纸品');
const printResult = await runAudit('印刷加工+LEO纸品', '印刷加工', BASE);
const leoResult = await runAudit('LEO纸品', 'LEO纸品', BASE);

phase('Synthesize');

// Merge all results
const allResults = {
  硅胶: siliconeResult?.files || {},
  UV油墨: uvResult?.files || {},
  印刷加工: printResult?.files || {},
  LEO纸品: leoResult?.files || {},
};

log('=== 审计完成 ===');
for (const [dir, files] of Object.entries(allResults)) {
  log(`\\n${dir}:`);
  for (const [fname, r] of Object.entries(files)) {
    if (r.error) {
      log(`  [ERR] ${fname}: ${r.error}`);
    } else {
      log(`  ${fname}: ${r.data_count} rows, ${r.steps} steps, header=R${r.header_row}, V=${r.compat?.V || 0}, X=${r.compat?.X || 0}`);
    }
  }
}

return allResults;
