/// <reference types="vitest" />
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src')
    }
  },
  server: {
    proxy: {
      // AI Copilot 聊天接口（放在 /api 前面，优先匹配）
      // 前端调 /copilot/chat → request baseURL:/api → 实际 /api/copilot/chat
      // 代理重写 /api/copilot/chat → /api/ai/copilot/chat（后端实际路径）
      '/api/copilot/chat': {
        target: 'http://localhost:8092',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/api\/copilot\/chat/, '/api/ai/copilot/chat')
      },
      // 必须保留路径中的 /api：后端 Controller 均为 @RequestMapping("/api/...")
      // 若 rewrite 去掉 /api，会打成 http://localhost:8093/gold-foil/...，导致 404 / No static resource xxx
      '/api': {
        // target: 'http://101.126.27.148:8092',
        // 与 yts_project/application.properties 的 server.port 一致（默认 8093）
        target: 'http://localhost:8092',
        changeOrigin: true,
        secure: false,
        ws: true,
        configure: (proxy, _options) => {
          proxy.on('error', (err, _req, _res) => {
            console.log('proxy error', err);
          });
        }
      },
      // 操作指南上传文件的访问（开发环境代理到后端文件目录）
      '/guide-uploads': {
        target: 'http://localhost:8093',
        changeOrigin: true,
        secure: false
      }
    }
  },
  test: {
    globals: true,
    resolve: {
      alias: {
        '@': path.resolve(__dirname, './src')
      }
    },
    environment: 'jsdom',
    testTimeout: 30000,
    setupFiles: './src/test-setup.ts',
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
      include: ['src/**/*.{ts,vue}'],
      exclude: ['src/**/*.spec.ts', 'src/**/__tests__/**'],
      thresholds: {
        branches: 20,
        functions: 15,
        lines: 25,
        statements: 20
      }
    }
  }
})