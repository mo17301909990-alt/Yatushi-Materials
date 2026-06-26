# 领域 ETL Handler 策略模式

## 来源
- 项目: YTS Materials（印刷厂物料匹配系统）
- 关键文件:
  - `database_scripts/handlers.py` — SchemaHandler 抽象 + 实现
  - `database_scripts/p0_etl.py` — 主调用流程
  - `database_scripts/models.py` — 数据模型 + 品类定义
  - `database_scripts/header_detector.py` — 表头检测

## 问题
从 Excel 导入结构化数据时，不同品类的 Excel 格式、表结构、字段映射存在差异：
- 80% 品类结构相似（物料编码+名称+工序兼容矩阵）
- 10% 品类字段完全不同（如过胶资料有膜厚/胶厚参数）
- 10% 品类列位置偏移（如 LEO 纸品编码在 G 列而非 F 列）

传统做法要么一刀切（导入不了特化品类）、要么每品类写独立脚本（90% 代码重复）。

## 方案

### Handler 策略模式三层架构

```
p0_etl.py（主流程编排）
  │
  ├─→ get_handler(schema_type) ──→ SchemaHandler（策略接口）
  │       │
  │       ├── StandardHandler（覆盖 80% 品类）
  │       │   DDL 模板化 + 列映射标准化
  │       │
  │       └── LaminationHandler（过胶特化）
  │           表结构不同 + 字段映射不同
  │
  └─→ HeaderDetector（表头定位，与品类无关）
       ├── find_sheet() — 自动选 sheet
       ├── find_step_header_row() — 定位工序行
       └── extract_compatibility_pairs() — 解析 V/X/▷/O
```

### Handler 接口定义

```python
class SchemaHandler:
    """策略接口 — 每个 schema_type 对应一个实现"""

    def create_tables(self, conn, cat: CategoryDef):
        """建表 DDL（每品类独立的 product 和 compat 表）"""

    def extract_product(self, ws, row_num) -> Product:
        """从 Excel 行提取 Product 对象（列映射在此）"""

    def insert_product(self, conn, product, cat) -> int | None:
        """写入 product 表（去重逻辑在此）"""

    def insert_compat(self, conn, product_id, step, status, cat):
        """写入 compat 表"""
```

### DDL 模板化

```sql
-- Standard 模板（建表 SQL 一致，只有表名不同）
CREATE TABLE IF NOT EXISTS {product_table} (
    id SERIAL PRIMARY KEY,
    material_code VARCHAR(100),
    material_name VARCHAR(500) NOT NULL,
    -- ... 20+ 公共字段
);

CREATE TABLE IF NOT EXISTS {compat_table} (
    id SERIAL PRIMARY KEY,
    product_id INTEGER REFERENCES {product_table}(id),
    post_processing_step VARCHAR(500) NOT NULL,
    compatibility_status VARCHAR(10) NOT NULL,
    -- ...
);
```

LaminationHandler 覆盖完全不同的表结构（lamination_material_products 字段不同），但接口相同。

### 品类新增流程

```
1. 在 p0_etl.py CATEGORIES 加一行定义
2. 如果 schema_type 已存在（standard/lamination）→ 完工
3. 如果 schema_type 是新的 → 写一个新 Handler 实现
4. 如果新表结构 → 在 handlers.py 加 DDL 模板
```

平均新增一个品类 = **1 行配置**（使用已有 Handler）+ **0 行代码**。

### 场景示例

| 品类 | schema_type | Handler | 差异点 |
|------|------------|---------|--------|
| UV 哑光油 | uv_oil_matte | StandardHandler | 标准列映射 |
| 硅胶发光油墨 | silicone | StandardHandler | 标准列映射 |
| 过胶材料 | lamination | LaminationHandler | 表结构不同（膜厚/胶厚） |
| LEO 书板纸 | silicone | StandardHandler | code_col=7 编码列偏移 |

## 关键设计决策

| 选项 | 选择 | 理由 |
|------|------|------|
| 策略条件 | schema_type 字符串 | 品类数量有限，不需要复杂路由 |
| Handler 单例 | factory 缓存 | 无状态 handler，不用重复创建 |
| DDL 位置 | 代码模板字符串 | 小范围变动，SQL 文件增加管理负担 |
| 品类表达 | Python dataclass | 品类变量少(6 字段)，YAML/JSON 徒增复杂度 |

## 潜在演进

- **YAML 定义品类** — 当品类超过 30 个，从代码抽离为配置
- **动态字段映射** — Excel 列号 → DB 字段的映射可配置化，不再硬编码 extract_product
- **Handler 测试桩** — 为每个 Handler 配 mock Excel，跑通全品类测试套件
- **差异合并 Handler** — 发现已有数据时，Handler 决定是跳过/覆盖/追加
