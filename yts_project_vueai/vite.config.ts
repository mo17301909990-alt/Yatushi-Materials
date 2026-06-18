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
  build: {
    rollupOptions: {
      output: {
        manualChunks(id: string) {
          if (id.includes('node_modules')) {
            if (id.includes('element-plus')) return 'vendor-element'
            if (id.includes('vue-quill')) return 'vendor-quill'
            if (id.includes('vue') || id.includes('pinia') || id.includes('axios') || id.includes('vue-router')) return 'vendor-core'
          }
        }
      }
    }
  },
  server: {
    proxy: {
      '/api/copilot/chat': {
        target: 'http://localhost:8092',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/api\/copilot\/chat/, '/api/ai/copilot/chat')
      },
      '/api': {
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
      '/guide-uploads': {
        target: 'http://localhost:8092',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
