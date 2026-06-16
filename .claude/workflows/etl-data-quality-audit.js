export const meta = {
  name: 'etl-data-quality-audit',
  description: '全面数据质量审计：重复/冲突/缺失/异常检测 + 可复用模块架构设计',
  phases: [
    { title: 'DB deep scan', detail: '全库数据质量扫描' },
    { title: 'Duplicate analysis', detail: '重复物料编号 + 兼容性冲突' },
    { title: 'Gap analysis', detail: '缺失/异常检测' },
    { title: 'Cross-module check', detail: '跨模块一致性 + 原始文件对照' },
    { title: 'Synthesize report', detail: '汇总报告 + 可复用架构设计' },
  ],
}

phase('DB deep scan');
const deepScan = await agent(`
Run comprehensive data quality checks on gold_foil_db.

## DB Connection
psql -h localhost -p 5432 -U postgres -d gold_foil_db
Password: HryENprJrxThYSDz

## ALL product tables (15 modules):
silicone_glow_ink_product, silicone_white_uv_product, silicone_glittering_star_product,
silicone_screen_uv_product, silicone_led_uv_spray_product, silicone_water_base_transparent_product,
silicone_coarse_uv_product, silicone_sandblast_uv_product, silicone_wrinkle_uv_product,
silicone_orange_peel_uv_product, silicone_watercolor_ink_product, silicone_mica_pearl_product,
silicone_post_process_product, uv_oil_matte_product, water_varnish_matte_product,
lamination_material_products, yaguang_uv_oil_product

## ALL compatibility tables:
Same pattern: silicone_*_compatibility, uv_oil_matte_compatibility, water_varnish_matte_compatibility,
lamination_material_compatibility, yaguang_uv_oil_compatibility

## Run these queries and capture ALL results:

### 1. Cross-module duplicate material codes
\`\`\`sql
SELECT material_code, material_name, string_agg(table_name, ', ') as in_tables, count(*) as occurrences
FROM (
  SELECT material_code, material_name, 'silicone_glow_ink_product' as table_name FROM silicone_glow_ink_product
  UNION ALL SELECT material_code, material_name, 'silicone_white_uv_product' FROM silicone_white_uv_product
  UNION ALL SELECT material_code, material_name, 'silicone_glittering_star_product' FROM silicone_glittering_star_product
  -- ... ALL modules
) sub
WHERE material_code IS NOT NULL AND material_code != ''
GROUP BY material_code, material_name
HAVING count(*) > 1
ORDER BY occurrences DESC;
\`\`\`

### 2. Duplicate material_code within same table
For each product table: find material_code that appears more than once.

### 3. Compatibility conflict (same material+step, different status)
For each compat table: find entries where same product_id+step_name has conflicting V/X values.

### 4. Orphan compatibility records
Compat records referencing product_id that doesn't exist in the parent table.

### 5. Products with ZERO compatibility records
Products with no entries in the corresponding compatibility table.

### 6. Step coverage analysis
For each module: how many unique step names exist, and what % of products have a V for each step.

### 7. Null analysis per table
For each product table: count NULLs per column, flag columns with >50% null rate.

Return ALL results as structured data.
`, {
  label: 'db-deep-scan',
  phase: 'DB deep scan',
  schema: {
    type: 'object',
    properties: {
      cross_module_duplicates: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            material_code: { type: 'string' },
            material_name: { type: 'string' },
            in_tables: { type: 'string' },
            occurrences: { type: 'number' },
          },
          required: ['material_code'],
        },
      },
      internal_duplicates: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            table_name: { type: 'string' },
            material_code: { type: 'string' },
            count: { type: 'number' },
          },
          required: ['table_name', 'material_code'],
        },
      },
      compat_conflicts: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            table_name: { type: 'string' },
            product_id: { type: 'number' },
            step_name: { type: 'string' },
            statuses: { type: 'string' },
          },
          required: ['table_name'],
        },
      },
      orphan_records: {
        type: 'array',
        items: { type: 'object' },
      },
      products_without_compat: {
        type: 'array',
        items: { type: 'object' },
      },
      null_analysis: {
        type: 'array',
        items: { type: 'object' },
      },
    },
    required: ['cross_module_duplicates', 'internal_duplicates', 'null_analysis'],
  },
});

