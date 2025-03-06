import request from './request'

// 查询所有设备
export function getAllDevices() {
  return request({
    url: '/device/queryAll',
    method: 'get'
  })
}

// 查询单个设备
export function getDevice(deviceToken) {
  return request({
    url: '/device/query',
    method: 'get',
    params: { deviceToken }
  })
}

// 添加/修改设备
export function saveDevice(data) {
  return request({
    url: '/device/gen',
    method: 'post',
    data
  })
}

// 激活设备
export function activateDevice(deviceToken) {
  return request({
    url: '/device/active',
    method: 'get',
    params: { deviceToken }
  })
}

// 停用设备
export function deactivateDevice(deviceToken) {
  return request({
    url: '/device/stop',
    method: 'get',
    params: { deviceToken }
  })
} 