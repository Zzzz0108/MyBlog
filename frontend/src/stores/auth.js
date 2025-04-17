import { defineStore } from 'pinia';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/utils/api';

export const useAuthStore = defineStore('auth', () => {
    const router = useRouter();
    const user = ref(null);
    const error = ref(null);

    // 从localStorage初始化用户
    const initUser = () => {
        const storedUser = localStorage.getItem('user');
        if (storedUser) user.value = JSON.parse(storedUser);
    };
    initUser();

    const login = async (username, password) => {
        try {
            const response = await api.post('/auth/login', { username, password });
            user.value = response.data;
            localStorage.setItem('user', JSON.stringify(response.data));
            router.push('/');
        } catch (err) {
            error.value = err.response?.data?.message || '登录失败';
            throw err;
        }
    };

    const register = async (username, password) => {
        try {
            await api.post('/auth/register', { username, password });
            await login(username, password); // 注册后自动登录
        } catch (err) {
            error.value = err.response?.data?.message || '注册失败';
            throw err;
        }
    };

    const logout = () => {
        user.value = null;
        localStorage.removeItem('user');
        router.push('/');
    };

    return { user, error, login, register, logout };
});