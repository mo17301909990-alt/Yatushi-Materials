# /commit-push-pr — 提交推送流程

## 标准流程

```bash
# 1. 查看当前状态
git status

# 2. 添加文件（指定具体文件，禁止 git add .）
git add <file1> <file2>

# 3. 提交
git commit -m "type(scope): description"

# 4. 推送到远程
git push origin <branch>
```

## Commit 规范

```
<type>(<scope>): <description>

type: feat / fix / refactor / chore / docs / test / style
scope: 模块名（如 product / permission / hot-stamping / lamination）
```

示例:
```
feat(product): 添加Excel导入功能
fix(permission): 修复角色权限缓存未刷新
refactor(hot-stamping): 抽取兼容性匹配逻辑到独立服务
```

## 提 PR

```bash
gh pr create --title "<title>" --body "<description>"
gh pr view --web
```

## 红线

- 永不使用 `git add -A` 或 `git add .`
- 永不使用 `--no-verify` 跳过检查
- 永不 force push 到 master/main
