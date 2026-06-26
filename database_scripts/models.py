from dataclasses import dataclass, field
from typing import Optional


@dataclass
class CategoryDef:
    dir: str = ""
    glob: str = ""
    data_count: int = 0
    product_table: str = ""
    compat_table: str = ""
    schema_type: str = ""
    subtype: str = ""
    code_col: int = 6  # Column index for material code (1-based)


@dataclass
class Product:
    material_code: str = ""
    supplier_code: str = ""
    stock_code: str = ""
    material_name: str = ""
    usage: str = ""
    category: str = ""
    color: str = ""
    responsible_person: str = ""
    min_sheet_size: str = ""
    max_sheet_size: str = ""
    min_spacing: str = ""
    max_spacing: str = ""
    design_info: str = ""
    applicable_interface: str = ""
    notes: str = ""
    thickness: str = ""
    tester: str = ""
    # 过胶材料专用字段
    usage_text: str = ""
    material_type: str = ""
    thickness_film: str = ""
    thickness_glue: str = ""
    size_info: str = ""
    shape: str = ""


@dataclass
class DataRow:
    row: int = 0
    code: str = ""
    name: str = ""
    is_primary: bool = False


@dataclass
class CategoryStats:
    files: int = 0
    products: int = 0
    compat: int = 0
    duplicates: int = 0
    skipped: int = 0
