# UV油/水油 XLSX 审计报告

**生成时间**: 2026-06-19
**审计范围**: 哑光水油标准书-20231117 (6).xlsx + uv_oil_matte_product/compatibility 表
**审计方法**: xlsx 文件深度扫描 + 数据库表结构对照 + ETL 映射链路追踪

---

## 一、xlsx 文件概览

| 属性 | 值 |
|------|-----|
| 文件名 | 哑光水油标准书-20231117 (6).xlsx |
| 工作表 | 印刷過水油類 |
| 总行数 | 57 |
| 总列数 | 123（A ~ DK） |
| 合并单元格 | 67 |
| 数据行数 | 30（Row 26~56，Row 49 无数据） |
| 产品数量（去重） | 30 |
| 兼容性步骤数 | ~80（AO~DG） |
| 兼容性大类 | 8 |

**注意**: 本报告审计的 xlsx 为"水油"类型（印刷過水油類）。UV 油（哑光UV油标准书）位于 G 盘，结构与此文件相同（同属 Type A 模板）。

---

## 二、完整字段清单

xlsx 采用四段式结构，共 123 列，按区域划分如下：

### 2.1 物料基本信息（A~P，16 列）

| 列 | 字段名 | 行 17 表头 | 行 18 子表头 | 数据类型 |
|-----|--------|-----------|------------|---------|
| A | 种类 | 種類 / 1 | - | 编码段 |
| B | 材料 | 材料 / 2 | - | 编码段 |
| C | 主类别 | 主類別 / 3~5 | - | 编码段 |
| D | 副类别 | 副類別 / 6~8 | - | 编码段 |
| E | 流水码 | 流水碼 / 9~14 | - | 编码段 |
| **F** | **NTD测试单号** | NTD測試單號 | - | 文本（作为 material_code） |
| **G** | **采购部申请编号** | 採購部申請編號 | - | 文本 |
| **H** | **物料型号/编号** | 物料型號/編號 | - | 文本 |
| **I** | **物料名称** | 物料名稱 | - | 文本 |
| **J** | **物料用途** | 物料用途 | - | 文本 |
| **K** | **材质/分类** | 材質 | - | 文本 |
| **L** | **厚度** | 厚度 | - | 文本 |
| **M** | **尺寸** | 尺寸 | - | 文本 |
| **N** | **颜色** | 顏色 | - | 文本 |
| **O** | **形状** | 形狀 | - | 文本 |
| **P** | **测试员** | 測試員 / (中文名) | - | 文本 |

### 2.2 设计/结构限制（Q~AG，19 列）

| 列 | 字段组 | 子字段 | 详细说明 |
|-----|--------|--------|---------|
| Q | 用纸尺寸 | 最小 (mm) | 纸张最小尺寸限制 |
| R | 用纸尺寸 | 最大 (mm) | 纸张最大尺寸限制 |
| S | 点 | 最小 (mm) | 最小网点尺寸 |
| T | 点 | 最大 (mm) | 最大网点尺寸 |
| U | 线 | 最小 (mm) | 最小线条宽度 |
| V | 线 | 最大 (mm) | 最大线条宽度 |
| W | 间距 | 最小 (mm) | 最小间距 |
| X | 间距 | 最大 (mm) | 最大间距 |
| Y | 加工面积 | 最小 (mm) | 单个图案最小面积 |
| Z | 加工面积 | 最大 (mm) | 单个图案最大面积 |
| AA | 结构应用 | 适用产品 | 描述性文本，如"咭書 / 精平裝書" |
| AB | 结构 V/X | 单面 | V/X 判定 |
| AC | 结构 V/X | 双面 | V/X 判定 |
| AD | 结构 V/X | 封面 | V/X 判定 |
| AE | 结构 V/X | 书脊 | V/X 判定 |
| AF | 结构 V/X | 踩坑位 | V/X 判定 |
| AG | 结构 V/X | 内文 | V/X 判定 |

### 2.3 适用界面（AH~AN，7 列）

