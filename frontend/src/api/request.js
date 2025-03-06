import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 添加token到请求头
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

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 根据BasicResponse中定义的SUCCESS_CODE="000000"来判断请求是否成功
    if (res.code && res.code !== "000000") {
      ElMessage({
        message: res.msg || '请求错误',
        type: 'error',
        duration: 5 * 1000
      })
      
      // 401: 未登录或token过期
      if (res.code === '401' || res.code === '403') {
        // 重定向到登录页
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        setTimeout(() => {
          window.location.href = '/login'
        }, 1500)
      }
      
      return Promise.reject(new Error(res.msg || '请求错误'))
    } else {
      // 请求成功，返回数据
      return res
    }
  },
  error => {
    console.error('请求错误:', error)
    // 更详细的错误信息展示，注意使用msg字段
    const errorMsg = error.response?.data?.msg || error.message || '网络错误'
    
    // 401: 未登录或token过期
    if (error.response && (error.response.status === 401 || error.response.status === 403)) {
      ElMessage({
        message: '登录已过期，请重新登录',
        type: 'error',
        duration: 5 * 1000
      })
      
      // 清除用户信息和token
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      
      // 重定向到登录页
      setTimeout(() => {
        window.location.href = '/login'
      }, 1500)
    } else {
      ElMessage({
        message: errorMsg,
        type: 'error',
        duration: 5 * 1000
      })
    }
    
    return Promise.reject(error)
  }
)

export default service 