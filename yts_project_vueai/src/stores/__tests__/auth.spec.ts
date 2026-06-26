import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useAuthStore } from '@/stores/auth'
import { authApi } from '@/api/modules/auth'
import type { LoginParams, RegisterParams } from '@/api/types/auth'

// Mock the auth API module
vi.mock('@/api/modules/auth', () => ({
  authApi: {
    login: vi.fn(),
    register: vi.fn(),
  },
}))

// Helper to build a valid mock user response
function mockUser(overrides?: Partial<ReturnType<typeof makeUser>>) {
  return {
    id: 1,
    username: 'testuser',
    email: 'test@example.com',
    role: 'admin',
    is_active: true,
    created_at: '2026-01-01T00:00:00Z',
    ...overrides,
  }
}

// Helper to create a user for localStorage seeding
function makeUser() {
  return {
    id: 1,
    username: 'testuser',
    email: 'test@example.com',
    role: 'admin',
    is_active: true,
    created_at: '2026-01-01T00:00:00Z',
  }
}

describe('auth store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
    vi.clearAllMocks()
  })

  // ── 1. Initial state ──────────────────────────────────────────

  describe('initial state', () => {
    it('userInfo 初始为 null', () => {
      const store = useAuthStore()
      expect(store.userInfo).toBeNull()
    })

    it('isLoggedIn 初始为 false', () => {
      const store = useAuthStore()
      expect(store.isLoggedIn).toBe(false)
    })
  })

  // ── 2. login action ───────────────────────────────────────────

  describe('login', () => {
    const validParams: LoginParams = {
      username: 'testuser',
      password: 'password123',
    }

    it('成功登录后设置 userInfo 和 isLoggedIn', async () => {
      const user = mockUser()
      vi.mocked(authApi.login).mockResolvedValue(user)

      const store = useAuthStore()
      const result = await store.login(validParams)

      expect(authApi.login).toHaveBeenCalledWith(validParams)
      expect(store.userInfo).toEqual(user)
      expect(store.isLoggedIn).toBe(true)
      expect(result).toEqual(user)
    })

    it('成功登录后将数据持久化到 localStorage', async () => {
      const user = mockUser({ username: 'persist_user' })
      vi.mocked(authApi.login).mockResolvedValue(user)

      const store = useAuthStore()
      await store.login(validParams)

      expect(localStorage.getItem('userInfo')).toBe(JSON.stringify(user))
      expect(localStorage.getItem('isLoggedIn')).toBe('true')
      expect(localStorage.getItem('loginTime')).not.toBeNull()
      expect(localStorage.getItem('expirationTime')).not.toBeNull()

      // 验证过期时间合理
      const loginTime = parseInt(localStorage.getItem('loginTime')!)
      const expirationTime = parseInt(localStorage.getItem('expirationTime')!)
      expect(expirationTime - loginTime).toBe(6 * 60 * 60 * 1000)
    })

    it('API 返回空响应时抛出错误并清除状态', async () => {
      vi.mocked(authApi.login).mockResolvedValue(null as any)

      const store = useAuthStore()
      // 预先设置一些状态，确保被清除
      store.userInfo = mockUser()
      store.isLoggedIn = true

      await expect(store.login(validParams)).rejects.toThrow('服务器响应为空')

      expect(store.userInfo).toBeNull()
      expect(store.isLoggedIn).toBe(false)
      expect(localStorage.getItem('userInfo')).toBeNull()
    })

    it('API 返回不含 id 的响应时抛出错误并清除状态', async () => {
      vi.mocked(authApi.login).mockResolvedValue({ username: 'noid', email: 'noid@test.com' } as any)

      const store = useAuthStore()
      store.userInfo = mockUser()
      store.isLoggedIn = true

      await expect(store.login(validParams)).rejects.toThrow(
        '登录失败：服务器返回的用户信息无效'
      )

      expect(store.userInfo).toBeNull()
      expect(store.isLoggedIn).toBe(false)
    })

    it('API 调用本身抛出异常时抛出后端返回的错误消息', async () => {
      const apiError = {
        response: {
          data: { message: '用户名或密码错误' },
        },
      }
      vi.mocked(authApi.login).mockRejectedValue(apiError)

      const store = useAuthStore()
      await expect(store.login(validParams)).rejects.toBe('用户名或密码错误')

      expect(store.userInfo).toBeNull()
      expect(store.isLoggedIn).toBe(false)
    })

    it('API 异常不含 response.data.message 时抛出 error.message', async () => {
      const apiError = new Error('Network Error')
      vi.mocked(authApi.login).mockRejectedValue(apiError)

      const store = useAuthStore()
      await expect(store.login(validParams)).rejects.toBe('Network Error')
    })

    it('API 异常没有 response 和 message 时抛出兜底消息', async () => {
      vi.mocked(authApi.login).mockRejectedValue({})

      const store = useAuthStore()
      await expect(store.login(validParams)).rejects.toBe('登录失败')
    })
  })

  // ── 3. register action ────────────────────────────────────────

  describe('register', () => {
    const validParams: RegisterParams = {
      username: 'newuser',
      password: 'newpass123',
      email: 'new@example.com',
    }

    it('成功注册后返回 data', async () => {
      const resp = { data: { id: 2, username: 'newuser', email: 'new@example.com', role: 'user' } }
      vi.mocked(authApi.register).mockResolvedValue(resp)

      const store = useAuthStore()
      const result = await store.register(validParams)

      expect(authApi.register).toHaveBeenCalledWith(validParams)
      expect(result).toEqual(resp.data)
    })

    it('注册失败时抛出后端错误消息', async () => {
      const apiError = {
        response: {
          data: { message: '用户名已存在' },
        },
      }
      vi.mocked(authApi.register).mockRejectedValue(apiError)

      const store = useAuthStore()
      await expect(store.register(validParams)).rejects.toThrow('用户名已存在')
    })

    it('注册失败且无后端消息时抛出兜底错误', async () => {
      vi.mocked(authApi.register).mockRejectedValue(new Error(''))

      const store = useAuthStore()
      await expect(store.register(validParams)).rejects.toThrow('注册失败，请重试')
    })

    it('注册失败且异常不含 response 和 message 时抛出兜底错误', async () => {
      vi.mocked(authApi.register).mockRejectedValue({})

      const store = useAuthStore()
      await expect(store.register(validParams)).rejects.toThrow('注册失败，请重试')
    })
  })

  // ── 4. logout action ──────────────────────────────────────────

  describe('logout', () => {
    it('清除内存状态', () => {
      const store = useAuthStore()
      // 先模拟已登录
      store.userInfo = mockUser()
      store.isLoggedIn = true

      store.logout()

      expect(store.userInfo).toBeNull()
      expect(store.isLoggedIn).toBe(false)
    })

    it('清除 localStorage 中的所有登录相关键', () => {
      // 预填 localStorage
      localStorage.setItem('userInfo', JSON.stringify(mockUser()))
      localStorage.setItem('isLoggedIn', 'true')
      localStorage.setItem('loginTime', Date.now().toString())
      localStorage.setItem('expirationTime', (Date.now() + 3600000).toString())

      const store = useAuthStore()
      store.logout()

      expect(localStorage.getItem('userInfo')).toBeNull()
      expect(localStorage.getItem('isLoggedIn')).toBeNull()
      expect(localStorage.getItem('loginTime')).toBeNull()
      expect(localStorage.getItem('expirationTime')).toBeNull()
    })
  })

  // ── 5. initUserState action ────────────────────────────────────

  describe('initUserState', () => {
    it('localStorage 中有有效数据且未过期时恢复登录状态', () => {
      const user = makeUser()
      const futureExp = Date.now() + 3600000 // 1小时后过期
      localStorage.setItem('userInfo', JSON.stringify(user))
      localStorage.setItem('isLoggedIn', 'true')
      localStorage.setItem('expirationTime', futureExp.toString())
      localStorage.setItem('loginTime', (futureExp - 6 * 60 * 60 * 1000).toString())

      const store = useAuthStore()
      store.initUserState()

      expect(store.isLoggedIn).toBe(true)
      expect(store.userInfo).toEqual(user)
    })

    it('localStorage 数据已过期时清除状态', () => {
      const user = makeUser()
      localStorage.setItem('userInfo', JSON.stringify(user))
      localStorage.setItem('isLoggedIn', 'true')
      localStorage.setItem('expirationTime', (Date.now() - 1000).toString()) // 已过期

      const store = useAuthStore()
      store.initUserState()

      expect(store.isLoggedIn).toBe(false)
      expect(store.userInfo).toBeNull()
      expect(localStorage.getItem('userInfo')).toBeNull()
    })

    it('localStorage 中没有 userInfo 时清除状态', () => {
      localStorage.setItem('isLoggedIn', 'true')

      const store = useAuthStore()
      store.initUserState()

      expect(store.isLoggedIn).toBe(false)
      expect(store.userInfo).toBeNull()
    })

    it('localStorage 中 isLoggedIn 不为 true 时清除状态', () => {
      const user = makeUser()
      localStorage.setItem('userInfo', JSON.stringify(user))
      localStorage.setItem('isLoggedIn', 'false')

      const store = useAuthStore()
      store.initUserState()

      expect(store.isLoggedIn).toBe(false)
      expect(store.userInfo).toBeNull()
    })

    it('userInfo 为字符串 "undefined" 时清除状态', () => {
      localStorage.setItem('userInfo', 'undefined')
      localStorage.setItem('isLoggedIn', 'true')

      const store = useAuthStore()
      store.initUserState()

      expect(store.isLoggedIn).toBe(false)
      expect(store.userInfo).toBeNull()
    })

    it('没有 expirationTime 时清除状态', () => {
      const user = makeUser()
      localStorage.setItem('userInfo', JSON.stringify(user))
      localStorage.setItem('isLoggedIn', 'true')
      // expirationTime 没有设置

      const store = useAuthStore()
      store.initUserState()

      expect(store.isLoggedIn).toBe(false)
      expect(store.userInfo).toBeNull()
    })

    it('userInfo JSON 解析失败时清除状态', () => {
      localStorage.setItem('userInfo', '{broken json}')
      localStorage.setItem('isLoggedIn', 'true')
      localStorage.setItem('expirationTime', (Date.now() + 3600000).toString())

      const store = useAuthStore()
      store.initUserState()

      expect(store.isLoggedIn).toBe(false)
      expect(store.userInfo).toBeNull()
    })

    it('剩余时间小于 AUTO_REFRESH_THRESHOLD 时自动延长登录状态', () => {
      const user = makeUser()
      // 设置过期时间在 15 分钟后（小于 30 分钟阈值）
      const nearExpiry = Date.now() + 15 * 60 * 1000
      localStorage.setItem('userInfo', JSON.stringify(user))
      localStorage.setItem('isLoggedIn', 'true')
      localStorage.setItem('expirationTime', nearExpiry.toString())
      localStorage.setItem('loginTime', (Date.now() - 5 * 60 * 60 * 1000).toString())

      const store = useAuthStore()
      store.initUserState()

      expect(store.isLoggedIn).toBe(true)
      expect(store.userInfo).toEqual(user)
      // 验证过期时间已刷新（新的过期时间 > 旧的）
      const newExp = parseInt(localStorage.getItem('expirationTime')!)
      expect(newExp).toBeGreaterThan(nearExpiry)
    })

    it('剩余时间大于 AUTO_REFRESH_THRESHOLD 时不刷新过期时间', () => {
      const user = makeUser()
      const futureExp = Date.now() + 3 * 60 * 60 * 1000 // 3小时后（远大于30分钟）
      localStorage.setItem('userInfo', JSON.stringify(user))
      localStorage.setItem('isLoggedIn', 'true')
      localStorage.setItem('expirationTime', futureExp.toString())
      localStorage.setItem('loginTime', (Date.now() - 3 * 60 * 60 * 1000).toString())

      const store = useAuthStore()
      store.initUserState()

      expect(store.isLoggedIn).toBe(true)
      expect(store.userInfo).toEqual(user)
      // 过期时间应保持不变
      expect(localStorage.getItem('expirationTime')).toBe(futureExp.toString())
    })
  })

  // ── 6. refreshLoginState action ───────────────────────────────

  describe('refreshLoginState', () => {
    it('已登录时刷新过期时间', () => {
      const store = useAuthStore()
      store.userInfo = makeUser()
      store.isLoggedIn = true

      const oldExp = (Date.now() + 100000).toString()
      localStorage.setItem('expirationTime', oldExp)

      store.refreshLoginState()

      const newExp = parseInt(localStorage.getItem('expirationTime')!)
      expect(newExp).toBeGreaterThan(parseInt(oldExp))
      expect(localStorage.getItem('loginTime')).not.toBeNull()
    })

    it('未登录时不做任何操作', () => {
      const oldExp = '1234567890'
      localStorage.setItem('expirationTime', oldExp)

      const store = useAuthStore()
      store.refreshLoginState()

      // 不应修改 localStorage
      expect(localStorage.getItem('expirationTime')).toBe(oldExp)
    })

    it('userInfo 为 null 时不做任何操作', () => {
      const oldExp = '1234567890'
      localStorage.setItem('expirationTime', oldExp)

      const store = useAuthStore()
      store.isLoggedIn = true
      store.userInfo = null
      store.refreshLoginState()

      expect(localStorage.getItem('expirationTime')).toBe(oldExp)
    })
  })

  // ── 7. clearLoginState action ─────────────────────────────────

  describe('clearLoginState', () => {
    it('清除所有 localStorage 键', () => {
      localStorage.setItem('userInfo', 'x')
      localStorage.setItem('isLoggedIn', 'true')
      localStorage.setItem('loginTime', '123')
      localStorage.setItem('expirationTime', '456')

      const store = useAuthStore()
      store.clearLoginState()

      expect(localStorage.getItem('userInfo')).toBeNull()
      expect(localStorage.getItem('isLoggedIn')).toBeNull()
      expect(localStorage.getItem('loginTime')).toBeNull()
      expect(localStorage.getItem('expirationTime')).toBeNull()
    })

    it('重置内存状态', () => {
      const store = useAuthStore()
      store.userInfo = mockUser()
      store.isLoggedIn = true

      store.clearLoginState()

      expect(store.userInfo).toBeNull()
      expect(store.isLoggedIn).toBe(false)
    })

    it('已经是空状态时仍能安全调用', () => {
      const store = useAuthStore()
      expect(() => store.clearLoginState()).not.toThrow()
      expect(store.userInfo).toBeNull()
      expect(store.isLoggedIn).toBe(false)
    })
  })

  // ── 8. Edge cases ──────────────────────────────────────────────

  describe('edge cases', () => {
    it('重复调用 logout 安全', () => {
      const store = useAuthStore()
      store.logout()
      store.logout()
      expect(store.userInfo).toBeNull()
      expect(store.isLoggedIn).toBe(false)
    })

    it('login 失败后重新 login 成功可以恢复正常状态', async () => {
      vi.mocked(authApi.login).mockRejectedValueOnce(new Error('首次失败'))
      vi.mocked(authApi.login).mockResolvedValueOnce(mockUser())

      const store = useAuthStore()

      await expect(store.login({ username: 'u', password: 'p' })).rejects.toThrow('首次失败')
      expect(store.isLoggedIn).toBe(false)

      // 第二次成功
      const result = await store.login({ username: 'u', password: 'p' })
      expect(store.isLoggedIn).toBe(true)
      expect(result).toEqual(mockUser())
    })

    it('userInfo 边界字段（空字符串、缺失字段）能正常处理', async () => {
      const minimalUser = { id: 99, username: '', email: '' }
      vi.mocked(authApi.login).mockResolvedValue(minimalUser)

      const store = useAuthStore()
      const result = await store.login({ username: '', password: '' })

      expect(store.isLoggedIn).toBe(true)
      expect(result).toEqual(minimalUser)
    })
  })
})
