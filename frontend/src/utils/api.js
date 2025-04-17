// src/utils/api.js
import axios from 'axios';

const api = axios.create({
    baseURL: '/api',
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true // 关键：允许跨域携带Cookie
});

// 移除请求拦截器中的手动Token添加
api.interceptors.request.use((config) => {
    return config; // 不再手动添加Authorization头
});

// 响应拦截器保持不变
api.interceptors.response.use(
    response => response.data,
    error => {
        if (error.response?.status === 401) {
            // 移除直接调用 store 的逻辑
            window.location.href = '/login?session_expired=1' // 改为重定向
        }
        return Promise.reject(error);
    }
);

export default api;