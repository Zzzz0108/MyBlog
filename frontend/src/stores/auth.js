// src/stores/auth.js
import { defineStore } from 'pinia';
import api from '@/utils/api';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

export const useAuthStore = defineStore('auth', () => {
    const router = useRouter();
    const user = ref(getDefaultUser()); // 使用默认用户对象
    const error = ref(null);
    const isLoading = ref(false);

    // 计算属性：生成动态头像（基于用户名首字母）
    const avatar = computed(() => {
        if (!user.value?.username) return '';

        const username = user.value.username;
        const letter = username.charAt(0).toUpperCase();
        const colors = ['#FFB6C1', '#87CEFA', '#98FB98', '#FFD700', '#FFA07A'];
        const color = colors[letter.charCodeAt(0) % colors.length];

        // 返回SVG格式的动态头像
        return `data:image/svg+xml;utf8,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100">
            <rect width="100" height="100" rx="50" fill="${color}"/>
            <text x="50" y="65" font-size="50" text-anchor="middle" fill="white">${letter}</text>
        </svg>`;
    });

    // 初始化时从 localStorage 恢复用户状态（带保护）
    const initUser = () => {
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            try {
                user.value = {
                    ...getDefaultUser(),
                    ...JSON.parse(storedUser)
                };
            } catch (e) {
                localStorage.removeItem('user');
                user.value = getDefaultUser();
            }
        }
    };

    // 立即初始化
    initUser();

// 添加一个检查token有效性的方法
    const checkAuth = async () => {
        if (user.value?.token) {
            try {
                await api.get('/auth/check');
                return true;
            } catch (e) {
                logout();
                return false;
            }
        }
        return false;
    };

    // 默认用户对象
    function getDefaultUser() {
        return {
            userid: null,  // 改为null
            username: '',
            email: '',
            token: '',
            avatar: '',
            bio: ''
        };
    }



    // src/stores/auth.js
    const login = async (username, password) => {
        try {
            console.log('调用 auth.login'); // 调试5
            error.value = null;
            const response = await api.post('/auth/login', { username, password });
            console.log('登录响应:', response); // 调试6
            // 从响应中获取用户数据（后端需返回）
            user.value = {
                ...response.data, // 假设后端返回用户信息
                token: response.data.token // 确保后端返回了 token
            };

            // 跳转到首页
            localStorage.setItem('user', JSON.stringify(user.value));
            console.log('更新后的用户状态:', user.value); // 调试：检查 Pinia 状态
            router.push('/');
        } catch (err) {
            error.value = err.response?.data?.message || 'Login failed';
            console.error('Auth.login 错误:', error); // 调试7
            throw err;
        }
    };

    const register = async ({ username, password }) => {
        isLoading.value = true;
        error.value = null;
        try {
            await api.post('/auth/register', { username, password });
            await login(username, password); // 复用登录逻辑
            router.push('/');
        } catch (err) {
            error.value = err.response?.data || 'Registration failed';
            throw err;
        } finally {
            isLoading.value = false;
        }
    };

    // src/stores/auth.js
    const logout = async () => {
        try {
            // 不再调用API，直接清除前端状态
            user.value = getDefaultUser();
            localStorage.removeItem('user');

            // 强制刷新页面以清除Session（确保后端Session失效）
            window.location.href = '/';
        } catch (e) {
            console.error('Logout error:', e);
        }
    };

    return {
        user,
        error,
        isLoading,
        avatar,
        login,
        register,
        logout
    };
});