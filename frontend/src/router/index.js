import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import LoginView from '@/views/LoginView.vue';
import ProfileView from '@/views/ProfileView.vue';
import { useAuthStore } from '@/stores/auth';
import RegisterView from "@/views/RegisterView.vue";

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: {}
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
  },
  {
    path: '/register',
    name: 'register',
    component:RegisterView,
  },
  {
    path: '/profile',
    name: 'profile',
    component: ProfileView,
    meta: { requiresAuth: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      return { top: 0 };
    }
  },
});

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  const isAuthenticated = !!authStore.user?.token;
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login?redirect=' + encodeURIComponent(to.fullPath));
  } else {
    next();
  }
});

router.onError((error) => {
  console.error('Router error:', error);
  if (error.message.includes('parentNode')) {
    // 特定错误处理
    console.warn('DOM操作在组件卸载后发生');
  }
});

export default router;