| 列 | 字段组 | 子字段 | 说明 |
|-----|--------|--------|------|
| AH | - | 基材厚度 | 如"所有厚度" |
| AI | 1-纸张面 | 适用 | 列出适用纸面材料类型 |
| AJ | 1-纸张面 | 不适用 | 列出不适用纸面材料 |
| AK | 2-印刷油墨面 | 适用 | 列出适用油墨类型 |
| AL | 2-印刷油墨面 | 不适用 | 列出不适用油墨 |
| AM | 3-后加工涂层面 | 适用 | 列出适用涂层 |
| AN | 3-后加工涂层面 | 不适用 | 列出不适用涂层 |

### 2.4 后加工兼容性矩阵（AO~DG，~77 列）

8 大类别，约 77 个步骤，每个步骤一列。二阶分类结构：

#### 大类 1：印刷（AO~BA，13 步）
單面印刷, 雙面印刷, 連線印刷, 離線印刷, 油性油, 水性油, UV油, 掃金粉, 逆向油, 冷燙, 仿皮革, 凸字粉, 凸字粉溝閃粉

#### 大类 2：烫金（BB~BN，13 步）
普通燙金, 鐳射燙金, 熒光燙金, 折光紋燙金, 立體燙金, 立體燙金擊凸, 擊凸, 擊凹, 立體擊凸, 深層擊凸, 超級深層擊凸, 擊紋, 熱辣

#### 大类 3：过胶（BO~BR，4 步）
單面過膠, 雙面過膠, 即塗型過膠, 預塗型過膠

#### 大类 4：丝印（BS~CF，14 步）
局部UV, 啞UV, 溝閃粉, 灑閃粉, 凸字油, 有色凸字油, 磨砂UV, 夜光油墨, 皺紋UV, LED珍珠, 巖石UV, 橘皮UV, 遇水變透明, 色墨

#### 大类 5：植毛（CG~CL，6 步）
普通植毛, 透明植毛, 閃粉植毛, 植毛壓紋, 熒光毛, 長毛

#### 大类 6：啤（CM~CR，6 步）
機啤, 手啤, 正啤折, 反啤折, 啤切, 啤半穿

#### 大类 7：手工（CS~DA，9 步）
壓紋, 壓竹脊, 燙鑽, 粘配件, 塗膠, 閘圓角, 人手掃色邊, 機掃色邊, 輥金邊

#### 大类 8：其他（DB~DG，6 步）
鐳射介紙, 車線, 粘接, 粘透明/可移性貼紙, 以產品原組合配件筆為準, 以客戶指定配件筆為準

### 2.5 备注区域（DH~DK，4 列）

| 列 | 字段 | 说明 |
|-----|------|------|
| DH | 注意事项 | 对特殊情况的说明 |
| DJ | 光泽 | 数值如"55~65" |
| DK | 水油书写功能 | 功能表说明文本 |
| DI-DR | D-生产商备注 | 10 列预留扩展，当前无数据 |

---

## 三、数据库现有字段对照

### 3.1 uv_oil_matte_product 表（19 字段）

