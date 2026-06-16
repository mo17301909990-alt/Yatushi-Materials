#!/usr/bin/env python3
"""
P0 ETL Script — Parse 新物料適用性指引書 xlsx files from G: drive and import into PostgreSQL.

Usage:
    python p0_etl.py                          # Full import
    python p0_etl.py --dry-run                # Print what would be done
    python p0_etl.py --category silicone_glow_ink  # Single category
    python p0_etl.py --validate               # Run SELECT COUNT(*) on all tables
    python p0_etl.py --create-tables-only     # Only create tables, no import
"""

import argparse
import glob
import os
import sys
from collections import OrderedDict

import openpyxl
from header_detector import HeaderDetector
from models import CategoryDef, Product, DataRow, CategoryStats
from handlers import get_handler, exec_sql, log

# ──────────────────────────────────────────────
# DB Configuration
# ──────────────────────────────────────────────
DB_HOST = os.environ.get("PGHOST", "localhost")
DB_PORT = int(os.environ.get("PGPORT", "5432"))
DB_NAME = os.environ.get("PGDATABASE", "gold_foil_db")
DB_USER = os.environ.get("PGUSER", "postgres")
DB_PASS = os.environ.get("PGPASSWORD", "HryENprJrxThYSDz")

BASE_DIR = r"G:/雅图仕/download"

# ──────────────────────────────────────────────
# CATEGORIES — 15 P0 categories (OrderedDict)
# Each entry:
#   key = short slug
#   value = {
#       dir: subdirectory under BASE_DIR,
#       glob: file pattern,
#       data_count: known rows (0 = empty, create empty tables),
#       product_table: PG product table name,
#       compat_table: PG compat table name,
#       schema_type: "silicone" | "yaguang" | "uv_oil_matte" | "water_varnish" | "lamination",
#       subtype: For schema_type=silicone, the specific file subtype pattern
#   }
# ──────────────────────────────────────────────
CATEGORIES = OrderedDict()

# --- Silicone (硅胶) — 9 subtypes, 7 with data ---
CATEGORIES["silicone_glow_ink"] = CategoryDef(
    dir="硅胶",
    glob="*发光油墨*",
    data_count=2,
    product_table="silicone_glow_ink_product",
    compat_table="silicone_glow_ink_compatibility",
    schema_type="silicone",
)

CATEGORIES["silicone_white_uv"] = CategoryDef(
    dir="硅胶",
    glob="*白UV*",
    data_count=2,
    product_table="silicone_white_uv_product",
    compat_table="silicone_white_uv_compatibility",
    schema_type="silicone",
)

CATEGORIES["silicone_glittering_star"] = CategoryDef(
    dir="硅胶",
    glob="*Glittering-Star*",
    data_count=7,
    product_table="silicone_glittering_star_product",
    compat_table="silicone_glittering_star_compatibility",
    schema_type="silicone",
)

CATEGORIES["silicone_screen_uv"] = CategoryDef(
    dir="硅胶",
    glob="*丝印UV*",
    data_count=5,
    product_table="silicone_screen_uv_product",
    compat_table="silicone_screen_uv_compatibility",
    schema_type="silicone",
)

CATEGORIES["silicone_led_uv_spray"] = CategoryDef(
    dir="硅胶",
    glob="*LED UV喷涂*",
    data_count=1,
    product_table="silicone_led_uv_spray_product",
    compat_table="silicone_led_uv_spray_compatibility",
    schema_type="silicone",
)

CATEGORIES["silicone_water_base_transparent"] = CategoryDef(
    dir="硅胶",
    glob="*水性透明白墨*",
    data_count=1,
    product_table="silicone_water_base_transparent_product",
    compat_table="silicone_water_base_transparent_compatibility",
    schema_type="silicone",
)

# Silicone subtypes with NO data rows (need empty tables for CRUD)
CATEGORIES["silicone_coarse_uv"] = CategoryDef(
    dir="硅胶",
    glob="*粗纹UV*",
    data_count=0,
    product_table="silicone_coarse_uv_product",
    compat_table="silicone_coarse_uv_compatibility",
    schema_type="silicone",
)

CATEGORIES["silicone_orange_peel_uv"] = CategoryDef(
    dir="硅胶",
    glob="*桔纹UV*",
    data_count=0,
    product_table="silicone_orange_peel_uv_product",
    compat_table="silicone_orange_peel_uv_compatibility",
    schema_type="silicone",
)

CATEGORIES["silicone_sandblast_uv"] = CategoryDef(
    dir="硅胶",
    glob="*磨砂UV*",
    data_count=0,
    product_table="silicone_sandblast_uv_product",
    compat_table="silicone_sandblast_uv_compatibility",
    schema_type="silicone",
)

