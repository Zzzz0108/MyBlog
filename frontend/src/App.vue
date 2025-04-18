<template>
  <div id="app">
    <!-- 新版导航栏 -->
    <header class="ins-header" :class="{ 'scrolled': isScrolled }">
      <div class="header-container">
        <!-- Logo区域 -->
        <router-link to="/" class="brand">
          <el-icon class="logo-icon"><EditPen /></el-icon>
          <span class="logo-text">MyBlog</span>
        </router-link>

        <!-- 导航菜单 -->
        <div class="nav-container">
          <nav class="nav-links">
            <router-link
                v-for="link in navLinks"
                :key="link.path"
                :to="link.path"
                class="nav-link"
                :class="{ 'active': $route.path === link.path }"
            >
              {{ link.name }}
            </router-link>
          </nav>

          <!-- 用户操作区域单独容器 -->
          <div class="auth-container">
            <div class="user-actions" v-if="auth.user">
              <el-dropdown>
                <div class="user-avatar">
                  <el-avatar :size="36">
                    {{ (auth.user?.username || '?').charAt(0).toUpperCase() }}
                  </el-avatar>
                </div>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
                    <el-dropdown-item divided @click="auth.logout">退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <template v-else>
              <router-link to="/login" class="auth-link">登录</router-link>
              <router-link to="/register" class="auth-link register">注册</router-link>
            </template>
          </div>
        </div>
      </div>
    </header>
    <el-main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </el-main>

    <!-- Footer -->
    <el-footer class="ins-footer">
      <div class="footer-container">
        <!-- 第一行：主要内容和链接 -->
        <div class="footer-main">
          <!-- 品牌信息 -->
          <div class="footer-brand">
            <router-link to="/" class="brand">
              <el-icon class="logo-icon"><EditPen /></el-icon>
              <span class="logo-text">MyBlog</span>
            </router-link>
            <p class="footer-desc">记录灵感，分享思考</p>
            <div class="social-links">
              <a v-for="social in socialLinks" :key="social.name" :href="social.url" class="social-icon">
                <el-icon :size="20"><component :is="social.icon" /></el-icon>
              </a>
            </div>
          </div>

          <!-- 链接区块 -->
          <div class="footer-links-group">
            <div class="links-column" v-for="(group, index) in linkGroups" :key="index">
              <h3 class="links-title">{{ group.title }}</h3>
              <router-link
                  v-for="link in group.links"
                  :key="link.path"
                  :to="link.path"
                  class="footer-link"
              >
                {{ link.name }}
              </router-link>
            </div>
          </div>
        </div>

        <!-- 第二行：版权和备案信息 -->
        <div class="footer-bottom">
          <div class="copyright">
            &copy; 2025 MyBlog. All rights reserved.
          </div>
          <div class="legal-links">
            <router-link to="/privacy" class="legal-link">隐私政策</router-link>
            <router-link to="/terms" class="legal-link">服务条款</router-link>
            <a href="#" class="legal-link">ICP备案号</a>
          </div>
        </div>
      </div>
    </el-footer>

    <!-- 其他内容保持不变 -->
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { EditPen, Promotion, ChatLineRound, Collection, Service  } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'

const auth = useAuthStore()
const isScrolled = ref(false)
const { user } = storeToRefs(auth) // 使用响应式解构

const navLinks = [
  { path: '/', name: '首页' },
  { path: '/discover', name: '发现' },
  { path: '/categories', name: '分类' },
  { path: '/about', name: '关于' }
]

const handleScroll = () => {
  isScrolled.value = window.scrollY > 10
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  // 从 localStorage 恢复状态
  const storedToken = localStorage.getItem('token')
  const storedUser = localStorage.getItem('user')
  if (storedToken && storedUser) {
    try {
      auth.token = storedToken
      auth.user = JSON.parse(storedUser)
      console.log('从 localStorage 恢复用户状态成功:', auth.user)
    } catch (error) {
      console.error('恢复用户状态失败:', error)
      // 如果解析失败，清除无效数据
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      auth.token = null
      auth.user = null
    }
  }
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})

// 社交媒体链接
const socialLinks = [
  { name: 'Twitter', icon: 'Promotion', url: '#' },
  { name: 'Instagram', icon: 'Collection', url: '#' },
  { name: 'WeChat', icon: 'ChatLineRound', url: '#' },
  { name: 'Email', icon: 'Service', url: '#' }
]

// 页脚链接分组
const linkGroups = [
  {
    title: '探索',
    links: [
      { path: '/discover', name: '发现' },
      { path: '/categories', name: '分类' },
      { path: '/popular', name: '热门' }
    ]
  },
  {
    title: '关于',
    links: [
      { path: '/about', name: '关于我们' },
      { path: '/team', name: '团队' },
      { path: '/contact', name: '联系我们' }
    ]
  },
  {
    title: '支持',
    links: [
      { path: '/help', name: '帮助中心' },
      { path: '/faq', name: '常见问题' },
      { path: '/feedback', name: '意见反馈' }
    ]
  }
]

</script>

