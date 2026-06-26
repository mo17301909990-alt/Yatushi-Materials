# 全品类匹配页面设计 — 输入输出审计

> 日期：2026-06-19
> 状态：待客户确认后开工

---

## 模式分类

### Pattern A：标准品（硅胶 13 类 + UV油 + 水油）

**共 15 个品类，结构完全一致**

| 品类 | 产品数 | 兼容规则数 | 数据状态 |
|------|--------|-----------|---------|
| silicone_glow_ink | 3 | 207 | ✅ |
| silicone_white_uv | 3 | 213 | ✅ |
| silicone_glittering_star | 6 | 426 | ✅ |
| silicone_screen_uv | 6 | 408 | ✅ |
| silicone_led_uv_spray | 2 | 154 | ✅ |
| silicone_water_base_transparent | 2 | 138 | ✅ |
| silicone_coarse_uv | 1 | 72 | ✅ |
| silicone_orange_peel_uv | 1 | 72 | ✅ |
| silicone_sandblast_uv | 1 | 71 | ✅ |
| silicone_wrinkle_uv | 1 | 71 | ✅ |
| silicone_watercolor_ink | 1 | 72 | ✅ |
| silicone_mica_pearl | 0 | 0 | ⚠️ 空 |
| uv_oil_matte | 13 | 923 | ✅ **pilot 已完成** |
| water_varnish_matte | 36 | 2,271 | ✅ |

**产品表字段：** material_code, supplier_code, stock_code, material_name, usage, category, color, responsible_person, min_sheet_size, max_sheet_size, min_spacing, max_spacing, design_info, applicable_interface, notes

**兼容性表字段：** product_id, post_processing_step (工序名称), compatibility_status (V/X), remark

**匹配输入：** 关键词搜索 + 后加工工序下拉筛选
**匹配输出：** 产品卡片（编码/名称/用途/类别/颜色）+ 兼容状态（V绿色/X红色）

**UI 设计建议：** UV油墨 pilot 模式可复用。每个品类独立的匹配页面，导航栏加入口。

---

### Pattern B：后加工材料（过胶膜类）

**1 个品类，字段结构不同**

| 品类 | 产品数 | 兼容规则数 | 数据状态 |
|------|--------|-----------|---------|
| lamination_material | 24 | 1,356 | ✅ |

**产品表字段：** material_code, stock_code, material_name, usage_text, material_type (BOPP/PET/PET), thickness_film (胶膜厚度), thickness_glue (胶水厚度), size_info, color, shape, responsible_person, category

**兼容性表字段：** product_id, post_processing_step, compatibility_status (V/X), remark

**匹配输入：** 关键词搜索 + 材料类型(BOPP/PET)筛选 + 后加工工序筛选
**匹配输出：** 产品卡片（编码/名称/材料类型/膜厚/胶厚/尺寸/颜色）+ 兼容状态

**与 Pattern A 差异：** 产品卡片展示字段不同（需显示 thickness_film/thickness_glue/material_type 等过胶专用字段，无 sheet_size/spacing 字段）

---

### Pattern C：烫金（已有完整系统）

**不改造，保持现状**

| 品类 | 产品数 | 兼容规则数 | 数据状态 |
|------|--------|-----------|---------|
| hot_stamping | 797 | 1,964 | ✅ **线上运行** |

**产品表字段：** name, model_number, material_number, hot_stamping_paper_type, descirption, fengdu, paizi, gaishu, danwei, status, usage_count, price (关联表)

**匹配输入：** 10 维参数（公司编号/型号/前工序/产品类型/烫金类型/图案/后加工/牌子/颜色/纸性能）
**匹配输出：** 产品列表 + 匹配度排序 + 价格

---

### LEO 纸品（未入库）

| 文件 | 产品数 | 状态 |
|------|--------|------|
| 书板贴纸-应用指引书 | 0 | ❌ ETL 未解析 |
| 平面产品贴纸-应用指引书 | 0 | ❌ ETL 未解析 |
| 非平面产品贴纸-应用指引书 | 0 | ❌ ETL 未解析 |

需要新的 xlsx 解析 handler 处理特殊格式。

---

## 实现优先级建议

| 优先级 | 品类 | 模式 | 工作量 |
|--------|------|------|--------|
| P0 | UV油墨 | A | ✅ **已完成 pilot** |
| P1 | 水油 | A | 小（复制 UV油墨 模式，换表名） |
| P1 | 硅胶 12 类 | A | 中（批量复制，12 个页面） |
| P2 | 后加工材料 | B | 中（改产品卡片模板） |
| P3 | LEO 纸品 | - | 大（先修 ETL 解析器） |

## 待确认事项

- [ ] 客户确认每个品类的搜索/筛选条件是否符合实际业务
- [ ] 客户确认产品卡片上需要展示哪些字段
- [ ] 是否所有品类都要独立页面，还是合并到统一入口
- [ ] LEO 纸品是否需要先修复 ETL 解析
