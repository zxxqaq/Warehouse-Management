import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import Login from '../views/Login.vue'
import Home from "@/views/Home.vue";

const routes = [
    {
        path: '/',
        redirect: '/login',
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/home',
        name: 'Home',
        component: Home,
        meta: {
            requiresAuth: true,
        }
    }
    // 其他路由配置
]
const router = createRouter({
    history: createWebHashHistory(),
    routes,
})


export default router
