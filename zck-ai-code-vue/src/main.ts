import { createApp, h } from 'vue'
import { createPinia } from 'pinia'

import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import { ConfigProvider } from 'ant-design-vue'
import './styles/global.css'
import { themeManager } from './utils/themeManager'

import App from './App.vue'
import router from './router'

// 初始化主题管理器
const initTheme = () => {
  // 应用保存的主题
  const savedTheme = localStorage.getItem('theme') || 'light'
  themeManager.applyTheme(savedTheme)
}

// 初始化主题
initTheme()

const app = createApp({
  render: () => h(ConfigProvider, { locale: zhCN }, { default: () => h(App) }),
})

app.use(createPinia())
app.use(router)
app.use(Antd)

// 全局注入主题管理器
app.provide('themeManager', themeManager)

app.mount('#app')
