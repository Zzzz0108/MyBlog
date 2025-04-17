<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2 class="auth-title">Welcome Back</h2>
      <p class="auth-subtitle">Please enter your credentials to login</p>

      <form @submit.prevent="handleSubmit" class="auth-form">
        <div class="form-group">
          <label for="username" class="form-label">Username</label>
          <input
              id="username"
              v-model="username"
              type="text"
              class="form-input"
              placeholder="Enter your username"
              required
          />
        </div>

        <div class="form-group">
          <label for="password" class="form-label">Password</label>
          <input
              id="password"
              v-model="password"
              type="password"
              class="form-input"
              placeholder="Enter your password"
              required
          />
        </div>

        <button type="submit" class="btn btn-primary btn-block" :disabled="auth.isLoading">
          {{ auth.isLoading ? 'Logging in...' : 'Login' }}
        </button>

        <p v-if="auth.error" class="error-message">{{ auth.error }}</p>
      </form>

      <p class="auth-footer">
        Don't have an account? <router-link to="/register" class="link">Register here</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref,onBeforeUnmount } from 'vue';
import { useAuthStore } from '@/stores/auth';

const auth = useAuthStore();
const username = ref('');
const password = ref('');
const isMounted = ref(true); // 组件挂载状态

onBeforeUnmount(() => {
  isMounted.value = false;
});
const handleSubmit = async () => {
  try {
    await auth.login(username.value, password.value);
    if (!isMounted.value) return;
    // 登录成功后，auth.js中的login方法已经处理了跳转
  } catch (error) {
    if (!isMounted.value) return;
    // 更详细的错误处理
    console.error('Login error:', error);
    // 错误处理
  }
};

</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
}

.auth-card {
  background: var(--white);
  padding: 2.5rem;
  border-radius: var(--border-radius);
  box-shadow: var(--shadow);
  width: 100%;
  max-width: 450px;
}

.auth-title {
  color: var(--primary-color);
  margin-bottom: 0.5rem;
  font-size: 1.8rem;
}

.auth-subtitle {
  color: var(--text-color);
  margin-bottom: 2rem;
  opacity: 0.8;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-weight: 500;
  color: var(--text-color);
}

.form-input {
  padding: 0.8rem 1rem;
  border: 1px solid #ddd;
  border-radius: var(--border-radius);
  font-size: 1rem;
  transition: border-color 0.3s;
}

.form-input:focus {
  outline: none;
  border-color: var(--primary-color);
}

.error-message {
  color: #e74c3c;
  text-align: center;
  margin-top: 1rem;
}

.auth-footer {
  text-align: center;
  margin-top: 1.5rem;
  color: var(--text-color);
}
</style>