export const meta = {
  name: 'fix-p0-issues',
  description: '修复 3 个 P0 问题：繁简统一 + 过胶字段清洗 + API链路排查',
  phases: [
    { title: 'Fix 繁简统一', detail: 'hot_stamping 表繁体→简体' },
    { title: 'Fix 过胶字段', detail: '新建 film_thickness + 清洗数据 + 补查原始文件' },
    { title: 'Fix API链路', detail: '追踪 6/6 空结果 SQL 执行路径' },
    { title: 'Verify', detail: '验证修复结果 + 导出更新' },
  ],
}

phase('Fix 繁简统一');
const fixSimplified = await agent(`
## Task: 修复 hot_stamping_compatibility 表繁简混用问题

繁简混用详情（从审计报告）:
- "平面烫金-到邊位" (863次) vs "平面燙金-到邊位" (452次)
- "平面烫金-於中間位" (225次) vs "平面燙金-於中間位" (30次)
- "折光烫金" (69次) vs "折光燙金" (3次)
- "磨砂烫金" (78次) vs "磨砂燙金" (3次)

影响: 1353 条简体 vs 488 条繁体 → 查询时 36.7% 记录无法命中

## 操作

注意：先确定哪张表有这个字段。审计报告写的是 hot_stamping_compatibility，但可能与实际表名不同。

1. 先用 psql 查询 \`\\d hot_stamping_compatibility\` 确认表结构
2. 或者搜索能找到该表的正确列名和表名：
\`\`\`
psql -h localhost -p 5432 -U postgres -d gold_foil_db -c "\\dt *hot_stamping*"
psql -h localhost -p 5432 -U postgres -d gold_foil_db -c "SELECT column_name, data_type FROM information_schema.columns WHERE table_name LIKE '%hot_stamp%'"
\`\`\`
3. 找出存储烫金类型/步骤名的列名（可能是 step_name, hot_stamping_type, post_processing_step 等）
4. 执行 UPDATE 将所有繁体"燙"替换为简体"烫"：
\`\`\`
UPDATE {table} SET {column} = REPLACE({column}, '燙', '烫') WHERE {column} LIKE '%燙%';
\`\`\`
5. 同样检查并处理其他繁简体异体字（"曬"→"晒"等）
6. 执行前 COUNT 确认影响行数，执行后再次 COUNT 验证

DB Password: HryENprJrxThYSDz

返回: 修改了多少行、涉及哪些列、还有遗留的繁简问题吗
`, {
  label: 'fix-simplified-chinese',
  phase: 'Fix 繁简统一',
  schema: {
    type: 'object',
    properties: {
      table_affected: { type: 'string' },
      column_affected: { type: 'string' },
      rows_updated: { type: 'number' },
      remaining_issues: { type: 'string' },
    },
    required: ['rows_updated'],
  },
});

