import request from './request'

// 发送通知 (GET方式)
export function sendNoticeGet(title, body, group) {
  return request({
    url: '/notice',
    method: 'get',
    params: { title, body, group }
  })
}

// 发送通知 (POST方式)
export function sendNoticePost(data) {
  return request({
    url: '/notice',
    method: 'post',
    data
  })
}

// Ping服务
export function pingService() {
  return request({
    url: '/ping',
    method: 'get',
    transformResponse: [(data) => {
      try {
        const response = JSON.parse(data)
        // 专门处理ping接口的响应
        if (response.code === 200 && response.message === 'pong') {
          return {
            success: true,
            data: response,
            message: response.message
          }
        }
        return data
      } catch (e) {
        return data
      }
    }]
  })
}

// 注册设备
export function registerDevice(devicetoken, key) {
  return request({
    url: '/register',
    method: 'get',
    params: { devicetoken, key }
  })
} 