CATEGORIES["silicone_wrinkle_uv"] = CategoryDef(
    dir="硅胶",
    glob="*皱纹UV*",
    data_count=0,
    product_table="silicone_wrinkle_uv_product",
    compat_table="silicone_wrinkle_uv_compatibility",
    schema_type="silicone",
)

CATEGORIES["silicone_watercolor_ink"] = CategoryDef(
    dir="硅胶",
    glob="*水彩油墨*",
    data_count=0,
    product_table="silicone_watercolor_ink_product",
    compat_table="silicone_watercolor_ink_compatibility",
    schema_type="silicone",
)

CATEGORIES["silicone_mica_pearl"] = CategoryDef(
    dir="硅胶",
    glob="*Mica Pearl*",
    data_count=0,
    product_table="silicone_mica_pearl_product",
    compat_table="silicone_mica_pearl_compatibility",
    schema_type="silicone",
)

# --- UV Ink (UV油墨) ---
CATEGORIES["uv_oil_matte"] = CategoryDef(
    dir="UV油墨",
    glob="*哑光UV油*",
    data_count=12,
    product_table="uv_oil_matte_product",
    compat_table="uv_oil_matte_compatibility",
    schema_type="uv_oil_matte",
)

CATEGORIES["water_varnish_matte"] = CategoryDef(
    dir="UV油墨",
    glob="*哑光水油*",
    data_count=36,
    product_table="water_varnish_matte_product",
    compat_table="water_varnish_matte_compatibility",
    schema_type="water_varnish",
)

# --- Lamination (印刷加工) — reference table, NOT product+compat ---
CATEGORIES["lamination_material"] = CategoryDef(
    dir="印刷加工",
    glob="*后加工资料标准书*",
    data_count=24,
    product_table="lamination_material_products",
    compat_table="lamination_material_compatibility",
    schema_type="lamination",
)

# ──────────────────────────────────────────────
# Database Operations
# ──────────────────────────────────────────────

def get_connection():
    """Get a psycopg2 database connection."""
    try:
        import psycopg2
        conn = psycopg2.connect(
            host=DB_HOST,
            port=DB_PORT,
            dbname=DB_NAME,
            user=DB_USER,
            password=DB_PASS,
        )
        conn.autocommit = False
        return conn
    except ImportError:
        log("psycopg2 not available, trying psycopg2-binary...")
        import subprocess
        subprocess.check_call([sys.executable, "-m", "pip", "install", "psycopg2-binary"])
        import psycopg2
        conn = psycopg2.connect(
            host=DB_HOST,
            port=DB_PORT,
            dbname=DB_NAME,
            user=DB_USER,
            password=DB_PASS,
        )
        conn.autocommit = False
        return conn


def create_tables(conn, category_key, cat_info):
    """Create product and compatibility tables for a category via handler."""
    handler = get_handler(cat_info.schema_type)
    handler.create_tables(conn, cat_info)


# ──────────────────────────────────────────────
# File Processing
# ──────────────────────────────────────────────

def find_files(cat_info):
    """Find files matching the category glob pattern."""
    dir_path = os.path.join(BASE_DIR, cat_info.dir)
    if not os.path.isdir(dir_path):
        log(f"  Directory not found: {dir_path}")
        return []

    pattern = os.path.join(dir_path, cat_info.glob)
    files = glob.glob(pattern)

    # Filter out temp files (starting with ~$)
    files = [f for f in files if not os.path.basename(f).startswith("~$")]

    return sorted(files)


def process_category(conn, category_key, cat_info, dry_run=False):
    """Process all files for one category."""
    detector = HeaderDetector()
    log(f"\n{'='*60}")
    log(f"Category: {category_key}")
    log(f"{'='*60}")

    files = find_files(cat_info)
    if not files:
        log(f"  No matching files found (glob: {cat_info.glob} in {cat_info.dir})")
        return CategoryStats()

    log(f"  Found {len(files)} file(s): {[os.path.basename(f) for f in files]}")

    stats = CategoryStats()
    handler = get_handler(cat_info.schema_type)

    for filepath in files:
        stats.files += 1
        fname = os.path.basename(filepath)
        log(f"\n  --- Processing: {fname} ---")

        try:
            wb = openpyxl.load_workbook(filepath, data_only=True)
            ws = wb[wb.sheetnames[0]]
            ws = detector.find_sheet(ws)
        except Exception as e:
            log(f"    Error opening file: {e}")
            stats.skipped += 1
            continue

        # Find step header row
        step_row = detector.find_step_header_row(ws)
        if step_row is None:
            log(f"    Could not find step header row, skipping")
            stats.skipped += 1
            continue

        cat_row = detector.find_category_header_row(ws)
        log(f"    Step header at row {step_row}, category header at row {cat_row}")

        # Find data rows
        data_rows = detector.find_data_rows(ws, step_row)
        log(f"    Found {len(data_rows)} data rows")

        if not data_rows:
            continue

        # Unified processing via handler strategy
        for dr in data_rows:
            row = dr.row
            product = handler.extract_product(ws, row)
            product.material_code = dr.code

            if dry_run:
                log(f"    [DRY-RUN] Row {row}: {dr.code[:40]} -> {product.material_name}")
                stats.products += 1
                pairs = detector.extract_compatibility_pairs(ws, row, step_row, cat_row)
                log(f"      {len(pairs)} compatibility entries")
                stats.compat += len(pairs)
                continue

            pid = handler.insert_product(conn, product, cat_info)
            if pid:
                stats.products += 1

                pairs = detector.extract_compatibility_pairs(ws, row, step_row, cat_row)
                for step_name, status in pairs:
                    handler.insert_compat(conn, pid, step_name, status, cat_info)
                    stats.compat += 1
            else:
                stats.duplicates += 1

        try:
            conn.commit()
        except Exception as e:
            log(f"    Commit error: {e}")
            conn.rollback()

        wb.close()

    log(f"\n  Results: {stats.products} products, {stats.compat} compat pairs, "
        f"{stats.duplicates} duplicates, {stats.skipped} skipped")
    return stats


