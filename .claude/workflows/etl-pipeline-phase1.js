export const meta = {
  name: 'etl-pipeline-phase1',
  description: 'Phase 1: 写 p0_etl.py + 修 Mapper XML 反引号 → 执行 Type A 入库',
  phases: [
    { title: 'Read references', detail: '读现有代码作为参考' },
    { title: 'Write p0_etl.py', detail: '创建统一 Type A 解析器' },
    { title: 'Fix Mapper XML', detail: '修正 5 个反引号' },
    { title: 'Test', detail: '--dry-run 验证 uv_oil_matte' },
    { title: 'Import', detail: '全量导入' },
  ],
}

phase('Read references');
// Read the existing import_p0_data.py for pattern reference
const refFiles = await agent(`
Read these reference files and return their content:

1. F:/yatushi-6-6/database_scripts/p0_etl.py — existing ETL script (full content)
2. F:/yatushi-6-6/yts_project/src/main/resources/mapper/SiliconeGlowInkMapper.xml — example mapper XML to understand column mapping
3. F:/yatushi-6-6/yts_project/src/main/java/com/it/yts_project/model/SiliconeGlowInkProduct.java — example model to understand field names
4. F:/yatushi-6-6/yts_project/src/main/java/com/it/yts_project/model/SiliconeGlowInkCompatibility.java — example compat model
5. F:/yatushi-6-6/CLAUDE.md — for DB connection info

Return ALL content verbatim. Do NOT summarize.
`, {
  label: 'read-reference-files',
  phase: 'Read references',
  schema: {
    type: 'object',
    properties: {
      import_p0_data_py: { type: 'string', description: 'Full content of import_p0_data.py' },
      mapper_xml: { type: 'string', description: 'Full content of SiliconeGlowInkMapper.xml' },
      model_java: { type: 'string', description: 'Full content of SiliconeGlowInkProduct.java' },
      compat_model_java: { type: 'string', description: 'Full content of SiliconeGlowInkCompatibility.java' },
      claude_md: { type: 'string', description: 'Full content of project CLAUDE.md' },
    },
    required: ['import_p0_data_py', 'mapper_xml', 'model_java', 'compat_model_java', 'claude_md'],
  },
});

phase('Write p0_etl.py');
const etlScript = await agent(`
Write a Python ETL script to F:/yatushi-6-6/database_scripts/p0_etl.py

## Context

This script parses Type A xlsx files (新物料適用性指引書 template) from G: drive and imports into PostgreSQL gold_foil_db.

## Key facts from real file audit

- Source dir: G:/雅图仕/download/
- 15 P0 categories total, only 9 have data rows (others need empty tables for CRUD)
- Silicone: glow_ink(2), white_uv(2), glittering_star(7), screen_uv(5), led_uv_spray(1), water_base_transparent(1)
- UV ink: uv_oil_matte(12), water_varnish_matte(36)
- Lamination: lamination_material(24) — from 印刷加工/
- Step headers: rows 18-24, 80-95 steps, max col 109-123
- 3-level headers: row-2=group, row-1=subcategory, row=step name
- Multi-line cells: use \\\\n separators, normalize to " / "
- Compat values: V/X/▷ are real status; other text values are category labels (skip)
- Data rows: col6 starts with # (primary), fallback: col6 matches pattern like UV-OS-170302
- col9 must be non-empty for data rows
- Merged cells: 67-227 per file, openpyxl returns None for non-top-left

## Reference patterns

From import_p0_data.py (already read):
- CATEGORIES OrderedDict structure
- find_step_header_row() scans cols 41-99
- find_data_rows() checks col6 starts with #
- extract_product_from_row() with fixed col mapping
- extract_compatibility_pairs() reads cols 41+
- dedup by material_code first line

## DB Connection

Host: localhost:5432, DB: gold_foil_db, User: postgres, Password: HryENprJrxThYSDz
Use psycopg2 (available in Anaconda) or subprocess psql.

## Script structure — MUST include:

### 1. CATEGORIES OrderedDict (15 entries)

Each entry: key = "silicone_glow_ink", value = {dir, glob, data_count, product_table, compat_table, product_ddl, compat_ddl}

Only SILICONE categories need product+compat tables. The lamination_material category is different (not product+compat — just a reference table).

### 2. PG DDL auto-creation

\`\`\`sql
CREATE TABLE IF NOT EXISTS {product_table} (
    id SERIAL PRIMARY KEY,
    material_code VARCHAR(100) NOT NULL,
    supplier_code VARCHAR(100) DEFAULT NULL,
    stock_code VARCHAR(100) DEFAULT NULL,
    material_name VARCHAR(500) NOT NULL,
    usage_text VARCHAR(500) DEFAULT NULL,
    category VARCHAR(200) DEFAULT NULL,
    color VARCHAR(200) DEFAULT NULL,
    responsible_person VARCHAR(200) DEFAULT NULL,
    remark TEXT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS {compat_table} (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES {product_table}(id),
    step_name VARCHAR(500) NOT NULL,
    compatibility_status VARCHAR(10) NOT NULL,
    remark TEXT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
\`\`\`

### 3. Parser functions

- find_step_header(ws) → int (best row)
- find_data_rows(ws, step_row) → list of dict {row, code, name, is_primary}
- extract_product(ws, row_num) → dict of field->value
- extract_compatibility(ws, row_num, step_row) → list of (step_name, status)

### 4. CLI args

- --dry-run: print what would be done
- --category X: only process one category
- --validate: run SELECT COUNT(*) on all tables
- --create-tables-only: create tables without importing

### 5. Error handling

- Skip files that don't match the glob pattern
- Handle merged cells gracefully (skip None values from merged ranges)
- Log progress with timestamps
- Count inserted/duplicate/skipped rows

## Important

- Column name: usage_text (NOT "usage" — PG doesn't reserve it but keeping the convention safer)
- Multi-line cells: str(v).strip().replace('\\\\n', ' / ')
- Only V/X/▷ count as compatibility status; anything else is descriptive text (skip)
- PG password is: HryENprJrxThYSDz
- Use psycopg2.connect() or subprocess with psql
`, {
  label: 'write-p0-etl',
  phase: 'Write p0_etl.py',
  schema: {
    type: 'object',
    properties: {
      file_path: { type: 'string', description: 'Path where the file was written' },
      summary: { type: 'string', description: 'Brief summary of what was created' },
    },
    required: ['file_path'],
  },
});

