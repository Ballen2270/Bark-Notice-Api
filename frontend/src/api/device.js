import request from './request'

export function getAllDevices() {
    return request({
        url: '/device/queryAll',
        method: 'get'
    })
}

export function getDevice(deviceToken) {
    return request({
        url: '/device/query',
        method: 'get',
        params: { deviceToken }
    })
}

export function saveDevice(data) {
    return request({
        url: '/device/gen',
        method: 'post',
        data
    })
}

export function activateDevice(deviceToken) {
    return request({
        url: '/device/active',
        method: 'get',
        params: { deviceToken }
    })
}

export function deactivateDevice(deviceToken) {
    return request({
        url: '/device/stop',
        method: 'get',
        params: { deviceToken }
    })
}
