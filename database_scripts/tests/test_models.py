"""Tests for models module — dataclass defaults and basic construction."""

from models import CategoryDef, Product, DataRow, CategoryStats


class TestCategoryDef:
    """测试 CategoryDef dataclass 构造与默认值。"""

    def test_defaults(self):
        """测所有字段使用默认值创建。"""
        c = CategoryDef()
        assert c.dir == ""
        assert c.glob == ""
        assert c.data_count == 0
        assert c.product_table == ""
        assert c.compat_table == ""
        assert c.schema_type == ""
        assert c.subtype == ""

    def test_custom_values(self):
        """测传入指定参数覆盖默认值。"""
        c = CategoryDef(
            dir="test",
            glob="*.xlsx",
            data_count=42,
            product_table="products",
            compat_table="compat",
            schema_type="silicone",
            subtype="UV",
        )
        assert c.dir == "test"
        assert c.glob == "*.xlsx"
        assert c.data_count == 42
        assert c.product_table == "products"
        assert c.compat_table == "compat"
        assert c.schema_type == "silicone"
        assert c.subtype == "UV"

    def test_partial_override(self):
        """测部分字段覆盖，其余保持默认。"""
        c = CategoryDef(dir="foo", glob="bar")
        assert c.dir == "foo"
        assert c.glob == "bar"
        assert c.data_count == 0  # 默认
        assert c.schema_type == ""

    def test_fields_are_mutable(self):
        """测创建后字段值可修改。"""
        c = CategoryDef(dir="old")
        c.dir = "new"
        assert c.dir == "new"


class TestProduct:
    """测试 Product dataclass 构造与字段。"""

    def test_defaults(self):
        """测所有字段使用默认值创建。"""
        p = Product()
        assert p.material_name == ""
        assert p.color == ""
        assert p.usage == ""
        assert p.material_code == ""
        assert p.supplier_code == ""
        assert p.stock_code == ""

    def test_material_name_required(self):
        """测创建时填入 material_name 后正确保留。"""
        p = Product(material_name="Test Material")
        assert p.material_name == "Test Material"
        assert p.color == ""  # 未填的保持默认

    def test_lamination_fields(self):
        """测过胶材料专用字段正常设值。"""
        p = Product(
            material_name="Lam Film",
            usage_text="覆合用",
            material_type="BOPP",
            thickness_film="0.018",
            thickness_glue="0.005",
            size_info="1000x500",
            shape="卷装",
        )
        assert p.usage_text == "覆合用"
        assert p.material_type == "BOPP"
        assert p.thickness_film == "0.018"
        assert p.thickness_glue == "0.005"
        assert p.size_info == "1000x500"
        assert p.shape == "卷装"

    def test_color_default_emptry_string(self):
        """测 color 字段默认值为空字符串。"""
        p = Product(material_name="Test")
        assert p.color == ""


class TestDataRow:
    """测试 DataRow dataclass 数据行模型。"""

    def test_defaults(self):
        """测所有字段使用默认值。"""
        r = DataRow()
        assert r.row == 0
        assert r.code == ""
        assert r.name == ""
        assert r.is_primary is False

    def test_basic_construction(self):
        """测基础构造赋值正确。"""
        r = DataRow(row=1, code="UV-001", name="Test Material")
        assert r.row == 1
        assert r.code == "UV-001"
        assert r.name == "Test Material"
        assert r.is_primary is False

    def test_primary_marker(self):
        """测主物料标记 is_primary=True 的构造。"""
        r = DataRow(row=5, code="#UV-001", name="Primary Material", is_primary=True)
        assert r.is_primary is True
        assert r.code == "#UV-001"

    def test_fields_are_mutable(self):
        """测创建后字段值可修改。"""
        r = DataRow(row=1, code="A", name="X")
        r.is_primary = True
        assert r.is_primary is True


class TestCategoryStats:
    """测试 CategoryStats dataclass 统计累积。"""

    def test_defaults_zero(self):
        """测所有计数默认从 0 开始。"""
        s = CategoryStats()
        assert s.files == 0
        assert s.products == 0
        assert s.compat == 0
        assert s.duplicates == 0
        assert s.skipped == 0

    def test_add_products(self):
        """测 products 计数累加。"""
        s = CategoryStats()
        s.products += 1
        assert s.products == 1

    def test_add_files(self):
        """测 files 计数累加。"""
        s = CategoryStats()
        s.files += 2
        assert s.files == 2

    def test_add_compat_and_skipped(self):
        """测 compat 和 skipped 计数累加。"""
        s = CategoryStats()
        s.compat += 10
        s.skipped += 3
        s.duplicates += 5
        assert s.compat == 10
        assert s.skipped == 3
        assert s.duplicates == 5

    def test_multiple_increments(self):
        """测多次累加结果正确。"""
        s = CategoryStats()
        s.files += 1
        s.files += 1
        s.products += 3
        s.products += 2
        assert s.files == 2
        assert s.products == 5
