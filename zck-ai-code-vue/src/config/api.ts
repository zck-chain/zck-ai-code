// API 配置文件
export const API_CONFIG = {
  BASE_URL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8123/api',
  DIST_URL: import.meta.env.VITE_DEPLOY_DOMAIN || 'http://localhost:8123/dist',
  TIMEOUT: 60000,
}
