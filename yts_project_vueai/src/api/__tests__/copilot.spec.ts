import { describe, it, expect } from 'vitest'
import { extractSources } from '@/api/modules/copilot'

describe('extractSources', () => {
  it('正常提取来源标记', () => {
    const reply = `根据数据库查询结果：
[来源: 物料兼容性表-烫金]
[来源: 产品规格书-v2]
该物料与烫金工艺兼容。`
    const result = extractSources(reply)
    expect(result.sources).toHaveLength(2)
    expect(result.sources[0]).toEqual({ title: '物料兼容性表-烫金' })
    expect(result.sources[1]).toEqual({ title: '产品规格书-v2' })
    expect(result.cleanReply).toBe('根据数据库查询结果：\n该物料与烫金工艺兼容。')
  })

  it('空回复返回空结果', () => {
    const result = extractSources('')
    expect(result.sources).toHaveLength(0)
    expect(result.cleanReply).toBe('')
  })

  it('无来源标记时返回原始文本', () => {
    const reply = '這是一個普通的回覆，沒有來源標記。'
    const result = extractSources(reply)
    expect(result.sources).toHaveLength(0)
    expect(result.cleanReply).toBe(reply)
  })

  it('来源标记带置信度', () => {
    const reply = '[来源: 工艺数据库]'
    const result = extractSources(reply)
    expect(result.sources).toHaveLength(1)
    expect(result.sources[0].title).toBe('工艺数据库')
    expect(result.sources[0].confidence).toBeUndefined()
  })

  it('处理只有来源标记的行', () => {
    const result = extractSources('[来源: 测试文档]')
    expect(result.sources).toHaveLength(1)
    expect(result.sources[0].title).toBe('测试文档')
    expect(result.cleanReply).toBe('')
  })
})
