import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          'vendor': ['vue', 'vue-router'],
          'axios': ['axios']
        }
      }
    },
    minify: 'esbuild',
    sourcemap: false,
    cssCodeSplit: false
  },
  server: {
    port: 5173,
    proxy: {
      '/api': { target: 'http://localhost:8080', changeOrigin: true },
      '/admin': { target: 'http://localhost:8080', changeOrigin: true },
      '/css': { target: 'http://localhost:8080', changeOrigin: true },
      '/js': { target: 'http://localhost:8080', changeOrigin: true },
      '/images': { target: 'http://localhost:8080', changeOrigin: true },
      '/favicon.svg': { target: 'http://localhost:8080', changeOrigin: true }
    }
  }
})
