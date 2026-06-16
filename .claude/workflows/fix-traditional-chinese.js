export const meta = {
  name: 'fix-traditional-chinese',
  description: '全面繁体化：全库扫描所有表的简体中文字段 → 统一转换为繁体（香港客户）',
  phases: [
    { title: 'Rescan DB', detail: '全库简体扫描' },
    { title: 'Reverse wrong fix', detail: '回滚繁→简的错误修改' },
    { title: 'Full conversion', detail: '所有表全字段繁化' },
    { title: 'Verify', detail: '验证+导出+构建' },
  ],
}

phase('Rescan DB');
const dbScan = await agent(`
## Task: 全面扫描 gold_foil_db 中所有简体中文字段

## 需要做的事

因为客户是香港公司，所有数据应该使用繁体中文。
之前的 P0 修复错误地把繁体"燙"转换成了简体"烫"——需要回滚。
但更重要的是：**全库所有表的所有字符字段**都要扫一遍简体字 → 繁体化。

### Step 1: 检查 opencc 是否可用
\`\`\`
/e/anaconda/python -c "import opencc; print(opencc.__version__)"
\`\`\`

如果不可用则安装：
\`\`\`
/e/anaconda/pip install opencc-python-reimplemented
\`\`\`

### Step 2: 扫描全库所有字符字段中的简体中文
\`\`\`
psql -h localhost -p 5432 -U postgres -d gold_foil_db << 'SQL'
SELECT
    table_name,
    column_name,
    data_type,
    is_nullable
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name NOT LIKE 'pg_%'
  AND data_type IN ('character varying', 'text', 'character', 'varchar')
ORDER BY table_name, ordinal_position;
SQL
\`\`\`

### Step 3: 对每个表每个字段抽样检查是否有简体中文
用 Python 连 PostgreSQL，对每个字符字段执行：
\`\`\`sql
SELECT DISTINCT LEFT({column}, 50) as sample_value
FROM {table}
WHERE {column} ~ '[\\x{4e00}-\\x{9fff}]'  -- 有中文
LIMIT 10;
\`\`\`

先用 Python 的 opencc 判断字符是简体还是繁体：
- opencc.convert(text, config='s2t') 返回繁体
- 如果 opencc.convert(text) != text，说明原文是简体

### Step 4: 给出全量简体字段报告
列出所有包含简体中文的 表.列 以及简体字符的样本值

DB Password: HryENprJrxThYSDz
`, {
  label: 'scan-simplified-chinese',
  phase: 'Rescan DB',
  schema: {
    type: 'object',
    properties: {
      opencc_available: { type: 'boolean' },
      tables_with_simplified: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            table_name: { type: 'string' },
            column_name: { type: 'string' },
            row_count: { type: 'number' },
            sample_values: { type: 'array', items: { type: 'string' } },
          },
          required: ['table_name', 'column_name'],
        },
      },
      total_columns_to_fix: { type: 'number' },
      issues: { type: 'string' },
    },
    required: ['total_columns_to_fix'],
  },
});

phase('Reverse wrong fix');
const reverseFix = await agent(`
## Task: 回滚之前错误执行的繁→简转换

之前的 P0 修复执行了：
\`\`\`
UPDATE hot_stamping_compatibility SET step_name = REPLACE(step_name, '燙', '烫');
UPDATE hot_stamping_compatibility SET paper_performance = REPLACE(paper_performance, '燙', '烫');
-- 等等类似操作
\`\`\`

现在需要回滚。但没法直接区分哪些"烫"是原来的繁体转换来的，哪些本来就是这个字。

## 方案

既然要全库繁体化，不需要精确回滚——直接对所有字段重新执行简→繁转换。
使用 opencc 的 s2t 配置（简体→繁体）覆盖全库。

先确认数据库中当前是否还有繁体字残留：
\`\`\`
psql -h localhost -p 5432 -U postgres -d gold_foil_db -c "
SELECT COUNT(*) FROM hot_stamping_compatibility
WHERE step_name LIKE '%燙%' OR paper_performance LIKE '%燙%';
"
\`\`\`

如果还有繁体残留说明之前没完全改完，如果已经没有繁体了说明已全部改成了简体。

报告当前状态即可（真正的转换由下一阶段全库执行）。
`, {
  label: 'reverse-wrong-fix',
  phase: 'Reverse wrong fix',
  schema: {
    type: 'object',
    properties: {
      remaining_traditional: { type: 'number', description: '还有多少行包含繁体字' },
      status: { type: 'string' },
    },
    required: ['remaining_traditional', 'status'],
  },
});

