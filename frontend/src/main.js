import router from "@/router";
import { createApp } from 'vue'
import App from './App.vue'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import store from './store';

const app = createApp(App);
function checkIfUserIsAuthenticated() {
    return store.getters.isAuthenticated;
}


// 导航守卫
router.beforeEach((to, from, next) => {
    const isAuthenticated = checkIfUserIsAuthenticated(); // 自定义的检查用户是否已登录的函数

    if (to.matched.some(record => record.meta.requiresAuth)) {
        // 如果路由需要登录验证
        if (!isAuthenticated) {
            // 如果用户未登录，重定向到登录页面
            next('/login');
        } else {
            // 如果用户已登录，允许访问
            next();
        }
    } else {
        // 如果路由不需要登录验证，允许访问
        next();
    }
});
app.use(Antd)
app.use(router)
app.use(store)
app.mount('#app');
