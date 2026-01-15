import { createApp, h } from 'vue'
import { createPinia } from 'pinia'

import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import { ConfigProvider } from 'ant-design-vue'

import App from './App.vue'
import router from './router'

const app = createApp({
  render: () => h(ConfigProvider, { locale: zhCN }, { default: () => h(App) })
})

app.use(createPinia())
app.use(router)
app.use(Antd)

app.mount('#app')
