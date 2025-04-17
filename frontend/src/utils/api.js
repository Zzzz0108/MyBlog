// src/utils/api.js
import axios from 'axios';

const api = axios.create({
    baseURL: '/api',
    headers: { 
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
    withCredentials: true // 关键：允许跨域携带Cookie
});

// 请求拦截器
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
});

// 响应拦截器
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