def validate_tables(conn):
    """Run SELECT COUNT(*) on all P0 tables."""
    log("\n" + "=" * 60)
    log("Validation: SELECT COUNT(*) on all P0 tables")
    log("=" * 60)

    # Collect all table names
    tables = set()
    for key, info in CATEGORIES.items():
        tables.add(info.product_table)
        if info.compat_table:
            tables.add(info.compat_table)

    # Rollback first to clear any stale transaction state
    conn.rollback()

    for table in sorted(tables):
        try:
            result = exec_sql(conn, f"SELECT COUNT(*) FROM {table}")
            count = result[0][0] if result else 0
            log(f"  {table}: {count} rows")
        except Exception as e:
            log(f"  {table}: ERROR - {e}")
            conn.rollback()  # Clear error for next query


def create_tables_only(conn):
    """Create all tables without importing data."""
    log(f"\n{'='*60}")
    log("Creating all tables (no import)")
    log(f"{'='*60}")

    for key, info in CATEGORIES.items():
        log(f"\nCategory: {key}")
        create_tables(conn, key, info)


# ──────────────────────────────────────────────
# Main
# ──────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(description="P0 ETL — Import xlsx files to PostgreSQL")
    parser.add_argument("--dry-run", action="store_true", help="Print what would be done without importing")
    parser.add_argument("--category", type=str, default=None, help="Only process one category (slug name)")
    parser.add_argument("--validate", action="store_true", help="Run SELECT COUNT(*) on all tables")
    parser.add_argument("--create-tables-only", action="store_true", help="Create tables without importing data")
    args = parser.parse_args()

    log(f"Starting P0 ETL")
    log(f"  DB: {DB_HOST}:{DB_PORT}/{DB_NAME}")
    log(f"  Source: {BASE_DIR}")

    if args.validate:
        conn = get_connection()
        try:
            validate_tables(conn)
        finally:
            conn.close()
        return

    if args.create_tables_only:
        conn = get_connection()
        try:
            create_tables_only(conn)
            conn.commit()
        except Exception as e:
            log(f"Error creating tables: {e}")
            conn.rollback()
        finally:
            conn.close()
        return

    # Main import flow
    tot_products = 0
    tot_compat = 0
    tot_duplicates = 0
    tot_skipped = 0
    tot_files = 0

    conn = get_connection()
    try:
        # Create all tables first
        log(f"\n{'='*60}")
        log("Phase 1: Creating tables")
        log(f"{'='*60}")
        create_tables_only(conn)

        log(f"\n{'='*60}")
        log("Phase 2: Importing data")
        log(f"{'='*60}")

        for key, info in CATEGORIES.items():
            if args.category and key != args.category:
                continue

            stats = process_category(conn, key, info, dry_run=args.dry_run)
            tot_files += stats.files
            tot_products += stats.products
            tot_compat += stats.compat
            tot_duplicates += stats.duplicates
            tot_skipped += stats.skipped

    except Exception as e:
        log(f"Fatal error: {e}")
        conn.rollback()
        raise
    finally:
        conn.close()

    log(f"\n{'='*60}")
    log("SUMMARY")
    log(f"{'='*60}")
    if args.dry_run:
        log(f"  DRY RUN — no data written")
    log(f"  Files processed: {tot_files}")
    log(f"  Products inserted: {tot_products}")
    log(f"  Compatibility pairs: {tot_compat}")
    log(f"  Duplicates skipped: {tot_duplicates}")
    log(f"  Files skipped: {tot_skipped}")
    log(f"Done.")


# ──────────────────────────────────────────────
# CLI entry
# ──────────────────────────────────────────────

if __name__ == "__main__":
    main()
