"""
Batch convert Simplified Chinese to Traditional Chinese in source code files.
Only converts CJK character runs, preserves code/tags/variables exactly.
"""
import os, re, sys

try:
    from opencc import OpenCC
    cc = OpenCC('s2t')
except ImportError:
    print("Install opencc: /e/anaconda/pip install opencc-python-reimplemented")
    sys.exit(1)

BACKEND = r"F:/YTS+JYY/yts_project/src"
FRONTEND = r"F:/YTS+JYY/yts_project_vueai/src"
SKIP_DIRS = {'node_modules', '.git', '__pycache__', 'dist', '.vscode'}

# Match contiguous runs of CJK characters (including common Chinese punctuation)
CJK_RE = re.compile(r'[一-鿿㐀-䶿豈-﫿　-〿＀-￯]+')

def convert_file(filepath):
    """Convert Simplified Chinese to Traditional in a file. Returns number of changes."""
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
    except:
        try:
            with open(filepath, 'r', encoding='gbk') as f:
                content = f.read()
        except:
            return 0

    new_content = []
    changes = 0

    for line in content.split('\n'):
        new_line = line
        matches = list(CJK_RE.finditer(line))
        if not matches:
            new_content.append(line)
            continue

        # Process from right to left to preserve positions
        offset = 0
        for m in matches:
            start, end = m.start() + offset, m.end() + offset
            orig = new_line[start:end]
            converted = cc.convert(orig)
            if converted != orig:
                new_line = new_line[:start] + converted + new_line[end:]
                offset += len(converted) - len(orig)
                changes += 1
        new_content.append(new_line)

    if changes > 0:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write('\n'.join(new_content))

    return changes

def main():
    sys.stdout.reconfigure(encoding='utf-8')
    total_changes = 0
    total_files = 0
    errors = []

    for label, root in [("Backend Java", BACKEND), ("Frontend Vue/TS", FRONTEND)]:
        print(f"\n=== {label} ===")
        for dirpath, dirnames, filenames in os.walk(root):
            dirnames[:] = [d for d in dirnames if d not in SKIP_DIRS]
            for f in filenames:
                if not (f.endswith('.java') or f.endswith('.vue') or f.endswith('.ts')):
                    continue
                fpath = os.path.join(dirpath, f)
                try:
                    c = convert_file(fpath)
                    if c > 0:
                        total_files += 1
                        total_changes += c
                        rel = os.path.relpath(fpath, root)
                        print(f"  {rel}: {c} changes")
                except Exception as e:
                    errors.append((fpath, str(e)))

    print(f"\n{'=' * 60}")
    print(f"Done: {total_files} files modified, {total_changes} total changes")
    if errors:
        print(f"Errors: {len(errors)}")
        for f, e in errors[:5]:
            print(f"  {f}: {e}")
    print(f"{'=' * 60}")

if __name__ == '__main__':
    main()
