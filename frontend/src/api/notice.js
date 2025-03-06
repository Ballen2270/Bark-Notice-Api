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
    method: 'get'
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