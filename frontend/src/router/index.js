import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../views/Layout.vue'
import Login from '../views/Login.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'login',
            component: Login
        },
        {
            path: '/',
            component: Layout,
            children: [
                {
                    path: '',
                    name: 'dashboard',
                    component: () => import('../views/Dashboard.vue')
                },
                {
                    path: 'devices',
                    name: 'devices',
                    component: () => import('../views/Devices.vue')
                },
                {
                    path: 'notices',
                    name: 'notices',
                    component: () => import('../views/Notices.vue')
                },
                {
                    path: 'logs',
                    name: 'logs',
                    component: () => import('../views/Logs.vue')
                },
                {
                    path: 'settings',
                    name: 'settings',
                    component: () => import('../views/Settings.vue')
                }
            ]
        }
    ]
})

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token')
    if (to.name !== 'login' && !token) {
        next({ name: 'login' })
    } else if (to.name === 'login' && token) {
        next({ name: 'dashboard' })
    } else {
        next()
    }
})

export default router
