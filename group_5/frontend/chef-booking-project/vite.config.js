// vite.config.js
// Mục đích: Trong môi trường DEV, mọi request bắt đầu bằng "/api"
// sẽ được Vite PROXY sang Spring Boot tại http://localhost:8080.
// => FE gọi fetch('/api/...') là tới thẳng BE, không cần CORS phía BE.
//
// Khi build production, bạn sẽ deploy FE ở domain thật,
// còn BE ở domain thật -> lúc đó bỏ proxy và cấu hình trên server/nginx.

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // địa chỉ BE local
        changeOrigin: true
      }
    }
  }
})
