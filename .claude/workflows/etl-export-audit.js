export const meta = {
  name: 'etl-export-audit',
  description: 'Phase 1.5: 数据库数据导出为 Excel — 每模块产品列表 + 兼容性矩阵，供工厂技术人员审核',
  phases: [
    { title: 'Read schema', detail: '获取数据库表结构' },
    { title: 'Write exporter', detail: '创建 Excel 导出脚本' },
    { title: 'Export all', detail: '逐模块导出 xlsx' },
    { title: 'Inventory', detail: '文件清单 & 验证' },
  ],
}

phase('Read schema');
const schemaInfo = await agent(`
Connect to PostgreSQL gold_foil_db and get the schema for ALL 15 P0 module tables:
- All silicone_*_product and silicone_*_compatibility tables (7+7=14 tables)
- uv_oil_matte_product and uv_oil_matte_compatibility
- water_varnish_matte_product and water_varnish_matte_compatibility
- yaguang_uv_oil_product and yaguang_uv_oil_compatibility
- lamination_material_products and lamination_material_compatibility

Also check if reference tables (reference_coating_surface, reference_ink_surface, etc.) exist and their schemas.

For each table, return:
1. Table name
2. Column names and types
3. Row count
4. Any foreign key relationships

Use psql: psql -h localhost -p 5432 -U postgres -d gold_foil_db -c "SQL"

Password: HryENprJrxThYSDz
`, {
  label: 'read-db-schema',
  phase: 'Read schema',
  schema: {
    type: 'object',
    properties: {
      product_tables: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            table_name: { type: 'string' },
            columns: { type: 'array', items: { type: 'string' } },
            row_count: { type: 'number' },
          },
          required: ['table_name', 'columns', 'row_count'],
        },
      },
      compat_tables: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            table_name: { type: 'string' },
            columns: { type: 'array', items: { type: 'string' } },
            row_count: { type: 'number' },
          },
          required: ['table_name', 'columns', 'row_count'],
        },
      },
    },
    required: ['product_tables', 'compat_tables'],
  },
});

phase('Write exporter');
const exporterScript = await agent(`
Write a Python script to F:/yatushi-6-6/database_scripts/export_to_excel.py

## Purpose
Export ALL P0 module data from PostgreSQL to individual Excel files, one workbook per module.
Each workbook has 2 sheets:
- Sheet 1 "产品列表": Product master data (material_code, material_name, usage, category, color, etc.)
- Sheet 2 "兼容性矩阵": Compatibility matrix in wide format (rows=products, columns=step_names, cells=V/X)

## Output directory
F:/yatushi-6-6/审核导出/

## File naming
Each module gets its own xlsx: \`{category_key}_审核.xlsx\` (e.g. \`uv_oil_matte_审核.xlsx\`)
Also generate a summary file: \`00_汇总清单.xlsx\` with one row per module (module name, product count, compat count, status)

## DB connection
Host: localhost:5432
Database: gold_foil_db
User: postgres
Password: HryENprJrxThYSDz

## Module list (15 modules)

### Silicone (7 with actual data tables):
- silicone_glow_ink
- silicone_white_uv
- silicone_glittering_star
- silicone_screen_uv
- silicone_led_uv_spray
- silicone_water_base_transparent

### Silicone (7 empty/模板 but tables exist):
- silicone_coarse_uv
- silicone_sandblast_uv
- silicone_wrinkle_uv
- silicone_orange_peel_uv
- silicone_watercolor_ink
- silicone_mica_pearl
- silicone_post_process

### UV Ink (2):
- uv_oil_matte
- water_varnish_matte

### Lamination (1):
- lamination_material

### Yaguang UV Oil (1):
- yaguang_uv_oil

## Important technical notes

### Pivot the compatibility table
The compatibility table stores data as:
  product_id, step_name, compatibility_status
We need to PIVOT this to wide format:
  material_code, material_name, step_1, step_2, ..., step_N
Each product becomes ONE row, step names become column headers, status values fill the cells.

Use pandas.DataFrame.pivot_table() or manual dict mapping.

### Special handling for empty modules
If a module has 0 products, still create the xlsx with:
- Sheet 1 "产品列表": Empty with just headers, row in summary says "(空)"
- Sheet 2: Skip or create with just a note

### Chinese column headers
Sheet 1 columns should use Chinese names:
- material_code → 物料编号
- material_name → 物料名称
- usage_text → 用途
- category → 性质
- color → 颜色
- responsible_person → 测试员
- stock_code → 库存编号
- supplier_code → 供应商编号

### Matrix formatting
- Use openpyxl (already available) to apply basic formatting
- Header row bold, auto-fit column widths (approximate)
- V cells light green background, X cells light red background (optional but nice)
- Freeze top row and first 2 columns

### The \`lamination_material\` module
Table names: lamination_material_products, lamination_material_compatibility
Same product+compat structure.

### Validation after export
- Count rows per sheet match DB row counts
- All 15 modules have an xlsx file
- Write validation results to \`00_审核导出报告.md\`
`, {
  label: 'write-exporter',
  phase: 'Write exporter',
  schema: {
    type: 'object',
    properties: {
      file_path: { type: 'string' },
      summary: { type: 'string' },
    },
    required: ['file_path'],
  },
});

