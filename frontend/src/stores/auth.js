import { defineStore } from 'pinia'
import { login, getUserInfo } from '../api/auth'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('token') || '',
        userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}')
    }),
    getters: {
        isLoggedIn: (state) => !!state.token
    },
    actions: {
        async login(loginForm) {
            try {
                const res = await login(loginForm)
                // Assuming res.data contains token or res is the data object
                // Based on original code, res is the response body.
                // We need to see the actual response structure.
                // Assuming standard structure: { code: "000000", data: { token: "..." } }
                const token = res.data.token
                this.token = token
                localStorage.setItem('token', token)

                // Get user info
                await this.getUserInfo()
                return true
            } catch (error) {
                console.error('Login failed:', error)
                throw error
            }
        },
        async getUserInfo() {
            try {
                const res = await getUserInfo()
                this.userInfo = res.data
                localStorage.setItem('userInfo', JSON.stringify(res.data))
            } catch (error) {
                console.error('Get user info failed:', error)
            }
        },
        logout() {
            this.token = ''
            this.userInfo = {}
            localStorage.removeItem('token')
            localStorage.removeItem('userInfo')
            // Router push to login handled in component or interceptor
        },
        setToken(token) {
            this.token = token
            localStorage.setItem('token', token)
        },
        setUserInfo(userInfo) {
            this.userInfo = userInfo
            localStorage.setItem('userInfo', JSON.stringify(userInfo))
        }
    }
})
