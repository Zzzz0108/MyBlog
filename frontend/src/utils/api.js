// src/utils/api.js
import axios from 'axios';
import { useAuthStore } from '@/stores/auth'


const api = axios.create({
    baseURL: '/api',
    headers: { 'Content-Type': 'application/json' }
});

// 请求拦截器：自动添加 Token
api.interceptors.request.use((config) => {
    const user = useAuthStore().user; // 从Pinia获取
    if (user?.token) {
        config.headers.Authorization = `Bearer ${user.token}`;
    }
    return config;
});
// 响应拦截器
api.interceptors.response.use(
    response => response.data,
    error => {
        if (error.response?.status === 401) {
            const auth = useAuthStore();
            // 先清除状态再跳转
            auth.user = null;
            auth.logout(); // 替换原来的手动清除
            window.location.href = '/';
        }
        return Promise.reject(error);
    }
);
export default api;
