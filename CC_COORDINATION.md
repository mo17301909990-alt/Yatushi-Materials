# CC 协调中心

多 Claude Code 实例共享此文件实现状态同步。

## 定位方式

所有 CC 实例应从此路径读取/写入本文件：
```
<repo-root>/CC_COORDINATION.md
```

对于工作树中的 CC，等价路径为：
```
cd $(git rev-parse --git-common-dir)/.. && pwd  # 得到 repo 根目录
```

## 协议

### 1. 启停信号

每次 CC 开始/结束工作时更新 `status` 段：

```json
{
  "cc_instances": [
    {
      "id": "cc-1",
      "name": "别名或分支名",
      "worktree": "saojinzhi",
      "task": "当前任务简述",
      "started_at": "2026-06-10T13:11:00+08:00",
      "status": "active | idle | done",
      "files_touching": ["相对路径1", "相对路径2"]
    }
  ]
}
```

### 2. 文件锁

同时只允许一个 CC 修改同一文件。按以下规则规避冲突：

- **写之前**：检查 `files_touching` 中有无冲突路径
- **发现冲突**：不要改，在 `messages` 段留言协商
- **读之前**：无需操作

### 3. 消息板

```json
{
  "messages": [
    {
      "from": "cc-1",
      "to": "cc-2 | all",
      "msg": "内容",
      "ts": "2026-06-10T13:11:00+08:00",
      "resolved": false
    }
  ]
}
```

CC 启动时检查 `resolved: false` 且指向自己的消息。

## 重要说明：工作目录统一

**所有 CC 实例必须操作同一目录：`F:\yatushi-6-6`**
- 这是 GitHub `main` 分支的本地副本，PR #1 已合并
- 回路文件位于 `.claude/loops/`
- 旧目录 `F:\YTS+JYY` 废弃，不再使用

## 当前状态

```json
{
  "cc_instances": [
    {
      "id": "cc-main",
      "name": "主 CC",
      "worktree": "F:\\yatushi-6-6 (main)",
      "task": "回路体系运维：开发质量回路(10min) + 数据质量回路(daily)",
      "started_at": "2026-06-13T18:10:00+08:00",
      "status": "active",
      "files_touching": [
        ".claude/loops/loop-registry.json",
        ".claude/loops/data-quality-loop.sh",
        ".claude/loops/record-error.sh",
        ".claude/loops/dev-quality.log",
        ".claude/loops/data-quality.log"
      ]
    }
  ],
  "messages": [
    {
      "from": "cc-main",
      "to": "all",
      "msg": "回路体系在 F:\\yatushi-6-6\\.claude\\loops/。工作目录统一为 F:\\yatushi-6-6，旧 F:\\YTS+JYY 废弃不用。",
      "ts": "2026-06-13T18:10:00+08:00",
      "resolved": false
    }
  ]
}
```