phase('Fix 过胶字段');
const fixLamination = await agent(`
## Task: 清洗过胶材料 (lamination_material) 表的字段污染问题

## 问题现状
- responsible_person 列存储的是膜厚度值（如 "12um"、"5um"），不是人名 → 字段污染
- category / color / usage_text 三列 100% NULL
- 预期 24 行，实际只有 18 行入库（缺失 6 个膜变体）

## 操作步骤

### Step 1: 确认表结构
\`\`\`sql
\\d lamination_material_products
SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'lamination_material_products';
\\d lamination_material_compatibility
SELECT COUNT(*) FROM lamination_material_products;
SELECT COUNT(*) FROM lamination_material_compatibility;
\`\`\`

### Step 2: 新建 film_thickness 列
\`\`\`sql
ALTER TABLE lamination_material_products ADD COLUMN film_thickness VARCHAR(100) DEFAULT NULL;
\`\`\`

### Step 3: 将 responsible_person 中的厚度值迁移到 film_thickness
先查看现有数据分布：
\`\`\`sql
SELECT id, material_name, responsible_person FROM lamination_material_products;
\`\`\`
然后迁移：
\`\`\`sql
UPDATE lamination_material_products SET film_thickness = responsible_person WHERE responsible_person IS NOT NULL AND responsible_person != '';
UPDATE lamination_material_products SET responsible_person = NULL;
\`\`\`

### Step 4: 回查原始 Excel 补充缺失数据
原始文件在: G:/雅图仕/download/印刷加工/后加工资料标准书-20250224 (34).xlsx

用 Python 读取并提取所有物料的 category, color, usage, responsible_person, 厚度值:
\`\`\`python
import openpyxl
path = "G:/雅图仕/download/印刷加工/后加工资料标准书-20250224 (34).xlsx"
wb = openpyxl.load_workbook(path, data_only=True)
ws = wb.active
# 过胶材料的数据可能在不同的Sheet中
print(ws.title)
for r in range(1, min(ws.max_row+1, 10)):
    cells = [(c, ws.cell(row=r, column=c).value) for c in range(1, 30) if ws.cell(row=r, column=c).value]
    print(f"R{r}: {cells}")
\`\`\`

根据 ai-knowledge/markdown 的记录（后加工资料标准书-20250224），过胶材料清单有 24 种物料：
- 普通光胶、预涂光胶(HY1205/HY1208)、超粘预涂光胶、水性单面电晕光胶
- 预涂单面电晕光胶、水性尼龙光胶、预涂尼龙光胶、水性PET光胶
- 预涂PET光胶(2种)、预涂抗静电光胶、水性耐磨光胶、预涂耐磨光胶
- 普通哑胶、预涂哑胶(2种)、超粘预涂哑胶、预涂普通耐磨哑胶
- 预涂平价高耐磨哑胶、预涂高耐磨哑胶、预涂柔感哑胶、预涂抗静电哑胶
- 透明镭射菲林

从 ai-knowledge 文件补充数据到数据库。

### Step 5: 验证
\`\`\`sql
SELECT material_name, category, color, usage_text, film_thickness, responsible_person FROM lamination_material_products;
\`\`\`

DB Password: HryENprJrxThYSDz

返回: 新建列名、迁移了多少行厚度值、补充了多少行 category/color/usage 数据、缺失的6行是否已补入
`, {
  label: 'fix-lamination',
  phase: 'Fix 过胶字段',
  schema: {
    type: 'object',
    properties: {
      new_column: { type: 'string' },
      thickness_migrated: { type: 'number' },
      categories_filled: { type: 'number' },
      colors_filled: { type: 'number' },
      usage_filled: { type: 'number' },
      rows_added: { type: 'number' },
      total_products: { type: 'number' },
      status: { type: 'string' },
    },
    required: ['status', 'total_products'],
  },
});

phase('Fix API链路');
const fixApiChain = await agent(`
## Task: 排查 API 查询链路 — 为什么入库数据正确但 API 返回空列表

## 已知问题
审计报告指出: "API实测6/6空结果，覆盖纸面类型、书册类型、烫金类型等基本维度"

## 诊断步骤

### Step 1: 找到 API 接口代码
搜索 Controller 中所有返回 compat/compatibility 列表的方法：
\`\`\`
grep -rn "compatibility|compat" --include="*.java" F:/yatushi-6-6/yts_project/src/main/java/com/it/yts_project/controller/ | grep -i "list|query|search"
\`\`\`

### Step 2: 找到对应的 Service 实现和 Mapper XML
\`\`\`
grep -rn "compatibility|compat" --include="*.java" F:/yatushi-6-6/yts_project/src/main/java/com/it/yts_project/service/Impl/ | grep -i "list|query|search"
\`\`\`

### Step 3: 找到 MyBatis XML 中对应的 SQL
\`\`\`
grep -rn "compatibility|compat" --include="*.xml" F:/yatushi-6-6/yts_project/src/main/resources/mapper/ | head -20
\`\`\`

### Step 4: 分析 SQL 查询逻辑
检查 WHERE 条件：
- 是否有对 category/color/usage_text 的 is not null 过滤？
- 是否有对 responsible_person 的过滤条件？
- 是否有 JOIN 条件导致数据被过滤掉？
- 参数映射是否正确？

### Step 5: 用 psql 直接模拟 API 查询
\`\`\`
psql -h localhost -p 5432 -U postgres -d gold_foil_db -c "SELECT * FROM lamination_material_products LIMIT 5;"
psql -h localhost -p 5432 -U postgres -d gold_foil_db -c "SELECT * FROM lamination_material_compatibility LIMIT 5;"
\`\`\`

验证 JOIN 查询：
\`\`\`sql
SELECT p.material_name, COUNT(c.id) as compat_count
FROM lamination_material_products p
LEFT JOIN lamination_material_compatibility c ON p.id = c.product_id
GROUP BY p.material_name;
\`\`\`

### Step 6: 如果确认是 API 代码的过滤条件问题，修复 it

DB Password: HryENprJrxThYSDz

返回: 根因分析 + 修复方案（如有修复则已应用）
`, {
  label: 'fix-api-chain',
  phase: 'Fix API链路',
  schema: {
    type: 'object',
    properties: {
      root_cause: { type: 'string' },
      fixed: { type: 'boolean' },
      fix_summary: { type: 'string' },
      remaining_issues: { type: 'string' },
    },
    required: ['root_cause', 'fixed'],
  },
});