| # | 字段名 | 类型 | 是否非空 | 对应 xlsx 列 | 映射状态 |
|---|--------|------|---------|------------|---------|
| 1 | id | SERIAL | PK | - | 自增 |
| 2 | material_code | VARCHAR(100) | 可空 | F - NTD測試單號 | ok |
| 3 | supplier_code | VARCHAR(100) | 可空 | G - 採購部申請編號 | ok |
| 4 | stock_code | VARCHAR(100) | 可空 | H - 物料型號/編號 | ok |
| 5 | material_name | VARCHAR(500) | NOT NULL | I - 物料名稱 | ok |
| 6 | usage | TEXT | 可空 | J - 物料用途 | ok |
| 7 | category | VARCHAR(200) | 可空 | K - 材質 | ok |
| 8 | color | VARCHAR(200) | 可空 | N - 顏色 | ok |
| 9 | responsible_person | VARCHAR(200) | 可空 | **M - 尺寸** | **映射错误** |
| 10 | min_sheet_size | VARCHAR(200) | 可空 | Q - 用紙尺寸最小 | **ETL 未填充** |
| 11 | max_sheet_size | VARCHAR(200) | 可空 | R - 用紙尺寸最大 | **ETL 未填充** |
| 12 | min_spacing | VARCHAR(100) | 可空 | W - 間距最小 | **ETL 未填充** |
| 13 | max_spacing | VARCHAR(100) | 可空 | X - 間距最大 | **ETL 未填充** |
| 14 | design_info | TEXT | 可空 | Q~Z 设计限制汇总 | **ETL 未填充** |
| 15 | applicable_interface | TEXT | 可空 | AH~AN 界面信息 | **ETL 未填充** |
| 16 | notes | TEXT | 可空 | DH - 注意事项 | **ETL 未填充** |
| 17 | is_active | BOOLEAN | 可空 | - | 默认 true |
| 18 | created_at | TIMESTAMP | 可空 | - | 自动 |
| 19 | updated_at | TIMESTAMP | 可空 | - | 自动 |

### 3.2 uv_oil_matte_compatibility 表（7 字段）

| # | 字段名 | 类型 | 对应 xlsx | 映射状态 |
|---|--------|------|----------|---------|
| 1 | id | SERIAL | - | 自增 |
| 2 | product_id | INTEGER | - | FK |
| 3 | post_processing_step | VARCHAR(500) | AO~DG 步骤名 | ok（但丢失大类层级） |
| 4 | compatibility_status | VARCHAR(10) | V/X | ok |
| 5 | remark | TEXT | - | 未使用 |
| 6 | display_order | INTEGER | - | 默认 0 |
| 7 | created_at | TIMESTAMP | - | 自动 |

---

## 四、丢失维度分析

### 4.1 ETL 完全丢失的字段（xlsx 有、DB 对应字段缺失）

| # | xlsx 字段 | 列 | 严重程度 | 说明 |
|---|-----------|-----|---------|------|
| 1 | **厚度** | L | **P1** | 物料物理厚度，对筛选和上机参数重要 |
| 2 | **形状** | O | **P1** | 物料形状描述（如卷装/片装） |
| 3 | **测试员** | P | **P1** | 责任人姓名，质量追溯用 |
| 4 | **结构适用性 V/X（7 列）** | AB~AG | **P2** | 单面/双面/封面/书脊/踩坑位/内文的兼容性 |
| 5 | **适用界面信息（7 列）** | AH~AN | **P1** | 基材/油墨/涂层的适用/不适用详细说明 |
| 6 | **光泽度** | DJ | **P2** | 光泽度范围值，品质参数 |
| 7 | **D-生产商备注（10 列）** | DI~DR | **P3** | 预留扩展字段，当前无数据 |

### 4.2 DB 有字段但 ETL 未填充的字段

| # | DB 字段 | 对应 xlsx | 严重程度 | 说明 |
|---|---------|----------|---------|------|
| 1 | min_sheet_size / max_sheet_size | Q, R | **P1** | 用纸尺寸在设计限制区，但 StandardHandler 不提取 |
| 2 | min_spacing / max_spacing | W, X | **P1** | 间距在设计限制区，但 StandardHandler 不提取 |
| 3 | design_info | Q~Z 综合 | **P1** | 设计限制综合字段，DB 有列但 ETL 不写入 |
| 4 | applicable_interface | AH~AN | **P1** | 界面适用性，DB 有列但 ETL 不写入 |
| 5 | notes | DH | **P1** | 备注信息，DB 有列但 ETL 不写入 |

### 4.3 字段映射错误

| # | 问题 | ETL 代码位置 | 影响 |
|---|------|-------------|------|
| 1 | **Column 13（M - 尺寸）被映射为 responsible_person** | handlers.py:237 | 尺寸数据丢失，responsible_person 实际为空或存储了尺寸值 |
| 2 | **Column 16（P - 测试员）未映射** | handlers.py 未出现 | 测试员姓名完全丢失，无法追溯质量责任人 |

