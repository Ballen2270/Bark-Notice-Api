import axios from 'axios'

const service = axios.create({
    baseURL: '/api',
    timeout: 10000
})

// Request interceptor
service.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`
        }
        return config
    },
    error => {
        console.error(error)
        return Promise.reject(error)
    }
)

// Response interceptor
service.interceptors.response.use(
    response => {
        const res = response.data

        // Check custom code if applicable, or just return data
        // The original code checks for res.code !== "000000"
        if (res.code && res.code !== "000000" && res.code !== 200) {
            // Handle errors
            if (res.code === '401' || res.code === '403') {
                localStorage.removeItem('token')
                localStorage.removeItem('userInfo')
                window.location.href = '/login'
            }
            // 返回包含错误码和错误消息的完整错误对象
            return Promise.reject({
                code: res.code,
                msg: res.msg || 'Error',
                message: res.msg || 'Error' // 保持向后兼容
            })
        } else {
            return res
        }
    },
    error => {
        console.error('Request error:', error)
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            localStorage.removeItem('token')
            localStorage.removeItem('userInfo')
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default service
