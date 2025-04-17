<template>
  <div class="auth-container">
    <div class="auth-card">
      <!-- 标题区 -->
      <div class="auth-header">
        <el-icon class="logo-icon"><EditPen /></el-icon>
        <h2 class="auth-title">加入MyBlog</h2>
        <p class="auth-subtitle">开启你的创作之旅</p>
      </div>

      <!-- 表单区 -->
      <el-form
          ref="registerForm"
          :model="form"
          :rules="rules"
          @submit.prevent="submitForm"
      >
        <el-form-item prop="username">
          <el-input
              v-model="form.username"
              placeholder="用户名"
              :prefix-icon="User"
              size="large"
              class="custom-input"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="form.password"
              placeholder="密码"
              :prefix-icon="Lock"
              type="password"
              show-password
              size="large"
              class="custom-input"
          />
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input
              v-model="form.confirmPassword"
              placeholder="确认密码"
              :prefix-icon="Lock"
              type="password"
              show-password
              size="large"
              class="custom-input"
          />
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              native-type="submit"
              :loading="loading"
              class="auth-btn"
          >
            立即注册
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 底部链接 -->
      <div class="auth-footer">
        <span>已有账号？</span>
        <router-link to="/login" class="auth-link">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { EditPen, User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const auth = useAuthStore()
const registerForm = ref(null)
const loading = ref(false)

const form = ref({
  username: '',
  password: '',
  confirmPassword: ''
})

// 验证规则
const validatePassword = (rule, value, callback) => {
  if (value !== form.value.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 4, max: 16, message: '长度在4-16个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ]
}

const submitForm = async () => {
  try {
    await registerForm.value.validate()
    loading.value = true

    await auth.register({
      username: form.value.username,
      password: form.value.password
    })

    ElMessage.success('注册成功！')
    router.push('/')
  } catch (error) {
    if (error.response) {
      const msg = error.response.data.message || '注册失败'
      ElMessage.error(msg)
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 与LoginView保持一致的基础样式 */
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f9f9ff;
  padding: 20px;
}

.auth-card {
  background: white;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(138, 43, 226, 0.1);
  width: 100%;
  max-width: 420px;
}

.auth-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo-icon {
  color: var(--primary-color);
  font-size: 2.5rem;
  margin-bottom: 10px;
}

.auth-title {
  color: var(--primary-color);
  font-size: 1.8rem;
  margin-bottom: 8px;
}

.auth-subtitle {
  color: var(--text-color);
  opacity: 0.8;
  font-size: 1rem;
}

/* 表单元素样式 */
:deep(.custom-input .el-input__wrapper) {
  border-radius: 8px;
  padding: 8px 15px;
  box-shadow: 0 0 0 1px #e6e6e6;
  transition: all 0.3s;
}

:deep(.custom-input .el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--primary-light);
}

:deep(.custom-input .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--primary-color);
}

/* 按钮样式 */
.auth-btn {
  width: 100%;
  height: 48px;
  font-size: 1rem;
  font-weight: 500;
  letter-spacing: 1px;
  background-color: var(--primary-color);
  border: none;
  border-radius: 8px;
  transition: all 0.3s;
}

.auth-btn:hover {
  background-color: var(--primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(138, 43, 226, 0.2);
}

/* 底部链接 */
.auth-footer {
  text-align: center;
  margin-top: 20px;
  color: var(--text-color);
  font-size: 0.95rem;
}

.auth-link {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
  margin-left: 5px;
  transition: all 0.3s;
}

.auth-link:hover {
  text-decoration: underline;
  color: var(--primary-dark);
}

/* 响应式调整 */
@media (max-width: 480px) {
  .auth-card {
    padding: 30px 20px;
  }

  .auth-title {
    font-size: 1.5rem;
  }
}
</style>