phase('Export all');
const exportResult = await agent(`
Run the export script:

\`\`\`
/e/anaconda/python F:/yatushi-6-6/database_scripts/export_to_excel.py
\`\`\`

Report:
1. How many xlsx files were created
2. For each module: product count, compat count, file size
3. Any errors or warnings
4. Path to each generated file
5. Read the generated \`00_审核导出报告.md\` content if it exists
`, {
  label: 'run-export',
  phase: 'Export all',
  schema: {
    type: 'object',
    properties: {
      total_files: { type: 'number' },
      modules: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            name: { type: 'string' },
            products: { type: 'number' },
            compat_pairs: { type: 'number' },
            file_size_kb: { type: 'number' },
            file_path: { type: 'string' },
          },
          required: ['name', 'products', 'file_path'],
        },
      },
      report_path: { type: 'string' },
      errors: { type: 'string' },
    },
    required: ['total_files', 'modules'],
  },
});

phase('Inventory');
const inventory = await agent(`
List the exported files and create an inventory:

1. List all files in F:/yatushi-6-6/审核导出/ with sizes
2. Read the 00_审核导出报告.md
3. Check if the validation row counts match what Phase 1 imported:
   - Total products should be ~90
   - Total compat pairs should be ~6460

Report a concise inventory table.
`, {
  label: 'inventory-files',
  phase: 'Inventory',
  schema: {
    type: 'object',
    properties: {
      files: {
        type: 'array',
        items: {
          type: 'object',
          properties: {
            filename: { type: 'string' },
            size_kb: { type: 'number' },
          },
          required: ['filename'],
        },
      },
      total_products_exported: { type: 'number' },
      total_compat_exported: { type: 'number' },
      report_content: { type: 'string' },
    },
    required: ['files', 'total_products_exported'],
  },
});

log(`
✅ 审计导出完成！

共 ${exportResult?.total_files} 个 xlsx 文件在 F:/yatushi-6-6/审核导出/
总产品数: ${inventory?.total_products_exported}
总兼容性记录: ${inventory?.total_compat_exported}

工厂技术人员直接打开 \`F:/yatushi-6-6/审核导出/\` 目录下的 xlsx 文件即可审阅。
每模块包含：
  - Sheet 1: 产品列表（物料编号、名称、用途等）
  - Sheet 2: 兼容性矩阵（V/X 矩阵，跟原始文件格式一致）
`);

return {
  exportDir: 'F:/yatushi-6-6/审核导出/',
  totalFiles: exportResult?.total_files,
  totalProducts: inventory?.total_products_exported,
  totalCompatPairs: inventory?.total_compat_exported,
  reportFile: exportResult?.report_path,
  modules: exportResult?.modules,
};
