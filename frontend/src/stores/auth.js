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

    // 立即初始化
    initUser();

    const login = async (username, password) => {
        try {
            error.value = null; // 清除之前的错误
            const response = await api.post('/auth/login', { username, password });
            // 获取完整用户数据
            const { data: userInfo } = await api.get(`/auth/user/${response.data.userid}`);

            user.value = {
                ...userInfo,
                token: response.data.token
            };

            localStorage.setItem('user', JSON.stringify(user.value));

            // 登录成功后跳转到首页或之前尝试访问的页面
            const redirect = router.currentRoute.value.query.redirect || '/';
            router.push(redirect);
        } catch (err) {
            error.value = err.response?.data?.message || 'Login failed';
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

    const logout = async () => {
        try {
            // 可选：通知后端注销
            await api.post('/auth/logout');
        } catch (e) {
            console.error('Logout error:', e);
        } finally {
            // 清除状态
            user.value = getDefaultUser();
            localStorage.removeItem('user');
            delete api.defaults.headers.common['Authorization'];

            // 跳转到首页，不需要强制刷新
            router.push('/');
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