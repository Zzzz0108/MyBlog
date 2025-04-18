<template>
  <div class="post-editor">
    <div class="editor-header">
      <h2>{{ isEdit ? '编辑文章' : '新建文章' }}</h2>
      <div class="header-actions">
        <el-button @click="saveDraft" type="primary" plain>保存草稿</el-button>
        <el-button @click="handlePublish" type="primary">发布文章</el-button>
      </div>
    </div>

    <div class="editor-content">
      <el-form :model="postForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="postForm.title" placeholder="请输入文章标题"></el-input>
        </el-form-item>

        <el-form-item label="分类">
          <el-select v-model="postForm.categoryId" placeholder="请选择分类">
            <el-option
              v-for="category in categories"
              :key="category.categoryId"
              :label="category.name"
              :value="category.categoryId">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="封面">
          <el-upload
            class="cover-uploader"
            :action="uploadUrl"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
            :before-upload="beforeCoverUpload">
            <img v-if="postForm.coverImage" :src="postForm.coverImage" class="cover">
            <el-icon v-else class="cover-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>

        <el-form-item label="内容">
          <div class="editor-wrapper">
            <v-md-editor
              v-model="postForm.content"
              height="500px"
              :toolbar="toolbar"
              @upload-image="handleUploadImage">
            </v-md-editor>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
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

const toolbar = {
  h1: true,
  h2: true,
  h3: true,
  h4: true,
  bold: true,
  italic: true,
  quote: true,
  ul: true,
  ol: true,
  link: true,
  image: true,
  code: true,
  table: true,
  preview: true
}

const uploadUrl = '/api/posts/cover'
const uploadHeaders = {
  Authorization: `Bearer ${localStorage.getItem('token')}`
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

const handleUploadImage = async (event, insertImage, files) => {
  // 实现图片上传逻辑
  // 这里需要调用后端的图片上传接口
}
</script>

<style scoped>
.post-editor {
  padding: 20px;
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.editor-content {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
}

.cover-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
}

.cover-uploader:hover {
  border-color: #409EFF;
}

.cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.cover {
  width: 178px;
  height: 178px;
  display: block;
}

.editor-wrapper {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}
</style> 