phase('Duplicate analysis');
const duplicateAnalysis = await agent(`
Given the deep scan results, analyze duplicates more deeply.

## Context from deep scan:
${JSON.stringify(deepScan, null, 2)}

## Tasks:

### A. For cross-module duplicates:
For each material_code that appears in multiple modules:
1. Query the actual row data from each table to compare material_name, usage_text, category, color
2. Are these actually the SAME material, or different materials with same code?
3. Flag as TRUE_DUPLICATE (should merge) or SAME_CODE_DIFFERENT_MEANING (keep separate)

### B. For compat conflicts:
For each conflict found:
1. Print the actual conflicting rows (both V and X for same product+step)
2. Check if there's a legitimate reason (same product_id? or are they different products with same id?)
3. Determine if this is a real data bug or an import logic issue

### C. Dedup recommendation:
For each duplicate case, recommend:
- MERGE: combine into one entry, keep all compat data
- REMOVE: delete the duplicate row
- KEEP_BOTH: intentionally separate

Use psql to run queries. Return structured results with your verdict for each case.
`, {
  label: 'duplicate-analysis',
  phase: 'Duplicate analysis',
  schema: {
    type: 'object',
    properties: {
      cross_module_verdicts: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            material_code: { type: 'string' },
            verdict: { type: 'string', enum: ['MERGE', 'REMOVE', 'KEEP_BOTH'] },
            reason: { type: 'string' },
          },
          required: ['material_code', 'verdict'],
        },
      },
      compat_conflict_details: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            module: { type: 'string' },
            product_id: { type: 'number' },
            step_name: { type: 'string' },
            issue: { type: 'string' },
          },
          required: ['module'],
        },
      },
      dedup_summary: {
        type: 'object',
        properties: {
          total_duplicates: { type: 'number' },
          to_merge: { type: 'number' },
          to_remove: { type: 'number' },
          to_keep: { type: 'number' },
        },
        required: ['total_duplicates'],
      },
    },
    required: ['cross_module_verdicts', 'dedup_summary'],
  },
});

phase('Gap analysis');
const gapAnalysis = await agent(`
Run gap/missing data analysis on gold_foil_db.

## Connect
psql -h localhost -p 5432 -U postgres -d gold_foil_db
Password: HryENprJrxThYSDz

## A. Compare DB row counts vs Phase 0 audit expected counts

Phase 0 audit said:
- uv_oil_matte: 12 → now in DB: ? (query it)
- water_varnish_matte: 36 → in DB: ?
- silicone_glow_ink: 2 → in DB: ?
- silicone_glittering_star: 7 → in DB: ?
- silicone_screen_uv: 5 → in DB: ?
- silicone_white_uv: 2 → in DB: ?
- silicone_led_uv_spray: 1 → in DB: ?
- silicone_water_base_transparent: 1 → in DB: ?
- lamination_material: 24 → in DB: ?

For any table where DB count ≠ audit expected, investigate why. Check:
- Did the parser pick up more/fewer rows than expected?
- Are there rows with non-standard col6 values that the parser found but manual audit missed?
- Or vice versa?

## B. Null rate deep dive
For each product table, report:
- % of materials with NULL usage_text
- % of materials with NULL category
- % of materials with NULL color
- % of materials with NULL responsible_person

## C. Field value analysis
- List ALL unique values of \`category\` across all modules
- List ALL unique values of \`usage_text\` (first 50 chars) to see if they're meaningful
- Any obviously wrong values? (placeholders, "N/A", "/", etc.)

## D. Legacy MySQL tables
Check if there are any old MySQL-style tables still in the DB (with AUTO_INCREMENT, ENGINE=InnoDB, etc.)

Return all results.
`, {
  label: 'gap-analysis',
  phase: 'Gap analysis',
  schema: {
    type: 'object',
    properties: {
      count_diffs: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            module: { type: 'string' },
            audit_expected: { type: 'number' },
            db_actual: { type: 'number' },
            diff: { type: 'number' },
            reason: { type: 'string' },
          },
          required: ['module', 'audit_expected', 'db_actual'],
        },
      },
      high_null_fields: {
        type: 'array',
        items: { type: 'object' },
      },
      category_values: { type: 'array', items: { type: 'string' } },
      issues: { type: 'array', items: { type: 'string' } },
    },
    required: ['count_diffs', 'issues'],
  },
});

