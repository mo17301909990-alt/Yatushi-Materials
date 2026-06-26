import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import CompatibilityResultCard from '@/components/copilot/CompatibilityResultCard.vue'
import type { CompatibilityResultData } from '@/types/copilot'

describe('CompatibilityResultCard', () => {
  it('无 props 时渲染空状态', () => {
    const wrapper = mount(CompatibilityResultCard)
    expect(wrapper.text()).toContain('暂无兼容性数据')
  })

  it('显示标题', () => {
    const data: CompatibilityResultData = {
      title: '烫金兼容性验证',
      items: [],
    }
    const wrapper = mount(CompatibilityResultCard, {
      props: { data },
    })
    expect(wrapper.text()).toContain('烫金兼容性验证')
  })

  it('渲染兼容性项目列表', () => {
    const data: CompatibilityResultData = {
      title: '兼容性结果',
      items: [
        { materialName: '金箔纸 A', status: 'compatible', verifyCount: 5 },
        { materialName: '特种纸 B', status: 'incompatible', verifyCount: 2 },
        { materialName: '普通纸 C', status: 'partial', verifyCount: 1 },
      ],
    }
    const wrapper = mount(CompatibilityResultCard, {
      props: { data },
    })
    expect(wrapper.text()).toContain('金箔纸 A')
    expect(wrapper.text()).toContain('特种纸 B')
    expect(wrapper.text()).toContain('普通纸 C')
    expect(wrapper.text()).toContain('验证 5 次')
    expect(wrapper.text()).toContain('验证 2 次')
    expect(wrapper.text()).toContain('验证 1 次')
  })

  it('compatible 状态显示绿色徽标（V图标）', () => {
    const data: CompatibilityResultData = {
      items: [{ materialName: '测试物料', status: 'compatible', verifyCount: 0 }],
    }
    const wrapper = mount(CompatibilityResultCard, {
      props: { data },
    })
    const badge = wrapper.find('span.shrink-0')
    expect(badge.exists()).toBe(true)
    expect(badge.classes()).toContain('bg-green-100')
    expect(badge.text()).toBe('V')
  })

  it('incompatible 状态显示红色徽标（X图标）', () => {
    const data: CompatibilityResultData = {
      items: [{ materialName: '测试物料', status: 'incompatible', verifyCount: 0 }],
    }
    const wrapper = mount(CompatibilityResultCard, {
      props: { data },
    })
    const badge = wrapper.find('span.shrink-0')
    expect(badge.classes()).toContain('bg-red-100')
    expect(badge.text()).toBe('X')
  })

  it('partial 状态显示黄色徽标（▷图标）', () => {
    const data: CompatibilityResultData = {
      items: [{ materialName: '测试物料', status: 'partial', verifyCount: 0 }],
    }
    const wrapper = mount(CompatibilityResultCard, {
      props: { data },
    })
    const badge = wrapper.find('span.shrink-0')
    expect(badge.classes()).toContain('bg-yellow-100')
    expect(badge.text()).toBe('▷')
  })
})
