import request from './request'

export function getUserList() {
    return request({
        url: '/user/list',
        method: 'get'
    })
}

export function activateUser(userId) {
    return request({
        url: '/user/activate',
        method: 'post',
        data: { userId }
    })
}

export function deactivateUser(userId) {
    return request({
        url: '/user/deactivate',
        method: 'post',
        data: { userId }
    })
}

export function registerUser(data) {
    return request({
        url: '/auth/register',
        method: 'post',
        data
    })
}
