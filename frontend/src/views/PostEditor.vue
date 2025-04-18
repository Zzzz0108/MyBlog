<template>
  <div class="post-editor">
    <div class="editor-container">
      <div class="editor-header">
        <div class="header-container">
          <div class="header-title">
            <h2>{{ isEdit ? '编辑文章' : '新建文章' }}</h2>
            <p class="subtitle">用心创作，分享你的知识与见解</p>
          </div>
          <div class="header-actions">
            <el-button @click="saveDraft" type="info" plain round>
              <el-icon><Document /></el-icon> 保存草稿
            </el-button>
            <el-button @click="handlePublish" type="primary" round>
              <el-icon><Promotion /></el-icon> {{ isEdit ? '更新发布' : '立即发布' }}
            </el-button>
          </div>
        </div>
      </div>

      <el-card class="editor-card" shadow="never">
        <el-form :model="postForm" label-width="100px">
          <el-form-item label="文章标题" class="form-item-title">
            <el-input
                v-model="postForm.title"
                placeholder="输入一个吸引人的标题..."
                class="title-input"
                clearable>
            </el-input>
          </el-form-item>

          <div class="form-row">
            <el-form-item label="文章分类" class="form-item-category">
              <el-select
                  v-model="postForm.categoryId"
                  placeholder="选择分类"
                  class="category-select"
                  clearable>
                <el-option
                    v-for="category in categories"
                    :key="category.categoryId"
                    :label="category.name"
                    :value="category.categoryId">
                </el-option>
              </el-select>
            </el-form-item>
          </div>

          <el-form-item label="封面图片" class="form-item-cover">
            <div class="cover-upload-tip">建议尺寸：1200×630像素，大小不超过2MB</div>
            <el-upload
                class="cover-uploader"
                :action="uploadUrl"
                :headers="uploadHeaders"
                :show-file-list="false"
                :on-success="handleCoverSuccess"
                :before-upload="beforeCoverUpload">
              <el-image
                  v-if="postForm.coverImage"
                  :src="postForm.coverImage"
                  fit="cover"
                  class="cover-image">
                <template #error>
                  <div class="image-error">
                    <el-icon><Picture /></el-icon>
                    <span>图片加载失败</span>
                  </div>
                </template>
              </el-image>
              <div v-else class="uploader-placeholder">
                <el-icon><Upload /></el-icon>
                <div class="placeholder-text">点击上传封面</div>
                <div class="placeholder-subtext">支持JPG/PNG格式</div>
              </div>
            </el-upload>
            <el-button
                v-if="postForm.coverImage"
                @click.stop="postForm.coverImage = ''"
                type="danger"
                plain
                size="small"
                class="remove-cover-btn">
              <el-icon><Delete /></el-icon> 移除封面
            </el-button>
          </el-form-item>

          <el-form-item label="文章内容" class="form-item-content">
            <el-input
                v-model="postForm.content"
                type="textarea"
                :autosize="{ minRows: 15 }"
                placeholder="开始你的创作之旅..."
                class="content-input"
                resize="none">
            </el-input>
            <div class="word-count" v-if="postForm.content">
              已输入 {{ postForm.content.length }} 字
            </div>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Document, Promotion, Picture, Upload, Delete } from '@element-plus/icons-vue'
import { getCategories } from '@/api/category'
import { createPost, updatePost, getPost, publishPost } from '@/api/post'

const route = useRoute()
const router = useRouter()
const isEdit = ref(false)
const postId = ref(null)

const postForm = ref({
  title: '',
  content: '',
  categoryId: null,
  coverImage: '',
  status: 'draft'
})

const categories = ref([])

const uploadUrl = 'http://localhost:9090/api/posts/cover'
const uploadHeaders = {
  Authorization: localStorage.getItem('token')
}

onMounted(async () => {
  // 获取分类列表
  const res = await getCategories()
  if (res.code === 200) {
    categories.value = res.data
  }

  // 如果是编辑模式，获取文章详情
  if (route.params.id) {
    isEdit.value = true
    postId.value = route.params.id
    const postRes = await getPost(postId.value)
    if (postRes.code === 200) {
      postForm.value = postRes.data
    }
  }
})

