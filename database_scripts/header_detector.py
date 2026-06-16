"""
Header Detector — Locate three-level header structure in P0 xlsx worksheets.
Extracted from p0_etl.py for reuse and testability.
"""

import re
from dataclasses import dataclass, field

from models import DataRow


@dataclass
class HeaderConfig:
    """表头检测配置参数"""

    step_keywords: set = field(default_factory=lambda: {
        "單面印刷", "雙面印刷", "連線印刷", "離線印刷", "油性油",
        "水性油", "UV油", "普通油", "普通燙金", "鐳射燙金",
        "立體燙金", "擊凸", "擊凹", "過膠", "絲印", "植毛",
        "機啤", "手啤", "壓紋", "粘貼", "車線",
    })
    category_keywords: set = field(default_factory=lambda: {
        "印刷", "燙金", "過膠", "絲印", "植毛", "啤", "手工", "其他",
        "物料", "適用", "不適用",
    })
    row_start: int = 15
    row_end: int = 30
    col_start: int = 40
    col_end: int = 125


class HeaderDetector:
    """表头检测器 — 从 xlsx worksheet 中定位三层表头位置"""

    def __init__(self, config: HeaderConfig = None):
        self.config = config or HeaderConfig()

    # ── Low-level cell helpers ──────────────────────────────────────

    def cell_text(self, ws, row, col) -> str:
        """安全获取单元格文本，处理合并单元格返回 None 的情况。"""
        try:
            v = ws.cell(row, col).value
        except Exception:
            return ""
        if v is None:
            return ""
        return self.normalize_multiline(v)

    def normalize_multiline(self, v) -> str:
        """将 \\n 分隔符替换为 ' / '，去除前后空白。"""
        if v is None:
            return ""
        s = str(v).strip()
        s = s.replace("\\n", " / ")
        s = s.replace("\n", " / ")
        while " /  / " in s:
            s = s.replace(" /  / ", " / ")
        while "  / " in s:
            s = s.replace("  / ", " / ")
        s = s.strip(" / ")
        return s.strip()

    def is_valid_status(self, v) -> bool:
        """检查单元格值是否为有效兼容状态 (V/X/▷/O)。"""
        s = str(v).strip()
        return s in ("V", "X", "▷", "O")

    # ── Sheet / header locating ─────────────────────────────────────

    def find_sheet(self, ws):
        """
        定位工作簿中正确的工作表。
        优先查找名称包含"適用性"或"指引"的 sheet，其次找行/列数足够的 sheet。
        """
        for sn in ws.parent.sheetnames:
            if "適用性" in sn or "指引" in sn:
                return ws.parent[sn]

        for sn in ws.parent.sheetnames:
            sheet = ws.parent[sn]
            if sheet.max_row > 15 and sheet.max_column > 60:
                return sheet

        return ws

    def find_step_header_row(self, ws):
        """
        定位步骤表头行（包含工序名称，如 單面印刷、雙面印刷 等）。

        步骤表头的特征是第 40-60 列有 >= 20 个非空格，
        且其中包含已知的步骤关键词。

        Returns:
            int: 步骤表头行号，或 None。
        """
        step_keywords = self.config.step_keywords
        best_row = None
        best_count = 0

        for row in range(self.config.row_start, min(ws.max_row + 1, self.config.row_end)):
            count = 0
            for col in range(self.config.col_start, min(ws.max_column + 1, 120)):
                v = self.cell_text(ws, row, col)
                if not v:
                    continue
                if any(kw in v for kw in step_keywords) or (len(v) >= 2 and len(v) <= 20):
                    count += 1

            if count > best_count:
                best_count = count
                best_row = row

        return best_row

    def find_category_header_row(self, ws):
        """
        定位分类组表头行（印刷、烫金、过胶、丝印等，通常在步骤表头上一行）。

        Returns:
            int: 分类组表头行号，或 None。
        """
        step_row = self.find_step_header_row(ws)
        if step_row is None:
            return None

        group_keywords = self.config.category_keywords

        for row in range(max(14, step_row - 5), step_row):
            count = 0
            for col in range(self.config.col_start, min(ws.max_column + 1, 120)):
                v = self.cell_text(ws, row, col)
                if not v:
                    continue
                if any(kw == v for kw in group_keywords) or (len(v) <= 4 and len(v) >= 1):
                    count += 1
            if count >= 3:
                return row

        return step_row - 1 if step_row else None

    # ── Data row / pair extraction ──────────────────────────────────

    def find_data_rows(self, ws, step_row):
        """
        定位工作表中的所有数据行。

        数据行必须满足：
        - 第 9 列（物料名称）非空
        - 第 6 列（测试编号）非空且不是 '/' 或 '-'
        - 第 6 列以 '#' 开头（主物料）或匹配物料编号模式
        - 跳过文档说明行（第 6 列文本超过 200 字符）
        - 去重

        Returns:
            list[DataRow]: 每项含 row, code, name, is_primary
        """
        data_rows = []
        seen_codes = set()

        for row in range(step_row + 1, ws.max_row + 1):
            col6 = self.cell_text(ws, row, 6)
            col9 = self.cell_text(ws, row, 9)

            if not col9 or col9 in ("/", "-"):
                continue

            if not col6 or col6 == "/" or col6 == "-":
                continue

            if len(col6) > 200:
                continue

            is_primary = col6.startswith("#")
            code_pattern = re.match(
                r'^#?[A-Za-z]+-\d+|^[A-Za-z]+-\d+|^[A-Za-z]+\d+', col6
            )

            if not is_primary and not code_pattern:
                continue

            first_code = col6.split(" / ")[0].strip().lstrip("#")
            if first_code in seen_codes:
                continue
            seen_codes.add(first_code)

            data_rows.append(DataRow(
                row=row,
                code=col6,
                name=col9,
                is_primary=is_primary,
            ))

        return data_rows

    def extract_compatibility_pairs(self, ws, row_num, step_row, cat_row=None):
        """
        从第 40+ 列提取某数据行的兼容性数据对。

        三层表头结构：
            Row cat_row  : 组分类 (印刷、烫金等)
            Row step_row : 单个工序名称

        Returns:
            list[tuple]: (step_name, status) 列表，仅包含 V/X/▷/O 值。
        """
        pairs = []

        max_col = min(ws.max_column, self.config.col_end)

        for col in range(self.config.col_start, max_col + 1):
            v = self.cell_text(ws, row_num, col)
            if not v:
                continue

            raw = v.strip()
            if raw in ("V", "X", "▷", "O"):
                step_name = self.cell_text(ws, step_row, col)
                if step_name:
                    if cat_row:
                        cat_name = self.cell_text(ws, cat_row, col)
                        if cat_name and cat_name != step_name:
                            if len(cat_name) <= 4 and cat_name in self.config.category_keywords:
                                full_name = f"{cat_name}/{step_name}"
                            else:
                                full_name = step_name
                        else:
                            full_name = step_name
                    else:
                        full_name = step_name

                    pairs.append((full_name, raw))

        return pairs
