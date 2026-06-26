# ETL 数据管道模式

## 来源
- 项目: YTS Materials（印刷厂物料匹配系统）
- 关键文件:
  - `database_scripts/p0_etl.py` — 主流程（Phase0→Phase1→validate→export）
  - `database_scripts/header_detector.py` — xlsx 表头检测器
  - `database_scripts/handlers.py` — 品类 Handler 策略模式
  - `database_scripts/models.py` — 品类定义 + 数据模型

## 问题
印刷厂有 15+ 品类（烫金/硅胶/UV哑油/水油/过胶/LEO纸品等），每批新物料以 xlsx 文件从 G 盘导入。之前的方式是：
- 每个品类写一份独立导入脚本 → 大量重复代码
- Excel 文件格式千奇百怪 → 需要自动检测表头位置
- V/X/▷/O 兼容状态分散在多列 → 需要三步定位法解析
- 导入完没有自动校验 → 数据问题要上线后才发现

## 方案

### 四阶段管道

```
Phase 0 ─── Phase 1 ─── Phase 2 ───── Phase 3
 建表       导入数据      数据校验      报表导出
    │          │            │             │
    │  DDL     │  Handler   │  COUNT(*)   │  Excel/CSV
    │ 模板化   │  策略模式  │  全表扫描   │  汇总输出
```

### 品类定义驱动

所有品类信息集中声明在 OrderedDict 中，每个条目只描述差异：

```python
CATEGORIES["uv_oil_matte"] = CategoryDef(
    dir="UV油墨",
    glob="*哑光UV油*",
    data_count=12,
    product_table="uv_oil_matte_product",
    compat_table="uv_oil_matte_compatibility",
    schema_type="uv_oil_matte",
)
```

一个品类 = 目录 + 文件匹配 + 表名 + Schema 类型，零代码新增。

### 表头自动检测

三步定位法替代硬编码行列号：

1. **Sheet 定位** — 优先选名称含 "適用性/指引" 的 sheet
2. **Step 表头检测** — 第 40-125 列扫描工序关键词（單面印刷/燙金/過膠/絲印等）
3. **Category 分类行** — 在 step 表头上一行找分类组（印刷/烫金/过胶等大类）

### 兼容性对解析

三层表头结构：
```
Row cat_row  : 分类组（印刷、烫金、过胶…）
Row step_row : 工序名（单面印刷、UV油、普通烫金…）
Row data_row : V/X/▷/O 状态值
```

适用场景：任何"横向列头为工序/属性 + 单元格为状态值"的矩阵型表格。

### Handler 策略模式

```python
handler = get_handler(cat.schema_type)  # SchemaHandler 接口
handler.create_tables(conn, cat)        # DDL 模板化
handler.extract_product(ws, row)        # 列映射品类化
handler.insert_product(conn, product)   # 写入通用化
handler.insert_compat(conn, pid, step)  # 兼容性去重写入
```

StandardHandler 覆盖 80% 品类，LaminationHandler 覆盖过胶品类（表结构不同），新增品类只需写新 Handler。

### 错误隔离与校验

- 每文件独立 try-catch，一个文件炸了不影响其他
- `--dry-run` 模式零副作用试跑
- `--validate` 模式跑全表 `SELECT COUNT(*)` 对比预期
- `conn.rollback()` 在异常时回滚事务，清除 stale 状态

## 关键设计决策

| 选项 | 选择 | 理由 |
|------|------|------|
| 品类定义位置 | 代码 OrderedDict | 品类数量有限(15+)，YAML 反而增加复杂度 |
| 表结构统一度 | 90% 统一模板 + 10% Handler 特化 | 覆盖全品类又不增加认知负担 |
| 兼容性列范围 | 40-125 硬编码 | 实测所有 xlsx 文件在此范围内 |
| 事务粒度 | 每文件 commit | 单品类文件独立，不互相依赖 |

## 潜在演进

- Phase 4: 差异报表（新导入 vs 已有数据对比）
- YAML 品类定义（当品类超过 30+ 时从代码抽离）
- 增量导入（根据文件修改时间只导新文件）
- Web 端导入触发器（拖拽上传替代 G 盘路径依赖）
