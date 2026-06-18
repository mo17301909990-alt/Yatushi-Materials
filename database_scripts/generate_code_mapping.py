#!/usr/bin/env python3
"""
自动生成初始编码映射 — Phase 0.3 物料编码对齐

遍历 P0 产品表（15 组 *_product 表 + 1 组 lamination_material_products），
对每条记录自动匹配到 production 系统（material_catalog / hot_stamping_compatibility），
将 INSERT 写入 code_mapping 表。
"""

import re
import sys

import psycopg2

# ── 数据库连接配置 ──────────────────────────────────────────────
DB_CONFIG = {
    "host": "localhost",
    "port": 5432,
    "dbname": "gold_foil_db",
    "user": "postgres",
    "password": "HryENprJrxThYSDz",
}

# ── P0 产品表（15 组 product + 1 组 lamination）───────────────
P0_TABLES = [
    "silicone_coarse_uv_product",
    "silicone_glittering_star_product",
    "silicone_glow_ink_product",
    "silicone_led_uv_spray_product",
    "silicone_mica_pearl_product",
    "silicone_orange_peel_uv_product",
    "silicone_sandblast_uv_product",
    "silicone_screen_uv_product",
    "silicone_water_base_transparent_product",
    "silicone_watercolor_ink_product",
    "silicone_white_uv_product",
    "silicone_wrinkle_uv_product",
    "uv_oil_matte_product",
    "water_varnish_matte_product",
    "yaguang_uv_oil_product",
    "lamination_material_products",
]

# ── hot_stamping_compatibility 的 paper_performance 精确值 ────
PAPER_PERFORMANCE_VALUES = ["高耐磨", "普通耐磨", "普通金紙", "鐳射燙金紙"]


def extract_keywords(name: str) -> list[str]:
    """从物料名称中提取有意义的关键词。

    规则：
    - 去掉 "#" 开头的物料编号（如 #UV-SS-180501）
    - 去掉 / #SS-250201 这类附属编号
    - 去掉纯数字片段 / 编号片段
    - 保留有意义的中文/英文词
    """
    if not name:
        return []

    text = name

    # 去掉 "#" 开头的编码（如 #UV-SS-180501, #FL-200701）
    text = re.sub(r'#[\w\-/]+', '', text)

    # 去掉 "/ #" 或 " / #" 后面的附属编号
    text = re.sub(r'/ #?[\w\-/]+', '', text)
    text = re.sub(r'（[^）]*耐磨[^）]*）', '', text)  # 去掉括号内的耐磨说明

    # 替换特殊字符为空格
    text = re.sub(r'[#/()（）【】\[\]{}、，。：；！？""''　]+', ' ', text)

    # 切分并过滤
    parts = text.strip().split()
    keywords = []
    for p in parts:
        p = p.strip()
        if not p:
            continue
        # 跳过纯数字/编号
        if re.match(r'^[\d\-]+$', p):
            continue
        if p not in keywords:
            keywords.append(p)

    # 回退：如果什么都没提取到，保留原始名称（去特殊字符）
    if not keywords:
        # 去掉剩余特殊字符
        cleaned = re.sub(r'[#/()（）【】\[\]{}、，。：；！？""\' ]', '', name)
        if cleaned:
            keywords.append(cleaned)

    return keywords


def search_catalog(cursor, keyword: str) -> list[tuple]:
    """在 material_catalog 中 ILIKE 搜索关键词。"""
    cursor.execute(
        "SELECT id, material_code, material_name FROM material_catalog "
        "WHERE material_name ILIKE %s ORDER BY id",
        (f"%{keyword}%",),
    )
    return cursor.fetchall()


def match_paper_performance(name: str) -> tuple[str | None, str | None, float]:
    """如果名称含 '耐磨'，尝试映射到 paper_performance。

    Returns:
        (paper_performance_value, "高耐磨"等, confidence)
        或 (None, None, 0.0)
    """
    if "耐磨" not in name:
        return None, None, 0.0

    for pp in PAPER_PERFORMANCE_VALUES:
        if pp in name:
            return pp, pp, 0.9

    # 包含"耐磨"但未匹配到具体值 → 默认高耐磨，低置信度
    return "高耐磨", "高耐磨", 0.5


def query_all(conn, table_name: str) -> list[tuple]:
    """读取指定表的所有记录。"""
    with conn.cursor() as cur:
        cur.execute(
            f'SELECT id, material_code, material_name FROM "{table_name}" ORDER BY id'
        )
        return cur.fetchall()