### 4.4 结构化信息扁平化丢失

兼容性数据的两级分类（大类 + 步骤名）在入库时被扁平化为单级：

- xlsx 结构：`印刷 > 單面印刷`, `印刷 > 雙面印刷`, `燙金 > 普通燙金`
- DB 结构：`post_processing_step = "單面印刷"`, `雙面印刷`, `普通燙金`

ETL 仅在步骤名与分类名不重复时拼接为 `分類/步驟` 格式，但并非所有步骤都进行了分类前缀拼接。这导致前端无法按大类（印刷/烫金/过胶/丝印）分类筛选兼容性步骤。

### 4.5 丢失维度汇总表

| 丢失类型 | 数量 | 涉及字段 |
|---------|------|---------|
| 完全丢失（DB 无对应列） | 4 | 厚度, 形状, 测试员, 光泽度 |
| DB 有列但 ETL 未填充 | 5 | min/max_sheet_size, min/max_spacing, design_info, applicable_interface, notes |
| 映射错误 | 2 | col13->responsible_person, col16 未映射 |
| 结构化丢失 | 1 | 分类层级（大类->步骤）扁平化 |
| D-生产商备注区（预留） | 10 列 | DI~DR，当前无数据 |

---

## 五、导航栏分类修正建议

### 5.1 当前分类问题

兼容性步骤在 DB 中以扁平的 `post_processing_step` 存储，失去了 xlsx 原有的两级分类结构。前端导航/筛选时无法按"印刷""烫金""过胶"等大类浏览。

### 5.2 建议的分类结构

xlsx 定义了清晰的 8 大类分类体系，建议将其纳入 DB 和前端导航：

```
印刷
  ├── 單面印刷
  ├── 雙面印刷
  ├── 連線印刷
  ├── 離線印刷
  ├── 油性油
  ├── 水性油
  ├── UV油
  ├── 掃金粉
  ├── 逆向油
  ├── 冷燙
  ├── 仿皮革
  ├── 凸字粉
  └── 凸字粉溝閃粉

燙金
  ├── 普通燙金
  ├── 鐳射燙金
  ├── 熒光燙金
  ├── 折光紋燙金
  ├── 立體燙金
  ├── 立體燙金擊凸
  ├── 擊凸
  ├── 擊凹
  ├── 立體擊凸
  ├── 深層擊凸
  ├── 超級深層擊凸
  ├── 擊紋
  └── 熱辣

過膠
  ├── 單面過膠
  ├── 雙面過膠
  ├── 即塗型過膠
  └── 預塗型過膠

絲印
  ├── 局部UV
  ├── 啞UV
  ├── 溝閃粉
  ├── 灑閃粉
  ├── 凸字油
  ├── 有色凸字油
  ├── 磨砂UV
  ├── 夜光油墨
  ├── 皺紋UV
  ├── LED珍珠
  ├── 巖石UV
  ├── 橘皮UV
  ├── 遇水變透明
  └── 色墨

植毛
  ├── 普通植毛
  ├── 透明植毛
  ├── 閃粉植毛
  ├── 植毛壓紋
  ├── 熒光毛
  └── 長毛

啤
  ├── 機啤
  ├── 手啤
  ├── 正啤折
  ├── 反啤折
  ├── 啤切
  └── 啤半穿

手工
  ├── 壓紋
  ├── 壓竹脊
  ├── 燙鑽
  ├── 粘配件
  ├── 塗膠
  ├── 閘圓角
  ├── 人手掃色邊
  ├── 機掃色邊
  └── 輥金邊

其他
  ├── 鐳射介紙
  ├── 車線
  ├── 粘接
  ├── 粘透明 / 可移性貼紙
  ├── 以產品原組合配件筆為準
  └── 以客戶指定配件筆為準
```

### 5.3 实施建议

**方案 A（推荐）**: 在兼容性表增加 `category_group` 列

