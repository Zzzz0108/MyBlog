<!-- src/views/ProfileView.vue -->
<template>
  <div class="profile-container">
    <h1>个人资料</h1>
    <div v-if="auth.user">
      <div class="profile-header">
        <el-avatar :size="100" :src="auth.user.avatar" />
        <h2>{{ auth.user.username }}</h2>
      </div>

      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.bio" type="textarea" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="updateProfile">保存</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useAuthStore } from '@/stores/auth';
import request from '@/utils/request';

const auth = useAuthStore();
const form = ref({
  username: '',
  bio: ''
});

// 初始化表单数据
watch(() => auth.user, (user) => {
  if (user) {
    form.value = {
      username: user.username,
      bio: user.bio || ''
    };
  }
}, { immediate: true });

const updateProfile = async () => {
  try {
    const response = await request({
      url: '/auth/profile',
      method: 'put',
      data: form.value
    });
    auth.user = { ...auth.user, ...response.data };
  } catch (error) {
    console.error('更新失败:', error);
  }
};
</script>

<style scoped>
.profile-container {
  max-width: 600px;
  margin: 2rem auto;
  padding: 1rem;
}
.profile-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2rem;
}
</style>