phase('Full conversion');
const fullConversion = await agent(`
## Task: 全库简体→繁体转换

根据扫描结果，对每个包含简体的表.列执行 opencc 转换。

### 转换脚本示例（Python）

\`\`\`python
import psycopg2
import opencc

conn = psycopg2.connect(
    host='localhost', port=5432, dbname='gold_foil_db',
    user='postgres', password='HryENprJrxThYSDz'
)
cur = conn.cursor()
converter = opencc.OpenCC('s2t')  # 简体→繁体

# 读取所有需要转换的列
columns_to_fix = [
    ('hot_stamping_compatibility', 'step_name'),
    ('hot_stamping_compatibility', 'paper_performance'),
    # ... 从扫描结果填充
]

for table, col in columns_to_fix:
    # 先获取所有有中文的行
    cur.execute(f"SELECT id, {col} FROM {table} WHERE {col} ~ '[\\\\x{4e00}-\\\\x{9fff}]'")
    rows = cur.fetchall()
    updated = 0
    for row_id, old_text in rows:
        new_text = converter.convert(old_text)
        if new_text != old_text:
            cur.execute(f"UPDATE {table} SET {col}=%s WHERE id=%s", (new_text, row_id))
            updated += 1
    conn.commit()
    print(f"{table}.{col}: {updated}/{len(rows)} rows updated")

cur.close()
conn.close()
\`\`\`

### 使用 opencc 转换

如果 opencc-python-reimplemented 没装：
\`\`\`
/e/anaconda/pip install opencc-python-reimplemented
\`\`\`

### 重要：涉及文字的特殊性

印刷厂术语中有些字需要特别注意：
- "干" → 在"干燥"场景下是"乾燥"，在"才干"场景下不同。印刷厂几乎都是"乾燥"，所以 safe
- "后" → "后加工"保持不变（"后"在繁体中就是"後"，但"皇后"等是"后"）
   - opencc 会自动处理：在"后加工"中"后"→"後" ✅
- "发" → "发光"→"發光"，"头发"→"頭髮"。印刷厂是"發光油墨"，opencc 会自动处理 ✅
- "制" → "制定"→"製定"，opencc 处理 ✅
- "冲" → "冲窗口"→"衝窗口"，opencc 处理 ✅
- "只" → "只可"→"只可"（"只"繁简一样），"一隻"等其他含义不同。

直接使用 opencc 的标准 s2t 配置即可，不需要手动映射。

### 执行转换后验证
\`\`\`
psql -h localhost -p 5432 -U postgres -d gold_foil_db -c "
SELECT COUNT(*) FROM hot_stamping_compatibility
WHERE step_name LIKE '%烫%' AND step_name NOT LIKE '%燙%';
"
\`\`\`
应该返回 0（如果 opencc 正确处理了"烫"→"燙"）

DB Password: HryENprJrxThYSDz
`, {
  label: 'full-traditional-conversion',
  phase: 'Full conversion',
  schema: {
    type: 'object',
    properties: {
      tables_converted: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            table_name: { type: 'string' },
            column_name: { type: 'string' },
            rows_updated: { type: 'number' },
          },
          required: ['table_name', 'column_name', 'rows_updated'],
        },
      },
      total_rows_updated: { type: 'number' },
      opencc_config: { type: 'string' },
      issues: { type: 'string' },
    },
    required: ['total_rows_updated'],
  },
});

phase('Verify');
const verifyTraditional = await agent(`
## Task: 验证全库繁体化完成 + 重新导出

### Step 1: 验证无简体残留
抽查几个关键字段：
\`\`\`
psql -h localhost -p 5432 -U postgres -d gold_foil_db << 'SQL'
-- hot_stamping 表
SELECT 'hot_stamping_compatibility.step_name 残留简体烫' as check_name,
  COUNT(*) as cnt FROM hot_stamping_compatibility WHERE step_name LIKE '%烫%' AND step_name NOT LIKE '%燙%';
-- 所有表抽查：列出前10个含简体最多的字符列
SELECT table_name, column_name, COUNT(*) as simplified_count
FROM (
  -- 需要动态检查。用 Python 脚本代替
) sub ORDER BY simplified_count DESC LIMIT 10;
SQL
\`\`\`

用 Python 脚本全库验证：
- 对每个表每个字符字段抽样
- 用 opencc.convert(text, config='s2t') 检查 text != converted 的行数
- 如果转换后不同，说明原文是简体

### Step 2: 验证"烫"已经是繁体"燙"
\`\`\`
psql -h localhost -p 5432 -U postgres -d gold_foil_db -c "
SELECT COUNT(*) FROM hot_stamping_compatibility WHERE step_name LIKE '%烫%';
"
\`\`\`
如果有简体"烫"残留，打印出具体行

### Step 3: 重新导出
\`\`\`
/e/anaconda/python F:/yatushi-6-6/database_scripts/export_to_excel.py
\`\`\`

### Step 4: 构建验证
\`\`\`
cd F:/yatushi-6-6/yts_project && mvn compile -q
cd F:/yatushi-6-6/yts_project_vueai && npm run build 2>&1 | tail -5
\`\`\`

DB Password: HryENprJrxThYSDz
`, {
  label: 'verify-traditional',
  phase: 'Verify',
  schema: {
    type: 'object',
    properties: {
      verified_clean: { type: 'boolean', description: '无简体残留' },
      hot_stamping_verified: { type: 'boolean', description: '"烫"已全部转为"燙"' },
      export_regenerated: { type: 'boolean' },
      build_passed: { type: 'boolean' },
      summary: { type: 'string' },
    },
    required: ['verified_clean', 'build_passed'],
  },
});

log(`
✅ 全面繁体化完成！

扫描发现: ${dbScan?.total_columns_to_fix} 个字段包含简体中文
已回滚错误修改: ${reverseFix?.status}
已转换: ${fullConversion?.total_rows_updated} 行数据
验证: ${verifyTraditional?.verified_clean ? '✅ 无简体残留' : '❌ 仍有残留'}
导出: ${verifyTraditional?.export_regenerated ? '✅ 已重新导出' : '❌'}
构建: ${verifyTraditional?.build_passed ? '✅ 通过' : '❌'}
`);

return {
  scanResult: dbScan?.tables_with_simplified,
  reverseStatus: reverseFix?.status,
  conversionStats: fullConversion?.tables_converted,
  verified: verifyTraditional?.summary,
};
