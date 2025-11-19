import request from './request'

export function getLogList(params) {
    const { deviceKey, ...otherParams } = params || {}
    return request({
        url: '/log/list',
        method: 'get',
        params: otherParams
    })
}

export function countByDate(params) {
    return request({
        url: '/log/count/date',
        method: 'get',
        params
    })
}

export function countByStatus(params) {
    return request({
        url: '/log/count/status',
        method: 'get',
        params
    })
}

export function countByGroup(params) {
    return request({
        url: '/log/count/group',
        method: 'get',
        params
    })
}

export function countByDevice(params) {
    return request({
        url: '/log/count/device',
        method: 'get',
        params
    })
}