def insert_mapping(conn, table_name: str, row_id: int, material_code: str | None,
                   material_name: str, target_type: str, target_id: int | None,
                   target_code: str, target_name: str, confidence: float,
                   notes: str) -> bool:
    """执行 INSERT，返回是否成功插入。"""
    safe_table = table_name.replace("'", "''")
    safe_code = (material_code or "").replace("'", "''")
    safe_name = material_name.replace("'", "''")
    safe_tcode = target_code.replace("'", "''")
    safe_tname = target_name.replace("'", "''")
    safe_notes = notes.replace("'", "''")

    tid_str = str(target_id) if target_id is not None else "NULL"

    sql = (
        "INSERT INTO code_mapping "
        "(p0_table_name, p0_row_id, p0_material_code, p0_material_name, "
        " target_type, target_id, target_code, target_name, "
        " match_type, confidence, notes) "
        "VALUES ("
        f"'{safe_table}', {row_id}, '{safe_code}', '{safe_name}', "
        f"'{target_type}', {tid_str}, '{safe_tcode}', '{safe_tname}', "
        f"'auto', {confidence}, '{safe_notes}'"
        ") ON CONFLICT (p0_table_name, p0_row_id, target_type) DO NOTHING;"
    )
    with conn.cursor() as cur:
        try:
            cur.execute(sql)
            return cur.rowcount is not None and cur.rowcount > 0
        except Exception as e:
            print(f"-- [错误] {table_name}#{row_id} ({material_name}): {e}", file=sys.stderr)
            return False


def main():
    conn = psycopg2.connect(**DB_CONFIG)

    inserted = 0
    no_match_count = 0
    conflict_count = 0

    for table_name in P0_TABLES:
        rows = query_all(conn, table_name)

        for row_id, material_code, material_name in rows:
            if not material_name:
                continue

            name = material_name.strip()
            keywords = extract_keywords(name)
            if not keywords:
                continue

            # ── Step 1: 匹配 material_catalog ─────────────────
            # 对每个关键词搜索 material_catalog
            catalog_matches: dict[str, list[tuple]] = {}
            with conn.cursor() as cur:
                for kw in keywords:
                    matches = search_catalog(cur, kw)
                    if matches:
                        catalog_matches[kw] = matches

            best_catalog = None  # (id, code, name, score)

            if catalog_matches:
                all_sets = [set(m[0] for m in kw_matches) for kw_matches in catalog_matches.values()]

                if len(all_sets) > 1:
                    common_ids = set.intersection(*all_sets)
                else:
                    common_ids = all_sets[0]

                if common_ids:
                    # 所有关键词都命中同一批 catalog 记录 → 完全匹配 0.9
                    mid = next(iter(common_ids))
                    with conn.cursor() as cur:
                        cur.execute(
                            "SELECT material_code, material_name FROM material_catalog WHERE id = %s",
                            (mid,),
                        )
                        mc = cur.fetchone()
                        if mc:
                            best_catalog = (mid, mc[0], mc[1], 0.9)
                else:
                    # 部分匹配 → 取第一个有关键词匹配的 catalog 记录 0.5
                    for kw_matches in catalog_matches.values():
                        m = kw_matches[0]
                        best_catalog = (m[0], m[1], m[2], 0.5)
                        break

            # ── Step 2: 匹配 paper_performance ────────────────
            pp_value, pp_name, pp_conf = match_paper_performance(name)

            # ── Step 3: 选最佳候选 ────────────────────────────
            candidates = []
            if best_catalog:
                candidates.append(("material_catalog", best_catalog[0],
                                   best_catalog[1], best_catalog[2], best_catalog[3]))
            if pp_value:
                candidates.append(("paper_performance", None,
                                   pp_value, pp_name, pp_conf))

            if candidates:
                candidates.sort(key=lambda c: c[4], reverse=True)
                target_type, target_id, target_code, target_name, confidence = candidates[0]

                if confidence >= 0.9:
                    notes = "自动匹配: 关键词完全匹配"
                elif confidence >= 0.5:
                    notes = "自动匹配: 关键词部分匹配"
                else:
                    notes = "自动匹配: 低置信度"

                ok = insert_mapping(conn, table_name, row_id, material_code, name,
                                    target_type, target_id, target_code, target_name,
                                    confidence, notes)
                if ok:
                    inserted += 1
                else:
                    conflict_count += 1
            else:
                # 无匹配 → 插入置信度 0 的记录供人工确认
                ok = insert_mapping(conn, table_name, row_id, material_code, name,
                                    "material_catalog", None, "", "",
                                    0.0, "待人工确认: 未找到自动匹配")
                if ok:
                    no_match_count += 1
                else:
                    conflict_count += 1

    conn.commit()
    conn.close()

    print(f"-- 完成! 插入 {inserted} 条映射, "
          f"{conflict_count} 条重复跳过, "
          f"{no_match_count} 条无匹配(已插入待确认记录).")
    return inserted


if __name__ == "__main__":
    count = main()
    print(count)