```sql
ALTER TABLE uv_oil_matte_compatibility 
ADD COLUMN category_group VARCHAR(50) DEFAULT NULL;
```

重新 ETL 时从 xlsx 的 Row 22（大类行）提取分类名填充此列。前端导航按 `category_group` 分组渲染。

**方案 B（轻量）**: 在步骤名前缀分类

已在部分步骤实施的策略（如"燙金/普通燙金"），统一对所有步骤加前缀。缺点是需要重构前端筛选逻辑中的步骤名匹配。

### 5.4 跨模块导航一致性

当前各 P0 模块的步骤集不完全一致。建议在 `compatibility` 表上建立统一的步骤注册表：

```sql
CREATE TABLE IF NOT EXISTS post_processing_step_registry (
    id SERIAL PRIMARY KEY,
    step_name VARCHAR(500) NOT NULL,
    category_group VARCHAR(50) NOT NULL,  -- 印刷/烫金/过胶/丝印/植毛/啤/手工/其他
    display_order INTEGER DEFAULT 0,
    applies_to TEXT[] DEFAULT '{}',  -- 适用的模块列表
    is_active BOOLEAN DEFAULT TRUE,
    UNIQUE(step_name)
);
```

这样前端导航栏统一从此表渲染，各模块共享同一套步骤分类。

---

## 六、ETL 改进建议

### 6.1 优先级 P0：修复字段映射错误

**问题**: StandardHandler.extract_product 中的列映射错误：
```python
# 当前代码（handlers.py:237）
responsible_person=_detector.cell_text(ws, row_num, 13),  # col 13 = 尺寸, 错误!
```

**修正方案**:
```python
# 正确的映射
size=_detector.cell_text(ws, row_num, 13),           # col 13 = 尺寸
responsible_person=_detector.cell_text(ws, row_num, 16),  # col 16 = 测试员
```

**注意**: DB 的 `responsible_person` 列名不变，修改 ETL 中的列索引从 13 改为 16。

### 6.2 优先级 P1：补齐丢失的字段提取

扩展 StandardHandler.extract_product 以提取 5 个当前忽略的设计限制字段：

```python
def extract_product(self, ws, row_num) -> Product:
    return Product(
        material_code=_detector.cell_text(ws, row_num, 6),
        supplier_code=_detector.cell_text(ws, row_num, 7),
        stock_code=_detector.cell_text(ws, row_num, 8),
        material_name=_detector.cell_text(ws, row_num, 9),
        usage=_detector.cell_text(ws, row_num, 10),
        category=_detector.cell_text(ws, row_num, 11),
        color=_detector.cell_text(ws, row_num, 14),
        responsible_person=_detector.cell_text(ws, row_num, 16),  # 修正
        # 新增丢失字段
        size=_detector.cell_text(ws, row_num, 13),         # M - 尺寸
        shape=_detector.cell_text(ws, row_num, 15),         # O - 形状
        thickness=_detector.cell_text(ws, row_num, 12),     # L - 厚度
        min_sheet_size=_detector.cell_text(ws, row_num, 17),  # Q
        max_sheet_size=_detector.cell_text(ws, row_num, 18),  # R
        min_spacing=_detector.cell_text(ws, row_num, 23),    # W
        max_spacing=_detector.cell_text(ws, row_num, 24),    # X
        applicable_interface=self._extract_interface(ws, row_num),  # AH~AN
        notes=_detector.cell_text(ws, row_num, 112),         # DH
    )
```

### 6.3 优先级 P1：提取适用界面信息

xlsx 的列 AH~AN 包含 7 列结构化界面信息（基材厚度、纸张面适用/不适用、油墨面适用/不适用、涂层适用/不适用）。ETL 应将此信息合并为 `applicable_interface` 字段或拆分到子表。

