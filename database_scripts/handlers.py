"""
Schema Handlers — Strategy pattern for P0 ETL schema types.

Each handler encapsulates the differences between standard product+compat
schemas (silicone, uv_oil_matte, water_varnish) and the lamination schema.

Usage:
    handler = get_handler(cat.schema_type)
    handler.create_tables(conn, cat)
    product = handler.extract_product(ws, row)
    pid = handler.insert_product(conn, product, cat)
    handler.insert_compat(conn, pid, step, status, cat)
"""

import time
from dataclasses import asdict

from header_detector import HeaderDetector
from models import CategoryDef, Product

_detector = HeaderDetector()


# ────────────────────────────────────────────────────────────────
# Shared helpers
# ────────────────────────────────────────────────────────────────


def log(msg):
    print(f"[{time.strftime('%H:%M:%S')}] {msg}")


def exec_sql(conn, sql, params=None):
    """Execute SQL with optional params."""
    with conn.cursor() as cur:
        if params:
            cur.execute(sql, params)
        else:
            cur.execute(sql)
        try:
            return cur.fetchall()
        except Exception:
            return []


def _insert_product(conn, table, product_data):
    """Insert a product row. Returns the new id or None."""
    if isinstance(product_data, Product):
        product_data = asdict(product_data)

    fields = []
    placeholders = []
    values = []

    for k, v in product_data.items():
        if v is not None and v != "" and v != "/":
            fields.append(k)
            placeholders.append("%s")
            values.append(v)

    if not fields:
        return None

    # Check for duplicate by material_code
    code_val = product_data.get("material_code", "")
    first_code = code_val.split(" / ")[0].strip()
    if first_code:
        existing = exec_sql(
            conn,
            f"SELECT id FROM {table} WHERE material_code LIKE %s",
            (f"{first_code}%",),
        )
        if existing:
            log(f"    Duplicate material_code '{first_code}', skipping")
            return None

    sql = (
        f"INSERT INTO {table} ({', '.join(fields)}) "
        f"VALUES ({', '.join(placeholders)}) RETURNING id"
    )
    try:
        result = exec_sql(conn, sql, values)
        if result:
            return result[0][0]
    except Exception:
        try:
            sql_no_return = (
                f"INSERT INTO {table} ({', '.join(fields)}) "
                f"VALUES ({', '.join(placeholders)})"
            )
            exec_sql(conn, sql_no_return, values)
            res = exec_sql(conn, "SELECT LASTVAL()")
            if res:
                return res[0][0]
        except Exception as e2:
            log(f"    Insert failed: {e2}")
            return None
    return None


def _insert_compatibility(conn, table, product_id, step_name, status):
    """Insert a compatibility row. Skip if duplicate."""
    try:
        exec_sql(
            conn,
            f"INSERT INTO {table} (product_id, post_processing_step, compatibility_status) "
            f"VALUES (%s, %s, %s)",
            (product_id, step_name, status),
        )
        return True
    except Exception:
        return False


# ────────────────────────────────────────────────────────────────
# Abstract handler
# ────────────────────────────────────────────────────────────────


class SchemaHandler:
    """Strategy interface for schema-type-specific operations."""

    def create_tables(self, conn, cat: CategoryDef):
        raise NotImplementedError

    def extract_product(self, ws, row_num) -> Product:
        raise NotImplementedError

    def insert_product(self, conn, product: Product, cat: CategoryDef) -> int | None:
        raise NotImplementedError

    def insert_compat(self, conn, product_id: int, step: str, status: str, cat: CategoryDef):
        raise NotImplementedError


# ────────────────────────────────────────────────────────────────
# DDL templates
# ────────────────────────────────────────────────────────────────

PRODUCT_DDL = """
CREATE TABLE IF NOT EXISTS {table} (
    id SERIAL PRIMARY KEY,
    material_code VARCHAR(100) DEFAULT NULL,
    supplier_code VARCHAR(100) DEFAULT NULL,
    stock_code VARCHAR(100) DEFAULT NULL,
    material_name VARCHAR(500) NOT NULL,
    usage TEXT DEFAULT NULL,
    category VARCHAR(200) DEFAULT NULL,
    color VARCHAR(200) DEFAULT NULL,
    responsible_person VARCHAR(200) DEFAULT NULL,
    min_sheet_size VARCHAR(200) DEFAULT NULL,
    max_sheet_size VARCHAR(200) DEFAULT NULL,
    min_spacing VARCHAR(100) DEFAULT NULL,
    max_spacing VARCHAR(100) DEFAULT NULL,
    design_info TEXT DEFAULT NULL,
    applicable_interface TEXT DEFAULT NULL,
    notes TEXT DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
"""

COMPAT_DDL = """
CREATE TABLE IF NOT EXISTS {table} (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES {product_table}(id),
    post_processing_step VARCHAR(500) NOT NULL,
    compatibility_status VARCHAR(10) NOT NULL,
    remark TEXT DEFAULT NULL,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
"""

