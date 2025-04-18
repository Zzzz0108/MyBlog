import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

// 创建 axios 实例
const service = axios.create({
  baseURL: '/', // 移除 /api 前缀，因为我们在每个 API 调用中都已经包含了
  timeout: 10000,
  retry: 3,
  retryDelay: 1000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res
    }
    ElMessage.error(res.msg || '请求失败')
    return Promise.reject(new Error(res.msg || '请求失败'))
  },
  error => {
    console.error('响应错误:', error)
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 未授权，清除用户信息并跳转到登录页
          localStorage.removeItem('token')
          localStorage.removeItem('user')
          const router = useRouter()
          router.push('/login')
          break
        case 403:
          // 禁止访问
          ElMessage.error('没有权限访问该资源')
          break
        case 404:
          // 资源不存在
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          // 服务器错误
          ElMessage.error('服务器错误，请稍后重试')
          break
        default:
          ElMessage.error(error.response.data?.msg || '请求失败')
      }
    } else if (error.request) {
      // 请求已发出但没有收到响应
      ElMessage.error('网络错误，请检查您的网络连接')
    } else {
      // 请求配置出错
      ElMessage.error('请求配置错误')
    }
    return Promise.reject(error)
  }
)

export default service 