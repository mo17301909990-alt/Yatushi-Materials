import { describe, it, expect, beforeEach } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import router from '../index'

/**
 * Route smoke test — validates configuration, component resolution,
 * and auth guard behavior without mounting any components.
 */

describe('Route Configuration', () => {
  it('all route names are unique', () => {
    const names = router.getRoutes().map(r => r.name)
    expect(new Set(names).size).toBe(names.length)
  })

  it('all route paths are unique', () => {
    const paths = router.getRoutes().map(r => r.path)
    expect(new Set(paths).size).toBe(paths.length)
  })

  it('root path "/" redirects to "/login"', () => {
    const root = router.getRoutes().find(r => r.path === '/')
    expect(root).toBeDefined()
    expect(root!.redirect).toBe('/login')
  })

  it('every named route (non-redirect) has a resolved Vue component', () => {
    for (const route of router.getRoutes()) {
      if (route.redirect) continue
      const comp = route.components?.default
      expect(comp).toBeTruthy()
      expect(typeof comp).toBe('object')
      expect(comp).not.toBeNull()
    }
  })

  it('every component has render or setup function (valid Vue SFC)', () => {
    for (const route of router.getRoutes()) {
      if (route.redirect) continue
      const comp = route.components?.default as any
      const hasRender = typeof comp.render === 'function'
      const hasSetup = typeof comp.setup === 'function'
      expect(hasRender || hasSetup).toBe(true)
    }
  })
})

describe('Route Meta — requiresAuth assignment', () => {
  const guardedPathPrefixes = ['/admin', '/matching/', '/agent/', '/guide/', '/sys/']

  it('all admin/* routes have requiresAuth: true', () => {
    for (const route of router.getRoutes()) {
      if (route.path.startsWith('/admin')) {
        expect(route.meta?.requiresAuth).toBe(true)
      }
    }
  })

  it('all matching/* routes have requiresAuth: true', () => {
    for (const route of router.getRoutes()) {
      if (route.path.startsWith('/matching/')) {
        expect(route.meta?.requiresAuth).toBe(true)
      }
    }
  })

  it('all agent/* routes have requiresAuth: true', () => {
    for (const route of router.getRoutes()) {
      if (route.path.startsWith('/agent/')) {
        expect(route.meta?.requiresAuth).toBe(true)
      }
    }
  })

  it('all guide/* routes have requiresAuth: true', () => {
    for (const route of router.getRoutes()) {
      if (route.path.startsWith('/guide/')) {
        expect(route.meta?.requiresAuth).toBe(true)
      }
    }
  })

  it('all sys/* routes have requiresAuth: true', () => {
    for (const route of router.getRoutes()) {
      if (route.path.startsWith('/sys/')) {
        expect(route.meta?.requiresAuth).toBe(true)
      }
    }
  })

  it('process-config and related main routes have requiresAuth: true', () => {
    for (const route of router.getRoutes()) {
      if (
        ['/process-config', '/tag-matching', '/matching-result', '/match-preference-config'].includes(route.path)
      ) {
        expect(route.meta?.requiresAuth).toBe(true)
      }
    }
  })

  it('public debug/test routes do NOT have requiresAuth', () => {
    const publicPaths = [
      '/login',
      '/register',
      '/debug-permissions',
      '/permission-test',
      '/permission-debug',
      '/test-api',
      '/simple-test',
    ]
    for (const route of router.getRoutes()) {
      if (publicPaths.includes(route.path)) {
        expect(route.meta?.requiresAuth).toBeFalsy()
      }
    }
  })

  it('resource-group-selection routes have requiresAuth: true', () => {
    const guardedPaths = [
      '/punching-resource-group-selection',
      '/mounting-resource-group-selection',
      '/silkscreen-resource-group-selection',
      '/resource-group-selector',
    ]
    for (const route of router.getRoutes()) {
      if (guardedPaths.includes(route.path)) {
        expect(route.meta?.requiresAuth).toBe(true)
      }
    }
  })
})

describe('Auth Navigation Guard', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    localStorage.clear()
  })

  it('redirects unauthenticated user from guarded route to /login', async () => {
    await router.push('/process-config')
    expect(router.currentRoute.value.path).toBe('/login')
    expect(router.currentRoute.value.name).toBe('Login')
  })

  it('allows unauthenticated access to /login', async () => {
    await router.push('/login')
    expect(router.currentRoute.value.path).toBe('/login')
    expect(router.currentRoute.value.name).toBe('Login')
  })

  it('allows unauthenticated access to /register', async () => {
    await router.push('/register')
    expect(router.currentRoute.value.path).toBe('/register')
    expect(router.currentRoute.value.name).toBe('Register')
  })

  it('allows unauthenticated access to public debug routes', async () => {
    await router.push('/debug-permissions')
    expect(router.currentRoute.value.path).toBe('/debug-permissions')
    expect(router.currentRoute.value.name).toBe('DebugPermissions')
  })

  it('allows authenticated access to guarded route', async () => {
    localStorage.setItem('userInfo', JSON.stringify({ id: 1, username: 'test', nickname: 'Test' }))
    localStorage.setItem('isLoggedIn', 'true')
    localStorage.setItem('expirationTime', (Date.now() + 3_600_000).toString())

    await router.push('/process-config')
    expect(router.currentRoute.value.path).toBe('/process-config')
    expect(router.currentRoute.value.name).toBe('ProcessConfig')
  })

  it('redirects authenticated user from /login to /process-config', async () => {
    localStorage.setItem('userInfo', JSON.stringify({ id: 1, username: 'test', nickname: 'Test' }))
    localStorage.setItem('isLoggedIn', 'true')
    localStorage.setItem('expirationTime', (Date.now() + 3_600_000).toString())

    await router.push('/login')
    expect(router.currentRoute.value.path).toBe('/process-config')
    expect(router.currentRoute.value.name).toBe('ProcessConfig')
  })

  it('redirects all guarded routes when not authenticated', async () => {
    for (const route of router.getRoutes()) {
      if (route.redirect) continue
      if (route.path.startsWith(':')) continue
      // Only test routes that have requiresAuth: true
      if (!route.meta?.requiresAuth) continue

      setActivePinia(createPinia())
      localStorage.clear()

      if (route.path.includes(':workCenterId') && route.path.includes(':resourceGroupId')) {
        const concretePath = '/admin/resource-group-detail/1/2'
        await router.push(concretePath)
      } else {
        await router.push(route.path)
      }

      expect(router.currentRoute.value.path).toBe('/login')
    }
  })
})
