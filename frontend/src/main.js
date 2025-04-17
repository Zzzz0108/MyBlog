import { createApp } from 'vue'
import { createPinia } from 'pinia' // 导入 createPinia
import App from './App.vue'
import router from './router'
import './assets/css/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { useAuthStore } from './stores/auth'

const app = createApp(App)
const pinia = createPinia() // 创建 Pinia 实例

app.use(pinia) // 注册 Pinia
app.use(router)
app.use(ElementPlus)

// 注册 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
// 应用启动时恢复状态
const auth = useAuthStore();
if (localStorage.getItem('token') && localStorage.getItem('user')) {
    auth.token = localStorage.getItem('token');
    auth.user = JSON.parse(localStorage.getItem('user'));
}

app.mount('#app')