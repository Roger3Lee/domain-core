import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      // Proxy API calls to the Spring Boot backend (default port 8080)
      '/sample': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