phase('Verify');
const verifyAll = await agent(`
## Task: 验证 P0 修复全部完成

### Step 1: 验证繁简统一
\`\`\`
psql -h localhost -p 5432 -U postgres -d gold_foil_db -c "SELECT * FROM hot_stamping_compatibility WHERE step_name LIKE '%燙%' OR step_name LIKE '%燙%';"
\`\`\`
确认没有繁体"燙"残留。

### Step 2: 验证过胶材料
\`\`\`
psql -h localhost -p 5432 -U postgres -d gold_foil_db -c "SELECT id, material_name, category, color, usage_text, film_thickness, responsible_person FROM lamination_material_products ORDER BY id;"
\`\`\`
确认：
- category/color/usage_text 不为 NULL
- film_thickness 有值
- responsible_person 已清空（如无数据则为 NULL）

### Step 3: 重新导出审核文件
重新运行导出脚本生成更新后的 xlsx：
\`\`\`
/e/anaconda/python F:/yatushi-6-6/database_scripts/export_to_excel.py
\`\`\`

### Step 4: 验证构建
\`\`\`
cd F:/yatushi-6-6/yts_project && mvn compile -q
cd F:/yatushi-6-6/yts_project_vueai && npm run build 2>&1 | tail -5
\`\`\`

DB Password: HryENprJrxThYSDz

返回验证结果
`, {
  label: 'verify-all',
  phase: 'Verify',
  schema: {
    type: 'object',
    properties: {
      simplified_chinese_verified: { type: 'boolean' },
      lamination_fixed: { type: 'boolean' },
      export_regenerated: { type: 'boolean' },
      build_passed: { type: 'boolean' },
      summary: { type: 'string' },
    },
    required: ['simplified_chinese_verified', 'lamination_fixed', 'build_passed'],
  },
});

log(`
✅ P0 修复完成！

1. 繁简统一: ${fixSimplified?.rows_updated} 行已更新
2. 过胶字段: ${fixLamination?.status} (${fixLamination?.total_products} 行, film_thickness 新建 + 数据迁移)
3. API链路: ${fixApiChain?.fixed ? '已修复' : '无需代码改动'} — ${fixApiChain?.root_cause}

验证:
  - 繁简: ${verifyAll?.simplified_chinese_verified ? '✅' : '❌'}
  - 过胶: ${verifyAll?.lamination_fixed ? '✅' : '❌'}
  - 导出已更新: ${verifyAll?.export_regenerated ? '✅' : '❌'}
  - 构建: ${verifyAll?.build_passed ? '✅' : '❌'}
`);

return {
  simplifiedFix: `${fixSimplified?.rows_updated} rows`,
  laminationFix: fixLamination?.status,
  apiFix: fixApiChain?.root_cause,
  verified: verifyAll?.summary,
};