phase('Fix Mapper XML');
const mapperFix = await agent(`
Fix 5 MyBatis Mapper XML files that use MySQL backtick syntax for the \`usage\` column.
PostgreSQL doesn't support backtick quoting — it uses double quotes or (better) no quotes at all since "usage" is not a reserved word in PG.

Files to fix (change \`\`\`usage\`\` to just \`usage\`):
1. F:/yatushi-6-6/yts_project/src/main/resources/mapper/SiliconeGlowInkMapper.xml
2. F:/yatushi-6-6/yts_project/src/main/resources/mapper/SiliconeWatercolorInkMapper.xml
3. F:/yatushi-6-6/yts_project/src/main/resources/mapper/UvOilMatteMapper.xml
4. F:/yatushi-6-6/yts_project/src/main/resources/mapper/WaterVarnishMatteMapper.xml
5. F:/yatushi-6-6/yts_project/src/main/resources/mapper/YaguangUvOilMapper.xml

Read each file first, then use Edit tool to replace all occurrences of \`\`\`usage\`\` (backtick-usage-backtick) with just \`usage\` (no backticks).

Report which files were changed and how many occurrences in each.
`, {
  label: 'fix-mapper-xml',
  phase: 'Fix Mapper XML',
  schema: {
    type: 'object',
    properties: {
      files_fixed: {
        type: 'array',
        items: { type: 'string' },
        description: 'List of files that were fixed',
      },
      total_occurrences: {
        type: 'number',
        description: 'Total backtick occurrences fixed across all files',
      },
    },
    required: ['files_fixed', 'total_occurrences'],
  },
});

phase('Test');
// Test on uv_oil_matte first
const testResult = await agent(`
Run the p0_etl.py script on a single module to verify it works:

\`\`\`
/e/anaconda/python F:/yatushi-6-6/database_scripts/p0_etl.py --category uv_oil_matte --dry-run
\`\`\`

Report:
1. Whether the script ran without errors
2. How many data rows were found (expected: 12)
3. How many steps were detected (expected: 83)
4. How many compatibility pairs would be inserted
5. Any warnings or anomalies
`, {
  label: 'test-uv-oil-matte',
  phase: 'Test',
  schema: {
    type: 'object',
    properties: {
      success: { type: 'boolean' },
      data_rows_found: { type: 'number' },
      steps_found: { type: 'number' },
      compat_pairs: { type: 'number' },
      errors: { type: 'string' },
      warnings: { type: 'string' },
    },
    required: ['success'],
  },
});

phase('Import');
const importResult = await agent(`
Run the full p0_etl.py import:

Step 1: Create all tables first:
\`\`\`
/e/anaconda/python F:/yatushi-6-6/database_scripts/p0_etl.py --create-tables-only
\`\`\`

Step 2: Import each category with data:
\`\`\`
/e/anaconda/python F:/yatushi-6-6/database_scripts/p0_etl.py --category uv_oil_matte
/e/anaconda/python F:/yatushi-6-6/database_scripts/p0_etl.py --category water_varnish_matte
/e/anaconda/python F:/yatushi-6-6/database_scripts/p0_etl.py --category lamination_material
# ... silicone categories
\`\`\`

Step 3: Validate:
\`\`\`
/e/anaconda/python F:/yatushi-6-6/database_scripts/p0_etl.py --validate
\`\`\`

Report per-category row counts and any errors.
`, {
  label: 'run-import',
  phase: 'Import',
  schema: {
    type: 'object',
    properties: {
      tables_created: { type: 'number' },
      categories_imported: { type: 'number' },
      total_products: { type: 'number' },
      total_compat_pairs: { type: 'number' },
      validation: {
        type: 'object',
        description: 'Per-module row counts from --validate',
      },
      errors: { type: 'string' },
    },
    required: ['tables_created', 'total_products'],
  },
});

log(`
✅ Phase 1 完成!
  - p0_etl.py created at: ${etlScript?.file_path}
  - Mapper XML fixes: ${mapperFix?.total_occurrences} backticks removed in ${mapperFix?.files_fixed?.length} files
  - Dry-run (uv_oil_matte): ${testResult?.success ? 'PASS' : 'FAIL'} (${testResult?.data_rows_found} rows / ${testResult?.steps_found} steps)
  - Full import: ${importResult?.total_products} products across ${importResult?.categories_imported} categories
`);

return {
  etlScript: etlScript?.file_path,
  mapperFixes: mapperFix?.files_fixed,
  testPassed: testResult?.success,
  importStats: {
    totalProducts: importResult?.total_products,
    totalCompatPairs: importResult?.total_compat_pairs,
    categories: importResult?.categories_imported,
    validation: importResult?.validation,
  },
};
