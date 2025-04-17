import { createApp } from 'vue'
import { createPinia } from 'pinia' // 导入 createPinia
import App from './App.vue'
import router from './router'
import './assets/css/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
const pinia = createPinia() // 创建 Pinia 实例

app.use(pinia) // 注册 Pinia
app.use(router)
app.use(ElementPlus)

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
// 应用启动时清除旧缓存
if (performance.navigation.type === 1) { // 页面刷新
    localStorage.removeItem('user');
}

app.mount('#app')