phase('Cross-module check');
const crossModuleCheck = await agent(`
Run cross-module consistency checks.

## Connect
psql -h localhost -p 5432 -U postgres -d gold_foil_db
Password: HryENprJrxThYSDz

## A. Step name normalization check
For each compatibility table, list the first 10 step names.
Compare step names across modules. Are they consistent?
(e.g. does "印刷普通油墨" appear with same spelling in multiple modules?)
NOTE: Chinese characters may have encoding differences. Print the raw hex bytes for any suspicious ones.

## B. Material code format consistency
For each product table, check if ALL material_codes follow expected format:
- Silicone products: should start with CS-, SS-, etc.
- UV oil: should start with UV-OS-, OS-, VN-, etc.
- Water varnish: VN-, UV-OS-, CS-, etc.
Flag any codes that don't match expected pattern.

## C. V/X distribution across modules
For each module, compute:
- Total products
- Total compat pairs
- Avg V per product
- Avg X per product
- V% rate
Flag any module with abnormal distribution (e.g. >80% V or >80% X).

## D. Step count consistency
For each module, count unique step names. Are they all roughly the same?
The audit found steps 79-118. Which modules are outliers?

Return all results.
`, {
  label: 'cross-module-check',
  phase: 'Cross-module check',
  schema: {
    type: 'object',
    properties: {
      step_name_issues: {
        type: 'array',
        items: { type: 'object' },
      },
      material_code_issues: {
        type: 'array',
        items: { type: 'object' },
      },
      vx_distribution: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            module: { type: 'string' },
            product_count: { type: 'number' },
            compat_pairs: { type: 'number' },
            v_pct: { type: 'number' },
            x_pct: { type: 'number' },
            flag: { type: 'string' },
          },
          required: ['module'],
        },
      },
      outliers: {
        type: 'array',
        items: { type: 'string' },
      },
    },
    required: ['vx_distribution', 'outliers'],
  },
});

phase('Synthesize report');
const finalReport = await agent(`
## Context from all previous phases:

### Deep scan results
${JSON.stringify(deepScan, null, 2).slice(0, 3000)}

### Duplicate analysis
${JSON.stringify(duplicateAnalysis, null, 2).slice(0, 2000)}

### Gap analysis
${JSON.stringify(gapAnalysis, null, 2).slice(0, 2000)}

### Cross-module check
${JSON.stringify(crossModuleCheck, null, 2).slice(0, 2000)}

## Task A: Write a comprehensive audit report

Write the audit report to F:/YTS+JYY/审核导出/01_数据质量审计报告.md

Structure:
1. **Executive Summary** (1 paragraph: overall data health)
2. **Duplicate Analysis** (table: what was found, verdicts, actions needed)
3. **Gap Analysis** (table: expected vs actual, missing fields)
4. **Cross-module Consistency** (V/X distribution, step name issues, code format)
5. **Critical Issues** (must-fix before proceeding)
6. **Recommended Fixes** (actionable items with priority: P0/P1/P2)
7. **Data Health Score** (A/B/C/D rating per module)

## Task B: Design reusable module architecture

Write the architecture doc to F:/YTS+JYY/审核导出/02_可复用资料模块架构.md

This should be a design doc for a generic "印刷厂资料管理模块" that can handle any factory document type:

1. **Core data model** (Generic enough for all document types)
   - Document template registry (Type A/B/C/D registration)
   - Generic material + compatibility model
   - Template-specific field extensions

2. **Pipeline architecture**
   - Upload → Parse → Validate → Preview → Import
   - Per-template parser plugins
   - Common validation rules engine

3. **Web UI design**
   - Template management dashboard
   - Upload wizard (step-by-step)
   - Data preview & edit before commit
   - Audit log & versioning

4. **Quality assurance**
   - Automatic duplicate detection on upload
   - Cross-reference with existing data
   - Approval workflow before data goes live

5. **Current tech stack mapping**
   - Python (openpyxl/pandas) → ETL pipeline
   - PostgreSQL → data storage
   - Java Spring Boot → CRUD API
   - Vue 3 → admin frontend

Keep it pragmatic - this is for a real factory, not a theoretical system.
Focus on what's been learned from processing 50+ real factory documents.
`, {
  label: 'synthesize-report',
  phase: 'Synthesize report',
  schema: {
    type: 'object',
    properties: {
      audit_report_path: { type: 'string' },
      architecture_doc_path: { type: 'string' },
      summary: { type: 'string' },
      data_health: {
        type: 'object',
        properties: {
          overall_grade: { type: 'string' },
          critical_issues_count: { type: 'number' },
          p0_fixes: { type: 'number' },
          p1_fixes: { type: 'number' },
        },
        required: ['overall_grade'],
      },
    },
    required: ['audit_report_path', 'architecture_doc_path', 'summary'],
  },
});

log(`
✅ 全面数据质量审计完成！

报告文件：
  1. ${finalReport?.audit_report_path} — 数据质量审计报告
  2. ${finalReport?.architecture_doc_path} — 可复用资料模块架构设计

数据健康评分: ${finalReport?.data_health?.overall_grade}
关键问题: ${finalReport?.data_health?.critical_issues_count}
P0修复项: ${finalReport?.data_health?.p0_fixes}
P1修复项: ${finalReport?.data_health?.p1_fixes}

${finalReport?.summary}
`);

return {
  auditReport: finalReport?.audit_report_path,
  architectureDoc: finalReport?.architecture_doc_path,
  dataHealth: finalReport?.data_health,
  summary: finalReport?.summary,
};
