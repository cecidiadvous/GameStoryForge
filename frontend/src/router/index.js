import { createRouter, createWebHistory } from 'vue-router';
import Login from '../components/Login.vue';
import Register from '../components/Register.vue';
import Home from '../components/Home.vue';
import AuthPage from "@/components/AuthPage.vue";
import Dashboard from "@/components/Dashboard.vue";
import Workshop from "@/components/Workshop.vue";

const routes = [
    { path: '/auth', component: AuthPage },
    {
        path: '/home',
        component: Home,
        meta: { requiresAuth: true }, // 需要身份验证
    },
    {
        path: '/dashboard',
        component: Dashboard, // 新增Dashboard路径
        meta: { requiresAuth: true }, // 需要身份验证
    },

    {
        path: '/workshop/:id', // 动态路由，:id 表示游戏 ID
        meta: { requiresAuth: true },
        component: Workshop,
    },
    { path: '/', redirect: '/auth' },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 设置导航守卫
router.beforeEach((to, from, next) => {
    const loggedIn = localStorage.getItem('user'); // 假设用户信息存储在 localStorage 中

    if (to.matched.some(record => record.meta.requiresAuth) && !loggedIn) {
        // 如果目标路由需要身份验证而用户未登录，则重定向到登录页面
        next('/login');
    } else {
        next(); // 否则继续导航
    }
});

export default router;


