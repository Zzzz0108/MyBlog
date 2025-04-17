import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/utils/api';

export const useAuthStore = defineStore('auth', () => {
    const router = useRouter();
    const user = ref(null);
    const token = ref(null);
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
            if (response.code === 200 && response.data) {
                user.value = response.data;
                token.value = response.data.token;
                localStorage.setItem('user', JSON.stringify(response.data));
                router.push('/');
            } else {
                throw new Error(response.msg || '登录失败');
            }
        } catch (err) {
            error.value = err.response?.data?.msg || err.message || '登录失败';
            throw err;
        }
    };

    const register = async (credentials) => {
        try {
            const response = await api.post('/auth/register', credentials);
            if (response.code === 200) {
                await login(credentials.username, credentials.password);
            } else {
                throw new Error(response.msg || '注册失败');
            }
        } catch (err) {
            error.value = err.response?.data?.msg || err.message || '注册失败';
            throw err;
        }
    };

    const logout = () => {
        user.value = null;
        token.value = null;
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        router.push('/');
    };

    const isAuthenticated = computed(() => {
        return !!token.value && !!user.value;
    });

    return { user, token, error, isAuthenticated, login, register, logout };
});