LAMINATION_TABLE_DDL = """
CREATE TABLE IF NOT EXISTS lamination_material_products (
    id SERIAL PRIMARY KEY,
    material_code VARCHAR(100) NOT NULL,
    stock_code VARCHAR(200) DEFAULT NULL,
    material_name VARCHAR(500) NOT NULL,
    usage_text TEXT DEFAULT NULL,
    material_type VARCHAR(200) DEFAULT NULL,
    thickness_film VARCHAR(100) DEFAULT NULL,
    thickness_glue VARCHAR(100) DEFAULT NULL,
    size_info VARCHAR(200) DEFAULT NULL,
    color VARCHAR(200) DEFAULT NULL,
    shape VARCHAR(200) DEFAULT NULL,
    responsible_person VARCHAR(200) DEFAULT NULL,
    category VARCHAR(200) DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS lamination_material_compatibility (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL REFERENCES lamination_material_products(id),
    post_processing_step VARCHAR(500) NOT NULL,
    compatibility_status VARCHAR(10) NOT NULL,
    remark TEXT DEFAULT NULL,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
"""


# ────────────────────────────────────────────────────────────────
# Standard handler (silicone, uv_oil_matte, water_varnish)
# ────────────────────────────────────────────────────────────────


class StandardHandler(SchemaHandler):
    """Handles standard product+compat schema types."""

    def create_tables(self, conn, cat: CategoryDef):
        sql = PRODUCT_DDL.format(table=cat.product_table)
        exec_sql(conn, sql)
        log(f"  Created table {cat.product_table}")

        if cat.compat_table:
            sql = COMPAT_DDL.format(table=cat.compat_table, product_table=cat.product_table)
            exec_sql(conn, sql)
            log(f"  Created table {cat.compat_table}")

        conn.commit()

    def extract_product(self, ws, row_num) -> Product:
        return Product(
            material_code=_detector.cell_text(ws, row_num, 6),
            supplier_code=_detector.cell_text(ws, row_num, 7),
            stock_code=_detector.cell_text(ws, row_num, 8),
            material_name=_detector.cell_text(ws, row_num, 9),
            usage=_detector.cell_text(ws, row_num, 10),
            category=_detector.cell_text(ws, row_num, 11),
            responsible_person=_detector.cell_text(ws, row_num, 13),
            color=_detector.cell_text(ws, row_num, 14),
        )

    def insert_product(self, conn, product: Product, cat: CategoryDef) -> int | None:
        return _insert_product(conn, cat.product_table, product)

    def insert_compat(self, conn, product_id: int, step: str, status: str, cat: CategoryDef):
        return _insert_compatibility(conn, cat.compat_table, product_id, step, status)


# ────────────────────────────────────────────────────────────────
# Lamination handler
# ────────────────────────────────────────────────────────────────


class LaminationHandler(SchemaHandler):
    """Handles lamination-specific table structure and column mapping."""

    def create_tables(self, conn, cat: CategoryDef):
        log(f"  Creating lamination tables...")
        parts = [p.strip() for p in LAMINATION_TABLE_DDL.split(";") if p.strip()]
        for part in parts:
            exec_sql(conn, part)

    def extract_product(self, ws, row_num) -> Product:
        product = Product(
            material_code=_detector.cell_text(ws, row_num, 6),
            supplier_code=_detector.cell_text(ws, row_num, 7),
            stock_code=_detector.cell_text(ws, row_num, 8),
            material_name=_detector.cell_text(ws, row_num, 9),
            responsible_person=_detector.cell_text(ws, row_num, 13),
        )
        product.usage_text = _detector.cell_text(ws, row_num, 10)
        product.material_type = _detector.cell_text(ws, row_num, 11)
        product.thickness_film = _detector.cell_text(ws, row_num, 12)
        glue_val = _detector.cell_text(ws, row_num, 13)
        product.thickness_glue = glue_val if glue_val else None
        product.size_info = _detector.cell_text(ws, row_num, 14)
        product.color = _detector.cell_text(ws, row_num, 15)
        product.shape = _detector.cell_text(ws, row_num, 16)
        return product

    def insert_product(self, conn, product: Product, cat: CategoryDef) -> int | None:
        return _insert_product(conn, "lamination_material_products", product)

    def insert_compat(self, conn, product_id: int, step: str, status: str, cat: CategoryDef):
        return _insert_compatibility(conn, "lamination_material_compatibility", product_id, step, status)


# ────────────────────────────────────────────────────────────────
# Factory
# ────────────────────────────────────────────────────────────────

_handlers = {}


def get_handler(schema_type: str) -> SchemaHandler:
    """Return the appropriate SchemaHandler for the given schema_type."""
    if schema_type not in _handlers:
        if schema_type == "lamination":
            _handlers[schema_type] = LaminationHandler()
        else:
            _handlers[schema_type] = StandardHandler()
    return _handlers[schema_type]
