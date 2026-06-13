"""
Scan project source code for Simplified Chinese text.
Scans .java/.vue/.ts files, uses opencc s2t to detect simplified chars.
"""
import os, sys

try:
    from opencc import OpenCC
    cc = OpenCC('s2t')
except ImportError:
    print("Install opencc: /e/anaconda/pip install opencc-python-reimplemented")
    sys.exit(1)

BACKEND = r"F:/YTS+JYY/yts_project/src"
FRONTEND = r"F:/YTS+JYY/yts_project_vueai/src"
SKIP_DIRS = {'node_modules', '.git', '__pycache__', 'dist', '.vscode'}

def is_chinese_char(c):
    return '一' <= c <= '鿿'

def extract_chinese(text):
    results = []
    for lineno, line in enumerate(text.split('\n'), 1):
        chinese_chars = [c for c in line if is_chinese_char(c)]
        if chinese_chars:
            results.append((lineno, line.strip(), ''.join(chinese_chars)))
    return results

def scan_file(filepath):
    findings = []
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
    except:
        try:
            with open(filepath, 'r', encoding='gbk') as f:
                content = f.read()
        except:
            return findings

    chinese_lines = extract_chinese(content)
    for lineno, line, chinese in chinese_lines:
        try:
            converted = cc.convert(chinese)
            if chinese != converted:
                findings.append((lineno, line.strip(), chinese, converted))
        except:
            pass
    return findings

def scan_dir(root_dir):
    all_findings = {}
    for dirpath, dirnames, filenames in os.walk(root_dir):
        dirnames[:] = [d for d in dirnames if d not in SKIP_DIRS]
        for f in filenames:
            if not (f.endswith('.java') or f.endswith('.vue') or f.endswith('.ts')):
                continue
            fpath = os.path.join(dirpath, f)
            relpath = os.path.relpath(fpath, root_dir)
            findings = scan_file(fpath)
            if findings:
                all_findings[relpath] = findings
    return all_findings

def main():
    sys.stdout.reconfigure(encoding='utf-8')
    total_files = 0
    total_simplified = 0

    for label, root in [("Backend Java", BACKEND), ("Frontend Vue/TS", FRONTEND)]:
        print(f"\n=== {label} ===")
        findings = scan_dir(root)
        for fpath, lines in sorted(findings.items()):
            total_files += 1
            total_simplified += len(lines)
            print(f"\n  [FILE] {fpath} ({len(lines)} locations)")
            for lineno, line, orig, conv in lines[:5]:
                print(f"    L{lineno}: {line[:80]}")
            if len(lines) > 5:
                print(f"    ... and {len(lines) - 5} more")

    print(f"\n{'=' * 60}")
    print(f"Total: {total_files} files with Simplified Chinese, {total_simplified} locations")
    print(f"{'=' * 60}")

if __name__ == '__main__':
    main()