const saveDraft = async () => {
  try {
    if (isEdit.value) {
      await updatePost(postId.value, postForm.value)
    } else {
      await createPost(postForm.value)
    }
    ElMessage.success('保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

const handlePublish = async () => {
  try {
    if (isEdit.value) {
      await updatePost(postId.value, { ...postForm.value, status: 'published' })
      await publishPost(postId.value)
    } else {
      const res = await createPost({ ...postForm.value, status: 'published' })
      await publishPost(res.data.postId)
    }
    ElMessage.success('发布成功')
    router.push('/posts')
  } catch (error) {
    ElMessage.error('发布失败')
  }
}

const handleCoverSuccess = (response) => {
  if (response.code === 200) {
    postForm.value.coverImage = response.data
    ElMessage.success('封面图片上传成功')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

const beforeCoverUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB！')
  }
  return isImage && isLt2M
}
</script>

<style scoped lang="scss">
.post-editor {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 24px;
  display: flex;
  justify-content: center;
}

.editor-container {
  width: 100%;
  max-width: 900px;
}

.editor-header {
  margin-bottom: 24px;

  .header-container {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    gap: 20px;
  }

  .header-title {
    h2 {
      margin: 0;
      color: #2c3e50;
      font-size: 24px;
      font-weight: 600;
      line-height: 1.3;
    }

    .subtitle {
      margin: 6px 0 0;
      color: #7f8c8d;
      font-size: 14px;
    }
  }
}

.editor-card {
  border-radius: 12px;
  border: none;
  background: white;
  box-shadow: 0 1px 20px rgba(0, 0, 0, 0.05);

  :deep(.el-card__body) {
    padding: 30px;
  }
}

.form-item-title {
  margin-bottom: 30px;

  :deep(.el-form-item__label) {
    font-size: 16px;
    font-weight: 600;
    color: #2c3e50;
  }
}

.title-input {
  :deep(.el-input__inner) {
    border: none;
    border-bottom: 1px solid #e0e0e0;
    border-radius: 0;
    padding: 12px 0;
    font-size: 20px;
    font-weight: 600;
    color: #2c3e50;
    transition: all 0.3s;

    &:focus {
      border-bottom-color: #8A2BE2;
      box-shadow: none;
    }

    &::placeholder {
      color: #bdc3c7;
      font-weight: normal;
    }
  }
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 25px;
}

.form-item-category {
  flex: 1;

  :deep(.el-form-item__label) {
    font-size: 14px;
    font-weight: 600;
    color: #2c3e50;
  }

  :deep(.el-select) {
    width: 100%;
  }
}

.form-item-cover {
  margin-bottom: 30px;

  .cover-upload-tip {
    font-size: 12px;
    color: #95a5a6;
    margin-bottom: 10px;
  }
}

.cover-uploader {
  width: 100%;
  aspect-ratio: 16/9;
  border: 2px dashed #e0e0e0;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
  overflow: hidden;
  background-color: #f8f9fa;
  position: relative;

  &:hover {
    border-color: #8A2BE2;
    background-color: rgba(138, 43, 226, 0.03);
  }

  .cover-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    transition: transform 0.3s;

    &:hover {
      transform: scale(1.02);
    }
  }

  .image-error {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #95a5a6;

    .el-icon {
      font-size: 40px;
      margin-bottom: 10px;
    }
  }

  .uploader-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: #bdc3c7;
    padding: 20px;
    text-align: center;

    .el-icon {
      font-size: 40px;
      margin-bottom: 12px;
      color: #8A2BE2;
    }

    .placeholder-text {
      font-size: 16px;
      font-weight: 500;
      margin-bottom: 6px;
      color: #7f8c8d;
    }

    .placeholder-subtext {
      font-size: 12px;
      color: #bdc3c7;
    }
  }
}

.remove-cover-btn {
  margin-top: 10px;

  :deep(.el-icon) {
    margin-right: 5px;
  }
}

.form-item-content {
  :deep(.el-form-item__label) {
    font-size: 14px;
    font-weight: 600;
    color: #2c3e50;
  }
}

.content-input {
  :deep(.el-textarea__inner) {
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    padding: 16px;
    font-size: 16px;
    line-height: 1.8;
    color: #2c3e50;
    transition: all 0.3s;
    box-shadow: none;

    &:focus {
      border-color: #8A2BE2;
      box-shadow: 0 0 0 1px rgba(138, 43, 226, 0.2);
    }

    &::placeholder {
      color: #bdc3c7;
    }
  }
}

.word-count {
  text-align: right;
  font-size: 12px;
  color: #95a5a6;
  margin-top: 8px;
}

:deep(.el-button) {
  border-radius: 20px;
  padding: 10px 20px;
  font-weight: 500;
  transition: all 0.3s;

  .el-icon {
    margin-right: 6px;
  }
}

:deep(.el-button--primary) {
  background-color: #8A2BE2;
  border-color: #8A2BE2;

  &:hover {
    background-color: #7B1FA2;
    border-color: #7B1FA2;
    transform: translateY(-1px);
    box-shadow: 0 2px 10px rgba(138, 43, 226, 0.3);
  }

  &:active {
    transform: translateY(0);
  }
}

:deep(.el-button--info.is-plain) {
  color: #7f8c8d;
  border-color: #bdc3c7;

  &:hover {
    color: #8A2BE2;
    border-color: #8A2BE2;
    background-color: rgba(138, 43, 226, 0.05);
  }
}

:deep(.el-button--danger.is-plain) {
  &:hover {
    transform: translateY(-1px);
  }
}

@media (max-width: 768px) {
  .post-editor {
    padding: 16px;
  }

  .editor-header {
    .header-container {
      flex-direction: column;
      align-items: flex-start;
      gap: 15px;
    }

    .header-actions {
      width: 100%;
      justify-content: flex-end;
    }
  }

  .editor-card {
    :deep(.el-card__body) {
      padding: 20px;
    }
  }

  .title-input {
    :deep(.el-input__inner) {
      font-size: 18px;
    }
  }

  .form-row {
    flex-direction: column;
    gap: 0;
  }
}
</style>