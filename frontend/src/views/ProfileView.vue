<!-- src/views/ProfileView.vue -->
<template>
  <div class="profile-container">
    <el-card class="profile-card">
      <!-- 头部区域 -->
      <div class="profile-header">
        <div class="avatar-wrapper">
          <el-avatar :size="120" :src="auth.user?.avatar || defaultAvatar" />
          <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
          >
            <el-icon class="avatar-edit-icon"><EditPen /></el-icon>
          </el-upload>
        </div>
        <div class="user-info">
          <h1>{{ userData.username }}</h1>
          <el-button
              type="primary"
              size="small"
              color="#8a2be2"
              round="20px"
              @click="showEditDialog = true"
          >
            编辑资料
          </el-button>
        </div>
      </div>

      <el-divider />

      <!-- 内容区域 -->
      <div class="profile-content">
        <el-tabs v-model="activeTab">
          <!-- 基本信息标签页 -->
          <el-tab-pane label="基本信息" name="info" >
            <el-descriptions :column="1" border>
              <el-descriptions-item label="用户名">
                {{ userData.username }}
              </el-descriptions-item>
              <el-descriptions-item label="注册时间">
                {{ formatDate(userData.createAt) }}
              </el-descriptions-item>
              <el-descriptions-item label="最后登录">
                {{ formatDate(userData.lastLogin) || '从未登录' }}
              </el-descriptions-item>
              <el-descriptions-item label="个人简介">
                {{ userData.bio || '这个人很懒，什么都没留下~' }}
              </el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>

          <!-- 文章统计标签页 -->
          <el-tab-pane label="创作统计" name="stats">
            <div class="stats-grid">
              <el-card v-for="stat in stats" :key="stat.title" shadow="hover">
                <div class="stat-item">
                  <div class="stat-value">{{ stat.value }}</div>
                  <div class="stat-title">{{ stat.title }}</div>
                </div>
              </el-card>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>

    <!-- 编辑资料对话框 -->
    <el-dialog
        v-model="showEditDialog"
        :show-close="false"
        width="500px"
        :close-on-click-modal="false"
        custom-class="ins-edit-dialog"
    >
      <div class="dialog-header">
        <h2>编辑资料</h2>
        <el-icon class="close-btn" @click="showEditDialog = false"><Close /></el-icon>
      </div>

      <el-form
          :model="editForm"
          label-position="top"
          class="ins-form"
      >
        <div class="form-section">
          <label class="ins-label">用户名</label>
          <el-input
              v-model="editForm.username"
              class="ins-input"
              :style="{ '--primary-color': 'var(--primary-color)' }"
          />
        </div>

        <div class="form-section">
          <label class="ins-label">个人简介 <span class="char-count">{{ editForm.bio?.length || 0 }}/200</span></label>
          <el-input
              v-model="editForm.bio"
              type="textarea"
              class="ins-textarea"
              :rows="4"
              maxlength="200"
              resize="none"
          />
        </div>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button
              class="ins-cancel-btn"
              @click="showEditDialog = false"
          >取消</el-button>
          <el-button
              type="primary"
              class="ins-submit-btn"
              @click="saveProfile"
          >保存更改</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import api from '@/utils/api'
import { EditPen } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { format } from 'date-fns'

const auth = useAuthStore()
const activeTab = ref('info')
const showEditDialog = ref(false)
const loading = ref(false)

// 用户数据（初始为空，从API获取）
const userData = ref({
  userid: 0,
  username: '',
  email: '',
  avatar: '',
  bio: '',
  postCount: 0,
  likeCount: 0,
  viewCount: 0,
  createAt: '',
  lastLogin: null
})

const defaultAvatar = computed(() =>
    auth.user?.avatar || auth.avatar || 'path/to/default-avatar.png'
)
// 编辑表单（从userData初始化）
const editForm = computed(() => ({
  username: userData.value.username,
  bio: userData.value.bio || ''
}))

