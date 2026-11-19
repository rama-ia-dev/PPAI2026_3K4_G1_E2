import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// El frontend corre en el puerto 5173 y redirige /api al backend (puerto 8081)
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8081'
    }
  }
})
