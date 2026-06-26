import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useMatchingStore } from '@/stores/matchingStore'

/**
 * Helper: create a valid MatchResult-like object with defaults.
 */
function makeMatchResult(overrides?: Record<string, unknown>) {
  return {
    name: '金箔紙 A',
    model: 'FO-100',
    id: '1',
    score: 95,
    features: ['耐高温', '高光泽'],
    scenarios: ['通用'],
    temperature: '110~130℃',
    width: '640mm',
    length: '120m',
    tension: '标准',
    costIndex: 2,
    ...overrides,
  }
}

describe('matching store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
  })

  // ── 1. Initial state ──────────────────────────────────────────

  describe('initial state', () => {
    it('matchingParams 所有字段初始为空字符串', () => {
      const store = useMatchingStore()
      const params = store.matchingParams
      // Spot-check representative fields
      expect(params.surfaceType).toBe('')
      expect(params.area).toBe('')
      expect(params.pattern).toBe('')
      expect(params.stampingType).toBe('')
      expect(params.hotStampingType).toBe('')
      expect(params.companyNumber).toBe('')
      expect(params.productType).toBe('')
      expect(params.priceLevel).toBe('')
      expect(params.flatHotStamping).toBe('')
      expect(params.oil6812Glossy33013440Dumb).toBe('')

      // All matchingParams keys are empty string
      const allEmpty = Object.values(params).every((v) => v === '')
      expect(allEmpty).toBe(true)
    })

    it('暴露的 ref 初始为 null', () => {
      const store = useMatchingStore()
      // 注: secondMatchResult 未暴露到 store 外部, 只通过 secondMatch getter 访问
      expect(store.searchMatchResult).toBeNull()
      expect(store.secondMatchParams).toBeNull()
      expect(store.thirdMatchParams).toBeNull()
      expect(store.thirdMatchResult).toBeNull()
      expect(store.firstMatchParams).toBeNull()
    })

    it('所有 getter 初始为 null 或空数组', () => {
      const store = useMatchingStore()
      expect(store.firstMatch).toBeNull()
      expect(store.secondMatch).toBeNull()
      expect(store.thirdMatch).toBeNull()
      expect(store.otherMatches).toEqual([])
    })
  })

  // ── 2. updateMatchingParams action ─────────────────────────────

  describe('updateMatchingParams', () => {
    it('合并局部参数到 matchingParams', () => {
      const store = useMatchingStore()
      store.updateMatchingParams({ surfaceType: '光面', area: '大面积' })
      expect(store.matchingParams.surfaceType).toBe('光面')
      expect(store.matchingParams.area).toBe('大面积')
      // 未更新的字段保持默认
      expect(store.matchingParams.pattern).toBe('')
    })

    it('多次调用逐步累积参数', () => {
      const store = useMatchingStore()
      store.updateMatchingParams({ pattern: '几何', stampingType: '平烫' })
      store.updateMatchingParams({ scenario: '包装', tension: '高张力' })
      expect(store.matchingParams.pattern).toBe('几何')
      expect(store.matchingParams.stampingType).toBe('平烫')
      expect(store.matchingParams.scenario).toBe('包装')
      expect(store.matchingParams.tension).toBe('高张力')
    })

    it('同名参数覆盖旧值', () => {
      const store = useMatchingStore()
      store.updateMatchingParams({ surfaceType: '光面' })
      store.updateMatchingParams({ surfaceType: '哑面' })
      expect(store.matchingParams.surfaceType).toBe('哑面')
    })

    it('传入空对象不改变任何字段', () => {
      const store = useMatchingStore()
      store.updateMatchingParams({})
      const allEmpty = Object.values(store.matchingParams).every((v) => v === '')
      expect(allEmpty).toBe(true)
    })

    it('不影响其他不相干的 ref', () => {
      const store = useMatchingStore()
      store.updateMatchingParams({ surfaceType: '光面' })
      expect(store.searchMatchResult).toBeNull()
      expect(store.firstMatchParams).toBeNull()
    })
  })

  // ── 3. setSearchMatchResult action ───────────────────────────

  describe('setSearchMatchResult', () => {
    it('设置有效结果, firstMatch 同步更新', () => {
      const store = useMatchingStore()
      const result = makeMatchResult({ id: 'search-1', score: 99 })
      store.setSearchMatchResult(result)
      expect(store.searchMatchResult).toEqual(result)
      expect(store.firstMatch).toStrictEqual(result)
    })

    it('传入 null 清除结果', () => {
      const store = useMatchingStore()
      store.setSearchMatchResult(makeMatchResult())
      store.setSearchMatchResult(null)
      expect(store.searchMatchResult).toBeNull()
      expect(store.firstMatch).toBeNull()
    })

    it('searchMatchResult 优先级高于 firstMatchParams', () => {
      const store = useMatchingStore()
      store.setFirstMatchParams({ paramName: '型号', paramValue: 'FO-200', displayName: '金箔' })
      store.setSearchMatchResult(makeMatchResult({ id: 'search-priority', score: 100 }))
      expect(store.firstMatch).not.toBeNull()
      expect(store.firstMatch!.id).toBe('search-priority')
    })
  })

  // ── 4. setSecondMatchResult action ──────────────────────────

  describe('setSecondMatchResult', () => {
    it('设置结果后 secondMatch getter 同步更新', () => {
      const store = useMatchingStore()
      const result = makeMatchResult({ id: 'second-1' })
      store.setSecondMatchResult(result)
      expect(store.secondMatch).toStrictEqual(result)
    })

    it('传入 null 清除结果', () => {
      const store = useMatchingStore()
      store.setSecondMatchResult(makeMatchResult())
      store.setSecondMatchResult(null)
      expect(store.secondMatch).toBeNull()
    })
  })

  // ── 5. setSecondMatchParams action ─────────────────────────

  describe('setSecondMatchParams', () => {
    it('设置参数对象', () => {
      const store = useMatchingStore()
      const params = { paramName: '型号', paramValue: 'FO-300' }
      store.setSecondMatchParams(params)
      expect(store.secondMatchParams).not.toBeNull()
      expect(store.secondMatchParams!.paramName).toBe('型号')
      expect(store.secondMatchParams!.paramValue).toBe('FO-300')
    })

    it('顶层属性通过解构拷贝, 外部修改不影响 store', () => {
      const store = useMatchingStore()
      const params = { paramName: '型号', paramValue: 'FO-300' }
      store.setSecondMatchParams(params)
      params.paramValue = 'CHANGED'
      expect(store.secondMatchParams!.paramValue).toBe('FO-300')
    })

    it('传入 null 清除参数', () => {
      const store = useMatchingStore()
      store.setSecondMatchParams({ paramName: 'x' })
      store.setSecondMatchParams(null)
      expect(store.secondMatchParams).toBeNull()
    })

    it('不影响其他参数', () => {
      const store = useMatchingStore()
      store.setFirstMatchParams({ paramName: '型号', paramValue: 'FO-100' })
      store.setSecondMatchParams(null)
      expect(store.firstMatchParams).not.toBeNull()
    })
  })

  // ── 6. setFirstMatchParams action ──────────────────────────

  describe('setFirstMatchParams', () => {
    it('设置并浅拷贝第一次匹配参数', () => {
      const store = useMatchingStore()
      const params = { paramName: '型号', paramValue: 'FO-100', displayName: '金箔紙 A' }
      store.setFirstMatchParams(params)
      expect(store.firstMatchParams).not.toBeNull()
      expect(store.firstMatchParams!.paramName).toBe('型号')
      expect(store.firstMatchParams!.paramValue).toBe('FO-100')
      // 顶层属性通过解构拷贝
      params.paramValue = 'CHANGED'
      expect(store.firstMatchParams!.paramValue).toBe('FO-100')
    })

    it('设置参数后 firstMatch getter 生成虚拟结果', () => {
      const store = useMatchingStore()
      store.setFirstMatchParams({ paramName: '型号', paramValue: 'FO-200', displayName: '金箔紙 B' })
      expect(store.firstMatch).not.toBeNull()
      expect(store.firstMatch!.name).toBe('金箔紙 B')
      expect(store.firstMatch!.model).toBe('FO-200')
      expect(store.firstMatch!.id).toContain('virtual-')
      expect(store.firstMatch!.score).toBe(100)
    })

    it('firstMatchParams 为 null 时清除虚拟结果', () => {
      const store = useMatchingStore()
      store.setFirstMatchParams({ paramName: '型号', paramValue: 'FO-200' })
      expect(store.firstMatch).not.toBeNull()
      store.setFirstMatchParams(null)
      expect(store.firstMatch).toBeNull()
    })

    it('displayName 缺省时虚拟结果使用默认名称', () => {
      const store = useMatchingStore()
      store.setFirstMatchParams({ paramName: '型号', paramValue: 'FO-300' })
      expect(store.firstMatch!.name).toBe('未知产品')
    })
  })

  // ── 7. setThirdMatchParams / setThirdMatchResult actions ───

  describe('setThirdMatchParams', () => {
    it('设置并浅拷贝第三次匹配参数', () => {
      const store = useMatchingStore()
      const params = { paramName: '型号', paramValue: 'FO-400' }
      store.setThirdMatchParams(params)
      params.paramValue = 'CHANGED'
      expect(store.thirdMatchParams!.paramValue).toBe('FO-400')
    })

    it('传入 null 清除参数', () => {
      const store = useMatchingStore()
      store.setThirdMatchParams({ paramName: 'x' })
      store.setThirdMatchParams(null)
      expect(store.thirdMatchParams).toBeNull()
    })
  })

  describe('setThirdMatchResult', () => {
    it('设置结果后 thirdMatch getter 同步更新', () => {
      const store = useMatchingStore()
      const result = makeMatchResult({ id: 'third-1' })
      store.setThirdMatchResult(result)
      expect(store.thirdMatchResult).toStrictEqual(result)
      expect(store.thirdMatch).toStrictEqual(result)
    })

    it('传入 null 清除结果', () => {
      const store = useMatchingStore()
      store.setThirdMatchResult(makeMatchResult())
      store.setThirdMatchResult(null)
      expect(store.thirdMatchResult).toBeNull()
      expect(store.thirdMatch).toBeNull()
    })
  })

  // ── 8. clearSearchResult action ────────────────────────────

  describe('clearSearchResult', () => {
    it('清除 searchMatchResult 并使 firstMatch 同步', () => {
      const store = useMatchingStore()
      store.setSearchMatchResult(makeMatchResult())
      store.clearSearchResult()
      expect(store.searchMatchResult).toBeNull()
      expect(store.firstMatch).toBeNull()
    })

    it('空状态下调用的安全', () => {
      const store = useMatchingStore()
      expect(() => store.clearSearchResult()).not.toThrow()
      expect(store.searchMatchResult).toBeNull()
    })
  })

  // ── 9. clearSecondMatchResult action ───────────────────────

  describe('clearSecondMatchResult', () => {
    it('清除后 secondMatch getter 返回 null', () => {
      const store = useMatchingStore()
      store.setSecondMatchResult(makeMatchResult())
      store.clearSecondMatchResult()
      expect(store.secondMatch).toBeNull()
    })

    it('不影响 secondMatchParams', () => {
      const store = useMatchingStore()
      store.setSecondMatchParams({ paramName: '型号', paramValue: 'FO-500' })
      store.setSecondMatchResult(makeMatchResult())
      store.clearSecondMatchResult()
      expect(store.secondMatchParams).not.toBeNull()
    })
  })

  // ── 10. resetAllParams action ────────────────────────────────

  describe('resetAllParams', () => {
    it('重置 matchingParams 所有字段为空字符串', () => {
      const store = useMatchingStore()
      store.updateMatchingParams({ surfaceType: '光面', area: '大面积', pattern: '几何' })
      store.resetAllParams()
      const allEmpty = Object.values(store.matchingParams).every((v) => v === '')
      expect(allEmpty).toBe(true)
    })

    it('重置所有 ref 和 getter', () => {
      const store = useMatchingStore()
      store.setSearchMatchResult(makeMatchResult())
      store.setSecondMatchResult(makeMatchResult())
      store.setSecondMatchParams({ paramName: 'x' })
      store.setFirstMatchParams({ paramName: 'x' })
      store.setThirdMatchParams({ paramName: 'x' })
      store.setThirdMatchResult(makeMatchResult())

      store.resetAllParams()

      expect(store.searchMatchResult).toBeNull()
      expect(store.secondMatchParams).toBeNull()
      expect(store.firstMatchParams).toBeNull()
      expect(store.thirdMatchParams).toBeNull()
      expect(store.thirdMatchResult).toBeNull()

      // getter 同步更新
      expect(store.firstMatch).toBeNull()
      expect(store.secondMatch).toBeNull()
      expect(store.thirdMatch).toBeNull()
    })

    it('已经是空状态时调用安全', () => {
      const store = useMatchingStore()
      expect(() => store.resetAllParams()).not.toThrow()
      expect(store.firstMatch).toBeNull()
    })
  })

  // ── 11. Getters ────────────────────────────────────────────

  describe('getters', () => {
    describe('firstMatch', () => {
      it('searchMatchResult 和 firstMatchParams 都为 null 时返回 null', () => {
        const store = useMatchingStore()
        expect(store.searchMatchResult).toBeNull()
        expect(store.firstMatchParams).toBeNull()
        expect(store.firstMatch).toBeNull()
      })

      it('searchMatchResult 非 null 时返回该结果', () => {
        const store = useMatchingStore()
        const result = makeMatchResult({ id: 'search-1' })
        store.setSearchMatchResult(result)
        expect(store.firstMatch).toStrictEqual(result)
      })

      it('firstMatchParams 有 paramName + paramValue 时生成虚拟结果', () => {
        const store = useMatchingStore()
        store.setFirstMatchParams({ paramName: '客户型号', paramValue: 'CM-001', displayName: '客户产品' })
        expect(store.firstMatch).not.toBeNull()
        expect(store.firstMatch!.name).toBe('客户产品')
        expect(store.firstMatch!.model).toBe('CM-001')
        expect(store.firstMatch!.score).toBe(100)
        expect(store.firstMatch!.features).toEqual(['虚拟匹配结果'])
        expect(store.firstMatch!.temperature).toBe('110~130℃')
        expect(store.firstMatch!.width).toBe('640mm')
        expect(store.firstMatch!.length).toBe('120m')
      })

      it('firstMatchParams 缺少 paramName 时返回 null', () => {
        const store = useMatchingStore()
        store.setFirstMatchParams({ paramValue: 'FO-100' })
        expect(store.firstMatch).toBeNull()
      })

      it('firstMatchParams 缺少 paramValue 时返回 null', () => {
        const store = useMatchingStore()
        store.setFirstMatchParams({ paramName: '型号' })
        expect(store.firstMatch).toBeNull()
      })

      it('searchMatchResult 优先级高于 firstMatchParams', () => {
        const store = useMatchingStore()
        store.setFirstMatchParams({ paramName: '型号', paramValue: 'FO-200' })
        store.setSearchMatchResult(makeMatchResult({ id: 'search-prio', score: 99 }))
        expect(store.firstMatch!.id).toBe('search-prio')
        // search 被清除后 fallback 到 firstMatchParams
        store.clearSearchResult()
        expect(store.firstMatch).not.toBeNull()
        expect(store.firstMatch!.score).toBe(100)
        expect(store.firstMatch!.model).toBe('FO-200')
      })

      it('虚拟结果 id 包含 virtual- 前缀', () => {
        const store = useMatchingStore()
        store.setFirstMatchParams({ paramName: '型号', paramValue: 'FO-100' })
        expect(store.firstMatch!.id).toMatch(/^virtual-/)
      })
    })

    describe('secondMatch', () => {
      it('secondMatchResult 为 null 时返回 null', () => {
        const store = useMatchingStore()
        expect(store.secondMatch).toBeNull()
      })

      it('secondMatchResult 非 null 时返回对应结果', () => {
        const store = useMatchingStore()
        const result = makeMatchResult({ id: 'second-getter', score: 88 })
        store.setSecondMatchResult(result)
        expect(store.secondMatch).toStrictEqual(result)
      })
    })

    describe('thirdMatch', () => {
      it('thirdMatchResult 为 null 时返回 null', () => {
        const store = useMatchingStore()
        expect(store.thirdMatch).toBeNull()
      })

      it('thirdMatchResult 非 null 时返回对应结果', () => {
        const store = useMatchingStore()
        const result = makeMatchResult({ id: 'third-getter', score: 77 })
        store.setThirdMatchResult(result)
        expect(store.thirdMatch).toStrictEqual(result)
      })
    })

    describe('otherMatches', () => {
      it('始终返回空数组', () => {
        const store = useMatchingStore()
        expect(store.otherMatches).toEqual([])
        // 即使设置了搜索结果, otherMatches 依然为空
        store.setSearchMatchResult(makeMatchResult())
        expect(store.otherMatches).toEqual([])
      })
    })
  })

  // ── 12. Edge cases ──────────────────────────────────────────

  describe('edge cases', () => {
    it('setFirstMatchParams 后 firstMatch 响应式更新', () => {
      const store = useMatchingStore()
      expect(store.firstMatch).toBeNull()
      store.setFirstMatchParams({ paramName: '型号', paramValue: 'FO-100' })
      expect(store.firstMatch).not.toBeNull()
    })

    it('setSearchMatchResult null 后 firstMatch 响应式切回 firstMatchParams', () => {
      const store = useMatchingStore()
      store.setFirstMatchParams({ paramName: '型号', paramValue: 'FO-100' })
      store.setSearchMatchResult(makeMatchResult())
      expect(store.firstMatch!.id).not.toContain('virtual-')
      store.setSearchMatchResult(null)
      expect(store.firstMatch).not.toBeNull()
      expect(store.firstMatch!.id).toContain('virtual-')
    })

    it('连续设置 secondMatchParams 只保留最后一次', () => {
      const store = useMatchingStore()
      store.setSecondMatchParams({ paramName: 'v1' })
      store.setSecondMatchParams({ paramName: 'v2' })
      expect(store.secondMatchParams!.paramName).toBe('v2')
    })

    it('重置后重新设置参数能正常工作', () => {
      const store = useMatchingStore()
      store.resetAllParams()
      store.setSearchMatchResult(makeMatchResult({ id: 'after-reset' }))
      expect(store.firstMatch).not.toBeNull()
      expect(store.firstMatch!.id).toBe('after-reset')
    })

    it('变更 matchingParams 不影响其他结果 ref', () => {
      const store = useMatchingStore()
      store.setSearchMatchResult(makeMatchResult())
      store.setSecondMatchResult(makeMatchResult({ id: 'second' }))
      store.updateMatchingParams({ surfaceType: '新值' })
      expect(store.searchMatchResult).not.toBeNull()
      expect(store.secondMatch).not.toBeNull()
    })

    it('setThirdMatchResult null 不影响其他结果', () => {
      const store = useMatchingStore()
      store.setSearchMatchResult(makeMatchResult())
      store.setThirdMatchResult(makeMatchResult({ id: 'third' }))
      store.setThirdMatchResult(null)
      expect(store.searchMatchResult).not.toBeNull()
    })

    it('多次清除操作均安全', () => {
      const store = useMatchingStore()
      store.clearSearchResult()
      store.clearSearchResult()
      store.clearSecondMatchResult()
      store.clearSecondMatchResult()
      store.resetAllParams()
      expect(store.firstMatch).toBeNull()
      expect(store.secondMatch).toBeNull()
      expect(store.searchMatchResult).toBeNull()
    })

    it('setSecondMatchParams 顶层属性不共享引用', () => {
      const store = useMatchingStore()
      const original = { paramName: '型号', paramValue: 'FO-100' }
      store.setSecondMatchParams(original)
      original.paramValue = 'CHANGED'
      expect(store.secondMatchParams!.paramValue).toBe('FO-100')
    })

    it('setFirstMatchParams 顶层属性不共享引用', () => {
      const store = useMatchingStore()
      const original = { paramName: '型号', paramValue: 'FO-100' }
      store.setFirstMatchParams(original)
      original.paramValue = 'CHANGED'
      expect(store.firstMatchParams!.paramValue).toBe('FO-100')
    })
  })
})