建议存储格式（JSON，存入 `applicable_interface` TEXT 字段）：
```json
{
  "substrate_thickness": "所有厚度",
  "paper_surface": {
    "applicable": "吸收性材料(粉紙面：單粉紙/咭、雙粉紙/咭、啞粉紙/咭、粉灰咭)...",
    "inapplicable": "/"
  },
  "ink_surface": {
    "applicable": "普通油墨 / 大豆油墨 / 快乾油墨 / LED-UV油墨",
    "inapplicable": "/"
  },
  "coating_surface": {
    "applicable": "燙金面",
    "inapplicable": "/"
  }
}
```

### 6.4 优先级 P1：兼容性步骤保留分类层级

修改 `extract_compatibility_pairs` 方法，确保每对 (step_name, status) 都携带大类信息：

```python
# 当前：仅对部分步骤加前缀，逻辑不一致
# 建议：统一格式为 "category/step_name"
for col in range(col_start, col_end + 1):
    status = cell_text(ws, row, col)
    if status in ("V", "X", "▷", "O"):
        step_name = cell_text(ws, step_row, col)
        # 从 row22 获取当前列的分类名（向下填充）
        category = get_category_for_column(ws, cat_row, col)
        full_name = f"{category}/{step_name}" if category else step_name
        pairs.append((full_name, status))
```

### 6.5 优先级 P2：DB 表结构调整

考虑为 `uv_oil_matte_product` 增加以下列：

```sql
-- 厚度和形状（新字段）
ALTER TABLE uv_oil_matte_product ADD COLUMN IF NOT EXISTS thickness VARCHAR(200) DEFAULT NULL;
ALTER TABLE uv_oil_matte_product ADD COLUMN IF NOT EXISTS shape VARCHAR(200) DEFAULT NULL;

-- 结构适用性（bitmask 或 JSON）
ALTER TABLE uv_oil_matte_product ADD COLUMN IF NOT EXISTS structure_applicability JSONB DEFAULT NULL;
-- 示例: {"single_side": true, "double_side": true, "cover": true, "spine": true, "deboss": false, "inner_page": true}

-- 光泽度
ALTER TABLE uv_oil_matte_product ADD COLUMN IF NOT EXISTS gloss_range VARCHAR(100) DEFAULT NULL;
```

### 6.6 优先级 P2：统一 ETL 列号配置

当前 ETL 的列号（col 6, 7, 8, 9, 10, 11, 13, 14）硬编码在 handlers.py 中。建议抽离为模块级配置，支持不同 xlsx 模板的列偏移：

```python
# 每个 schema_type 定义其列映射
COLUMN_MAP = {
    "uv_oil_matte": {
        "material_code": 6,
        "supplier_code": 7,
        "stock_code": 8,
        "material_name": 9,
        "usage": 10,
        "category": 11,
        "thickness": 12,
        "size": 13,
        "color": 14,
        "shape": 15,
        "responsible_person": 16,
    },
    "water_varnish": { ... },
    "silicone": { ... },
}
```

### 6.7 D-生产商备注预留列

xlsx 列 DI~DR（10 列）标记为"D-其它補充資料(可根據不同物料的需求作自行添加)"，当前文件中无数据。ETL 应跳过这些列，但需记录此扩展区域的存在，以便未来有数据时快速接入。

---

## 七、改进行动计划

| 优先级 | 任务 | 涉及文件 | 预估工时 |
|-------|------|---------|---------|
| P0 | 修正 responsible_person 列映射（col13->col16） | handlers.py | 0.5h |
| P1 | 补齐厚度/形状字段提取 | handlers.py, models.py | 1h |
| P1 | 补齐 sheet_size/spacing 字段提取 | handlers.py | 1h |
| P1 | 补齐 applicable_interface 提取 | handlers.py, header_detector.py | 2h |
| P1 | 补齐 notes 字段提取 | handlers.py | 0.5h |
| P1 | 兼容性步骤保留分类层级 | header_detector.py | 1h |
| P2 | DB 表加 thickness/shape 列 | DDL SQL | 0.5h |
| P2 | 列号配置抽离 | handlers.py, models.py | 1h |
| P2 | 建立统一步骤注册表 | DDL + ETL | 2h |

**合计**: 约 9.5 小时开发 + 2 小时重新 ETL 验证。
