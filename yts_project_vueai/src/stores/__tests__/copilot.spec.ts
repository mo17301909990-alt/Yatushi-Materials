import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useCopilotStore } from '@/stores/copilot'

describe('copilot store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  it('初始化后应有欢迎消息', () => {
    const store = useCopilotStore()
    expect(store.messages).toHaveLength(1)
    expect(store.messages[0].role).toBe('assistant')
    expect(store.messages[0].content).toContain('Copilot')
    expect(store.processing).toBe(false)
    expect(store.collapsed).toBe(false)
    expect(store.error).toBeNull()
  })

  it('clearMessages 重置状态', () => {
    const store = useCopilotStore()
    store.clearMessages()
    expect(store.messages).toHaveLength(1) // initWelcome 重新执行
    expect(store.messages[0].role).toBe('assistant')
    expect(store.stage).toBe('idle')
    expect(store.guideStepIndex).toBe(0)
    expect(store.guideFilters).toEqual({})
  })

  it('toggleCollapse 切换折叠状态', () => {
    const store = useCopilotStore()
    expect(store.collapsed).toBe(false)
    store.toggleCollapse()
    expect(store.collapsed).toBe(true)
    store.toggleCollapse()
    expect(store.collapsed).toBe(false)
  })

  it('setAgentRole 切换到 guide 时初始化引导状态', () => {
    const store = useCopilotStore()
    store.setAgentRole('guide')
    expect(store.agentRole).toBe('guide')
    expect(store.stage).toBe('selecting-product')
    expect(store.guideStepIndex).toBe(0)
    expect(store.guideFilters).toEqual({})
    // 应该有新的 assistant 消息
    const lastMsg = store.messages[store.messages.length - 1]
    expect(lastMsg.role).toBe('assistant')
    expect(lastMsg.content).toContain('產品類型')
  })

  it('setAgentRole 切换到非 guide 时重置引导状态', () => {
    const store = useCopilotStore()
    store.setAgentRole('guide')
    expect(store.stage).toBe('selecting-product')

    store.setAgentRole('general')
    expect(store.stage).toBe('idle')
    expect(store.guideStepIndex).toBe(0)
    expect(store.guideFilters).toEqual({})
  })

  it('setFilterParams 设置筛选参数', () => {
    const store = useCopilotStore()
    store.setFilterParams({ productTypeId: 1, patternId: 3 })
    expect(store.filterParams).toEqual({ productTypeId: 1, patternId: 3 })
  })

  it('pushInlineResults 添加商品结果消息', () => {
    const store = useCopilotStore()
    const products = [
      { id: 1, name: '金箔紙 A' },
      { id: 2, name: '金箔紙 B' },
    ] as any[]
    store.pushInlineResults(products, '找到以下匹配：')
    expect(store.messages).toHaveLength(2) // Welcome + 新消息
    const msg = store.messages[1]
    expect(msg.role).toBe('assistant')
    expect(msg.products).toHaveLength(2)
    expect(msg.suggestions).toContain('查看全部結果')
  })

  it('pushInlineResults 空数组不添加消息', () => {
    const store = useCopilotStore()
    const beforeCount = store.messages.length
    store.pushInlineResults([], '')
    expect(store.messages).toHaveLength(beforeCount)
  })
})