<style scoped>
/* 新版导航栏样式 */
.ins-header {
  position: sticky;
  top: 0;
  z-index: 1000;
  height: 80px; /* 增加高度 */
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
  border-bottom: 2px solid rgba(138, 43, 226, 0.1); /* 初始浅色底边 */
}

.ins-header.scrolled {
  border-bottom: 2px solid var(--primary-color); /* 滚动后紫色底边 */
  box-shadow: 0 2px 15px rgba(138, 43, 226, 0.3);
  background-color: #fff;
}

.header-container {
  width: 100%;
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 新增布局容器 */
.nav-container {
  display: flex;
  align-items: center;
  gap: 2rem; /* 控制导航和登录区域的间距 */
}

.nav-links {
  display: flex;
  gap: 2rem; /* 导航项间距 */
}

.auth-container {
  display: flex;
  align-items: center;
  gap: 0.7rem; /* 登录注册按钮间距 */
}

.brand {
  display: flex;
  align-items: center;
  text-decoration: none;
  margin-right: 2rem; /* 增加logo右边距 */
}

.logo-icon {
  color: var(--primary-color);
  font-size: 2rem; /* 增大图标 */
  margin-right: 10px;
}

.logo-text {
  color: var(--primary-dark);
  font-size: 1.6rem; /* 增大文字 */
  font-weight: 600;
}

/* 导航链接样式保持不变 */
.nav-link {
  color: blueviolet;
  text-decoration: none;
  font-weight: 500;
  font-size: 1rem;
  position: relative;
  padding: 8px 2px;
  transition: all 0.3s ease;

}
.nav-link:hover {
  border: blueviolet;
}

.nav-link.active {
  color: rgb(94, 9, 175);
  font-weight: 600;
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #8a2be2;
  border-radius: 2px;
}

.auth-link {
  color: blueviolet;
  padding: 5px 12px;
  border-radius: 20px;
  transition: all 0.3s;
}

.auth-link:hover {
  color: white;
  background-color:var(--primary-color);
}

.user-avatar {
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 4px;
  border-radius: 50%;
  transition: all 0.3s;
}

.user-avatar:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* 下拉菜单样式覆盖 */
:deep(.el-dropdown-menu) {
  border: none;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  padding: 8px 0;
}

:deep(.el-dropdown-menu__item) {
  padding: 8px 16px;
  font-size: 0.9rem;
}

:deep(.el-dropdown-menu__item:hover) {
  background-color: rgba(138, 43, 226, 0.1);
  color: var(--primary-color);
}

/* 用户头像增大 */
.user-avatar {
  width: 36px;
  height: 36px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .nav-container {
    gap: 1rem;
  }

  .nav-links {
    display: none; /* 移动端隐藏导航菜单 */
  }

  .auth-container {
    margin-left: auto;
  }
}
/* 新版页脚样式 */
.ins-footer {
  background-color: #f9f9ff; /* 浅紫色背景 */
  padding: 60px 0 30px;
  border-top: 1px solid rgba(138, 43, 226, 0.1);
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.footer-main {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-bottom: 40px;
}

.footer-brand {
  flex: 0 0 300px;
  margin-bottom: 2rem;
}

.brand {
  display: flex;
  align-items: center;
  text-decoration: none;
  margin-bottom: 1rem;
}

.logo-icon {
  color: var(--primary-color);
  font-size: 1.8rem;
  margin-right: 10px;
}

.logo-text {
  color: var(--primary-dark);
  font-size: 1.6rem;
  font-weight: 600;
}

.footer-desc {
  color: var(--text-color);
  opacity: 0.8;
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.social-links {
  display: flex;
  gap: 1rem;
}

.social-icon {
  color: var(--primary-color);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(138, 43, 226, 0.05);
  transition: all 0.3s ease;
}

.social-icon:hover {
  background-color: rgba(138, 43, 226, 0.1);
  transform: translateY(-3px);
}

.footer-links-group {
  display: flex;
  gap: 3rem;
}

.links-column {
  min-width: 150px;
}

.links-title {
  color: var(--primary-dark);
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  position: relative;
  padding-bottom: 8px;
}

.links-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 40px;
  height: 2px;
  background-color: var(--primary-color);
}

.footer-link {
  display: block;
  color: var(--text-color);
  margin-bottom: 12px;
  text-decoration: none;
  transition: all 0.3s ease;
  opacity: 0.8;
}

.footer-link:hover {
  color: var(--primary-color);
  opacity: 1;
  padding-left: 5px;
}

.footer-bottom {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  padding-top: 30px;
  border-top: 1px solid rgba(138, 43, 226, 0.1);
}

.copyright {
  color: var(--text-color);
  opacity: 0.7;
  font-size: 0.9rem;
}

.legal-links {
  display: flex;
  gap: 1.5rem;
}

.legal-link {
  color: var(--text-color);
  text-decoration: none;
  font-size: 0.9rem;
  opacity: 0.7;
  transition: opacity 0.3s;
}

.legal-link:hover {
  opacity: 1;
  color: var(--primary-color);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .footer-main {
    flex-direction: column;
  }

  .footer-links-group {
    flex-wrap: wrap;
    gap: 2rem;
    margin-top: 2rem;
  }

  .footer-bottom {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
  }

  .legal-links {
    justify-content: center;
  }
}


</style>