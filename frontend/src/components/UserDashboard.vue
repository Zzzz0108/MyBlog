<template>
  <div class="user-dashboard">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner" :style="{ background: userBannerStyle }">
      <div class="welcome-content">
        <el-avatar :size="80" :src="user.avatar || defaultAvatar" />
        <h2>欢迎回来, {{ user.username }}!</h2>
        <p>您有 {{ user.postCount || 0 }} 篇创作</p>
        <el-button type="primary" size="large" @click="$router.push('/editor')">
          <el-icon><Edit /></el-icon>
          开始创作
        </el-button>
      </div>
    </div>

    <!-- 用户数据概览 -->
    <div class="stats-grid">
      <div class="stat-card" v-for="stat in userStats" :key="stat.title">
        <div class="stat-icon" :style="{ color: stat.color }">
          <component :is="stat.icon" />
        </div>
        <h3>{{ stat.value }}</h3>
        <p>{{ stat.title }}</p>
      </div>
    </div>

    <!-- 最近创作 -->
    <div class="recent-posts">
      <h3 class="section-title">最近创作</h3>
      <el-skeleton :rows="3" animated v-if="loading" />
      <post-list v-else :posts="recentPosts" />
    </div>
  </div>
</template>

<script setup>
import { Document, CollectionTag, Clock, Edit } from '@element-plus/icons-vue'
import PostList from '@/components/PostList.vue'
import { computed, ref, onMounted } from 'vue'
import { getRecentPosts } from '@/api/post'

const props = defineProps({
  user: {
    type: Object,
    required: true
  }
})

const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
const recentPosts = ref([])
const loading = ref(true)

const userBannerStyle = computed(() => {
  return `linear-gradient(135deg, ${props.user.themeColor || '#8a2be2'} 0%, #a95bf1 100%)`
})

const userStats = [
  { icon: Document, title: '总文章数', value: props.user.postCount || 0, color: '#8a2be2' },
  { icon: CollectionTag, title: '分类数量', value: props.user.categoryCount || 0, color: '#a95bf1' },
  { icon: Clock, title: '创作天数', value: props.user.activeDays || 0, color: '#c77dff' }
]

const fetchRecentPosts = async () => {
  try {
    loading.value = true
    const response = await getRecentPosts(props.user.userid, 5)
    if (response.code === 200) {
      recentPosts.value = response.data
    }
  } catch (error) {
    console.error('获取最近文章失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchRecentPosts()
})
</script>

<style scoped>
.user-dashboard {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-banner {
  color: white;
  padding: 3rem 2rem;
  border-radius: 12px;
  margin-bottom: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.welcome-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.welcome-content h2 {
  margin: 0.5rem 0 0;
  font-size: 1.8rem;
}

.welcome-content .el-button {
  margin-top: 1rem;
  padding: 0.8rem 2rem;
  font-size: 1.1rem;
}

.welcome-content .el-button .el-icon {
  margin-right: 0.5rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.stat-icon {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.stat-card h3 {
  font-size: 1.8rem;
  margin: 0.5rem 0;
  color: var(--primary-dark);
}

.section-title {
  font-size: 1.5rem;
  color: var(--primary-color);
  margin-bottom: 1.5rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid var(--light-gray);
}
</style>