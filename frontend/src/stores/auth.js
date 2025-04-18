import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { login as loginApi, register as registerApi } from '@/api/auth';

export const useAuthStore = defineStore('auth', () => {
    const router = useRouter();
    const user = ref(null);
    const token = ref(localStorage.getItem('token'));
    const error = ref(null);
    const isLoading = ref(false);

    // 从localStorage初始化用户
    const initUser = () => {
        try {
            const storedUser = localStorage.getItem('user');
            const storedToken = localStorage.getItem('token');
            
            if (storedUser && storedToken) {
                user.value = JSON.parse(storedUser);
                token.value = storedToken;
                console.log('从 localStorage 恢复用户状态:', user.value);
            } else {
                // 如果只有部分数据，清除所有数据
                localStorage.removeItem('user');
                localStorage.removeItem('token');
                user.value = null;
                token.value = null;
            }
        } catch (err) {
            console.error('恢复用户状态失败:', err);
            localStorage.removeItem('user');
            localStorage.removeItem('token');
            user.value = null;
            token.value = null;
        }
    };
    initUser();

    const login = async (username, password) => {
        if (!username || !password) {
            error.value = '用户名和密码不能为空';
            throw new Error('用户名和密码不能为空');
        }

        isLoading.value = true;
        error.value = null;
        
        try {
            console.log('开始登录请求...');
            const response = await loginApi({ username, password });
            console.log('登录响应:', response);
            
            if (response.code === 200 && response.data) {
                const authData = response.data;
                
                // 验证token格式
                if (!authData.token || typeof authData.token !== 'string') {
                    throw new Error('服务器返回的token格式无效');
                }
                
                // 构建用户对象
                const userData = {
                    id: authData.userid,
                    username: authData.username,
                    avatar: authData.avatar || '',
                    email: authData.email || '',
                    bio: authData.bio || '',
                    postCount: authData.postCount || 0,
                    likeCount: authData.likeCount || 0,
                    viewCount: authData.viewCount || 0
                };
                
                user.value = userData;
                // 确保 token 格式正确
                const formattedToken = authData.token.startsWith('Bearer ') ? authData.token : `Bearer ${authData.token}`;
                token.value = formattedToken;
                localStorage.setItem('user', JSON.stringify(userData));
                localStorage.setItem('token', formattedToken);
                console.log('登录成功，用户信息:', userData);
                console.log('设置的 token:', formattedToken);
                router.push('/');
            } else {
                throw new Error(response.msg || '登录失败');
            }
        } catch (err) {
            error.value = err.message;
            throw err;
        } finally {
            isLoading.value = false;
        }
    };

    const register = async (credentials) => {
        if (!credentials.username || !credentials.password) {
            error.value = '用户名和密码不能为空';
            throw new Error('用户名和密码不能为空');
        }

        isLoading.value = true;
        error.value = null;
        
        try {
            const response = await registerApi(credentials);
            
            if (response.code === 200) {
                await login(credentials.username, credentials.password);
            } else {
                throw new Error(response.msg || '注册失败');
            }
        } catch (err) {
            console.error('注册失败:', err);
            error.value = err.response?.data?.msg || err.message || '注册失败';
            throw err;
        } finally {
            isLoading.value = false;
        }
    };

    const logout = () => {
        console.log('执行登出操作');
        user.value = null;
        token.value = null;
        localStorage.removeItem('user');
        localStorage.removeItem('token');
        router.push('/login');
    };

    const isAuthenticated = computed(() => {
        const authenticated = !!token.value
        console.log('检查认证状态:', authenticated, 'Token:', token.value)
        return authenticated
    });

    return { 
        user, 
        token, 
        error, 
        isLoading,
        isAuthenticated, 
        login, 
        register, 
        logout 
    };
});