// 获取用户数据
const fetchUserData = async () => {
  try {
    loading.value = true
    const { data } = await api.get(`/auth/user/${auth.user.userid}`)
    userData.value = data
  } catch (error) {
    ElMessage.error('获取用户数据失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchUserData()
})
// 日期格式化函数
const formatDate = (dateString) => {
  if (!dateString) return '';
  try {
    // 处理Java LocalDateTime格式（带T）和普通时间戳
    const date = new Date(dateString.includes('T') ? dateString : parseInt(dateString));
    return format(date, 'yyyy-MM-dd HH:mm:ss');
  } catch (e) {
    return '无效日期';
  }
}


// 保存资料
const saveProfile = async () => {
  try {
    loading.value = true
    const { data } = await api.put('/auth/update', {
      userid: auth.user.userid,
      username: editForm.value.username,
      bio: editForm.value.bio
    })

    // 更新全局状态
    auth.user = { ...auth.user, ...data }
    userData.value = { ...userData.value, ...data }
    showEditDialog.value = false
    ElMessage.success('资料更新成功')
  } catch (error) {
    ElMessage.error('更新失败: ' + (error.response?.data?.message || error.message))
  } finally {
    loading.value = false
  }
}

// 头像上传
const handleAvatarSuccess = async (_, uploadFile) => {
  const formData = new FormData();
  formData.append('file', uploadFile.raw);

  const { data } = await api.post('/auth/upload-avatar', formData);
  const avatarUrl = data.startsWith('http') ? data : `${api.defaults.baseURL}${data}`;

  auth.user.avatar = avatarUrl;
  userData.value.avatar = avatarUrl;
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过2MB!')
  }

  return isImage && isLt2M
}


</script>

<style scoped>
.profile-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 0 1rem;
}

.profile-card {
  padding: 2rem;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 2rem;
  margin-bottom: 1.5rem;
}

.avatar-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
}

.avatar-uploader {
  position: absolute;
  right: -10px;
  bottom: -10px;
  cursor: pointer;
}

.avatar-edit-icon {
  background: white;
  border-radius: 50%;
  padding: 5px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  color: var(--primary-color);
  transition: all 0.3s ease;
}

.user-info {
  flex: 1;
}

.user-info h1 {
  margin: 0 0 1rem 0;
  font-size: 1.8rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 1rem;
  margin-top: 1rem;
}

.stat-item {
  padding: 1rem;
  text-align: center;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: bold;
  color: var(--primary-color);
}

.stat-title {
  color: var(--el-text-color-secondary);
  margin-top: 0.5rem;
}

@media (max-width: 600px) {
  .profile-header {
    flex-direction: column;
    text-align: center;
  }

  .user-info {
    text-align: center;
  }
}
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: var(--primary-color);
  font-size: 32px;
  font-weight: 600;
}

/*对话框*/
:deep(.ins-edit-dialog) {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: none;
}

:deep(.ins-edit-dialog .el-dialog__header) {
  display: none;
}

.dialog-header {
  padding: 0px 24px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 20px;
}

.dialog-header h2 {
  margin-bottom: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.close-btn {
  cursor: pointer;
  margin-bottom: 10px;
  font-size: 18px;
  color: #999;
  transition: all 0.3s;
}

.close-btn:hover {
  color: var(--primary-color);
}

/* Ins风格表单 */
.ins-form {
  padding: 0 24px;
}

.form-section {
  margin-bottom: 24px;
}

.ins-label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #666;
  font-weight: 500;
}

.char-count {
  float: right;
  color: #999;
  font-weight: normal;
}

/* 输入框样式 */
:deep(.ins-input .el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px #eee;
  transition: all 0.3s;
  padding: 8px 12px;
  background: #fafafa;
}

:deep(.ins-input .el-input__wrapper:hover),
:deep(.ins-input .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--primary-color);
  background: #fff;
}

/* 文本域样式 */
:deep(.ins-textarea .el-textarea__inner) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px #eee;
  transition: all 0.3s;
  padding: 12px;
  background: #fafafa;
  color: #333;
  font-size: 14px;
}

:deep(.ins-textarea .el-textarea__inner:hover),
:deep(.ins-textarea .el-textarea__inner:focus) {
  box-shadow: 0 0 0 1px var(--primary-color);
  background: #fff;
  border-color: transparent;
}

/* 底部按钮 */
.dialog-footer {
  padding: 10px 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  border-top: 1px solid #f0f0f0;
}

.ins-cancel-btn {
  border-radius: 8px;
  padding: 10px 20px;
  background: #f5f5f5;
  border: none;
  color: #666;
  transition: all 0.3s;
}

.ins-cancel-btn:hover {
  background: #eee;
  color: #333;
}

.ins-submit-btn {
  border-radius: 8px;
  padding: 10px 20px;
  background: var(--primary-color);
  border: none;
  transition: all 0.3s;
}

.ins-submit-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}
/* 修复保存按钮hover颜色 */
:deep(.ins-submit-btn:hover) {
  background: var(--primary-color);
  opacity: 0.9;
}

/* 统一聚焦状态 */
:deep(.el-input__wrapper.is-focus),
:deep(.el-textarea__inner:focus) {
  box-shadow: 0 0 0 1px var(--primary-color) !important;
}
</style>