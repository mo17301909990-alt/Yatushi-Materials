/**
 * API 模块烟雾测试 — 自动发现所有 api/modules/*.ts 文件
 *
 * 验证每个模块：
 * 1. 可导入（文件存在、无语法错误）
 * 2. 导出的函数/变量存在
 *
 * 不测试业务逻辑，只做烟雾检查
 */
import { describe, it, expect } from 'vitest'
import * as fs from 'fs'
import * as path from 'path'

const modulesDir = path.resolve(__dirname, '../modules')
const files = fs.readdirSync(modulesDir)
  .filter(f => f.endsWith('.ts') && !f.endsWith('.d.ts'))
  .sort()

describe('API Modules Smoke', () => {
  it(`${files.length} 个 API 模块文件`, () => {
    expect(files.length).toBeGreaterThan(0)
  })

  it.each(files)('%s 可导入且暴露了导出', async (file) => {
    const mod = await import(`../modules/${file}`)
    const keys = Object.keys(mod)
    expect(keys.length).toBeGreaterThan(0)
    // 必须至少有一个可调用导出（函数或对象含 request 调用）
    const hasCallable = keys.some(k => typeof mod[k] === 'function' || typeof mod[k] === 'object')
    expect(hasCallable).toBe(true)
  })
})
