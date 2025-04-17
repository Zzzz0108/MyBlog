<template>
  <div class="home-view">
    <!-- 已登录用户看到的个人主页 -->
    <div v-if="auth.user" class="user-home">
      <user-dashboard :user="auth.user" />
    </div>
    <!-- 未登录用户看到的公开页面 -->
    <div v-else>
    <!-- 主标语 -->
    <div class="hero-section">
      <h1 class="hero-title">📝 灵感收藏家</h1>
      <p class="hero-subtitle">用文字定格每一个闪光时刻</p>
    </div>

    <!-- 时尚走马灯 -->
    <div class="carousel-container">
      <el-carousel
          height="500px"
          :interval="4000"
          indicator-position="outside"
          arrow="always"
      >
        <el-carousel-item v-for="(slide, index) in slides" :key="index">
          <div class="slide-content" :style="{ background: slide.bgColor }">
            <div class="slide-text">
              <h3>{{ slide.title }}</h3>
              <p>{{ slide.description }}</p>
              <el-button type="primary" round class="slide-btn">
                {{ slide.buttonText }}
              </el-button>
            </div>
            <div class="slide-image">
              <img :src="slide.image" alt="" class="mock-image">
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 功能亮点 -->
    <div class="features-section">
      <h2 class="section-title">为什么选择 MyBlog</h2>
      <div class="features-grid">
        <div v-for="(feature, index) in features" :key="index" class="feature-card">
          <div class="feature-icon" :style="{ color: feature.color }">
            <component :is="feature.icon" />
          </div>
          <h3>{{ feature.title }}</h3>
          <p>{{ feature.description }}</p>
        </div>
      </div>
    </div>
    </div>
  </div>
</template>

<script setup>
import { MagicStick, Notebook, Picture, User } from '@element-plus/icons-vue'
import { storeToRefs } from 'pinia'
import { useAuthStore } from '@/stores/auth'
import UserDashboard from '@/components/UserDashboard.vue'
import { watch } from 'vue';


// 正确获取 store 和响应式 user
const auth = useAuthStore()
const { user } = storeToRefs(auth) // 保持响应式

// 安全监听
watch(user, (newVal) => {
  console.log('用户状态变化:', newVal)
}, { immediate: true })

const slides = [
  {
    title: '极简写作体验',
    description: '清爽界面让创作更专注',
    buttonText: '开始写作 →',
    bgColor: 'linear-gradient(135deg, #f9f0ff 0%, #e6d3ff 100%)',
    image: 'https://via.placeholder.com/400x500/f9f0ff/8a2be2?text=Mock+Image'
  },
  {
    title: '个性化主题',
    description: '多种配色方案随心切换',
    buttonText: '探索主题 →',
    bgColor: 'linear-gradient(135deg, #f0e6ff 0%, #d9c2ff 100%)',
    image: 'https://via.placeholder.com/400x500/f0e6ff/8a2be2?text=Mock+Image'
  },
  {
    title: '灵感随时记录',
    description: '多设备同步你的创作',
    buttonText: '立即体验 →',
    bgColor: 'linear-gradient(135deg, #e8d8ff 0%, #cbb1ff 100%)',
    image: 'https://via.placeholder.com/400x500/e8d8ff/8a2be2?text=Mock+Image'
  }
]

const features = [
  {
    icon: MagicStick,
    title: '智能排版',
    description: '自动优化文章格式，让内容更专业',
    color: '#8a2be2'
  },
  {
    icon: Notebook,
    title: '多格式支持',
    description: 'Markdown/富文本随心切换',
    color: '#a95bf1'
  },
  {
    icon: Picture,
    title: '精美配图',
    description: '内置免费图库一键插入',
    color: '#c77dff'
  },
  {
    icon: User,
    title: '个人品牌',
    description: '打造专属作者主页',
    color: '#6a1bb9'
  }
]
</script>

<style scoped>
.home-view {
  padding: 0;
}
.auth-buttons {
  margin-top: 2rem;
  display: flex;
  gap: 1rem;
  justify-content: center;
}

.user-home {
  padding: 2rem;
}
/* 英雄区域 */
.hero-section {
  text-align: center;
  padding: 4rem 0 3rem;
  background: linear-gradient(135deg, rgba(138, 43, 226, 0.05) 0%, rgba(138, 43, 226, 0.02) 100%);
}

.hero-title {
  font-size: 3rem;
  color: var(--primary-color);
  margin-bottom: 1rem;
  font-weight: 600;
}

.hero-subtitle {
  font-size: 1.3rem;
  color: var(--text-color);
  opacity: 0.8;
  max-width: 600px;
  margin: 0 auto;
}

/* 走马灯样式 */
.carousel-container {
  max-width: 1200px;
  margin: 0 auto 4rem;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(138, 43, 226, 0.15);
}

.slide-content {
  display: flex;
  height: 100%;
  padding: 2rem;
}

.slide-text {
  flex: 1;
  padding: 3rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.slide-text h3 {
  font-size: 2.2rem;
  color: var(--primary-dark);
  margin-bottom: 1rem;
}

.slide-text p {
  font-size: 1.1rem;
  color: var(--text-color);
  margin-bottom: 2rem;
  max-width: 80%;
}

.slide-btn {
  align-self: flex-start;
  padding: 12px 24px;
  font-size: 1rem;
  background-color: var(--primary-color);
  border: none;
}

.slide-btn:hover {
  background-color: var(--primary-dark);
}

.slide-image {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.mock-image {
  height: 80%;
  border-radius: 12px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

/* 功能亮点 */
.features-section {
  max-width: 1200px;
  margin: 0 auto 5rem;
  padding: 0 2rem;
}

.section-title {
  text-align: center;
  font-size: 2rem;
  color: var(--primary-color);
  margin-bottom: 3rem;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
  box-shadow: 0 5px 15px rgba(138, 43, 226, 0.1);
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(138, 43, 226, 0.15);
}

.feature-icon {
  font-size: 2.5rem;
  margin-bottom: 1.5rem;
}

.feature-card h3 {
  color: var(--primary-dark);
  margin-bottom: 1rem;
  font-size: 1.3rem;
}

.feature-card p {
  color: var(--text-color);
  opacity: 0.8;
  line-height: 1.6;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .hero-title {
    font-size: 2.2rem;
  }

  .slide-content {
    flex-direction: column;
    padding: 1rem;
  }

  .slide-text {
    padding: 1.5rem;
  }

  .slide-text h3 {
    font-size: 1.8rem;
  }

  .mock-image {
    height: auto;
    width: 80%;
    margin-top: 1rem;
  }
}
</style>